package nonamecrackers2.witherstormmod.common.entity.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class DoNothingGoal extends Goal {
   private final WitherStormEntity storm;
   private final int head;

   public DoNothingGoal(WitherStormEntity entity, int head) {
      this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
      this.storm = entity;
      this.head = head;
   }

   public boolean canUse() {
      return this.storm.shouldDoNothing() || this.head > 0 && this.storm.areOtherHeadsDisabled();
   }
}
