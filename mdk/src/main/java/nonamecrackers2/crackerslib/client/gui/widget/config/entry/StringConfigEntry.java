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
      EditBox box = new EditBox(this.mc.f_91062_, x + 6, y + height / 2 - 10, 60, 20, CommonComponents.f_237098_);
      box.m_94144_((String)this.value.get());
      box.m_94151_(value -> this.getValueUpdatedResponder().run());
      return box;
   }

   protected String getCurrentValue() {
      return this.widget.m_94155_();
   }

   protected void setCurrentValue(String value) {
      this.widget.m_94144_(value);
   }
}
