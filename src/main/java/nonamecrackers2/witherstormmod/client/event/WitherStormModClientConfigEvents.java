package nonamecrackers2.witherstormmod.client.event;

import java.nio.file.Path;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.forgespi.locating.IModFile;
import nonamecrackers2.crackerslib.client.event.impl.AddConfigEntryToMenuEvent;
import nonamecrackers2.crackerslib.client.event.impl.ConfigMenuButtonEvent;
import nonamecrackers2.crackerslib.client.event.impl.OnConfigScreenOpened;
import nonamecrackers2.crackerslib.client.event.impl.RegisterConfigScreensEvent;
import nonamecrackers2.crackerslib.client.gui.ConfigHomeScreen;
import nonamecrackers2.crackerslib.client.gui.title.TextTitle;
import nonamecrackers2.crackerslib.common.compat.CompatHelper;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.client.gui.WitherStormModConfigHomeScreen;
import nonamecrackers2.witherstormmod.client.util.Contributors;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;

public class WitherStormModClientConfigEvents {
   public static void registerConfigScreen(RegisterConfigScreensEvent event) {
      event.builder(
            ConfigHomeScreen.builder(TextTitle.ofModDisplayName("witherstormmod"))
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
            consumer -> {
               PackLocationInfo location = new PackLocationInfo(
                  "witherstormmod:programmer_art",
                  Component.translatable("witherstormmod.resourcepacks.programmer_art"),
                  PackSource.BUILT_IN,
                  Optional.empty()
               );
               consumer.accept(
                  Pack.readMetaAndCreate(location, new Pack.ResourcesSupplier() {
                     private PackResources open(PackLocationInfo info) {
                        IModFile modFile = ModList.get().getModFileById("witherstormmod").getFile();
                        Path resourcePath = modFile.findResource(new String[]{"resourcepacks/programmer_art"});
                        return new PathPackResources(info, resourcePath);
                     }

                     public PackResources openPrimary(PackLocationInfo info) {
                        return this.open(info);
                     }

                     public PackResources openFull(PackLocationInfo info, Pack.Metadata metadata) {
                        return this.open(info);
                     }
                  }, event.getPackType(), new PackSelectionConfig(false, Position.TOP, false))
               );
            }
         );
      }
   }
}
