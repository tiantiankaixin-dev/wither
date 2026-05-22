package nonamecrackers2.witherstormmod.common.entity;

import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCriteriaTriggers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEffects;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.world.tainting.WorldTainting;

public interface WitherSickened {
   static final Predicate<Mob> CAN_WEAR_ARMOR = mob -> mob instanceof SickenedZombie
         || mob instanceof SickenedSkeleton
         || mob instanceof SickenedVillager
         || mob instanceof SickenedPillager;

   default int getConversionProgress() {
      Mob mob = this.witherstormmod$cast();
      int i = 1;
      if (mob.getRandom().nextFloat() < 0.01F) {
         int j = 0;
         MutableBlockPos pos = new MutableBlockPos();

         for (int k = (int)mob.getX() - 4; k < (int)mob.getX() + 4 && j < 14; k++) {
            for (int l = (int)mob.getY() - 4; l < (int)mob.getY() + 4 && j < 14; l++) {
               for (int i1 = (int)mob.getZ() - 4; i1 < (int)mob.getZ() + 4 && j < 14; i1++) {
                  Block block = mob.level().getBlockState(pos.set(k, l, i1)).getBlock();
                  if (block == Blocks.IRON_BARS || block instanceof BedBlock) {
                     if (mob.getRandom().nextFloat() < 0.3F) {
                        i++;
                     }

                     j++;
                  }
               }
            }
         }
      }

      return i;
   }

   default void sickenedTick() {
      Mob cast = this.witherstormmod$cast();
      WitherSickened.Data data = this.getData();
      if (!cast.level().isClientSide && cast.isAlive() && this.isConverting()) {
         int i = this.getConversionProgress();
         data.conversionTime -= i;
         if (data.getConversionTime() <= 0 && ForgeEventFactory.canLivingConvert(cast, (EntityType)cast.getType(), timer -> data.conversionTime = timer)) {
            this.cure((ServerLevel)cast.level());
         }
      }
   }

   default <E extends Mob> E sickenedConvertTo(EntityType<E> type, boolean loot) {
      Mob cast = this.witherstormmod$cast();
      WitherSickened.Data data = this.getData();
      if (!cast.isAlive()) {
         return null;
      } else {
         E t = (E)type.create(cast.level());
         if (data.getOriginalData() != null) {
            assert t != null;
            t.load(data.getOriginalData());
         }

         assert t != null;
         if (true) {
            t.setHealth(cast.getHealth());
            t.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(WitherSicknessTracker::cure);
            t.removeAllEffects();
            t.copyPosition(cast);
            t.setBaby(cast.isBaby());
            t.setNoAi(cast.isNoAi());
            if (cast.hasCustomName()) {
               t.setCustomName(cast.getCustomName());
               t.setCustomNameVisible(cast.isCustomNameVisible());
            }

            if (cast.isPersistenceRequired()) {
               t.setPersistenceRequired();
            }

            t.setInvulnerable(cast.isInvulnerable());
            if (loot) {
               t.setCanPickUpLoot(cast.canPickUpLoot());

               for (EquipmentSlot slot : EquipmentSlot.values()) {
                  ItemStack stack = cast.getItemBySlot(slot);
                  if (!stack.isEmpty()) {
                     t.setItemSlot(slot, stack.copy());
                     t.setDropChance(slot, this.getSickenedEquipmentDropChance(slot));
                     stack.setCount(0);
                  }
               }
            }

            cast.level().addFreshEntity(t);
            if (cast.isPassenger()) {
               Entity entity = cast.getVehicle();
               cast.stopRiding();
               assert entity != null;

               t.startRiding(entity, true);
            }

            cast.discard();
            return t;
         }
         return null;
      }
   }

   @Nullable
   default InteractionResult sickenedMobInteract(Player player, InteractionHand hand) {
      Mob cast = this.witherstormmod$cast();
      ItemStack stack = player.getItemInHand(hand);
      if (stack.getItem() == WitherStormModItems.GOLDEN_APPLE_STEW.get()) {
         if (!this.isConverting() && this.getOriginalType() != null) {
            if (!player.getAbilities().instabuild) {
               stack.shrink(1);
            }

            if (!cast.level().isClientSide) {
               this.startConverting(player.getUUID(), cast.getRandom().nextInt(2401) + 3600);
            }

            return InteractionResult.SUCCESS;
         } else {
            return InteractionResult.CONSUME;
         }
      } else {
         return null;
      }
   }

