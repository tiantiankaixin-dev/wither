package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;

public class SuperBeaconToggleAreaMessage extends Packet {
   private boolean shouldShow;

   public SuperBeaconToggleAreaMessage(boolean shouldShow) {
      super(true);
      this.shouldShow = shouldShow;
   }

   public SuperBeaconToggleAreaMessage() {
      super(false);
   }

   public boolean shouldShowArea() {
      return this.shouldShow;
   }

   public void decode(FriendlyByteBuf buffer) {
      this.shouldShow = buffer.readBoolean();
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeBoolean(this.shouldShow);
   }

   public Runnable getProcessor(Context context) {
      return () -> WitherStormModMessageHandlerServer.processSuperBeaconToggleAreaMessage(this, context.getSender());
   }
}
