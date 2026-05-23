package nonamecrackers2.crackerslib.client.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.crackerslib.CrackersLib;

public class CollapseButton extends AbstractButton {
   private static final ResourceLocation ICON = CrackersLib.id("textures/gui/config/collapse.png");
   private static final Component NAME = Component.m_237115_("gui.crackerslib.button.collapse.title");
   private static final Component TOOLTIP = Component.m_237115_("gui.crackerslib.button.collapse.description");
   private final Runnable onPressed;

   public CollapseButton(int x, int y, Runnable onPressed) {
      super(x, y, 20, 20, NAME);
      this.onPressed = onPressed;
      this.m_257544_(Tooltip.m_257550_(TOOLTIP));
   }

   public void m_5691_() {
      this.onPressed.run();
   }

   public void m_87963_(GuiGraphics stack, int mouseX, int mouseY, float partialTick) {
      super.m_87963_(stack, mouseX, mouseY, partialTick);
      stack.m_280411_(ICON, this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_(), 0.0F, 0.0F, this.m_5711_(), this.m_93694_(), 256, 256);
   }

   public void m_280139_(GuiGraphics stack, Font pFont, int pColor) {
   }

   protected void m_168797_(NarrationElementOutput pNarrationElementOutput) {
      this.m_168802_(pNarrationElementOutput);
   }
}
