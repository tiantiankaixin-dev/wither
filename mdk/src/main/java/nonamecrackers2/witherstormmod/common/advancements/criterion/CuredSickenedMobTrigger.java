package nonamecrackers2.witherstormmod.common.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.storage.loot.LootContext;

public class CuredSickenedMobTrigger extends SimpleCriterionTrigger<CuredSickenedMobTrigger.TriggerInstance> {
   @Override
   public Codec<TriggerInstance> codec() {
      return TriggerInstance.CODEC;
   }

   public void trigger(ServerPlayer player, Mob entity, Mob conversion) {
      LootContext entityContext = EntityPredicate.createContext(player, entity);
      LootContext conversionContext = EntityPredicate.createContext(player, conversion);
      this.trigger(player, instance -> instance.matches(entityContext, conversionContext));
   }

   public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> sickened, Optional<ContextAwarePredicate> conversion) implements SimpleInstance {
      public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
         EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
         EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("sickened").forGetter(TriggerInstance::sickened),
         EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("conversion").forGetter(TriggerInstance::conversion)
      ).apply(instance, TriggerInstance::new));

      public boolean matches(LootContext sickenedCtx, LootContext conversionCtx) {
         if (this.sickened.isPresent() && !this.sickened.get().matches(sickenedCtx)) return false;
         return this.conversion.isEmpty() || this.conversion.get().matches(conversionCtx);
      }

      @Override
      public Optional<ContextAwarePredicate> player() {
         return player;
      }
   }
}
