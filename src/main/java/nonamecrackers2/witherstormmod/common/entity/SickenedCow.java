package nonamecrackers2.witherstormmod.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.entity.goal.SickenedMobsAttackGoal;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import org.jetbrains.annotations.NotNull;

public class SickenedCow extends Cow implements WitherSickened, Enemy {
   private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(SickenedCow.class, EntityDataSerializers.BOOLEAN);
   private final WitherSickened.Data sickenedData = new WitherSickened.Data();

   public SickenedCow(EntityType<? extends SickenedCow> type, Level level) {
      super(type, level);
   }

   @NotNull
   public MobType getMobType() {
      return WitherStormModMobTypes.SICKENED;
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.MAX_HEALTH, 25.0).add(Attributes.FOLLOW_RANGE, 24.0);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.125, false));
      this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true));
      this.targetSelector.addGoal(2, new SickenedMobsAttackGoal(this));
   }

   public void tick() {
      super.tick();
      this.sickenedTick();
   }

   public <T extends Mob> T convertTo(EntityType<T> type, boolean loot) {
      return this.sickenedConvertTo(type, loot);
   }

   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      InteractionResult result = this.sickenedMobInteract(player, hand);
      return result != null ? result : super.mobInteract(player, hand);
   }

   public boolean removeWhenFarAway(double dist) {
      return false;
   }

   public boolean addEffect(MobEffectInstance effect, Entity entity) {
      return this.sickenedAddEffect(effect, entity) ? super.addEffect(effect, entity) : false;
   }

   public boolean killedEntity(ServerLevel level, LivingEntity entity) {
      return this.sickenedInfect(entity);
   }

   public float getVoicePitch() {
      return this.sickenedGetVoicePitch(1.15F, 0.65F);
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

   public Cow getBreedOffspring(ServerLevel level, AgeableMob mob) {
      return null;
   }

   public boolean isFood(ItemStack stack) {
      return false;
   }

   public boolean doHurtTarget(Entity target) {
      boolean flag = super.doHurtTarget(target);
      if (flag) {
         this.addWitherToTarget(target);
      }

      return flag;
   }

   public boolean hurt(DamageSource source, float amount) {
      return !this.sickenedCanBeHurt(source, amount) ? false : super.hurt(source, amount);
   }
}
