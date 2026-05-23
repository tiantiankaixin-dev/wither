package nonamecrackers2.witherstormmod.common.entity;

import com.mojang.datafixers.util.Pair;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
// TODO_MIG[MOBTYPE]: MobType removed in 1.21; getMobType() is gone 鈥?use entity tags
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.api.common.entity.WitherStormBase;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.goal.LookAtDistractionGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.LookAtTargetGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.NearestDistractionGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.YAffectedLookRandomlyGoal;
import nonamecrackers2.witherstormmod.common.init.WitherStormModDamageTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.PlayerMotionMessage;
import nonamecrackers2.witherstormmod.common.particle.TractorBeamParticleOptions;
import nonamecrackers2.witherstormmod.common.util.ConditionalLookController;
import nonamecrackers2.witherstormmod.common.util.EmptyBodyController;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;
import nonamecrackers2.witherstormmod.common.util.WitherStormModNBTUtil;
import org.jetbrains.annotations.NotNull;

public class WitherStormHeadEntity extends Monster implements WitherStormBase, RangedAttackMob {
   private static final EntityDataAccessor<Boolean> IS_ACTIVE = SynchedEntityData.defineId(WitherStormHeadEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> IS_ROARING = SynchedEntityData.defineId(WitherStormHeadEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> IS_BITING = SynchedEntityData.defineId(WitherStormHeadEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> IS_HURT = SynchedEntityData.defineId(WitherStormHeadEntity.class, EntityDataSerializers.BOOLEAN);
   @Nullable
   private Vec3 distractedPos;
   private int distractedTime;
   private int nextRoar = 400 + this.random.nextInt(600);
   private int roarTime;
   private int shootTime = 100;
   private int biteTime;
   private float mouthAnim;
   private float mouthAnimO;
   private float fadeAnimation;
   private float fadeAnimationO;
   private boolean isShaking;
   private float shakeAnim;
   private float shakeAnimO;
   private LookAtTargetGoal<WitherStormHeadEntity> lookGoal;
   private int specialDeathTime;

   public WitherStormHeadEntity(EntityType<? extends WitherStormHeadEntity> type, Level world) {
      super(type, world);
      this.getNavigation().setCanFloat(true);
      this.noCulling = true;
      this.setNoGravity(true);
      this.lookControl = new ConditionalLookController(this, entity -> false);
   }

   @NotNull
   protected BodyRotationControl createBodyControl() {
      return new EmptyBodyController(this);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(IS_ACTIVE, true);
      this.entityData.define(IS_ROARING, false);
      this.entityData.define(IS_BITING, false);
      this.entityData.define(IS_HURT, false);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new WitherStormHeadEntity.DoNothingGoal(this));
      this.goalSelector.addGoal(1, new LookAtDistractionGoal(this, 0));
      this.lookGoal = new LookAtTargetGoal(this, 0, s -> 3);
      this.goalSelector.addGoal(2, this.lookGoal);
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 12.0F));
      this.goalSelector.addGoal(4, new YAffectedLookRandomlyGoal(this));
      this.targetSelector.addGoal(0, new HurtByTargetGoal(this, new Class[0]));
      this.targetSelector.addGoal(1, new NearestDistractionGoal(this, 0, WitherStormEntity.DISTRACTION_SELECTOR, 8));
      this.targetSelector
         .addGoal(
            2,
            new WitherStormHeadEntity.AttackGoal<LivingEntity>(
               this, LivingEntity.class, 100, true, false, WitherStormEntity.DESTROYER_LIVING_ENTITY_SELECTOR.and(entity -> !this.isATarget(entity))
            )
         );
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 60.0)
         .add(Attributes.FOLLOW_RANGE, 40.0)
         .add(Attributes.MOVEMENT_SPEED, 0.0)
         .add(Attributes.ARMOR, 8.0);
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      compound.putBoolean("IsRoaring", this.isRoaring());
      compound.putInt("RoarTime", this.roarTime);
      super.addAdditionalSaveData(compound);
      compound.putBoolean("IsActive", (Boolean)this.entityData.get(IS_ACTIVE));
      if (this.getDistractedPos(0) != null) {
         compound.put("DistractedPos", WitherStormModNBTUtil.writeVector3d(Objects.requireNonNull(this.getDistractedPos(0))));
      }

