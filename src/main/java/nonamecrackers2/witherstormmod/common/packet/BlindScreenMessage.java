package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class BlindScreenMessage extends Packet {
   private int duration;
   private int fadeInDuration;
   private int fadeOutDuration;

   public BlindScreenMessage(int duration, int fadeInDuration, int fadeOutDuration) {
      super(true);
      this.duration = duration;
      this.fadeInDuration = fadeInDuration;
      this.fadeOutDuration = fadeOutDuration;
   }

   public BlindScreenMessage() {
      super(false);
   }

   public int getDuration() {
      return this.duration;
   }

   public int getFadeInDuration() {
      return this.fadeInDuration;
   }

   public int getFadeOutDuration() {
      return this.fadeOutDuration;
   }

   public void decode(FriendlyByteBuf buffer) throws IllegalArgumentException, IndexOutOfBoundsException {
      this.duration = buffer.readInt();
      this.fadeInDuration = buffer.readInt();
      this.fadeOutDuration = buffer.readInt();
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeInt(this.duration);
      buffer.writeInt(this.fadeInDuration);
      buffer.writeInt(this.fadeOutDuration);
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processBlindScreenMessage(this));
   }
}
