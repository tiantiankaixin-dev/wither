package nonamecrackers2.witherstormmod.common.entity;

import net.neoforged.api.distmarker.Dist;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
// TODO_MIG[MOBTYPE]: MobType removed in 1.21; getMobType() is gone 鈥?use entity tags
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.IronGolem.Crackiness;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.goal.SickenedMobsAttackGoal;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import org.jetbrains.annotations.NotNull;

public class SickenedIronGolem extends AbstractGolem implements WitherSickened, Enemy {
   private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(SickenedIronGolem.class, EntityDataSerializers.BOOLEAN);
   private final WitherSickened.Data sickenedData = new WitherSickened.Data();
   private int attackAnimationTick;

   public SickenedIronGolem(EntityType<? extends SickenedIronGolem> type, Level level) {
      super(type, level);
      this.setMaxUpStep(1.0F);
   }
   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 60.0)
         .add(Attributes.FOLLOW_RANGE, 48.0)
         .add(Attributes.MOVEMENT_SPEED, 0.25)
         .add(Attributes.ATTACK_DAMAGE, 10.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.1, true));
      this.goalSelector.addGoal(1, new MoveTowardsTargetGoal(this, 1.0, 32.0F));
      this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(0, new HurtByTargetGoal(this, new Class[0]));
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

   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
      InteractionResult result = this.sickenedMobInteract(player, hand);
      if (result != null) {
         return result;
      } else {
         ItemStack itemstack = player.getItemInHand(hand);
         if (!itemstack.is(Items.IRON_INGOT)) {
            return InteractionResult.PASS;
         } else {
            float f = this.getHealth();
            this.heal(this.getMaxHealth() / 4.0F);
            if (this.getHealth() == f) {
               return InteractionResult.PASS;
            } else {
               float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
               this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f1);
               if (!player.getAbilities().instabuild) {
                  itemstack.shrink(1);
               }

               return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
         }
      }
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

   @Override
   public int getInfectedHealAmount() {
      return 6;
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

   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(CONVERTING, false);
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

   protected int decreaseAirSupply(int supply) {
      return supply;
   }

   public void aiStep() {
      super.aiStep();
      if (this.attackAnimationTick > 0) {
         this.attackAnimationTick--;
      }

      if (this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F && this.random.nextInt(5) == 0) {
         int i = Mth.floor(this.getX());
         int j = Mth.floor(this.getY() - 0.2);
         int k = Mth.floor(this.getZ());
         BlockPos pos = new BlockPos(i, j, k);
         BlockState blockstate = this.level().getBlockState(pos);
         if (!blockstate.isAir()) {
            this.level()
               .addParticle(
                  new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(pos),
                  this.getX() + ((double)this.random.nextFloat() - 0.5) * (double)this.getBbWidth(),
                  this.getY() + 0.1,
                  this.getZ() + ((double)this.random.nextFloat() - 0.5) * (double)this.getBbWidth(),
                  4.0 * ((double)this.random.nextFloat() - 0.5),
                  0.5,
                  ((double)this.random.nextFloat() - 0.5) * 4.0
               );
         }
      }
   }

   public boolean doHurtTarget(Entity target) {
      this.attackAnimationTick = 10;
      this.level().broadcastEntityEvent(this, (byte)4);
      float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
      float damageModified = (int)damage > 0 ? damage / 2.0F + (float)this.random.nextInt((int)damage) : damage;
      boolean flag = target.hurt(this.damageSources().mobAttack(this), damageModified);
      if (flag) {
         double d2;
         if (target instanceof LivingEntity living) {
            d2 = living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
         } else {
            d2 = 0.0;
         }

         double d1 = Math.max(0.0, 1.0 - d2);
         target.setDeltaMovement(target.getDeltaMovement().add(0.0, 0.4 * d1, 0.0));
         this.doEnchantDamageEffects(this, target);
         this.addWitherToTarget(target);
      }

      this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
      return flag;
   }

   public boolean hurt(DamageSource source, float amount) {
      if (!this.sickenedCanBeHurt(source, amount)) {
         return false;
      } else {
         IronGolem.Crackiness crackiness = this.getCrackiness();
         boolean flag = super.hurt(source, amount);
         if (flag && this.getCrackiness() != crackiness) {
            this.playSound(SoundEvents.IRON_GOLEM_DAMAGE);
         }

         return flag;
      }
   }

   public int /* Crackiness removed */ getCrackiness() {
      return IronGolem.Crackiness.NONE(this.getHealth() / this.getMaxHealth());
   }

   public void handleEntityEvent(byte event) {
      if (event == 4) {
         this.attackAnimationTick = 10;
         this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
      } else {
         super.handleEntityEvent(event);
      }
   }

   public int getAttackAnimationTick() {
      return this.attackAnimationTick;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.IRON_GOLEM_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.IRON_GOLEM_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState state) {
      this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
   }

   protected Vec3 getLeashOffset() {
      return new Vec3(0.0, (double)(0.875F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
   }
}
