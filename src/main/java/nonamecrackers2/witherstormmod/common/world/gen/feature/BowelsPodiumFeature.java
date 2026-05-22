package nonamecrackers2.witherstormmod.common.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import nonamecrackers2.witherstormmod.common.world.gen.feature.template.BlockIgnoreIrreplacableProcessor;

public class BowelsPodiumFeature extends TemplateFeature<NoneFeatureConfiguration> {
   private final ResourceLocation templateId;

   public BowelsPodiumFeature(Codec<NoneFeatureConfiguration> codec, ResourceLocation templateId) {
      super(codec);
      this.templateId = templateId;
   }

   @Override
   protected TemplateFeature.TemplateFeaturePiece setupTemplate(BlockPos pos, RandomSource random, StructureTemplateManager manager) {
      return new TemplateFeature.TemplateFeaturePiece(
         manager.getOrCreate(this.templateId),
         new StructurePlaceSettings()
            .setRotation(Rotation.getRandom(RandomSource.create(pos.asLong())))
            .setMirror(Mirror.NONE)
            .setRotationPivot(BlockPos.ZERO)
            .addProcessor(BlockIgnoreIrreplacableProcessor.AIR)
      );
   }

   public boolean placeWithTemplate(
      ServerLevel world,
      ChunkGenerator generator,
      RandomSource random,
      BlockPos pos,
      NoneFeatureConfiguration config,
      TemplateFeature.TemplateFeaturePiece piece
   ) {
      StructureTemplate template = piece.template();
      BoundingBox box = template.getBoundingBox(piece.settings(), pos);
      Vec3i center = box.getCenter();
      int xOffset = pos.getX() - center.getX();
      int zOffset = pos.getZ() - center.getZ();
      BlockPos newPos = pos.offset(xOffset, -box.getYSpan(), zOffset);
      template.placeInWorld(world, newPos, newPos, piece.settings(), random, 2);
      return true;
   }
}
