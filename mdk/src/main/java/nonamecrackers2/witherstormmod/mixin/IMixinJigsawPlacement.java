package nonamecrackers2.witherstormmod.mixin;

import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({JigsawPlacement.class})
public interface IMixinJigsawPlacement {
   @Invoker("addPieces")
   static void invokeAddPieces(
      RandomState state,
      int depth,
      boolean flag,
      ChunkGenerator generator,
      StructureTemplateManager manager,
      LevelHeightAccessor accessor,
      RandomSource source,
      Registry<StructureTemplatePool> registry,
      PoolElementStructurePiece start,
      List<PoolElementStructurePiece> pieces,
      VoxelShape maxDistShape
   ) {
      throw new AssertionError();
   }
}
