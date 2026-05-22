package nonamecrackers2.witherstormmod.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import nonamecrackers2.witherstormmod.common.entity.SickenedSnowGolem;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;

public class SickenedSnowGolemHeadLayer extends RenderLayer<SickenedSnowGolem, SnowGolemModel<SickenedSnowGolem>> {
   private final BlockRenderDispatcher blockRenderer;
   private final ItemRenderer itemRenderer;

   public SickenedSnowGolemHeadLayer(
      RenderLayerParent<SickenedSnowGolem, SnowGolemModel<SickenedSnowGolem>> parent, BlockRenderDispatcher blockRenderDispatcher, ItemRenderer itemRenderer
   ) {
      super(parent);
      this.blockRenderer = blockRenderDispatcher;
      this.itemRenderer = itemRenderer;
   }

   public void render(
      PoseStack poseStack,
      MultiBufferSource buffer,
      int p_117351_,
      SickenedSnowGolem entity,
      float p_117353_,
      float p_117354_,
      float p_117355_,
      float p_117356_,
      float p_117357_,
      float p_117358_
   ) {
      if (entity.hasPumpkin()) {
         boolean flag = Minecraft.getInstance().shouldEntityAppearGlowing(entity) && entity.isInvisible();
         if (!entity.isInvisible() || flag) {
            poseStack.pushPose();
            ((SnowGolemModel)this.getParentModel()).getHead().translateAndRotate(poseStack);
            poseStack.translate(0.0F, -0.34375F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.scale(0.625F, -0.625F, -0.625F);
            ItemStack itemstack = new ItemStack((ItemLike)WitherStormModBlocks.TAINTED_CARVED_PUMPKIN.get());
            if (flag) {
               BlockState blockstate = ((Block)WitherStormModBlocks.TAINTED_CARVED_PUMPKIN.get()).defaultBlockState();
               BakedModel bakedmodel = this.blockRenderer.getBlockModel(blockstate);
               int i = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
               poseStack.translate(-0.5F, -0.5F, -0.5F);
               this.blockRenderer
                  .getModelRenderer()
                  .renderModel(
                     poseStack.last(),
                     buffer.getBuffer(RenderType.outline(InventoryMenu.BLOCK_ATLAS)),
                     blockstate,
                     bakedmodel,
                     0.0F,
                     0.0F,
                     0.0F,
                     p_117351_,
                     i,
                     ModelData.EMPTY,
                     null
                  );
            } else {
               this.itemRenderer
                  .renderStatic(
                     entity,
                     itemstack,
                     ItemDisplayContext.HEAD,
                     false,
                     poseStack,
                     buffer,
                     entity.level(),
                     p_117351_,
                     LivingEntityRenderer.getOverlayCoords(entity, 0.0F),
                     entity.getId()
                  );
            }

            poseStack.popPose();
         }
      }
   }
}
