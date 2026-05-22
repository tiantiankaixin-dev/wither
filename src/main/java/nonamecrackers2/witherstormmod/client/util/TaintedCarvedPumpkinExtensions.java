package nonamecrackers2.witherstormmod.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class TaintedCarvedPumpkinExtensions implements IClientItemExtensions {
   public static final TaintedCarvedPumpkinExtensions INSTANCE = new TaintedCarvedPumpkinExtensions();
   private static final ResourceLocation TAINTED_PUMPKIN_BLUR = new ResourceLocation("witherstormmod", "textures/misc/tainted_pumpkin_blur.png");

   private TaintedCarvedPumpkinExtensions() {
   }

   public void renderHelmetOverlay(ItemStack stack, Player player, int width, int height, float partialTick) {
      GuiGraphics graphics = new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
      graphics.blit(TAINTED_PUMPKIN_BLUR, 0, 0, -90, 0.0F, 0.0F, width, height, width, height);
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
