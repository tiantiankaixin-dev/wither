package nonamecrackers2.witherstormmod.common.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.init.WitherStormModDamageTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateDamagingProjectileMessage;
import org.jetbrains.annotations.NotNull;

public class FlamingWitherSkullEntity extends AbstractHurtingProjectile implements IEntityAdditionalSpawnData {
   public FlamingWitherSkullEntity(EntityType<? extends FlamingWitherSkullEntity> type, Level world) {
      super(type, world);
   }

   public FlamingWitherSkullEntity(EntityType<? extends FlamingWitherSkullEntity> type, Level world, LivingEntity owner, double x, double y, double z) {
      super(type, owner, new Vec3(x, y, z), world);
   }

   public FlamingWitherSkullEntity(Level world, LivingEntity owner, double x, double y, double z) {
      this((EntityType<? extends FlamingWitherSkullEntity>)WitherStormModEntityTypes.FLAMING_WITHER_SKULL.get(), world, owner, x, y, z);
   }

   protected float getInertia() {
      return 0.9F;
   }

   public boolean isOnFire() {
      return true;
   }

   public boolean displayFireAnimation() {
      return false;
   }

   @NotNull
   protected ParticleOptions getTrailParticle() {
      return (ParticleOptions)(WitherStormMod.isAprilFools() && WitherStormModConfig.CLIENT.aprilFools.get() ? ParticleTypes.HEART : super.getTrailParticle());
   }

   protected ParticleOptions getParticle() {
      return ParticleTypes.FLAME;
   }

   public void tick() {
      super.tick();
      Vec3 vec3 = this.getDeltaMovement();
      double d0 = this.getX() + vec3.x + (double)this.random.nextFloat();
      double d1 = this.getY() + vec3.y + (double)this.random.nextFloat();
      double d2 = this.getZ() + vec3.z + (double)this.random.nextFloat();
      this.level().addParticle(this.getParticle(), d0 - 0.5, d1, d2 - 0.5, 0.0, 0.0, 0.0);
   }

   protected void onHitEntity(@NotNull EntityHitResult ray) {
      super.onHitEntity(ray);
      if (!this.level().isClientSide) {
         Entity victim = ray.getEntity();
         if (victim instanceof LivingEntity living) {
            ItemStack use = living.getUseItem();
            if (use.is(Items.SHIELD)) {
               this.explodeAndDiscard();
               return;
            }
         }

         boolean flag;
         if (this.getOwner() instanceof LivingEntity livingOwner) {
            DamageSource source = damageSource(this, livingOwner);
            flag = victim.hurt(source, 10.0F);
            if (flag) {
               if (victim.isAlive()) {
                  if (this.level() instanceof ServerLevel serverLevel) {
                     EnchantmentHelper.doPostAttackEffects(serverLevel, victim, source);
                  }
               } else {
                  livingOwner.heal(10.0F);
               }
            }
         } else {
            flag = victim.hurt(this.damageSources().magic(), 8.0F);
         }

         if (flag && victim instanceof LivingEntity) {
            int i = 0;
            if (this.level().getDifficulty() == Difficulty.NORMAL) {
               i = 10;
            } else if (this.level().getDifficulty() == Difficulty.HARD) {
               i = 40;
            }

            if (i > 0) {
               ((LivingEntity)victim).addEffect(new MobEffectInstance(MobEffects.WITHER, 180, 1));
            }
         }
      }
   }

   protected void onHit(@NotNull HitResult ray) {
      super.onHit(ray);
      if (!this.level().isClientSide && ray.getType() != Type.ENTITY) {
         this.explodeAndDiscard();
      }
   }

   protected void explodeAndDiscard() {
      boolean flag = ForgeEventFactory.getMobGriefingEvent(this.level(), this.getOwner());
      this.playSound(
         WitherStormModSoundEvents.FLAMING_SKULL_IMPACT.get(), 6.0F, (this.random.nextFloat() - this.random.nextFloat()) * -0.2F + 1.0F
      );
      WitherStormModPacketHandlers.MAIN
         .send(
            PacketDistributor.NEAR
               .with(new TargetPoint(this.position().x, this.position().y, this.position().z, 45.0, this.level().dimension())),
            new ShakeScreenMessage(20.0F, 4.0F)
         );
      this.level()
         .explode(
            this,
            this.getX(),
            this.getY(),
            this.getZ(),
            (float)((Double)WitherStormModConfig.SERVER.flamingSkullExplosionSize.get() + (double)this.random.nextInt(2)),
            flag,
            ExplosionInteraction.MOB
         );
      this.discard();
   }

   public boolean hurt(@NotNull DamageSource source, float amount) {
      if (source.getEntity() instanceof LivingEntity entity) {
         ItemStack item = entity.getMainHandItem();
         if (!item.isEmpty() && item.getItem() instanceof SwordItem) {
            boolean flag = super.hurt(source, amount);
            if (flag && !this.level().isClientSide()) {
               item.hurtAndBreak(120 + this.random.nextInt(140), entity, EquipmentSlot.MAINHAND);
               WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(this), new UpdateDamagingProjectileMessage(this));
            }

            return flag;
         }
      }

      return false;
   }

   protected boolean shouldBurn() {
      return false;
   }

   public static DamageSource damageSource(FlamingWitherSkullEntity cause, Entity entity) {
      return WitherStormModDamageTypes.source(cause.level().registryAccess(), WitherStormModDamageTypes.FLAMING_WITHER_SKULL, cause, entity);
   }

   @NotNull
   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
      return new ClientboundAddEntityPacket(this, serverEntity);
   }

   public void writeSpawnData(FriendlyByteBuf buffer) {
      buffer.writeDouble(this.getDeltaMovement().x);
      buffer.writeDouble(this.getDeltaMovement().y);
      buffer.writeDouble(this.getDeltaMovement().z);
   }

   public void readSpawnData(FriendlyByteBuf additionalData) {
      this.setDeltaMovement(additionalData.readDouble(), additionalData.readDouble(), additionalData.readDouble());
   }
}
