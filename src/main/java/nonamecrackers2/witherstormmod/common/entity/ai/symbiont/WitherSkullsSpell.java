package nonamecrackers2.witherstormmod.common.entity.ai.symbiont;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;

public class WitherSkullsSpell extends SymbiontSpell {
   public WitherSkullsSpell(WitheredSymbiontEntity symbiont, SpellType type) {
      super(symbiont, type);
   }

   @Override
   public void cast(LivingEntity target) {
   }

   @Override
   public void doCasting(LivingEntity target) {
      if (this.entity.tickCount % (this.entity.shouldIncreaseDifficulty() ? 10 : 20) == 0) {
         int spellCastingTime = this.type.spellTime() - this.entity.getSpellCastingTime();
         double radius = 2.0;
         float angle = (float)spellCastingTime * 0.08F;
         float angleBetweenTarget = (float)Math.atan2(target.getX() - this.entity.getX(), target.getZ() - this.entity.getZ());
         float offset = (float) (Math.PI / 2);
         double xOffset = (double)Mth.cos(offset - angleBetweenTarget) * radius;
         double zOffset = (double)Mth.sin(offset - angleBetweenTarget) * radius;
         double rawX = (double)(Mth.cos(angle) * Mth.cos(offset * 2.0F - angleBetweenTarget)) * radius;
         double rawY = (double)Mth.sin(angle) * radius;
         double rawZ = (double)(Mth.cos(angle) * Mth.sin(offset * 2.0F - angleBetweenTarget)) * radius;
         double x = xOffset + rawX + this.entity.getX();
         double y = this.entity.getEyeY() + rawY;
         double z = zOffset + rawZ + this.entity.getZ();
         Vec3 skullPose = new Vec3(x, y, z);
         Vec3 delta = skullPose.subtract(target.getEyePosition(1.0F)).normalize().scale(-2.5);
         WitherSkull skull = new WitherSkull(this.entity.level(), this.entity, delta);
         skull.setPos(x, y, z);
         if (this.entity.getRandom().nextInt(11) == 1) {
            skull.setDangerous(true);
         }

         this.entity.level().addFreshEntity(skull);
         skull.playSound(SoundEvents.WITHER_SHOOT, 4.0F, 1.0F);
         ((ServerLevel)this.entity.level())
            .sendParticles(
               ParticleTypes.LARGE_SMOKE,
               x,
               y,
               z,
               20,
               this.entity.getRandom().nextGaussian(),
               this.entity.getRandom().nextGaussian(),
               this.entity.getRandom().nextGaussian(),
               0.01
            );
         ((ServerLevel)this.entity.level())
            .sendParticles(
               WitherStormModParticleTypes.COMMAND_BLOCK.get(),
               x,
               y,
               z,
               20,
               this.entity.getRandom().nextGaussian(),
               this.entity.getRandom().nextGaussian(),
               this.entity.getRandom().nextGaussian(),
               0.2
            );
      }
   }

   @Override
   public int getDelay(RandomSource random, float modifier) {
      return Math.max(440, random.nextInt(580)) - Mth.floor(modifier) * 10;
   }
}
