package nonamecrackers2.witherstormmod.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.client.renderer.entity.AbstractWitherStormRenderer;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.AbstractWitherStormModel;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormModRenderEvents {
   @SubscribeEvent
   public static void renderWitherStormPost(RenderLevelStageEvent event) {
      if (event.getStage().equals(Stage.AFTER_PARTICLES)) {
         Minecraft mc = Minecraft.getInstance();
         ClientLevel world = mc.level;
         PoseStack stack = new PoseStack();
         stack.mulPose(event.getPoseStack());
         BufferSource buffer = mc.renderBuffers().bufferSource();
         Vec3 pos = mc.gameRenderer.getMainCamera().getPosition();
         Vec3 negPos = new Vec3(-pos.x, -pos.y, -pos.z);
         float partialTicks = event.getPartialTick();
         EntityRenderDispatcher manager = mc.getEntityRenderDispatcher();
         boolean flag = mc.level.effects().isFoggyAt(Mth.floor(pos.x), Mth.floor(pos.y)) || mc.gui.getBossOverlay().shouldCreateWorldFog();
         FogRenderer.setupColor(mc.gameRenderer.getMainCamera(), partialTicks, world, (Integer)mc.options.renderDistance().get(), mc.gameRenderer.getDarkenWorldAmount(partialTicks));
         FogRenderer.levelFogColor();
         if ((Integer)mc.options.renderDistance().get() >= 4) {
            FogRenderer.setupFog(mc.gameRenderer.getMainCamera(), FogMode.FOG_TERRAIN, Math.max(mc.gameRenderer.getRenderDistance() - 16.0F, 32.0F), flag, partialTicks);
         }

         renderStorms(
            manager,
            world,
            stack,
            partialTicks,
            negPos,
            buffer,
            (s, r) -> {
               if ((Boolean)WitherStormModConfig.CLIENT.renderDebrisRings.get()
                  && (!(Boolean)WitherStormModConfig.CLIENT.hideDebrisRingsUntilSplit.get() || s.getPhase() > 5)) {
                  r.renderDebrisRings(s, stack, buffer, partialTicks, manager.getPackedLightCoords(s, partialTicks));
               }
            }
         );
         buffer.endBatch();
         FogRenderer.setupNoFog();
         renderStorms(manager, world, stack, partialTicks, negPos, buffer, (s, r) -> {
            if ((Boolean)WitherStormModConfig.CLIENT.renderTractorBeams.get() && (!s.onGround() || !s.isPlayingDead())) {
               int packedLight = manager.getPackedLightCoords(s, partialTicks);
               r.prepareHeadAnimsForTractorBeams(s, partialTicks);
               r.renderTractorBeams(s, stack, buffer, partialTicks, packedLight);
            }
         });
         buffer.endBatch();
         renderStorms(manager, world, stack, partialTicks, negPos, buffer, (s, r) -> {
            if ((Boolean)WitherStormModConfig.CLIENT.renderShine.get() && s.shouldShine()) {
               AbstractWitherStormRenderer.renderShine(s, stack, partialTicks, mc.gameRenderer.getMainCamera(), buffer);
            }
         });
         buffer.endBatch();
      }
   }

   private static <T extends WitherStormEntity, M extends AbstractWitherStormModel<T>> void renderStorms(
      EntityRenderDispatcher manager,
      ClientLevel world,
      PoseStack stack,
      float partialTicks,
      Vec3 negPos,
      MultiBufferSource buffer,
      BiConsumer<T, AbstractWitherStormRenderer<T, M>> action
   ) {
      for (Entity entity : world.entitiesForRendering()) {
         if (entity instanceof WitherStormEntity) {
            T storm = (T)entity;
            AbstractWitherStormRenderer<T, M> renderer = AbstractWitherStormRenderer.getRenderer(storm, manager);
            if (renderer != null) {
               stack.pushPose();
               double x = Mth.lerp((double)partialTicks, storm.xOld, storm.getX());
               double y = Mth.lerp((double)partialTicks, storm.yOld, storm.getY());
               double z = Mth.lerp((double)partialTicks, storm.zOld, storm.getZ());
               stack.translate(negPos.x, negPos.y, negPos.z);
               stack.translate(x, y, z);
               renderer.updateModel(storm);
               action.accept(storm, renderer);
               stack.popPose();
            }
         }
      }
   }
}
