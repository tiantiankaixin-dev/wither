package nonamecrackers2.witherstormmod.client.audio;

import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;

public class WitherStormTractorBeamLoop extends TractorBeamLoop<WitherStormEntity> {
   private final int head;

   public WitherStormTractorBeamLoop(WitherStormEntity entity, int head) {
      super(entity);
      this.head = head;
   }

   public int getHead() {
      return this.head;
   }

   @Override
   public boolean shouldStop(double distance) {
      return this.entity.isDeadOrPlayingDead()
         || !this.entity.isAlive()
         || distance > TractorBeamLoop.DISTANCE_REQUIRED
         || !this.entity.tractorBeamActive(this.head);
   }

   @Override
   public void calculateVolume() {
      if (this.head != 0 && (this.head <= 0 || this.entity.areOtherHeadsDisabled())) {
         this.volume = 0.0F;
      } else {
         this.volume = Math.max(0.0F, 0.3F - this.getDistance(new Vec3(this.x, this.y, this.z)) / 60.0F);
      }
   }

   @Override
   protected Vec3 calculateClosestPoint() {
      return TractorBeamHelper.calculateClosestPoint(this.player.position(), this.entity, this.head);
   }
}
