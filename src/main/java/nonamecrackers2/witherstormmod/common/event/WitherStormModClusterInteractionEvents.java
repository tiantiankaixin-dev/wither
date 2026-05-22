package nonamecrackers2.witherstormmod.common.event;

import net.minecraft.world.entity.EntityType;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.RegisterWorldInteractionsEvent;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.clustersource.DefaultClusterSource;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.clustersource.HunchbackClusterSource;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.clustersource.NatureClusterSource;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.clustersource.SmallClusterSource;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.pullbehavior.BlockClusterPullBehavior;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.pullbehavior.ItemPullBehavior;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.pullbehavior.SlimePullBehavior;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;

public class WitherStormModClusterInteractionEvents {
   public static void registerClusterInteractions(RegisterWorldInteractionsEvent event) {
      event.registerPullBehavior(EntityType.SLIME, new SlimePullBehavior());
      event.registerPullBehavior(EntityType.ITEM, new ItemPullBehavior());
      event.registerPullBehavior((EntityType<?>)WitherStormModEntityTypes.BLOCK_CLUSTER.get(), new BlockClusterPullBehavior());
      event.registerBlockClusterSource(new DefaultClusterSource());
      event.registerBlockClusterSource(new HunchbackClusterSource());
      event.registerBlockClusterSource(new SmallClusterSource());
      event.registerBlockClusterSource(new NatureClusterSource());
   }
}
