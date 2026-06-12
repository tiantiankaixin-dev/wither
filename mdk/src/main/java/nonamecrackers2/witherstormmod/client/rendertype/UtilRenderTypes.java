package nonamecrackers2.witherstormmod.client.rendertype;

import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderStateShard.CullStateShard;
import net.minecraft.client.renderer.RenderStateShard.DepthTestStateShard;
import net.minecraft.client.renderer.RenderStateShard.LightmapStateShard;
import net.minecraft.client.renderer.RenderStateShard.OverlayStateShard;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderStateShard.TransparencyStateShard;
import net.minecraft.client.renderer.RenderStateShard.WriteMaskStateShard;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.client.init.WitherStormModShaders;

public class UtilRenderTypes {
   private static final ShaderStateShard ENTITY_DECAL_SHADER = new ShaderStateShard(GameRenderer::getRendertypeEntityDecalShader);
   private static final ShaderStateShard EYES_SHADER = new ShaderStateShard(GameRenderer::getRendertypeEyesShader);
   private static final TransparencyStateShard TRANSLUCENT_TRANSPARENCY = new TransparencyStateShard("translucent_transparency", () -> {
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ONE_MINUS_SRC_ALPHA);
   }, () -> {
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
   });
   private static final TransparencyStateShard ADDITIVE_TRANSPARENCY = new TransparencyStateShard("additive_transparency", () -> {
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(SourceFactor.ONE, DestFactor.ONE);
   }, () -> {
      RenderSystem.disableBlend();
      RenderSystem.defaultBlendFunc();
   });
   protected static final TransparencyStateShard NO_TRANSPARENCY = new TransparencyStateShard("no_transparency", () -> RenderSystem.disableBlend(), () -> {
   });
   private static final CullStateShard NO_CULL = new CullStateShard(false);
   private static final CullStateShard CULL = new CullStateShard(false);
   private static final LightmapStateShard LIGHTMAP = new LightmapStateShard(true);
   private static final OverlayStateShard OVERLAY = new OverlayStateShard(true);
   private static final DepthTestStateShard EQUAL_DEPTH_TEST = new DepthTestStateShard("==", 514);
   private static final WriteMaskStateShard COLOR_WRITE = new WriteMaskStateShard(true, false);
   private static final ShaderStateShard WITHER_STORM_SHADER = new ShaderStateShard(WitherStormModShaders::getWitherStormShader);
   private static final ShaderStateShard WITHER_STORM_DISSOLVE_SHADER = new ShaderStateShard(WitherStormModShaders::getWitherStormDissolveShader);
   private static final Function<ResourceLocation, RenderType> ENTITY_DECAL_TRANSLUCENT = Util.memoize(
      loc -> {
         CompositeState state = CompositeState.builder()
            .setShaderState(ENTITY_DECAL_SHADER)
            .setTextureState(new TextureStateShard(loc, false, false))
            .setDepthTestState(EQUAL_DEPTH_TEST)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setCullState(NO_CULL)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(false);
         return RenderType.create("entity_decal_translucent", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 256, false, false, state);
      }
   );
   private static final Function<ResourceLocation, RenderType> EMISSIVE_NO_CULL = Util.memoize(
      tex -> {
         TextureStateShard state = new TextureStateShard(tex, false, false);
         return RenderType.create(
            "emissive_no_cull",
            DefaultVertexFormat.NEW_ENTITY,
            Mode.QUADS,
            256,
            false,
            true,
            CompositeState.builder()
               .setShaderState(EYES_SHADER)
               .setTextureState(state)
               .setTransparencyState(ADDITIVE_TRANSPARENCY)
               .setCullState(NO_CULL)
               .setWriteMaskState(COLOR_WRITE)
               .createCompositeState(false)
         );
      }
   );
   private static final Function<ResourceLocation, RenderType> EMISSIVE_TRANSLUCENT = Util.memoize(
      tex -> {
         TextureStateShard state = new TextureStateShard(tex, false, false);
         return RenderType.create(
            "emissive_translucent",
            DefaultVertexFormat.NEW_ENTITY,
            Mode.QUADS,
            256,
            false,
            true,
            CompositeState.builder()
               .setShaderState(EYES_SHADER)
               .setTextureState(state)
               .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
               .setCullState(CULL)
               .setWriteMaskState(COLOR_WRITE)
               .createCompositeState(false)
         );
      }
   );
   private static final Function<ResourceLocation, RenderType> WITHER_STORM = Util.memoize(
      p_269670_ -> {
         CompositeState compositeState = CompositeState.builder()
            .setShaderState(WITHER_STORM_SHADER)
            .setTextureState(new TextureStateShard(p_269670_, false, false))
            .setTransparencyState(NO_TRANSPARENCY)
            .setCullState(NO_CULL)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(true);
         return RenderType.create("wither_storm", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 256, true, false, compositeState);
      }
   );
   private static final Function<ResourceLocation, RenderType> WITHER_STORM_DISSOLVE = Util.memoize(
      tex -> {
         CompositeState compositeState = CompositeState.builder()
            .setShaderState(WITHER_STORM_DISSOLVE_SHADER)
            .setTextureState(new TextureStateShard(tex, false, false))
            .createCompositeState(true);
         return RenderType.create("wither_storm_dissolve", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 256, false, false, compositeState);
      }
   );
   private static final Function<ResourceLocation, RenderType> WITHER_STORM_DECAL = Util.memoize(
      tex -> {
         CompositeState compositeState = CompositeState.builder()
            .setShaderState(WITHER_STORM_SHADER)
            .setTextureState(new TextureStateShard(tex, false, false))
            .setDepthTestState(EQUAL_DEPTH_TEST)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(false);
         return RenderType.create("entity_decal", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 256, false, false, compositeState);
      }
   );

   public static RenderType entityDecalTranslucent(ResourceLocation tex) {
      return ENTITY_DECAL_TRANSLUCENT.apply(tex);
   }

   public static RenderType emissiveNoCull(ResourceLocation tex) {
      return EMISSIVE_NO_CULL.apply(tex);
   }

   public static RenderType emissiveTranslucent(ResourceLocation tex) {
      return EMISSIVE_TRANSLUCENT.apply(tex);
   }

   public static RenderType witherStorm(ResourceLocation tex) {
      return WITHER_STORM.apply(tex);
   }

   public static RenderType witherStormDissolve(ResourceLocation tex) {
      return WITHER_STORM_DISSOLVE.apply(tex);
   }

   public static RenderType witherStormDecal(ResourceLocation tex) {
      return WITHER_STORM_DECAL.apply(tex);
   }
}
