package nonamecrackers2.witherstormmod.common.block;

import com.mojang.serialization.MapCodec;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.pattern.BlockPattern.BlockPatternMatch;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import nonamecrackers2.witherstormmod.common.entity.SickenedIronGolem;
import nonamecrackers2.witherstormmod.common.entity.SickenedSnowGolem;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;

public class TaintedCarvedPumpkinBlock extends HorizontalDirectionalBlock implements Equipable {
   public static final MapCodec<TaintedCarvedPumpkinBlock> CODEC = simpleCodec(TaintedCarvedPumpkinBlock::new);
   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
   @Nullable
   private BlockPattern taintedSnowGolemBase;
   @Nullable
   private BlockPattern taintedSnowGolemFull;
   @Nullable
   private BlockPattern taintedIronGolemBase;
   @Nullable
   private BlockPattern taintedIronGolemFull;
   private static final Predicate<BlockState> PUMPKINS_PREDICATE = state -> state != null
         && (
            state.is((Block)WitherStormModBlocks.TAINTED_CARVED_PUMPKIN.get())
               || state.is((Block)WitherStormModBlocks.TAINTED_JACK_O_LANTERN.get())
         );

   public TaintedCarvedPumpkinBlock(Properties properties) {
      super(properties);
      this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
   }

   @Override
   protected MapCodec<TaintedCarvedPumpkinBlock> codec() {
      return CODEC;
   }

   public boolean canSpawnGolem(LevelReader level, BlockPos pos) {
      return this.getOrCreateTaintedIronGolemBase().find(level, pos) != null || this.getOrCreateTaintedSnowGolemBase().find(level, pos) != null;
   }

   private void trySpawnGolem(Level level, BlockPos pos) {
      BlockPatternMatch snowmatch = this.getOrCreateTaintedSnowGolemFull().find(level, pos);
      if (snowmatch != null) {
         SickenedSnowGolem golem = (SickenedSnowGolem)(WitherStormModEntityTypes.SICKENED_SNOW_GOLEM.get()).create(level);
         if (golem != null) {
            BlockPos spawnPos = snowmatch.getBlock(0, 2, 0).getPos();
            CarvedPumpkinBlock.clearPatternBlocks(level, snowmatch);
            golem.moveTo((double)spawnPos.getX() + 0.5, (double)spawnPos.getY() + 0.05, (double)spawnPos.getZ() + 0.5, 0.0F, 0.0F);
            level.addFreshEntity(golem);
            CarvedPumpkinBlock.updatePatternBlocks(level, snowmatch);
         }
      } else {
         BlockPatternMatch ironmatch = this.getOrCreateTaintedIronGolemFull().find(level, pos);
         if (ironmatch != null) {
            SickenedIronGolem golem = (SickenedIronGolem)(WitherStormModEntityTypes.SICKENED_IRON_GOLEM.get()).create(level);
            if (golem != null) {
               BlockPos spawnPos = ironmatch.getBlock(1, 2, 0).getPos();
               CarvedPumpkinBlock.clearPatternBlocks(level, ironmatch);
               golem.moveTo((double)spawnPos.getX() + 0.5, (double)spawnPos.getY() + 0.05, (double)spawnPos.getZ() + 0.5, 0.0F, 0.0F);
               level.addFreshEntity(golem);

               for (ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, golem.getBoundingBox().inflate(5.0))) {
                  CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, golem);
               }

               CarvedPumpkinBlock.updatePatternBlocks(level, ironmatch);
            }
         }
      }
   }

   public EquipmentSlot getEquipmentSlot() {
      return EquipmentSlot.HEAD;
   }

   public void onPlace(BlockState state, Level level, BlockPos pos, BlockState other, boolean flag) {
      if (!state.is(other.getBlock())) {
         this.trySpawnGolem(level, pos);
      }
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }

   private BlockPattern getOrCreateTaintedSnowGolemBase() {
      if (this.taintedSnowGolemBase == null) {
         this.taintedSnowGolemBase = BlockPatternBuilder.start()
            .aisle(new String[]{" ", "#", "#"})
            .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK)))
            .build();
      }

      return this.taintedSnowGolemBase;
   }

   private BlockPattern getOrCreateTaintedSnowGolemFull() {
      if (this.taintedSnowGolemFull == null) {
         this.taintedSnowGolemFull = BlockPatternBuilder.start()
            .aisle(new String[]{"^", "#", "#"})
            .where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE))
            .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK)))
            .build();
      }

      return this.taintedSnowGolemFull;
   }

   private BlockPattern getOrCreateTaintedIronGolemBase() {
      if (this.taintedIronGolemBase == null) {
         this.taintedIronGolemBase = BlockPatternBuilder.start()
            .aisle(new String[]{"~ ~", "###", "~#~"})
            .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK)))
            .where('~', BlockInWorld.hasState(BlockStateBase::isAir))
            .build();
      }

      return this.taintedIronGolemBase;
   }

   private BlockPattern getOrCreateTaintedIronGolemFull() {
      if (this.taintedIronGolemFull == null) {
         this.taintedIronGolemFull = BlockPatternBuilder.start()
            .aisle(new String[]{"~^~", "###", "~#~"})
            .where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE))
            .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK)))
            .where('~', BlockInWorld.hasState(BlockStateBase::isAir))
            .build();
      }

      return this.taintedIronGolemFull;
   }
}
