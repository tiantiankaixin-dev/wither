package nonamecrackers2.crackerslib.client.gui.widget.config;

import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import nonamecrackers2.crackerslib.common.config.preset.ConfigPreset;

public interface ConfigListItem extends Comparable<ConfigListItem> {
   void init(List<AbstractWidget> var1, int var2, int var3, int var4, int var5);

   void render(GuiGraphics var1, int var2, int var3, int var4, int var5, int var6, int var7, float var8);

   void onSavedAndClosed();

   void resetValue();

   boolean isValueReset();

   boolean matchesPreset(ConfigPreset var1, Predicate<String> var2);

   void setFromPreset(ConfigPreset var1, Predicate<String> var2);

   @Nullable
   Tooltip getTooltip(@Nullable ConfigPreset var1);

   boolean matchesSearch(String var1);

   static Component shortenText(Component name, int allowedWidth) {
      Minecraft mc = Minecraft.getInstance();
      String text = name.getString();
      int currentSize = 0;
      int lastIndex = -1;

      for (int i = 0; i < text.length(); i++) {
         currentSize += mc.font.width(FormattedText.of(String.valueOf(text.charAt(i)), name.getStyle()));
         lastIndex = i;
         if (currentSize > allowedWidth) {
            break;
         }
      }

      if (lastIndex > 0) {
         String newText = text.substring(0, lastIndex + 1);
         if (currentSize > allowedWidth) {
            newText = newText + "...";
         }

         return Component.literal(newText).withStyle(name.getStyle());
      } else {
         return CommonComponents.EMPTY;
      }
   }

   static String extractNameFromPath(String path) {
      String name = path;
      int index = path.lastIndexOf(46);
      if (index > 0 && index + 1 < path.length()) {
         name = path.substring(index + 1);
      }

      return name;
   }
}
