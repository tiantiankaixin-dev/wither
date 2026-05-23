package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.pullbehavior;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.pullbehavior.WitherStormPullBehavior;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;

public class BlockClusterPullBehavior extends WitherStormPullBehavior<BlockClusterEntity> {
   private static final double HUNCH_MAX_CLUSTER_SPEED = 2.0;
   private static final double MAX_CLUSTER_SPEED = 5.0;
   private static final double CLUSTER_TIME = 15.0;

   public Vec3 pullEntity(BlockClusterEntity cluster, WitherStormEntity storm, Vec3 absorptionPoint, Vec3 defaultVelocity, double defaultSpeed) {
      cluster.setFadePos(storm.blockPosition());
      if (storm.getPhase() > 3) {
         cluster.setFadeStrength(75.0F);
         cluster.setFadeDistanceOffset(20);
      }

      if (cluster.position().distanceTo(absorptionPoint) > (double)storm.getUnmodifiedWidth() * 1.5) {
         double clusterRotationSpeed = this.getClusterRotationSpeed(cluster, storm);
         if (storm.getPhase() <= 3) {
            clusterRotationSpeed = 0.2;
         }

         boolean rotateCounterClockwise = (Boolean)WitherStormModConfig.SERVER.canClustersSpiralCounterClockwise.get()
            && cluster.getTags().contains("RotateClockwise");
         Vec3 rotationVector = absorptionPoint.subtract(cluster.position())
            .normalize()
            .cross(new Vec3(0.0, rotateCounterClockwise ? 1.0 : -1.0, 0.0))
            .normalize()
            .scale(clusterRotationSpeed);
         if (cluster.createdFromTractorBeam() && cluster.position().distanceTo(storm.getEyePosition()) > 25.0) {
            int head = cluster.getHeadCreatedFrom();
            if (storm.tractorBeamActive(head)) {
               Vec3 pos = TractorBeamHelper.calculateClosestPoint(cluster.position(), storm, head);
               double distance = Math.sqrt(cluster.position().distanceToSqr(pos));
               double distanceFromHead = Math.sqrt(cluster.position().distanceToSqr(storm.getHeadPos(head)));
               double tractDistance = 4.0 * (distanceFromHead + 20.0) * 0.015;
               double threshold = cluster.getTractorBeamDistanceThreshold() * Mth.clamp((distanceFromHead - 60.0) * 0.1, 0.0, 1.0);
               double tractorAlignSpeed = Mth.clamp(distance + threshold - tractDistance, 0.0, 4.0);
               Vec3 delta = pos.subtract(cluster.position()).normalize().scale(tractorAlignSpeed);
               Vec3 toHeadDelta = storm.getHeadPos(cluster.getHeadCreatedFrom()).subtract(cluster.position()).normalize().scale(defaultSpeed);
               return toHeadDelta.add(delta);
            } else {
               return defaultVelocity.add(rotationVector);
            }
         } else {
            return defaultVelocity.add(rotationVector);
         }
      } else {
         return defaultVelocity;
      }
   }

   private double getClusterRotationSpeed(BlockClusterEntity cluster, WitherStormEntity storm) {
      double rotationSpeed = 2.0E-4 + (double)cluster.time * 15.0 * 4.0;
      int size = cluster.getSize();
      double clusterSize = Math.pow((double)size, -0.25);
      double clusterRotationSpeed;
      if (storm.getPhase() <= 3) {
         clusterRotationSpeed = Math.min(rotationSpeed * 10000.0 + (double)cluster.time * 15.0 * clusterSize, clusterSize);
      } else {
         clusterRotationSpeed = Math.min(rotationSpeed + (double)cluster.time * 15.0 * clusterSize, clusterSize);
      }

      if (clusterRotationSpeed > 5.0 && storm.getPhase() >= 4) {
         clusterRotationSpeed = 5.0;
      } else if (clusterRotationSpeed > 2.0 && storm.getPhase() <= 3) {
         clusterRotationSpeed = 2.0;
      }

      return clusterRotationSpeed;
   }

   public double getSpeed(BlockClusterEntity entity, WitherStormEntity storm, Vec3 absorptionPoint) {
      Vec3 delta = absorptionPoint.subtract(entity.position());
      double distanceToStorm = delta.length();
      double speed;
      if (storm.getPhase() <= 3) {
         speed = 0.125;
      } else if (distanceToStorm >= 240.0 && !entity.createdFromTractorBeam()) {
         speed = 0.375;
      } else {
         speed = 0.0625;
      }

      speed += (double)entity.time * 0.005;
      double configSpeedModifier;
      if (entity.createdFromTractorBeam()) {
         configSpeedModifier = (Double)WitherStormModConfig.SERVER.tractorBeamClusterSpeedModifier.get();
      } else {
         configSpeedModifier = (Double)WitherStormModConfig.SERVER.blockClusterPullSpeedModifier.get();
      }

      speed *= configSpeedModifier;
      return speed * Mth.clamp(entity.position().distanceTo(absorptionPoint) / configSpeedModifier, 0.1, 1.0);
   }

   public boolean canPullIn(BlockClusterEntity entity, WitherStormEntity storm) {
      return entity.getShakeTime() <= 0;
   }

   public boolean doClientsideVelocityUpdates(BlockClusterEntity entity, WitherStormEntity storm) {
      return true;
   }
}
