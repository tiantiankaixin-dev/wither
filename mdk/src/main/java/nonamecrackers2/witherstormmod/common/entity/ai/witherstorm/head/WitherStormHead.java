package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head;

import net.neoforged.fml.config.ModConfig.Type;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.neoforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCriteriaTriggers;
import nonamecrackers2.crackerslib.common.packet.SimpleChannel;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.NotifyHeadInjuryMessage;
import nonamecrackers2.witherstormmod.common.packet.OnHeadAttackedMessage;
import nonamecrackers2.witherstormmod.common.util.HeadConfiguration;
import nonamecrackers2.witherstormmod.common.util.WitherStormModNBTUtil;

public abstract class WitherStormHead {
   protected final WitherStormEntity storm;
   protected final int headIndex;
   private final boolean syncHeadRotations;
   protected float lerpXRot;
   protected float lerpYRot;
   protected float lerpXSteps;
   protected float lerpYSteps;
   protected float mouthAnim;
   protected float mouthAnimO;
   protected float jawBrokenAnimation;
   protected float jawBrokenAnimationO;
   protected boolean isHeadShaking;
   protected float headShakeAnim;
   protected float headShakeAnimO;
   @Nullable
   protected Vec3 headPos = Vec3.ZERO;
   @Nullable
   protected Vec3 headPosO = Vec3.ZERO;
   @Nullable
   protected Vec3 distractionPos;
   protected int distractedTime;
   @Nullable
   protected AABB box;
   protected int nextClusterPickup;
   protected int idleClusterPickup;
   protected int nextRoarTick;
   protected int roarTick;
   protected int biteTick;
   protected int nextShake;
   protected double tractorBeamCutoffDistance = -1.0;
   public int nextHeadUpdate;
   public int idleHeadUpdates;
   protected int headHits;
   protected int requiredHits;
   protected int headHurtDuration;

   public WitherStormHead(WitherStormEntity storm, int headIndex, boolean syncHeadRotations) {
      if (headIndex >= storm.getTotalHeads()) {
         throw new IllegalArgumentException("Head index too large! Maximum amount of heads allowed is " + storm.getTotalHeads());
      } else {
         this.storm = storm;
         this.headIndex = headIndex;
         this.syncHeadRotations = syncHeadRotations;
         this.requiredHits = this.getRandomHitCount();
      }
   }

   public void update(int phase) {
      this.requiredHits = this.getRandomHitCount();
   }

   public boolean syncHeadRotations() {
      return this.syncHeadRotations;
   }

   public int getIndex() {
      return this.headIndex;
   }

   public CompoundTag save() {
      CompoundTag tag = new CompoundTag();
      tag.putBoolean("IsRoaring", this.isRoaring());
      tag.putInt("RoaringTime", this.getRoarTicks());
      if (this.getDistractedPos() != null) {
         tag.put("DistractedPos", WitherStormModNBTUtil.writeVector3d(this.getDistractedPos()));
      }

      tag.putInt("DistractedTime", this.getDistractedTime());
      tag.putInt("AttackCooldown", this.getHeadInjureAttemptCooldown());
      tag.putInt("InjuryTime", this.getHeadInjuryTicks());
      tag.putInt("Hits", this.getHeadHits());
      return tag;
   }

   public void read(CompoundTag tag) {
      if (tag.contains("IsRoaring")) {
         this.setRoar(tag.getBoolean("IsRoaring"));
      }

      if (tag.contains("RoaringTime")) {
         this.setRoarTicks(tag.getInt("RoaringTime"));
      }

      if (tag.contains("DistractedPos")) {
         this.setDistractedPos(WitherStormModNBTUtil.readVector3d(tag.getCompound("DistractedPos")));
      }

      if (tag.contains("DistractedTime")) {
         this.distractedTime = tag.getInt("DistractedTime");
      }

      if (tag.contains("AttackCooldown")) {
         this.setHeadInjureAttemptCooldown(tag.getInt("AttackCooldown"));
      }

      if (tag.contains("InjuryTime")) {
         this.setHeadInjuryTicks(tag.getInt("InjuryTime"));
      }

      if (tag.contains("Hits")) {
         this.setHeadHitCount(tag.getInt("Hits"));
      }
   }

