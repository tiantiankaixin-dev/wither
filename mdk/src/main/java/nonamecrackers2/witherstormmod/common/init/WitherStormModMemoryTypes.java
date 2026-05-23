package nonamecrackers2.witherstormmod.common.init;

import java.util.Optional;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormModMemoryTypes {
   public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(
      BuiltInRegistries.MEMORY_MODULE_TYPES, "witherstormmod"
   );
   public static final DeferredHolder<MemoryModuleType<WitherStormEntity>> NEAREST_WITHER_STORM = MEMORY_MODULE_TYPES.register(
      "nearest_wither_storm", () -> new MemoryModuleType(Optional.empty())
   );
}
