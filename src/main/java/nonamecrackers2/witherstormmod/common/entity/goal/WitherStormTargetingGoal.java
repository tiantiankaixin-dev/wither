package nonamecrackers2.witherstormmod.common.entity.goal;

import com.google.common.collect.Lists;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModAttributes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class WitherStormTargetingGoal extends Goal {
   private static final Logger LOGGER = LogManager.getLogger("witherstormmod/WitherStormTargetingGoal");
   private static final boolean LOGGING = false;
   protected final WitherStormEntity storm;
   protected final int headIndex;
   protected TargetingConditions targetConditions;
   @Nullable
   protected LivingEntity target;
   private int unseenTicks;
   @Nullable
   private Vec3 lastTargetPos;

   public WitherStormTargetingGoal(WitherStormEntity entity, Predicate<LivingEntity> mobSelector, int headIndex) {
      this.storm = entity;
      this.targetConditions = TargetingConditions.forCombat().selector(mobSelector).ignoreLineOfSight();
      this.headIndex = headIndex;
      this.setFlags(EnumSet.of(Flag.TARGET));
   }

   public boolean canUse() {
      if (!this.storm.isPlayingDead() && !this.storm.isAttractingFormidibomb() && !this.storm.isDistracted(this.headIndex)) {
         this.findApplicableTarget();
         return this.target != null;
      } else {
         return false;
      }
   }

   public boolean canContinueToUse() {
      LivingEntity entity = this.storm.getTarget(this.headIndex);
      if (entity == null) {
         return false;
      } else if (!entity.isAlive()) {
         return false;
      } else {
         Team team = this.storm.getTeam();
         Team team1 = entity.getTeam();
         if (team != null && team1 == team) {
            return false;
         } else {
            double d0 = this.getFollowDistance();
            if (this.storm.distanceToSqr(entity) > d0 * d0) {
               return false;
            } else {
               if (this.storm.getHeadManager().getHead(this.headIndex).canSee(entity)) {
                  this.unseenTicks = 0;
               } else if (++this.unseenTicks > (this.storm.getPhase() < 4 ? 80 : 20)) {
                  return false;
               }

               if (entity instanceof Player player && player.getAbilities().invulnerable) {
                  return false;
               }

               if (this.storm.getPhase() > 3 && this.storm.isEntityBehindBack(entity)) {
                  return false;
               } else if (!entity.level().dimension().equals(this.storm.level().dimension())) {
                  return false;
               } else if (this.lastTargetPos != null && entity.position().distanceTo(this.lastTargetPos) > 20.0) {
                  return false;
               } else if (this.storm.getTrackedEntities().contains(entity)) {
                  return false;
               } else {
                  this.lastTargetPos = entity.position();
                  return true;
               }
            }
         }
      }
   }

   public void start() {
      this.storm.getHeadManager().getHead(this.headIndex).setTarget(this.target);
      this.unseenTicks = 0;
      this.lastTargetPos = null;
   }

   public void stop() {
      this.storm.getHeadManager().getHead(this.headIndex).setTarget(null);
   }

   @NotNull
   protected AABB getTargetSearchArea(double range) {
      return this.storm.getPhase() > 3 ? this.storm.getBoundingBox().inflate(range, range + 50.0, range) : this.storm.getBoundingBox().inflate(range, range * 2.0, range);
   }

   protected double getFollowDistance() {
      return this.storm.getAttributeValue(Attributes.FOLLOW_RANGE) + 100.0;
   }

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
         if (!(t1.getBoundingBox().getSize() <= 0.5) || t1.getRandom().nextInt(4) <= 0) {
            double d1 = t1.distanceToSqr(this.storm.getX(), this.storm.getEyeY(), this.storm.getZ());
            if (d0 == -1.0 || d1 < d0) {
               d0 = d1;
               t = t1;
            }
         }
      }

      this.target = t;
      if (this.target != null) {
         this.target.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(WitherSicknessTracker::countContact);
      }
   }
}
