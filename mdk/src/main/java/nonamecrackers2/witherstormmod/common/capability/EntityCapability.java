package nonamecrackers2.witherstormmod.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

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
}