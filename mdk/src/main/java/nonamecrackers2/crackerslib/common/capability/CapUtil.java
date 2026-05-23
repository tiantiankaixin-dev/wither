package nonamecrackers2.crackerslib.common.capability;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.capabilities.ICapabilitySerializable;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: LazyOptional removed, new Capability API returns T or null;
import net.neoforged.neoforge.common.util.NonNullSupplier;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: AttachCapabilitiesEvent removed, use RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

public class CapUtil {
   public static void registerCap(AttachCapabilitiesEvent<?> event, ResourceLocation id, final Capability<?> cap, @Nullable NonNullSupplier<?> object) {
      final LazyOptional<?> optional = LazyOptional.of(object);
      event.addCapability(id, new ICapabilityProvider() {
         @NotNull
         public <M> LazyOptional<M> getCapability(@NotNull Capability<M> in, @Nullable Direction side) {
            return in == cap ? optional.cast() : null /* MIG_LAZYOPT */;
         }
      });
      event.addListener(optional::invalidate);
   }

   public static <T extends TagSerializable> void registerSerializableCap(
      AttachCapabilitiesEvent<?> event, ResourceLocation id, final Capability<?> cap, @Nullable NonNullSupplier<T> object
   ) {
      final LazyOptional<T> optional = LazyOptional.of(object);
      event.addCapability(id, new ICapabilitySerializable<CompoundTag>() {
         @NotNull
         public <M> LazyOptional<M> getCapability(@NotNull Capability<M> in, @Nullable Direction side) {
            return in == cap ? optional.cast() : null /* MIG_LAZYOPT */;
         }

         public CompoundTag serializeNBT() {
            return ((TagSerializable)optional.orElse(null)).write();
         }

         public void deserializeNBT(CompoundTag nbt) {
            ((TagSerializable)optional.orElse(null)).read(nbt);
         }
      });
   }
}
