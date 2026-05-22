package nonamecrackers2.witherstormmod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import nonamecrackers2.witherstormmod.client.util.AmuletAnimationHelper;
import nonamecrackers2.witherstormmod.client.util.FormidiBladeAnimationHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemInHandRenderer.class})
public abstract class MixinItemInHandRenderer {
   @Inject(
      method = {"renderArmWithItem"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
      )}
   )
   public void renderArmWithItemInvoke(
      AbstractClientPlayer player,
      float partialTicks,
      float f1,
      InteractionHand hand,
      float f2,
      ItemStack item,
      float f3,
      PoseStack stack,
      MultiBufferSource buffer,
      int i,
      CallbackInfo ci
   ) {
      AmuletAnimationHelper.onRenderItemInHand(item, stack, hand, partialTicks, buffer);
      FormidiBladeAnimationHelper.onRenderItemInHand(player, item, stack, hand, partialTicks, buffer);
   }
}
