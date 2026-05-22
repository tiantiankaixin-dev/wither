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
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;

public class SmallClusterSource extends BlockClusterSource {
   public SmallClusterSource() {
      super(1024);
   }

   @Override
   protected void createCluster(WitherStormEntity storm) {
      int phase = storm.getPhase();

      int multiClusters = switch (phase) {
         case 4 -> 4;
         case 5 -> 5;
         case 6 -> 6;
         case 7 -> 8;
         default -> 1;
      };

      for (int i = 0; i < multiClusters; i++) {
         super.createCluster(storm);
      }
   }

   @Override
   protected float getClusterSizeRadius(WitherStormEntity storm) {
      return 1.0F;
   }

   @Override
   protected int getClusterSearchRadius(WitherStormEntity storm) {
      return storm.getEntityConsumptionRadius() * storm.phaseRadiusMultiplier(storm.getPhase());
   }

   @Override
   protected int calculateShakeTime(WitherStormEntity storm, RandomSource random) {
      return 0;
   }

   @Override
   protected boolean isValidClusterBlock(WitherStormEntity storm, BlockState state) {
      return !state.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST) && !state.is(WitherStormModBlockTags.SMALL_CLUSTER_BLACKLIST);
   }

   @Override
   protected boolean isInvalidInitialStartBlock(WitherStormEntity storm, BlockState state) {
      return state.is(WitherStormModBlockTags.LESS_FAVORABLE_BLOCKS) && storm.getRandom().nextDouble() <= 0.9;
   }

   @Override
   protected int getPickupInterval(WitherStormEntity storm) {
      int phase = storm.getPhase();
      if ((Boolean)WitherStormModConfig.SERVER.constantBlackhole.get()) {
         return 6;
      } else {
         return switch (phase) {
            case 0, 1, 2, 3 -> 64;
            case 4 -> 30;
            case 5 -> 25;
            case 6 -> 20;
            case 7 -> 15;
            default -> 100;
         };
      }
   }

   @Override
   protected void onClusterAddedToWorld(WitherStormEntity storm, BlockClusterEntity cluster, BlockPos startPos, BlockState startState) {
      SoundType sound = startState.getSoundType(storm.level(), startPos, null);
      storm.level().playSound(null, startPos, sound.getBreakSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
   }

   @Override
   protected boolean canUse(WitherStormEntity storm) {
      return !(storm instanceof WitherStormSegmentEntity);
   }
}
