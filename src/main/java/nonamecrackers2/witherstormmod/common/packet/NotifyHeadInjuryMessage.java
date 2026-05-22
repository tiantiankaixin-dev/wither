package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class NotifyHeadInjuryMessage extends Packet {
   private int entityId;
   private byte head;

   public NotifyHeadInjuryMessage(WitherStormEntity entity, int head) {
      super(true);
      this.entityId = entity.getId();
      this.head = (byte)head;
   }

   public NotifyHeadInjuryMessage() {
      super(false);
   }

   public int getEntityID() {
      return this.entityId;
   }

   public byte getHead() {
      return this.head;
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeInt(this.entityId);
      buffer.writeByte(this.head);
   }

   public void decode(FriendlyByteBuf buffer) {
      this.entityId = buffer.readInt();
      this.head = buffer.readByte();
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processNotifyHeadInjuryMessage(this));
   }

   public String toString() {
      return "NotifyHeadInjuryMessage[entityId=" + this.entityId + ", head=" + this.head + "]";
   }
}
