package nonamecrackers2.witherstormmod.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Spider.SpiderEffectsGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import nonamecrackers2.witherstormmod.common.entity.goal.SickenedMobsAttackGoal;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import org.jetbrains.annotations.NotNull;

public class SickenedSpider extends Spider implements WitherSickened {
   private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(SickenedSpider.class, EntityDataSerializers.BOOLEAN);
   private final WitherSickened.Data sickenedData = new WitherSickened.Data();

   public SickenedSpider(EntityType<? extends SickenedSpider> type, Level world) {
      super(type, world);
   }

   @NotNull
   public MobType getMobType() {
      return WitherStormModMobTypes.SICKENED;
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 20.0)
         .add(Attributes.MOVEMENT_SPEED, 0.34F)
         .add(Attributes.ATTACK_DAMAGE, 3.0)
         .add(Attributes.FOLLOW_RANGE, 32.0);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.45F));
      this.goalSelector.addGoal(4, new SickenedSpider.AttackGoal(this));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
      this.targetSelector.addGoal(3, new SickenedMobsAttackGoal(this));
   }

   public void tick() {
      super.tick();
      this.sickenedTick();
   }

   public <T extends Mob> T convertTo(EntityType<T> type, boolean loot) {
      return this.sickenedConvertTo(type, loot);
   }

   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
      InteractionResult result = this.sickenedMobInteract(player, hand);
      return result != null ? result : super.mobInteract(player, hand);
   }

   public boolean removeWhenFarAway(double dist) {
      return this.sickenedRemoveWhenFarAway(dist);
   }

   public boolean addEffect(MobEffectInstance effect, Entity entity) {
      return this.sickenedAddEffect(effect, entity) ? super.addEffect(effect, entity) : false;
   }

   public boolean killedEntity(ServerLevel level, LivingEntity entity) {
      return this.sickenedInfect(entity);
   }

   public float getVoicePitch() {
      return this.sickenedGetVoicePitch();
   }

   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      this.sickenedSave(tag);
   }

   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.sickenedRead(tag);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(CONVERTING, false);
   }

   @Override
   public WitherSickened.Data getData() {
      return this.sickenedData;
   }

   @Override
   public boolean isConverting() {
      return (Boolean)this.entityData.get(CONVERTING);
   }

   @Override
   public void setConverting(boolean flag) {
      this.entityData.set(CONVERTING, flag);
   }

   @Override
   public float getSickenedEquipmentDropChance(EquipmentSlot slot) {
      return this.getEquipmentDropChance(slot);
   }

   public boolean canAttackType(EntityType<?> type) {
      return type != WitherStormModEntityTypes.WITHERED_SYMBIONT.get() && super.canAttackType(type);
   }

   public boolean canAttack(LivingEntity entity) {
      return super.canAttack(entity) && this.sickenedCanAttack(entity);
   }

   public boolean doHurtTarget(Entity target) {
      boolean flag = super.doHurtTarget(target);
      if (flag) {
         this.addWitherToTarget(target);
      }

      return flag;
   }

   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData groupData, CompoundTag tag) {
      RandomSource random = level.getRandom();
      if (random.nextInt(100) == 0) {
         SickenedSkeleton sickenedSkeleton = (SickenedSkeleton)(WitherStormModEntityTypes.SICKENED_SKELETON.get()).create(this.level());
         if (sickenedSkeleton != null) {
            sickenedSkeleton.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            sickenedSkeleton.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null, null);
            sickenedSkeleton.startRiding(this);
         }
      }

      if (groupData == null) {
         groupData = new SpiderEffectsGroupData();
         if (level.getDifficulty() == Difficulty.HARD && random.nextFloat() < 0.1F * difficulty.getSpecialMultiplier()) {
            ((SpiderEffectsGroupData)groupData).setRandomEffect(random);
         }
      }

      if (groupData instanceof SpiderEffectsGroupData sickenedSpider$spidereffectsgroupdata) {
         MobEffect mobEffect = sickenedSpider$spidereffectsgroupdata.effect;
         if (mobEffect != null) {
            this.addEffect(new MobEffectInstance(mobEffect, -1));
         }
      }

      return groupData;
   }

   public boolean hurt(DamageSource source, float amount) {
      return !this.sickenedCanBeHurt(source, amount) ? false : super.hurt(source, amount);
   }

   private static class AttackGoal extends MeleeAttackGoal {
      public AttackGoal(SickenedSpider entity) {
         super(entity, 1.0, true);
      }

      public boolean canUse() {
         return super.canUse() && !this.mob.isVehicle();
      }

      protected double getAttackReachSqr(LivingEntity target) {
         return (double)(4.0F + target.getBbWidth());
      }
   }
}
