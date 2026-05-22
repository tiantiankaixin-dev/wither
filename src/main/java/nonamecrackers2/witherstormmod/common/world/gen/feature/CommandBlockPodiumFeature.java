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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import nonamecrackers2.witherstormmod.common.world.gen.feature.template.BlockIgnoreIrreplacableProcessor;

public class CommandBlockPodiumFeature extends TemplateFeature<NoneFeatureConfiguration> implements RemovableTemplateFeature<NoneFeatureConfiguration> {
   private final ResourceLocation templateId;

   public CommandBlockPodiumFeature(Codec<NoneFeatureConfiguration> codec, ResourceLocation templateId) {
      super(codec);
      this.templateId = templateId;
   }

   @Override
   protected TemplateFeature.TemplateFeaturePiece setupTemplate(BlockPos pos, RandomSource random, StructureTemplateManager manager) {
      StructureTemplate template = manager.getOrCreate(this.templateId);
      RandomSource newRandom = RandomSource.create(pos.asLong());
      StructurePlaceSettings settings = new StructurePlaceSettings()
         .setRotation(Rotation.getRandom(newRandom))
         .setMirror(Mirror.NONE)
         .setRotationPivot(BlockPos.ZERO)
         .addProcessor(BlockIgnoreIrreplacableProcessor.AIR);
      return new TemplateFeature.TemplateFeaturePiece(template, settings);
   }

   @Override
   public TemplateFeature.TemplateFeaturePiece setupRemovalTemplate(BlockPos pos, RandomSource random, StructureTemplateManager manager) {
      StructureTemplate template = manager.getOrCreate(this.templateId);
      RandomSource newRandom = RandomSource.create(pos.asLong());
      StructurePlaceSettings settings = new StructurePlaceSettings()
         .setRotation(Rotation.getRandom(newRandom))
         .setMirror(Mirror.NONE)
         .setRotationPivot(BlockPos.ZERO)
         .addProcessor(BlockIgnoreIrreplacableProcessor.AIR_REMOVE);
      return new TemplateFeature.TemplateFeaturePiece(template, settings);
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
      Vec3i center = template.getBoundingBox(piece.settings(), pos).getCenter();
      int xOffset = pos.getX() - center.getX();
      int yOffset = pos.getY() - center.getY();
      int zOffset = pos.getZ() - center.getZ();
      BlockPos newPos = pos.offset(xOffset, yOffset, zOffset);
      template.placeInWorld(world, newPos, newPos, piece.settings(), random, 2);
      return true;
   }
}
