package nonamecrackers2.witherstormmod.client.event;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ViewportEvent.ComputeFogColor;
import // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.ClientTickEvent;
import // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.Phase;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.client.capability.WitherStormDistantRenderer;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.client.renderer.entity.AbstractWitherStormRenderer;
import nonamecrackers2.witherstormmod.client.resources.WitherStormResourceConfigManager;
import nonamecrackers2.witherstormmod.client.resources.color.SkyColorSet;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;

public class WitherStormAmbienceEffects {
   private static final int COLOR_TRANSITION = 40;
   private final Minecraft mc;
   private float alpha = 1.0F;
   private float alphaO = 1.0F;
   private int colorTransitionTime;
   private SkyColorSet current = SkyColorSet.DEFAULT_SKY_COLORS;
   private SkyColorSet previous = SkyColorSet.DEFAULT_SKY_COLORS;

   public WitherStormAmbienceEffects(Minecraft mc) {
      this.mc = mc;
   }

   public void tick() {
      if ((Boolean)WitherStormModConfig.CLIENT.renderSkyAmbienceEffects.get()) {
         List<WitherStormEntity> storms = getApplicableEntities(this.mc.level);
         Vec3 cameraPos = this.mc.gameRenderer.getMainCamera().getPosition();
         WitherStormEntity storm = storms.stream().sorted(Comparator.comparingInt(WitherStormEntity::getConsumedEntities).reversed()).findFirst().orElse(null);
         this.alphaO = this.alpha;
         if (storm != null && !storm.isDeadOrPlayingDead()) {
            float alpha = modifyEffectMagnitude(storm, Mth.clamp((float)(cameraPos.distanceTo(storm.position()) - 200.0) * 0.005F, 0.0F, 1.0F));
            this.alpha = this.alpha + (alpha - this.alpha) / 25.0F;
            SkyColorSet set = WitherStormResourceConfigManager.INSTANCE.getColorSetByPhase(storm.getPhase()).skyColors();
            if (set != null && !set.equals(this.current)) {
               this.previous = this.current;
               this.current = set;
               this.colorTransitionTime = 40;
            }
         } else {
            this.alpha = this.alpha + (1.0F - this.alpha) / 100.0F;
         }

         if (this.colorTransitionTime > 0) {
            this.colorTransitionTime--;
         }
      }
   }

   public float lerpAlpha(float partialTicks) {
      return Mth.lerp(partialTicks, this.alphaO, this.alpha);
   }

   public Color lerpColorsByTransition(Function<SkyColorSet, Color> dayColorGetter, Function<SkyColorSet, Color> nightColorGetter, float partialTicks) {
      float lerp = Mth.clamp(((float)this.colorTransitionTime - partialTicks) / 40.0F, 0.0F, 1.0F);
      Color previous = lerpNightColors(dayColorGetter.apply(this.previous), nightColorGetter.apply(this.previous), partialTicks);
      Color current = lerpNightColors(dayColorGetter.apply(this.current), nightColorGetter.apply(this.current), partialTicks);
      int r = Mth.floor(Mth.lerp(lerp, (float)current.getRed(), (float)previous.getRed()));
      int g = Mth.floor(Mth.lerp(lerp, (float)current.getGreen(), (float)previous.getGreen()));
      int b = Mth.floor(Mth.lerp(lerp, (float)current.getBlue(), (float)previous.getBlue()));
      int a = Mth.floor(Mth.lerp(lerp, (float)current.getAlpha(), (float)previous.getAlpha()));
      return new Color(r, g, b, a);
   }

   private static Color lerpNightColors(Color day, @Nullable Color night, float partialTicks) {
      if (night != null) {
         float nightLerp = AbstractWitherStormRenderer.getNightTimeLerp(Minecraft.getInstance().level, partialTicks);
         int r = Mth.floor(Mth.lerp(nightLerp, (float)day.getRed(), (float)night.getRed()));
         int g = Mth.floor(Mth.lerp(nightLerp, (float)day.getGreen(), (float)night.getGreen()));
         int b = Mth.floor(Mth.lerp(nightLerp, (float)day.getBlue(), (float)night.getBlue()));
         int a = Mth.floor(Mth.lerp(nightLerp, (float)day.getAlpha(), (float)night.getAlpha()));
         return new Color(r, g, b, a);
      } else {
         return day;
      }
   }

   private static List<WitherStormEntity> getApplicableEntities(ClientLevel level) {
      return WitherStormDistantRenderer.getAllStorms(level)
         .stream()
         .filter(entity -> !(entity instanceof WitherStormSegmentEntity))
         .collect(Collectors.toList());
   }

