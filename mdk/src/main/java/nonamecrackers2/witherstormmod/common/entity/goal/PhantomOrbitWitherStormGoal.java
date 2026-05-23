package nonamecrackers2.witherstormmod.common.entity.goal;

import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import nonamecrackers2.witherstormmod.mixin.IMixinPhantom;

public class PhantomOrbitWitherStormGoal extends Goal {
   private final Phantom entity;
   @Nullable
   private WitherStormEntity storm;
   private float angle;
   private float distance;
   private float height;
   private float clockwise;
   private final int chance;

   public PhantomOrbitWitherStormGoal(Phantom entity) {
      this(entity, 100);
   }

   public PhantomOrbitWitherStormGoal(Phantom entity, int chance) {
      this.entity = entity;
      this.chance = chance;
      this.setFlags(EnumSet.of(Flag.MOVE));
   }

   protected boolean touchingTarget() {
      return ((IMixinPhantom)this.entity).getMoveTargetPoint().distanceToSqr(this.entity.getX(), this.entity.getY(), this.entity.getZ()) < 4.0;
   }

   public boolean canUse() {
      if (this.entity.getRandom().nextInt(this.chance) == 0) {
         this.storm = this.findStorm();
      }

      return this.storm != null && this.storm.getPhase() > 2;
   }

   public void start() {
      this.distance = 25.0F + this.entity.getRandom().nextFloat() * 10.0F;
      this.height = -4.0F + this.entity.getRandom().nextFloat() * 9.0F;
      this.clockwise = this.entity.getRandom().nextBoolean() ? 1.0F : -1.0F;
      this.selectNext();
   }

   public void tick() {
      if (this.entity.getRandom().nextInt(350) == 0) {
         this.height = -4.0F + this.entity.getRandom().nextFloat() * 9.0F;
      }

      if (this.entity.getRandom().nextInt(250) == 0) {
         this.distance++;
         if (this.distance > 75.0F) {
            this.distance = 25.0F;
            this.clockwise = -this.clockwise;
         }
      }

      if (this.entity.getRandom().nextInt(450) == 0) {
         this.angle = this.entity.getRandom().nextFloat() * 2.0F * (float) Math.PI;
         this.selectNext();
      }

      if (this.touchingTarget()) {
         this.selectNext();
      }

      if (((IMixinPhantom)this.entity).getMoveTargetPoint().y < this.entity.getY()
         && !this.entity.level().isEmptyBlock(this.entity.blockPosition().below(1))) {
         this.height = Math.max(1.0F, this.height);
         this.selectNext();
      }

      if (((IMixinPhantom)this.entity).getMoveTargetPoint().y > this.entity.getY()
         && !this.entity.level().isEmptyBlock(this.entity.blockPosition().below(1))) {
         this.height = Math.min(-1.0F, this.height);
         this.selectNext();
      }
   }

   private void selectNext() {
      this.angle = this.angle + this.clockwise * 15.0F * (float) (Math.PI / 180.0);
      if (this.storm != null) {
         ((IMixinPhantom)this.entity).setAnchorPoint(this.storm.blockPosition().above((int)this.storm.getBbHeight() + 20));
      }

      ((IMixinPhantom)this.entity)
         .setMoveTargetPoint(
            Vec3.atLowerCornerOf(((IMixinPhantom)this.entity).getAnchorPoint())
               .add((double)(this.distance * Mth.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * Mth.sin(this.angle)))
         );
   }

   private WitherStormEntity findStorm() {
      List<WitherStormEntity> entities = WorldUtil.getPerformantEntitiesOfClass(
         (ServerLevel)this.entity.level(), WitherStormEntity.class, this.entity.getBoundingBox().inflate(100.0)
      );
      double d0 = -1.0;
      WitherStormEntity closest = null;

      for (WitherStormEntity storm : entities) {
         if (this.entity.hasLineOfSight(storm)) {
            double d1 = storm.distanceToSqr(this.entity.getX(), this.entity.getY(), this.entity.getZ());
            if (d0 == -1.0 || d1 < d0) {
               d0 = d1;
               closest = storm;
            }
         }
      }

      return closest;
   }
}
