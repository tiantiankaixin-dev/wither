package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class PregnantHunchbackBodyModel {
   public static PartDefinition createBodyModel(PartDefinition root, float texScale) {
      PartDefinition back = root.addOrReplaceChild(
         "mass",
         CubeListBuilder.create()
            .texOffs(0, 140)
            .addBox(-4.0F, -27.0F, 1.0F, 10.0F, 10.0F, 10.0F, CubeDeformation.NONE)
            .texOffs(0, 144)
            .addBox(4.0F, -20.0F, 1.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
            .texOffs(0, 136)
            .addBox(-6.0F, -17.0F, -4.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE)
            .texOffs(0, 144)
            .addBox(4.0F, -32.0F, 0.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(10.0F, -30.0F, -1.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-8.0F, -30.0F, -1.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-1.0F, -33.0F, 3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 146)
            .addBox(-14.0F, -32.0F, 2.0F, 7.0F, 7.0F, 7.0F, CubeDeformation.NONE)
            .texOffs(0, 146)
            .addBox(-16.0F, -26.0F, 3.0F, 7.0F, 7.0F, 7.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-1.0F, -26.0F, 6.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-9.0F, -26.0F, 0.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-3.0F, -9.0F, 1.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(2.0F, -10.0F, 0.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-9.0F, -13.0F, 1.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-11.0F, -14.0F, -2.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 144)
            .addBox(-14.0F, -21.0F, -1.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-7.0F, -22.0F, 3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-17.0F, -30.0F, 1.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-15.0F, -22.0F, 0.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-1.0F, -24.0F, -2.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 142)
            .addBox(6.0F, -27.0F, 1.0F, 9.0F, 9.0F, 9.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(7.0F, -21.0F, 8.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(7.0F, -13.0F, 1.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(10.0F, -18.0F, 1.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(11.0F, -31.0F, 2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(5.0F, -34.0F, 2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(7.0F, -31.0F, 6.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(10.0F, -29.0F, 4.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(9.0F, -16.0F, 3.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-6.0F, -19.0F, 8.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-8.0F, -8.0F, -1.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-1.0F, -6.0F, -1.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-3.0F, -29.0F, 2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-13.0F, -33.0F, 3.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-15.0F, -34.0F, 4.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-18.0F, -31.0F, 2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(15.0F, -26.0F, 3.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-6.0F, -20.0F, -3.0F, 11.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-6.0F, -8.0F, -2.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(6, 150)
            .addBox(5.0F, -13.0F, -2.0F, 3.0F, 3.0F, 3.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(8.0F, -17.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(2.0F, -21.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 150)
            .addBox(-2.0F, -3.0F, 0.0F, 4.0F, 6.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 153)
            .addBox(0.0F, 7.0F, 2.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
            .texOffs(0, 154)
            .addBox(-1.0F, 3.0F, 1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(4.0F, -17.0F, -6.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-7.0F, -17.0F, -5.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-9.0F, -10.0F, -3.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(4.0F, -17.0F, -6.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-7.0F, -17.0F, -5.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(0, 150)
            .addBox(2.0F, -10.0F, -5.0F, 5.0F, 5.0F, 5.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(-8.0F, -12.0F, -7.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE),
         PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      back.addOrReplaceChild(
         "back_r1",
         CubeListBuilder.create().texOffs(0, 148).addBox(-3.0F, -3.0F, 3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(6.0F, -11.0F, -7.0F, -0.0873F, -0.0873F, 0.1309F)
      );
      back.addOrReplaceChild(
         "back_r2",
         CubeListBuilder.create().texOffs(0, 152).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-4.0F, -7.0F, -7.0F, 0.0F, -0.0436F, -0.0873F)
      );
      back.addOrReplaceChild(
         "back_r3",
         CubeListBuilder.create().texOffs(0, 152).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-5.0F, -13.0F, -8.0F, 0.0F, 0.0F, 0.0873F)
      );
      back.addOrReplaceChild(
         "back_r4",
         CubeListBuilder.create().texOffs(0, 144).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(1.0F, -7.0F, -2.0F, -0.0873F, -0.1745F, 0.0873F)
      );
      back.addOrReplaceChild(
         "back_r5",
         CubeListBuilder.create().texOffs(0, 136).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(0.0F, -12.0F, -3.0F, 0.1309F, 0.0F, 0.0436F)
      );
      return back;
   }
}
