package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedZombie;

public class SickenedZombieRenderer extends AbstractZombieRenderer<SickenedZombie, ZombieModel<SickenedZombie>> {
   private static final ResourceLocation SICKENED_ZOMBIE_LOCATION = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_zombie.png");
   private static final ResourceLocation SICKENED_ZOMBIE_EMISSIVE_LOCATION = new ResourceLocation(
      "witherstormmod", "textures/entity/sickened/sickened_zombie_emissive.png"
   );

   public SickenedZombieRenderer(Context context) {
      super(
         context,
         new ZombieModel(context.bakeLayer(WitherStormModRenderers.SICKENED_ZOMBIE)),
         new ZombieModel(context.bakeLayer(WitherStormModRenderers.SICKENED_ZOMBIE_INNER_ARMOR)),
         new ZombieModel(context.bakeLayer(WitherStormModRenderers.SICKENED_ZOMBIE_OUTER_ARMOR))
      );
      this.addLayer(new EyesLayer<SickenedZombie, ZombieModel<SickenedZombie>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedZombieRenderer.SICKENED_ZOMBIE_EMISSIVE_LOCATION);
         }
      });
   }

   public ResourceLocation getTextureLocation(Zombie zombie) {
      return SICKENED_ZOMBIE_LOCATION;
   }

   protected boolean isShaking(SickenedZombie entity) {
      return super.isShaking(entity) || entity.isConverting();
   }
}
