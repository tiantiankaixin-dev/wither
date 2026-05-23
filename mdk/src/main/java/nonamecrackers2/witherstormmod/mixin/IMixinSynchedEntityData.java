package nonamecrackers2.witherstormmod.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.concurrent.locks.ReadWriteLock;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({SynchedEntityData.class})
public interface IMixinSynchedEntityData {
   @Accessor
   Int2ObjectMap<DataItem<?>> getItemsById();

   @Accessor
   ReadWriteLock getLock();
}
