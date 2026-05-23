package nonamecrackers2.witherstormmod.client.capability;

import net.minecraft.client.Minecraft;
import nonamecrackers2.witherstormmod.client.audio.EntitySoundManager;
import nonamecrackers2.witherstormmod.client.audio.WitheredSymbiontSpellLoop;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;

public class WitheredSymbiontSpellLoopManager extends EntitySoundManager<WitheredSymbiontEntity, WitheredSymbiontSpellLoop> {
   public WitheredSymbiontSpellLoopManager(Minecraft minecraft) {
      super(minecraft, WitheredSymbiontEntity.class);
   }

   protected boolean canPlay(WitheredSymbiontEntity entity) {
      return super.canPlay(entity) && entity.isCastingSpell() && entity.getSpell().spellLoop().isPresent();
   }

   protected boolean alreadyHasLoop(WitheredSymbiontEntity entity) {
      boolean flag = false;

      for (WitheredSymbiontSpellLoop loop : this.loops) {
         if (loop.entity == entity && loop.spell == entity.getSpell()) {
            flag = true;
            break;
         }
      }

      return flag;
   }

   protected WitheredSymbiontSpellLoop create(WitheredSymbiontEntity entity) {
      return new WitheredSymbiontSpellLoop(entity, entity.getSpell().spellLoop().get().get());
   }

   protected WitheredSymbiontSpellLoop copyFrom(WitheredSymbiontSpellLoop loop) {
      return new WitheredSymbiontSpellLoop(loop.entity, loop.spell.spellLoop().get().get());
   }
}
