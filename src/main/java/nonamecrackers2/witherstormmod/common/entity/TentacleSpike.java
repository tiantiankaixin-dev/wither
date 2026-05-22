package nonamecrackers2.witherstormmod.common.entity;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;

public class TentacleSpike extends Entity implements TraceableEntity {
   @Nullable
   private UUID ownerUUID;
   @Nullable
   private LivingEntity owner;
   private boolean sentSpikeEvent;
   private int warmupDelayTicks;
   private int lifeTicks = 22;
   private boolean clientSideAttackStarted;
   private float damageModifier;

   public TentacleSpike(EntityType<? extends TentacleSpike> type, Level level) {
      super(type, level);
   }

   public TentacleSpike(Level level, double x, double y, double z, float angle, int warmupDelay, LivingEntity owner, float damageModifier) {
      this((EntityType<? extends TentacleSpike>)WitherStormModEntityTypes.TENTACLE_SPIKE.get(), level);
      this.warmupDelayTicks = warmupDelay;
      this.setOwner(owner);
      this.setYRot(angle * (180.0F / (float)Math.PI));
      this.setPos(x, y, z);
      this.damageModifier = damageModifier;
   }

   protected void defineSynchedData() {
   }

   public void setOwner(@Nullable LivingEntity entity) {
      this.owner = entity;
      this.ownerUUID = entity == null ? null : entity.getUUID();
   }

   @Nullable
   public LivingEntity getOwner() {
      if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel level) {
         Entity entity = level.getEntity(this.ownerUUID);
         if (entity instanceof LivingEntity) {
            this.owner = (LivingEntity)entity;
         }
      }

      return this.owner;
   }

   protected void readAdditionalSaveData(CompoundTag tag) {
      this.warmupDelayTicks = tag.getInt("WarmupDelay");
      if (tag.hasUUID("Owner")) {
         this.ownerUUID = tag.getUUID("Owner");
      }

      this.damageModifier = tag.getFloat("DamageModifier");
   }

   protected void addAdditionalSaveData(CompoundTag tag) {
      tag.putInt("WarmupDelay", this.warmupDelayTicks);
      if (this.ownerUUID != null) {
         tag.putUUID("Owner", this.ownerUUID);
      }

      tag.putFloat("DamageModifier", this.damageModifier);
   }

   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         if (this.clientSideAttackStarted) {
            this.lifeTicks--;
            if (this.lifeTicks == 20) {
               for (int i = 0; i < 12; i++) {
                  double d0 = this.getX() + (this.random.nextDouble() * 2.0 - 1.0) * (double)this.getBbWidth() * 0.5;
                  double d1 = this.getY() + 0.05 + this.random.nextDouble() * 2.0;
                  double d2 = this.getZ() + (this.random.nextDouble() * 2.0 - 1.0) * (double)this.getBbWidth() * 0.5;
                  double d3 = (this.random.nextDouble() * 2.0 - 1.0) * 0.3;
                  double d4 = 0.3 + this.random.nextDouble() * 0.3;
                  double d5 = (this.random.nextDouble() * 2.0 - 1.0) * 0.3;
                  this.level().addParticle(ParticleTypes.CRIT, d0, d1, d2, d3, d4, d5);
               }
            }
         }
      } else if (--this.warmupDelayTicks < 0) {
         if (this.warmupDelayTicks == -2) {
            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.6, 0.0, 0.6))) {
               this.dealDamageTo(entity);
            }
         }

         if (!this.sentSpikeEvent) {
            this.level().broadcastEntityEvent(this, (byte)4);
            this.sentSpikeEvent = true;
         }

         if (--this.lifeTicks < 0) {
            this.discard();
         }
      }
   }

   private void dealDamageTo(LivingEntity entity) {
      LivingEntity owner = this.getOwner();
      if (entity.isAlive() && !entity.isInvulnerable() && entity != owner) {
         if (owner == null) {
            entity.hurt(this.damageSources().magic(), 6.0F);
         } else {
            if (owner.isAlliedTo(entity)) {
               return;
            }

            entity.hurt(this.damageSources().indirectMagic(this, owner), 6.0F + this.damageModifier);
         }
      }
   }

   public void handleEntityEvent(byte event) {
      super.handleEntityEvent(event);
      if (event == 4) {
         this.clientSideAttackStarted = true;
         if (!this.isSilent()) {
            this.level()
               .playLocalSound(
                  this.getX(),
                  this.getY(),
                  this.getZ(),
                  WitherStormModSoundEvents.TENTACLE_SPIKE_STAB.get(),
                  this.getSoundSource(),
                  1.0F,
                  this.random.nextFloat() * 0.2F + 0.85F,
                  false
               );
         }
      }
   }

   public float getAnimationProgress(float partialTick) {
      if (!this.clientSideAttackStarted) {
         return 0.0F;
      } else {
         int i = this.lifeTicks - 2;
         return i <= 0 ? 1.0F : 1.0F - ((float)i - partialTick) / 20.0F;
      }
   }
}
