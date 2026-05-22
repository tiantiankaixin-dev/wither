package nonamecrackers2.witherstormmod.client.renderer.entity.model;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;

public class WitheredSymbiontModel<T extends WitheredSymbiontEntity> extends HumanoidModel<T> {
   private final ModelPart tentacle;
   private final ModelPart segment1;
   private final ModelPart segment2;
   private final ModelPart segment3;
   private final ModelPart segment4;
   private final ModelPart tentacle2;
   private final ModelPart segment5;
   private final ModelPart segment6;
   private final ModelPart segment7;
   private final ModelPart segment8;
   private final ModelPart tentacle3;
   private final ModelPart segment9;
   private final ModelPart segment10;
   private final ModelPart segment11;
   private final ModelPart segment12;
   private float crouchAnim;

   public WitheredSymbiontModel(ModelPart root) {
      super(root);
      ModelPart body = root.getChild("body");
      this.tentacle = body.getChild("tentacle");
      this.segment1 = this.tentacle.getChild("segment1");
      this.segment2 = this.segment1.getChild("segment2");
      this.segment3 = this.segment2.getChild("segment3");
      this.segment4 = this.segment3.getChild("segment4");
      this.tentacle2 = body.getChild("tentacle2");
      this.segment5 = this.tentacle2.getChild("segment1");
      this.segment6 = this.segment5.getChild("segment2");
      this.segment7 = this.segment6.getChild("segment3");
      this.segment8 = this.segment7.getChild("segment4");
      this.tentacle3 = body.getChild("tentacle3");
      this.segment9 = this.tentacle3.getChild("segment1");
      this.segment10 = this.segment9.getChild("segment2");
      this.segment11 = this.segment10.getChild("segment3");
      this.segment12 = this.segment11.getChild("segment4");
      this.hat.visible = false;
   }

