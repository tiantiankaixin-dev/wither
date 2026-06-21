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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class SummonMobSuperBeaconTrigger extends SimpleCriterionTrigger<SummonMobSuperBeaconTrigger.Instance> {
   private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("witherstormmod", "summon_mob_withered_beacon");

   public ResourceLocation id() {
      return ID;
   }

   public Codec<SummonMobSuperBeaconTrigger.Instance> codec() {
      return SummonMobSuperBeaconTrigger.Instance.CODEC;
   }

   public void trigger(ServerPlayer player, Entity summoned) {
      LootContext context = EntityPredicate.createContext(player, summoned);
      this.trigger(player, instance -> instance.matches(context));
   }

   public record Instance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> resummoned) implements SimpleInstance {
      public static final Codec<SummonMobSuperBeaconTrigger.Instance> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
                  EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(SummonMobSuperBeaconTrigger.Instance::player),
                  EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("resummoned").forGetter(SummonMobSuperBeaconTrigger.Instance::resummoned)
               )
               .apply(instance, SummonMobSuperBeaconTrigger.Instance::new)
      );

      public boolean matches(LootContext context) {
         return this.resummoned.isEmpty() || this.resummoned.get().matches(context);
      }
   }
}
