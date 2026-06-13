package nonamecrackers2.witherstormmod.common.packet;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class SuperBeaconValidEffectsMessage extends Packet {
   private Set<Holder<MobEffect>> effects;

   public SuperBeaconValidEffectsMessage(Set<Holder<MobEffect>> effects) {
      super(true);
      this.effects = effects;
   }

   public SuperBeaconValidEffectsMessage() {
      super(false);
   }

   public Set<Holder<MobEffect>> getEffects() {
      return this.effects;
   }

   public void decode(FriendlyByteBuf buffer) {
      this.effects = (Set<Holder<MobEffect>>)buffer.readCollection(HashSet::new, buf -> BuiltInRegistries.MOB_EFFECT.getHolder(buf.readVarInt()).orElse(null));
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeCollection(this.effects, (buf, effect) -> buf.writeVarInt(BuiltInRegistries.MOB_EFFECT.getId(effect.value())));
   }

   public Runnable getProcessor(IPayloadContext context) {
      return () -> client(() -> WitherStormModMessageHandlerClient.processSuperBeaconValidEffectsMessage(this));
   }
}
