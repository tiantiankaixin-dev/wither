package nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.RandomSource;
import nonamecrackers2.crackerslib.common.compat.CompatHelper;
import nonamecrackers2.witherstormmod.client.instancing.RenderBufferer;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.mixin.IMixinModelPart;

public abstract class AbstractWitherStormModel<T extends WitherStormEntity> {
   public static final String MASS = "mass";
   public static final String LOWRESMASS = "lowResMass";
   public static final String TENTACLES = "tentacles";
   public static final String HEADSROOT = "heads";
   public static final String[] HEADS = new String[]{"head0", "head1", "head2"};
   protected ModelPart body;
   protected ModelPart lowResBody;
   protected TentacleModel[] tentacles = new TentacleModel[0];
   public final Int2ObjectMap<HeadModel<T>> heads = new Int2ObjectOpenHashMap();
   public final float headScale;

   protected AbstractWitherStormModel(ModelPart root, float headScale) {
      this.body = root.getChild("mass");
      this.lowResBody = root.getChild("lowResMass");
      this.configureTentacles(root.getChild("tentacles"));
      this.configureHeads(root.getChild("heads"), headScale);
      this.headScale = headScale;
   }

   protected abstract void configureTentacles(ModelPart var1);

   protected abstract void configureHeads(ModelPart var1, float var2);

