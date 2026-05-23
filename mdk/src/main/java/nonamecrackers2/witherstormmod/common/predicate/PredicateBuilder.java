package nonamecrackers2.witherstormmod.common.predicate;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Predicate;

public abstract class PredicateBuilder<T, B extends PredicateBuilder<T, B>> {
   private final List<Predicate<T>> predicates = Lists.newArrayList();
   private final PredicateBuilder.Comparison comparisonMethod;

   protected PredicateBuilder(PredicateBuilder.Comparison comparisonMethod) {
      this.comparisonMethod = comparisonMethod;
   }

   public B addTest(Predicate<T> predicate) {
      this.predicates.add(predicate);
      return (B)this;
   }

   public B isInstanceOf(Class<?> clazz) {
      return this.addTest(e -> clazz.isAssignableFrom(e.getClass()));
   }

   public B isNotInstanceOf(Class<?> clazz) {
      return this.addTest(e -> !clazz.isAssignableFrom(e.getClass()));
   }

   public B isExactly(Class<? extends T> clazz) {
      return this.addTest(e -> e.getClass().equals(clazz));
   }

   public B isNotExactly(Class<? extends T> clazz) {
      return this.addTest(e -> !e.getClass().equals(clazz));
   }

   public Predicate<T> build() {
      return this.comparisonMethod.combine(this.predicates);
   }

   public static enum Comparison {
      OR {
         @Override
         protected <T> Predicate<T> combine(Predicate<T> current, Predicate<T> other) {
            return current.or(other);
         }
      },
      AND {
         @Override
         protected <T> Predicate<T> combine(Predicate<T> current, Predicate<T> other) {
            return current.and(other);
         }
      };

      protected abstract <T> Predicate<T> combine(Predicate<T> var1, Predicate<T> var2);

      public <T> Predicate<T> combine(List<Predicate<T>> predicates) {
         if (predicates.isEmpty()) {
            return o -> true;
         } else {
            Predicate<T> base = predicates.get(0);

            for (int i = 1; i < predicates.size(); i++) {
               base = this.combine(base, predicates.get(i));
            }

            return base;
         }
      }
   }
}
