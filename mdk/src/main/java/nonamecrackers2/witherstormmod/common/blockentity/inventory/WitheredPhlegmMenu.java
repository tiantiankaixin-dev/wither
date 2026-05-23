package nonamecrackers2.witherstormmod.common.blockentity.inventory;

import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMenuTypes;

public class WitheredPhlegmMenu extends AbstractContainerMenu {
   private final Container container;
   private final ContainerData data;

   public WitheredPhlegmMenu(int id, Inventory inventory) {
      this(id, inventory, new SimpleContainer(25), new SimpleContainerData(1));
   }

   public Container getContainer() {
      return this.container;
   }

   public WitheredPhlegmMenu(int id, Inventory playerInventory, Container container, ContainerData data) {
      super((MenuType)WitherStormModMenuTypes.WITHERED_PHLEGM.get(), id);
      this.container = container;
      this.data = data;
      container.startOpen(playerInventory.player);
      int rowsColumns = Mth.ceil(Math.sqrt((double)container.getContainerSize()));
      int totalSlots = 0;

      label47:
      for (int y = 0; y < rowsColumns; y++) {
         for (int x = 0; x < rowsColumns; x++) {
            if (totalSlots >= container.getContainerSize()) {
               break label47;
            }

            this.addSlot(new Slot(container, totalSlots, 44 + x * 18, 18 + y * 18));
            totalSlots++;
         }
      }

      int yOffset = 22;

      for (int l = 0; l < 3; l++) {
         for (int j1 = 0; j1 < 9; j1++) {
            this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + yOffset));
         }
      }

      for (int i1 = 0; i1 < 9; i1++) {
         this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + yOffset));
      }

      this.addDataSlots(data);
   }

   public boolean stillValid(Player player) {
      return this.container.stillValid(player);
   }

   public void removed(Player player) {
      super.removed(player);
      this.container.stopOpen(player);
   }

   public ItemStack quickMoveStack(Player player, int slotId) {
      ItemStack stack = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(slotId);
      if (slot != null && slot.hasItem()) {
         ItemStack inSlot = slot.getItem();
         stack = inSlot.copy();
         if (slotId < this.container.getContainerSize()) {
            if (!this.moveItemStackTo(inSlot, this.container.getContainerSize(), this.slots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(inSlot, 0, this.container.getContainerSize(), false)) {
            return ItemStack.EMPTY;
         }

         if (inSlot.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
      }

      return stack;
   }

   public int getXp() {
      return this.data.get(0);
   }
}
