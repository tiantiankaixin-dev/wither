package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.renderer.entity.layer.SickenedSnowGolemHeadLayer;
import nonamecrackers2.witherstormmod.common.entity.SickenedSnowGolem;

public class SickenedSnowGolemRenderer extends MobRenderer<SickenedSnowGolem, SnowGolemModel<SickenedSnowGolem>> {
   private static final ResourceLocation SNOW_GOLEM_LOCATION = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_snow_golem.png");
   private static final ResourceLocation SNOW_GOLEM_EMISSIVE_LOCATION = new ResourceLocation(
      "witherstormmod", "textures/entity/sickened/sickened_snow_golem_emissive.png"
   );

   public SickenedSnowGolemRenderer(Context context) {
      super(context, new SnowGolemModel(context.bakeLayer(WitherStormModRenderers.SICKENED_SNOW_GOLEM)), 0.5F);
      this.addLayer(new SickenedSnowGolemHeadLayer(this, context.getBlockRenderDispatcher(), context.getItemRenderer()));
      this.addLayer(new EyesLayer<SickenedSnowGolem, SnowGolemModel<SickenedSnowGolem>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedSnowGolemRenderer.SNOW_GOLEM_EMISSIVE_LOCATION);
         }
      });
   }

   public ResourceLocation getTextureLocation(SickenedSnowGolem entity) {
      return SNOW_GOLEM_LOCATION;
   }
}
