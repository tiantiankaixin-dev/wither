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

public class NatureClusterSource extends BlockClusterSource {
   public NatureClusterSource() {
      super(256);
   }

   @Override
   protected boolean shouldntCountToConsumedEntities(WitherStormEntity storm) {
      return true;
   }

   @Override
   protected void createCluster(WitherStormEntity storm) {
      int phase = storm.getPhase();

      int multiClusters = switch (phase) {
         case 0 -> 4;
         case 1 -> 6;
         case 2 -> 8;
         default -> 10;
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
      return storm.getPhase() <= 3
         ? storm.entityConsumptionRadiusHunch() + 12
         : storm.getEntityConsumptionRadius() * storm.phaseRadiusMultiplierNature(storm.getPhase());
   }

   @Override
   protected int calculateShakeTime(WitherStormEntity storm, RandomSource random) {
      int phase = storm.getPhase();

      return switch (phase) {
         case 0 -> 20 + storm.getRandom().nextInt(10);
         case 1 -> 15 + storm.getRandom().nextInt(10);
         case 2 -> 10 + storm.getRandom().nextInt(10);
         case 3 -> 5 + storm.getRandom().nextInt(5);
         default -> 0;
      };
   }

   @Override
   protected boolean isValidClusterBlock(WitherStormEntity storm, BlockState state) {
      return !state.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST) && state.is(WitherStormModBlockTags.NATURE_CLUSTER_WHITELIST);
   }

   @Override
   protected int getPickupInterval(WitherStormEntity storm) {
      int phase = storm.getPhase();
      if ((Boolean)WitherStormModConfig.SERVER.constantBlackhole.get()) {
         return 1;
      } else {
         return switch (phase) {
            case 0 -> 60;
            case 1 -> 40;
            case 2 -> 20;
            case 3 -> 15;
            case 4 -> 30;
            case 5 -> 24;
            case 6 -> 16;
            case 7 -> 12;
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
