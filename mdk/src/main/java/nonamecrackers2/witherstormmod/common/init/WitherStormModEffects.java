package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import nonamecrackers2.witherstormmod.common.potion.WitherSicknessEffect;

public class WitherStormModEffects {
   public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, "witherstormmod");
   public static final DeferredHolder<MobEffect, MobEffect> WITHER_SICKNESS = EFFECTS.register(
      "wither_sickness",
      () -> new WitherSicknessEffect(MobEffectCategory.HARMFUL, 8192505)
            .addAttributeModifier(Attributes.MAX_HEALTH, "08BA7AB9-0056-4B4F-AA13-7103B4B9D127", 0.0, Operation.ADD_VALUE)
   );
}
