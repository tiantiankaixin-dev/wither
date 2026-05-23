package nonamecrackers2.witherstormmod.common.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public abstract class TemplateFeature<C extends FeatureConfiguration> extends Feature<C> {
   public TemplateFeature(Codec<C> codec) {
      super(codec);
   }

   public boolean place(FeaturePlaceContext<C> context) {
      if (context.level() instanceof ServerLevel world) {
         StructureTemplateManager manager = world.getStructureManager();
         TemplateFeature.TemplateFeaturePiece piece = this.setupTemplate(context.origin(), context.random(), manager);
         return this.placeWithTemplate(world, context.chunkGenerator(), context.random(), context.origin(), (C)context.config(), piece);
      } else {
         return false;
      }
   }

   protected abstract TemplateFeature.TemplateFeaturePiece setupTemplate(BlockPos var1, RandomSource var2, StructureTemplateManager var3);

   protected abstract boolean placeWithTemplate(
      ServerLevel var1, ChunkGenerator var2, RandomSource var3, BlockPos var4, C var5, TemplateFeature.TemplateFeaturePiece var6
   );

   public static record TemplateFeaturePiece(StructureTemplate template, StructurePlaceSettings settings) {
   }
}
