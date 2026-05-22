package nonamecrackers2.witherstormmod.client.renderer.entity.model.sickenedentity;

import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import nonamecrackers2.witherstormmod.common.entity.SickenedZombie;

@Deprecated
public class SickenedZombieModel<T extends SickenedZombie> extends AbstractZombieModel<T> {
   public SickenedZombieModel(ModelPart part) {
      super(part);
   }

   public boolean isAggressive(T entity) {
      return entity.isAggressive();
   }
}
