package nonamecrackers2.witherstormmod.client.renderer.blockentity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.blockentity.AbstractSuperBeaconBlockEntity;
import nonamecrackers2.witherstormmod.common.blockentity.SuperSupportBeaconBlockEntity;

public class SuperSupportBeaconRenderer extends AbstractSuperBeaconRenderer<SuperSupportBeaconBlockEntity> {
   private static final float[] UVS = new float[]{0.375F, 0.375F, 0.625F, 0.625F};
   private static final ResourceLocation DIAMOND = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/block/support_beacon_diamond.png");
   private static final ResourceLocation EMERALD = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/block/support_beacon_emerald.png");
   private static final ResourceLocation IRON = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/block/support_beacon_iron.png");
   private static final ResourceLocation REDSTONE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/block/support_beacon_redstone.png");
   private static final Map<AbstractSuperBeaconBlockEntity.Color, ResourceLocation> TEX_BY_COLOR = ImmutableMap.of(
      AbstractSuperBeaconBlockEntity.Color.AQUA,
      DIAMOND,
      AbstractSuperBeaconBlockEntity.Color.GREEN,
      EMERALD,
      AbstractSuperBeaconBlockEntity.Color.GRAY,
      IRON,
      AbstractSuperBeaconBlockEntity.Color.RED,
      REDSTONE
   );

   public SuperSupportBeaconRenderer(Context context) {
      super(context);
   }

   public void render(SuperSupportBeaconBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight, int overlayTexture) {
      super.render(entity, partialTicks, stack, buffer, packedLight, overlayTexture);
      BlockPos pos = entity.getBeamPos();
      if (pos != null) {
         stack.pushPose();
         stack.translate(0.5, 0.6, 0.5);
         Minecraft mc = Minecraft.getInstance();
         VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(BEAM));
         int[] color = entity.getBeamColor();
         float anim = entity.getActivateAnimation(partialTicks);
         float r = Mth.lerp(anim, 0.5F, (float)color[0] / 255.0F);
         float g = Mth.lerp(anim, 0.5F, (float)color[1] / 255.0F);
         float b = Mth.lerp(anim, 0.5F, (float)color[2] / 255.0F);
         float pulse = this.getCrsytalScale(entity, partialTicks);
         if (anim > 0.01F) {
            renderConnectBeam(
               consumer, stack, mc.gameRenderer.getMainCamera(), pulse, Vec3.atCenterOf(entity.getBlockPos()), Vec3.atCenterOf(pos).add(0.0, -0.1, 0.0), r, g, b, anim
            );
         }

         stack.popPose();
      }
   }

   @Override
   protected float[] getUVS() {
      return UVS;
   }

   protected RenderType getRenderType(SuperSupportBeaconBlockEntity entity) {
      return RenderType.entityCutoutNoCull(entity.getColor() == null ? IRON : TEX_BY_COLOR.get(entity.getColor()));
   }

   protected float getCrsytalScale(SuperSupportBeaconBlockEntity entity, float partialTicks) {
      return Mth.lerp(entity.getActivateAnimation(partialTicks), 0.2F, (Mth.sin(((float)entity.getTicks() + partialTicks) * 0.1F) + 10.0F) * 0.035F);
   }

   protected void transformCrystal(SuperSupportBeaconBlockEntity entity, float partialTicks, PoseStack stack) {
      stack.translate(0.0, 0.1, 0.0);
      float speed = Mth.lerp(entity.getActivateAnimation(partialTicks), 0.2F, 1.0F);
      stack.mulPose(Axis.XP.rotationDegrees(Mth.wrapDegrees((float)entity.getTicks() + partialTicks) * 4.0F * speed));
      stack.mulPose(Axis.YN.rotationDegrees(Mth.wrapDegrees((float)entity.getTicks() + partialTicks) * 8.0F * speed));
   }
}
