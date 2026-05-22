package nonamecrackers2.witherstormmod.common.util;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class EmptyBodyController extends BodyRotationControl {
   public EmptyBodyController(Mob entity) {
      super(entity);
   }

   public void clientTick() {
   }
}
