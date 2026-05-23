package nonamecrackers2.witherstormmod.common.entity;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
// TODO_MIG[MOBTYPE]: MobType removed in 1.21; getMobType() is gone 鈥?use entity tags
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import nonamecrackers2.witherstormmod.common.entity.goal.SickenedMobsAttackGoal;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import org.jetbrains.annotations.NotNull;

public class SickenedZombie extends Zombie implements WitherSickened, RangedAttackMob {
   private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(SickenedZombie.class, EntityDataSerializers.BOOLEAN);
   private final WitherSickened.Data sickenedData = new WitherSickened.Data();

   public SickenedZombie(EntityType<? extends SickenedZombie> type, Level world) {
      super(type, world);
   }
   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 24.0)
         .add(Attributes.FOLLOW_RANGE, 48.0)
         .add(Attributes.MOVEMENT_SPEED, 0.28F)
         .add(Attributes.ATTACK_DAMAGE, 3.5)
         .add(Attributes.ARMOR, 2.2)
         .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
   }

   protected void addBehaviourGoals() {
      super.addBehaviourGoals();
      this.goalSelector.addGoal(2, new SickenedZombie.SickenedZombieTridentAttackGoal(this, 1.0, 40, 10.0F));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
      this.targetSelector.addGoal(3, new SickenedMobsAttackGoal(this));
   }

   public void tick() {
      super.tick();
      this.sickenedTick();
   }

   protected int decreaseAirSupply(int supply) {
      return supply;
   }

   protected boolean isSunSensitive() {
      return false;
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

   public boolean addEffect(@NotNull MobEffectInstance effect, Entity entity) {
      return this.sickenedAddEffect(effect, entity) && super.addEffect(effect, entity);
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

   protected ItemStack getSkull() {
      return ItemStack.EMPTY;
   }

   protected void doUnderWaterConversion() {
   }

   public boolean isUnderWaterConverting() {
      return false;
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

   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance instance) {
      super.populateDefaultEquipmentSlots(random, instance);
      if (this.random.nextFloat() < (this.level().getDifficulty() == Difficulty.HARD ? 0.05F : 0.01F)) {
         int i = this.random.nextInt(4);
         if (i == 0) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
         } else if (i < 4) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
         } else if (i == 4) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
         }
      }
   }

   public boolean doHurtTarget(Entity target) {
      boolean flag = super.doHurtTarget(target);
      if (flag) {
         this.addWitherToTarget(target);
      }

      return flag;
   }

   public boolean killedEntity(ServerLevel level, LivingEntity entity) {
      return this.sickenedInfect(entity);
   }

   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData groupData, CompoundTag tag) {
      ItemStack head = this.getItemBySlot(EquipmentSlot.HEAD);
      this.setCanBreakDoors(this.supportsBreakDoorGoal());
      this.populateDefaultEquipmentSlots(this.random, difficulty);
      this.populateDefaultEquipmentEnchantments(this.random, difficulty);
      if (head.is(Items.JACK_O_LANTERN)) {
         this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)WitherStormModBlocks.TAINTED_JACK_O_LANTERN.get()));
      } else if (head.is(Items.CARVED_PUMPKIN)) {
         this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike)WitherStormModBlocks.TAINTED_CARVED_PUMPKIN.get()));
      }

      if (this.isBaby()) {
         RandomSource random = level.getRandom();
         if ((double)random.nextFloat() < 0.05) {
            List<SickenedChicken> list = level.getEntitiesOfClass(SickenedChicken.class, this.getBoundingBox().inflate(5.0, 3.0, 5.0), EntitySelector.ENTITY_NOT_BEING_RIDDEN);
            if (!list.isEmpty()) {
               SickenedChicken sickenedChicken = list.get(0);
               sickenedChicken.setChickenJockey(true);
               this.startRiding(sickenedChicken);
            }
         } else if ((double)random.nextFloat() < 0.05) {
            SickenedChicken sickenedChicken = (SickenedChicken)(WitherStormModEntityTypes.SICKENED_CHICKEN.get()).create(this.level());
            if (sickenedChicken != null) {
               sickenedChicken.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
               sickenedChicken.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null, null);
               sickenedChicken.setChickenJockey(true);
               this.startRiding(sickenedChicken);
               level.addFreshEntity(sickenedChicken);
            }
         }
      }

      return groupData;
   }

   public void performRangedAttack(LivingEntity zombie, float p_32357_) {
      ThrownTrident throwntrident = new ThrownTrident(this.level(), this, new ItemStack(Items.TRIDENT));
      double d0 = zombie.getX() - this.getX();
      double d1 = zombie.getY(0.3333333333333333) - throwntrident.getY();
      double d2 = zombie.getZ() - this.getZ();
      double d3 = Math.sqrt(d0 * d0 + d2 * d2);
      throwntrident.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
      this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
      this.level().addFreshEntity(throwntrident);
   }

   public boolean hurt(DamageSource source, float amount) {
      return !this.sickenedCanBeHurt(source, amount) ? false : super.hurt(source, amount);
   }

   static class SickenedZombieTridentAttackGoal extends RangedAttackGoal {
      private final SickenedZombie zombie;

      public SickenedZombieTridentAttackGoal(RangedAttackMob attackMob, double p_25769_, int p_25770_, float p_25771_) {
         super(attackMob, p_25769_, p_25770_, p_25771_);
         this.zombie = (SickenedZombie)attackMob;
      }

      public boolean canUse() {
         return super.canUse() && this.zombie.getMainHandItem().is(Items.TRIDENT);
      }

      public void start() {
         super.start();
         this.zombie.setAggressive(true);
         this.zombie.startUsingItem(InteractionHand.MAIN_HAND);
      }

      public void stop() {
         super.stop();
         this.zombie.stopUsingItem();
         this.zombie.setAggressive(false);
      }
   }
}
