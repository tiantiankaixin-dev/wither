package nonamecrackers2.crackerslib.client.gui.widget.config.entry;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;

public class DoubleConfigEntry extends NumberConfigEntry<Double> {
   public DoubleConfigEntry(Minecraft mc, String modid, Type type, String path, ModConfigSpec spec, Runnable onValueUpdated) {
      super(mc, modid, type, path, spec, onValueUpdated);
   }

   protected Double parseValue(String contents) throws NumberFormatException {
      return Double.parseDouble(contents);
   }
}
