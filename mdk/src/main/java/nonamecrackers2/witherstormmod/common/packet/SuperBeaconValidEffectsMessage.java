package nonamecrackers2.witherstormmod.common.packet;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class SuperBeaconValidEffectsMessage extends Packet {
   private Set<MobEffect> effects;

   public SuperBeaconValidEffectsMessage(Set<MobEffect> effects) {
      super(true);
      this.effects = effects;
   }

   public SuperBeaconValidEffectsMessage() {
      super(false);
   }

   public Set<MobEffect> getEffects() {
      return this.effects;
   }

   public void decode(FriendlyByteBuf buffer) {
      this.effects = (Set<MobEffect>)buffer.readCollection(HashSet::new, buf -> MobEffect.byId(buf.readVarInt()));
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeCollection(this.effects, (buf, effect) -> buf.writeVarInt(BuiltInRegistries.MOB_EFFECT.getId(effect)));
   }

   public Runnable getProcessor(IPayloadContext context) {
      return () -> client(() -> WitherStormModMessageHandlerClient.processSuperBeaconValidEffectsMessage(this));
   }
}
