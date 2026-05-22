package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class LowResTornEvolvedDevourerBodyModel {
   public static PartDefinition createBodyModel(PartDefinition root, float texScale) {
      PartDefinition body = root.addOrReplaceChild(
         "lowResMass",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-3.0F, -6.0F, -3.0F, 5.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-3.0F, 12.0F, -3.0F, 5.0F, 3.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-2.0F, 16.0F, 0.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.0F, 15.0F, 0.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-1.0F, 16.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-1.0F, 16.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-1.0F, 15.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-2.0F, 15.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-3.0F, 15.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-2.0F, 15.0F, 0.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.0F, 15.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.0F, 15.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.0F, 16.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-1.0F, 15.0F, 2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.0F, 15.0F, 1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offset(0.0F, -15.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part1",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -14.0F, -2.0F, 5.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, 0.3927F)
      );
      body.addOrReplaceChild(
         "part2",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-12.9838F, -13.0207F, 0.0017F, 13.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(-18.75F, -1.85F, 21.7F, 1.8162F, -0.1854F, 0.5843F)
      );
      body.addOrReplaceChild(
         "part3",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-19.25F, -15.214F, -3.1328F, 13.0F, 15.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(0.25F, 2.25F, 23.5F, 1.8497F, -0.1286F, 0.371F)
      );
      body.addOrReplaceChild(
         "part4",
         CubeListBuilder.create().texOffs(0, 24).addBox(6.25F, -15.214F, -3.1328F, 13.0F, 15.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, 2.25F, 23.5F, 1.8497F, 0.1286F, -0.371F)
      );
      body.addOrReplaceChild(
         "part5",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -15.214F, -0.1328F, 14.0F, 15.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, 2.25F, 23.5F, 2.0508F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part6",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-13.9513F, -15.9413F, 0.013F, 14.0F, 15.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(-19.15F, -19.05F, 30.65F, 2.5743F, -0.5406F, 0.2977F)
      );
      body.addOrReplaceChild(
         "part7",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-19.25F, -2.0646F, -2.0619F, 12.0F, 2.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false)
            .texOffs(0, 24)
            .mirror()
            .addBox(-19.25F, -20.0646F, -2.0619F, 12.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(0.25F, -19.25F, 31.25F, 2.6589F, -0.123F, 0.045F)
      );
      body.addOrReplaceChild(
         "part8",
         CubeListBuilder.create().texOffs(0, 24).addBox(12.25F, -24.0646F, -3.5619F, 14.0F, 15.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -19.25F, 31.25F, 2.6142F, 0.0112F, -0.4007F)
      );
      body.addOrReplaceChild(
         "part9",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(7.25F, -2.0646F, -2.0619F, 12.0F, 2.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(7.25F, -20.0646F, -2.0619F, 12.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -19.25F, 31.25F, 2.6589F, 0.123F, -0.045F)
      );
      body.addOrReplaceChild(
         "part10",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-6.75F, -5.0646F, -0.0619F, 14.0F, 5.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-6.75F, -23.0646F, -0.0619F, 14.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -19.25F, 31.25F, 2.7925F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part11",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.05F, -5.8678F, -0.0087F, 11.0F, 11.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.05F, -25.05F, 31.05F, -3.0955F, 0.2181F, 0.0155F)
      );
      body.addOrReplaceChild(
         "part12",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-16.9283F, 0.0265F, -0.2188F, 17.0F, 11.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-18.95F, -19.15F, 31.05F, -3.1327F, -0.4793F, -0.0291F)
      );
      body.addOrReplaceChild(
         "part13",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.75F, -11.076F, -0.0816F, 2.0F, 11.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, -11.076F, -0.0816F, 18.0F, 11.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -11.076F, -0.0816F, 18.0F, 11.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -30.25F, 30.75F, -3.098F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part14",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.75F, 18.0412F, -0.0476F, 2.0F, 4.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.75F, 0.0412F, -0.0476F, 2.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, 18.0412F, -0.0476F, 18.0F, 4.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, 0.0412F, -0.0476F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, 18.0412F, -0.0476F, 18.0F, 4.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, 0.0412F, -0.0476F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -57.05F, 33.25F, -2.4958F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part15",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-0.05F, 18.0412F, -0.0476F, 18.0F, 2.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false)
            .texOffs(0, 24)
            .mirror()
            .addBox(-0.05F, 0.0412F, -0.0476F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(19.05F, -57.05F, 33.25F, -2.5705F, 0.2768F, 0.2154F)
      );
      body.addOrReplaceChild(
         "part16",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-17.95F, 18.0412F, -0.0476F, 18.0F, 4.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-17.95F, 0.0412F, -0.0476F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-19.05F, -57.05F, 33.25F, -2.4657F, -0.2767F, -0.2156F)
      );
      body.addOrReplaceChild(
         "part17",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(1.25F, 18.1476F, -0.0853F, 18.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.75F, 18.1476F, -0.0853F, 2.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, 18.1476F, -0.0853F, 18.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.75F, 0.1476F, -0.0853F, 2.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, 0.1476F, -0.0853F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, 0.1476F, -0.0853F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -30.05F, 30.85F, 3.0543F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part18",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(0.05F, 16.1476F, -0.0853F, 18.0F, 1.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false)
            .texOffs(0, 24)
            .mirror()
            .addBox(0.05F, -1.8524F, -0.0853F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false)
            .texOffs(0, 24)
            .mirror()
            .addBox(18.05F, 17.1476F, -0.0853F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false)
            .texOffs(0, 24)
            .mirror()
            .addBox(0.05F, 17.1476F, -0.0853F, 18.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(18.95F, -30.05F, 30.85F, 3.0513F, 0.2608F, -0.0233F)
      );
      body.addOrReplaceChild(
         "part19",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-18.05F, 18.1476F, -0.0853F, 18.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.05F, 16.1476F, -0.0853F, 18.0F, 2.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.05F, -1.8524F, -0.0853F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-18.95F, -30.05F, 30.85F, 3.0513F, -0.2608F, 0.0233F)
      );
      body.addOrReplaceChild(
         "part20",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.75F, -18.1384F, -0.1095F, 2.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, -18.1384F, -0.1095F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -18.1384F, -0.1095F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -80.0F, 3.0F, -1.8762F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part21",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.75F, -13.2882F, -0.3956F, 2.0F, 17.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -13.2882F, -0.3956F, 18.0F, 17.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, -13.2882F, -0.3956F, 18.0F, 17.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -78.0F, -10.0F, -1.4399F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part22",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.05F, -11.2626F, -0.004F, 18.0F, 12.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.05F, -78.0F, -13.0F, -1.3657F, -0.0711F, 0.4386F)
      );
      body.addOrReplaceChild(
         "part23",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.05F, -12.8482F, -0.0306F, 18.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.05F, -65.8F, -23.3F, -0.7429F, -0.402F, 0.345F)
      );
      body.addOrReplaceChild(
         "part24",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(1.25F, -15.9264F, -0.0929F, 18.0F, 16.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.75F, -15.9264F, -0.0929F, 2.0F, 16.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -15.9264F, -0.0929F, 18.0F, 16.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -65.7F, -23.3F, -0.672F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part25",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-23.05F, -10.9264F, -0.0929F, 5.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.05F, -10.9264F, -0.0929F, 18.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-18.95F, -65.7F, -23.3F, -0.6952F, 0.2376F, -0.1938F)
      );
      body.addOrReplaceChild(
         "part26",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.192F, -10.0817F, -0.0438F, 13.0F, 11.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(33.85F, -52.6F, -17.0F, -0.3518F, -0.123F, 0.045F)
      );
      body.addOrReplaceChild(
         "part27",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.05F, -13.9123F, 0.0311F, 18.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(18.95F, -52.6F, -28.2F, -0.4181F, -0.5692F, 0.2351F)
      );
      body.addOrReplaceChild(
         "part28",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(1.25F, -14.1003F, -0.0373F, 18.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.75F, -14.1003F, -0.0373F, 2.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -14.1003F, -0.0373F, 18.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -52.4F, -28.2F, -0.3491F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part29",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-20.95F, -14.1003F, -0.0373F, 3.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-17.95F, -14.1003F, -0.0373F, 18.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-19.05F, -52.4F, -28.2F, -0.3729F, 0.3516F, -0.1339F)
      );
      body.addOrReplaceChild(
         "part30",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.75F, -28.5003F, -7.2373F, 2.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -28.5003F, -7.2373F, 18.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, -28.5003F, -7.2373F, 18.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offset(-0.25F, -24.0F, -21.0F)
      );
      body.addOrReplaceChild(
         "part31",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-14.0084F, -11.5003F, -0.0114F, 14.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-35.85F, -41.0F, -22.1F, 0.1309F, 0.6545F, 0.0F)
      );
      body.addOrReplaceChild(
         "part32",
         CubeListBuilder.create().texOffs(0, 24).addBox(-17.95F, -11.5003F, 0.1627F, 18.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-19.05F, -41.0F, -28.4F, 0.0F, 0.3491F, 0.0F)
      );
      body.addOrReplaceChild(
         "part33",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0567F, -6.9003F, -0.0206F, 11.0F, 11.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(34.15F, -45.6F, -18.6F, 0.2752F, -0.4317F, -0.0372F)
      );
      body.addOrReplaceChild(
         "part34",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.05F, -13.5003F, -0.0373F, 18.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.05F, -39.0F, -28.2F, 0.0F, -0.5672F, 0.0F)
      );
      body.addOrReplaceChild(
         "part35",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0011F, -5.4718F, 0.0234F, 11.0F, 8.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(32.15F, -27.8F, -18.6F, 0.7805F, -0.6178F, -0.5208F)
      );
      body.addOrReplaceChild(
         "part36",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-12.0317F, -5.1888F, 0.0189F, 12.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(-34.65F, -35.5F, -20.5F, 0.5506F, 0.7081F, 0.2457F)
      );
      body.addOrReplaceChild(
         "part37",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-16.95F, -8.9854F, 0.0985F, 17.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(-19.05F, -30.0F, -25.7F, 0.3103F, 0.3757F, 0.1171F)
      );
      body.addOrReplaceChild(
         "part38",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-11.9612F, -3.5467F, -0.0198F, 12.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(-34.05F, -29.8F, -19.1F, 0.7805F, 0.6178F, 0.5208F)
      );
      body.addOrReplaceChild(
         "part39",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-16.15F, -6.9703F, 0.0167F, 16.0F, 8.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(-18.85F, -23.8F, -21.7F, 0.6404F, 0.284F, 0.2058F)
      );
      body.addOrReplaceChild(
         "part40",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.15F, -6.9703F, 0.0167F, 14.0F, 8.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(18.85F, -23.8F, -21.7F, 0.6404F, -0.284F, -0.2058F)
      );
      PartDefinition part41 = body.addOrReplaceChild(
         "part41",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(6.25F, -7.208F, -0.6714F, 13.0F, 7.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.75F, -7.208F, -0.6714F, 7.0F, 7.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -7.208F, -0.6714F, 10.0F, 7.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-8.75F, -7.208F, -0.6714F, 8.0F, 7.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -24.0F, -21.0F, 0.6109F, 0.0F, 0.0F)
      );
      part41.addOrReplaceChild(
         "part44_r1",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-7.7326F, -0.9323F, -17.1831F, 10.0F, 2.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.75F, -2.208F, -0.6714F, 2.7094F, 0.0411F, -0.5029F)
      );
      part41.addOrReplaceChild(
         "part43_r1",
         CubeListBuilder.create().texOffs(0, 24).addBox(-7.7326F, -2.9017F, 1.5262F, 10.0F, 2.0F, 4.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.75F, -2.208F, -0.6714F, -2.2648F, 0.0411F, -0.5029F)
      );
      part41.addOrReplaceChild(
         "part42_r1",
         CubeListBuilder.create().texOffs(0, 24).addBox(-3.0366F, -3.5339F, 2.1066F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(2.75F, -2.208F, -0.6714F, -2.4164F, 0.1274F, 0.4185F)
      );
      part41.addOrReplaceChild(
         "part42_r2",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.0395F, 1.8056F, 0.7308F, 8.0F, 2.0F, 9.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(2.75F, -3.208F, 5.3286F, -0.4733F, 0.233F, 0.4235F)
      );
      body.addOrReplaceChild(
         "part42",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.75F, -16.0732F, -2.7038F, 2.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -16.0732F, -2.7038F, 18.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, -16.0732F, -2.7038F, 18.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -24.0F, -21.0F, 0.288F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part43",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0737F, -7.4439F, 0.0463F, 14.0F, 8.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(31.85F, -33.0F, -21.0F, 0.369F, -0.6641F, -0.234F)
      );
      body.addOrReplaceChild(
         "part44",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.05F, -8.9854F, 0.0985F, 14.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.05F, -30.0F, -25.7F, 0.3103F, -0.3757F, -0.1171F)
      );
      body.addOrReplaceChild(
         "part45",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.05F, -6.7472F, 0.0307F, 4.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(3.95F, -6.7472F, 0.0307F, 16.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.05F, -18.25F, -18.25F, 0.577F, -0.4114F, -0.2546F)
      );
      body.addOrReplaceChild(
         "part46",
         CubeListBuilder.create().texOffs(0, 24).addBox(-15.95F, 0.1489F, -0.0248F, 16.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-19.05F, -24.25F, -21.65F, 0.6206F, 0.4114F, 0.2546F)
      );
      PartDefinition part47 = body.addOrReplaceChild(
         "part47",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(5.25F, 0.1989F, 0.0618F, 14.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, 0.1989F, 0.0618F, 10.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -24.25F, -21.75F, 0.5236F, 0.0F, 0.0F)
      );
      part47.addOrReplaceChild(
         "part50_r1",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.2499F, -1.504F, -9.4638F, 2.0F, 10.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-9.0083F, 5.6428F, 8.1579F, -2.8937F, -0.7736F, 2.9112F)
      );
      part47.addOrReplaceChild(
         "part49_r1",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.3265F, 0.496F, -1.8192F, 2.0F, 8.0F, 4.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-10.0988F, 5.1989F, -0.3635F, -2.9278F, 0.5964F, -3.0757F)
      );
      part47.addOrReplaceChild(
         "part50_r2",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.5F, -2.5F, -3.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(3.75F, 1.6989F, 3.0618F, 0.0F, 0.0F, 0.5236F)
      );
      part47.addOrReplaceChild(
         "part49_r2",
         CubeListBuilder.create().texOffs(0, 24).addBox(-3.5F, -2.5F, -3.0F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-6.25F, 1.6989F, 3.0618F, 0.0F, 0.0F, -0.5236F)
      );
      part47.addOrReplaceChild(
         "part49_r3",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.1326F, 0.1209F, -0.4472F, 2.0F, 6.0F, 4.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(6.4372F, 8.1989F, 0.7052F, -3.0806F, -0.7486F, -2.8368F)
      );
      part47.addOrReplaceChild(
         "part48_r1",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.4166F, -2.4513F, 0.258F, 2.0F, 6.0F, 4.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(6.25F, 5.1989F, 0.0618F, 3.113F, -0.6563F, 2.986F)
      );
      part47.addOrReplaceChild(
         "part48_r2",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.8289F, -2.4019F, 0.1568F, 2.0F, 11.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(7.25F, 5.1989F, 6.0618F, 0.0F, -0.829F, 0.0F)
      );
      body.addOrReplaceChild(
         "part48",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(10.0F, -6.6204F, -0.1022F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.0F, -6.6204F, -0.1022F, 10.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(4.0F, 0.75F, -6.5F, 0.7153F, -0.1996F, -0.1706F)
      );
      body.addOrReplaceChild(
         "part49",
         CubeListBuilder.create().texOffs(0, 24).addBox(-14.95F, -0.3463F, -0.0146F, 15.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-13.05F, -4.0F, -10.6F, 0.8898F, 0.1996F, 0.1706F)
      );
      body.addOrReplaceChild(
         "part50",
         CubeListBuilder.create().texOffs(0, 24).addBox(-12.75F, -0.4106F, -0.0912F, 17.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -4.0F, -10.5F, 0.6981F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part51",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0384F, -7.2163F, 0.0219F, 15.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.0F, -10.5F, -11.8F, 0.49F, -0.1874F, -0.1119F)
      );
      PartDefinition part52 = body.addOrReplaceChild(
         "part52",
         CubeListBuilder.create().texOffs(0, 24).addBox(4.0F, -0.4395F, -0.0793F, 11.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(4.0F, -15.5F, -16.5F, 0.4839F, -0.1103F, -0.0706F)
      );
      part52.addOrReplaceChild(
         "part54_r1",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-1.4074F, 3.8724F, -0.6706F, 2.2533F, 0.3172F, -0.6719F)
      );
      part52.addOrReplaceChild(
         "part54_r2",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.5685F, 2.1572F, -1.3748F, 7.0F, 2.0F, 10.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(0.1931F, 2.0695F, 5.9207F, 0.5236F, 0.0F, -0.3054F)
      );
      part52.addOrReplaceChild(
         "part53_r1",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.5F, -5.5F, -3.0F, 9.0F, 12.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(2.5F, 6.0605F, 2.9207F, 0.0F, 0.0F, -0.6109F)
      );
      body.addOrReplaceChild(
         "part53",
         CubeListBuilder.create().texOffs(0, 24).addBox(-12.05F, -0.9753F, -0.0258F, 12.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-12.95F, -15.0F, -16.3F, 0.4943F, 0.2316F, 0.1231F)
      );
      PartDefinition part54 = body.addOrReplaceChild(
         "part54",
         CubeListBuilder.create().texOffs(0, 24).addBox(-5.75F, -13.0F, 0.0F, 10.0F, 7.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, 1.0F, -8.0F, 0.48F, 0.0F, 0.0F)
      );
      part54.addOrReplaceChild(
         "part56_r1",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.1934F, -3.9686F, -3.4346F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-7.2869F, -18.1526F, 0.0F, 2.3998F, 0.0F, 0.48F)
      );
      part54.addOrReplaceChild(
         "part56_r2",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.3448F, 1.6068F, -2.8615F, 8.0F, 2.0F, 9.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-8.2104F, -17.3786F, 7.0F, 0.7418F, 0.0F, 0.48F)
      );
      part54.addOrReplaceChild(
         "part55_r1",
         CubeListBuilder.create().texOffs(0, 24).addBox(-5.0F, -4.5F, -3.0F, 11.0F, 8.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-10.75F, -12.5F, 3.0F, 0.0F, 0.0F, 0.8727F)
      );
      part54.addOrReplaceChild(
         "part55_r2",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.0F, -2.7376F, -4.1592F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-1.75F, -16.0F, 0.0F, 2.1817F, 0.0F, 0.0F)
      );
      part54.addOrReplaceChild(
         "part55_r3",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.5F, 1.9054F, -3.1351F, 13.0F, 2.0F, 11.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-2.25F, -15.0F, 7.0F, 0.7418F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part55",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.1099F, -2.0994F, -2.0F, 5.0F, 2.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.1099F, -11.0994F, -2.0F, 5.0F, 9.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.1099F, -23.0994F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-43.4F, -27.25F, -1.0F, 0.0F, 0.0F, -0.48F)
      );
      body.addOrReplaceChild(
         "part56",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(0.0232F, -4.9735F, -12.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.0232F, -16.9735F, -12.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-34.8F, -75.85F, -3.0F, 0.0F, -0.3054F, 1.3526F)
      );
      body.addOrReplaceChild(
         "part57",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(0.0232F, -2.9735F, 0.0F, 5.0F, 3.0F, 10.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.0232F, -16.9735F, 0.0F, 5.0F, 14.0F, 10.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-34.8F, -75.85F, 9.0F, 0.0F, 0.4363F, 1.3526F)
      );
      body.addOrReplaceChild(
         "part58",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(0.0232F, -4.9735F, -2.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.0232F, -16.9735F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-34.8F, -75.85F, -1.0F, 0.0F, 0.0F, 1.3526F)
      );
      body.addOrReplaceChild(
         "part59",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.004F, -0.9735F, -12.0F, 5.0F, 1.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.004F, -12.9735F, -12.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-46.8F, -70.85F, -3.0F, 0.0F, -0.3491F, 1.1781F)
      );
      body.addOrReplaceChild(
         "part60",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.004F, -3.9735F, 0.0F, 5.0F, 4.0F, 15.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.004F, -12.9735F, 0.0F, 5.0F, 9.0F, 15.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-46.8F, -70.85F, 9.0F, 0.0F, 0.6545F, 1.1781F)
      );
      body.addOrReplaceChild(
         "part61",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.004F, -0.9735F, -2.0F, 5.0F, 1.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.004F, -12.9735F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-46.8F, -70.85F, -1.0F, 0.0F, 0.0F, 1.1781F)
      );
      body.addOrReplaceChild(
         "part62",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0425F, -8.9606F, -12.0F, 5.0F, 9.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-53.4F, -64.75F, -3.0F, 0.0F, -0.3054F, 0.829F)
      );
      body.addOrReplaceChild(
         "part63",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0425F, -8.9606F, 0.0F, 5.0F, 9.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-53.4F, -64.75F, 9.0F, 0.0F, 0.6981F, 0.829F)
      );
      body.addOrReplaceChild(
         "part64",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0425F, -8.9606F, -2.0F, 5.0F, 9.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-53.4F, -64.75F, -1.0F, 0.0F, 0.0F, 0.829F)
      );
      body.addOrReplaceChild(
         "part65",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(0.07F, 5.2511F, -11.935F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.07F, -6.7489F, -11.935F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-48.7F, -57.75F, -14.0F, 0.0F, -1.1345F, 0.0436F)
      );
      body.addOrReplaceChild(
         "part66",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0847F, -4.9749F, -12.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.0847F, -16.9749F, -12.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-54.1F, -47.75F, -3.0F, 0.0F, -0.4363F, 0.0436F)
      );
      body.addOrReplaceChild(
         "part67",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0718F, -3.3112F, -0.0014F, 5.0F, 4.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.0718F, -14.3112F, -0.0014F, 5.0F, 11.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-48.0F, -50.15F, 19.4F, 0.3253F, 0.8731F, 0.4583F)
      );
      body.addOrReplaceChild(
         "part68",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0847F, -4.9749F, 0.0F, 5.0F, 3.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.0847F, -16.9749F, 0.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-54.1F, -47.75F, 9.0F, 0.0F, 0.5236F, 0.0436F)
      );
      body.addOrReplaceChild(
         "part69",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0847F, -16.9749F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.0847F, -4.9749F, -2.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-54.1F, -47.75F, -1.0F, 0.0F, 0.0F, 0.0436F)
      );
      body.addOrReplaceChild(
         "part70",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0346F, -4.7469F, -12.0F, 5.0F, 9.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.0346F, -16.7469F, -12.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-46.4F, -32.85F, -3.0F, 0.0F, -0.5236F, -0.48F)
      );
      body.addOrReplaceChild(
         "part71",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0294F, -2.1577F, 0.0376F, 5.0F, 6.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.0294F, -14.1577F, 0.0376F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-40.9F, -35.25F, 19.4F, 0.1681F, 0.8068F, -0.2521F)
      );
      body.addOrReplaceChild(
         "part72",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.1099F, -11.0994F, 0.0F, 5.0F, 9.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.1099F, -23.0994F, 0.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-43.4F, -27.25F, 9.0F, 0.0F, 0.5236F, -0.48F)
      );
      body.addOrReplaceChild(
         "part73",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.1099F, -3.2415F, -12.0F, 5.0F, 3.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.1099F, -15.2415F, -12.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-33.4F, -17.25F, -3.0F, 0.0F, -0.5236F, -0.7854F)
      );
      body.addOrReplaceChild(
         "part74",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.1099F, 0.7585F, 0.0F, 5.0F, 2.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.1099F, -3.2415F, 0.0F, 5.0F, 4.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.1099F, -15.2415F, 0.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-33.4F, -17.25F, 9.0F, 0.0F, 0.48F, -0.7854F)
      );
      body.addOrReplaceChild(
         "part75",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0013F, -9.3725F, -0.0051F, 5.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-33.7F, -25.25F, 19.7F, -0.0668F, 0.6516F, -0.8952F)
      );
      body.addOrReplaceChild(
         "part76",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.1099F, -2.2415F, -2.0F, 5.0F, 2.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.1099F, -14.2415F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-33.4F, -17.25F, -1.0F, 0.0F, 0.0F, -0.7854F)
      );
      body.addOrReplaceChild(
         "part77",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0672F, -9.0045F, -12.0F, 5.0F, 4.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.0672F, -21.0045F, -12.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-28.4F, -1.25F, -3.0F, 0.0F, -0.6981F, -0.3054F)
      );
      body.addOrReplaceChild(
         "part78",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0147F, -2.2993F, 0.0F, 5.0F, 10.0F, 11.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-32.9F, -15.25F, 9.0F, 0.0F, 0.3665F, -0.3054F)
      );
      body.addOrReplaceChild(
         "part79",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0672F, -5.0045F, -2.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.0672F, -17.0045F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-28.4F, -1.25F, -1.0F, 0.0F, 0.0F, -0.3054F)
      );
      body.addOrReplaceChild(
         "part80",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0048F, -5.0256F, -2.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.0048F, -17.0256F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-13.0F, 6.0F, -1.0F, 0.0F, 0.0F, -1.1345F)
      );
      body.addOrReplaceChild(
         "part81",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -10.0F, -2.0F, 5.0F, 10.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, -1.2217F)
      );
      body.addOrReplaceChild(
         "part82",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9699F, 0.0973F, 0.1F, 5.0F, 6.0F, 14.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(43.0F, -34.1F, 8.9F, 0.0F, -0.3054F, -0.1745F)
      );
      body.addOrReplaceChild(
         "part83",
         CubeListBuilder.create().texOffs(0, 24).addBox(-5.0218F, 5.119F, -12.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(42.7F, -36.1F, -3.0F, 0.0F, 0.7418F, -0.1745F)
      );
      body.addOrReplaceChild(
         "part84",
         CubeListBuilder.create().texOffs(0, 24).addBox(-5.0579F, -5.0416F, -2.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(44.5F, -26.1F, -1.0F, 0.0F, 0.0F, -0.1745F)
      );
      body.addOrReplaceChild(
         "part85",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0251F, -4.0204F, 0.0F, 5.0F, 4.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0251F, -16.0204F, 0.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(34.9F, -74.5F, 9.0F, 0.0F, -0.4363F, -1.2217F)
      );
      body.addOrReplaceChild(
         "part86",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0251F, -4.0204F, -2.0F, 5.0F, 4.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0251F, -16.0204F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(34.9F, -74.5F, -1.0F, 0.0F, 0.0F, -1.2217F)
      );
      body.addOrReplaceChild(
         "part87",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.9718F, -6.9442F, -15.0F, 5.0F, 7.0F, 15.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.9718F, -15.9442F, -15.0F, 5.0F, 9.0F, 15.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(49.0F, -67.1F, -3.0F, 0.0F, 0.6109F, -1.0908F)
      );
      body.addOrReplaceChild(
         "part88",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9709F, 0.0095F, -3.3729F, 5.0F, 6.0F, 8.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(45.2F, -59.7F, 22.9F, -0.6819F, -0.9338F, -0.3005F)
      );
      body.addOrReplaceChild(
         "part89",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0437F, -0.1556F, -0.0354F, 5.0F, 4.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0437F, -12.1556F, -0.0354F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(44.0F, -65.7F, 20.5F, 0.0F, -1.0908F, -1.0908F)
      );
      body.addOrReplaceChild(
         "part90",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.9718F, -3.9442F, 0.0F, 5.0F, 4.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.9718F, -15.9442F, 0.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(49.0F, -67.1F, 9.0F, 0.0F, -0.3054F, -1.0908F)
      );
      body.addOrReplaceChild(
         "part91",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.9718F, -3.9442F, -2.0F, 5.0F, 4.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.9718F, -15.9442F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(49.0F, -67.1F, -1.0F, 0.0F, 0.0F, -1.0908F)
      );
      body.addOrReplaceChild(
         "part92",
         CubeListBuilder.create().texOffs(0, 24).addBox(-3.0214F, -16.0916F, -10.0F, 5.0F, 10.0F, 10.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(56.5F, -52.8F, -3.0F, -0.0999F, 0.5148F, -0.6808F)
      );
      body.addOrReplaceChild(
         "part93",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0214F, -4.0916F, 0.0F, 5.0F, 4.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0214F, -16.0916F, 0.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(56.5F, -52.8F, 9.0F, 0.0F, -0.2182F, -0.48F)
      );
      body.addOrReplaceChild(
         "part94",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0214F, -4.0916F, -2.0F, 5.0F, 4.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0214F, -16.0916F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(56.5F, -52.8F, -1.0F, 0.0F, 0.0F, -0.48F)
      );
      body.addOrReplaceChild(
         "part95",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.9518F, -3.0182F, -15.0F, 5.0F, 4.0F, 15.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.9518F, -12.0182F, -15.0F, 5.0F, 9.0F, 15.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(54.5F, -44.1F, -3.0F, 0.0F, 0.6894F, 0.2182F)
      );
      body.addOrReplaceChild(
         "part96",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9161F, -7.563F, 0.1404F, 5.0F, 9.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(51.3F, -46.3F, 20.3F, 0.0F, -1.0472F, 0.2182F)
      );
      body.addOrReplaceChild(
         "part97",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9518F, -9.0182F, 0.0F, 5.0F, 9.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(54.5F, -44.1F, 9.0F, 0.0F, -0.3054F, 0.2182F)
      );
      body.addOrReplaceChild(
         "part98",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.9518F, -9.0182F, -2.0F, 5.0F, 9.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(54.5F, -44.1F, -1.0F, 0.0F, 0.0F, 0.2182F)
      );
      body.addOrReplaceChild(
         "part99",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0225F, -3.0598F, -13.0F, 5.0F, 11.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0225F, -14.0598F, -13.0F, 5.0F, 11.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(43.6F, -31.0F, -3.0F, -0.0621F, 0.5108F, 0.5485F)
      );
      body.addOrReplaceChild(
         "part100",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0216F, 4.8429F, -0.0255F, 5.0F, 6.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0216F, -7.1571F, -0.0255F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(43.6F, -37.4F, 20.3F, 0.0F, -0.9599F, 0.6981F)
      );
      body.addOrReplaceChild(
         "part101",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0225F, -5.0598F, 0.0F, 5.0F, 7.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0225F, -17.0598F, 0.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(43.6F, -31.0F, 9.0F, 0.0F, -0.3491F, 0.6981F)
      );
      body.addOrReplaceChild(
         "part102",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0225F, -5.0598F, -2.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0225F, -17.0598F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(43.6F, -31.0F, -1.0F, 0.0F, 0.0F, 0.6981F)
      );
      body.addOrReplaceChild(
         "part103",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.996F, -1.6845F, -0.0289F, 5.0F, 4.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.996F, -13.6845F, -0.0289F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(34.95F, -15.5F, 20.3F, 0.0F, -0.6981F, 0.4363F)
      );
      body.addOrReplaceChild(
         "part104",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0061F, 0.2384F, 0.0F, 5.0F, 11.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0061F, -11.7616F, 0.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(39.5F, -15.5F, 9.0F, 0.0F, -0.3491F, 0.4363F)
      );
      body.addOrReplaceChild(
         "part105",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0061F, 1.2384F, -11.0F, 5.0F, 6.0F, 11.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0061F, -11.7616F, -11.0F, 5.0F, 13.0F, 11.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(39.5F, -15.5F, -3.0F, 0.0F, 0.8465F, 0.4363F)
      );
      body.addOrReplaceChild(
         "part106",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0929F, -0.9617F, -2.0F, 5.0F, 1.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0929F, -12.9617F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0929F, -24.9617F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(34.0F, -3.5F, -1.0F, 0.0F, 0.0F, 0.4363F)
      );
      body.addOrReplaceChild(
         "part107",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.0179F, 3.2586F, 0.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.0179F, -8.7414F, 0.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(25.5F, -1.3F, 9.0F, 0.0475F, -0.346F, 1.1698F)
      );
      body.addOrReplaceChild(
         "part108",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.9523F, 2.2409F, -12.0F, 5.0F, 3.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.9523F, -9.7591F, -12.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(24.5F, -1.1F, -3.0F, 0.0F, 0.9599F, 1.309F)
      );
      body.addOrReplaceChild(
         "part109",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.7207F, -2.8735F, -2.0F, 5.0F, 3.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.7207F, -14.8735F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.5F, 0.0F, -1.0F, 0.0F, 0.0F, 1.309F)
      );
      body.addOrReplaceChild(
         "part110",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.883F, -10.9741F, -2.0F, 5.0F, 11.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(10.5F, 6.5F, -1.0F, 0.0F, 0.0F, 0.9599F)
      );
      body.addOrReplaceChild(
         "part111",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -11.0F, -2.0F, 5.0F, 11.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, 1.1345F)
      );
      body.addOrReplaceChild(
         "part112",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0489F, -3.0731F, -2.0F, 5.0F, 3.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-10.7F, 8.0F, -1.0F, 0.0F, 0.0F, -0.8727F)
      );
      body.addOrReplaceChild(
         "part113",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 4.0F, -6.0F, 0.1309F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part114",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-2.4965F, -8.2953F, -1.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 1.2519F, -0.2368F, 0.8109F)
      );
      body.addOrReplaceChild(
         "part115",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.4965F, -4.2953F, -2.0F, 11.0F, 4.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -1.1345F)
      );
      body.addOrReplaceChild(
         "part116",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-4.4965F, -7.2953F, -2.0F, 7.0F, 8.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 0.0F, 0.0F, 0.6981F)
      );
      body.addOrReplaceChild(
         "part117",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-2.0341F, -3.1877F, -4.5034F, 5.0F, 7.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(0.5F, 0.0F, -3.0F, 1.9222F, -0.318F, 0.8876F)
      );
      body.addOrReplaceChild(
         "part118",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-3.4217F, -10.2414F, -9.7983F, 6.0F, 7.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-7.5F, -5.0F, -1.0F, 2.2689F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part119",
         CubeListBuilder.create().texOffs(0, 24).addBox(-3.4217F, -10.2414F, -5.7983F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-7.5F, -5.0F, -1.0F, 1.4399F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part120",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -8.2953F, -2.0F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 1.0036F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part121",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5035F, -7.2953F, -2.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -0.5672F)
      );
      body.addOrReplaceChild(
         "part122",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.4965F, -6.2953F, -2.0F, 5.0F, 7.0F, 9.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, -1.0943F, 0.0603F, 0.8873F)
      );
      body.addOrReplaceChild(
         "part123",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -6.2953F, -2.0F, 6.0F, 8.0F, 9.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, -1.0908F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part124",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -7.2953F, -2.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part125",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.5F, -8.8659F, -2.4041F, 3.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-2.5F, 6.5F, -3.0F, 0.3295F, 0.1172F, -0.3295F)
      );
      body.addOrReplaceChild(
         "part126",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -14.0F, -3.0F, 5.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 13.0F, 0.0F, 0.3491F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part127",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-2.5F, -2.0F, -2.0F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-2.5F, -21.0F, -2.0F, 5.0F, 19.0F, 5.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 15.0F, 0.0F, -0.2182F, 0.0F, 0.0F)
      );
      return body;
   }
}
