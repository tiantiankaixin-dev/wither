package nonamecrackers2.witherstormmod.common.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class PlayerWitherStormData extends EntityCapability<PlayerWitherStormData, Player> {
   private static final int AREA_CHECK_INTERVAL = 80;
   private List<PlayerWitherStormData.Snapshot> data = new ArrayList<>();
   private int invulnerableTime;
   private boolean hasActivatedSuperBeacon;
   private UUID killedByStorm;
   private int nextAreaCheck;
   private boolean isInAnOpenArea;

   public PlayerWitherStormData(Player player) {
      super(player);
   }

   public PlayerWitherStormData() {
   }

   @Override
   public void tick() {
      for (int i = 0; i < this.data.size(); i++) {
         PlayerWitherStormData.Snapshot snapshot = this.data.get(i);
         snapshot.tick();
         if (snapshot.shouldRemove) {
            this.data.remove(i);
         }
      }

      if (this.invulnerableTime > 0) {
         this.invulnerableTime--;
      }

      if (this.nextAreaCheck > 0) {
         this.nextAreaCheck--;
      }

      if (this.nextAreaCheck == 0) {
         this.isInAnOpenArea = WorldUtil.isInAnOpenArea(this.entity);
         this.nextAreaCheck = 80;
      }
   }

   @Override
   public CompoundTag write() {
      CompoundTag compound = new CompoundTag();
      ListTag list = new ListTag();

      for (PlayerWitherStormData.Snapshot snapshot : this.data) {
         list.add(snapshot.write());
      }

      compound.put("Data", list);
      compound.putInt("InvulnerableTime", this.invulnerableTime);
      compound.putBoolean("HasActivatedSuperBeacon", this.hasActivatedSuperBeacon);
      if (this.killedByStorm != null) {
         compound.putUUID("KilledByStorm", this.killedByStorm);
      }

      return compound;
   }

   @Override
   public void read(CompoundTag compound) {
      ListTag list = compound.getList("Data", 10);

      for (int i = 0; i < list.size(); i++) {
         CompoundTag snapshot = list.getCompound(i);
         this.data.add(PlayerWitherStormData.Snapshot.read(snapshot));
      }

      this.invulnerableTime = compound.getInt("InvulnerableTime");
      this.hasActivatedSuperBeacon = compound.getBoolean("HasActivatedSuperBeacon");
      if (compound.hasUUID("KilledByStorm")) {
         this.killedByStorm = compound.getUUID("KilledByStorm");
      }
   }

   public void copyFrom(PlayerWitherStormData instance) {
      this.data = instance.data;
      this.invulnerableTime = instance.invulnerableTime;
      this.killedByStorm = instance.killedByStorm;
   }

   public boolean hasRecentlySummonedSymbiont(WitherStormEntity entity) {
      PlayerWitherStormData.Snapshot snapshot = this.getFor(entity);
      return snapshot != null ? snapshot.getSymbiontSummonCooldown() > 0 : false;
   }

   public boolean hasKilledSymbiontRecently() {
      return this.invulnerableTime > 0;
   }

   public boolean hasChangedPhase(WitherStormEntity entity) {
      PlayerWitherStormData.Snapshot snapshot = this.getFor(entity);
      return snapshot != null ? snapshot.getLastPhase() != entity.getPhase() : true;
   }

   public void markSummonedSymbiont(WitherStormEntity entity) {
      PlayerWitherStormData.Snapshot snapshot = this.getFor(entity);
      int symbiontSummonCooldown = Mth.clamp((Integer)WitherStormModConfig.SERVER.playerSummoningDelay.get(), 1, 60) * 1200
         + this.entity.getRandom().nextInt(2400);
      if (snapshot != null) {
         snapshot.symbiontSummonCooldown = symbiontSummonCooldown;
         snapshot.lastPhase = entity.getPhase();
      } else {
         this.data.add(new PlayerWitherStormData.Snapshot(entity.getUUID(), entity.getPhase(), symbiontSummonCooldown));
      }
   }

   public void markKilledSymbiont(WitherStormEntity entity) {
      int symbiontSummonCooldown = Mth.clamp((Integer)WitherStormModConfig.SERVER.playerSummoningDelayOnKill.get(), 1, 60) * 1200
         + this.entity.getRandom().nextInt(24000);
      PlayerWitherStormData.Snapshot snapshot = this.getFor(entity);
      if (snapshot != null) {
         snapshot.symbiontSummonCooldown = symbiontSummonCooldown;
      } else {
         this.data.add(new PlayerWitherStormData.Snapshot(entity.getUUID(), entity.getPhase(), symbiontSummonCooldown));
      }
   }

   public void makeInvulnerable(int time) {
      this.invulnerableTime = time;
   }

   @Nullable
   public PlayerWitherStormData.Snapshot getFor(WitherStormEntity entity) {
      for (PlayerWitherStormData.Snapshot snapshot : this.data) {
         if (snapshot.lastStorm != null && snapshot.lastStorm.equals(entity.getUUID())) {
            return snapshot;
         }
      }

      return null;
   }

   public boolean hasActivatedSuperBeacon() {
      return this.hasActivatedSuperBeacon;
   }

   public void setActivatedSuperBeacon(boolean flag) {
      this.hasActivatedSuperBeacon = flag;
   }

   public UUID getKilledByStorm() {
      return this.killedByStorm;
   }

   public void setKilledByStorm(@Nullable UUID id) {
      this.killedByStorm = id;
   }

   public boolean isInAnOpenArea() {
      return this.isInAnOpenArea;
   }

   public static class Snapshot {
      @Nullable
      private UUID lastStorm;
      private int lastPhase;
      private int symbiontSummonCooldown;
      public boolean shouldRemove;

      public Snapshot(@Nullable UUID lastStorm, int lastPhase, int cooldown) {
         this.lastStorm = lastStorm;
         this.lastPhase = lastPhase;
         this.symbiontSummonCooldown = cooldown;
      }

      @Nullable
      public UUID getLastStorm() {
         return this.lastStorm;
      }

      public int getLastPhase() {
         return this.lastPhase;
      }

      public int getSymbiontSummonCooldown() {
         return this.symbiontSummonCooldown;
      }

      public void tick() {
         if (this.symbiontSummonCooldown > 0) {
            this.symbiontSummonCooldown--;
         } else {
            this.shouldRemove = true;
         }
      }

      public CompoundTag write() {
         CompoundTag data = new CompoundTag();
         if (this.lastStorm != null) {
            data.putUUID("Summoner", this.lastStorm);
         }

         data.putInt("SummonerPhase", this.lastPhase);
         data.putInt("SummoningCooldown", this.symbiontSummonCooldown);
         return data;
      }

      public static PlayerWitherStormData.Snapshot read(CompoundTag compound) {
         UUID lastStorm = null;
         if (compound.contains("Summoner")) {
            lastStorm = compound.getUUID("Summoner");
         }

         int lastPhase = compound.getInt("SummonerPhase");
         int summoningCooldown = compound.getInt("SummoningCooldown");
         return new PlayerWitherStormData.Snapshot(lastStorm, lastPhase, summoningCooldown);
      }

      @Override
      public String toString() {
         return "Snapshot[UUID=" + this.lastStorm + ", lastPhase" + this.lastPhase + ", summoningCooldown" + this.symbiontSummonCooldown + "]";
      }
   }
}
