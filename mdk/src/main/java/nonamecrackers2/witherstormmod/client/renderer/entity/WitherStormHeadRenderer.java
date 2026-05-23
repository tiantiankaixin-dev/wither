package nonamecrackers2.witherstormmod.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.time.temporal.ChronoField;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.client.init.WitherStormModRenderers;
import nonamecrackers2.witherstormmod.client.renderer.entity.layer.WitherStormHeadEyesLayer;
import nonamecrackers2.witherstormmod.client.renderer.entity.model.WitherStormHeadModel;
import nonamecrackers2.witherstormmod.client.resources.WitherStormResourceConfigManager;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormHeadEntity;

public class WitherStormHeadRenderer extends MobRenderer<WitherStormHeadEntity, WitherStormHeadModel> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("witherstormmod", "textures/entity/wither_storm_head/wither_storm_head.png");
   public static final ResourceLocation TEXTURE_HURT = new ResourceLocation("witherstormmod", "textures/entity/wither_storm_head/wither_storm_head_hurt.png");
   public static final ResourceLocation EMISSIVE = new ResourceLocation("witherstormmod", "textures/entity/wither_storm_head/wither_storm_head_emissive.png");
   public static final ResourceLocation EMISSIVE_HURT = new ResourceLocation(
      "witherstormmod", "textures/entity/wither_storm_head/wither_storm_head_emissive_hurt.png"
   );

   public WitherStormHeadRenderer(Context context) {
      super(context, new WitherStormHeadModel(context.bakeLayer(WitherStormModRenderers.WITHER_STORM_HEAD)), 3.5F);
      this.addLayer(new WitherStormHeadEyesLayer(this));
   }

   protected int getBlockLightLevel(WitherStormHeadEntity entity, BlockPos pos) {
      return Math.max(0, (int)((100.0F - entity.getFadeAnimation()) / 4.0F - 10.0F));
   }

   public void render(WitherStormHeadEntity entity, float p_225623_2_, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
      super.render(entity, p_225623_2_, partialTicks, stack, buffer, packedLight);
      if ((Boolean)WitherStormModConfig.CLIENT.renderTractorBeams.get()
         && !entity.isPlayingDead()
         && !entity.isHurt()
         && ((WitherStormHeadModel)this.getModel()).getHead().shouldRenderTractorBeam(entity, 0)) {
         int month = WitherStormMod.DATE.get(ChronoField.MONTH_OF_YEAR);
         int day = WitherStormMod.DATE.get(ChronoField.DAY_OF_MONTH);
         boolean flag = month == 10 && day == 31;
         Color color = WitherStormResourceConfigManager.INSTANCE.getColorSetByPhase(4).tractorBeamColor();
         float r = flag ? 0.5294118F : (float)color.getRed() / 255.0F;
         float g = flag ? 0.32156864F : (float)color.getGreen() / 255.0F;
         float b = flag ? 0.10980392F : (float)color.getBlue() / 255.0F;
         ((WitherStormHeadModel)this.getModel()).getHead().renderTractorBeam(entity, stack, buffer, packedLight, r, g, b, 0.5F, partialTicks, -1.0, 1.0F);
      }
   }

   public ResourceLocation getTextureLocation(WitherStormHeadEntity entity) {
      return entity.isHurt() ? TEXTURE_HURT : TEXTURE;
   }

   protected void scale(WitherStormHeadEntity entity, PoseStack stack, float p_225620_3_) {
      stack.scale(2.0F, 2.0F, 2.0F);
   }
}
