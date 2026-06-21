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
import net.minecraft.world.level.storage.loot.LootContext;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormTrigger extends SimpleCriterionTrigger<WitherStormTrigger.Instance> {
   private final ResourceLocation id;

   public WitherStormTrigger(ResourceLocation id) {
      this.id = id;
   }

   public ResourceLocation id() {
      return this.id;
   }

   public Codec<WitherStormTrigger.Instance> codec() {
      return WitherStormTrigger.Instance.CODEC;
   }

   public void trigger(ServerPlayer player, WitherStormEntity entity) {
      LootContext context = EntityPredicate.createContext(player, entity);
      this.trigger(player, instance -> instance.matches(context));
   }

   public record Instance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) implements SimpleInstance {
      public static final Codec<WitherStormTrigger.Instance> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
                  EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(WitherStormTrigger.Instance::player),
                  EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(WitherStormTrigger.Instance::entity)
               )
               .apply(instance, WitherStormTrigger.Instance::new)
      );

      public boolean matches(LootContext context) {
         return this.entity.isEmpty() || this.entity.get().matches(context);
      }
   }
}
