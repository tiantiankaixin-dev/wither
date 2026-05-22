package nonamecrackers2.witherstormmod.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.Builder;
import net.minecraft.resources.ResourceLocation;

public class RefreshSoundsButton extends Button {
   private static final ResourceLocation SOUND_REFRESHER = new ResourceLocation("witherstormmod", "textures/gui/sound_refresher.png");

   public RefreshSoundsButton(Builder builder) {
      super(builder);
   }

   public void renderWidget(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
      int x = 0;
      int y = 0;
      if (!this.active) {
         x = 20;
      }

      if (this.isHoveredOrFocused()) {
         y = 20;
      }

      RenderSystem.enableDepthTest();
      stack.blit(SOUND_REFRESHER, this.getX(), this.getY(), (float)x, (float)y, this.width, this.height, 256, 256);
   }
}
