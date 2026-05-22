package nonamecrackers2.witherstormmod.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraftforge.event.PlayLevelSoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class PlaySoundEvents {
   @SubscribeEvent
   public static void onPlaySound(PlayLevelSoundEvent event) {
      if (event.getLevel().isClientSide && (Boolean)WitherStormModConfig.SERVER.occludeSoundsUnderground.get()) {
         Holder<SoundEvent> sound = event.getSound();
         LocalPlayer player = Minecraft.getInstance().player;
         if (!WorldUtil.isInAnOpenArea(player)) {
            float original = event.getOriginalVolume();
            float volume = original * ((20.0F - Mth.clamp((float)(-player.getY()) + 40.0F, 0.0F, 20.0F)) / 20.0F) * 0.5F;
            if (WitherStormEntity.isOccludedSound(sound.get())) {
               event.setNewVolume(volume);
            }
         }
      }
   }
}
