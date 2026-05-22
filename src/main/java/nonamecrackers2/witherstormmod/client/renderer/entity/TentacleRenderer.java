package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.TentacleModel;
import nonamecrackers2.witherstormmod.common.entity.TentacleEntity;

public class TentacleRenderer extends EntityRenderer<TentacleEntity> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/tentacle/tentacle.png");
   private final TentacleModel model;

   public TentacleRenderer(Context context) {
      super(context);
      this.model = new TentacleModel(context.bakeLayer(WitherStormModRenderers.TENTACLE));
      this.shadowRadius = 0.5F;
   }

   public void render(TentacleEntity entity, float p_225623_2_, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
      super.render(entity, p_225623_2_, partialTicks, stack, buffer, packedLight);
      stack.pushPose();
      stack.scale(-1.0F, -1.0F, 1.0F);
      stack.scale(2.0F, 2.0F, 2.0F);
      VertexConsumer builder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
      this.model.setupAnim(entity, partialTicks, 0.0F, 0.0F, entity.getYRot(), entity.getXRot());
      int i = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
      this.model.renderToBuffer(stack, builder, packedLight, i, 1.0F, 1.0F, 1.0F, 1.0F);
      stack.popPose();
   }

   public ResourceLocation getTextureLocation(TentacleEntity entity) {
      return TEXTURE;
   }
}
