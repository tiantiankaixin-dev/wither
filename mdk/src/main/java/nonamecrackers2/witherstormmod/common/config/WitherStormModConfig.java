package nonamecrackers2.witherstormmod.common.config;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.fml.config.ModConfig.Type;
import nonamecrackers2.crackerslib.common.config.ConfigHelper;
import nonamecrackers2.crackerslib.common.config.preset.ConfigPreset;
import nonamecrackers2.crackerslib.common.config.preset.RegisterConfigPresetsEvent;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.ultimatetarget.UltimateTargetManager;
import nonamecrackers2.witherstormmod.common.util.ItemPreservationCondition;
import org.apache.commons.lang3.tuple.Pair;

public class WitherStormModConfig {
   public static final WitherStormModConfig.ClientConfig CLIENT;
   public static final ForgeConfigSpec CLIENT_SPEC;
   public static final WitherStormModConfig.CommonConfig COMMON;
   public static final ForgeConfigSpec COMMON_SPEC;
   public static final WitherStormModConfig.ServerConfig SERVER;
   public static final ForgeConfigSpec SERVER_SPEC;

   public static void registerPresets(RegisterConfigPresetsEvent event) {
      event.exclude(CLIENT.playWitherStormTheme)
         .exclude(CLIENT.playSymbiontTheme)
         .exclude(CLIENT.chromaticAberration)
         .exclude(CLIENT.blindingEffects)
         .exclude(CLIENT.cameraShakeEffects)
         .exclude(CLIENT.distantFog)
         .exclude(CLIENT.earRingingEffects)
         .exclude(CLIENT.renderTractorBeamOverlay)
         .exclude(CLIENT.renderSkyAmbienceEffects)
         .exclude(CLIENT.tractorBeamParticles)
         .exclude(CLIENT.renderShine)
         .exclude(CLIENT.distantRenderer)
         .exclude(CLIENT.witherSicknessLayer)
         .exclude(CLIENT.vertexBufferRendering)
         .exclude(CLIENT.optifineWarning)
         .exclude(CLIENT.aprilFools)
         .exclude(CLIENT.patronCosmetic)
         .exclude(CLIENT.customPanorama)
         .exclude(CLIENT.asyncBufferBuilders)
         .exclude(CLIENT.hideDebrisRingsUntilSplit)
         .exclude(CLIENT.playMinecraftMusic);
      event.registerPreset(
         Type.CLIENT,
         ConfigPreset.builder(Component.translatable("config.witherstormmod.preset.client.medium.title"))
            .setPreset(CLIENT.renderDebrisCloud, false)
            .setPreset(CLIENT.tractorBeamParticles, false)
            .setPreset(CLIENT.lowResModels, false)
            .setPreset(CLIENT.witherStormLOD, false)
            .setPreset(CLIENT.blockClusterRendering, true)
            .setDescription(Component.translatable("config.witherstormmod.preset.client.medium.description"))
            .build()
      );
      event.registerPreset(
         Type.CLIENT,
         ConfigPreset.builder(Component.translatable("config.witherstormmod.preset.client.low.title"))
            .setPreset(CLIENT.renderDebrisCloud, false)
            .setPreset(CLIENT.tractorBeamParticles, false)
            .setPreset(CLIENT.lowResModels, false)
            .setPreset(CLIENT.witherStormLOD, true)
            .setPreset(CLIENT.blockClusterRendering, false)
            .setDescription(Component.translatable("config.witherstormmod.preset.client.low.description"))
            .build()
      );
      event.registerPreset(
         Type.CLIENT,
         ConfigPreset.builder(Component.translatable("config.witherstormmod.preset.client.ultra_low.title"))
            .setPreset(CLIENT.renderDebrisCloud, false)
            .setPreset(CLIENT.tractorBeamParticles, false)
            .setPreset(CLIENT.lowResModels, true)
            .setPreset(CLIENT.witherStormLOD, false)
            .setPreset(CLIENT.blockClusterRendering, false)
            .setDescription(Component.translatable("config.witherstormmod.preset.client.ultra_low.description"))
            .build()
      );
      event.registerPreset(
         Type.SERVER,
         ConfigPreset.builder(Component.translatable("config.witherstormmod.preset.server.performance.title"))
            .setPreset(SERVER.squashHitbox, true)
            .setDescription(Component.translatable("config.witherstormmod.preset.server.performance.description"))
            .build()
      );
      event.registerPreset(
         Type.SERVER,
         ConfigPreset.builder(Component.translatable("config.witherstormmod.preset.server.mass_destruction.title"))
            .setPreset(SERVER.hunchbackClusterPickupInterval, 10)
            .setPreset(SERVER.clusterPickupInterval, 10)
            .setPreset(SERVER.devourerClusterPickupInterval, 10)
            .setPreset(SERVER.flamingSkullExplosionSize, 12.0)
            .setPreset(SERVER.flamingSkullSpeedModifier, 4.0)
            .setDescription(Component.translatable("config.witherstormmod.preset.server.mass_destruction.description"))
            .build()
      );
   }

   static {
      Pair<WitherStormModConfig.ClientConfig, ForgeConfigSpec> clientSpecPair = new Builder().configure(WitherStormModConfig.ClientConfig::new);
      CLIENT_SPEC = (ForgeConfigSpec)clientSpecPair.getRight();
      CLIENT = (WitherStormModConfig.ClientConfig)clientSpecPair.getLeft();
      Pair<WitherStormModConfig.CommonConfig, ForgeConfigSpec> commonSpecPair = new Builder().configure(WitherStormModConfig.CommonConfig::new);
      COMMON_SPEC = (ForgeConfigSpec)commonSpecPair.getRight();
      COMMON = (WitherStormModConfig.CommonConfig)commonSpecPair.getLeft();
      Pair<WitherStormModConfig.ServerConfig, ForgeConfigSpec> serverSpecPair = new Builder().configure(WitherStormModConfig.ServerConfig::new);
      SERVER_SPEC = (ForgeConfigSpec)serverSpecPair.getRight();
      SERVER = (WitherStormModConfig.ServerConfig)serverSpecPair.getLeft();
   }

   public static class ClientConfig extends ConfigHelper {
      public final ConfigValue<Boolean> renderDebrisCloud;
      public final ConfigValue<Boolean> renderDebrisRings;
      public final ConfigValue<Boolean> renderDistantDebris;
      public final ConfigValue<Boolean> witherStormLOD;
      public final ConfigValue<Boolean> lowResModels;
      public final ConfigValue<Boolean> renderTractorBeams;
      public final ConfigValue<Boolean> renderPulse;
      public final ConfigValue<Boolean> distantRenderer;
      public final ConfigValue<Boolean> blockClusterRendering;
      public final ConfigValue<Boolean> witherSicknessLayer;
      public final ConfigValue<Boolean> playWitherStormTheme;
      public final ConfigValue<Boolean> playSymbiontTheme;
      public final ConfigValue<Boolean> chromaticAberration;
      public final ConfigValue<Boolean> blindingEffects;
      public final ConfigValue<Boolean> cameraShakeEffects;
      public final ConfigValue<Boolean> distantFog;
      public final ConfigValue<Boolean> earRingingEffects;
      public final ConfigValue<Boolean> renderTractorBeamOverlay;
      public final ConfigValue<Boolean> renderSkyAmbienceEffects;
      public final ConfigValue<Boolean> tractorBeamParticles;
      public final ConfigValue<Boolean> renderShine;
      public final ConfigValue<Boolean> optifineWarning;
      public final ConfigValue<Boolean> aprilFools;
      public final ConfigValue<Boolean> vertexBufferRendering;
      public final ConfigValue<Boolean> patronCosmetic;
      public final ConfigValue<Boolean> customPanorama;
      public final ConfigValue<Boolean> asyncBufferBuilders;
      public final ConfigValue<Boolean> hideDebrisRingsUntilSplit;
      public final ConfigValue<Boolean> playMinecraftMusic;
      public final ConfigValue<Boolean> renderEmissiveDecalForHeads;

