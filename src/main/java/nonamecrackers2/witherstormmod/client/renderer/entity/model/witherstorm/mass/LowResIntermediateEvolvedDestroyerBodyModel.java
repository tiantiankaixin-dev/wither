package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class LowResIntermediateEvolvedDestroyerBodyModel {
   public static PartDefinition createBodyModel(PartDefinition root, float texScale) {
      PartDefinition body = root.addOrReplaceChild(
         "lowResMass",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-3.0F, -6.0F, -3.0F, 5.0F, 18.0F, 6.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-3.0F, -11.0F, -10.0F, 11.0F, 9.0F, 8.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-6.0F, -18.0F, -3.0F, 11.0F, 12.0F, 6.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-3.0F, 12.0F, -3.0F, 5.0F, 3.0F, 6.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-6.0F, -14.3919F, 2.4473F, 11.0F, 9.0F, 5.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-2.0F, 16.0F, 0.0F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.0F, 15.0F, 0.0F, 1.0F, 7.0F, 1.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-1.0F, 16.0F, -2.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-1.0F, 16.0F, -3.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-1.0F, 15.0F, -3.0F, 1.0F, 1.0F, 3.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-2.0F, 15.0F, -3.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-3.0F, 15.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-2.0F, 15.0F, 0.0F, 1.0F, 1.0F, 3.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.0F, 15.0F, -2.0F, 1.0F, 1.0F, 2.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.0F, 15.0F, -2.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.0F, 16.0F, -2.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-1.0F, 15.0F, 2.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.0F, 15.0F, 1.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offset(0.0F, -15.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r1",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 5.0F, 2.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 4.0F, -6.0F, 0.1309F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r2",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -14.0F, -2.0F, 5.0F, 14.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, 0.3927F)
      );
      body.addOrReplaceChild(
         "voxel_r3",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-2.4965F, -8.2953F, -1.0F, 5.0F, 8.0F, 5.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 1.2519F, -0.2368F, 0.8109F)
      );
      body.addOrReplaceChild(
         "voxel_r4",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.4965F, -4.2953F, -2.0F, 11.0F, 4.0F, 6.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -1.1345F)
      );
      body.addOrReplaceChild(
         "voxel_r5",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.704F, -9.1279F, -2.0F, 7.0F, 11.0F, 6.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(8.5F, -8.0F, -1.0F, 0.0F, 0.0F, -0.6545F)
      );
      body.addOrReplaceChild(
         "voxel_r6",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-4.4965F, -7.2953F, -2.0F, 7.0F, 8.0F, 6.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 0.0F, 0.0F, 0.6981F)
      );
      body.addOrReplaceChild(
         "voxel_r7",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-2.0341F, -3.1877F, -4.5034F, 5.0F, 7.0F, 13.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(0.5F, 0.0F, -3.0F, 1.9222F, -0.318F, 0.8876F)
      );
      body.addOrReplaceChild(
         "voxel_r8",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-3.4217F, -10.2414F, -9.7983F, 6.0F, 7.0F, 13.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.5F, -5.0F, -1.0F, 2.2689F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r9",
         CubeListBuilder.create().texOffs(0, 24).addBox(-3.4217F, -10.2414F, -5.7983F, 6.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.5F, -5.0F, -1.0F, 1.4399F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r10",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -8.2953F, -2.0F, 12.0F, 10.0F, 7.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 1.0036F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r11",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5035F, -7.2953F, -2.0F, 5.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -0.5672F)
      );
      body.addOrReplaceChild(
         "voxel_r12",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.4965F, -6.2953F, -2.0F, 5.0F, 7.0F, 9.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, -1.0943F, 0.0603F, 0.8873F)
      );
      body.addOrReplaceChild(
         "voxel_r13",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -6.2953F, -2.0F, 6.0F, 8.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, -1.0908F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r14",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.564F, -3.5178F, -9.0F, 6.0F, 10.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-9.5F, -11.0F, -6.0F, 0.0F, -1.1345F, 0.1309F)
      );
      body.addOrReplaceChild(
         "voxel_r15",
         CubeListBuilder.create().texOffs(0, 24).addBox(-3.9555F, -5.4921F, -5.0F, 8.0F, 10.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-9.5F, -8.0F, -5.0F, 0.0F, -0.7418F, 0.1309F)
      );
      body.addOrReplaceChild(
         "voxel_r16",
         CubeListBuilder.create().texOffs(0, 24).addBox(-3.3471F, -12.4664F, -5.0F, 6.0F, 13.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-9.5F, -5.0F, -1.0F, 0.0F, 0.0F, 0.1309F)
      );
      body.addOrReplaceChild(
         "voxel_r17",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -7.2953F, -2.0F, 6.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r18",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -14.0F, -2.0F, 5.0F, 14.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, -0.4363F)
      );
      body.addOrReplaceChild(
         "voxel_r19",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.5F, -8.8659F, -2.4041F, 3.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-2.5F, 6.5F, -3.0F, 0.3295F, 0.1172F, -0.3295F)
      );
      body.addOrReplaceChild(
         "voxel_r20",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -14.0F, -3.0F, 5.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 13.0F, 0.0F, 0.3491F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r21",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-2.5F, -2.0F, -2.0F, 5.0F, 2.0F, 5.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-2.5F, -21.0F, -2.0F, 5.0F, 19.0F, 5.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 15.0F, 0.0F, -0.2182F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r22",
         CubeListBuilder.create().texOffs(0, 24).addBox(-5.5F, -3.3919F, -2.5527F, 11.0F, 5.0F, 5.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, -14.0F, 5.0F, 1.2217F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r23",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -5.3919F, -2.5527F, 7.0F, 9.0F, 5.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(4.5F, -9.0F, 5.0F, 0.0F, 0.829F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r24",
         CubeListBuilder.create().texOffs(0, 24).addBox(-9.0F, -8.0F, -2.0F, 14.0F, 8.0F, 8.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(3.0F, -11.0F, -8.0F, -0.9163F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r25",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.0F, -8.0F, -1.0F, 9.0F, 11.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(8.0F, -5.0F, -7.0F, 0.0F, -1.0908F, 0.0F)
      );
      return body;
   }
}
