package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({PlayerItemInHandLayer.class})
public class MixinPlayerItemInHandLayer {
   @Redirect(
      method = {"renderArmWithItem"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
      )
   )
   public boolean redirectIsRenderArmWithItem(ItemStack stack, Item item) {
      return stack.is(item) || stack.is((Item)WitherStormModItems.PHASOMETER.get());
   }
}
