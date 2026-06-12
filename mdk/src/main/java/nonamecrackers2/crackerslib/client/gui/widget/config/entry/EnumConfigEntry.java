package nonamecrackers2.crackerslib.client.gui.widget.config.entry;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;
import nonamecrackers2.crackerslib.client.gui.widget.CyclableButton;

public class EnumConfigEntry<T extends Enum<T>> extends ConfigEntry<T, CyclableButton<T>> {
   private final Class<T> enumClass = ((Enum)this.value.getDefault()).getDeclaringClass();

   public EnumConfigEntry(Minecraft mc, String modid, Type type, String path, ModConfigSpec spec, Runnable onValueUpdated) {
      super(mc, modid, type, path, spec, onValueUpdated);
   }

   protected CyclableButton<T> buildWidget(int x, int y, int width, int height) {
      CyclableButton<T> button = new CyclableButton<>(x + 6, y, 100, Arrays.asList(this.enumClass.getEnumConstants()), (T)this.value.get());
      button.setResponder(val -> this.getValueUpdatedResponder().run());
      return button;
   }

   protected T getCurrentValue() {
      return this.widget.get();
   }

   protected void setCurrentValue(T value) {
      this.widget.setValue(value);
   }
}
