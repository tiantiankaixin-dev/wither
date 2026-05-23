package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.clustersource;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.clustersource.BlockClusterSource;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class HunchbackClusterSource extends BlockClusterSource {
   public HunchbackClusterSource() {
      super(256);
   }

   @Override
   protected boolean shouldScanUpwards(WitherStormEntity storm) {
      if (storm.getRandom().nextInt(2) == 0) {
         int x = storm.getBlockX();
         int z = storm.getBlockZ();
         int height = WorldUtil.getCeilingStartingAt(storm.level(), storm.getBlockY(), x, z);
         BlockPos pos = new BlockPos(x, height, z);
         return !storm.level().getBlockState(pos).isAir();
      } else {
         return false;
      }
   }

   @Override
   protected void createCluster(WitherStormEntity storm) {
      int multiClusters = switch (storm.getPhase()) {
         case 1 -> 3;
         case 2 -> 9;
         case 3 -> 18;
         default -> 1;
      };

      for (int i = 0; i < multiClusters; i++) {
         super.createCluster(storm);
      }
   }

   @Override
   protected int calculateShakeTime(WitherStormEntity storm, RandomSource random) {
      if (storm.getConsumedEntities() >= storm.adjustAmountForEvolutionSpeed(15000)) {
         return 0;
      } else {
         return storm.getConsumedEntities() >= storm.adjustAmountForEvolutionSpeed(10000) ? random.nextInt(10) : random.nextInt(40);
      }
   }

   @Override
   protected float getClusterSizeRadius(WitherStormEntity storm) {
      return 1.0F;
   }

   @Override
   protected boolean isInvalidInitialStartBlock(WitherStormEntity storm, BlockState state) {
      return state.is(WitherStormModBlockTags.LESS_FAVORABLE_BLOCKS_HUNCH) && storm.getRandom().nextDouble() <= 0.995 && storm.getPhase() == 3;
   }

   @Override
   protected int getClusterSearchRadius(WitherStormEntity storm) {
      return storm.entityConsumptionRadiusHunch();
   }

   @Override
   protected int getPickupInterval(WitherStormEntity storm) {
      return WitherStormModConfig.SERVER.constantBlackhole.get() ? 1 : Math.max(1, 60 - Math.round((float)storm.getConsumedEntities() * 0.00375F));
   }

   @Override
   protected boolean canUse(WitherStormEntity storm) {
      return storm.getPhase() <= 3;
   }

   @Override
   protected void onClusterAddedToWorld(WitherStormEntity storm, BlockClusterEntity cluster, BlockPos startPos, BlockState startState) {
      SoundType sound = startState.getSoundType(storm.level(), startPos, null);
      storm.level().playSound(null, startPos, sound.getBreakSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
   }
}
