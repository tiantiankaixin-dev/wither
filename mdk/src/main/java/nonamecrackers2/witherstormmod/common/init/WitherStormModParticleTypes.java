package nonamecrackers2.witherstormmod.common.init;

import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleOptions.Deserializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import nonamecrackers2.witherstormmod.common.particle.TractorBeamParticleOptions;

public class WitherStormModParticleTypes {
   public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(NeoBuiltInRegistries.PARTICLE_TYPE, "witherstormmod");
   public static final DeferredHolder<SimpleParticleType> COMMAND_BLOCK = PARTICLE_TYPES.register("command_block", () -> new SimpleParticleType(true));
   public static final DeferredHolder<ParticleType<TractorBeamParticleOptions>> TRACTOR_BEAM = register(
      "tractor_beam", false, TractorBeamParticleOptions.DESERIALIZIER, type -> TractorBeamParticleOptions.CODEC
   );
   public static final DeferredHolder<SimpleParticleType> PHLEGM = PARTICLE_TYPES.register("phlegm", () -> new SimpleParticleType(true));

   public static <T extends ParticleOptions> DeferredHolder<ParticleType<T>> register(
      String name, boolean overrideLimiter, Deserializer<T> deserializer, Function<ParticleType<T>, Codec<T>> codecGetter
   ) {
      return PARTICLE_TYPES.register(name, () -> new ParticleType<T>(overrideLimiter, deserializer) {
            public Codec<T> codec() {
               return (Codec<T>)codecGetter.apply(this);
            }
         });
   }
}
