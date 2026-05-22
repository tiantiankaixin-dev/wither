package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.GoatModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedGoat;

public class SickenedGoatRenderer extends MobRenderer<SickenedGoat, GoatModel<SickenedGoat>> {
   private static final ResourceLocation GOAT_LOCATION = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_snow_golem.png");
   private static final ResourceLocation GOAT_EMISSIVE_LOCATION = new ResourceLocation(
      "witherstormmod", "textures/entity/sickened/sickened_snow_golem_emissive.png"
   );

   public SickenedGoatRenderer(Context context) {
      super(context, new GoatModel(context.bakeLayer(WitherStormModRenderers.SICKENED_GOAT)), 0.5F);
      this.addLayer(new EyesLayer<SickenedGoat, GoatModel<SickenedGoat>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedGoatRenderer.GOAT_EMISSIVE_LOCATION);
         }
      });
   }

   public ResourceLocation getTextureLocation(SickenedGoat entity) {
      return GOAT_LOCATION;
   }
}
