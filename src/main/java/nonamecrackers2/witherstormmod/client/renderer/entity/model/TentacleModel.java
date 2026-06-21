package nonamecrackers2.witherstormmod.client.renderer.entity.model;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.common.entity.TentacleEntity;
import nonamecrackers2.witherstormmod.common.entity.part.TentaclePartEntity;

public class TentacleModel extends EntityModel<TentacleEntity> {
   private final List<ModelPart> segments;
   private final ModelPart base;
   private final ModelPart segment;
   private final ModelPart segment2;
   private final ModelPart segment3;
   private final ModelPart segment4;
   private final ModelPart segment5;

   public TentacleModel(ModelPart root) {
      this.base = root;
      this.segment = this.base.getChild("segment");
      this.segment2 = this.segment.getChild("segment2");
      this.segment3 = this.segment2.getChild("segment3");
      this.segment4 = this.segment3.getChild("segment4");
      this.segment5 = this.segment4.getChild("segment5");
      this.segments = Lists.newArrayList(new ModelPart[]{this.segment, this.segment2, this.segment3, this.segment4, this.segment5});
   }

   public static LayerDefinition createLayerDefinition() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition base = mesh.getRoot();
      PartDefinition segment = base.addOrReplaceChild(
         "segment", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, false), PartPose.ZERO
      );
      PartDefinition segment2 = segment.addOrReplaceChild(
         "segment2",
         CubeListBuilder.create().texOffs(0, 24).addBox(-5.0F, -16.0F, -5.0F, 10.0F, 16.0F, 10.0F, false),
         PartPose.offset(0.0F, -12.0F, 0.0F)
      );
      PartDefinition segment3 = segment2.addOrReplaceChild(
         "segment3",
         CubeListBuilder.create().texOffs(40, 16).addBox(-4.0F, -20.0F, -4.0F, 8.0F, 20.0F, 8.0F, false),
         PartPose.offset(0.0F, -16.0F, 0.0F)
      );
      PartDefinition segment4 = segment3.addOrReplaceChild(
         "segment4",
         CubeListBuilder.create().texOffs(34, 44).addBox(-3.0F, -24.0F, -3.0F, 6.0F, 24.0F, 6.0F, false),
         PartPose.offset(0.0F, -20.0F, 0.0F)
      );
      segment4.addOrReplaceChild(
         "segment5",
         CubeListBuilder.create().texOffs(0, 50).addBox(-2.0F, -24.0F, -2.0F, 4.0F, 24.0F, 4.0F, false),
         PartPose.offset(0.0F, -24.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 128, 128);
   }

   public void setupAnim(TentacleEntity entity, float partialTicks, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
      float offset = 90.0F;
      TentaclePartEntity<TentacleEntity> part = entity.getTentacle();
      List<TentaclePartEntity<TentacleEntity>> chained = Lists.newArrayList(new TentaclePartEntity[]{part});
      chained.addAll(part.getChained());
      TentaclePartEntity<TentacleEntity> previous = null;

      for (int j = 0; j < this.segments.size(); j++) {
         TentaclePartEntity<TentacleEntity> chain = chained.get(j);
         float prevXRot = 0.0F;
         float prevYRot = 0.0F;
         if (previous != null) {
            prevXRot = Mth.lerp(partialTicks, previous.xRotO, previous.getXRot()) + offset;
            prevYRot = Mth.lerp(partialTicks, previous.yRotO, previous.getYRot());
         }

         ModelPart segment = this.segments.get(j);
         segment.xRot = -(Mth.lerp(partialTicks, chain.xRotO, chain.getXRot()) - prevXRot + offset) * (float) (Math.PI / 180.0);
         segment.yRot = (Mth.lerp(partialTicks, chain.yRotO, chain.getYRot()) - prevYRot) * (float) (Math.PI / 180.0);
         previous = chain;
      }
   }

   @Override
   public void renderToBuffer(PoseStack stack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
      this.base.render(stack, buffer, packedLight, packedOverlay);
   }

   public void renderToBuffer(PoseStack stack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.renderToBuffer(stack, buffer, packedLight, packedOverlay, -1);
   }
}
