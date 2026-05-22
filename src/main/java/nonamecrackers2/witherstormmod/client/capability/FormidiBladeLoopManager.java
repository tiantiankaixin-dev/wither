package nonamecrackers2.witherstormmod.client.capability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.sounds.SoundEvent;
import nonamecrackers2.witherstormmod.client.audio.EntitySoundManager;
import nonamecrackers2.witherstormmod.client.audio.FormidiBladeLoop;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;

public class FormidiBladeLoopManager extends EntitySoundManager<AbstractClientPlayer, FormidiBladeLoop> {
   public FormidiBladeLoopManager(Minecraft minecraft) {
      super(minecraft, AbstractClientPlayer.class);
   }

   protected boolean canPlay(AbstractClientPlayer entity) {
      return super.canPlay(entity) && FormidiBladeLoop.canPlay(entity);
   }

   protected boolean alreadyHasLoop(AbstractClientPlayer entity) {
      return this.loops.stream().anyMatch(l -> l.player == entity);
   }

   protected FormidiBladeLoop create(AbstractClientPlayer entity) {
      return new FormidiBladeLoop(entity, WitherStormModSoundEvents.FORMIDIBOMB_PULSE_LOOP.get());
   }

   protected FormidiBladeLoop copyFrom(FormidiBladeLoop loop) {
      return this.create(loop.player);
   }
}