   protected static MeshDefinition createMesh() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild("mass", CubeListBuilder.create(), PartPose.ZERO);
      root.addOrReplaceChild("lowResMass", CubeListBuilder.create(), PartPose.ZERO);
      root.addOrReplaceChild("tentacles", CubeListBuilder.create(), PartPose.ZERO);
      root.addOrReplaceChild("heads", CubeListBuilder.create(), PartPose.ZERO);
      return mesh;
   }

   public void setupAnimations(T entity, float partialTicks, float tickCount, float yRot, float xRot) {
      ObjectIterator i = this.heads.int2ObjectEntrySet().iterator();

      while (i.hasNext()) {
         Entry<HeadModel<T>> entry = (Entry<HeadModel<T>>)i.next();
         ((HeadModel)entry.getValue()).setupAnimations(entity, partialTicks, tickCount, yRot, xRot, entry.getIntKey());
      }

      for (int ix = 0; ix < this.tentacles.length; ix++) {
         this.tentacles[ix].setupAnimations(entity.getTentacleAnimation(partialTicks), partialTicks);
      }
   }

   public void renderHeads(T entity, PoseStack stack, VertexConsumer consumer, int overlayTexture, int packedLight, float r, float g, float b, float a) {
      this.renderHeads(i -> true, entity, stack, consumer, overlayTexture, packedLight, r, g, b, a);
   }

   public void renderHeads(
      Predicate<Integer> canRender, T entity, PoseStack stack, VertexConsumer consumer, int overlayTexture, int packedLight, float r, float g, float b, float a
   ) {
      ObjectIterator var11 = this.heads.int2ObjectEntrySet().iterator();

      while (var11.hasNext()) {
         Entry<HeadModel<T>> entry = (Entry<HeadModel<T>>)var11.next();
         if (canRender.test(entry.getIntKey()) && (!entity.areOtherHeadsDisabled() || entity.areOtherHeadsDisabled() && entry.getIntKey() == 0)) {
            this.renderHead(entry.getIntKey(), entity, stack, consumer, overlayTexture, packedLight, r, g, b, a);
         }
      }
   }

   public void renderHead(int head, T entity, PoseStack stack, VertexConsumer consumer, int overlayTexture, int packedLight, float r, float g, float b, float a) {
      HeadModel<T> headModel = (HeadModel<T>)this.heads.get(head);
      stack.pushPose();
      headModel.scale(stack);
      int hurtDir = entity.getHeadManager().getHead(head).getHeadHurtDuration();
      int overlay = hurtDir > 0 ? 3 : overlayTexture;
      if (!entity.areOtherHeadsDisabled() || head == 0) {
         headModel.root().render(stack, consumer, packedLight, overlay, r, g, b, a);
      }

      stack.popPose();
   }

   public void render(
      T entity,
      PoseStack stack,
      MultiBufferSource source,
      RenderType type,
      @Nullable RenderType emissiveType,
      @Nullable RenderType massEmissiveType,
      @Nullable RenderType hurtOverlayType,
      int packedLight,
      int overlayTexture,
      float r,
      float g,
      float b,
      float alpha
   ) {
      stack.pushPose();
      VertexConsumer consumer = source.getBuffer(type);
      this.renderHeads(entity, stack, consumer, overlayTexture, packedLight, r, g, b, alpha);
      stack.pushPose();
      this.transformForMirrored(stack, entity.isMirrored());

      for (int i = 0; i < this.tentacles.length; i++) {
         stack.pushPose();
         this.scaleTentacles(stack, this.tentacles[i]);
         this.tentacles[i].tentacle.render(stack, consumer, packedLight, overlayTexture, 1.0F, 1.0F, 1.0F, alpha);
         stack.popPose();
      }

      this.renderExtra(stack, consumer, packedLight, overlayTexture, r, g, b, alpha);
      stack.popPose();
      if (hurtOverlayType != null) {
         this.renderHeads(i -> entity.isHeadInjured(i), entity, stack, source.getBuffer(hurtOverlayType), overlayTexture, packedLight, r, g, b, alpha);
      }

      if ((!entity.onGround() || !entity.isDeadOrPlayingDead()) && !entity.shouldFlicker() && emissiveType != null) {
         VertexConsumer emissive = source.getBuffer(emissiveType);
         this.renderHeads(i -> !entity.isHeadInjured(i), entity, stack, emissive, overlayTexture, packedLight, r, g, b, alpha);
      }

      stack.pushPose();
      this.transformForMirrored(stack, entity.isMirrored());
      this.scaleMass(stack);
      RenderBufferer.pushCullFaces();
      if (entity.isMirrored()) {
         RenderBufferer.pushFlipFaces();
      }

      boolean flag = RenderBufferer.shouldUse();
      if (flag && entity.isDeadOrDying() && CompatHelper.areShadersRunning()) {
         flag = false;
      }

      RenderBufferer.buildAndOrRender(
         this + ", " + type + ", " + this.lowResModelsEnabled(entity),
         type,
         () -> false,
         this.getMassModel(entity)::render,
         stack,
         packedLight,
         overlayTexture,
         1.0F,
         1.0F,
         1.0F,
         alpha,
         flag
      );
      if (flag && emissiveType != null) {
         RenderBufferer.pushNoFog();
         RenderBufferer.buildAndOrRender(
            this + ", " + massEmissiveType + ", " + this.lowResModelsEnabled(entity) + ", emissive",
            massEmissiveType,
            () -> false,
            this.getMassModel(entity)::render,
            stack,
            packedLight,
            overlayTexture,
            1.0F,
            1.0F,
            1.0F,
            alpha,
            true
         );
      }

      RenderBufferer.popFlipFaces();
      RenderBufferer.popCullFaces();
      if (!entity.isDeadOrDying()) {
         this.renderMassDecal(entity, stack, source, packedLight, overlayTexture, r, g, b, alpha);
      }

      stack.popPose();
      stack.popPose();
   }

   protected void transformForMirrored(PoseStack stack, boolean mirrored) {
      stack.scale(mirrored ? -1.0F : 1.0F, 1.0F, 1.0F);
   }

   protected void renderExtra(PoseStack stack, VertexConsumer consumer, int packedLight, int overlayTexture, float r, float g, float b, float a) {
   }

   public ModelPart getMassModel(T entity) {
      if (this.lowResMassPresent()) {
         return this.lowResModelsEnabled(entity) ? this.lowResBody : this.body;
      } else {
         return this.body;
      }
   }

   public boolean lowResModelsEnabled(T entity) {
      return this.lowResMassPresent()
         && (
            (Boolean)WitherStormModConfig.CLIENT.lowResModels.get()
               || (Boolean)WitherStormModConfig.CLIENT.witherStormLOD.get() && entity.isOnDistantRenderer()
         );
   }

   public void scaleMass(PoseStack stack) {
      stack.scale(10.0F, 10.0F, 10.0F);
   }

   public void scaleTentacles(PoseStack stack, TentacleModel model) {
      stack.scale(model.scale, model.scale, model.scale);
   }

   public boolean massPresent() {
      return this.body != null;
   }

   public boolean lowResMassPresent() {
      return !this.lowResBody.isEmpty();
   }

   public ModelPart getRandomPart(T entity, RandomSource random) {
      ModelPart model = this.getMassModel(entity);
      List<ModelPart> children = new ArrayList<>((java.util.Collection<ModelPart>)((IMixinModelPart)(Object)model).getChildren().values());
      if (!children.isEmpty()) {
         ModelPart potential = children.get(random.nextInt(children.size()));
         if (!potential.isEmpty()) {
            model = potential;
         }
      }

      return model;
   }

   public void renderMassDecal(T entity, PoseStack stack, MultiBufferSource buffer, int packedLight, int overlayCoords, float r, float g, float b, float a) {
   }
}
