package nonamecrackers2.witherstormmod.client.capability;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.timings.TimeTracker;
import nonamecrackers2.crackerslib.common.compat.CompatHelper;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.client.renderer.blockentity.AbstractSuperBeaconRenderer;
import nonamecrackers2.witherstormmod.client.renderer.entity.AbstractWitherStormRenderer;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.AbstractWitherStormModel;
import nonamecrackers2.witherstormmod.client.util.SuperBeaconDistantInstance;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.mixin.IMixinGameRenderer;
import org.joml.Matrix4f;

public class WitherStormDistantRenderer {
   private final Int2ObjectMap<WitherStormEntity> stormsToRender = new Int2ObjectOpenHashMap();
   private final Minecraft minecraft;
   private final EntityRenderDispatcher manager;
   private final Map<BlockPos, SuperBeaconDistantInstance> superBeacons = Maps.newHashMap();

   public WitherStormDistantRenderer(Minecraft minecraft) {
      this.minecraft = minecraft;
      this.manager = minecraft.getEntityRenderDispatcher();
   }

   public WitherStormDistantRenderer() {
      this.minecraft = null;
      this.manager = null;
   }

   public void tick() {
      ClientLevel world = this.minecraft.level;
      if (world != null) {
         world.getProfiler().push("distantWitherStorms");
         this.stormsToRender.forEach((id, entity) -> {
            if (!entity.isRemoved() && !entity.isPassenger()) {
               this.guardEntityTick(this::tickEntity, entity);
            }
         });
         this.removeAllPendingEntityRemovals();
         world.getProfiler().pop();
      }
   }

   public void tickEntity(WitherStormEntity entity) {
      entity.setOldPosAndRot();
      entity.tickCount++;
      this.minecraft.level.getProfiler().push(() -> ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString());
      if (entity.canUpdate() && entity.level().getEntity(entity.getId()) == null) {
         entity.tick();
      }

      this.minecraft.level.getProfiler().pop();
   }

   public void guardEntityTick(Consumer<WitherStormEntity> consumer, WitherStormEntity entity) {
      try {
         TimeTracker.ENTITY_UPDATE.trackStart(entity);
         consumer.accept(entity);
      } catch (Throwable var9) {
         CrashReport report = CrashReport.forThrowable(var9, "Distant Ticking WitherStormEntity");
         CrashReportCategory category = report.addCategory("Trying to tick distant WitherStormEntity on client");
         entity.fillCrashReportCategory(category);
         throw new ReportedException(report);
      } finally {
         TimeTracker.ENTITY_UPDATE.trackEnd(entity);
      }
   }

   public void removeAllPendingEntityRemovals() {
      ObjectIterator<Entry<WitherStormEntity>> iterator = this.stormsToRender.int2ObjectEntrySet().iterator();

      while (iterator.hasNext()) {
         Entry<WitherStormEntity> entry = (Entry<WitherStormEntity>)iterator.next();
         WitherStormEntity entity = (WitherStormEntity)entry.getValue();
         if (entity.isRemoved()) {
            iterator.remove();
         }
      }
   }

