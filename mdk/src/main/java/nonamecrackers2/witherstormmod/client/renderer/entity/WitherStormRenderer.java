package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Random;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.AbstractWitherStormModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormCommandBlockModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormDestroyerModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormDevourerModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormDismantledModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormEvolvedDestroyerModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormEvolvedDevourerModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormGrowingHunchbackModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormHunchback1_1Model;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormHunchback1_2Model;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormHunchback2_1Model;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormHunchback3_1Model;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormHunchback3_2Model;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormHunchbackModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormIntermediateDevourerModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormIntermediateEvolvedDestroyerModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormIntermediateEvolvedDevourerModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormPregnantHunchbackModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormTornEvolvedDevourerModel;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import org.joml.Matrix4f;

public class WitherStormRenderer extends AbstractWitherStormRenderer<WitherStormEntity, AbstractWitherStormModel<WitherStormEntity>> {
   private final WitherStormCommandBlockModel<WitherStormEntity> commandBlockModel;
   private final WitherStormDestroyerModel<WitherStormEntity> destroyerModel;
   private final WitherStormIntermediateEvolvedDestroyerModel<WitherStormEntity> intermediateEvolvedDestroyer;
   private final WitherStormEvolvedDestroyerModel<WitherStormEntity> evolvedDestroyerModel;
   private final WitherStormIntermediateDevourerModel<WitherStormEntity> intermediateDevourerModel;
   private final WitherStormDevourerModel<WitherStormEntity> devourerModel;
   private final WitherStormIntermediateEvolvedDevourerModel<WitherStormEntity> intermediateEvolvedDevourerModel;
   private final WitherStormEvolvedDevourerModel<WitherStormEntity> evolvedDevourerModel;
   private final WitherStormDismantledModel<WitherStormEntity> dismantledModel;
   private final WitherStormTornEvolvedDevourerModel<WitherStormEntity> tornModel;
   private final WitherStormHunchbackModel<WitherStormEntity> hunchbackP1;
   private final WitherStormHunchback1_1Model<WitherStormEntity> hunchbackP125;
   private final WitherStormHunchback1_2Model<WitherStormEntity> hunchbackP15;
   private final WitherStormGrowingHunchbackModel<WitherStormEntity> hunchbackP2;
   private final WitherStormHunchback2_1Model<WitherStormEntity> hunchbackP25;
   private final WitherStormPregnantHunchbackModel<WitherStormEntity> hunchbackP3;
   private final WitherStormHunchback3_1Model<WitherStormEntity> hunchbackP31;
   private final WitherStormHunchback3_2Model<WitherStormEntity> hunchbackP32;

