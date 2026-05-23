package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PigRenderer.class})
public class MixinPigRenderer {
   @Unique
   private static final ResourceLocation REUBEN_TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/misc/reuben.png");

   @Inject(
      method = {"getTextureLocation(Lnet/minecraft/world/entity/animal/Pig;)Lnet/minecraft/resources/ResourceLocation;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getTextureLocationHead(Pig pig, CallbackInfoReturnable<ResourceLocation> ci) {
      if (pig.hasCustomName() && pig.getName().getString().equals("reuben")) {
         ci.setReturnValue(REUBEN_TEXTURE);
      }
   }
}
