package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.awt.Color;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.crackerslib.common.compat.CompatHelper;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.instancing.RenderBufferer;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.SantaHatModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.AbstractWitherStormModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.HeadModel;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.witherstorm.impl.WitherStormCommandBlockModel;
import nonamecrackers2.witherstormmod.client.rendertype.UtilRenderTypes;
import nonamecrackers2.witherstormmod.client.resources.WitherStormResourceConfigManager;
import nonamecrackers2.witherstormmod.client.resources.color.ColorSet;
import nonamecrackers2.witherstormmod.client.resources.texture.TextureSet;
import nonamecrackers2.witherstormmod.client.util.SpecialDay;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.MainHead;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.WitherStormHead;
import nonamecrackers2.witherstormmod.common.entity.section.Section;
import nonamecrackers2.witherstormmod.common.util.DebrisCluster;
import nonamecrackers2.witherstormmod.common.util.DebrisRingSettings;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class AbstractWitherStormRenderer<T extends WitherStormEntity, M extends AbstractWitherStormModel<T>> extends EntityRenderer<T> {
   private static final Color MAIN_HEAD_COLOR = new Color(1.0F, 0.6F, 0.0F);
   private static final Color EXTRA_HEAD_COLOR = new Color(1.0F, 1.0F, 0.0F);
   public static final ResourceLocation WITHER_STORM_INVULNERABLE_LOCATION = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/wither_storm/wither_storm_invulnerable.png");
   public static final ResourceLocation WITHER_STORM_LOCATION = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/wither_storm/wither_storm.png");
   public static final ResourceLocation WITHER_STORM_EXPLODING_LOCATION = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/wither_storm/wither_storm_exploding.png");
   public static final ResourceLocation EMISSIVE_DECAL = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/wither_storm/wither_storm_emissive_decal.png");
   public static final ResourceLocation HURT_OVERLAY = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/wither_storm/wither_storm_hurt_overlay.png");
   public static final ResourceLocation PULSE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/wither_storm/wither_storm_pulse.png");
   public static final ResourceLocation DEBRIS_RING = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/wither_storm/debris.png");
   public static final ResourceLocation SHINE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/entity/wither_storm/shine.png");
   public static final ResourceLocation PINK_WITHER_STORM_LOCATION = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/misc/pink_wither_storm.png");
   public static final ResourceLocation WITHER_ARMOR_LOCATION = ResourceLocation.parse("textures/entity/wither/wither_armor.png");
   protected final SantaHatModel santaHat;
   @Nullable
   protected final SpecialDay specialDay;
   protected M model;
   protected WitherStormCommandBlockModel<T> swirlModel;

   public AbstractWitherStormRenderer(Context context, M model) {
      super(context);
      this.model = model;
      this.swirlModel = new WitherStormCommandBlockModel<>(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_ARMOR));
      this.santaHat = new SantaHatModel(context.bakeLayer(WitherStormModRenderers.SANTA_HAT));
      this.specialDay = SpecialDay.getForCurrentDate();
   }

   public void updateModel(T entity) {
      this.model = this.fetchModel(entity);
   }

   public M getModel() {
      return this.model;
   }

   public abstract M fetchModel(T var1);

   protected int getBlockLightLevel(T entity, BlockPos pos) {
      return Math.max(0, (int)((100.0F - entity.getFadeAnimation()) / 4.0F - 10.0F));
   }

   public void render(T entity, float yRot, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
      stack.pushPose();
      this.updateModel(entity);
      renderDebrisClusters(this.getTextureLocation(entity), entity, stack, buffer, partialTicks, packedLight);
      float yBodyRot = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
      float xBodyRot = Mth.rotLerp(partialTicks, entity.xBodyRotO, entity.xBodyRot);
      float yHeadRot = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
      float netRot = yHeadRot - yBodyRot;
      float xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
      float bob = (float)entity.tickCount + partialTicks;
      this.setupRotations(entity, stack, xBodyRot, yBodyRot, partialTicks);
      stack.scale(-1.0F, -1.0F, 1.0F);
      this.scale(entity, stack, partialTicks);
      stack.translate(0.0F, -1.501F, 0.0F);
      this.model.setupAnimations(entity, partialTicks, bob, netRot, xRot);
      int i = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
      boolean shadersEnabled = CompatHelper.areShadersRunning();
      RenderType emissive = WitherStormModConfig.CLIENT.renderEmissiveDecalForHeads.get()
         ? UtilRenderTypes.emissiveTranslucent(this.getEmissiveDecalLocation(entity))
         : RenderType.entityCutout(this.getEmissiveDecalLocation(entity));
      RenderType massEmissive = RenderType.eyes(this.getEmissiveDecalLocation(entity));
      RenderType hurtOverlay = RenderType.entityTranslucent(HURT_OVERLAY);
      if (entity.getDeathTime() > 0) {
         float fade = Math.min((float)entity.getDeathTime(), 400.0F) / 400.0F;
         RenderType explodingType = shadersEnabled
            ? RenderType.dragonExplosionAlpha(this.getExplodingTextureLocation(entity))
            : UtilRenderTypes.witherStormDissolve(this.getExplodingTextureLocation(entity));
         this.model.render(entity, stack, buffer, explodingType, null, null, hurtOverlay, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, fade);
         RenderType decalType = shadersEnabled
            ? RenderType.entityDecal(this.getTextureLocation(entity))
            : UtilRenderTypes.witherStormDecal(this.getTextureLocation(entity));
         this.model.render(entity, stack, buffer, decalType, emissive, massEmissive, hurtOverlay, packedLight, i, -1);
      } else {
         this.model
            .render(
               entity,
               stack,
               buffer,
               shadersEnabled ? RenderType.entityCutoutNoCull(this.getTextureLocation(entity)) : UtilRenderTypes.witherStorm(this.getTextureLocation(entity)),
               emissive,
               massEmissive,
               hurtOverlay,
               packedLight,
               i, -1);
      }

      if (entity.isPowered()) {
         this.renderEnergyShield(entity, stack, buffer, partialTicks, bob, netRot, xRot, packedLight, i);
      }

      if (this.specialDay == SpecialDay.CHRISTMAS) {
         this.renderChristmasFestivities(entity, stack, buffer, packedLight, i);
      }

      if ((Boolean)WitherStormModConfig.CLIENT.renderPulse.get() && entity.isBeingTornApart() && !entity.isDeadOrDying()) {
         this.renderPulsing(entity, stack, buffer, partialTicks, packedLight, i);
      }

      stack.popPose();
      super.render(entity, yRot, partialTicks, stack, buffer, packedLight);
   }

   protected void renderEnergyShield(
      T entity, PoseStack stack, MultiBufferSource bufferSource, float partialTicks, float bob, float yRot, float xRot, int packedLight, int overlayTexture
   ) {
      this.swirlModel.setupAnimations(entity, partialTicks, bob, yRot, xRot);
      float xOffset = Mth.cos(bob * 0.02F) * 3.0F;
      RenderType type = RenderType.energySwirl(WITHER_ARMOR_LOCATION, xOffset % 1.0F, bob * 0.01F % 1.0F);
      this.swirlModel.render(entity, stack, bufferSource, type, null, null, null, packedLight, overlayTexture, -1);
   }

   protected void renderChristmasFestivities(T entity, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int overlayTexture) {
      ObjectIterator var6 = this.getModel().heads.int2ObjectEntrySet().iterator();

      while (var6.hasNext()) {
         Entry<HeadModel<T>> entry = (Entry<HeadModel<T>>)var6.next();
         if (!entity.areOtherHeadsDisabled() || entry.getIntKey() == 0) {
            stack.pushPose();
            HeadModel<T> model = (HeadModel<T>)entry.getValue();
            model.scale(stack);
            model.root().translateAndRotate(stack);
            this.santaHat
               .renderToBuffer(stack, bufferSource.getBuffer(this.santaHat.renderType(SantaHatModel.TEXTURE)), packedLight, overlayTexture, -1);
            stack.popPose();
         }
      }
   }

   protected void renderPulsing(T entity, PoseStack stack, MultiBufferSource bufferSource, float partialTicks, int packedLight, int overlayTexture) {
      if (this.model.getMassModel(entity) != null) {
         VertexConsumer builder = bufferSource.getBuffer(RenderType.entityTranslucent(this.getPulseTextureLocation(entity)));

         for (int k = 0; k < this.getPulseAmount(entity); k++) {
            float tick = (float)(entity.tickCount + k) + partialTicks;
            long seed = (long)(tick / 20.0F);
            RandomSource random = RandomSource.create(seed + (long)((double)k * Math.PI));
            ModelPart part = this.model.getRandomPart(entity, random);
            Vector3f pos = getRandomPointOnCubeSurface(part.getRandomCube(random), random);
            ModelPart mirror = new ModelPart(
               CubeListBuilder.create()
                  .texOffs(random.nextInt(28), Math.max(16, random.nextInt(30)))
                  .addBox(pos.x(), pos.y(), pos.z(), 1.0F, 1.0F, 1.0F, new CubeDeformation(0.01F))
                  .getCubes()
                  .stream()
                  .map(def -> def.bake(32, 32))
                  .collect(Collectors.toList()),
               Maps.newHashMap()
            );
            ModelPart mass = this.model.getMassModel(entity);
            mirror.x = mass.x;
            mirror.y = mass.y;
            mirror.z = mass.z;
            if (part != mass) {
               mirror.x = mirror.x + part.x;
               mirror.y = mirror.y + part.y;
               mirror.z = mirror.z + part.z;
            }

            mirror.xRot = part.xRot;
            mirror.yRot = part.yRot;
            mirror.zRot = part.zRot;
            stack.pushPose();
            stack.scale(entity.isMirrored() ? -1.0F : 1.0F, 1.0F, 1.0F);
            this.model.scaleMass(stack);
            float fade = 1.0F - tick % 20.0F * 0.1F / 2.0F;
            int overlayCoords = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
            mirror.render(stack, builder, packedLight, overlayCoords, 1.0F, 1.0F, 1.0F, fade);
            stack.popPose();
         }
      }
   }

   public void renderTractorBeams(T entity, PoseStack stack, MultiBufferSource buffer, float partialTicks, int packedLight) {
      ObjectIterator var6 = this.getModel().heads.int2ObjectEntrySet().iterator();

      while (var6.hasNext()) {
         Entry<HeadModel<T>> entry = (Entry<HeadModel<T>>)var6.next();
         HeadModel<T> head = (HeadModel<T>)entry.getValue();
         stack.pushPose();
         if (head.shouldRenderTractorBeam(entity, entry.getIntKey())) {
            Minecraft mc = Minecraft.getInstance();
            Vec3 pos = mc.gameRenderer.getMainCamera().getPosition();
            double distance = pos.distanceTo(entity.position());
            float renderDistance = (float)((Integer)mc.options.renderDistance().get()).intValue() / 16.0F;
            float distLerp = getDistanceLerp(distance, renderDistance);
            float nightLerp = getNightTimeLerp(entity.level(), partialTicks);
            float alpha = distLerp * (1.0F - nightLerp);
            boolean rainbow = entity.hasCustomName() && entity.getName().getString().equals("jeb_");
            if (!rainbow && entity instanceof WitherStormSegmentEntity segment) {
               WitherStormEntity parent = segment.getParent();
               if (parent != null && parent.hasCustomName() && parent.getName().getString().equals("jeb_")) {
                  rainbow = true;
               }
            }

            float r;
            float g;
            float b;
            if (rainbow) {
               float[] rainbowColor = getRainbowColor(entity, entry.getIntKey(), partialTicks);
               r = rainbowColor[0];
               g = rainbowColor[1];
               b = rainbowColor[2];
            } else if (this.specialDay != null) {
               Color color = this.specialDay.getColor(entity, partialTicks, entry.getIntKey());
               r = (float)color.getRed() / 255.0F;
               g = (float)color.getGreen() / 255.0F;
               b = (float)color.getBlue() / 255.0F;
            } else {
               ColorSet set = WitherStormResourceConfigManager.INSTANCE.getColorSetByPhase(entity.getPhase());
               Color color = set.tractorBeamColor();
               Color night = set.tractorBeamNightColor();
               r = Mth.lerp(nightLerp, (float)color.getRed(), (float)night.getRed()) / 255.0F;
               g = Mth.lerp(nightLerp, (float)color.getGreen(), (float)night.getGreen()) / 255.0F;
               b = Mth.lerp(nightLerp, (float)color.getBlue(), (float)night.getBlue()) / 255.0F;
            }

            if ((Boolean)WitherStormModConfig.CLIENT.distantFog.get()) {
               float rDelta = 0.3F - r;
               float gDelta = 0.3F - g;
               float bDelta = 0.3F - b;
               r += rDelta * alpha;
               g += gDelta * alpha;
               b += bDelta * alpha;
            }

            double cutoff = entity.getTractorBeamCutoffDistance(entry.getIntKey());
            if (cutoff != -1.0) {
               cutoff = cutoff / (double)this.model.headScale + 10.0;
            }

            head.renderTractorBeam(entity, stack, buffer, packedLight, r, g, b, 0.5F, partialTicks, cutoff, 0.5F * (1.0F - distLerp));
         }

         stack.popPose();
      }
   }

   private static float[] getRainbowColor(WitherStormEntity entity, int offset, float partialTicks) {
      int tickCount = entity.tickCount / 25 + entity.getId() + offset;
      int allDyeColors = DyeColor.values().length;
      int k = tickCount % allDyeColors;
      int l = (tickCount + 1) % allDyeColors;
      float f3 = ((float)(entity.tickCount % 25) + partialTicks) / 25.0F;
      float[] afloat1 = Sheep.getColorArray(DyeColor.byId(k));
      float[] afloat2 = Sheep.getColorArray(DyeColor.byId(l));
      float r = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
      float g = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
      float b = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
      return new float[]{r, g, b};
   }

   public void prepareHeadAnimsForTractorBeams(T entity, float partialTicks) {
      ObjectIterator var3 = this.model.heads.int2ObjectEntrySet().iterator();

      while (var3.hasNext()) {
         Entry<HeadModel<T>> entry = (Entry<HeadModel<T>>)var3.next();
         HeadModel<T> head = (HeadModel<T>)entry.getValue();
         float yBodyRot = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
         float yHeadRot = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
         float yRot = yHeadRot - yBodyRot;
         float xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
         float bob = (float)entity.tickCount + partialTicks;
         head.setupAnimations(entity, partialTicks, bob, yRot, xRot, entry.getIntKey());
      }
   }

   public boolean shouldRender(T entity, Frustum clipping, double x, double y, double z) {
      if (!entity.shouldRender(x, y, z)) {
         return false;
      } else if (!entity.isOnDistantRenderer() && entity.noCulling) {
         return true;
      } else {
         AABB box = entity.getBoundingBoxForCulling().inflate(0.5);
         if (box.hasNaN() || box.getSize() == 0.0) {
            box = new AABB(
               entity.getX() - 2.0,
               entity.getY() - 2.0,
               entity.getZ() - 2.0,
               entity.getX() + 2.0,
               entity.getY() + 2.0,
               entity.getZ() + 2.0
            );
         }

         return clipping.isVisible(box);
      }
   }

   protected boolean shouldShowName(T entity) {
      return super.shouldShowName(entity) && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
   }

   public void renderDebrisRings(WitherStormEntity entity, PoseStack stack, MultiBufferSource buffer, float partialTicks, int packedLightIn) {
      Minecraft mc = Minecraft.getInstance();
      Vec3 pos = mc.gameRenderer.getMainCamera().getPosition();
      double distance = pos.distanceTo(entity.position());
      float baseAlpha = Mth.clamp((float)(400.0 - distance) * 0.005F, 0.2F, 0.5F);
      ResourceLocation tex = WitherStormResourceConfigManager.INSTANCE.getTextureSetByPhase(entity.getPhase()).debrisRing();
      VertexConsumer debrisBuilder = buffer.getBuffer(RenderType.entityTranslucentCull(tex));

      for (DebrisRingSettings settings : entity.getDebrisRings()) {
         if (settings.alpha() > 0.0F && entity.getPhase() >= settings.getPhaseRequirement()) {
            stack.pushPose();
            int segments = settings.getSegments();
            float bottomRadius = settings.getBottomRadius();
            float topRadius = settings.getTopRadius();
            float y = settings.getY();
            float height = settings.getHeight();
            float alpha = settings.alpha() * baseAlpha;
            Pose entry = stack.last();
            Matrix4f matrix4f = entry.pose();
            Matrix3f matrix3f = entry;
            float u = 0.0F;
            float v = 0.0F;
            float uMax = 1.0F;
            float vMax = 1.0F;
            float tickCount = ((float)entity.tickCount + partialTicks) * settings.getSpeedModifier() * (settings.clockwise() ? 1.0F : -1.0F);

            for (int i = 0; i < segments; i++) {
               float theta = (float)((Math.PI * 2) / (double)segments);
               float angle = theta * (float)i + tickCount;
               float x = Mth.cos(angle);
               float z = Mth.sin(angle);
               float angle2 = theta * (float)(i + 1) + tickCount;
               float x2 = Mth.cos(angle2);
               float z2 = Mth.sin(angle2);
               debrisBuilder.addVertex(matrix4f, x * bottomRadius, y, z * bottomRadius)
                  .setColor(1.0F, 1.0F, 1.0F, alpha)
                  .setUv(u, v)
                  .setOverlay(OverlayTexture.NO_OVERLAY)
                  .setLight(packedLightIn)
                  .setNormal(0.0F, -1.0F, 0.0F)
                  ;
               debrisBuilder.addVertex(matrix4f, x * topRadius, height, z * topRadius)
                  .setColor(1.0F, 1.0F, 1.0F, alpha)
                  .setUv(u, vMax)
                  .setOverlay(OverlayTexture.NO_OVERLAY)
                  .setLight(packedLightIn)
                  .setNormal(0.0F, -1.0F, 0.0F)
                  ;
               debrisBuilder.addVertex(matrix4f, x2 * topRadius, height, z2 * topRadius)
                  .setColor(1.0F, 1.0F, 1.0F, alpha)
                  .setUv(uMax, vMax)
                  .setOverlay(OverlayTexture.NO_OVERLAY)
                  .setLight(packedLightIn)
                  .setNormal(0.0F, -1.0F, 0.0F)
                  ;
               debrisBuilder.addVertex(matrix4f, x2 * bottomRadius, y, z2 * bottomRadius)
                  .setColor(1.0F, 1.0F, 1.0F, alpha)
                  .setUv(uMax, v)
                  .setOverlay(OverlayTexture.NO_OVERLAY)
                  .setLight(packedLightIn)
                  .setNormal(0.0F, -1.0F, 0.0F)
                  ;
               debrisBuilder.addVertex(matrix4f, x2 * bottomRadius, y, z2 * bottomRadius)
                  .setColor(1.0F, 1.0F, 1.0F, alpha)
                  .setUv(uMax, vMax)
                  .setOverlay(OverlayTexture.NO_OVERLAY)
                  .setLight(packedLightIn)
                  .setNormal(0.0F, -1.0F, 0.0F)
                  ;
               debrisBuilder.addVertex(matrix4f, x2 * topRadius, height, z2 * topRadius)
                  .setColor(1.0F, 1.0F, 1.0F, alpha)
                  .setUv(uMax, v)
                  .setOverlay(OverlayTexture.NO_OVERLAY)
                  .setLight(packedLightIn)
                  .setNormal(0.0F, -1.0F, 0.0F)
                  ;
               debrisBuilder.addVertex(matrix4f, x * topRadius, height, z * topRadius)
                  .setColor(1.0F, 1.0F, 1.0F, alpha)
                  .setUv(u, v)
                  .setOverlay(OverlayTexture.NO_OVERLAY)
                  .setLight(packedLightIn)
                  .setNormal(0.0F, -1.0F, 0.0F)
                  ;
               debrisBuilder.addVertex(matrix4f, x * bottomRadius, y, z * bottomRadius)
                  .setColor(1.0F, 1.0F, 1.0F, alpha)
                  .setUv(u, vMax)
                  .setOverlay(OverlayTexture.NO_OVERLAY)
                  .setLight(packedLightIn)
                  .setNormal(0.0F, -1.0F, 0.0F)
                  ;
            }

            stack.popPose();
         }
      }
   }

   public static void renderDebrisClusters(
      ResourceLocation tex, WitherStormEntity entity, PoseStack stack, MultiBufferSource bufferSource, float partialTicks, int packedLight
   ) {
      if ((Boolean)WitherStormModConfig.CLIENT.renderDebrisCloud.get()
         && (
            !(Boolean)WitherStormModConfig.CLIENT.renderDistantDebris.get() && !entity.isOnDistantRenderer()
               || (Boolean)WitherStormModConfig.CLIENT.renderDistantDebris.get()
         )) {
         for (DebrisCluster cluster : entity.getDebrisClusters()) {
            if (!cluster.isDisabled() && cluster.getRenderPhase() <= entity.getPhase()) {
               float orbitalAngle = cluster.getOrbitalAngle(partialTicks);
               stack.pushPose();
               stack.translate(0.0, (double)cluster.getVerticalOffset(), 0.0);
               stack.mulPose(Axis.YP.rotationDegrees(orbitalAngle));
               stack.translate((double)cluster.getRadiusFromCenter(), 0.0, 0.0);
               stack.mulPose(Axis.XP.rotationDegrees(cluster.getXRot(partialTicks)));
               stack.mulPose(Axis.YP.rotationDegrees(cluster.getYRot(partialTicks)));
               boolean flag = cluster.isGlowing() && entity.getPhase() > 5 || cluster.isForcedGlowing();
               float u;
               float v;
               float uMax;
               float vMax;
               if (flag) {
                  u = 0.3125F;
                  v = 0.3125F;
                  uMax = 0.375F;
                  vMax = 0.375F;
               } else {
                  u = 0.9F;
                  v = 0.8F;
                  uMax = 1.0F;
                  vMax = 0.9F;
               }

               RenderBufferer.pushCullFaces();
               if (flag) {
                  RenderBufferer.pushNoFog();
               }

               RenderBufferer.buildAndOrRender(
                  cluster.toString() + flag + tex,
                  RenderType.entityCutout(tex),
                  (bstack, consumer, bpackedLight, boverlayTexture, br, bg, bb, ba) -> renderPieces(
                        cluster, bstack, consumer, br, bg, bb, ba, boverlayTexture, bpackedLight, u, v, uMax, vMax
                     ),
                  stack,
                  packedLight,
                  OverlayTexture.NO_OVERLAY, -1);
               if (flag) {
                  RenderBufferer.pushNoFog();
                  RenderBufferer.buildAndOrRender(
                     cluster.toString() + ", " + flag + ", emissive" + tex,
                     RenderType.eyes(tex),
                     (bstack, consumer, bpackedLight, boverlayTexture, br, bg, bb, ba) -> renderPieces(
                           cluster, bstack, consumer, br, bg, bb, ba, boverlayTexture, bpackedLight, u, v, uMax, vMax
                        ),
                     stack,
                     packedLight,
                     OverlayTexture.NO_OVERLAY, -1);
               }

               RenderBufferer.popCullFaces();
               stack.popPose();
            }
         }
      }
   }

   private static void renderPieces(
      DebrisCluster cluster,
      PoseStack bstack,
      VertexConsumer consumer,
      float br,
      float bg,
      float bb,
      float ba,
      int boverlayTexture,
      int bpackedLight,
      float u,
      float v,
      float uMax,
      float vMax
   ) {
      for (DebrisCluster.Piece piece : cluster.getPieces()) {
         bstack.pushPose();
         bstack.translate(piece.x(), piece.y(), piece.z());
         Matrix4f matrix4f = bstack.last().pose();
         Matrix3f matrix3f = bstack.last();
         float startSize = piece.size();
         float endSize = -piece.size();
         consumer.addVertex(matrix4f, startSize, startSize, endSize)
            .setColor(br, bg, bb, ba)
            .setUv(u, v)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, startSize, endSize, endSize)
            .setColor(br, bg, bb, ba)
            .setUv(u, vMax)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, endSize, endSize, endSize)
            .setColor(br, bg, bb, ba)
            .setUv(uMax, vMax)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, endSize, startSize, endSize)
            .setColor(br, bg, bb, ba)
            .setUv(uMax, v)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, endSize, startSize, endSize)
            .setColor(br, bg, bb, ba)
            .setUv(u, v)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, endSize, endSize, endSize)
            .setColor(br, bg, bb, ba)
            .setUv(u, vMax)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, endSize, endSize, startSize)
            .setColor(br, bg, bb, ba)
            .setUv(uMax, vMax)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, endSize, startSize, startSize)
            .setColor(br, bg, bb, ba)
            .setUv(uMax, v)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, startSize, startSize, startSize)
            .setColor(br, bg, bb, ba)
            .setUv(u, v)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, startSize, endSize, startSize)
            .setColor(br, bg, bb, ba)
            .setUv(u, vMax)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, startSize, endSize, endSize)
            .setColor(br, bg, bb, ba)
            .setUv(uMax, vMax)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, startSize, startSize, endSize)
            .setColor(br, bg, bb, ba)
            .setUv(uMax, v)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, endSize, startSize, startSize)
            .setColor(br, bg, bb, ba)
            .setUv(u, v)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, endSize, endSize, startSize)
            .setColor(br, bg, bb, ba)
            .setUv(u, vMax)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, startSize, endSize, startSize)
            .setColor(br, bg, bb, ba)
            .setUv(uMax, vMax)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, startSize, startSize, startSize)
            .setColor(br, bg, bb, ba)
            .setUv(uMax, v)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, startSize, startSize, startSize)
            .setColor(br, bg, bb, ba)
            .setUv(u, v)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, startSize, startSize, endSize)
            .setColor(br, bg, bb, ba)
            .setUv(u, vMax)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, endSize, startSize, endSize)
            .setColor(br, bg, bb, ba)
            .setUv(uMax, vMax)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, endSize, startSize, startSize)
            .setColor(br, bg, bb, ba)
            .setUv(uMax, v)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, startSize, endSize, startSize)
            .setColor(br, bg, bb, ba)
            .setUv(u, v)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, endSize, endSize, startSize)
            .setColor(br, bg, bb, ba)
            .setUv(u, vMax)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, endSize, endSize, endSize)
            .setColor(br, bg, bb, ba)
            .setUv(uMax, vMax)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         consumer.addVertex(matrix4f, startSize, endSize, endSize)
            .setColor(br, bg, bb, ba)
            .setUv(uMax, v)
            .setOverlay(boverlayTexture)
            .setLight(bpackedLight)
            .setNormal(0.0F, -1.0F, 0.0F)
            ;
         bstack.popPose();
      }
   }

   public ResourceLocation getTextureLocation(T entity) {
      TextureSet set = WitherStormResourceConfigManager.INSTANCE.getTextureSetByPhase(entity.getPhase());
      boolean flag = WitherStormMod.isAprilFools() && (Boolean)WitherStormModConfig.CLIENT.aprilFools.get();
      int i = entity.getInvulnerableTicks();
      return i <= 0 || i <= 80 && i / 5 % 2 == 1 ? (flag ? PINK_WITHER_STORM_LOCATION : set.main()) : set.invulnerable();
   }

   public ResourceLocation getExplodingTextureLocation(T entity) {
      return WITHER_STORM_EXPLODING_LOCATION;
   }

   public ResourceLocation getPulseTextureLocation(T entity) {
      return PULSE;
   }

   public ResourceLocation getEmissiveDecalLocation(T entity) {
      return WitherStormResourceConfigManager.INSTANCE.getTextureSetByPhase(entity.getPhase()).emissiveDecal();
   }

   protected void scale(T entity, PoseStack stack, float partialTicks) {
      float f = 2.0F;
      int i = entity.getInvulnerableTicks();
      int j = Math.max(0, i - 750);
      if (j > 0) {
         f -= ((float)j - partialTicks) / (float)Math.max(0, entity.getStartingInvulnerableTicks() - 750) * 0.5F;
      }

      stack.scale(f, f, f);
   }

   protected void setupRotations(T entity, PoseStack stack, float xBodyRot, float yBodyRot, float partialTicks) {
      stack.mulPose(Axis.YP.rotationDegrees(180.0F - yBodyRot));
      stack.mulPose(Axis.XP.rotationDegrees(xBodyRot));
   }

   public int getPulseAmount(T entity) {
      return (int)((float)entity.getPhase() * 15.0F / 2.0F * (WitherStormModConfig.CLIENT.lowResModels.get() ? 3.0F : 1.0F));
   }

   @Nullable
   public static <T extends WitherStormEntity, M extends AbstractWitherStormModel<T>> AbstractWitherStormRenderer<T, M> getRenderer(
      T storm, EntityRenderDispatcher manager
   ) {
      EntityRenderer<? super T> raw = manager.getRenderer(storm);
      return raw instanceof AbstractWitherStormRenderer ? (AbstractWitherStormRenderer)raw : null;
   }

   public static void renderShine(WitherStormEntity storm, PoseStack stack, float partialTicks, Camera camera, MultiBufferSource buffer) {
      Minecraft mc = Minecraft.getInstance();
      Color configColor = WitherStormResourceConfigManager.INSTANCE.getColorSetByPhase(storm.getPhase()).nightShineColor();
      boolean rainbow = storm.hasCustomName() && storm.getName().getString().equals("jeb_");
      if (!rainbow && storm instanceof WitherStormSegmentEntity segment) {
         WitherStormEntity parent = segment.getParent();
         if (parent != null && parent.hasCustomName() && parent.getName().getString().equals("jeb_")) {
            rainbow = true;
         }
      }

      int[] color;
      if (!rainbow) {
         color = new int[]{configColor.getRed(), configColor.getGreen(), configColor.getBlue(), configColor.getAlpha()};
      } else {
         float[] rainbowColor = getRainbowColor(storm, 0, partialTicks);
         color = new int[]{(int)(rainbowColor[0] * 255.0F), (int)(rainbowColor[1] * 255.0F), (int)(rainbowColor[2] * 255.0F), 75};
      }

      float distanceLerp = 1.0F;
      if (storm.getPhase() > 5 || storm.getConsumptionAmountForPhase(5) <= storm.getConsumedEntities()) {
         distanceLerp = getDistanceLerp(storm.getEyePosition().distanceTo(camera.getPosition()), (float)((Integer)mc.options.renderDistance().get()).intValue() / 8.0F);
      }

      float f1 = getNightTimeLerp(storm.level(), partialTicks) * distanceLerp * storm.getShineAlpha(partialTicks);
      color[3] = Mth.floor((float)color[3] * f1);
      if (color[3] > 0) {
         VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(SHINE));
         Matrix4f matrix = stack.last().pose();
         Vec3 dir = camera.getPosition().subtract(storm.getEyePosition(partialTicks)).normalize();
         float pitch = (float)Math.asin(dir.y);
         float yaw = (float)Math.atan2(dir.x, dir.z);
         stack.mulPose(Axis.YN.rotationDegrees(180.0F));
         stack.mulPose(Axis.YP.rotation(yaw));
         stack.mulPose(Axis.XP.rotation(pitch));
         float scale = storm.getShineScale();
         float xStretch = scale * (storm.getPhase() > 5 ? 1.5F : 1.0F);
         stack.translate(-xStretch / 2.0F, storm.getUnmodifiedHeight() / 2.0F - scale / 2.0F, scale / 2.0F);
         stack.scale(xStretch, scale, 1.0F);
         consumer.addVertex(matrix, 0.0F, 0.0F, 0.0F)
            .setColor(color[0], color[1], color[2], color[3])
            .setUv(0.0F, 0.0F)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(15728880)
            .setNormal(0.0F, 1.0F, 1.0F)
            ;
         consumer.addVertex(matrix, 0.0F, 1.0F, 0.0F)
            .setColor(color[0], color[1], color[2], color[3])
            .setUv(0.0F, 1.0F)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(15728880)
            .setNormal(0.0F, 1.0F, 1.0F)
            ;
         consumer.addVertex(matrix, 1.0F, 1.0F, 0.0F)
            .setColor(color[0], color[1], color[2], color[3])
            .setUv(1.0F, 1.0F)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(15728880)
            .setNormal(0.0F, 1.0F, 1.0F)
            ;
         consumer.addVertex(matrix, 1.0F, 0.0F, 0.0F)
            .setColor(color[0], color[1], color[2], color[3])
            .setUv(1.0F, 0.0F)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(15728880)
            .setNormal(0.0F, 1.0F, 1.0F)
            ;
      }
   }

   public static float getNightTimeLerp(Level level, float partialTicks) {
      float f1 = Mth.cos(level.getTimeOfDay(partialTicks) * (float) (Math.PI * 2)) * 2.0F + 0.5F;
      return 1.0F - Mth.clamp(f1 + 0.5F, 0.0F, 1.0F);
   }

   public static float getDistanceLerp(double distance, float renderDistance) {
      return Mth.clamp((float)(distance - (double)(200.0F * renderDistance)) * 0.005F, 0.0F, 1.0F);
   }

   public static Vector3f getRandomPointOnCubeSurface(Cube box, RandomSource random) {
      Vector3f pos = new Vector3f(box.minX, box.minY, box.minZ);
      float xDif = box.maxX - pos.x();
      float yDif = box.maxY - pos.y();
      float zDif = box.maxZ - pos.z();
      boolean flag = xDif > 0.0F && yDif > 0.0F && zDif > 0.0F;
      if (flag) {
         Direction direction = Direction.getRandom(random);
         if (direction.getAxis().equals(net.minecraft.core.Direction.Axis.X)) {
            pos.set(direction.equals(Direction.WEST) ? box.minX : box.maxX - 1.0F, pos.y, pos.z);
            pos.set(pos.x, pos.y() + (float)random.nextInt((int)yDif), pos.z);
            pos.set(pos.x, pos.y, pos.z() + (float)random.nextInt((int)zDif));
         } else if (direction.getAxis().equals(net.minecraft.core.Direction.Axis.Y)) {
            pos.set(pos.x() + (float)random.nextInt((int)xDif), pos.y, pos.z);
            pos.set(pos.x, direction.equals(Direction.DOWN) ? box.minY : box.maxY - 1.0F, pos.z);
            pos.set(pos.x, pos.y, pos.z() + (float)random.nextInt((int)zDif));
         } else if (direction.getAxis().equals(net.minecraft.core.Direction.Axis.Z)) {
            pos.set(pos.x() + (float)random.nextInt((int)xDif), pos.y, pos.z);
            pos.set(pos.x, pos.y() + (float)random.nextInt((int)yDif), pos.z);
            pos.set(pos.x, pos.y, direction.equals(Direction.NORTH) ? box.minZ : box.maxZ - 1.0F);
         }
      }

      return pos;
   }

   public static void renderExtraHitboxes(PoseStack stack, VertexConsumer consumer, WitherStormEntity storm, float partialTick) {
      for (WitherStormHead head : storm.getHeadManager().getHeads()) {
         stack.pushPose();
         stack.translate(-storm.getX(), -storm.getY(), -storm.getZ());
         if (head.getBoundingBox() != null) {
            AABB box = head.getBoundingBox();
            Color col = EXTRA_HEAD_COLOR;
            if (head instanceof MainHead) {
               col = MAIN_HEAD_COLOR;
            }

            LevelRenderer.renderLineBox(stack, consumer, box, (float)col.getRed() / 255.0F, (float)col.getGreen() / 255.0F, (float)col.getBlue() / 255.0F, 1.0F);
            Vec3 viewVector = storm.getViewVector(head.getHeadXRot(partialTick), head.getHeadYRot(partialTick), (float)box.getSize());
            Vec3 eyePos = head.getHeadPos();
            Matrix4f matrix4f = stack.last().pose();
            Matrix3f matrix3f = stack.last();
            consumer.addVertex(matrix4f, (float)eyePos.x, (float)eyePos.y, (float)eyePos.z)
               .setColor(0, 0, 255, 255)
               .setNormal((float)viewVector.x, (float)viewVector.y, (float)viewVector.z)
               ;
            consumer.addVertex(
                  matrix4f,
                  (float)eyePos.x + (float)viewVector.x,
                  (float)eyePos.y + (float)viewVector.y,
                  (float)eyePos.z + (float)viewVector.z
               )
               .setColor(0, 0, 255, 255)
               .setNormal((float)viewVector.x, (float)viewVector.y, (float)viewVector.z)
               ;
         }

         if (storm.getPhase() > 4 && storm.partsEnabled) {
            for (Section part : storm.getSections()) {
               if (part.isActive()) {
                  LevelRenderer.renderLineBox(stack, consumer, part.getBoundingBox(), part.getColor()[0], part.getColor()[1], part.getColor()[2], 1.0F);
               }
            }
         }

         stack.popPose();
      }
   }
}
