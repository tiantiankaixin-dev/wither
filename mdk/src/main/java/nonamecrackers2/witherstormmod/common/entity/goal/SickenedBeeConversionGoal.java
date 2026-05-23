package nonamecrackers2.witherstormmod.common.entity.goal;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import nonamecrackers2.witherstormmod.common.entity.SickenedBee;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;
import nonamecrackers2.witherstormmod.common.world.tainting.WorldTainting;

public class SickenedBeeConversionGoal extends Goal {
   private static final int COOLDOWN = 200;
   private final SickenedBee bee;
   private BlockPos targetPos;
   private int conversionTimer;
   private int conversionCooldown;

   public SickenedBeeConversionGoal(SickenedBee bee) {
      this.bee = bee;
      this.setFlags(EnumSet.of(Flag.MOVE));
   }

   public boolean canUse() {
      return this.findConvertibleBlock() && this.conversionCooldown <= 0;
   }

   public boolean canContinueToUse() {
      return this.targetPos != null
         && this.bee.distanceToSqr((double)this.targetPos.getX(), (double)this.targetPos.getY(), (double)this.targetPos.getZ()) < 6.0
         && this.conversionTimer > 0;
   }

   public void start() {
      this.bee.getNavigation().moveTo((double)this.targetPos.getX(), (double)this.targetPos.getY(), (double)this.targetPos.getZ(), 0.75);
      this.conversionTimer = 20;
   }

   public void stop() {
      this.targetPos = null;
      this.conversionTimer = 0;
   }

   public void tick() {
      if (this.conversionCooldown > 0) {
         this.conversionCooldown--;
      }

      if (this.targetPos != null) {
         BlockState targetBlockState = this.bee.level().getBlockState(this.targetPos);
         if (targetBlockState.is(WitherStormModBlockTags.SICKENED_BEE_CAN_CONVERT)
            && this.bee.distanceToSqr((double)this.targetPos.getX(), (double)this.targetPos.getY(), (double)this.targetPos.getZ()) <= 6.0) {
            this.conversionTimer--;
            if (this.conversionTimer <= 0) {
               this.taintBlock();
               this.stop();
               this.conversionCooldown = 200;
            }
         } else {
            this.stop();
         }
      }
   }

   private boolean findConvertibleBlock() {
      Level level = this.bee.level();
      BlockPos blockPos = this.bee.blockPosition();

      for (int x = -6; x <= 6; x++) {
         for (int y = -6; y <= 6; y++) {
            for (int z = -6; z <= 6; z++) {
               BlockPos pos = blockPos.offset(x, y, z);
               BlockState state = level.getBlockState(pos);
               if (state.is(WitherStormModBlockTags.SICKENED_BEE_CAN_CONVERT)) {
                  this.targetPos = pos;
                  return true;
               }
            }
         }
      }

      return false;
   }

   private void taintBlock() {
      if (this.targetPos != null) {
         WorldTainting.getInstance().convertBlock(this.targetPos, this.bee.level());
      }
   }
}
