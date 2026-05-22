package nonamecrackers2.witherstormmod.api.common.ai.witherstorm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.clustersource.BlockClusterSource;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.pullbehavior.WitherStormPullBehavior;

public class RegisterWorldInteractionsEvent extends Event implements IModBusEvent {
   protected final Map<EntityType<?>, WitherStormPullBehavior<?>> pullBehaviors = Maps.newHashMap();
   protected final List<BlockClusterSource> sources = Lists.newArrayList();

   public void registerPullBehavior(EntityType<?> type, WitherStormPullBehavior<?> behavior) {
      if (this.pullBehaviors.containsKey(type)) {
         throw new IllegalArgumentException("Type '" + type + "' is already registered");
      } else {
         this.pullBehaviors.put(type, behavior);
      }
   }

   public void registerBlockClusterSource(BlockClusterSource source) {
      this.sources.add(source);
   }
}
