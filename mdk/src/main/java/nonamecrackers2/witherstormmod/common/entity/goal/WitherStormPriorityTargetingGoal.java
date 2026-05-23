package nonamecrackers2.witherstormmod.common.entity.goal;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModAttributes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModEntityTags;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class WitherStormPriorityTargetingGoal extends WitherStormTargetingGoal {
   public WitherStormPriorityTargetingGoal(WitherStormEntity entity, Predicate<LivingEntity> mobSelector, int headIndex) {
      super(entity, mobSelector.and(e -> e.getType().is(WitherStormModEntityTags.FAVOURABLE_MOBS)), headIndex);
   }

   @Override
   public boolean canUse() {
      return (Boolean)WitherStormModConfig.SERVER.specialTargetingBias.get()
         && this.storm.getPlayingJukeboxes().isEmpty()
         && this.storm.getTarget(this.headIndex) == null
         && this.storm.getRandom().nextInt(100) <= (Integer)WitherStormModConfig.SERVER.specialTargetingBiasChance.get()
         && super.canUse();
   }

   @Override
   protected void findApplicableTarget() {
      double range = this.storm.getPhase() > 3
         ? this.storm.getAttributeValue(Attributes.FOLLOW_RANGE)
         : this.storm.getAttributeValue((Attribute)WitherStormModAttributes.HUNCHBACK_FOLLOW_RANGE.get());
      List<LivingEntity> nearbyEntities = WorldUtil.getPerformantEntitiesOfClass(
         (ServerLevel)this.storm.level(), LivingEntity.class, this.getTargetSearchArea(range)
      );
      List<LivingEntity> targetableEntities = Lists.newArrayList();

      for (LivingEntity entity : nearbyEntities) {
         if (this.storm.targetApplicable(entity, this.headIndex, this.targetConditions)) {
            targetableEntities.add(entity);
         }
      }

      double d0 = -1.0;
      LivingEntity t = null;

      for (LivingEntity t1 : targetableEntities) {
         double d1 = t1.distanceToSqr(this.storm.getX(), this.storm.getEyeY(), this.storm.getZ());
         if (d0 == -1.0 || d1 < d0) {
            d0 = d1;
            t = t1;
         }
      }

      this.target = t;
      if (this.target != null) {
         this.target.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(WitherSicknessTracker::countContact);
      }
   }
}
