package nonamecrackers2.crackerslib.client.event;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.event.ScreenEvent.Init.Pre;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig.Type;
import nonamecrackers2.crackerslib.client.event.impl.RegisterConfigScreensEvent;
import nonamecrackers2.crackerslib.client.gui.ConfigHomeScreen;
import nonamecrackers2.crackerslib.client.gui.ConfigMenuButtons;
import nonamecrackers2.crackerslib.client.gui.title.TextTitle;
import nonamecrackers2.crackerslib.common.config.CrackersLibConfig;

public class CrackersLibClientEvents {
   public static void registerConfigScreen(RegisterConfigScreensEvent event) {
      event.builder(ConfigHomeScreen.builder(TextTitle.ofModDisplayName("crackerslib")).crackersDefault().build())
         .addSpec(Type.CLIENT, CrackersLibConfig.CLIENT_SPEC)
         .register();
   }

   @SubscribeEvent
   public static void initGui(Pre event) {
      if (event.getScreen() instanceof OptionsScreen screen) {
         Minecraft mc = Minecraft.getInstance();
         GridLayout layout = new GridLayout().columnSpacing(4);
         RowHelper rowHelper = layout.createRowHelper(1);
         ModList.get()
            .forEachModInOrder(
               mod -> {
                  if (!((List<?>)CrackersLibConfig.CLIENT.hiddenConfigMenuButtons.get()).contains(mod.getModId())) {
                     mod.getModInfo().getOwningFile().getFile().getFileName();
                     IConfigScreenFactory.getForMod(mod.getModInfo())
                        .ifPresent(
                           factory -> {
                              ConfigMenuButtons.Factory buttonFactory = ConfigMenuButtons.getButtonFactory(mod.getModId());
                              if (buttonFactory != null) {
                                 AbstractButton button = (AbstractButton)rowHelper.addChild(
                                    buttonFactory.makeButton(action -> mc.setScreen(factory.createScreen(mc, screen)))
                                 );
                                 button.setWidth(20);
                                 button.rowSpacing(20);
                                 button.setTooltip(Tooltip.create(Component.literal(mod.getModInfo().getDisplayName())));
                              }
                           }
                        );
                  }
               }
            );
         layout.arrangeElements();
         FrameLayout.alignInRectangle(layout, screen.width / 2 - 180, screen.height / 6 + 42, 20, 200, 0.5F, 0.0F);
         layout.visitWidgets(event::addListener);
      }
   }
}
