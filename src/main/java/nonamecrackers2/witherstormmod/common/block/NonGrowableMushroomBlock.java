package nonamecrackers2.witherstormmod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NonGrowableMushroomBlock extends BushBlock {
   protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);

   public NonGrowableMushroomBlock(Properties properties) {
      super(properties);
   }

   protected boolean mayPlaceOn(BlockState state, BlockGetter reader, BlockPos pos) {
      return state.isSolidRender(reader, pos);
   }

   public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
      BlockPos blockpos = pos.below();
      BlockState blockstate = reader.getBlockState(blockpos);
      return blockstate.is(BlockTags.MUSHROOM_GROW_BLOCK) ? true : reader.getRawBrightness(pos, 0) < 13 && blockstate.canSustainPlant(reader, blockpos, Direction.UP, this);
   }

   public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
      return SHAPE;
   }
}
