package nonamecrackers2.witherstormmod.mixin;

import net.neoforged.api.distmarker.Dist;

import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({AbstractSkeleton.class})
public abstract class MixinAbstractSkeleton extends Monster {
   private MixinAbstractSkeleton(EntityType<? extends Monster> type, Level level) {
      super(type, level);
   }

   @Inject(
      method = {"performRangedAttack"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;getX()D",
         ordinal = 0
      )},
      cancellable = true,
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   public void performRangedAttackInvoke(LivingEntity entity, float f, CallbackInfo ci, ItemStack stack, AbstractArrow arrow) {
      if (entity instanceof WitherStormEntity storm) {
         Pair<Boolean, Integer> pair = TractorBeamHelper.isInsideTractorBeam(this, storm, 4.0);
         if ((Boolean)pair.getFirst()) {
            Vec3 pos = storm.getHeadPos((Integer)pair.getSecond());
            double x = pos.x() - this.getX();
            double y = pos.y() - arrow.getY();
            double z = pos.z() - this.getZ();
            double dist = Math.sqrt(x * x + z * z);
            arrow.shoot(x, y + dist * 0.2F, z, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(arrow);
            ci.cancel();
         }
      }
   }
}
