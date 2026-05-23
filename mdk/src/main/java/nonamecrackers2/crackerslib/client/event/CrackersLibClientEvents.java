package nonamecrackers2.crackerslib.client.event;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.ConfigScreenHandler;
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
         Minecraft mc = Minecraft.m_91087_();
         GridLayout layout = new GridLayout().m_267750_(4);
         RowHelper rowHelper = layout.m_264606_(1);
         ModList.get()
            .forEachModInOrder(
               mod -> {
                  if (!((List)CrackersLibConfig.CLIENT.hiddenConfigMenuButtons.get()).contains(mod.getModId())) {
                     ConfigScreenHandler.getScreenFactoryFor(mod.getModInfo())
                        .ifPresent(
                           factory -> {
                              ConfigMenuButtons.Factory buttonFactory = ConfigMenuButtons.getButtonFactory(mod.getModId());
                              if (buttonFactory != null) {
                                 AbstractButton button = (AbstractButton)rowHelper.m_264139_(
                                    buttonFactory.makeButton(action -> mc.m_91152_((Screen)factory.apply(mc, screen)))
                                 );
                                 button.m_93674_(20);
                                 button.setHeight(20);
                                 button.m_257544_(Tooltip.m_257550_(Component.m_237113_(mod.getModInfo().getDisplayName())));
                              }
                           }
                        );
                  }
               }
            );
         layout.m_264036_();
         FrameLayout.m_264460_(layout, screen.f_96543_ / 2 - 180, screen.f_96544_ / 6 + 42, 20, 200, 0.5F, 0.0F);
         layout.m_264134_(event::addListener);
      }
   }
}
