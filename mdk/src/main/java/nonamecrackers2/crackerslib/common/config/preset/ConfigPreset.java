package nonamecrackers2.crackerslib.common.config.preset;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import nonamecrackers2.crackerslib.common.config.ConfigHelper;

public record ConfigPreset(Map<String, Object> values, Component name, @Nullable Component description) {
   public boolean hasValue(String path) {
      return this.values.containsKey(path);
   }

   @Nullable
   public <T> T getValue(String path) {
      return (T)this.values.get(path);
   }

   public boolean isDefault() {
      return this.values.isEmpty();
   }

   public Component getTooltip(boolean hasShiftDown) {
      MutableComponent tooltip = Component.m_237113_(this.name().getString());
      if (!hasShiftDown) {
         tooltip.m_130946_("\n");
         tooltip.m_7220_(Component.m_237115_("gui.crackerslib.button.preset.holdShift").m_130940_(ChatFormatting.DARK_GRAY));
      } else {
         if (this.description() != null) {
            String[] components = this.description().getString().split("\n");

            for (int i = 0; i < components.length; i++) {
               tooltip.m_130946_("\n");
               tooltip.m_7220_(Component.m_237113_(components[i].trim()).m_130940_(ChatFormatting.GRAY));
            }
         }

         tooltip.m_130946_("\n");
         tooltip.m_7220_(Component.m_237115_("config.crackerslib.preset.note").m_130940_(ChatFormatting.GRAY));
      }

      return tooltip;
   }

   public static ConfigPreset.Builder builder(Component name) {
      return new ConfigPreset.Builder(name);
   }

   public static ConfigPreset defaultPreset() {
      return new ConfigPreset(
         ImmutableMap.of(),
         Component.m_237115_("config.crackerslib.preset.default.title"),
         Component.m_237115_("config.crackerslib.preset.default.description")
      );
   }

   public static class Builder {
      private final Map<String, Object> values = Maps.newHashMap();
      private final Component name;
      @Nullable
      private Component description;

      private Builder(Component name) {
         this.name = name;
      }

      public ConfigPreset.Builder setDescription(Component desc) {
         this.description = desc;
         return this;
      }

      public <T> ConfigPreset.Builder setPreset(ConfigValue<T> config, T value) {
         return this.setPreset(ConfigHelper.DOT_JOINER.join(config.getPath()), value);
      }

      public ConfigPreset.Builder setPreset(String path, Object value) {
         this.values.put(path, value);
         return this;
      }

      public ConfigPreset build() {
         return new ConfigPreset(ImmutableMap.copyOf(this.values), this.name, this.description);
      }
   }
}
