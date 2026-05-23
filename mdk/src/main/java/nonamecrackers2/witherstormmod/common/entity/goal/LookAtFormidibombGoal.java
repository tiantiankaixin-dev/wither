package nonamecrackers2.witherstormmod.common.entity.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class LookAtFormidibombGoal extends Goal {
   protected final WitherStormEntity storm;
   private FormidibombEntity target;

   public LookAtFormidibombGoal(WitherStormEntity storm) {
      this.storm = storm;
      this.setFlags(EnumSet.of(Flag.LOOK));
   }

   public boolean canUse() {
      if (this.storm.canBeFormidibombed(false)) {
         this.target = this.storm.getFormidibomb();
         return true;
      } else {
         return false;
      }
   }

   public boolean canContinueToUse() {
      return this.canUse();
   }

   public void start() {
      if (!this.storm.shouldIgnoreFormidibomb) {
         this.storm.getHeadManager().getHead(0).doRoar(false);
      }

      super.start();
   }

   public void stop() {
      if (this.target != null) {
         this.target.setNoGravity(false);
      }

      this.target = null;
      super.stop();
   }

   public void tick() {
      this.storm.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
      Vec3 entityPos = this.target.position();
      Vec3 headPos = this.storm.getHeadPos(0);
      Vec3 motion = headPos.subtract(entityPos).normalize();
      this.target.setNoGravity(true);
      this.target.setDeltaMovement(motion.multiply(0.1, 0.1, 0.1));
   }

   public boolean hasTarget() {
      return this.target != null;
   }
}
