package nonamecrackers2.witherstormmod.mixin;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import nonamecrackers2.witherstormmod.client.audio.bosstheme.BossThemeManager;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MusicManager.class})
public abstract class MixinMusicManager {
   @Final
   @Shadow
   private Minecraft minecraft;
   @Shadow
   @Nullable
   private SoundInstance currentMusic;

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void witherstormmod$stopCurrentMusicIfBossMusicPlaying_tick(CallbackInfo ci) {
      if (WitherStormModConfig.CLIENT_SPEC.isLoaded() && !(Boolean)WitherStormModConfig.CLIENT.playMinecraftMusic.get()) {
         if (this.currentMusic != null) {
            this.stopPlaying();
         }

         ci.cancel();
      }

      ClientLevel level = this.minecraft.level;
      if (level != null) {
         BossThemeManager manager = (BossThemeManager)level.getCapability(WitherStormModClientCapabilities.BOSS_THEME_MANAGER).orElse(null);
         if (manager != null && manager.isPlaying()) {
            if (this.currentMusic != null) {
               this.stopPlaying();
            }

            ci.cancel();
         }
      }
   }

   @Shadow
   public abstract void stopPlaying();
}
