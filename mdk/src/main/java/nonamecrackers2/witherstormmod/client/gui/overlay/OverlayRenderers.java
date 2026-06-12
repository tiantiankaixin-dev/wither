package nonamecrackers2.witherstormmod.client.gui.overlay;

import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import nonamecrackers2.witherstormmod.client.capability.PlayerTractorBeamEffects;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;

public class OverlayRenderers {
   private static final ResourceLocation TRACTOR_BEAM_OUTLINE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/misc/tractor_beam_outline.png");

   public static void registerOverlays(RegisterGuiLayersEvent event) {
      event.registerAboveAll(
         ResourceLocation.fromNamespaceAndPath("witherstormmod", "tractor_beam"),
         (graphics, partialTick) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            PlayerTractorBeamEffects effects = mc.player.getData(WitherStormModClientCapabilities.TRACTOR_BEAM_EFFECTS);
            if (effects.getTicksInTractorBeam() > 0
               && (Boolean)WitherStormModConfig.CLIENT.renderTractorBeamOverlay.get()
               && (Boolean)WitherStormModConfig.CLIENT.renderTractorBeams.get()) {
               int width = mc.getWindow().getGuiScaledWidth();
               int height = mc.getWindow().getGuiScaledHeight();
               renderTextureOverlay(graphics, TRACTOR_BEAM_OUTLINE, effects.getPercent(), width, height);
            }
         }
      );
      event.registerBelow(VanillaGuiLayers.HOTBAR, ResourceLocation.fromNamespaceAndPath("witherstormmod", "blinding"),
         (graphics, partialTick) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            var effects = mc.player.getData(WitherStormModClientCapabilities.SCREEN_BLINDER);
            float fade = effects.getFade(partialTick);
            if (fade > 0.0F && (Boolean)WitherStormModConfig.CLIENT.blindingEffects.get()) {
               int width = mc.getWindow().getGuiScaledWidth();
               int height = mc.getWindow().getGuiScaledHeight();
               renderSolidOverlay(1.0F, 1.0F, 1.0F, fade, width, height);
            }
         }
      );
      event.registerAboveAll(
         ResourceLocation.fromNamespaceAndPath("witherstormmod", "bosstheme_watermark"),
         (graphics, partialTick) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;
            var manager = mc.level.getData(WitherStormModClientCapabilities.BOSS_THEME_MANAGER);
            int time = manager.getWatermarkTime();
            Component watermark = manager.getWatermark();
            if (watermark != null && time > 0) {
               int width = mc.getWindow().getGuiScaledWidth();
               int height = mc.getWindow().getGuiScaledHeight();
               float fadeDuration = 40.0F;
               int alpha = Mth.floor(
                  Math.min(1.0F, (fadeDuration - ((float)time - ((float)manager.getWatermarkStartTime() - fadeDuration))) / fadeDuration)
                     * Math.min(1.0F, (float)time / fadeDuration)
                     * 255.0F
               );
               graphics.drawString(mc.font, watermark, 10, height - 9 - 8, 16777215 + (alpha << 24));
            }
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
      BufferBuilder builder = tesselator;
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      builder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      builder.addVertex(0.0, (double)height, 0.0).setColor(r, g, b, alpha);
      builder.addVertex((double)width, (double)height, 0.0).setColor(r, g, b, alpha);
      builder.addVertex((double)width, 0.0, 0.0).setColor(r, g, b, alpha);
      builder.addVertex(0.0, 0.0, 0.0).setColor(r, g, b, alpha);
      BufferUploader.drawWithShader(buffer.buildOrThrow());
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
   }
}
