package nonamecrackers2.witherstormmod.client.audio;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;

public class WitheredSymbiontSpellLoop extends FadingSoundLoop implements IForceStoppableSound {
   public final WitheredSymbiontEntity entity;
   public final SpellType spell;

   public WitheredSymbiontSpellLoop(WitheredSymbiontEntity entity, SoundEvent event) {
      super(event, SoundSource.AMBIENT);
      this.entity = entity;
      this.spell = entity.getSpell();
   }

   @Override
   public void tick() {
      super.tick();
      this.x = this.entity.getX();
      this.y = this.entity.getY();
      this.z = this.entity.getZ();
      if (!this.entity.isAlive() || !this.entity.isCastingSpell() || this.entity.getSpell() != this.spell) {
         this.stopSound();
      }
   }

   @Override
   protected int getFadeTime() {
      return 20;
   }

   @Override
   public void forceStop() {
      this.stop();
   }
}
