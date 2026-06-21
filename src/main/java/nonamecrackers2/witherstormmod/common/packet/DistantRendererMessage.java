package nonamecrackers2.witherstormmod.common.packet;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataItem;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import nonamecrackers2.witherstormmod.common.network.Packet;
import nonamecrackers2.witherstormmod.mixin.IMixinSynchedEntityData;

public abstract class DistantRendererMessage extends Packet {
   private List<Integer> applicable;

   protected DistantRendererMessage(boolean valid, List<Integer> applicable) {
      super(valid);
      this.applicable = applicable;
   }

   public void decode(FriendlyByteBuf buffer) throws IllegalArgumentException, IndexOutOfBoundsException {
      int length = buffer.readVarInt();
      List<Integer> applicable = Lists.newArrayList();

      for (int i = 0; i < length; i++) {
         applicable.add(buffer.readVarInt());
      }

      this.applicable = applicable;
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeVarInt(this.applicable.size());
      this.applicable.forEach(buffer::writeVarInt);
   }

   public List<Integer> getApplicable() {
      return this.applicable;
   }

   protected static List<DataValue<?>> getPackedData(SynchedEntityData data) {
      IMixinSynchedEntityData mixinData = (IMixinSynchedEntityData)data;
      List<DataValue<?>> values = Lists.newArrayList();

      for (DataItem<?> item : mixinData.getItemsById()) {
         if (item != null) {
            values.add(item.value());
         }
      }

      return values;
   }
}
