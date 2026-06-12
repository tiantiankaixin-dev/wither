package nonamecrackers2.witherstormmod.client.init;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
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
   public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
         DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "witherstormmod");

   // Level-level attachments
   public static final Supplier<AttachmentType<WitherStormDistantRenderer>> DISTANT_RENDERER = ATTACHMENT_TYPES.register(
         "distant_renderer", () -> AttachmentType.builder(holder -> new WitherStormDistantRenderer(Minecraft.getInstance())).build());

   public static final Supplier<AttachmentType<SoundManagersHolder>> SOUND_MANAGERS = ATTACHMENT_TYPES.register(
         "sound_managers_holder", () -> AttachmentType.builder(holder -> {
            Minecraft mc = Minecraft.getInstance();
            SoundManagersHolder h = new SoundManagersHolder();
            h.putManager(new WitherStormLoopingSoundManager(mc));
            h.putManager(new FormidibombEffectsManager(mc));
            h.putManager(new CommandBlockSoundManager(mc));
            h.putManager(new WitheredSymbiontSpellLoopManager(mc));
            h.putManager(new WitheredSymbiontSoundManager(mc));
            h.putManager(new BossThemeManager(mc));
            h.putManager(new WitherStormHeadSoundManager(mc));
            h.putManager(new FormidiBladeLoopManager(mc));
            return h;
         }).build());

   public static final Supplier<AttachmentType<WitherStormLoopingSoundManager>> LOOPING_MANAGER = ATTACHMENT_TYPES.register(
         "looping_sound_manager", () -> AttachmentType.builder(holder -> new WitherStormLoopingSoundManager(Minecraft.getInstance())).build());

   public static final Supplier<AttachmentType<FormidibombEffectsManager>> FORMIDIBOMB_EFFECTS = ATTACHMENT_TYPES.register(
         "formidibomb_effects_manager", () -> AttachmentType.builder(holder -> new FormidibombEffectsManager(Minecraft.getInstance())).build());

   public static final Supplier<AttachmentType<CommandBlockSoundManager>> COMMAND_BLOCK_SOUND_MANAGER = ATTACHMENT_TYPES.register(
         "command_block_sound_manager", () -> AttachmentType.builder(holder -> new CommandBlockSoundManager(Minecraft.getInstance())).build());

   public static final Supplier<AttachmentType<WitheredSymbiontSpellLoopManager>> WITHERED_SYMBIONT_SPELL_LOOP_MANAGER = ATTACHMENT_TYPES.register(
         "withered_symbiont_spell_loop_manager", () -> AttachmentType.builder(holder -> new WitheredSymbiontSpellLoopManager(Minecraft.getInstance())).build());

   public static final Supplier<AttachmentType<WitheredSymbiontSoundManager>> WITHERED_SYMBIONT_SOUND_MANAGER = ATTACHMENT_TYPES.register(
         "withered_symbiont_sound_manager", () -> AttachmentType.builder(holder -> new WitheredSymbiontSoundManager(Minecraft.getInstance())).build());

   public static final Supplier<AttachmentType<BossThemeManager>> BOSS_THEME_MANAGER = ATTACHMENT_TYPES.register(
         "boss_theme_manager", () -> AttachmentType.builder(holder -> new BossThemeManager(Minecraft.getInstance())).build());

   public static final Supplier<AttachmentType<BowelsEffectsManager>> BOWELS_EFFECTS_MANAGER = ATTACHMENT_TYPES.register(
         "bowels_effects_manager", () -> AttachmentType.builder(holder -> new BowelsEffectsManager(Minecraft.getInstance())).build());

   public static final Supplier<AttachmentType<WitherStormHeadSoundManager>> WITHER_STORM_HEAD_SOUND_MANAGER = ATTACHMENT_TYPES.register(
         "wither_storm_head_sound_manager", () -> AttachmentType.builder(holder -> new WitherStormHeadSoundManager(Minecraft.getInstance())).build());

   public static final Supplier<AttachmentType<WitherStormAmbienceEffects>> AMBIENT_EFFECTS = ATTACHMENT_TYPES.register(
         "wither_storm_ambience_effects", () -> AttachmentType.builder(holder -> new WitherStormAmbienceEffects(Minecraft.getInstance())).build());

   public static final Supplier<AttachmentType<FormidiBladeLoopManager>> FORMIDI_BLADE_LOOP_MANAGER = ATTACHMENT_TYPES.register(
         "formidi_blade_loop_manager", () -> AttachmentType.builder(holder -> new FormidiBladeLoopManager(Minecraft.getInstance())).build());

   // Entity-level attachments (for LocalPlayer)
   public static final Supplier<AttachmentType<PlayerCameraShaker>> CAMERA_SHAKER = ATTACHMENT_TYPES.register(
         "camera_shaker", () -> AttachmentType.<PlayerCameraShaker>builder(holder -> new PlayerCameraShaker(null)).build());

   public static final Supplier<AttachmentType<PlayerScreenBlinder>> SCREEN_BLINDER = ATTACHMENT_TYPES.register(
         "screen_blinder", () -> AttachmentType.builder(holder -> new PlayerScreenBlinder()).build());

   public static final Supplier<AttachmentType<PlayerTractorBeamEffects>> TRACTOR_BEAM_EFFECTS = ATTACHMENT_TYPES.register(
         "tractor_beam_effects", () -> AttachmentType.<PlayerTractorBeamEffects>builder(holder -> new PlayerTractorBeamEffects(null)).build());
}
