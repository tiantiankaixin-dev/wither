package nonamecrackers2.witherstormmod.common.util;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraftforge.registries.RegistryObject;

public final class PotionStackUtil {
   private PotionStackUtil() {
   }

   public static ItemStack setPotion(ItemStack stack, Holder<Potion> potion) {
      stack.set(DataComponents.POTION_CONTENTS, contents(stack).withPotion(potion));
      return stack;
   }

   public static ItemStack setPotion(ItemStack stack, RegistryObject<Potion> potion) {
      return setPotion(stack, potion.getHolder().orElseThrow());
   }

   public static ItemStack setCustomEffects(ItemStack stack, List<MobEffectInstance> effects) {
      PotionContents contents = contents(stack);
      stack.set(DataComponents.POTION_CONTENTS, new PotionContents(contents.potion(), contents.customColor(), List.copyOf(effects)));
      return stack;
   }

   public static PotionContents contents(ItemStack stack) {
      return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
   }

   public static Optional<Holder<Potion>> getPotionHolder(ItemStack stack) {
      return contents(stack).potion();
   }

   public static Potion getPotion(ItemStack stack) {
      return getPotionHolder(stack).map(Holder::value).orElse(null);
   }
}
