package nonamecrackers2.witherstormmod.common.item;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraft.world.item.Item.Properties;

public class WitheredNetherStarItem extends SimpleFoiledItem {
   public WitheredNetherStarItem(Properties properties) {
      super(properties);
   }

   public boolean canBeHurtBy(DamageSource source) {
      return source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
   }
}
