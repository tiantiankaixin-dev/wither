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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class LinkAmuletTrigger extends SimpleCriterionTrigger<LinkAmuletTrigger.Instance> {
   private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("witherstormmod", "link_amulet");

   public ResourceLocation id() {
      return ID;
   }

   public Codec<LinkAmuletTrigger.Instance> codec() {
      return LinkAmuletTrigger.Instance.CODEC;
   }

   public void trigger(ServerPlayer player, Entity linked, int totalLinked) {
      LootContext context = EntityPredicate.createContext(player, linked);
      this.trigger(player, instance -> instance.matches(context, totalLinked));
   }

   public record Instance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> linked, Ints totalLinked) implements SimpleInstance {
      public static final Codec<LinkAmuletTrigger.Instance> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
                  EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(LinkAmuletTrigger.Instance::player),
                  EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("linked").forGetter(LinkAmuletTrigger.Instance::linked),
                  Ints.CODEC.optionalFieldOf("total_linked", Ints.ANY).forGetter(LinkAmuletTrigger.Instance::totalLinked)
               )
               .apply(instance, LinkAmuletTrigger.Instance::new)
      );

      public boolean matches(LootContext context, int totalLinked) {
         return (this.linked.isEmpty() || this.linked.get().matches(context)) && this.totalLinked.matches(totalLinked);
      }
   }
}
