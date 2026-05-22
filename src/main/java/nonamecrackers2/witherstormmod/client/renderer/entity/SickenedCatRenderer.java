package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.CatModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;

public class SickenedCatRenderer extends CatRenderer {
   public static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_cat.png");
   public static final ResourceLocation EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_cat_emissive.png");

   public SickenedCatRenderer(Context context) {
      super(context);
      this.addLayer(new EyesLayer<Cat, CatModel<Cat>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedCatRenderer.EMISSIVE);
         }
      });
   }

   public ResourceLocation getTextureLocation(Cat cat) {
      return TEXTURE;
   }

   protected boolean isShaking(Cat entity) {
      return super.isShaking(entity) || ((WitherSickened)entity).isConverting();
   }
}
