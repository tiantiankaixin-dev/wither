package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import nonamecrackers2.witherstormmod.common.entity.SuperTNTEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;

public class SuperTNTRenderer extends EntityRenderer<SuperTNTEntity> {
   public SuperTNTRenderer(Context context) {
      super(context);
      this.shadowRadius = 0.5F;
   }

   public void render(SuperTNTEntity entity, float p_225623_2_, float partialTicks, PoseStack stack, MultiBufferSource buffer, int p_225623_6_) {
      stack.pushPose();
      stack.translate(0.0, 0.5, 0.0);
      if ((float)entity.getFuse() - partialTicks + 1.0F < 10.0F) {
         float f = 1.0F - ((float)entity.getFuse() - partialTicks + 1.0F) / 10.0F;
         f = Mth.clamp(f, 0.0F, 1.0F);
         f *= f;
         f *= f;
         float f1 = 1.0F + f * 5.0F;
         stack.scale(f1, f1, f1);
      }

      stack.mulPose(Axis.YP.rotationDegrees(-90.0F));
      stack.translate(-0.5, -0.5, 0.5);
      stack.mulPose(Axis.YP.rotationDegrees(90.0F));
      renderWhiteSolidBlock(((Block)WitherStormModBlocks.SUPER_TNT.get()).defaultBlockState(), stack, buffer, p_225623_6_, entity.getFuse() / 5 % 2 == 0);
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

   public ResourceLocation getTextureLocation(SuperTNTEntity entity) {
      return InventoryMenu.BLOCK_ATLAS;
   }
}
