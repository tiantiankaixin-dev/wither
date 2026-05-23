package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class WitherStormModAttributes {
   public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTES, "witherstormmod");
   public static final DeferredHolder<Attribute> TARGET_STATIONARY_FLYING_SPEED = ATTRIBUTES.register(
      "target_stationary_flying_speed",
      () -> new RangedAttribute("attribute.witherstormmod.name.target_stationary_flying_speed", 0.4, 0.01, 1.0).setSyncable(true)
   );
   public static final DeferredHolder<Attribute> SLOW_FLYING_SPEED = ATTRIBUTES.register(
      "slow_flying_speed", () -> new RangedAttribute("attribute.witherstormmod.name.slow_flying_speed", 0.02, 0.01, 1.0).setSyncable(true)
   );
   public static final DeferredHolder<Attribute> EVOLUTION_SPEED = ATTRIBUTES.register(
      "evolution_speed", () -> new RangedAttribute("attribute.witherstormmod.name.evolution_speed", 1.0, 0.0, 1024.0).setSyncable(true)
   );
   public static final DeferredHolder<Attribute> HUNCHBACK_FOLLOW_RANGE = ATTRIBUTES.register(
      "hunchback_follow_range", () -> new RangedAttribute("attribute.witherstormmod.name.hunchback_follow_range", 32.0, 0.0, 2048.0).setSyncable(true)
   );
}
