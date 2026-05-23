package nonamecrackers2.witherstormmod.common.advancements.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds.Ints;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class LinkAmuletTrigger extends SimpleCriterionTrigger<LinkAmuletTrigger.Instance> {
   private static final ResourceLocation ID = new ResourceLocation("witherstormmod", "link_amulet");

   public ResourceLocation getId() {
      return ID;
   }

   protected LinkAmuletTrigger.Instance createInstance(JsonObject object, ContextAwarePredicate player, DeserializationContext context) {
      ContextAwarePredicate linked = EntityPredicate.fromJson(object, "linked", context);
      Ints totalLinked = Ints.fromJson(object.get("total_linked"));
      return new LinkAmuletTrigger.Instance(player, linked, totalLinked);
   }

   public void trigger(ServerPlayer player, Entity linked, int totalLinked) {
      LootContext context = EntityPredicate.createContext(player, linked);
      this.trigger(player, instance -> instance.matches(context, totalLinked));
   }

   public static class Instance extends AbstractCriterionTriggerInstance {
      private final ContextAwarePredicate linked;
      private final Ints totalLinked;

      public Instance(ContextAwarePredicate player, ContextAwarePredicate linked, Ints totalLinked) {
         super(LinkAmuletTrigger.ID, player);
         this.linked = linked;
         this.totalLinked = totalLinked;
      }

      public boolean matches(LootContext context, int totalLinked) {
         return this.linked.matches(context) && this.totalLinked.matches(totalLinked);
      }

      public JsonObject serializeToJson(SerializationContext context) {
         JsonObject obj = super.serializeToJson(context);
         obj.add("linked", this.linked.toJson(context));
         obj.add("total_linked", this.totalLinked.serializeToJson());
         return obj;
      }
   }
}
