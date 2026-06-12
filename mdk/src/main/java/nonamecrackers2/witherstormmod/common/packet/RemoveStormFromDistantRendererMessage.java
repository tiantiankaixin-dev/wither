package nonamecrackers2.witherstormmod.common.packet;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class RemoveStormFromDistantRendererMessage extends DistantRendererMessage {
   private int id;

   public RemoveStormFromDistantRendererMessage(List<Integer> applicable, WitherStormEntity entity) {
      super(true, applicable);
      this.id = entity.getId();
   }

   public RemoveStormFromDistantRendererMessage() {
      super(false, Lists.newArrayList());
   }

   public int getId() {
      return this.id;
   }

   @Override
   public void encode(FriendlyByteBuf buffer) {
      super.encode(buffer);
      buffer.writeVarInt(this.id);
   }

   @Override
   public void decode(FriendlyByteBuf buffer) {
      super.decode(buffer);
      this.id = buffer.readVarInt();
   }

   public Runnable getProcessor(IPayloadContext context) {
      return () -> client(() -> WitherStormModMessageHandlerClient.processRemoveStormFromDistantRendererMessage(this));
   }

   public String toString() {
      return "RemoveStormFromDistantRendererMessage[id=" + this.id + "]";
   }
}
