package nonamecrackers2.witherstormmod.common.item;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item.Properties;

public class CommandBlockSwordItem extends SwordItem {
   public CommandBlockSwordItem(Tier tier, int damage, float attackSpeed, Properties properties) {
      super(tier, damage, attackSpeed, properties);
   }

   public boolean isEnchantable(ItemStack stack) {
      return this.getMaxStackSize(stack) == 1;
   }

   public boolean canBeHurtBy(DamageSource source) {
      return source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
   }
}
