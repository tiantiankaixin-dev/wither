package nonamecrackers2.witherstormmod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.BlockHitResult;
import nonamecrackers2.witherstormmod.common.blockentity.AbstractSuperBeaconBlockEntity;
import nonamecrackers2.witherstormmod.common.blockentity.SuperBeaconBlockEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlockEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModStats;

public class SuperBeaconBlock extends AbstractSuperBeaconBlock {
   public SuperBeaconBlock(Properties properties) {
      super(properties);
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new SuperBeaconBlockEntity(pos, state);
   }

   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
      return createBeaconTicker(level, type, (BlockEntityType<? extends AbstractSuperBeaconBlockEntity>)WitherStormModBlockEntityTypes.SUPER_BEACON.get());
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
      if (level.isClientSide) {
         return InteractionResult.SUCCESS;
      } else {
         if (level.getBlockEntity(pos) instanceof SuperBeaconBlockEntity beacon && !beacon.isDoingResummonAnimation() && beacon.canPlayerUseItems(player)) {
            ItemStack item = player.getItemInHand(hand);
            if (!player.isShiftKeyDown() && beacon.getContainerSize() > 0 && item.isEmpty()) {
               ItemStack stack = beacon.takeItem();
               if (!stack.isEmpty()) {
                  player.addItem(stack);
               }
            } else if (!item.isEmpty()) {
               if (beacon.getContainerSize() < 16) {
                  ItemStack stack = item.copy();
                  stack.setCount(1);
                  if (!player.getAbilities().instabuild) {
                     item.shrink(1);
                  }

                  beacon.addItem(stack);
                  level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
               }
            } else {
               player.openMenu(beacon);
               player.awardStat(WitherStormModStats.INTERACT_WITH_SUPER_BEACON);
            }
         }

         return InteractionResult.CONSUME;
      }
   }

   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean flag) {
      if (!state.is(state2.getBlock())) {
         if (level.getBlockEntity(pos) instanceof Container container) {
            Containers.dropContents(level, pos, container);
         }

         super.onRemove(state, level, pos, state2, flag);
      }
   }
}
