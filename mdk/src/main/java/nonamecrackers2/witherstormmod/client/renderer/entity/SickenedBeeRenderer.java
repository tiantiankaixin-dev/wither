package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.BeeModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedBee;

public class SickenedBeeRenderer extends MobRenderer<SickenedBee, BeeModel<SickenedBee>> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_bee.png");
   private static final ResourceLocation EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_bee_emissive.png");

   public SickenedBeeRenderer(Context context) {
      super(context, new BeeModel(context.bakeLayer(WitherStormModRenderers.SICKENED_BEE)), 0.4F);
      this.addLayer(new EyesLayer<SickenedBee, BeeModel<SickenedBee>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedBeeRenderer.EMISSIVE);
         }
      });
   }

   public ResourceLocation getTextureLocation(SickenedBee bee) {
      return TEXTURE;
   }

   protected boolean isShaking(SickenedBee entity) {
      return super.isShaking(entity) || entity.isConverting();
   }
}
