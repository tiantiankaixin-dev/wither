package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.HeadConfiguration;

public class AdditionalHead extends WitherStormHead {
   @Nullable
   protected LivingEntity target;
   public float xRot;
   public float yRot;
   public float xRotO;
   public float yRotO;

   public AdditionalHead(WitherStormEntity storm, int headIndex) {
      super(storm, headIndex, true);
   }

   @Override
   public CompoundTag save() {
      CompoundTag tag = super.save();
      tag.putFloat("xRot", this.xRot);
      tag.putFloat("yRot", this.yRot);
      return tag;
   }

   @Override
   public void read(CompoundTag tag) {
      super.read(tag);
      if (tag.contains("xRot")) {
         this.xRot = tag.getFloat("xRot");
      }

      if (tag.contains("yRot")) {
         this.yRot = tag.getFloat("yRot");
      }
   }

   @Override
   public void setLookPos(@Nullable Vec3 pos, int steps) {
      this.setLookSteps(steps);
      this.storm.getEntityData().set(HeadManager.TARGETS.get(this.headIndex), Optional.ofNullable(pos));
   }

   @Nullable
   public Vec3 getLookPos() {
      return (Vec3)((Optional)this.storm.getEntityData().get(HeadManager.TARGETS.get(this.headIndex))).orElse(null);
   }

   protected void setLookSteps(int steps) {
      this.storm.getEntityData().set(HeadManager.LOOK_STEPS.get(this.headIndex), steps);
   }

   protected int getLookSteps() {
      return (Integer)this.storm.getEntityData().get(HeadManager.LOOK_STEPS.get(this.headIndex));
   }

   @Override
   public void baseTick(HeadConfiguration config) {
      super.baseTick(config);
      this.xRotO = this.xRot;
      this.yRotO = this.yRot;
   }

   @Override
   public void doHeadLookLogic() {
      Vec3 pos = this.getLookPos();
      if (pos != null) {
         Vec3 headPos = this.getHeadPos();
         double deltaX = pos.x() - headPos.x;
         double deltaY = pos.y() - headPos.y;
         double deltaZ = pos.z() - headPos.z;
         double d7 = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
         float f = (float)(Mth.atan2(deltaZ, deltaX) * 180.0F / (float)Math.PI) - 90.0F;
         float f1 = (float)(-(Mth.atan2(deltaY, d7) * 180.0F / (float)Math.PI));
         this.lerpHeadTo(f1, f, (float)this.getLookSteps());
      } else if (!this.storm.isOnDistantRenderer() && !this.storm.isDeadOrPlayingDead()) {
         this.yRot = WitherStormEntity.rotlerp(this.yRot, this.storm.yBodyRot, 10.0F);
      }
   }

   @Override
   public float getHeadXRot() {
      return this.xRot;
   }

   @Override
   public float getHeadXRotO() {
      return this.xRotO;
   }

   @Override
   public float getHeadYRot() {
      return this.yRot;
   }

   @Override
   public float getHeadYRotO() {
      return this.yRotO;
   }

   @Override
   public void setHeadXRot(float rot) {
      this.xRot = rot;
   }

   @Override
   public void setHeadYRot(float rot) {
      this.yRot = rot;
   }

   @Override
   public LivingEntity getTarget() {
      return this.target;
   }

   @Override
   public void setTarget(@Nullable LivingEntity target) {
      this.target = target;
   }
}
