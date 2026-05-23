package nonamecrackers2.witherstormmod.mixin;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.PanoramaRenderer;
import nonamecrackers2.witherstormmod.client.util.PanoramaExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({PanoramaRenderer.class})
public abstract class MixinPanoramaRenderer implements PanoramaExtensions {
   @Shadow
   private float spin;
   @Nullable
   private Float xOffset;

   @Override
   public void setXOffset(float offset) {
      this.xOffset = offset;
   }

   @Override
   public void setStartingSpin(float spin) {
      this.spin = spin;
   }

   @ModifyConstant(
      method = {"render"},
      constant = {@Constant(
         floatValue = 10.0F
      )}
   )
   private float modifyXOffset(float offset) {
      return this.xOffset != null ? this.xOffset : offset;
   }
}
