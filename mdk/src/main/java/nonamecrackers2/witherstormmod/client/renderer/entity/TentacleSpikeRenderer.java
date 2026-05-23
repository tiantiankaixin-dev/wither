package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.TentacleSpikeModel;
import nonamecrackers2.witherstormmod.common.entity.TentacleSpike;

public class TentacleSpikeRenderer extends EntityRenderer<TentacleSpike> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/tentacle_spike/tentacle_spike.png");
   private static final ResourceLocation EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/tentacle_spike/tentacle_spike_emissive.png");
   private final TentacleSpikeModel<TentacleSpike> model;

   public TentacleSpikeRenderer(Context context) {
      super(context);
      this.model = new TentacleSpikeModel<>(context.bakeLayer(WitherStormModRenderers.TENTACLE_SPIKE));
   }

   public void render(TentacleSpike fang, float p_114486_, float partialTicks, PoseStack stack, MultiBufferSource buffers, int packedLight) {
      float animProg = fang.getAnimationProgress(partialTicks);
      if (animProg != 0.0F) {
         float vertScale = 1.0F;
         float horzScale = 1.0F;
         if (animProg > 0.9F) {
            horzScale *= (1.0F - animProg) / 0.1F;
         }

         if (animProg > 0.9F) {
            vertScale *= (1.0F - animProg) / 0.1F;
         } else if (animProg < 0.08F) {
            vertScale *= animProg / 0.08F;
         }

         stack.pushPose();
         stack.mulPose(Axis.YP.rotationDegrees(90.0F - fang.getYRot()));
         stack.scale(-horzScale, -vertScale, horzScale);
         this.model.setupAnim(fang, animProg, 0.0F, 0.0F, fang.getYRot(), fang.getXRot());
         VertexConsumer consumer = buffers.getBuffer(this.model.renderType(this.getTextureLocation(fang)));
         this.model.renderToBuffer(stack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
         VertexConsumer emissive = buffers.getBuffer(RenderType.eyes(EMISSIVE));
         this.model.renderToBuffer(stack, emissive, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
         stack.popPose();
         super.render(fang, p_114486_, partialTicks, stack, buffers, packedLight);
      }
   }

   public ResourceLocation getTextureLocation(TentacleSpike fang) {
      return TEXTURE;
   }
}