   public void renderTick(PoseStack stack, BufferSource buffer, float partialTicks, Frustum clippinghelper) {
      if (this.minecraft.level != null) {
         GameRenderer renderer = this.minecraft.gameRenderer;
         Camera camera = renderer.getMainCamera();
         boolean distantFog = (Boolean)WitherStormModConfig.CLIENT.distantFog.get();
         Vec3 pos = camera.getPosition();
         if (distantFog) {
            boolean flag = this.minecraft.level.effects().isFoggyAt(Mth.floor(pos.x), Mth.floor(pos.y))
               || this.minecraft.gui.getBossOverlay().shouldCreateWorldFog();
            float f = renderer.getRenderDistance();
            FogRenderer.setupColor(camera, partialTicks, this.minecraft.level, (Integer)this.minecraft.options.renderDistance().get(), 1.0F);
            FogRenderer.levelFogColor();
            FogRenderer.setupFog(camera, FogMode.FOG_TERRAIN, Math.max(f - 16.0F, 32.0F), flag, partialTicks);
         }

         this.minecraft.level.getProfiler().popPush("distant_wither_storms");
         ObjectIterator var20 = this.stormsToRender.int2ObjectEntrySet().iterator();

         while (var20.hasNext()) {
            Entry<WitherStormEntity> entry = (Entry<WitherStormEntity>)var20.next();
            WitherStormEntity entity = (WitherStormEntity)entry.getValue();
            if (this.manager.shouldRender(entity, clippinghelper, pos.x, pos.y, pos.z)
               && this.minecraft.level.getEntity(entity.getId()) == null) {
               float f1 = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
               double x = Mth.lerp((double)partialTicks, entity.xOld, entity.getX());
               double y = Mth.lerp((double)partialTicks, entity.yOld, entity.getY());
               double z = Mth.lerp((double)partialTicks, entity.zOld, entity.getZ());
               int packedLight = this.manager.getPackedLightCoords(entity, partialTicks);
               this.manager.render(entity, x - pos.x, y - pos.y, z - pos.z, f1, partialTicks, stack, buffer, packedLight);
            }
         }

         this.renderStorms(
            clippinghelper,
            pos,
            partialTicks,
            stack,
            buffer,
            (s, r) -> {
               if ((Boolean)WitherStormModConfig.CLIENT.renderDebrisRings.get()
                  && (!(Boolean)WitherStormModConfig.CLIENT.hideDebrisRingsUntilSplit.get() || s.getPhase() > 5)) {
                  r.renderDebrisRings(s, stack, buffer, partialTicks, this.manager.getPackedLightCoords(s, partialTicks));
               }
            }
         );
         buffer.endBatch();
         if (distantFog) {
            FogRenderer.setupNoFog();
         }

         this.renderStorms(clippinghelper, pos, partialTicks, stack, buffer, (s, r) -> {
            if ((Boolean)WitherStormModConfig.CLIENT.renderTractorBeams.get() && (!s.onGround() || !s.isPlayingDead())) {
               int packedLightx = this.manager.getPackedLightCoords(s, partialTicks);
               r.prepareHeadAnimsForTractorBeams(s, partialTicks);
               r.renderTractorBeams(s, stack, buffer, partialTicks, packedLightx);
            }
         });
         this.renderStorms(clippinghelper, pos, partialTicks, stack, buffer, (s, r) -> {
            if ((Boolean)WitherStormModConfig.CLIENT.renderShine.get() && s.shouldShine()) {
               AbstractWitherStormRenderer.renderShine(s, stack, partialTicks, this.minecraft.gameRenderer.getMainCamera(), buffer);
            }
         });

         for (java.util.Map.Entry<BlockPos, SuperBeaconDistantInstance> entry : this.superBeacons.entrySet()) {
            SuperBeaconDistantInstance instance = entry.getValue();
            stack.pushPose();
            stack.translate(
               (double)instance.pos.getX() - pos.x,
               (double)instance.pos.getY() - pos.y,
               (double)instance.pos.getZ() - pos.z
            );
            float camDist = (float)(
               pos.distanceTo(Vec3.atCenterOf(instance.pos)) - (double)((float)((Integer)this.minecraft.options.renderDistance().get()).intValue() * 16.0F)
            );
            float widthMul = (float)Math.max(1.0, (double)camDist * 0.01);
            AbstractSuperBeaconRenderer.renderBeam(
               instance.active,
               stack,
               instance.color,
               buffer,
               camDist > 0.0F ? 0.0F : partialTicks,
               camDist > 0.0F ? 0L : this.minecraft.level.getGameTime(),
               instance.beaconHeight,
               instance.beamWidth * widthMul,
               instance.outerBeamWidth * widthMul
            );
            stack.popPose();
         }
      }
   }

   private <T extends WitherStormEntity, M extends AbstractWitherStormModel<T>> void renderStorms(
      Frustum frustum, Vec3 cameraPos, float partialTicks, PoseStack stack, MultiBufferSource buffer, BiConsumer<T, AbstractWitherStormRenderer<T, M>> action
   ) {
      ObjectIterator var7 = this.stormsToRender.int2ObjectEntrySet().iterator();

      while (var7.hasNext()) {
         Entry<WitherStormEntity> entry = (Entry<WitherStormEntity>)var7.next();
         T entity = (T)entry.getValue();
         if (this.manager.shouldRender(entity, frustum, cameraPos.x, cameraPos.y, cameraPos.z)
            && this.minecraft.level.getEntity(entity.getId()) == null) {
            AbstractWitherStormRenderer<T, M> renderer = AbstractWitherStormRenderer.getRenderer(entity, this.manager);
            if (renderer != null) {
               double x = Mth.lerp((double)partialTicks, entity.xOld, entity.getX());
               double y = Mth.lerp((double)partialTicks, entity.yOld, entity.getY());
               double z = Mth.lerp((double)partialTicks, entity.zOld, entity.getZ());
               stack.pushPose();
               stack.translate(x - cameraPos.x, y - cameraPos.y, z - cameraPos.z);
               renderer.updateModel(entity);
               action.accept(entity, renderer);
               stack.popPose();
            }
         }
      }
   }

   public void addWitherStorm(int id, WitherStormEntity entity) {
      this.stormsToRender.put(id, entity);
   }

   public WitherStormEntity get(int id) {
      return (WitherStormEntity)this.stormsToRender.get(id);
   }

   public boolean contains(int id) {
      return this.stormsToRender.containsKey(id);
   }