   public abstract float getHeadXRot();

   public abstract float getHeadYRot();

   public abstract float getHeadXRotO();

   public abstract float getHeadYRotO();

   public abstract void setHeadXRot(float var1);

   public abstract void setHeadYRot(float var1);

   @Nullable
   public abstract LivingEntity getTarget();

   public abstract void setTarget(@Nullable LivingEntity var1);

   public abstract void setLookPos(@Nullable Vec3 var1, int var2);

   public abstract void doHeadLookLogic();

   protected boolean canShootNormalWitherSkulls() {
      return !this.storm.tractorBeamActive(this.headIndex);
   }

   protected boolean canShootFlamingSkull() {
      return this.storm.tractorBeamActive(this.headIndex);
   }

   public float getHeadXRot(float partialTicks) {
      return Mth.lerp(partialTicks, this.getHeadXRotO(), this.getHeadXRot());
   }

   public float getHeadYRot(float partialTicks) {
      return Mth.lerp(partialTicks, this.getHeadYRotO(), this.getHeadYRot());
   }

   public Vec3 getHeadPos() {
      return this.headPos;
   }

   public Vec3 getHeadPosO() {
      return this.headPosO;
   }

   public void baseTick(HeadConfiguration config) {
      this.headPosO = this.headPos;
      this.headPos = this.calculateHeadPosition(config);
      float size = this.storm.getPhase() > 3 ? 3.0F : 0.5F;
      this.box = new AABB(
         this.headPos.x - (double)size,
         this.headPos.y - (double)size,
         this.headPos.z - (double)size,
         this.headPos.x + (double)size,
         this.headPos.y + (double)size,
         this.headPos.z + (double)size
      );
   }

   public void tick() {
      this.handleAnimations();
      if (this.lerpXSteps > 0.0F) {
         float rot = this.getHeadXRot();
         float rotNew = (float)((double)rot + Mth.wrapDegrees((double)this.lerpXRot - (double)rot) / (double)this.lerpXSteps);
         this.setHeadXRot(rotNew);
         this.lerpXSteps--;
      }

      if (this.lerpYSteps > 0.0F) {
         float rot = this.getHeadYRot();
         float rotNew = (float)((double)rot + Mth.wrapDegrees((double)this.lerpYRot - (double)rot) / (double)this.lerpYSteps);
         this.setHeadYRot(rotNew);
         this.lerpYSteps--;
      }

      if (!this.storm.isOnDistantRenderer()) {
         float x = this.getHeadXRot();
         float y = this.getHeadYRot();
         Vec3 end = this.headPos.add(this.storm.getViewVector(x, y, 250.0F));
         BlockHitResult hitResult = this.storm.level().clip(new ClipContext(this.headPos, end, Block.COLLIDER, Fluid.NONE, null));
         if (hitResult.getType() == Type.BLOCK) {
            this.tractorBeamCutoffDistance = this.headPos.distanceTo(hitResult.getLocation());
         } else {
            this.tractorBeamCutoffDistance = -1.0;
         }
      }

      if (this.headHurtDuration > 0) {
         this.headHurtDuration--;
      }
   }

   public void doAi() {
      this.doHeadLookLogic();
      if (this.getHeadInjuryTicks() > 0) {
         this.decreaseHeadInjuryTicks();
         if (this.getHeadInjuryTicks() == 0 && !this.storm.isDeadOrPlayingDead()) {
            this.storm
               .playSound(WitherStormModSoundEvents.WITHER_STORM_TRACTOR_BEAM_ACTIVATES.get(), this.headIndex, this.storm.getSoundVolume() + 2.5F, 1.0F);
         }
      }

      if (this.getHeadInjureAttemptCooldown() > 0) {
         this.decreaseInjureAttemptCooldown();
      }

      if (this.isHeadInjured() && !this.storm.isDeadOrPlayingDead() && this.nextShake > 0) {
         this.nextShake--;
         if (this.nextShake == 0) {
            this.isHeadShaking = true;
         }
      }
   }

