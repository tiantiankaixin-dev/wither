package nonamecrackers2.witherstormmod.client.shader;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.mixin.IMixinPostChain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostProcessingShaders implements ResourceManagerReloadListener {
   public static final PostProcessingShaders INSTANCE = new PostProcessingShaders(Minecraft.getInstance());
   private static final ResourceLocation ABERRATION = ResourceLocation.fromNamespaceAndPath("witherstormmod", "shaders/post/aberration.json");
   private static final Logger LOGGER = LogManager.getLogger();
   private final Minecraft minecraft;
   private PostChain aberrationEffect;
   private float prevWidth;
   private float prevHeight;

   public PostProcessingShaders(Minecraft minecraft) {
      this.minecraft = minecraft;
   }

   public void renderShaders(float partialTicks) {
      this.minecraft.getProfiler().push("chromatic_aberration_effect");
      if (this.shouldRenderChromaticAberration()) {
         if (this.prevWidth != (float)this.minecraft.getWindow().getWidth() || this.prevHeight != (float)this.minecraft.getWindow().getHeight()) {
            this.aberrationEffect.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
         }

         ClientLevel world = this.minecraft.level;
         { var effects = world.getData(WitherStormModClientCapabilities.FORMIDIBOMB_EFFECTS.get());
            if (effects.getStartFuse() > 0) {
               float multiplier = ((float)effects.getStartFuse() - (float)effects.getLife()) / (float)effects.getStartFuse();

               for (PostPass shader : this.getShaders()) {
                  shader.getEffect().safeGetUniform("Multiplier").set(multiplier * 0.1F);
               }

               this.aberrationEffect.process(partialTicks);
               this.minecraft.getMainRenderTarget().bindWrite(false);
            }
         }
      }

      this.minecraft.getProfiler().pop();
      this.prevWidth = (float)this.minecraft.getWindow().getWidth();
      this.prevHeight = (float)this.minecraft.getWindow().getHeight();
   }

   public void initShader(ResourceManager manager) {
      if (this.aberrationEffect != null) {
         this.aberrationEffect.close();
      }

      this.aberrationEffect = null;

      try {
         this.aberrationEffect = new PostChain(this.minecraft.getTextureManager(), manager, this.minecraft.getMainRenderTarget(), ABERRATION);
         this.aberrationEffect.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
      } catch (JsonSyntaxException var3) {
         LOGGER.warn("Failed to parse shader: {}", ABERRATION, var3);
      } catch (IOException var4) {
         LOGGER.warn("Failed to load shader: {}", ABERRATION, var4);
      }
   }

   public boolean shouldRenderChromaticAberration() {
      return (Boolean)WitherStormModConfig.CLIENT.chromaticAberration.get() && this.aberrationEffect != null;
   }

   public void onResourceManagerReload(ResourceManager resourceManager) {
      this.initShader(resourceManager);
   }

   public List<PostPass> getShaders() {
      return ((IMixinPostChain)this.aberrationEffect).getPasses();
   }
}
