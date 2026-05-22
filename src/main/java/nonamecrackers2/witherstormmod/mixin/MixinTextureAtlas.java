package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.SpriteLoader.Preparations;
import nonamecrackers2.witherstormmod.client.util.TextureAtlasAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TextureAtlas.class})
public abstract class MixinTextureAtlas implements TextureAtlasAccessor {
   @Unique
   private int totalWidth;
   @Unique
   private int totalHeight;

   @Inject(
      method = {"upload"},
      at = {@At("TAIL")}
   )
   public void uploadTail(Preparations preperations, CallbackInfo ci) {
      this.totalWidth = preperations.width();
      this.totalHeight = preperations.height();
   }

   @Override
   public int getHeight() {
      return this.totalHeight;
   }

   @Override
   public int getWidth() {
      return this.totalWidth;
   }
}