      private ClientConfig(Builder builder) {
         super(builder, "witherstormmod");
         builder.comment("Client options").push("client");
         this.playMinecraftMusic = this.createValue(
            true,
            "playMinecraftMusic",
            false,
            "Specifies if Minecraft music should play at all. Used for if you want the boss music to play but not the Minecraft music"
         );
         builder.comment("Compatibility").push("compatibility");
         this.distantRenderer = this.createValue(
            true,
            "distantRenderer",
            false,
            "The distant renderer allows for the Wither Storm to be rendered from much greater distances than what vanilla allows. Disable if you're facing issues with it"
         );
         this.witherSicknessLayer = this.createValue(
            true, "witherSicknessLayer", true, "Specifies if an overlay should be applied to entities that renders wither sickness"
         );
         builder.pop();
         builder.comment("Accessibility").push("accessibility");
         this.chromaticAberration = this.createValue(true, "chromaticAberration", false, "Toggle to enable/disable the chromatic aberration effect");
         this.blindingEffects = this.createValue(
            true, "blindingEffects", false, "Specifies whether or not a white overlay should cover the screen during certain events"
         );
         this.cameraShakeEffects = this.createValue(true, "cameraShakeEffects", false, "Specifies whether or not camera shake effects should be used");
         this.earRingingEffects = this.createValue(true, "earRingingEffects", false, "Turn off to disable the ear ringing effects used in the mod");
         builder.pop();
         builder.comment("Preference").push("preference");
         this.renderDebrisCloud = this.createValue(true, "renderDebrisCloud", false, "Toggles the rendering of the debris cloud surrounding the Wither Storm");
         this.renderDebrisRings = this.createValue(
            true,
            "renderDebrisRings",
            false,
            "Toggles the rendering of debris rings that surround the Wither Storm (much more performant than the debris cloud)"
         );
         this.renderTractorBeams = this.createValue(true, "renderTractorBeams", false, "Toggles the rendering of the tractor beams");
         this.renderTractorBeamOverlay = this.createValue(
            true, "renderTractorBeamOverlay", false, "Turn off to disable the overlay that appears when inside a tractor beam"
         );
         this.renderSkyAmbienceEffects = this.createValue(
            true, "renderSkyAmbienceEffects", false, "Specifies if sky ambience affects (sky darkening) should render when a Wither Storm is nearby"
         );
         this.tractorBeamParticles = this.createValue(
            true, "tractorBeamParticles", false, "Specifies if particles inside the tractor beams of the Wither Storm should render"
         );
         this.distantFog = this.createValue(true, "distantFog", false, "Specifies if fog should be applied to Wither Storms being rendered from a distance");
         this.renderShine = this.createValue(true, "renderShine", false, "Specifies if a purple shine should render behind the Wither Storm at night");
         this.renderPulse = this.createValue(
            true,
            "renderPulse",
            false,
            "Specifies if a pulsating effect, mimicking endermen pulling the Wither Storm apart, should be rendered once its evolution is complete"
         );
         this.hideDebrisRingsUntilSplit = this.createValue(
            false, "hideDebrisRingsUntilSplit", false, "Hides the debris rings until the Wither Storm has split (phase 6)"
         );
         this.renderEmissiveDecalForHeads = this.createValue(
            true, "renderEmissiveDecalForHeads", false, "Specifies if the emissive decal (eyes and teeth) for the Wither Storm heads should be rendered"
         );
         builder.pop();
         builder.comment("Boss music").push("boss_music");
         this.playWitherStormTheme = this.createValue(
            false, "playWitherStormTheme", false, "Toggles the Wither Storm boss theme. NOTE: this theme is from MC:SM and is subject to copyright"
         );
         this.playSymbiontTheme = this.createValue(true, "playSymbiontTheme", false, "Toggles the Withered Symbiont theme, created for CWSM by Mar Mar");
         builder.pop();
         builder.comment("Instancing").push("instancing");
         this.vertexBufferRendering = this.createValue(
            true,
            "vertexBufferRendering",
            false,
            "Specifies if CWSM should use more performant rendering when rendering Block Clusters and the Wither Storm's mass. NOTE: It is not recommended to disable this option unless necessary"
         );
         this.asyncBufferBuilders = this.createValue(
            true,
            "asyncBufferBuilders",
            false,
            "Builds instanced vertex buffers off thread to help reduce stuttering. It is only recommended to disable this if facing compatibility issues"
         );
         builder.pop();
         builder.comment("Performance").push("performance");
         this.renderDistantDebris = this.createValue(
            true,
            "renderDistantDebris",
            false,
            "Setting this value to false will disable the debris cloud when rendering the Wither Storm from large distances"
         );
         this.witherStormLOD = this.createValue(
            false, "witherStormLOD", false, "Specifies if the low res models should be used when being rendered via the distant renderer"
         );
         this.lowResModels = this.createValue(
            false, "lowResModels", false, "Uses larger cubes to make up the the phase 4 and up models. May result in a performance increase"
         );
         this.blockClusterRendering = this.createValue(true, "blockClusterRendering", false, "Toggles the rendering of Block Clusters");
         builder.pop();
         this.optifineWarning = this.createValue(true, "optifineWarning", false, "Toggles the OptiFine warning upon joining a world");
         this.aprilFools = this.createValue(true, "aprilFools", false, "Toggles April Fools special effects");
         this.patronCosmetic = this.createValue(true, "patronCosmetic", false, "Toggles the patron cosmetic for nonamecrackers2's Wither Storm backers");
         this.customPanorama = this.createValue(true, "customPanorama", false, "Toggles the custom main menu panorama added by the mod");
         builder.pop();
      }
   }

   public static class CommonConfig extends ConfigHelper {
      public final ConfigValue<Boolean> blockClustersDropItems;
      public final ConfigValue<Boolean> shouldPickUpVehicles;
      public final ConfigValue<Boolean> phantomsOrbitWitherStorm;
      public final ConfigValue<Boolean> playerCannotDismountTentacles;
      public final ConfigValue<Boolean> injectCustomAiBehavior;
      public final ConfigValue<List<? extends String>> injectAiMobBlacklist;
      public final ConfigValue<Boolean> autoSpawnWitherStorm;
      public final ConfigValue<Integer> autoSpawnTime;

