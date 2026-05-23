package nonamecrackers2.witherstormmod.client.instancing;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.ClientTickEvent;
import // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.Phase;
import // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.RenderTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.crackerslib.common.compat.CompatHelper;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.mixin.IMixinLightTexture;
import nonamecrackers2.witherstormmod.mixin.IMixinOverlayTexture;
import nonamecrackers2.witherstormmod.mixin.MixinRenderSystemAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class RenderBufferer {
   public static final RenderBufferer INSTANCE = new RenderBufferer(Minecraft.getInstance());
   private static final Logger LOGGER = LogManager.getLogger("witherstormmod/RenderBufferer");
   private final Minecraft mc;
   private final Map<Object, BufferedInstance> instances = Maps.newHashMap();
   private final ExecutorService asyncBufferBuilderPool = new ThreadPoolExecutor(0, 3, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
   private boolean noFog;
   private boolean tempDisabled;
   private boolean useAsyncBuilder;
   private boolean flipFaces;
   private boolean cullFaces;

   private RenderBufferer(Minecraft mc) {
      this.mc = mc;
   }

   public int getTotalInstances() {
      return this.instances.size();
   }

   public void tick() {
   }

   public void renderTick() {
      Iterator<BufferedInstance> iterator = this.instances.values().iterator();

      while (iterator.hasNext()) {
         BufferedInstance instance = iterator.next();
         if (instance.getBuffer() != null && (!instance.hasRemoveSupplier() || instance.shouldRemove()) && !instance.wasRenderedLastFrame()) {
            instance.close();
            iterator.remove();
         }

         instance.setRenderedLastFrame(false);
      }
   }

   private void render(
      Object key,
      RenderType type,
      @Nullable Supplier<Boolean> shouldRemove,
      BufferedInstance.Bufferable bufferer,
      PoseStack stack,
      int packedLight,
      int overlayTexture,
      float r,
      float g,
      float b,
      float a,
      boolean renderBuffered
   ) {
      if (renderBuffered && !this.tempDisabled) {
         BufferedInstance instance = this.instances
            .computeIfAbsent(
               key,
               e -> (BufferedInstance)(this.useAsyncBuilder
                     ? new AsyncBufferedInstance(type, bufferer, shouldRemove)
                     : new BufferedInstance(type, bufferer, shouldRemove))
            );
         if (instance != null) {
            if (instance.requiresComputing()) {
               PoseStack s = new PoseStack();
               s.scale(-1.0F, -1.0F, 1.0F);
               instance.buildBuffer(s, this.asyncBufferBuilderPool);
            }

            if (instance instanceof AsyncBufferedInstance async) {
               async.checkBufferBuilderStatus();
            }

            instance.setBufferer(bufferer);
            VertexBuffer buffer = instance.getBuffer();
            if (buffer != null) {
               buffer.bind();
               stack.pushPose();
               stack.scale(-1.0F, -1.0F, 1.0F);
               RenderType renderType = instance.getRenderType();
               renderType.setupRenderState();
               this.applyOverlays(packedLight, overlayTexture, r, g, b, a);
               float prevStart = RenderSystem.getShaderFogStart();
               if (this.noFog) {
                  RenderSystem.setShaderFogStart(Float.MAX_VALUE);
               }

               FogShape prev = RenderSystem.getShaderFogShape();
               RenderSystem.setShaderFogShape(FogShape.SPHERE);
               if (this.cullFaces) {
                  RenderSystem.enableCull();
               }

               if (this.flipFaces) {
                  GL11.glFrontFace(2304);
               }

               Vector3f[] shaderLights = MixinRenderSystemAccessor.witherstormmod$getShaderLightDirections();
               Vector3f[] storedOriginalShaderLights = Arrays.copyOf(shaderLights, shaderLights.length);

               for (int i = 0; i < shaderLights.length; i++) {
                  shaderLights[i] = shaderLights[i].mul(RenderSystem.getInverseViewRotationMatrix(), new Vector3f());
               }

               buffer.drawWithShader(stack.last().pose(), RenderSystem.getProjectionMatrix(), RenderSystem.getShader());
               instance.setRenderedLastFrame(true);

               for (int i = 0; i < shaderLights.length; i++) {
                  shaderLights[i] = storedOriginalShaderLights[i];
               }

               if (this.cullFaces) {
                  RenderSystem.disableCull();
               }

               if (this.flipFaces) {
                  GL11.glFrontFace(2305);
               }

               this.resetOverlays();
               RenderSystem.setShaderFogShape(prev);
               if (this.noFog) {
                  RenderSystem.setShaderFogStart(prevStart);
               }

               renderType.clearRenderState();
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               stack.popPose();
               VertexBuffer.unbind();
            }
         }
      } else {
         bufferer.bufferInto(stack, Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(type), packedLight, overlayTexture, r, g, b, a);
      }

      this.noFog = false;
      this.tempDisabled = false;
      this.useAsyncBuilder = false;
   }

   private void applyOverlays(int packedLight, int overlayTexture, float r, float g, float b, float a) {
      IMixinLightTexture lightTexture = (IMixinLightTexture)this.mc.gameRenderer.lightTexture();
      IMixinOverlayTexture overlay = (IMixinOverlayTexture)this.mc.gameRenderer.overlayTexture();
      float[] packedLightCol = getRGB(lightTexture.getLightTexture(), (packedLight & 65535) / 16, (packedLight >> 16 & 65535) / 16);
      float[] overlayCol = getRGB(overlay.getTexture(), overlayTexture & 65535, overlayTexture >> 16 & 65535);
      Uniform uniform = RenderSystem.getShader().getUniform("OverlayTextureColor");
      if (uniform != null) {
         uniform.set(overlayCol[0], overlayCol[1], overlayCol[2], overlayCol[3]);
      }

      RenderSystem.setShaderColor(r * packedLightCol[0], g * packedLightCol[1], b * packedLightCol[2], a * packedLightCol[3]);
   }

   private void resetOverlays() {
      Uniform uniform = RenderSystem.getShader().getUniform("OverlayTextureColor");
      if (uniform != null) {
         uniform.set(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   private static float[] getRGB(DynamicTexture texture, int u, int v) {
      int color = texture.getPixels().getPixelRGBA(u, v);
      float r = (float)(color >> 0 & 0xFF) / 255.0F;
      float g = (float)(color >> 8 & 0xFF) / 255.0F;
      float b = (float)(color >> 16 & 0xFF) / 255.0F;
      float a = (float)(color >> 24 & 0xFF) / 255.0F;
      return new float[]{r, g, b, a};
   }

   public static boolean shouldUse() {
      return (Boolean)WitherStormModConfig.CLIENT.vertexBufferRendering.get() && (!CompatHelper.isOptifineLoaded() || !CompatHelper.areShadersRunning());
   }

   public static void buildAndOrRender(
      Object key,
      RenderType type,
      BufferedInstance.Bufferable bufferer,
      PoseStack stack,
      int packedLight,
      int overlayTexture,
      float r,
      float g,
      float b,
      float a,
      boolean renderBuffered
   ) {
      INSTANCE.render(key, type, null, bufferer, stack, packedLight, overlayTexture, r, g, b, a, renderBuffered);
   }

   public static void buildAndOrRender(
      Object key,
      RenderType type,
      Supplier<Boolean> shouldRemove,
      BufferedInstance.Bufferable bufferer,
      PoseStack stack,
      int packedLight,
      int overlayTexture,
      float r,
      float g,
      float b,
      float a,
      boolean renderBuffered
   ) {
      INSTANCE.render(key, type, shouldRemove, bufferer, stack, packedLight, overlayTexture, r, g, b, a, renderBuffered);
   }

   public static void buildAndOrRender(
      Object key,
      RenderType type,
      BufferedInstance.Bufferable bufferer,
      PoseStack stack,
      int packedLight,
      int overlayTexture,
      float r,
      float g,
      float b,
      float a
   ) {
      INSTANCE.render(key, type, null, bufferer, stack, packedLight, overlayTexture, r, g, b, a, shouldUse());
   }

   public static void buildAndOrRender(
      Object key,
      RenderType type,
      Supplier<Boolean> shouldRemove,
      BufferedInstance.Bufferable bufferer,
      PoseStack stack,
      int packedLight,
      int overlayTexture,
      float r,
      float g,
      float b,
      float a
   ) {
      INSTANCE.render(key, type, shouldRemove, bufferer, stack, packedLight, overlayTexture, r, g, b, a, shouldUse());
   }

   public static void pushNoFog() {
      INSTANCE.noFog = true;
   }

   public static void pushTempDisabled() {
      INSTANCE.tempDisabled = true;
   }

   public static void pushUseAsyncBuilder() {
      if ((Boolean)WitherStormModConfig.CLIENT.asyncBufferBuilders.get()) {
         INSTANCE.useAsyncBuilder = true;
      }
   }

   public static void pushFlipFaces() {
      INSTANCE.flipFaces = true;
   }

   public static void popFlipFaces() {
      INSTANCE.flipFaces = false;
   }

   public static void pushCullFaces() {
      INSTANCE.cullFaces = true;
   }

   public static void popCullFaces() {
      INSTANCE.cullFaces = false;
   }

   private void purgeInstances() {
      LOGGER.debug("Clearing {} instances", this.instances.size());
      Iterator<Entry<Object, BufferedInstance>> iterator = this.instances.entrySet().iterator();

      while (iterator.hasNext()) {
         Entry<Object, BufferedInstance> instance = iterator.next();
         instance.getValue().close();
         iterator.remove();
      }
   }

   public void levelReload() {
      LOGGER.debug("Refreshing...");
      this.purgeInstances();
   }

   public void shutdown() {
      LOGGER.debug("Shutting down...");
      this.purgeInstances();
      this.asyncBufferBuilderPool.shutdown();
   }

   public static class Events {
      @SubscribeEvent
      public static void onClientTick(ClientTickEvent event) {
         Minecraft mc = Minecraft.getInstance();
         if (event.phase == Phase.START && !mc.isPaused() && mc.level != null) {
            RenderBufferer.INSTANCE.tick();
         }
      }

      @SubscribeEvent
      public static void onRender(RenderTickEvent event) {
         if (event.phase == Phase.START) {
            RenderBufferer.INSTANCE.renderTick();
         }
      }
   }
}
