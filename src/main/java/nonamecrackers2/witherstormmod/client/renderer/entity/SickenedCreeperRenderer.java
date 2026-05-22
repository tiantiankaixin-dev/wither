package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedCreeper;

public class SickenedCreeperRenderer extends MobRenderer<SickenedCreeper, CreeperModel<SickenedCreeper>> {
   private static final ResourceLocation SICKENED_CREEPER_LOCATION = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_creeper.png");
   private static final ResourceLocation SICKENED_CREEPER_EMISSIVE_LOCATION = new ResourceLocation(
      "witherstormmod", "textures/entity/sickened/sickened_creeper_emissive.png"
   );

   public SickenedCreeperRenderer(Context context) {
      super(context, new CreeperModel(context.bakeLayer(WitherStormModRenderers.SICKENED_CREEPER)), 0.5F);
      this.addLayer(new SickenedCreeperRenderer.ChargeLayer(this, context.getModelSet()));
      this.addLayer(new EyesLayer<SickenedCreeper, CreeperModel<SickenedCreeper>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedCreeperRenderer.SICKENED_CREEPER_EMISSIVE_LOCATION);
         }
      });
   }

   protected void scale(SickenedCreeper entity, PoseStack stack, float partialTicks) {
      float f = entity.getSwelling(partialTicks);
      float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
      f *= f;
      f *= f;
      float f2 = (1.0F + f * 0.4F) * f1;
      float f3 = (1.0F + f * 0.1F) / f1;
      stack.scale(f2, f3, f2);
   }

   protected float getWhiteOverlayProgress(SickenedCreeper entity, float partialTicks) {
      float f = entity.getSwelling(partialTicks);
      return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
   }

   public ResourceLocation getTextureLocation(SickenedCreeper creeper) {
      return SICKENED_CREEPER_LOCATION;
   }

   protected boolean isShaking(SickenedCreeper entity) {
      return super.isShaking(entity) || entity.isConverting();
   }

   private static class ChargeLayer extends EnergySwirlLayer<SickenedCreeper, CreeperModel<SickenedCreeper>> {
      private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
      private final CreeperModel<SickenedCreeper> model;

      public ChargeLayer(RenderLayerParent<SickenedCreeper, CreeperModel<SickenedCreeper>> renderer, EntityModelSet set) {
         super(renderer);
         this.model = new CreeperModel(set.bakeLayer(WitherStormModRenderers.SICKENED_CREEPER_ARMOR));
      }

      protected float xOffset(float p_225634_1_) {
         return p_225634_1_ * 0.01F;
      }

      protected ResourceLocation getTextureLocation() {
         return POWER_LOCATION;
      }

      protected EntityModel<SickenedCreeper> model() {
         return this.model;
      }
   }
}
