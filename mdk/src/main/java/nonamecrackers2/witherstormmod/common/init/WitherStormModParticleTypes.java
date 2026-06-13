package nonamecrackers2.witherstormmod.common.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import nonamecrackers2.witherstormmod.common.particle.TractorBeamParticleOptions;

public class WitherStormModParticleTypes {
   public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, "witherstormmod");
   public static final DeferredHolder<ParticleType<?>, SimpleParticleType> COMMAND_BLOCK = PARTICLE_TYPES.register("command_block", () -> new SimpleParticleType(true));
   public static final DeferredHolder<ParticleType<?>, ParticleType<TractorBeamParticleOptions>> TRACTOR_BEAM = PARTICLE_TYPES.register(
      "tractor_beam", () -> new ParticleType<TractorBeamParticleOptions>(false) {
         @Override
         public MapCodec<TractorBeamParticleOptions> codec() {
            return TractorBeamParticleOptions.CODEC;
         }
         
         @Override
         public StreamCodec<? super FriendlyByteBuf, TractorBeamParticleOptions> streamCodec() {
            return TractorBeamParticleOptions.STREAM_CODEC;
         }
      }
   );
   public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PHLEGM = PARTICLE_TYPES.register("phlegm", () -> new SimpleParticleType(true));

   // register method removed - use direct PARTICLE_TYPES.register with MapCodec and StreamCodec
}
