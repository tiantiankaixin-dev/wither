package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.TentacleModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.ThreeHeadedWitherStormModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass.IntermediateDevourerBodyModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass.LowResIntermediateDevourerBodyModel;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormIntermediateDevourerModel<T extends WitherStormEntity> extends ThreeHeadedWitherStormModel<T> {
   public WitherStormIntermediateDevourerModel(ModelPart root) {
      super(root, 3.0F);
   }

   public static LayerDefinition createLayerDefinition() {
      MeshDefinition mesh = ThreeHeadedWitherStormModel.createMesh(
         new PartPose[]{PartPose.offset(-22.0F, -65.0F, -40.0F), PartPose.offset(0.0F, -32.0F, -23.0F), PartPose.offset(32.0F, -60.0F, -24.0F)}
      );
      PartDefinition root = mesh.getRoot();
      IntermediateDevourerBodyModel.createBodyModel(root, 0.2F);
      LowResIntermediateDevourerBodyModel.createBodyModel(root, 0.3F);
      PartDefinition tentacles = root.getChild("tentacles");
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle0", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 24, 24, 28, 28, 32},
         PartPose.offset(-20.0F, -50.0F, 0.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle1", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 24, 24, 24, 28, 38},
         PartPose.offset(20.0F, -55.0F, 0.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle2", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 24, 24, 28, 32, 28},
         PartPose.offset(-10.0F, -40.0F, -10.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle3", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 18, 24, 24, 24, 28},
         PartPose.offset(8.0F, -45.0F, -10.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle4", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 18, 24, 28, 32, 32},
         PartPose.offset(-8.0F, -50.0F, 12.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle5", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 20, 26, 28, 32, 28},
         PartPose.offset(10.0F, -45.0F, 12.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle6", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{18, 20, 26, 28, 28, 24},
         PartPose.offset(-2.0F, 0.0F, 0.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacleLarge0", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{16, 20, 24, 28, 32, 28},
         PartPose.offset(-34.0F, -43.0F, 0.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacleLarge1", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{20, 20, 24, 24, 28, 32},
         PartPose.offset(38.0F, -48.0F, 2.0F)
      );
      return LayerDefinition.create(mesh, 160, 160);
   }

   @Override
   protected void configureHeads(ModelPart root, float headScale) {
      super.configureHeads(root, headScale);
      this.rightHead.tractorBeamDistance = 90.0F;
      this.rightHead.tractorBeamStartSize = 0.1F;
      this.rightHead.tractorBeamEndSize = 5.0F;
      this.rightHead.tractorBeamXOffset = 0.0F;
      this.rightHead.tractorBeamYOffset = 8.0F;
      this.rightHead.tractorBeamZOffset = 0.0F;
      this.rightHead.animationOffset = 100.0F;
      this.rightHead.pivotOffsetX = -4.0F;
      this.rightHead.pivotOffsetY = 0.325F;
      this.rightHead.pivotOffsetZ = 0.0F;
      this.middleHead.tractorBeamDistance = 90.0F;
      this.middleHead.tractorBeamStartSize = 0.1F;
      this.middleHead.tractorBeamEndSize = 5.0F;
      this.middleHead.tractorBeamXOffset = 0.0F;
      this.middleHead.tractorBeamYOffset = 8.0F;
      this.middleHead.tractorBeamZOffset = 0.0F;
      this.middleHead.pivotOffsetX = -4.0F;
      this.middleHead.pivotOffsetY = 0.325F;
      this.middleHead.pivotOffsetZ = 0.0F;
      this.leftHead.tractorBeamDistance = 90.0F;
      this.leftHead.tractorBeamStartSize = 0.1F;
      this.leftHead.tractorBeamEndSize = 5.0F;
      this.leftHead.tractorBeamXOffset = 0.0F;
      this.leftHead.tractorBeamYOffset = 8.0F;
      this.leftHead.tractorBeamZOffset = 0.0F;
      this.leftHead.animationOffset = 175.0F;
      this.leftHead.pivotOffsetX = -4.0F;
      this.leftHead.pivotOffsetY = 0.325F;
      this.leftHead.pivotOffsetZ = 0.0F;
   }

   @Override
   protected void configureTentacles(ModelPart root) {
      this.tentacles = new TentacleModel[9];
      this.tentacles[0] = new TentacleModel(root.getChild("tentacle0"), 3.0F);
      this.tentacles[0].animationSpeed = 0.2F;
      this.tentacles[0].yRotationalOffset = (float)Math.toRadians(90.0);
      this.tentacles[0].xRotationalOffset = (float)Math.toRadians(90.0);
      this.tentacles[0].xAngularOffset = -0.3925F;
      this.tentacles[0].yAngularOffset = 0.17444445F;
      this.tentacles[0].reach = 2.0F;
      this.tentacles[1] = new TentacleModel(root.getChild("tentacle1"), 3.0F);
      this.tentacles[1].animationOffset = 8.0F;
      this.tentacles[1].animationSpeed = 0.4F;
      this.tentacles[1].yRotationalOffset = (float)Math.toRadians(270.0);
      this.tentacles[1].xRotationalOffset = (float)Math.toRadians(90.0);
      this.tentacles[1].xAngularOffset = -0.3925F;
      this.tentacles[1].yAngularOffset = -0.17444445F;
      this.tentacles[1].reach = 1.0F;
      this.tentacles[2] = new TentacleModel(root.getChild("tentacle2"), 3.0F);
      this.tentacles[2].animationOffset = 16.0F;
      this.tentacles[2].animationSpeed = 0.2F;
      this.tentacles[2].xRotationalOffset = (float)Math.toRadians(100.0);
      this.tentacles[2].xAngularOffset = -0.3925F;
      this.tentacles[2].yAngularOffset = 0.2616667F;
      this.tentacles[2].reach = 1.5F;
      this.tentacles[3] = new TentacleModel(root.getChild("tentacle3"), 3.0F);
      this.tentacles[3].animationOffset = 9.0F;
      this.tentacles[3].animationSpeed = 0.2F;
      this.tentacles[3].yRotationalOffset = (float)Math.toRadians(320.0);
      this.tentacles[3].xRotationalOffset = (float)Math.toRadians(90.0);
      this.tentacles[3].xAngularOffset = -0.3925F;
      this.tentacles[3].yAngularOffset = -0.13083334F;
      this.tentacles[3].reach = 1.75F;
      this.tentacles[4] = new TentacleModel(root.getChild("tentacle4"), 3.0F);
      this.tentacles[4].animationOffset = 12.0F;
      this.tentacles[4].animationSpeed = 0.2F;
      this.tentacles[4].yRotationalOffset = (float)Math.toRadians(120.0);
      this.tentacles[4].xRotationalOffset = (float)Math.toRadians(70.0);
      this.tentacles[4].xAngularOffset = -0.3925F;
      this.tentacles[4].yAngularOffset = 0.2616667F;
      this.tentacles[4].reach = 2.0F;
      this.tentacles[5] = new TentacleModel(root.getChild("tentacle5"), 3.0F);
      this.tentacles[5].animationOffset = 20.0F;
      this.tentacles[5].animationSpeed = 0.4F;
      this.tentacles[5].yRotationalOffset = (float)Math.toRadians(220.0);
      this.tentacles[5].xRotationalOffset = (float)Math.toRadians(70.0);
      this.tentacles[5].xAngularOffset = -0.44857144F;
      this.tentacles[5].yAngularOffset = -0.2616667F;
      this.tentacles[5].reach = 1.5F;
      this.tentacles[6] = new TentacleModel(root.getChild("tentacle6"), 3.0F);
      this.tentacles[6].animationOffset = 24.0F;
      this.tentacles[6].animationSpeed = 0.15F;
      this.tentacles[6].yRotationalOffset = (float)Math.toRadians(45.0);
      this.tentacles[6].xRotationalOffset = (float)Math.toRadians(90.0);
      this.tentacles[6].xAngularOffset = 0.2616667F;
      this.tentacles[6].yAngularOffset = 0.3925F;
      this.tentacles[6].reach = 2.0F;
      this.tentacles[7] = new TentacleModel(root.getChild("tentacleLarge0"), 3.0F);
      this.tentacles[7].animationOffset = 80.0F;
      this.tentacles[7].animationSpeed = 0.25F;
      this.tentacles[7].yRotationalOffset = (float)Math.toRadians(-100.0);
      this.tentacles[7].xRotationalOffset = (float)Math.toRadians(120.0);
      this.tentacles[7].xAngularOffset = 0.2616667F;
      this.tentacles[7].yAngularOffset = 0.3925F;
      this.tentacles[7].reach = 2.0F;
      this.tentacles[8] = new TentacleModel(root.getChild("tentacleLarge1"), 3.0F);
      this.tentacles[8].animationOffset = 35.0F;
      this.tentacles[8].animationSpeed = 0.25F;
      this.tentacles[8].yRotationalOffset = (float)Math.toRadians(120.0);
      this.tentacles[8].xRotationalOffset = (float)Math.toRadians(100.0);
      this.tentacles[8].xAngularOffset = 0.3925F;
      this.tentacles[8].yAngularOffset = -0.2616667F;
      this.tentacles[8].reach = 2.0F;
   }
}
