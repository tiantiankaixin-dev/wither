package nonamecrackers2.witherstormmod.common.world.gen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public interface RemovableTemplateFeature<C extends FeatureConfiguration> extends RemovableFeature<C> {
   @Override
   default boolean remove(WorldGenLevel reader, ChunkGenerator generator, RandomSource random, BlockPos pos, C config) {
      if (reader instanceof ServerLevel world) {
         StructureTemplateManager manager = world.getStructureManager();
         TemplateFeature.TemplateFeaturePiece piece = this.setupRemovalTemplate(pos, random, manager);
         return this.placeWithTemplate(world, generator, random, pos, config, piece);
      } else {
         return false;
      }
   }

   TemplateFeature.TemplateFeaturePiece setupRemovalTemplate(BlockPos var1, RandomSource var2, StructureTemplateManager var3);

   boolean placeWithTemplate(ServerLevel var1, ChunkGenerator var2, RandomSource var3, BlockPos var4, C var5, TemplateFeature.TemplateFeaturePiece var6);
}
