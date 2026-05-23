package nonamecrackers2.crackerslib.common.compat;

import com.google.common.collect.Lists;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import net.neoforged.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompatHelper {
   private static final Logger LOGGER = LogManager.getLogger("crackerslib/CompatHelper");
   private static final List<String> COMPAT_ERRORS = Lists.newArrayList();
   private static boolean optifineLoaded;
   private static boolean vivecraftStandaloneLoaded;

   public static void checkForLoaded() {
      try {
         Class.forName("net.optifine.Config");
         optifineLoaded = true;
      } catch (ClassNotFoundException var2) {
      }

      try {
         Class.forName("org.vivecraft.settings.VRSettings");
         vivecraftStandaloneLoaded = true;
      } catch (ClassNotFoundException var1) {
      }
   }

   public static boolean areShadersRunning() {
      try {
         if (isOculusLoaded()) {
            Class<?> clazz = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            Method instanceGetter = clazz.getMethod("getInstance");
            Object irisApi = instanceGetter.invoke(null);
            return (Boolean)irisApi.getClass().getMethod("isShaderPackInUse").invoke(irisApi);
         } else if (isOptifineLoaded()) {
            Class<?> clazz = Class.forName("net.optifine.Config");
            Method method = clazz.getMethod("isShaders");
            return (Boolean)method.invoke(null);
         } else {
            return false;
         }
      } catch (IllegalArgumentException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException var3) {
         doErrorFor("shaders", () -> {
            LOGGER.error("Failed to check if shaders are enabled:");
            var3.printStackTrace();
         });
         return false;
      }
   }

   public static boolean isVrActive() {
      if (ModList.get().isLoaded("vivecraft")) {
         try {
            Class<?> clazz = Class.forName("org.vivecraft.api_beta.client.VivecraftClientAPI");
            Method instanceGetter = clazz.getMethod("getInstance");
            Object vivecraftApi = instanceGetter.invoke(null);
            return (Boolean)vivecraftApi.getClass().getMethod("isVrActive").invoke(vivecraftApi);
         } catch (IllegalArgumentException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException var3) {
            doErrorFor("vr", () -> {
               LOGGER.error("Failed to check if VR is active:");
               var3.printStackTrace();
            });
            return false;
         }
      } else {
         return vivecraftStandaloneLoaded;
      }
   }

   public static boolean isVivecraftLoaded() {
      return vivecraftStandaloneLoaded || ModList.get().isLoaded("vivecraft");
   }

   public static boolean isOculusLoaded() {
      return ModList.get().isLoaded("oculus");
   }

   public static boolean isOptifineLoaded() {
      return optifineLoaded;
   }

   public static boolean isSodiumLoaded() {
      return ModList.get().isLoaded("rubidium");
   }

   private static final void doErrorFor(String mod, Runnable runnable) {
      if (!COMPAT_ERRORS.contains(mod)) {
         runnable.run();
         COMPAT_ERRORS.add(mod);
      }
   }
}
