package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class WitherStormModPotions {
   public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(NeoForgeRegistries.POTIONS, "witherstormmod");
   public static final DeferredHolder<Potion> WITHER = POTIONS.register(
      "wither", () -> new Potion(new MobEffectInstance[]{new MobEffectInstance(MobEffects.WITHER, 900)})
   );
   public static final DeferredHolder<Potion> LONG_WITHER = POTIONS.register(
      "long_wither", () -> new Potion(new MobEffectInstance[]{new MobEffectInstance(MobEffects.WITHER, 1800)})
   );
   public static final DeferredHolder<Potion> STRONG_WITHER = POTIONS.register(
      "strong_wither", () -> new Potion(new MobEffectInstance[]{new MobEffectInstance(MobEffects.WITHER, 432, 1)})
   );
}
