package nonamecrackers2.witherstormmod.api.common.registry;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.Registry;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;

public class WitherStormModRegistries {
   public static final ResourceLocation SPELL_TYPES_NAME = WitherStormMod.id("symbiont_spells");
   public static Supplier<Registry<SpellType>> SPELL_TYPES;

   public static void registerRegistries(@Nonnull NewRegistryEvent event) {
      SPELL_TYPES = event.create(new RegistryBuilder().setName(SPELL_TYPES_NAME));
   }
}
