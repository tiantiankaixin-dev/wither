package nonamecrackers2.witherstormmod.common.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger.SimpleInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.storage.loot.LootContext;

public class CuredSickenedMobTrigger extends SimpleCriterionTrigger<CuredSickenedMobTrigger.Instance> {
   private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("witherstormmod", "cured_sickened_mob");

   public ResourceLocation id() {
      return ID;
   }

   public Codec<CuredSickenedMobTrigger.Instance> codec() {
      return CuredSickenedMobTrigger.Instance.CODEC;
   }

   public void trigger(ServerPlayer player, Mob entity, Mob conversion) {
      LootContext entityContext = EntityPredicate.createContext(player, entity);
      LootContext conversionContext = EntityPredicate.createContext(player, conversion);
      this.trigger(player, instance -> instance.matches(entityContext, conversionContext));
   }

   public record Instance(
      Optional<ContextAwarePredicate> player,
      Optional<ContextAwarePredicate> sickened,
      Optional<ContextAwarePredicate> conversion
   ) implements SimpleInstance {
      public static final Codec<CuredSickenedMobTrigger.Instance> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
                  EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(CuredSickenedMobTrigger.Instance::player),
                  EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("sickened").forGetter(CuredSickenedMobTrigger.Instance::sickened),
                  EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("conversion").forGetter(CuredSickenedMobTrigger.Instance::conversion)
               )
               .apply(instance, CuredSickenedMobTrigger.Instance::new)
      );

      public boolean matches(LootContext sickenedContext, LootContext conversionContext) {
         boolean sickenedMatches = this.sickened.isEmpty() || this.sickened.get().matches(sickenedContext);
         boolean conversionMatches = this.conversion.isEmpty() || this.conversion.get().matches(conversionContext);
         return sickenedMatches && conversionMatches;
      }
   }
}
