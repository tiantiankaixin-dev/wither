package nonamecrackers2.witherstormmod.client.capability;

import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;

public class PlayerScreenBlinder {
   private int duration;
   private int fadeInDuration;
   private int fadeOutDuration;
   private float fade;
   private float fadeO;

   public void tick() {
      this.fadeO = this.fade;
      if ((Boolean)WitherStormModConfig.CLIENT.blindingEffects.get()) {
         if (this.fadeInDuration > 0) {
            this.fade = this.fade + (1.0F - this.fade) / (float)this.fadeInDuration;
            this.fadeInDuration--;
         } else if (this.duration > 0) {
            this.duration--;
         } else if (this.fadeOutDuration > 0) {
            this.fade = this.fade + (0.0F - this.fade) / (float)this.fadeOutDuration;
            this.fadeOutDuration--;
         }
      }
   }

   public float getFade(float partialTicks) {
      return Mth.lerp(partialTicks, this.fadeO, this.fade);
   }

   public void blind(int duration, int fadeInDuration, int fadeOutDuration) {
      if ((Boolean)WitherStormModConfig.CLIENT.cameraShakeEffects.get()) {
         this.duration = duration;
         this.fadeInDuration = fadeInDuration;
         this.fadeOutDuration = fadeOutDuration;
      }
   }
}
