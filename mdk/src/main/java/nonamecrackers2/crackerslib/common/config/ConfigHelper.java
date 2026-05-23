package nonamecrackers2.crackerslib.common.config;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.ValueSpec;

public abstract class ConfigHelper {
   public static final Splitter DOT_SPLITTER = Splitter.on(".");
   public static final Joiner DOT_JOINER = Joiner.on('.');
   protected final Builder builder;
   protected final String modid;

   protected ConfigHelper(Builder builder, String modid) {
      this.builder = builder;
      this.modid = modid;
   }

   protected <T> ConfigValue<T> createValue(T value, String name, boolean restart, String description) {
      return this.defaultProperties(name, description, restart, value).define(name, value);
   }

   protected ConfigValue<Double> createRangedDoubleValue(double value, double min, double max, String name, boolean restart, String description) {
      return this.defaultProperties(name, description, restart, value).defineInRange(name, value, min, max);
   }

   protected ConfigValue<Integer> createRangedIntValue(int value, int min, int max, String name, boolean restart, String description) {
      return this.defaultProperties(name, description, restart, value).defineInRange(name, value, min, max);
   }

   protected ConfigValue<Long> createRangedLongValue(long value, long min, long max, String name, boolean restart, String description) {
      return this.defaultProperties(name, description, restart, value).defineInRange(name, value, min, max);
   }

   protected <T extends Enum<T>> ConfigValue<T> createEnumValue(T value, String name, boolean restart, String description) {
      return this.defaultProperties(name, description, restart, value).defineEnum(name, value);
   }

   protected <T> ConfigValue<List<? extends T>> createListValue(
      Class<T> valueClass, Supplier<List<? extends T>> value, Predicate<T> validator, String name, boolean restart, String description
   ) {
      return this.defaultProperties(name, description, restart, null)
         .defineListAllowEmpty(split(name), value, obj -> valueClass.isAssignableFrom(obj.getClass()) && validator.test((T)obj));
   }

   protected <T> Builder defaultProperties(String name, String desc, boolean restart, @Nullable T defaultValue) {
      if (restart) {
         this.builder.worldRestart().comment(desc);
         this.builder.comment("Requires restart.");
      } else {
         this.builder.comment(desc + ".");
      }

      if (defaultValue != null) {
         this.builder.comment("Default: " + defaultValue.toString());
      }

      this.builder.translation("gui." + this.modid + ".config." + name + ".description");
      return this.builder;
   }

   private static List<String> split(String path) {
      return Lists.newArrayList(DOT_SPLITTER.split(path));
   }

   public static Map<String, ConfigValue<?>> getAllValues(ModConfigSpec spec) {
      return searchForValues("", spec.getValues().valueMap())
         .entrySet()
         .stream()
         .map(e -> Map.entry(e.getKey(), (ConfigValue)e.getValue()))
         .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
   }

   public static Map<String, ValueSpec> getAllSpecs(ModConfigSpec spec) {
      return searchForValues("", spec.getSpec().valueMap())
         .entrySet()
         .stream()
         .map(e -> Map.entry(e.getKey(), (ValueSpec)e.getValue()))
         .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
   }

   private static Map<String, Object> searchForValues(String previousPath, Map<String, Object> values) {
      Map<String, Object> map = Maps.newHashMap();

      for (Entry<String, Object> entry : values.entrySet()) {
         String path = entry.getKey();
         if (!previousPath.isEmpty()) {
            path = previousPath + "." + path;
         }

         if (entry.getValue() instanceof UnmodifiableConfig next) {
            map.putAll(searchForValues(path, next.valueMap()));
         } else {
            map.put(path, entry.getValue());
         }
      }

      return map;
   }
}
