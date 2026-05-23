package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;
import net.neoforged.neoforge.common.capabilities.ICapabilitySerializable;
import net.neoforged.neoforge.common.capabilities.RegisterCapabilitiesEvent;
import // TODO_MIG: LazyOptional removed, new Capability API returns T or null;
import // TODO_MIG: AttachCapabilitiesEvent removed, use RegisterCapabilitiesEvent (see CAPABILITY_AUDIT.md);
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.capability.ChunkLoadingBlockEntities;
import nonamecrackers2.witherstormmod.common.capability.EntityCapability;
import nonamecrackers2.witherstormmod.common.capability.PlayerWitherStormData;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.capability.WitherStormAutoSpawner;
import nonamecrackers2.witherstormmod.common.capability.WitherStormBowelsManager;
import nonamecrackers2.witherstormmod.common.capability.WitherStormModChunkLoader;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;

public class WitherStormModCapabilities {
   public static final Capability<WitherStormModChunkLoader> CHUNK_LOADER = CapabilityManager.get(new CapabilityToken<WitherStormModChunkLoader>() {
   });
   public static final Capability<WitherSicknessTracker> WITHER_SICKNESS_TRACKER = CapabilityManager.get(new CapabilityToken<WitherSicknessTracker>() {
   });
   public static final Capability<PlayerWitherStormData> PLAYER_WITHER_STORM_DATA = CapabilityManager.get(new CapabilityToken<PlayerWitherStormData>() {
   });
   public static final Capability<WitherStormBowelsManager> BOWELS_MANAGER = CapabilityManager.get(new CapabilityToken<WitherStormBowelsManager>() {
   });
   public static final Capability<ChunkLoadingBlockEntities> CHUNK_LOADING_BLOCK_ENTITIES = CapabilityManager.get(
      new CapabilityToken<ChunkLoadingBlockEntities>() {
      }
   );
   public static final Capability<WitherStormAutoSpawner> WITHER_STORM_AUTO_SPAWNER = CapabilityManager.get(new CapabilityToken<WitherStormAutoSpawner>() {
   });

   public static void registerCapabilities(RegisterCapabilitiesEvent event) {
      event.register(WitherSicknessTracker.class);
      event.register(PlayerWitherStormData.class);
      event.register(WitherStormBowelsManager.class);
      event.register(ChunkLoadingBlockEntities.class);
      event.register(WitherStormModChunkLoader.class);
      event.register(WitherStormAutoSpawner.class);
   }

   public static void attachWorldCapabilities(AttachCapabilitiesEvent<Level> event) {
      Level world = (Level)event.getObject();
      if (!world.isClientSide) {
         final LazyOptional<ChunkLoadingBlockEntities> chunkLoadingBlockEntities = LazyOptional.of(() -> new ChunkLoadingBlockEntities((ServerLevel)world));
         event.addCapability(new ResourceLocation("witherstormmod", "chunk_loading_block_entities"), new ICapabilitySerializable<CompoundTag>() {
            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
               return cap == WitherStormModCapabilities.CHUNK_LOADING_BLOCK_ENTITIES ? chunkLoadingBlockEntities.cast() : null /* TODO_MIG: LazyOptional.empty() -> null */;
            }

            public CompoundTag serializeNBT() {
               return ((ChunkLoadingBlockEntities)chunkLoadingBlockEntities.orElse(null)).write();
            }

            public void deserializeNBT(CompoundTag nbt) {
               ((ChunkLoadingBlockEntities)chunkLoadingBlockEntities.orElse(null)).read(nbt);
            }
         });
         event.addListener(chunkLoadingBlockEntities::invalidate);
         final LazyOptional<WitherStormModChunkLoader> chunkLoader = LazyOptional.of(() -> new WitherStormModChunkLoader((ServerLevel)world));
         event.addCapability(new ResourceLocation("witherstormmod", "chunk_loader"), new ICapabilitySerializable<Tag>() {
            public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
               return capability == WitherStormModCapabilities.CHUNK_LOADER ? chunkLoader.cast() : null /* TODO_MIG: LazyOptional.empty() -> null */;
            }

            public Tag serializeNBT() {
               return ((WitherStormModChunkLoader)chunkLoader.orElse(null)).write();
            }

            public void deserializeNBT(Tag nbt) {
               ((WitherStormModChunkLoader)chunkLoader.orElse(null)).read((CompoundTag)nbt);
            }
         });
         event.addListener(chunkLoader::invalidate);
         if (world.dimension().location().equals(WitherStormMod.bowelsLocation())) {
            final LazyOptional<WitherStormBowelsManager> bowelsManager = LazyOptional.of(() -> new WitherStormBowelsManager((ServerLevel)world));
            event.addCapability(new ResourceLocation("witherstormmod", "bowels_manager"), new ICapabilitySerializable<Tag>() {
               public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
                  return capability == WitherStormModCapabilities.BOWELS_MANAGER ? bowelsManager.cast() : null /* TODO_MIG: LazyOptional.empty() -> null */;
               }

               public Tag serializeNBT() {
                  return ((WitherStormBowelsManager)bowelsManager.orElse(null)).write();
               }

               public void deserializeNBT(Tag nbt) {
                  ((WitherStormBowelsManager)bowelsManager.orElse(null)).read((CompoundTag)nbt);
               }
            });
            event.addListener(bowelsManager::invalidate);
         } else if (world.dimension().equals(Level.OVERWORLD)) {
            final LazyOptional<WitherStormAutoSpawner> autoSpawner = LazyOptional.of(() -> new WitherStormAutoSpawner((ServerLevel)world));
            event.addCapability(new ResourceLocation("witherstormmod", "auto_spawner"), new ICapabilitySerializable<CompoundTag>() {
               public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                  return cap == WitherStormModCapabilities.WITHER_STORM_AUTO_SPAWNER ? autoSpawner.cast() : null /* TODO_MIG: LazyOptional.empty() -> null */;
               }

               public CompoundTag serializeNBT() {
                  return ((WitherStormAutoSpawner)autoSpawner.orElse(null)).write();
               }

               public void deserializeNBT(CompoundTag nbt) {
                  ((WitherStormAutoSpawner)autoSpawner.orElse(null)).read(nbt);
               }
            });
            event.addListener(autoSpawner::invalidate);
         }
      }
   }

   public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
      Entity entity = (Entity)event.getObject();
      if (!(entity instanceof WitherSickened) && entity instanceof LivingEntity && (Boolean)WitherStormModConfig.SERVER.witherSicknessEnabled.get()) {
         LivingEntity living = (LivingEntity)entity;
         LazyOptional<WitherSicknessTracker> tracker = LazyOptional.of(() -> new WitherSicknessTracker(living));
         event.addCapability(
            new ResourceLocation("witherstormmod", "wither_sickness_tracker"),
            new EntityCapability.Serializable<WitherSicknessTracker, Capability<WitherSicknessTracker>>(WITHER_SICKNESS_TRACKER, tracker)
         );
      }

      if (entity instanceof Player player) {
         LazyOptional<PlayerWitherStormData> data = LazyOptional.of(() -> new PlayerWitherStormData(player));
         event.addCapability(
            new ResourceLocation("witherstormmod", "wither_storm_data"),
            new EntityCapability.Serializable<PlayerWitherStormData, Capability<PlayerWitherStormData>>(PLAYER_WITHER_STORM_DATA, data)
         );
      }
   }
}
