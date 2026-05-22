package nonamecrackers2.witherstormmod.api.common.event;

import net.minecraftforge.eventbus.api.Event;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public abstract class WitherStormEvent extends Event {
   private final WitherStormEntity storm;

   public WitherStormEvent(WitherStormEntity storm) {
      this.storm = storm;
   }

   public WitherStormEntity getEntity() {
      return this.storm;
   }
}
