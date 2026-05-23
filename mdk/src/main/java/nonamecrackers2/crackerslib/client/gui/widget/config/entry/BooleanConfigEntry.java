package nonamecrackers2.crackerslib.client.gui.widget.config.entry;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;
import nonamecrackers2.crackerslib.client.gui.widget.CyclableButton;

public class BooleanConfigEntry extends ConfigEntry<Boolean, CyclableButton<Boolean>> {
   public BooleanConfigEntry(Minecraft mc, String modid, Type type, String path, ModConfigSpec spec, Runnable onValueUpdated) {
      super(mc, modid, type, path, spec, onValueUpdated);
   }

   protected CyclableButton<Boolean> buildWidget(int x, int y, int width, int height) {
      CyclableButton<Boolean> button = new CyclableButton<>(
         x + 6, y, 60, Lists.newArrayList(new Boolean[]{Boolean.FALSE, Boolean.TRUE}), (Boolean)this.value.get(), value -> {
            Component message;
            if (value) {
               message = Component.m_237113_("ON").m_130940_(ChatFormatting.GREEN);
            } else {
               message = Component.m_237113_("OFF").m_130940_(ChatFormatting.RED);
            }

            return message;
         }
      );
      button.setResponder(val -> this.getValueUpdatedResponder().run());
      return button;
   }

   protected Boolean getCurrentValue() {
      return this.widget.getValue();
   }

   protected void setCurrentValue(Boolean value) {
      this.widget.setValue(value);
   }
}
