package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({LightTexture.class})
public interface IMixinLightTexture {
   @Accessor
   DynamicTexture getLightTexture();
}
