package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public abstract class ThreeHeadedWitherStormModel<T extends WitherStormEntity> extends AbstractWitherStormModel<T> {
   protected HeadModel<T> rightHead;
   protected HeadModel<T> middleHead;
   protected HeadModel<T> leftHead;

   public ThreeHeadedWitherStormModel(ModelPart root, float headScale) {
      super(root, headScale);
   }

   protected static MeshDefinition createMesh(PartPose[] headPositions) {
      MeshDefinition mesh = AbstractWitherStormModel.createMesh();
      PartDefinition root = mesh.getRoot();
      PartDefinition heads = root.getChild("heads");
      HeadModel.populateDefinition(heads.addOrReplaceChild(HEADS[0], CubeListBuilder.create(), headPositions[0]));
      HeadModel.populateDefinition(heads.addOrReplaceChild(HEADS[1], CubeListBuilder.create(), headPositions[1]));
      HeadModel.populateDefinition(heads.addOrReplaceChild(HEADS[2], CubeListBuilder.create(), headPositions[2]));
      return mesh;
   }

   @Override
   protected void configureHeads(ModelPart root, float scale) {
      this.rightHead = new HeadModel(root.getChild(HEADS[0]), scale);
      this.middleHead = new HeadModel(root.getChild(HEADS[1]), scale);
      this.leftHead = new HeadModel(root.getChild(HEADS[2]), scale);
      this.heads.put(2, this.rightHead);
      this.heads.put(0, this.middleHead);
      this.heads.put(1, this.leftHead);
   }
}
