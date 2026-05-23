package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedCow;

public class SickenedCowRenderer extends MobRenderer<SickenedCow, CowModel<SickenedCow>> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_cow.png");
   private static final ResourceLocation EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_cow_emissive.png");

   public SickenedCowRenderer(Context context) {
      super(context, new CowModel(context.bakeLayer(WitherStormModRenderers.SICKENED_COW)), 0.7F);
      this.addLayer(new EyesLayer<SickenedCow, CowModel<SickenedCow>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedCowRenderer.EMISSIVE);
         }
      });
   }

   public ResourceLocation getTextureLocation(SickenedCow cow) {
      return TEXTURE;
   }
}
