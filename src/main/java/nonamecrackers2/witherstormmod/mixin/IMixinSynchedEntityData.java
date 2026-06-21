package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({SynchedEntityData.class})
public interface IMixinSynchedEntityData {
   @Accessor
   DataItem<?>[] getItemsById();
}
