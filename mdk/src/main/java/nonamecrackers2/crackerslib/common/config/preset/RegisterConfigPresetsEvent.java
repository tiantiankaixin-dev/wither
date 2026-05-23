package nonamecrackers2.crackerslib.common.config.preset;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.IModBusEvent;
import nonamecrackers2.crackerslib.common.config.ConfigHelper;

public class RegisterConfigPresetsEvent extends Event implements IModBusEvent {
   private final Multimap<Type, ConfigPreset> presets = Multimaps.newSetMultimap(Maps.newEnumMap(Type.class), Sets::newHashSet);
   private final Builder<String> excludedConfigOptions = ImmutableList.builder();
   private final String modid;

   public RegisterConfigPresetsEvent(String modid) {
      this.modid = modid;
   }

   public void registerPreset(Type type, ConfigPreset preset) {
      this.presets.put(type, preset);
   }

   protected Multimap<Type, ConfigPreset> buildPresets() {
      return ImmutableMultimap.copyOf(this.presets);
   }

   protected List<String> buildExcludedConfigOptions() {
      return this.excludedConfigOptions.build();
   }

   public RegisterConfigPresetsEvent exclude(String path) {
      this.excludedConfigOptions.add(path);
      return this;
   }

   public RegisterConfigPresetsEvent exclude(ConfigValue<?> value) {
      return this.exclude(ConfigHelper.DOT_JOINER.join(value.getPath()));
   }

   public String getModId() {
      return this.modid;
   }
}
