package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT;
import // TODO_MIG: NetworkEvent removed, use IPayloadContext.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class RemoveAdditionalLoopingSoundMessage extends Packet {
   private int id;

   public RemoveAdditionalLoopingSoundMessage(WitherStormEntity entity) {
      super(true);
      this.id = entity.getId();
   }

   public RemoveAdditionalLoopingSoundMessage() {
      super(false);
   }

   public int getId() {
      return this.id;
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeVarInt(this.id);
   }

   public void decode(FriendlyByteBuf buffer) {
      this.id = buffer.readVarInt();
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processRemoveAdditionalLoopingSoundMessage(this));
   }

   public String toString() {
      return "RemoveAdditionalLoopingSoundMessage[id=" + this.id + "]";
   }
}
