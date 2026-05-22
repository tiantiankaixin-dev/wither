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
import net.minecraft.world.level.storage.loot.LootContext;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormTrigger extends SimpleCriterionTrigger<WitherStormTrigger.Instance> {
   private final ResourceLocation id;

   public WitherStormTrigger(ResourceLocation id) {
      this.id = id;
   }

   public ResourceLocation getId() {
      return this.id;
   }

   public void trigger(ServerPlayer player, WitherStormEntity entity) {
      LootContext context = EntityPredicate.createContext(player, entity);
      this.trigger(player, instance -> instance.matches(context));
   }

   protected WitherStormTrigger.Instance createInstance(JsonObject object, ContextAwarePredicate player, DeserializationContext parser) {
      ContextAwarePredicate entity = EntityPredicate.fromJson(object, "entity", parser);
      return new WitherStormTrigger.Instance(this.id, player, entity);
   }

   public static class Instance extends AbstractCriterionTriggerInstance {
      private final ContextAwarePredicate entity;

      public Instance(ResourceLocation id, ContextAwarePredicate player, ContextAwarePredicate entity) {
         super(id, player);
         this.entity = entity;
      }

      public boolean matches(LootContext context) {
         return this.entity.matches(context);
      }

      public JsonObject serializeToJson(SerializationContext serializer) {
         JsonObject object = super.serializeToJson(serializer);
         object.add("entity", this.entity.toJson(serializer));
         return object;
      }
   }
}
