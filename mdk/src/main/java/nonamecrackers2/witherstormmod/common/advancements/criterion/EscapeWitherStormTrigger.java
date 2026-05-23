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

@Deprecated
public class EscapeWitherStormTrigger extends SimpleCriterionTrigger<EscapeWitherStormTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("witherstormmod", "escape_wither_storm");

   public ResourceLocation getId() {
      return ID;
   }

   public void trigger(ServerPlayer player, WitherStormEntity entity) {
      LootContext context = EntityPredicate.createContext(player, entity);
      this.trigger(player, instance -> instance.matches(context));
   }

   protected EscapeWitherStormTrigger.Instance createInstance(JsonObject object, ContextAwarePredicate player, DeserializationContext parser) {
      ContextAwarePredicate entity = EntityPredicate.fromJson(object, "entity", parser);
      return new EscapeWitherStormTrigger.Instance(ID, player, entity);
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
