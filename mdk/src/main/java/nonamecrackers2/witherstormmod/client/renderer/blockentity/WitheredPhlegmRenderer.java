package nonamecrackers2.witherstormmod.client.renderer.blockentity;

import net.neoforged.api.distmarker.Dist;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import nonamecrackers2.witherstormmod.common.blockentity.WitheredPhlegmBlockEntity;

public class WitheredPhlegmRenderer implements BlockEntityRenderer<WitheredPhlegmBlockEntity> {
   private final ItemRenderer itemRenderer;

   public WitheredPhlegmRenderer(Context context) {
      this.itemRenderer = context.getItemRenderer();
   }

   public void render(WitheredPhlegmBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight, int overlayTexture) {
      stack.translate(0.5, 0.5, 0.5);
      long seed = entity.getBlockPos().asLong();
      RandomSource random = RandomSource.create(seed);
      int totalItems = 0;

      for (int i = 0; i < entity.getContainerSize(); i++) {
         if (!entity.getItem(i).isEmpty()) {
            totalItems++;
         }
      }

      float ratio = (float)totalItems / (float)entity.getContainerSize();

      for (int ix = 0; ix < entity.getContainerSize(); ix++) {
         ItemStack item = entity.getItem(ix);
         if (!item.isEmpty()) {
            stack.pushPose();
            float scale = 0.5F + (1.0F - ratio) / 2.0F;
            float dist = 1.0F;
            stack.scale(scale, scale, scale);
            stack.translate(
               random.nextDouble() * (double)dist - (double)dist / 2.0,
               random.nextDouble() * (double)dist - (double)dist / 2.0,
               random.nextDouble() * (double)dist - (double)dist / 2.0
            );
            stack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            this.itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, packedLight, overlayTexture, stack, buffer, entity.getLevel(), (int)seed);
            stack.popPose();
         }
      }
   }

   public int getViewDistance() {
      return 32;
   }
}
