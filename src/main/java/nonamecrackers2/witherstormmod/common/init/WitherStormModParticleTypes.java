package nonamecrackers2.witherstormmod.common.init;

import com.mojang.serialization.MapCodec;
import java.util.function.Function;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.common.particle.TractorBeamParticleOptions;

public class WitherStormModParticleTypes {
   public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "witherstormmod");
   public static final RegistryObject<SimpleParticleType> COMMAND_BLOCK = PARTICLE_TYPES.register("command_block", () -> new SimpleParticleType(true));
   public static final RegistryObject<ParticleType<TractorBeamParticleOptions>> TRACTOR_BEAM = register(
      "tractor_beam", false, type -> TractorBeamParticleOptions.CODEC, type -> TractorBeamParticleOptions.STREAM_CODEC
   );
   public static final RegistryObject<SimpleParticleType> PHLEGM = PARTICLE_TYPES.register("phlegm", () -> new SimpleParticleType(true));

   public static <T extends ParticleOptions> RegistryObject<ParticleType<T>> register(
      String name,
      boolean overrideLimiter,
      Function<ParticleType<T>, MapCodec<T>> codecGetter,
      Function<ParticleType<T>, StreamCodec<RegistryFriendlyByteBuf, T>> streamCodecGetter
   ) {
      return PARTICLE_TYPES.register(name, () -> new ParticleType<T>(overrideLimiter) {
            public MapCodec<T> codec() {
               return codecGetter.apply(this);
            }

            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
               return streamCodecGetter.apply(this);
            }
         });
   }
}
