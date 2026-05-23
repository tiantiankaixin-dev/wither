package nonamecrackers2.crackerslib.common.util.data;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ValueSpec;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

public class ConfigLangGeneratorHelper {
   public static void langForSpec(String modid, ModConfigSpec spec, LanguageProvider provider, ConfigLangGeneratorHelper.Info infoType) {
      forValues(modid, spec.getSpec().valueMap(), provider, infoType);
   }

   public static void langForSpec(String modid, ModConfigSpec spec, LanguageProvider provider) {
      langForSpec(modid, spec, provider, ConfigLangGeneratorHelper.Info.ALL);
   }

   private static void forValues(String modid, Map<String, Object> values, LanguageProvider provider, ConfigLangGeneratorHelper.Info infoType) {
      for (Entry<String, Object> entry : values.entrySet()) {
         String name = entry.getKey();
         if (entry.getValue() instanceof ValueSpec spec) {
            String properTitle = StringUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(name), " "));
            tryAdd("gui." + modid + ".config." + name + ".title", properTitle, provider);
            String desc = spec.getComment();
            String finalDesc = desc;
            Pattern pattern = infoType.getPattern();
            if (pattern != null) {
               finalDesc = pattern.matcher(desc).replaceAll("");
            }

            tryAdd(spec.getTranslationKey(), finalDesc, provider);
         } else if (entry.getValue() instanceof UnmodifiableConfig category) {
            String[] split = name.split("_");

            for (int i = 0; i < split.length; i++) {
               split[i] = StringUtils.capitalize(split[i]);
            }

            String properTitle = StringUtils.join(split, " ");
            tryAdd("gui." + modid + ".config.category." + name + ".title", properTitle, provider);
            forValues(modid, category.valueMap(), provider, infoType);
         }
      }
   }

   private static void tryAdd(String key, String entry, LanguageProvider provider) {
      try {
         provider.add(key, entry);
      } catch (IllegalStateException var4) {
      }
   }

   public static enum Info {
      ALL(null),
      ONLY_RANGE("(?!\\nRange)\\n.*?(?=\\nRange|$)"),
      NONE_EXTRA("\\n.*");

      private final Pattern pattern;

      private Info(@Nullable String regex) {
         if (regex != null) {
            this.pattern = Pattern.compile(regex, 8);
         } else {
            this.pattern = null;
         }
      }

      @Nullable
      public Pattern getPattern() {
         return this.pattern;
      }
   }
}
