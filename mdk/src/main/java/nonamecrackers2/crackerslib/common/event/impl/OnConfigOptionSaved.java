package nonamecrackers2.crackerslib.common.event.impl;

import javax.annotation.Nullable;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.config.ModConfig.Type;

public class OnConfigOptionSaved<T> extends Event {
   private final String modid;
   private final Type type;
   private final OnConfigOptionSaved.Source source;
   private final ConfigValue<T> config;
   private final T newValue;
   private final boolean didValueChange;
   @Nullable
   private T override;

   public OnConfigOptionSaved(String modid, Type type, OnConfigOptionSaved.Source source, ConfigValue<T> config, T newValue, boolean didValueChange) {
      this.modid = modid;
      this.type = type;
      this.source = source;
      this.config = config;
      this.newValue = newValue;
      this.didValueChange = didValueChange;
   }

   public String getModId() {
      return this.modid;
   }

   public Type getType() {
      return this.type;
   }

   public OnConfigOptionSaved.Source getSource() {
      return this.source;
   }

   public T getNewValue() {
      return this.newValue;
   }

   public ConfigValue<T> getConfigOption() {
      return this.config;
   }

   public void overrideValue(T value) {
      this.override = value;
   }

   public T getOverrideValue() {
      return this.override;
   }

   public boolean didValueChange() {
      return this.didValueChange;
   }

   public static enum Source {
      CONFIG_SCREEN,
      COMMAND;
   }
}
