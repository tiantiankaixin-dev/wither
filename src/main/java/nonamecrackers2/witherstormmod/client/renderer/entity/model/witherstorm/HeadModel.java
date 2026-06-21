package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Random;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import nonamecrackers2.witherstormmod.api.common.entity.WitherStormBase;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class HeadModel<T extends LivingEntity & WitherStormBase> {
   protected final ModelPart head;
   protected final ModelPart upper;
   protected final ModelPart lower;
   private float jawHinge;
   public float tractorBeamXOffset;
   public float tractorBeamYOffset;
   public float tractorBeamZOffset;
   public float pivotOffsetX;
   public float pivotOffsetY;
   public float pivotOffsetZ;
   public float scale;
   public float tractorBeamStartSize;
   public float tractorBeamDistance;
   public float tractorBeamEndSize;
   public float animationOffset;

   public HeadModel(ModelPart root, float scale) {
      this.head = root;
      this.upper = root.getChild("upperJaw");
      this.lower = root.getChild("lowerJaw");
      this.scale = scale;
   }

   public static void populateDefinition(PartDefinition root) {
      PartDefinition upper = root.addOrReplaceChild(
         "upperJaw",
         CubeListBuilder.create()
            .texOffs(0, 65)
            .addBox(-4.0F, -6.5F, 12.0F, 8.0F, 6.0F, 2.0F, false)
            .texOffs(0, 47)
            .addBox(-2.0F, -8.5F, 10.0F, 4.0F, 2.0F, 2.0F, false)
            .texOffs(0, 35)
            .addBox(-4.0F, -8.5F, 0.0F, 8.0F, 2.0F, 10.0F, false)
            .texOffs(0, 47)
            .addBox(-6.0F, -6.5F, 0.0F, 12.0F, 6.0F, 12.0F, false)
            .texOffs(4, 13)
            .addBox(-1.0F, -4.5F, 13.1F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE, 0.2F, 0.2F),
         PartPose.offset(0.0F, 2.5F, 0.0F)
      );
      upper.addOrReplaceChild(
         "upperTeeth",
         CubeListBuilder.create()
            .texOffs(0, 54)
            .addBox(-1.0F, -1.0F, 13.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-3.0F, -1.0F, 12.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-5.0F, -1.0F, 11.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-6.0F, -1.0F, 9.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-6.0F, -1.0F, 7.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-6.0F, -1.0F, 5.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-6.0F, -1.0F, 3.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-6.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(1.0F, -1.0F, 13.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(3.0F, -1.0F, 12.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(4.0F, -1.0F, 10.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(5.0F, -1.0F, 8.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(5.0F, -1.0F, 6.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(5.0F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(5.0F, -1.0F, 2.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(5.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, false),
         PartPose.offset(0.0F, 0.5F, 0.0F)
      );
      PartDefinition lower = root.addOrReplaceChild(
         "lowerJaw",
         CubeListBuilder.create()
            .texOffs(0, 73)
            .addBox(-4.0F, 0.5F, 12.0F, 8.0F, 2.0F, 2.0F, false)
            .texOffs(48, 0)
            .addBox(-6.0F, 0.5F, 0.0F, 12.0F, 2.0F, 12.0F, false),
         PartPose.offset(0.0F, 2.5F, 0.0F)
      );
      lower.addOrReplaceChild(
         "lowerTeeth",
         CubeListBuilder.create()
            .texOffs(0, 54)
            .addBox(0.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(2.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(4.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(5.0F, -3.0F, 1.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(5.0F, -3.0F, -1.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(5.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(5.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-2.0F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-4.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-5.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-6.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-6.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-6.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-6.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, false)
            .texOffs(0, 54)
            .addBox(-6.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, false),
         PartPose.offset(0.0F, 2.5F, 8.0F)
      );
   }

   public ModelPart root() {
      return this.head;
   }

   public void scale(PoseStack stack) {
      stack.scale(this.scale, this.scale, this.scale);
   }

   public void setupAnimations(T entity, float partialTicks, float tickCount, float yRot, float xRot, int head) {
      if (head > 0) {
         this.head.yRot = (
                  Mth.lerp(partialTicks, entity.getHeadYRotO(head), entity.getHeadYRot(head))
                     - Mth.lerp(partialTicks, entity.yBodyRotO, entity.yBodyRot)
               )
               * (float) (Math.PI / 180.0)
            + 3.1416F;
         this.head.xRot = -(Mth.lerp(partialTicks, entity.getHeadXRotO(head), entity.getHeadXRot(head)) * (float) (Math.PI / 180.0));
      } else {
         this.head.yRot = 3.1416F + yRot * (float) (Math.PI / 180.0);
         this.head.xRot = -xRot * (float) (Math.PI / 180.0);
      }

      float hinge = entity.getMouthAnimation(head, partialTicks);
      this.jawHinge = hinge * 0.3F;
      float ticks = (float)entity.tickCount + partialTicks;
      if (entity.isDeadOrPlayingDead()) {
         ticks = 0.0F;
      }

      float f = Mth.cos((ticks + this.animationOffset) * 0.1F);
      float d = Mth.cos(this.jawHinge);
      this.lower.xRot = d * 10.0F - 10.0F + (0.065F + 0.02F * f) * (float) Math.PI - 0.5F;
      Random random = new Random((long)entity.getId());
      boolean mirror = false;

      for (int i = 0; i < head; i++) {
         mirror = random.nextBoolean();
         if (i == head) {
            break;
         }
      }

      float s = Mth.cos(entity.getBrokenJawAnimation(head, partialTicks) * 0.3F);
      this.lower.zRot = (s * 10.0F - 10.0F) * (mirror ? -1.0F : 1.0F);
      this.head.zRot = entity.getHeadShakeAnim(head, partialTicks);
   }

   public boolean shouldRenderTractorBeam(T entity, int head) {
      boolean flag = false;
      if (entity.areOtherHeadsDisabled()) {
         flag = head == 0;
      } else {
         flag = true;
      }

      return flag && !entity.isHeadInjured(head);
   }

   public void renderTractorBeam(
      T entity,
      PoseStack stack,
      MultiBufferSource buffer,
      int packedLightIn,
      float r,
      float g,
      float b,
      float a,
      float partialTicks,
      double tractorBeamCutoff,
      float endFadeAlpha
   ) {
      stack.pushPose();
      stack.mulPose(Axis.YP.rotationDegrees(-Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot)));
      Pose pose = entity.getPose();
      if (pose != Pose.SLEEPING) {
         float xBodyRot = Mth.rotLerp(partialTicks, entity.getXBodyRotO(), entity.getXBodyRot());
         stack.mulPose(Axis.XP.rotationDegrees(-xBodyRot));
      }

      stack.translate(
         (this.head.x + this.tractorBeamXOffset) / 8.0F * this.scale,
         -((this.head.y - this.tractorBeamYOffset) / 8.0F) * this.scale,
         -((this.head.z - this.tractorBeamZOffset) / 8.0F) * this.scale
      );
      stack.scale(this.scale, this.scale, this.scale);
      stack.mulPose(Axis.ZP.rotation(-this.head.zRot));
      stack.mulPose(Axis.YP.rotation(-this.head.yRot - (float)Math.toRadians(90.0)));
      stack.mulPose(Axis.ZP.rotation(-this.head.xRot));
      stack.translate(this.pivotOffsetX / 8.0F * this.scale, this.pivotOffsetY / 8.0F * this.scale, this.pivotOffsetZ / 8.0F * this.scale);
      com.mojang.blaze3d.vertex.PoseStack.Pose entry = stack.last();
      Matrix4f matrix4f = entry.pose();
      Matrix3f matrix3f = entry.normal();
      VertexConsumer builder = buffer.getBuffer(RenderType.lightning());
      float distance = this.tractorBeamDistance;
      if (tractorBeamCutoff != -1.0) {
         distance = (float)tractorBeamCutoff;
      }

      float distanceScale = this.tractorBeamEndSize / this.tractorBeamDistance;
      float endSize = distanceScale * distance;
      builder.addVertex(matrix4f, 0.0F, 0.0F + this.tractorBeamStartSize, 0.0F + this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F - this.tractorBeamStartSize, 0.0F + this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, -endSize, endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, endSize, endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F + this.tractorBeamStartSize, 0.0F - this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F - this.tractorBeamStartSize, 0.0F - this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F - this.tractorBeamStartSize, 0.0F + this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F + this.tractorBeamStartSize, 0.0F + this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, endSize, -endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, -endSize, -endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F - this.tractorBeamStartSize, 0.0F - this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F + this.tractorBeamStartSize, 0.0F - this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, endSize, -endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F + this.tractorBeamStartSize, 0.0F - this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F + this.tractorBeamStartSize, 0.0F + this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, endSize, endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, -endSize, endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F - this.tractorBeamStartSize, 0.0F + this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F - this.tractorBeamStartSize, 0.0F - this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, -endSize, -endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F + this.tractorBeamStartSize, 0.0F - this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F - this.tractorBeamStartSize, 0.0F - this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, -endSize, -endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, endSize, -endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F + this.tractorBeamStartSize, 0.0F + this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F - this.tractorBeamStartSize, 0.0F + this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F - this.tractorBeamStartSize, 0.0F - this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F + this.tractorBeamStartSize, 0.0F - this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, endSize, endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, -endSize, endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F - this.tractorBeamStartSize, 0.0F + this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F + this.tractorBeamStartSize, 0.0F + this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, endSize, endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F + this.tractorBeamStartSize, 0.0F + this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F + this.tractorBeamStartSize, 0.0F - this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, endSize, -endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, -endSize, -endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F - this.tractorBeamStartSize, 0.0F - this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, 0.0F, 0.0F - this.tractorBeamStartSize, 0.0F + this.tractorBeamStartSize)
         .setColor(r, g, b, a)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      builder.addVertex(matrix4f, -distance, -endSize, endSize)
         .setColor(r, g, b, endFadeAlpha)
         .setUv(0.0F, 0.0F)
         .setOverlay(OverlayTexture.NO_OVERLAY)
         .setLight(packedLightIn)
         .setNormal(0.0F, -1.0F, 0.0F)
;
      stack.popPose();
   }
}
