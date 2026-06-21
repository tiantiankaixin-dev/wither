package nonamecrackers2.witherstormmod.common.block;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class TaintedTorchBlock extends TorchBlock {
   protected final Supplier<? extends ParticleOptions> flameParticle;

   public TaintedTorchBlock(Properties properties, Supplier<? extends ParticleOptions> flameParticle) {
      super(ParticleTypes.FLAME, properties);
      this.flameParticle = flameParticle;
   }

   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      double x = (double)pos.getX() + 0.5 + random.nextGaussian() * 0.1;
      double y = (double)pos.getY() + 0.7 + random.nextGaussian() * 0.1;
      double z = (double)pos.getZ() + 0.5 + random.nextGaussian() * 0.1;
      level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
      level.addParticle(this.flameParticle.get(), x, y, z, 0.0, 0.01, 0.0);
   }
}
