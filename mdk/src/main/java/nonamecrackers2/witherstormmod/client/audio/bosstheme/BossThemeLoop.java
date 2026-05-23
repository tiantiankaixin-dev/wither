package nonamecrackers2.witherstormmod.client.audio.bosstheme;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import nonamecrackers2.witherstormmod.client.audio.FadingSoundLoop;
import nonamecrackers2.witherstormmod.client.audio.IForceStoppableSound;
import nonamecrackers2.witherstormmod.common.entity.BossThemeEntity;

public class BossThemeLoop extends FadingSoundLoop implements IForceStoppableSound {
   private final Minecraft minecraft;
   protected BossThemeEntity entity;
   protected final SoundEvent event;

   public BossThemeLoop(BossThemeEntity entity) {
      super(entity.getBossTheme(), entity.getCategory());
      this.relative = true;
      this.attenuation = Attenuation.NONE;
      this.minecraft = Minecraft.getInstance();
      this.entity = entity;
      this.event = entity.getBossTheme();
   }

   protected BossThemeLoop(BossThemeEntity entity, SoundEvent event) {
      super(event, entity.getCategory());
      this.minecraft = Minecraft.getInstance();
      this.entity = entity;
      this.event = event;
   }

   @Override
   public void tick() {
      super.tick();
      this.x = this.entity.getPosition().x();
      this.y = this.entity.getPosition().y();
      this.z = this.entity.getPosition().z();
      if (!this.entity.shouldPlayBossTheme()
         || !BossThemeManager.inRange(this.entity, this.minecraft.player)
         || !BossThemeManager.hasGeneralAccessTo(this.entity, this.minecraft.player)) {
         ClientLevel world = this.minecraft.level;
         boolean flag = false;

         for (Entity entity : world.entitiesForRendering()) {
            if (entity instanceof BossThemeEntity bossEntity
               && bossEntity.shouldPlayBossTheme()
               && this.entity.matches(bossEntity)
               && BossThemeManager.inRange(bossEntity, this.minecraft.player)
               && BossThemeManager.hasGeneralAccessTo(bossEntity, this.minecraft.player)) {
               this.entity = bossEntity;
               flag = true;
               break;
            }
         }

         if (!flag) {
            this.stopSound();
         }
      }
   }

   @Override
   protected int getFadeTime() {
      return this.entity.getFadeTime();
   }

   @Override
   public void forceStop() {
      this.stop();
   }

   public BossThemeEntity getEntity() {
      return this.entity;
   }
}
