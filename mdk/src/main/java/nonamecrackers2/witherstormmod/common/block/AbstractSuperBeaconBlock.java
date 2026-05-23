package nonamecrackers2.witherstormmod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import nonamecrackers2.witherstormmod.common.blockentity.AbstractSuperBeaconBlockEntity;

public abstract class AbstractSuperBeaconBlock extends BaseEntityBlock {
   public AbstractSuperBeaconBlock(Properties properties) {
      super(properties);
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   protected static <T extends BlockEntity> BlockEntityTicker<T> createBeaconTicker(
      Level level, BlockEntityType<T> type, BlockEntityType<? extends AbstractSuperBeaconBlockEntity> type1
   ) {
      return createTickerHelper(type, type1, (l, s, p, entity) -> entity.tick());
   }

   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
      if (stack.hasCustomHoverName() && level.getBlockEntity(pos) instanceof AbstractSuperBeaconBlockEntity beacon) {
         beacon.setCustomName(stack.getHoverName());
      }
   }
}
