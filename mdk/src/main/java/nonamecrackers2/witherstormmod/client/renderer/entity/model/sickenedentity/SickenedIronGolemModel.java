package nonamecrackers2.witherstormmod.client.renderer.entity.model.sickenedentity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.common.entity.SickenedIronGolem;

public class SickenedIronGolemModel<T extends SickenedIronGolem> extends HierarchicalModel<T> {
   private final ModelPart root;
   private final ModelPart head;
   private final ModelPart rightArm;
   private final ModelPart leftArm;
   private final ModelPart rightLeg;
   private final ModelPart leftLeg;

   public SickenedIronGolemModel(ModelPart root) {
      this.root = root;
      this.head = root.getChild("head");
      this.rightArm = root.getChild("right_arm");
      this.leftArm = root.getChild("left_arm");
      this.rightLeg = root.getChild("right_leg");
      this.leftLeg = root.getChild("left_leg");
   }

   public ModelPart root() {
      return this.root;
   }

   public static LayerDefinition createBodyLayer() {
      return IronGolemModel.createBodyLayer();
   }

   public void setupAnim(T entity, float walkAnimation, float animationSpeed, float bob, float yRot, float xRot) {
      this.head.yRot = yRot * (float) (Math.PI / 180.0);
      this.head.xRot = xRot * (float) (Math.PI / 180.0);
      this.rightLeg.xRot = -1.5F * Mth.triangleWave(walkAnimation, 13.0F) * animationSpeed;
      this.leftLeg.xRot = 1.5F * Mth.triangleWave(walkAnimation, 13.0F) * animationSpeed;
      this.rightLeg.yRot = 0.0F;
      this.leftLeg.yRot = 0.0F;
   }

   public void prepareMobModel(T entity, float walkAnimation, float animationSpeed, float partialTicks) {
      int i = entity.getAttackAnimationTick();
      if (i > 0) {
         this.rightArm.xRot = -2.0F + 1.5F * Mth.triangleWave((float)i - partialTicks, 10.0F);
         this.leftArm.xRot = -2.0F + 1.5F * Mth.triangleWave((float)i - partialTicks, 10.0F);
      } else {
         this.rightArm.xRot = (-0.2F + 1.5F * Mth.triangleWave(walkAnimation, 13.0F)) * animationSpeed;
         this.leftArm.xRot = (-0.2F - 1.5F * Mth.triangleWave(walkAnimation, 13.0F)) * animationSpeed;
      }
   }
}
