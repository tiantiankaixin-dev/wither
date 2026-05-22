package nonamecrackers2.witherstormmod.common.entity.goal;

import java.util.EnumSet;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import nonamecrackers2.witherstormmod.api.common.entity.WitherStormBase;

public class LookAtTargetGoal<T extends LivingEntity & WitherStormBase> extends Goal {
   protected final T mob;
   protected final int headIndex;
   protected final Function<T, Integer> lookStepsGetter;
   protected LivingEntity target;

   public LookAtTargetGoal(T mob, int headIndex, Function<T, Integer> lookStepsGetter) {
      this.mob = mob;
      this.headIndex = headIndex;
      this.lookStepsGetter = lookStepsGetter;
      this.setFlags(EnumSet.of(Flag.LOOK));
   }

   public boolean canUse() {
      LivingEntity target = this.getTarget();
      if (target != null && target.isAlive()) {
         this.target = target;
         return true;
      } else {
         return false;
      }
   }

   @Nullable
   protected LivingEntity getTarget() {
      return this.mob.getTarget(this.headIndex);
   }

   public void stop() {
      this.target = null;
      this.mob.setLookAt(this.headIndex, null);
      super.stop();
   }

   public void tick() {
      this.mob.setLookAt(this.headIndex, this.target.getEyePosition(), this.lookStepsGetter.apply(this.mob));
   }
}
