package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.AbstractWitherStormModel;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormCommandBlockModel<T extends WitherStormEntity> extends AbstractWitherStormModel<T> {
   private final ModelPart base;
   private final ModelPart centerHead;
   private final ModelPart rightHead;
   private final ModelPart leftHead;
   private final ModelPart ribcage;
   private final ModelPart tail;

   public WitherStormCommandBlockModel(ModelPart root) {
      super(root, 1.0F);
      this.base = root.getChild("witherBase");
      this.ribcage = this.base.getChild("ribcage");
      this.tail = this.base.getChild("tail");
      this.centerHead = this.base.getChild("center_head");
      this.rightHead = this.base.getChild("right_head");
      this.leftHead = this.base.getChild("left_head");
   }

   public static LayerDefinition createLayerDefinition(CubeDeformation def) {
      return LayerDefinition.create(createBaseMesh(AbstractWitherStormModel.createMesh(), def, true, true, true), 160, 160);
   }

   public static MeshDefinition createBaseMesh(MeshDefinition mesh, CubeDeformation def, boolean hasCenterHead, boolean hasRibcageExtension, boolean hasTail) {
      PartDefinition base = mesh.getRoot().addOrReplaceChild("witherBase", CubeListBuilder.create(), PartPose.ZERO);
      base.addOrReplaceChild("shoulders", CubeListBuilder.create().texOffs(0, 16).addBox(-10.0F, 3.9F, -0.5F, 20.0F, 3.0F, 3.0F, def), PartPose.ZERO);
      PartDefinition ribcage = base.addOrReplaceChild(
         "ribcage",
         CubeListBuilder.create()
            .texOffs(0, 22)
            .addBox(0.0F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F, def)
            .texOffs(24, 22)
            .addBox(-4.0F, 1.5F, 0.5F, 11.0F, 2.0F, 2.0F, def)
            .texOffs(24, 22)
            .addBox(-4.0F, 4.5F, 0.5F, 11.0F, 2.0F, 2.0F, def)
            .texOffs(24, 22)
            .addBox(-4.0F, 7.5F, 0.5F, 11.0F, 2.0F, 2.0F, def),
         PartPose.offsetAndRotation(-2.0F, 6.9F, -0.5F, 0.20420352F, 0.0F, 0.0F)
      );
      if (hasRibcageExtension) {
         PartDefinition ribcageExtension = ribcage.addOrReplaceChild(
            "ribcageExtension",
            CubeListBuilder.create()
               .texOffs(128, 40)
               .addBox(-5.5F, -2.0F, -4.0F, 2.0F, 2.0F, 8.0F, def)
               .texOffs(128, 40)
               .addBox(-5.5F, -5.0F, -4.0F, 2.0F, 2.0F, 8.0F, def)
               .texOffs(128, 40)
               .addBox(-5.5F, -8.0F, -4.0F, 2.0F, 2.0F, 8.0F, def)
               .texOffs(128, 40)
               .addBox(3.5F, -8.0F, -4.0F, 2.0F, 2.0F, 8.0F, def)
               .texOffs(128, 40)
               .addBox(3.5F, -5.0F, -4.0F, 2.0F, 2.0F, 8.0F, def)
               .texOffs(128, 40)
               .addBox(3.5F, -2.0F, -4.0F, 2.0F, 2.0F, 8.0F, def)
               .texOffs(140, 44)
               .addBox(2.5F, -2.0F, -4.0F, 1.0F, 2.0F, 2.0F, def)
               .texOffs(140, 44)
               .addBox(2.5F, -5.0F, -4.0F, 1.0F, 2.0F, 2.0F, def)
               .texOffs(140, 44)
               .addBox(2.5F, -8.0F, -4.0F, 1.0F, 2.0F, 2.0F, def)
               .texOffs(140, 44)
               .addBox(-3.5F, -8.0F, -4.0F, 1.0F, 2.0F, 2.0F, def)
               .texOffs(140, 44)
               .addBox(-3.5F, -5.0F, -4.0F, 1.0F, 2.0F, 2.0F, def)
               .texOffs(140, 44)
               .addBox(-3.5F, -2.0F, -4.0F, 1.0F, 2.0F, 2.0F, def),
            PartPose.offset(1.5F, 9.5F, -3.5F)
         );
         ribcageExtension.addOrReplaceChild(
            "block",
            CubeListBuilder.create().texOffs(48, 0).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 8.0F, 8.0F, def.extend(0.001F), 0.5F, 0.5F),
            PartPose.ZERO
         );
      }

      if (hasTail) {
         base.addOrReplaceChild(
            "tail",
            CubeListBuilder.create().texOffs(12, 22).addBox(0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F, def),
            PartPose.offsetAndRotation(-2.0F, 6.9F + Mth.cos(0.20420352F) * 10.0F, -0.5F + Mth.sin(0.20420352F) * 10.0F, 0.83252203F, 0.0F, 0.0F)
         );
      }

      if (hasCenterHead) {
         base.addOrReplaceChild("center_head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, def), PartPose.ZERO);
      }

      CubeListBuilder sideHeads = CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F, def);
      base.addOrReplaceChild("right_head", sideHeads, PartPose.offset(-8.0F, 4.0F, 0.0F));
      base.addOrReplaceChild("left_head", sideHeads, PartPose.offset(10.0F, 4.0F, 0.0F));
      return mesh;
   }

   @Override
   protected void configureHeads(ModelPart root, float scale) {
   }

   @Override
   protected void configureTentacles(ModelPart root) {
   }

   @Override
   public void setupAnimations(T entity, float partialTicks, float tickCount, float yRot, float xRot) {
      super.setupAnimations(entity, partialTicks, tickCount, yRot, xRot);
      float f = Mth.cos(tickCount * 0.1F);
      this.ribcage.xRot = (0.065F + 0.05F * f) * (float) Math.PI;
      this.tail.setPos(-2.0F, 6.9F + Mth.cos(this.ribcage.xRot) * 10.0F, -0.5F + Mth.sin(this.ribcage.xRot) * 10.0F);
      this.tail.xRot = (0.265F + 0.1F * f) * (float) Math.PI;
      this.centerHead.yRot = yRot * (float) (Math.PI / 180.0);
      this.centerHead.xRot = xRot * (float) (Math.PI / 180.0);
      setupHeadRotation(entity, this.leftHead, 1, partialTicks);
      setupHeadRotation(entity, this.rightHead, 2, partialTicks);
   }

   @Override
   protected void renderExtra(PoseStack stack, VertexConsumer consumer, int packedLight, int overlayTexture, float r, float g, float b, float a) {
      this.base.render(stack, consumer, packedLight, overlayTexture, r, g, b, a);
   }

   public static void setupHeadRotation(WitherStormEntity storm, ModelPart headModel, int head, float partialTick) {
      headModel.yRot = (
            Mth.lerp(partialTick, storm.getHeadYRotO(head), storm.getHeadYRot(head)) - Mth.lerp(partialTick, storm.yBodyRotO, storm.yBodyRot)
         )
         * (float) (Math.PI / 180.0);
      headModel.xRot = Mth.lerp(partialTick, storm.getHeadXRotO(head), storm.getHeadXRot(head)) * (float) (Math.PI / 180.0);
   }
}
