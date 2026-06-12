package nonamecrackers2.witherstormmod.common.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

@Deprecated
public class EscapeWitherStormTrigger extends SimpleCriterionTrigger<EscapeWitherStormTrigger.TriggerInstance> {
   @Override
   public Codec<TriggerInstance> codec() {
      return TriggerInstance.CODEC;
   }

   public void trigger(ServerPlayer player, WitherStormEntity entity) {
      LootContext context = EntityPredicate.createContext(player, entity);
      this.trigger(player, instance -> instance.matches(context));
   }

   public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) implements SimpleInstance {
      public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
         EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
         EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity)
      ).apply(instance, TriggerInstance::new));

      public boolean matches(LootContext context) {
         return this.entity.isEmpty() || this.entity.get().matches(context);
      }

      @Override
      public Optional<ContextAwarePredicate> player() {
         return player;
      }
   }
}
