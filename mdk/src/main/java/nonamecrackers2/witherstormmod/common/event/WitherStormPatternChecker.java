package nonamecrackers2.witherstormmod.common.event;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.pattern.BlockPattern.BlockPatternMatch;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.level.BlockEvent.EntityPlaceEvent;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.AdditionalHead;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;

public class WitherStormPatternChecker {
   private static BlockPattern WITHER_STORM_PATTERN;

   @SubscribeEvent
   public static void checkForWitherStormPattern(EntityPlaceEvent event) {
      if (event.getPlacedBlock().is(Blocks.WITHER_SKELETON_SKULL) || event.getPlacedBlock().is(Blocks.WITHER_SKELETON_WALL_SKULL)) {
         Level world = (Level)event.getLevel();
         if (event.getPos().getY() >= world.getMinBuildHeight() && world.getDifficulty() != Difficulty.PEACEFUL) {
            BlockPattern blockPattern = getOrCreateWitherStorm();
            BlockPatternMatch patternHelper = blockPattern.find(world, event.getPos());
            if (patternHelper != null) {
               for (int i = 0; i < blockPattern.getWidth(); i++) {
                  for (int j = 0; j < blockPattern.getHeight(); j++) {
                     BlockInWorld cachedBlockInfo = patternHelper.getBlock(i, j, 0);
                     world.setBlock(cachedBlockInfo.getPos(), Blocks.AIR.defaultBlockState(), 2);
                     world.levelEvent(2001, cachedBlockInfo.getPos(), Block.getId(cachedBlockInfo.getState()));
                  }
               }

               WitherStormEntity witherStorm = (WitherStormEntity)(WitherStormModEntityTypes.WITHER_STORM.get()).create(world);
               BlockPos blockPos = patternHelper.getBlock(1, 2, 0).getPos();
               Vec3 vector = new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.55, (double)blockPos.getZ() + 0.5);
               float rotation = patternHelper.getForwards().toYRot();
               witherStorm.moveTo(vector.x, vector.y, vector.z, rotation, 0.0F);
               witherStorm.yBodyRot = rotation;

               for (AdditionalHead head : witherStorm.getHeadManager().getOtherHeads()) {
                  head.setHeadYRot(rotation);
               }

               witherStorm.makeInvulnerable();
               witherStorm.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_ACTIVATES.get(), 4.0F, 1.0F);

               for (ServerPlayer player : world.getEntitiesOfClass(ServerPlayer.class, witherStorm.getBoundingBox().inflate(50.0))) {
                  CriteriaTriggers.SUMMONED_ENTITY.trigger(player, witherStorm);
               }

               for (int k = 0; k < blockPattern.getWidth(); k++) {
                  for (int l = 0; l < blockPattern.getHeight(); l++) {
                     world.blockUpdated(patternHelper.getBlock(k, l, 0).getPos(), Blocks.AIR);
                  }
               }

               world.addFreshEntity(witherStorm);
            }
         }
      }
   }

   private static BlockPattern getOrCreateWitherStorm() {
      if (WITHER_STORM_PATTERN == null) {
         WITHER_STORM_PATTERN = BlockPatternBuilder.start()
            .aisle(new String[]{"^^^", "#$#", "~#~"})
            .where('#', block -> block.getState().is(WitherStormModBlockTags.WITHER_STORM_SUMMON_BASE_BLOCKS))
            .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_SKULL).or(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_WALL_SKULL))))
            .where('~', BlockInWorld.hasState(BlockStateBase::isAir))
            .where('$', block -> block.getState().is(WitherStormModBlockTags.WITHER_STORM_SUMMON_COMMAND_BLOCKS))
            .build();
      }

      return WITHER_STORM_PATTERN;
   }
}
