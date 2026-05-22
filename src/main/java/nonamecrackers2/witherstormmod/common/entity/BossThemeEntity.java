package nonamecrackers2.witherstormmod.common.entity;

import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public interface BossThemeEntity {
   SoundEvent getBossTheme();

   default SoundSource getCategory() {
      return SoundSource.MUSIC;
   }

   default boolean matches(BossThemeEntity entity) {
      return this.getBossTheme() == entity.getBossTheme() && this.getCategory() == entity.getCategory() && this.priority() == entity.priority();
   }

   default int getFadeTime() {
      return 240;
   }

   boolean isStillAlive();

   default boolean shouldPlayBossTheme() {
      return this.isStillAlive() && this.getBossTheme() != null && this.checkConfig();
   }

   default boolean checkConfig() {
      return true;
   }

   default double distanceToPlay() {
      return 0.0;
   }

   int priority();

   default boolean hasPriority(BossThemeEntity entity) {
      return this.priority() > entity.priority();
   }

   Vec3 getPosition();

   @Nullable
   default Component getWatermark() {
      return null;
   }

   default boolean smartBossMusic() {
      return false;
   }
}
