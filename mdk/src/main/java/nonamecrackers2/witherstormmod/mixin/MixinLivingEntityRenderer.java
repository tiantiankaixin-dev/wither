package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntityRenderer.class})
public class MixinLivingEntityRenderer {
   @Inject(
      method = {"isEntityUpsideDown"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void isEntityUpsideDownHead(LivingEntity entity, CallbackInfoReturnable<Boolean> ci) {
      if (entity instanceof WitherStormEntity) {
         ci.setReturnValue(false);
      }
   }
}
