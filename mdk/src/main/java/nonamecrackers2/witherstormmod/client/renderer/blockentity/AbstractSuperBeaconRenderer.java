package nonamecrackers2.witherstormmod.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.blockentity.AbstractSuperBeaconBlockEntity;
import org.joml.Matrix4f;

public abstract class AbstractSuperBeaconRenderer<T extends AbstractSuperBeaconBlockEntity> implements BlockEntityRenderer<T> {
   protected static final ResourceLocation BEAM = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/misc/beam.png");

   public AbstractSuperBeaconRenderer(Context context) {
   }

   public void render(T entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight, int overlayTexture) {
      stack.pushPose();
      VertexConsumer consumer = buffer.getBuffer(this.getRenderType(entity));
      float scale = this.getCrsytalScale(entity, partialTicks);
      stack.translate(0.5, 0.5, 0.5);
      this.transformCrystal(entity, partialTicks, stack);
      float[] uvs = this.getUVS();
      int light = this.getLightTexture(entity, packedLight);
      renderOrb(stack, consumer, scale, uvs, light);
      stack.popPose();
   }

   public static void renderOrb(PoseStack stack, VertexConsumer consumer, float scale, int packedLight) {
      renderOrb(stack, consumer, scale, new float[]{0.0F, 0.0F, 1.0F, 1.0F}, packedLight);
   }

   public static void renderOrb(PoseStack stack, VertexConsumer consumer, float scale, float[] uvs, int packedLight) {
      stack.pushPose();
      stack.mulPose(Axis.XP.rotationDegrees(45.0F));
      drawBox(consumer, stack, OverlayTexture.NO_OVERLAY, packedLight, 1.0F, uvs[0], uvs[1], uvs[2], uvs[3], scale / 2.0F);
      stack.popPose();
      stack.pushPose();
      stack.mulPose(Axis.YP.rotationDegrees(45.0F));
      drawBox(consumer, stack, OverlayTexture.NO_OVERLAY, packedLight, 1.0F, uvs[0], uvs[1], uvs[2], uvs[3], scale / 2.0F);
      stack.popPose();
      stack.pushPose();
      stack.mulPose(Axis.ZP.rotationDegrees(45.0F));
      drawBox(consumer, stack, OverlayTexture.NO_OVERLAY, packedLight, 1.0F, uvs[0], uvs[1], uvs[2], uvs[3], scale / 2.0F);
      stack.popPose();
   }

   public static void renderBeam(
      boolean active,
      PoseStack stack,
      int[] color,
      MultiBufferSource buffer,
      float partialTicks,
      long gameTime,
      int beamHeight,
      float beamThickness,
      float outerThickness
   ) {
      if (active) {
         stack.pushPose();
         stack.translate(0.0, 0.5, 0.0);
         float r = (float)color[0] / 255.0F;
         float g = (float)color[1] / 255.0F;
         float b = (float)color[2] / 255.0F;
         BeaconRenderer.renderBeaconBeam(
            stack, buffer, BeaconRenderer.BEAM_LOCATION, partialTicks, 1.0F, gameTime, 0, beamHeight, new float[]{r, g, b, 1.0F}, beamThickness, outerThickness
         );
         stack.popPose();
      }
   }

   public static void renderConnectBeam(
      VertexConsumer consumer, PoseStack stack, Camera camera, float pulse, Vec3 start, Vec3 end, float r, float g, float b, float a
   ) {
      stack.pushPose();
      double dX = start.x - end.x;
      double dY = start.y - end.y;
      double dZ = start.z - end.z;
      float yaw = (float)Math.atan2(dZ, dX) - (float) (Math.PI / 2);
      float pitch = (float)Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + (float) Math.PI;
      stack.mulPose(Axis.YN.rotation(yaw));
      stack.mulPose(Axis.XP.rotation(pitch));
      Matrix4f pose = stack.last().pose();
      consumer.vertex(pose, -pulse, 0.0F, 0.0F)
         .color(r, g, b, a)
         .uv(0.0F, 0.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(15728880)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, pulse, 0.0F, 0.0F)
         .color(r, g, b, a)
         .uv(1.0F, 0.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(15728880)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, pulse, (float)start.distanceTo(end), 0.0F)
         .color(r, g, b, a)
         .uv(1.0F, 1.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(15728880)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, -pulse, (float)start.distanceTo(end), 0.0F)
         .color(r, g, b, a)
         .uv(0.0F, 1.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(15728880)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, 0.0F, 0.0F, -pulse)
         .color(r, g, b, a)
         .uv(0.0F, 0.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(15728880)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, 0.0F, 0.0F, pulse)
         .color(r, g, b, a)
         .uv(1.0F, 0.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(15728880)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, 0.0F, (float)start.distanceTo(end), pulse)
         .color(r, g, b, a)
         .uv(1.0F, 1.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(15728880)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, 0.0F, (float)start.distanceTo(end), -pulse)
         .color(r, g, b, a)
         .uv(0.0F, 1.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(15728880)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      stack.popPose();
   }

   protected abstract float[] getUVS();

   protected abstract RenderType getRenderType(T var1);

   protected abstract float getCrsytalScale(T var1, float var2);

   protected abstract void transformCrystal(T var1, float var2, PoseStack var3);

   protected int getLightTexture(T entity, int packedLight) {
      return entity.isActive() ? packedLight : 15728880;
   }

   public int getViewDistance() {
      return 256;
   }

   public boolean shouldRender(T entity, Vec3 pos) {
      return Vec3.atCenterOf(entity.getBlockPos()).multiply(1.0, 0.0, 1.0).closerThan(pos.multiply(1.0, 0.0, 1.0), (double)this.getViewDistance());
   }

   public boolean shouldRenderOffScreen(T entity) {
      return true;
   }

   protected static void drawBox(
      VertexConsumer consumer, PoseStack stack, int overlayTexture, int packedLight, float alpha, float uMin, float vMin, float uMax, float vMax, float scale
   ) {
      Matrix4f pose = stack.last().pose();
      consumer.vertex(pose, -scale, -scale, -scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMin, vMin)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, -scale, scale, -scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMin, vMax)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, scale, scale, -scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMax, vMax)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, scale, -scale, -scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMax, vMin)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, -scale, -scale, scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMin, vMin)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, -scale, scale, scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMin, vMax)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, scale, scale, scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMax, vMax)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, scale, -scale, scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMax, vMin)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, -scale, -scale, -scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMin, vMin)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, -scale, -scale, scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMin, vMax)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, scale, -scale, scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMax, vMax)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, scale, -scale, -scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMax, vMin)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, -scale, scale, -scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMin, vMin)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, -scale, scale, scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMin, vMax)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, scale, scale, scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMax, vMax)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, scale, scale, -scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMax, vMin)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, -scale, -scale, -scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMin, vMin)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, -scale, -scale, scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMin, vMax)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, -scale, scale, scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMax, vMax)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, -scale, scale, -scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMax, vMin)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, scale, -scale, -scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMin, vMin)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, scale, -scale, scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMin, vMax)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, scale, scale, scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMax, vMax)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
      consumer.vertex(pose, scale, scale, -scale)
         .color(1.0F, 1.0F, 1.0F, alpha)
         .uv(uMax, vMin)
         .overlayCoords(overlayTexture)
         .uv2(packedLight)
         .normal(0.0F, 1.0F, 0.0F)
         .endVertex();
   }
}
