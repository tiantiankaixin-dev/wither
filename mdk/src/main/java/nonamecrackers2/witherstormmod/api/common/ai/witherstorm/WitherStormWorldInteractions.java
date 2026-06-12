package nonamecrackers2.witherstormmod.api.common.ai.witherstorm;
import net.neoforged.fml.ModList;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.ModList;
// 
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.clustersource.BlockClusterSource;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.pullbehavior.WitherStormPullBehavior;

public class WitherStormWorldInteractions {
   @Nullable
   private static WitherStormWorldInteractions instance;
   private final Map<EntityType<?>, WitherStormPullBehavior<?>> pullBehaviors;
   private final List<BlockClusterSource> sources;

   private WitherStormWorldInteractions(Map<EntityType<?>, WitherStormPullBehavior<?>> pullBehaviors, List<BlockClusterSource> sources) {
      this.pullBehaviors = pullBehaviors;
      this.sources = sources;
   }

   public <T extends Entity> WitherStormPullBehavior<T> getPullBehavior(EntityType<?> type) {
      return Objects.requireNonNull((WitherStormPullBehavior<T>)this.pullBehaviors.get(type), "Pull behavior does not exist for entity!");
   }

   public boolean hasPullBehavior(EntityType<?> type) {
      return this.pullBehaviors.containsKey(type);
   }

   public List<BlockClusterSource> getClusterSources() {
      return this.sources;
   }

   public static void initialize() {
      if (instance != null) {
         throw new IllegalStateException("Cluster interactions have already been initialized!");
      } else {
         List<RegisterWorldInteractionsEvent> postedEvents = Lists.newArrayList();
         Builder<EntityType<?>, WitherStormPullBehavior<?>> pullBehaviors = ImmutableMap.builder();
         com.google.common.collect.ImmutableList.Builder<BlockClusterSource> sources = ImmutableList.builder();
         ModList.get().runEventGenerator(mod -> {
            RegisterWorldInteractionsEvent eventx = new RegisterWorldInteractionsEvent();
            postedEvents.add(eventx);
            return eventx;
         });

         for (RegisterWorldInteractionsEvent event : postedEvents) {
            pullBehaviors.putAll(event.pullBehaviors);
            sources.addAll(event.sources);
         }

         instance = new WitherStormWorldInteractions(pullBehaviors.buildKeepingLast(), sources.build());
      }
   }

   public static WitherStormWorldInteractions getInstance() {
      return Objects.requireNonNull(instance, "Cluster interactions have not been initialized yet");
   }
}
