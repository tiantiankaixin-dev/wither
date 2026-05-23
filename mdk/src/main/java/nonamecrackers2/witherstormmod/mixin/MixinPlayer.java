package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.TentacleEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Player.class})
public class MixinPlayer {
   @Inject(
      method = {"wantsToStopRiding"},
      at = {@At("HEAD")},
      cancellable = true
   )
   protected void wantsToStopRiding(CallbackInfoReturnable<Boolean> callback) {
      if (((Player)(Object)this).getVehicle() instanceof TentacleEntity && (Boolean)WitherStormModConfig.COMMON.playerCannotDismountTentacles.get()) {
         callback.setReturnValue(false);
      }
   }

   @Redirect(
      method = {"isScoping"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
      )
   )
   public boolean redirectIsIsScoping(ItemStack stack, Item item) {
      return stack.is(item) || stack.is((Item)WitherStormModItems.PHASOMETER.get());
   }
}
