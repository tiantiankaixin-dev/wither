package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedSkeleton;

public class SickenedSkeletonRenderer extends SkeletonRenderer {
   private static final ResourceLocation SICKENED_SKELETON_LOCATION = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_skeleton.png");
   private static final ResourceLocation SICKENED_SKELETON_EMISSIVE_LOCATION = new ResourceLocation(
      "witherstormmod", "textures/entity/sickened/sickened_skeleton_emissive.png"
   );

   public SickenedSkeletonRenderer(Context context) {
      super(
         context,
         WitherStormModRenderers.SICKENED_SKELETON,
         WitherStormModRenderers.SICKENED_SKELETON_INNER_ARMOR,
         WitherStormModRenderers.SICKENED_SKELETON_OUTER_ARMOR
      );
      this.addLayer(new EyesLayer<AbstractSkeleton, SkeletonModel<AbstractSkeleton>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedSkeletonRenderer.SICKENED_SKELETON_EMISSIVE_LOCATION);
         }
      });
   }

   public ResourceLocation getTextureLocation(AbstractSkeleton entity) {
      return SICKENED_SKELETON_LOCATION;
   }

   protected boolean isShaking(AbstractSkeleton entity) {
      return super.isShaking(entity) || ((SickenedSkeleton)entity).isConverting();
   }
}
