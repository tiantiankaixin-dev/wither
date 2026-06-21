package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.renderer.entity.layer.WitheredSymbiontEyesLayer;
import nonamecrackers2.witherstormmod.client.renderer.entity.layer.WitheredSymbiontTearLayer;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.WitheredSymbiontModel;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;

public class WitheredSymbiontRenderer extends MobRenderer<WitheredSymbiontEntity, WitheredSymbiontModel<WitheredSymbiontEntity>> {
   private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/withered_symbiont/withered_symbiont.png");
   private static final ResourceLocation EGG_TEXTURE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/misc/crackers.png");

   public WitheredSymbiontRenderer(Context context) {
      super(context, new WitheredSymbiontModel(context.bakeLayer(WitherStormModRenderers.WITHERED_SYMBIONT)), 0.8F);
      this.addLayer(new WitheredSymbiontEyesLayer(this));
      this.addLayer(new WitheredSymbiontTearLayer(this));
      this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
      this.addLayer(
         new HumanoidArmorLayer(
            this,
            new HumanoidModel(context.bakeLayer(WitherStormModRenderers.SYMBIONT_INNER_ARMOR)),
            new HumanoidModel(context.bakeLayer(WitherStormModRenderers.SYMBIONT_OUTER_ARMOR)),
            context.getModelManager()
         )
      );
   }

   public ResourceLocation getTextureLocation(WitheredSymbiontEntity entity) {
      return entity.hasCustomName() && entity.getName().getString().equals("nonamecrackers2") ? EGG_TEXTURE : TEXTURE;
   }

   protected void setupRotations(WitheredSymbiontEntity entity, PoseStack stack, float p_225621_3_, float p_225621_4_, float p_225621_5_, float scale) {
      super.setupRotations(entity, stack, p_225621_3_, p_225621_4_, p_225621_5_, scale);
      if (!((double)entity.walkAnimation.speed() < 0.01)) {
         float f1 = entity.walkAnimation.position(p_225621_5_) + 6.0F;
         float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
         stack.mulPose(Axis.ZP.rotationDegrees(6.5F * f2));
      }
   }

   protected void scale(WitheredSymbiontEntity entity, PoseStack stack, float partialTicks) {
      stack.scale(1.8F, 1.8F, 1.8F);
   }
}
