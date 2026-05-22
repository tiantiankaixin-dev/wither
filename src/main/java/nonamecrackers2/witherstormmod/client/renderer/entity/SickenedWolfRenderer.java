package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;

public class SickenedWolfRenderer extends WolfRenderer {
   public static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_wolf.png");
   public static final ResourceLocation EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_wolf_emissive.png");

   public SickenedWolfRenderer(Context context) {
      super(context);
      this.addLayer(new EyesLayer<Wolf, WolfModel<Wolf>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedWolfRenderer.EMISSIVE);
         }
      });
   }

   public ResourceLocation getTextureLocation(Wolf wolf) {
      return TEXTURE;
   }

   protected boolean isShaking(Wolf entity) {
      return super.isShaking(entity) || ((WitherSickened)entity).isConverting();
   }
}
