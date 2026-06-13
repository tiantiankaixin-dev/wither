package nonamecrackers2.witherstormmod.common.block;

import java.util.function.Supplier;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.Nullable;

public class StrippableLogBlock extends RotatedPillarBlock {
   private final Supplier<Block> supplier;

   public StrippableLogBlock(Properties properties, Supplier<Block> supplier) {
      super(properties);
      this.supplier = supplier;
   }

   @Nullable
   public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility toolAction, boolean simulate) {
      BlockState transformed = super.getToolModifiedState(state, context, toolAction, simulate);
      return transformed == null && ItemAbilities.AXE_STRIP == toolAction
         ? (BlockState)this.supplier.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, (Axis)state.getValue(RotatedPillarBlock.AXIS))
         : transformed;
   }
}
