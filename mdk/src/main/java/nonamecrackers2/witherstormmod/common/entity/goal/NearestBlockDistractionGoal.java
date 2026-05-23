package nonamecrackers2.witherstormmod.common.entity.goal;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import nonamecrackers2.witherstormmod.api.common.entity.WitherStormBase;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class NearestBlockDistractionGoal<T extends Mob & WitherStormBase> extends Goal {
   private static final int TICKS_UNTIL_RETRY = 60;
   private static final int LOOK_AT_DISTRACTION_TIME_MIN = 120;
   private static final int LOOK_AT_DISTRACTION_TIME_MAX = 180;
   private static final double MINIMUM_DISTANCE_BETWEEN_BEAMS = 10.0;
   protected final T mob;
   protected final int headIndex;
   private BlockPos targetPos;
   private int retry;

   public NearestBlockDistractionGoal(T mob, int headIndex) {
      this.mob = mob;
      this.headIndex = headIndex;
      this.setFlags(EnumSet.of(Flag.TARGET));
   }

   public boolean canUse() {
      if (!ForgeEventFactory.getMobGriefingEvent(this.mob.level(), this.mob)) {
         return false;
      } else if (this.retry > 0) {
         this.retry--;
         return false;
      } else if (this.mob.getTarget(this.headIndex) != null
         && TractorBeamHelper.isInsideTractorBeam(Objects.requireNonNull(this.mob.getTarget(this.headIndex)), this.mob, 4.0, this.headIndex)) {
         return false;
      } else if (this.mob.canBeDistracted(this.headIndex, WitherStormBase.DistractionType.STRUCTURES) && !this.mob.isDistracted(this.headIndex)) {
         BlockPos pos = this.findTargetPosition();
         if (pos != null) {
            for (int i = 0; i < this.mob.getTotalHeads(); i++) {
               Vec3 distractedPos = this.mob.getDistractedPos(i);
               if (distractedPos != null
                  && distractedPos.distanceToSqr((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5) < Math.pow(10.0, 2.0)
                  && this.mob.getRandom().nextInt(5) != 0) {
                  this.targetPos = null;
                  this.retry = 60;
                  return false;
               }
            }

            if (this.mob.isPosBehindBack(Vec3.atCenterOf(pos))) {
               return false;
            }
         }

         this.targetPos = pos;
         return this.targetPos != null;
      } else {
         return false;
      }
   }

   public void start() {
      this.mob.makeDistracted(Vec3.atCenterOf(this.targetPos), this.mob.getRandom().nextInt(60) + 120, this.headIndex);
   }

   private BlockPos findTargetPosition() {
      int scanRadius = (Integer)WitherStormModConfig.SERVER.tractorBeamBlockSearchRadius.get();
      Vec3 currentTarget = this.mob.getHeadPos(this.headIndex);
      Vec3 beamEnd = currentTarget.add(this.mob.getViewVector(this.mob.getHeadXRot(this.headIndex), this.mob.getHeadYRot(this.headIndex), 200.0F));
      BlockHitResult result = this.mob.level().clip(new ClipContext(currentTarget, beamEnd, Block.COLLIDER, Fluid.NONE, null));
      BlockPos hitPos = result.getBlockPos()
         .offset(this.mob.getRandom().nextIntBetweenInclusive(-4, 4), this.mob.getRandom().nextIntBetweenInclusive(-4, 4), this.mob.getRandom().nextIntBetweenInclusive(-4, 4));
      return WorldUtil.isLoaded((ServerLevel)this.mob.level(), hitPos)
         ? WorldUtil.forEachBlockSpiralOutwards(
            this.mob.level(), hitPos, scanRadius, p -> this.mob.level().getBlockState(p).is(WitherStormModBlockTags.TRACTOR_BEAM_DISTRACTION_BLOCKS)
         )
         : null;
   }
}
