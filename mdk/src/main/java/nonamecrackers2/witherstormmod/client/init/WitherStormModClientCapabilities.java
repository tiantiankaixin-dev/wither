package nonamecrackers2.witherstormmod.client.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.capabilities.RegisterCapabilitiesEvent;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: LazyOptional removed, new Capability API returns T or null
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: AttachCapabilitiesEvent removed, use RegisterCapabilitiesEvent (see CAPABILITY_AUDIT.md)
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.client.audio.ISoundManager;
import nonamecrackers2.witherstormmod.client.audio.bosstheme.BossThemeManager;
import nonamecrackers2.witherstormmod.client.capability.BowelsEffectsManager;
import nonamecrackers2.witherstormmod.client.capability.CommandBlockSoundManager;
import nonamecrackers2.witherstormmod.client.capability.FormidiBladeLoopManager;
import nonamecrackers2.witherstormmod.client.capability.FormidibombEffectsManager;
import nonamecrackers2.witherstormmod.client.capability.PlayerCameraShaker;
import nonamecrackers2.witherstormmod.client.capability.PlayerScreenBlinder;
import nonamecrackers2.witherstormmod.client.capability.PlayerTractorBeamEffects;
import nonamecrackers2.witherstormmod.client.capability.SoundManagersHolder;
import nonamecrackers2.witherstormmod.client.capability.WitherStormDistantRenderer;
import nonamecrackers2.witherstormmod.client.capability.WitherStormHeadSoundManager;
import nonamecrackers2.witherstormmod.client.capability.WitherStormLoopingSoundManager;
import nonamecrackers2.witherstormmod.client.capability.WitheredSymbiontSoundManager;
import nonamecrackers2.witherstormmod.client.capability.WitheredSymbiontSpellLoopManager;
import nonamecrackers2.witherstormmod.client.event.WitherStormAmbienceEffects;

public class WitherStormModClientCapabilities {
   public static final Capability<WitherStormDistantRenderer> DISTANT_RENDERER = CapabilityManager.get(new CapabilityToken<WitherStormDistantRenderer>() {
   });
   public static final Capability<SoundManagersHolder> SOUND_MANAGERS = CapabilityManager.get(new CapabilityToken<SoundManagersHolder>() {
   });
   public static final Capability<WitherStormLoopingSoundManager> LOOPING_MANAGER = CapabilityManager.get(
      new CapabilityToken<WitherStormLoopingSoundManager>() {
      }
   );
   public static final Capability<PlayerCameraShaker> CAMERA_SHAKER = CapabilityManager.get(new CapabilityToken<PlayerCameraShaker>() {
   });
   public static final Capability<FormidibombEffectsManager> FORMIDIBOMB_EFFECTS = CapabilityManager.get(new CapabilityToken<FormidibombEffectsManager>() {
   });
   public static final Capability<PlayerScreenBlinder> SCREEN_BLINDER = CapabilityManager.get(new CapabilityToken<PlayerScreenBlinder>() {
   });
   public static final Capability<CommandBlockSoundManager> COMMAND_BLOCK_SOUND_MANAGER = CapabilityManager.get(
      new CapabilityToken<CommandBlockSoundManager>() {
      }
   );
   public static final Capability<WitheredSymbiontSpellLoopManager> WITHERED_SYMBIONT_SPELL_LOOP_MANAGER = CapabilityManager.get(
      new CapabilityToken<WitheredSymbiontSpellLoopManager>() {
      }
   );
   public static final Capability<WitheredSymbiontSoundManager> WITHERED_SYMBIONT_SOUND_MANAGER = CapabilityManager.get(
      new CapabilityToken<WitheredSymbiontSoundManager>() {
      }
   );
   public static final Capability<BossThemeManager> BOSS_THEME_MANAGER = CapabilityManager.get(new CapabilityToken<BossThemeManager>() {
   });
   public static final Capability<BowelsEffectsManager> BOWELS_EFFECTS_MANAGER = CapabilityManager.get(new CapabilityToken<BowelsEffectsManager>() {
   });
   public static final Capability<WitherStormHeadSoundManager> WITHER_STORM_HEAD_SOUND_MANAGER = CapabilityManager.get(
      new CapabilityToken<WitherStormHeadSoundManager>() {
      }
   );
   public static final Capability<PlayerTractorBeamEffects> TRACTOR_BEAM_EFFECTS = CapabilityManager.get(new CapabilityToken<PlayerTractorBeamEffects>() {
   });
   public static final Capability<WitherStormAmbienceEffects> AMBIENT_EFFECTS = CapabilityManager.get(new CapabilityToken<WitherStormAmbienceEffects>() {
   });
   public static final Capability<FormidiBladeLoopManager> FORMIDI_BLADE_LOOP_MANAGER = CapabilityManager.get(new CapabilityToken<FormidiBladeLoopManager>() {
   });

