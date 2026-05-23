package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.renderer.entity.layer.SickenedMushroomCowMushroomLayer;
import nonamecrackers2.witherstormmod.common.entity.SickenedMushroomCow;

public class SickenedMushroomCowRenderer extends MobRenderer<SickenedMushroomCow, CowModel<SickenedMushroomCow>> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_mushroom_cow.png");
   private static final ResourceLocation EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_mushroom_cow_emissive.png");

   public SickenedMushroomCowRenderer(Context context) {
      super(context, new CowModel(context.bakeLayer(WitherStormModRenderers.SICKENED_MUSHROOM_COW)), 0.7F);
      this.addLayer(new EyesLayer<SickenedMushroomCow, CowModel<SickenedMushroomCow>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedMushroomCowRenderer.EMISSIVE);
         }
      });
      this.addLayer(new SickenedMushroomCowMushroomLayer(this, context.getBlockRenderDispatcher()));
   }

   public ResourceLocation getTextureLocation(SickenedMushroomCow cow) {
      return TEXTURE;
   }
}
