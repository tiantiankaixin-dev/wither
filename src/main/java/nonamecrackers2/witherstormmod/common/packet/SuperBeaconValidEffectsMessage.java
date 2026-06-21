package nonamecrackers2.witherstormmod.common.packet;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import nonamecrackers2.witherstormmod.common.network.LegacyNetworkEvent.Context;
import nonamecrackers2.witherstormmod.common.network.Packet;
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
      this.effects = buffer.readCollection(HashSet::new, buf -> BuiltInRegistries.MOB_EFFECT.getHolder(buf.readVarInt()).orElseThrow());
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeCollection(this.effects, (buf, effect) -> buf.writeVarInt(BuiltInRegistries.MOB_EFFECT.getId(effect.value())));
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processSuperBeaconValidEffectsMessage(this));
   }
}
