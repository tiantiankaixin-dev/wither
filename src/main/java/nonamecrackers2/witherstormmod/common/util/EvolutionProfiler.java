package nonamecrackers2.witherstormmod.common.util;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class EvolutionProfiler {
   private final Int2ObjectMap<Integer> ticksToEvolve = new Int2ObjectOpenHashMap();
   private final List<Integer> consumedEntitiesPerSeconds = Lists.newArrayList();
   private double consumedEntitiesPerSecond;
   private int ticksSinceLastPhase;
   private int lastConsumedEntities;
   private boolean isProfiling;

   public void tick(WitherStormEntity storm) {
      this.ticksSinceLastPhase++;
      MinecraftServer server = storm.level().getServer();
      if (this.ticksToEvolve.containsKey(7)) {
         ObjectIterator size = this.ticksToEvolve.int2ObjectEntrySet().iterator();

         while (size.hasNext()) {
            Entry<Integer> entry = (Entry<Integer>)size.next();
            int phase = entry.getIntKey();
            int ticks = (Integer)entry.getValue();
            Component message = Component.literal(ticks + " ticks to evolve from " + (phase - 1) + " to " + phase).withStyle(ChatFormatting.GOLD);

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
               player.sendSystemMessage(message);
            }
         }

         this.isProfiling = false;
      }

      if (this.ticksSinceLastPhase % 20 == 0) {
         this.consumedEntitiesPerSeconds.add(storm.getConsumedEntities() - this.lastConsumedEntities);
         this.lastConsumedEntities = storm.getConsumedEntities();
         int size = this.consumedEntitiesPerSeconds.size();
         int sum = 0;

         for (int i : this.consumedEntitiesPerSeconds) {
            sum += i;
         }

         this.consumedEntitiesPerSecond = (double)sum / (double)size;
         if (this.consumedEntitiesPerSeconds.size() > 60) {
            this.consumedEntitiesPerSeconds.clear();

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
               player.sendSystemMessage(
                  Component.literal("Consumed entities per second for phase " + storm.getPhase() + ": " + this.consumedEntitiesPerSecond)
                     .withStyle(ChatFormatting.YELLOW)
               );
            }
         }
      }
   }

   public void onEvolve(WitherStormEntity storm) {
      int phase = storm.getPhase();
      ((java.util.Map<Integer,Integer>)this.ticksToEvolve).put(phase, this.ticksSinceLastPhase);
      Component message = Component.literal("Phase " + (phase - 1) + " to " + phase + " took " + this.ticksSinceLastPhase + " ticks")
         .withStyle(ChatFormatting.GOLD);

      for (ServerPlayer player : storm.level().getServer().getPlayerList().getPlayers()) {
         player.sendSystemMessage(message);
      }

      this.ticksSinceLastPhase = 0;
   }

   public void begin() {
      this.isProfiling = true;
      this.ticksToEvolve.clear();
      this.ticksSinceLastPhase = 0;
      this.consumedEntitiesPerSeconds.clear();
      this.consumedEntitiesPerSecond = 0.0;
   }

   public boolean isProfiling() {
      return this.isProfiling;
   }

   public double getConsumedEntitiesPerSecond() {
      return this.consumedEntitiesPerSecond;
   }

   public void save(CompoundTag tag) {
      tag.putBoolean("IsProfiling", this.isProfiling);
      tag.putInt("TicksSinceLastPhase", this.ticksSinceLastPhase);
      ListTag list = new ListTag();
      ObjectIterator var3 = this.ticksToEvolve.int2ObjectEntrySet().iterator();

      while (var3.hasNext()) {
         Entry<Integer> entry = (Entry<Integer>)var3.next();
         CompoundTag entryTag = new CompoundTag();
         entryTag.putInt("Phase", entry.getIntKey());
         entryTag.putInt("Ticks", (Integer)entry.getValue());
         list.add(entryTag);
      }

      tag.put("TicksToEvolve", list);
   }

   public void read(CompoundTag tag) {
      this.isProfiling = tag.getBoolean("IsProfiling");
      this.ticksSinceLastPhase = tag.getInt("TicksSinceLastPhase");
      ListTag list = tag.getList("TicksToEvolve", 10);
      this.ticksToEvolve.clear();

      for (int i = 0; i < list.size(); i++) {
         CompoundTag entryTag = list.getCompound(i);
         ((java.util.Map<Integer,Integer>)this.ticksToEvolve).put(entryTag.getInt("Phase"), entryTag.getInt("Ticks"));
      }
   }
}
