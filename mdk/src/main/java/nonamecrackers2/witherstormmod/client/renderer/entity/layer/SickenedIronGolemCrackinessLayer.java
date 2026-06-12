package nonamecrackers2.witherstormmod.client.renderer.entity.layer;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.IronGolem.Crackiness;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.sickenedentity.SickenedIronGolemModel;
import nonamecrackers2.witherstormmod.common.entity.SickenedIronGolem;

public class SickenedIronGolemCrackinessLayer extends RenderLayer<SickenedIronGolem, SickenedIronGolemModel<SickenedIronGolem>> {
   private static final Map<IronGolem.Crackiness, ResourceLocation> LAYERS = ImmutableMap.of(
      IronGolem.Crackiness.NONE,
      ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/sickened/sickened_iron_golem_crackiness_low.png"),
      IronGolem.Crackiness.NONE,
      ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/sickened/sickened_iron_golem_crackiness_medium.png"),
      IronGolem.Crackiness.NONE,
      ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/sickened/sickened_iron_golem_crackiness_high.png")
   );

   public SickenedIronGolemCrackinessLayer(RenderLayerParent<SickenedIronGolem, SickenedIronGolemModel<SickenedIronGolem>> parent) {
      super(parent);
   }

   public void render(
      PoseStack stack,
      MultiBufferSource buffer,
      int p_117351_,
      SickenedIronGolem entity,
      float p_117353_,
      float p_117354_,
      float p_117355_,
      float p_117356_,
      float p_117357_,
      float p_117358_
   ) {
      if (!entity.isInvisible()) {
         IronGolem.Crackiness irongolem$int /* Crackiness removed */ = entity.getCrackiness();
         if (irongolem$int /* Crackiness removed */ != IronGolem.Crackiness.NONE) {
            ResourceLocation resourcelocation = LAYERS.get(irongolem$int /* Crackiness removed */);
            renderColoredCutoutModel(this.getParentModel(), resourcelocation, stack, buffer, p_117351_, entity, 1.0F, 1.0F, 1.0F);
         }
      }
   }
}
