package nonamecrackers2.crackerslib.client.event.impl;

import javax.annotation.Nullable;
// TODO_MIG[CANCELABLE]: removed; class must implement ICancellableEvent
import net.neoforged.bus.api.Event;
import net.neoforged.fml.config.ModConfig.Type;

// TODO_MIG[CANCELABLE]: implement ICancellableEvent on this class instead
public class OnConfigScreenOpened extends Event {
   private final String modid;
   private final Type type;
   @Nullable
   private String initialPath;

   public OnConfigScreenOpened(String modid, Type type) {
      this.modid = modid;
      this.type = type;
   }

   public String getModId() {
      return this.modid;
   }

   public Type getType() {
      return this.type;
   }

   @Nullable
   public String getInitialPath() {
      return this.initialPath;
   }

   public void setInitialPath(String path) {
      this.initialPath = path;
   }
}
