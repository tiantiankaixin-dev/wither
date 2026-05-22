package nonamecrackers2.witherstormmod.common.entity.ai.symbiont;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;

public class ArrowsSpell extends SymbiontSpell {
   public ArrowsSpell(WitheredSymbiontEntity entity, SpellType type) {
      super(entity, type);
   }

   @Override
   public void start(LivingEntity target) {
      for (Entity projectile : this.projectiles) {
         projectile.discard();
      }

      this.projectiles.clear();
   }

   @Override
   public void doCasting(LivingEntity target) {
      int count = this.entity.shouldIncreaseDifficulty() ? 6 : 3;
      int timer = this.entity.shouldIncreaseDifficulty() ? 10 : 20;
      if (this.entity.tickCount % 4 == 0) {
         for (int i = 0; i < count; i++) {
            Arrow arrow = new Arrow(this.entity.level(), this.entity);
            arrow.setNoGravity(true);
            double deltaX = this.entity.getRandom().nextGaussian() * 0.55;
            double deltaY = this.entity.getRandom().nextDouble() * 0.75;
            double deltaZ = this.entity.getRandom().nextGaussian() * 0.55;
            arrow.setDeltaMovement(deltaX, deltaY, deltaZ);
            Vec2 rot = this.getRot(new Vec3(deltaX, deltaY, deltaZ));
            arrow.setXRot(rot.x);
            arrow.setYRot(rot.y);
            this.entity.level().addFreshEntity(arrow);
            this.projectiles.add(arrow);
         }

         this.entity.playSound(SoundEvents.DISPENSER_DISPENSE, 4.0F, 0.5F + (this.entity.getRandom().nextFloat() - 0.5F) * 0.1F);
      }

      for (Entity projectile : this.projectiles) {
         AbstractArrow arrow = (AbstractArrow)projectile;
         if (projectile.isAlive() && projectile.tickCount == timer) {
            LivingEntity nearestTarget = this.entity.getRandomNearbyTargetOrFallback(target, WitheredSymbiontEntity.TARGET_PREDICATE);
            if (nearestTarget != null) {
               double x = nearestTarget.getX() + this.entity.getRandom().nextGaussian() * 2.0 - arrow.getX();
               double y = nearestTarget.getY(0.3333333333333333) + this.entity.getRandom().nextGaussian() * 0.5 - arrow.getY();
               double z = nearestTarget.getZ() + this.entity.getRandom().nextGaussian() * 2.0 - arrow.getZ();
               double d0 = Math.sqrt(x * x + z * z);
               arrow.shoot(x, y + d0 * 0.2, z, 2.5F, (float)(14 - this.entity.level().getDifficulty().getId() * 4));
               arrow.setNoGravity(false);
               arrow.playSound(SoundEvents.ARROW_SHOOT, 2.0F, 1.0F);
            }
         }

         if (projectile.isAlive() && projectile.tickCount < timer) {
            double x = projectile.getX() + this.entity.getRandom().nextGaussian() * 0.5;
            double y = projectile.getEyeY() + this.entity.getRandom().nextGaussian() * 0.5;
            double z = projectile.getZ() + this.entity.getRandom().nextGaussian() * 0.5;
            Vec3 delta = projectile.getEyePosition(1.0F).subtract(x, y, z).normalize().multiply(0.1, 0.1, 0.1);
            ((ServerLevel)this.entity.level())
               .sendParticles(WitherStormModParticleTypes.COMMAND_BLOCK.get(), x, y, z, 0, delta.x(), delta.y(), delta.z(), 1.0);
         }
      }
   }

   @Override
   public void cast(LivingEntity target) {
   }

   @Override
   public void finish() {
      for (Entity projectile : this.projectiles) {
         double x = projectile.getX() + this.entity.getRandom().nextGaussian() * 0.5;
         double y = projectile.getEyeY() + this.entity.getRandom().nextGaussian() * 0.5;
         double z = projectile.getZ() + this.entity.getRandom().nextGaussian() * 0.5;
         Vec3 delta = projectile.getEyePosition(1.0F).subtract(x, y, z).normalize().multiply(0.1, 0.1, 0.1);
         ((ServerLevel)this.entity.level()).sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 0, delta.x(), delta.y(), delta.z(), 1.0);
         projectile.discard();
      }
   }

   @Override
   public int getDelay(RandomSource random, float modifier) {
      return Math.max(160, random.nextInt(200)) - Mth.floor(modifier) * 10;
   }

   private Vec2 getRot(Vec3 delta) {
      float f = Mth.sqrt((float)delta.horizontalDistanceSqr());
      float xRot = (float)(Mth.atan2(delta.y, (double)f) * 180.0F / (float)Math.PI);
      float yRot = (float)(Mth.atan2(delta.x, delta.z) * 180.0F / (float)Math.PI);
      return new Vec2(xRot, yRot);
   }
}
