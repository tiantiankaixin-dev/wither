package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class LowResDestroyerBodyModel {
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
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 4.0F, -6.0F, 0.1309F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part2",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -14.0F, -2.0F, 5.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, 0.3927F)
      );
      body.addOrReplaceChild(
         "part3",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-2.4965F, -8.2953F, -1.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 1.2519F, -0.2368F, 0.8109F)
      );
      body.addOrReplaceChild(
         "part4",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.4965F, -4.2953F, -2.0F, 11.0F, 4.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -1.1345F)
      );
      body.addOrReplaceChild(
         "part5",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-4.4965F, -7.2953F, -2.0F, 7.0F, 8.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, 0.0F, 0.0F, 0.6981F)
      );
      body.addOrReplaceChild(
         "part6",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-2.0341F, -3.1877F, -4.5034F, 5.0F, 7.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(0.5F, 0.0F, -3.0F, 1.9222F, -0.318F, 0.8876F)
      );
      body.addOrReplaceChild(
         "part7",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-3.4217F, -10.2414F, -9.7983F, 6.0F, 7.0F, 13.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-7.5F, -5.0F, -1.0F, 2.2689F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part8",
         CubeListBuilder.create().texOffs(0, 24).addBox(-3.4217F, -10.2414F, -5.7983F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-7.5F, -5.0F, -1.0F, 1.4399F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part9",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -8.2953F, -2.0F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 1.0036F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part10",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5035F, -7.2953F, -2.0F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -0.5672F)
      );
      body.addOrReplaceChild(
         "part11",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .mirror()
            .addBox(-3.4965F, -6.2953F, -2.0F, 5.0F, 7.0F, 9.0F, new CubeDeformation(0.0F), texScale, texScale)
            .mirror(false),
         PartPose.offsetAndRotation(4.5F, -2.0F, -1.0F, -1.0943F, 0.0603F, 0.8873F)
      );
      body.addOrReplaceChild(
         "part12",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -6.2953F, -2.0F, 6.0F, 8.0F, 9.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, -1.0908F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part13",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5035F, -7.2953F, -2.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-4.5F, -2.0F, -1.0F, 0.0F, 0.0F, -1.0036F)
      );
      body.addOrReplaceChild(
         "part14",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -14.0F, -2.0F, 5.0F, 14.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 9.0F, -1.0F, 0.0F, 0.0F, -0.4363F)
      );
      body.addOrReplaceChild(
         "part15",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.5F, -8.8659F, -2.4041F, 3.0F, 9.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-2.5F, 6.5F, -3.0F, 0.3295F, 0.1172F, -0.3295F)
      );
      body.addOrReplaceChild(
         "part16",
         CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, -14.0F, -3.0F, 5.0F, 13.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(-0.5F, 13.0F, 0.0F, 0.3491F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part17",
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
