package nonamecrackers2.witherstormmod.common.block;

import java.util.function.ToIntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class TaintedVeinBlock extends MultifaceBlock implements BonemealableBlock, SimpleWaterloggedBlock {
   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   private final MultifaceSpreader spreader = new MultifaceSpreader(this);

   public TaintedVeinBlock(Properties properties) {
      super(properties);
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(WATERLOGGED, false));
   }

   public static ToIntFunction<BlockState> emission(int lightLevel) {
      return state -> MultifaceBlock.hasAnyFace(state) ? lightLevel : 0;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> stateBuilder) {
      super.createBlockStateDefinition(stateBuilder);
      stateBuilder.add(new Property[]{WATERLOGGED});
   }

   public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor levelAccessor, BlockPos pos, BlockPos pos2) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         levelAccessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
      }

      return super.updateShape(state, direction, state2, levelAccessor, pos, pos2);
   }

   public boolean canBeReplaced(BlockState state, BlockPlaceContext placeContext) {
      return !placeContext.getItemInHand().is(Items.GLOW_LICHEN) || super.canBeReplaced(state, placeContext);
   }

   public boolean isValidBonemealTarget(LevelReader levelreader, BlockPos pos, BlockState state, boolean yeah) {
      return false;
   }

   public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos pos, BlockState state) {
      return false;
   }

   public void performBonemeal(ServerLevel level, RandomSource randomSource, BlockPos pos, BlockState state) {
      this.spreader.spreadFromRandomFaceTowardRandomDirection(state, level, pos, randomSource);
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }

   public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
      return state.getFluidState().isEmpty();
   }

   public MultifaceSpreader getSpreader() {
      return this.spreader;
   }
}