   public WitherStormRenderer(Context context) {
      this(context, new WitherStormCommandBlockModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_0)));
   }

   private WitherStormRenderer(Context context, WitherStormCommandBlockModel<WitherStormEntity> base) {
      super(context, base);
      this.commandBlockModel = base;
      this.hunchbackP1 = new WitherStormHunchbackModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_1));
      this.hunchbackP125 = new WitherStormHunchback1_1Model<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_1_1));
      this.hunchbackP15 = new WitherStormHunchback1_2Model<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_1_2));
      this.hunchbackP2 = new WitherStormGrowingHunchbackModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_2));
      this.hunchbackP25 = new WitherStormHunchback2_1Model<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_2_1));
      this.hunchbackP3 = new WitherStormPregnantHunchbackModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_3));
      this.hunchbackP31 = new WitherStormHunchback3_1Model<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_3_1));
      this.hunchbackP32 = new WitherStormHunchback3_2Model<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_3_2));
      this.destroyerModel = new WitherStormDestroyerModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_4));
      this.intermediateEvolvedDestroyer = new WitherStormIntermediateEvolvedDestroyerModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_4_5));
      this.evolvedDestroyerModel = new WitherStormEvolvedDestroyerModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_5));
      this.intermediateDevourerModel = new WitherStormIntermediateDevourerModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_5_5));
      this.devourerModel = new WitherStormDevourerModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_6));
      this.intermediateEvolvedDevourerModel = new WitherStormIntermediateEvolvedDevourerModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_6_5));
      this.evolvedDevourerModel = new WitherStormEvolvedDevourerModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_7));
      this.dismantledModel = new WitherStormDismantledModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_DISMANTLED));
      this.tornModel = new WitherStormTornEvolvedDevourerModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_TORN));
   }

   @Override
   public void render(WitherStormEntity entity, float yRot, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
      super.render(entity, yRot, partialTicks, stack, buffer, packedLight);
      if (entity.getDeathTime() > 0) {
         float f1 = ((float)entity.getDeathTime() + partialTicks) / 200.0F;
         float f2 = Math.min(f1 > 1.6F ? (f1 - 1.6F) / 0.2F : 0.0F, 1.0F);
         Random random = new Random(382L);
         VertexConsumer builder = buffer.getBuffer(RenderType.lightning());
         stack.pushPose();
         stack.translate(0.0, (double)entity.getUnmodifiedHeight() / 2.0, 0.0);

         for (int i = 0; (float)i < (f1 + f1 * f1) / 2.0F * 60.0F; i++) {
            stack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F + f1 * 90.0F));
            float f3 = random.nextFloat() * (entity.getUnmodifiedSize() / 1.5F) * 2.5F + 5.0F + f2 * 10.0F;
            float f4 = random.nextFloat() * 10.0F + 1.0F + f2 * 2.0F;
            Matrix4f matrix4f = stack.last().pose();
            int k = (int)(255.0F * (1.0F - f2));
            builder.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(255, 255, 255, k);
            builder.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(255, 255, 255, k);
            builder.addVertex(matrix4f, -((float)(Math.sqrt(3.0) / 2.0)) * f4, f3, -0.5F * f4).setColor(255, 0, 255, 0);
            builder.addVertex(matrix4f, (float)(Math.sqrt(3.0) / 2.0) * f4, f3, -0.5F * f4).setColor(255, 0, 255, 0);
            builder.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(255, 255, 255, k);
            builder.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(255, 255, 255, k);
            builder.addVertex(matrix4f, (float)(Math.sqrt(3.0) / 2.0) * f4, f3, -0.5F * f4).setColor(255, 0, 255, 0);
            builder.addVertex(matrix4f, 0.0F, f3, f4).setColor(255, 0, 255, 0);
            builder.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(255, 255, 255, k);
            builder.addVertex(matrix4f, 0.0F, 0.0F, 0.0F).setColor(255, 255, 255, k);
            builder.addVertex(matrix4f, 0.0F, f3, f4).setColor(255, 0, 255, 0);
            builder.addVertex(matrix4f, -((float)(Math.sqrt(3.0) / 2.0)) * f4, f3, -0.5F * f4).setColor(255, 0, 255, 0);
         }

         stack.popPose();
      }
   }

   @Override
   public AbstractWitherStormModel<WitherStormEntity> fetchModel(WitherStormEntity entity) {
      int phase = entity.getPhase();
      int consumptionAmountForPhase = entity.getConsumptionAmountForPhase(phase);
      int consumedEntities = entity.getConsumedEntities();
      if (phase == 1) {
         if (consumedEntities >= entity.adjustAmountForEvolutionSpeed(250)) {
            return this.hunchbackP15;
         } else if (consumedEntities >= entity.adjustAmountForEvolutionSpeed(150)) {
            return this.hunchbackP125;
         } else {
            return consumptionAmountForPhase >= consumedEntities ? this.hunchbackP1 : null;
         }
      } else if (phase == 2) {
         return (AbstractWitherStormModel<WitherStormEntity>)(consumedEntities >= entity.adjustAmountForEvolutionSpeed(800)
            ? this.hunchbackP25
            : this.hunchbackP2);
      } else if (phase == 3) {
         if (consumedEntities >= entity.adjustAmountForEvolutionSpeed(3500)) {
            return this.hunchbackP32;
         } else {
            return (AbstractWitherStormModel<WitherStormEntity>)(consumedEntities >= entity.adjustAmountForEvolutionSpeed(2350)
               ? this.hunchbackP31
               : this.hunchbackP3);
         }
      } else if (phase == 4) {
         return (AbstractWitherStormModel<WitherStormEntity>)(consumedEntities <= entity.getSubPhaseRequirement(phase)
            ? this.destroyerModel
            : this.intermediateEvolvedDestroyer);
      } else if (phase == 5) {
         if (consumptionAmountForPhase < consumedEntities) {
            return this.devourerModel;
         } else {
            return (AbstractWitherStormModel<WitherStormEntity>)(consumedEntities <= entity.getSubPhaseRequirement(phase)
               ? this.evolvedDestroyerModel
               : this.intermediateDevourerModel);
         }
      } else if (phase == 6) {
         return (AbstractWitherStormModel<WitherStormEntity>)(consumedEntities <= entity.getSubPhaseRequirement(phase)
            ? this.dismantledModel
            : this.intermediateEvolvedDevourerModel);
      } else if (phase == 7) {
         return (AbstractWitherStormModel<WitherStormEntity>)(!entity.isBeingTornApart() ? this.evolvedDevourerModel : this.tornModel);
      } else {
         return this.commandBlockModel;
      }
   }
}
