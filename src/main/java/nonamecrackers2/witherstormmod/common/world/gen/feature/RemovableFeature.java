package nonamecrackers2.witherstormmod.common.world.gen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public interface RemovableFeature<C extends FeatureConfiguration> {
   boolean remove(WorldGenLevel var1, ChunkGenerator var2, RandomSource var3, BlockPos var4, C var5);
}
