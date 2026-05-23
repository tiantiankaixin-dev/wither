package nonamecrackers2.crackerslib.client.event.impl;

import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
// TODO_MIG[CANCELABLE]: removed; class must implement ICancellableEvent
import net.neoforged.bus.api.Event;
import net.neoforged.fml.config.ModConfig.Type;
import nonamecrackers2.crackerslib.common.config.ConfigHelper;

// TODO_MIG[CANCELABLE]: implement ICancellableEvent on this class instead
public class AddConfigEntryToMenuEvent extends Event {
   private final String modid;
   private final Type type;
   private final String path;

   public AddConfigEntryToMenuEvent(String modid, Type type, String path) {
      this.modid = modid;
      this.type = type;
      this.path = path;
   }

   public String getModId() {
      return this.modid;
   }

   public Type getType() {
      return this.type;
   }

   public String getValuePath() {
      return this.path;
   }

   public boolean isValue(ConfigValue<?> value) {
      return this.path.equals(ConfigHelper.DOT_JOINER.join(value.getPath()));
   }
}
