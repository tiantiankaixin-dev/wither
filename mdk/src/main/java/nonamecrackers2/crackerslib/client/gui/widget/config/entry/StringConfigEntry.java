package nonamecrackers2.crackerslib.client.gui.widget.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;

public class StringConfigEntry extends ConfigEntry<String, EditBox> {
   public StringConfigEntry(Minecraft mc, String modid, Type type, String path, ModConfigSpec spec, Runnable onValueUpdated) {
      super(mc, modid, type, path, spec, onValueUpdated);
   }

   protected EditBox buildWidget(int x, int y, int width, int height) {
      EditBox box = new EditBox(this.mc.font, x + 6, y + height / 2 - 10, 60, 20, CommonComponents.EMPTY);
      box.setValue((String)this.value.get());
      box.setResponder(value -> this.getValueUpdatedResponder().run());
      return box;
   }

   protected String getCurrentValue() {
      return this.widget.getValue();
   }

   protected void setCurrentValue(String value) {
      this.widget.setValue(value);
   }
}
