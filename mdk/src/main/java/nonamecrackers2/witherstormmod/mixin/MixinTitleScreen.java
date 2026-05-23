package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.Util;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({TitleScreen.class})
public class MixinTitleScreen {
   private static final CubeMap WITHERSTORMMOD_CUBE_MAP = (CubeMap)Util.make(
      new CubeMap(new ResourceLocation("textures/gui/title/background/panorama")),
      map -> ((MixinCubeMap)map).getImages()[0] = new ResourceLocation("witherstormmod", "textures/gui/title/background/panorama_0.png")
   );
   private final PanoramaRenderer witherstormmodPanorama = new PanoramaRenderer(WITHERSTORMMOD_CUBE_MAP);

   @Redirect(
      method = {"render"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/PanoramaRenderer;render(FF)V"
      )
   )
   public void render_overridePanorama(PanoramaRenderer panorama, float partialTicks, float alpha) {
      if ((Boolean)WitherStormModConfig.CLIENT.customPanorama.get()) {
         this.witherstormmodPanorama.render(partialTicks, alpha);
      } else {
         panorama.render(partialTicks, alpha);
      }
   }
}
