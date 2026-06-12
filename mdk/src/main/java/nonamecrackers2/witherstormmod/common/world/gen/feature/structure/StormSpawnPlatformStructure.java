package nonamecrackers2.witherstormmod.common.world.gen.feature.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationStub;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.init.WitherStormModStructures;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBiomeTags;

public class StormSpawnPlatformStructure extends Structure {
   public static final Codec<StormSpawnPlatformStructure> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(settingsCodec(instance), BlockPos.CODEC.fieldOf("position").forGetter(structure -> structure.spawnPos))
            .apply(instance, StormSpawnPlatformStructure::new)
   );
   private static final ResourceLocation DESERT = ResourceLocation.fromNamespaceAndPath("witherstormmod", "desert_storm_spawn_platform");
   private static final ResourceLocation JUNGLE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "jungle_storm_spawn_platform");
   private static final ResourceLocation SAVANNA = ResourceLocation.fromNamespaceAndPath("witherstormmod", "savanna_storm_spawn_platform");
   private static final ResourceLocation TAIGA = ResourceLocation.fromNamespaceAndPath("witherstormmod", "taiga_storm_spawn_platform");
   private static final ResourceLocation SNOWY = ResourceLocation.fromNamespaceAndPath("witherstormmod", "snowy_storm_spawn_platform");
   private static final ResourceLocation RUINS = ResourceLocation.fromNamespaceAndPath("witherstormmod", "ruins_storm_spawn_platform");
   private static final ResourceLocation ORDER_TEMPLE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "order_temple_storm_spawn_platform");
   private static final ResourceLocation FOREST = ResourceLocation.fromNamespaceAndPath("witherstormmod", "forest_storm_spawn_platform");
   private static final ResourceLocation AUTO_SPAWN = WitherStormMod.id("auto_spawn_platform");
   private BlockPos spawnPos;

   public StormSpawnPlatformStructure(StructureSettings settings, BlockPos pos) {
      super(settings);
      this.spawnPos = pos;
   }

   private boolean isInChunk(ChunkPos pos) {
      return this.spawnPos.getX() >= pos.getMinBlockX()
         && this.spawnPos.getX() <= pos.getMaxBlockX()
         && this.spawnPos.getZ() >= pos.getMinBlockZ()
         && this.spawnPos.getZ() <= pos.getMaxBlockZ();
   }

   private static boolean isFeatureChunk(GenerationContext context, StormSpawnPlatformStructure structure) {
      return structure.isInChunk(context.chunkPos());
   }

   public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
      if (isFeatureChunk(context, this)) {
         Rotation rotation = Rotation.getRandom(context.random());
         ChunkPos chunk = context.chunkPos();
         BlockPos prePos = new BlockPos(chunk.getMinBlockX(), 0, chunk.getMinBlockZ());
         BlockPos pos = prePos.above(
            context.chunkGenerator().getBaseHeight(prePos.getX(), prePos.getZ(), Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState())
         );
         Holder<Biome> biome = context.biomeSource()
            .getNoiseBiome(
               QuartPos.fromBlock(pos.getX()), QuartPos.fromBlock(pos.getY()), QuartPos.fromBlock(pos.getZ()), context.randomState().sampler()
            );
         ResourceLocation structure = getStructureForBiome(biome, context.random());
         return Optional.of(
            new GenerationStub(pos, builder -> builder.addPiece(new StormSpawnPlatformStructure.Piece(context.structureTemplateManager(), structure, pos, rotation, 0)))
         );
      } else {
         return Optional.empty();
      }
   }

   private static ResourceLocation getStructureForBiome(Holder<Biome> biome, RandomSource random) {
      if ((Boolean)WitherStormModConfig.COMMON.autoSpawnWitherStorm.get()) {
         return AUTO_SPAWN;
      } else if (biome.is(WitherStormModBiomeTags.HAS_RUINS_STORM_SPAWN_PLATFORM) && random.nextFloat() <= 0.2F) {
         return RUINS;
      } else if (biome.is(WitherStormModBiomeTags.HAS_ORDER_TEMPLE_STORM_SPAWN_PLATFORM) && random.nextFloat() <= 0.25F) {
         return ORDER_TEMPLE;
      } else if (biome.is(WitherStormModBiomeTags.HAS_FOREST_STORM_SPAWN_PLATFORM) && random.nextFloat() <= 0.2F) {
         return FOREST;
      } else if (biome.is(WitherStormModBiomeTags.HAS_DESERT_STORM_SPAWN_PLATFORM)) {
         return DESERT;
      } else if (biome.is(WitherStormModBiomeTags.HAS_JUNGLE_STORM_SPAWN_PLATFORM)) {
         return JUNGLE;
      } else if (biome.is(WitherStormModBiomeTags.HAS_SAVANNA_STORM_SPAWN_PLATFORM)) {
         return SAVANNA;
      } else if (biome.is(WitherStormModBiomeTags.HAS_SNOWY_STORM_SPAWN_PLATFORM)) {
         return SNOWY;
      } else {
         return biome.is(WitherStormModBiomeTags.HAS_TAIGA_STORM_SPAWN_PLATFORM) ? TAIGA : WitherStormModStructures.STORM_SPAWN_PLATFORM.getId();
      }
   }

   public StructureType<?> type() {
      return (StructureType<?>)WitherStormModStructures.STORM_SPAWN_PLATFORM.get();
   }

   public static class Piece extends TemplateStructurePiece {
      @Nullable
      private BlockPos spawnPos;

      public Piece(StructureTemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation, int offsetY) {
         super(WitherStormModStructures.PLATFORM, 0, manager, location, location.toString(), makeSettings(rotation, pos), pos);
      }

      public Piece(StructureTemplateManager manager, CompoundTag nbt) {
         super(
            WitherStormModStructures.PLATFORM,
            nbt,
            manager,
            id -> {
               StructureTemplate template = manager.getOrCreate(id);
               return makeSettings(
                  Rotation.valueOf(nbt.getString("Rot")), new BlockPos(template.getSize().getX() / 2, 0, template.getSize().getZ() / 2)
               );
            }
         );
         if (nbt.contains("SpawnPos", 10)) {
            this.spawnPos = NbtUtils.readBlockPos(nbt, "SpawnPos");
         }
      }

      protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
         super.addAdditionalSaveData(context, tag);
         tag.putString("Rot", this.placeSettings.getRotation().name());
         if (this.spawnPos != null) {
            tag.put("SpawnPos", NbtUtils.writeBlockPos(this.spawnPos));
         }
      }

      private static StructurePlaceSettings makeSettings(Rotation rotation, BlockPos pos) {
         return new StructurePlaceSettings().setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
      }

      protected void handleDataMarker(String metadata, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {
         if (metadata.equals("spawn_position")) {
            this.spawnPos = pos;
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
         }
      }

      @Nullable
      public BlockPos getSpawnPos() {
         return this.spawnPos;
      }
   }
}
