package nonamecrackers2.crackerslib.client.gui.widget.config.entry;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;

public class ListConfigEntry extends ConfigEntry<List<?>, EditBox> {
   protected static final String PATH_SPLITTER = ", ";
   protected static final Joiner JOINER = Joiner.on(", ");
   protected static final Splitter SPLITTER = Splitter.on(", ");
   private final ListConfigEntry.ValueParser<?> parser;

   public ListConfigEntry(
      Minecraft mc, String modid, Type type, String path, ModConfigSpec spec, Runnable onValueUpdated, ListConfigEntry.ValueParser<?> parser
   ) {
      super(mc, modid, type, path, spec, onValueUpdated);
      this.parser = parser;
   }

   protected EditBox buildWidget(int x, int y, int width, int height) {
      EditBox box = new EditBox(this.mc.f_91062_, x + 6, y + height / 2 - 10, 200, 20, CommonComponents.f_237098_);
      if (((List)this.value.getDefault()).size() > 0) {
         box.m_257771_(Component.m_237113_(String.valueOf(((List)this.value.getDefault()).get(0))).m_130940_(ChatFormatting.DARK_GRAY));
      }

      box.m_94199_(500);
      box.m_94144_(this.compileListToString((List<?>)this.value.get()));
      box.m_94151_(value -> {
         try {
            this.getValueUpdatedResponder().run();
            List<?> val = this.compileValuesFromString(value);
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

   protected String compileListToString(List<?> values) {
      return JOINER.join(values);
   }

   protected List<?> compileValuesFromString(String values) throws NumberFormatException {
      List<Object> valuesList = Lists.newArrayList();

      for (String val : SPLITTER.split(values)) {
         valuesList.add(this.parser.parse(val));
      }

      return valuesList;
   }

   protected List<?> getCurrentValue() {
      try {
         return this.compileValuesFromString(this.widget.m_94155_());
      } catch (NumberFormatException var2) {
         return (List<?>)this.value.get();
      }
   }

   protected void setCurrentValue(List<?> value) {
      this.widget.m_94144_(this.compileListToString(value));
   }

   @FunctionalInterface
   public interface ValueParser<T> {
      T parse(String var1) throws NumberFormatException;
   }
}
