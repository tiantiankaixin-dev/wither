package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.FlamingWitherSkullModel;
import nonamecrackers2.witherstormmod.client.rendertype.UtilRenderTypes;
import nonamecrackers2.witherstormmod.common.entity.FlamingWitherSkullEntity;

public class FlamingWitherSkullRenderer<T extends FlamingWitherSkullEntity> extends EntityRenderer<T> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/flaming_wither_skull/flaming_wither_skull.png");
   private static final ResourceLocation EMISSIVE = new ResourceLocation(
      "witherstormmod", "textures/entity/flaming_wither_skull/flaming_wither_skull_emissive.png"
   );
   private final FlamingWitherSkullModel model;

   public FlamingWitherSkullRenderer(Context context) {
      super(context);
      this.model = new FlamingWitherSkullModel(context.bakeLayer(WitherStormModRenderers.FLAMING_WITHER_SKULL));
   }

   protected int getBlockLightLevel(FlamingWitherSkullEntity p_225624_1_, BlockPos p_225624_2_) {
      return 15;
   }

   public void render(T entity, float p_225623_2_, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
      stack.pushPose();
      stack.scale(-1.0F, -1.0F, 1.0F);
      stack.scale(1.8F, 1.8F, 1.8F);
      float f = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
      float f1 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
      VertexConsumer builder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
      this.model.setupAnim(partialTicks, f, f1);
      this.model.renderToBuffer(stack, builder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      VertexConsumer emissive = buffer.getBuffer(UtilRenderTypes.emissiveNoCull(this.getEmissiveTextureLocation(entity)));
      this.model.renderToBuffer(stack, emissive, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      stack.popPose();
      super.render(entity, p_225623_2_, partialTicks, stack, buffer, packedLight);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }

   public ResourceLocation getEmissiveTextureLocation(T entity) {
      return EMISSIVE;
   }
}
