package nonamecrackers2.witherstormmod.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor.TargetPoint;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;

public class BlueFlamingWitherSkullEntity extends FlamingWitherSkullEntity {
   public BlueFlamingWitherSkullEntity(EntityType<? extends BlueFlamingWitherSkullEntity> type, Level world) {
      super(type, world);
   }

   public BlueFlamingWitherSkullEntity(EntityType<? extends BlueFlamingWitherSkullEntity> type, Level world, LivingEntity owner, double x, double y, double z) {
      super(type, world, owner, x, y, z);
   }

   public BlueFlamingWitherSkullEntity(Level world, LivingEntity owner, double x, double y, double z) {
      this((EntityType<? extends BlueFlamingWitherSkullEntity>)WitherStormModEntityTypes.BLUE_FLAMING_WITHER_SKULL.get(), world, owner, x, y, z);
   }

   @Override
   protected float getInertia() {
      return 0.85F;
   }

   @Override
   protected ParticleOptions getParticle() {
      return ParticleTypes.SOUL_FIRE_FLAME;
   }

   @Override
   protected void explodeAndDiscard() {
      boolean flag = ForgeEventFactory.getMobGriefingEvent(this.level(), this.getOwner());
      this.playSound(
         WitherStormModSoundEvents.FLAMING_SKULL_IMPACT.get(), 6.0F, (this.random.nextFloat() - this.random.nextFloat()) * -0.2F + 0.8F
      );
      WitherStormModPacketHandlers.MAIN
         .send(
            PacketDistributor.NEAR
               .with(TargetPoint.p(this.position().x, this.position().y, this.position().z, 60.0, this.level().dimension())),
            new ShakeScreenMessage(20.0F, 6.0F)
         );
      this.level()
         .explode(
            this,
            this.getX(),
            this.getY(),
            this.getZ(),
            (float)((Double)WitherStormModConfig.SERVER.flamingSkullExplosionSize.get() + 4.0),
            flag,
            ExplosionInteraction.MOB
         );
      this.discard();
   }

   public float getBlockExplosionResistance(Explosion explosion, BlockGetter blockGetter, BlockPos pos, BlockState state, FluidState fluidState, float resistance) {
      float blastResistance = state.getExplosionResistance(blockGetter, pos, explosion);
      if (blastResistance <= 1200.0F) {
         return state.canEntityDestroy(blockGetter, pos, this) ? Math.min(0.8F, resistance) : resistance;
      } else {
         return resistance;
      }
   }
}
