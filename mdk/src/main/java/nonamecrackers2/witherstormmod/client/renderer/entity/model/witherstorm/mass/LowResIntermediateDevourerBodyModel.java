package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class LowResIntermediateDevourerBodyModel {
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
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -0.2412F, -0.1716F, 9.0F, 5.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -40.75F, 8.5F, -1.8753F, -0.0226F, -0.0843F)
      );
      body.addOrReplaceChild(
         "voxel_r3",
         CubeListBuilder.create().texOffs(0, 24).addBox(-13.95F, 0.1602F, -0.0218F, 14.0F, 7.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.05F, -36.75F, 13.5F, -2.1992F, -0.3272F, -0.4164F)
      );
      body.addOrReplaceChild(
         "voxel_r4",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, 0.1602F, -0.0218F, 14.0F, 6.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -36.75F, 13.5F, -2.2689F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r5",
         CubeListBuilder.create().texOffs(0, 24).addBox(-16.05F, 2.026F, -0.1117F, 16.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-6.95F, -22.75F, 18.5F, -2.7597F, -0.4084F, -0.1582F)
      );
      body.addOrReplaceChild(
         "voxel_r6",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.05F, 0.026F, -0.1117F, 7.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(6.95F, -22.75F, 18.5F, -2.7812F, 0.2457F, 0.0914F)
      );
      body.addOrReplaceChild(
         "voxel_r7",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -1.0001F, -2.9307F, 14.0F, 15.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -22.75F, 15.5F, -2.7925F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r8",
         CubeListBuilder.create().texOffs(0, 24).addBox(-9.0228F, 6.0021F, 0.1476F, 9.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-13.0F, -7.75F, 14.5F, 2.9907F, -0.5187F, 0.0752F)
      );
      body.addOrReplaceChild(
         "voxel_r9",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.0F, -1.1284F, -0.9729F, 6.0F, 16.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.0F, -7.75F, 15.5F, 3.0024F, -0.346F, 0.0475F)
      );
      body.addOrReplaceChild(
         "voxel_r10",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.05F, -0.9589F, 0.0186F, 10.0F, 16.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(6.95F, -7.75F, 16.5F, 3.0083F, 0.1903F, -0.0254F)
      );
      body.addOrReplaceChild(
         "voxel_r11",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -0.8284F, -0.9729F, 14.0F, 16.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -7.75F, 15.5F, 3.0107F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r12",
         CubeListBuilder.create().texOffs(0, 24).addBox(-5.95F, 6.1739F, -1.5475F, 7.0F, 11.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.05F, 7.25F, 6.5F, 2.4957F, -0.5904F, 0.3972F)
      );
      body.addOrReplaceChild(
         "voxel_r13",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.05F, -6.7344F, 0.0065F, 9.0F, 8.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(7.05F, -5.75F, 15.8F, 2.388F, 0.4187F, -0.2598F)
      );
      body.addOrReplaceChild(
         "voxel_r14",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -15.9504F, 0.1127F, 14.0F, 16.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -6.75F, 16.5F, 2.618F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r15",
         CubeListBuilder.create().texOffs(0, 24).addBox(-9.75F, -9.9012F, -0.3477F, 14.0F, 10.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -37.25F, -15.75F, -1.1348F, 0.0167F, -0.0403F)
      );
      body.addOrReplaceChild(
         "voxel_r16",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.25F, -9.6708F, -0.479F, 11.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(3.75F, -27.25F, -19.75F, -0.4152F, -0.3215F, 0.1384F)
      );
      body.addOrReplaceChild(
         "voxel_r17",
         CubeListBuilder.create().texOffs(0, 24).addBox(-8.05F, -9.6708F, -0.479F, 8.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-9.95F, -27.25F, -19.75F, -0.4097F, 0.2815F, -0.1201F)
      );
      body.addOrReplaceChild(
         "voxel_r18",
         CubeListBuilder.create().texOffs(0, 24).addBox(-9.75F, -10.6708F, -0.479F, 14.0F, 10.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -27.25F, -19.75F, -0.3927F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r19",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-13.75F, -12.2293F, -4.0004F, 18.0F, 14.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -16.25F, -14.75F, 0.1309F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r20",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, -6.6204F, -0.1022F, 11.0F, 11.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(4.0F, 0.75F, -6.5F, 0.7365F, -0.0486F, -0.3414F)
      );
      body.addOrReplaceChild(
         "voxel_r21",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -0.4106F, -0.0912F, 7.0F, 8.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-11.25F, -4.0F, -10.5F, 0.8546F, 0.4894F, 0.4539F)
      );
      body.addOrReplaceChild(
         "voxel_r22",
         CubeListBuilder.create().texOffs(0, 24).addBox(-10.75F, -0.4106F, -0.0912F, 15.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -4.0F, -10.5F, 0.6981F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r23",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -12.4395F, -3.0793F, 15.0F, 12.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(4.0F, -15.5F, -16.5F, 0.0492F, -0.2846F, -0.0792F)
      );
      body.addOrReplaceChild(
         "voxel_r24",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.2193F, -14.3536F, -2.9688F, 15.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(4.0F, -4.5F, -7.5F, 0.6274F, -0.2168F, -0.1478F)
      );
      body.addOrReplaceChild(
         "voxel_r25",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-10.05F, -12.9753F, -3.0258F, 10.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-12.95F, -15.0F, -16.3F, 0.1122F, 0.492F, 0.1529F)
      );
      body.addOrReplaceChild(
         "voxel_r26",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-9.8676F, -12.6159F, -0.7064F, 10.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-12.95F, -5.0F, -10.3F, 0.7413F, 0.4652F, 0.3333F)
      );
      body.addOrReplaceChild(
         "voxel_r27",
         CubeListBuilder.create().texOffs(0, 24).addBox(-12.75F, -19.0F, 0.0F, 17.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, 1.0F, -8.0F, 0.6109F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r28",
         CubeListBuilder.create().texOffs(2, 27).addBox(-0.6441F, -9.8547F, -9.0F, 5.0F, 10.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-16.5F, -2.0F, -3.0F, 0.0F, -0.5672F, -0.5236F)
      );
      body.addOrReplaceChild(
         "voxel_r29",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.5533F, -13.7432F, -11.0F, 5.0F, 13.0F, 11.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(1.5F, -42.0F, -4.0F, 0.0F, -0.3927F, 1.8326F)
      );
      body.addOrReplaceChild(
         "voxel_r30",
         CubeListBuilder.create().texOffs(0, 26).addBox(-0.5533F, -13.7432F, 0.0F, 5.0F, 13.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(1.5F, -42.0F, 5.0F, 0.0F, 0.6109F, 1.8326F)
      );
      body.addOrReplaceChild(
         "voxel_r31",
         CubeListBuilder.create().texOffs(0, 26).addBox(-0.5533F, -13.7432F, -3.0F, 5.0F, 13.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(1.5F, -42.0F, -1.0F, 0.0F, 0.0F, 1.8326F)
      );
      body.addOrReplaceChild(
         "voxel_r32",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.5036F, -12.7847F, -6.0F, 5.0F, 3.0F, 12.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 25)
            .addBox(-0.5036F, -9.7847F, -6.0F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-10.5F, -41.0F, -1.0F, 0.0F, 0.0F, 1.4835F)
      );
      body.addOrReplaceChild(
         "voxel_r33",
         CubeListBuilder.create().texOffs(4, 29).addBox(-0.4222F, -9.2184F, -7.0F, 5.0F, 10.0F, 7.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-18.5F, -36.0F, -7.0F, -0.0275F, -0.3042F, 1.095F)
      );
      body.addOrReplaceChild(
         "voxel_r34",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.4222F, -9.2184F, -6.0F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-18.5F, -36.0F, -1.0F, 0.0F, 0.0F, 1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r35",
         CubeListBuilder.create().texOffs(3, 28).addBox(0.0713F, -10.5213F, -8.0F, 5.0F, 8.0F, 8.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-26.5F, -28.0F, -7.0F, 0.0F, -0.3927F, 0.7418F)
      );
      body.addOrReplaceChild(
         "voxel_r36",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0713F, -10.5213F, -6.0F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-26.5F, -28.0F, -1.0F, 0.0F, 0.0F, 0.7418F)
      );
      body.addOrReplaceChild(
         "voxel_r37",
         CubeListBuilder.create().texOffs(2, 27).addBox(-0.112F, -9.5748F, -9.0F, 5.0F, 10.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-27.5F, -19.0F, -7.0F, 0.0F, -0.5411F, 0.1309F)
      );
      body.addOrReplaceChild(
         "voxel_r38",
         CubeListBuilder.create().texOffs(3, 28).addBox(-0.112F, -9.5748F, 0.0F, 5.0F, 10.0F, 8.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-27.5F, -19.0F, 5.0F, 0.0F, 0.6981F, 0.1309F)
      );
      body.addOrReplaceChild(
         "voxel_r39",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.112F, -9.5748F, -6.0F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-27.5F, -19.0F, -1.0F, 0.0F, 0.0F, 0.1309F)
      );
      body.addOrReplaceChild(
         "voxel_r40",
         CubeListBuilder.create().texOffs(5, 30).addBox(-1.18F, -10.9265F, -6.0F, 5.0F, 10.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-20.5F, -10.0F, -7.0F, 0.0F, -0.6545F, -0.5672F)
      );
      body.addOrReplaceChild(
         "voxel_r41",
         CubeListBuilder.create().texOffs(3, 28).addBox(0.0851F, -7.1205F, 0.0F, 5.0F, 7.0F, 8.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-22.0F, -10.0F, 5.0F, 0.0F, 0.6545F, -0.5672F)
      );
      body.addOrReplaceChild(
         "voxel_r42",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.18F, -10.9265F, -6.0F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-20.5F, -10.0F, -1.0F, 0.0F, 0.0F, -0.5672F)
      );
      body.addOrReplaceChild(
         "voxel_r43",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.6441F, -10.8547F, 0.0F, 5.0F, 11.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-16.5F, -2.0F, 5.0F, 0.0F, 0.6981F, -0.5236F)
      );
      body.addOrReplaceChild(
         "voxel_r44",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.6441F, -9.8547F, -6.0F, 5.0F, 10.0F, 12.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-16.5F, -2.0F, -1.0F, 0.0F, 0.0F, -0.5236F)
      );
      body.addOrReplaceChild(
         "voxel_r45",
         CubeListBuilder.create().texOffs(1, 26).addBox(-0.0981F, -10.8397F, -3.0F, 5.0F, 10.0F, 10.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-10.5F, 6.0F, -1.0F, 0.2615F, -0.0149F, -0.6571F)
      );
      body.addOrReplaceChild(
         "voxel_r46",
         CubeListBuilder.create().texOffs(4, 29).addBox(-0.9641F, -9.3397F, 0.0F, 5.0F, 10.0F, 7.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-10.5F, 5.0F, 5.0F, 0.0F, 0.7418F, -0.672F)
      );
      body.addOrReplaceChild(
         "voxel_r47",
         CubeListBuilder.create().texOffs(2, 27).addBox(-0.9641F, -9.3397F, -3.0F, 5.0F, 10.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-10.5F, 5.0F, -1.0F, 0.0F, 0.0F, -0.672F)
      );
      body.addOrReplaceChild(
         "voxel_r48",
         CubeListBuilder.create().texOffs(2, 27).addBox(-0.9641F, 0.6603F, -3.0F, 5.0F, 10.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-10.5F, 5.0F, -1.0F, 0.0F, 0.0F, -1.0472F)
      );
      body.addOrReplaceChild(
         "voxel_r49",
         CubeListBuilder.create().texOffs(2, 27).addBox(-4.8774F, -3.5892F, -8.6519F, 5.0F, 8.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(24.3F, -13.3F, -4.0F, 0.0122F, 0.6108F, 0.414F)
      );
      body.addOrReplaceChild(
         "voxel_r50",
         CubeListBuilder.create().texOffs(0, 25).addBox(-5.0471F, -7.7588F, 0.0F, 5.0F, 8.0F, 11.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(23.1F, -9.3F, 5.0F, 0.0F, -0.5236F, 0.3927F)
      );
      body.addOrReplaceChild(
         "voxel_r51",
         CubeListBuilder.create().texOffs(2, 27).addBox(-5.2319F, -7.6823F, -3.0F, 5.0F, 8.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(23.3F, -9.3F, -1.0F, 0.0F, 0.0F, 0.3927F)
      );
      body.addOrReplaceChild(
         "voxel_r52",
         CubeListBuilder.create().texOffs(0, 24).addBox(-5.0053F, -16.0427F, -11.0F, 5.0F, 13.0F, 11.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(25.5F, -27.3F, -4.0F, 0.0F, 0.4363F, -0.7592F)
      );
      body.addOrReplaceChild(
         "voxel_r53",
         CubeListBuilder.create()
            .texOffs(8, 31)
            .addBox(-5.0053F, -16.0427F, 9.0F, 5.0F, 12.0F, 4.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(3, 26)
            .addBox(-5.0053F, -16.0427F, 0.0F, 5.0F, 12.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(25.5F, -27.3F, 5.0F, 0.0F, -0.6109F, -0.7592F)
      );
      body.addOrReplaceChild(
         "voxel_r54",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0053F, -4.0427F, -3.0F, 5.0F, 4.0F, 9.0F, CubeDeformation.NONE, texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0053F, -16.0427F, -3.0F, 5.0F, 12.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(25.5F, -27.3F, -1.0F, 0.0F, 0.0F, -0.7592F)
      );
      body.addOrReplaceChild(
         "voxel_r55",
         CubeListBuilder.create().texOffs(0, 24).addBox(-5.3248F, -11.0671F, -13.0F, 5.0F, 11.0F, 13.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(26.3F, -16.3F, -4.0F, 0.0F, 0.5672F, -0.0436F)
      );
      body.addOrReplaceChild(
         "voxel_r56",
         CubeListBuilder.create().texOffs(3, 28).addBox(-4.9725F, -7.9347F, 0.1591F, 5.0F, 9.0F, 8.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(23.2F, -19.3F, 11.3F, 0.0F, -0.9163F, -0.0436F)
      );
      body.addOrReplaceChild(
         "voxel_r57",
         CubeListBuilder.create().texOffs(4, 29).addBox(-5.0251F, -11.054F, 0.0F, 5.0F, 11.0F, 7.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(26.0F, -16.3F, 5.0F, 0.0F, -0.3927F, -0.0436F)
      );
      body.addOrReplaceChild(
         "voxel_r58",
         CubeListBuilder.create().texOffs(2, 27).addBox(-5.3248F, -11.0671F, -3.0F, 5.0F, 11.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(26.3F, -16.3F, -1.0F, 0.0F, 0.0F, -0.0436F)
      );
      body.addOrReplaceChild(
         "voxel_r59",
         CubeListBuilder.create().texOffs(2, 27).addBox(-0.1165F, -8.1621F, -4.2182F, 5.0F, 12.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(12.5F, -9.0F, -4.0F, 0.2761F, 0.7016F, 1.1165F)
      );
      body.addOrReplaceChild(
         "voxel_r60",
         CubeListBuilder.create().texOffs(2, 27).addBox(-4.4657F, -8.2112F, 0.0F, 5.0F, 8.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(17.5F, -3.0F, 5.0F, 0.0F, -0.48F, 0.6545F)
      );
      body.addOrReplaceChild(
         "voxel_r61",
         CubeListBuilder.create().texOffs(2, 27).addBox(-4.4657F, -8.2112F, -3.0F, 5.0F, 8.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(17.5F, -3.0F, -1.0F, 0.0F, 0.0F, 0.6545F)
      );
      body.addOrReplaceChild(
         "voxel_r62",
         CubeListBuilder.create().texOffs(3, 28).addBox(-4.9592F, -11.5142F, 0.0F, 5.0F, 12.0F, 8.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(9.5F, 5.0F, 5.0F, 0.0F, -0.6545F, 0.829F)
      );
      body.addOrReplaceChild(
         "voxel_r63",
         CubeListBuilder.create().texOffs(2, 27).addBox(-4.9592F, -11.5142F, -3.0F, 5.0F, 11.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(9.5F, 5.0F, -1.0F, 0.0F, 0.0F, 0.829F)
      );
      body.addOrReplaceChild(
         "voxel_r64",
         CubeListBuilder.create().texOffs(2, 27).addBox(-2.5F, -11.0F, -3.0F, 5.0F, 11.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, 0.9599F)
      );
      body.addOrReplaceChild(
         "voxel_r65",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 5.0F, 2.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 4.0F, -6.0F, 0.1309F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r66",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-2.4965F, -8.2953F, -1.0F, 5.0F, 8.0F, 5.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 1.2519F, -0.2368F, 0.8109F)
      );
      body.addOrReplaceChild(
         "voxel_r67",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.4965F, -4.2953F, -2.0F, 11.0F, 4.0F, 6.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -1.1345F)
      );
      body.addOrReplaceChild(
         "voxel_r68",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-4.4965F, -7.2953F, -2.0F, 7.0F, 8.0F, 6.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 0.0F, 0.0F, 0.6981F)
      );
      body.addOrReplaceChild(
         "voxel_r69",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-2.0341F, -3.1877F, -4.5034F, 5.0F, 7.0F, 13.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(0.5F, 0.0F, -3.0F, 1.9222F, -0.318F, 0.8876F)
      );
      body.addOrReplaceChild(
         "voxel_r70",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-3.4217F, -10.2414F, -9.7983F, 6.0F, 7.0F, 13.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.5F, -5.0F, -1.0F, 2.2689F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r71",
         CubeListBuilder.create().texOffs(0, 24).addBox(-3.4217F, -10.2414F, -5.7983F, 6.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-7.5F, -5.0F, -1.0F, 1.4399F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r72",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5035F, -7.2953F, -2.0F, 5.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -0.5672F)
      );
      body.addOrReplaceChild(
         "voxel_r73",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.4965F, -6.2953F, -2.0F, 5.0F, 7.0F, 9.0F, CubeDeformation.NONE, texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, -1.0943F, 0.0603F, 0.8873F)
      );
      body.addOrReplaceChild(
         "voxel_r74",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -6.2953F, -2.0F, 6.0F, 8.0F, 9.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, -1.0908F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r75",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -7.2953F, -2.0F, 6.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "voxel_r76",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.5F, -8.8659F, -2.4041F, 3.0F, 9.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-2.5F, 6.5F, -3.0F, 0.3295F, 0.1172F, -0.3295F)
      );
      body.addOrReplaceChild(
         "voxel_r77",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -14.0F, -3.0F, 5.0F, 13.0F, 6.0F, CubeDeformation.NONE, texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 13.0F, 0.0F, 0.3491F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "voxel_r78",
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
