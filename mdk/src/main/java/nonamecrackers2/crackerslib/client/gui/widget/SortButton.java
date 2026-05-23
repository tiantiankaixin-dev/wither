package nonamecrackers2.crackerslib.client.gui.widget;

import java.util.function.Consumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.crackerslib.CrackersLib;
import nonamecrackers2.crackerslib.client.util.SortType;

public class SortButton extends AbstractButton {
   private static final ResourceLocation SORT_ICONS = CrackersLib.id("textures/gui/config/sort.png");
   private static final Component NAME = Component.m_237115_("gui.crackerslib.button.sorting.title");
   private final Consumer<SortType> onPressed;
   private SortType type = SortType.A_TO_Z;

   public SortButton(int x, int y, Consumer<SortType> onPressed) {
      super(x, y, 20, 20, NAME);
      this.onPressed = onPressed;
      this.m_257544_(this.buildTooltip());
   }

   public void m_5691_() {
      int next = this.type.ordinal() + 1;
      if (next >= SortType.values().length) {
         next = 0;
      }

      this.type = SortType.values()[next];
      this.onPressed.accept(this.type);
      this.m_257544_(this.buildTooltip());
   }

   public void m_87963_(GuiGraphics stack, int mouseX, int mouseY, float partialTick) {
      super.m_87963_(stack, mouseX, mouseY, partialTick);
      float texY = 0.0F;
      if (this.type == SortType.Z_TO_A) {
         texY = 20.0F;
      }

      stack.m_280411_(SORT_ICONS, this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_(), 0.0F, texY, this.m_5711_(), this.m_93694_(), 256, 256);
   }

   public void m_280139_(GuiGraphics stack, Font pFont, int pColor) {
   }

   protected void m_168797_(NarrationElementOutput pNarrationElementOutput) {
      this.m_168802_(pNarrationElementOutput);
   }

   public Tooltip buildTooltip() {
      Component text = NAME.m_6881_().m_130946_(" ").m_7220_(this.type.getName());
      return Tooltip.m_257550_(text);
   }
}
