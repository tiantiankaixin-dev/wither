package nonamecrackers2.witherstormmod.client.event;

import java.nio.file.Path;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.Pack.Position;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforgespi.locating.IModFile;
import nonamecrackers2.crackerslib.client.event.impl.AddConfigEntryToMenuEvent;
import nonamecrackers2.crackerslib.client.event.impl.ConfigMenuButtonEvent;
import nonamecrackers2.crackerslib.client.event.impl.OnConfigScreenOpened;
import nonamecrackers2.crackerslib.client.event.impl.RegisterConfigScreensEvent;
import nonamecrackers2.crackerslib.client.gui.ConfigHomeScreen;
import nonamecrackers2.crackerslib.client.gui.title.ImageTitle;
import nonamecrackers2.crackerslib.common.compat.CompatHelper;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.client.gui.WitherStormModConfigHomeScreen;
import nonamecrackers2.witherstormmod.client.util.Contributors;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;

public class WitherStormModClientConfigEvents {
   public static void registerConfigScreen(RegisterConfigScreensEvent event) {
      event.builder(
            ConfigHomeScreen.builder(ImageTitle.ofMod("witherstormmod", 256, 128, 1.0F))
               .crackersDefault("https://github.com/nonamecrackers2/crackers-wither-storm-mod/issues")
               .addLinkButton(
                  Component.translatable("gui.witherstormmod.screen.wsmoptions.nazaKofi").withStyle(ChatFormatting.GREEN),
                  "https://ko-fi.com/nazaru",
                  Tooltip.create(Component.translatable("gui.witherstormmod.screen.wsmoptions.nazaKofi.info"))
               )
               .build(WitherStormModConfigHomeScreen::new)
         )
         .addSpec(Type.CLIENT, WitherStormModConfig.CLIENT_SPEC)
         .addSpec(Type.COMMON, WitherStormModConfig.COMMON_SPEC)
         .addSpec(Type.SERVER, WitherStormModConfig.SERVER_SPEC)
         .register();
   }

   public static void registerConfigMenuButton(ConfigMenuButtonEvent event) {
      event.defaultButtonWithSingleCharacter('W', 7143633);
   }

   @SubscribeEvent
   public static void onConfigMenuOpened(OnConfigScreenOpened event) {
      if (event.getModId().equals("witherstormmod")) {
         event.setInitialPath(event.getType().extension());
      }
   }

   @SubscribeEvent
   public static void onOptionAddedToMenu(AddConfigEntryToMenuEvent event) {
      if (event.getModId().equals("witherstormmod")) {
         if (event.isValue(WitherStormModConfig.SERVER.flyingDisabledWarning)) {
            event.setCanceled(true);
         } else if (event.isValue(WitherStormModConfig.SERVER.shouldChunkLoadWhenNoPlayers)) {
            event.setCanceled(true);
         } else if (event.isValue(WitherStormModConfig.CLIENT.optifineWarning)) {
            event.setCanceled(!CompatHelper.isOptifineLoaded());
         } else if (event.isValue(WitherStormModConfig.CLIENT.aprilFools)) {
            event.setCanceled(!WitherStormMod.isAprilFools());
         } else if (event.isValue(WitherStormModConfig.CLIENT.patronCosmetic)) {
            event.setCanceled(!Contributors.currentPlayerHasCosmetic());
         }
      }
   }

   public static void addPackFindersEvent(AddPackFindersEvent event) {
      if (event.getPackType() == PackType.CLIENT_RESOURCES) {
         event.addRepositorySource(
            consumer -> consumer.accept(
                  Pack.readMetaAndCreate("witherstormmod:programmer_art", Component.translatable("witherstormmod.resourcepacks.programmer_art"), false, id -> {
                     IModFile modFile = ModList.get().getModFileById("witherstormmod").getFile();
                     Path resourcePath = modFile.findResource(new String[]{"resourcepacks/programmer_art"});
                     return new PathPackResources(modFile.getFileName() + ":" + resourcePath, resourcePath, true);
                  }, event.getPackType(), Position.TOP, PackSource.BUILT_IN)
               )
         );
      }
   }
}
