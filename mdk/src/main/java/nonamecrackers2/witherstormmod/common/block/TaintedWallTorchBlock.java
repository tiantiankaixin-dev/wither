package nonamecrackers2.witherstormmod.common.block;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class TaintedWallTorchBlock extends WallTorchBlock {
   protected final Supplier<? extends ParticleOptions> flameParticle;

   public TaintedWallTorchBlock(Properties properties, Supplier<? extends ParticleOptions> flameParticle) {
      super(properties, null);
      this.flameParticle = flameParticle;
   }

   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      Direction direction = (Direction)state.getValue(FACING);
      double x = (double)pos.getX() + 0.5 + random.nextGaussian() * 0.1;
      double y = (double)pos.getY() + 0.7 + random.nextGaussian() * 0.1;
      double z = (double)pos.getZ() + 0.5 + random.nextGaussian() * 0.1;
      Direction opposite = direction.getOpposite();
      level.addParticle(ParticleTypes.SMOKE, x + 0.27 * (double)opposite.getStepX(), y + 0.22, z + 0.27 * (double)opposite.getStepZ(), 0.0, 0.0, 0.0);
      level.addParticle(this.flameParticle.get(), x + 0.27 * (double)opposite.getStepX(), y + 0.22, z + 0.27 * (double)opposite.getStepZ(), 0.0, 0.01, 0.0);
   }
}
