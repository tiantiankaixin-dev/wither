package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.pullbehavior;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.pullbehavior.WitherStormPullBehavior;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class SlimePullBehavior extends WitherStormPullBehavior<Slime> {
   private static final double MAX_SLIME_SPEED = 5.0;
   private static final double SLIME_ROTATION_SPEED = 0.25;

   public Vec3 pullEntity(Slime slime, WitherStormEntity storm, Vec3 absorptionPoint, Vec3 defaultVelocity, double defaultSpeed) {
      if (slime.position().distanceTo(absorptionPoint) > (double)storm.getUnmodifiedWidth() * 1.5) {
         Vec3 rotationVector = absorptionPoint.subtract(slime.position()).normalize().cross(new Vec3(0.0, -1.0, 0.0)).normalize().scale(0.25);
         Vec3 delta = absorptionPoint.subtract(slime.position()).normalize();
         Vec3 itemVelocity = delta.scale(defaultSpeed).add(rotationVector);
         if (itemVelocity.length() > 5.0) {
            itemVelocity = itemVelocity.normalize().scale(5.0);
         }

         return itemVelocity;
      } else {
         return defaultVelocity;
      }
   }

   public double getSpeed(Slime entity, WitherStormEntity storm, Vec3 absorptionPoint) {
      double speed = 0.375;
      double configSpeedModifier = (Double)WitherStormModConfig.SERVER.blockClusterPullSpeedModifier.get();
      speed *= configSpeedModifier;
      return speed * Mth.clamp(entity.position().distanceTo(absorptionPoint) / configSpeedModifier, 0.1, 1.0);
   }

   public boolean canPullIn(Slime entity, WitherStormEntity storm) {
      return true;
   }

   public boolean doClientsideVelocityUpdates(Slime entity, WitherStormEntity storm) {
      return false;
   }
}
