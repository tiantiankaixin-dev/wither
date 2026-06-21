package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nonamecrackers2.witherstormmod.client.util.PhasometerRenderHelper;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Gui.class})
public class MixinGui {
   @Unique
   private static final ResourceLocation PHASOMETER_SCOPE_TEXTURE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/misc/phasometer_scope.png");
   @Final
   @Shadow
   private Minecraft minecraft;
   @Shadow
   private int tickCount;
   @Unique
   private String dotDotDot = "";

   @ModifyArg(
      method = {"renderSpyglassOverlay"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V"
      )
   )
   public ResourceLocation renderSpyglassOverlayInvoke(ResourceLocation original) {
      return this.minecraft.player.getUseItem().is((Item)WitherStormModItems.PHASOMETER.get()) ? PHASOMETER_SCOPE_TEXTURE : original;
   }

   @Inject(
      method = {"renderSpyglassOverlay"},
      at = {@At("TAIL")}
   )
   public void renderSpyglassOverlayTail(GuiGraphics stack, float partialTicks, CallbackInfo ci) {
      ItemStack item = this.minecraft.player.getUseItem();
      if (item.is((Item)WitherStormModItems.PHASOMETER.get())) {
         PhasometerRenderHelper.renderPhasometerOverlay(item, stack, partialTicks, stack.guiWidth(), stack.guiHeight(), this.dotDotDot);
      }
   }

   @Inject(
      method = {"tick()V"},
      at = {@At("TAIL")}
   )
   public void tickTail(CallbackInfo ci) {
      if (this.tickCount % 5 == 0) {
         if (this.dotDotDot.length() >= 3) {
            this.dotDotDot = "";
         } else {
            this.dotDotDot = this.dotDotDot + ".";
         }
      }
   }
}
