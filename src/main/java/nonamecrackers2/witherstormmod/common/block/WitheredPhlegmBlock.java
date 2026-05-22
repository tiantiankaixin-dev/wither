package nonamecrackers2.witherstormmod.common.block;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.blockentity.WitheredPhlegmBlockEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlockEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;

public class WitheredPhlegmBlock extends BaseEntityBlock {
   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

   public WitheredPhlegmBlock(Properties properties) {
      super(properties);
      this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(POWERED, false));
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{POWERED});
   }

   public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos1, boolean flag) {
      boolean neighborSignal = level.hasNeighborSignal(pos);
      if (neighborSignal != (Boolean)state.getValue(POWERED)) {
         level.setBlock(pos, (BlockState)state.setValue(POWERED, neighborSignal), 3);
      }
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
      if (level.isClientSide) {
         return InteractionResult.SUCCESS;
      } else {
         if (level.getBlockEntity(pos) instanceof WitheredPhlegmBlockEntity phlegmBlock) {
            player.openMenu(phlegmBlock);
         }

         return InteractionResult.CONSUME;
      }
   }

   public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state1, boolean flag) {
      if (!state.is(state1.getBlock()) && level.getBlockEntity(pos) instanceof Container container) {
         Containers.dropContents(level, pos, container);
         level.updateNeighbourForOutputSignal(pos, this);
      }

      super.onRemove(state, level, pos, state1, flag);
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new WitheredPhlegmBlockEntity(pos, state);
   }

   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
      return level.isClientSide
         ? null
         : createTickerHelper(type, (BlockEntityType)WitherStormModBlockEntityTypes.WITHERED_PHLEGM.get(), WitheredPhlegmBlockEntity::serverTick);
   }

   public boolean hasAnalogOutputSignal(BlockState state) {
      return true;
   }

   public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
      return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
   }

   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack item) {
      if (item.hasCustomHoverName() && level.getBlockEntity(pos) instanceof WitheredPhlegmBlockEntity phlegmBlock) {
         phlegmBlock.setCustomName(item.getHoverName());
      }
   }

   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
      if (!(Boolean)state.getValue(BlockStateProperties.POWERED)) {
         doParticles(source, pos, level, 6, 3.0F, 0.1F);
      } else {
         doParticles(source, pos, level, 2, 1.0F, -0.01F);
      }
   }

   private static void doParticles(RandomSource random, BlockPos pos, Level level, int amount, float radius, float speed) {
      for (int i = 0; i < amount; i++) {
         double x = ((double)random.nextFloat() * 2.0 - 1.0) * (double)radius;
         double y = ((double)random.nextFloat() * 2.0 - 1.0) * (double)radius;
         double z = ((double)random.nextFloat() * 2.0 - 1.0) * (double)radius;
         Vec3 start = Vec3.atCenterOf(pos).add(x, y, z);
         Vec3 delta = new Vec3(x, y, z).scale((double)speed);
         level.addParticle(
            (ParticleOptions)WitherStormModParticleTypes.PHLEGM.get(),
            start.x,
            start.y,
            start.z,
            -delta.x,
            -delta.y,
            -delta.z
         );
      }
   }

   public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> text, TooltipFlag flag) {
      text.add(Component.translatable("description.withered_phlegm.use").withStyle(ChatFormatting.DARK_GRAY));
   }

   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      if (level.getBlockEntity(pos) instanceof WitheredPhlegmBlockEntity entity) {
         entity.recheckOpen();
      }
   }

   public int getExpDrop(BlockState state, LevelReader level, RandomSource randomSource, BlockPos pos, int fortuneLevel, int silkTouchLevel) {
      return level.getBlockEntity(pos) instanceof WitheredPhlegmBlockEntity phlegm ? phlegm.getStoredXp() : 0;
   }
}
