package nonamecrackers2.witherstormmod.api.common.entity;

import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public interface WitherStormBase {
   float getMouthAnimation(int var1, float var2);

   float getBrokenJawAnimation(int var1, float var2);

   float getFadeAnimation(float var1);

   float getFadeAnimation();

   float getTentacleAnimation(float var1);

   Vec3 getHeadPos(int var1);

   float getHeadYRot(int var1);

   float getHeadYRotO(int var1);

   float getHeadXRot(int var1);

   float getHeadXRotO(int var1);

   float getXBodyRot();

   float getXBodyRotO();

   boolean areOtherHeadsDisabled();

   boolean isHeadInjured(int var1);

   default boolean canBeDistracted(int head, WitherStormBase.DistractionType type) {
      return this.tractorBeamActive(head);
   }

   default boolean isDistracted(int head) {
      return this.getDistractedPos(head) != null;
   }

   @Nullable
   Vec3 getDistractedPos(int var1);

   void setDistractedPos(int var1, @Nullable Vec3 var2);

   void makeDistracted(Vec3 var1, int var2, int var3);

   void setLookAt(int var1, @Nullable Vec3 var2, int var3);

   float getHeadShakeAnim(int var1, float var2);

   @Nullable
   LivingEntity getTarget(int var1);

   void setTarget(int var1, @Nullable LivingEntity var2);

   boolean canSee(int var1, Entity var2);

   boolean isPosBehindBack(Vec3 var1);

   boolean isDeadOrPlayingDead();

   boolean isPlayingDead();

   default boolean isEntityBehindBack(Entity entity) {
      return this.isPosBehindBack(entity.position());
   }

   default void setLookAt(int head, @Nullable Vec3 pos) {
      this.setLookAt(head, pos, 3);
   }

   default int getTotalHeads() {
      return 3;
   }

   default double getTractorBeamCutoffDistance(int head) {
      return -1.0;
   }

   default boolean tractorBeamActive(int head) {
      boolean flag = false;
      if (this.areOtherHeadsDisabled()) {
         flag = head == 0;
      } else {
         flag = true;
      }

      return flag && !this.isHeadInjured(head);
   }

   default Vec3 getViewVector(float x, float y, float range) {
      float f = x * (float) (Math.PI / 180.0);
      float f1 = -y * (float) (Math.PI / 180.0);
      float f2 = Mth.cos(f1);
      float f3 = Mth.sin(f1);
      float f4 = Mth.cos(f);
      float f5 = Mth.sin(f);
      return new Vec3((double)(f3 * f4) * (double)range, (double)(-f5) * (double)range, (double)(f2 * f4) * (double)range);
   }

   public static enum DistractionType {
      ENTITY_BASED,
      STRUCTURES;
   }
}
