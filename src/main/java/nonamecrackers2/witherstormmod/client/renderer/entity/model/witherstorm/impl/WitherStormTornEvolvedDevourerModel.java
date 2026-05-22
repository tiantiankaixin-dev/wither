package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.TentacleModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.ThreeHeadedWitherStormModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass.LowResTornEvolvedDevourerBodyModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass.TornEvolvedDevourerBodyModel;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class WitherStormTornEvolvedDevourerModel<T extends WitherStormEntity> extends WitherStormEvolvedDevourerModel<T> {
   public WitherStormTornEvolvedDevourerModel(ModelPart root) {
      super(root);
   }

   public static LayerDefinition createLayerDefinition() {
      MeshDefinition mesh = ThreeHeadedWitherStormModel.createMesh(
         new PartPose[]{PartPose.offset(-22.0F, -65.0F, -40.0F), PartPose.offset(0.0F, -32.0F, -23.0F), PartPose.offset(32.0F, -60.0F, -24.0F)}
      );
      PartDefinition root = mesh.getRoot();
      TornEvolvedDevourerBodyModel.createBodyModel(root, 0.2F);
      LowResTornEvolvedDevourerBodyModel.createBodyModel(root, 0.3F);
      PartDefinition tentacles = root.getChild("tentacles");
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle0", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 24, 24, 28, 28, 32},
         PartPose.offset(-20.0F, -25.0F, 5.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle1", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 24, 24, 24, 28, 38},
         PartPose.offset(20.0F, -27.5F, 7.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle2", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 24, 24, 28, 32, 28},
         PartPose.offset(-10.0F, -30.0F, -10.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle3", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 18, 24, 24, 24, 28},
         PartPose.offset(8.0F, -34.0F, -6.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle4", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 18, 24, 28, 32, 32},
         PartPose.offset(-8.0F, -25.0F, 16.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle5", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 20, 26, 28, 32, 28},
         PartPose.offset(10.0F, -23.0F, 19.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle6", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 20, 26, 28, 28, 24},
         PartPose.offset(-2.0F, 0.0F, 0.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacleLarge0", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{16, 20, 24, 28, 32, 28},
         PartPose.offset(-24.0F, -28.0F, 0.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacleLarge1", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{20, 20, 24, 24, 28, 32},
         PartPose.offset(28.0F, -28.0F, 2.0F)
      );
      return LayerDefinition.create(mesh, 160, 160);
   }

   @Override
   public void renderMassDecal(T entity, PoseStack stack, MultiBufferSource buffer, int packedLight, int overlayCoords, float r, float g, float b, float a) {
      stack.pushPose();
      VertexConsumer builder = buffer.getBuffer(RenderType.lightning());
      Pose entry = stack.last();
      Matrix4f matrix4f = entry.pose();
      Matrix3f matrix3f = entry.normal();
      float aR = 0.5F;
      float aG = 0.3F;
      float aB = 0.8F;
      float aA = a * 0.2F;
      float size = 0.35F;
      float topZOffset = 0.4F;
      float stretch = 1.1F;
      if (this.lowResModelsEnabled(entity)) {
         topZOffset = 0.45F;
         stretch = 1.4F;
         stack.translate(-0.12, -2.0, -0.8);
      } else {
         stack.translate(-0.12, -2.0, -0.9);
      }

      builder.vertex(matrix4f, size * stretch, size, 0.0F)
         .color(aR, aG, aB, aA)
         .uv(0.0F, 0.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(packedLight)
         .normal(matrix3f, 0.0F, -1.0F, 0.0F)
         .endVertex();
      builder.vertex(matrix4f, size * stretch, -size, -topZOffset)
         .color(aR, aG, aB, aA)
         .uv(0.0F, 0.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(packedLight)
         .normal(matrix3f, 0.0F, -1.0F, 0.0F)
         .endVertex();
      builder.vertex(matrix4f, -size * stretch, -size, -topZOffset)
         .color(aR, aG, aB, aA)
         .uv(0.0F, 0.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(packedLight)
         .normal(matrix3f, 0.0F, -1.0F, 0.0F)
         .endVertex();
      builder.vertex(matrix4f, -size * stretch, size, 0.0F)
         .color(aR, aG, aB, aA)
         .uv(0.0F, 0.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(packedLight)
         .normal(matrix3f, 0.0F, -1.0F, 0.0F)
         .endVertex();
      builder.vertex(matrix4f, -size * stretch, size, 0.0F)
         .color(aR, aG, aB, aA)
         .uv(0.0F, 0.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(packedLight)
         .normal(matrix3f, 0.0F, -1.0F, 0.0F)
         .endVertex();
      builder.vertex(matrix4f, -size * stretch, -size, -topZOffset)
         .color(aR, aG, aB, aA)
         .uv(0.0F, 0.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(packedLight)
         .normal(matrix3f, 0.0F, -1.0F, 0.0F)
         .endVertex();
      builder.vertex(matrix4f, size * stretch, -size, -topZOffset)
         .color(aR, aG, aB, aA)
         .uv(0.0F, 0.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(packedLight)
         .normal(matrix3f, 0.0F, -1.0F, 0.0F)
         .endVertex();
      builder.vertex(matrix4f, size * stretch, size, 0.0F)
         .color(aR, aG, aB, aA)
         .uv(0.0F, 0.0F)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(packedLight)
         .normal(matrix3f, 0.0F, -1.0F, 0.0F)
         .endVertex();
      stack.popPose();
   }
}
