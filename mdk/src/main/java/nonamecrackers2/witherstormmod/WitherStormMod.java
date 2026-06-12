package nonamecrackers2.witherstormmod;

import net.neoforged.fml.loading.FMLEnvironment;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import nonamecrackers2.crackerslib.common.extending.BlockEntityTypeExtender;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.WitherStormWorldInteractions;
import nonamecrackers2.witherstormmod.api.common.registry.WitherStormModRegistries;
import nonamecrackers2.witherstormmod.client.audio.SoundManagersRefresher;
import nonamecrackers2.witherstormmod.client.capability.BowelsEffectsManager;
import nonamecrackers2.witherstormmod.client.event.ParticleEvents;
import nonamecrackers2.witherstormmod.client.event.WitherStormModClientConfigEvents;
import nonamecrackers2.witherstormmod.client.event.WitherStormModRegisterBlockColors;
import nonamecrackers2.witherstormmod.client.gui.overlay.OverlayRenderers;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientEvents;
import nonamecrackers2.witherstormmod.client.init.WitherStormModMenuScreens;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRecipeBookTypes;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.init.WitherStormModShaders;
import nonamecrackers2.witherstormmod.client.resources.WitherStormResourceConfigManager;
import nonamecrackers2.witherstormmod.client.shader.PostProcessingShaders;
import nonamecrackers2.witherstormmod.client.util.Contributors;
import nonamecrackers2.witherstormmod.common.command.argument.entityselector.WitherStormSelector;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.HeadManager;
import nonamecrackers2.witherstormmod.common.event.WitherStormModClusterInteractionEvents;
import nonamecrackers2.witherstormmod.common.event.WitherStormModDataEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModActivities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModAttributes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlockEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCriteriaTriggers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEffects;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModFeatures;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItemTabs;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMemoryTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMenuTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPaintingTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPotions;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSensorTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModStats;
import nonamecrackers2.witherstormmod.common.init.WitherStormModStructures;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSymbiontSpellTypes;
import nonamecrackers2.witherstormmod.common.item.FormidiBladeItem;
import nonamecrackers2.witherstormmod.common.serializer.WitherStormModDataSerializers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;

@Mod("witherstormmod")
public class WitherStormMod {
   public static final Logger LOGGER = LogManager.getLogger();
   public static final String MOD_ID = "witherstormmod";
   private static final ResourceLocation BOWELS = ResourceLocation.fromNamespaceAndPath("witherstormmod", "bowels");
   public static final LocalDate DATE = LocalDate.now();
   private static ArtifactVersion version;
   private static boolean isAprilFools;

