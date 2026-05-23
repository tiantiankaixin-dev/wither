package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({PointedDripstoneBlock.class})
public interface MixinPointedDripstoneBlock {
   @Invoker
   static boolean callIsStalagmite(BlockState state) {
      throw new AssertionError();
   }

   @Invoker
   static void callSpawnFallingStalactite(BlockState state, ServerLevel level, BlockPos pos) {
      throw new AssertionError();
   }
}
