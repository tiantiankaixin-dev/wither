package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedPillager;

public class SickenedPillagerRenderer extends IllagerRenderer<SickenedPillager> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_pillager.png");
   public static final ResourceLocation EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_pillager_emissive.png");

   public SickenedPillagerRenderer(Context context) {
      super(context, new IllagerModel(context.bakeLayer(WitherStormModRenderers.SICKENED_PILLAGER)), 0.5F);
      this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
      this.addLayer(new EyesLayer<SickenedPillager, IllagerModel<SickenedPillager>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedPillagerRenderer.EMISSIVE);
         }
      });
   }

   public ResourceLocation getTextureLocation(SickenedPillager pillager) {
      return TEXTURE;
   }
}
