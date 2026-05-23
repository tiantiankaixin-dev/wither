package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.SingleHeadWitherStormModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.TentacleModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass.WSHunchback3_1;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormHunchback3_1Model<T extends WitherStormEntity> extends SingleHeadWitherStormModel<T> {
   private final ModelPart base;
   private final ModelPart rightHead;
   private final ModelPart leftHead;
   private final ModelPart ribcage;

   public WitherStormHunchback3_1Model(ModelPart root) {
      super(root, 0.7F);
      this.base = root.getChild("witherBase");
      this.ribcage = this.base.getChild("ribcage");
      this.rightHead = this.base.getChild("right_head");
      this.leftHead = this.base.getChild("left_head");
   }

   public static LayerDefinition createLayerDefinition(CubeDeformation def) {
      MeshDefinition mesh = WitherStormCommandBlockModel.createBaseMesh(SingleHeadWitherStormModel.createMesh(PartPose.ZERO), def, false, false, false);
      PartDefinition root = mesh.getRoot();
      WSHunchback3_1.createBodyModel(root, 1.0F);
      PartDefinition tentacles = root.getChild("tentacles");
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle0", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 24, 24, 24, 24, 32},
         PartPose.offset(0.0F, 0.0F, 20.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle1", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 24, 24, 24, 24, 32},
         PartPose.offset(0.0F, 0.0F, 20.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle2", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 24, 24, 24, 24, 32},
         PartPose.offset(0.0F, 20.0F, 20.0F)
      );
      return LayerDefinition.create(mesh, 160, 160);
   }

   @Override
   protected void configureTentacles(ModelPart root) {
      this.tentacles = new TentacleModel[3];
      this.tentacles[0] = new TentacleModel(root.getChild("tentacle0"), 0.5F);
      this.tentacles[0].animationSpeed = 0.5F;
      this.tentacles[0].yRotationalOffset = 4.14F;
      this.tentacles[0].xAngularOffset = -0.17444445F;
      this.tentacles[0].yAngularOffset = 0.3488889F;
      this.tentacles[0].reach = 2.0F;
      this.tentacles[1] = new TentacleModel(root.getChild("tentacle1"), 0.5F);
      this.tentacles[1].animationSpeed = 0.5F;
      this.tentacles[1].yRotationalOffset = 11.775001F;
      this.tentacles[1].xRotationalOffset = 9.859601F;
      this.tentacles[1].xAngularOffset = 0.17444445F;
      this.tentacles[1].yAngularOffset = 0.3488889F;
      this.tentacles[1].animationOffset = 10.0F;
      this.tentacles[1].reach = 2.15F;
      this.tentacles[2] = new TentacleModel(root.getChild("tentacle2"), 0.5F);
      this.tentacles[2].animationSpeed = 0.5F;
      this.tentacles[2].yRotationalOffset = 3.14F;
      this.tentacles[2].xRotationalOffset = 0.785F;
      this.tentacles[2].xAngularOffset = 0.5233334F;
      this.tentacles[2].yAngularOffset = -0.17444445F;
      this.tentacles[2].animationOffset = 32.0F;
      this.tentacles[2].reach = 1.85F;
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
      stack.mulPose(Axis.XP.rotationDegrees(20.0F));
   }

   @Override
   public void scaleTentacles(PoseStack stack, TentacleModel model) {
      super.scaleTentacles(stack, model);
      stack.mulPose(Axis.XP.rotationDegrees(20.0F));
   }
}
