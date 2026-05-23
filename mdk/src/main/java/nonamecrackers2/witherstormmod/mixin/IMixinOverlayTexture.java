package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({OverlayTexture.class})
public interface IMixinOverlayTexture {
   @Accessor
   DynamicTexture getTexture();
}
