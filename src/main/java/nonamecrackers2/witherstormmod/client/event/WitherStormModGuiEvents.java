package nonamecrackers2.witherstormmod.client.event;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingIn;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent.DebugText;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent.DebugText.Side;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.crackerslib.common.compat.CompatHelper;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.client.instancing.RenderBufferer;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;

public class WitherStormModGuiEvents {
   @SubscribeEvent
   public static void onRenderOverlay(DebugText event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.getDebugOverlay().showDebugScreen() && event.getSide() == Side.Right) {
         List<String> text = event.getText();
         text.add("");
         text.add("witherstormmod: " + WitherStormMod.getVersion());
         text.add("Buffered Instances: " + RenderBufferer.INSTANCE.getTotalInstances());
         boolean flag = RenderBufferer.shouldUse();
         String s = String.valueOf(flag);
         if (flag) {
            s = ChatFormatting.GREEN + s;
         } else {
            s = ChatFormatting.RED + s;
         }

         text.add("Render Bufferer Active: " + s);
      }
   }

   @SubscribeEvent
   public static void onPlayerJoinLevel(LoggingIn event) {
      if (CompatHelper.isOptifineLoaded() && (Boolean)WitherStormModConfig.CLIENT.optifineWarning.get()) {
         Minecraft mc = Minecraft.getInstance();
         ClickEvent clickEvent = new ClickEvent(Action.OPEN_URL, "https://github.com/nonamecrackers2/crackers-wither-storm-mod/wiki/Known-Compatibility-Issues");
         mc.gui
            .getChat()
            .addMessage(Component.translatable("chat.witherstormmod.optifine.notice").withStyle(Style.EMPTY.applyFormat(ChatFormatting.RED).withClickEvent(clickEvent)));
         WitherStormModConfig.CLIENT.optifineWarning.set(false);
      }
   }
}
