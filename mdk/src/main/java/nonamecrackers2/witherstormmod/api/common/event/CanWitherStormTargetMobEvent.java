package nonamecrackers2.witherstormmod.api.common.event;

import net.minecraft.world.entity.LivingEntity;
// TODO_MIG[CANCELABLE]: removed; class must implement ICancellableEvent
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

// TODO_MIG[CANCELABLE]: implement ICancellableEvent on this class instead
public class CanWitherStormTargetMobEvent extends WitherStormEvent {
   private final LivingEntity entity;

   public CanWitherStormTargetMobEvent(WitherStormEntity storm, LivingEntity entity) {
      super(storm);
      this.entity = entity;
   }

   public LivingEntity getPotentialTarget() {
      return this.entity;
   }
}
