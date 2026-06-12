package nonamecrackers2.witherstormmod.common.capability;


import net.neoforged.neoforge.event.tick.LevelTickEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.icu.impl.locale.XCldrStub.ImmutableMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.ChunkLoader;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.util.WitherStormModNBTUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WitherStormModChunkLoader {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final TicketType<ChunkPos> WITHER_STORM = TicketType.create("witherstormmod:wither_storm", Comparator.comparingLong(ChunkPos::toLong));
   public static final TicketType<ChunkPos> LOAD = TicketType.create("witherstormmod:load", Comparator.comparingLong(ChunkPos::toLong), 100);
   private final ServerLevel level;
   private final Map<UUID, WitherStormModChunkLoader.Instance> instances = Maps.newHashMap();
   private List<ChunkPos> lastKnownPositions = Lists.newArrayList();
   private boolean loadedLastKnown;

   public WitherStormModChunkLoader(ServerLevel level) {
      this.level = level;
   }

   @Nullable
   public WitherStormModChunkLoader.Instance getInstance(UUID id) {
      return this.instances.get(id);
   }

   public Map<UUID, WitherStormModChunkLoader.Instance> getInstances() {
      return ImmutableMap.copyOf(this.instances);
   }

   public boolean hasChunkLoaders() {
      return !this.instances.isEmpty();
   }

   public void refreshAllLoaders() {
      for (WitherStormModChunkLoader.Instance instance : this.instances.values()) {
         instance.unload();
         instance.load();
         LOGGER.debug("Refreshed chunk loaders");
      }
   }

   public void tick() {
      if (this.hasChunkLoaders()) {
         this.level.resetEmptyTime();
      }

      if (!this.loadedLastKnown) {
         int count = 0;

         for (ChunkPos pos : this.lastKnownPositions) {
            count++;
            this.level.getChunkSource().addRegionTicket(LOAD, pos, 2, pos);
         }

         LOGGER.debug("Loaded {} chunks for initial loading sequence in {}", count, this.level.dimension());
         this.loadedLastKnown = true;
      }

      for (Entity entity : this.level.getAllEntities()) {
         if (entity instanceof ChunkLoader) {
            ChunkLoader loader = (ChunkLoader)entity;
            if (loader.shouldLoad()) {
               WitherStormModChunkLoader.Instance instance = this.instances.computeIfAbsent(entity.getUUID(), id -> {
                  LOGGER.debug("A new chunk loading entity has entered the world: {}", entity);
                  ChunkPos pos = entity.chunkPosition();
                  return new WitherStormModChunkLoader.Instance(loader, pos, loader.loadRadius());
               });
               instance.updateCurrentPosition(entity.chunkPosition());
            }
         }
      }

      Iterator<Entry<UUID, WitherStormModChunkLoader.Instance>> iterator = this.instances.entrySet().iterator();

      while (iterator.hasNext()) {
         Entry<UUID, WitherStormModChunkLoader.Instance> entry = iterator.next();
         WitherStormModChunkLoader.Instance instance = entry.getValue();
         if (instance.loader.isStillValidForChunkLoading()) {
            if (!(Boolean)WitherStormModConfig.SERVER.shouldChunkLoadWhenNoPlayers.get() && this.level.getServer().getPlayerCount() <= 0) {
               if (!instance.needsInit) {
                  instance.unload();
                  instance.needsInit = true;
               }
            } else if (instance.checkAndClearNeedsInit()) {
               instance.load();
            }
         } else {
            instance.unload();
            iterator.remove();
         }
      }
   }

   public CompoundTag write() {
      CompoundTag tag = new CompoundTag();
      if (!this.instances.isEmpty()) {
         tag.put(
            "LastKnownPositions",
            WitherStormModNBTUtil.writeChunkPosList(this.instances.values().stream().map(WitherStormModChunkLoader.Instance::getPos).toList())
         );
      }

      return tag;
   }

   public void read(CompoundTag tag) {
      if (tag.contains("LastKnownPositions")) {
         this.lastKnownPositions = WitherStormModNBTUtil.readChunkPosList(tag.getCompound("LastKnownPositions"));
      }
   }

   public static class Events {
      private Events() {
      }

      @SubscribeEvent
      public static void onLevelTick(LevelTickEvent.Post event) {
         event.getLevel().getData(WitherStormModCapabilities.CHUNK_LOADER.get()).tick();
      }
   }

   public class Instance {
      private ChunkPos current;
      private int radius;
      private boolean needsInit = true;
      private final ChunkLoader loader;

      public Instance(ChunkLoader loader, ChunkPos current, int radius) {
         this.loader = loader;
         this.current = current;
         this.radius = radius;
      }

      public int getRadius() {
         return this.radius;
      }

      public ChunkPos getPos() {
         return this.current;
      }

      public void updateCurrentPosition(ChunkPos pos) {
         if (!this.current.equals(pos)) {
            this.unload();
            this.current = pos;
            this.load();
         }
      }

      public void unload() {
         WitherStormModChunkLoader.LOGGER.debug("Unloading chunk {}", this.current);
         WitherStormModChunkLoader.this.level
            .getChunkSource()
            .removeRegionTicket(WitherStormModChunkLoader.WITHER_STORM, this.current, this.radius, this.current, true);
      }

      public void load() {
         WitherStormModChunkLoader.LOGGER.debug("Loading chunk {} with radius {}", this.current, this.radius);
         WitherStormModChunkLoader.this.level.getChunkSource().addRegionTicket(WitherStormModChunkLoader.WITHER_STORM, this.current, this.radius, this.current, true);
      }

      public boolean checkAndClearNeedsInit() {
         boolean flag = this.needsInit;
         if (flag) {
            this.needsInit = false;
         }

         return flag;
      }
   }
}
