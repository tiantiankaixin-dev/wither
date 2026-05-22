package nonamecrackers2.witherstormmod.common.entity;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Entity.MoveFunction;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.witherstormmod.common.entity.part.TentaclePartEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.util.ConditionalLookController;
import nonamecrackers2.witherstormmod.common.util.EmptyBodyController;
import nonamecrackers2.witherstormmod.common.world.tainting.WorldTainting;
import org.jetbrains.annotations.NotNull;

public class TentacleEntity extends Monster implements IMultipartHurtable<TentaclePartEntity<TentacleEntity>> {
   private static final EntityDataAccessor<Boolean> DORMANT = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> ANIMATION_OFFSET = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Float> XOFFSET = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> YOFFSET = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Integer> OFFSETSTEPS = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> SHOULDWRAPY = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Float> XOFFSETANIM = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> YOFFSETANIM = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> XCURL = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> YCURL = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Integer> CURLSTEPS = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Float> XCURLANIM = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> YCURLANIM = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> LASTXCURLANIM = SynchedEntityData.defineId(TentacleEntity.class, EntityDataSerializers.FLOAT);
   private float xCurlAnim;
   private float yCurlAnim;
   private final TentaclePartEntity<TentacleEntity> tentacle;
   private final TentaclePartEntity<TentacleEntity> last;
   private final TentaclePartEntity<TentacleEntity> secondLast;
   private int tentacleAnim;
   private float xRotOffsetAnim;
   private float yRotOffsetAnim;
   private int strangleTime;
   private float tentacleAnimSpeed = 1.0F;
   private float tentacleAnimReach = 1.0F;
   private int tentacleSwingTime;
   private int nextSwing;
   private int knockbackWait;
   private int awakeTime;
   private boolean canStrangle = true;
   private boolean canSwing = true;
   private TentacleEntity.StrangleGoal strangleGoal;
   private TentacleEntity.SwingGoal swingGoal;

   public TentacleEntity(EntityType<? extends TentacleEntity> type, Level world) {
      super(type, world);
      this.tentacle = new TentaclePartEntity(
         this,
         new TentaclePartEntity(
            this,
            new TentaclePartEntity(
               this,
               new TentaclePartEntity(this, new TentaclePartEntity(this, 1.5F, 3.0F, 4, 1.5F, 1.0F), 1.5F, 3.0F, 3, 1.5F, 1.0F),
               1.5F,
               2.5F,
               2,
               1.5F,
               1.0F
            ),
            1.5F,
            2.0F,
            1,
            1.5F,
            1.0F
         ),
         1.5F,
         1.5F,
         0,
         1.5F,
         1.0F
      );
      this.secondLast = (TentaclePartEntity<TentacleEntity>)this.tentacle.getSegment(3);
      this.last = (TentaclePartEntity<TentacleEntity>)this.tentacle.getLast();
      this.lookControl = new ConditionalLookController(this, entity -> false);
      this.blocksBuilding = false;
   }

