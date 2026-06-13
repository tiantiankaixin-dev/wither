package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import nonamecrackers2.crackerslib.common.packet.Packet;

public class SuperBeaconSetEffectMessage extends Packet {
   private int effect;

   public SuperBeaconSetEffectMessage(int effect) {
      super(true);
      this.effect = effect;
   }

   public SuperBeaconSetEffectMessage() {
      super(false);
   }

   public int getEffectId() {
      return this.effect;
   }

   public void decode(FriendlyByteBuf buffer) {
      this.effect = buffer.readVarInt();
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeVarInt(this.effect);
   }

   public Runnable getProcessor(IPayloadContext context) {
      return () -> context.player()
         .filter(ServerPlayer.class::isInstance)
         .map(ServerPlayer.class::cast)
         .ifPresent(player -> WitherStormModMessageHandlerServer.processSuperBeaconSetEffectMessage(this, player));
   }
}