      private CommonConfig(Builder builder) {
         super(builder, "witherstormmod");
         builder.comment("Common options").push("common");
         this.blockClustersDropItems = this.createValue(
            false, "blockClustersDropItems", true, "Toggle to enable/disable drops from Block Clusters. NOTE: Enabling this feature can cause world lag"
         );
         this.shouldPickUpVehicles = this.createValue(
            true,
            "shouldPickUpVehicles",
            false,
            "Specifies if the current entity the Wither Storm is picking up has a vehicle, that it should pick it up as well. Ex: Should pick up a player riding a boat"
         );
         this.phantomsOrbitWitherStorm = this.createValue(
            true,
            "phantomsOrbitWitherStorm",
            true,
            "Specifies if phantoms AI should be overriden to allow circling above any nearby Wither Storms. Disable if you wish for default behaviour and/or if issues arise"
         );
         this.playerCannotDismountTentacles = this.createValue(
            true,
            "playerCannotDismountTentacles",
            false,
            "Specifies if players should not be able to dismount a tentacle entity. Disable if facing compatibility issues"
         );
         this.injectCustomAiBehavior = this.createValue(
            true,
            "injectCustomAiBehavior",
            true,
            "Specifies if custom AI behavior should be injected into certain entities (such as mobs running away from the Wither Storm). Disable if facing compatibility issues"
         );
         this.injectAiMobBlacklist = this.createListValue(String.class, () -> {
            List<String> list = Lists.newArrayList();
            list.add("witherstormmod:example");
            return list;
         }, val -> ResourceLocation.isValidResourceLocation(val), "injectAiMobBlacklist", true, "A list of mobs that should not have custom AI injected into them");
         this.autoSpawnWitherStorm = this.createValue(
            false, "autoSpawnWitherStorm", false, "Specifies if the Wither Storm should automatically be spawned upon world creation"
         );
         this.autoSpawnTime = this.createRangedIntValue(
            1, 0, 120, "autoSpawnTime", false, "Specifies the amount of time (in minutes) required for the Wither Storm to automatically spawn, if enabled"
         );
         builder.pop();
      }
   }

   public static class ServerConfig extends ConfigHelper {
      public final ConfigValue<Boolean> shouldChunkLoadWhenNoPlayers;
      public final ConfigValue<Boolean> shouldChaseWhenTargetStopped;
      public final ConfigValue<Integer> targetStationaryChunkRadius;
      public final ConfigValue<Double> evolutionAttributeModifier;
      public final ConfigValue<Integer> targetStationaryMinutes;
      public final ConfigValue<Integer> targetRunawayMinutes;
      public final ConfigValue<Integer> invulnerabilityTime;
      public final ConfigValue<Double> chasingFlyingSpeed;
      public final ConfigValue<Double> normalFlyingSpeed;
      public final ConfigValue<Boolean> usePhaseAsDistanceMultiplier;
      public final ConfigValue<Double> distanceMultiplier;
      public final ConfigValue<Boolean> chaseOnPhaseChange;
      public final ConfigValue<Boolean> targetRunawayAttempts;
      public final ConfigValue<Integer> targetRunawayAttemptMinutes;
      public final ConfigValue<Integer> targetRunawayAttemptsRequired;
      public final ConfigValue<Integer> minutesTillRunawayAttemptDiminish;
      public final ConfigValue<Boolean> targettingDistractionsEnabled;
      public final ConfigValue<Integer> distractionTimeMinutes;
      public final ConfigValue<Integer> maximumDistractionDistance;
      public final ConfigValue<Integer> minimumDistractionDistance;
      public final ConfigValue<Boolean> randomDistractionChances;
      public final ConfigValue<Integer> searchRangeMultiplier;
      public final ConfigValue<Integer> distractionWaitTime;
      public final ConfigValue<Boolean> clustersRemoveItems;
      public final ConfigValue<Boolean> squashHitbox;
      public final ConfigValue<Integer> hunchbackClusterPickupInterval;
      public final ConfigValue<Integer> clusterPickupInterval;
      public final ConfigValue<Integer> devourerClusterPickupInterval;
      public final ConfigValue<Integer> tractorBeamBlockSearchRadius;
      public final ConfigValue<Integer> flyingHeight;
      public final ConfigValue<Boolean> dynamicFlyingHeight;
      public final ConfigValue<Integer> dynamicFlyingHeightTime;
      public final ConfigValue<Integer> tillShouldShowHole;
      public final ConfigValue<Double> rotationSpeed;
      public final ConfigValue<Boolean> canPickupMobClusters;
      public final ConfigValue<Boolean> witherStormInvulnerability;
      public final ConfigValue<Boolean> amuletOverride;
      public final ConfigValue<Boolean> smartBossbar;
      public final ConfigValue<Integer> headEscapeTime;
      public final ConfigValue<Boolean> randomBowelsEntrace;
      public final ConfigValue<Integer> chunkLoadingRadius;
      public final ConfigValue<Boolean> randomStrollingWhenTargetHidden;
      public final ConfigValue<Boolean> boatingForTooLongDistractions;
      public final ConfigValue<Integer> boatingForTooLongSeconds;
      public final ConfigValue<Integer> maxRandomStrollTargetingTypeRadius;
      public final ConfigValue<Boolean> ignoreUltimateTargetIfHidden;
      public final ConfigValue<Boolean> caveRumbles;
      public final ConfigValue<Boolean> crossbowsSupportEnderPearls;
      public final ConfigValue<Boolean> removeNearbyJunk;
      public final ConfigValue<Boolean> mobsRunIntoPortals;
      public final ConfigValue<Boolean> preventWitherStormCamping;
      public final ConfigValue<Boolean> occludeSoundsUnderground;
      public final ConfigValue<UltimateTargetManager.TargetingType> ultimateTargetingType;
      public final ConfigValue<Boolean> witherSicknessEnabled;
      public final ConfigValue<Boolean> sickenedMobConversions;
      public final ConfigValue<Boolean> increaseAmplifier;
      public final ConfigValue<Integer> requiredContacts;
      public final ConfigValue<Integer> requiredProximitySeconds;
      public final ConfigValue<Integer> applicationDelay;
      public final ConfigValue<Integer> cureDelay;
      public final ConfigValue<Integer> lowImmuneRequiredProximitySeconds;
      public final ConfigValue<Integer> lowImmuneApplicationDelay;
      public final ConfigValue<Integer> lowImmuneCureDelay;
      public final ConfigValue<Integer> proximitySecondsModifierMax;
      public final ConfigValue<Integer> applicationDelayModifierMax;
      public final ConfigValue<Integer> cureDelayModifierMax;
      public final ConfigValue<Integer> lowImmuneProximityModifierMax;
      public final ConfigValue<Integer> lowImmuneApplicationModifierMax;
      public final ConfigValue<Integer> lowImmuneCureDelayModifierMax;
      public final ConfigValue<Boolean> keepSicknessAfterRespawn;
      public final ConfigValue<Integer> craftFuseTicks;
      public final ConfigValue<Integer> catchFireFuseTicks;
      public final ConfigValue<Integer> dropInterval;
      public final ConfigValue<Boolean> shouldDropFromInventory;
      public final ConfigValue<Boolean> lowerBlockResistance;
      public final ConfigValue<Boolean> revivalTimer;
      public final ConfigValue<Integer> revivalTimeMinutes;
      public final ConfigValue<Integer> revivalPlayerProtection;
      public final ConfigValue<Boolean> canSummonSymbiont;
      public final ConfigValue<Integer> minimumSpawnCheckInterval;
      public final ConfigValue<Integer> witherStormSummoningDelay;
      public final ConfigValue<Integer> playerInvulnerableTime;
      public final ConfigValue<Integer> playerSummoningDelay;
      public final ConfigValue<Integer> playerSummoningDelayOnKill;
      public final ConfigValue<Double> flamingSkullExplosionSize;
      public final ConfigValue<Double> flamingSkullSpeedModifier;
      public final ConfigValue<Double> tractorPullSpeedModifier;
      public final ConfigValue<Boolean> bowelsFallResistance;
      public final ConfigValue<Boolean> flyingDisabledWarning;
      public final ConfigValue<Boolean> shouldShowHole;
      public final ConfigValue<Integer> resummonedPhase;
      public final ConfigValue<Integer> minimumRoarInterval;
      public final ConfigValue<Integer> maximumRoarInterval;
      public final ConfigValue<Integer> clusterSizeModifier;
      public final ConfigValue<Boolean> attackableWhenNotVulnerable;
      public final ConfigValue<Boolean> bookDropsInInventory;
      public final ConfigValue<Boolean> canAttackHeads;
      public final ConfigValue<Double> healthScalePerPlayer;
      public final ConfigValue<Integer> farthestTargetingTime;
      public final ConfigValue<Integer> randomizedTargetingTime;
      public final ConfigValue<Boolean> randomlySpeedUpWithTargetChange;
      public final ConfigValue<Boolean> tractorBeamClusterPickUp;
      public final ConfigValue<Boolean> tractorBeamsRemoveFluids;
      public final ConfigValue<Double> blockClusterPullSpeedModifier;
      public final ConfigValue<Double> tractorBeamClusterSpeedModifier;
      public final ConfigValue<Double> caveRumbleIntensity;
      public final ConfigValue<Boolean> chanceForExtendedRumbles;
      public final ConfigValue<Integer> caveRumbleIntervalMin;
      public final ConfigValue<Integer> caveRumbleIntervalMax;
      public final ConfigValue<Boolean> caveRumblesMessWithRedstone;
      public final ConfigValue<Boolean> shouldSymbiontAttackMobs;
      public final ConfigValue<Boolean> formidibombFuseEnabled;
      public final ConfigValue<Boolean> endOfPhaseFiveBombableExclusively;
      public final ConfigValue<Boolean> shouldPlayGlobalSoundsCrossDimensionally;
      public final ConfigValue<Boolean> witherStormsFollowBiggerStorms;
      public final ConfigValue<Boolean> onlyTryPickingUpTractorTagged;
      public final ConfigValue<Integer> tractorBeamFluidRemovalHeight;
      public final ConfigValue<Boolean> constantBlackhole;
      public final ConfigValue<ItemPreservationCondition> itemPreservation;
      public final ConfigValue<Boolean> preserveDropsForAllMobs;
      public final ConfigValue<Boolean> instantChomp;
      public final ConfigValue<Boolean> healFromChomp;
      public final ConfigValue<Boolean> convertFallingBlocks;
      public final ConfigValue<Boolean> specialTargetingBias;
      public final ConfigValue<Integer> specialTargetingBiasChance;
      public final ConfigValue<Boolean> canClustersSpiralCounterClockwise;

