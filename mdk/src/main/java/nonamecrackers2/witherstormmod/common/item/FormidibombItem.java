package nonamecrackers2.witherstormmod.common.item;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import nonamecrackers2.witherstormmod.common.blockentity.FormidibombBlockEntity;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;

public class FormidibombItem extends BlockItem {
   public FormidibombItem(Block block, Properties properties) {
      super(block, properties);
   }

   public void inventoryTick(ItemStack stack, Level world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
      if (!world.isClientSide) {
         this.tickFuse(stack, world, entity, entity.blockPosition());
      }
   }

   public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
      this.tickFuse(stack, entity.level(), null, entity.blockPosition());
      return super.onEntityItemUpdate(stack, entity);
   }

   public void tickFuse(ItemStack stack, Level world, @Nullable Entity entity, BlockPos pos) {
      if (!world.isClientSide) {
         if ((Boolean)WitherStormModConfig.SERVER.formidibombFuseEnabled.get() && this.getStartFuse(stack) > 0) {
            this.countFuse(stack, -1);
         }

         if (this.getStartFuse(stack) > 0) {
            if ((Boolean)WitherStormModConfig.SERVER.shouldDropFromInventory.get()
               && this.getFuse(stack) <= this.getStartFuse(stack) / (Integer)WitherStormModConfig.SERVER.dropInterval.get()
               && !world.isClientSide) {
               LivingEntity living = null;
               if (entity instanceof LivingEntity) {
                  living = (LivingEntity)entity;
               }

               FormidibombEntity formidibomb = new FormidibombEntity(
                  world,
                  (double)pos.getX() + 0.5,
                  (double)pos.getY(),
                  (double)pos.getZ() + 0.5,
                  living,
                  null,
                  ((Block)WitherStormModBlocks.FORMIDIBOMB.get()).defaultBlockState()
               );
               formidibomb.setFuse(this.getFuse(stack));
               formidibomb.setStartFuse(this.getStartFuse(stack));
               if (world.addFreshEntity(formidibomb)) {
                  stack.shrink(1);
                  world.playSound(
                     null, formidibomb.getX(), formidibomb.getY(), formidibomb.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F
                  );
               }
            }

            if (this.getFuse(stack) <= 0) {
               stack.shrink(1);
               if (!world.isClientSide) {
                  FormidibombEntity.explode(
                     world, entity, 48 + world.random.nextInt(9), 3, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ()
                  );
               }
            }
         }
      }
   }

   public void onCraftedBy(ItemStack stack, Level world, Player entity) {
      this.setFuse(stack, (Integer)WitherStormModConfig.SERVER.craftFuseTicks.get());
   }

   public int getBarWidth(ItemStack stack) {
      return Math.round(13.0F - (float)(this.getStartFuse(stack) - this.getFuse(stack)) * 13.0F / (float)this.getStartFuse(stack));
   }

   public int getBarColor(ItemStack stack) {
      int fuse = this.getStartFuse(stack) / this.getFuse(stack);
      return fuse % 2 == 0 ? 12718080 : 10027161;
   }

   public boolean isBarVisible(ItemStack stack) {
      return this.getFuse(stack) > 0 && this.getFuse(stack) < this.getStartFuse(stack);
   }

   protected boolean updateCustomBlockEntityTag(BlockPos pos, Level world, Player player, ItemStack stack, BlockState state) {
      int fuse = this.getFuse(stack);
      int startFuse = this.getStartFuse(stack);
      if (world.getBlockEntity(pos) instanceof FormidibombBlockEntity formidibomb) {
         formidibomb.setLifeFuse(fuse);
         formidibomb.setStartFuse(startFuse);
         formidibomb.setFormidibombOwner(player);
         formidibomb.setChanged();
      }

      return super.updateCustomBlockEntityTag(pos, world, player, stack, state);
   }

   public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
      return slotChanged ? super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) : false;
   }

   public int getFuse(ItemStack stack) {
      return stack.getOrCreateTag().getInt("Fuse");
   }

   public int getStartFuse(ItemStack stack) {
      return stack.getOrCreateTag().getInt("StartFuse");
   }

   protected void countFuse(ItemStack stack, int amount) {
      CompoundTag compound = stack.getOrCreateTag();
      compound.putInt("Fuse", compound.getInt("Fuse") + amount);
   }

   public void setFuse(ItemStack stack, int fuse) {
      CompoundTag compound = stack.getOrCreateTag();
      compound.putInt("Fuse", fuse);
      compound.putInt("StartFuse", fuse);
   }
}
