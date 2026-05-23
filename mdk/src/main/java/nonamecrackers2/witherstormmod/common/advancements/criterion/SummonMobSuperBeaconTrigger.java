package nonamecrackers2.witherstormmod.common.advancements.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class SummonMobSuperBeaconTrigger extends SimpleCriterionTrigger<SummonMobSuperBeaconTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("witherstormmod", "summon_mob_withered_beacon");

   public ResourceLocation getId() {
      return ID;
   }

   protected SummonMobSuperBeaconTrigger.Instance createInstance(JsonObject object, ContextAwarePredicate player, DeserializationContext context) {
      ContextAwarePredicate summoned = EntityPredicate.fromJson(object, "resummoned", context);
      return new SummonMobSuperBeaconTrigger.Instance(player, summoned);
   }

   public void trigger(ServerPlayer player, Entity summoned) {
      LootContext context = EntityPredicate.createContext(player, summoned);
      this.trigger(player, instance -> instance.matches(context));
   }

   public static class Instance extends AbstractCriterionTriggerInstance {
      private final ContextAwarePredicate summoned;

      public Instance(ContextAwarePredicate player, ContextAwarePredicate summoned) {
         super(SummonMobSuperBeaconTrigger.ID, player);
         this.summoned = summoned;
      }

      public boolean matches(LootContext context) {
         return this.summoned.matches(context);
      }

      public JsonObject serializeToJson(SerializationContext context) {
         JsonObject obj = super.serializeToJson(context);
         obj.add("summoned", this.summoned.toJson(context));
         return obj;
      }
   }
}
