package nonamecrackers2.witherstormmod.mixin;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.state.BlockState;
import nonamecrackers2.witherstormmod.common.entity.SickenedBee;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   targets = {"net.minecraft.world.entity.animal.Bee$BeePollinateGoal"}
)
public abstract class MixinBeePollinateGoal {
   @Unique
   @Final
   Bee this$0;

   @Inject(
      method = {"findNearbyFlower"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void witherstormmod$findSickenedBeeTaintableBlock_findNearbyFlower(CallbackInfoReturnable<Optional<BlockPos>> ci) {
      if (this.this$0 instanceof SickenedBee) {
         ci.setReturnValue(this.findNearestBlock(SickenedBee.TAINTABLE, 5.0));
      }
   }

   @Shadow
   protected abstract Optional<BlockPos> findNearestBlock(Predicate<BlockState> var1, double var2);
}
