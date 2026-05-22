package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.SingleHeadWitherStormModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass.WSHunchback2_1;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormHunchback2_1Model<T extends WitherStormEntity> extends SingleHeadWitherStormModel<T> {
   private final ModelPart base;
   private final ModelPart rightHead;
   private final ModelPart leftHead;
   private final ModelPart ribcage;

   public WitherStormHunchback2_1Model(ModelPart root) {
      super(root, 0.7F);
      this.base = root.getChild("witherBase");
      this.ribcage = this.base.getChild("ribcage");
      this.rightHead = this.base.getChild("right_head");
      this.leftHead = this.base.getChild("left_head");
   }

   public static LayerDefinition createLayerDefinition(CubeDeformation def) {
      MeshDefinition mesh = WitherStormCommandBlockModel.createBaseMesh(SingleHeadWitherStormModel.createMesh(PartPose.ZERO), def, false, true, false);
      PartDefinition root = mesh.getRoot();
      WSHunchback2_1.createBodyModel(root, 1.0F);
      return LayerDefinition.create(mesh, 160, 160);
   }

   @Override
   protected void configureTentacles(ModelPart root) {
   }

   @Override
   public void setupAnimations(T entity, float partialTicks, float tickCount, float yRot, float xRot) {
      super.setupAnimations(entity, partialTicks, tickCount, yRot, xRot);
      float f = Mth.cos(tickCount * 0.1F);
      this.ribcage.xRot = (0.065F + 0.05F * f) * (float) Math.PI;
      WitherStormCommandBlockModel.setupHeadRotation(entity, this.leftHead, 1, partialTicks);
      WitherStormCommandBlockModel.setupHeadRotation(entity, this.rightHead, 2, partialTicks);
   }

   @Override
   protected void renderExtra(PoseStack stack, VertexConsumer consumer, int packedLight, int overlayTexture, float r, float g, float b, float a) {
      this.base.render(stack, consumer, packedLight, overlayTexture, r, g, b, a);
   }

   @Override
   public void scaleMass(PoseStack stack) {
      stack.scale(1.001F, 1.001F, 1.001F);
   }
}