   public static float modifyEffectMagnitude(WitherStormEntity storm, float alpha) {
      if (storm.getPhase() < 5) {
         return 1.0F;
      } else {
         return storm.getPhase() == 5 ? Math.min(alpha + Mth.clamp(1.0F - storm.getPhaseProgress(), 0.15F, 1.0F), 1.0F) : Math.min(alpha + 0.15F, 1.0F);
      }
   }

   public static Vec3 modifySkyColor(Minecraft mc, Vec3 original, Vec3 cameraPos, float partialTicks) {
      return transitionColor(mc, cameraPos, original, SkyColorSet::skyColor, SkyColorSet::nightSkyColor, 200.0, partialTicks);
   }

   public static float modifySkyDarken(Minecraft mc, Vec3 cameraPos, float original, float partialTicks) {
      WitherStormAmbienceEffects effects = (WitherStormAmbienceEffects)mc.level.getCapability(WitherStormModClientCapabilities.AMBIENT_EFFECTS).orElse(null);
      return effects != null ? original * Math.min(effects.lerpAlpha(partialTicks) + 0.4F, 1.0F) : original;
   }

   public static Vec3 modifyCloudColors(Minecraft mc, Vec3 cameraPos, Vec3 original, float partialTicks) {
      return transitionColor(mc, cameraPos, original, SkyColorSet::cloudColor, SkyColorSet::nightCloudColor, 200.0, partialTicks);
   }

   private static Vec3 transitionColor(
      Minecraft mc,
      Vec3 cameraPos,
      Vec3 original,
      Function<SkyColorSet, Color> dayColorGetter,
      Function<SkyColorSet, Color> nightColorGetter,
      double distance,
      float partialTicks
   ) {
      WitherStormAmbienceEffects effects = (WitherStormAmbienceEffects)mc.level.getCapability(WitherStormModClientCapabilities.AMBIENT_EFFECTS).orElse(null);
      if (effects != null) {
         float alpha = effects.lerpAlpha(partialTicks);
         Color col = effects.lerpColorsByTransition(dayColorGetter, nightColorGetter, partialTicks);
         int[] color = new int[]{col.getRed(), col.getGreen(), col.getBlue()};
         double rDelta = original.x * 255.0 - (double)color[0];
         double gDelta = original.y * 255.0 - (double)color[1];
         double bDelta = original.z * 255.0 - (double)color[2];
         color[0] = (int)((double)color[0] + rDelta * (double)alpha);
         color[1] = (int)((double)color[1] + gDelta * (double)alpha);
         color[2] = (int)((double)color[2] + bDelta * (double)alpha);
         return new Vec3((double)color[0] / 255.0, (double)color[1] / 255.0, (double)color[2] / 255.0);
      } else {
         return original;
      }
   }

   public static class Events {
      @SubscribeEvent
      public static void onWorldTick(ClientTickEvent event) {
         if (event.phase == Phase.START) {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level != null && !mc.isPaused()) {
               level.getCapability(WitherStormModClientCapabilities.AMBIENT_EFFECTS).ifPresent(WitherStormAmbienceEffects::tick);
            }
         }
      }

      @SubscribeEvent
      public static void fogColor(ComputeFogColor event) {
         if ((Boolean)WitherStormModConfig.CLIENT.renderSkyAmbienceEffects.get()) {
            Minecraft mc = Minecraft.getInstance();
            mc.level.getCapability(WitherStormModClientCapabilities.AMBIENT_EFFECTS).ifPresent(effects -> {
               float alpha = effects.lerpAlpha((float)event.getPartialTick());
               Color fog = effects.lerpColorsByTransition(SkyColorSet::fogColor, SkyColorSet::nightFogColor, (float)event.getPartialTick());
               int[] color = new int[]{fog.getRed(), fog.getGreen(), fog.getBlue()};
               float rDelta = event.getRed() * 255.0F - (float)color[0];
               float gDelta = event.getGreen() * 255.0F - (float)color[1];
               float bDelta = event.getBlue() * 255.0F - (float)color[2];
               color[0] = (int)((float)color[0] + rDelta * alpha);
               color[1] = (int)((float)color[1] + gDelta * alpha);
               color[2] = (int)((float)color[2] + bDelta * alpha);
               event.setRed((float)color[0] / 255.0F);
               event.setGreen((float)color[1] / 255.0F);
               event.setBlue((float)color[2] / 255.0F);
            });
         }
      }
   }
}