      protected ServerConfig(Builder builder) {
         super(builder, "witherstormmod");
         builder.comment("Server options").push("server");
         builder.comment("Misc").push("misc");
         this.shouldChunkLoadWhenNoPlayers = this.createValue(
            false, "shouldChunkLoadWhenNoPlayers", false, "Toggle to enable/disable Wither Storm chunk loading when no players are online"
         );
         this.invulnerabilityTime = this.createRangedIntValue(
            50,
            1,
            320,
            "invulnerabilityTime",
            false,
            "Specifies the invulnerability time when the Wither Storm has been summoned, in seconds. When invulnerable, the Wither Storm doesn't move and can't be attacked"
         );
         this.flyingHeight = this.createRangedIntValue(
            75, 10, 150, "flyingHeight", false, "Specifies the height the Wither Storm will fly at during phase 4 and up"
         );
         this.dynamicFlyingHeight = this.createValue(
            false, "dynamicFlyingHeight", false, "Makes the Wither Storm randomly adjust it's flying height itself between 40 and 80"
         );
         this.dynamicFlyingHeightTime = this.createRangedIntValue(
            60,
            15,
            1200,
            "dynamicFlyingHeightTime",
            false,
            "The time (in seconds) that the Wither Storm should take before adjusting it's dynamic flying height"
         );
         this.tillShouldShowHole = this.createRangedIntValue(
            6,
            1,
            30,
            "tillShouldShowHole",
            false,
            "If the Wither Storm's ultimate target has a command block tool and the Wither Storm is at phase 7, a timer specified by this value (in minutes) + random will countdown until the hole will automatically appear"
         );
         this.shouldShowHole = this.createValue(
            true, "shouldShowHole", false, "Specifies if the bowels entrance hole in the Wither Storm's mass at the end of phase 7 should be available or not"
         );
         this.rotationSpeed = this.createRangedDoubleValue(0.1, 0.1, 1.0, "rotationSpeed", false, "Specifies the rotation speed of the Wither Storm");
         this.witherStormInvulnerability = this.createValue(
            true, "witherStormInvulnerability", false, "Specifies if the Wither Storm should regenerate its health and be melee attackable"
         );
         this.smartBossbar = this.createValue(
            true,
            "smartBossbar",
            false,
            "The Wither Storm's bossbar and boss music will automatically toggle based on whether the player is underground or not. Disable to revert back to default bossbar/boss theme logic"
         );
         this.randomBowelsEntrace = this.createValue(
            true,
            "randomBowelsEntrance",
            false,
            "Specifies if players should spawn somewhere random when entering the bowels. Disable to revert to a static entrance position"
         );
         this.crossbowsSupportEnderPearls = this.createValue(
            true,
            "crossbowsSupportEnderPearls",
            false,
            "Specifies if crossbows should be able to also shoot ender pearls. Disable if facing compatibility issues"
         );
         this.preventWitherStormCamping = this.createValue(
            true,
            "preventWitherStormCamping",
            false,
            "Specifies if players should temporarily respawn away from the Wither Storm when they die to it, if their respawn point is near the Storm"
         );
         this.bowelsFallResistance = this.createValue(
            true,
            "bowelsFallResistance",
            false,
            "Specifies if max strength damange resistance should be given to the player when falling out of the bowels, to mitigate fall damage"
         );
         this.resummonedPhase = this.createRangedIntValue(
            4, 0, 7, "resummonedPhase", false, "Specifies the phase the Wither Storm should be set to when resummoned by a withered beacon"
         );
         this.canAttackHeads = this.createValue(true, "canAttackHeads", false, "Specifies if the Wither Storm's heads can be attacked");
         this.endOfPhaseFiveBombableExclusively = this.createValue(
            false, "endOfPhaseFiveBombableExclusively", false, "Specifies if only the end of phase five can be formidibombed, instead of all of phase 5"
         );
         this.shouldPlayGlobalSoundsCrossDimensionally = this.createValue(
            false,
            "shouldPlayGlobalSoundsCrossDimensionally",
            false,
            "Specifies if global sounds made by the Wither Storm (e.x. evolve sound) should play cross dimensionally"
         );
         this.onlyTryPickingUpTractorTagged = this.createValue(
            false,
            "onlyTryPickingUpTractorTagged",
            false,
            "Makes the Wither Storm attempt to only grab blocks that were tagged in the Tractor Distractions json"
         );
         this.constantBlackhole = this.createValue(
            false, "constantBlackhole", false, "CAUTION THIS WILL BE EXTREMELY LAGGY, THIS SETS THE WITHER STORM TO HAVE NO SMALL CLUSTER COOLDOWN"
         );
         this.instantChomp = this.createValue(
            false,
            "instantChomp",
            false,
            "Specifies if a Wither Storm head should immediately kill the player upon biting them. Does not apply to heads inside the bowels"
         );
         this.healFromChomp = this.createValue(
            false, "healFromChomp", false, "Specifies if the Wither Storm should heal whenever it eats a mob (determined by half the mob's max health)"
         );
         builder.pop();
         builder.comment("Ultimate Target Logic").push("ultimate_target_logic");
         this.ultimateTargetingType = this.createEnumValue(
            UltimateTargetManager.TargetingType.NEAREST,
            "ultimateTargetingType",
            false,
            "Specifies the targeting type the Wither Storm should use to determine it's ultimate target"
         );
         this.farthestTargetingTime = this.createRangedIntValue(
            15,
            1,
            60,
            "farthestTargetingTime",
            false,
            "Makes the Wither Storm target the player that was farthest from it while using FARTHEST targeting for the configured amount of time (in minutes)"
         );
         this.randomizedTargetingTime = this.createRangedIntValue(
            15,
            1,
            60,
            "randomizedTargetingTime",
            false,
            "If you're using RANDOMIZED targeting you can use this to set how often (in minutes) the Wither Storm should switch its targeting type"
         );
         this.randomlySpeedUpWithTargetChange = this.createValue(
            true,
            "randomlySpeedUpWithTargetChange",
            false,
            "When set to true the Wither Storm will have a 10% chance to speedup when it changes it's targeting type while using RANDOMIZED targeting"
         );
         this.amuletOverride = this.createValue(true, "amuletOverride", false, "Specifies if the amulet should override the Wither Storm's targeting");
         this.maxRandomStrollTargetingTypeRadius = this.createRangedIntValue(
            500,
            200,
            5000,
            "maxRandomStrollTargetingTypeRadius",
            false,
            "The max amount of distance a Wither Storm can travel at once when using the RANDOM_STROLL or RANDOM_STROLL_NEAR_PLAYER targeting type"
         );
         this.ignoreUltimateTargetIfHidden = this.createValue(
            true,
            "ignoreUltimateTargetIfHidden",
            false,
            "If the Wither Storm's ultimate target is hidden (bossbar is not visible for that target) for an extended period of time, it will ignore that target temporarily and go for a different player. Toggle to disable. NOTE: Only functional on a multiplayer environment"
         );
         this.witherStormsFollowBiggerStorms = this.createValue(
            true, "witherStormsFollowBiggerStorms", false, "Specifies if smaller Wither Storms should follow one with more consumed entities"
         );
         builder.comment("Chases").push("chases");
         this.shouldChaseWhenTargetStopped = this.createValue(
            true,
            "shouldChaseWhenTargetStopped",
            false,
            "If the ultimate target is stationary for a specific amount of time, the Wither Storm will begin to move towards it at a faster rate (chase). Toggle to enable/disable this feature"
         );
         this.chaseOnPhaseChange = this.createValue(
            true, "chaseOnPhaseChange", false, "Specifies if the Wither Storm should accelerate when it evolves into the next phase."
         );
         builder.pop();
         builder.comment("Target Stationary Logic").push("target_stationary_logic");
         this.targetStationaryChunkRadius = this.createRangedIntValue(
            8,
            0,
            16,
            "targetStationaryChunkRadius",
            true,
            "If the player remains in a radius of chunks specified by this value for a specific amount of time, the Wither Storm will begin to move towards the player at a faster rate"
         );
         this.targetStationaryMinutes = this.createRangedIntValue(
            30,
            1,
            120,
            "targetStationaryMinutes",
            true,
            "Specifies the amount of time in minutes that the Wither Storm's ultimate target must be stationary in order for it to accelerate"
         );
         this.usePhaseAsDistanceMultiplier = this.createValue(
            true,
            "usePhaseAsDistanceMultiplier",
            false,
            "Specifies if the Wither Storm should use the phase as a multiplier when calculating the time in ticks it takes for it to start accelerating, when its ultimate target is stationary"
         );
         this.distanceMultiplier = this.createRangedDoubleValue(
            1.0,
            0.1,
            24.0,
            "distanceMultiplier",
            false,
            "Specifies if the Wither Storm should use this value as a multiplier when calculating the time in ticks it takes for it to start accelerating, when its ultimate target is stationary"
         );
         this.targetRunawayMinutes = this.createRangedIntValue(
            10,
            1,
            90,
            "targetRunawayMinutes",
            false,
            "Specifies the amount of time in minutes that the Wither Storm's ultimate target has begun to run away in order to slow back down again"
         );
         builder.pop();
         builder.comment("Runaway Attempts").push("runaway_attempts");
         this.targetRunawayAttempts = this.createValue(
            true,
            "targetRunawayAttempts",
            false,
            "Specifies if the Wither Storm should count the times its ultimate target leaves the stationary chunk radius. After a specified amount of attempts has been reached, the Wither Storm will accelerate to its Target Stationary Speed"
         );
         this.targetRunawayAttemptMinutes = this.createRangedIntValue(
            2,
            1,
            20,
            "targetRunawayAttemptMinutes",
            false,
            "Specifies the required time, in minutes, the Wither Storms ultimate target must be stationary in order to be able to count a runaway attempt when it tries to leave the stationary chunk radius"
         );
         this.targetRunawayAttemptsRequired = this.createRangedIntValue(
            5,
            1,
            32,
            "targetRunawayAttemptsRequired",
            false,
            "Specifies the amount of runaway attempts made by the Wither Storms ultimate target that must be met for it to begin a chase"
         );
         this.minutesTillRunawayAttemptDiminish = this.createRangedIntValue(
            16,
            1,
            48,
            "minutesTillRunawayAttemptDiminish",
            false,
            "Specifies the required time that must pass (in minutes) in order to decrease the runaway attempt amount. This timer only counts if the Wither Storms ultimate target remains in the stationary chunk radius"
         );
         builder.pop();
         builder.comment("Distractions").push("distractions");
         this.targettingDistractionsEnabled = this.createValue(
            true,
            "targettingDistractionsEnabled",
            false,
            "Specifies when the Wither Storm is done chasing its ultimate target if it should become 'distracted' and go to a random nearby area"
         );
         this.distractionTimeMinutes = this.createRangedIntValue(
            25,
            1,
            25,
            "distractionTimeMinutes",
            false,
            "Specifies the time in minutes that the Wither Storm should be distracted for, plus some random modifying and more"
         );
         this.maximumDistractionDistance = this.createRangedIntValue(
            1000,
            100,
            3000,
            "maximumDistractionDistance",
            false,
            "Distractions will not occur if the Wither Storm's ultimate target is outside the radius determined by this value + its target range. Although, if the ultimate target is inside the radius, but then leaves, a distraction can then later occur"
         );
         this.minimumDistractionDistance = this.createRangedIntValue(
            50,
            10,
            500,
            "minimumDistractionDistance",
            false,
            "Distractions will not occur IMMEDIATELY if the Wither Storm's ultimate target is INSIDE the radius determined by this value + its target range. If the target is inside this radius, the Wither Storm will wait before becoming distracted, to see if the target is outside the radius. If not, a distraction will not occur. Set to 0 to disable"
         );
         this.randomDistractionChances = this.createValue(
            true,
            "randomDistractionChances",
            false,
            "Distractions may or may not occur if the conditions are met/unmet based off of a random chance. Toggle to enable/disable"
         );
         this.searchRangeMultiplier = this.createRangedIntValue(
            1, 1, 8, "searchableRangeMultiplier", false, "Specifies the search radius multiplier for when searching for a random location to go to"
         );
         this.distractionWaitTime = this.createRangedIntValue(
            2,
            1,
            20,
            "distractionWaitTime",
            false,
            "For when the Wither Storm needs to wait to become distracted, this value (in minutes) will specifiy the wait time, plus a random modifier"
         );
         this.boatingForTooLongDistractions = this.createValue(
            true,
            "boatingForTooLongDistractions",
            false,
            "Specifies if the Wither Storm should get distracted when a player boats across water for too long. Used to prevent specific scenarios where the Wither Storm is chasing players but it can't quite keep up"
         );
         this.boatingForTooLongSeconds = this.createRangedIntValue(
            60, 30, 300, "boatingForTooLongSeconds", false, "The amount of time a player needs to be boating until the Wither Storm will become distracted"
         );
         builder.pop();
         builder.comment("Random Strolling").push("random_strolling");
         this.randomStrollingWhenTargetHidden = this.createValue(
            true, "randomStrollingWhenTargetHidden", false, "Specifies if the Wither Storm should stroll around the player if they're hidden from the storm"
         );
         builder.pop();
         builder.comment("Speed").push("speed");
         this.chasingFlyingSpeed = this.createRangedDoubleValue(
            0.4,
            0.01,
            1.0,
            "chasingFlyingSpeed",
            true,
            "Specifies a modifier value of Target Stationary Flying Speed attribute. The higher the value, the faster the Wither Storm will go when its ultimate target is stationary"
         );
         this.normalFlyingSpeed = this.createRangedDoubleValue(
            0.02,
            0.01,
            1.0,
            "normalFlyingSpeed",
            true,
            "Specifies a modifier value of Slow Flying Speed attribute. The higher the value, the faster the Wither Storm will go when it's ultimate target is not stationary"
         );
         builder.pop();
         builder.pop();
         builder.comment("Targeting").push("targeting");
         this.headEscapeTime = this.createRangedIntValue(
            40,
            0,
            60,
            "headEscapeTime",
            false,
            "The targeting invulnerability time in seconds players who escape from a Wither Storm after injuring a head get"
         );
         this.tractorPullSpeedModifier = this.createRangedDoubleValue(
            0.2, 0.1, 1.0, "tractorPullSpeedModifier", false, "Modifies the tractor beam pull speed, higher = faster"
         );
         this.specialTargetingBias = this.createValue(
            true, "specialTargetingBias", false, "Specifies if certain mobs (by default, players) should be targeted over others"
         );
         this.specialTargetingBiasChance = this.createRangedIntValue(
            75, 0, 100, "specialTargetingBiasChance", false, "The percent chance certain mobs (by default, players) should be picked up over others"
         );
         builder.pop();
         builder.comment("Evolution").push("evolution");
         this.evolutionAttributeModifier = this.createRangedDoubleValue(
            1.0,
            0.01,
            32.0,
            "evolutionAttributeModifier",
            false,
            "Specifies a modifier value of the evolution rate attribute. The higher the value, the longer it takes for the Wither Storm to make a complete evolution (from phases 0 to 7), and vice versa. Cannot be lower than 0"
         );
         builder.pop();
         builder.comment("Performance").push("performance");
         this.clustersRemoveItems = this.createValue(
            true,
            "clustersRemoveItems",
            false,
            "Specifies if Block Clusters should remove non-important items in its path. NOTE: Disabling can cause major lag"
         );
         this.squashHitbox = this.createValue(
            false,
            "squashHitbox",
            true,
            "If true, the hitbox of the Wither Storm and Wither Storm Segment's will be shrunk vertically to one block. Enable if facing major server lag in the bigger Wither Storm phases"
         );
         this.chunkLoadingRadius = this.createRangedIntValue(12, 6, 32, "chunkLoadingRadius", true, "Specifies the chunk loading radius for the Wither Storm");
         this.removeNearbyJunk = this.createValue(
            true,
            "removeNearbyJunk",
            false,
            "Specifies if junk items near the Wither Storm should be immediately destroyed. NOTE: Disabling will cause massive server side and potential FPS lag!"
         );
         this.mobsRunIntoPortals = this.createValue(
            true, "mobsRunIntoPortals", false, "Specifies if mobs should go into nearby nether portals when running away from a Wither Storm"
         );
         builder.pop();
         builder.comment("World Consumption").push("world_consumption");
         this.hunchbackClusterPickupInterval = this.createRangedIntValue(
            20,
            10,
            80,
            "hunchbackClusterPickupInterval",
            false,
            "Alters the interval (in ticks) of picking up block clusters for the hunchback phases (phase 0 - 3). NOTE: This value changes the evolution speed of the Wither Storm significantly"
         );
         this.clusterPickupInterval = this.createRangedIntValue(
            40,
            10,
            80,
            "clusterPickupInterval",
            false,
            "Alters the interval (in ticks) of picking up block clusters for the destroyer phases (phase 4 - 5). NOTE: This value changes the evolution speed of the Wither Storm significantly"
         );
         this.devourerClusterPickupInterval = this.createRangedIntValue(
            40,
            10,
            80,
            "devourerClusterPickupInterval",
            false,
            "Alters the interval (in ticks) of picking up block clusters for the devourer phases (phase 6+). NOTE: This value changes the evolution speed of the Wither Storm significantly"
         );
         this.canPickupMobClusters = this.createValue(
            true, "canPickupMobClusters", false, "Specifies if the Wither Storm's tractor beams can pull in multiple mobs at once that interesect the beam"
         );
         this.clusterSizeModifier = this.createRangedIntValue(
            0,
            0,
            16,
            "clusterSizeModifier",
            false,
            "Increases the radius of block clusters linearly by this amount. NOTE: Greatly impacts the Wither Storm's evolution rate. Greater values also lead to worse performance!"
         );
         this.tractorBeamClusterPickUp = this.createValue(
            true, "tractorBeamClusterPickUp", false, "Specifies if the Wither Storm should be able to pick up block clusters with its tractor beams"
         );
         this.tractorBeamsRemoveFluids = this.createValue(
            true, "tractorBeamsRemoveFluids", false, "Makes it so the Wither Storm will remove fluids above Y 63 with its tractor beams"
         );
         this.blockClusterPullSpeedModifier = this.createRangedDoubleValue(
            1.0,
            0.1,
            10.0,
            "blockClusterPullSpeedModifier",
            false,
            "Modifies the Wither Storm's block cluster pull speed by multiplying the default speed with this value"
         );
         this.tractorBeamClusterSpeedModifier = this.createRangedDoubleValue(
            1.0,
            0.1,
            10.0,
            "tractorBeamClusterSpeedModifier",
            false,
            "Multiplies the speed of block clusters being pulled in by a Wither Storm's tractor beam by this value"
         );
         this.tractorBeamBlockSearchRadius = this.createRangedIntValue(
            10,
            4,
            256,
            "tractorBeamBlockSearchRadius",
            false,
            "How far the tractor beams should search for distraction blocks, NOTE: High values may cause lag"
         );
         this.canClustersSpiralCounterClockwise = this.createValue(
            false,
            "canClustersSpiralCounterClockwise",
            false,
            "Specifies if block clusters can have a chance to spiral counter clockwise when being consumed by the Wither Storm"
         );
         this.convertFallingBlocks = this.createValue(
            false,
            "convertFallingBlocks",
            false,
            "Specifies if the Wither Storm should convert falling blocks into clusters (NOTE: This can be very laggy and cause the Wither Storm to evolve fast!)"
         );
         this.tractorBeamFluidRemovalHeight = this.createRangedIntValue(
            63,
            -64,
            320,
            "tractorBeamFluidRemovalHeight",
            false,
            "Specifies the height of which tractor beams should remove water. The default of 63 is ocean height and upwards"
         );
         builder.pop();
         builder.comment("Caves").push("caves");
         this.caveRumbles = this.createValue(
            true,
            "caveRumbles",
            false,
            "Specifies if the screen should shake and other various cave rumble effects should occur when underground and near the Storm"
         );
         this.occludeSoundsUnderground = this.createValue(
            true, "occludeSoundsUnderground", false, "Specifies if sounds from the Wither Storm should be heard when deep underground"
         );
         this.caveRumbleIntensity = this.createRangedDoubleValue(
            0.25, 0.0, 1.0, "caveRumbleIntensity", false, "Higher values makes cave rumbles more 'intense,' and makes more dripstone and glowberries fall"
         );
         this.chanceForExtendedRumbles = this.createValue(
            true, "chanceForExtendedRumbles", false, "Specifies if a random chance for extended cave rumbles to occur should be possible"
         );
         this.caveRumbleIntervalMin = this.createRangedIntValue(
            60, 5, 1800, "caveRumbleIntervalMin", false, "Specifies the minimum interval possible (in seconds) between cave rumbles"
         );
         this.caveRumbleIntervalMax = this.createRangedIntValue(
            180, 5, 1800, "caveRumbleIntervalMax", false, "Specifies the maximum interval possible (in seconds) between cave rumbles"
         );
         this.caveRumblesMessWithRedstone = this.createValue(
            true, "caveRumblesMessWithRedstone", false, "Specifies if cave rumbles should mess with redstone type blocks"
         );
         builder.pop();
         builder.comment("Wither Sickness").push("wither_sickness");
         this.witherSicknessEnabled = this.createValue(true, "witherSicknessEnabled", true, "Specifies if mobs should be able to receive wither sickness");
         this.sickenedMobConversions = this.createValue(
            true, "sickenedMobConversions", false, "Specifies if mobs should convert to sickened mobs once they die from wither sickness"
         );
         this.increaseAmplifier = this.createValue(
            true,
            "increaseAmplifier",
            false,
            "Specifies if entities who are reinfected over a short period of time should receive wither sickness with a greater strength"
         );
         this.requiredContacts = this.createRangedIntValue(
            6, 1, 40, "requiredContacts", true, "After a mob has been targetted by the Wither Storm a set maximum of times, the mob will become infected"
         );
         this.requiredProximitySeconds = this.createRangedIntValue(
            600,
            12,
            1200,
            "requiredProximitySeconds",
            true,
            "Specifies the amount of time in seconds high immunity mobs (players) must be near the Wither Storm in order to to begin infection"
         );
         this.applicationDelay = this.createRangedIntValue(
            720,
            12,
            1200,
            "applicationDelay",
            true,
            "Specifies the amount of time in seconds high immunity mobs (players) must be infected in order to be applied the wither sickness effect"
         );
         this.cureDelay = this.createRangedIntValue(
            480, 12, 1200, "cureDelay", true, "Specifies the delay, in seconds, before high immunity mobs (players) are cured of wither sickness"
         );
         this.lowImmuneRequiredProximitySeconds = this.createRangedIntValue(
            360,
            12,
            1200,
            "lowImmuneRequiredProximitySeconds",
            true,
            "Specifies the amount of time in seconds that low immunity mobs must be near the Wither Storm in order to begin infection"
         );
         this.lowImmuneApplicationDelay = this.createRangedIntValue(
            410,
            12,
            1200,
            "lowImmuneApplicationDelay",
            true,
            "Specifies the amount of time in seconds low immunity mobs must be infected in order to be applied the wither sickness effect"
         );
         this.lowImmuneCureDelay = this.createRangedIntValue(
            480, 12, 1200, "lowImmuneCureDelay", true, "Specifies the delay, in seconds, before low immunity mobs are cured of wither sickness"
         );
         this.proximitySecondsModifierMax = this.createRangedIntValue(
            180,
            12,
            1200,
            "proximitySecondsModifierMax",
            false,
            "High immunity mobs (players) will be assigned a random proximity seconds modifier that will change the proximity seconds time. This value will set the maximum potential limit for that modifier, in seconds"
         );
         this.applicationDelayModifierMax = this.createRangedIntValue(
            300,
            12,
            1200,
            "applicationDelayModifierMax",
            false,
            "High immunity mobs (players) will be assigned a random application delay modifier that will change the application delay. This value will set the maximum potential limit for that modifier, in seconds"
         );
         this.cureDelayModifierMax = this.createRangedIntValue(
            180,
            12,
            1200,
            "cureDelayModifierMax",
            false,
            "High immunity mobs (players) will be assigned a random cure delay modifier that will change the cure delay. This value will set the maximum potential limit for that modifier, in seconds"
         );
         this.lowImmuneProximityModifierMax = this.createRangedIntValue(
            180,
            12,
            1200,
            "lowImmuneProximityModifierMax",
            false,
            "Low immunity mobs will be assigned a random proximity seconds delay modifier that will change the proximity seconds time. This value will set the maximum potential limit for that modifier, in seconds"
         );
         this.lowImmuneApplicationModifierMax = this.createRangedIntValue(
            140,
            12,
            1200,
            "lowImmuneApplicationModifierMax",
            false,
            "Low immunity mobs will be assigned a random application delay modifier that will change the proximity seconds time. This value will set the maximum potential limit for that modifier, in seconds"
         );
         this.lowImmuneCureDelayModifierMax = this.createRangedIntValue(
            180,
            12,
            1200,
            "lowImmuneCureDelayModifierMax",
            false,
            "Low immunity mobs will be assigned a random cure delay modifier that will change the cure delay time. This value will set the maximum potential limit for that modifier, in seconds"
         );
         this.keepSicknessAfterRespawn = this.createValue(
            true, "keepSicknessAfterRespawn", false, "Specifies if Wither Sickness should be removed after a player respawns"
         );
         builder.pop();
         builder.comment("Formidibomb").push("formidibomb");
         this.craftFuseTicks = this.createRangedIntValue(
            12000,
            1,
            12000,
            "craftFuseTicks",
            false,
            "Specifies the fuse in ticks that count down to the formidibomb's explosion. The fuse is set when the block is crafted"
         );
         this.catchFireFuseTicks = this.createRangedIntValue(
            1200,
            1,
            12000,
            "catchFireFuseTicks",
            false,
            "If the formidibomb is manually set on fire the fuse tick count will be set to this value, if the original fuse is greater than this value"
         );
         this.shouldDropFromInventory = this.createValue(
            true,
            "shouldDropFromInventory",
            false,
            "Specifies if after a set amount of time the formidibomb item should drop out of whatever inventory is holding it and should spawn as the entity"
         );
         this.dropInterval = this.createRangedIntValue(
            4,
            1,
            8,
            "dropInterval",
            false,
            "Specifies the interval for when the formidibomb will drop out of its inventory (if enabled). This value divides the crafted fuse ticks to get the interval (e.x. 12000 / 4 = 3000)"
         );
         this.lowerBlockResistance = this.createValue(
            true,
            "lowerBlockResistance",
            false,
            "Lowers the resistance of blocks in the path of the explosion, effectively increasing the strength of the explosion which allows for obsidian, etc. to be destroyed"
         );
         this.formidibombFuseEnabled = this.createValue(
            true, "formidibombFuseEnabled", false, "Specifies if a formidibomb's auto detonation fuse should be enabled"
         );
         builder.pop();
         builder.comment("Playing dead").push("playing_dead");
         this.revivalTimer = this.createValue(
            true, "revivalTimer", false, "Specifies if the Wither Storm should automatically revive if not found after a period of time"
         );
         this.revivalTimeMinutes = this.createRangedIntValue(
            60, 1, 120, "revivalTimeMinutes", false, "Specifies when (in minutes) the Wither Storm will automatically revive when playing dead"
         );
         this.revivalPlayerProtection = this.createRangedIntValue(
            3, 1, 40, "revivalPlayerProtection", false, "Specifies the time (in minutes) after being revived that the Wither Storm should ignore players"
         );
         builder.pop();
         builder.comment("Withered Symbiont").push("withered_symbiont");
         this.canSummonSymbiont = this.createValue(true, "canSummonSymbiont", false, "Specifies if the Wither Storm can summon the Withered Symbiont");
         this.shouldSymbiontAttackMobs = this.createValue(
            false,
            "shouldSymbiontAttackMobs",
            false,
            "Specifies if the Withered Symbiont is hostile towards other mobs (This is only applied to Symbionts spawned after this change is applied)"
         );
         this.minimumSpawnCheckInterval = this.createRangedIntValue(
            60,
            1,
            240,
            "minimumSpawnCheckInterval",
            false,
            "Specifies the minimum interval (+random) in seconds that the Wither Storm should check for Withered Symbiont spawn conditions"
         );
         this.witherStormSummoningDelay = this.createRangedIntValue(
            10,
            1,
            20,
            "witherStormSummoningDelay",
            false,
            "Specifies the delay in minutes (+random) that the Wither Storm will be able to summon a Withered Symbiont"
         );
         this.playerInvulnerableTime = this.createRangedIntValue(
            5,
            1,
            10,
            "playerInvulnerableTime",
            false,
            "Specifies the time in minutes (+random) that the players who killed a Withered Symbiont should be ignored by the Wither Storm for"
         );
         this.playerSummoningDelay = this.createRangedIntValue(
            10,
            1,
            60,
            "playerSummoningDelay",
            false,
            "Specifies the delay in minutes (+random) that the Wither Storm should be able summon a Withered Symbiont for the player it summoned one for"
         );
         this.playerSummoningDelayOnKill = this.createRangedIntValue(
            40,
            1,
            60,
            "playerSummoningDelayOnKill",
            false,
            "If a player kills a Withered Symbiont, their sumoning delay will be increased to this value in minutes (+random)"
         );
         this.attackableWhenNotVulnerable = this.createValue(
            false, "attackableWhenNotVulnerable", false, "Specifies if the Withered Symbiont can be attacked from behind if it's not vulnerable"
         );
         this.bookDropsInInventory = this.createValue(
            true,
            "bookDropsInInventory",
            false,
            "If multiple players are near the Withered Symbiont, the command block book will automatically drop into the inventory of the player the Symbiont was spawned for, if they are nearby and have inventory space"
         );
         this.healthScalePerPlayer = this.createRangedDoubleValue(
            20.0,
            0.0,
            100.0,
            "healthScalePerPlayer",
            false,
            "Adds this value multiplied by the amount of nearby players to the Symbiont's max health when it is spawned. Set to zero to disable this feature"
         );
         builder.pop();
         builder.comment("Flaming Skulls").push("flaming_skulls");
         this.flamingSkullExplosionSize = this.createRangedDoubleValue(
            5.0, 1.0, 16.0, "flamingSkullExplosionSize", false, "The flaming skull explosion size when they collide with blocks"
         );
         this.flamingSkullSpeedModifier = this.createRangedDoubleValue(
            1.0, 0.5, 8.0, "flamingSkullSpeedModifier", false, "The speed modifier for flaming wither skulls. Higher = faster"
         );
         builder.pop();
         builder.comment("Roaring").push("roaring");
         this.minimumRoarInterval = this.createRangedIntValue(
            20,
            1,
            100,
            "minimumRoarInterval",
            false,
            "Specifies the lowest time, in seconds, it will take for one of the Wither Storm's heads to initiate a roar and shoot a flaming skull"
         );
         this.maximumRoarInterval = this.createRangedIntValue(
            50,
            1,
            100,
            "maximumRoarInterval",
            false,
            "Specifies the greatest time, in seconds, it will take for one of the Wither Storm's heads to initiate a roar and shoot a flaming skull"
         );
         builder.pop();
         builder.comment("Item Preservation").push("item_preservation");
         this.itemPreservation = this.createEnumValue(
            ItemPreservationCondition.CHOMPED_OR_KILLED_NEAR_HEAD,
            "itemPreservation",
            false,
            "When a player dies, a block cluster containing their items will be created into the world based on the condition defined by this value. Acts as a gravestone"
         );
         this.preserveDropsForAllMobs = this.createValue(
            false, "preserveDropsForAllMobs", false, "Specifies if drops should be preserved using block clusters (gravestones) for all mobs, not just players"
         );
         builder.pop();
         this.flyingDisabledWarning = this.createValue(
            true, "flyingEnabledWarning", false, "Specifies if a warning should be printed out to server operators if flying is disabled"
         );
         builder.pop();
      }
   }
}
