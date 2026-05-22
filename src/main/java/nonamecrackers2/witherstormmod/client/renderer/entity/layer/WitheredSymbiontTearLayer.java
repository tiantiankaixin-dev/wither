package nonamecrackers2.witherstormmod.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.WitheredSymbiontModel;
import nonamecrackers2.witherstormmod.client.rendertype.UtilRenderTypes;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;

public class WitheredSymbiontTearLayer extends RenderLayer<WitheredSymbiontEntity, WitheredSymbiontModel<WitheredSymbiontEntity>> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/withered_symbiont/withered_symbiont_tear.png");

   public WitheredSymbiontTearLayer(RenderLayerParent<WitheredSymbiontEntity, WitheredSymbiontModel<WitheredSymbiontEntity>> parent) {
      super(parent);
   }

   public void render(
      PoseStack stack,
      MultiBufferSource buffer,
      int p_117351_,
      WitheredSymbiontEntity entity,
      float p_117353_,
      float p_117354_,
      float partialTicks,
      float p_117356_,
      float p_117357_,
      float p_117358_
   ) {
      VertexConsumer consumer = buffer.getBuffer(UtilRenderTypes.emissiveTranslucent(TEXTURE));
      ((WitheredSymbiontModel)this.getParentModel())
         .renderToBuffer(stack, consumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, entity.getTearAlpha(partialTicks));
   }
}