   public WitherStormMod(IEventBus modEventBus) {
      ModLoadingContext context = ModLoadingContext.get();
      version = context.getActiveContainer().getModInfo().getVersion();
      context.registerConfig(Type.CLIENT, WitherStormModConfig.CLIENT_SPEC);
      context.registerConfig(Type.COMMON, WitherStormModConfig.COMMON_SPEC);
      context.registerConfig(Type.SERVER, WitherStormModConfig.SERVER_SPEC);
      modEventBus.addListener(WitherStormModEntityTypes::addEntityAttributes);
      modEventBus.addListener(WitherStormModEntityTypes::registerSpawnPlacements);
      WitherStormModCapabilities.ATTACHMENT_TYPES.register(modEventBus);
      modEventBus.addListener(WitherStormModDataEvents::gatherData);
      modEventBus.addListener(WitherStormModConfig::registerPresets);
      modEventBus.addListener(WitherStormModClusterInteractionEvents::registerClusterInteractions);
      modEventBus.addListener(WitherStormModRegistries::registerRegistries);
      modEventBus.addListener(this::commonSetup);
      modEventBus.addListener(this::clientSetup);
      WitherStormModItemTabs.TABS.register(modEventBus);
      WitherStormModBlocks.BLOCKS.register(modEventBus);
      WitherStormModEffects.EFFECTS.register(modEventBus);
      WitherStormModEntityTypes.ENTITIES.register(modEventBus);
      WitherStormModFeatures.FEATURES.register(modEventBus);
      WitherStormModItems.ITEMS.register(modEventBus);
      WitherStormModParticleTypes.PARTICLE_TYPES.register(modEventBus);
      WitherStormModSoundEvents.SOUND_EVENTS.register(modEventBus);
      WitherStormModStructures.STRUCTURE_FEATURES.register(modEventBus);
      WitherStormModBlockEntityTypes.BLOCK_ENTITIES.register(modEventBus);
      WitherStormModDataSerializers.DATA_SERIALIZERS.register(modEventBus);
      WitherStormModAttributes.ATTRIBUTES.register(modEventBus);
      WitherStormModMemoryTypes.MEMORY_MODULE_TYPES.register(modEventBus);
      WitherStormModSensorTypes.SENSOR_TYPES.register(modEventBus);
      WitherStormModActivities.ACTIVITIES.register(modEventBus);
      WitherStormModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
      WitherStormModPaintingTypes.PAINTING_MOTIVES.register(modEventBus);
      WitherStormModMenuTypes.MENU_TYPES.register(modEventBus);
      WitherStormModRecipeTypes.RECIPE_TYPES.register(modEventBus);
      WitherStormModPotions.POTIONS.register(modEventBus);
      WitherStormModSymbiontSpellTypes.register(modEventBus);
      IEventBus forgeBus = NeoForge.EVENT_BUS;
      forgeBus.addListener(WitherStormModDataEvents::addResourceListeners);
      if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(WitherStormModRegisterBlockColors::registerBlockColors);
            WitherStormModClientCapabilities.ATTACHMENT_TYPES.register(modEventBus);
            modEventBus.addListener(ParticleEvents::registerFactories);
            modEventBus.addListener(OverlayRenderers::registerOverlays);
            modEventBus.addListener(WitherStormModRecipeBookTypes::registerRecipeBookCategories);
            modEventBus.addListener(BowelsEffectsManager.Events::registerSpecialEffects);
            modEventBus.addListener(WitherStormModClientConfigEvents::registerConfigScreen);
            modEventBus.addListener(WitherStormModClientConfigEvents::registerConfigMenuButton);
            modEventBus.addListener(WitherStormModClientConfigEvents::addPackFindersEvent);
            modEventBus.register(WitherStormModRenderers.class);
            modEventBus.register(WitherStormModShaders.class);
            Minecraft mc = Minecraft.getInstance();
            if (mc != null) {
               ReloadableResourceManager manager = (ReloadableResourceManager)mc.getResourceManager();
               manager.registerReloadListener(PostProcessingShaders.INSTANCE);
               manager.registerReloadListener(WitherStormResourceConfigManager.INSTANCE);
            }
      }
      int month = DATE.get(ChronoField.MONTH_OF_YEAR);
      int day = DATE.get(ChronoField.DAY_OF_MONTH);
      isAprilFools = month == 4 && day == 1;
      HeadManager.bootstrap();
   }

   private void commonSetup(FMLCommonSetupEvent event) {
      WitherStormModEvents.registerEvents();
      WitherStormModStructures.registerPieceTypes();
      WitherStormModPacketHandlers.registerPackets();
      WitherStormModStats.register();
      ((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(WitherStormModBlocks.TAINTED_MUSHROOM.getId(), WitherStormModBlocks.POTTED_TAINTED_MUSHROOM);
      EntitySelectorManager.register("w", new WitherStormSelector());
      event.enqueueWork(
         () -> {
            CriteriaTriggers.register(WitherStormModCriteriaTriggers.PLAY_DEAD_TRIGGER);
            CriteriaTriggers.register(WitherStormModCriteriaTriggers.REVIVAL_TRIGGER);
            CriteriaTriggers.register(WitherStormModCriteriaTriggers.ESCAPE_STORM);
            CriteriaTriggers.register(WitherStormModCriteriaTriggers.CURED_SICKENED_MOB);
            CriteriaTriggers.register(WitherStormModCriteriaTriggers.ACTIVATE_SUPER_BEACON);
            CriteriaTriggers.register(WitherStormModCriteriaTriggers.RING_BELL_NEAR_STORM);
            CriteriaTriggers.register(WitherStormModCriteriaTriggers.SUMMON_MOB_SUPER_BEACON);
            CriteriaTriggers.register(WitherStormModCriteriaTriggers.LINK_AMULET);
            CriteriaTriggers.register(WitherStormModCriteriaTriggers.NEARLY_KILL_WITHER_STORM);
            WitherStormModItems.registerBrewingRecipes();
            WitherStormWorldInteractions.initialize();
            BlockEntityTypeExtender.addToBlockEntityType(
               BlockEntityType.SIGN, new Block[]{(Block)WitherStormModBlocks.TAINTED_SIGN.get(), (Block)WitherStormModBlocks.TAINTED_WALL_SIGN.get()}
            );
         }
      );
   }

   private void clientSetup(FMLClientSetupEvent event) {
      WitherStormModClientEvents.registerEvents();
      WitherStormModMenuScreens.register();
      event.enqueueWork(
         () -> {
            Minecraft mc = Minecraft.getInstance();
            ReloadableResourceManager manager = (ReloadableResourceManager)mc.getResourceManager();
            manager.registerReloadListener(SoundManagersRefresher.INSTANCE);
            ItemProperties.register(
               Items.CROSSBOW,
               ResourceLocation.fromNamespaceAndPath("witherstormmod", "ender_pearl"),
               (stack, world, entity, i) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, Items.ENDER_PEARL) ? 1.0F : 0.0F
            );
            FormidiBladeItem.registerItemProperty();
         }
      );
      Contributors.getContributors();
   }

   public static ServerLevel bowels(ServerLevel world) {
      return world.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, bowelsLocation()));
   }

   public static ResourceLocation bowelsLocation() {
      return BOWELS;
   }

   public static ArtifactVersion getVersion() {
      return version;
   }

   public static boolean isAprilFools() {
      return isAprilFools;
   }

   public static ResourceLocation id(String path) {
      return ResourceLocation.fromNamespaceAndPath("witherstormmod", path);
   }
}
