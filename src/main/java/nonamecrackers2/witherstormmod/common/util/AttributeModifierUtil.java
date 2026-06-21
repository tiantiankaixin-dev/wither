package nonamecrackers2.witherstormmod.common.util;

import java.util.Locale;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;

public final class AttributeModifierUtil {
   private AttributeModifierUtil() {
   }

   public static ResourceLocation id(String path) {
      return ResourceLocation.fromNamespaceAndPath("witherstormmod", sanitize(path));
   }

   public static ResourceLocation id(UUID uuid) {
      return id(uuid.toString());
   }

   private static String sanitize(String value) {
      String lower = value.toLowerCase(Locale.ROOT);
      StringBuilder builder = new StringBuilder(lower.length());

      for (int i = 0; i < lower.length(); i++) {
         char c = lower.charAt(i);
         builder.append(c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '_' || c == '-' || c == '/' || c == '.' ? c : '_');
      }

      return builder.toString();
   }
}
