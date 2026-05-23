package nonamecrackers2.witherstormmod.api.common.ai.witherstorm.clustersource;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.event.EventHooks;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public abstract class BlockClusterSource {
   protected final int maximumCreationAttempts;

   public BlockClusterSource(int maximumCreationAttempts) {
      this.maximumCreationAttempts = maximumCreationAttempts;
   }

   public void tick(WitherStormEntity storm) {
      if (EventHooks.getMobGriefingEvent(storm.level(), storm) && this.canUse(storm) && storm.tickCount % this.getPickupInterval(storm) == 0) {
         this.createCluster(storm);
      }
   }

   protected void createCluster(WitherStormEntity storm) {
      this.createClusterNearby(
         storm,
         this.searchCenter(storm),
         this.getClusterSizeRadius(storm),
         this.getClusterSearchRadius(storm),
         this.calculateShakeTime(storm, storm.getRandom()),
         this.maximumCreationAttempts,
         this.calculateRotationDelta(storm, storm.getRandom()),
         this.shouldScanUpwards(storm),
         this.shouldntCountToConsumedEntities(storm)
      );
   }

   protected BlockPos searchCenter(WitherStormEntity storm) {
      int flooredX = Mth.floor(storm.getX());
      int flooredY = Math.min(storm.level().getMaxBuildHeight() - 1, Mth.floor(storm.getEyeY() + 1.0));
      int flooredZ = Mth.floor(storm.getZ());
      return new BlockPos(flooredX, flooredY, flooredZ);
   }

   protected abstract float getClusterSizeRadius(WitherStormEntity var1);

   protected int getClusterSearchRadius(WitherStormEntity storm) {
      return storm.getEntityConsumptionRadius();
   }

   protected abstract int calculateShakeTime(WitherStormEntity var1, RandomSource var2);

   protected Vec2 calculateRotationDelta(WitherStormEntity storm, RandomSource random) {
      return storm.getConsumedEntities() < storm.adjustAmountForEvolutionSpeed(10000)
         ? new Vec2((float)(random.nextInt(20) - 10) * 0.125F, (float)(random.nextInt(20) - 10) * 0.125F)
         : new Vec2((float)(random.nextInt(20) - 10) * 0.75F, (float)(random.nextInt(20) - 10) * 0.75F);
   }

   protected boolean isInvalidInitialStartBlock(WitherStormEntity storm, BlockState state) {
      return false;
   }

   protected boolean isValidClusterBlock(WitherStormEntity storm, BlockState state) {
      return !state.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST);
   }

   protected abstract int getPickupInterval(WitherStormEntity var1);

   protected abstract boolean canUse(WitherStormEntity var1);

   protected boolean shouldScanUpwards(WitherStormEntity storm) {
      return false;
   }

   protected boolean shouldntCountToConsumedEntities(WitherStormEntity storm) {
      return false;
   }

   protected boolean shouldPullBlocksWithRandomYOffset(WitherStormEntity storm, float clusterSizeRadius) {
      return clusterSizeRadius <= 1.0F;
   }

   protected void onClusterAddedToWorld(WitherStormEntity storm, BlockClusterEntity cluster, BlockPos startPos, BlockState startState) {
   }

   protected void createClusterNearby(
      WitherStormEntity storm,
      BlockPos searchCenter,
      float clusterSizeRadius,
      int radius,
      int shakeTime,
      int maxAttempts,
      Vec2 rotationDelta,
      boolean scanUpwards,
      boolean countToConsumedEntities
   ) {
      for (int i = 0; i < maxAttempts; i++) {
         int randomX = storm.getRandom().nextInt(radius * 2) - radius;
         int randomZ = storm.getRandom().nextInt(radius * 2) - radius;
         double distance = Math.sqrt((double)(randomX * randomX + randomZ * randomZ));
         if (distance < (double)radius) {
            BlockPos blockToCollect = new BlockPos(searchCenter.getX() + randomX, searchCenter.getY(), searchCenter.getZ() + randomZ);
            if (!WorldUtil.isLoaded((ServerLevel)storm.level(), blockToCollect)) {
               break;
            }

            BlockState blockState;
            for (blockState = storm.level().getBlockState(blockToCollect);
               blockToCollect.getY() > storm.level().getMinBuildHeight()
                  && blockToCollect.getY() < storm.level().getMaxBuildHeight()
                  && (blockState.is(Blocks.AIR) || blockState.is(Blocks.WATER));
               blockState = storm.level().getBlockState(blockToCollect)
            ) {
               blockToCollect = scanUpwards ? blockToCollect.above() : blockToCollect.below();
            }

            if (!scanUpwards && this.shouldPullBlocksWithRandomYOffset(storm, clusterSizeRadius)) {
               BlockPos originalBlockToCollect = blockToCollect;
               BlockState originalBlockState = blockState;
               int minY = blockToCollect.getY();
               int maxY = Math.max(storm.level().getMinBuildHeight(), minY - 10);
               int randomY = storm.getRandom().nextInt(minY - maxY + 1) + maxY;
               blockToCollect = new BlockPos(blockToCollect.getX(), randomY, blockToCollect.getZ());
               blockState = storm.level().getBlockState(blockToCollect);
               if (blockState.isAir() || blockState.is(Blocks.WATER) || blockState.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST)) {
                  blockToCollect = originalBlockToCollect;
                  blockState = originalBlockState;
               }
            }

            if (!this.isInvalidInitialStartBlock(storm, blockState)
               && WorldUtil.isBlockExposed(storm.level(), blockToCollect)
               && storm.level()
                  .getEntitiesOfClass(WitheredSymbiontEntity.class, new AABB(blockToCollect).inflate(15.0))
                  .stream()
                  .filter(LivingEntity::isAlive)
                  .findFirst()
                  .isEmpty()) {
               BlockClusterEntity clusterEntity = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(storm.level());

               assert clusterEntity != null;

               clusterEntity.populateWithRadius(blockToCollect, clusterSizeRadius, state -> this.isValidClusterBlock(storm, state));
               if (clusterEntity.getSize() > 0) {
                  if (clusterEntity.getSize() >= 55 && storm.getRandom().nextInt(3) == 0) {
                     clusterEntity.setShouldCrumble(true);
                  }

                  clusterEntity.setTime(50);
                  clusterEntity.setShakeTime(shakeTime);
                  if (clusterEntity.getSize() >= 2) {
                     clusterEntity.playSound(
                        WitherStormModSoundEvents.BLOCK_CLUSTER_SHAKE.get(),
                        2.0F,
                        (storm.getRandom().nextFloat() - storm.getRandom().nextFloat()) * 0.2F + 1.0F
                     );
                  }

                  storm.getTrackedEntities().trackEntityToConsume(clusterEntity);
                  clusterEntity.setRotationDelta(rotationDelta);
                  if (storm.getRandom().nextBoolean()) {
                     clusterEntity.addTag("RotateClockwise");
                  }

                  clusterEntity.setNoGravity(true);
                  clusterEntity.setPhysics(false);
                  clusterEntity.setShouldntCountToConsumedEntities(countToConsumedEntities);
                  this.onClusterAddedToWorld(storm, clusterEntity, blockToCollect, blockState);
                  storm.level().addFreshEntity(clusterEntity);
                  break;
               }
            }
         }
      }
   }
}
