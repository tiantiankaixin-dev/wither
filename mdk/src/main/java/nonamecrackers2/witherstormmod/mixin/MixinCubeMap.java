package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.client.renderer.CubeMap;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({CubeMap.class})
public interface MixinCubeMap {
   @Accessor
   ResourceLocation[] getImages();
}
