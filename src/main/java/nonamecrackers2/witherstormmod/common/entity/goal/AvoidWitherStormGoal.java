package nonamecrackers2.witherstormmod.common.entity.goal;

import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class AvoidWitherStormGoal extends AvoidEntityGoal<WitherStormEntity> {
   private final float smallPhaseMaxDist;

   public AvoidWitherStormGoal(PathfinderMob mob, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
      super(mob, WitherStormEntity.class, maxDist, walkSpeedModifier, sprintSpeedModifier);
      this.smallPhaseMaxDist = maxDist / 8.0F;
   }

   public boolean canUse() {
      AABB box = this.mob.getBoundingBox().inflate((double)this.maxDist);
      List<WitherStormEntity> storms = this.mob.level().getEntitiesOfClass(WitherStormEntity.class, box, storm -> storm != this.mob && storm.isAlive());
      storms.sort(Comparator.comparingDouble(this.mob::distanceToSqr));
      this.toAvoid = storms.stream().findFirst().orElse(null);
      if (this.toAvoid != null
         && (!(Boolean)TractorBeamHelper.isInsideTractorBeam(this.mob, (WitherStormEntity)this.toAvoid, 4.0).getFirst() || this.mob.onGround())
         && WorldUtil.canSeeOrIsNotInASmallArea(this.toAvoid, this.mob)) {
         BlockPos portalPos = null;
         if ((Boolean)WitherStormModConfig.SERVER.mobsRunIntoPortals.get()) {
            portalPos = this.getNearestLoadedBlockPos(this.mob.position(), 16, Blocks.NETHER_PORTAL);
         }

         if ((
               ((WitherStormEntity)this.toAvoid).getPhase() >= 4
                  || (((WitherStormEntity)this.toAvoid).getPhase() == 2 || ((WitherStormEntity)this.toAvoid).getPhase() == 3)
                     && this.mob.getBoundingBox().inflate((double)this.smallPhaseMaxDist).contains(((WitherStormEntity)this.toAvoid).position())
            )
            && !((WitherStormEntity)this.toAvoid).isDeadOrPlayingDead()) {
            Vec3 vec3 = portalPos != null
               ? new Vec3((double)portalPos.getX() + 0.5, (double)(portalPos.getY() + 1), (double)portalPos.getZ() + 0.5)
               : this.mob.position().subtract(((WitherStormEntity)this.toAvoid).position()).normalize().scale(16.0).add(this.mob.position());
            if (this.path == null || this.path.isDone()) {
               this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
               this.mob.setTarget(null);
            }

            return this.path != null;
         }
      }

      return false;
   }

   public boolean canContinueToUse() {
      boolean flag = this.toAvoid == null
         || !(Boolean)TractorBeamHelper.isInsideTractorBeam(this.mob, (WitherStormEntity)this.toAvoid, 4.0).getFirst()
         || this.mob.onGround();
      return super.canContinueToUse() && flag;
   }

   public BlockPos getNearestLoadedBlockPos(Vec3 pos, int radius, Block block) {
      int minX = (int)pos.x - radius;
      int minY = (int)pos.y - 4;
      int minZ = (int)pos.z - radius;
      int maxX = (int)pos.x + radius;
      int maxY = (int)pos.y + 4;
      int maxZ = (int)pos.z + radius;

      for (BlockPos blockPos : BlockPos.betweenClosed(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ))) {
         BlockState blockState = this.mob.level().getBlockState(blockPos);
         if (blockState.getBlock() == block) {
            return blockPos;
         }
      }

      return null;
   }
}
