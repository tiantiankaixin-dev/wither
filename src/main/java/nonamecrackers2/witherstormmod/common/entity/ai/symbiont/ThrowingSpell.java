package nonamecrackers2.witherstormmod.common.entity.ai.symbiont;

import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import org.jetbrains.annotations.NotNull;

public class ThrowingSpell extends SymbiontSpell {
   public ThrowingSpell(WitheredSymbiontEntity symbiont, SpellType type) {
      super(symbiont, type);
   }

   @Override
   public void start(LivingEntity target) {
      this.projectiles.clear();
   }

   @Override
   public void doCasting(LivingEntity target) {
      if (this.entity.tickCount % Math.max(2, this.entity.getRandom().nextInt(24 / this.entity.level().getDifficulty().getId())) == 0) {
         int randomProjectile = this.entity.getRandom().nextInt(5);
         int randomPotion = this.entity.getRandom().nextInt(8);
         int randomArrow = this.entity.getRandom().nextInt(12);
         Projectile projectile = this.getRandomProjectile(randomProjectile);
         if (projectile instanceof ThrownPotion thrownPotion) {
            projectile.setXRot(projectile.getXRot() - -20.0F);
            MobEffectInstance potion = getPotion(randomPotion);
            ItemStack stack = new ItemStack(Items.SPLASH_POTION);
            PotionUtils.setPotion(stack, Potions.WATER);
            if (potion != null) {
               PotionUtils.setCustomEffects(stack, Lists.newArrayList(new MobEffectInstance[]{potion}));
            }

            thrownPotion.setItem(stack);
         }

         if (projectile instanceof Arrow arrow && randomArrow < 6) {
            arrow.addEffect(getArrowTip(randomPotion));
         }

         projectile.setNoGravity(true);
         double offsetX = this.entity.getRandom().nextGaussian() * 5.0;
         double offsetY = this.entity.getRandom().nextDouble() * 10.0;
         double offsetZ = this.entity.getRandom().nextGaussian() * 5.0;
         projectile.setPos(this.entity.getX() + offsetX, this.entity.getEyeY() + offsetY + 2.0, this.entity.getZ() + offsetZ);
         this.entity.level().addFreshEntity(projectile);
         this.projectiles.add(projectile);
      }

      for (Entity entity : this.projectiles) {
         if (entity instanceof Projectile) {
            Projectile projectilex = (Projectile)entity;
            LivingEntity nearestTarget = this.entity.getRandomNearbyTargetOrFallback(target, WitheredSymbiontEntity.TARGET_PREDICATE);
            double x = projectilex.getX() + this.entity.getRandom().nextGaussian() * 0.5;
            double y = projectilex.getEyeY() + this.entity.getRandom().nextGaussian() * 0.5;
            double z = projectilex.getZ() + this.entity.getRandom().nextGaussian() * 0.5;
            Vec3 delta = projectilex.getEyePosition(1.0F).subtract(x, y, z).normalize().multiply(0.1, 0.1, 0.1);
            ((ServerLevel)this.entity.level())
               .sendParticles(WitherStormModParticleTypes.COMMAND_BLOCK.get(), x, y, z, 0, delta.x(), delta.y(), delta.z(), 1.0);
            if (projectilex.isAlive() && projectilex.isNoGravity() && projectilex instanceof AbstractArrow) {
               Vec3 motion = nearestTarget.position().subtract(projectilex.position()).normalize().multiply(0.005, 0.005, 0.005);
               projectilex.setDeltaMovement(motion);
               projectilex.tick();
            }

            if (projectilex.tickCount == 40 && nearestTarget != null) {
               projectilex.setDeltaMovement(Vec3.ZERO);
               double x1 = nearestTarget.getX() - projectilex.getX();
               double y1 = nearestTarget.getY(0.3333333333333333) - projectilex.getY();
               double z1 = nearestTarget.getZ() - projectilex.getZ();
               double distance = Math.sqrt(x1 * x1 + z1 * z1);
               projectilex.setNoGravity(false);
               projectilex.playSound(SoundEvents.ARROW_SHOOT, 4.0F, 1.0F);
               if (projectilex instanceof ThrownPotion) {
                  projectilex.shoot(x1, y1 + distance * 0.2, z1, (float)(0.75 + distance * 0.02), 8.0F);
               } else {
                  projectilex.shoot(
                     x1, y1 + distance * 0.2, z1, (float)(1.6F + distance * 0.02), (float)(14 - this.entity.level().getDifficulty().getId() * 4)
                  );
               }
            }
         }
      }
   }

   @Nullable
   private static MobEffectInstance getPotion(int randomPotion) {
      switch (randomPotion) {
         case 0:
            return new MobEffectInstance(MobEffects.WITHER, 60, 2);
         case 1:
            return new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 400, 2);
         case 2:
            return new MobEffectInstance(MobEffects.HUNGER, 100, 1);
         case 3:
            return new MobEffectInstance(MobEffects.UNLUCK, 800, 2);
         case 4:
            return new MobEffectInstance(MobEffects.WEAKNESS, 100, 1);
         case 5:
            return new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1);
         case 6:
            return new MobEffectInstance(MobEffects.BLINDNESS, 60);
         default:
            return null;
      }
   }

   @NotNull
   private Projectile getRandomProjectile(int randomProjectile) {
      return switch (randomProjectile) {
         case 0 -> new ThrownPotion(this.entity.level(), this.entity);
         case 1 -> new Snowball(this.entity.level(), this.entity);
         case 2 -> new Arrow(this.entity.level(), this.entity);
         case 3 -> new SpectralArrow(this.entity.level(), this.entity);
         default -> new ThrownTrident(this.entity.level(), this.entity, new ItemStack(Items.TRIDENT));
      };
   }

   @NotNull
   private static MobEffectInstance getArrowTip(int randomArrow) {
      return switch (randomArrow) {
         case 0 -> new MobEffectInstance(MobEffects.BLINDNESS, 10, 5);
         case 1 -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 8);
         case 2 -> new MobEffectInstance(MobEffects.WEAKNESS, 10, 1);
         case 3 -> new MobEffectInstance(MobEffects.HUNGER, 10, 2);
         case 4 -> new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 10, 3);
         default -> new MobEffectInstance(MobEffects.CONFUSION, 10);
      };
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
      return Math.max(360, random.nextInt(480)) - Mth.floor(modifier) * 10;
   }
}
