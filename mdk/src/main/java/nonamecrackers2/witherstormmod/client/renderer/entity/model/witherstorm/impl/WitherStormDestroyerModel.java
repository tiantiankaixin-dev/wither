package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.TentacleModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.ThreeHeadedWitherStormModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass.DestroyerBodyModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass.LowResDestroyerBodyModel;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormDestroyerModel<T extends WitherStormEntity> extends ThreeHeadedWitherStormModel<T> {
   public WitherStormDestroyerModel(ModelPart root) {
      super(root, 3.0F);
   }

   public static LayerDefinition createLayerDefinition() {
      MeshDefinition mesh = ThreeHeadedWitherStormModel.createMesh(
         new PartPose[]{PartPose.offset(-22.0F, -65.0F, -40.0F), PartPose.offset(0.0F, -32.0F, -23.0F), PartPose.offset(32.0F, -60.0F, -24.0F)}
      );
      PartDefinition root = mesh.getRoot();
      DestroyerBodyModel.createBodyModel(root, 0.2F);
      LowResDestroyerBodyModel.createBodyModel(root, 0.3F);
      PartDefinition tentacles = root.getChild("tentacles");
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle0", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{23, 28, 28, 28, 32, 32},
         PartPose.offset(-10.0F, -30.0F, 0.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle1", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{23, 28, 28, 28, 32, 32},
         PartPose.offset(30.0F, -115.0F, -10.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle2", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{23, 28, 28, 28, 32, 32},
         PartPose.offset(10.0F, -40.0F, 10.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle3", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{23, 28, 28, 28, 32, 32},
         PartPose.offset(-50.0F, -100.0F, 0.0F)
      );
      TentacleModel.populateDefinition(
         tentacles.addOrReplaceChild("tentacle4", CubeListBuilder.create(), PartPose.ZERO),
         new int[]{23, 28, 28, 28, 32, 32},
         PartPose.offset(-10.0F, -95.0F, 15.0F)
      );
      return LayerDefinition.create(mesh, 160, 160);
   }

   @Override
   protected void configureHeads(ModelPart part, float headScale) {
      super.configureHeads(part, headScale);
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
      this.tentacles = new TentacleModel[5];
      this.tentacles[0] = new TentacleModel(root.getChild("tentacle0"), 2.0F);
      this.tentacles[0].animationSpeed = 0.6F;
      this.tentacles[0].yRotationalOffset = 1.57F;
      this.tentacles[0].xRotationalOffset = 0.3925F;
      this.tentacles[0].xAngularOffset = -0.19625F;
      this.tentacles[0].yAngularOffset = -0.19625F;
      this.tentacles[0].reach = 2.0F;
      this.tentacles[1] = new TentacleModel(root.getChild("tentacle1"), 2.0F);
      this.tentacles[1].animationSpeed = 0.6F;
      this.tentacles[1].yRotationalOffset = -0.3925F;
      this.tentacles[1].xRotationalOffset = -0.628F;
      this.tentacles[1].xAngularOffset = 0.3925F;
      this.tentacles[1].yAngularOffset = -0.2616667F;
      this.tentacles[1].reach = 2.0F;
      this.tentacles[1].animationOffset = 20.0F;
      this.tentacles[2] = new TentacleModel(root.getChild("tentacle2"), 2.0F);
      this.tentacles[2].animationSpeed = 0.6F;
      this.tentacles[2].yRotationalOffset = -1.7444445F;
      this.tentacles[2].xAngularOffset = 0.3925F;
      this.tentacles[2].yAngularOffset = -0.19625F;
      this.tentacles[2].reach = 2.0F;
      this.tentacles[2].animationOffset = 10.0F;
      this.tentacles[3] = new TentacleModel(root.getChild("tentacle3"), 2.0F);
      this.tentacles[3].animationSpeed = 0.6F;
      this.tentacles[3].yRotationalOffset = 1.256F;
      this.tentacles[3].xAngularOffset = -0.5233334F;
      this.tentacles[3].yAngularOffset = 0.3925F;
      this.tentacles[3].animationOffset = 30.0F;
      this.tentacles[3].reach = 2.0F;
      this.tentacles[4] = new TentacleModel(root.getChild("tentacle4"), 2.0F);
      this.tentacles[4].animationSpeed = 0.6F;
      this.tentacles[4].yRotationalOffset = -0.19625F;
      this.tentacles[4].xRotationalOffset = -1.0466667F;
      this.tentacles[4].xAngularOffset = 0.2616667F;
      this.tentacles[4].yAngularOffset = 0.098125F;
      this.tentacles[4].animationOffset = 40.0F;
      this.tentacles[4].reach = 2.0F;
   }
}
