package nonamecrackers2.crackerslib.client.gui.title;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.neoforged.fml.ModList;

public record TextTitle(Component title, int width, int height) implements TitleLogo {
   public static TextTitle ofModDisplayName(String modid, Style style) {
      Minecraft mc = Minecraft.m_91087_();
      return ModList.get().getModContainerById(modid).map(container -> {
         Component text = Component.m_237113_(container.getModInfo().getDisplayName()).m_130948_(style);
         return new TextTitle(text, mc.f_91062_.m_92852_(text), 9);
      }).orElseThrow(() -> new NullPointerException("Could not find mod with id '" + modid + "'"));
   }

   public static TextTitle ofModDisplayName(String modid) {
      return ofModDisplayName(modid, Style.f_131099_.m_131136_(true).m_131162_(true));
   }

   @Override
   public void blit(GuiGraphics stack, int x, int y, float partialTicks) {
      Minecraft mc = Minecraft.m_91087_();
      stack.m_280430_(mc.f_91062_, this.title, x, y, -1);
   }

   @Override
   public int getWidth() {
      return this.width;
   }

   @Override
   public int getHeight() {
      return this.height;
   }
}
