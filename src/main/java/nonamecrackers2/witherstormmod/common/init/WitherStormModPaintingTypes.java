package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WitherStormModPaintingTypes {
   public static final DeferredRegister<PaintingVariant> PAINTING_MOTIVES = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, "witherstormmod");
   public static final RegistryObject<PaintingVariant> AMULET = PAINTING_MOTIVES.register("amulet", () -> new PaintingVariant(16, 32));

   public static void register(IEventBus eventBus) {
      PAINTING_MOTIVES.register(eventBus);
   }
}
