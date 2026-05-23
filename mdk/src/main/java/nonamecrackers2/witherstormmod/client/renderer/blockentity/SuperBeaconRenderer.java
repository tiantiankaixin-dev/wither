package nonamecrackers2.witherstormmod.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import nonamecrackers2.witherstormmod.client.util.TextureAtlasAccessor;
import nonamecrackers2.witherstormmod.common.blockentity.AbstractSuperBeaconBlockEntity;
import nonamecrackers2.witherstormmod.common.blockentity.SuperBeaconBlockEntity;
import nonamecrackers2.witherstormmod.common.blockentity.SuperSupportBeaconBlockEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SuperBeaconRenderer extends AbstractSuperBeaconRenderer<SuperBeaconBlockEntity> {
   private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "block/tainted_dust_block");
   private final TextureAtlas atlas;
   private final TextureAtlasSprite texture;
   private final ItemRenderer itemRenderer;

   public SuperBeaconRenderer(Context context) {
      super(context);
      Minecraft mc = Minecraft.getInstance();
      this.atlas = mc.getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);
      this.texture = this.atlas.getSprite(TEXTURE);
      this.itemRenderer = mc.getItemRenderer();
   }

   public void render(SuperBeaconBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight, int overlayTexture) {
      super.render(entity, partialTicks, stack, buffer, packedLight, overlayTexture);
      Minecraft mc = Minecraft.getInstance();
      float tickCount = (float)entity.getTicks() + partialTicks;
      float resummonTicks = (float)entity.getResummonTicks() + partialTicks;
      if (entity.getResummonEntity() == WitherStormModEntityTypes.WITHER_STORM.get() && entity.getResummonTicks() > 60) {
         float speed = 200.0F / (resummonTicks - 400.0F);
         stack.pushPose();
         Vec2 shake = entity.getShake(partialTicks);
         stack.translate((double)(shake.x * speed), 3.0, (double)(shake.y * speed));
         stack.translate(0.5, 0.5, 0.5);
         stack.mulPose(Axis.ZP.rotationDegrees(tickCount * speed));
         stack.mulPose(Axis.XN.rotationDegrees(tickCount * speed));
         stack.translate(-0.5, -0.5, -0.5);
         mc.getBlockRenderer().renderSingleBlock(Blocks.COMMAND_BLOCK.defaultBlockState(), stack, buffer, 15728880, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
         stack.popPose();
      }

      stack.translate(0.5, 0.5, 0.5);
      if (entity.getResummonEntity() == WitherStormModEntityTypes.WITHER_STORM.get() && entity.getResummonTicks() > 60) {
         stack.pushPose();
         VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(BEAM));
         int[] color = entity.getBeamColor();
         float anim = entity.getActivateAnimation(partialTicks);
         float r = Mth.lerp(anim, 0.5F, (float)color[0] / 255.0F);
         float g = Mth.lerp(anim, 0.5F, (float)color[1] / 255.0F);
         float b = Mth.lerp(anim, 0.5F, (float)color[2] / 255.0F);
         float pulse = this.getCrsytalScale(entity, partialTicks);
         Vec3 pos = Vec3.atCenterOf(entity.getBlockPos());
         if (anim > 0.01F) {
            renderConnectBeam(consumer, stack, mc.gameRenderer.getMainCamera(), pulse, pos, pos.add(0.0, 3.0, 0.0), r, g, b, anim);
         }

         stack.popPose();
      }

      stack.pushPose();
      float scale = 1.0F;
      float baseRotation;
      if (entity.getResummonTicks() > 0) {
         baseRotation = (tickCount + (float)Math.pow((double)((float)entity.getResummonTicks() + partialTicks), 2.0)) * 0.02F;
         scale -= ((float)entity.getResummonTicks() + partialTicks) / 60.0F;
      } else {
         baseRotation = tickCount * 0.02F;
      }

      stack.scale(scale, scale, scale);
      stack.translate(0.0, 1.0, 0.0);
      int total = entity.getContainerSize();
      float angleInterval = 360.0F / (float)total * (float) (Math.PI / 180.0);

      for (int i = 0; i < total; i++) {
         stack.pushPose();
         float angle = angleInterval * (float)i;
         float x = Mth.sin(angle + baseRotation);
         float z = Mth.cos(angle + baseRotation);
         stack.translate((double)x, 0.0, (double)z);
         Vec2 shake = entity.getShake(partialTicks);
         if (entity.isDoingResummonAnimation()) {
            stack.translate(shake.x, 0.0F, shake.y);
         }

         stack.translate(0.0, (double)(Mth.sin((tickCount + (float)(i * total)) * 0.2F) * 0.05F), 0.0);
         stack.mulPose(Axis.YN.rotationDegrees(tickCount + (float)(i * 100)));
         this.itemRenderer
            .renderStatic(
               entity.getItem(i), ItemDisplayContext.FIXED, packedLight, overlayTexture, stack, buffer, entity.getLevel(), (int)entity.getBlockPos().asLong()
            );
         stack.popPose();
      }

      stack.popPose();
      if (entity.showWorkingArea()) {
         for (Entry<AbstractSuperBeaconBlockEntity.Color, BlockPos> entry : entity.getConnected().entrySet()) {
            BlockPos pos = entry.getValue();
            AbstractSuperBeaconBlockEntity.Color color = entry.getKey();
            float angle = SuperSupportBeaconBlockEntity.getAngleBetween(pos, entity.getBlockPos());
            float arcHalf = 45.0F;
            VertexConsumer consumer = buffer.getBuffer(RenderType.lines());
            drawLineSegment(stack, consumer, 0.0F, 10.0F, color.getRed(), color.getGreen(), color.getBlue(), angle + arcHalf, -0.05F);
            drawLineSegment(stack, consumer, 11.0F, 1.0F, color.getRed(), color.getGreen(), color.getBlue(), angle + arcHalf, -0.05F);
            drawLineSegment(stack, consumer, 13.0F, 1.0F, color.getRed(), color.getGreen(), color.getBlue(), angle + arcHalf, -0.05F);
            drawLineSegment(stack, consumer, 15.0F, 0.5F, color.getRed(), color.getGreen(), color.getBlue(), angle + arcHalf, -0.05F);
            drawLineSegment(stack, consumer, 0.0F, 10.0F, color.getRed(), color.getGreen(), color.getBlue(), angle - arcHalf, 0.05F);
            drawLineSegment(stack, consumer, 11.0F, 1.0F, color.getRed(), color.getGreen(), color.getBlue(), angle - arcHalf, 0.05F);
            drawLineSegment(stack, consumer, 13.0F, 1.0F, color.getRed(), color.getGreen(), color.getBlue(), angle - arcHalf, 0.05F);
            drawLineSegment(stack, consumer, 15.0F, 0.5F, color.getRed(), color.getGreen(), color.getBlue(), angle - arcHalf, 0.05F);
         }
      }
   }

   private static void drawLineSegment(PoseStack stack, VertexConsumer consumer, float zOffset, float distance, int r, int g, int b, float angle, float xOffset) {
      stack.pushPose();
      stack.mulPose(Axis.YP.rotationDegrees(angle));
      stack.translate((double)xOffset, 0.0, 0.0);
      Matrix4f pose = stack.last().pose();
      Matrix3f normal = stack.last().setNormal();
      consumer.addVertex(pose, 0.0F, 0.0F, zOffset).setColor(r, g, b, 255).setNormal(0.0F, 0.0F, 1.0F);
      consumer.addVertex(pose, 0.0F, 0.0F, zOffset + distance).setColor(r, g, b, 255).setNormal(0.0F, 0.0F, 1.0F);
      stack.popPose();
   }

   @Override
   protected float[] getUVS() {
      TextureAtlasAccessor accessor = (TextureAtlasAccessor)this.atlas;
      float uvSquish = 3.0F;
      return new float[]{
         this.texture.getU0() + uvSquish / (float)accessor.getWidth(),
         this.texture.getV0() + uvSquish / (float)accessor.getHeight(),
         this.texture.getU1() - uvSquish / (float)accessor.getWidth(),
         this.texture.getV1() - uvSquish / (float)accessor.getHeight()
      };
   }

   protected RenderType getRenderType(SuperBeaconBlockEntity entity) {
      return RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS);
   }

   protected float getCrsytalScale(SuperBeaconBlockEntity entity, float partialTicks) {
      return Mth.lerp(entity.getActivateAnimation(partialTicks), 0.4F, (Mth.sin(((float)entity.getTicks() + partialTicks) * 0.1F) + 10.0F) * 0.05F);
   }

   protected void transformCrystal(SuperBeaconBlockEntity entity, float partialTicks, PoseStack stack) {
      float speed = Mth.lerp(entity.getActivateAnimation(partialTicks), 0.1F, 1.0F);
      stack.mulPose(Axis.XP.rotationDegrees(Mth.wrapDegrees((float)entity.getTicks() + partialTicks) * 5.0F * speed));
      stack.mulPose(Axis.ZN.rotationDegrees(Mth.wrapDegrees((float)entity.getTicks() + partialTicks) * 12.0F * speed));
   }
}
