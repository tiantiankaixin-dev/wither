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
      EditBox box = new EditBox(this.mc.f_91062_, x + 6, y + height / 2 - 10, 60, 20, CommonComponents.f_237098_);
      box.m_94144_(String.valueOf(this.value.get()));
      box.m_94151_(value -> {
         try {
            this.getValueUpdatedResponder().run();
            T val = this.parseValue(value);
            if (this.valueSpec.test(val)) {
               this.widget.m_94202_(-1);
            } else {
               this.widget.m_94202_(ChatFormatting.RED.m_126665_());
            }
         } catch (NumberFormatException var3x) {
            this.widget.m_94202_(ChatFormatting.RED.m_126665_());
         }
      });
      return box;
   }

   protected T getCurrentValue() {
      try {
         return this.parseValue(this.widget.m_94155_());
      } catch (NumberFormatException var2) {
         return (T)this.value.get();
      }
   }

   protected void setCurrentValue(T value) {
      this.widget.m_94144_(String.valueOf(value));
   }

   protected abstract T parseValue(String var1) throws NumberFormatException;
}
