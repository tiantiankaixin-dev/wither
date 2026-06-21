package nonamecrackers2.witherstormmod.common.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds.Ints;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger.SimpleInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ActivateSuperBeaconTrigger extends SimpleCriterionTrigger<ActivateSuperBeaconTrigger.TriggerInstance> {
   private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("witherstormmod", "activate_super_beacon");

   public ResourceLocation id() {
      return ID;
   }

   public Codec<ActivateSuperBeaconTrigger.TriggerInstance> codec() {
      return ActivateSuperBeaconTrigger.TriggerInstance.CODEC;
   }

   public void trigger(ServerPlayer player, int totalActivated) {
      this.trigger(player, instance -> instance.matches(totalActivated));
   }

   public record TriggerInstance(Optional<ContextAwarePredicate> player, Ints totalActivated) implements SimpleInstance {
      public static final Codec<ActivateSuperBeaconTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
                  EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ActivateSuperBeaconTrigger.TriggerInstance::player),
                  Ints.CODEC.optionalFieldOf("total_activated", Ints.ANY).forGetter(ActivateSuperBeaconTrigger.TriggerInstance::totalActivated)
               )
               .apply(instance, ActivateSuperBeaconTrigger.TriggerInstance::new)
      );

      public boolean matches(int activated) {
         return this.totalActivated.matches(activated);
      }
   }
}
