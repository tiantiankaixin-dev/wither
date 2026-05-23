package nonamecrackers2.witherstormmod.common.entity.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.phys.AABB;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;
import org.jetbrains.annotations.NotNull;

public class NearestAttackingWitherStormGoal extends NearestAttackableTargetGoal<WitherStormEntity> {
   public NearestAttackingWitherStormGoal(Mob mob, int randomInterval) {
      super(mob, WitherStormEntity.class, randomInterval, true, false, null);
   }

   @NotNull
   protected AABB getTargetSearchArea(double range) {
      return this.mob.getBoundingBox().inflate(range, range * 2.0, range);
   }

   protected void findTarget() {
      super.findTarget();
      if (this.target instanceof WitherStormEntity storm) {
         boolean flag = true;
         if (storm.getPhase() > 3
            && (double)storm.distanceTo(this.mob) > 30.0
            && (Boolean)TractorBeamHelper.isInsideTractorBeam(this.mob, storm, 4.0).getFirst()
            && !this.mob.onGround()) {
            flag = false;
         }

         if (flag) {
            this.target = null;
         }
      }
   }

   protected double getFollowDistance() {
      return 100.0;
   }
}
