package nonamecrackers2.witherstormmod.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
   public static final MapCodec<SuperSupportBeaconBlock> CODEC = simpleCodec(SuperSupportBeaconBlock::new);
   private static final VoxelShape SHAPE = Shapes.join(
      Block.box(3.0, 4.0, 3.0, 13.0, 14.0, 13.0),
      Shapes.join(Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0), Block.box(2.0, 3.0, 2.0, 14.0, 4.0, 14.0), BooleanOp.OR),
      BooleanOp.OR
   );

   public SuperSupportBeaconBlock(Properties properties) {
      super(properties);
   }

   @Override
   protected MapCodec<SuperSupportBeaconBlock> codec() {
      return CODEC;
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

   @Override
   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
      return this.interact(level, pos, player);
   }

   @Override
   protected ItemInteractionResult useItemOn(ItemStack item, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
      return this.interact(level, pos, player).consumesAction()
         ? ItemInteractionResult.sidedSuccess(level.isClientSide)
         : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
   }

   private InteractionResult interact(Level level, BlockPos pos, Player player) {
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
