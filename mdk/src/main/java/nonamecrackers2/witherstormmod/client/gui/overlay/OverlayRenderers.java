package nonamecrackers2.witherstormmod.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;

public class OverlayRenderers {
   private static final ResourceLocation TRACTOR_BEAM_OUTLINE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/misc/tractor_beam_outline.png");

   public static void registerOverlays(RegisterGuiOverlaysEvent event) {
      event.registerAboveAll(
         "tractor_beam",
         (gui, stack, partialTicks, width, height) -> {
            Minecraft mc = Minecraft.getInstance();
            gui.setupOverlayRenderState(true, false);
            mc.player
               .getCapability(WitherStormModClientCapabilities.TRACTOR_BEAM_EFFECTS)
               .ifPresent(
                  effects -> {
                     if (effects.getTicksInTractorBeam() > 0
                        && (Boolean)WitherStormModConfig.CLIENT.renderTractorBeamOverlay.get()
                        && (Boolean)WitherStormModConfig.CLIENT.renderTractorBeams.get()) {
                        renderTextureOverlay(stack, TRACTOR_BEAM_OUTLINE, effects.getPercent(), width, height);
                     }
                  }
               );
         }
      );
      event.registerBelow(VanillaGuiOverlay.HOTBAR.id(), "blinding", (gui, stack, partialTicks, width, height) -> {
         Minecraft mc = Minecraft.getInstance();
         gui.setupOverlayRenderState(true, false);
         mc.player.getCapability(WitherStormModClientCapabilities.SCREEN_BLINDER).ifPresent(effects -> {
            float fade = effects.getFade(partialTicks);
            if (fade > 0.0F && (Boolean)WitherStormModConfig.CLIENT.blindingEffects.get()) {
               renderSolidOverlay(1.0F, 1.0F, 1.0F, fade, width, height);
            }
         });
      });
      event.registerAboveAll(
         "bosstheme_watermark",
         (gui, stack, partialTicks, width, height) -> {
            Minecraft mc = Minecraft.getInstance();
            gui.setupOverlayRenderState(true, false);
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
                        stack.drawString(gui.getFont(), watermark, 10, height - 9 - 8, 16777215 + (alpha << 24));
                     }
                  }
               );
         }
      );
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

   private static void renderSolidOverlay(float r, float g, float b, float alpha, int width, int height) {
      Tesselator tesselator = Tesselator.getInstance();
      BufferBuilder builder = tesselator.getBuilder();
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      builder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      builder.vertex(0.0, (double)height, 0.0).color(r, g, b, alpha).endVertex();
      builder.vertex((double)width, (double)height, 0.0).color(r, g, b, alpha).endVertex();
      builder.vertex((double)width, 0.0, 0.0).color(r, g, b, alpha).endVertex();
      builder.vertex(0.0, 0.0, 0.0).color(r, g, b, alpha).endVertex();
      tesselator.end();
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
   }
}
