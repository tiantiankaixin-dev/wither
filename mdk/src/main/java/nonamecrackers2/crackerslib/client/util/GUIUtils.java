package nonamecrackers2.crackerslib.client.util;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;

public class GUIUtils {
   public static void openLink(String link) {
      Minecraft mc = Minecraft.m_91087_();
      Screen current = mc.f_91080_;
      mc.m_91152_(new ConfirmLinkScreen(b -> {
         if (b) {
            Util.m_137581_().m_137646_(link);
         }

         mc.m_91152_(current);
      }, link, true));
   }
}
