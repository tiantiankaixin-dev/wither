package nonamecrackers2.crackerslib.example.client.event.common.config;

import com.google.common.collect.Lists;
import java.util.List;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import nonamecrackers2.crackerslib.common.config.ConfigHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ExampleConfig {
   public static final ExampleConfig.ClientConfig CLIENT;
   public static final ModConfigSpec CLIENT_SPEC;

   static {
      Pair<ExampleConfig.ClientConfig, ModConfigSpec> clientPair = new Builder().configure(ExampleConfig.ClientConfig::new);
      CLIENT = (ExampleConfig.ClientConfig)clientPair.getLeft();
      CLIENT_SPEC = (ModConfigSpec)clientPair.getRight();
   }

   public static class ClientConfig extends ConfigHelper {
      public final ConfigValue<Boolean> exampleBoolean = this.createValue(true, "exampleBoolean", false, "A simple boolean config value");
      public final ConfigValue<Integer> exampleInteger;
      public final ConfigValue<Double> exampleDouble;
      public final ConfigValue<String> exampleString;
      public final ConfigValue<ExampleConfig.ExampleEnum> exampleEnum;
      public final ConfigValue<List<? extends String>> exampleListString;
      public final ConfigValue<List<? extends Integer>> exampleListInteger;
      public final ConfigValue<List<? extends Double>> exampleListDouble;

      public ClientConfig(Builder builder) {
         super(builder, "crackerslib");
         builder.comment("Numbers").push("numbers");
         this.exampleInteger = this.createRangedIntValue(10, 0, 100, "exampleInteger", true, "A simple ranged integer value");
         this.exampleDouble = this.createRangedDoubleValue(0.5, 0.0, 1.0, "exampleDouble", true, "A simple ranged value with decimals");
         builder.pop();
         builder.comment("Extra").push("extra");
         this.exampleString = this.createValue("hello!", "exampleString", false, "A simple string value");
         builder.pop();
         this.exampleEnum = this.createEnumValue(ExampleConfig.ExampleEnum.HEY, "exampleEnum", false, "A simple enum config value");
         builder.comment("Lists").push("list");
         this.exampleListString = this.createListValue(
            String.class,
            () -> Lists.newArrayList(new String[]{"heres", "some", "default", "values"}),
            val -> StringUtils.isAllLowerCase(val),
            "exampleListString",
            false,
            "An example list of strings that must all be lowercase"
         );
         this.exampleListInteger = this.createListValue(
            Integer.class,
            () -> Lists.newArrayList(new Integer[]{2, 3, 4, 5}),
            val -> val >= 2,
            "exampleListInteger",
            false,
            "An example list of integers that must be greater than or equal to 2"
         );
         builder.comment("Category Example").push("category_example");
         this.exampleListDouble = this.createListValue(
            Double.class, () -> Lists.newArrayList(new Double[]{0.0, 1.0, 2.0, 3.0}), val -> true, "exampleListDouble", false, "An example list of doubles"
         );
         builder.pop();
         builder.pop();
      }
   }

   public static enum ExampleEnum {
      HEY,
      HOWS,
      IT,
      GOING;
   }
}