      compound.putInt("DistractedTime", this.distractedTime);
      compound.putFloat("YBodyRot", this.yBodyRot);
      compound.putBoolean("IsHurt", this.isHurt());
   }

   public void readAdditionalSaveData(CompoundTag compound) {
      this.entityData.set(IS_ROARING, compound.getBoolean("IsRoaring"));
      this.roarTime = compound.getInt("RoarTime");
      super.readAdditionalSaveData(compound);
      if (compound.contains("IsActive")) {
         this.entityData.set(IS_ACTIVE, compound.getBoolean("IsActive"));
      }

      if (compound.contains("DistractedPos")) {
         this.setDistractedPos(0, WitherStormModNBTUtil.readVector3d(compound.getCompound("DistractedPos")));
      }

      this.distractedTime = compound.getInt("DistractedTime");
      if (compound.contains("YBodyRot")) {
         this.yBodyRot = compound.getFloat("YBodyRot");
         this.yBodyRotO = this.yBodyRot;
      }

      if (compound.contains("IsHurt")) {
         this.setHurt(compound.getBoolean("IsHurt"));
      }
   }

   public void aiStep() {
      super.aiStep();
      if (this.distractedTime > 0) {
         this.distractedTime--;
         if (this.distractedTime == 0) {
            this.setDistractedPos(0, null);
         }
      }

      if (!this.isDeadOrPlayingDead()) {
         if (!this.level().isClientSide) {
            if (this.nextRoar > 0) {
               this.nextRoar--;
               if (this.nextRoar == 0) {
                  this.setRoar(false);
                  this.nextRoar = 400 + this.random.nextInt(600);
                  this.roarTime = 40;
               }
            }

            if (this.roarTime > 0) {
               this.roarTime--;
               if (this.roarTime == 0) {
                  this.disableRoar();
               }
            }

            if (this.biteTime > 0) {
               this.biteTime--;
               if (this.biteTime == 0) {
                  this.setBite(false);
                  this.playSound(WitherStormModSoundEvents.WITHER_STORM_BITE.get(), this.getSoundVolume(), 1.0F);
               }
            }
         }

         if (this.isHurt() && this.tickCount % 20 == 0 && this.shootTime > 60) {
            this.isShaking = true;
         }

         if (this.isHurt() && this.shootTime > 0) {
            this.shootTime--;
            if (this.shootTime < 60) {
               LivingEntity entity = this.getTarget();
               if (entity != null) {
                  this.setLookAt(0, entity.position());
                  this.isShaking = false;
               }
            }

            if (this.shootTime == 0) {
               this.shootSkullAtTarget();
               this.shootTime = 60 + this.random.nextInt(40);
               this.isShaking = false;
            }
         }
      }
   }

   public void tick() {
      super.tick();
      this.mouthAnimO = this.mouthAnim;
      if (!this.isBiting() && this.isRoaring()) {
         this.mouthAnim = this.mouthAnim + (1.0F - this.mouthAnim) * 0.15F + 0.04F;
         if (this.mouthAnim > 2.0F) {
            this.mouthAnim = 2.0F;
         }
      } else if (this.isBiting()) {
         this.mouthAnim = this.mouthAnim + (1.0F - this.mouthAnim) * 0.16F + 0.1F;
         if (this.mouthAnim > 1.4F) {
            this.mouthAnim = 1.4F;
         }
      } else {
         this.mouthAnim = this.mouthAnim + (-this.mouthAnim * 0.16F - 0.02F);
         if (this.mouthAnim < 0.0F) {
            this.mouthAnim = 0.0F;
         }
      }

      this.fadeAnimationO = this.fadeAnimation;
      if (this.isPlayingDead()) {
         this.fadeAnimation = this.fadeAnimation + 1.0F + this.random.nextFloat() * 2.0F;
         if (this.fadeAnimation > 300.0F) {
            this.fadeAnimation = 300.0F;
         }
      } else {
         this.fadeAnimation = this.fadeAnimation - (1.0F + this.random.nextFloat() * 2.0F);
         if (this.fadeAnimation < 0.0F) {
            this.fadeAnimation = 0.0F;
         }
      }

      this.shakeAnimO = this.shakeAnim;
      if (this.isShaking) {
         this.shakeAnim = this.shakeAnim + 0.02F + this.random.nextFloat() * 0.05F;
         if (this.shakeAnimO >= 2.0F) {
            this.shakeAnimO = 0.0F;
            this.shakeAnim = 0.0F;
            this.isShaking = false;
         }
      }

      if (this.level().isClientSide && (Boolean)WitherStormModConfig.CLIENT.tractorBeamParticles.get() && this.tractorBeamActive(0)) {
         for (int amount = 0; amount < 5; amount++) {
            float x = this.getXRot();
            float y = this.yHeadRot;
            Vec3 lookVec = this.getViewVector(x, y, this.random.nextFloat() * 200.0F);
            Vec3 headPos = this.getHeadPos(0);
            Vec3 pos = headPos.add(lookVec).add(0.0, 1.5, 0.0);
            double distanceFromHead = Math.sqrt(pos.distanceToSqr(headPos));
            double distanceAllowed = distanceFromHead * 2.0 * 0.02;
            double randX = this.random.nextGaussian() * distanceAllowed;
            double randY = this.random.nextGaussian() * distanceAllowed;
            double randZ = this.random.nextGaussian() * distanceAllowed;
            pos = pos.add(randX, randY, randZ);
            Pair<Boolean, Integer> result = TractorBeamHelper.isInsideTractorBeam(pos, this, 4.0);
            if ((Boolean)result.getFirst()) {
               Vec3 delta = pos.subtract(headPos).normalize().scale(-0.8);
               if (WitherStormMod.isAprilFools() && (Boolean)WitherStormModConfig.CLIENT.aprilFools.get()) {
                  this.level()
                     .addParticle(ParticleTypes.HEART, true, pos.x, pos.y, pos.z, delta.x, delta.y, delta.z);
               } else {
                  this.level()
                     .addParticle(
                        new TractorBeamParticleOptions(this.getId(), (Integer)result.getSecond()),
                        true,
                        pos.x,
                        pos.y,
                        pos.z,
                        delta.x,
                        delta.y,
                        delta.z
                     );
               }
            }
         }
      }
   }

   public void die(@NotNull DamageSource source) {
      super.die(source);
      this.setRoar(true);
   }

   protected void tickDeath() {
      this.specialDeathTime++;
      if (!this.level().isClientSide) {
         this.setXRot(this.getXRot() - 1.0F);
         if (this.getXRot() < -50.0F) {
            this.setXRot(-50.0F);
         }
      }

      if (this.specialDeathTime > 120) {
         this.remove(RemovalReason.KILLED);
      }
   }

   public void push(double deltaX, double deltaY, double deltaZ) {
   }

   protected SoundEvent getAmbientSound() {
      return !this.isPlayingDead() ? WitherStormModSoundEvents.WITHER_STORM_GROWL.get() : null;
   }

   protected SoundEvent getHurtSound(@NotNull DamageSource source) {
      return WitherStormModSoundEvents.WITHER_STORM_HURT.get();
   }

   public int getAmbientSoundInterval() {
      return 80 + this.random.nextInt(40);
   }

   protected float getSoundVolume() {
      return 8.0F;
   }

   protected void customServerAiStep() {
      if (!this.isPlayingDead() && !this.isHurt()) {
         if (this.getTarget() != null) {
            Entity target;
            Entity vehicle;
            Vec3 delta;
            boolean var10000;
            label49: {
               double speed = 0.2;
               target = this.getTarget();
               vehicle = target.getVehicle();
               delta = this.position().subtract(target.position()).normalize().scale(speed);
               if (vehicle instanceof LivingEntity living && !WitherStormEntity.DESTROYER_LIVING_ENTITY_SELECTOR.test(living)) {
                  var10000 = false;
                  break label49;
               }

               var10000 = true;
            }

            boolean flag = var10000;
            if (target.isPassenger() && (Boolean)WitherStormModConfig.COMMON.shouldPickUpVehicles.get() && flag) {
               vehicle.setDeltaMovement(delta);
            } else {
               target.setDeltaMovement(delta);
            }

            if (target instanceof Player) {
               WitherStormModPacketHandlers.MAIN.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)target), new PlayerMotionMessage(delta));
            }

            if (this.getBoundingBox().intersects(target.getBoundingBox())) {
               if (target instanceof Player player) {
                  if (!player.isDeadOrDying() && player.isAlive() && !player.getAbilities().invulnerable) {
                     player.hurt(WitherStormModDamageTypes.witherStormAttack(this), 3.5F);
                  }

                  this.setBite(true);
               } else {
                  target.hurt(WitherStormModDamageTypes.witherStormAttackMob(this), Float.MAX_VALUE);
                  this.setBite(true);
               }
            }
         }

         if (this.tickCount % 80 == 0) {
            this.heal(10.0F);
         }
      }
   }

   @Override
   public void setDistractedPos(int head, @Nullable Vec3 pos) {
      this.distractedPos = pos;
   }

   @Nullable
   @Override
   public Vec3 getDistractedPos(int head) {
      return this.distractedPos;
   }

   @Override
   public void makeDistracted(Vec3 pos, int time, int head) {
      this.distractedTime = time;
      this.setDistractedPos(head, pos);
   }

   public void setRoar(boolean screaming) {
      this.entityData.set(IS_ROARING, true);
      SoundEvent event = WitherStormModSoundEvents.WITHER_STORM_ROAR.get();
      if (screaming) {
         event = WitherStormModSoundEvents.WITHER_STORM_HURT.get();
      }

      this.playSound(event, this.getSoundVolume(), 1.0F);
   }

   public void setRoarTime(int time) {
      this.roarTime = time;
   }

   public void disableRoar() {
      this.entityData.set(IS_ROARING, false);
   }

   public boolean isRoaring() {
      return (Boolean)this.entityData.get(IS_ROARING);
   }

   public void setBite(boolean biting) {
      if (biting) {
         this.biteTime = 10;
      }

      this.entityData.set(IS_BITING, biting);
   }

   public boolean isBiting() {
      return (Boolean)this.entityData.get(IS_BITING);
   }

   public boolean hurt(DamageSource source, float amount) {
      if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
         return super.hurt(source, amount);
      } else if (!this.isPlayingDead() && !this.isHurt()) {
         if (!this.isRoaring()) {
            this.setRoar(true);
            this.roarTime = 20;
         }

         boolean flag = super.hurt(source, amount);
         if (this.getHealth() < this.getMaxHealth() / 1.5F) {
            this.setHurt(true);
         }

         return flag;
      } else {
         return false;
      }
   }

   public boolean isInWall() {
      return false;
   }

   public void checkDespawn() {
      this.noActionTime = 0;
   }

   public boolean addEffect(@NotNull MobEffectInstance effect, @Nullable Entity entity) {
      return false;
   }

   @NotNull
   public MobType getMobType() {
      return WitherStormModMobTypes.SICKENED;
   }

   public boolean canChangeDimensions() {
      return false;
   }

   public boolean canBeAffected(@NotNull MobEffectInstance effect) {
      return false;
   }

   public boolean attackable() {
      return !this.isPlayingDead();
   }

   protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions size) {
      return size.height / 1.5F;
   }

   public void startSleeping(@NotNull BlockPos pos) {
   }

   public boolean canBeLeashed(@NotNull Player player) {
      return false;
   }

   public float getMouthAnimation(float partialTicks) {
      return Mth.lerp(partialTicks, this.mouthAnimO, this.mouthAnim);
   }

   public boolean isPushable() {
      return false;
   }

   public boolean isPushedByFluid() {
      return false;
   }

   @Override
   public boolean isDeadOrPlayingDead() {
      return this.isPlayingDead() || this.isDeadOrDying();
   }

   @Override
   public boolean isPlayingDead() {
      return !(Boolean)this.entityData.get(IS_ACTIVE);
   }

   public void setActive(boolean active) {
      this.entityData.set(IS_ACTIVE, active);
   }

   @Override
   public float getMouthAnimation(int head, float partialTicks) {
      return Mth.lerp(partialTicks, this.mouthAnimO, this.mouthAnim);
   }

   @Override
   public float getBrokenJawAnimation(int head, float partialTicks) {
      return 0.0F;
   }

   @Override
   public float getFadeAnimation(float partialTicks) {
      return Mth.lerp(partialTicks, this.fadeAnimationO, this.fadeAnimation);
   }

   @Override
   public float getFadeAnimation() {
      return this.fadeAnimation;
   }

   @Override
   public float getTentacleAnimation(float partialTicks) {
      return 0.0F;
   }

   @Override
   public float getHeadYRot(int head) {
      return this.yHeadRot;
   }

   @Override
   public float getHeadYRotO(int head) {
      return this.yHeadRotO;
   }

   @Override
   public float getHeadXRot(int head) {
      return this.getXRot();
   }

   @Override
   public float getHeadXRotO(int head) {
      return this.xRotO;
   }

   @Override
   public float getXBodyRot() {
      return 0.0F;
   }

   @Override
   public float getXBodyRotO() {
      return 0.0F;
   }

   @Override
   public boolean isPosBehindBack(Vec3 pos) {
      return false;
   }

   public void knockback(double strength, double x, double z) {
   }

   public boolean isNoGravity() {
      return true;
   }

   public boolean isATarget(LivingEntity entity) {
      for (WitherStormHeadEntity entity1 : this.level().getEntitiesOfClass(WitherStormHeadEntity.class, this.getBoundingBox().inflate(this.getAttributeValue(Attributes.FOLLOW_RANGE)))) {
         if (!entity1.isHurt() && entity1.getTarget() == entity) {
            return true;
         }
      }

      return false;
   }

   public boolean isHurt() {
      return this.isHeadInjured(0);
   }

   @Override
   public boolean isHeadInjured(int head) {
      return (Boolean)this.entityData.get(IS_HURT);
   }

   public void setHurt(boolean hurt) {
      this.entityData.set(IS_HURT, hurt);
      if (hurt) {
         this.goalSelector.removeGoal(this.lookGoal);
      } else {
         this.goalSelector.removeGoal(this.lookGoal);
         this.goalSelector.addGoal(2, this.lookGoal);
      }
   }

   @Override
   public float getHeadShakeAnim(int head, float partialTicks) {
      float lerp = Mth.clamp(Mth.lerp(partialTicks, this.shakeAnimO, this.shakeAnim), 0.0F, 1.0F);
      return Mth.sin(lerp * (float) Math.PI) * Mth.sin(lerp * (float) Math.PI * 12.0F) * 0.05F * (float) Math.PI;
   }

   @Override
   public boolean areOtherHeadsDisabled() {
      return false;
   }

   @Override
   public Vec3 getHeadPos(int head) {
      return this.getBoundingBox().getCenter();
   }

   @Override
   public int getTotalHeads() {
      return 1;
   }

   @Override
   public void setLookAt(int head, Vec3 pos, int steps) {
      if (pos != null) {
         this.lookControl.setLookAt(pos);
      }
   }

   @Override
   public LivingEntity getTarget(int head) {
      return this.getTarget();
   }

   @Override
   public void setTarget(int head, LivingEntity entity) {
      this.setTarget(entity);
   }

   @Override
   public boolean tractorBeamActive(int head) {
      return WitherStormBase.super.tractorBeamActive(head) && !this.isPlayingDead();
   }

   @Override
   public boolean canSee(int head, Entity entity) {
      return this.hasLineOfSight(entity);
   }

   public void performRangedAttack(@NotNull LivingEntity head, float p_33318_) {
   }

   private void shootSkullAtTarget() {
      LivingEntity entity = this.getTarget();
      if (entity != null) {
         Vec3 direction = entity.position().subtract(this.position()).normalize();
         WitherSkull witherSkull = new WitherSkull(this.level(), this, direction.x, direction.y, direction.z);
         this.level().addFreshEntity(witherSkull);
         if (this.getRandom().nextInt(16) == 1) {
            witherSkull.setDangerous(true);
         }

         witherSkull.playSound(SoundEvents.WITHER_SHOOT, 2.0F, 1.0F);
      }
   }

   private static class AttackGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
      public AttackGoal(WitherStormHeadEntity entity, Class<T> clazz, int randomInterval, boolean mustSee, boolean mustReach, Predicate<LivingEntity> predicate) {
         super(entity, clazz, randomInterval, mustSee, mustReach, predicate);
      }

      @NotNull
      protected AABB getTargetSearchArea(double followRange) {
         return this.mob.getBoundingBox().inflate(followRange);
      }
   }

   private static class DoNothingGoal extends Goal {
      private final WitherStormHeadEntity entity;

      public DoNothingGoal(WitherStormHeadEntity entity) {
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
         this.entity = entity;
      }

      public boolean canUse() {
         return this.entity.isPlayingDead();
      }
   }
}
