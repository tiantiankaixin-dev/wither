package nonamecrackers2.witherstormmod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;

public class TaintedPumpkinBlock extends Block {
   public TaintedPumpkinBlock(Properties properties) {
      super(properties);
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
      ItemStack item = player.getItemInHand(hand);
      if (item.canPerformAction(ToolActions.SHEARS_CARVE)) {
         if (!level.isClientSide) {
            Direction hitSide = hitResult.getDirection();
            Direction direction = hitSide.getAxis() == Axis.Y ? player.getDirection().getOpposite() : hitSide;
            level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(
               pos, (BlockState)((Block)WitherStormModBlocks.TAINTED_CARVED_PUMPKIN.get()).defaultBlockState().setValue(TaintedCarvedPumpkinBlock.FACING, direction), 11
            );
            item.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            level.gameEvent(player, GameEvent.SHEAR, pos);
            player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
         }

         return InteractionResult.sidedSuccess(level.isClientSide);
      } else {
         return InteractionResult.PASS;
      }
   }
}
