package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.AbstractWitherStormModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass.WSHunchback1_1;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormHunchback1_1Model<T extends WitherStormEntity> extends AbstractWitherStormModel<T> {
   private final ModelPart base;
   private final ModelPart centerHead;
   private final ModelPart rightHead;
   private final ModelPart leftHead;
   private final ModelPart ribcage;
   private final ModelPart tail;

   public WitherStormHunchback1_1Model(ModelPart root) {
      super(root, 1.0F);
      this.base = root.getChild("witherBase");
      this.ribcage = this.base.getChild("ribcage");
      this.tail = this.base.getChild("tail");
      this.centerHead = this.base.getChild("center_head");
      this.rightHead = this.base.getChild("right_head");
      this.leftHead = this.base.getChild("left_head");
   }

   @Override
   protected void configureHeads(ModelPart root, float scale) {
   }

   @Override
   protected void configureTentacles(ModelPart root) {
   }

   public static LayerDefinition createLayerDefinition(CubeDeformation def) {
      MeshDefinition mesh = WitherStormCommandBlockModel.createBaseMesh(AbstractWitherStormModel.createMesh(), def, true, true, true);
      WSHunchback1_1.createBodyModel(mesh.getRoot(), 1.0F);
      return LayerDefinition.create(mesh, 160, 160);
   }

   @Override
   public void setupAnimations(T entity, float partialTicks, float tickCount, float yRot, float xRot) {
      super.setupAnimations(entity, partialTicks, tickCount, yRot, xRot);
      float f = Mth.cos(tickCount * 0.1F);
      this.ribcage.xRot = (0.065F + 0.05F * f) * (float) Math.PI;
      this.tail.setPos(-2.0F, 6.9F + Mth.cos(this.ribcage.xRot) * 10.0F, -0.5F + Mth.sin(this.ribcage.xRot) * 10.0F);
      this.tail.xRot = (0.265F + 0.1F * f) * (float) Math.PI;
      this.centerHead.yRot = yRot * (float) (Math.PI / 180.0);
      this.centerHead.xRot = xRot * (float) (Math.PI / 180.0);
      WitherStormCommandBlockModel.setupHeadRotation(entity, this.leftHead, 1, partialTicks);
      WitherStormCommandBlockModel.setupHeadRotation(entity, this.rightHead, 2, partialTicks);
   }

   @Override
   protected void renderExtra(PoseStack stack, VertexConsumer consumer, int packedLight, int overlayTexture, float r, float g, float b, float a) {
      this.base.render(stack, consumer, packedLight, overlayTexture, r, g, b, a);
   }

   @Override
   public void scaleMass(PoseStack stack) {
   }
}
