package nonamecrackers2.witherstormmod.common.entity.ai.symbiont;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.packet.PlayerMotionMessage;

public class PullSpell extends SymbiontSpell {
   public PullSpell(WitheredSymbiontEntity symbiont, SpellType type) {
      super(symbiont, type);
   }

   @Override
   public void cast(LivingEntity target) {
   }

   @Override
   public void doCasting(LivingEntity target) {
      for (LivingEntity entity : this.entity.getNearbyTargets(WitheredSymbiontEntity.MOB_TARGET_PREDICATE).toList()) {
         if (entity.isAlive() && entity != this.entity) {
            Vec3 delta = this.entity.position().subtract(entity.position()).normalize().scale(0.1);
            if ((double)entity.distanceTo(this.entity) < 3.0) {
               this.entity.breakSpell();
               return;
            }

            delta = new Vec3(delta.x(), entity.getDeltaMovement().y(), delta.z());
            entity.setDeltaMovement(delta);
            if (entity instanceof ServerPlayer) {
               PlayerMotionMessage message = new PlayerMotionMessage(delta);
               WitherStormModPacketHandlers.MAIN.send(PacketDistributor.PLAYER.with((ServerPlayer)entity), message);
            }

            double x = entity.getX() + entity.getRandom().nextGaussian() * entity.getBoundingBox().getXsize() * 0.4;
            double y = entity.getBoundingBox().getCenter().y + entity.getRandom().nextGaussian() * entity.getBoundingBox().getYsize() * 0.4;
            double z = entity.getZ() + entity.getRandom().nextGaussian() * entity.getBoundingBox().getZsize() * 0.4;
            Vec3 particleDelta = this.entity.getEyePosition(1.0F).subtract(x, y, z).normalize().scale(0.1);
            ((ServerLevel)this.entity.level())
               .sendParticles(
                  WitherStormModParticleTypes.COMMAND_BLOCK.get(),
                  x,
                  y,
                  z,
                  0,
                  particleDelta.x(),
                  particleDelta.y(),
                  particleDelta.z(),
                  1.0
               );
         }
      }
   }

   @Override
   public int getDelay(RandomSource random, float modifier) {
      return Math.max(340, random.nextInt(420)) - Mth.floor(modifier) * 10;
   }
}
