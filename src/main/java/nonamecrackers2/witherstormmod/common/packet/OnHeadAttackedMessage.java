package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class OnHeadAttackedMessage extends Packet {
   private int entityId;
   private int headIndex;

   public OnHeadAttackedMessage(int entityId, int headIndex) {
      super(true);
      this.entityId = entityId;
      this.headIndex = headIndex;
   }

   public OnHeadAttackedMessage() {
      super(false);
   }

   protected void decode(FriendlyByteBuf buffer) {
      this.entityId = buffer.readVarInt();
      this.headIndex = buffer.readVarInt();
   }

   protected void encode(FriendlyByteBuf buffer) {
      buffer.writeVarInt(this.entityId);
      buffer.writeVarInt(this.headIndex);
   }

   public int getEntityId() {
      return this.entityId;
   }

   public int getHeadIndex() {
      return this.headIndex;
   }

   public Runnable getProcessor(Context context) {
      return client(() -> WitherStormModMessageHandlerClient.processOnHeadAttackedMessage(this));
   }
}
