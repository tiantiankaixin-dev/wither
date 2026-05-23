package nonamecrackers2.witherstormmod.common.world.gen.feature.template;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class BlockReplaceProcessor extends StructureProcessor {
   public static final Codec<BlockReplaceProcessor> CODEC = BlockState.CODEC
      .xmap(BlockStateBase::getBlock, Block::defaultBlockState)
      .listOf()
      .fieldOf("blocks")
      .xmap(BlockReplaceProcessor::new, proc -> proc.toReplace)
      .codec();
   private final ImmutableList<Block> toReplace;

   public BlockReplaceProcessor(List<Block> blocks) {
      this.toReplace = ImmutableList.copyOf(blocks);
   }

   public StructureBlockInfo processBlock(
      LevelReader reader, BlockPos pos, BlockPos pos1, StructureBlockInfo original, StructureBlockInfo copy, StructurePlaceSettings settings
   ) {
      BlockState prev = reader.getBlockState(copy.pos());
      return this.toReplace.contains(prev.getBlock()) ? copy : null;
   }

   protected StructureProcessorType<?> getType() {
      return null;
   }
}
