package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import org.joml.Matrix4f;

public class FormidibombRenderer extends EntityRenderer<FormidibombEntity> {
   public FormidibombRenderer(Context context) {
      super(context);
      this.shadowRadius = 0.5F;
   }

   public void render(FormidibombEntity entity, float p_225623_2_, float partialTicks, PoseStack stack, MultiBufferSource buffer, int p_225623_6_) {
      stack.pushPose();
      stack.translate(0.0, 0.5, 0.0);
      if ((float)entity.getFuseLife() - partialTicks + 1.0F < 20.0F) {
         float f = 1.0F - ((float)entity.getFuseLife() - partialTicks + 1.0F) / 20.0F;
         f = Mth.clamp(f, 0.0F, 1.0F);
         f *= f;
         f *= f;
         float f1 = 1.0F + f * 20.0F;
         stack.scale(f1, f1, f1);
      }

      if ((float)entity.getFuseLife() - partialTicks < 240.0F) {
         float ticks = (240.0F - (float)entity.getFuseLife() + partialTicks) / 150.0F;
         Random random = new Random(289L);
         VertexConsumer builder = buffer.getBuffer(RenderType.lightning());
         stack.pushPose();

         for (int i = 0; (float)i < (ticks + ticks * ticks) / 2.0F * 10.0F; i++) {
            stack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F + ticks * 90.0F));
            float f3 = random.nextFloat() * (float)entity.getBoundingBox().getSize() * 0.2F
               + ticks * (float)Math.max(1.0, (double)((40.0F - (float)entity.getFuseLife() + partialTicks) / 6.0F));
            float f4 = random.nextFloat() * 0.025F + ticks;
            Matrix4f matrix4f = stack.last().pose();
            int k = 255;
            builder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, k).endVertex();
            builder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, k).endVertex();
            builder.vertex(matrix4f, -((float)(Math.sqrt(3.0) / 2.0)) * f4, f3, -0.5F * f4).color(255, 0, 255, 0).endVertex();
            builder.vertex(matrix4f, (float)(Math.sqrt(3.0) / 2.0) * f4, f3, -0.5F * f4).color(255, 0, 255, 0).endVertex();
            builder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, k).endVertex();
            builder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, k).endVertex();
            builder.vertex(matrix4f, (float)(Math.sqrt(3.0) / 2.0) * f4, f3, -0.5F * f4).color(255, 0, 255, 0).endVertex();
            builder.vertex(matrix4f, 0.0F, f3, 1.0F * f4).color(255, 0, 255, 0).endVertex();
            builder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, k).endVertex();
            builder.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, k).endVertex();
            builder.vertex(matrix4f, 0.0F, f3, 1.0F * f4).color(255, 0, 255, 0).endVertex();
            builder.vertex(matrix4f, -((float)(Math.sqrt(3.0) / 2.0)) * f4, f3, -0.5F * f4).color(255, 0, 255, 0).endVertex();
         }

         stack.popPose();
      }

      stack.mulPose(Axis.YP.rotationDegrees(-90.0F));
      stack.translate(-0.5, -0.5, 0.5);
      stack.mulPose(Axis.YP.rotationDegrees(90.0F));
      float ticks = (float)entity.getAirTime() + partialTicks;
      if (!entity.onGround()) {
         stack.translate(0.5, 0.5, 0.5);
         stack.mulPose(Axis.YP.rotationDegrees(ticks));
         stack.mulPose(Axis.XP.rotationDegrees(ticks * 0.5F));
         stack.translate(-0.5, -0.5, -0.5);
      }

      BlockState state = entity.getBlockState();
      int fuse = entity.getStartFuse() > 0 && entity.getFuseLife() > 0 ? entity.getStartFuse() / entity.getFuseLife() : 0;
      renderWhiteSolidBlock(state, stack, buffer, p_225623_6_, fuse % 2 == 0);
      stack.popPose();
      super.render(entity, p_225623_2_, partialTicks, stack, buffer, p_225623_6_);
   }

   private static void renderWhiteSolidBlock(BlockState state, PoseStack stack, MultiBufferSource buffer, int packedLight, boolean flash) {
      int i;
      if (flash) {
         i = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
      } else {
         i = OverlayTexture.NO_OVERLAY;
      }

      Minecraft mc = Minecraft.getInstance();
      mc.getBlockRenderer().renderSingleBlock(state, stack, buffer, packedLight, i, ModelData.EMPTY, ItemBlockRenderTypes.getRenderType(state, false));
   }

   public ResourceLocation getTextureLocation(FormidibombEntity entity) {
      return InventoryMenu.BLOCK_ATLAS;
   }
}
