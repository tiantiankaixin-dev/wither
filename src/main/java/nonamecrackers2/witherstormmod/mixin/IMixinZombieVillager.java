package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ZombieVillager.class})
public interface IMixinZombieVillager {
   @Accessor
   Tag getGossips();

   @Accessor
   MerchantOffers getTradeOffers();
}
