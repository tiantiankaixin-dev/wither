package nonamecrackers2.witherstormmod.client.renderer.entity.layer;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Crackiness;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.sickenedentity.SickenedIronGolemModel;
import nonamecrackers2.witherstormmod.common.entity.SickenedIronGolem;

public class SickenedIronGolemCrackinessLayer extends RenderLayer<SickenedIronGolem, SickenedIronGolemModel<SickenedIronGolem>> {
   private static final Map<Crackiness.Level, ResourceLocation> LAYERS = ImmutableMap.of(
      Crackiness.Level.LOW,
      ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/sickened/sickened_iron_golem_crackiness_low.png"),
      Crackiness.Level.MEDIUM,
      ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/sickened/sickened_iron_golem_crackiness_medium.png"),
      Crackiness.Level.HIGH,
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
         Crackiness.Level irongolem$crackiness = entity.getCrackiness();
         if (irongolem$crackiness != Crackiness.Level.NONE) {
            ResourceLocation resourcelocation = LAYERS.get(irongolem$crackiness);
            renderColoredCutoutModel(
               this.getParentModel(),
               resourcelocation,
               stack,
               buffer,
               p_117351_,
               entity,
               FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 1.0F, 1.0F)
            );
         }
      }
   }
}
