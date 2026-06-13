package nonamecrackers2.witherstormmod.common.block;

import java.util.Collections;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.blockentity.FormidibombBlockEntity;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.item.FormidibombItem;
import nonamecrackers2.witherstormmod.common.util.IFormidibomb;

public class FormidibombBlock extends TntBlock implements EntityBlock {
   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

   public FormidibombBlock(Properties properties) {
      super(properties);
      this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(TntBlock.UNSTABLE, false));
   }

   public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
      if (!world.isClientSide) {
         BlockEntity tile = world.getBlockEntity(pos);
         IFormidibomb formidibomb = null;
         if (tile instanceof IFormidibomb) {
            formidibomb = (IFormidibomb)tile;
         }

         FormidibombEntity entity = new FormidibombEntity(
            world,
            (double)pos.getX() + 0.5,
            (double)pos.getY(),
            (double)pos.getZ() + 0.5,
            explosion.getIndirectSourceEntity(),
            formidibomb,
            world.getBlockState(pos)
         );
         entity.initiateFuse(20);
         world.addFreshEntity(entity);
      }
   }

   public void onCaughtFire(BlockState state, Level world, BlockPos pos, Direction face, LivingEntity igniter) {
      if (!world.isClientSide) {
         BlockEntity tile = world.getBlockEntity(pos);
         IFormidibomb formidibomb = null;
         if (tile instanceof IFormidibomb) {
            formidibomb = (IFormidibomb)tile;
         }

         FormidibombEntity entity = new FormidibombEntity(
            world, (double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5, igniter, formidibomb, state
         );
         int newFuse = (Integer)WitherStormModConfig.SERVER.catchFireFuseTicks.get();
         if (entity.getFuseLife() > newFuse) {
            entity.initiateFuse(newFuse);
         }

         world.addFreshEntity(entity);
         world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
      }
   }

   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
      doParticles(source, pos, level, 6, 3.0F, 0.1F);
   }

   private static void doParticles(RandomSource random, BlockPos pos, Level level, int amount, float radius, float speed) {
      for (int i = 0; i < amount; i++) {
         double x = ((double)random.nextFloat() * 2.0 - 1.0) * (double)radius;
         double y = ((double)random.nextFloat() * 2.0 - 1.0) * (double)radius;
         double z = ((double)random.nextFloat() * 2.0 - 1.0) * (double)radius;
         Vec3 start = Vec3.atCenterOf(pos).add(x, y, z);
         Vec3 delta = new Vec3(x, y, z).scale((double)speed);
         level.addParticle(
            (ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(),
            start.x,
            start.y,
            start.z,
            -delta.x,
            -delta.y,
            -delta.z
         );
      }
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
   }

   public BlockState rotate(BlockState state, Rotation rotation) {
      return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
   }

   public BlockState mirror(BlockState state, Mirror mirror) {
      return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(new Property[]{FACING});
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new FormidibombBlockEntity(pos, state);
   }

   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
      return castTicker(FormidibombBlockEntity::tick);
   }

   private static <T extends BlockEntity> BlockEntityTicker<T> castTicker(BlockEntityTicker<FormidibombBlockEntity> ticker) {
      return (BlockEntityTicker<T>)ticker;
   }

   public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder loot) {
      ResourceLocation location = this.getLootTable();
      if (location == BuiltInLootTables.EMPTY) {
         return Collections.emptyList();
      } else {
         LootParams context = loot.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
         ServerLevel world = context.getLevel();
         LootTable table = world.getServer().getLootData().getLootTable(location);
         List<ItemStack> stacks = table.getRandomItems(context);

         for (ItemStack stack : stacks) {
            if (stack.getItem() instanceof FormidibombItem) {
               BlockEntity tile = (BlockEntity)loot.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
               if (tile instanceof IFormidibomb formidibomb) {
                  CompoundTag compound = stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
                  compound.putInt("Fuse", formidibomb.getFuseLife());
                  compound.putInt("StartFuse", formidibomb.getStartFuse());
                  stack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(compound));
               }
            }
         }

         return stacks;
      }
   }

   public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> components, TooltipFlag flag) {
      super.appendHoverText(stack, world, components, flag);
      if (stack.getItem() instanceof FormidibombItem) {
         FormidibombItem item = (FormidibombItem)stack.getItem();
         if (item.getStartFuse(stack) > 0 && item.getFuse(stack) < item.getStartFuse(stack)) {
            ChatFormatting style = item.getFuse(stack) / 10 % 2 == 0 ? ChatFormatting.RED : ChatFormatting.DARK_PURPLE;
            components.add(Component.translatable("description.formidibomb.fuse", new Object[]{item.getFuse(stack) / 20}).withStyle(style));
         }
      }
   }

   public PushReaction getPistonPushReaction(BlockState state) {
      return PushReaction.BLOCK;
   }
}
