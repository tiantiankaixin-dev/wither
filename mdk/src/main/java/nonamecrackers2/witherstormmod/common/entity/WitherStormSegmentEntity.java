package nonamecrackers2.witherstormmod.common.entity;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.BowelsInstanceManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.PlayDeadManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.SegmentsManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.SymbiontSummoningManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.controller.WitherStormBodyController;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.HeadManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.WitherStormHead;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.ultimatetarget.UltimateTargetManager;
import nonamecrackers2.witherstormmod.common.init.WitherStormModAttributes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.util.ClusterBuilderHelper;
import nonamecrackers2.witherstormmod.common.util.StormHeadOffsets;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;
import org.jetbrains.annotations.NotNull;

public class WitherStormSegmentEntity extends WitherStormEntity {
   private static final EntityDataAccessor<Optional<UUID>> PARENT_UUID = SynchedEntityData.defineId(
      WitherStormSegmentEntity.class, EntityDataSerializers.OPTIONAL_UUID
   );
   private static final EntityDimensions STARTING_SIZE = EntityDimensions.scalable(15.0F, 17.5F);
   private static final EntityDimensions EVOLVED_SIZE = EntityDimensions.scalable(15.0F, 17.5F);
   @Nullable
   private WitherStormEntity parent;
   private final int tillFreeFall;
   private int dropTime;
   private int nextDropTime = 120 + this.random.nextInt(160);
   private int timeWithParent;
   @Nullable
   private Vec3 wantedSegmentPos;
   @Nullable
   private Vec3 randomStrollPos;
   private int tillNextRandomStroll;
   private float randomBodyRotAngleOffset;

   public WitherStormSegmentEntity(EntityType<? extends WitherStormSegmentEntity> entityTypeIn, Level worldIn) {
      super(entityTypeIn, worldIn);
      this.xpReward = 0;
      this.partsEnabled = false;
      this.shouldFollowUltimateTarget = false;
      this.chunkloads = false;
      this.shouldPlaySoundLoop = false;
      this.shouldPlayGlobalSounds = false;
      this.shouldIgnoreFormidibomb = true;
      this.tillFreeFall = Math.max(220, this.random.nextInt(260));
   }

   @NotNull
   @Override
   protected BodyRotationControl createBodyControl() {
      return new WitherStormSegmentEntity.BodyController();
   }

   @Override
   protected HeadManager makeHeadManager() {
      return new HeadManager(this, StormHeadOffsets.SEGMENT);
   }

   @Override
   protected Optional<SegmentsManager> makeSegmentsManager() {
      return Optional.empty();
   }

   @Override
   protected Optional<SymbiontSummoningManager> makeSummoningManager() {
      return Optional.empty();
   }

   @Override
   protected Optional<BowelsInstanceManager> makeBowelsInstanceManager() {
      return Optional.empty();
   }

   @Override
   protected Optional<UltimateTargetManager> makeUltimateTargetManager() {
      return Optional.empty();
   }

   @Override
   protected Optional<ServerBossEvent> makeBossEvent() {
      return Optional.empty();
   }

   public WitherStormSegmentEntity(WitherStormEntity parent) {
      this(WitherStormModEntityTypes.WITHER_STORM_SEGMENT.get(), parent.level());
      this.setParent(parent);
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add((Attribute)WitherStormModAttributes.TARGET_STATIONARY_FLYING_SPEED.get(), 0.4)
         .add((Attribute)WitherStormModAttributes.SLOW_FLYING_SPEED.get(), 0.05)
         .add((Attribute)WitherStormModAttributes.EVOLUTION_SPEED.get(), 1.0)
         .add(Attributes.FLYING_SPEED, 0.0)
         .add(Attributes.MAX_HEALTH, 4000.0)
         .add(Attributes.MOVEMENT_SPEED, 0.6)
         .add(Attributes.FOLLOW_RANGE, 160.0)
         .add((Attribute)WitherStormModAttributes.HUNCHBACK_FOLLOW_RANGE.get(), 40.0)
         .add(Attributes.ARMOR, 6.0);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(PARENT_UUID, Optional.empty());
   }

