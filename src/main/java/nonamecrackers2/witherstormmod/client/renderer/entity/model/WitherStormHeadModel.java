package nonamecrackers2.witherstormmod.client.renderer.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.HeadModel;
import nonamecrackers2.witherstormmod.common.entity.WitherStormHeadEntity;

public class WitherStormHeadModel extends EntityModel<WitherStormHeadEntity> {
   private final HeadModel<WitherStormHeadEntity> head;

   public WitherStormHeadModel(ModelPart root) {
      this.head = new HeadModel(root, 3.0F);
      this.head.tractorBeamDistance = 20.0F;
      this.head.tractorBeamStartSize = 0.1F;
      this.head.tractorBeamEndSize = 2.0F;
      this.head.tractorBeamXOffset = 0.0F;
      this.head.tractorBeamYOffset = 8.0F;
      this.head.tractorBeamZOffset = 0.0F;
      this.head.pivotOffsetX = -4.0F;
      this.head.pivotOffsetY = 0.325F;
      this.head.pivotOffsetZ = 0.0F;
   }

   public static LayerDefinition createLayerDefinition() {
      MeshDefinition mesh = new MeshDefinition();
      HeadModel.populateDefinition(mesh.getRoot());
      return LayerDefinition.create(mesh, 160, 160);
   }

   public void setupAnim(WitherStormHeadEntity entity, float walkAnimPos, float walkAnimSpeed, float bob, float yRot, float xRot) {
      this.head.setupAnimations(entity, Minecraft.getInstance().getPartialTick(), bob, yRot, xRot, 0);
   }

   public void renderToBuffer(
      PoseStack stack, VertexConsumer builder, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_
   ) {
      stack.pushPose();
      this.head.scale(stack);
      this.head.root().render(stack, builder, p_225598_3_, p_225598_4_);
      stack.popPose();
   }

   public HeadModel<WitherStormHeadEntity> getHead() {
      return this.head;
   }
}
