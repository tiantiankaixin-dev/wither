package nonamecrackers2.crackerslib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import nonamecrackers2.crackerslib.client.event.CrackersLibClientEvents;
import nonamecrackers2.crackerslib.client.event.impl.RegisterConfigScreensEvent;
import nonamecrackers2.crackerslib.client.gui.ConfigMenuButtons;
import nonamecrackers2.crackerslib.common.compat.CompatHelper;
import nonamecrackers2.crackerslib.common.config.CrackersLibConfig;
import nonamecrackers2.crackerslib.common.config.preset.ConfigPresets;
import nonamecrackers2.crackerslib.common.event.CrackersLibDataEvents;
import nonamecrackers2.crackerslib.common.extending.BlockEntityTypeExtender;
import nonamecrackers2.crackerslib.common.init.CrackersLibCommandArguments;

@Mod("crackerslib")
public class CrackersLib {
   public static final String MODID = "crackerslib";

   public CrackersLib(IEventBus modBus) {
      modBus.addListener(this::commonSetup);
      modBus.addListener(this::clientSetup);
      modBus.addListener(CrackersLibDataEvents::gatherData);
      ModLoadingContext context = ModLoadingContext.get();
      context.getActiveContainer().registerConfig(Type.CLIENT, CrackersLibConfig.CLIENT_SPEC);
      CrackersLibCommandArguments.register(modBus);
   }

   public void clientSetup(FMLClientSetupEvent event) {
      IEventBus forgeBus = NeoForge.EVENT_BUS;
      forgeBus.addListener(CrackersLibClientEvents::registerConfigScreen);
      forgeBus.register(CrackersLibClientEvents.class);
      event.enqueueWork(() -> {
         ModLoader.runEventGenerator(mod -> new RegisterConfigScreensEvent(mod.getModId()));
         ConfigMenuButtons.gatherButtonFactories();
      });
   }

   public void commonSetup(FMLCommonSetupEvent event) {
      event.enqueueWork(() -> {
         ConfigPresets.gatherPresets();
         CompatHelper.checkForLoaded();
         BlockEntityTypeExtender.addToBlockEntityType(BlockEntityType.BEACON, Blocks.BARREL);
      });
   }

   public static ResourceLocation id(String path) {
      return ResourceLocation.fromNamespaceAndPath("crackerslib", path);
   }
}
