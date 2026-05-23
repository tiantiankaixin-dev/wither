package nonamecrackers2.witherstormmod.common.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.TentacleEntity;

public class TentacleOffsets {
   private double x;
   private double y;
   private double z;
   private float xCurl;
   private float yCurl;
   private float baseXRot;
   private float baseYRot;

   public TentacleOffsets(double x, double y, double z, float xCurl, float yCurl, float baseXRot, float baseYRot) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.xCurl = xCurl;
      this.yCurl = yCurl;
      this.baseXRot = baseXRot;
      this.baseYRot = baseYRot;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public float getXCurl() {
      return this.xCurl;
   }

   public float getYCurl() {
      return this.yCurl;
   }

   public float getBaseXRot() {
      return this.baseXRot;
   }

   public float getBaseYRot() {
      return this.baseYRot;
   }

   public void apply(LivingEntity living, TentacleEntity entity) {
      this.setPos(living, entity);
      entity.setSavedXCurl(this.getXCurl());
      entity.setSavedYCurl(this.getYCurl());
      entity.setSavedXOffset(this.getBaseXRot());
      entity.setSavedYOffset(this.getBaseYRot() - living.yBodyRot - 90.0F);
   }

   public void setPos(LivingEntity living, TentacleEntity entity) {
      float angle = (float)Mth.atan2(this.getX(), this.getZ());
      double p = Math.sqrt(this.getX() * this.getX() + this.getZ() * this.getZ());
      float rot = -living.yBodyRot * (float) (Math.PI / 180.0);
      double newXOffset = (double)Mth.cos(rot + angle) * p;
      double newZOffset = (double)Mth.sin(rot + angle) * p;
      Vec3 pos = new Vec3(living.getX() + newXOffset, living.getY() + this.getY(), living.getZ() + newZOffset);
      entity.setPos(pos.x, pos.y, pos.z);
   }
}
