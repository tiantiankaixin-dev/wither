package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.math.Axis;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.commandblock.RibcageModel;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import org.joml.Matrix4f;

public class CommandBlockRenderer extends EntityRenderer<CommandBlockEntity> {
   private static final ResourceLocation RIBCAGE_LOCATION = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/command_block/ribcage.png");
   private final RibcageModel ribcage;
   @Nullable
   private EntityModel<CommandBlockEntity> model;

   public CommandBlockRenderer(Context context) {
      super(context);
      this.ribcage = new RibcageModel(context.bakeLayer(WitherStormModRenderers.RIBCAGE));
   }

   protected int getBlockLightLevel(CommandBlockEntity entity, BlockPos pos) {
      return entity.getMode() == CommandBlockEntity.Mode.TENTACLES ? 15 : super.getBlockLightLevel(entity, pos);
   }

   public void render(CommandBlockEntity entity, float p_225623_2_, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
      this.model = this.getModel(entity.getMode());
      if (this.model != null) {
         stack.pushPose();
         stack.scale(-1.0F, -1.0F, 1.0F);
         stack.mulPose(Axis.YN.rotationDegrees(-Mth.lerp(partialTicks, entity.yBodyRotO, entity.yBodyRot) + 90.0F));
         VertexConsumer builder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
         this.model.setupAnim(entity, entity.getModeAnim(partialTicks), partialTicks, 0.0F, entity.getYRot(), entity.getXRot());
         this.model.renderToBuffer(stack, builder, packedLight, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 1.0F, 1.0F));
         stack.popPose();
      }

      stack.pushPose();
      stack.translate(-0.5, (double)entity.lerpProtectionYOffset(partialTicks), -0.5);
      Minecraft mc = Minecraft.getInstance();
      BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
      BlockState state = entity.getBlockState();
      blockRenderer.renderSingleBlock(
         state, stack, buffer, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, ItemBlockRenderTypes.getRenderType(entity.getBlockState(), false)
      );
      int health = Mth.floor((entity.getMaxHealth() - entity.getHealth()) / (entity.getMaxHealth() / 4.0F));
      Pose entry = stack.last();
      if (health < 5 && health > 0) {
         RenderType type = (RenderType)ModelBakery.DESTROY_TYPES.get(health * 2);
         VertexConsumer blockBreakingBuilder = new SheetedDecalTextureGenerator(buffer.getBuffer(type), entry, 1.0F);
         blockRenderer.getModelRenderer()
            .renderModel(
               entry,
               blockBreakingBuilder,
               state,
               blockRenderer.getBlockModel(state),
               1.0F,
               1.0F,
               1.0F,
               packedLight,
               OverlayTexture.NO_OVERLAY,
               ModelData.EMPTY,
               type
            );
      }

      stack.popPose();
      if (entity.getSpecialDeathTime() > 0 || entity.getHitGlareTime() > 0) {
         float f1 = 0.0F;
         float k = 1.0F;
         if (entity.getSpecialDeathTime() > 0) {
            f1 = ((float)entity.getSpecialDeathTime() + partialTicks) / 20.0F;
            k = 1.0F;
         } else if (entity.getHitGlareTime() > 0) {
            f1 = 2.0F + ((float)entity.getHitGlareTime() - partialTicks) / 30.0F;
            k = ((float)entity.getHitGlareTime() - partialTicks) / 60.0F;
         }

         Random random = new Random(122L);
         VertexConsumer builder = buffer.getBuffer(RenderType.lightning());
         stack.pushPose();
         stack.translate(0.0, entity.getBoundingBox().getYsize() / 2.0, 0.0);

         for (int i = 0; (float)i < (f1 + f1 * f1) / 2.0F; i++) {
            stack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F + f1 * 90.0F));
            float f3 = random.nextFloat() * 0.2F + f1;
            float f4 = (random.nextFloat() + 0.2F) * 0.05F * f1;
            Matrix4f matrix4f = stack.last().pose();
            float sqrt = (float)(Math.sqrt(3.0) / 2.0);
            builder.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, k);
            builder.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, k);
            builder.addVertex(matrix4f, -sqrt * f4, f3, -0.5F * f4).setColor(255, 123, 0, 0);
            builder.addVertex(matrix4f, sqrt * f4, f3, -0.5F * f4).setColor(255, 123, 0, 0);
            builder.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, k);
            builder.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, k);
            builder.addVertex(matrix4f, sqrt * f4, f3, -0.5F * f4).setColor(255, 123, 0, 0);
            builder.addVertex(matrix4f, 0.0F, f3, 1.0F * f4).setColor(255, 123, 0, 0);
            builder.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, k);
            builder.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, k);
            builder.addVertex(matrix4f, 0.0F, f3, 1.0F * f4).setColor(255, 123, 0, 0);
            builder.addVertex(matrix4f, -sqrt * f4, f3, -0.5F * f4).setColor(255, 123, 0, 0);
         }

         stack.popPose();
      }

      super.render(entity, p_225623_2_, partialTicks, stack, buffer, packedLight);
   }

   private EntityModel<CommandBlockEntity> getModel(CommandBlockEntity.Mode mode) {
      return mode == CommandBlockEntity.Mode.RIBS ? this.ribcage : null;
   }

   public ResourceLocation getTextureLocation(CommandBlockEntity entity) {
      return entity.getMode() == CommandBlockEntity.Mode.RIBS ? RIBCAGE_LOCATION : InventoryMenu.BLOCK_ATLAS;
   }
}
