package nonamecrackers2.crackerslib.client.event.impl;

import com.google.common.collect.Maps;
import java.util.Map;
import net.neoforged.neoforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.IModBusEvent;
import nonamecrackers2.crackerslib.client.config.ConfigHomeScreenFactory;

public class RegisterConfigScreensEvent extends Event implements IModBusEvent {
   private final String modid;

   public RegisterConfigScreensEvent(String modid) {
      this.modid = modid;
   }

   public RegisterConfigScreensEvent.Builder builder(ConfigHomeScreenFactory factory) {
      return new RegisterConfigScreensEvent.Builder(this.modid, factory);
   }

   public class Builder {
      private final Map<Type, ModConfigSpec> specsByType = Maps.newEnumMap(Type.class);
      private final String modid;
      private final ConfigHomeScreenFactory factory;

      private Builder(String modid, ConfigHomeScreenFactory factory) {
         this.modid = modid;
         this.factory = factory;
      }

      public RegisterConfigScreensEvent.Builder addSpec(Type type, ModConfigSpec spec) {
         if (this.specsByType.containsKey(type)) {
            throw new IllegalArgumentException("Type is already registered");
         } else {
            this.specsByType.put(type, spec);
            return this;
         }
      }

      public void register() {
         ModList.get()
            .getModContainerById(this.modid)
            .ifPresentOrElse(
               mod -> mod.registerExtensionPoint(
                  ConfigScreenFactory.class,
                  () -> new ConfigScreenFactory((mc, screen) -> this.factory.build(this.modid, this.specsByType, mc.f_91073_ != null, mc.m_91091_(), screen))
               ),
               () -> {
                  throw new IllegalArgumentException("Unknown mod with id '" + this.modid + "'");
               }
            );
      }
   }
}
