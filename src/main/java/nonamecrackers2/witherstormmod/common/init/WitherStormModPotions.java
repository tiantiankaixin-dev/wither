package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WitherStormModPotions {
   public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, "witherstormmod");
   public static final RegistryObject<Potion> WITHER = POTIONS.register(
      "wither", () -> new Potion(new MobEffectInstance[]{new MobEffectInstance(MobEffects.WITHER, 900)})
   );
   public static final RegistryObject<Potion> LONG_WITHER = POTIONS.register(
      "long_wither", () -> new Potion(new MobEffectInstance[]{new MobEffectInstance(MobEffects.WITHER, 1800)})
   );
   public static final RegistryObject<Potion> STRONG_WITHER = POTIONS.register(
      "strong_wither", () -> new Potion(new MobEffectInstance[]{new MobEffectInstance(MobEffects.WITHER, 432, 1)})
   );
}
