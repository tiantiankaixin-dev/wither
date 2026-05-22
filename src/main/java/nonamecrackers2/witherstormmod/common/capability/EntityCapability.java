package nonamecrackers2.witherstormmod.common.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public abstract class EntityCapability<E extends EntityCapability<E, T>, T extends Entity> {
   protected final T entity;

   public EntityCapability(T entity) {
      this.entity = entity;
   }

   public EntityCapability() {
      this.entity = null;
   }

   public abstract void tick();

   public abstract CompoundTag write();

   public abstract void read(CompoundTag var1);

   public abstract void copyFrom(E var1);

   public static class Serializable<O extends EntityCapability<?, ?>, C extends Capability<O>> implements ICapabilitySerializable<Tag> {
      private final C capability;
      private final LazyOptional<O> optional;

      public Serializable(C capability, LazyOptional<O> optional) {
         this.capability = capability;
         this.optional = optional;
      }

      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
         return cap == this.capability ? this.optional.cast() : LazyOptional.empty();
      }

      public Tag serializeNBT() {
         return this.optional.isPresent() ? ((EntityCapability)this.optional.orElse(null)).write() : null;
      }

      public void deserializeNBT(Tag nbt) {
         if (this.optional.isPresent()) {
            ((EntityCapability)this.optional.orElse(null)).read((CompoundTag)nbt);
         }
      }
   }
}
