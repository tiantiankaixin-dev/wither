package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public abstract class SingleHeadWitherStormModel<T extends WitherStormEntity> extends AbstractWitherStormModel<T> {
   protected HeadModel<T> primaryHead;

   public SingleHeadWitherStormModel(ModelPart root, float scale) {
      super(root, scale);
   }

   protected static MeshDefinition createMesh(PartPose headPos) {
      MeshDefinition mesh = AbstractWitherStormModel.createMesh();
      PartDefinition root = mesh.getRoot();
      PartDefinition heads = root.getChild("heads");
      HeadModel.populateDefinition(heads.addOrReplaceChild(HEADS[0], CubeListBuilder.create(), headPos));
      return mesh;
   }

   @Override
   protected void configureHeads(ModelPart root, float scale) {
      this.primaryHead = new HeadModel(root.getChild(HEADS[0]), scale);
      this.primaryHead.tractorBeamDistance = 90.0F;
      this.primaryHead.tractorBeamStartSize = 0.1F;
      this.primaryHead.tractorBeamEndSize = 5.0F;
      this.primaryHead.tractorBeamXOffset = 0.0F;
      this.primaryHead.tractorBeamYOffset = 34.0F;
      this.primaryHead.tractorBeamZOffset = 0.0F;
      this.primaryHead.pivotOffsetX = -19.0F;
      this.primaryHead.pivotOffsetY = 1.85F;
      this.heads.put(0, this.primaryHead);
   }
}
