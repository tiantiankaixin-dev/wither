package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.pullbehavior;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.pullbehavior.WitherStormPullBehavior;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class ItemPullBehavior extends WitherStormPullBehavior<ItemEntity> {
   private static final double MAX_ITEM_SPEED = 5.0;
   private static final double ITEM_ROTATION_SPEED = 0.25;

   public Vec3 pullEntity(ItemEntity itemEntity, WitherStormEntity storm, Vec3 absorptionPoint, Vec3 defaultVelocity, double defaultSpeed) {
      if (itemEntity.position().distanceTo(absorptionPoint) > (double)storm.getUnmodifiedWidth() * 1.5 && absorptionPoint.distanceTo(itemEntity.position()) > 4.0) {
         Vec3 rotationVector = absorptionPoint.subtract(itemEntity.position()).normalize().cross(new Vec3(0.0, -1.0, 0.0)).normalize().scale(0.25);
         Vec3 delta = absorptionPoint.subtract(itemEntity.position()).normalize();
         Vec3 itemVelocity = delta.scale(defaultSpeed).add(rotationVector);
         if (itemVelocity.length() > 5.0) {
            itemVelocity = itemVelocity.normalize().scale(5.0);
         }

         return itemVelocity;
      } else {
         return defaultVelocity;
      }
   }

   public double getSpeed(ItemEntity entity, WitherStormEntity storm, Vec3 absorptionPoint) {
      double speed = 0.375;
      double configSpeedModifier = (Double)WitherStormModConfig.SERVER.blockClusterPullSpeedModifier.get();
      speed *= configSpeedModifier;
      return speed * Mth.clamp(entity.position().distanceTo(absorptionPoint) / configSpeedModifier, 0.1, 1.0);
   }

   public boolean canPullIn(ItemEntity entity, WitherStormEntity storm) {
      return true;
   }

   public boolean doClientsideVelocityUpdates(ItemEntity entity, WitherStormEntity storm) {
      return true;
   }
}
