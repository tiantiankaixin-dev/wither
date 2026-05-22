package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.clustersource;

import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec2;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.clustersource.BlockClusterSource;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class DefaultClusterSource extends BlockClusterSource {
   public DefaultClusterSource() {
      super(256);
   }

   @Override
   protected float getClusterSizeRadius(WitherStormEntity storm) {
      return storm.getClusterRadius() + (float)((Integer)WitherStormModConfig.SERVER.clusterSizeModifier.get()).intValue();
   }

   @Override
   protected int calculateShakeTime(WitherStormEntity storm, RandomSource random) {
      return random.nextInt(10) + 20;
   }

   @Override
   protected Vec2 calculateRotationDelta(WitherStormEntity storm, RandomSource random) {
      return new Vec2((float)random.nextInt(20) * 0.1F / 2.0F, (float)random.nextInt(20) * 0.1F / 2.0F);
   }

   @Override
   protected int getPickupInterval(WitherStormEntity storm) {
      if (storm.getPhase() > 3 && storm.shouldSpeedUp()) {
         return (Integer)WitherStormModConfig.SERVER.devourerClusterPickupInterval.get() * 4;
      } else {
         return storm.getPhase() < 6
            ? (Integer)WitherStormModConfig.SERVER.clusterPickupInterval.get()
            : (Integer)WitherStormModConfig.SERVER.devourerClusterPickupInterval.get();
      }
   }

   @Override
   protected boolean canUse(WitherStormEntity storm) {
      return storm.getPhase() >= 4;
   }
}
