package nonamecrackers2.witherstormmod.client.audio;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;

public class WitherStormBossLoop extends FadingSoundLoop implements IForceStoppableSound {
   private WitherStormEntity entity;

   public WitherStormBossLoop(WitherStormEntity entity, SoundEvent event) {
      super(event, SoundSource.RECORDS);
      this.entity = entity;
   }

   public WitherStormBossLoop(WitherStormEntity entity) {
      this(entity, WitherStormModSoundEvents.WITHER_STORM_BOSS_THEME.get());
   }

   public WitherStormEntity getEntity() {
      return this.entity;
   }

   @Override
   public void tick() {
      if (this.entity == null) {
         this.stop();
      } else {
         this.x = this.entity.getX();
         this.y = this.entity.getY();
         this.z = this.entity.getZ();
         if (!this.entity.shouldPlayBossTheme()) {
            ClientLevel world = (ClientLevel)this.entity.level();
            boolean flag = false;

            for (Entity entity : world.entitiesForRendering()) {
               if (entity instanceof WitherStormEntity storm && storm.shouldPlayBossTheme()) {
                  this.entity = storm;
                  flag = true;
                  break;
               }
            }

            if (!flag) {
               this.stopSound();
            }
         }
      }
   }

   @Override
   public void forceStop() {
      this.stop();
   }

   @Override
   protected int getFadeTime() {
      return 240;
   }
}
