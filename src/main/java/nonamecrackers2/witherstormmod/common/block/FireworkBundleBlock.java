package nonamecrackers2.witherstormmod.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.BlockHitResult;
import nonamecrackers2.witherstormmod.common.blockentity.FireworkBundleBlockEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlockEntityTypes;
import org.jetbrains.annotations.Nullable;

public class FireworkBundleBlock extends BaseEntityBlock {
   public static final MapCodec<FireworkBundleBlock> CODEC = simpleCodec(FireworkBundleBlock::new);

   public FireworkBundleBlock(Properties properties) {
      super(properties);
   }

   @Override
   protected MapCodec<FireworkBundleBlock> codec() {
      return CODEC;
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
      this.activateFuse(level, pos);
   }

   public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean flag) {
      if (state2.is(state.getBlock()) && level.hasNeighborSignal(pos)) {
         this.activateFuse(level, pos);
      }
   }

   public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean flag) {
      if (level.hasNeighborSignal(pos)) {
         this.activateFuse(level, pos);
      }
   }

   @Override
   protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
      if (!stack.is(Items.FLINT_AND_STEEL) && !stack.is(Items.FIRE_CHARGE)) {
         return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
      } else {
         this.onCaughtFire(state, level, pos, hitResult.getDirection(), player);
         Item item = stack.getItem();
         if (!player.isCreative()) {
            if (stack.is(Items.FLINT_AND_STEEL)) {
               stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            } else {
               stack.shrink(1);
            }
         }

         player.awardStat(Stats.ITEM_USED.get(item));
         return ItemInteractionResult.sidedSuccess(level.isClientSide);
      }
   }

   public void onProjectileHit(Level p_60453_, BlockState p_60454_, BlockHitResult p_60455_, Projectile p_60456_) {
   }

   public boolean dropFromExplosion(Explosion explosion) {
      return false;
   }

   private void activateFuse(Level level, BlockPos pos) {
      if (level.getBlockEntity(pos) instanceof FireworkBundleBlockEntity entity) {
         entity.beginFuse();
      }
   }

   public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
      if (level.getBlockEntity(pos) instanceof FireworkBundleBlockEntity entity && entity.isActivated()) {
         return 4;
      }

      return super.getLightEmission(state, level, pos);
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new FireworkBundleBlockEntity(pos, state);
   }

   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
      return level.isClientSide
         ? null
         : createTickerHelper(type, (BlockEntityType)WitherStormModBlockEntityTypes.FIREWORK_BUNDLE.get(), FireworkBundleBlockEntity::serverTick);
   }
}
