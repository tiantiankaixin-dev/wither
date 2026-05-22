package nonamecrackers2.witherstormmod.common.util;

import java.util.function.Predicate;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

public class ConditionalLookController<T extends Mob> extends LookControl {
   private final T mob;
   private final Predicate<T> resetXRot;

   public ConditionalLookController(T mob, Predicate<T> resetXRot) {
      super(mob);
      this.mob = mob;
      this.resetXRot = resetXRot;
   }

   protected boolean resetXRotOnTick() {
      return this.resetXRot.test(this.mob);
   }
}
