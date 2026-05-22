package nonamecrackers2.witherstormmod.client.renderer.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class FlamingWitherSkullModel extends SkullModelBase {
   private final ModelPart root;
   private final ModelPart base;

   public FlamingWitherSkullModel(ModelPart root) {
      this.root = root;
      this.base = root.getChild("head");
   }

   public static LayerDefinition createLayerDefinition() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition base = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, false), PartPose.offset(0.0F, -3.5F, 0.0F)
      );
      PartDefinition flame = base.addOrReplaceChild(
         "flame",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.0F, -8.0F, 4.0F, 8.0F, 0.0F, 8.0F, CubeDeformation.NONE, 0.5F, 0.5F)
            .texOffs(0, 24)
            .addBox(-4.0F, 0.0F, 4.0F, 8.0F, 0.0F, 8.0F, CubeDeformation.NONE, 0.5F, 0.5F),
         PartPose.offset(0.0F, 4.0F, 0.0F)
      );
      flame.addOrReplaceChild(
         "flameSide",
         CubeListBuilder.create()
            .texOffs(0, 16)
            .addBox(0.0F, -4.0F, -4.0F, 0.0F, 8.0F, 8.0F, CubeDeformation.NONE, 0.5F, 0.5F)
            .texOffs(0, 16)
            .addBox(8.0F, -4.0F, -4.0F, 0.0F, 8.0F, 8.0F, CubeDeformation.NONE, 0.5F, 0.5F),
         PartPose.offsetAndRotation(-4.0F, -4.0F, 8.0F, -1.5708F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 32, 32);
   }

   public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.root.render(matrixStack, buffer, packedLight, packedOverlay);
   }

   public void setupAnim(float f, float yRot, float xRot) {
      this.base.yRot = yRot * (float) (Math.PI / 180.0);
      this.base.xRot = xRot * (float) (Math.PI / 180.0);
   }
}
