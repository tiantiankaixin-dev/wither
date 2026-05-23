package nonamecrackers2.witherstormmod.common.entity.bossfight;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;

public class BossfightPhase<T extends Entity> {
   @Nullable
   private BossfightPhase<T> parent;
   private boolean hasFixedTime;
   private int time;
   private Predicate<T> shouldMoveToNextPhase;
   private Consumer<T> initAction;
   private Consumer<T> finishAction = entity -> {
   };
   private BiConsumer<Integer, T> action = (time, entity) -> {
   };

   public BossfightPhase(Consumer<T> initAction, Predicate<T> shouldMoveToNextPhase) {
      this.initAction = initAction;
      this.shouldMoveToNextPhase = shouldMoveToNextPhase;
   }

   public BossfightPhase(Predicate<T> shouldMoveToNextPhase) {
      this(entity -> {
      }, shouldMoveToNextPhase);
   }

   public BossfightPhase(Consumer<T> initAction, int time) {
      this(initAction, entity -> false);
      this.setFixedTime(time);
   }

   public static <T extends Entity> BossfightPhase<T> blank() {
      return new BossfightPhase<>(entity -> false);
   }

   public static <T extends Entity> BossfightPhase<T> copyOf(BossfightPhase<T> parent, Consumer<T> initAction, Predicate<T> shouldMoveToNextPhase) {
      BossfightPhase<T> phase = new BossfightPhase<>(parent.initAction.andThen(initAction), parent.shouldMoveToNextPhase.or(shouldMoveToNextPhase))
         .setTickAction(parent.action)
         .setFinishAction(parent.finishAction);
      if (parent.hasFixedTime) {
         phase.setFixedTime(parent.time);
      }

      phase.parent = parent;
      return phase;
   }

   public BossfightPhase<T> setTickAction(BiConsumer<Integer, T> action) {
      this.action = action;
      return this;
   }

   public BossfightPhase<T> setFinishAction(Consumer<T> action) {
      this.finishAction = action;
      return this;
   }

   public BossfightPhase<T> setFixedTime(int time) {
      this.hasFixedTime = true;
      this.time = time;
      return this;
   }

   public void init(BossfightManager<T> manager, T entity) {
      this.initAction.accept(entity);
   }

   public void tick(BossfightManager<T> manager, T entity) {
      this.action.accept(manager.getTicksSincePhaseInit(), entity);
   }

   public void finish(BossfightManager<T> manager, T entity) {
      this.finishAction.accept(entity);
   }

   public boolean shouldAdvance(BossfightManager<T> manager, T entity) {
      return this.hasFixedTime ? manager.getTicksSincePhaseInit() > this.time : this.shouldMoveToNextPhase.test(entity);
   }

   @Override
   public boolean equals(Object obj) {
      if (super.equals(obj)) {
         return true;
      } else if (this.parent != null) {
         for (BossfightPhase<T> current = this.parent; current != null; current = current.parent) {
            if (current == obj) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
