package nonamecrackers2.witherstormmod.common.world.gen.feature.template;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class BlockIgnoreIrreplacableProcessor extends BlockIgnoreProcessor {
   public static final BlockIgnoreIrreplacableProcessor AIR = new BlockIgnoreIrreplacableProcessor(ImmutableList.of(Blocks.AIR), false);
   public static final BlockIgnoreIrreplacableProcessor AIR_REMOVE = new BlockIgnoreIrreplacableProcessor(ImmutableList.of(Blocks.AIR), true);
   private final boolean removeMode;

   public BlockIgnoreIrreplacableProcessor(List<Block> ignore, boolean remove) {
      super(ignore);
      this.removeMode = remove;
   }

   public StructureBlockInfo processBlock(
      LevelReader world, BlockPos pos, BlockPos pos1, StructureBlockInfo original, StructureBlockInfo copy, StructurePlaceSettings settings
   ) {
      BlockState previous = world.getBlockState(copy.pos());
      StructureBlockInfo info = copy;
      if (previous.is(BlockTags.FEATURES_CANNOT_REPLACE)) {
         info = null;
      }

      if (super.processBlock(world, pos, pos1, original, copy, settings) == null) {
         info = null;
      }

      if (this.removeMode && info != null) {
         info = new StructureBlockInfo(info.pos(), Blocks.AIR.defaultBlockState(), info.nbt());
      }

      return info;
   }
}
