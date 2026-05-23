package nonamecrackers2.witherstormmod.client.renderer.entity;

import net.minecraft.client.model.ZombieVillagerModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.common.entity.SickenedVillager;

public class SickenedVillagerRenderer extends HumanoidMobRenderer<SickenedVillager, ZombieVillagerModel<SickenedVillager>> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_villager.png");
   private static final ResourceLocation TEXTURE_EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/sickened/sickened_villager_emissive.png");

   public SickenedVillagerRenderer(Context context) {
      super(context, new ZombieVillagerModel(context.bakeLayer(WitherStormModRenderers.SICKENED_VILLAGER)), 0.5F);
      this.addLayer(
         new HumanoidArmorLayer(
            this,
            new ZombieVillagerModel(context.bakeLayer(WitherStormModRenderers.SICKENED_VILLAGER_INNER_ARMOR)),
            new ZombieVillagerModel(context.bakeLayer(WitherStormModRenderers.SICKENED_VILLAGER_OUTER_ARMOR)),
            context.getModelManager()
         )
      );
      this.addLayer(new VillagerProfessionLayer(this, context.getResourceManager(), "zombie_villager"));
      this.addLayer(new EyesLayer<SickenedVillager, ZombieVillagerModel<SickenedVillager>>(this) {
         public RenderType renderType() {
            return RenderType.eyes(SickenedVillagerRenderer.TEXTURE_EMISSIVE);
         }
      });
   }

   public ResourceLocation getTextureLocation(SickenedVillager entity) {
      return TEXTURE;
   }

   protected boolean isShaking(SickenedVillager entity) {
      return super.isShaking(entity) || entity.isConverting();
   }
}
