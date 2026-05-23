package nonamecrackers2.witherstormmod.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import nonamecrackers2.witherstormmod.client.renderer.entity.WitherStormHeadRenderer;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.WitherStormHeadModel;
import nonamecrackers2.witherstormmod.common.entity.WitherStormHeadEntity;

public class WitherStormHeadEyesLayer extends EyesLayer<WitherStormHeadEntity, WitherStormHeadModel> {
   private static final RenderType EYES = RenderType.eyes(WitherStormHeadRenderer.EMISSIVE);
   private static final RenderType EYES_HURT = RenderType.eyes(WitherStormHeadRenderer.EMISSIVE_HURT);
   private static final RenderType ENTITY_CUTOUT = RenderType.entityCutout(WitherStormHeadRenderer.EMISSIVE);

   public WitherStormHeadEyesLayer(RenderLayerParent<WitherStormHeadEntity, WitherStormHeadModel> renderer) {
      super(renderer);
   }

   public void render(
      PoseStack stack,
      MultiBufferSource buffer,
      int packedLight,
      WitherStormHeadEntity entity,
      float p_225628_5_,
      float p_225628_6_,
      float p_225628_7_,
      float p_225628_8_,
      float p_225628_9_,
      float p_225628_10_
   ) {
      VertexConsumer builder = null;
      if (entity.isPlayingDead()) {
         builder = buffer.getBuffer(ENTITY_CUTOUT);
      } else if (entity.isHurt()) {
         builder = buffer.getBuffer(EYES_HURT);
      } else {
         builder = buffer.getBuffer(EYES);
      }

      ((WitherStormHeadModel)this.getParentModel()).renderToBuffer(stack, builder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
   }

   public RenderType renderType() {
      return EYES;
   }
}
