package nonamecrackers2.witherstormmod.common.entity.goal;

import java.util.EnumSet;
import java.util.function.Supplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.entity.WitherStormBase;

public class LookAtDistractionGoal<T extends Mob & WitherStormBase> extends Goal {
   protected final int headIndex;
   protected final T entity;
   protected final Supplier<Double> allowedTargetingRadius;
   protected Vec3 target;

   public LookAtDistractionGoal(T entity, int headIndex, Supplier<Double> allowedTargetingRadius) {
      this.entity = entity;
      this.allowedTargetingRadius = allowedTargetingRadius;
      this.setFlags(EnumSet.of(Flag.LOOK));
      this.headIndex = headIndex;
   }

   public LookAtDistractionGoal(T entity, int headIndex) {
      this(entity, headIndex, () -> Double.POSITIVE_INFINITY);
   }

   public boolean canUse() {
      Vec3 target = this.entity.getDistractedPos(this.headIndex);
      if (target != null && target.subtract(this.entity.position()).horizontalDistance() <= this.allowedTargetingRadius.get() && !this.entity.isPosBehindBack(target)) {
         this.target = target;
         return true;
      } else {
         return false;
      }
   }

   public void stop() {
      this.target = null;
      this.entity.setDistractedPos(this.headIndex, null);
      super.stop();
   }

   public void tick() {
      this.entity.setLookAt(this.headIndex, this.target, 10);
   }
}