   public void doServerAi() {
      if (this.storm.tractorBeamActive(this.headIndex) && this.storm.tickCount >= this.nextClusterPickup) {
         if (this.storm instanceof WitherStormSegmentEntity) {
            this.nextClusterPickup = this.storm.tickCount + 12;
         } else if (this.storm.getPhase() <= 2) {
            this.nextClusterPickup = this.storm.tickCount + 24;
         } else if (this.storm.getPhase() == 3) {
            this.nextClusterPickup = this.storm.tickCount + 15;
         } else if (this.storm.getPhase() == 4) {
            this.nextClusterPickup = this.storm.tickCount + 5 + this.storm.getRandom().nextInt(20);
         } else if (this.storm.getPhase() == 5) {
            this.nextClusterPickup = this.storm.tickCount + 5 + this.storm.getRandom().nextInt(15);
         } else if (this.storm.getPhase() >= 6) {
            this.nextClusterPickup = this.storm.tickCount + this.storm.getRandom().nextInt(15);
         } else {
            this.nextClusterPickup = this.storm.tickCount + 45;
         }

         this.idleClusterPickup++;
         float x = this.getHeadXRot();
         float y = this.getHeadYRot();
         if ((Boolean)WitherStormModConfig.SERVER.tractorBeamClusterPickUp.get()) {
            if (this.storm instanceof WitherStormSegmentEntity) {
               this.storm.createClusterFromLook(x, y, (int)Math.max(1.0, Math.min(1.25, 1.0 + 0.125 * this.storm.getRandom().nextGaussian())), this.headIndex);
            } else {
               this.storm.createClusterFromLook(x, y, (int)this.storm.getClusterRadius(), this.headIndex);
            }
         }

         if ((Boolean)WitherStormModConfig.SERVER.tractorBeamsRemoveFluids.get()) {
            this.storm.removeFluidFromLook(x, y, this.headIndex);
         }

         this.idleClusterPickup = 0;
      }

      if (this.storm.tickCount >= this.nextHeadUpdate) {
         if (this.storm.getPhase() < 4) {
            this.nextHeadUpdate = this.storm.tickCount + 10 + this.storm.getRandom().nextInt(10);
         } else {
            this.nextHeadUpdate = this.storm.tickCount + 1200 + this.storm.getRandom().nextInt(120);
         }

         int k3 = this.idleHeadUpdates++;
         if (k3 > 15) {
            if (this.canShootNormalWitherSkulls()) {
               Vec3 pos = this.getHeadPos();
               double d0 = Mth.nextDouble(this.storm.getRandom(), pos.x - 10.0, pos.z + 10.0);
               double d1 = Mth.nextDouble(this.storm.getRandom(), pos.y - 5.0, pos.y + 5.0);
               double d2 = Mth.nextDouble(this.storm.getRandom(), pos.z - 10.0, pos.z + 10.0);
               this.storm.performRangedAttack(this.headIndex, d0, d1, d2, true);
            }

            this.idleHeadUpdates = 0;
         }

         if (this.getTarget() != null) {
            if (this.canShootNormalWitherSkulls()) {
               this.storm.performRangedAttack(this.headIndex, this.getTarget());
            }

            if (this.storm.getPhase() < 4) {
               this.nextHeadUpdate = this.storm.tickCount + 40 + this.storm.getRandom().nextInt(20);
            } else {
               this.nextHeadUpdate = this.storm.tickCount + 1800 + this.storm.getRandom().nextInt(160);
            }

            this.idleHeadUpdates = 0;
         } else {
            this.nextHeadUpdate = this.storm.tickCount + 40 + this.storm.getRandom().nextInt(20);
         }
      }

      if (this.nextRoarTick == 0) {
         this.nextRoarTick = this.storm.tickCount + 200 + this.storm.getRandom().nextInt(200);
      }

      if (this.storm.tickCount > this.nextRoarTick) {
         if (this.canShootFlamingSkull() && !this.storm.isAttractingFormidibomb()) {
            Vec3 view = this.storm.getViewVector(this.getHeadXRot(), this.getHeadYRot(), 1.0F);
            Vec3 headPos = this.getHeadPos();
            this.storm
               .spawnFlamingWitherSkull(this.headIndex, view.x + headPos.x, view.y + headPos.y, view.z + headPos.z);
         }

         this.doRoar(this.storm.isHeadInjured(this.headIndex));
         int min = (Integer)WitherStormModConfig.SERVER.minimumRoarInterval.get() * 20;
         int max = (Integer)WitherStormModConfig.SERVER.maximumRoarInterval.get() * 20;
         int randomMax = max - min;
         int random = 0;
         if (randomMax > 0) {
            random = this.storm.getRandom().nextInt(randomMax);
         }

         this.nextRoarTick = this.storm.tickCount + min + random;
      }

      if (this.isRoaring()) {
         this.roarTick++;
         if (this.roarTick > 40) {
            this.setRoar(false);
            this.roarTick = 0;
         }
      }

      if (this.isBiting()) {
         this.biteTick++;
         if (this.biteTick > 10) {
            this.setBiting(false);
            this.biteTick = 0;
            this.storm.playSound(WitherStormModSoundEvents.WITHER_STORM_BITE.get(), this.headIndex, Math.max(2.0F, this.storm.getSoundVolume()), 1.0F);
         }
      }

      if (this.distractedTime > 0) {
         this.distractedTime--;
         if (this.distractedTime <= 0) {
            this.setDistractedPos(null);
         }
      }

      if (this.isHeadInjured()) {
         this.storm
            .getRemovableGoalsManager()
            .removeGoals(WitherStormEntity.REMOVABLE_LOOK_GOALS[this.headIndex], this.storm.getGoalSelectorForHead(this.headIndex));
         this.storm
            .getRemovableGoalsManager()
            .removeGoals(WitherStormEntity.REMOVABLE_TARGET_GOALS[this.headIndex], this.storm.getTargetSelectorForHead(this.headIndex));
      } else {
         this.storm
            .getRemovableGoalsManager()
            .putGoals(WitherStormEntity.REMOVABLE_LOOK_GOALS[this.headIndex], this.storm.getGoalSelectorForHead(this.headIndex));
         this.storm
            .getRemovableGoalsManager()
            .putGoals(WitherStormEntity.REMOVABLE_TARGET_GOALS[this.headIndex], this.storm.getTargetSelectorForHead(this.headIndex));
      }
   }

