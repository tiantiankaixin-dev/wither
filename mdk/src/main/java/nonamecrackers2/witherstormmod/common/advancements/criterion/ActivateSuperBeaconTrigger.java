package nonamecrackers2.witherstormmod.common.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.server.level.ServerPlayer;

public class ActivateSuperBeaconTrigger extends SimpleCriterionTrigger<ActivateSuperBeaconTrigger.TriggerInstance> {
   @Override
   public Codec<TriggerInstance> codec() {
      return TriggerInstance.CODEC;
   }

   public void trigger(ServerPlayer player, int totalActivated) {
      this.trigger(player, instance -> instance.matches(totalActivated));
   }

   public record TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints totalActivated) implements SimpleInstance {
      public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
         EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
         MinMaxBounds.Ints.CODEC.optionalFieldOf("total_activated", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::totalActivated)
      ).apply(instance, TriggerInstance::new));

      public boolean matches(int activated) {
         return this.totalActivated.matches(activated);
      }

      @Override
      public Optional<ContextAwarePredicate> player() {
         return player;
      }
   }
}
