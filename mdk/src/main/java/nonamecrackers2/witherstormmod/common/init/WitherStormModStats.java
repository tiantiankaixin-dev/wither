package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class WitherStormModStats {
   public static ResourceLocation INTERACT_WITH_SUPER_BEACON;

   public static void register() {
      INTERACT_WITH_SUPER_BEACON = makeCustomStat("interact_with_super_beacon", StatFormatter.DEFAULT);
   }

   private static ResourceLocation makeCustomStat(String id, StatFormatter formatter) {
      ResourceLocation rl = new ResourceLocation("witherstormmod", id);
      Registry.register(BuiltInRegistries.CUSTOM_STAT, rl, rl);
      Stats.CUSTOM.get(rl, formatter);
      return rl;
   }
}