   public Vec3 calculateHeadPosition(HeadConfiguration config) {
      float yBodyRot = (this.storm.yBodyRot + 180.0F) * (float) (Math.PI / 180.0);
      float yBodyRot90 = (this.storm.yBodyRot + 270.0F) * (float) (Math.PI / 180.0);
      float xBodyRot = -(this.storm.xBodyRot + 270.0F) * (float) (Math.PI / 180.0);
      Vec3 headOffset = config.getOffsetForHead(this.headIndex);
      double staticX = headOffset.x;
      double staticY = headOffset.y;
      double staticZ = headOffset.z;
      double xOffset = (double)Mth.cos(yBodyRot) * staticX;
      double zOffset = (double)Mth.sin(yBodyRot) * staticX;
      float offset = (float)Mth.atan2(staticZ, staticY);
      double rawX = (double)(Mth.cos(xBodyRot + offset) * Mth.cos(yBodyRot90));
      double rawY = (double)Mth.sin(xBodyRot + offset);
      double rawZ = (double)(Mth.cos(xBodyRot + offset) * Mth.sin(yBodyRot90));
      double sqrt = Math.sqrt(staticZ * staticZ + staticY * staticY);
      double x = xOffset + this.storm.getX() + rawX * sqrt;
      double y = this.storm.getY() + rawY * sqrt;
      double z = zOffset + this.storm.getZ() + rawZ * sqrt;
      return new Vec3(x, y, z);
   }

   protected void handleAnimations() {
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

      this.jawBrokenAnimationO = this.jawBrokenAnimation;
      if (this.storm.onGround() && this.storm.isDeadOrPlayingDead()) {
         this.jawBrokenAnimation = this.jawBrokenAnimation + (1.0F - this.jawBrokenAnimation) * 0.2F + 0.05F;
         if (this.jawBrokenAnimation > 1.5F) {
            this.jawBrokenAnimation = 1.5F;
         }
      } else {
         this.jawBrokenAnimation = this.jawBrokenAnimation + (-this.jawBrokenAnimation * 0.2F - 0.05F);
         if (this.jawBrokenAnimation < 0.0F) {
            this.jawBrokenAnimation = 0.0F;
         }
      }

      this.headShakeAnimO = this.headShakeAnim;
      if (this.isHeadShaking) {
         this.headShakeAnim = this.headShakeAnim + 0.02F + this.storm.getRandom().nextFloat() * 0.05F;
         if (this.headShakeAnimO >= 2.0F) {
            this.headShakeAnimO = 0.0F;
            this.headShakeAnim = 0.0F;
            this.isHeadShaking = false;
            this.nextShake = 20 + this.storm.getRandom().nextInt(20);
         }
      }
   }

