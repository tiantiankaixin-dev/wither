package nonamecrackers2.witherstormmod.common.predicate;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class EntityPredicateBuilder<T extends Entity> extends PredicateBuilder<T, EntityPredicateBuilder<T>> {
   private EntityPredicateBuilder(PredicateBuilder.Comparison comparisonMethod) {
      super(comparisonMethod);
   }

   public static <T extends Entity> EntityPredicateBuilder<T> or() {
      return new EntityPredicateBuilder<>(PredicateBuilder.Comparison.OR);
   }

   public static <T extends Entity> EntityPredicateBuilder<T> and() {
      return new EntityPredicateBuilder<>(PredicateBuilder.Comparison.AND);
   }

   public EntityPredicateBuilder<T> isExactly(EntityType<? extends T> type) {
      return this.addTest(e -> e.getType().equals(type));
   }

   public EntityPredicateBuilder<T> isNotExactly(EntityType<? extends T> type) {
      return this.addTest(e -> !e.getType().equals(type));
   }

   public EntityPredicateBuilder<T> isTag(TagKey<EntityType<?>> tag) {
      return this.addTest(e -> e.getType().is(tag));
   }

   public EntityPredicateBuilder<T> isNotTag(TagKey<EntityType<?>> tag) {
      return this.addTest(e -> !e.getType().is(tag));
   }
}
