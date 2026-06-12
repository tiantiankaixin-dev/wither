package nonamecrackers2.witherstormmod.common.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Plane;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import org.joml.Vector3f;

public class WireBlock extends Block {
   public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;
   public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;
   public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;
   public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;
   public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = Maps.newEnumMap(
      ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST)
   );
   private static final VoxelShape SHAPE_DOT = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);
   private static final Map<Direction, VoxelShape> SHAPES_FLOOR = Maps.newEnumMap(
      ImmutableMap.of(
         Direction.NORTH,
         Block.box(3.0, 0.0, 0.0, 13.0, 1.0, 13.0),
         Direction.SOUTH,
         Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 16.0),
         Direction.EAST,
         Block.box(3.0, 0.0, 3.0, 16.0, 1.0, 13.0),
         Direction.WEST,
         Block.box(0.0, 0.0, 3.0, 13.0, 1.0, 13.0)
      )
   );
   private static final Map<Direction, VoxelShape> SHAPES_UP = Maps.newEnumMap(
      ImmutableMap.of(
         Direction.NORTH,
         Shapes.or(SHAPES_FLOOR.get(Direction.NORTH), Block.box(3.0, 0.0, 0.0, 13.0, 16.0, 1.0)),
         Direction.SOUTH,
         Shapes.or(SHAPES_FLOOR.get(Direction.SOUTH), Block.box(3.0, 0.0, 15.0, 13.0, 16.0, 16.0)),
         Direction.EAST,
         Shapes.or(SHAPES_FLOOR.get(Direction.EAST), Block.box(15.0, 0.0, 3.0, 16.0, 16.0, 13.0)),
         Direction.WEST,
         Shapes.or(SHAPES_FLOOR.get(Direction.WEST), Block.box(0.0, 0.0, 3.0, 1.0, 16.0, 13.0))
      )
   );
   private final Map<BlockState, VoxelShape> shapesCache = Maps.newHashMap();
   protected final Vector3f color;

   public WireBlock(float r, float g, float b, Properties properties) {
      super(properties);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(NORTH, RedstoneSide.NONE))
                  .setValue(EAST, RedstoneSide.NONE))
               .setValue(SOUTH, RedstoneSide.NONE))
            .setValue(WEST, RedstoneSide.NONE)
      );
      UnmodifiableIterator var5 = this.getStateDefinition().getPossibleStates().iterator();

      while (var5.hasNext()) {
         BlockState state = (BlockState)var5.next();
         this.shapesCache.put(state, this.calculateShape(state));
      }

      this.color = new Vector3f(r, g, b);
   }

   protected VoxelShape calculateShape(BlockState state) {
      VoxelShape voxelshape = SHAPE_DOT;

      for (Direction direction : Plane.HORIZONTAL) {
         RedstoneSide redstoneside = (RedstoneSide)state.get((Property)PROPERTY_BY_DIRECTION.get(direction));
         if (redstoneside == RedstoneSide.SIDE) {
            voxelshape = Shapes.or(voxelshape, SHAPES_FLOOR.get(direction));
         } else if (redstoneside == RedstoneSide.UP) {
            voxelshape = Shapes.or(voxelshape, SHAPES_UP.get(direction));
         }
      }

      return voxelshape;
   }

   public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
      return this.shapesCache.get(state);
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return this.getConnectionState(context.getLevel(), this.defaultBlockState(), context.getClickedPos());
   }

   protected BlockState getConnectionState(BlockGetter reader, BlockState state, BlockPos pos) {
      boolean flag = isDot(state);
      state = this.getMissingConnections(reader, this.defaultBlockState(), pos);
      if (flag && isDot(state)) {
         return state;
      } else {
         boolean flag1 = ((RedstoneSide)state.get(NORTH)).isConnected();
         boolean flag2 = ((RedstoneSide)state.get(SOUTH)).isConnected();
         boolean flag3 = ((RedstoneSide)state.get(EAST)).isConnected();
         boolean flag4 = ((RedstoneSide)state.get(WEST)).isConnected();
         boolean flag5 = !flag1 && !flag2;
         boolean flag6 = !flag3 && !flag4;
         if (!flag4 && flag5) {
            state = (BlockState)state.setValue(WEST, RedstoneSide.SIDE);
         }

         if (!flag3 && flag5) {
            state = (BlockState)state.setValue(EAST, RedstoneSide.SIDE);
         }

         if (!flag1 && flag6) {
            state = (BlockState)state.setValue(NORTH, RedstoneSide.SIDE);
         }

         if (!flag2 && flag6) {
            state = (BlockState)state.setValue(SOUTH, RedstoneSide.SIDE);
         }

         return state;
      }
   }

   protected BlockState getMissingConnections(BlockGetter reader, BlockState state, BlockPos pos) {
      boolean flag = !reader.getBlockState(pos.above()).isRedstoneConductor(reader, pos);

      for (Direction direction : Plane.HORIZONTAL) {
         if (!((RedstoneSide)state.get((Property)PROPERTY_BY_DIRECTION.get(direction))).isConnected()) {
            RedstoneSide redstoneside = this.getConnectingSide(reader, pos, direction, flag);
            state = (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(direction), redstoneside);
         }
      }

      return state;
   }

   public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor world, BlockPos pos, BlockPos pos1) {
      if (direction == Direction.DOWN) {
         return state;
      } else if (direction == Direction.UP) {
         return this.getConnectionState(world, state, pos);
      } else {
         RedstoneSide redstoneside = this.getConnectingSide(world, pos, direction);
         return redstoneside.isConnected() == ((RedstoneSide)state.get((Property)PROPERTY_BY_DIRECTION.get(direction))).isConnected() && !isCross(state)
            ? (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(direction), redstoneside)
            : this.getConnectionState(world, (BlockState)this.defaultBlockState().setValue((Property)PROPERTY_BY_DIRECTION.get(direction), redstoneside), pos);
      }
   }

   protected static boolean isCross(BlockState state) {
      return ((RedstoneSide)state.get(NORTH)).isConnected()
         && ((RedstoneSide)state.get(SOUTH)).isConnected()
         && ((RedstoneSide)state.get(EAST)).isConnected()
         && ((RedstoneSide)state.get(WEST)).isConnected();
   }

   protected static boolean isDot(BlockState state) {
      return !((RedstoneSide)state.get(NORTH)).isConnected()
         && !((RedstoneSide)state.get(SOUTH)).isConnected()
         && !((RedstoneSide)state.get(EAST)).isConnected()
         && !((RedstoneSide)state.get(WEST)).isConnected();
   }

   public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor world, BlockPos pos, int p_196248_4_, int p_196248_5_) {
      MutableBlockPos blockpos$mutable = new MutableBlockPos();

      for (Direction direction : Plane.HORIZONTAL) {
         RedstoneSide redstoneside = (RedstoneSide)state.get((Property)PROPERTY_BY_DIRECTION.get(direction));
         if (redstoneside != RedstoneSide.NONE && !world.getBlockState(blockpos$mutable.setWithOffset(pos, direction)).is(this)) {
            blockpos$mutable.move(Direction.DOWN);
            BlockState blockstate = world.getBlockState(blockpos$mutable);
            if (!blockstate.is(Blocks.OBSERVER)) {
               BlockPos blockpos = blockpos$mutable.relative(direction.getOpposite());
               BlockState blockstate1 = blockstate.updateShape(direction.getOpposite(), world.getBlockState(blockpos), world, blockpos$mutable, blockpos);
               updateOrDestroy(blockstate, blockstate1, world, blockpos$mutable, p_196248_4_, p_196248_5_);
            }

            blockpos$mutable.setWithOffset(pos, direction).move(Direction.UP);
            BlockState blockstate3 = world.getBlockState(blockpos$mutable);
            if (!blockstate3.is(Blocks.OBSERVER)) {
               BlockPos blockpos1 = blockpos$mutable.relative(direction.getOpposite());
               BlockState blockstate2 = blockstate3.updateShape(direction.getOpposite(), world.getBlockState(blockpos1), world, blockpos$mutable, blockpos1);
               updateOrDestroy(blockstate3, blockstate2, world, blockpos$mutable, p_196248_4_, p_196248_5_);
            }
         }
      }
   }

   protected RedstoneSide getConnectingSide(BlockGetter reader, BlockPos pos, Direction direction) {
      return this.getConnectingSide(reader, pos, direction, !reader.getBlockState(pos.above()).isRedstoneConductor(reader, pos));
   }

   protected RedstoneSide getConnectingSide(BlockGetter reader, BlockPos pos, Direction direction, boolean upCheck) {
      BlockPos blockpos = pos.relative(direction);
      BlockState blockstate = reader.getBlockState(blockpos);
      if (upCheck) {
         boolean flag = this.canSurviveOn(reader, blockpos, blockstate);
         if (flag && canConnectTo(reader.getBlockState(blockpos.above()), reader, blockpos.above(), null)) {
            if (blockstate.isFaceSturdy(reader, blockpos, direction.getOpposite())) {
               return RedstoneSide.UP;
            }

            return RedstoneSide.SIDE;
         }
      }

      return canConnectTo(blockstate, reader, blockpos, direction)
            || !blockstate.isRedstoneConductor(reader, blockpos) && canConnectTo(reader.getBlockState(blockpos.below()), reader, blockpos.below(), null)
         ? RedstoneSide.SIDE
         : RedstoneSide.NONE;
   }

   public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
      BlockPos blockpos = pos.below();
      BlockState blockstate = reader.getBlockState(blockpos);
      return this.canSurviveOn(reader, blockpos, blockstate);
   }

   protected boolean canSurviveOn(BlockGetter reader, BlockPos pos, BlockState state) {
      return state.isFaceSturdy(reader, pos, Direction.UP) || state.is(Blocks.HOPPER);
   }

   protected void checkCornerChangeAt(Level world, BlockPos pos) {
      if (world.getBlockState(pos).is(this)) {
         world.updateNeighborsAt(pos, this);

         for (Direction direction : Direction.values()) {
            world.updateNeighborsAt(pos.relative(direction), this);
         }
      }
   }

   public void onPlace(BlockState state, Level world, BlockPos pos, BlockState state1, boolean flag) {
      if (!state1.is(state.getBlock()) && !world.isClientSide) {
         for (Direction direction : Plane.VERTICAL) {
            world.updateNeighborsAt(pos.relative(direction), this);
         }

         this.updateNeighborsOfNeighboringWires(world, pos);
      }
   }

   public void onRemove(BlockState state, Level world, BlockPos pos, BlockState state1, boolean flag) {
      if (!flag && !state.is(state1.getBlock())) {
         super.onRemove(state, world, pos, state1, flag);
         if (!world.isClientSide) {
            for (Direction direction : Direction.values()) {
               world.updateNeighborsAt(pos.relative(direction), this);
            }

            this.updateNeighborsOfNeighboringWires(world, pos);
         }
      }
   }

   protected void updateNeighborsOfNeighboringWires(Level world, BlockPos pos) {
      for (Direction direction : Plane.HORIZONTAL) {
         this.checkCornerChangeAt(world, pos.relative(direction));
      }

      for (Direction direction1 : Plane.HORIZONTAL) {
         BlockPos blockpos = pos.relative(direction1);
         if (world.getBlockState(blockpos).isRedstoneConductor(world, blockpos)) {
            this.checkCornerChangeAt(world, blockpos.above());
         } else {
            this.checkCornerChangeAt(world, blockpos.below());
         }
      }
   }

   public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos pos1, boolean flag) {
      if (!world.isClientSide && !state.canSurvive(world, pos)) {
         dropResources(state, world, pos);
         world.removeBlock(pos, false);
      }
   }

   protected static boolean canConnectTo(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction direction) {
      return state.is((Block)WitherStormModBlocks.TAINTED_DUST.get());
   }

   protected void spawnParticlesAlongLine(
      Level world, RandomSource random, BlockPos pos, Vector3f colors, Direction direction, Direction direction1, float f3, float f4
   ) {
      float f = f4 - f3;
      if (!(random.nextFloat() >= 0.2F * f)) {
         float f1 = 0.4375F;
         float f2 = f3 + f * random.nextFloat();
         double d0 = 0.5 + (double)(f1 * (float)direction.getStepX()) + (double)(f2 * (float)direction1.getStepX());
         double d1 = 0.5 + (double)(f1 * (float)direction.getStepY()) + (double)(f2 * (float)direction1.getStepY());
         double d2 = 0.5 + (double)(f1 * (float)direction.getStepZ()) + (double)(f2 * (float)direction1.getStepZ());
         world.addParticle(
            new DustParticleOptions(new Vector3f(colors.x(), colors.y(), colors.z()), 1.0F),
            (double)pos.getX() + d0,
            (double)pos.getY() + d1,
            (double)pos.getZ() + d2,
            0.0,
            0.0,
            0.0
         );
      }
   }

   public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
      for (Direction direction : Plane.HORIZONTAL) {
         RedstoneSide redstoneside = (RedstoneSide)state.get((Property)PROPERTY_BY_DIRECTION.get(direction));
         switch (redstoneside) {
            case UP:
               this.spawnParticlesAlongLine(world, random, pos, this.color, direction, Direction.UP, -0.5F, 0.5F);
            case SIDE:
               this.spawnParticlesAlongLine(world, random, pos, this.color, Direction.DOWN, direction, 0.0F, 0.5F);
               break;
            case NONE:
            default:
               this.spawnParticlesAlongLine(world, random, pos, this.color, Direction.DOWN, direction, 0.0F, 0.3F);
         }
      }
   }

   public BlockState rotate(BlockState state, Rotation rotation) {
      switch (rotation) {
         case CLOCKWISE_180:
            return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (RedstoneSide)state.get(SOUTH)))
                     .setValue(EAST, (RedstoneSide)state.get(WEST)))
                  .setValue(SOUTH, (RedstoneSide)state.get(NORTH)))
               .setValue(WEST, (RedstoneSide)state.get(EAST));
         case COUNTERCLOCKWISE_90:
            return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (RedstoneSide)state.get(EAST)))
                     .setValue(EAST, (RedstoneSide)state.get(SOUTH)))
                  .setValue(SOUTH, (RedstoneSide)state.get(WEST)))
               .setValue(WEST, (RedstoneSide)state.get(NORTH));
         case CLOCKWISE_90:
            return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (RedstoneSide)state.get(WEST)))
                     .setValue(EAST, (RedstoneSide)state.get(NORTH)))
                  .setValue(SOUTH, (RedstoneSide)state.get(EAST)))
               .setValue(WEST, (RedstoneSide)state.get(SOUTH));
         default:
            return state;
      }
   }

   public BlockState mirror(BlockState state, Mirror mirror) {
      switch (mirror) {
         case LEFT_RIGHT:
            return (BlockState)((BlockState)state.setValue(NORTH, (RedstoneSide)state.get(SOUTH))).setValue(SOUTH, (RedstoneSide)state.get(NORTH));
         case FRONT_BACK:
            return (BlockState)((BlockState)state.setValue(EAST, (RedstoneSide)state.get(WEST))).setValue(WEST, (RedstoneSide)state.get(EAST));
         default:
            return super.mirror(state, mirror);
      }
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{NORTH, EAST, SOUTH, WEST});
   }

   public Vector3f getColor() {
      return this.color;
   }
}