   public static void registerCapabilities(RegisterCapabilitiesEvent event) {
      event.register(WitherStormDistantRenderer.class);
      event.register(SoundManagersHolder.class);
      event.register(WitherStormLoopingSoundManager.class);
      event.register(PlayerCameraShaker.class);
      event.register(FormidibombEffectsManager.class);
      event.register(PlayerScreenBlinder.class);
      event.register(CommandBlockSoundManager.class);
      event.register(WitheredSymbiontSpellLoopManager.class);
      event.register(WitheredSymbiontSoundManager.class);
      event.register(BossThemeManager.class);
      event.register(BowelsEffectsManager.class);
      event.register(WitherStormHeadSoundManager.class);
      event.register(PlayerTractorBeamEffects.class);
      event.register(WitherStormAmbienceEffects.class);
      event.register(FormidiBladeLoopManager.class);
   }

   public static void attachWorldCapabilities(AttachCapabilitiesEvent<Level> event) {
      Level world = (Level)event.getObject();
      if (world.isClientSide) {
         Minecraft mc = Minecraft.getInstance();
         final LazyOptional<WitherStormDistantRenderer> distantRenderer = LazyOptional.of(() -> new WitherStormDistantRenderer(mc));
         event.addCapability(ResourceLocation.fromNamespaceAndPath("witherstormmod", "distant_renderer"), new ICapabilityProvider() {
            public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
               return capability == WitherStormModClientCapabilities.DISTANT_RENDERER ? distantRenderer.cast() : null /* TODO_MIG: LazyOptional.empty() -> null */;
            }
         });
         event.addListener(distantRenderer::invalidate);
         if (world.dimension().location().equals(WitherStormMod.bowelsLocation())) {
            final LazyOptional<BowelsEffectsManager> bowelsEffects = LazyOptional.of(() -> new BowelsEffectsManager(mc));
            event.addCapability(ResourceLocation.fromNamespaceAndPath("witherstormmod", "bowels_effects_manager"), new ICapabilityProvider() {
               public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
                  return capability == WitherStormModClientCapabilities.BOWELS_EFFECTS_MANAGER ? bowelsEffects.cast() : null /* TODO_MIG: LazyOptional.empty() -> null */;
               }
            });
            event.addListener(bowelsEffects::invalidate);
         }

