package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class WSHunchback3_1 {
   public static PartDefinition createBodyModel(PartDefinition root, float texScale) {
      PartDefinition back = root.addOrReplaceChild(
         "mass",
         CubeListBuilder.create()
            .texOffs(0, 148)
            .addBox(-3.0F, -9.0F, 1.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-13.0F, -13.0F, 1.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-15.0F, -14.0F, -2.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 144)
            .addBox(-16.0F, -21.0F, -1.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(7.0F, -21.0F, 8.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(8.0F, -12.0F, 1.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(11.0F, -17.0F, 1.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(10.0F, -15.0F, 3.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-12.0F, -8.0F, -1.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-6.0F, -20.0F, -3.0F, 11.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(6, 150)
            .addBox(5.0F, -13.0F, -2.0F, 3.0F, 3.0F, 3.0F, CubeDeformation.NONE)
            .texOffs(0, 150)
            .addBox(2.0F, -10.0F, -5.0F, 5.0F, 5.0F, 5.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(8.0F, -17.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(4.0F, -17.0F, -6.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-7.0F, -17.0F, -5.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-8.0F, -12.0F, -7.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-13.0F, -10.0F, -3.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE),
         PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      back.addOrReplaceChild(
         "back_r1",
         CubeListBuilder.create().texOffs(0, 152).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-4.0F, -7.0F, -7.0F, 0.0F, -0.0436F, -0.0873F)
      );
      back.addOrReplaceChild(
         "back_r2",
         CubeListBuilder.create().texOffs(0, 152).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-5.0F, -13.0F, -8.0F, 0.0F, 0.0F, 0.0873F)
      );
      back.addOrReplaceChild(
         "back_r3",
         CubeListBuilder.create().texOffs(0, 150).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 6.0F, 4.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, 0.0F, 0.0F, -0.0436F)
      );
      back.addOrReplaceChild(
         "back_r4",
         CubeListBuilder.create().texOffs(0, 153).addBox(-0.5F, -2.0F, 0.25F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(0.0F, 9.4208F, 2.1556F, 0.2182F, -0.0436F, -0.0436F)
      );
      back.addOrReplaceChild(
         "back_r5",
         CubeListBuilder.create().texOffs(0, 154).addBox(-1.0F, -2.5F, -0.75F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(0.0F, 6.0F, 2.0F, 0.1309F, 0.0F, 0.0436F)
      );
      back.addOrReplaceChild(
         "back_r6",
         CubeListBuilder.create().texOffs(0, 136).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(0.0F, -12.0F, -3.0F, 0.1309F, 0.0F, 0.0436F)
      );
      back.addOrReplaceChild(
         "back_r7",
         CubeListBuilder.create().texOffs(0, 144).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(1.0F, -7.0F, -2.0F, -0.0873F, -0.1745F, 0.0873F)
      );
      back.addOrReplaceChild(
         "back_r8",
         CubeListBuilder.create().texOffs(0, 148).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-3.0F, -5.0F, 1.0F, 0.0F, -0.0873F, 0.0873F)
      );
      back.addOrReplaceChild(
         "back_r9",
         CubeListBuilder.create().texOffs(0, 148).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(2.0F, -3.0F, 2.0F, -0.1309F, 0.0F, 0.0F)
      );
      back.addOrReplaceChild(
         "back_r10",
         CubeListBuilder.create().texOffs(0, 132).addBox(-5.0F, 1.0F, -12.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-13.0F, -34.0F, 6.0F, 0.48F, -0.7418F, 0.1309F)
      );
      back.addOrReplaceChild(
         "back_r11",
         CubeListBuilder.create().texOffs(0, 148).addBox(-3.0F, -3.0F, 3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(6.0F, -11.0F, -7.0F, -0.0873F, -0.0873F, 0.1309F)
      );
      back.addOrReplaceChild(
         "back_r12",
         CubeListBuilder.create().texOffs(0, 148).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(5.0F, -7.0F, 2.0F, -0.0873F, 0.0F, 0.1309F)
      );
      back.addOrReplaceChild(
         "back_r13",
         CubeListBuilder.create().texOffs(0, 136).addBox(-6.0F, -9.0F, -2.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-0.2442F, -24.9694F, 5.7408F, -0.48F, 0.1309F, 0.0F)
      );
      back.addOrReplaceChild(
         "back_r14",
         CubeListBuilder.create().texOffs(0, 128).addBox(-8.0F, -8.0F, -7.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-2.0F, -13.0F, 4.0F, -0.0436F, -0.0436F, 0.2618F)
      );
      back.addOrReplaceChild(
         "back_r15",
         CubeListBuilder.create().texOffs(0, 140).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(8.0F, -16.0F, 6.0F, -0.48F, 0.1745F, 0.2182F)
      );
      back.addOrReplaceChild(
         "back_r16",
         CubeListBuilder.create().texOffs(0, 136).addBox(-6.0F, -3.0F, -9.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(9.0F, -28.0F, 7.0F, 0.2182F, 1.0472F, -0.5236F)
      );
      return back;
   }
}
