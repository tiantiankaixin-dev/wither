package nonamecrackers2.witherstormmod.common.entity.part;

import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import nonamecrackers2.witherstormmod.common.entity.IMultipartHurtable;

public class TentaclePartEntity<T extends LivingEntity & IMultipartHurtable<TentaclePartEntity<T>>> extends LinkedPartEntity<T, TentaclePartEntity<T>> {
   public float xCurl;
   public float yCurl;
   public double xOffset;
   public double yOffset;
   public double zOffset;
   public float xRotOffset;
   public float yRotOffset;
   private float newWidth;
   private float newHeight;

   public TentaclePartEntity(T parent, float width, float height, int segment, float xCurl, float yCurl) {
      super(parent, width, height, segment);
      this.xCurl = xCurl;
      this.yCurl = yCurl;
      this.pushEntities = true;
      this.newWidth = width;
      this.newHeight = height;
   }

   public TentaclePartEntity(T parent, @Nullable TentaclePartEntity<T> linkedChild, float width, float height, int segment, float xCurl, float yCurl) {
      super(parent, linkedChild, width, height, segment);
      this.xCurl = xCurl;
      this.yCurl = yCurl;
      this.pushEntities = true;
      this.newWidth = width;
      this.newHeight = height;
   }

   public TentaclePartEntity<T> setOffset(double x, double y, double z, float xRot, float yRot) {
      this.xOffset = x;
      this.yOffset = y;
      this.zOffset = z;
      this.xRotOffset = xRot;
      this.yRotOffset = yRot;
      return this;
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.isBase) {
         float rot = this.getXRot() * this.xCurl;
         this.newWidth = Math.abs(Mth.sin(rot * (float) (Math.PI / 180.0))) * (this.size.width - this.size.height) + this.size.height;
         this.newHeight = Math.abs(Mth.sin(rot * (float) (Math.PI / 180.0))) * (this.size.height - this.size.width) + this.size.width;
      }

      this.refreshDimensions();
   }

   @Override
   protected void tickChild() {
      super.tickChild();
      this.linkedChild.setXRot(this.getXRot() * this.xCurl);
      this.linkedChild.setYRot((this.getYRot() - this.yRotOffset) * this.yCurl + this.yRotOffset);
   }

   @Override
   public EntityDimensions getDimensions(Pose pose) {
      return EntityDimensions.scalable(this.newWidth, this.newHeight);
   }

   public boolean hurt(DamageSource source, float damage) {
      return !this.isInvulnerableTo(source) ? ((IMultipartHurtable)((LivingEntity)this.getParent())).hurt(this, source, damage) : false;
   }

   public boolean isPickable() {
      return true;
   }

   public boolean shouldBeSaved() {
      return false;
   }
}
