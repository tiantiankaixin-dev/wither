package nonamecrackers2.witherstormmod.common.block;

import java.util.function.Supplier;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

public class StrippableLogBlock extends RotatedPillarBlock {
   private final Supplier<Block> supplier;

   public StrippableLogBlock(Properties properties, Supplier<Block> supplier) {
      super(properties);
      this.supplier = supplier;
   }

   @Nullable
   public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
      BlockState transformed = super.getToolModifiedState(state, context, toolAction, simulate);
      return transformed == null && ToolActions.AXE_STRIP == toolAction
         ? (BlockState)this.supplier.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, (Axis)state.getValue(RotatedPillarBlock.AXIS))
         : transformed;
   }
}
