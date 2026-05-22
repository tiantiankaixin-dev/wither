package nonamecrackers2.witherstormmod.mixin;

import com.mojang.datafixers.util.Pair;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({SwellGoal.class})
public abstract class MixinSwellGoal {
   @Final
   @Shadow
   private Creeper creeper;
   @Shadow
   @Nullable
   private LivingEntity target;

   @Inject(
      method = {"canUse"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/monster/Creeper;getTarget()Lnet/minecraft/world/entity/LivingEntity;",
         shift = Shift.BY,
         by = 2
      )},
      cancellable = true,
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   public void canUseInvoke(CallbackInfoReturnable<Boolean> ci, LivingEntity livingentity) {
      Vec3 headPos = this.getHeadPosOfTractorBeamCreeperIsIn(livingentity);
      if (headPos != null && Math.sqrt(this.creeper.distanceToSqr(headPos)) < 12.0 && this.creeper.getRandom().nextInt(3) == 0) {
         ci.setReturnValue(true);
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void tickInvoke(CallbackInfo ci) {
      Vec3 headPos = this.getHeadPosOfTractorBeamCreeperIsIn(this.target);
      if (headPos != null) {
         if (this.creeper.getEyePosition().distanceTo(headPos) > 16.0) {
            this.creeper.setSwellDir(-1);
         } else {
            this.creeper.setSwellDir(1);
         }

         ci.cancel();
      }
   }

   @Unique
   @Nullable
   private Vec3 getHeadPosOfTractorBeamCreeperIsIn(@Nullable LivingEntity target) {
      if (target instanceof WitherStormEntity storm) {
         Pair<Boolean, Integer> pair = TractorBeamHelper.isInsideTractorBeam(this.creeper, storm, 4.0);
         if ((Boolean)pair.getFirst()) {
            return storm.getHeadPos((Integer)pair.getSecond());
         }
      }

      return null;
   }
}
