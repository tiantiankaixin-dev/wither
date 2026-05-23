package nonamecrackers2.witherstormmod.client.audio.bosstheme;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import nonamecrackers2.witherstormmod.client.audio.ISoundManager;
import nonamecrackers2.witherstormmod.common.entity.BossThemeEntity;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import nonamecrackers2.witherstormmod.mixin.MixinSoundEngineAccessor;
import nonamecrackers2.witherstormmod.mixin.MixinSoundManagerAccessor;

public class BossThemeManager implements ISoundManager {
   public static final int WATERMARK_TIME = 160;
   protected final Minecraft minecraft;
   @Nullable
   protected BossThemeLoop theme;
   protected Component watermark;
   protected int watermarkTime;
   protected int startTime;

   public BossThemeManager(Minecraft minecraft) {
      this.minecraft = minecraft;
   }

   public BossThemeManager() {
      this(null);
   }

   @Override
   public void tick() {
      ClientLevel world = this.minecraft.level;
      SoundManager manager = this.minecraft.getSoundManager();

      for (Entity entity : world.entitiesForRendering()) {
         if (entity instanceof BossThemeEntity) {
            BossThemeEntity bossEntity = (BossThemeEntity)entity;
            if (this.minecraft.options.getSoundSourceVolume(bossEntity.getCategory()) > 0.0F
               && this.minecraft.options.getSoundSourceVolume(SoundSource.MASTER) > 0.0F
               && bossEntity.shouldPlayBossTheme()
               && inRange(bossEntity, this.minecraft.player)
               && hasGeneralAccessTo(bossEntity, this.minecraft.player)) {
               BossThemeEntity current = this.getEntity();
               if (current != null) {
                  if (current == bossEntity) {
                     this.theme.continueSound();
                  } else if (bossEntity.hasPriority(current)) {
                     if (this.theme != null) {
                        this.theme.stopSound();
                     }

                     this.playLoop(bossEntity);
                  }
               } else {
                  this.playLoop(bossEntity);
               }
            }
         }
      }

      SoundEngine engine = ((MixinSoundManagerAccessor)manager).witherstormmod$getSoundEngine();
      if (this.theme != null) {
         if (this.theme.isStopped()) {
            this.theme = null;
         } else if (!manager.isActive(this.theme) && !((MixinSoundEngineAccessor)engine).witherstormmod$getQueuedTickableSounds().contains(this.theme)) {
            this.theme = null;
         } else if (this.minecraft.options.getSoundSourceVolume(this.theme.getSource()) <= 0.0F || this.minecraft.options.getSoundSourceVolume(SoundSource.MASTER) <= 0.0F) {
            this.theme.forceStop();
            this.theme = null;
         }
      }

      if (this.theme != null && !this.theme.event.equals(this.theme.getEntity().getBossTheme())) {
         this.theme.stopSound();
         this.playLoop(this.theme.getEntity());
      }

      if (this.watermarkTime > 0) {
         this.watermarkTime--;
      }
   }

   @Nullable
   public BossThemeEntity getEntity() {
      return this.theme != null ? this.theme.getEntity() : null;
   }

   private void playLoop(BossThemeEntity entity) {
      this.theme = new BossThemeLoop(entity);
      this.minecraft.getSoundManager().queueTickingSound(this.theme);
      this.watermark = entity.getWatermark();
      this.watermarkTime = 160;
      this.startTime = 160;
   }

   public void forceStop() {
      if (this.theme != null) {
         this.theme.forceStop();
      }
   }

   @Override
   public void refresh() {
      if (this.theme != null && !this.theme.isStopped() && !this.theme.isStopping()) {
         this.theme.forceStop();
         this.playLoop(this.theme.getEntity());
      }
   }

   public boolean isPlaying() {
      return this.theme != null;
   }

   protected static boolean inRange(BossThemeEntity entity, LocalPlayer player) {
      boolean flag = true;
      if (entity.distanceToPlay() > 0.0) {
         flag = Math.sqrt(player.distanceToSqr(entity.getPosition().x(), entity.getPosition().y(), entity.getPosition().z()))
            < entity.distanceToPlay();
      }

      return flag;
   }

   protected static boolean hasGeneralAccessTo(BossThemeEntity theme, LocalPlayer player) {
      return theme.smartBossMusic() ? WorldUtil.canSeeOrIsNotInASmallArea((Entity)theme, player) : true;
   }

   @Nullable
   public Component getWatermark() {
      return this.watermark;
   }

   public int getWatermarkTime() {
      return this.watermarkTime;
   }

   public int getWatermarkStartTime() {
      return this.startTime;
   }
}
