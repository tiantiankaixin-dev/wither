package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.client.event.WitherStormAmbienceEffects;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ClientLevel.class})
public abstract class MixinClientLevel {
   @Redirect(
      method = {"getSkyColor"},
      at = @At(
         value = "NEW",
         target = "(DDD)Lnet/minecraft/world/phys/Vec3;"
      )
   )
   public Vec3 redirectGetSkyColor(double r, double g, double b, Vec3 cameraPos, float partialTicks) {
      Vec3 color = new Vec3(r, g, b);
      return WitherStormModConfig.CLIENT.renderSkyAmbienceEffects.get()
         ? WitherStormAmbienceEffects.modifySkyColor(this.getMinecraft(), color, cameraPos, partialTicks)
         : color;
   }

   @Inject(
      method = {"getSkyDarken"},
      at = {@At("RETURN")},
      cancellable = true,
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   public void redirectGetSkyDarken(float partialTicks, CallbackInfoReturnable<Float> ci, float f, float f1) {
      float toReturn = f1 * 0.8F + 0.2F;
      if ((Boolean)WitherStormModConfig.CLIENT.renderSkyAmbienceEffects.get()) {
         Minecraft mc = this.getMinecraft();
         ci.setReturnValue(WitherStormAmbienceEffects.modifySkyDarken(mc, mc.gameRenderer.getMainCamera().getPosition(), toReturn, 0.0F));
      }
   }

   @Redirect(
      method = {"getCloudColor"},
      at = @At(
         value = "NEW",
         target = "(DDD)Lnet/minecraft/world/phys/Vec3;"
      )
   )
   public Vec3 redirectGetCloudColor(double r, double g, double b, float partialTicks) {
      Vec3 color = new Vec3(r, g, b);
      if ((Boolean)WitherStormModConfig.CLIENT.renderSkyAmbienceEffects.get()) {
         Minecraft mc = Minecraft.getInstance();
         return WitherStormAmbienceEffects.modifyCloudColors(mc, mc.gameRenderer.getMainCamera().getPosition(), color, partialTicks);
      } else {
         return color;
      }
   }

   @Inject(
      method = {"shouldTickDeath"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void shouldTickDeathHead(Entity entity, CallbackInfoReturnable<Boolean> ci) {
      if (entity instanceof WitherStormEntity storm && storm.isOnDistantRenderer()) {
         ci.setReturnValue(true);
      }
   }

   @Accessor
   public abstract Minecraft getMinecraft();
}
