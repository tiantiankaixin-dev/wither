package nonamecrackers2.witherstormmod.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEffects;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.UpdateEffectInstanceMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateWitherSicknessTrackerMessage;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModEntityTags;
import nonamecrackers2.witherstormmod.common.world.tainting.WorldTainting;

public class WitherSicknessTracker extends EntityCapability<WitherSicknessTracker, LivingEntity> {
   private int requiredProximityTicks = Math.max(240, (Integer)WitherStormModConfig.SERVER.requiredProximitySeconds.get() * 20);
   private int applicationDelay = Math.max(240, (Integer)WitherStormModConfig.SERVER.applicationDelay.get() * 20);
   private int cureDelay = Math.max(240, (Integer)WitherStormModConfig.SERVER.cureDelay.get() * 20);
   private int requiredContacts = Math.max(1, (Integer)WitherStormModConfig.SERVER.requiredContacts.get());
   private int multiplierDecreaseTime = 7200;
   private int contactsDecreaseTime = 9600;
   private int proximityTicksModifier;
   private int applicationDelayModifier;
   private int cureDelayModifier;
   private int proximityTicks;
   private int prevProximityTicks;
   private int delayTicks;
   private int prevDelayTicks;
   private int contacts;
   private int totalInfections;
   private int totalCures;
   private int amplifierDecreaseTicks;
   private int amplifier;
   private int contactsDecreaseTicks;
   private int cureDelayTicks;
   private int prevCureDelayTicks;
   private boolean isInfected;
   private boolean isBeingCured;
   private boolean isNearStorm;
   private boolean shouldUpdate;
   private boolean isActuallyImmune = true;

   public WitherSicknessTracker(LivingEntity entity) {
      super(entity);
      if (!entity.level().isClientSide) {
         this.randomizeModifiers();
      }
   }

   public void randomizeModifiers() {
      if (this.isLowImmunity()) {
         this.requiredProximityTicks = Math.max(240, (Integer)WitherStormModConfig.SERVER.lowImmuneRequiredProximitySeconds.get() * 20);
         this.applicationDelay = Math.max(240, (Integer)WitherStormModConfig.SERVER.lowImmuneApplicationDelay.get() * 20);
         this.cureDelay = Math.max(240, (Integer)WitherStormModConfig.SERVER.lowImmuneCureDelay.get() * 20);
         this.proximityTicksModifier = -this.entity
            .getRandom()
            .nextInt(Math.max(240, (Integer)WitherStormModConfig.SERVER.lowImmuneProximityModifierMax.get() * 20));
         this.applicationDelayModifier = -this.entity
            .getRandom()
            .nextInt(Math.max(240, (Integer)WitherStormModConfig.SERVER.lowImmuneApplicationModifierMax.get() * 20));
         this.cureDelayModifier = -this.entity
            .getRandom()
            .nextInt(Math.max(240, (Integer)WitherStormModConfig.SERVER.lowImmuneCureDelayModifierMax.get() * 20));
      } else {
         this.proximityTicksModifier = this.entity
            .getRandom()
            .nextInt(Math.max(240, (Integer)WitherStormModConfig.SERVER.proximitySecondsModifierMax.get() * 20));
         this.applicationDelayModifier = this.entity
            .getRandom()
            .nextInt(Math.max(240, (Integer)WitherStormModConfig.SERVER.applicationDelayModifierMax.get() * 20));
         this.cureDelayModifier = this.entity.getRandom().nextInt(Math.max(240, (Integer)WitherStormModConfig.SERVER.cureDelayModifierMax.get() * 20));
      }
   }

   public WitherSicknessTracker() {
   }

   public void copyFrom(WitherSicknessTracker tracker) {
      this.requiredProximityTicks = tracker.requiredProximityTicks;
      this.applicationDelay = tracker.applicationDelay;
      this.requiredContacts = tracker.requiredContacts;
      this.multiplierDecreaseTime = tracker.multiplierDecreaseTime;
      this.contactsDecreaseTime = tracker.contactsDecreaseTime;
      this.proximityTicksModifier = tracker.proximityTicksModifier;
      this.applicationDelayModifier = tracker.applicationDelayModifier;
      this.proximityTicks = tracker.proximityTicks;
      this.prevProximityTicks = tracker.prevProximityTicks;
      this.delayTicks = tracker.delayTicks;
      this.prevDelayTicks = tracker.prevDelayTicks;
      this.contacts = tracker.contacts;
      this.totalInfections = tracker.totalInfections;
      this.amplifierDecreaseTicks = tracker.amplifierDecreaseTicks;
      this.amplifier = tracker.amplifier;
      this.contactsDecreaseTicks = tracker.contactsDecreaseTicks;
      this.isInfected = tracker.isInfected;
      this.isNearStorm = tracker.isNearStorm;
      this.shouldUpdate = tracker.shouldUpdate;
      this.cureDelay = tracker.cureDelay;
      this.cureDelayModifier = tracker.cureDelayModifier;
      this.cureDelayTicks = tracker.cureDelayTicks;
      this.isBeingCured = tracker.isBeingCured;
      this.prevCureDelayTicks = tracker.prevCureDelayTicks;
      this.totalCures = tracker.totalCures;
      this.isActuallyImmune = tracker.isActuallyImmune;
   }

