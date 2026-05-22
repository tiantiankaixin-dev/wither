package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedPig;

public class SickenedPigRenderer extends MobRenderer<SickenedPig, PigModel<SickenedPig>> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_pig.png");
   private static final ResourceLocation EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_pig_emissive.png");
   private static final ResourceLocation EGG_TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/misc/sickened_reuben.png");

   public SickenedPigRenderer(Context context) {
      super(context, new PigModel(context.bakeLayer(WitherStormModRenderers.SICKENED_PIG)), 0.7F);
      this.addLayer(new EyesLayer<SickenedPig, PigModel<SickenedPig>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedPigRenderer.EMISSIVE);
         }
      });
   }

   public ResourceLocation getTextureLocation(SickenedPig pig) {
      return pig.hasCustomName() && pig.getName().getString().equals("reuben") ? EGG_TEXTURE : TEXTURE;
   }
}