   public Iterable<WitherStormEntity> getKnown() {
      return Iterables.unmodifiableIterable(this.stormsToRender.values());
   }

   public void addAndOrUpdateSuperBeacon(BlockPos pos, int[] color, boolean active, int beaconHeight, float beamWidth, float outerBeamWidth) {
      SuperBeaconDistantInstance instance = this.superBeacons.computeIfAbsent(pos, p -> new SuperBeaconDistantInstance(p, color));
      instance.color = color;
      instance.active = active;
      instance.beaconHeight = beaconHeight;
      instance.beamWidth = beamWidth;
      instance.outerBeamWidth = outerBeamWidth;
   }

   public void removeSuperBeacon(BlockPos pos) {
      this.superBeacons.remove(pos);
   }

   public static List<WitherStormEntity> getAllStorms(ClientLevel level) {
      List<WitherStormEntity> storms = Lists.newArrayList();
      level.entitiesForRendering().forEach(entity -> {
         if (entity instanceof WitherStormEntity storm) {
            storms.add(storm);
         }
      });
      level.getCapability(WitherStormModClientCapabilities.DISTANT_RENDERER).ifPresent(renderer -> {
         List<Integer> ids = storms.stream().collect(Collectors.mapping(Entity::getId, Collectors.toList()));
         renderer.getKnown().forEach(storm -> {
            if (!ids.contains(storm.getId())) {
               storms.add(storm);
            }
         });
      });
      return storms;
   }

   private static void setClipPlanes(Matrix4f mat, float near, float far) {
      mat.set(2, 2, -((far + near) / (far - near))).set(3, 2, -(2.0F * far * near / (far - near)));
   }

   public static class Events {
      @SubscribeEvent
      public static void renderTickDistantRenderer(RenderLevelStageEvent event) {
         if (event.getStage().equals(Stage.AFTER_PARTICLES)) {
            PoseStack stack = new PoseStack();
            stack.mulPose(event.getPoseStack());
            render(stack, event.getPartialTick());
         }
      }

      @SubscribeEvent
      public static void clientTickDistantRenderer(ClientTickEvent event) {
         Minecraft mc = Minecraft.getInstance();
         if (event.phase == Phase.START) {
            ClientLevel world = mc.level;
            if (world != null) {
               world.getCapability(WitherStormModClientCapabilities.DISTANT_RENDERER).ifPresent(distantRenderer -> {
                  if ((!mc.isPaused() || !mc.hasSingleplayerServer()) && (Boolean)WitherStormModConfig.CLIENT.distantRenderer.get()) {
                     distantRenderer.tick();
                  }
               });
            }
         }
      }

      public static void render(PoseStack stack, float partialTicks) {
         if ((Boolean)WitherStormModConfig.CLIENT.distantRenderer.get()) {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel world = mc.level;
            world.getCapability(WitherStormModClientCapabilities.DISTANT_RENDERER)
               .ifPresent(
                  distantRenderer -> {
                     Matrix4f originalMatrix = RenderSystem.getProjectionMatrix();
                     GameRenderer renderer = mc.gameRenderer;
                     Camera renderInfo = renderer.getMainCamera();
                     Matrix4f projection = null;
                     if (!CompatHelper.isVrActive()) {
                        double fov = ((IMixinGameRenderer)renderer).callGetFov(renderInfo, partialTicks, true);
                        projection = new Matrix4f()
                           .perspective(
                              (float)(fov * (float) (Math.PI / 180.0)),
                              (float)mc.getWindow().getWidth() / (float)mc.getWindow().getHeight(),
                              0.05F,
                              renderer.getRenderDistance() * 180.0F
                           );
                        Matrix4f defaultProjection = renderer.getProjectionMatrix(fov);
                        Matrix4f invertedDefaultProjection = new Matrix4f(defaultProjection);
                        invertedDefaultProjection.invert();
                        Matrix4f distortionMatrix = new Matrix4f(invertedDefaultProjection);
                        distortionMatrix.mul(originalMatrix);
                        projection.mul(distortionMatrix);
                     } else {
                        projection = new Matrix4f(originalMatrix);
                        WitherStormDistantRenderer.setClipPlanes(projection, 0.05F, renderer.getRenderDistance() * 180.0F);
                     }

                     renderer.resetProjectionMatrix(projection);
                     Vec3 pos = renderInfo.getPosition();
                     BufferSource buffer = mc.renderBuffers().bufferSource();
                     Frustum clippinghelper = new Frustum(stack.last().pose(), projection);
                     clippinghelper.prepare(pos.x(), pos.y(), pos.z());
                     distantRenderer.renderTick(stack, buffer, partialTicks, clippinghelper);
                     buffer.endBatch();
                     renderer.resetProjectionMatrix(originalMatrix);
                  }
               );
         }
      }
   }
}
