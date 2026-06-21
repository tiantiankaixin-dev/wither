package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.common.potion.WitherSicknessEffect;

public class WitherStormModEffects {
   public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "witherstormmod");
   public static final RegistryObject<MobEffect> WITHER_SICKNESS = EFFECTS.register(
      "wither_sickness",
      () -> new WitherSicknessEffect(MobEffectCategory.HARMFUL, 8192505)
            .addAttributeModifier(Attributes.MAX_HEALTH, WitherSicknessEffect.MAX_HEALTH_MODIFIER, 0.0, Operation.ADD_VALUE)
   );

   public static Holder<MobEffect> holder(RegistryObject<MobEffect> effect) {
      return effect.getHolder().orElseThrow(() -> new IllegalStateException("Missing effect holder: " + effect.getId()));
   }
}
