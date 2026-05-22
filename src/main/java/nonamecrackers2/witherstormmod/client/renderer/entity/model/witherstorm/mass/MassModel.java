package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass;

import net.minecraft.client.model.geom.ModelPart;

@Deprecated
public class MassModel {
   protected final ModelPart body;

   public MassModel(ModelPart root) {
      this.body = root;
   }

   public ModelPart getMass() {
      return this.body;
   }
}
