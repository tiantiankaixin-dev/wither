package nonamecrackers2.witherstormmod.common.advancements.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds.Ints;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ActivateSuperBeaconTrigger extends SimpleCriterionTrigger<ActivateSuperBeaconTrigger.TriggerInstance> {
   private static final ResourceLocation ID = new ResourceLocation("witherstormmod", "activate_super_beacon");

   public ResourceLocation getId() {
      return ID;
   }

   protected ActivateSuperBeaconTrigger.TriggerInstance createInstance(JsonObject obj, ContextAwarePredicate player, DeserializationContext contet) {
      Ints totalActivated = Ints.fromJson(obj.get("total_activated"));
      return new ActivateSuperBeaconTrigger.TriggerInstance(player, totalActivated);
   }

   public void trigger(ServerPlayer player, int totalActivated) {
      this.trigger(player, instance -> instance.matches(totalActivated));
   }

   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
      private final Ints totalActivated;

      public TriggerInstance(ContextAwarePredicate player, Ints totalActivated) {
         super(ActivateSuperBeaconTrigger.ID, player);
         this.totalActivated = totalActivated;
      }

      public boolean matches(int activated) {
         return this.totalActivated.matches(activated);
      }

      public JsonObject serializeToJson(SerializationContext context) {
         JsonObject obj = super.serializeToJson(context);
         obj.add("level", this.totalActivated.serializeToJson());
         return obj;
      }
   }
}
