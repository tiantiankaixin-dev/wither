package nonamecrackers2.crackerslib.common.config.preset;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.config.ModConfig.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigPresets {
   @Nullable
   public static Map<String, ConfigPresets.Presets> presetsByMod;
   private static final Logger LOGGER = LogManager.getLogger("crackerslib/ConfigPresets");

   @Nullable
   public static ConfigPresets.Presets getPresetsForModId(String id) {
      Objects.requireNonNull(presetsByMod, "Presets have not yet been gathered!");
      return presetsByMod.get(id);
   }

   public static void gatherPresets() {
      if (presetsByMod != null) {
         throw new IllegalStateException("Presets have already been gathered!");
      } else {
         Builder<String, ConfigPresets.Presets> presetsBuilder = ImmutableMap.builder();
         List<RegisterConfigPresetsEvent> postedEvents = Lists.newArrayList();
         ModLoader.runEventGenerator(mod -> {
            RegisterConfigPresetsEvent event = new RegisterConfigPresetsEvent(mod.getModId());
            postedEvents.add(event);
            return event;
         });
         postedEvents.forEach(e -> {
            Multimap<Type, ConfigPreset> presets = e.buildPresets();
            List<String> excluded = e.buildExcludedConfigOptions();
            if (!presets.isEmpty()) {
               presetsBuilder.put(e.getModId(), new ConfigPresets.Presets(presets, excluded));
            }
         });
         presetsByMod = presetsBuilder.build();
         LOGGER.debug("Gathered presets for {} mod(s)", presetsByMod.size());
      }
   }

   public static class Presets {
      private final Multimap<Type, ConfigPreset> presetsByType;
      private final List<String> excludedConfigOptions;

      Presets(Multimap<Type, ConfigPreset> presetsByType, List<String> excludedConfigOptions) {
         this.presetsByType = presetsByType;
         this.excludedConfigOptions = excludedConfigOptions;
      }

      public Collection<ConfigPreset> getPresetsForType(Type type) {
         return this.presetsByType.get(type);
      }

      public List<String> getExcludedConfigOptions() {
         return this.excludedConfigOptions;
      }
   }
}
