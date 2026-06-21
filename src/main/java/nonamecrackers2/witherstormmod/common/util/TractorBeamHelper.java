package nonamecrackers2.witherstormmod.common.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.entity.WitherStormBase;

public class TractorBeamHelper {
   public static Pair<Boolean, Integer> isInsideTractorBeam(Entity target, WitherStormBase entity, double radius) {
      return isInsideTractorBeam(target.position(), entity, radius);
   }

   public static Pair<Boolean, Integer> isInsideTractorBeam(Vec3 target, WitherStormBase entity, double radius) {
      for (int i = 0; i < entity.getTotalHeads(); i++) {
         if (isInsideTractorBeam(target, entity, radius, i)) {
            return Pair.of(true, i);
         }
      }

      return Pair.of(false, -1);
   }

   public static boolean isInsideTractorBeam(Entity target, WitherStormBase entity, double radius, int head) {
      return isInsideTractorBeam(target.position(), entity, radius, head);
   }

   public static boolean isInsideTractorBeam(Vec3 target, WitherStormBase entity, double radius, int head) {
      if (entity.tractorBeamActive(head)) {
         Vec3 pos = calculateClosestPoint(target, entity, head);
         double distance = Math.sqrt(target.distanceToSqr(pos));
         double distanceFromHead = Math.sqrt(target.distanceToSqr(entity.getHeadPos(head)));
         if (distance <= radius * (distanceFromHead + 30.0) * 0.014) {
            return true;
         }
      }

      return false;
   }

   public static Vec3 calculateClosestPoint(Vec3 target, WitherStormBase entity, int head) {
      return calculateClosestPoint(target, entity, head, 0.0);
   }

   public static Vec3 calculateClosestPoint(Vec3 target, WitherStormBase entity, int head, double distanceOffset) {
      float x = entity.getHeadXRot(head);
      float y = entity.getHeadYRot(head);
      Vec3 headPos = entity.getHeadPos(head);
      double cutoff = entity.getTractorBeamCutoffDistance(head);
      float distanceToHead = (float)(headPos.distanceTo(target) + distanceOffset);
      if (cutoff != -1.0) {
         distanceToHead = Mth.clamp(distanceToHead, 0.0F, (float)cutoff);
      }

      return headPos.add(entity.getViewVector(x, y, distanceToHead));
   }

   public static Vec3 calculateClosestPoint(Entity target, LivingEntity entity) {
      Vec3 headPos = entity.getEyePosition();
      float distanceToHead = (float)headPos.distanceTo(target.position());
      Vec3 closest = headPos.add(getViewVector(entity).scale((double)distanceToHead));
      BlockHitResult ray = target.level().clip(new ClipContext(headPos, closest, Block.COLLIDER, Fluid.NONE, target));
      return ray.getLocation();
   }

   private static Vec3 getViewVector(LivingEntity entity) {
      float xRot = entity.getXRot() * (float) (Math.PI / 180.0);
      float yRot = -entity.yHeadRot * (float) (Math.PI / 180.0);
      float cosY = Mth.cos(yRot);
      float sinY = Mth.sin(yRot);
      float cosX = Mth.cos(xRot);
      float sinX = Mth.sin(xRot);
      return new Vec3((double)(sinY * cosX), (double)(-sinX), (double)(cosY * cosX));
   }
}
