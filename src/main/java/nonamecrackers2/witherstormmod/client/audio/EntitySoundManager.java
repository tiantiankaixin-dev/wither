package nonamecrackers2.witherstormmod.client.audio;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.world.entity.Entity;

public abstract class EntitySoundManager<T extends Entity, L extends AbstractTickableSoundInstance & IForceStoppableSound> implements ISoundManager {
   protected final Minecraft minecraft;
   protected final Class<T> entityClass;
   protected final List<L> loops = new ArrayList<>();

   public EntitySoundManager(Minecraft minecraft, Class<T> entityClass) {
      this.minecraft = minecraft;
      this.entityClass = entityClass;
   }

   @Override
   public void tick() {
      ClientLevel world = this.minecraft.level;

      for (Entity entity : world.entitiesForRendering()) {
         if (this.entityClass.isInstance(entity) && this.canPlay((T)entity)) {
            this.putLoop(this.create((T)entity));
         }
      }

      for (int i = 0; i < this.loops.size(); i++) {
         L loop = this.loops.get(i);
         if (loop.isStopped() || loop instanceof FadingSoundLoop && ((FadingSoundLoop)loop).isStopping()) {
            this.loops.remove(i);
         }
      }
   }

   protected abstract boolean alreadyHasLoop(T var1);

   protected void putLoop(L loop) {
      if (!this.loops.contains(loop)) {
         this.loops.add(loop);
         this.minecraft.getSoundManager().queueTickingSound(loop);
      }
   }

   protected boolean canPlay(T entity) {
      return !this.alreadyHasLoop(entity) && entity.isAlive();
   }

   @Override
   public void refresh() {
      List<L> soundsToAdd = new ArrayList<>();

      for (int i = 0; i < this.loops.size(); i++) {
         L loop = this.loops.get(i);
         L newLoop = this.copyFrom(loop);
         loop.forceStop();
         soundsToAdd.add(newLoop);
         this.minecraft.getSoundManager().queueTickingSound(newLoop);
      }

      this.loops.clear();
      this.loops.addAll(soundsToAdd);
   }

   protected abstract L create(T var1);

   protected abstract L copyFrom(L var1);
}
