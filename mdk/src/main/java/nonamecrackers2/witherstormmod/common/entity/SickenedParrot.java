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
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.entity.goal.SickenedMobsAttackGoal;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import org.jetbrains.annotations.NotNull;

public class SickenedParrot extends Parrot implements WitherSickened, Enemy {
   private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(SickenedParrot.class, EntityDataSerializers.BOOLEAN);
   private final WitherSickened.Data sickenedData = new WitherSickened.Data();

   public SickenedParrot(EntityType<? extends SickenedParrot> type, Level level) {
      super(type, level);
   }

   @NotNull
   public MobType getMobType() {
      return WitherStormModMobTypes.SICKENED;
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1, false));
      this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0, 3.0F, 7.0F));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
      this.targetSelector.addGoal(3, new SickenedMobsAttackGoal(this));
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MOVEMENT_SPEED, 0.4)
         .add(Attributes.MAX_HEALTH, 16.0)
         .add(Attributes.FLYING_SPEED, 0.9F)
         .add(Attributes.FOLLOW_RANGE, 24.0);
   }

   public boolean isInSittingPose() {
      return false;
   }

   public boolean isAlliedTo(Entity entity) {
      return false;
   }

   public void tame(Player player) {
   }

   public boolean canAttack(LivingEntity entity) {
      if (entity instanceof Player player && this.isOwnedBy(player) && !player.isCreative() && !player.isSpectator()) {
         return true;
      }

      return super.canAttack(entity) && this.sickenedCanAttack(entity);
   }

   public void aiStep() {
      super.aiStep();
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
      return result != null ? result : InteractionResult.FAIL;
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

   @Override
   public void convertFrom(Mob mob) {
      if (mob instanceof Parrot parrot) {
         this.setVariant(parrot.getVariant());
         if (parrot.isTame()) {
            this.setTame(true);
            this.setOwnerUUID(parrot.getOwnerUUID());
         }
      }
   }

   @Override
   public void doExtraHandling(Mob mob) {
      if (mob instanceof Parrot parrot) {
         parrot.setVariant(this.getVariant());
         if (this.isTame()) {
            parrot.setTame(true);
            parrot.setOwnerUUID(this.getOwnerUUID());
         }
      }
   }

   public boolean hurt(DamageSource source, float amount) {
      return !this.sickenedCanBeHurt(source, amount) ? false : super.hurt(source, amount);
   }

   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
      return null;
   }
}