   public void copyFromMessage(UpdateWitherSicknessTrackerMessage message) {
      this.requiredProximityTicks = message.getRequiredProximityTicks();
      this.applicationDelay = message.getApplicationDelay();
      this.cureDelay = message.getCureDelay();
      this.proximityTicksModifier = message.getProximityTicksModifier();
      this.applicationDelayModifier = message.getApplicationDelayModifier();
      this.proximityTicks = message.getProximityTicks();
      this.delayTicks = message.getDelayTicks();
      this.contacts = message.getContacts();
      this.totalInfections = message.getTotalInfections();
      this.amplifierDecreaseTicks = message.getMultiplierDecreaseTicks();
      this.amplifier = message.getMultiplier();
      this.contactsDecreaseTicks = message.getContactsDecreaseTicks();
      this.isInfected = message.isInfected();
      this.isNearStorm = message.isNearStorm();
      this.cureDelayModifier = message.getCureDelayModifier();
      this.cureDelayTicks = message.getCureDelayTicks();
      this.isBeingCured = message.isBeingCured();
      this.totalCures = message.getTotalCures();
      this.isActuallyImmune = message.isActuallyImmune();
   }

   @Override
   public CompoundTag write() {
      CompoundTag compound = new CompoundTag();
      compound.putInt("ProximityTicks", this.getProximityTicks());
      compound.putInt("DelayTicks", this.getDelayTicks());
      compound.putInt("Contacts", this.getContacts());
      compound.putInt("MultiplierDecreaseTicks", this.getAmplifierDecreaseTicks());
      compound.putInt("Multiplier", this.getBaseMultiplier());
      compound.putInt("TotalInfections", this.getTotalInfections());
      compound.putBoolean("IsInfected", this.isInfected());
      compound.putInt("ProximityTicksModifier", this.getRequiredProximityTicksModifier());
      compound.putInt("ApplicationDelayModifier", this.getApplicationDelayModifier());
      compound.putInt("CureDelayModifier", this.getCureDelayModifier());
      compound.putInt("ContactsDecreaseTicks", this.getContactsDecreaseTicks());
      compound.putInt("CureDelayTicks", this.getCureDelayTicks());
      compound.putBoolean("IsBeingCured", this.isBeingCured());
      compound.putInt("TotalCures", this.getTotalCures());
      return compound;
   }

   @Override
   public void read(CompoundTag compound) {
      this.proximityTicks = compound.getInt("ProximityTicks");
      this.delayTicks = compound.getInt("DelayTicks");
      this.contacts = compound.getInt("Contacts");
      this.amplifierDecreaseTicks = compound.getInt("AmplifierDecreaseTicks");
      this.amplifier = compound.getInt("Amplifier");
      this.totalInfections = compound.getInt("TotalInfections");
      this.isInfected = compound.getBoolean("IsInfected");
      this.proximityTicksModifier = compound.getInt("ProximityTicksModifier");
      this.applicationDelayModifier = compound.getInt("ApplicationDelayModifier");
      this.cureDelayModifier = compound.getInt("CureDelayModifier");
      this.contactsDecreaseTicks = compound.getInt("ContactsDecreaseTicks");
      this.cureDelayTicks = compound.getInt("CureDelayTicks");
      this.isBeingCured = compound.getBoolean("IsBeingCured");
      this.totalCures = compound.getInt("TotalCures");
   }

