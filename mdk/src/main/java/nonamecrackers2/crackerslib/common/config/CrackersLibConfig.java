package nonamecrackers2.crackerslib.common.config;

import com.google.common.collect.Lists;
import java.util.List;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class CrackersLibConfig {
   public static final CrackersLibConfig.ClientConfig CLIENT;
   public static final ModConfigSpec CLIENT_SPEC;

   static {
      Pair<CrackersLibConfig.ClientConfig, ModConfigSpec> clientPair = new Builder().configure(CrackersLibConfig.ClientConfig::new);
      CLIENT = (CrackersLibConfig.ClientConfig)clientPair.getLeft();
      CLIENT_SPEC = (ModConfigSpec)clientPair.getRight();
   }

   public static class ClientConfig extends ConfigHelper {
      public final ConfigValue<List<? extends String>> hiddenConfigMenuButtons = this.createListValue(
         String.class,
         () -> Lists.newArrayList(new String[]{"example_mod_id"}),
         v -> true,
         "hiddenConfigMenuButtons",
         false,
         "A list of mod ids that cannot have their registered config menu buttons appear in the options screen"
      );

      public ClientConfig(Builder builder) {
         super(builder, "crackerslib");
      }
   }
}
