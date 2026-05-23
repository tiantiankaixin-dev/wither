package nonamecrackers2.witherstormmod.common.entity.ai.symbiont;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.common.entity.SickenedCreeper;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import org.jetbrains.annotations.NotNull;

public class BombingSpell extends SymbiontSpell {
   public BombingSpell(WitheredSymbiontEntity symbiont, SpellType type) {
      super(symbiont, type);
   }

   @Override
   public void start(LivingEntity target) {
      double x = this.entity.getX() + this.entity.getRandom().nextGaussian() * 1.0;
      double y = this.entity.getEyeY() + 1.0;
      double z = this.entity.getZ() + this.entity.getRandom().nextGaussian() * 1.0;
      SickenedCreeper livingProjectile = new SickenedCreeper(
         (EntityType<? extends SickenedCreeper>)WitherStormModEntityTypes.SICKENED_CREEPER.get(), this.entity.level()
      );
      livingProjectile.setNoGravity(true);
      livingProjectile.setPos(x, y, z);
      livingProjectile.setDeltaMovement(0.0, this.entity.getRandom().nextDouble() * 0.07, 0.0);
      this.entity.level().addFreshEntity(livingProjectile);
      this.projectiles.add(livingProjectile);
      livingProjectile.setTarget(target);
      this.entity.playSound(SoundEvents.CREEPER_HURT, 4.0F, 0.75F);
   }

   @Override
   public void cast(@NotNull LivingEntity target) {
      for (Entity livingProjectile : this.projectiles) {
         SickenedCreeper projectile = (SickenedCreeper)livingProjectile;
         LivingEntity targetEntity = this.entity.getRandomNearbyTargetOrFallback(target, WitheredSymbiontEntity.TARGET_PREDICATE);
         double x = targetEntity.getX() - projectile.getX();
         double y = this.entity.getRandom().nextGaussian() * 4.0 + targetEntity.getY(0.34) - projectile.getY();
         double z = targetEntity.getZ() - projectile.getZ();
         double distance = Math.sqrt(x * x + y * y + z * z);
         projectile.setDeltaMovement(x / (distance * 0.3), y / (distance * 0.25) + 1.0, z / (distance * 0.3));
         projectile.setNoGravity(false);
         projectile.setTarget(targetEntity);
         projectile.ignite();
      }

      this.entity.playSound(SoundEvents.CREEPER_PRIMED, 4.0F, 1.0F);
   }

   @Override
   public void doCasting(@NotNull LivingEntity target) {
      for (Entity livingProjectile : this.projectiles) {
         if (livingProjectile.isAlive()) {
            double x = livingProjectile.getX() + this.entity.getRandom().nextGaussian() * 1.0;
            double y = livingProjectile.getEyeY() + this.entity.getRandom().nextGaussian() * 1.0;
            double z = livingProjectile.getZ() + this.entity.getRandom().nextGaussian() * 1.0;
            Vec3 delta = livingProjectile.getEyePosition(1.0F).subtract(x, y, z).normalize().multiply(0.1, 0.1, 0.1);
            ((ServerLevel)this.entity.level())
               .sendParticles(WitherStormModParticleTypes.COMMAND_BLOCK.get(), x, y, z, 0, delta.x(), delta.y(), delta.z(), 1.0);
            ((ServerLevel)this.entity.level())
               .sendParticles(
                  ParticleTypes.LARGE_SMOKE,
                  livingProjectile.getX(),
                  livingProjectile.getY(),
                  livingProjectile.getZ(),
                  0,
                  delta.x(),
                  delta.y(),
                  delta.z(),
                  0.125
               );
         }
      }
   }

   @Override
   public void finish() {
      if (this.entity.getTarget() == null) {
         for (Entity livingProjectile : this.projectiles) {
            livingProjectile.discard();
         }
      }
   }

   @Override
   public int getDelay(RandomSource random, float modifier) {
      return Math.max(360, random.nextInt(600)) - Mth.floor(modifier) * 10;
   }
}
