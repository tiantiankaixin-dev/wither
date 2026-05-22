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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.entity.goal.PhantomOrbitWitherStormGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.SickenedMobsAttackGoal;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import nonamecrackers2.witherstormmod.mixin.IMixinPhantom;
import org.jetbrains.annotations.NotNull;

public class SickenedPhantom extends Phantom implements WitherSickened {
   private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(SickenedPhantom.class, EntityDataSerializers.BOOLEAN);
   private final WitherSickened.Data sickenedData = new WitherSickened.Data();

   public SickenedPhantom(EntityType<? extends SickenedPhantom> type, Level level) {
      super(type, level);
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 3.0);
   }

   @NotNull
   public MobType getMobType() {
      return WitherStormModMobTypes.SICKENED;
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new PhantomOrbitWitherStormGoal(this, 1));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
      this.targetSelector.addGoal(3, new SickenedMobsAttackGoal(this));
      super.registerGoals();
   }

   protected int decreaseAirSupply(int supply) {
      return supply;
   }

   public boolean isSunBurnTick() {
      return false;
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
      return this.goalSelector.getRunningGoals().noneMatch(entry -> entry.getGoal() instanceof PhantomOrbitWitherStormGoal);
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

   @Override
   public void convertFrom(Mob mob) {
      Phantom phantom = (Phantom)mob;
      ((IMixinPhantom)this).setAnchorPoint(this.blockPosition().above(5));
      this.setPhantomSize(phantom.getPhantomSize());
   }

   @Override
   public void doExtraHandling(Mob mob) {
      Phantom phantom = (Phantom)mob;
      phantom.setPhantomSize(this.getPhantomSize());
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
