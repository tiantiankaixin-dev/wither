package nonamecrackers2.witherstormmod.common.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class LinkAmuletTrigger extends SimpleCriterionTrigger<LinkAmuletTrigger.TriggerInstance> {
   @Override
   public Codec<TriggerInstance> codec() {
      return TriggerInstance.CODEC;
   }

   public void trigger(ServerPlayer player, Entity linked, int totalLinked) {
      LootContext context = EntityPredicate.createContext(player, linked);
      this.trigger(player, instance -> instance.matches(context, totalLinked));
   }

   public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> linked, MinMaxBounds.Ints totalLinked) implements SimpleInstance {
      public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
         EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
         EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("linked").forGetter(TriggerInstance::linked),
         MinMaxBounds.Ints.CODEC.optionalFieldOf("total_linked", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::totalLinked)
      ).apply(instance, TriggerInstance::new));

      public boolean matches(LootContext context, int totalLinked) {
         if (this.linked.isPresent() && !this.linked.get().matches(context)) return false;
         return this.totalLinked.matches(totalLinked);
      }

      @Override
      public Optional<ContextAwarePredicate> player() {
         return player;
      }
   }
}
