package nonamecrackers2.witherstormmod.client.audio;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;

public class CommandBlockEntityLoop extends AbstractTickableSoundInstance implements IForceStoppableSound {
   public final CommandBlockEntity entity;

   public CommandBlockEntityLoop(CommandBlockEntity entity) {
      super(WitherStormModSoundEvents.COMMAND_BLOCK_PULSE_LOOP.get(), SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
      this.entity = entity;
      this.looping = true;
   }

   public void tick() {
      if (this.shouldFadeOut()) {
         if (this.volume > 0.0F) {
            this.volume -= 0.1F;
         }
      } else if (this.volume < 0.5F) {
         this.volume += 0.1F;
      }

      this.pitch = (this.entity.getMaxHealth() - this.entity.getHealth()) / this.entity.getMaxHealth() * 0.4F + 1.0F;
      this.x = this.entity.getX();
      this.y = this.entity.getY();
      this.z = this.entity.getZ();
      if (this.shouldStop()) {
         this.forceStop();
      }
   }

   private boolean shouldFadeOut() {
      return this.entity.getState() == CommandBlockEntity.State.PLAYING_DEAD;
   }

   public boolean canStartSilent() {
      return true;
   }

   public boolean shouldStop() {
      return !this.entity.isAlive();
   }

   @Override
   public void forceStop() {
      this.stop();
   }
}
