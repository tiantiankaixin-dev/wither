package nonamecrackers2.witherstormmod.common.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleOptions.Deserializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;

public record TractorBeamParticleOptions(int storm, int head) implements ParticleOptions {
   public static final Codec<TractorBeamParticleOptions> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
               Codec.INT.fieldOf("storm").forGetter(TractorBeamParticleOptions::storm), Codec.INT.fieldOf("head").forGetter(TractorBeamParticleOptions::head)
            )
            .apply(instance, TractorBeamParticleOptions::new)
   );
   public static final Deserializer<TractorBeamParticleOptions> DESERIALIZIER = new Deserializer<TractorBeamParticleOptions>() {
      public TractorBeamParticleOptions fromCommand(ParticleType<TractorBeamParticleOptions> type, StringReader reader) throws CommandSyntaxException {
         return new TractorBeamParticleOptions(reader.readInt(), reader.readInt());
      }

      public TractorBeamParticleOptions fromNetwork(ParticleType<TractorBeamParticleOptions> type, FriendlyByteBuf buffer) {
         return new TractorBeamParticleOptions(buffer.readVarInt(), buffer.readVarInt());
      }
   };

   public ParticleType<?> getType() {
      return (ParticleType<?>)WitherStormModParticleTypes.TRACTOR_BEAM.get();
   }

   public void writeToNetwork(FriendlyByteBuf buffer) {
      buffer.writeVarInt(this.head);
      buffer.writeVarInt(this.head);
   }

   public String writeToString() {
      return NeoBuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()) + " " + this.storm + " " + this.head;
   }
}
