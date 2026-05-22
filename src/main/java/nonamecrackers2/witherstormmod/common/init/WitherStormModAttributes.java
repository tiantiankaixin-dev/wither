package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WitherStormModAttributes {
   public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, "witherstormmod");
   public static final RegistryObject<Attribute> TARGET_STATIONARY_FLYING_SPEED = ATTRIBUTES.register(
      "target_stationary_flying_speed",
      () -> new RangedAttribute("attribute.witherstormmod.name.target_stationary_flying_speed", 0.4, 0.01, 1.0).setSyncable(true)
   );
   public static final RegistryObject<Attribute> SLOW_FLYING_SPEED = ATTRIBUTES.register(
      "slow_flying_speed", () -> new RangedAttribute("attribute.witherstormmod.name.slow_flying_speed", 0.02, 0.01, 1.0).setSyncable(true)
   );
   public static final RegistryObject<Attribute> EVOLUTION_SPEED = ATTRIBUTES.register(
      "evolution_speed", () -> new RangedAttribute("attribute.witherstormmod.name.evolution_speed", 1.0, 0.0, 1024.0).setSyncable(true)
   );
   public static final RegistryObject<Attribute> HUNCHBACK_FOLLOW_RANGE = ATTRIBUTES.register(
      "hunchback_follow_range", () -> new RangedAttribute("attribute.witherstormmod.name.hunchback_follow_range", 32.0, 0.0, 2048.0).setSyncable(true)
   );
}
