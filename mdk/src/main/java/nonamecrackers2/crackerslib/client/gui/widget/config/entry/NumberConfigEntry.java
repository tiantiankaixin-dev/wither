package nonamecrackers2.crackerslib.client.gui.widget.config.entry;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;

public abstract class NumberConfigEntry<T extends Number> extends ConfigEntry<T, EditBox> {
   public NumberConfigEntry(Minecraft mc, String modid, Type type, String path, ModConfigSpec spec, Runnable onValueUpdated) {
      super(mc, modid, type, path, spec, onValueUpdated);
   }

   protected EditBox buildWidget(int x, int y, int width, int height) {
      EditBox box = new EditBox(this.mc.font, x + 6, y + height / 2 - 10, 60, 20, CommonComponents.EMPTY);
      box.setFocused(String.valueOf(this.value.get()));
      box.setResponder(value -> {
         try {
            this.getValueUpdatedResponder().run();
            T val = this.parseValue(value);
            if (this.valueSpec.test(val)) {
               this.widget.setEditable(-1);
            } else {
               this.widget.setEditable(ChatFormatting.RED.getColor());
            }
         } catch (NumberFormatException var3x) {
            this.widget.setEditable(ChatFormatting.RED.getColor());
         }
      });
      return box;
   }

   protected T getCurrentValue() {
      try {
         return this.parseValue(this.widget.get());
      } catch (NumberFormatException var2) {
         return (T)this.value.get();
      }
   }

   protected void setCurrentValue(T value) {
      this.widget.setFocused(String.valueOf(value));
   }

   protected abstract T parseValue(String var1) throws NumberFormatException;
}
