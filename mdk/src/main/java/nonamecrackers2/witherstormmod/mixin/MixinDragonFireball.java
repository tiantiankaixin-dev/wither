package nonamecrackers2.witherstormmod.mixin;

import java.util.List;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.util.DragonFireballAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({DragonFireball.class})
public class MixinDragonFireball extends AbstractHurtingProjectile implements DragonFireballAccessor {
   @Unique
   private static final EntityDataAccessor<Boolean> CREATED_FROM_SYMBIONT = SynchedEntityData.defineId(
      MixinDragonFireball.class, EntityDataSerializers.BOOLEAN
   );

   private MixinDragonFireball(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
      super(type, level);
      throw new UnsupportedOperationException();
   }

   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(CREATED_FROM_SYMBIONT, false);
   }

   @Inject(
      method = {"onHit"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/AreaEffectCloud;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)V",
         shift = Shift.AFTER
      )},
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   public void witherstormmod$addMobEffects_onHit(HitResult result, CallbackInfo ci, List<LivingEntity> list, AreaEffectCloud areaEffectCloud, Entity entity) {
      if (this.createdBySymbiont()) {
         ((MixinAreaEffectCloud)areaEffectCloud).witherstormmod$getEffects().clear();
         areaEffectCloud.setParticle(ParticleTypes.SMOKE);
         areaEffectCloud.setRadius(2.5F);
         areaEffectCloud.setDuration(120);
         areaEffectCloud.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 2));
         areaEffectCloud.setRadiusPerTick((10.0F - areaEffectCloud.getRadius()) / (float)areaEffectCloud.getDuration());
      }
   }

   @Inject(
      method = {"getTrailParticle"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void witherstormmod$useCustomTrailParticle_getTrailParticle(CallbackInfoReturnable<ParticleOptions> ci) {
      if (this.createdBySymbiont()) {
         ci.setReturnValue((ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get());
      }
   }

   @Override
   public boolean createdBySymbiont() {
      return (Boolean)(Object)this.entityData.get(CREATED_FROM_SYMBIONT);
   }

   @Override
   public void setCreatedBySymbiont(boolean flag) {
      this.entityData.set(CREATED_FROM_SYMBIONT, flag);
   }

   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      if (this.createdBySymbiont()) {
         tag.putBoolean("CreatedBySymbiont", this.createdBySymbiont());
      }
   }

   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      if (tag.contains("CreatedBySymbiont", 1)) {
         this.setCreatedBySymbiont(tag.getBoolean("CreatedBySymbiont"));
      }
   }
}
