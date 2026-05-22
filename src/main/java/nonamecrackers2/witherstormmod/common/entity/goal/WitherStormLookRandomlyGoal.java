package nonamecrackers2.witherstormmod.common.entity.goal;

import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormLookRandomlyGoal extends YAffectedLookRandomlyGoal {
   private final WitherStormEntity storm;
   private final int head;

   public WitherStormLookRandomlyGoal(WitherStormEntity entity, int head, int Xmin, int Xmax, int Ymin, int Ymax, int lookTime) {
      super(entity, Xmin, Xmax, Ymin, Ymax, lookTime);
      this.storm = entity;
      this.head = head;
   }

   public WitherStormLookRandomlyGoal(WitherStormEntity entity, int head, int lookTime) {
      this(entity, head, -140, -30, -80, 80, lookTime);
   }

   @Override
   public boolean canUse() {
      return true;
   }

   @Override
   public Vec3 getPos() {
      return this.storm.getHeadPos(this.head).add(this.relX, this.relY, this.relZ);
   }

   @Override
   protected int getRandomLookTime() {
      if (this.storm.getPhase() < 4) {
         return 20;
      } else {
         return this.storm.isHeadInjured(this.head) ? 20 : super.getRandomLookTime();
      }
   }

   @Override
   protected void look() {
      int steps = !this.storm.isHeadInjured(this.head) && this.storm.getPhase() >= 4 ? 50 : 3;
      this.storm.setLookAt(this.head, this.getPos(), steps);
   }
}
