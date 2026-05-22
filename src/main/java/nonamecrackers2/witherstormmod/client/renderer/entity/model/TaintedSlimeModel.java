package nonamecrackers2.witherstormmod.client.renderer.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import nonamecrackers2.witherstormmod.client.renderer.entity.animation.TaintedSlimeAnimations;
import nonamecrackers2.witherstormmod.common.entity.TaintedSlime;

public class TaintedSlimeModel<T extends TaintedSlime> extends HierarchicalModel<T> {
   private final ModelPart root;

   public TaintedSlimeModel(ModelPart root) {
      this.root = root;
   }

   public static LayerDefinition createLayerDefinition() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));
      bone.addOrReplaceChild(
         "cube_r1",
         CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(-0.1F)),
         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F)
      );
      partdefinition.addOrReplaceChild(
         "bb_main",
         CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      return LayerDefinition.create(meshdefinition, 48, 48);
   }

   public ModelPart root() {
      return this.root;
   }

   public void setupAnim(T entity, float walkAnimation, float animationSpeed, float bob, float yRot, float xRot) {
      this.root().getAllParts().forEach(ModelPart::resetPose);
      this.animate(entity.idle, TaintedSlimeAnimations.MODEL_IDLE, bob);
   }
}