   default boolean sickenedRemoveWhenFarAway(double dist) {
      return !this.isConverting();
   }

   default boolean sickenedAddEffect(MobEffectInstance effect, @Nullable Entity entity) {
      return effect.getEffect() != WitherStormModEffects.WITHER_SICKNESS.get() && effect.getEffect() != MobEffects.WITHER;
   }

   default float sickenedGetVoicePitch() {
      return this.sickenedGetVoicePitch(1.35F, 0.85F);
   }

   default float sickenedGetVoicePitch(float babyBasePitch, float adultBasePitch) {
      Mob cast = this.witherstormmod$cast();
      return cast.isBaby()
         ? (cast.getRandom().nextFloat() - cast.getRandom().nextFloat()) * 0.2F + babyBasePitch
         : (cast.getRandom().nextFloat() - cast.getRandom().nextFloat()) * 0.2F + adultBasePitch;
   }

   default void sickenedSave(CompoundTag compound) {
      WitherSickened.Data data = this.getData();
      String id = data.encodeOriginalId();
      if (id != null) {
         compound.putString("OriginalType", id);
      }

      if (data.getOriginalData() != null) {
         compound.put("OriginalData", data.getOriginalData());
      }

      compound.putInt("ConversionTime", this.isConverting() ? data.getConversionTime() : -1);
      if (data.getConversionStarter() != null) {
         compound.putUUID("ConversionPlayer", data.getConversionStarter());
      }
   }

   default void sickenedRead(CompoundTag compound) {
      WitherSickened.Data data = this.getData();
      if (compound.contains("OriginalType")) {
         ResourceLocation location = ResourceLocation.tryParse(compound.getString("OriginalType"));
         EntityType<?> type = (EntityType<?>)ForgeRegistries.ENTITY_TYPES.getValue(location);
         data.setOriginal(type, compound.contains("OriginalData") ? compound.getCompound("OriginalData") : null);
      }

      if (compound.contains("ConversionTime") && compound.getInt("ConversionTime") > -1) {
         this.startConverting(compound.hasUUID("ConversionPlayer") ? compound.getUUID("ConversionPlayer") : null, compound.getInt("ConversionTime"));
      }
   }