   protected BodyRotationControl createBodyControl() {
      return new EmptyBodyController(this);
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 80.0)
         .add(Attributes.FOLLOW_RANGE, 8.0)
         .add(Attributes.MOVEMENT_SPEED, 0.0)
         .add(Attributes.ARMOR, 12.0)
         .add(Attributes.ATTACK_KNOCKBACK, 1.5);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new TentacleEntity.DoNothingGoal(this));
      this.swingGoal = new TentacleEntity.SwingGoal(this);
      this.goalSelector.addGoal(1, this.swingGoal);
      this.strangleGoal = new TentacleEntity.StrangleGoal(this);
      this.goalSelector.addGoal(2, this.strangleGoal);
      this.targetSelector.addGoal(0, new TentacleEntity.TargetGoal(this, Player.class, true, true));
      this.targetSelector.addGoal(1, new TentacleEntity.TargetGoal(this, Animal.class, true, true));
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(DORMANT, false);
      this.entityData.define(ANIMATION_OFFSET, 0);
      this.entityData.define(XOFFSET, 20.0F);
      this.entityData.define(YOFFSET, 0.0F);
      this.entityData.define(OFFSETSTEPS, 0);
      this.entityData.define(SHOULDWRAPY, true);
      this.entityData.define(XOFFSETANIM, 0.0F);
      this.entityData.define(YOFFSETANIM, 0.0F);
      this.entityData.define(XCURL, 1.3F);
      this.entityData.define(YCURL, 1.0F);
      this.entityData.define(CURLSTEPS, 0);
      this.entityData.define(XCURLANIM, 0.0F);
      this.entityData.define(YCURLANIM, 0.0F);
      this.entityData.define(LASTXCURLANIM, 0.0F);
   }

   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("Dormant")) {
         this.setDormant(compound.getBoolean("Dormant"));
      }

      if (compound.contains("AnimOffset")) {
         this.entityData.set(ANIMATION_OFFSET, compound.getInt("AnimOffset"));
      }

      if (compound.contains("XOffset")) {
         float offset = compound.getFloat("XOffset");
         this.entityData.set(XOFFSET, offset);
         this.setXOffset(offset);
      }

      if (compound.contains("YOffset")) {
         float offset = compound.getFloat("YOffset");
         this.entityData.set(YOFFSET, offset);
         this.setYOffset(offset);
      }

      if (compound.contains("XCurl")) {
         float xCurl = compound.getFloat("XCurl");
         this.entityData.set(XCURL, xCurl);
         this.setXCurl(xCurl);
      }

      if (compound.contains("YCurl")) {
         float yCurl = compound.getFloat("YCurl");
         this.entityData.set(YCURL, yCurl);
         this.setYCurl(yCurl);
      }

      this.nextSwing = compound.getInt("NextSwing");
      if (compound.contains("CanStrangle")) {
         this.setCanStrangle(compound.getBoolean("CanStrangle"));
      }

      if (compound.contains("CanSwing")) {
         this.setCanSwing(compound.getBoolean("CanSwing"));
      }

      if (compound.contains("XCurlAnim")) {
         this.lerpCurlXTo(compound.getFloat("XCurlAnim"), 1);
      }

      if (compound.contains("YCurlAnim")) {
         this.lerpCurlYTo(compound.getFloat("YCurlAnim"), 1);
      }

      if (compound.contains("XOffsetAnim")) {
         this.lerpBaseXTo(compound.getFloat("XOffsetAnim"), 1);
      }

      if (compound.contains("YOffsetAnim")) {
         this.lerpBaseYTo(compound.getFloat("YOffsetAnim"), 1, true);
      }
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("Dormant", this.isDormant());
      compound.putInt("AnimOffset", (Integer)this.entityData.get(ANIMATION_OFFSET));
      compound.putFloat("XOffset", (Float)this.entityData.get(XOFFSET));
      compound.putFloat("YOffset", (Float)this.entityData.get(YOFFSET));
      compound.putFloat("XCurl", (Float)this.entityData.get(XCURL));
      compound.putFloat("YCurl", (Float)this.entityData.get(YCURL));
      compound.putInt("NextSwing", this.nextSwing);
      compound.putBoolean("CanStrangle", this.canStrangle);
      compound.putBoolean("CanSwing", this.canSwing);
      compound.putFloat("XCurlAnim", (Float)this.entityData.get(XCURLANIM));
      compound.putFloat("YCurlAnim", (Float)this.entityData.get(YCURLANIM));
      compound.putFloat("XOffsetAnim", (Float)this.entityData.get(XOFFSETANIM));
      compound.putFloat("YOffsetAnim", (Float)this.entityData.get(YOFFSETANIM));
   }

   public void setDormant(boolean dormant) {
      this.entityData.set(DORMANT, dormant);
   }

   public boolean isDormant() {
      return (Boolean)this.entityData.get(DORMANT);
   }

   public void aiStep() {
      super.aiStep();
      int offsetSteps = (Integer)this.entityData.get(OFFSETSTEPS);
      if (offsetSteps > 0) {
         this.xRotOffsetAnim = this.xRotOffsetAnim
            + (float)Mth.wrapDegrees((double)((Float)this.entityData.get(XOFFSETANIM)).floatValue() - (double)this.xRotOffsetAnim) / (float)offsetSteps;
         this.yRotOffsetAnim = this.yRotOffsetAnim
            + (float)((double)((Float)this.entityData.get(YOFFSETANIM)).floatValue() - (double)this.yRotOffsetAnim) / (float)offsetSteps;
      }

      this.setXOffset(this.xRotOffsetAnim + this.getXOffset());
      this.setYOffset(this.yRotOffsetAnim + Mth.wrapDegrees(this.getYOffset()));
      if (this.strangleTime > 0) {
         this.strangleTime--;
         if (this.strangleTime == 0) {
            this.tentacleAnimSpeed = 1.0F;
         }
      }

      if (!this.isDormant()) {
         this.tentacleAnim++;
      }

      float vanillaAnim = this.walkAnimation.position() - this.walkAnimation.speed() * 2.0F;
      this.setXRot(
         (float)Math.toDegrees(
                  (double)(Mth.cos((float)(this.tentacleAnim + this.getAnimationOffset()) * 0.05F * this.tentacleAnimSpeed) + Mth.cos(vanillaAnim))
               )
               * 0.05F
               * this.tentacleAnimReach
            - 90.0F
            + this.tentacle.xRotOffset
      );
      this.setYRot(
         (float)Math.toDegrees(
                  (double)(Mth.sin((float)(this.tentacleAnim + this.getAnimationOffset()) * 0.06F * this.tentacleAnimSpeed) + Mth.sin(vanillaAnim))
               )
               * 0.14F
               * this.tentacleAnimReach
            - 270.0F
            + this.tentacle.yRotOffset
      );
      int curlSteps = (Integer)this.entityData.get(CURLSTEPS);
      if (curlSteps > 0) {
         this.xCurlAnim = this.xCurlAnim
            + (float)((double)((Float)this.entityData.get(XCURLANIM)).floatValue() - (double)this.xCurlAnim) / (float)curlSteps;
         this.yCurlAnim = this.yCurlAnim
            + (float)wrap(
                  (double)((Float)this.entityData.get(YCURLANIM)).floatValue() - (double)this.yCurlAnim, (Boolean)this.entityData.get(SHOULDWRAPY)
               )
               / (float)curlSteps;
      }

      this.setXCurl(this.xCurlAnim + this.getXCurl());
      this.secondLast.xCurl = this.tentacle.xCurl + (Float)this.entityData.get(LASTXCURLANIM);
      this.setYCurl(this.yCurlAnim + this.getYCurl());
      this.tentacle.tickAndO();
      this.tentacle.setPos(this.getX(), this.getY(), this.getZ());
      this.tentacle.setXRot(this.getXRot());
      this.tentacle.setYRot(this.getYRot());
      if (!this.level().isClientSide && this.tickCount % 120 == 0) {
         WitherStormModPacketHandlers.MAIN
            .send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new TentacleEntity.UpdateAnimationMessage(this.getId(), this.tentacleAnim));
      }

      if (!this.isDeadOrDying()) {
         if (this.canSwing()) {
            if (this.tentacleSwingTime > 0) {
               this.tentacleSwingTime--;
               if (this.tentacleSwingTime == 25) {
                  this.lerpBaseYTo(80.0F, 4, false);
                  this.lerpCurlYTo(-0.1F, 4);
               }

               if (this.tentacleSwingTime == 15 && this.getTarget() != null) {
                  this.doSwingAnimation(this.getTarget().position(), 20.0F, 2);
               }

               if (this.tentacleSwingTime == 0) {
                  this.stopSwingAnimation(false);
               }
            }

            if (this.nextSwing > 0) {
               this.nextSwing--;
            }

            if (this.tentacleSwingTime > 0) {
               this.knockbackWait++;
               LivingEntity target = this.getTarget();
               if (this.knockbackWait >= 35 && target != null) {
                  if (target instanceof Player player
                     && !player.getUseItem().isEmpty()
                     && player.getUseItem().getItem() instanceof ShieldItem
                     && !player.getCooldowns().isOnCooldown(Items.SHIELD)) {
                     player.getCooldowns().addCooldown(Items.SHIELD, 100);
                     this.level().broadcastEntityEvent(player, (byte)30);
                  }

                  boolean flag = target.hurt(this.damageSources().mobAttack(this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 3.5F);
                  if (flag) {
                     target.knockback(
                        (double)((float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK)), this.getX() - target.getX(), this.getZ() - target.getZ()
                     );
                     this.doEnchantDamageEffects(this, target);
                     this.setLastHurtByMob(target);
                  }
               }
            } else {
               this.knockbackWait = 0;
            }
         }

         if (this.awakeTime > 0) {
            this.awakeTime--;
            if (this.awakeTime == 0) {
               this.stopAwakeAnimation();
            }
         }
      }
   }

   private static double wrap(double f, boolean shouldWrap) {
      return shouldWrap ? Mth.wrapDegrees(f) : f;
   }

   public void push(double deltaX, double deltaY, double deltaZ) {
   }

   public boolean hurt(TentaclePartEntity<TentacleEntity> part, DamageSource source, float amount) {
      if (this.isDormant()) {
         return source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) ? this.reallyHurt(source, amount) : false;
      } else {
         return this.reallyHurt(source, amount);
      }
   }

   private boolean reallyHurt(DamageSource source, float amount) {
      return super.hurt(source, amount);
   }

   public boolean hurt(DamageSource source, float amount) {
      return source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) ? super.hurt(source, amount) : false;
   }

   public void checkDespawn() {
      this.noActionTime = 0;
   }

   public boolean addEffect(MobEffectInstance effect, @Nullable Entity entity) {
      return false;
   }

   public boolean killedEntity(ServerLevel level, LivingEntity entity) {
      if (entity instanceof Mob mob && WorldTainting.getInstance().convertMob(mob, false)) {
         return true;
      }

      return false;
   }

   public boolean canBeAffected(MobEffectInstance effect) {
      return false;
   }

   @NotNull
   public MobType getMobType() {
      return WitherStormModMobTypes.SICKENED;
   }

   protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
      return size.height / 2.0F;
   }

   public void startSleeping(BlockPos pos) {
   }

   public boolean canBeLeashed(Player player) {
      return false;
   }

   public boolean isPushable() {
      return false;
   }

   public boolean isPushedByFluid() {
      return false;
   }

   public void knockback(double strength, double deltaX, double deltaZ) {
   }

   public boolean isMultipartEntity() {
      return true;
   }

   public PartEntity<?>[] getParts() {
      List<TentaclePartEntity<TentacleEntity>> parts = Lists.newArrayList();
      parts.add(this.tentacle);
      parts.addAll(this.tentacle.getChained());
      Collections.reverse(parts);
      return parts.toArray(new PartEntity[parts.size()]);
   }

   public int getAnimationOffset() {
      return (Integer)this.entityData.get(ANIMATION_OFFSET);
   }

   public void setAnimationOffset(int offset) {
      this.entityData.set(ANIMATION_OFFSET, offset);
   }

   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      WitherStormModPacketHandlers.MAIN
         .send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new TentacleEntity.UpdateAnimationMessage(this.getId(), this.tentacleAnim));
      return super.getAddEntityPacket();
   }

   public TentaclePartEntity<TentacleEntity> getTentacle() {
      return this.tentacle;
   }

   public void positionRider(Entity entity, MoveFunction moveFunction) {
      if ((double)entity.distanceTo(this.last) < 20.0) {
         entity.setPos(this.last.getX(), this.last.getY(), this.last.getZ());
      } else {
         super.positionRider(entity, moveFunction);
      }
   }

   public Vec3 getDismountLocationForPassenger(LivingEntity entity) {
      return this.last.position();
   }

   public void onSyncedDataUpdated(EntityDataAccessor<?> parameter) {
      super.onSyncedDataUpdated(parameter);
      if (parameter.equals(XOFFSET)) {
         this.setXOffset(this.getXOffset());
      }

      if (parameter.equals(YOFFSET)) {
         this.setYOffset(this.getYOffset());
      }

      if (parameter.equals(XCURL)) {
         this.setXCurl(this.getXCurl());
      }

      if (parameter.equals(YCURL)) {
         this.setYCurl(this.getYCurl());
      }

      if (parameter.equals(LASTXCURLANIM)) {
         this.secondLast.xCurl = this.getXCurl() + (Float)this.entityData.get(LASTXCURLANIM);
      }
   }

   private void setXCurl(float curl) {
      this.tentacle.xCurl = curl;

      for (TentaclePartEntity<TentacleEntity> part : this.tentacle.getChained()) {
         part.xCurl = this.tentacle.xCurl;
      }
   }

   private void setYCurl(float curl) {
      this.tentacle.yCurl = curl;

      for (TentaclePartEntity<TentacleEntity> part : this.tentacle.getChained()) {
         part.yCurl = this.tentacle.yCurl;
      }
   }

   private void setXOffset(float rot) {
      this.tentacle.xRotOffset = rot;

      for (TentaclePartEntity<TentacleEntity> part : this.tentacle.getChained()) {
         part.xRotOffset = this.tentacle.xRotOffset;
      }
   }

   private void setYOffset(float rot) {
      this.tentacle.yRotOffset = rot;

      for (TentaclePartEntity<TentacleEntity> part : this.tentacle.getChained()) {
         part.yRotOffset = this.tentacle.yRotOffset;
      }
   }

   public void setSavedXCurl(float rot) {
      this.entityData.set(XCURL, rot);
      this.setXCurl(rot);
   }

   public void setSavedYCurl(float rot) {
      this.entityData.set(YCURL, rot);
      this.setYCurl(rot);
   }

   public void setSavedXOffset(float rot) {
      this.entityData.set(XOFFSET, rot);
      this.setXOffset(rot);
   }

   public void setSavedYOffset(float rot) {
      this.entityData.set(YOFFSET, rot);
      this.setYOffset(rot);
   }

   public float getXOffset() {
      return (Float)this.entityData.get(XOFFSET);
   }

   public float getYOffset() {
      return (Float)this.entityData.get(YOFFSET);
   }

   public float getXCurl() {
      return (Float)this.entityData.get(XCURL);
   }

   public float getYCurl() {
      return (Float)this.entityData.get(YCURL);
   }

   public void doSwingAttack(LivingEntity target) {
      this.tentacleSwingTime = 40;
   }

   public boolean isDoingSwingAttack() {
      return this.tentacleSwingTime > 0;
   }

   public boolean canDoSwingAttack() {
      return this.nextSwing <= 0 && !this.isDoingSwingAttack();
   }

   public void doSwingAnimation(Vec3 target, float overReach, int steps) {
      Vec3 delta = target.subtract(this.position());
      float atan2 = (float)Mth.atan2(delta.x, delta.z);
      float angle = atan2 * (180.0F / (float)Math.PI);
      this.lerpBaseYTo(-(angle + 360.0F - 90.0F + overReach + Mth.wrapDegrees(this.getYOffset())) % 360.0F, steps, false);
      this.lerpCurlTo(0.1F, 0.1F, 4);
      this.playSound(WitherStormModSoundEvents.WHOOSH.get(), 3.0F, 1.0F);
   }

   public void stopSwingAnimation(boolean setBaseY) {
      if (setBaseY) {
         this.setYOffset(this.tentacle.yRotOffset);
         this.entityData.set(YOFFSET, this.tentacle.yRotOffset);
      }

      this.lerpBaseYTo(0.0F, 12, true);
      this.lerpCurlTo(0.0F, 0.0F, 4);
   }

   public void doDeathAnimation() {
      this.lerpCurlTo(0.3F, 0.1F, 3);
      this.lerpBaseXTo(70.0F, 8);
   }

   public void die(DamageSource source) {
      super.die(source);
      this.doDeathAnimation();
      this.ejectPassengers();
   }

   public void doAwakeAnimation() {
      this.tentacleAnimSpeed = 6.0F;
      this.tentacleAnimReach = 4.0F;
      this.lerpCurlYTo(0.05F * (float)this.random.nextGaussian(), 8);
      this.awakeTime = 40;
      this.level().broadcastEntityEvent(this, (byte)13);
      this.playSound(WitherStormModSoundEvents.WHOOSH.get(), 3.0F, 1.0F);
   }

   public void doIndefiniteAwakeAnimation() {
      if (!this.level().isClientSide) {
         this.doAwakeAnimation();
      }

      this.awakeTime = 0;
      this.level().broadcastEntityEvent(this, (byte)14);
   }

   public void stopAwakeAnimation() {
      this.tentacleAnimSpeed = 1.0F;
      this.tentacleAnimReach = 1.0F;
      this.lerpCurlYTo(0.0F, 4);
   }

   protected void tickDeath() {
      this.deathTime++;
      if (this.deathTime >= 40) {
         this.remove(RemovalReason.KILLED);

         for (int i = 0; i < 40; i++) {
            double d0 = this.random.nextGaussian() * 0.02;
            double d1 = this.random.nextGaussian() * 0.02;
            double d2 = this.random.nextGaussian() * 0.02;
            this.level().addParticle(ParticleTypes.POOF, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), d0, d1, d2);
         }
      }
   }

   public void doStrangle() {
      this.strangleTime = 20;
      this.tentacleAnimSpeed = 15.0F;
      this.level().broadcastEntityEvent(this, (byte)11);
   }

   public void stopStrangle() {
      this.strangleTime = 0;
      this.tentacleAnimSpeed = 1.0F;
      this.level().broadcastEntityEvent(this, (byte)12);
   }

   public void handleEntityEvent(byte event) {
      if (event == 11) {
         this.doStrangle();
      } else if (event == 12) {
         this.stopStrangle();
      } else if (event == 13) {
         this.doAwakeAnimation();
      } else if (event == 14) {
         this.doIndefiniteAwakeAnimation();
      } else {
         super.handleEntityEvent(event);
      }
   }

   public void lerpBaseOffsetTo(float x, float y, int steps) {
      this.entityData.set(XOFFSETANIM, x);
      this.entityData.set(YOFFSETANIM, y);
      this.entityData.set(OFFSETSTEPS, steps);
   }

   public void lerpCurlTo(float x, float y, int steps) {
      this.entityData.set(XCURLANIM, x);
      this.entityData.set(YCURLANIM, y);
      this.entityData.set(CURLSTEPS, steps);
   }

   public void lerpBaseXTo(float x, int steps) {
      this.entityData.set(XOFFSETANIM, x);
      this.entityData.set(OFFSETSTEPS, steps);
   }

   public void lerpBaseYTo(float y, int steps, boolean wrap) {
      this.entityData.set(YOFFSETANIM, y);
      this.entityData.set(OFFSETSTEPS, steps);
      this.entityData.set(SHOULDWRAPY, wrap);
   }

   public void lerpCurlXTo(float x, int steps) {
      this.entityData.set(XCURLANIM, x);
      this.entityData.set(CURLSTEPS, steps);
   }

   public void lerpCurlYTo(float y, int steps) {
      this.entityData.set(YCURLANIM, y);
      this.entityData.set(CURLSTEPS, steps);
   }

   public AABB getBoundingBoxForCulling() {
      return this.getBoundingBox().inflate(4.0);
   }

   public boolean isPickable() {
      return false;
   }

   protected void pushEntities() {
   }

   public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData existing, CompoundTag data) {
      this.setSavedYOffset((float)this.random.nextInt(360));
      this.setSavedXOffset(15.0F + this.random.nextFloat() * 5.0F);
      this.setSavedXCurl(1.25F + this.random.nextFloat() * 0.1F);
      this.setAnimationOffset(this.random.nextInt(35) * 10000);
      return super.finalizeSpawn(world, difficulty, reason, existing, data);
   }

   public void setCanStrangle(boolean canStrangle) {
      this.canStrangle = canStrangle;
      if (!canStrangle) {
         this.goalSelector.removeGoal(this.strangleGoal);
      } else {
         this.goalSelector.removeGoal(this.strangleGoal);
         this.goalSelector.addGoal(2, this.strangleGoal);
      }
   }

   public boolean canStrangle() {
      return this.canStrangle;
   }

   public void setCanSwing(boolean canSwing) {
      this.canSwing = canSwing;
      if (!canSwing) {
         this.goalSelector.removeGoal(this.swingGoal);
      } else {
         this.goalSelector.removeGoal(this.swingGoal);
         this.goalSelector.addGoal(1, this.swingGoal);
      }
   }

   public boolean canSwing() {
      return this.canSwing;
   }

   public void curlAround(Vec3 pos) {
      Vec3 delta = pos.subtract(this.position());
      float atan2 = (float)Mth.atan2(delta.x, delta.z);
      float angle = atan2 * (180.0F / (float)Math.PI);
      this.lerpBaseYTo(-(angle + 180.0F + this.getYOffset()), 8, false);
      this.lerpCurlTo(0.1F, 0.1F, 4);
   }

   public void stopCurlingAround() {
      this.lerpBaseYTo(0.0F, 8, true);
      this.lerpCurlTo(0.0F, 0.0F, 4);
   }

   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
      super.recreateFromPacket(packet);
      PartEntity<?>[] parts = this.getParts();

      for (int i = 0; i < parts.length; i++) {
         parts[i].setId(1 + i + packet.getId());
      }
   }

   public PushReaction getPistonPushReaction() {
      return PushReaction.IGNORE;
   }

   public boolean canAttack(LivingEntity entity) {
      return super.canAttack(entity) && !(entity instanceof WitherSickened);
   }

   private static class DoNothingGoal extends Goal {
      private final TentacleEntity entity;

      public DoNothingGoal(TentacleEntity entity) {
         this.entity = entity;
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP, Flag.TARGET));
      }

      public boolean canUse() {
         return this.entity.isDormant();
      }
   }

   private static class StrangleGoal extends Goal {
      private final TentacleEntity entity;
      private int grabWait;
      private int nextStrangle;

      public StrangleGoal(TentacleEntity entity) {
         this.entity = entity;
      }

      public boolean canUse() {
         LivingEntity target = this.entity.getTarget();
         return !this.entity.isDormant() && target != null && target.isAlive() && this.entity.isAlive() && !this.entity.isDoingSwingAttack();
      }

      public void tick() {
         LivingEntity target = this.entity.getTarget();
         if (!this.entity.hasLineOfSight(target)) {
            target.stopRiding();
            this.entity.setTarget(null);
         } else {
            this.grabWait++;
            if (this.grabWait > 5) {
               target.startRiding(this.entity);
            }

            if (this.nextStrangle > 0) {
               this.nextStrangle--;
               if (this.nextStrangle <= 0) {
                  this.entity.doStrangle();
                  this.nextStrangle = 20 + this.entity.getRandom().nextInt(40);
                  this.entity.doHurtTarget(target);
               }
            }
         }
      }

      public void start() {
         LivingEntity target = this.entity.getTarget();
         this.entity.doSwingAnimation(target.position(), 0.0F, 4);
         this.entity.entityData.set(TentacleEntity.LASTXCURLANIM, 0.3F);
         this.nextStrangle = 20 + this.entity.getRandom().nextInt(40);
      }

      public void stop() {
         this.entity.ejectPassengers();
         this.grabWait = 0;
         this.entity.stopSwingAnimation(true);
         this.entity.stopStrangle();
         this.entity.entityData.set(TentacleEntity.LASTXCURLANIM, 0.0F);
      }
   }

   private static class SwingGoal extends Goal {
      private final TentacleEntity entity;

      public SwingGoal(TentacleEntity entity) {
         this.entity = entity;
      }

      public boolean canUse() {
         LivingEntity target = this.entity.getTarget();
         return !this.entity.isDormant()
            && target != null
            && target.isAlive()
            && this.entity.isAlive()
            && (this.entity.getHealth() < this.entity.getMaxHealth() || !this.entity.canStrangle)
            && this.entity.canDoSwingAttack();
      }

      public boolean canContinueToUse() {
         LivingEntity target = this.entity.getTarget();
         return target != null && target.isAlive() && this.entity.isAlive() && this.entity.isDoingSwingAttack();
      }

      public void start() {
         LivingEntity target = this.entity.getTarget();
         this.entity.doSwingAttack(target);
         this.entity.nextSwing = 120 + this.entity.getRandom().nextInt(120);
      }
   }

   public static class TargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
      public TargetGoal(Mob entity, Class<T> targetType, boolean mustSee, boolean mustReach) {
         super(entity, targetType, 10, mustSee, mustReach, e -> !(e instanceof Enemy));
      }

      protected AABB getTargetSearchArea(double range) {
         return this.mob.getBoundingBox().inflate(range);
      }

      protected void findTarget() {
         super.findTarget();

         for (Entity entity : this.mob.level().getEntitiesOfClass(TentacleEntity.class, this.getTargetSearchArea(this.getFollowDistance() + 10.0))) {
            TentacleEntity tentacle = (TentacleEntity)entity;
            if (tentacle.isAlive() && tentacle.getTarget() == this.target) {
               this.target = null;
               break;
            }
         }
      }
   }

   public static class UpdateAnimationMessage extends nonamecrackers2.crackerslib.common.packet.Packet {
      private int id;
      private int anim;

      public UpdateAnimationMessage(int entityId, int anim) {
         super(true);
         this.id = entityId;
         this.anim = anim;
      }

      public UpdateAnimationMessage() {
         super(false);
      }

      public void encode(FriendlyByteBuf buffer) {
         buffer.writeVarInt(this.id);
         buffer.writeInt(this.anim);
      }

      public void decode(FriendlyByteBuf buffer) {
         this.id = buffer.readVarInt();
         this.anim = buffer.readInt();
      }

      public Runnable getProcessor(Context context) {
         return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                  Optional<Level> optional = (Optional<Level>)LogicalSidedProvider.CLIENTWORLD.get(context.getDirection().getReceptionSide());
                  optional.ifPresent(world -> {
                     if (world.getEntity(this.id) instanceof TentacleEntity tentacle) {
                        tentacle.tentacleAnim = this.anim;
                     }
                  });
               });
      }
   }
}
