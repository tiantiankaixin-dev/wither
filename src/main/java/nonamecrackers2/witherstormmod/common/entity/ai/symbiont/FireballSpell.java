package nonamecrackers2.witherstormmod.common.entity.ai.symbiont;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.common.entity.FlamingWitherSkullEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.UpdateDamagingProjectileMessage;

public class FireballSpell extends SymbiontSpell {
   private final FireballSpell.ProjectileFactory factory;
   private final int count;
   private final int throwInterval;

   public FireballSpell(WitheredSymbiontEntity symbiont, SpellType type, FireballSpell.ProjectileFactory factory, int count) {
      super(symbiont, type);
      this.factory = factory;
      this.count = count;
      this.throwInterval = type.spellTime() / count;
   }

   @Override
   public void start(LivingEntity target) {
      this.projectiles.clear();
      double y = this.entity.getEyeY() + 4.0;
      float theta = (float)((Math.PI * 2) / (double)this.count);

      for (int i = 0; i < this.count; i++) {
         float angle = theta * (float)i;
         double x = 7.0 * (double)Mth.cos(angle) + this.entity.getX();
         double z = 7.0 * (double)Mth.sin(angle) + this.entity.getZ();
         Projectile projectile = this.factory.make(this.entity.level(), this.entity, 0.0, 0.0, 0.0);
         projectile.setPos(x, y, z);
         projectile.setNoGravity(true);
         this.entity.level().addFreshEntity(projectile);
         this.projectiles.add(projectile);
      }
   }

   @Override
   public void cast(LivingEntity target) {
   }

   @Override
   public void doCasting(LivingEntity target) {
      int size = this.projectiles.size();
      int spellCastingTime = this.type.spellTime() - this.entity.getSpellCastingTime();
      float theta = (float) (Math.PI * 2) / (float)size;
      double radius = 7.0;

      for (int i = this.projectiles.size() - 1; i >= 0; i--) {
         Entity projectile = this.projectiles.get(i);
         if (projectile instanceof AbstractHurtingProjectile damaging && projectile.isAlive()) {
            float angle = theta * (float)i + (float)spellCastingTime * 0.08F;
            double x = radius * (double)Mth.cos(angle) + this.entity.getX();
            double y = this.entity.getEyeY() + 4.0;
            double z = radius * (double)Mth.sin(angle) + this.entity.getZ();
            Vec3 wanted = new Vec3(x, y, z);
            double distance = damaging.position().distanceTo(wanted);
            double multiplier = Math.min(1.0, distance);
            Vec3 delta = wanted.subtract(damaging.position()).normalize().multiply(multiplier, multiplier, multiplier);
            if (spellCastingTime % this.throwInterval == 0 && i == 0) {
               Vec3 targetDelta = this.entity
                  .getRandomNearbyTargetOrFallback(target, WitheredSymbiontEntity.TARGET_PREDICATE)
                  .getEyePosition(1.0F)
                  .subtract(damaging.position())
                  .normalize()
                  .multiply(0.1, 0.1, 0.1);
               if (projectile instanceof FlamingWitherSkullEntity) {
                  damaging.playSound(WitherStormModSoundEvents.WITHER_STORM_SHOOT.get(), 4.0F, 1.0F);
               } else {
                  damaging.playSound(SoundEvents.BLAZE_SHOOT, 4.0F, 1.0F);
               }

               this.projectiles.remove(i);
               damaging.setDeltaMovement(Vec3.ZERO);
               damaging.xPower = targetDelta.x();
               damaging.yPower = targetDelta.y();
               damaging.zPower = targetDelta.z();
               UpdateDamagingProjectileMessage message = new UpdateDamagingProjectileMessage(damaging);
               WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(() -> damaging), message);
            } else {
               damaging.setDeltaMovement(delta);
               ((ServerChunkCache)this.entity.getCommandSenderWorld().getChunkSource()).broadcast(damaging, new ClientboundSetEntityMotionPacket(damaging));
            }
            continue;
         }

         this.projectiles.remove(i);
      }
   }

   @Override
   public void finish() {
      for (Entity projectile : this.projectiles) {
         projectile.discard();
      }
   }

   @Override
   public int getDelay(RandomSource random, float modifier) {
      return Math.max(400, random.nextInt(520)) - Mth.floor(modifier) * 10;
   }

   @FunctionalInterface
   public interface ProjectileFactory {
      AbstractHurtingProjectile make(Level var1, LivingEntity var2, double var3, double var5, double var7);
   }
}
