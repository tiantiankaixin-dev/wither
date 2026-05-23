package nonamecrackers2.witherstormmod.client.audio;

import java.util.function.Predicate;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class EntitySoundLoop<T extends Entity> extends FadingSoundLoop implements IForceStoppableSound {
   public final T entity;
   protected int fadeTime;
   protected float maxVolume;
   protected final Predicate<T> shouldStop;

   public EntitySoundLoop(T entity, SoundEvent event, SoundSource category, int fadeTime, float maxVolume, Predicate<T> shouldStop) {
      super(event, category);
      this.entity = entity;
      this.fadeTime = fadeTime;
      this.maxVolume = maxVolume;
      this.shouldStop = shouldStop;
   }

   @Override
   public void tick() {
      super.tick();
      this.x = this.entity.getX();
      this.y = this.entity.getY();
      this.z = this.entity.getZ();
      if (!this.entity.isAlive() || this.shouldStop.test(this.entity)) {
         this.stopSound();
      }
   }

   @Override
   protected int getFadeTime() {
      return this.fadeTime;
   }

   @Override
   protected float maximumVolume() {
      return this.maxVolume;
   }

   @Override
   public void forceStop() {
      this.stop();
   }
}