   public boolean isRoaring() {
      return (Boolean)this.storm.getEntityData().get(HeadManager.HEAD_ROARS.get(this.headIndex));
   }

   public void setRoarTicks(int ticks) {
      this.roarTick = ticks;
   }

   public int getRoarTicks() {
      return this.roarTick;
   }

   public boolean isBiting() {
      return (Boolean)this.storm.getEntityData().get(HeadManager.HEADS_BITING.get(this.headIndex));
   }

   public void setRoar(boolean flag) {
      this.storm.getEntityData().set(HeadManager.HEAD_ROARS.get(this.headIndex), flag);
   }

   public void setBiting(boolean flag) {
      this.storm.getEntityData().set(HeadManager.HEADS_BITING.get(this.headIndex), flag);
   }

   public void doRoar(boolean screaming) {
      this.setRoar(true);
      SoundEvent event = WitherStormModSoundEvents.WITHER_STORM_ROAR.get();
      if (screaming) {
         event = WitherStormModSoundEvents.WITHER_STORM_HURT.get();
      }

      if (this.storm.areOtherHeadsDisabled() || this.storm.getPhase() < 4 && this.storm.getPhase() > 1) {
         if (this.headIndex == 0) {
            this.storm.playSound(event, this.headIndex, Math.max(6.0F, this.storm.getSoundVolume() + 2.5F), 1.0F);
         }
      } else if (this.storm.getPhase() > 3 && !this.storm.areOtherHeadsDisabled()) {
         this.storm.playSound(event, this.headIndex, Math.max(6.0F, this.storm.getSoundVolume() + 2.5F), 1.0F);
      }
   }

   public void startBiting() {
      this.biteTick = 0;
      this.storm.getEntityData().set(HeadManager.HEADS_BITING.get(this.headIndex), true);
   }

   @Nullable
   public Vec3 getDistractedPos() {
      return this.distractionPos;
   }

   public void setDistractedPos(@Nullable Vec3 pos) {
      this.distractionPos = pos;
   }

   public void makeDistracted(Vec3 pos, int time) {
      this.distractedTime = time;
      this.setDistractedPos(pos);
   }

   public void lerpHeadTo(float lerpToX, float lerpToY, float steps) {
      this.lerpXRot = lerpToX;
      this.lerpYRot = lerpToY;
      this.lerpXSteps = steps;
      this.lerpYSteps = steps;
   }

   public void lerpHeadXTo(float lerpToX, float steps) {
      this.lerpXRot = lerpToX;
      this.lerpXSteps = steps;
   }

   public void lerpHeadYTo(float lerpToY, float steps) {
      this.lerpYRot = lerpToY;
      this.lerpYSteps = steps;
   }

   public int getDistractedTime() {
      return this.distractedTime;
   }

   public int getNextRoarTick() {
      return this.nextRoarTick;
   }

   public void setNextRoarTick(int ticks) {
      this.nextRoarTick = ticks;
   }

   public float getMouthAnimation(float partialTicks) {
      return Mth.lerp(partialTicks, this.mouthAnimO, this.mouthAnim);
   }

   public float getBrokenJawAnimation(float partialTicks) {
      return Mth.lerp(partialTicks, this.jawBrokenAnimationO, this.jawBrokenAnimation);
   }

   public float getRollAngle(float partialTicks) {
      float lerp = Mth.clamp(Mth.lerp(partialTicks, this.headShakeAnimO, this.headShakeAnim), 0.0F, 1.0F);
      return Mth.sin(lerp * (float) Math.PI) * Mth.sin(lerp * (float) Math.PI * 12.0F) * 0.05F * (float) Math.PI;
   }

   public double getTractorBeamCutoff() {
      return this.tractorBeamCutoffDistance;
   }

