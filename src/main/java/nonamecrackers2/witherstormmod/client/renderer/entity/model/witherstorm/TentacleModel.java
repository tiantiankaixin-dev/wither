package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class TentacleModel {
   public final ModelPart tentacle;
   protected final ModelPart segmentOne;
   protected final ModelPart segmentTwo;
   protected final ModelPart segmentThree;
   protected final ModelPart segmentFour;
   protected final ModelPart segmentFive;
   protected final ModelPart segmentSix;
   public float scale;
   public float xRotationalOffset;
   public float yRotationalOffset;
   public float animationSpeed = 1.0F;
   public float yAngularOffset;
   public float xAngularOffset;
   public float animationOffset;
   public float reach = 1.0F;

   public TentacleModel(ModelPart root, float scale) {
      this.tentacle = root.getChild("base");
      this.segmentOne = this.tentacle.getChild("segment1");
      this.segmentTwo = this.segmentOne.getChild("segment2");
      this.segmentThree = this.segmentTwo.getChild("segment3");
      this.segmentFour = this.segmentThree.getChild("segment4");
      this.segmentFive = this.segmentFour.getChild("segment5");
      this.segmentSix = this.segmentFive.getChild("segment6");
      this.scale = scale;
   }

   public static void populateDefinition(PartDefinition root, int[] length, PartPose offset) {
      PartDefinition tentacle = root.addOrReplaceChild("base", CubeListBuilder.create(), offset);
      PartDefinition segment1 = tentacle.addOrReplaceChild(
         "segment1",
         CubeListBuilder.create()
            .texOffs(36 + (24 - length[0]), 14 + (24 - length[0]))
            .addBox(-6.0F, -6.0F, (float)(-length[0]), 12.0F, 12.0F, (float)length[0], false),
         PartPose.ZERO
      );
      PartDefinition segment2 = segment1.addOrReplaceChild(
         "segment2",
         CubeListBuilder.create()
            .texOffs(76 + (32 - length[1]), 32 + (32 - length[1]))
            .addBox(-5.0F, -5.0F, (float)(-length[1]), 10.0F, 10.0F, (float)length[1], false),
         PartPose.offset(0.0F, 0.0F, (float)(-length[0]))
      );
      PartDefinition segment3 = segment2.addOrReplaceChild(
         "segment3",
         CubeListBuilder.create()
            .texOffs(76 + (32 - length[2]), 33 + (32 - length[2]))
            .addBox(-4.0F, -4.0F, (float)(-length[2]), 8.0F, 8.0F, (float)length[2], false),
         PartPose.offset(0.0F, 0.0F, (float)(-length[1]))
      );
      PartDefinition segment4 = segment3.addOrReplaceChild(
         "segment4",
         CubeListBuilder.create()
            .texOffs(77 + (32 - length[3]), 34 + (32 - length[3]))
            .addBox(-3.0F, -3.0F, (float)(-length[3]), 6.0F, 6.0F, (float)length[3], false),
         PartPose.offset(0.0F, 0.0F, (float)(-length[2]))
      );
      PartDefinition segment5 = segment4.addOrReplaceChild(
         "segment5",
         CubeListBuilder.create()
            .texOffs(76 + (34 - length[4]), 36 + (34 - length[4]))
            .addBox(-2.0F, -2.0F, (float)(-length[4]), 4.0F, 4.0F, (float)length[4], false),
         PartPose.offset(0.0F, 0.0F, (float)(-length[3]))
      );
      segment5.addOrReplaceChild(
         "segment6",
         CubeListBuilder.create()
            .texOffs(77 + (34 - length[5]), 38 + (34 - length[5]))
            .addBox(-1.0F, -1.0F, (float)(-length[5]), 2.0F, 2.0F, (float)length[5], false),
         PartPose.offset(0.0F, 0.0F, (float)(-length[4]))
      );
   }

   public void setupAnimations(float animation, float partialTicks) {
      float speed = this.animationSpeed;
      float f = Mth.cos((animation + this.animationOffset * 10.0F) * speed * 0.1F) * this.reach;
      float s = Mth.sin((animation + this.animationOffset * 10.0F) * speed / 2.0F * 0.1F) * this.reach;
      this.tentacle.yRot = s * f * 0.05F + this.yRotationalOffset;
      this.tentacle.xRot = f * s * 0.05F + this.xRotationalOffset;
      this.segmentOne.xRot = f * -0.1F;
      this.segmentTwo.xRot = f * 0.1F + this.xAngularOffset;
      this.segmentThree.xRot = f * 0.075F + this.xAngularOffset;
      this.segmentFour.xRot = f * 0.05F + this.xAngularOffset;
      this.segmentFive.xRot = f * 0.1F + this.xAngularOffset;
      this.segmentSix.xRot = f * 0.1F + this.xAngularOffset;
      this.segmentTwo.yRot = this.yAngularOffset;
      this.segmentThree.yRot = this.yAngularOffset;
      this.segmentFour.yRot = this.yAngularOffset;
      this.segmentFive.yRot = this.yAngularOffset;
      this.segmentSix.yRot = this.yAngularOffset;
   }
}
