package nonamecrackers2.witherstormmod.common.util;

import java.awt.Color;
import net.minecraft.client.renderer.DimensionSpecialEffects.NetherEffects;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.client.resources.WitherStormResourceConfigManager;

public class BowelsSpecialEffects extends NetherEffects {
   public Vec3 getBrightnessDependentFogColor(Vec3 col, float dayCycle) {
      Color color = WitherStormResourceConfigManager.INSTANCE.getBowelsFogColor().orElse(null);
      return color != null
         ? new Vec3((double)((float)color.getRed() / 255.0F), (double)((float)color.getGreen() / 255.0F), (double)((float)color.getBlue() / 255.0F))
         : super.getBrightnessDependentFogColor(col, dayCycle);
   }
}
