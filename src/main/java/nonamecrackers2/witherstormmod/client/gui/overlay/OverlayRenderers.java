package nonamecrackers2.witherstormmod.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent.Chat;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;

public class OverlayRenderers {
   private static final ResourceLocation TRACTOR_BEAM_OUTLINE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/misc/tractor_beam_outline.png");

   public static void renderOverlays(Chat event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || mc.options.hideGui) {
         return;
      }

      GuiGraphics graphics = event.getGuiGraphics();
      int width = event.getWindow().getGuiScaledWidth();
      int height = event.getWindow().getGuiScaledHeight();
      mc.player
         .getCapability(WitherStormModClientCapabilities.TRACTOR_BEAM_EFFECTS)
         .ifPresent(
            effects -> {
               if (effects.getTicksInTractorBeam() > 0
                  && (Boolean)WitherStormModConfig.CLIENT.renderTractorBeamOverlay.get()
                  && (Boolean)WitherStormModConfig.CLIENT.renderTractorBeams.get()) {
                  renderTextureOverlay(graphics, TRACTOR_BEAM_OUTLINE, effects.getPercent(), width, height);
               }
            }
         );
      mc.player.getCapability(WitherStormModClientCapabilities.SCREEN_BLINDER).ifPresent(effects -> {
            float fade = effects.getFade(event.getPartialTick());
            if (fade > 0.0F && (Boolean)WitherStormModConfig.CLIENT.blindingEffects.get()) {
               renderSolidOverlay(graphics, 1.0F, 1.0F, 1.0F, fade, width, height);
            }
         });
      if (mc.level != null) {
         mc.level
            .getCapability(WitherStormModClientCapabilities.BOSS_THEME_MANAGER)
            .ifPresent(
               manager -> {
                  int time = manager.getWatermarkTime();
                  Component watermark = manager.getWatermark();
                  if (watermark != null && time > 0) {
                     float fade = 40.0F;
                     int alpha = Mth.floor(
                        Math.min(1.0F, (fade - ((float)time - ((float)manager.getWatermarkStartTime() - fade))) / fade)
                           * Math.min(1.0F, (float)time / fade)
                           * 255.0F
                     );
                     graphics.drawString(mc.gui.getFont(), watermark, 10, height - 9 - 8, 16777215 + (alpha << 24));
                  }
               }
            );
      }
   }

   private static void renderTextureOverlay(GuiGraphics graphics, ResourceLocation location, float alpha, int width, int height) {
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      graphics.setColor(1.0F, 1.0F, 1.0F, alpha);
      graphics.blit(location, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private static void renderSolidOverlay(GuiGraphics graphics, float r, float g, float b, float alpha, int width, int height) {
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.defaultBlendFunc();
      int color = (Mth.clamp((int)(alpha * 255.0F), 0, 255) << 24)
         | (Mth.clamp((int)(r * 255.0F), 0, 255) << 16)
         | (Mth.clamp((int)(g * 255.0F), 0, 255) << 8)
         | Mth.clamp((int)(b * 255.0F), 0, 255);
      graphics.fill(0, 0, width, height, color);
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
   }
}
