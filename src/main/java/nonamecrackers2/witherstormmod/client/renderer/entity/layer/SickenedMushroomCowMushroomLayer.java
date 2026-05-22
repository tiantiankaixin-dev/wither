package nonamecrackers2.witherstormmod.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import nonamecrackers2.witherstormmod.common.entity.SickenedMushroomCow;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;

public class SickenedMushroomCowMushroomLayer<T extends SickenedMushroomCow> extends RenderLayer<T, CowModel<T>> {
   private final BlockRenderDispatcher blockRenderer;

   public SickenedMushroomCowMushroomLayer(RenderLayerParent<T, CowModel<T>> parent, BlockRenderDispatcher blockRenderer) {
      super(parent);
      this.blockRenderer = blockRenderer;
   }

   public void render(
      PoseStack stack,
      MultiBufferSource buffer,
      int p_117351_,
      T entity,
      float p_117353_,
      float p_117354_,
      float p_117355_,
      float p_117356_,
      float p_117357_,
      float p_117358_
   ) {
      if (!entity.isBaby()) {
         Minecraft minecraft = Minecraft.getInstance();
         boolean flag = minecraft.shouldEntityAppearGlowing(entity) && entity.isInvisible();
         if (!entity.isInvisible() || flag) {
            BlockState state = ((Block)WitherStormModBlocks.TAINTED_MUSHROOM.get()).defaultBlockState();
            int i = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
            BakedModel bakedmodel = this.blockRenderer.getBlockModel(state);
            stack.pushPose();
            stack.translate(0.2F, -0.35F, 0.5F);
            stack.mulPose(Axis.YP.rotationDegrees(-48.0F));
            stack.scale(-1.0F, -1.0F, 1.0F);
            stack.translate(-0.5F, -0.5F, -0.5F);
            this.renderMushroomBlock(stack, buffer, 15728880, flag, state, i, bakedmodel);
            stack.popPose();
            stack.pushPose();
            stack.translate(0.2F, -0.35F, 0.5F);
            stack.mulPose(Axis.YP.rotationDegrees(42.0F));
            stack.translate(0.1F, 0.0F, -0.6F);
            stack.mulPose(Axis.YP.rotationDegrees(-48.0F));
            stack.scale(-1.0F, -1.0F, 1.0F);
            stack.translate(-0.5F, -0.5F, -0.5F);
            this.renderMushroomBlock(stack, buffer, 15728880, flag, state, i, bakedmodel);
            stack.popPose();
            stack.pushPose();
            ((CowModel)this.getParentModel()).getHead().translateAndRotate(stack);
            stack.translate(0.0F, -0.7F, -0.2F);
            stack.mulPose(Axis.YP.rotationDegrees(-78.0F));
            stack.scale(-1.0F, -1.0F, 1.0F);
            stack.translate(-0.5F, -0.5F, -0.5F);
            this.renderMushroomBlock(stack, buffer, 15728880, flag, state, i, bakedmodel);
            stack.popPose();
         }
      }
   }

   private void renderMushroomBlock(
      PoseStack stack, MultiBufferSource buffer, int p_234855_, boolean p_234856_, BlockState state, int p_234858_, BakedModel model
   ) {
      if (p_234856_) {
         this.blockRenderer
            .getModelRenderer()
            .renderModel(
               stack.last(),
               buffer.getBuffer(RenderType.outline(InventoryMenu.BLOCK_ATLAS)),
               state,
               model,
               0.0F,
               0.0F,
               0.0F,
               p_234855_,
               p_234858_,
               ModelData.EMPTY,
               null
            );
      } else {
         this.blockRenderer.renderSingleBlock(state, stack, buffer, p_234855_, p_234858_, ModelData.EMPTY, null);
      }
   }
}
