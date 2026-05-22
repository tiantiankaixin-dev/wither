package nonamecrackers2.witherstormmod.common.entity.ai.symbiont;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;

public class SmashSpell extends SymbiontSpell {
   private int stompCount = 0;
   private int stompCooldown = 0;

   public SmashSpell(WitheredSymbiontEntity symbiont, SpellType type) {
      super(symbiont, type);
   }

   @Override
   public void start(LivingEntity target) {
      RandomSource random = this.entity.getRandom();
      this.stompCount = random.nextInt(3) + 1;
   }

   @Override
   public void cast(LivingEntity target) {
      if (this.entity.getBlockStateOn().getBlock() != Blocks.AIR) {
         this.stomp(target);
      }
   }

   private void stomp(LivingEntity target) {
      if (this.entity.getBlockStateOn().getBlock() != Blocks.AIR) {
         float f = this.entity.getJumpPower() * 5.0F;
         if (this.entity.hasEffect(MobEffects.JUMP)) {
            f += 0.1F * (float)(this.entity.getEffect(MobEffects.JUMP).getAmplifier() + 1);
         }

         Vec3 prevDelta = this.entity.getDeltaMovement();
         double x = this.entity.getX() - target.getX();
         double z = this.entity.getZ() - target.getZ();
         double multiplier = Math.min(0.2, (double)this.entity.distanceTo(target) * 0.05);
         Vec3 delta = new Vec3(x, (double)f, z).multiply(-multiplier, 1.0, -multiplier);
         delta.add(prevDelta.x, 0.0, prevDelta.z);
         this.entity.setDeltaMovement(delta);
         this.entity.setSmashing(true);
         this.entity.playSound(this.entity.getBlockStateOn().getSoundType().getBreakSound(), 4.0F, 1.0F);
      }
   }

   @Override
   public void doCasting(LivingEntity target) {
      if (this.entity.shouldIncreaseDifficulty()) {
         ((ServerLevel)this.entity.level())
            .sendParticles(
               ParticleTypes.FLAME,
               this.entity.getX() + (double)this.entity.getRandom().nextFloat(),
               this.entity.getY(),
               this.entity.getZ() + (double)this.entity.getRandom().nextFloat(),
               1,
               0.0,
               0.0,
               0.0,
               0.0
            );
      }

      if (this.stompCount > 0 && this.stompCooldown <= 0) {
         this.stomp(target);
         this.stompCount--;
         this.stompCooldown = 5;
         if (this.stompCount > 0) {
            LivingEntity newTarget = this.entity.getRandomNearbyTargetOrFallback(target, WitheredSymbiontEntity.TARGET_PREDICATE);
            this.cast(newTarget);
         }
      } else {
         this.stompCooldown--;
      }
   }

   @Override
   public int getDelay(RandomSource random, float modifier) {
      return Math.max(160, random.nextInt(200)) - Mth.floor(modifier) * 10;
   }
}
