package nonamecrackers2.witherstormmod.client.audio;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import nonamecrackers2.witherstormmod.common.blockentity.FormidibombBlockEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.util.IFormidibomb;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class FormidibombFuseLoop extends AbstractTickableSoundInstance implements IForceStoppableSound {
   public final IFormidibomb formidibomb;

   public FormidibombFuseLoop(IFormidibomb formidibomb) {
      super(WitherStormModSoundEvents.FORMIDIBOMB_PULSE_LOOP.get(), SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
      this.formidibomb = formidibomb;
      this.looping = true;
   }

   public void tick() {
      this.volume = 1.0F + this.getPercentage() * 2.0F;
      float additionalPitch = this.formidibomb.getStartFuse() > 0 ? (float)Math.max(0.0, (120.0 - (double)this.formidibomb.getFuseLife()) / 120.0) : 0.0F;
      this.pitch = 1.0F + this.getPercentage() * 0.1F + additionalPitch;
      this.x = this.formidibomb.getPosition().x();
      this.y = this.formidibomb.getPosition().y();
      this.z = this.formidibomb.getPosition().z();
      if (this.shouldStop()) {
         this.forceStop();
      }
   }

   private float getPercentage() {
      return this.formidibomb.getStartFuse() > 0
         ? ((float)this.formidibomb.getStartFuse() - (float)this.formidibomb.getFuseLife()) / (float)this.formidibomb.getStartFuse()
         : 0.0F;
   }

   public boolean shouldStop() {
      boolean flag = false;
      if (this.formidibomb instanceof FormidibombBlockEntity tile) {
         Minecraft mc = Minecraft.getInstance();
         ClientLevel world = mc.level;
         List<BlockEntity> blocks = WorldUtil.getBlockEntitiesInAABB(world, mc.player.getBoundingBox().inflate(50.0));
         flag = !blocks.contains(tile);
      }

      return !this.formidibomb.isStillAlive() || flag;
   }

   @Override
   public void forceStop() {
      this.stop();
   }

   public boolean canStartSilent() {
      return true;
   }
}
