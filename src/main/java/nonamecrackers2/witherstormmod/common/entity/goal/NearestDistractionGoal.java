package nonamecrackers2.witherstormmod.common.entity.goal;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.entity.WitherStormBase;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class NearestDistractionGoal<T extends Mob & WitherStormBase> extends Goal {
   protected final T entity;
   protected int head;
   protected int unseenTime = 180;
   @Nullable
   protected Entity target;
   protected int unseenTicks;
   protected Predicate<Entity> condition;
   protected int randomInterval;

   public NearestDistractionGoal(T entity, int head, Predicate<Entity> condition, int interval) {
      this.entity = entity;
      this.head = head;
      this.condition = condition;
      this.randomInterval = interval;
      this.setFlags(EnumSet.of(Flag.TARGET));
   }

   public boolean canUse() {
      if (this.entity.canBeDistracted(this.head, WitherStormBase.DistractionType.ENTITY_BASED)) {
         if (!this.entity.isDistracted(this.head)) {
            if (this.randomInterval > 0 && this.entity.getRandom().nextInt(this.randomInterval) != 0) {
               return false;
            } else {
               this.findTarget();
               return this.target != null;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean canContinueToUse() {
      Entity entity = this.target;
      if (!this.entity.isDistracted(this.head)) {
         if (entity == null) {
            return false;
         } else if (!entity.isAlive()) {
            return false;
         } else if ((double)this.entity.distanceTo(entity) > this.getFollowDistance()) {
            return false;
         } else {
            if (this.entity.canSee(this.head, entity)) {
               this.unseenTicks = 0;
            } else if (this.unseenTicks++ > this.unseenTime) {
               return false;
            }

            if (!this.entity.isDistracted(this.head)) {
               this.entity.makeDistracted(this.getTargetPos(), this.entity.getRandom().nextInt(80) + 80, this.head);
            } else {
               this.entity.setDistractedPos(this.head, this.getTargetPos());
            }

            return true;
         }
      } else {
         if (entity == null || !entity.isAlive()) {
            if (this.entity.getRandom().nextInt(8) == 0) {
               Vec3 vector = this.entity.getDistractedPos(this.head);

               assert vector != null;

               this.entity
                  .setDistractedPos(
                     this.head, vector.add(this.entity.getRandom().nextGaussian(), this.entity.getRandom().nextGaussian(), this.entity.getRandom().nextGaussian())
                  );
            }

            this.findTarget();
            if (this.target != null && entity != this.target) {
               this.entity.makeDistracted(this.getTargetPos(), this.entity.getRandom().nextInt(80) + 80, this.head);
            }
         }

         return true;
      }
   }

   public void findTarget() {
      List<Entity> entities = WorldUtil.getPerformantEntitiesOfClass((ServerLevel)this.entity.level(), Entity.class, this.getSearchArea());
      double d0 = -1.0;
      Entity e = null;

      for (Entity e1 : entities) {
         if (this.canBeDistractedBy(e1) && !this.entity.isEntityBehindBack(e1)) {
            double d1 = e1.distanceToSqr(this.entity.getHeadPos(this.head));
            if (d0 == -1.0 || d1 < d0) {
               d0 = d1;
               e = e1;
            }
         }
      }

      this.target = e;
   }

   public void start() {
      this.unseenTicks = 0;
      this.entity.setTarget(this.head, null);
   }

   public void stop() {
      this.entity.setDistractedPos(this.head, null);
      this.target = null;
   }

   protected double getFollowDistance() {
      return this.entity.getAttributeValue(Attributes.FOLLOW_RANGE);
   }

   protected AABB getSearchArea() {
      return this.entity.getBoundingBox().inflate(this.getFollowDistance());
   }

   protected boolean canBeDistractedBy(Entity entity) {
      return this.condition.test(entity);
   }

   protected Vec3 getTargetPos() {
      assert this.target != null;

      return this.target.position();
   }
}
