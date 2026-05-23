package nonamecrackers2.witherstormmod.common.blockentity;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.block.WitheredPhlegmBlock;
import nonamecrackers2.witherstormmod.common.blockentity.inventory.WitheredPhlegmMenu;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlockEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;

public class WitheredPhlegmBlockEntity extends RandomizableContainerBlockEntity {
   public static final int CONTAINER_SIZE = 25;
   private NonNullList<ItemStack> items = NonNullList.withSize(25, ItemStack.EMPTY);
   private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
      protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int i, int i2) {
      }

      protected void onOpen(Level level, BlockPos pos, BlockState state) {
         WitheredPhlegmBlockEntity.this.playSound(WitherStormModSoundEvents.WITHERED_PHLEGM_BLOCK_OPEN.get());
      }

      protected void onClose(Level level, BlockPos pos, BlockState state) {
         WitheredPhlegmBlockEntity.this.playSound(WitherStormModSoundEvents.WITHERED_PHLEGM_BLOCK_CLOSE.get());
      }

      protected boolean isOwnContainer(Player player) {
         return player.containerMenu instanceof WitheredPhlegmMenu menu ? menu.getContainer() == WitheredPhlegmBlockEntity.this : false;
      }
   };
   private final ContainerData dataAccess = new ContainerData() {
      public void set(int slot, int data) {
         if (slot == 0) {
            WitheredPhlegmBlockEntity.this.storedExperience = data;
         }
      }

      public int getCount() {
         return 1;
      }

      public int get(int slot) {
         return slot == 0 ? WitheredPhlegmBlockEntity.this.storedExperience : 0;
      }
   };
   private int storedExperience;

   public WitheredPhlegmBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)WitherStormModBlockEntityTypes.WITHERED_PHLEGM.get(), pos, state);
   }

   public int getContainerSize() {
      return 25;
   }

   protected NonNullList<ItemStack> getItems() {
      return this.items;
   }

   protected void setItems(NonNullList<ItemStack> items) {
      this.items = items;
   }

   protected Component getDefaultName() {
      return Component.translatable("container.witherstormmod.phlegm_block");
   }

   protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
      return new WitheredPhlegmMenu(id, inventory, this, this.dataAccess);
   }

   public static void serverTick(Level level, BlockPos pos, BlockState state, WitheredPhlegmBlockEntity entity) {
      if (!(Boolean)state.getValue(WitheredPhlegmBlock.POWERED) && !entity.getItems().stream().allMatch(Predicate.not(ItemStack::isEmpty))) {
         AABB box = new AABB(pos, pos.offset(1, 1, 1));

         for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, box.inflate(8.0))) {
            Vec3 vec3 = Vec3.atCenterOf(pos);
            double d = item.position().distanceTo(vec3);
            Vec3 delta = vec3.subtract(item.position()).normalize().scale(Math.max(1.0 - d / 8.0, 0.1));
            delta = item.getDeltaMovement().multiply(0.8, 1.0, 0.8).add(delta.multiply(1.0, 0.2, 1.0));
            item.setDeltaMovement(delta);
            ((ServerChunkCache)item.getCommandSenderWorld().getChunkSource()).broadcast(item, new ClientboundSetEntityMotionPacket(item));
            if (item.getBoundingBox().intersects(box.inflate(0.5)) && HopperBlockEntity.addItem(entity, item)) {
               level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
         }
      }
   }

   public void load(CompoundTag tag) {
      super.load(tag);
      this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
      if (!this.tryLoadLootTable(tag)) {
         ContainerHelper.loadAllItems(tag, this.items);
      }

      this.storedExperience = tag.getInt("StoredXp");
   }

   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      if (!this.trySaveLootTable(tag)) {
         ContainerHelper.saveAllItems(tag, this.items);
      }

      tag.putInt("StoredXp", this.storedExperience);
   }

   private void playSound(SoundEvent event) {
      this.level.playSound(null, this.getBlockPos(), event, SoundSource.BLOCKS, 1.0F, 1.0F);
   }

   public void startOpen(Player player) {
      if (!this.remove && !player.isSpectator()) {
         this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
      }
   }

   public void stopOpen(Player player) {
      if (!this.remove && !player.isSpectator()) {
         this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
      }
   }

   public void recheckOpen() {
      if (!this.remove) {
         this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
      }
   }

   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   public CompoundTag getUpdateTag() {
      CompoundTag tag = new CompoundTag();
      ContainerHelper.saveAllItems(tag, this.items);
      return tag;
   }

   public void handleUpdateTag(CompoundTag tag) {
      ContainerHelper.loadAllItems(tag, this.items);
   }

   public void setChanged() {
      super.setChanged();
      if (this.level != null) {
         this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
      }
   }

   public int getStoredXp() {
      return this.storedExperience;
   }
}
