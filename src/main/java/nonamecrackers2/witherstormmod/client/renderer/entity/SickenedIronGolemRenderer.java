package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.renderer.entity.layer.SickenedIronGolemCrackinessLayer;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.sickenedentity.SickenedIronGolemModel;
import nonamecrackers2.witherstormmod.common.entity.SickenedIronGolem;

public class SickenedIronGolemRenderer extends MobRenderer<SickenedIronGolem, SickenedIronGolemModel<SickenedIronGolem>> {
   public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/sickened/sickened_iron_golem.png");
   public static final ResourceLocation EMISSIVE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/sickened/sickened_iron_golem_emissive.png");

   public SickenedIronGolemRenderer(Context context) {
      super(context, new SickenedIronGolemModel(context.bakeLayer(WitherStormModRenderers.SICKENED_IRON_GOLEM)), 0.7F);
      this.addLayer(new EyesLayer<SickenedIronGolem, SickenedIronGolemModel<SickenedIronGolem>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedIronGolemRenderer.EMISSIVE);
         }
      });
      this.addLayer(new SickenedIronGolemCrackinessLayer(this));
   }

   public ResourceLocation getTextureLocation(SickenedIronGolem golem) {
      return TEXTURE;
   }

   protected void setupRotations(SickenedIronGolem entity, PoseStack stack, float p_115016_, float p_115017_, float p_115018_, float scale) {
      super.setupRotations(entity, stack, p_115016_, p_115017_, p_115018_, scale);
      if (!((double)entity.walkAnimation.speed() < 0.01)) {
         float f1 = entity.walkAnimation.position(p_115018_) + 6.0F;
         float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
         stack.mulPose(Axis.ZP.rotationDegrees(6.5F * f2));
      }
   }
}
