package nonamecrackers2.crackerslib.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({GameRenderer.class})
public interface MixinGameRendererAccessor {
   @Invoker("getFov")
   double crackerslib$getFov(Camera var1, float var2, boolean var3);

   @Accessor("zoom")
   float crackerslib$getZoom();

   @Accessor("zoomX")
   float crackerslib$getZoomX();

   @Accessor("zoomY")
   float crackerslib$getZoomY();
}
