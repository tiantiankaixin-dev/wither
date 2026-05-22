package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class InjureHeadMessage extends Packet {
   private int entityId;
   private byte head;
   private InteractionHand hand;

   public InjureHeadMessage(WitherStormEntity entity, int head, InteractionHand hand) {
      super(true);
      this.entityId = entity.getId();
      this.head = (byte)head;
      this.hand = hand;
   }

   public InjureHeadMessage() {
      super(false);
   }

   public int getEntityID() {
      return this.entityId;
   }

   public byte getHead() {
      return this.head;
   }

   public InteractionHand getHand() {
      return this.hand;
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeInt(this.entityId);
      buffer.writeByte(this.head);
      buffer.writeEnum(this.hand);
   }

   public void decode(FriendlyByteBuf buffer) throws IllegalArgumentException, IndexOutOfBoundsException {
      this.entityId = buffer.readInt();
      this.head = buffer.readByte();
      this.hand = (InteractionHand)buffer.readEnum(InteractionHand.class);
   }

   public Runnable getProcessor(Context context) {
      return () -> WitherStormModMessageHandlerServer.processInjureHeadMessage(this, context.getSender());
   }

   public String toString() {
      return "InjureHeadMessage[entityId=" + this.entityId + ", head=" + this.head + ", hand=" + this.hand + "]";
   }
}
