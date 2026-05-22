package nonamecrackers2.witherstormmod.mixin;

import java.util.function.Predicate;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({CrossbowItem.class})
public class MixinCrossbowItem {
   @Unique
   private static final Predicate<ItemStack> HELD = stack -> stack.getItem() == Items.ENDER_PEARL;

   @Inject(
      method = {"getSupportedHeldProjectiles"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getSupportedHeldProjectilesHead(CallbackInfoReturnable<Predicate<ItemStack>> callback) {
      if ((Boolean)WitherStormModConfig.SERVER.crossbowsSupportEnderPearls.get()) {
         callback.setReturnValue(ProjectileWeaponItem.ARROW_OR_FIREWORK.or(HELD));
      }
   }

   @Inject(
      method = {"shootProjectile"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void shootProjectile(
      Level world,
      LivingEntity entity,
      InteractionHand hand,
      ItemStack stack,
      ItemStack projectile,
      float shotPitch,
      boolean instabuild,
      float power,
      float f,
      float offset,
      CallbackInfo callback
   ) {
      if (!world.isClientSide && (Boolean)WitherStormModConfig.SERVER.crossbowsSupportEnderPearls.get() && projectile.getItem() == Items.ENDER_PEARL) {
         Projectile projectileEntity = new ThrownEnderpearl(world, entity);
         if (entity instanceof CrossbowAttackMob crossbowUser) {
            crossbowUser.shootCrossbowProjectile(crossbowUser.getTarget(), stack, projectileEntity, offset);
         } else {
            Vec3 upVector = entity.getUpVector(1.0F);
            Quaternionf quaternion = new Quaternionf()
               .setAngleAxis((double)(offset * (float) (Math.PI / 180.0)), upVector.x, upVector.y, upVector.z);
            Vec3 viewVector = entity.getViewVector(1.0F);
            Vector3f vector3f = viewVector.toVector3f().rotate(quaternion);
            projectileEntity.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), power, f);
         }

         stack.hurtAndBreak(3, entity, livingEntity -> livingEntity.broadcastBreakEvent(hand));
         world.addFreshEntity(projectileEntity);
         world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, shotPitch);
         callback.cancel();
      }
   }
}
