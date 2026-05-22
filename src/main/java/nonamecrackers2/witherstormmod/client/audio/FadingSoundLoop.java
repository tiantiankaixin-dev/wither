package nonamecrackers2.witherstormmod.client.audio;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public abstract class FadingSoundLoop extends AbstractTickableSoundInstance {
   protected float fade;
   protected boolean stopping;
   protected boolean starting = true;
   protected float dampen;

   protected FadingSoundLoop(SoundEvent event, SoundSource category) {
      super(event, category, SoundInstance.createUnseededRandom());
      this.volume = 0.0F;
      this.delay = 0;
      this.looping = true;
   }

   public void tick() {
      if (this.stopping) {
         if (this.fade > 0.0F) {
            this.fade--;
         } else {
            this.stop();
         }
      } else if (this.starting) {
         if (this.fade < (float)this.getFadeTime()) {
            this.fade++;
         } else {
            this.starting = false;
         }
      }

      float volume = Math.max(0.0F, Math.min(this.fade / (float)this.getFadeTime(), 1.0F)) * this.maximumVolume();
      this.volume = volume - Math.max(0.0F, this.dampen * 0.02F);
   }

   protected boolean shouldFadeOut() {
      return this.stopping;
   }

   public boolean isStopping() {
      return this.stopping;
   }

   public boolean isStarting() {
      return this.starting;
   }

   public void stopSound() {
      if (!this.stopping) {
         this.stopping = true;
         this.fade = (float)this.getFadeTime();
      }
   }

   public void continueSound() {
      if (this.stopping) {
         this.stopping = false;
         this.starting = true;
      }
   }

   protected abstract int getFadeTime();

   protected float maximumVolume() {
      return 1.0F;
   }

   public boolean canStartSilent() {
      return true;
   }
}
