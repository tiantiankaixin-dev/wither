package nonamecrackers2.witherstormmod.common.entity;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.mixin.IMixinZombieVillager;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

public class SickenedVillager extends SickenedZombie implements VillagerDataHolder {
   public static final Map<VillagerProfession, ItemListing[]> SICKENED_TRADES = Util.make(
      Maps.newHashMap(),
      map -> {
         for (VillagerProfession profession : ForgeRegistries.VILLAGER_PROFESSIONS.getValues()) {
            if (profession != VillagerProfession.NONE && profession != VillagerProfession.NITWIT) {
               if (profession == VillagerProfession.CLERIC) {
                  map.put(profession, makeDefaultWitheredItems(new BasicItemListing(1, new ItemStack((ItemLike)WitherStormModItems.TAINTED_DUST.get()), 12, 2)));
               } else if (profession == VillagerProfession.FARMER) {
                  map.put(
                     profession,
                     makeDefaultWitheredItems(new BasicItemListing(16, new ItemStack((ItemLike)WitherStormModItems.GOLDEN_APPLE_STEW.get()), 1, 30))
                  );
               } else if (profession == VillagerProfession.MASON) {
                  map.put(profession, makeDefaultMobStatues());
               } else {
                  map.put(profession, makeDefaultWitheredItems());
               }
            }
         }
      }
   );
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final EntityDataAccessor<VillagerData> VILLAGER_DATA = SynchedEntityData.defineId(SickenedVillager.class, EntityDataSerializers.VILLAGER_DATA);
   @Nullable
   private Tag gossips;
   private CompoundTag tradeOffers;
   private int villagerXp;
   private final SickenedVillager.SickenedData data = new SickenedVillager.SickenedData();

   public SickenedVillager(EntityType<? extends SickenedVillager> type, Level world) {
      super(type, world);
      BuiltInRegistries.VILLAGER_PROFESSION.getRandom(this.random).ifPresent(prof -> this.setVillagerData(this.getVillagerData().setProfession((VillagerProfession)prof.value())));
   }

   private static ItemListing[] makeDefaultWitheredItems() {
      return new ItemListing[]{
         new BasicItemListing(1, new ItemStack((ItemLike)WitherStormModItems.WITHERED_FLESH.get()), 16, 5),
         new BasicItemListing(1, new ItemStack((ItemLike)WitherStormModItems.WITHERED_BONE.get()), 16, 5),
         new BasicItemListing(1, new ItemStack((ItemLike)WitherStormModItems.WITHERED_SPIDER_EYE.get()), 16, 5)
      };
   }

   private static ItemListing[] makeDefaultWitheredItems(ItemListing... and) {
      return (ItemListing[])ArrayUtils.addAll(makeDefaultWitheredItems(), and);
   }

   private static ItemListing[] makeDefaultMobStatues() {
      return new ItemListing[]{
         new BasicItemListing(2, new ItemStack((ItemLike)WitherStormModItems.TAINTED_ZOMBIE_SITTING.get()), 12, 20),
         new BasicItemListing(2, new ItemStack((ItemLike)WitherStormModItems.TAINTED_ZOMBIE_WALL.get()), 12, 20),
         new BasicItemListing(2, new ItemStack((ItemLike)WitherStormModItems.TAINTED_ZOMBIE_LYING.get()), 12, 20),
         new BasicItemListing(2, new ItemStack((ItemLike)WitherStormModItems.TAINTED_BONE_PILE.get()), 12, 20),
         new BasicItemListing(2, new ItemStack((ItemLike)WitherStormModItems.TAINTED_SKELETON_WALL.get()), 12, 20),
         new BasicItemListing(2, new ItemStack((ItemLike)WitherStormModItems.TAINTED_SKULL_CEILING.get()), 12, 20)
      };
   }

   private record BasicItemListing(int emeralds, ItemStack result, int maxUses, int xp) implements ItemListing {
      @Override
      public MerchantOffer getOffer(net.minecraft.world.entity.Entity entity, net.minecraft.util.RandomSource random) {
         return new MerchantOffer(new ItemCost(Items.EMERALD, this.emeralds), this.result.copy(), this.maxUses, this.xp, 0.05F);
      }
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      VillagerData.CODEC
         .encodeStart(NbtOps.INSTANCE, this.getVillagerData())
         .resultOrPartial(LOGGER::error)
         .ifPresent(tag -> compound.put("VillagerData", tag));
      if (this.tradeOffers != null) {
         compound.put("Offers", this.tradeOffers);
      }

      if (this.gossips != null) {
         compound.put("Gossips", this.gossips);
      }

      compound.putInt("Xp", this.villagerXp);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("VillagerData", 10)) {
         DataResult<VillagerData> result = VillagerData.CODEC.parse(new Dynamic(NbtOps.INSTANCE, compound.get("VillagerData")));
         result.resultOrPartial(LOGGER::error).ifPresent(this::setVillagerData);
      }

