package nonamecrackers2.witherstormmod.client.renderer.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.WitherStormMod;

public class SantaHatModel extends Model {
   public static final ResourceLocation TEXTURE = WitherStormMod.id("textures/misc/santa_hat.png");
   private final ModelPart root;

   public SantaHatModel(ModelPart root) {
      super(RenderType::entityCutoutNoCull);
      this.root = root;
   }

   public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int packedLight, int overlayTexture, float r, float g, float b, float alpha) {
      this.root.render(stack, consumer, packedLight, overlayTexture, r, g, b, alpha);
   }

   public static LayerDefinition createLayerDefinition() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition base = partdefinition.addOrReplaceChild(
         "base",
         CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -3.0F, -6.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, -4.0F, 6.1F)
      );
      PartDefinition segment1 = base.addOrReplaceChild(
         "segment1",
         CubeListBuilder.create().texOffs(0, 15).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.1222F)
      );
      PartDefinition segment2 = segment1.addOrReplaceChild(
         "segment2",
         CubeListBuilder.create().texOffs(0, 29).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.6196F)
      );
      PartDefinition segment3 = segment2.addOrReplaceChild(
         "segment3",
         CubeListBuilder.create().texOffs(30, 15).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.3229F)
      );
      PartDefinition segment4 = segment3.addOrReplaceChild(
         "segment4",
         CubeListBuilder.create().texOffs(24, 29).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.25F, -3.0F, 0.0F, 0.0F, 0.0F, -0.4887F)
      );
      segment4.addOrReplaceChild(
         "tip",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
         PartPose.offsetAndRotation(0.0F, -1.7F, 0.0F, 0.0F, 0.0F, -0.5672F)
      );
      return LayerDefinition.create(meshdefinition, 64, 64);
   }
}
