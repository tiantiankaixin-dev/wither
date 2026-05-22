package nonamecrackers2.witherstormmod.common.packet;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataItem;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import nonamecrackers2.crackerslib.common.packet.Packet;
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
      mixinData.getLock().readLock().lock();
      ObjectIterator var3 = mixinData.getItemsById().values().iterator();

      while (var3.hasNext()) {
         DataItem<?> item = (DataItem<?>)var3.next();
         values.add(item.value());
      }

      mixinData.getLock().readLock().unlock();
      return values;
   }
}
