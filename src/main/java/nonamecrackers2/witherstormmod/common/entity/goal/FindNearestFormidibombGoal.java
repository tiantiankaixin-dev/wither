package nonamecrackers2.witherstormmod.common.entity.goal;

import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class FindNearestFormidibombGoal extends TargetGoal {
   private final WitherStormEntity storm;
   private FormidibombEntity target;
   private final int randomInterval;

   public FindNearestFormidibombGoal(WitherStormEntity storm, int randomInterval) {
      super(storm, true);
      this.storm = storm;
      this.randomInterval = randomInterval;
      this.setFlags(EnumSet.of(Flag.TARGET));
   }

   public boolean canUse() {
      if (this.storm.shouldIgnoreFormidibomb) {
         return false;
      } else if (this.randomInterval > 0 && this.storm.getRandom().nextInt(this.randomInterval) != 0) {
         return false;
      } else {
         this.findTarget();
         return this.target != null;
      }
   }

   private void findTarget() {
      this.target = this.getNearestLoadedFormidibomb(
         this.storm.getX(), this.storm.getEyeY(), this.storm.getZ(), this.getTargetSearchArea(this.getFollowDistance())
      );
   }

   public void start() {
      this.storm.setFormidibomb(this.target);
      super.start();
   }

   protected double getFollowDistance() {
      return this.mob.getAttributeBaseValue(Attributes.FOLLOW_RANGE) + 50.0;
   }

   private AABB getTargetSearchArea(double inflation) {
      return this.storm.getPhase() > 3
         ? this.mob.getBoundingBox().inflate(inflation, inflation + 255.0, inflation)
         : this.mob.getBoundingBox().inflate(40.0, 20.0, 40.0);
   }

   @Nullable
   private FormidibombEntity getNearestLoadedFormidibomb(double x, double y, double z, AABB bounding) {
      return this.getNearestFormidibomb(
         WorldUtil.getPerformantEntitiesOfClass((ServerLevel)this.storm.level(), FormidibombEntity.class, bounding, EntitySelector.NO_SPECTATORS),
         this.storm,
         x,
         y,
         z
      );
   }

   @Nullable
   private FormidibombEntity getNearestFormidibomb(List<FormidibombEntity> entities, WitherStormEntity targeter, double x, double y, double z) {
      double d0 = -1.0;
      FormidibombEntity formidibomb = null;

      for (FormidibombEntity entity : entities) {
         if (this.test(targeter, entity)) {
            double d1 = entity.distanceToSqr(x, y, z);
            if (d0 == -1.0 || d1 < d0) {
               d0 = d1;
               formidibomb = entity;
            }
         }
      }

      return formidibomb;
   }

   private boolean test(WitherStormEntity entity, Entity target) {
      if (entity == target) {
         return false;
      } else if (!(target instanceof FormidibombEntity)) {
         return false;
      } else if (target.isSpectator()) {
         return false;
      } else if (!target.isAlive()) {
         return false;
      } else if (entity != null) {
         if (this.getFollowDistance() > 0.0) {
            double d0 = Math.max(this.getFollowDistance(), 2.0);
            double d1 = entity.distanceToSqr(target.getX(), target.getY(), target.getZ());
            if (d1 > d0 * d0) {
               return false;
            }
         }

         return entity.getHeadManager().getHead(0).canSee(target);
      } else {
         return true;
      }
   }
}
