package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class CreateDebrisMessage extends Packet {
   private int entityId;
   private boolean hidden;

   public CreateDebrisMessage(WitherStormEntity entity, boolean hidden) {
      super(true);
      this.entityId = entity.getId();
      this.hidden = hidden;
   }

   public CreateDebrisMessage() {
      super(false);
   }

   public int getEntityID() {
      return this.entityId;
   }

   public boolean isDebrisHidden() {
      return this.hidden;
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeVarInt(this.entityId);
      buffer.writeBoolean(this.hidden);
   }

   public void decode(FriendlyByteBuf buffer) {
      this.entityId = buffer.readVarInt();
      this.hidden = buffer.readBoolean();
   }

   public Runnable getProcessor(IPayloadContext context) {
      return () -> client(() -> WitherStormModMessageHandlerClient.processCreateDebrisMessage(this));
   }

   public String toString() {
      return "CreateDebrisMessage[id=" + this.entityId + ", hidden=" + this.hidden + "]";
   }
}
