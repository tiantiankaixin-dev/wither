package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.entity.decoration.PaintingVariant;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class WitherStormModPaintingTypes {
   public static final DeferredRegister<PaintingVariant> PAINTING_MOTIVES = DeferredRegister.create(NeoForgeRegistries.PAINTING_VARIANTS, "witherstormmod");
   public static final DeferredHolder<PaintingVariant> AMULET = PAINTING_MOTIVES.register("amulet", () -> new PaintingVariant(16, 32));

   public static void register(IEventBus eventBus) {
      PAINTING_MOTIVES.register(eventBus);
   }
}
