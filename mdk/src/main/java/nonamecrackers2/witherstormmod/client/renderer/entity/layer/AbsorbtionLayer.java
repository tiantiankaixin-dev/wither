package nonamecrackers2.witherstormmod.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import nonamecrackers2.witherstormmod.client.rendertype.UtilRenderTypes;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class AbsorbtionLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/misc/absorbtion.png");
   private static final RenderType RENDER_TYPE = UtilRenderTypes.entityDecalTranslucent(TEXTURE);

   public AbsorbtionLayer(RenderLayerParent<T, M> parent) {
      super(parent);
   }

   public void render(
      PoseStack stack,
      MultiBufferSource buffer,
      int p_117351_,
      T entity,
      float p_117353_,
      float p_117354_,
      float p_117355_,
      float p_117356_,
      float p_117357_,
      float p_117358_
   ) {
      if (!entity.isInvisible()) {
         WitherStormEntity storm = (WitherStormEntity)entity.level()
            .getNearestEntity(
               WitherStormEntity.class,
               TargetingConditions.forNonCombat(),
               null,
               entity.getX(),
               entity.getY(),
               entity.getZ(),
               entity.getBoundingBox().inflate(50.0)
            );
         if (storm != null && storm.getPhase() > 3 && !storm.isDeadOrPlayingDead()) {
            float alpha = Mth.clamp((30.0F - (float)storm.getEyePosition().distanceTo(entity.getEyePosition())) * 0.1F, 0.0F, 0.9F);
            if (alpha > 0.0F) {
               this.renderOverlay(stack, buffer, p_117351_, entity, alpha);
            }
         }
      }
   }

   private void renderOverlay(PoseStack stack, MultiBufferSource buffer, int packedLight, T entity, float alpha) {
      this.getParentModel().renderToBuffer(stack, buffer.getBuffer(RENDER_TYPE), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), ((int)(alpha * 255.0F) & 0xFF) << 24 | 0xFFFFFF);
   }
}
