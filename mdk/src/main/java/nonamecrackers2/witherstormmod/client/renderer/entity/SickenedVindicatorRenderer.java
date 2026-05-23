package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedVindicator;
import org.jetbrains.annotations.NotNull;

public class SickenedVindicatorRenderer extends IllagerRenderer<SickenedVindicator> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_vindicator.png");
   public static final ResourceLocation EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_vindicator_emissive.png");

   public SickenedVindicatorRenderer(Context context) {
      super(context, new IllagerModel(context.bakeLayer(WitherStormModRenderers.SICKENED_VINDICATOR)), 0.5F);
      this.addLayer(
         new ItemInHandLayer<SickenedVindicator, IllagerModel<SickenedVindicator>>(this, context.getItemInHandRenderer()) {
            public void render(
               @NotNull PoseStack stack,
               @NotNull MultiBufferSource source,
               int i,
               @NotNull SickenedVindicator sickenedVindicator,
               float f1,
               float f2,
               float f3,
               float f4,
               float f5,
               float f6
            ) {
               if (sickenedVindicator.isAggressive()) {
                  super.render(stack, source, i, sickenedVindicator, f1, f2, f3, f4, f5, f6);
               }
            }
         }
      );
      this.addLayer(new EyesLayer<SickenedVindicator, IllagerModel<SickenedVindicator>>(this) {
         @NotNull
         public RenderType renderType() {
            return RenderType.eyes(SickenedVindicatorRenderer.EMISSIVE);
         }
      });
   }

   @NotNull
   public ResourceLocation getTextureLocation(@NotNull SickenedVindicator sickenedVindicator) {
      return TEXTURE;
   }

   protected boolean isShaking(SickenedVindicator entity) {
      return super.isShaking(entity) || entity.isConverting();
   }
}
