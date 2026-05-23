package nonamecrackers2.witherstormmod.common.util;

import net.minecraft.network.FriendlyByteBuf;

public interface EntitySyncableData {
   void writeData(FriendlyByteBuf var1);

   void readData(FriendlyByteBuf var1);
}
