package nonamecrackers2.witherstormmod.client.capability;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;

public class PlayerCameraShaker {
   private final LocalPlayer player;
   private float xShake;
   private float yShake;
   private float xShakeO;
   private float yShakeO;
   private float duration;
   private float initialDuration;
   private float power;

   public PlayerCameraShaker(LocalPlayer player) {
      this.player = player;
   }

   public void tick() {
      this.xShakeO = this.xShake;
      this.yShakeO = this.yShake;
      if ((Boolean)WitherStormModConfig.CLIENT.cameraShakeEffects.get() && this.duration > 0.0F) {
         float power;
         if (this.player.onGround()) {
            power = this.power;
         } else {
            power = 0.0F;
         }

         this.xShake = power * (this.player.getRandom().nextFloat() * 2.0F - 1.0F) * this.getDurationPercentage();
         this.yShake = power * (this.player.getRandom().nextFloat() * 2.0F - 1.0F) * this.getDurationPercentage();
         this.duration--;
      }
   }

   public void shake(float duration, float power) {
      if ((Boolean)WitherStormModConfig.CLIENT.cameraShakeEffects.get()) {
         this.initialDuration = duration;
         this.duration = duration;
         this.power = power;
      }
   }

   public float getXShake(float partialTicks) {
      return Mth.lerp(partialTicks, this.xShakeO, this.xShake);
   }

   public float getYShake(float partialTicks) {
      return Mth.lerp(partialTicks, this.yShakeO, this.yShake);
   }

   private float getDurationPercentage() {
      return this.duration / this.initialDuration;
   }
}
