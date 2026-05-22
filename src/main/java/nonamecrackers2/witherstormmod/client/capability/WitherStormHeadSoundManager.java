package nonamecrackers2.witherstormmod.client.capability;

import net.minecraft.client.Minecraft;
import nonamecrackers2.witherstormmod.client.audio.EntitySoundManager;
import nonamecrackers2.witherstormmod.client.audio.TractorBeamLoop;
import nonamecrackers2.witherstormmod.common.entity.WitherStormHeadEntity;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;

public class WitherStormHeadSoundManager extends EntitySoundManager<WitherStormHeadEntity, TractorBeamLoop<WitherStormHeadEntity>> {
   public WitherStormHeadSoundManager(Minecraft minecraft) {
      super(minecraft, WitherStormHeadEntity.class);
   }

   protected boolean canPlay(WitherStormHeadEntity entity) {
      double distance = Math.sqrt(this.minecraft.player.distanceToSqr(TractorBeamHelper.calculateClosestPoint(this.minecraft.player, entity)));
      return super.canPlay(entity) && !entity.isDeadOrPlayingDead() && distance <= TractorBeamLoop.DISTANCE_REQUIRED && entity.tractorBeamActive(0);
   }

   protected boolean alreadyHasLoop(WitherStormHeadEntity entity) {
      for (TractorBeamLoop<WitherStormHeadEntity> loop : this.loops) {
         if (loop.getEntity() == entity) {
            return true;
         }
      }

      return false;
   }

   protected TractorBeamLoop<WitherStormHeadEntity> create(WitherStormHeadEntity entity) {
      return new TractorBeamLoop<>(entity, (WitherStormHeadEntity loopEntity) -> !loopEntity.tractorBeamActive(0));
   }

   protected TractorBeamLoop<WitherStormHeadEntity> copyFrom(TractorBeamLoop<WitherStormHeadEntity> loop) {
      return new TractorBeamLoop<>((WitherStormHeadEntity)loop.getEntity(), (WitherStormHeadEntity loopEntity) -> !loopEntity.tractorBeamActive(0));
   }
}
