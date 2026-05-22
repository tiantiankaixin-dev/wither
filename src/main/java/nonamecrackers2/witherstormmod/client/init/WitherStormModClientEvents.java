package nonamecrackers2.witherstormmod.client.init;

import net.minecraftforge.common.MinecraftForge;
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
      MinecraftForge.EVENT_BUS.register(WitherStormModRenderEvents.class);
      MinecraftForge.EVENT_BUS.register(WitherStormModGuiEvents.class);
      MinecraftForge.EVENT_BUS.register(WitherStormDistantRenderer.Events.class);
      MinecraftForge.EVENT_BUS.register(PlayTractorBeamLoopEvents.class);
      MinecraftForge.EVENT_BUS.register(CheckForHeadHit.class);
      MinecraftForge.EVENT_BUS.register(WITHER_SICKNESS_OVERLAY);
      MinecraftForge.EVENT_BUS.register(ClientWitherSicknessEvents.class);
      MinecraftForge.EVENT_BUS.register(PlayerCameraShakerTicker.class);
      MinecraftForge.EVENT_BUS.register(ScreenBlinderEvents.class);
      MinecraftForge.EVENT_BUS.register(BowelsEffectsManager.Events.class);
      MinecraftForge.EVENT_BUS.register(SoundManagersHolder.Events.class);
      MinecraftForge.EVENT_BUS.register(WitherStormAmbienceEffects.Events.class);
      MinecraftForge.EVENT_BUS.register(RenderBufferer.Events.class);
      MinecraftForge.EVENT_BUS.register(PlayerCosmeticsEvents.class);
      MinecraftForge.EVENT_BUS.register(PlaySoundEvents.class);
      MinecraftForge.EVENT_BUS.register(WitherStormModClientConfigEvents.class);
      MinecraftForge.EVENT_BUS.addListener(ParticleEvents::onClientTick);
      MinecraftForge.EVENT_BUS.addListener(PlayerTractorBeamEffects::onPlayerTick);
      MinecraftForge.EVENT_BUS.addListener(AmuletAnimationHelper::onClientTick);
   }
}