   @Override
   public void addAdditionalSaveData(@NotNull CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      if (this.getParentUUID() != null) {
         compound.putUUID("Parent", this.getParentUUID());
      }

      compound.putInt("TimeWithParent", this.timeWithParent);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("Parent")) {
         this.setParentUUID(compound.getUUID("Parent"));
      }

      this.timeWithParent = compound.getInt("TimeWithParent");
   }

   @Override
   public void tick() {
      super.tick();
      WitherStormEntity parent = this.getParent();
      if (parent == null && this.getParentUUID() != null) {
         List<? extends Entity> entities = this.level().getEntitiesOfClass(WitherStormEntity.class, this.getBoundingBox().inflate(1000.0));
         if (!this.level().isClientSide) {
            entities = Lists.newArrayList(((ServerLevel)this.level()).getAllEntities());
         }

         for (Entity entity : entities) {
            if (this.getParentUUID().equals(entity.getUUID())) {
               WitherStormEntity storm = (WitherStormEntity)entity;
               this.setParent(storm);
            }
         }

         if (this.isOnDistantRenderer()) {
            this.level().getCapability(WitherStormModClientCapabilities.DISTANT_RENDERER).ifPresent(renderer -> {
               for (WitherStormEntity stormx : renderer.getKnown()) {
                  if (this.getParentUUID().equals(stormx.getUUID())) {
                     this.setParent(stormx);
                  }
               }
            });
         }
      }

      if (parent != null) {
         this.timeWithParent++;
      }
   }

   @Override
   protected void customServerAiStep() {
      super.customServerAiStep();
      WitherStormEntity parent = this.getParent();
      if (parent != null) {
         this.calculateDesiredPos();
         Vec3 desiredPos = this.getDesiredPos();
         Vec3 parentPos = new Vec3(parent.getX() - this.getX(), parent.getY() - this.getY(), parent.getZ() - this.getZ());
         if (parentPos.horizontalDistance() > 200.0) {
            assert desiredPos != null;

            this.moveTo(desiredPos);
         }

         if (parent.walkAnimation.speed() < 0.3F && this.position().distanceTo(Objects.requireNonNull(desiredPos)) < 50.0) {
            if (this.tillNextRandomStroll == 0) {
               double x = this.random.nextDouble() * 20.0 - 10.0 + desiredPos.x;
               double y = this.random.nextDouble() * 40.0 - 20.0 + desiredPos.y;
               double z = this.random.nextDouble() * 20.0 - 10.0 + desiredPos.z;
               this.randomStrollPos = new Vec3(x, y, z);
               this.tillNextRandomStroll = 200 + this.random.nextInt(100);
               this.randomBodyRotAngleOffset = (this.random.nextFloat() * 20.0F + 20.0F) * (this.isMirrored() ? -1.0F : 1.0F);
            }

            if (this.tillNextRandomStroll > 0) {
               this.tillNextRandomStroll--;
            }

            if (this.randomStrollPos != null && this.position().distanceTo(this.randomStrollPos) < 5.0) {
               this.tillNextRandomStroll = 0;
            }
         } else {
            this.tillNextRandomStroll = 0;
            this.randomStrollPos = null;
            this.randomBodyRotAngleOffset = 0.0F;
         }
      }

      CommandBlockEntity commandBlock = this.getBowelsCommandBlock();
      if (commandBlock != null && commandBlock.getHealth() < commandBlock.getMaxHealth()) {
         this.nextDropTime--;
         if (this.nextDropTime == 0) {
            this.dropTime = 10 + this.random.nextInt(5);
            this.nextDropTime = (int)((double)(360 + this.random.nextInt(160)) * Math.max(0.2, (double)(commandBlock.getHealth() / commandBlock.getMaxHealth())));
         }
      }

      if (this.dropTime > 0) {
         this.dropTime--;
      }
   }

   @Override
   public double getHeightToAscendTo(Vec3 vector3d, double height, double ascendSpeed) {
      return this.getParent() != null ? this.getParent().getDesiredSegmentY(this.getSegmentIndex()) : super.getHeightToAscendTo(vector3d, height, ascendSpeed);
   }

   @Override
   public double getFlyingSpeed(Vec3 ultimateTarget) {
      WitherStormEntity parent = this.getParent();
      if (parent != null) {
         if (this.randomStrollPos == null) {
            double speed = parent.getAttributeValue(Attributes.FLYING_SPEED);
            if (parent.shouldSpeedUp()) {
               speed += parent.getDefaultChasingSpeed() + 0.05;
            } else {
               speed += parent.getDefaultNormalSpeed() + 0.08;
            }

            return speed;
         } else {
            return 0.01;
         }
      } else {
         return super.getFlyingSpeed(ultimateTarget);
      }
   }

   @Override
   protected Vec3 doFlying(Vec3 vector3d) {
      if (this.getParent() != null && !this.isDeadOrPlayingDead()) {
         double ascendSpeed = this.randomStrollPos == null ? 0.02 : 0.01;
         Vec3 wanted;
         if (this.randomStrollPos != null) {
            wanted = this.randomStrollPos;
         } else {
            wanted = this.getDesiredPos();
         }

         assert wanted != null;

         double height = wanted.y;
         double d0 = vector3d.y;
         if (this.getY() < height || !this.isPowered() && this.getY() < height + 5.0) {
            d0 = (height - this.getY()) * ascendSpeed;
         }

         vector3d = new Vec3(vector3d.x, d0, vector3d.z);
         Vec3 vector3d1 = new Vec3(wanted.x - this.getX(), 0.0, wanted.z - this.getZ());
         double speed = this.getFlyingSpeed(vector3d1);
         speed = Math.min(speed, Math.sqrt(this.distanceToSqr(wanted)) * 0.01);
         if (vector3d1.horizontalDistance() > 1.0) {
            Vec3 vector3d2 = vector3d1.normalize();
            vector3d = vector3d.add(vector3d2.x * speed - vector3d.x * 0.6, 0.0, vector3d2.z * speed - vector3d.z * 0.6);
         }

         return vector3d;
      } else {
         return super.doFlying(vector3d);
      }
   }

   @Override
   protected void tickDeath() {
      super.tickDeath();
      if (this.level().isClientSide && !this.onGround()) {
         float xOffset = (this.random.nextFloat() - 0.5F) * ((float)this.getBoundingBox().getXsize() + 5.0F);
         float yOffset = (this.random.nextFloat() - 0.5F) * ((float)this.getBoundingBox().getYsize() + 5.0F);
         float zOffset = (this.random.nextFloat() - 0.5F) * ((float)this.getBoundingBox().getZsize() + 5.0F);
         this.level()
            .addParticle(
               ParticleTypes.EXPLOSION, this.getX() + (double)xOffset, this.getEyeY() + (double)yOffset, this.getZ() + (double)zOffset, -5.0, 0.0, 0.0
            );
      }

      if (this.onGround()) {
         for (WitherStormHead head : this.headManager.getHeads()) {
            head.lerpHeadXTo(90.0F, 10.0F);
         }
      }
   }

   @NotNull
   @Override
   public EntityDimensions getDimensions(@NotNull Pose pose) {
      EntityDimensions size = this.getUnmodifiedDimensions(pose);
      if ((Boolean)WitherStormModConfig.SERVER.squashHitbox.get() && this.getPhase() > 3) {
         size = EntityDimensions.scalable(size.width, 1.0F);
      }

      return size;
   }

   @Override
   public EntityDimensions getUnmodifiedDimensions(Pose pose) {
      EntityDimensions size = STARTING_SIZE;
      if (this.getPhase() == 7) {
         size = EVOLVED_SIZE;
      }

      return size;
   }

   @Override
   protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size) {
      return 10.0F;
   }

   @Override
   public boolean setPhase(int phase, int consumedEntities) {
      boolean result = false;
      if (this.getParent() != null) {
         result = false;
         this.entityData.set(PHASE, this.getParent().getPhase());
      } else if (phase <= 7) {
         this.entityData.set(PHASE, Math.max(phase, 6));
         result = phase >= 6;
      } else {
         result = false;
      }

      this.clusterRadius = (float)((int)Math.max(1.0, (double)((float)this.getPhase() * 0.75F)));
      this.entityConsumptionRadius = this.getPhase() > 3 ? 64 : 16;
      this.setConsumedEntities(consumedEntities);
      this.reapplyPosition();
      this.refreshDimensions();
      return result;
   }

   public void setParent(WitherStormEntity parent) {
      this.parent = parent;
      if (parent != null) {
         this.entityData.set(PARENT_UUID, Optional.of(parent.getUUID()));

         assert this.getParent() != null;

         this.getPlayDeadManager().setStateRaw(this.getParent().getPlayDeadManager().getState());
         this.getPlayDeadManager().setTickAmount(this.getParent().getPlayDeadManager().getTicks());
      } else {
         this.entityData.set(PARENT_UUID, Optional.empty());
      }
   }

   protected void setParentUUID(UUID uuid) {
      this.entityData.set(PARENT_UUID, Optional.of(uuid));
   }

   @Nullable
   public UUID getParentUUID() {
      return (UUID)((Optional)this.entityData.get(PARENT_UUID)).orElse(null);
   }

   @Nullable
   public WitherStormEntity getParent() {
      return this.parent;
   }

   @Override
   public LivingEntity getUltimateTarget() {
      return this.getParent() != null ? this.getParent().getUltimateTarget() : null;
   }

   @Override
   public int getConsumedEntities() {
      return this.getParent() != null ? this.getParent().getConsumedEntities() : 0;
   }

   @Override
   public void setConsumedEntities(int newAmount) {
   }

   @Override
   public void addToConsumedEntities(int amount) {
   }

   @Override
   public int getInvulnerableTicks() {
      return this.getParent() != null ? this.getParent().getInvulnerableTicks() : 0;
   }

   @Override
   public void setInvulnerableTicks(int ticks) {
   }

   @Override
   public boolean targetInUseBySegment(Entity entity) {
      WitherStormEntity parent = this.getParent();
      if (parent != null) {
         if (parent.alreadyATarget(entity, true)) {
            return true;
         }

         Optional<SegmentsManager> manager = this.getParent().getSegmentsManager();
         if (manager.isPresent()) {
            WitherStormSegmentEntity[] segments = manager.get().getSegments();

            for (WitherStormSegmentEntity segment : segments) {
               if (segment != null && !this.equals(segment) && segment.alreadyATarget(entity, true)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @Override
   public void createDebrisRings(boolean hidden) {
   }

   @Override
   public void createDebrisClusters(boolean hidden) {
   }

   public int getTimeTillFreeFall() {
      return this.tillFreeFall;
   }

   @Override
   public void onBigFall() {
      super.onBigFall();

      for (int i = 0; i < 6; i++) {
         this.level().explode(this, this.getX(), this.getEyeY() - (double)i, this.getZ(), 16.0F, false, ExplosionInteraction.MOB);
      }
   }

   public void regatherCapabilities() {
      this.gatherCapabilities();
   }

   @Override
   public boolean shouldDoCustomMovement() {
      PlayDeadManager.State state = this.getPlayDeadManager().getState();
      return this.dropTime <= 0
         && this.getDeathTime() < this.getTimeTillFreeFall()
         && (state != PlayDeadManager.State.FALLING || this.getPlayDeadManager().getTicks() <= 200)
         && this.shouldDoCustomMovement;
   }

   @Override
   public boolean canFallOnBack() {
      return false;
   }

   @Override
   public Vec3 getUltimateTargetPos() {
      return this.getParent() != null ? this.getParent().getUltimateTargetPos() : super.getUltimateTargetPos();
   }

   @Override
   public CommandBlockEntity getBowelsCommandBlock() {
      return this.getParent() != null ? this.getParent().getBowelsCommandBlock() : super.getBowelsCommandBlock();
   }

   @Override
   public void dropDropsAt(Entity player) {
   }

   @Override
   protected boolean canTrackEntity(Entity entity) {
      return !super.canTrackEntity(entity) ? false : this.getParent() == null || !this.getParent().getTrackedEntities().contains(entity);
   }

   @Override
   public boolean isBeingTornApart() {
      return this.getParent() != null ? this.getParent().isBeingTornApart() : super.isBeingTornApart();
   }

   public int getTimeWithParent() {
      return this.timeWithParent;
   }

   @Override
   protected boolean isInsideOtherTractorBeam(LivingEntity entity, int head) {
      if (this.getParent() != null) {
         List<WitherStormEntity> storms = Lists.newArrayList(new WitherStormEntity[]{this.getParent()});
         this.getParent().getSegmentsManager().ifPresent(manager -> {
            for (WitherStormSegmentEntity segment : manager.getSegments()) {
               if (segment != null && segment.isAlive()) {
                  storms.add(segment);
               }
            }
         });

         for (WitherStormEntity storm : storms) {
            Pair<Boolean, Integer> flag = TractorBeamHelper.isInsideTractorBeam(entity, storm, 5.0);
            if ((Boolean)flag.getFirst() && (Integer)flag.getSecond() != head) {
               return true;
            }
         }

         return false;
      } else {
         return super.isInsideOtherTractorBeam(entity, head);
      }
   }

   @Override
   public void dropMassCluster(int radius) {
   }

   @Override
   public boolean shouldShine() {
      return false;
   }

   private void calculateDesiredPos() {
      WitherStormEntity parent = this.getParent();
      int index = this.getSegmentIndex();

      assert parent != null;

      this.wantedSegmentPos = new Vec3(parent.getDesiredSegmentX(index), parent.getDesiredSegmentY(index), parent.getDesiredSegmentZ(index));
   }

   @Nullable
   private Vec3 getDesiredPos() {
      if (this.wantedSegmentPos == null && this.getParent() != null) {
         this.calculateDesiredPos();
      }

      return this.wantedSegmentPos;
   }

   private int getSegmentIndex() {
      return this.isMirrored() ? 1 : 2;
   }

   @Override
   public boolean hurt(DamageSource source, float floatIn) {
      if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
         return super.hurt(source, floatIn);
      } else {
         return this.isCompletelyInvulnerable() ? false : super.hurt(source, 0.0F);
      }
   }

   @Override
   protected void splitCluster(BlockClusterEntity cluster, List<Entity> toAdd) {
      BlockClusterEntity split = cluster.splitAt(Axis.getRandom(this.random));
      if (split != null) {
         this.level().addFreshEntity(split);
         if (this.random.nextBoolean() && this.getParent() != null) {
            this.getParent().getTrackedEntities().trackEntityToConsume(split);
         } else {
            toAdd.add(split);
         }
      }
   }

   @Override
   public int loadRadius() {
      return 6;
   }

   @Override
   public void dropSmallMassCluster(int radius) {
      BlockClusterEntity cluster = ClusterBuilderHelper.buildSmallRandomDeathCluster(this.level(), this.random, radius);
      cluster.setSink(-1);
      cluster.setPos(
         this.position()
            .add(
               this.random.nextGaussian() * 5.0, (double)this.getUnmodifiedHeight() / 2.0 + this.random.nextGaussian() * 5.0, this.random.nextGaussian() * 5.0
            )
      );
      cluster.setDeltaMovement(this.random.nextGaussian() * 0.4, this.random.nextGaussian() * 0.3, this.random.nextGaussian() * 0.4);
      cluster.setRotationDelta(new Vec2((float)this.random.nextInt(90) * 0.3F / 2.0F, (float)this.random.nextInt(90) * 0.3F / 2.0F));
      this.level().addFreshEntity(cluster);
   }

   @Override
   protected void dropDeathClusters() {
      if (this.getDeathTime() > 10 && this.getDeathTime() % 10 == 0) {
         this.dropSmallMassCluster(1);
      }
   }

   @Override
   protected void searchForPlayingJukeboxes() {
      WitherStormEntity parent = this.getParent();
      this.getPlayingJukeboxes().clear();
      if (parent != null) {
         for (BlockPos pos : parent.getPlayingJukeboxes()) {
            this.getPlayingJukeboxes().add(pos);
         }
      }
   }

   @Override
   public void storePet(Entity entity) {
      WitherStormEntity parent = this.getParent();
      if (parent != null) {
         parent.storePet(entity);
      } else {
         super.storePet(entity);
      }
   }

   private class BodyController extends WitherStormBodyController {
      public BodyController() {
         super(WitherStormSegmentEntity.this);
      }

      @Override
      protected float getYRotD() {
         return Mth.wrapDegrees(super.getYRotD() + WitherStormSegmentEntity.this.randomBodyRotAngleOffset);
      }
   }
}
