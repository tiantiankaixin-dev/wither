package nonamecrackers2.witherstormmod.api.common.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

@Cancelable
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
