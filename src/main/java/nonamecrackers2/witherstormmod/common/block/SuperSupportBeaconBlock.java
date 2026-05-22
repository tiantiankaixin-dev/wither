package nonamecrackers2.witherstormmod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nonamecrackers2.witherstormmod.common.blockentity.AbstractSuperBeaconBlockEntity;
import nonamecrackers2.witherstormmod.common.blockentity.SuperBeaconBlockEntity;
import nonamecrackers2.witherstormmod.common.blockentity.SuperSupportBeaconBlockEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlockEntityTypes;

public class SuperSupportBeaconBlock extends AbstractSuperBeaconBlock {
   private static final VoxelShape SHAPE = Shapes.join(
      Block.box(3.0, 4.0, 3.0, 13.0, 14.0, 13.0),
      Shapes.join(Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0), Block.box(2.0, 3.0, 2.0, 14.0, 4.0, 14.0), BooleanOp.OR),
      BooleanOp.OR
   );

   public SuperSupportBeaconBlock(Properties properties) {
      super(properties);
   }

   public AbstractSuperBeaconBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new SuperSupportBeaconBlockEntity(pos, state);
   }

   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
      return createBeaconTicker(
         level, type, (BlockEntityType<? extends AbstractSuperBeaconBlockEntity>)WitherStormModBlockEntityTypes.SUPER_SUPPORT_BEACON.get()
      );
   }

   public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
      return SHAPE;
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
      if (level.isClientSide) {
         return InteractionResult.SUCCESS;
      } else {
         if (level.getBlockEntity(pos) instanceof SuperSupportBeaconBlockEntity beacon) {
            SuperBeaconBlockEntity connected = beacon.getConnectedBeaconEntity();
            if (connected == null || !connected.isDoingResummonAnimation()) {
               player.openMenu(beacon);
            }
         }

         return InteractionResult.CONSUME;
      }
   }
}
