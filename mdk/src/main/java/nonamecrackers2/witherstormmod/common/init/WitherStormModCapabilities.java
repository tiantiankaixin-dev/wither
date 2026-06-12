package nonamecrackers2.witherstormmod.common.init;

import java.util.function.Supplier;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.capability.ChunkLoadingBlockEntities;
import nonamecrackers2.witherstormmod.common.capability.PlayerWitherStormData;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.capability.WitherStormAutoSpawner;
import nonamecrackers2.witherstormmod.common.capability.WitherStormBowelsManager;
import nonamecrackers2.witherstormmod.common.capability.WitherStormModChunkLoader;

public class WitherStormModCapabilities {
   public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, WitherStormMod.MODID);

   public static final Supplier<AttachmentType<WitherStormModChunkLoader>> CHUNK_LOADER = ATTACHMENT_TYPES.register(
      "chunk_loader", () -> AttachmentType.serializable(WitherStormModChunkLoader::new).build()
   );
   public static final Supplier<AttachmentType<WitherSicknessTracker>> WITHER_SICKNESS_TRACKER = ATTACHMENT_TYPES.register(
      "wither_sickness_tracker", () -> AttachmentType.serializable(WitherSicknessTracker::new).build()
   );
   public static final Supplier<AttachmentType<PlayerWitherStormData>> PLAYER_WITHER_STORM_DATA = ATTACHMENT_TYPES.register(
      "player_wither_storm_data", () -> AttachmentType.serializable(PlayerWitherStormData::new).build()
   );
   public static final Supplier<AttachmentType<WitherStormBowelsManager>> BOWELS_MANAGER = ATTACHMENT_TYPES.register(
      "bowels_manager", () -> AttachmentType.serializable(WitherStormBowelsManager::new).build()
   );
   public static final Supplier<AttachmentType<ChunkLoadingBlockEntities>> CHUNK_LOADING_BLOCK_ENTITIES = ATTACHMENT_TYPES.register(
      "chunk_loading_block_entities", () -> AttachmentType.serializable(ChunkLoadingBlockEntities::new).build()
   );
   public static final Supplier<AttachmentType<WitherStormAutoSpawner>> WITHER_STORM_AUTO_SPAWNER = ATTACHMENT_TYPES.register(
      "auto_spawner", () -> AttachmentType.serializable(WitherStormAutoSpawner::new).build()
   );
}
