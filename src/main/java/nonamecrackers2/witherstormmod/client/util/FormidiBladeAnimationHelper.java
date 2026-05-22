package nonamecrackers2.witherstormmod.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import nonamecrackers2.witherstormmod.common.item.FormidiBladeItem;

public class FormidiBladeAnimationHelper {
   public static void onRenderItemInHand(
      AbstractClientPlayer player, ItemStack item, PoseStack stack, InteractionHand hand, float partialTicks, MultiBufferSource buffer
   ) {
      if (item.getItem() instanceof FormidiBladeItem) {
         VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(AmuletAnimationHelper.GLARE));
         CompoundTag tag = item.getTag();
         float pulseIntensity = 0.0F;
         if (tag != null) {
            pulseIntensity = Math.min(1.0F, tag.getFloat("Power"));
         }

         AmuletAnimationHelper.drawGlare(
            stack, hand, 1.0F, -0.33, 0.089, -0.43, 0.5058824F, 0.0F, 0.7764706F, 0.0F, player.tickCount, partialTicks, consumer, pulseIntensity
         );
      }
   }
}
