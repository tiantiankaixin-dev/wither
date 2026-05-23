package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.mass;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class WSHunchback3_2 {
   public static PartDefinition createBodyModel(PartDefinition root, float texScale) {
      PartDefinition back = root.addOrReplaceChild(
         "mass",
         CubeListBuilder.create()
            .texOffs(0, 148)
            .addBox(-3.0F, -9.0F, 1.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(136, 128)
            .addBox(-15.0F, -14.0F, -2.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE)
            .texOffs(0, 152)
            .addBox(7.0F, -21.0F, 8.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
            .texOffs(128, 124)
            .addBox(0.0F, -21.0F, 8.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
            .texOffs(0, 136)
            .addBox(-4.0F, -13.0F, -2.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE)
            .texOffs(0, 136)
            .addBox(-7.1632F, -8.8161F, -0.1051F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE)
            .texOffs(0, 148)
            .addBox(-4.75F, -21.25F, -1.5F, 16.0F, 6.0F, 6.0F, CubeDeformation.NONE),
         PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      back.addOrReplaceChild(
         "back_r1",
         CubeListBuilder.create().texOffs(0, 147).addBox(-2.5F, -0.75F, -3.25F, 5.0F, 7.5F, 5.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-1.0F, 6.5F, 6.5F, 0.2618F, 0.0F, 0.0436F)
      );
      back.addOrReplaceChild(
         "back_r2",
         CubeListBuilder.create().texOffs(0, 150).addBox(-0.5F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-1.6469F, 16.5278F, 7.4202F, 0.1745F, -0.0436F, -0.0436F)
      );
      back.addOrReplaceChild(
         "back_r3",
         CubeListBuilder.create().texOffs(0, 150).addBox(-0.5F, -1.0F, -0.75F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-0.6939F, 16.7283F, 8.741F, 0.1745F, 0.0436F, -0.0436F)
      );
      back.addOrReplaceChild(
         "back_r4",
         CubeListBuilder.create().texOffs(-1, 150).addBox(-1.75F, -1.625F, -0.9375F, 3.0F, 5.0F, 3.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-1.0F, 12.75F, 6.5F, 0.2182F, 0.0F, 0.0436F)
      );
      back.addOrReplaceChild(
         "back_r5",
         CubeListBuilder.create().texOffs(0, 145).addBox(-3.75F, -1.75F, -4.25F, 7.5F, 7.5F, 7.5F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-0.5F, 3.4829F, 6.2389F, 0.1309F, 0.0F, 0.0F)
      );
      back.addOrReplaceChild(
         "back_r6",
         CubeListBuilder.create().texOffs(0, 140).addBox(-5.25F, -6.5F, -5.0F, 10.0F, 10.0F, 10.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-2.2795F, -14.2006F, 12.2003F, 0.9163F, -0.1309F, 0.0873F)
      );
      back.addOrReplaceChild(
         "back_r7",
         CubeListBuilder.create().texOffs(0, 136).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(0.0F, -12.0F, -3.0F, 0.1309F, 0.0F, 0.0436F)
      );
      back.addOrReplaceChild(
         "back_r8",
         CubeListBuilder.create().texOffs(0, 144).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(5.2032F, -11.5751F, -0.4504F, -0.1309F, -0.1309F, 0.3927F)
      );
      back.addOrReplaceChild(
         "back_r9",
         CubeListBuilder.create().texOffs(128, 133).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(1.0F, -7.0F, -2.0F, -0.0873F, -0.1745F, 0.0873F)
      );
      back.addOrReplaceChild(
         "back_r10",
         CubeListBuilder.create().texOffs(0, 140).addBox(-4.5F, -6.0F, -6.0F, 10.0F, 10.0F, 10.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-8.9905F, -16.1304F, -3.0436F, 1.8762F, -2.618F, 0.1309F)
      );
      back.addOrReplaceChild(
         "back_r11",
         CubeListBuilder.create().texOffs(0, 132).addBox(-5.0F, 1.0F, -12.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-13.0F, -34.0F, 6.0F, 0.48F, -0.7418F, 0.1309F)
      );
      back.addOrReplaceChild(
         "back_r12",
         CubeListBuilder.create().texOffs(0, 144).addBox(-1.0F, -0.75F, -4.75F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-9.25F, -7.5F, 6.25F, 0.0873F, 0.0436F, -0.4363F)
      );
      back.addOrReplaceChild(
         "back_r13",
         CubeListBuilder.create().texOffs(0, 128).addBox(-8.0F, -11.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-6.0F, -22.0F, 12.0F, -0.5236F, -0.1745F, 0.0436F)
      );
      back.addOrReplaceChild(
         "back_r14",
         CubeListBuilder.create().texOffs(0, 132).addBox(-7.0F, -7.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(3.0F, -24.6809F, 16.2128F, -0.48F, 0.4363F, 0.2182F)
      );
      back.addOrReplaceChild(
         "back_r15",
         CubeListBuilder.create().texOffs(136, 125).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(5.0F, -7.0F, 2.0F, -0.0873F, 0.0F, 0.1309F)
      );
      back.addOrReplaceChild(
         "back_r16",
         CubeListBuilder.create().texOffs(0, 136).addBox(-6.0F, -9.0F, -2.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-0.2442F, -24.9694F, 5.7408F, -0.48F, 0.1309F, 0.0F)
      );
      back.addOrReplaceChild(
         "back_r17",
         CubeListBuilder.create().texOffs(0, 128).addBox(-8.0F, -8.0F, -7.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(-2.0F, -13.0F, 4.0F, 0.0F, 0.0F, 0.2618F)
      );
      back.addOrReplaceChild(
         "back_r18",
         CubeListBuilder.create().texOffs(0, 140).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(8.0F, -16.0F, 6.0F, -0.48F, 0.1745F, 0.2182F)
      );
      back.addOrReplaceChild(
         "back_r19",
         CubeListBuilder.create().texOffs(0, 136).addBox(-6.0F, -3.0F, -9.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE),
         PartPose.offsetAndRotation(9.0F, -28.0F, 7.0F, 0.2182F, 1.0472F, -0.5236F)
      );
      return back;
   }
}