   @Override
   public void tick() {
      if (!this.entity.level().isClientSide) {
         boolean flag = this.entity.getType().is(WitherStormModEntityTags.WITHER_SICKNESS_IMMUNE);
         if (this.isActuallyImmune != flag) {
            this.shouldUpdate = true;
            this.isActuallyImmune = flag;
         }

         if (this.entity.tickCount % 120 == 0) {
            this.shouldUpdate = true;
         }

         if (this.shouldUpdate) {
            UpdateWitherSicknessTrackerMessage message = new UpdateWitherSicknessTrackerMessage(this.entity);
            WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), message);
            this.shouldUpdate = false;
         }
      }

      if (!this.isActuallyImmune()) {
         this.prevProximityTicks = this.proximityTicks;
         this.prevDelayTicks = this.delayTicks;
         this.prevCureDelayTicks = this.cureDelayTicks;
         if (this.isNearStorm()) {
            if (this.proximityTicks < this.getRequiredProximityTicks()) {
               this.proximityTicks++;
            }
         } else {
            if (this.proximityTicks > 0) {
               this.proximityTicks--;
            }

            if (this.amplifierDecreaseTicks < this.getAmplifierDecreaseTime() && !this.isInfected) {
               this.amplifierDecreaseTicks++;
            } else {
               this.amplifierDecreaseTicks = 0;
               if (this.amplifier > 0) {
                  this.amplifier--;
               }
            }

            if (this.contactsDecreaseTicks < this.getContactsDecreaseTime()) {
               this.contactsDecreaseTicks++;
            } else {
               this.contactsDecreaseTicks = 0;
               if (this.contacts > 0) {
                  this.contacts--;
               }
            }
         }

         if (this.prevProximityTicks != this.proximityTicks && this.proximityTicks >= this.getRequiredProximityTicks()) {
            this.beginInfection();
         }

         if (this.isInfected()) {
            if (this.delayTicks < this.getApplicationDelay() && !this.isBeingCured()) {
               this.delayTicks++;
            }

            if (this.delayTicks >= this.getApplicationDelay() && !this.entity.level().isClientSide) {
               if (this.delayTicks != this.prevDelayTicks) {
                  this.infect();
               }

               MobEffectInstance effect = this.entity.getEffect((MobEffect)WitherStormModEffects.WITHER_SICKNESS.get());
               if (effect != null && effect.getDuration() < 7200) {
                  MobEffectInstance newEffect = new MobEffectInstance(
                     (MobEffect)WitherStormModEffects.WITHER_SICKNESS.get(), 12000, effect.getAmplifier(), false, false, true
                  );
                  effect.update(newEffect);
                  UpdateEffectInstanceMessage message = new UpdateEffectInstanceMessage(this.entity.getId(), newEffect, true);
                  WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), message);
               }
            }

            if (!this.entity.level().isClientSide
               && !this.entity.hasEffect((MobEffect)WitherStormModEffects.WITHER_SICKNESS.get())
               && this.delayTicks >= this.getApplicationDelay()) {
               this.setInfected(false);
               this.setProximityTicks(0);
            }

            if (this.isBeingCured()) {
               if (this.cureDelayTicks < this.getCureDelay()) {
                  this.cureDelayTicks++;
               }

               if (this.cureDelayTicks >= this.getCureDelay() && this.cureDelayTicks != this.prevCureDelayTicks) {
                  this.cure();
               }
            } else if (this.cureDelayTicks >= this.getCureDelay()) {
               this.cureDelayTicks = 0;
            }
         } else {
            this.delayTicks = 0;
         }
      }
   }

   public int getRequiredProximityTicks() {
      return Math.max(1200, this.requiredProximityTicks + this.proximityTicksModifier);
   }

   public int getRequiredContacts() {
      return this.requiredContacts;
   }

   public int getApplicationDelay() {
      return Math.max(1200, this.applicationDelay + this.applicationDelayModifier);
   }

   public int getProximityTicks() {
      return this.proximityTicks;
   }

   public int getDelayTicks() {
      return this.delayTicks;
   }

   public int getContacts() {
      return this.contacts;
   }

   public int getTotalInfections() {
      return this.totalInfections;
   }

   public void beginInfection() {
      if (!this.isActuallyImmune()) {
         this.shouldUpdate = true;
         this.isInfected = true;
         this.isBeingCured = false;
      }
   }

   public void setInfected(boolean infected) {
      if (!this.isActuallyImmune()) {
         if (infected != this.isInfected) {
            this.shouldUpdate = true;
         }

         this.isInfected = infected;
         if (infected) {
            this.isBeingCured = false;
         }
      }
   }

   public void infect() {
      if (!this.isBeingCured() && !this.isActuallyImmune() && !this.entity.isSpectator()) {
         if (this.entity instanceof Player player && player.isCreative()) {
            return;
         }

         this.shouldUpdate = true;
         this.totalInfections++;
         MobEffectInstance effect = new MobEffectInstance(
            (MobEffect)WitherStormModEffects.WITHER_SICKNESS.get(), 12000, this.getAmplifier(), false, false, true
         );
         this.entity.addEffect(effect);
         if ((Boolean)WitherStormModConfig.SERVER.increaseAmplifier.get()) {
            this.amplifier++;
         }
      }
   }

   public int getAmplifierDecreaseTime() {
      return this.multiplierDecreaseTime;
   }

   public int getAmplifierDecreaseTicks() {
      return this.amplifierDecreaseTicks;
   }

   public int getAmplifier() {
      return this.isConvertable() ? this.amplifier + 5 : this.amplifier;
   }

   public int getBaseMultiplier() {
      return this.amplifier;
   }

   public boolean isInfected() {
      return this.isInfected;
   }

   public void setProximityTicks(int amount) {
      if (!this.isActuallyImmune()) {
         if (amount != this.proximityTicks) {
            this.shouldUpdate = true;
         }

         this.proximityTicks = amount;
      }
   }

   public void setDelayTicks(int amount) {
      if (!this.isActuallyImmune()) {
         if (amount != this.delayTicks) {
            this.shouldUpdate = true;
         }

         this.delayTicks = amount;
      }
   }

   public int getRequiredProximityTicksModifier() {
      return this.proximityTicksModifier;
   }

   public int getApplicationDelayModifier() {
      return this.applicationDelayModifier;
   }

   public int getContactsDecreaseTime() {
      return this.contactsDecreaseTime;
   }

   public int getContactsDecreaseTicks() {
      return this.contactsDecreaseTicks;
   }

   public void countContact() {
      if (!this.isActuallyImmune()) {
         this.shouldUpdate = true;
         this.contacts++;
         if (this.contacts > this.requiredContacts) {
            this.beginInfection();
         }
      }
   }

   public void setContacts(int amount) {
      if (!this.isActuallyImmune()) {
         if (amount != this.contacts) {
            this.shouldUpdate = true;
         }

         this.contacts = amount;
      }
   }

   public void setContactDecreaseTicks(int amount) {
      if (!this.isActuallyImmune()) {
         if (amount != this.contactsDecreaseTicks) {
            this.shouldUpdate = true;
         }

         this.contactsDecreaseTicks = amount;
      }
   }

   public boolean isNearStorm() {
      return this.isNearStorm;
   }

   public void setNearStorm(boolean near) {
      if (!this.isActuallyImmune()) {
         if (near != this.isNearStorm) {
            this.shouldUpdate = true;
         }

         this.isNearStorm = near;
      }
   }

   public int getCureDelay() {
      return Math.max(1200, this.cureDelay + this.cureDelayModifier);
   }

   public int getCureDelayModifier() {
      return this.cureDelayModifier;
   }

   public int getCureDelayTicks() {
      return this.cureDelayTicks;
   }

   public boolean isBeingCured() {
      return this.isBeingCured;
   }

   public void cure() {
      if (this.isInfected() && !this.isActuallyImmune()) {
         this.shouldUpdate = true;
         this.entity.removeEffect((MobEffect)WitherStormModEffects.WITHER_SICKNESS.get());
         this.setInfected(false);
         this.setProximityTicks(0);
         this.setCureTicks(0);
         this.countCure();
         this.setBeingCured(false);
      }
   }

   public void beginCure() {
      if (!this.isActuallyImmune()) {
         this.shouldUpdate = true;
         this.isBeingCured = true;
      }
   }

   public int getTotalCures() {
      return this.totalCures;
   }

   public void countCure() {
      if (!this.isActuallyImmune()) {
         this.shouldUpdate = true;
         this.totalCures++;
      }
   }

   public void setBeingCured(boolean cured) {
      if (!this.isActuallyImmune()) {
         if (cured != this.isBeingCured) {
            this.shouldUpdate = true;
         }

         this.isBeingCured = cured;
      }
   }

   public void setCureTicks(int amount) {
      if (!this.isActuallyImmune()) {
         if (amount != this.cureDelayTicks) {
            this.shouldUpdate = true;
         }

         this.cureDelayTicks = amount;
      }
   }

   public boolean isLowImmunity() {
      return !this.entity.getType().is(WitherStormModEntityTags.HIGH_IMMUNITY);
   }

   public boolean isConvertable() {
      return WorldTainting.getInstance().canConvertMob(this.entity, true);
   }

   public boolean isActuallyImmune() {
      return this.isActuallyImmune;
   }

   public int getRawRequiredProximityTicks() {
      return this.requiredProximityTicks;
   }

   public int getRawApplicationDelay() {
      return this.applicationDelay;
   }

   public int getRawCureDelay() {
      return this.cureDelay;
   }
}
