package nonamecrackers2.witherstormmod.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;

public class StructureAnimationHelper {
   public float baseXRotOffset;
   public float baseYRotOffset;
   public float baseXRot;
   public float baseXRotO;
   private float lerpBaseXRot;
   private int lerpBaseXRotSteps;
   public float baseYRot;
   public float baseYRotO;
   private float lerpBaseYRot;
   private int lerpBaseYRotSteps;
   public float xRot;
   public float xRotO;
   private float lerpXRot;
   private int lerpXRotSteps;
   public float yRot;
   public float yRotO;
   private float lerpYRot;
   private int lerpYRotSteps;
   public boolean twisted;

   public StructureAnimationHelper setBaseRotationAngle(float x, float y) {
      this.baseXRotOffset = x;
      this.baseYRotOffset = y;
      return this;
   }

   public void write(CompoundTag compound) {
      compound.putFloat("BaseXRot", this.baseXRot);
      compound.putFloat("BaseYRot", this.baseYRot);
      compound.putFloat("XRot", this.xRot);
      compound.putFloat("YRot", this.yRot);
   }

   public void read(CompoundTag compound) {
      this.baseXRot = compound.getFloat("BaseXRot");
      this.baseXRotO = this.baseXRot;
      this.baseYRot = compound.getFloat("BaseYRot");
      this.baseYRotO = this.baseYRot;
      this.xRot = compound.getFloat("XRot");
      this.xRotO = this.xRot;
      this.yRot = compound.getFloat("YRot");
      this.yRotO = this.yRot;
   }

   public void writeBuffer(FriendlyByteBuf buffer) {
      buffer.writeFloat(this.baseXRot);
      buffer.writeFloat(this.lerpBaseXRot);
      buffer.writeInt(this.lerpBaseXRotSteps);
      buffer.writeFloat(this.baseYRot);
      buffer.writeFloat(this.lerpBaseYRot);
      buffer.writeInt(this.lerpBaseYRotSteps);
      buffer.writeFloat(this.xRot);
      buffer.writeFloat(this.lerpXRot);
      buffer.writeInt(this.lerpXRotSteps);
      buffer.writeFloat(this.yRot);
      buffer.writeFloat(this.lerpYRot);
      buffer.writeInt(this.lerpYRotSteps);
   }

   public void readBuffer(FriendlyByteBuf buffer) {
      this.baseXRot = buffer.readFloat();
      this.lerpBaseXRot = buffer.readFloat();
      this.lerpBaseXRotSteps = buffer.readInt();
      this.baseYRot = buffer.readFloat();
      this.lerpBaseYRot = buffer.readFloat();
      this.lerpBaseYRotSteps = buffer.readInt();
      this.xRot = buffer.readFloat();
      this.lerpXRot = buffer.readFloat();
      this.lerpXRotSteps = buffer.readInt();
      this.yRot = buffer.readFloat();
      this.lerpYRot = buffer.readFloat();
      this.lerpYRotSteps = buffer.readInt();
   }

   public void tick() {
      this.baseXRotO = this.baseXRot;
      this.baseYRotO = this.baseYRot;
      this.xRotO = this.xRot;
      this.yRotO = this.yRot;
      if (this.lerpBaseXRotSteps > 0) {
         this.baseXRot = (float)((double)this.baseXRot + Mth.wrapDegrees((double)this.lerpBaseXRot - (double)this.baseXRot) / (double)this.lerpBaseXRotSteps);
         this.lerpBaseXRotSteps--;
      }

      if (this.lerpBaseYRotSteps > 0) {
         this.baseYRot = (float)((double)this.baseYRot + Mth.wrapDegrees((double)this.lerpBaseYRot - (double)this.baseYRot) / (double)this.lerpBaseYRotSteps);
         this.lerpBaseYRotSteps--;
      }

      if (this.lerpXRotSteps > 0) {
         this.xRot = (float)((double)this.xRot + Mth.wrapDegrees((double)this.lerpXRot - (double)this.xRot) / (double)this.lerpXRotSteps);
         this.lerpXRotSteps--;
      }

      if (this.lerpYRotSteps > 0) {
         this.yRot = (float)((double)this.yRot + Mth.wrapDegrees((double)this.lerpYRot - (double)this.yRot) / (double)this.lerpYRotSteps);
         this.lerpYRotSteps--;
      }
   }

   public void lerpBaseTo(CommandBlockEntity entity, float baseRotX, float baseRotY, int steps) {
      if (this.lerpBaseXRot != baseRotX || this.lerpBaseYRot != baseRotY) {
         entity.getMode().playMovementSound(entity);
      }

      if (this.lerpBaseXRot != baseRotX) {
         this.lerpBaseXRot = baseRotX;
         this.lerpBaseXRotSteps = steps;
      }

      if (this.lerpBaseYRot != baseRotY) {
         this.lerpBaseYRot = baseRotY;
         this.lerpBaseYRotSteps = steps;
      }
   }

   public void lerpTo(CommandBlockEntity entity, float rotX, float rotY, int steps) {
      if (this.lerpXRot != rotX || this.lerpYRot != rotY) {
         entity.getMode().playMovementSound(entity);
      }

      if (this.lerpXRot != rotX) {
         this.lerpXRot = rotX;
         this.lerpXRotSteps = steps;
      }

      if (this.lerpYRot != rotY) {
         this.lerpYRot = rotY;
         this.lerpYRotSteps = steps;
      }
   }

   public float getBaseXRot(float partialTicks) {
      return Mth.lerp(partialTicks, this.baseXRotO, this.baseXRot) + this.baseXRotOffset;
   }

   public float getBaseYRot(float partialTicks) {
      return Mth.lerp(partialTicks, this.baseYRotO, this.baseYRot) + this.baseYRotOffset;
   }

   public float getXRot(float partialTicks) {
      return Mth.lerp(partialTicks, this.xRotO, this.xRot);
   }

   public float getYRot(float partialTicks) {
      return Mth.lerp(partialTicks, this.yRotO, this.yRot);
   }
}
