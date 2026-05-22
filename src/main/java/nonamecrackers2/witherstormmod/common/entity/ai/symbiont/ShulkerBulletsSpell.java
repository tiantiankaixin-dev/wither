package nonamecrackers2.witherstormmod.common.entity.ai.symbiont;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.mixin.IMixinShulkerBullet;
import org.jetbrains.annotations.NotNull;

public class ShulkerBulletsSpell extends SymbiontSpell {
   public ShulkerBulletsSpell(WitheredSymbiontEntity symbiont, SpellType type) {
      super(symbiont, type);
   }

   @Override
   public void start(@NotNull LivingEntity target) {
      this.projectiles.clear();
      double y = this.entity.getEyeY();
      int amount = this.entity.shouldIncreaseDifficulty() ? 6 : 5;

      for (int i = 0; i < amount; i++) {
         float theta = (float)((Math.PI * 2) / (double)amount);
         float angle = theta * (float)i;
         double x = 5.0 * (double)Mth.cos(angle) + this.entity.getX();
         double z = 5.0 * (double)Mth.sin(angle) + this.entity.getZ();
         ShulkerBullet projectile = new ShulkerBullet(EntityType.SHULKER_BULLET, this.entity.level());
         projectile.moveTo(x, y, z);
         projectile.setNoGravity(true);
         projectile.setOwner(this.entity);
         this.entity.level().addFreshEntity(projectile);
         this.projectiles.add(projectile);
      }
   }

   @Override
   public void cast(LivingEntity target) {
      int size = this.projectiles.size();

      for (int i = 0; i < size; i++) {
         Entity projectile = this.projectiles.get(i);
         if (projectile instanceof ShulkerBullet) {
            IMixinShulkerBullet bullet = (IMixinShulkerBullet)projectile;
            bullet.setFinalTarget(this.entity.getRandomNearbyTargetOrFallback(target, WitheredSymbiontEntity.TARGET_PREDICATE));
            bullet.setCurrentMoveDirection(Direction.UP);
            bullet.callSelectNextMoveDirection(Axis.Y);
            projectile.setNoGravity(false);
         }
      }

      this.entity.playSound(SoundEvents.SHULKER_SHOOT, 4.0F, 1.0F);
      this.projectiles.clear();
   }

   @Override
   public void doCasting(LivingEntity target) {
      int size = this.projectiles.size();
      int spellCastingTime = this.type.spellTime() - this.entity.getSpellCastingTime();

      for (int i = 0; i < size; i++) {
         Entity entity = this.projectiles.get(i);
         if (entity.isAlive()) {
            float theta = (float)((Math.PI * 2) / (double)size);
            float angle = theta * (float)i + (float)spellCastingTime * 0.1F;
            double radius = 5.0;
            double x = radius * (double)Mth.cos(angle) + this.entity.getX();
            double y = this.entity.getEyeY();
            double z = radius * (double)Mth.sin(angle) + this.entity.getZ();
            Vec3 wanted = new Vec3(x, y, z);
            double distance = entity.position().distanceTo(wanted);
            Vec3 delta = wanted.subtract(entity.position()).normalize().multiply(distance, distance, distance);
            entity.setDeltaMovement(delta);
            BlockPos.betweenClosedStream(entity.getBoundingBox().inflate(1.0)).forEach(pos -> entity.level().destroyBlock(pos, true));
         }
      }
   }

   @Override
   public int getDelay(RandomSource random, float modifier) {
      return Math.max(500, random.nextInt(620)) - Mth.floor(modifier) * 10;
   }
}
