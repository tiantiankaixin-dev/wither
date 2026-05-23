package nonamecrackers2.witherstormmod.client.util;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import nonamecrackers2.witherstormmod.common.item.PhasometerItem;

public class PhasometerRenderHelper {
   public static void renderPhasometerOverlay(ItemStack item, GuiGraphics stack, float partialTicks, int width, int height, String dotDotDot) {
      CompoundTag tag = item.getOrCreateTag();
      Minecraft mc = Minecraft.getInstance();
      if (tag.contains(PhasometerItem.DataEntry.PHASE.tagName)) {
         List<PhasometerItem.DataEntry> entries = PhasometerItem.getEntries(tag);

         for (int i = 0; i < entries.size(); i++) {
            Component text = entries.get(i).getDisplayText(tag);
            stack.drawCenteredString(mc.font, text, width / 2, 30 + i * (9 + 2), -1);
         }
      } else {
         Component text;
         if (tag.contains(PhasometerItem.DataEntry.OBSTRUCTED.tagName)) {
            text = PhasometerItem.DataEntry.OBSTRUCTED.getDisplayText(tag);
         } else {
            text = Component.translatable("description.phasometer.searching", new Object[]{dotDotDot}).withStyle(ChatFormatting.GRAY);
         }

         stack.drawCenteredString(mc.font, text, width / 2, 30, -1);
      }
   }
}
