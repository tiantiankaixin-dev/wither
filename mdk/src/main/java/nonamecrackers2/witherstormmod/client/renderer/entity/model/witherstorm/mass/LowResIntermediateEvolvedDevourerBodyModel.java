package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class LowResIntermediateEvolvedDevourerBodyModel {
   public static PartDefinition createBodyModel(PartDefinition root, float texScale) {
      PartDefinition body = root.addOrReplaceChild(
         "lowResMass",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-3.0F, -6.0F, -3.0F, 5.0F, 18.0F, 6.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-3.0F, 12.0F, -3.0F, 5.0F, 3.0F, 6.0F, CubeDeformation.NONE, texScale, texScale)
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
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -14.0F, -2.0F, 5.0F, 14.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, 0.3927F)
      );
      body.addOrReplaceChild(
         "voxel_r2",
         CubeListBuilder.create().texOffs(0, 24).addBox(-13.9237F, 2.9582F, 0.0425F, 14.0F, 11.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-20.95F, -26.05F, -24.4F, 0.6668F, 0.3512F, 0.2644F)
      );
      body.addOrReplaceChild(
         "voxel_r3",
         CubeListBuilder.create().texOffs(0, 24).addBox(-13.85F, 0.2938F, 0.0977F, 14.0F, 15.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.15F, -26.05F, -26.7F, 0.6237F, 0.1065F, 0.0763F)
      );
      body.addOrReplaceChild(
         "voxel_r4",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.1844F, -2.0999F, 0.001F, 15.0F, 16.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(21.05F, -26.05F, -24.3F, 0.689F, -0.4192F, -0.3235F)
      );
      body.addOrReplaceChild(
         "voxel_r5",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.05F, 0.2938F, 0.0977F, 14.0F, 15.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(6.95F, -26.05F, -26.7F, 0.6237F, -0.1065F, -0.0763F)
      );
      body.addOrReplaceChild(
         "voxel_r6",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, 0.2938F, 0.0977F, 14.0F, 15.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -26.05F, -26.7F, 0.6196F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r7",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-10.8919F, -3.7192F, -0.1851F, 11.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-20.95F, -30.95F, -24.6F, 0.163F, 0.376F, -0.0375F)
      );
      body.addOrReplaceChild(
         "voxel_r8",
         CubeListBuilder.create().texOffs(0, 24).addBox(-13.95F, 0.2115F, -0.1135F, 14.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.05F, -34.95F, -26.1F, -0.0439F, 0.1133F, -0.005F)
      );
      body.addOrReplaceChild(
         "voxel_r9",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0523F, 0.338F, 0.0232F, 13.0F, 6.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(20.75F, -34.95F, -23.2F, -0.0485F, -0.4533F, 0.0213F)
      );
      body.addOrReplaceChild(
         "voxel_r10",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.05F, 0.2115F, -0.1135F, 14.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(7.05F, -34.95F, -26.1F, -0.0447F, -0.218F, 0.0097F)
      );
      body.addOrReplaceChild(
         "voxel_r11",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, 0.2115F, -0.1135F, 14.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -34.95F, -26.1F, -0.0436F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r12",
         CubeListBuilder.create().texOffs(0, 24).addBox(-13.95F, -0.0326F, 0.1049F, 14.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.05F, -47.55F, -24.1F, -0.176F, 0.1289F, -0.0229F)
      );
      body.addOrReplaceChild(
         "voxel_r13",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0021F, -1.0062F, 0.0059F, 14.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(20.65F, -46.05F, -21.2F, -0.1886F, -0.3864F, 0.0718F)
      );
      body.addOrReplaceChild(
         "voxel_r14",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.05F, -0.0326F, 0.1049F, 14.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(7.05F, -47.55F, -24.1F, -0.1787F, -0.2148F, 0.0385F)
      );
      body.addOrReplaceChild(
         "voxel_r15",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -0.0326F, 0.1049F, 14.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -47.55F, -24.1F, -0.1745F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r16",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0857F, -2.0658F, 0.0463F, 14.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(20.65F, -56.6F, -16.9F, -0.3753F, -0.3678F, 0.1407F)
      );
      body.addOrReplaceChild(
         "voxel_r17",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.15F, 0.1331F, -0.1049F, 14.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(7.15F, -59.85F, -19.4F, -0.3604F, -0.2457F, 0.0914F)
      );
      body.addOrReplaceChild(
         "voxel_r18",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, 0.1331F, -0.1049F, 14.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -59.85F, -19.4F, -0.3491F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r19",
         CubeListBuilder.create().texOffs(0, 24).addBox(-9.1219F, -11.964F, -0.1495F, 9.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-20.75F, -47.85F, -21.8F, -0.3646F, 0.2865F, -0.1074F)
      );
      body.addOrReplaceChild(
         "voxel_r20",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-13.95F, -12.6481F, -0.0744F, 14.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.05F, -47.85F, -23.8F, -0.3518F, 0.123F, -0.045F)
      );
      body.addOrReplaceChild(
         "voxel_r21",
         CubeListBuilder.create().texOffs(0, 24).addBox(-14.15F, -0.185F, -0.0043F, 14.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-6.85F, -67.65F, -9.4F, -0.9278F, 0.1321F, -0.1741F)
      );
      body.addOrReplaceChild(
         "voxel_r22",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -0.185F, -0.0043F, 14.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -67.65F, -9.4F, -0.9163F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r23",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -0.185F, -0.0043F, 14.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -67.65F, 3.6F, -1.5708F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r24",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, 0.0345F, -0.0089F, 14.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -65.65F, 12.6F, -1.789F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r25",
         CubeListBuilder.create().texOffs(0, 24).addBox(-9.9953F, -0.0018F, -0.255F, 10.0F, 10.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-20.45F, -52.25F, 23.7F, -2.1573F, -0.3093F, -0.4295F)
      );
      body.addOrReplaceChild(
         "voxel_r26",
         CubeListBuilder.create().texOffs(0, 24).addBox(-14.05F, 0.0151F, 0.0378F, 14.0F, 17.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-6.95F, -55.35F, 26.1F, -2.2086F, -0.1582F, -0.2095F)
      );
      body.addOrReplaceChild(
         "voxel_r27",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, 0.0151F, 0.0378F, 14.0F, 17.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -55.35F, 26.1F, -2.2253F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r28",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.05F, 0.0151F, 0.0378F, 14.0F, 17.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(6.95F, -55.35F, 26.1F, -2.2211F, 0.0795F, 0.1041F)
      );
      body.addOrReplaceChild(
         "voxel_r29",
         CubeListBuilder.create().texOffs(0, 24).addBox(-13.95F, -0.1774F, 0.1114F, 14.0F, 17.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.05F, -39.85F, 32.6F, -2.7319F, -0.2815F, -0.1201F)
      );
      body.addOrReplaceChild(
         "voxel_r30",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0594F, -7.7106F, 0.033F, 9.0F, 15.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(20.85F, -45.85F, 27.4F, -2.9819F, 0.3614F, 0.1572F)
      );
      body.addOrReplaceChild(
         "voxel_r31",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.05F, -0.1774F, 0.1114F, 14.0F, 17.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(7.05F, -39.85F, 32.6F, -2.7435F, 0.1611F, 0.0674F)
      );
      body.addOrReplaceChild(
         "voxel_r32",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -0.1774F, 0.1114F, 14.0F, 17.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -39.85F, 32.6F, -2.7489F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r33",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-10.9682F, 0.2493F, -0.3086F, 11.0F, 10.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-20.05F, -39.65F, 27.4F, -2.7544F, -0.4795F, 0.0227F)
      );
      body.addOrReplaceChild(
         "voxel_r34",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-11.0951F, -3.4567F, -0.0649F, 11.0F, 10.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-20.05F, -33.35F, 27.4F, 3.0924F, -0.4795F, 0.0227F)
      );
      body.addOrReplaceChild(
         "voxel_r35",
         CubeListBuilder.create().texOffs(0, 24).addBox(-13.95F, -0.1617F, -0.0082F, 14.0F, 11.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.05F, -28.85F, 32.0F, 3.0952F, -0.3487F, 0.0159F)
      );
      body.addOrReplaceChild(
         "voxel_r36",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0119F, -6.0018F, -0.156F, 8.0F, 14.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(20.45F, -31.85F, 28.4F, 3.0051F, 0.4795F, -0.0227F)
      );
      body.addOrReplaceChild(
         "voxel_r37",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.15F, -0.1617F, -0.0082F, 14.0F, 11.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(7.15F, -28.85F, 32.0F, 3.0964F, 0.2615F, -0.0117F)
      );
      body.addOrReplaceChild(
         "voxel_r38",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -0.1617F, -0.0082F, 14.0F, 11.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -28.85F, 32.0F, 3.098F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r39",
         CubeListBuilder.create().texOffs(0, 24).addBox(-12.0562F, -2.4174F, 0.1568F, 12.0F, 8.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-20.35F, -24.35F, 26.6F, 2.8415F, -0.504F, 0.1483F)
      );
      body.addOrReplaceChild(
         "voxel_r40",
         CubeListBuilder.create().texOffs(0, 24).addBox(-14.05F, -0.1464F, -0.0224F, 14.0F, 15.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-6.95F, -14.35F, 28.1F, 2.8677F, -0.2947F, 0.0814F)
      );
      body.addOrReplaceChild(
         "voxel_r41",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.25F, -0.1464F, -0.0224F, 14.0F, 15.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(6.75F, -14.35F, 28.1F, 2.871F, 0.2527F, -0.0692F)
      );
      body.addOrReplaceChild(
         "voxel_r42",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -0.1464F, -0.0224F, 14.0F, 15.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -14.35F, 28.1F, 2.8798F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r43",
         CubeListBuilder.create().texOffs(0, 24).addBox(-14.05F, 0.0605F, 0.0021F, 14.0F, 15.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-6.95F, -0.85F, 21.1F, 2.6463F, -0.2393F, 0.1274F)
      );
      body.addOrReplaceChild(
         "voxel_r44",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0351F, -7.857F, 0.0495F, 14.0F, 15.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(7.05F, -7.85F, 24.8F, 2.4153F, 0.3027F, -0.1746F)
      );
      body.addOrReplaceChild(
         "voxel_r45",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, 0.0605F, 0.0021F, 14.0F, 15.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -0.85F, 21.1F, 2.6616F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r46",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, 0.4364F, -0.3882F, 14.0F, 15.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, 9.25F, 9.5F, 2.3126F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r47",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.5299F, -6.6204F, 1.9973F, 10.0F, 8.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(13.7F, 0.75F, -6.5F, 0.71F, -0.1666F, -0.1415F)
      );
      body.addOrReplaceChild(
         "voxel_r48",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -6.6204F, -0.1022F, 10.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(4.0F, 0.75F, -6.5F, 0.71F, -0.1666F, -0.1415F)
      );
      body.addOrReplaceChild(
         "voxel_r49",
         CubeListBuilder.create().texOffs(0, 24).addBox(-9.95F, -0.4106F, -0.0912F, 10.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-13.05F, -4.0F, -10.5F, 0.7644F, 0.235F, 0.1971F)
      );
      body.addOrReplaceChild(
         "voxel_r50",
         CubeListBuilder.create().texOffs(0, 24).addBox(-12.75F, -0.4106F, -0.0912F, 17.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -4.0F, -10.5F, 0.6981F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r51",
         CubeListBuilder.create().texOffs(0, 24).addBox(-9.9836F, -9.3378F, 0.0153F, 10.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-22.65F, -8.9F, -10.8F, 0.7302F, 0.4209F, 0.3507F)
      );
      body.addOrReplaceChild(
         "voxel_r52",
         CubeListBuilder.create().texOffs(0, 24).addBox(-9.95F, -12.8285F, -0.056F, 10.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-13.05F, -4.5F, -10.8F, 0.6737F, 0.2205F, 0.1728F)
      );
      body.addOrReplaceChild(
         "voxel_r53",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.05F, -12.8285F, -0.056F, 17.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(3.95F, -4.5F, -10.8F, 0.6587F, -0.1037F, -0.08F)
      );
      body.addOrReplaceChild(
         "voxel_r54",
         CubeListBuilder.create().texOffs(0, 24).addBox(-12.75F, -12.8285F, -0.056F, 17.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -4.5F, -10.8F, 0.6545F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r55",
         CubeListBuilder.create().texOffs(5, 29).addBox(0.2032F, -10.1153F, -0.0509F, 5.0F, 10.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-16.1F, -65.9F, 5.8F, 0.0199F, 0.2241F, 1.445F)
      );
      body.addOrReplaceChild(
         "voxel_r56",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.1338F, -9.9836F, -7.2605F, 5.0F, 10.0F, 14.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-16.1F, -65.9F, -1.0F, 0.0194F, 0.0496F, 1.4416F)
      );
      body.addOrReplaceChild(
         "voxel_r57",
         CubeListBuilder.create()
            .texOffs(0, 25)
            .addBox(-0.0124F, -13.1351F, -0.0187F, 5.0F, 11.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-28.7F, -62.6F, 6.0F, 0.0214F, 0.5319F, 1.3205F)
      );
      body.addOrReplaceChild(
         "voxel_r58",
         CubeListBuilder.create()
            .texOffs(0, 31)
            .addBox(-0.1135F, -4.9142F, -12.0212F, 5.0F, 5.0F, 12.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 28)
            .addBox(-0.1135F, -12.9142F, -12.0212F, 5.0F, 8.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-28.7F, -62.6F, -6.0F, 0.0217F, -0.5587F, 1.2982F)
      );
      body.addOrReplaceChild(
         "voxel_r59",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0714F, -4.0062F, -5.0201F, 5.0F, 4.0F, 12.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.0714F, -13.0062F, -5.0201F, 5.0F, 9.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-28.7F, -62.6F, -1.0F, 0.0184F, 0.0084F, 1.3099F)
      );
      body.addOrReplaceChild(
         "voxel_r60",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(0.0979F, -13.0953F, -0.0471F, 5.0F, 8.0F, 12.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.0979F, -5.0953F, -0.0471F, 5.0F, 5.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-38.7F, -54.3F, 6.2F, 0.0153F, 0.539F, 0.8812F)
      );
      body.addOrReplaceChild(
         "voxel_r61",
         CubeListBuilder.create()
            .texOffs(0, 31)
            .addBox(-0.0887F, -4.9364F, -11.9496F, 5.0F, 5.0F, 12.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 28)
            .addBox(-0.0887F, -12.9364F, -11.9496F, 5.0F, 8.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-38.7F, -54.3F, -5.9F, 0.015F, -0.5081F, 0.866F)
      );
      body.addOrReplaceChild(
         "voxel_r62",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0131F, -4.0007F, -4.8486F, 5.0F, 4.0F, 12.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.0131F, -13.0007F, -4.8486F, 5.0F, 9.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-38.7F, -54.3F, -1.0F, 0.0131F, 0.0154F, 0.8735F)
      );
      body.addOrReplaceChild(
         "voxel_r63",
         CubeListBuilder.create().texOffs(3, 27).addBox(0.1373F, -10.0495F, 0.048F, 5.0F, 8.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-44.8F, -46.4F, 6.2F, 0.0108F, 0.4978F, 0.6603F)
      );
      body.addOrReplaceChild(
         "voxel_r64",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0774F, -9.9357F, -11.9545F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-44.8F, -46.4F, -5.8F, 0.0105F, -0.4359F, 0.6508F)
      );
      body.addOrReplaceChild(
         "voxel_r65",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0085F, -9.9812F, -4.7535F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-44.8F, -46.4F, -1.0F, 0.0095F, 0.0179F, 0.6554F)
      );
      body.addOrReplaceChild(
         "voxel_r66",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0105F, -9.9865F, -0.1337F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-38.9F, -45.6F, 16.8F, 0.8349F, 0.769F, 1.1409F)
      );
      body.addOrReplaceChild(
         "voxel_r67",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0335F, -5.4448F, -0.0621F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-39.7F, -40.2F, 16.8F, -0.0015F, 1.0674F, 0.1304F)
      );
      body.addOrReplaceChild(
         "voxel_r68",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(0.1058F, -10.2583F, -0.0606F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-46.1F, -36.2F, 6.3F, -8.0E-4F, 0.5002F, 0.1313F)
      );
      body.addOrReplaceChild(
         "voxel_r69",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(0.0597F, -9.9512F, -11.9989F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-39.2F, -45.7F, -16.4F, -0.6312F, -0.6993F, 0.9799F)
      );
      body.addOrReplaceChild(
         "voxel_r70",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(0.0063F, -7.4053F, -11.9398F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-40.1F, -38.3F, -16.4F, -0.0012F, -0.9048F, 0.1326F)
      );
      body.addOrReplaceChild(
         "voxel_r71",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.1391F, -10.2672F, -11.9631F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-46.1F, -36.2F, -5.8F, -8.0E-4F, -0.5034F, 0.1321F)
      );
      body.addOrReplaceChild(
         "voxel_r72",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.042F, -10.2636F, -4.7621F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-46.1F, -36.2F, -1.0F, -7.0E-4F, 0.0202F, 0.1317F)
      );
      body.addOrReplaceChild(
         "voxel_r73",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0102F, -6.97F, 0.1138F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-39.0F, -31.0F, 16.7F, -0.0155F, 0.9957F, -0.2739F)
      );
      body.addOrReplaceChild(
         "voxel_r74",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.1259F, -10.0269F, 0.0573F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-43.6F, -26.7F, 6.1F, -0.0095F, 0.4722F, -0.2652F)
      );
      body.addOrReplaceChild(
         "voxel_r75",
         CubeListBuilder.create().texOffs(1, 25).addBox(0.0012F, -6.9745F, -9.9033F, 5.0F, 7.0F, 11.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-28.85F, -41.3F, -23.3F, -1.3291F, -1.1144F, 1.0912F)
      );
      body.addOrReplaceChild(
         "voxel_r76",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0389F, -7.9915F, -12.0609F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-28.85F, -33.0F, -23.1F, -0.0797F, -1.4648F, -0.1817F)
      );
      body.addOrReplaceChild(
         "voxel_r77",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(0.0088F, -6.3833F, -11.9972F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-38.6F, -32.0F, -16.1F, -0.0143F, -0.9415F, -0.2494F)
      );
      body.addOrReplaceChild(
         "voxel_r78",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0933F, -10.1271F, -12.0451F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-43.6F, -26.7F, -5.8F, -0.0099F, -0.5488F, -0.2558F)
      );
      body.addOrReplaceChild(
         "voxel_r79",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0049F, -10.0867F, -4.8442F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-43.6F, -26.7F, -1.0F, -0.0084F, 0.0184F, -0.2611F)
      );
      body.addOrReplaceChild(
         "voxel_r80",
         CubeListBuilder.create().texOffs(2, 26).addBox(-0.0562F, -6.2083F, 0.0828F, 5.0F, 9.0F, 10.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-32.8F, -23.5F, 16.2F, -0.0258F, 0.9293F, -0.7179F)
      );
      body.addOrReplaceChild(
         "voxel_r81",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0504F, -10.0542F, 0.0014F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-37.1F, -19.0F, 6.0F, -0.0179F, 0.5367F, -0.7064F)
      );
      body.addOrReplaceChild(
         "voxel_r82",
         CubeListBuilder.create().texOffs(6, 30).addBox(0.032F, -3.5024F, -5.9243F, 5.0F, 10.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-37.1F, -28.0F, -16.5F, -0.0236F, -0.8594F, -0.6794F)
      );
      body.addOrReplaceChild(
         "voxel_r83",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.1072F, -10.2392F, -12.001F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-37.1F, -19.0F, -6.0F, -0.0177F, -0.5104F, -0.6886F)
      );
      body.addOrReplaceChild(
         "voxel_r84",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0416F, -10.1621F, -5.0F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-37.1F, -19.0F, -1.0F, -0.0154F, 0.0131F, -0.6975F)
      );
      body.addOrReplaceChild(
         "voxel_r85",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0092F, -6.7604F, 0.1196F, 5.0F, 11.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-29.0F, -16.6F, 16.0F, 0.0F, 0.7854F, -0.4363F)
      );
      body.addOrReplaceChild(
         "voxel_r86",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0461F, -10.0516F, 0.0F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-33.0F, -10.0F, 6.0F, 0.0F, 0.5672F, -0.4363F)
      );
      body.addOrReplaceChild(
         "voxel_r87",
         CubeListBuilder.create()
            .texOffs(2, 26)
            .addBox(0.0734F, -7.6234F, -10.0158F, 5.0F, 12.0F, 10.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-34.9F, -14.0F, -3.0F, 0.0252F, -0.523F, -0.4867F)
      );
      body.addOrReplaceChild(
         "voxel_r88",
         CubeListBuilder.create().texOffs(3, 27).addBox(0.0461F, -10.0516F, -2.0F, 5.0F, 10.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-33.0F, -10.0F, -1.0F, 0.0F, 0.0F, -0.4363F)
      );
      body.addOrReplaceChild(
         "voxel_r89",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.011F, -6.6644F, 0.074F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-24.5F, -8.5F, 16.6F, 0.0852F, 0.914F, -0.5573F)
      );
      body.addOrReplaceChild(
         "voxel_r90",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0356F, -9.9108F, 0.0F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-26.6F, -2.5F, 6.0F, 0.0F, 0.48F, -0.6981F)
      );
      body.addOrReplaceChild(
         "voxel_r91",
         CubeListBuilder.create().texOffs(3, 27).addBox(-0.0356F, -9.9108F, -9.0F, 5.0F, 12.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-26.6F, -2.5F, -3.0F, 0.0F, -0.7418F, -0.6981F)
      );
      body.addOrReplaceChild(
         "voxel_r92",
         CubeListBuilder.create().texOffs(3, 27).addBox(-0.0356F, -9.9108F, -2.0F, 5.0F, 10.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-26.6F, -2.5F, -1.0F, 0.0F, 0.0F, -0.6981F)
      );
      body.addOrReplaceChild(
         "voxel_r93",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.1267F, -11.0114F, -2.0F, 5.0F, 11.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-17.6F, 4.0F, -1.0F, 0.0F, 0.0F, -0.9599F)
      );
      body.addOrReplaceChild(
         "voxel_r94",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.1267F, -11.0114F, 0.0F, 5.0F, 11.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-17.6F, 4.0F, 9.0F, 0.0F, 0.5672F, -0.9599F)
      );
      body.addOrReplaceChild(
         "voxel_r95",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0048F, -5.0256F, 0.0F, 5.0F, 11.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-13.0F, 6.0F, 9.0F, -0.121F, 0.6003F, -1.3465F)
      );
      body.addOrReplaceChild(
         "voxel_r96",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0048F, -5.0256F, -2.0F, 5.0F, 5.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-13.0F, 6.0F, -1.0F, 0.0F, 0.0F, -1.1345F)
      );
      body.addOrReplaceChild(
         "voxel_r97",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -10.0F, -2.0F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, -1.2217F)
      );
      body.addOrReplaceChild(
         "voxel_r98",
         CubeListBuilder.create()
            .texOffs(0, 27)
            .addBox(-4.843F, -15.9616F, -5.0076F, 5.0F, 16.0F, 5.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 25)
            .addBox(-4.843F, -15.9616F, -7.0076F, 5.0F, 16.0F, 7.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(22.2F, -65.7F, -3.0F, 0.0F, 0.0436F, -1.4835F)
      );
      body.addOrReplaceChild(
         "voxel_r99",
         CubeListBuilder.create()
            .texOffs(0, 33)
            .addBox(-4.9493F, 3.0516F, -10.0056F, 5.0F, 5.0F, 10.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 28)
            .addBox(-4.9493F, -6.9484F, -10.0056F, 5.0F, 10.0F, 10.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(14.2F, -66.2F, -10.0F, 0.0638F, 0.7395F, -1.389F)
      );
      body.addOrReplaceChild(
         "voxel_r100",
         CubeListBuilder.create()
            .texOffs(0, 28)
            .addBox(-5.0173F, -5.9616F, -2.0F, 5.0F, 6.0F, 14.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0173F, -15.9616F, -2.0F, 5.0F, 10.0F, 14.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(22.2F, -65.7F, -1.0F, 0.0F, -0.0873F, -1.4835F)
      );
      body.addOrReplaceChild(
         "voxel_r101",
         CubeListBuilder.create().texOffs(5, 29).addBox(-5.0662F, -8.5069F, 0.0666F, 5.0F, 12.0F, 7.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(28.2F, -57.2F, 19.4F, 0.0F, -0.7854F, -1.2217F)
      );
      body.addOrReplaceChild(
         "voxel_r102",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9354F, -12.0166F, 0.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(33.5F, -61.5F, 9.0F, 0.0F, -0.5236F, -1.2217F)
      );
      body.addOrReplaceChild(
         "voxel_r103",
         CubeListBuilder.create().texOffs(4, 28).addBox(-5.0664F, -12.256F, -7.9655F, 5.0F, 12.0F, 8.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(33.5F, -60.8F, -9.0F, 0.0F, 0.829F, -1.2217F)
      );
      body.addOrReplaceChild(
         "voxel_r104",
         CubeListBuilder.create().texOffs(6, 30).addBox(-4.9354F, -12.0166F, -6.0F, 5.0F, 12.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(33.5F, -61.5F, -3.0F, 0.0F, 0.1309F, -1.2217F)
      );
      body.addOrReplaceChild(
         "voxel_r105",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9354F, -12.0166F, -2.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(33.5F, -61.5F, -1.0F, 0.0F, 0.0F, -1.2217F)
      );
      body.addOrReplaceChild(
         "voxel_r106",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9747F, -5.7591F, 0.2646F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(33.5F, -53.0F, 18.9F, 0.0F, -1.0472F, -0.829F)
      );
      body.addOrReplaceChild(
         "voxel_r107",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9701F, -12.1919F, 0.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(42.5F, -53.3F, 9.0F, 0.0F, -0.5672F, -0.829F)
      );
      body.addOrReplaceChild(
         "voxel_r108",
         CubeListBuilder.create().texOffs(3, 27).addBox(-4.9701F, -12.1919F, -9.0F, 5.0F, 11.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(42.5F, -53.3F, -3.0F, 0.0F, 0.48F, -0.829F)
      );
      body.addOrReplaceChild(
         "voxel_r109",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9701F, -12.1919F, -2.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(42.5F, -53.3F, -1.0F, 0.0F, 0.0F, -0.829F)
      );
      body.addOrReplaceChild(
         "voxel_r110",
         CubeListBuilder.create().texOffs(0, 24).addBox(-5.1215F, -8.907F, -0.0031F, 5.0F, 8.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(39.2F, -42.5F, 18.6F, 0.0F, -0.9425F, -0.3927F)
      );
      body.addOrReplaceChild(
         "voxel_r111",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9945F, -11.8919F, 0.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(47.0F, -42.5F, 9.0F, 0.0F, -0.6545F, -0.3927F)
      );
      body.addOrReplaceChild(
         "voxel_r112",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9945F, -11.8919F, -12.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(47.0F, -42.5F, -3.0F, 0.0F, 0.7418F, -0.3927F)
      );
      body.addOrReplaceChild(
         "voxel_r113",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9945F, -11.8919F, -2.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(47.0F, -42.5F, -1.0F, 0.0F, 0.0F, -0.3927F)
      );
      body.addOrReplaceChild(
         "voxel_r114",
         CubeListBuilder.create()
            .texOffs(11, 33)
            .addBox(-4.9976F, -6.144F, 10.9692F, 5.0F, 12.0F, 3.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(3, 25)
            .addBox(-4.9976F, -6.144F, -0.0308F, 5.0F, 12.0F, 11.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(38.8F, -36.6F, 18.2F, -0.1669F, -0.9141F, 0.259F)
      );
      body.addOrReplaceChild(
         "voxel_r115",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.8143F, -11.8111F, 0.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(46.3F, -30.6F, 9.0F, 0.0F, -0.7156F, 0.0436F)
      );
      body.addOrReplaceChild(
         "voxel_r116",
         CubeListBuilder.create()
            .texOffs(6, 30)
            .addBox(-4.9944F, -12.0621F, -4.7139F, 5.0F, 12.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(38.2F, -42.7F, -12.6F, -0.4182F, 0.8223F, -0.4803F)
      );
      body.addOrReplaceChild(
         "voxel_r117",
         CubeListBuilder.create()
            .texOffs(4, 28)
            .addBox(-4.9136F, -11.0262F, -7.9757F, 5.0F, 12.0F, 8.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(39.1F, -31.7F, -12.6F, -0.1237F, 0.8935F, -0.0933F)
      );
      body.addOrReplaceChild(
         "voxel_r118",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.8143F, -11.8111F, -12.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(46.3F, -30.6F, -3.0F, 0.0F, 0.6545F, 0.0436F)
      );
      body.addOrReplaceChild(
         "voxel_r119",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.8143F, -11.8111F, -2.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(46.3F, -30.6F, -1.0F, 0.0F, 0.0F, 0.0436F)
      );
      body.addOrReplaceChild(
         "voxel_r120",
         CubeListBuilder.create()
            .texOffs(9, 33)
            .addBox(-5.0048F, -10.9853F, 10.3338F, 5.0F, 12.0F, 3.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(2, 26)
            .addBox(-5.0048F, -10.9853F, 0.3338F, 5.0F, 12.0F, 10.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(35.3F, -23.4F, 18.3F, 0.0F, -0.9599F, 0.4363F)
      );
      body.addOrReplaceChild(
         "voxel_r121",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9315F, -11.8936F, 0.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(41.3F, -19.6F, 9.0F, 0.0F, -0.6545F, 0.4363F)
      );
      body.addOrReplaceChild(
         "voxel_r122",
         CubeListBuilder.create().texOffs(4, 28).addBox(-5.0382F, -3.7048F, -8.0797F, 5.0F, 8.0F, 8.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(36.2F, -26.6F, -12.2F, 0.0F, 0.7854F, 0.4363F)
      );
      body.addOrReplaceChild(
         "voxel_r123",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9315F, -11.8936F, -12.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(41.3F, -19.6F, -3.0F, 0.0F, 0.6981F, 0.4363F)
      );
      body.addOrReplaceChild(
         "voxel_r124",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9315F, -11.8936F, -2.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(41.3F, -19.6F, -1.0F, 0.0F, 0.0F, 0.4363F)
      );
      body.addOrReplaceChild(
         "voxel_r125",
         CubeListBuilder.create()
            .texOffs(0, 32)
            .addBox(-4.9858F, -10.5294F, 0.0224F, 5.0F, 3.0F, 13.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 26)
            .addBox(-4.9858F, -7.5294F, 0.0224F, 5.0F, 9.0F, 13.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(29.6F, -13.7F, 18.2F, 0.0F, -0.9163F, 0.48F)
      );
      body.addOrReplaceChild(
         "voxel_r126",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9475F, -11.8355F, 0.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(35.8F, -9.0F, 9.0F, 0.0F, -0.6981F, 0.48F)
      );
      body.addOrReplaceChild(
         "voxel_r127",
         CubeListBuilder.create().texOffs(6, 30).addBox(-5.024F, -0.6994F, -5.9239F, 5.0F, 8.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(31.8F, -18.0F, -12.3F, 0.3557F, 0.9518F, 1.0008F)
      );
      body.addOrReplaceChild(
         "voxel_r128",
         CubeListBuilder.create()
            .texOffs(4, 27)
            .addBox(-4.9596F, -7.3178F, -12.0101F, 5.0F, 13.0F, 8.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(8, 31)
            .addBox(-4.9596F, -7.3178F, -4.0101F, 5.0F, 13.0F, 4.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(37.9F, -13.0F, -3.0F, 0.0758F, 0.7385F, 0.598F)
      );
      body.addOrReplaceChild(
         "voxel_r129",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9475F, -11.8355F, -2.0F, 5.0F, 12.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(35.8F, -9.0F, -1.0F, 0.0F, 0.0F, 0.48F)
      );
      body.addOrReplaceChild(
         "voxel_r130",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9475F, -8.3208F, 0.0F, 5.0F, 7.0F, 14.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(29.8F, -3.0F, 9.0F, 0.0F, -0.7592F, 0.7854F)
      );
      body.addOrReplaceChild(
         "voxel_r131",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9475F, -8.3208F, -12.0F, 5.0F, 8.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(29.8F, -3.0F, -3.0F, 0.0F, 0.9163F, 0.7854F)
      );
      body.addOrReplaceChild(
         "voxel_r132",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9475F, -8.3208F, -2.0F, 5.0F, 8.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(29.8F, -3.0F, -1.0F, 0.0F, 0.0F, 0.7854F)
      );
      body.addOrReplaceChild(
         "voxel_r133",
         CubeListBuilder.create()
            .texOffs(7, 29)
            .addBox(-5.0508F, -4.861F, 7.9321F, 5.0F, 14.0F, 4.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(4, 26)
            .addBox(-5.0508F, -4.861F, -0.0679F, 5.0F, 14.0F, 8.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(19.8F, -17.2F, 26.2F, 0.0F, -1.3788F, 1.2217F)
      );
      body.addOrReplaceChild(
         "voxel_r134",
         CubeListBuilder.create()
            .texOffs(8, 30)
            .addBox(-4.9931F, -8.1786F, 8.0598F, 5.0F, 14.0F, 4.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(4, 26)
            .addBox(-4.9931F, -8.1786F, 0.0598F, 5.0F, 14.0F, 8.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(19.8F, -7.5F, 18.2F, 0.0F, -0.8552F, 1.2217F)
      );
      body.addOrReplaceChild(
         "voxel_r135",
         CubeListBuilder.create()
            .texOffs(8, 30)
            .addBox(-4.9102F, -10.949F, 8.0F, 5.0F, 14.0F, 4.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(4, 26)
            .addBox(-4.9102F, -10.949F, 0.0F, 5.0F, 14.0F, 8.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(19.8F, 0.6F, 9.0F, 0.0F, -0.6981F, 1.2217F)
      );
      body.addOrReplaceChild(
         "voxel_r136",
         CubeListBuilder.create()
            .texOffs(3, 27)
            .addBox(-4.9102F, -10.949F, -21.0F, 5.0F, 11.0F, 9.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.9102F, -10.949F, -12.0F, 5.0F, 11.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(19.8F, 0.6F, -3.0F, 0.0F, 0.8727F, 1.2217F)
      );
      body.addOrReplaceChild(
         "voxel_r137",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9102F, -10.949F, -2.0F, 5.0F, 11.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(19.8F, 0.6F, -1.0F, 0.0F, 0.0F, 1.2217F)
      );
      body.addOrReplaceChild(
         "voxel_r138",
         CubeListBuilder.create()
            .texOffs(0, 27)
            .addBox(-4.9737F, -6.0163F, 0.0F, 5.0F, 8.0F, 13.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 30)
            .addBox(-4.9737F, -11.0163F, 0.0F, 5.0F, 5.0F, 13.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(10.5F, 6.6F, 9.0F, 0.0F, -0.672F, 1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r139",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9737F, -11.0163F, -2.0F, 5.0F, 11.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(10.5F, 6.6F, -1.0F, 0.0F, 0.0F, 1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r140",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -11.0F, -2.0F, 5.0F, 11.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, 1.1345F)
      );
      body.addOrReplaceChild(
         "voxel_r141",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0489F, -3.0731F, -2.0F, 5.0F, 3.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-10.7F, 8.0F, -1.0F, 0.0F, 0.0F, -0.8727F)
      );
      body.addOrReplaceChild(
         "voxel_r142",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 5.0F, 2.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 4.0F, -6.0F, 0.1309F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r143",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-2.4965F, -8.2953F, -1.0F, 5.0F, 8.0F, 5.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 1.2519F, -0.2368F, 0.8109F)
      );
      body.addOrReplaceChild(
         "voxel_r144",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.4965F, -4.2953F, -2.0F, 11.0F, 4.0F, 6.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -1.1345F)
      );
      body.addOrReplaceChild(
         "voxel_r145",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-4.4965F, -7.2953F, -2.0F, 7.0F, 8.0F, 6.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 0.0F, 0.0F, 0.6981F)
      );
      body.addOrReplaceChild(
         "voxel_r146",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-2.0341F, -3.1877F, -4.5034F, 5.0F, 7.0F, 13.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(0.5F, 0.0F, -3.0F, 1.9222F, -0.318F, 0.8876F)
      );
      body.addOrReplaceChild(
         "voxel_r147",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-3.4217F, -10.2414F, -9.7983F, 6.0F, 7.0F, 13.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.5F, -5.0F, -1.0F, 2.2689F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r148",
         CubeListBuilder.create().texOffs(0, 24).addBox(-3.4217F, -10.2414F, -5.7983F, 6.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.5F, -5.0F, -1.0F, 1.4399F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r149",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -8.2953F, -2.0F, 6.0F, 10.0F, 7.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 1.0036F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r150",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5035F, -7.2953F, -2.0F, 5.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -0.5672F)
      );
      body.addOrReplaceChild(
         "voxel_r151",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.4965F, -6.2953F, -2.0F, 5.0F, 7.0F, 9.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, -1.0943F, 0.0603F, 0.8873F)
      );
      body.addOrReplaceChild(
         "voxel_r152",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -6.2953F, -2.0F, 6.0F, 8.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, -1.0908F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r153",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -7.2953F, -2.0F, 6.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r154",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.5F, -8.8659F, -2.4041F, 3.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-2.5F, 6.5F, -3.0F, 0.3295F, 0.1172F, -0.3295F)
      );
      body.addOrReplaceChild(
         "voxel_r155",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -14.0F, -3.0F, 5.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 13.0F, 0.0F, 0.3491F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r156",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-2.5F, -2.0F, -2.0F, 5.0F, 2.0F, 5.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-2.5F, -21.0F, -2.0F, 5.0F, 19.0F, 5.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 15.0F, 0.0F, -0.2182F, 0.0F, 0.0F)
      );
      return body;
   }
}
