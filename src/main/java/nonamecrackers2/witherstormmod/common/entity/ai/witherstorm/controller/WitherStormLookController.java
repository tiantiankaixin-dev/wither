package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.controller;

import java.util.Optional;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormLookController extends LookControl {
   private final WitherStormEntity storm;

   public WitherStormLookController(WitherStormEntity storm) {
      super(storm);
      this.storm = storm;
   }

   public void tick() {
      if (this.lookAtCooldown > 0) {
         this.lookAtCooldown--;
         this.getYRotD().ifPresent(rot -> this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, rot, this.yMaxRotSpeed));
         this.getXRotD().ifPresent(rot -> this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), rot, this.yMaxRotSpeed)));
      } else {
         this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, 10.0F);
      }

      this.clampHeadRotationToBody();
   }

   public void setLookAt(double wantedX, double wantedY, double wantedZ, float yMaxSpeed, float xMaxRotAngle) {
      this.wantedX = wantedX;
      this.wantedY = wantedY;
      this.wantedZ = wantedZ;
      this.yMaxRotSpeed = yMaxSpeed;
      this.lookAtCooldown = 2;
   }

   protected Optional<Float> getXRotD() {
      if (this.storm.getPhase() > 3) {
         Vec3 pos = this.storm.getHeadPos(0);
         double d0 = this.wantedX - pos.x;
         double d1 = this.wantedY - pos.y;
         double d2 = this.wantedZ - pos.z;
         double d3 = Math.sqrt(d0 * d0 + d2 * d2);
         return Optional.of((float)(-(Mth.atan2(d1, d3) * 180.0F / (float)Math.PI)));
      } else {
         return super.getXRotD();
      }
   }

   protected Optional<Float> getYRotD() {
      if (this.storm.getPhase() > 3) {
         Vec3 pos = this.storm.getHeadPos(0);
         double d0 = this.wantedX - pos.x;
         double d1 = this.wantedZ - pos.z;
         return Optional.of((float)(Mth.atan2(d1, d0) * 180.0F / (float)Math.PI) - 90.0F);
      } else {
         return super.getYRotD();
      }
   }

   protected boolean resetXRotOnTick() {
      return false;
   }
}