   public boolean canSee(Entity entity) {
      Vec3 pos = this.getHeadPos();
      Vec3 entityPos = entity.getEyePosition(1.0F);
      return entity.level() != this.storm.level()
         ? false
         : this.storm.level().clip(new ClipContext(pos, entityPos, Block.COLLIDER, Fluid.NONE, this.storm)).getType() == Type.MISS;
   }

   protected int getRandomHitCount() {
      return this.storm.getPhase() > 3 ? 3 + this.storm.getRandom().nextInt(3) : 1 + this.storm.getRandom().nextInt(2);
   }

   public int getHeadInjureAttemptCooldown() {
      return (Integer)this.storm.getEntityData().get(HeadManager.INJURE_ATTEMPT_COOLDOWN.get(this.headIndex));
   }

   public void setHeadInjureAttemptCooldown(int amount) {
      this.storm.getEntityData().set(HeadManager.INJURE_ATTEMPT_COOLDOWN.get(this.headIndex), amount);
   }

   public int getHeadInjuryTicks() {
      return (Integer)this.storm.getEntityData().get(HeadManager.HURT_HEAD_TIME.get(this.headIndex));
   }

   public void setHeadInjuryTicks(int amount) {
      this.storm.getEntityData().set(HeadManager.HURT_HEAD_TIME.get(this.headIndex), amount);
   }

   public int getHeadHits() {
      return this.headHits;
   }

   public void setHeadHitCount(int amount) {
      this.headHits = amount;
   }

   public void decreaseHeadInjuryTicks() {
      this.setHeadInjuryTicks(this.getHeadInjuryTicks() - 1);
   }

   public void decreaseInjureAttemptCooldown() {
      this.setHeadInjureAttemptCooldown(this.getHeadInjureAttemptCooldown() - 1);
   }

   public boolean isHeadInjured() {
      return this.getHeadInjuryTicks() > 0;
   }

   public int getHeadHurtDuration() {
      return this.headHurtDuration;
   }

   public void hurt(@Nullable Entity entity, int injuryTime) {
      this.setHeadInjuryTicks(injuryTime);
      this.doRoar(true);
      Vec3 view = this.storm.getViewVector(this.getHeadXRot(), this.getHeadYRot(), 1.0F);
      Vec3 headPos = this.getHeadPos();
      this.storm
         .spawnBlueFlamingWitherSkull(this.headIndex, view.x + headPos.x, view.y + headPos.y, view.z + headPos.z);
      this.setRoarTicks(20);
      this.setHeadInjureAttemptCooldown(40);
      if (!this.storm.level().isClientSide()) {
         this.requiredHits = this.getRandomHitCount();
         this.headHits = 0;
         NotifyHeadInjuryMessage message = new NotifyHeadInjuryMessage(this.storm, this.headIndex);
         ResourceKey<Level> dimension = this.storm.level().dimension();
         WitherStormModPacketHandlers.MAIN.send(SimpleChannel.toDimension((ServerLevel)this.storm.level()), message);
         if (entity instanceof ServerPlayer player && this.storm.alreadyATarget(entity, true)) {
            entity.getData(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA.get())
               .ifPresent(data -> data.makeInvulnerable((Integer)WitherStormModConfig.SERVER.headEscapeTime.get() * 20 + player.getRandom().nextInt(80)));
            WitherStormModCriteriaTriggers.ESCAPE_STORM.trigger(player, this.storm);
         }
      }
   }

   public boolean checkAndCountAttack() {
      if ((Boolean)WitherStormModConfig.SERVER.canAttackHeads.get()) {
         WitherStormModPacketHandlers.MAIN
            .send(SimpleChannel.toTracking(this.storm), new OnHeadAttackedMessage(this.storm.getId(), this.headIndex));
         this.headHits++;
         if (this.headHits >= this.requiredHits) {
            return true;
         } else {
            this.doRoar(true);
            this.setRoarTicks(20);
            return false;
         }
      } else {
         return false;
      }
   }

   public void handleHeadAttackedOnClient() {
      this.headHurtDuration = 10;
      this.isHeadShaking = true;
   }

   @Nullable
   public AABB getBoundingBox() {
      return this.box;
   }
}