   default boolean cure(ServerLevel world) {
      Mob cast = this.witherstormmod$cast();
      WitherSickened.Data data = this.getData();
      EntityType<?> original = this.getOriginalType();
      if (original != null) {
         @SuppressWarnings("unchecked") Mob entity = (Mob)cast.convertTo((EntityType<? extends Mob>)original, false);
         if (entity != null) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
               ItemStack stack = cast.getItemBySlot(slot);
               if (!stack.isEmpty()) {
                  if (EnchantmentHelper.hasBindingCurse(stack)) {
                     entity.getSlot(slot.getIndex() + 300).set(stack);
                  } else {
                     double d0 = (double)this.getSickenedEquipmentDropChance(slot);
                     if (d0 > 1.0) {
                        cast.spawnAtLocation(stack);
                     }
                  }
               }
            }

            WorldTainting.copyExtraData(this.witherstormmod$cast(), entity);
            this.doExtraHandling(entity);
            if (data.conversionStarter != null) {
               Player player = world.getPlayerByUUID(data.conversionStarter);
               if (player instanceof ServerPlayer) {
                  WitherStormModCriteriaTriggers.CURED_SICKENED_MOB.trigger((ServerPlayer)player, cast, entity);
               }
            }

            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            cast.playSound(WitherStormModSoundEvents.MOB_CURED.get());
            ForgeEventFactory.onLivingConvert(cast, entity);
            return true;
         }
      }

      return false;
   }

   default void startConverting(@Nullable UUID uuid, int duration) {
      Mob cast = this.witherstormmod$cast();
      WitherSickened.Data data = this.getData();
      if (this.getOriginalType() != null) {
         data.conversionStarter = uuid;
         data.conversionTime = duration;
         this.setConverting(true);
         cast.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, Math.min(cast.level().getDifficulty().getId() - 1, 0)));
         this.playCureSound();
      }
   }

   default void playCureSound() {
      Mob cast = this.witherstormmod$cast();
      cast.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F + cast.getRandom().nextFloat(), cast.getRandom().nextFloat() * 0.7F + 0.3F);
   }

   default Mob witherstormmod$cast() {
      return (Mob)this;
   }

   default boolean sickenedCanAttack(LivingEntity entity) {
      return !(entity instanceof WitherSickened)
         && !(entity instanceof TentacleEntity)
         && !(entity instanceof WitherStormEntity)
         && !(entity instanceof WitheredSymbiontEntity)
         && !(entity instanceof WitherStormHeadEntity);
   }

   default void addWitherToTarget(Entity entity) {
      if (this.witherstormmod$cast().getMainHandItem().isEmpty() && entity instanceof LivingEntity living) {
         float f = this.witherstormmod$cast().level().getCurrentDifficultyAt(this.witherstormmod$cast().blockPosition()).getEffectiveDifficulty();
         living.addEffect(new MobEffectInstance(MobEffects.WITHER, 120 * (int)f), this.witherstormmod$cast());
      }
   }

   default void addPotentWitherToTarget(Entity entity) {
      if (this.witherstormmod$cast().getMainHandItem().isEmpty() && entity instanceof LivingEntity living) {
         float f = this.witherstormmod$cast().level().getCurrentDifficultyAt(this.witherstormmod$cast().blockPosition()).getEffectiveDifficulty();
         living.addEffect(new MobEffectInstance(MobEffects.WITHER, 75 * (int)f, 1), this.witherstormmod$cast());
      }
   }

   default void doExtraHandling(Mob mob) {
   }

   default void convertFrom(Mob mob) {
   }

   WitherSickened.Data getData();

   boolean isConverting();

   void setConverting(boolean var1);

   float getSickenedEquipmentDropChance(EquipmentSlot var1);

   default int getInfectedHealAmount() {
      return 8;
   }

   default boolean sickenedInfect(LivingEntity entity) {
      if (entity instanceof Mob mob && WorldTainting.getInstance().convertMob(mob, false)) {
         int healAmount = this.getInfectedHealAmount();
         if (healAmount > 0) {
            this.witherstormmod$cast().heal((float)healAmount);
         }

         return false;
      }

      return true;
   }

   default boolean sickenedCanBeHurt(DamageSource source, float amount) {
      if (source.getDirectEntity() instanceof AbstractHurtingProjectile || source.getDirectEntity() instanceof AbstractArrow) {
         Projectile projectile = (Projectile)source.getDirectEntity();
         if (projectile.getOwner() instanceof WitherSickened) {
            return false;
         }
      }

      return true;
   }

   @Nullable
   default EntityType<?> getOriginalType() {
      EntityType<?> storedOriginal = this.getData().getStoredOriginalType();
      return storedOriginal == null ? WorldTainting.getInstance().getOriginalTypeFromConvertedType(this.witherstormmod$cast().getType()) : storedOriginal;
   }


   public static class Data {
      protected int conversionTime;
      protected UUID conversionStarter;
      @Nullable
      protected EntityType<?> original;
      @Nullable
      protected CompoundTag originalData;

      @Nullable
      public final String encodeOriginalId() {
         if (this.original == null) {
            return null;
         } else {
            EntityType<?> type = this.original;
            ResourceLocation location = ForgeRegistries.ENTITY_TYPES.getKey(type);
            return type.canSerialize() && location != null ? location.toString() : null;
         }
      }

      public UUID getConversionStarter() {
         return this.conversionStarter;
      }

      public int getConversionTime() {
         return this.conversionTime;
      }

      public EntityType<?> getStoredOriginalType() {
         return this.original;
      }

      @Nullable
      protected CompoundTag getOriginalData() {
         return this.originalData;
      }

      public void setOriginal(EntityType<?> originalType, CompoundTag originalData) {
         this.original = originalType;
         this.originalData = originalData;
      }
   }
}
