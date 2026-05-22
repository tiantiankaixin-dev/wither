package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class LowResSegmentBodyModel {
   public static PartDefinition createBodyModel(PartDefinition root, float texScale) {
      PartDefinition body = root.addOrReplaceChild(
         "lowResMass",
         CubeListBuilder.create()
            .texOffs(17, 40)
            .addBox(-6.0F, -0.5F, -10.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(17, 40)
            .addBox(-3.0F, -0.5F, -10.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(17, 40)
            .addBox(0.0F, -0.5F, -10.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(17, 40)
            .addBox(-3.0F, 0.5F, -10.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offset(2.0F, -4.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "part1",
         CubeListBuilder.create().texOffs(0, 24).addBox(-3.0F, -10.0F, -6.0F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(0.0F, 2.5F, -0.5F, 0.0F, 0.2618F, 0.0436F)
      );
      body.addOrReplaceChild(
         "part2",
         CubeListBuilder.create().texOffs(0, 24).addBox(-7.0F, -4.3F, -9.0F, 12.0F, 5.0F, 2.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(0.0F, 2.5F, -0.5F, 0.0F, 0.0F, -0.0436F)
      );
      body.addOrReplaceChild(
         "part3",
         CubeListBuilder.create().texOffs(0, 24).addBox(-7.0F, -7.0F, -7.0F, 14.0F, 9.0F, 7.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(0.0F, 2.5F, -0.5F, 0.0F, 0.0F, -0.2182F)
      );
      body.addOrReplaceChild(
         "part4",
         CubeListBuilder.create()
            .texOffs(0, 24)
            .addBox(-2.1745F, -3.0038F, 4.0F, 5.0F, 7.0F, 6.0F, new CubeDeformation(0.0F), texScale, texScale)
            .texOffs(0, 24)
            .addBox(-2.1745F, -6.0038F, -1.0F, 6.0F, 7.0F, 7.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(0.0F, 0.5F, -0.5F, 0.0F, 0.2618F, 0.1309F)
      );
      body.addOrReplaceChild(
         "part5",
         CubeListBuilder.create().texOffs(0, 24).addBox(-10.0F, -5.0F, -3.2F, 5.0F, 6.0F, 7.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(0.0F, 4.5F, -0.5F, -2.4771F, 1.2923F, -2.5016F)
      );
      body.addOrReplaceChild(
         "part6",
         CubeListBuilder.create().texOffs(0, 24).addBox(-10.0F, -5.0F, -5.0F, 11.0F, 6.0F, 7.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(0.0F, 4.5F, -0.5F, -0.2618F, 0.0F, -0.0436F)
      );
      body.addOrReplaceChild(
         "part7",
         CubeListBuilder.create().texOffs(0, 24).addBox(-4.0F, -5.0F, 0.0F, 11.0F, 7.0F, 7.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(0.0F, 4.5F, -0.5F, 0.0F, 0.0F, -0.0436F)
      );
      body.addOrReplaceChild(
         "part8",
         CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -9.0F, -0.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F), texScale, texScale),
         PartPose.offsetAndRotation(2.5F, 11.5F, 10.0F, 0.7854F, 0.0F, 0.0F)
      );
      return body;
   }
}
