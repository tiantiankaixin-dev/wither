package nonamecrackers2.witherstormmod.common.entity;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import nonamecrackers2.witherstormmod.common.entity.goal.SickenedMobsAttackGoal;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import nonamecrackers2.witherstormmod.common.util.SnowballAccessor;
import org.jetbrains.annotations.NotNull;

public class SickenedSnowGolem extends SnowGolem implements WitherSickened, Enemy {
   private static final EntityDataAccessor<Boolean> CONVERTING = SynchedEntityData.defineId(SickenedSnowGolem.class, EntityDataSerializers.BOOLEAN);
   private final WitherSickened.Data sickenedData = new WitherSickened.Data();

   public SickenedSnowGolem(EntityType<? extends SickenedSnowGolem> type, Level world) {
      super(type, world);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25, 12, 10.0F));
      this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0, 1.0000001E-5F));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
      this.targetSelector.addGoal(3, new SickenedMobsAttackGoal(this));
   }

   @NotNull
   public MobType getMobType() {
      return WitherStormModMobTypes.SICKENED;
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.24);
   }

   protected int decreaseAirSupply(int supply) {
      return supply;
   }

   public boolean isSensitiveToWater() {
      return false;
   }

   public void tick() {
      super.tick();
      this.sickenedTick();
   }

   public void shear(SoundSource source) {
      this.level().playSound((Player)null, this, SoundEvents.SNOW_GOLEM_SHEAR, source, 1.0F, 1.0F);
      if (!this.level().isClientSide()) {
         this.setPumpkin(false);
         this.spawnAtLocation(new ItemStack((ItemLike)WitherStormModItems.TAINTED_CARVED_PUMPKIN.get()), 1.7F);
      }
   }

   public void performRangedAttack(LivingEntity entity, float p_29913_) {
      Snowball snowball = new Snowball(this.level(), this);
      if ((double)this.random.nextFloat() < 0.1) {
         ((SnowballAccessor)snowball).setWitherEffect(true);
      }

      double d0 = entity.getEyeY() - 1.1F;
      double d1 = entity.getX() - this.getX();
      double d2 = d0 - snowball.getY();
      double d3 = entity.getZ() - this.getZ();
      double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.2F;
      snowball.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
      this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
      this.level().addFreshEntity(snowball);
   }

   public <T extends Mob> T convertTo(EntityType<T> type, boolean loot) {
      return this.sickenedConvertTo(type, loot);
   }

   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
      InteractionResult result = this.sickenedMobInteract(player, hand);
      return result != null ? result : super.mobInteract(player, hand);
   }

   public List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level world, BlockPos pos, int fortune) {
      world.playSound(null, this, SoundEvents.SNOW_GOLEM_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
      this.gameEvent(GameEvent.SHEAR, player);
      if (!world.isClientSide()) {
         this.setPumpkin(false);
         return Collections.singletonList(new ItemStack((ItemLike)WitherStormModItems.TAINTED_CARVED_PUMPKIN.get()));
      } else {
         return Collections.emptyList();
      }
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

   protected SoundEvent getAmbientSound() {
      return SoundEvents.SNOW_GOLEM_AMBIENT;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.SNOW_GOLEM_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.SNOW_GOLEM_DEATH;
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

   public boolean hurt(DamageSource source, float amount) {
      return !this.sickenedCanBeHurt(source, amount) ? false : super.hurt(source, amount);
   }
}
