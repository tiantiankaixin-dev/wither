package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.PhantomModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedPhantom;

public class SickenedPhantomRenderer extends MobRenderer<SickenedPhantom, PhantomModel<SickenedPhantom>> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_phantom.png");
   private static final ResourceLocation TEXTURE_EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_phantom_emissive.png");

   public SickenedPhantomRenderer(Context context) {
      super(context, new PhantomModel(context.bakeLayer(WitherStormModRenderers.SICKENED_PHANTOM)), 0.75F);
      this.addLayer(new EyesLayer<SickenedPhantom, PhantomModel<SickenedPhantom>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedPhantomRenderer.TEXTURE_EMISSIVE);
         }
      });
   }

   public ResourceLocation getTextureLocation(SickenedPhantom phantom) {
      return TEXTURE;
   }

   protected void scale(SickenedPhantom phantom, PoseStack stack, float partialTicks) {
      int i = phantom.getPhantomSize();
      float f = 1.0F + 0.15F * (float)i;
      stack.scale(f, f, f);
      stack.translate(0.0F, 1.3125F, 0.1875F);
   }

   protected void setupRotations(SickenedPhantom phantom, PoseStack stack, float p_115687_, float p_115688_, float p_115689_) {
      super.setupRotations(phantom, stack, p_115687_, p_115688_, p_115689_);
      stack.mulPose(Axis.XP.rotationDegrees(phantom.getXRot()));
   }
}
