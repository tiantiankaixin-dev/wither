package nonamecrackers2.witherstormmod.common.item.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.item.AmuletItem;

public class LockAmuletRecipe extends CustomRecipe {
   public LockAmuletRecipe(ResourceLocation id, CraftingBookCategory category) {
      super(id, category);
   }

   public boolean matches(CraftingContainer container, Level level) {
      boolean flag = true;
      int totalAmulets = 0;

      for (int i = 0; i < container.getContainerSize(); i++) {
         ItemStack stack = container.getItem(i);
         if (stack.getItem() instanceof AmuletItem) {
            totalAmulets++;
         }

         if (!stack.isEmpty() && !(stack.getItem() instanceof AmuletItem) || stack.getOrCreateTag().getBoolean("Locked") || totalAmulets > 1) {
            flag = false;
         }
      }

      return flag;
   }

   public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
      for (int i = 0; i < container.getContainerSize(); i++) {
         ItemStack slotItem = container.getItem(i);
         if (slotItem.getItem() instanceof AmuletItem) {
            ItemStack stack = slotItem.copy();
            stack.getOrCreateTag().putBoolean("Locked", true);
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
