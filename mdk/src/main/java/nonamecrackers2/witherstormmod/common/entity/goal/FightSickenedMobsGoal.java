package nonamecrackers2.witherstormmod.common.entity.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import nonamecrackers2.witherstormmod.common.entity.SickenedCreeper;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;

public class FightSickenedMobsGoal extends NearestAttackableTargetGoal<Mob> {
   public FightSickenedMobsGoal(Mob mob) {
      super(mob, Mob.class, 10, true, false, entity -> entity instanceof WitherSickened && !(entity instanceof SickenedCreeper));
   }
}
