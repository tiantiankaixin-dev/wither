package nonamecrackers2.witherstormmod.client.init;

import net.neoforged.neoforge.common.NeoForge;
import nonamecrackers2.witherstormmod.client.capability.BowelsEffectsManager;
import nonamecrackers2.witherstormmod.client.capability.PlayerTractorBeamEffects;
import nonamecrackers2.witherstormmod.client.capability.SoundManagersHolder;
import nonamecrackers2.witherstormmod.client.capability.WitherStormDistantRenderer;
import nonamecrackers2.witherstormmod.client.event.CheckForHeadHit;
import nonamecrackers2.witherstormmod.client.event.ClientWitherSicknessEvents;
import nonamecrackers2.witherstormmod.client.event.ParticleEvents;
import nonamecrackers2.witherstormmod.client.event.PlaySoundEvents;
import nonamecrackers2.witherstormmod.client.event.PlayTractorBeamLoopEvents;
import nonamecrackers2.witherstormmod.client.event.PlayerCameraShakerTicker;
import nonamecrackers2.witherstormmod.client.event.PlayerCosmeticsEvents;
import nonamecrackers2.witherstormmod.client.event.RenderWitherSicknessOverlay;
import nonamecrackers2.witherstormmod.client.event.ScreenBlinderEvents;
import nonamecrackers2.witherstormmod.client.event.WitherStormAmbienceEffects;
import nonamecrackers2.witherstormmod.client.event.WitherStormModClientConfigEvents;
import nonamecrackers2.witherstormmod.client.event.WitherStormModGuiEvents;
import nonamecrackers2.witherstormmod.client.event.WitherStormModRenderEvents;
import nonamecrackers2.witherstormmod.client.instancing.RenderBufferer;
import nonamecrackers2.witherstormmod.client.util.AmuletAnimationHelper;

public class WitherStormModClientEvents {
   public static final RenderWitherSicknessOverlay WITHER_SICKNESS_OVERLAY = new RenderWitherSicknessOverlay();

   public static void registerEvents() {
      NeoForge.EVENT_BUS.register(WitherStormModRenderEvents.class);
      NeoForge.EVENT_BUS.register(WitherStormModGuiEvents.class);
      NeoForge.EVENT_BUS.register(WitherStormDistantRenderer.Events.class);
      NeoForge.EVENT_BUS.register(PlayTractorBeamLoopEvents.class);
      NeoForge.EVENT_BUS.register(CheckForHeadHit.class);
      NeoForge.EVENT_BUS.register(WITHER_SICKNESS_OVERLAY);
      NeoForge.EVENT_BUS.register(ClientWitherSicknessEvents.class);
      NeoForge.EVENT_BUS.register(PlayerCameraShakerTicker.class);
      NeoForge.EVENT_BUS.register(ScreenBlinderEvents.class);
      NeoForge.EVENT_BUS.register(BowelsEffectsManager.Events.class);
      NeoForge.EVENT_BUS.register(SoundManagersHolder.Events.class);
      NeoForge.EVENT_BUS.register(WitherStormAmbienceEffects.Events.class);
      NeoForge.EVENT_BUS.register(RenderBufferer.Events.class);
      NeoForge.EVENT_BUS.register(PlayerCosmeticsEvents.class);
      NeoForge.EVENT_BUS.register(PlaySoundEvents.class);
      NeoForge.EVENT_BUS.register(WitherStormModClientConfigEvents.class);
      NeoForge.EVENT_BUS.addListener(ParticleEvents::onClientTick);
      NeoForge.EVENT_BUS.addListener(PlayerTractorBeamEffects::onPlayerTick);
      NeoForge.EVENT_BUS.addListener(AmuletAnimationHelper::onClientTick);
   }
}
