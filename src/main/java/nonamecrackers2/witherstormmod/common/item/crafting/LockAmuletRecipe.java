package nonamecrackers2.witherstormmod.common.item.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.item.AmuletItem;
import nonamecrackers2.witherstormmod.common.util.ItemStackDataUtil;

public class LockAmuletRecipe extends CustomRecipe {
   public LockAmuletRecipe(CraftingBookCategory category) {
      super(category);
   }

   public boolean matches(CraftingInput input, Level level) {
      boolean flag = true;
      int totalAmulets = 0;

      for (int i = 0; i < input.size(); i++) {
         ItemStack stack = input.getItem(i);
         if (stack.getItem() instanceof AmuletItem) {
            totalAmulets++;
         }

         CompoundTag tag = ItemStackDataUtil.getTag(stack);
         if (!stack.isEmpty() && (!(stack.getItem() instanceof AmuletItem) || tag != null && tag.getBoolean("Locked")) || totalAmulets > 1) {
            flag = false;
         }
      }

      return flag;
   }

   public ItemStack assemble(CraftingInput input, HolderLookup.Provider access) {
      for (int i = 0; i < input.size(); i++) {
         ItemStack slotItem = input.getItem(i);
         if (slotItem.getItem() instanceof AmuletItem) {
            ItemStack stack = slotItem.copy();
            ItemStackDataUtil.getOrCreateTag(stack).putBoolean("Locked", true);
            return stack;
         }
      }

      return ItemStack.EMPTY;
   }

   public boolean canCraftInDimensions(int param1, int param2) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      //
      // Bytecode:
      // 0: iload 1
      // 1: iload 2
      // 2: imul
      // 3: bipush 1
      // 4: if_icmplt b
      // 7: bipush 1
      // 8: goto c
      // b: bipush 0
      // c: ireturn
      return param1 * param2 >= 1;
   }

   public RecipeSerializer<?> getSerializer() {
      return (RecipeSerializer<?>)WitherStormModRecipeSerializers.LOCK_AMULET.get();
   }
}
