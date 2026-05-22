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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.storage.loot.LootContext;

public class CuredSickenedMobTrigger extends SimpleCriterionTrigger<CuredSickenedMobTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("witherstormmod", "cured_sickened_mob");

   public ResourceLocation getId() {
      return ID;
   }

   public void trigger(ServerPlayer player, Mob entity, Mob conversion) {
      LootContext entityContext = EntityPredicate.createContext(player, entity);
      LootContext conversionContext = EntityPredicate.createContext(player, conversion);
      this.trigger(player, instance -> instance.matches(entityContext, conversionContext));
   }

   protected CuredSickenedMobTrigger.Instance createInstance(JsonObject object, ContextAwarePredicate player, DeserializationContext parser) {
      ContextAwarePredicate sickened = EntityPredicate.fromJson(object, "sickened", parser);
      ContextAwarePredicate conversion = EntityPredicate.fromJson(object, "converison", parser);
      return new CuredSickenedMobTrigger.Instance(ID, player, sickened, conversion);
   }

   public static class Instance extends AbstractCriterionTriggerInstance {
      private final ContextAwarePredicate sickened;
      private final ContextAwarePredicate conversion;

      public Instance(ResourceLocation id, ContextAwarePredicate player, ContextAwarePredicate sickened, ContextAwarePredicate conversion) {
         super(id, player);
         this.sickened = sickened;
         this.conversion = conversion;
      }

      public boolean matches(LootContext sickened, LootContext conversion) {
         return !this.sickened.matches(sickened) ? false : this.conversion.matches(conversion);
      }

      public JsonObject serializeToJson(SerializationContext serializer) {
         JsonObject object = super.serializeToJson(serializer);
         object.add("sickened", this.sickened.toJson(serializer));
         object.add("conversion", this.sickened.toJson(serializer));
         return object;
      }
   }
}
