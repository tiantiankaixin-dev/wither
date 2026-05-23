package nonamecrackers2.crackerslib.example.client.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.config.ModConfig.Type;
import nonamecrackers2.crackerslib.client.event.impl.AddConfigEntryToMenuEvent;
import nonamecrackers2.crackerslib.client.event.impl.ConfigMenuButtonEvent;
import nonamecrackers2.crackerslib.client.event.impl.OnConfigScreenOpened;
import nonamecrackers2.crackerslib.client.event.impl.RegisterConfigScreensEvent;
import nonamecrackers2.crackerslib.client.gui.ConfigHomeScreen;
import nonamecrackers2.crackerslib.client.gui.title.TextTitle;
import nonamecrackers2.crackerslib.example.client.event.common.config.ExampleConfig;

public class ExampleClientEvents {
   public static void registerConfigScreen(RegisterConfigScreensEvent event) {
      event.builder(
            ConfigHomeScreen.builder(TextTitle.ofModDisplayName("crackerslib"))
               .crackersDefault("https://github.com/nonamecrackers2/crackerslib/issues")
               .build()
         )
         .addSpec(Type.CLIENT, ExampleConfig.CLIENT_SPEC)
         .register();
   }

   public static void registerConfigMenuButton(ConfigMenuButtonEvent event) {
      event.defaultButtonWithSingleCharacter('C', -666558);
   }

   @SubscribeEvent
   public static void onConfigScreenOpened(OnConfigScreenOpened event) {
      if (event.getModId().equals("crackerslib") && event.getType() == Type.CLIENT) {
         event.setInitialPath("list.category_example");
      }
   }

   @SubscribeEvent
   public static void onConfigEntryAddedToMenu(AddConfigEntryToMenuEvent event) {
      if (event.getModId().equals("crackerslib") && event.getType() == Type.CLIENT) {
         if (event.isValue(ExampleConfig.CLIENT.exampleString)) {
            event.setCanceled(true);
         } else if (event.isValue(ExampleConfig.CLIENT.exampleDouble)) {
            event.setCanceled(true);
         } else if (event.isValue(ExampleConfig.CLIENT.exampleEnum)) {
            event.setCanceled(true);
         }
      }
   }
}
