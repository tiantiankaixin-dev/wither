package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.minecraft.core.registries.BuiltInRegistries;
import nonamecrackers2.witherstormmod.client.entity.ClientBlockClusterEntity;
import nonamecrackers2.witherstormmod.client.instancing.RenderBufferer;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;

public class BlockClusterRenderer extends EntityRenderer<BlockClusterEntity> {
   public BlockClusterRenderer(Context context) {
      super(context);
   }

   public void render(BlockClusterEntity cluster, float p_225623_2_, float partialTicks, PoseStack stack, MultiBufferSource buffer, int p_225623_6_) {
      if (!(cluster instanceof ClientBlockClusterEntity entity)) {
         throw new ClassCastException("Can only render instances of ClientBlockClusterEntity! Is a mod doing something it shouldn't be doing?");
      } else {
         stack.pushPose();
         stack.translate(0.0, 0.5, 0.0);
         float x = Mth.lerp(partialTicks, entity.shakeO.x, entity.shake.x);
         float z = Mth.lerp(partialTicks, entity.shakeO.y, entity.shake.y);
         stack.mulPose(Axis.YP.rotationDegrees(-entity.getClusterYRot(partialTicks) - x * 50.0F));
         stack.mulPose(Axis.XP.rotationDegrees(entity.getClusterXRot(partialTicks) - z * 30.0F));
         stack.translate(0.0, -0.5, 0.0);
         double xOffset = -0.5 + (Math.round(entity.getBoundingBox().getXsize()) % 2L == 0L ? -0.5 : 0.0);
         double yOffset = -0.5 + (Math.round(entity.getBoundingBox().getYsize()) % 2L == 0L ? -0.5 : 0.0);
         double zOffset = -0.5 + (Math.round(entity.getBoundingBox().getZsize()) % 2L == 0L ? -0.5 : 0.0);
         stack.translate(xOffset, yOffset, zOffset);
         Minecraft minecraft = Minecraft.getInstance();
         BlockRenderDispatcher dispatcher = minecraft.getBlockRenderer();
         Level world = entity.level();
         BlockAndTintGetter getter = entity.getBlockGetter();
         float fade = entity.lerpFadeAmount(partialTicks);
         float scale = Math.max(0.8F, fade * 0.5F + 0.5F);
         stack.scale(scale, scale, scale);
         stack.translate((double)x, 0.0, (double)z);
         float r = Math.min(1.0F, fade + 0.1F);
         float g = fade;
         float b = Math.min(1.0F, fade + 0.2F);

         for (Entry<RenderType, Map<BlockPos, BlockState>> entry : entity.toRender().entrySet()) {
            RenderType type = entry.getKey();
            RenderBufferer.pushUseAsyncBuilder();
            RenderBufferer.buildAndOrRender(
               entity.getToRenderUniqueId() + ", " + type.hashCode(),
               type,
               entity::isRemoved,
               (s, c, p, o, unusedR, unusedG, unusedB, a) -> {
                  for (Entry<BlockPos, BlockState> blockEntry : entry.getValue().entrySet()) {
                     BlockState statex = blockEntry.getValue();
                     BlockPos relativePosx = blockEntry.getKey();
                     s.pushPose();
                     s.translate(
                        (double)relativePosx.getX(),
                        (double)relativePosx.getY() + entity.getBoundingBox().getYsize() / 2.0,
                        (double)relativePosx.getZ()
                     );
                     dispatcher.renderBatched(
                        statex,
                        relativePosx.offset(entity.getStartPos()),
                        getter,
                        s,
                        c,
                        true,
                        RandomSource.create(relativePosx.asLong()),
                        ModelData.EMPTY,
                        type
                     );
                     s.popPose();
                  }
               },
               stack,
               p_225623_6_,
               OverlayTexture.NO_OVERLAY,
               r,
               g,
               b,
               1.0F
            );
         }

         for (Entry<BlockPos, BlockState> entry : entity.tilesToRender().entrySet()) {
            BlockState state = entry.getValue();
            if (state.getRenderShape() == RenderShape.ENTITYBLOCK_ANIMATED) {
               BlockPos relativePos = entry.getKey();
               BlockPos pos = BlockPos.containing(
                  entity.getX() + (double)relativePos.getX(),
                  entity.getY() + (double)relativePos.getY() + entity.getBoundingBox().getYsize() / 2.0 - 0.5,
                  entity.getZ() + (double)relativePos.getZ()
               );
               stack.pushPose();
               stack.translate(
                  (double)relativePos.getX(), (double)relativePos.getY() + entity.getBoundingBox().getYsize() / 2.0, (double)relativePos.getZ()
               );
               CompoundTag data = entity.getTileDataFromOffsetPos(relativePos);
               if (data != null) {
                  String id = data.getString("id");
                  BlockEntity tile = ((BlockEntityType)NeoBuiltInRegistries.BLOCK_ENTITY_TYPE.getValue(new ResourceLocation(id))).create(pos, state);
                  BlockEntityRenderer<BlockEntity> tileRenderer = minecraft.getBlockEntityRenderDispatcher().getRenderer(tile);
                  tile.setLevel(world);
                  tile.load(data);
                  if (tileRenderer != null) {
                     tileRenderer.render(tile, partialTicks, stack, buffer, LevelRenderer.getLightColor(getter, pos), OverlayTexture.NO_OVERLAY);
                  }
               }

               stack.popPose();
            }
         }

         stack.popPose();
         super.render(entity, p_225623_2_, partialTicks, stack, buffer, p_225623_6_);
      }
   }

   public boolean shouldRender(BlockClusterEntity entity, Frustum p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
      return !WitherStormModConfig.CLIENT.blockClusterRendering.get() && !entity.forceRender()
         ? false
         : super.shouldRender(entity, p_225626_2_, p_225626_3_, p_225626_5_, p_225626_7_);
   }

   public ResourceLocation getTextureLocation(BlockClusterEntity entity) {
      return null;
   }
}
