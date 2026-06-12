package nonamecrackers2.witherstormmod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.client.instancing.RenderBufferer;
import nonamecrackers2.witherstormmod.client.shader.PostProcessingShaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameRenderer.class})
public class MixinGameRenderer {
   @Inject(
      method = {"renderLevel"},
      at = {@At("TAIL")}
   )
   public void renderLevelTail(float partialTicks, long l, PoseStack stack, CallbackInfo ci) {
      PostProcessingShaders.INSTANCE.renderShaders(partialTicks);
   }

   @Inject(
      method = {"bobView"},
      at = {@At("TAIL")}
   )
   public void bobViewTail(PoseStack stack, float partialTicks, CallbackInfo ci) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.getCameraEntity() instanceof LocalPlayer player) {
         { var shaker = player.getData(WitherStormModClientCapabilities.CAMERA_SHAKER.get());
            float x = shaker.getXShake(partialTicks);
            float y = shaker.getYShake(partialTicks);
            stack.translate((double)Mth.sin((float)Math.toRadians((double)x)), (double)Mth.sin((float)Math.toRadians((double)y)), 0.0);
         }
      }
   }

   @Inject(
      method = {"close"},
      at = {@At("TAIL")}
   )
   public void witherstormmod$shutdownRenderBufferer_close(CallbackInfo ci) {
      RenderBufferer.INSTANCE.shutdown();
   }
}