         final LazyOptional<WitherStormAmbienceEffects> ambientEffects = LazyOptional.of(() -> new WitherStormAmbienceEffects(mc));
         event.addCapability(ResourceLocation.fromNamespaceAndPath("witherstormmod", "wither_storm_ambience_effects"), new ICapabilityProvider() {
            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
               return cap == WitherStormModClientCapabilities.AMBIENT_EFFECTS ? ambientEffects.cast() : null /* TODO_MIG: LazyOptional.empty() -> null */;
            }
         });
         event.addListener(ambientEffects::invalidate);
         SoundManagersHolder holder = new SoundManagersHolder();
         final LazyOptional<SoundManagersHolder> holderOptional = LazyOptional.of(() -> holder);
         event.addCapability(ResourceLocation.fromNamespaceAndPath("witherstormmod", "sound_managers_holder"), new ICapabilityProvider() {
            public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
               return capability == WitherStormModClientCapabilities.SOUND_MANAGERS ? holderOptional.cast() : null /* TODO_MIG: LazyOptional.empty() -> null */;
            }
         });
         event.addListener(holderOptional::invalidate);
         registerSoundManager(event, holder, "looping_sound_manager", new WitherStormLoopingSoundManager(mc), LOOPING_MANAGER);
         registerSoundManager(event, holder, "formidibomb_effects_manager", new FormidibombEffectsManager(mc), FORMIDIBOMB_EFFECTS);
         registerSoundManager(event, holder, "command_block_sound_manager", new CommandBlockSoundManager(mc), COMMAND_BLOCK_SOUND_MANAGER);
         registerSoundManager(
            event, holder, "withered_symbiont_spell_loop_manager", new WitheredSymbiontSpellLoopManager(mc), WITHERED_SYMBIONT_SPELL_LOOP_MANAGER
         );
         registerSoundManager(event, holder, "withered_symbiont_sound_manager", new WitheredSymbiontSoundManager(mc), WITHERED_SYMBIONT_SOUND_MANAGER);
         registerSoundManager(event, holder, "boss_theme_manager", new BossThemeManager(mc), BOSS_THEME_MANAGER);
         registerSoundManager(event, holder, "wither_storm_head_sound_manager", new WitherStormHeadSoundManager(mc), WITHER_STORM_HEAD_SOUND_MANAGER);
         registerSoundManager(event, holder, "formidi_blade_loop_manager", new FormidiBladeLoopManager(mc), FORMIDI_BLADE_LOOP_MANAGER);
      }
   }

   private static <H> void registerSoundManager(
      AttachCapabilitiesEvent<Level> event, SoundManagersHolder holder, String id, ISoundManager manager, final Capability<H> capability
   ) {
      final LazyOptional<ISoundManager> optional = LazyOptional.of(() -> manager);
      event.addCapability(ResourceLocation.fromNamespaceAndPath("witherstormmod", id), new ICapabilityProvider() {
         public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == capability ? optional.cast() : null /* TODO_MIG: LazyOptional.empty() -> null */;
         }
      });
      event.addListener(optional::invalidate);
      holder.putManager(manager);
   }

   public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
      Entity entity = (Entity)event.getObject();
      if (entity instanceof LocalPlayer player) {
         final LazyOptional<PlayerCameraShaker> cameraShaker = LazyOptional.of(() -> new PlayerCameraShaker(player));
         event.addCapability(ResourceLocation.fromNamespaceAndPath("witherstormmod", "camera_shaker"), new ICapabilityProvider() {
            public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
               return capability == WitherStormModClientCapabilities.CAMERA_SHAKER ? cameraShaker.cast() : null /* TODO_MIG: LazyOptional.empty() -> null */;
            }
         });
         event.addListener(cameraShaker::invalidate);
         final LazyOptional<PlayerScreenBlinder> screenBlinder = LazyOptional.of(PlayerScreenBlinder::new);
         event.addCapability(ResourceLocation.fromNamespaceAndPath("witherstormmod", "blinder"), new ICapabilityProvider() {
            public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
               return capability == WitherStormModClientCapabilities.SCREEN_BLINDER ? screenBlinder.cast() : null /* TODO_MIG: LazyOptional.empty() -> null */;
            }
         });
         event.addListener(screenBlinder::invalidate);
         final LazyOptional<PlayerTractorBeamEffects> tractorBeamEffects = LazyOptional.of(() -> new PlayerTractorBeamEffects(player));
         event.addCapability(ResourceLocation.fromNamespaceAndPath("witherstormmod", "tractor_beam_effects"), new ICapabilityProvider() {
            public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
               return capability == WitherStormModClientCapabilities.TRACTOR_BEAM_EFFECTS ? tractorBeamEffects.cast() : null /* TODO_MIG: LazyOptional.empty() -> null */;
            }
         });
         event.addListener(tractorBeamEffects::invalidate);
      }
   }
}
