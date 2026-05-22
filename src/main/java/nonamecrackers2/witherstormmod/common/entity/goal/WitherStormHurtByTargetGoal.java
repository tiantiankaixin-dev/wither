package nonamecrackers2.witherstormmod.common.entity.goal;

import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class WitherStormHurtByTargetGoal extends HurtByTargetGoal {
   private final Predicate<LivingEntity> entitySelector;

   public WitherStormHurtByTargetGoal(PathfinderMob mob, Predicate<LivingEntity> entitySelector) {
      super(mob, new Class[0]);
      this.entitySelector = entitySelector;
   }

   protected boolean canAttack(LivingEntity entity, TargetingConditions conditions) {
      return this.entitySelector.test(entity) && super.canAttack(entity, conditions);
   }
}
