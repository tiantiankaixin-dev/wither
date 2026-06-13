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
      EditBox box = new EditBox(this.mc.font, x + 6, y + height / 2 - 10, 200, 20, CommonComponents.EMPTY);
      if (((List)this.value.getDefault()).size() > 0) {
         box.setHint(Component.literal(String.valueOf(((List)this.value.getDefault()).get(0))).withStyle(ChatFormatting.DARK_GRAY));
      }

      box.setMaxLength(500);
      box.setValue(this.compileListToString((List<?>)this.value.get()));
      box.setResponder(value -> {
         try {
            this.getValueUpdatedResponder().run();
            List<?> val = this.compileValuesFromString(value);
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
         return this.compileValuesFromString(this.widget.getValue());
      } catch (NumberFormatException var2) {
         return (List<?>)this.value.get();
      }
   }

   protected void setCurrentValue(List<?> value) {
      this.widget.setValue(this.compileListToString(value));
   }

   @FunctionalInterface
   public interface ValueParser<T> {
      T parse(String var1) throws NumberFormatException;
   }
}
