package nonamecrackers2.witherstormmod.common.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class ItemStackDataUtil {
   private ItemStackDataUtil() {
   }

   public static CompoundTag getOrCreateTag(ItemStack stack) {
      CustomData data = stack.get(DataComponents.CUSTOM_DATA);
      if (data == null) {
         stack.set(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag()));
         data = stack.get(DataComponents.CUSTOM_DATA);
      }

      return data.getUnsafe();
   }

   public static CompoundTag getTag(ItemStack stack) {
      CustomData data = stack.get(DataComponents.CUSTOM_DATA);
      return data != null && !data.isEmpty() ? data.getUnsafe() : null;
   }

   public static boolean hasTag(ItemStack stack) {
      CustomData data = stack.get(DataComponents.CUSTOM_DATA);
      return data != null && !data.isEmpty();
   }

   public static void setTag(ItemStack stack, CompoundTag tag) {
      if (tag == null || tag.isEmpty()) {
         stack.remove(DataComponents.CUSTOM_DATA);
      } else {
         stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag.copy()));
      }
   }

   public static void updateTag(ItemStack stack, java.util.function.Consumer<CompoundTag> updater) {
      CompoundTag tag = getOrCreateTag(stack);
      updater.accept(tag);
      setTag(stack, tag);
   }
}
