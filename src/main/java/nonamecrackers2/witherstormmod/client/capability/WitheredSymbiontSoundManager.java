package nonamecrackers2.witherstormmod.client.capability;

import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import nonamecrackers2.witherstormmod.client.audio.EntitySoundLoop;
import nonamecrackers2.witherstormmod.client.audio.EntitySoundManager;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;

public class WitheredSymbiontSoundManager extends EntitySoundManager<WitheredSymbiontEntity, EntitySoundLoop<WitheredSymbiontEntity>> {
   private static final Predicate<WitheredSymbiontEntity> SHOULD_STOP = entity -> !entity.isVulnerable();

   public WitheredSymbiontSoundManager(Minecraft minecraft) {
      super(minecraft, WitheredSymbiontEntity.class);
   }

   protected boolean canPlay(WitheredSymbiontEntity entity) {
      return super.canPlay(entity) && !SHOULD_STOP.test(entity);
   }

   protected boolean alreadyHasLoop(WitheredSymbiontEntity entity) {
      boolean flag = false;

      for (EntitySoundLoop<WitheredSymbiontEntity> loop : this.loops) {
         if (loop.entity == entity) {
            flag = true;
            break;
         }
      }

      return flag;
   }

   protected EntitySoundLoop<WitheredSymbiontEntity> create(WitheredSymbiontEntity entity) {
      return new EntitySoundLoop(entity, WitherStormModSoundEvents.WITHERED_SYMBIONT_HEART_BEAT.get(), SoundSource.AMBIENT, 20, 7.0F, SHOULD_STOP);
   }

   protected EntitySoundLoop<WitheredSymbiontEntity> copyFrom(EntitySoundLoop<WitheredSymbiontEntity> loop) {
      return new EntitySoundLoop(
         loop.entity, WitherStormModSoundEvents.WITHERED_SYMBIONT_HEART_BEAT.get(), SoundSource.AMBIENT, 20, 7.0F, SHOULD_STOP
      );
   }
}
