package nonamecrackers2.witherstormmod.client.renderer.entity.layer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.WitheredSymbiontModel;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;

public class WitheredSymbiontEyesLayer extends EyesLayer<WitheredSymbiontEntity, WitheredSymbiontModel<WitheredSymbiontEntity>> {
   private static final RenderType EYES = RenderType.eyes(
      new ResourceLocation("witherstormmod", "textures/entity/withered_symbiont/withered_symbiont_emissive.png")
   );

   public WitheredSymbiontEyesLayer(RenderLayerParent<WitheredSymbiontEntity, WitheredSymbiontModel<WitheredSymbiontEntity>> renderer) {
      super(renderer);
   }

   public RenderType renderType() {
      return EYES;
   }
}