      if (compound.contains("Offers", 10)) {
         this.tradeOffers = compound.getCompound("Offers");
      }

      if (compound.contains("Gossips", 10)) {
         this.gossips = compound.getList("Gossips", 10);
      }

      if (compound.contains("Xp", 3)) {
         this.villagerXp = compound.getInt("Xp");
      }
   }

   @Override
   public void doExtraHandling(Mob mob) {
      Villager villager = (Villager)mob;
      villager.setVillagerData(this.getVillagerData());
      if (this.gossips != null) {
         villager.setGossips(this.gossips);
      }

      if (this.tradeOffers != null) {
         MerchantOffers.CODEC
            .parse(new Dynamic(NbtOps.INSTANCE, this.tradeOffers))
            .resultOrPartial(error -> LOGGER.error(error.toString()))
            .ifPresent(offers -> villager.setOffers((MerchantOffers)offers));
      }

      villager.setVillagerXp(this.villagerXp);
      if (this.getData().getConversionStarter() != null) {
         Player player = this.level().getPlayerByUUID(this.getData().getConversionStarter());
         ((ServerLevel)this.level()).onReputationEvent(ReputationEventType.ZOMBIE_VILLAGER_CURED, player, villager);
      }

      ItemListing[] listings = SICKENED_TRADES.get(villager.getVillagerData().getProfession());
      if (listings != null) {
         for (ItemListing listing : listings) {
            MerchantOffers offers = villager.getOffers();
            offers.add(listing.getOffer(villager, villager.getRandom()));
         }
      }
   }

   @Override
   public void convertFrom(Mob mob) {
      if (mob instanceof VillagerDataHolder holder) {
         this.setVillagerData(holder.getVillagerData());
      }

      if (mob instanceof Villager villager) {
         this.setGossips((Tag)villager.getGossips().store(NbtOps.INSTANCE));
         MerchantOffers.CODEC.encodeStart(NbtOps.INSTANCE, villager.getOffers()).resultOrPartial(LOGGER::error).ifPresent(tag -> {
            if (tag instanceof CompoundTag compound) {
               this.setTradeOffers(compound);
            }
         });
         this.setVillagerXp(villager.getVillagerXp());
      } else if (mob instanceof ZombieVillager villager) {
         IMixinZombieVillager mixinVillager = (IMixinZombieVillager)villager;
         this.setGossips(mixinVillager.getGossips());
         this.setTradeOffers(mixinVillager.getTradeOffers());
         this.setVillagerXp(villager.getVillagerXp());
      }
   }

   public SoundEvent getAmbientSound() {
      return SoundEvents.ZOMBIE_VILLAGER_AMBIENT;
   }

   public SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.ZOMBIE_VILLAGER_HURT;
   }

   public SoundEvent getDeathSound() {
      return SoundEvents.ZOMBIE_VILLAGER_DEATH;
   }

   public SoundEvent getStepSound() {
      return SoundEvents.ZOMBIE_VILLAGER_STEP;
   }

   public void setTradeOffers(CompoundTag tag) {
      this.tradeOffers = tag;
   }

   public void setTradeOffers(MerchantOffers offers) {
      MerchantOffers.CODEC.encodeStart(NbtOps.INSTANCE, offers).resultOrPartial(LOGGER::error).ifPresent(tag -> {
         if (tag instanceof CompoundTag compound) {
            this.setTradeOffers(compound);
         }
      });
   }

   public void setGossips(Tag tag) {
      this.gossips = tag;
   }

   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance instance, MobSpawnType reason, SpawnGroupData data) {
      this.setVillagerData(this.getVillagerData().setType(VillagerType.byBiome(world.getBiome(this.blockPosition()))));
      return super.finalizeSpawn(world, instance, reason, data);
   }

   public VillagerData getVillagerData() {
      return (VillagerData)this.entityData.get(VILLAGER_DATA);
   }

   public void setVillagerData(VillagerData data) {
      VillagerData villagerdata = this.getVillagerData();
      if (villagerdata.getProfession() != data.getProfession()) {
         this.tradeOffers = null;
      }

      this.entityData.set(VILLAGER_DATA, data);
   }

   public int getVillagerXp() {
      return this.villagerXp;
   }

   public void setVillagerXp(int xp) {
      this.villagerXp = xp;
   }

   @Override
   public WitherSickened.Data getData() {
      return this.data;
   }

   @Override
   public boolean removeWhenFarAway(double dist) {
      return !this.isConverting() && this.villagerXp == 0;
   }

   public static class SickenedData extends WitherSickened.Data {
      @Override
      public EntityType<?> getStoredOriginalType() {
         return EntityType.VILLAGER;
      }

      @Override
      public void setOriginal(EntityType<?> originalType, CompoundTag originalData) {
      }
   }
}
