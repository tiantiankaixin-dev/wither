package nonamecrackers2.witherstormmod.api.common.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class CanWitherStormTargetMobEvent extends WitherStormEvent implements ICancellableEvent {
   private final LivingEntity entity;

   public CanWitherStormTargetMobEvent(WitherStormEntity storm, LivingEntity entity) {
      super(storm);
      this.entity = entity;
   }

   public LivingEntity getPotentialTarget() {
      return this.entity;
   }
}
