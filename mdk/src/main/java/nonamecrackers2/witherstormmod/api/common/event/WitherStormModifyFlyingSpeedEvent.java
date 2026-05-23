package nonamecrackers2.witherstormmod.api.common.event;

import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormModifyFlyingSpeedEvent extends WitherStormEvent {
   private double originalSpeed;

   public WitherStormModifyFlyingSpeedEvent(WitherStormEntity storm, double originalSpeed) {
      super(storm);
      this.originalSpeed = originalSpeed;
   }

   public double getOriginalSpeed() {
      return this.originalSpeed;
   }

   public void setSpeed(double speed) {
      this.originalSpeed = speed;
   }
}
