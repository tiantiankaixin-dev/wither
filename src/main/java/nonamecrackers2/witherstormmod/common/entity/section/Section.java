package nonamecrackers2.witherstormmod.common.entity.section;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class Section {
   protected AABB boundingBox;
   protected EntityDimensions size;
   protected Vec3 position;
   protected Vec3 offset;
   protected final WitherStormEntity owner;
   protected Predicate<WitherStormEntity> isActive;
   protected final float[] color = new float[]{0.0F, 1.0F, 0.0F};

   public Section(WitherStormEntity entity, float height, float width, double x, double y, double z, Predicate<WitherStormEntity> isActive) {
      this.owner = entity;
      this.size = EntityDimensions.scalable(width, height);
      this.position = entity.position();
      this.offset = new Vec3(x, y, z);
      this.boundingBox = this.size.makeBoundingBox(entity.position());
      this.isActive = isActive;
   }

   public Section(WitherStormEntity entity, float height, float width, double x, double y, double z, int phase) {
      this(entity, height, width, x, y, z, storm -> storm.getPhase() > phase);
   }

   public void setSize(float height, float width) {
      this.size = EntityDimensions.scalable(width, height);
   }

   public void setOffset(double x, double y, double z) {
      this.offset = new Vec3(x, y, z);
   }

   public void tick() {
      float yBodyRot = this.owner.yBodyRot * (float) (Math.PI / 180.0);
      float yBodyRot90 = (this.owner.yBodyRot + 90.0F) * (float) (Math.PI / 180.0);
      float xBodyRot = (this.owner.xBodyRot + 90.0F) * (float) (Math.PI / 180.0);
      double xOffset = (double)Mth.cos(yBodyRot) * this.getOffset().x();
      double zOffset = (double)Mth.sin(yBodyRot) * this.getOffset().x();
      float offset = (float)Math.atan2(this.getOffset().z(), this.getOffset().y());
      double rawX = (double)(Mth.cos(xBodyRot + offset) * Mth.cos(yBodyRot90));
      double rawY = (double)Mth.sin(xBodyRot + offset);
      double rawZ = (double)(Mth.cos(xBodyRot + offset) * Mth.sin(yBodyRot90));
      double x = xOffset
         + this.owner.getX()
         + rawX * Math.sqrt(this.getOffset().z() * this.getOffset().z() + this.getOffset().y() * this.getOffset().y());
      double y = this.owner.getY()
         + rawY * Math.sqrt(this.getOffset().z() * this.getOffset().z() + this.getOffset().y() * this.getOffset().y());
      double z = zOffset
         + this.owner.getZ()
         + rawZ * Math.sqrt(this.getOffset().z() * this.getOffset().z() + this.getOffset().y() * this.getOffset().y());
      this.position = new Vec3(x, y, z);
      this.boundingBox = this.size.makeBoundingBox(this.position);
      this.collideWithNearbyEntities();
   }

   protected void collideWithNearbyEntities() {
      List<Entity> list = this.owner.level().getEntities(this.owner, this.getBoundingBox(), EntitySelector.pushableBy(this.owner));
      if (!list.isEmpty()) {
         for (Entity entity : list) {
            this.applyEntityCollision(entity);
         }
      }
   }

   public void applyEntityCollision(Entity entity) {
      if (!this.owner.isPassengerOfSameVehicle(entity) && !entity.noPhysics && !this.owner.noPhysics) {
         double d0 = entity.getX() - this.position.x;
         double d1 = entity.getZ() - this.position.z;
         double d2 = Mth.absMax(d0, d1);
         if (d2 >= 0.01F) {
            d2 = Math.sqrt(d2);
            d0 /= d2;
            d1 /= d2;
            double d3 = 1.0 / d2;
            if (d3 > 1.0) {
               d3 = 1.0;
            }

            d0 *= d3;
            d1 *= d3;
            d0 *= 0.05F;
            d1 *= 0.05F;
            if (!entity.isVehicle()) {
               entity.push(d0, 0.0, d1);
            }
         }
      }
   }

   public Vec3 getPosition() {
      return this.position;
   }

   public Vec3 getOffset() {
      return this.offset;
   }

   public void setOffset(Vec3 vector) {
      this.offset = vector;
   }

   public AABB getBoundingBox() {
      return this.boundingBox;
   }

   public Level getLevel() {
      return this.owner.level();
   }

   public boolean isActive() {
      return this.isActive.test(this.owner);
   }

   public Section setColor(float r, float g, float b) {
      this.color[0] = r;
      this.color[1] = g;
      this.color[2] = b;
      return this;
   }

   public float[] getColor() {
      return this.color;
   }
}
