package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class LowResDismantledBodyModel {
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
            .addBox(-13.0563F, -13.002F, 0.0055F, 13.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(-29.35F, -8.85F, 19.3F, 1.4882F, -0.2954F, 1.7976F)
      );
      body.addOrReplaceChild(
         "part3",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-12.9838F, -13.0207F, 0.0017F, 13.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(-18.75F, -1.85F, 21.7F, 1.8162F, -0.1854F, 0.5843F)
      );
      body.addOrReplaceChild(
         "part4",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-19.25F, -15.214F, -3.1328F, 13.0F, 15.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(0.25F, 2.25F, 23.5F, 1.8497F, -0.1286F, 0.371F)
      );
      body.addOrReplaceChild(
         "part5",
         CubeListBuilder.create().texOffs(0, 24).addBox(6.25F, -15.214F, -3.1328F, 13.0F, 15.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, 2.25F, 23.5F, 1.8497F, 0.1286F, -0.371F)
      );
      body.addOrReplaceChild(
         "part6",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -15.214F, -0.1328F, 14.0F, 15.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, 2.25F, 23.5F, 2.0508F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part7",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-14.904F, -12.5148F, -0.0157F, 15.0F, 15.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(-28.95F, -19.05F, 22.85F, 0.9267F, -0.9568F, 2.2947F)
      );
      body.addOrReplaceChild(
         "part8",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-11.9513F, -15.9413F, 0.013F, 12.0F, 15.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(-19.15F, -19.05F, 30.65F, 2.5743F, -0.5406F, 0.2977F)
      );
      body.addOrReplaceChild(
         "part9",
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
         "part10",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0621F, 1.9527F, 0.0131F, 13.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(22.35F, -46.0F, -6.3F, -1.7068F, -0.9976F, 1.7322F)
      );
      body.addOrReplaceChild(
         "part11",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.05F, 6.7118F, -0.3956F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.05F, -54.0F, -10.0F, -0.6037F, -0.3286F, 0.219F)
      );
      body.addOrReplaceChild(
         "part12",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0114F, -0.9148F, -0.0446F, 11.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(27.35F, -36.4F, -9.25F, -2.722F, -0.8277F, 2.8242F)
      );
      body.addOrReplaceChild(
         "part13",
         CubeListBuilder.create().texOffs(0, 24).addBox(1.05F, -3.9331F, -0.0549F, 11.0F, 12.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(18.95F, -36.4F, -20.2F, -0.3064F, -0.4183F, 0.1278F)
      );
      body.addOrReplaceChild(
         "part14",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0729F, -2.6003F, -0.0204F, 18.0F, 15.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(37.05F, -16.25F, 17.95F, 0.6628F, 0.6132F, -3.1135F)
      );
      body.addOrReplaceChild(
         "part15",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(12.25F, -24.0646F, -0.5619F, 21.0F, 15.0F, 3.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(12.25F, -24.0646F, -3.5619F, 21.0F, 15.0F, 3.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -19.25F, 31.25F, 2.6142F, 0.0112F, -0.4007F)
      );
      body.addOrReplaceChild(
         "part16",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(7.25F, -2.0646F, -2.0619F, 12.0F, 2.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(7.25F, -20.0646F, -2.0619F, 12.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -19.25F, 31.25F, 2.6589F, 0.123F, -0.045F)
      );
      body.addOrReplaceChild(
         "part17",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-6.75F, -5.0646F, -0.0619F, 14.0F, 5.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-6.75F, -23.0646F, -0.0619F, 14.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -19.25F, 31.25F, 2.7925F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part18",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.05F, -5.8678F, -0.0087F, 11.0F, 11.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.05F, -25.05F, 31.05F, -3.0955F, 0.2181F, 0.0155F)
      );
      body.addOrReplaceChild(
         "part19",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-10.9062F, -8.5007F, -0.1143F, 13.0F, 11.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-24.65F, -27.45F, 19.95F, -0.0606F, -1.4403F, -3.1065F)
      );
      body.addOrReplaceChild(
         "part20",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-10.9283F, 0.0265F, -0.2188F, 11.0F, 11.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-18.95F, -19.15F, 31.05F, -3.132F, -0.6102F, -0.0305F)
      );
      body.addOrReplaceChild(
         "part21",
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
         "part22",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.11F, -5.137F, -0.0404F, 11.0F, 11.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(28.15F, -30.75F, 20.75F, -0.8452F, 0.9056F, 2.416F)
      );
      body.addOrReplaceChild(
         "part23",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.05F, -6.2013F, 0.0631F, 11.0F, 11.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(18.95F, -35.75F, 28.05F, -2.6473F, 0.2316F, 0.1231F)
      );
      body.addOrReplaceChild(
         "part24",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-19.8969F, -12.2757F, 0.1673F, 20.0F, 17.0F, 4.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-22.05F, -40.15F, 16.85F, -1.4872F, -1.089F, -1.6651F)
      );
      body.addOrReplaceChild(
         "part25",
         CubeListBuilder.create().texOffs(0, 24).addBox(-5.95F, 3.1051F, 0.0496F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-19.05F, -30.15F, 30.95F, -2.6202F, -0.3843F, -0.2121F)
      );
      body.addOrReplaceChild(
         "part26",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(1.25F, -5.1081F, 0.0706F, 18.0F, 5.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.75F, -5.1081F, 0.0706F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -5.1081F, 0.0706F, 18.0F, 5.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.75F, -23.1081F, 0.0706F, 2.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, -23.1081F, 0.0706F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -23.1081F, 0.0706F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -50.75F, 20.25F, -2.6616F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part27",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.75F, -18.1384F, -0.1095F, 2.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, -18.1384F, -0.1095F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -18.1384F, -0.1095F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -56.0F, 3.0F, -1.8762F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part28",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(17.95F, -20.274F, -0.0226F, 10.0F, 12.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.05F, -16.274F, -0.0226F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.05F, -49.8F, 3.0F, -1.547F, 0.2987F, 1.388F)
      );
      body.addOrReplaceChild(
         "part29",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-18.05F, -18.1384F, -0.1095F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-18.95F, -56.0F, 3.0F, -1.5983F, -0.3042F, -1.4793F)
      );
      body.addOrReplaceChild(
         "part30",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.75F, -13.2882F, -0.3956F, 2.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -13.2882F, 5.6044F, 18.0F, 13.0F, 2.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -13.2882F, -0.3956F, 18.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, -13.2882F, -0.3956F, 18.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -54.0F, -10.0F, -1.4399F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part31",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(18.05F, -10.8751F, -0.0504F, 18.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.05F, -10.8751F, -0.0504F, 18.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(18.95F, -49.7F, -7.0F, -1.5423F, -0.1278F, 1.3508F)
      );
      body.addOrReplaceChild(
         "part32",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.75F, -0.2882F, -0.3956F, 2.0F, 17.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -0.2882F, -0.3956F, 18.0F, 17.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, -0.2882F, -0.3956F, 18.0F, 17.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -54.0F, -10.0F, -0.5672F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part33",
         CubeListBuilder.create().texOffs(0, 24).addBox(-19.05F, -0.5569F, 0.0261F, 19.0F, 17.0F, 5.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-18.95F, -54.0F, -10.5F, -1.3687F, 0.9903F, -1.3305F)
      );
      body.addOrReplaceChild(
         "part34",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.75F, -0.2146F, -0.9767F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-18.25F, -31.0F, -21.0F, 0.0036F, 0.3923F, 0.0181F)
      );
      body.addOrReplaceChild(
         "part35",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(1.25F, -7.208F, -0.6714F, 18.0F, 7.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.75F, -7.208F, -0.6714F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -7.208F, -0.6714F, 18.0F, 7.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -24.0F, -21.0F, 0.0436F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part36",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.9926F, -7.6892F, 0.0223F, 7.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-23.55F, -30.6F, -13.6F, -1.8659F, 1.2786F, -1.8781F)
      );
      body.addOrReplaceChild(
         "part37",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.95F, -9.977F, -0.0195F, 7.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-19.05F, -30.6F, -21.9F, -0.2962F, 0.335F, -0.1F)
      );
      body.addOrReplaceChild(
         "part38",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.75F, -10.1692F, -0.0746F, 2.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, -10.1692F, -0.0746F, 18.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, -10.1692F, -0.0746F, 18.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -30.4F, -21.9F, -0.2793F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part39",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.05F, -6.7472F, 0.0307F, 4.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(3.95F, -6.7472F, 0.0307F, 12.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.05F, -18.25F, -18.25F, 0.577F, -0.4114F, -0.2546F)
      );
      body.addOrReplaceChild(
         "part40",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0141F, -7.1011F, 0.1069F, 12.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(30.45F, -24.25F, -7.15F, 2.4773F, -0.625F, -2.712F)
      );
      body.addOrReplaceChild(
         "part41",
         CubeListBuilder.create().texOffs(0, 24).addBox(-17.024F, -5.1149F, 0.0412F, 17.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-22.35F, -24.25F, -11.65F, 1.6593F, 1.0061F, 1.6517F)
      );
      body.addOrReplaceChild(
         "part42",
         CubeListBuilder.create().texOffs(0, 24).addBox(-6.95F, 0.1489F, -0.0248F, 7.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-19.05F, -24.25F, -21.65F, 0.6206F, 0.4114F, 0.2546F)
      );
      body.addOrReplaceChild(
         "part43",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.75F, 0.1989F, 0.0618F, 2.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-18.75F, 0.1989F, 0.0618F, 18.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.25F, 0.1989F, 0.0618F, 18.0F, 10.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -24.25F, -21.75F, 0.5236F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part44",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(10.0F, -6.6204F, -0.1022F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(0.0F, -6.6204F, -0.1022F, 10.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(4.0F, 0.75F, -6.5F, 0.7153F, -0.1996F, -0.1706F)
      );
      body.addOrReplaceChild(
         "part45",
         CubeListBuilder.create().texOffs(0, 24).addBox(-14.95F, -0.3463F, -0.0146F, 15.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-13.05F, -4.0F, -10.6F, 0.8898F, 0.1996F, 0.1706F)
      );
      body.addOrReplaceChild(
         "part46",
         CubeListBuilder.create().texOffs(0, 24).addBox(-12.75F, -0.4106F, -0.0912F, 17.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, -4.0F, -10.5F, 0.6981F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part47",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0026F, -12.4879F, 0.0061F, 12.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(35.3F, -10.5F, -0.8F, 2.6182F, -0.3897F, -2.9386F)
      );
      body.addOrReplaceChild(
         "part48",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0384F, -7.2163F, 0.0219F, 18.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.0F, -10.5F, -11.8F, 0.49F, -0.1874F, -0.1119F)
      );
      body.addOrReplaceChild(
         "part49",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -0.4395F, -0.0793F, 15.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(4.0F, -15.5F, -16.5F, 0.4839F, -0.1103F, -0.0706F)
      );
      body.addOrReplaceChild(
         "part50",
         CubeListBuilder.create().texOffs(0, 24).addBox(-15.05F, -0.9753F, -0.0258F, 15.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-12.95F, -15.0F, -16.3F, 0.4943F, 0.2316F, 0.1231F)
      );
      body.addOrReplaceChild(
         "part51",
         CubeListBuilder.create().texOffs(0, 24).addBox(-12.75F, -19.0F, 0.0F, 17.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.25F, 1.0F, -8.0F, 0.48F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part52",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0672F, -9.0045F, -12.0F, 5.0F, 4.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-28.4F, -1.25F, -3.0F, 0.0F, -0.6981F, -0.3054F)
      );
      body.addOrReplaceChild(
         "part53",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.044F, -13.0153F, -6.5784F, 6.0F, 13.0F, 11.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-33.5F, -22.75F, -3.0F, -0.3938F, 0.5588F, 1.7578F)
      );
      body.addOrReplaceChild(
         "part54",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(1.9328F, -7.0045F, -13.0F, 5.0F, 1.0F, 11.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(1.9328F, -20.0045F, -13.0F, 5.0F, 13.0F, 11.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-28.4F, -1.25F, -3.0F, -0.0479F, -0.6699F, -0.6419F)
      );
      body.addOrReplaceChild(
         "part55",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-7.0198F, -5.1664F, -2.0F, 7.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-19.0198F, -5.1664F, -2.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-29.9F, -5.85F, -1.0F, 0.0F, 0.0F, 1.9199F)
      );
      body.addOrReplaceChild(
         "part56",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.0672F, -5.0045F, -2.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-28.4F, -1.25F, -1.0F, 0.0F, 0.0F, -0.3054F)
      );
      body.addOrReplaceChild(
         "part57",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-0.0048F, -5.0256F, -2.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-0.0048F, -17.0256F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-13.0F, 6.0F, -1.0F, 0.0F, 0.0F, -1.1345F)
      );
      body.addOrReplaceChild(
         "part58",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -10.0F, -2.0F, 5.0F, 10.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, -1.2217F)
      );
      body.addOrReplaceChild(
         "part59",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.051F, -9.1636F, -13.1116F, 5.0F, 11.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(27.9F, -41.7F, 16.0F, 0.2347F, 1.3475F, -0.8946F)
      );
      body.addOrReplaceChild(
         "part60",
         CubeListBuilder.create().texOffs(0, 24).addBox(-5.0294F, -8.706F, 0.0138F, 5.0F, 11.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(28.5F, -44.5F, 20.1F, 0.0955F, -1.0006F, -1.2042F)
      );
      body.addOrReplaceChild(
         "part61",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.0179F, 3.2586F, 0.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.0179F, 0.2586F, 0.0F, 5.0F, 3.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.0179F, -11.7414F, 0.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(25.5F, -1.3F, 9.0F, 0.0475F, -0.346F, 1.1698F)
      );
      body.addOrReplaceChild(
         "part62",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-5.0342F, -3.0348F, -1.9937F, 5.0F, 3.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-5.0342F, -15.0348F, -1.9937F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(34.7F, -9.8F, 9.0F, 0.3244F, 0.1313F, -0.633F)
      );
      body.addOrReplaceChild(
         "part63",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.9523F, 2.2409F, -12.0F, 5.0F, 3.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.9523F, -6.7591F, -12.0F, 5.0F, 9.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(24.5F, -1.1F, -3.0F, 0.0F, 0.9599F, 1.309F)
      );
      body.addOrReplaceChild(
         "part64",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.7207F, -3.8735F, -2.0F, 5.0F, 4.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.7207F, -15.8735F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(19.5F, 0.0F, -1.0F, 0.0F, 0.0F, 1.309F)
      );
      body.addOrReplaceChild(
         "part65",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-4.9406F, -1.0988F, -2.0F, 5.0F, 1.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-4.9406F, -13.0988F, -2.0F, 5.0F, 12.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(33.5F, -8.7F, -1.0F, 0.0F, 0.0F, -0.5236F)
      );
      body.addOrReplaceChild(
         "part66",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.883F, -10.9741F, -2.0F, 5.0F, 11.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(10.5F, 6.5F, -1.0F, 0.0F, 0.0F, 0.9599F)
      );
      body.addOrReplaceChild(
         "part67",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -11.0F, -2.0F, 5.0F, 11.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, 1.1345F)
      );
      body.addOrReplaceChild(
         "part68",
         CubeListBuilder.create().texOffs(0, 24).addBox(0.0489F, -3.0731F, -2.0F, 5.0F, 3.0F, 12.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-10.7F, 8.0F, -1.0F, 0.0F, 0.0F, -0.8727F)
      );
      body.addOrReplaceChild(
         "part69",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 4.0F, -6.0F, 0.1309F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part70",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-2.4965F, -8.2953F, -1.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 1.2519F, -0.2368F, 0.8109F)
      );
      body.addOrReplaceChild(
         "part71",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.4965F, -4.2953F, -2.0F, 11.0F, 4.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -1.1345F)
      );
      body.addOrReplaceChild(
         "part72",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-4.4965F, -7.2953F, -2.0F, 7.0F, 8.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 0.0F, 0.0F, 0.6981F)
      );
      body.addOrReplaceChild(
         "part73",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-2.0341F, -3.1877F, -4.5034F, 5.0F, 7.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(0.5F, 0.0F, -3.0F, 1.9222F, -0.318F, 0.8876F)
      );
      body.addOrReplaceChild(
         "part74",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-3.4217F, -10.2414F, -9.7983F, 6.0F, 7.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-7.5F, -5.0F, -1.0F, 2.2689F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part75",
         CubeListBuilder.create().texOffs(0, 24).addBox(-3.4217F, -10.2414F, -5.7983F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-7.5F, -5.0F, -1.0F, 1.4399F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part76",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -8.2953F, -2.0F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 1.0036F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part77",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5035F, -7.2953F, -2.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -0.5672F)
      );
      body.addOrReplaceChild(
         "part78",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.4965F, -6.2953F, -2.0F, 5.0F, 7.0F, 9.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, -1.0943F, 0.0603F, 0.8873F)
      );
      body.addOrReplaceChild(
         "part79",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -6.2953F, -2.0F, 6.0F, 8.0F, 9.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, -1.0908F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part80",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -7.2953F, -2.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part81",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.5F, -8.8659F, -2.4041F, 3.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-2.5F, 6.5F, -3.0F, 0.3295F, 0.1172F, -0.3295F)
      );
      body.addOrReplaceChild(
         "part82",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -14.0F, -3.0F, 5.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 13.0F, 0.0F, 0.3491F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part83",
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
