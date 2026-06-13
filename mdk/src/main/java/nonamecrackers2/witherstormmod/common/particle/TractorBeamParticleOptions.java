package nonamecrackers2.witherstormmod.common.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;

public record TractorBeamParticleOptions(int storm, int head) implements ParticleOptions {
   public static final MapCodec<TractorBeamParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
               Codec.INT.fieldOf("storm").forGetter(TractorBeamParticleOptions::storm), 
               Codec.INT.fieldOf("head").forGetter(TractorBeamParticleOptions::head)
            )
            .apply(instance, TractorBeamParticleOptions::new)
   );
   
   public static final StreamCodec<FriendlyByteBuf, TractorBeamParticleOptions> STREAM_CODEC = StreamCodec.of(
      (buffer, options) -> {
         buffer.writeVarInt(options.storm);
         buffer.writeVarInt(options.head);
      },
      buffer -> new TractorBeamParticleOptions(buffer.readVarInt(), buffer.readVarInt())
   );

   public ParticleType<?> getType() {
      return (ParticleType<?>)WitherStormModParticleTypes.TRACTOR_BEAM.get();
   }

   // writeToNetwork is now handled by STREAM_CODEC

   public String writeToString() {
      return BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()) + " " + this.storm + " " + this.head;
   }
}
