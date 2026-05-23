package nonamecrackers2.witherstormmod.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.VillagerHeadModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;
import nonamecrackers2.crackerslib.common.compat.CompatHelper;
import nonamecrackers2.witherstormmod.client.rendertype.UtilRenderTypes;
import nonamecrackers2.witherstormmod.client.util.TiledTextureGenerator;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;

public class WitherSicknessLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   private static final ResourceLocation WITHER_SICKNESS_LAYER_64 = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/wither_sickness_layer/wither_sickness_layer.png");
   private final M model;

   public WitherSicknessLayer(RenderLayerParent<T, M> renderer) {
      super(renderer);
      this.model = (M)renderer.getModel();
   }

   public void render(
      PoseStack stack,
      MultiBufferSource buffer,
      int packedLight,
      T entity,
      float p_225628_5_,
      float p_225628_6_,
      float partialTicks,
      float p_225628_8_,
      float p_225628_9_,
      float p_225628_10_
   ) {
      ResourceLocation location = WITHER_SICKNESS_LAYER_64;
      entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER)
         .ifPresent(
            tracker -> {
               if (!tracker.isActuallyImmune() && (tracker.isInfected() || tracker.isBeingCured()) && !entity.isInvisible()) {
                  M model = this.getModel();
                  if (model instanceof VillagerHeadModel m) {
                     m.hatVisible(false);
                  }

                  VertexConsumer consumer;
                  if (CompatHelper.isSodiumLoaded() && !ModList.get().isLoaded("embeddium")) {
                     consumer = buffer.getBuffer(UtilRenderTypes.entityDecalTranslucent(location));
                  } else {
                     consumer = new TiledTextureGenerator(buffer.getBuffer(UtilRenderTypes.entityDecalTranslucent(location)), stack, 0.25F);
                  }

                  float healthRatio = 0.8F - entity.getHealth() / entity.getMaxHealth();
                  float alpha = (float)tracker.getDelayTicks()
                        / (float)tracker.getApplicationDelay()
                        * 0.5F
                        * (Mth.cos(((float)entity.tickCount + partialTicks) * healthRatio) + 2.0F)
                        * 0.25F
                     + 0.2F;
                  if (tracker.isBeingCured()) {
                     alpha = ((float)tracker.getCureDelay() - (float)tracker.getCureDelayTicks()) / (float)tracker.getCureDelay() * 0.5F * alpha * 2.0F;
                  }

                  this.getModel().renderToBuffer(stack, consumer, packedLight, OverlayTexture.NO_OVERLAY, ((int)(alpha * 255.0F) & 0xFF) << 24 | 0xFFFFFF);
                  if (model instanceof VillagerHeadModel m) {
                     m.hatVisible(true);
                  }
               }
            }
         );
   }

   public M getModel() {
      return this.model;
   }
}
