package nonamecrackers2.witherstormmod.common.entity.bossfight;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class BossfightManager<T extends Entity> {
   private final T entity;
   private final Int2ObjectMap<BossfightManager.AdvanceableBossfightPhase<T>> phases = new Int2ObjectOpenHashMap();
   @Nullable
   private BossfightManager.AdvanceableBossfightPhase<T> currentPhase;
   private int phaseIndex;
   private int ticksSincePhaseInit;

   private BossfightManager(BossfightPhase<T> defaultPhase, BossfightManager.AdvanceableBossfightPhase.PhaseAdvancement advancement, int next, T entity) {
      this.entity = entity;
      this.addPhase(0, defaultPhase, advancement, next);
      this.setCurrentPhase(0);
   }

   public BossfightManager(BossfightPhase<T> defaultPhase, BossfightManager.AdvanceableBossfightPhase.PhaseAdvancement advancement, T entity) {
      this(defaultPhase, advancement, 0, entity);
      if (advancement == BossfightManager.AdvanceableBossfightPhase.PhaseAdvancement.SPECIFIED) {
         throw new IllegalArgumentException("Use BossFightManager(defaultPhase, next, entity) instead");
      }
   }

   public BossfightManager(BossfightPhase<T> defaultPhase, int next, T entity) {
      this(defaultPhase, BossfightManager.AdvanceableBossfightPhase.PhaseAdvancement.SPECIFIED, next, entity);
   }

   public BossfightManager(BossfightPhase<T> defaultPhase, T entity) {
      this(defaultPhase, BossfightManager.AdvanceableBossfightPhase.PhaseAdvancement.NEXT, 0, entity);
   }

   private BossfightManager<T> addPhase(int key, BossfightPhase<T> phase, BossfightManager.AdvanceableBossfightPhase.PhaseAdvancement advancement, int next) {
      this.phases.put(key, new BossfightManager.AdvanceableBossfightPhase<>(phase, advancement, key));
      return this;
   }

   public BossfightManager<T> addPhase(int key, BossfightPhase<T> phase, BossfightManager.AdvanceableBossfightPhase.PhaseAdvancement advancement) {
      if (advancement == BossfightManager.AdvanceableBossfightPhase.PhaseAdvancement.SPECIFIED) {
         throw new IllegalArgumentException("Use addPhase(key, phase, next) instead");
      } else {
         return this.addPhase(key, phase, advancement, 0);
      }
   }

   public BossfightManager<T> addPhase(int key, BossfightPhase<T> phase, int next) {
      return this.addPhase(key, phase, BossfightManager.AdvanceableBossfightPhase.PhaseAdvancement.SPECIFIED, next);
   }

   public BossfightManager<T> addPhase(int key, BossfightPhase<T> phase) {
      return this.addPhase(key, phase, BossfightManager.AdvanceableBossfightPhase.PhaseAdvancement.NEXT, 0);
   }

   public void tick() {
      this.ticksSincePhaseInit++;
      this.currentPhase.getPhase().tick(this, this.entity);
      if (this.currentPhase.getPhase().shouldAdvance(this, this.entity)) {
         this.currentPhase.advance(this);
      }
   }

   public void goToNextPhase() {
      int nextPhase = this.phaseIndex + 1;
      if (nextPhase > this.phases.size() - 1) {
         nextPhase = 0;
      }

      this.setCurrentPhase(nextPhase);
   }

   public void goToPreviousPhase() {
      int nextPhase = this.phaseIndex - 1;
      if (nextPhase < 0) {
         nextPhase = this.phases.size() - 1;
      }

      this.setCurrentPhase(nextPhase);
   }

   public void setCurrentPhase(int key) {
      if (this.currentPhase != null) {
         this.currentPhase.getPhase().finish(this, this.entity);
      }

      this.phaseIndex = key;
      this.currentPhase = (BossfightManager.AdvanceableBossfightPhase<T>)this.phases.get(key);
      this.currentPhase.getPhase().init(this, this.entity);
      this.ticksSincePhaseInit = 0;
   }

   public void setCurrentFromNextInOrder(BossfightPhase<T> phase) {
      ObjectIterator var2 = this.phases.int2ObjectEntrySet().iterator();

      while (var2.hasNext()) {
         Entry<BossfightManager.AdvanceableBossfightPhase<T>> entry = (Entry<BossfightManager.AdvanceableBossfightPhase<T>>)var2.next();
         BossfightManager.AdvanceableBossfightPhase<T> value = (BossfightManager.AdvanceableBossfightPhase<T>)entry.getValue();
         if (value.getPhase() == phase) {
            this.setCurrentPhase(entry.getIntKey());
            break;
         }
      }
   }

   public BossfightPhase<T> getPhase(int key) {
      return ((BossfightManager.AdvanceableBossfightPhase)this.phases.get(key)).getPhase();
   }

   public int getTicksSincePhaseInit() {
      return this.ticksSincePhaseInit;
   }

   public BossfightPhase<T> getCurrentPhase() {
      return this.currentPhase.getPhase();
   }

   public int getCurrentPhaseIndex() {
      return this.phaseIndex;
   }

   public CompoundTag write() {
      CompoundTag compound = new CompoundTag();
      compound.putInt("CurrentPhase", this.phaseIndex);
      compound.putInt("PhaseTicks", this.ticksSincePhaseInit);
      return compound;
   }

   public void read(CompoundTag compound) {
      this.setCurrentPhase(compound.getInt("CurrentPhase"));
      this.ticksSincePhaseInit = compound.getInt("PhaseTicks");
   }

   public static class AdvanceableBossfightPhase<T extends Entity> {
      private final BossfightPhase<T> phase;
      private final BossfightManager.AdvanceableBossfightPhase.PhaseAdvancement phaseAdvancement;
      private final int nextPhase;

      public AdvanceableBossfightPhase(BossfightPhase<T> phase, BossfightManager.AdvanceableBossfightPhase.PhaseAdvancement advancement, int next) {
         this.phase = phase;
         this.phaseAdvancement = advancement;
         this.nextPhase = next;
      }

      public void advance(BossfightManager<T> manager) {
         this.phaseAdvancement.toNextPhase(manager, this);
      }

      public int getPhaseToGoTo() {
         return this.nextPhase;
      }

      public BossfightPhase<T> getPhase() {
         return this.phase;
      }

      public static enum PhaseAdvancement {
         NEXT {
            @Override
            public <T extends Entity> void toNextPhase(BossfightManager<T> manager, BossfightManager.AdvanceableBossfightPhase<T> phase) {
               manager.goToNextPhase();
            }
         },
         PREVIOUS {
            @Override
            public <T extends Entity> void toNextPhase(BossfightManager<T> manager, BossfightManager.AdvanceableBossfightPhase<T> phase) {
               manager.goToPreviousPhase();
            }
         },
         SPECIFIED {
            @Override
            public <T extends Entity> void toNextPhase(BossfightManager<T> manager, BossfightManager.AdvanceableBossfightPhase<T> phase) {
               manager.setCurrentPhase(phase.getPhaseToGoTo());
            }
         };

         public abstract <T extends Entity> void toNextPhase(BossfightManager<T> var1, BossfightManager.AdvanceableBossfightPhase<T> var2);
      }
   }
}
