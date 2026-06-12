package nonamecrackers2.witherstormmod.common.item;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class WitheredNetherStarItem extends Item {
   public WitheredNetherStarItem(Properties properties) {
      super(properties);
   }

   @Override
   public boolean isFoil(ItemStack stack) {
      return true;
   }

   public boolean canBeHurtBy(DamageSource source) {
      return source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
   }
}
