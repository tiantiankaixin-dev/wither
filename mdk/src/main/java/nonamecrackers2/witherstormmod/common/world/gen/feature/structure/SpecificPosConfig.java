package nonamecrackers2.witherstormmod.common.world.gen.feature.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class SpecificPosConfig implements FeatureConfiguration {
   public static final Codec<SpecificPosConfig> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(BlockPos.CODEC.fieldOf("position").forGetter(config -> config.pos)).apply(instance, SpecificPosConfig::new)
   );
   private final BlockPos pos;

   public SpecificPosConfig(BlockPos pos) {
      this.pos = pos;
   }

   public boolean isInChunk(ChunkPos pos) {
      return this.pos.getX() >= pos.getMinBlockX()
         && this.pos.getX() <= pos.getMaxBlockX()
         && this.pos.getZ() >= pos.getMinBlockZ()
         && this.pos.getZ() <= pos.getMaxBlockZ();
   }

   public BlockPos getPosition() {
      return this.pos;
   }
}
