package nonamecrackers2.witherstormmod.client.renderer.entity.model.commandblock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.List;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.util.StructureAnimationHelper;

public class RibcageModel extends EntityModel<CommandBlockEntity> {
   private final Int2ObjectMap<RibModel> ribs;
   private final RibModel rib;
   private final RibModel rib2;
   private final RibModel rib3;
   private final RibModel rib4;
   private final RibModel rib5;
   private final RibModel rib6;

   public RibcageModel(ModelPart root) {
      Int2ObjectMap<RibModel> ribs = new Int2ObjectOpenHashMap();
      this.rib = new RibModel(root.getChild("rib"));
      this.rib2 = new RibModel(root.getChild("rib2"));
      this.rib3 = new RibModel(root.getChild("rib3"));
      this.rib4 = new RibModel(root.getChild("rib4"));
      this.rib5 = new RibModel(root.getChild("rib5"));
      this.rib6 = new RibModel(root.getChild("rib6"));
      ribs.put(0, this.rib);
      ribs.put(1, this.rib2);
      ribs.put(2, this.rib3);
      ribs.put(3, this.rib4);
      ribs.put(4, this.rib5);
      ribs.put(5, this.rib6);
      this.ribs = ribs;
   }

   public static LayerDefinition createLayerDefinition() {
      MeshDefinition definition = new MeshDefinition();
      PartDefinition root = definition.getRoot();
      RibModel.populateDefinition(root.addOrReplaceChild("rib", CubeListBuilder.create(), PartPose.ZERO), PartPose.offset(0.0F, 24.0F, 68.0F), false);
      RibModel.populateDefinition(root.addOrReplaceChild("rib2", CubeListBuilder.create(), PartPose.ZERO), PartPose.offset(0.0F, 24.0F, -68.0F), false);
      RibModel.populateDefinition(root.addOrReplaceChild("rib3", CubeListBuilder.create(), PartPose.ZERO), PartPose.offset(42.0F, 24.0F, -57.0F), false);
      RibModel.populateDefinition(root.addOrReplaceChild("rib4", CubeListBuilder.create(), PartPose.ZERO), PartPose.offset(42.0F, 24.0F, 57.0F), false);
      RibModel.populateDefinition(root.addOrReplaceChild("rib5", CubeListBuilder.create(), PartPose.ZERO), PartPose.offset(-42.0F, 24.0F, 57.0F), true);
      RibModel.populateDefinition(root.addOrReplaceChild("rib6", CubeListBuilder.create(), PartPose.ZERO), PartPose.offset(-42.0F, 24.0F, -57.0F), true);
      return LayerDefinition.create(definition, 128, 128);
   }

   public void renderToBuffer(PoseStack stack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
      stack.pushPose();
      stack.translate(0.0, -1.5, 0.0);
      ObjectIterator var9 = this.ribs.int2ObjectEntrySet().iterator();

      while (var9.hasNext()) {
         Entry<RibModel> entry = (Entry<RibModel>)var9.next();
         ((RibModel)entry.getValue()).rib.render(stack, buffer, packedLight, packedOverlay, color);
      }

      stack.popPose();
   }

   public void setupAnim(CommandBlockEntity entity, float animation, float partialTicks, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
      ObjectIterator var7 = this.ribs.int2ObjectEntrySet().iterator();

      while (var7.hasNext()) {
         Entry<RibModel> entry = (Entry<RibModel>)var7.next();
         RibModel model = (RibModel)entry.getValue();
         int key = entry.getIntKey();
         List<StructureAnimationHelper> structure = entity.getRibStructure();
         if (key < structure.size()) {
            StructureAnimationHelper helper = structure.get(key);
            model.rib.xRot = (float)Math.toRadians((double)helper.getBaseXRot(partialTicks));
            model.rib.yRot = (float)Math.toRadians((double)helper.getBaseYRot(partialTicks));
            model.segmentOne.xRot = (float)Math.toRadians((double)(helper.getXRot(partialTicks) * 0.4F));
            model.segmentOne.yRot = (float)Math.toRadians((double)(helper.getYRot(partialTicks) * 0.4F));
            model.segmentTwo.xRot = (float)Math.toRadians((double)(helper.getXRot(partialTicks) * 0.4F));
            model.segmentTwo.yRot = (float)Math.toRadians((double)(helper.getYRot(partialTicks) * 0.4F));
            model.segmentThree.xRot = (float)Math.toRadians((double)(helper.getXRot(partialTicks) * 0.8F));
            model.segmentThree.yRot = (float)Math.toRadians((double)(helper.getYRot(partialTicks) * 0.8F));
            model.segmentFour.xRot = (float)Math.toRadians((double)helper.getXRot(partialTicks));
            model.segmentFour.yRot = (float)Math.toRadians((double)helper.getYRot(partialTicks));
         }
      }
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
