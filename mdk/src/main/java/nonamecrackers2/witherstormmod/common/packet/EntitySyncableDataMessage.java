package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: NetworkEvent removed, use IPayloadContext.Context
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;
import nonamecrackers2.witherstormmod.common.util.EntitySyncableData;

public class EntitySyncableDataMessage extends Packet {
   private int id;
   private final EntitySyncableData entity;
   private FriendlyByteBuf buffer;

   public EntitySyncableDataMessage(int id, EntitySyncableData entity) {
      super(true);
      this.id = id;
      this.entity = entity;
   }

   public EntitySyncableDataMessage() {
      super(false);
      this.entity = null;
   }

   public int getId() {
      return this.id;
   }

   public FriendlyByteBuf getBuffer() {
      return this.buffer;
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeVarInt(this.id);
      this.entity.writeData(buffer);
   }

   public void decode(FriendlyByteBuf buffer) throws IllegalArgumentException, IndexOutOfBoundsException {
      this.id = buffer.readVarInt();
      this.buffer = buffer;
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processEntitySyncableDataMessage(this));
   }

   public String toString() {
      return "EntitySyncableDataMessage[entity=" + this.id + "]";
   }
}
