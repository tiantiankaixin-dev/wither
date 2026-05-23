package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import nonamecrackers2.witherstormmod.common.entity.SickenedBee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Bee.class})
public abstract class MixinBee extends Animal {
   private MixinBee() {
      super(null, null);
      throw new UnsupportedOperationException();
   }

   @Inject(
      method = {"isFlowerValid"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void witherstormmod$checkIfFlowerValidForSickenedBee_isFlowerValid(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
      if ((Bee)(Object)this instanceof SickenedBee) {
         ci.setReturnValue(SickenedBee.TAINTABLE.test(this.level().getBlockState(pos)));
      }
   }
}
