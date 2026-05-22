package nonamecrackers2.witherstormmod.client.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.io.IOException;
import java.util.Objects;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.WitherStormMod;

public class WitherStormModShaders {
   private static ShaderInstance witherStormShader;
   private static ShaderInstance witherStormDissolveShader;

   @SubscribeEvent
   public static void registerShaders(RegisterShadersEvent event) throws IOException {
      event.registerShader(
         new ShaderInstance(event.getResourceProvider(), WitherStormMod.id("wither_storm"), DefaultVertexFormat.NEW_ENTITY), s -> witherStormShader = s
      );
      event.registerShader(
         new ShaderInstance(event.getResourceProvider(), WitherStormMod.id("wither_storm_dissolve"), DefaultVertexFormat.NEW_ENTITY),
         s -> witherStormDissolveShader = s
      );
   }

   public static ShaderInstance getWitherStormShader() {
      return Objects.requireNonNull(witherStormShader, "The wither storm shader has not been created yet");
   }

   public static ShaderInstance getWitherStormDissolveShader() {
      return Objects.requireNonNull(witherStormDissolveShader, "The wither storm dissolve shader has not been created yet");
   }
}
