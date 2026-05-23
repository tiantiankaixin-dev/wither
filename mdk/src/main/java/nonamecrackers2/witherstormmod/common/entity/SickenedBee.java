package nonamecrackers2.witherstormmod.common.entity;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
// TODO_MIG[MOBTYPE]: MobType removed in 1.21; getMobType() is gone 鈥?use entity tags
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Bee.BeeGoToHiveGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import nonamecrackers2.witherstormmod.common.entity.goal.SickenedMobsAttackGoal;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;
import nonamecrackers2.witherstormmod.common.world.tainting.WorldTainting;
import org.jetbrains.annotations.NotNull;

public class SickenedBee extends Bee implements WitherSickened, Enemy {
   private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(SickenedBee.class, EntityDataSerializers.BOOLEAN);
   public static final Predicate<BlockState> TAINTABLE = state -> state.is(WitherStormModBlockTags.SICKENED_BEE_CAN_CONVERT)
         && !state.is(WitherStormModBlockTags.TAINTED_BLOCKS)
         && !state.is(Blocks.WITHER_ROSE);
   private final WitherSickened.Data sickenedData = new WitherSickened.Data();
   private int underWaterTicks;

   public SickenedBee(EntityType<? extends SickenedBee> type, Level level) {
      super(type, level);
   }

   @NotNull
   public MobType getMobType() {
      return WitherStormModMobTypes.SICKENED;
   }

   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector
         .removeAllGoals(
            g -> {
               String className = g.getClass().getName();
               return className.equals("net.minecraft.world.entity.animal.Bee$BeeGrowCropGoal")
                  || className.equals("net.minecraft.world.entity.animal.Bee$BeeLocateHiveGoal")
                  || g instanceof BeeGoToHiveGoal;
            }
         );
      this.goalSelector.addGoal(7, new SickenedBee.TaintGoal());
      this.targetSelector.removeAllGoals(g -> true);
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
      this.targetSelector.addGoal(3, new SickenedMobsAttackGoal(this));
   }

   protected void customServerAiStep() {
      if (this.isInWaterOrBubble()) {
         this.underWaterTicks++;
      } else {
         this.underWaterTicks = 0;
      }

      if (this.underWaterTicks > 200) {
         this.hurt(this.damageSources().drown(), 1.0F);
      }

      if (!this.level().isClientSide) {
         this.updatePersistentAnger((ServerLevel)this.level(), false);
      }
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MOVEMENT_SPEED, 0.3)
         .add(Attributes.MAX_HEALTH, 15.0)
         .add(Attributes.FLYING_SPEED, 1.2)
         .add(Attributes.ATTACK_DAMAGE, 2.0)
         .add(Attributes.FOLLOW_RANGE, 48.0);
   }

   public boolean hasHive() {
      return false;
   }

   public boolean hasNectar() {
      return false;
   }

   public void aiStep() {
      super.aiStep();
   }

   public void tick() {
      super.tick();
      this.sickenedTick();
      if (this.tickCount % 4 == 0) {
         double d0 = this.getX() + (double)this.random.nextFloat();
         double d1 = this.getY() + (double)this.random.nextFloat();
         double d2 = this.getZ() + (double)this.random.nextFloat();
         this.level().addParticle((ParticleOptions)WitherStormModParticleTypes.PHLEGM.get(), d0 - 0.5, d1, d2 - 0.5, 0.0, 0.0, 0.0);
      }
   }

   public <T extends Mob> T convertTo(EntityType<T> type, boolean loot) {
      return this.sickenedConvertTo(type, loot);
   }

   public boolean killedEntity(ServerLevel level, LivingEntity entity) {
      return this.sickenedInfect(entity);
   }

   @NotNull
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      InteractionResult result = this.sickenedMobInteract(player, hand);
      return result != null ? result : super.mobInteract(player, hand);
   }

   public boolean addEffect(MobEffectInstance effect, Entity entity) {
      return this.sickenedAddEffect(effect, entity) && super.addEffect(effect, entity);
   }

   public boolean removeWhenFarAway(double dist) {
      return false;
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

   public Bee getBreedOffspring(ServerLevel level, AgeableMob mob) {
      return null;
   }

   public boolean isFood(ItemStack stack) {
      return false;
   }

   public boolean canAttack(LivingEntity entity) {
      return super.canAttack(entity) && this.sickenedCanAttack(entity);
   }

   public boolean doHurtTarget(Entity target) {
      boolean flag = target.hurt(this.damageSources().sting(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
      if (flag) {
         this.addPotentWitherToTarget(target);
         this.playSound(SoundEvents.BEE_STING, 1.0F, 0.8F);
      }

      return flag;
   }

   public boolean hurt(DamageSource source, float amount) {
      return !this.sickenedCanBeHurt(source, amount) ? false : super.hurt(source, amount);
   }

   class TaintGoal extends Goal {
      private static final int MAX_USE_TICKS = 120;
      private int useTicks;

      public boolean canUse() {
         return SickenedBee.this.isAngry() ? false : !(SickenedBee.this.random.nextFloat() < 0.3F);
      }

      public boolean canContinueToUse() {
         return !SickenedBee.this.isAngry() && this.useTicks > 0;
      }

      public void start() {
         this.useTicks = 120;
      }

      public void tick() {
         this.useTicks--;
         if (SickenedBee.this.random.nextInt(this.adjustedTickDelay(30)) == 0) {
            BlockPos pos = SickenedBee.this.blockPosition();
            BlockPos saved = SickenedBee.this.getSavedFlowerPos();
            if (SickenedBee.TAINTABLE.test(SickenedBee.this.level().getBlockState(pos))) {
               WorldTainting.getInstance().convertBlock(pos, SickenedBee.this.level());
            } else if (saved != null && pos.distManhattan(saved) <= 1) {
               WorldTainting.getInstance().convertBlock(saved, SickenedBee.this.level());
            }
         }
      }
   }
}
