package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedChicken;

public class SickenedChickenRenderer extends MobRenderer<SickenedChicken, ChickenModel<SickenedChicken>> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_chicken.png");
   public static final ResourceLocation EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_chicken_emissive.png");

   public SickenedChickenRenderer(Context context) {
      super(context, new ChickenModel(context.bakeLayer(WitherStormModRenderers.SICKENED_CHICKEN)), 0.3F);
      this.addLayer(new EyesLayer<SickenedChicken, ChickenModel<SickenedChicken>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedChickenRenderer.EMISSIVE);
         }
      });
   }

   public ResourceLocation getTextureLocation(SickenedChicken chicken) {
      return TEXTURE;
   }

   protected float getBob(SickenedChicken chicken, float partialTicks) {
      float f = Mth.lerp(partialTicks, chicken.oFlap, chicken.flap);
      float f1 = Mth.lerp(partialTicks, chicken.oFlapSpeed, chicken.flapSpeed);
      return (Mth.sin(f) + 1.0F) * f1;
   }
}
