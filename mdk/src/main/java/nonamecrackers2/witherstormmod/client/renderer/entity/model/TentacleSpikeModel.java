package nonamecrackers2.witherstormmod.client.renderer.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import nonamecrackers2.witherstormmod.common.entity.TentacleSpike;

public class TentacleSpikeModel<T extends TentacleSpike> extends HierarchicalModel<T> {
   private final ModelPart root;
   private final ModelPart base;
   private final ModelPart middle;
   private final ModelPart end;

   public TentacleSpikeModel(ModelPart root) {
      this.root = root;
      this.base = root.getChild("base");
      this.middle = this.base.getChild("middle");
      this.end = this.middle.getChild("end");
   }

   public static LayerDefinition createLayerDefinition() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition base = partdefinition.addOrReplaceChild(
         "base", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -7.0F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.ZERO
      );
      PartDefinition middle = base.addOrReplaceChild(
         "middle",
         CubeListBuilder.create().texOffs(0, 13).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -7.0F, 0.0F)
      );
      middle.addOrReplaceChild(
         "end",
         CubeListBuilder.create().texOffs(16, 13).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -9.0F, 0.0F)
      );
      return LayerDefinition.create(meshdefinition, 32, 32);
   }

   public void setupAnim(T entity, float anim, float f, float f1, float yRot, float xRot) {
      float sway = anim * (3.0F - anim) + RandomSource.create((long)entity.getId()).nextFloat() * 1000.0F;
      float zSway = Mth.cos(sway) * 0.1F;
      float xSway = Mth.sin(sway) * 0.1F;
      this.base.zRot = zSway;
      this.base.xRot = xSway;
      this.middle.zRot = zSway;
      this.middle.xRot = xSway;
      this.end.zRot = zSway;
      this.end.xRot = xSway;
   }

   public ModelPart root() {
      return this.root;
   }
}
