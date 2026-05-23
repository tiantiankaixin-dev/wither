package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.util.SnowballAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Snowball.class})
public class MixinSnowball implements SnowballAccessor {
   @Unique
   private boolean givesWitherEffect;

   @Override
   public void setWitherEffect(boolean flag) {
      this.givesWitherEffect = flag;
   }

   @Override
   public boolean hasWitherEffect() {
      return this.givesWitherEffect;
   }

   @Inject(
      method = {"onHitEntity"},
      at = {@At("HEAD")}
   )
   public void onHitEntity(EntityHitResult result, CallbackInfo callbackInfo) {
      Entity entity = result.getEntity();
      if (this.givesWitherEffect && entity instanceof LivingEntity) {
         ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.WITHER, 150, 1));
      }

      if (!entity.level().isClientSide() && entity.level() instanceof ServerLevel level && this.givesWitherEffect) {
         for (int i = 0; i < 5; i++) {
            double x = entity.getX() + level.getRandom().nextGaussian() * 0.5;
            double y = entity.getEyeY() + level.getRandom().nextGaussian() * 0.5;
            double z = entity.getZ() + level.getRandom().nextGaussian() * 0.5;
            Vec3 delta = entity.getEyePosition(1.0F).subtract(x, y, z).normalize().multiply(0.1, 0.1, 0.1);
            level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 0, delta.x(), delta.y(), delta.z(), 1.0);
         }
      }
   }
}
