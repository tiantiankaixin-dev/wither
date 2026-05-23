package nonamecrackers2.witherstormmod.api.common.ai.witherstorm.pullbehavior;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public abstract class WitherStormPullBehavior<T extends Entity> {
   private final double defaultSpeed;

   public WitherStormPullBehavior(double speed) {
      this.defaultSpeed = speed;
   }

   public WitherStormPullBehavior() {
      this(0.5);
   }

   public abstract Vec3 pullEntity(T var1, WitherStormEntity var2, Vec3 var3, Vec3 var4, double var5);

   public double getSpeed(T entity, WitherStormEntity storm, Vec3 absorptionPoint) {
      return this.defaultSpeed;
   }

   public boolean canPullIn(T entity, WitherStormEntity storm) {
      return true;
   }

   public boolean doClientsideVelocityUpdates(T entity, WitherStormEntity storm) {
      return false;
   }
}
