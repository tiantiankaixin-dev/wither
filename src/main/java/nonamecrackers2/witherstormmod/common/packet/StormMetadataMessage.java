package nonamecrackers2.witherstormmod.common.packet;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class StormMetadataMessage extends DistantRendererMessage {
   private int entityId;
   private List<DataValue<?>> packedItems;

   @Deprecated
   public StormMetadataMessage(List<Integer> applicable, int id, SynchedEntityData data) {
      super(true, applicable);
      this.entityId = id;
      this.packedItems = getPackedData(data);
   }

   public StormMetadataMessage(List<Integer> applicable, int id, List<DataValue<?>> packedItems) {
      super(true, applicable);
      this.entityId = id;
      this.packedItems = packedItems;
   }

   public StormMetadataMessage() {
      super(false, Lists.newArrayList());
   }

   public int getEntityID() {
      return this.entityId;
   }

   public List<DataValue<?>> getUnpackedItems() {
      return this.packedItems;
   }

   @Override
   public void encode(FriendlyByteBuf buffer) {
      super.encode(buffer);
      buffer.writeVarInt(this.entityId);

      for (DataValue<?> value : this.packedItems) {
         value.write(buffer);
      }

      buffer.writeByte(255);
   }

   @Override
   public void decode(FriendlyByteBuf buffer) {
      super.decode(buffer);
      this.entityId = buffer.readVarInt();
      List<DataValue<?>> packedData = Lists.newArrayList();

      int j;
      while ((j = buffer.readUnsignedByte()) != 255) {
         packedData.add(DataValue.read(buffer, j));
      }

      this.packedItems = packedData;
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processStormMetadataMessage(this));
   }

   public String toString() {
      return "StormMetadataMessage[id=" + this.entityId + ", data=" + this.packedItems.toString() + "]";
   }
}
