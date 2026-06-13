package nonamecrackers2.witherstormmod.mixin;

import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import nonamecrackers2.witherstormmod.client.instancing.RenderBufferer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LevelRenderer.class})
public abstract class MixinLevelRenderer {
   @Shadow
   @Nullable
   private ClientLevel level;

   @Inject(
      method = {"allChanged"},
      at = {@At("TAIL")}
   )
   public void allChangedTail(CallbackInfo ci) {
      if (this.level() != null) {
         RenderBufferer.INSTANCE.levelReload();
      }
   }
}