   public static LayerDefinition createLayerDefinition() {
      MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild(
         "left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(1.9F, 12.0F, 0.0F)
      );
      PartDefinition body = root.getChild("body");
      body.addOrReplaceChild(
         "mass1",
         CubeListBuilder.create().texOffs(36, 32).addBox(-4.5F, -4.5F, -1.5F, 6.0F, 9.0F, 3.0F, false),
         PartPose.offsetAndRotation(1.2478F, 5.7655F, 2.1684F, -0.0783F, -0.1909F, -0.2221F)
      );
      body.addOrReplaceChild(
         "mass2",
         CubeListBuilder.create().texOffs(32, 4).addBox(-3.5F, -2.5F, -2.5F, 7.0F, 9.0F, 3.0F, false),
         PartPose.offsetAndRotation(-0.6213F, 2.8595F, -1.0036F, 0.2661F, -0.1641F, 0.244F)
      );
      body.addOrReplaceChild(
         "mass3",
         CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, false),
         PartPose.offsetAndRotation(-2.0355F, 9.8385F, 1.8323F, -2.5517F, -0.5708F, 1.7651F)
      );
      body.addOrReplaceChild(
         "mass4",
         CubeListBuilder.create().texOffs(16, 32).addBox(-2.5F, -0.5F, -4.5F, 5.0F, 5.0F, 5.0F, false),
         PartPose.offsetAndRotation(1.5F, 6.5F, -1.5F, 0.6346F, -0.678F, -0.4326F)
      );
      PartDefinition rightLeg = root.getChild("right_leg");
      rightLeg.addOrReplaceChild(
         "mass5",
         CubeListBuilder.create().texOffs(16, 32).addBox(-2.5F, -2.5F, 1.5F, 5.0F, 5.0F, 5.0F, false),
         PartPose.offsetAndRotation(-1.5F, 7.5F, -1.5F, 0.0406F, 0.4854F, -1.2322F)
      );
      rightLeg.addOrReplaceChild(
         "mass6",
         CubeListBuilder.create().texOffs(16, 32).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, false),
         PartPose.offsetAndRotation(-1.5F, 7.5F, -1.5F, 0.6929F, 0.4557F, 0.3503F)
      );
      rightLeg.addOrReplaceChild(
         "mass7",
         CubeListBuilder.create().texOffs(16, 32).addBox(-1.5F, -3.5F, -2.5F, 5.0F, 5.0F, 5.0F, false),
         PartPose.offsetAndRotation(-1.5F, 3.5F, -1.5F, 0.432F, -0.8648F, -0.6805F)
      );
      PartDefinition rightArm = root.getChild("right_arm");
      rightArm.addOrReplaceChild(
         "mass8",
         CubeListBuilder.create().texOffs(0, 32).addBox(-2.5F, -1.5F, -1.5F, 4.0F, 4.0F, 4.0F, false),
         PartPose.offsetAndRotation(-1.2211F, 4.1554F, 1.9027F, 1.6619F, -0.8156F, -1.5077F)
      );
      rightArm.addOrReplaceChild(
         "mass9",
         CubeListBuilder.create().texOffs(16, 32).addBox(-2.0F, 2.0F, 21.0F, 5.0F, 5.0F, 5.0F, false),
         PartPose.offsetAndRotation(5.0F, 22.0F, 0.0F, 1.7759F, 0.1628F, -0.3449F)
      );
      rightArm.addOrReplaceChild(
         "mass10",
         CubeListBuilder.create().texOffs(16, 32).addBox(-2.0F, -15.0F, 12.0F, 5.0F, 5.0F, 5.0F, false),
         PartPose.offsetAndRotation(5.0F, 22.0F, 0.0F, 0.9318F, -0.52F, -0.4963F)
      );
      PartDefinition head = root.getChild("head");
      head.addOrReplaceChild(
         "mass11",
         CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, false),
         PartPose.offsetAndRotation(-5.2146F, -1.96F, 2.8784F, 3.1231F, -0.7686F, -1.9387F)
      );
      head.addOrReplaceChild(
         "mass12",
         CubeListBuilder.create().texOffs(16, 32).addBox(-1.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, false),
         PartPose.offsetAndRotation(-4.7943F, -4.4645F, -3.6255F, -2.4784F, -0.4058F, -2.4209F)
      );
      head.addOrReplaceChild(
         "mass13",
         CubeListBuilder.create().texOffs(16, 32).addBox(-1.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, false),
         PartPose.offsetAndRotation(-2.5F, -7.5F, 2.5F, -2.4343F, -0.4891F, 2.7597F)
      );
      PartDefinition tentacle = body.addOrReplaceChild("tentacle", CubeListBuilder.create(), PartPose.offset(-3.5F, 9.5F, 0.0F));
      PartDefinition segment1 = tentacle.addOrReplaceChild(
         "segment1", CubeListBuilder.create().texOffs(54, 30).addBox(-1.5F, -1.5F, 1.0F, 3.0F, 3.0F, 8.0F, false), PartPose.ZERO
      );
      PartDefinition segment2 = segment1.addOrReplaceChild(
         "segment2", CubeListBuilder.create().texOffs(72, 33).addBox(-1.3F, -1.3F, 0.0F, 2.6F, 2.6F, 9.0F, false), PartPose.offset(0.0F, 0.0F, 9.0F)
      );
      PartDefinition segment3 = segment2.addOrReplaceChild(
         "segment3",
         CubeListBuilder.create().texOffs(68, 19).addBox(-0.9F, -0.9F, 0.0F, 1.8F, 1.8F, 11.0F, false),
         PartPose.offset(0.0F, 0.0F, 9.0F)
      );
      segment3.addOrReplaceChild(
         "segment4",
         CubeListBuilder.create().texOffs(58, 0).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 16.0F, false),
         PartPose.offset(0.0F, 0.0F, 11.0F)
      );
      PartDefinition tentacle2 = body.addOrReplaceChild("tentacle2", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));
      PartDefinition segment5 = tentacle2.addOrReplaceChild(
         "segment1", CubeListBuilder.create().texOffs(54, 30).addBox(-1.5F, -1.5F, 1.0F, 3.0F, 3.0F, 8.0F, false), PartPose.ZERO
      );
      PartDefinition segment6 = segment5.addOrReplaceChild(
         "segment2", CubeListBuilder.create().texOffs(72, 33).addBox(-1.3F, -1.3F, 0.0F, 2.6F, 2.6F, 9.0F, false), PartPose.offset(0.0F, 0.0F, 9.0F)
      );
      PartDefinition segment7 = segment6.addOrReplaceChild(
         "segment3",
         CubeListBuilder.create().texOffs(68, 19).addBox(-0.9F, -0.9F, 0.0F, 1.8F, 1.8F, 11.0F, false),
         PartPose.offset(0.0F, 0.0F, 9.0F)
      );
      segment7.addOrReplaceChild(
         "segment4",
         CubeListBuilder.create().texOffs(58, 0).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 16.0F, false),
         PartPose.offset(0.0F, 0.0F, 11.0F)
      );
      PartDefinition tentacle3 = body.addOrReplaceChild("tentacle3", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.5F, 0.0F));
      PartDefinition segment9 = tentacle3.addOrReplaceChild(
         "segment1", CubeListBuilder.create().texOffs(54, 30).addBox(-1.5F, -1.5F, 1.0F, 3.0F, 3.0F, 8.0F, false), PartPose.ZERO
      );
      PartDefinition segment10 = segment9.addOrReplaceChild(
         "segment2", CubeListBuilder.create().texOffs(72, 33).addBox(-1.3F, -1.3F, 0.0F, 2.6F, 2.6F, 9.0F, false), PartPose.offset(0.0F, 0.0F, 9.0F)
      );
      PartDefinition segment11 = segment10.addOrReplaceChild(
         "segment3",
         CubeListBuilder.create().texOffs(68, 19).addBox(-0.9F, -0.9F, 0.0F, 1.8F, 1.8F, 11.0F, false),
         PartPose.offset(0.0F, 0.0F, 9.0F)
      );
      segment11.addOrReplaceChild(
         "segment4",
         CubeListBuilder.create().texOffs(58, 0).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 16.0F, false),
         PartPose.offset(0.0F, 0.0F, 11.0F)
      );
      return LayerDefinition.create(mesh, 96, 96);
   }

   public void prepareMobModel(T entity, float p_212843_2_, float p_212843_3_, float partialTicks) {
      super.prepareMobModel(entity, p_212843_2_, p_212843_3_, partialTicks);
      float tickCount = entity.isVulnerable() ? 0.0F : (float)entity.tickCount + partialTicks;
      float additionalAnim = entity.walkAnimation.position(partialTicks);
      this.animateTentacle(
         tickCount, additionalAnim, this.tentacle, this.segment1, this.segment2, this.segment3, this.segment4, 0.0F, 0.4F, 1.5F, 0.0F, 0.0F, -30.0F, 10.0F
      );
      this.animateTentacle(
         tickCount, additionalAnim, this.tentacle2, this.segment5, this.segment6, this.segment7, this.segment8, 20.0F, 0.4F, 1.5F, 0.0F, 0.0F, 40.0F, 15.0F
      );
      this.animateTentacle(
         tickCount, additionalAnim, this.tentacle3, this.segment9, this.segment10, this.segment11, this.segment12, 40.0F, 0.4F, 1.5F, 0.0F, 0.0F, -10.0F, 50.0F
      );
      this.crouchAnim = entity.getVulnerableAnim(partialTicks);
   }

   public void setupAnim(T entity, float p_225597_2_, float p_225597_3_, float tickCount, float p_225597_5_, float p_225597_6_) {
      super.setupAnim(entity, p_225597_2_, p_225597_3_, tickCount, p_225597_5_, p_225597_6_);
      float tick = entity.isVulnerable() ? 0.0F : tickCount;
      float f = Mth.sin(this.attackTime * (float) Math.PI);
      float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float) Math.PI);
      this.rightArm.zRot = 0.0F;
      this.leftArm.zRot = 0.0F;
      this.rightArm.yRot = -(0.1F - f * 0.6F);
      this.leftArm.yRot = 0.1F - f * 0.6F;
      float f2 = (float) -Math.PI / (entity.isAggressive() ? 1.5F : 2.25F);
      this.rightArm.xRot = f2;
      this.leftArm.xRot = f2;
      this.rightArm.xRot += f * 1.2F - f1 * 0.4F;
      this.leftArm.xRot += f * 1.2F - f1 * 0.4F;
      AnimationUtils.bobArms(this.rightArm, this.leftArm, tick);
      this.body.xRot = this.body.xRot + this.crouchAnim;
      this.rightArm.xRot = this.rightArm.xRot + this.crouchAnim;
      this.leftArm.xRot = this.leftArm.xRot + this.crouchAnim;
      this.rightLeg.z = this.rightLeg.z + this.crouchAnim * 10.5F;
      this.leftLeg.z = this.leftLeg.z + this.crouchAnim * 10.5F;
      this.rightLeg.y = this.rightLeg.y + this.crouchAnim * 0.4F;
      this.leftLeg.y = this.leftLeg.y + this.crouchAnim * 0.4F;
      this.head.y = this.head.y + this.crouchAnim * 2.0F;
      this.body.y = this.body.y + this.crouchAnim * 3.0F;
      this.leftArm.y = this.leftArm.y + this.crouchAnim * 3.0F;
      this.rightArm.y = this.rightArm.y + this.crouchAnim * 3.0F;
      this.leftArm.z = this.leftArm.z + this.crouchAnim * 2.0F;
      this.rightArm.z = this.rightArm.z + this.crouchAnim * 2.0F;
      if (entity.isCastingSpell() || entity.isSummoningMobs() || entity.isDeadOrDying() || entity.hasAttackDelay()) {
         this.leftArm.yRot = -0.4F - 0.8F * Mth.sin(tickCount * 0.5F);
         this.rightArm.yRot = -0.4F - 0.8F * -Mth.sin(tickCount * 0.5F);
         this.leftArm.xRot = -2.5F;
         this.rightArm.xRot = -2.5F;
      }
   }

   protected void animateTentacle(
      float animation,
      float additionalAnim,
      ModelPart base,
      ModelPart segment1,
      ModelPart segment2,
      ModelPart segment3,
      ModelPart segment4,
      float offset,
      float speed,
      float reach,
      float xRotOffset,
      float yRotOffset,
      float xAngleOffset,
      float yAngleOffset
   ) {
      float f = Mth.cos((animation + offset * 10.0F) * speed * 0.1F) * reach + Mth.cos(additionalAnim);
      float s = Mth.sin((animation + offset * 10.0F) * speed / 2.0F * 0.1F) * reach + Mth.sin(additionalAnim);
      float yRotOffRadians = (float)Math.toRadians((double)yRotOffset);
      float xRotOffRadians = (float)Math.toRadians((double)xRotOffset);
      float xAngleOffRadians = (float)Math.toRadians((double)yAngleOffset);
      float yAngleOffRadians = (float)Math.toRadians((double)xAngleOffset);
      base.yRot = s * f * 0.05F + yRotOffRadians;
      base.xRot = f * s * 0.05F + xRotOffRadians;
      segment1.xRot = f * -0.1F;
      segment2.xRot = f * 0.1F + xAngleOffRadians;
      segment3.xRot = f * 0.075F + xAngleOffRadians;
      segment4.xRot = f * 0.05F + xAngleOffRadians;
      segment1.yRot = s * 0.1F + yAngleOffRadians;
      segment2.yRot = s * 0.075F + yAngleOffRadians;
      segment3.yRot = s * 0.05F + yAngleOffRadians;
      segment4.yRot = s * 0.01F + yAngleOffRadians;
   }

   protected void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
