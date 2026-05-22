package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedSpider;

public class SickenedSpiderRenderer extends SpiderRenderer<SickenedSpider> {
   private static final ResourceLocation SICKENED_SPIDER_LOCATION = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_spider.png");
   private static final ResourceLocation SICKENED_SPIDER_EMISSIVE_LOCATION = new ResourceLocation(
      "witherstormmod", "textures/entity/sickened/sickened_spider_emissive.png"
   );

   public SickenedSpiderRenderer(Context context) {
      super(context, WitherStormModRenderers.SICKENED_SPIDER);
      this.layers.removeIf(l -> l instanceof SpiderEyesLayer);
      this.addLayer(new EyesLayer<SickenedSpider, SpiderModel<SickenedSpider>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedSpiderRenderer.SICKENED_SPIDER_EMISSIVE_LOCATION);
         }
      });
   }

   protected void scale(SickenedSpider entity, PoseStack stack, float p_225620_3_) {
      stack.scale(1.2F, 1.2F, 1.2F);
   }

   protected boolean isShaking(SickenedSpider entity) {
      return super.isShaking(entity) || entity.isConverting();
   }

   public ResourceLocation getTextureLocation(SickenedSpider entity) {
      return SICKENED_SPIDER_LOCATION;
   }
}
