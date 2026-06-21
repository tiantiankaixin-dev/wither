package nonamecrackers2.witherstormmod.common.world.gen.feature.structure;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationStub;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nonamecrackers2.witherstormmod.common.init.WitherStormModStructures;
import nonamecrackers2.witherstormmod.mixin.IMixinJigsawPlacement;

public class BowelsStructure extends Structure {
   public static final MapCodec<BowelsStructure> CODEC = RecordCodecBuilder.mapCodec(
      builder -> builder.group(
               settingsCodec(builder),
               StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
               HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight)
            )
            .apply(builder, BowelsStructure::new)
   );
   private final Holder<StructureTemplatePool> startPool;
   private final HeightProvider startHeight;

   public BowelsStructure(StructureSettings settings, Holder<StructureTemplatePool> startPool, HeightProvider heightProvider) {
      super(settings);
      this.startPool = startPool;
      this.startHeight = heightProvider;
   }

   public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
      RegistryAccess access = context.registryAccess();
      ChunkGenerator generator = context.chunkGenerator();
      StructureTemplateManager manager = context.structureTemplateManager();
      LevelHeightAccessor heightAccessor = context.heightAccessor();
      WorldgenRandom random = context.random();
      Registry<StructureTemplatePool> registry = access.registryOrThrow(Registries.TEMPLATE_POOL);
      Rotation rotation = Rotation.NONE;
      StructureTemplatePool pool = (StructureTemplatePool)this.startPool.value();
      StructurePoolElement start = pool.getRandomTemplate(random);
      if (start == EmptyPoolElement.INSTANCE) {
         return Optional.empty();
      } else {
         ChunkPos chunk = context.chunkPos();
         int i = this.startHeight.sample(random, new WorldGenerationContext(generator, heightAccessor));
         BlockPos pos = new BlockPos(chunk.getMinBlockX(), i, chunk.getMinBlockZ());
         PoolElementStructurePiece startPiece = new PoolElementStructurePiece(
            manager, start, pos, start.getGroundLevelDelta(), rotation, start.getBoundingBox(manager, pos, rotation), LiquidSettings.IGNORE_WATERLOGGING
         );
         BoundingBox box = startPiece.getBoundingBox();
         int x = pos.getX() + box.getXSpan() / 2;
         int z = pos.getZ() + box.getZSpan() / 2;
         int y = pos.getY();
         int l = box.minY() + startPiece.getGroundLevelDelta();
         startPiece.move(0, y - l, 0);
         return Optional.of(
            new GenerationStub(
               new BlockPos(x, y, z),
               builder -> {
                  List<PoolElementStructurePiece> pieces = Lists.newArrayList();
                  pieces.add(startPiece);
                  AABB maxDistBox = new AABB(
                     (double)(x - 128), (double)(y - 128), (double)(z - 128), (double)(x + 128 + 1), (double)(y + 128 + 1), (double)(z + 128 + 1)
                  );
                  VoxelShape shape = Shapes.join(Shapes.create(maxDistBox), Shapes.create(AABB.of(box)), BooleanOp.ONLY_FIRST);
                  IMixinJigsawPlacement.invokeAddPieces(
                     context.randomState(),
                     8,
                     false,
                     generator,
                     manager,
                     heightAccessor,
                     random,
                     registry,
                     startPiece,
                     pieces,
                     shape,
                     PoolAliasLookup.EMPTY,
                     LiquidSettings.IGNORE_WATERLOGGING
                  );
                  pieces.forEach(piece -> {
                     piece.move(box.getXSpan() / -2, 0, box.getZSpan() / -2);
                     builder.addPiece(piece);
                  });
               }
            )
         );
      }
   }

   public StructureType<?> type() {
      return (StructureType<?>)WitherStormModStructures.BOWELS.get();
   }
}
