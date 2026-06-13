package nonamecrackers2.witherstormmod.client.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.awt.Color;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import nonamecrackers2.witherstormmod.client.resources.color.ColorSet;
import nonamecrackers2.witherstormmod.client.resources.color.SkyColorSet;
import nonamecrackers2.witherstormmod.client.resources.texture.TextureSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WitherStormResourceConfigManager extends SimpleJsonResourceReloadListener {
   private static final Logger LOGGER = LogManager.getLogger("witherstormmod/WitherStormResourceConfigManager");
   private static final Gson GSON = new GsonBuilder().create();
   private static final int COLOR_CONFIG_FORMAT_VERSION = 2;
   private static final int TEXTURES_FORMAT_VERSION = 1;
   public static final WitherStormResourceConfigManager INSTANCE = new WitherStormResourceConfigManager();
   private Int2ObjectMap<ColorSet> colors = new Int2ObjectOpenHashMap();
   private Optional<Color> bowelsFogColor;
   private Int2ObjectMap<TextureSet> textures = new Int2ObjectOpenHashMap();

   public WitherStormResourceConfigManager() {
      super(GSON, "config");
      this.defaultColors();
      this.defaultTextures();
   }

   private void defaultColors() {
      for (int i = 0; i <= 7; i++) {
         this.colors.put(i, ColorSet.DEFAULT);
      }

      this.bowelsFogColor = Optional.empty();
   }

   private void defaultTextures() {
      for (int i = 0; i <= 7; i++) {
         this.textures.put(i, TextureSet.DEFAULT);
      }
   }

   protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
      Int2ObjectMap<ColorSet.Builder> colorSetsByPhase = new Int2ObjectOpenHashMap();
      Int2ObjectMap<TextureSet> textureSetsByPhase = new Int2ObjectOpenHashMap();

      try {
         Iterator built = files.entrySet().iterator();

         label113:
         while (true) {
            JsonObject obj;
            label106:
            while (true) {
               if (!built.hasNext()) {
                  break label113;
               }

               Entry<ResourceLocation, JsonElement> entry = (Entry<ResourceLocation, JsonElement>)built.next();
               obj = entry.getValue().getAsJsonObject();
               String var9 = entry.getKey().getPath();
               switch (var9) {
                  case "colors":
                     int version = 1;
                     if (obj.has("format_version")) {
                        version = GsonHelper.getAsInt(obj, "format_version");
                     }

                     if (version < 2) {
                        LOGGER.info(
                           "Your colors.json file is out of date. Please update in order for your colors.json file to work correctly. More info here: https://github.com/nonamecrackers2/crackers-wither-storm-mod/wiki/Advanced-Modification"
                        );
                     }

                     if (obj.has("bowels_fog")) {
                        this.bowelsFogColor = Optional.of(colorFromJsonNoAlpha(GsonHelper.getAsJsonObject(obj, "bowels_fog")));
                     } else {
                        this.bowelsFogColor = Optional.empty();
                     }

                     LOGGER.debug("Populating default color sets for all phases");
                     ColorSet.Builder defaultColors = ColorSet.builder();
                     populateColorSetBuilder(defaultColors, obj);
                     colorSetsByPhase.put(0, defaultColors);

                     for (int i = 1; i <= 7; i++) {
                        colorSetsByPhase.put(i, defaultColors.copy());
                     }

                     if (obj.has("sky_colors")) {
                        if (version < 2) {
                           LOGGER.debug("Doing compat stuff with older formats");
                           JsonObject phases = GsonHelper.getAsJsonObject(obj, "sky_colors");
                           int i = 5;

                           while (true) {
                              if (i > 7) {
                                 break label106;
                              }

                              JsonObject phase = GsonHelper.getAsJsonObject(phases, String.valueOf(i));
                              ((ColorSet.Builder)colorSetsByPhase.get(i)).setSkyColors(getColorSetFromJson(phase));
                              i++;
                           }
                        }

                        LOGGER.info(
                           "Looks like you're trying to use an older format for the sky color set with the newer format version. Please see the updated format: https://github.com/nonamecrackers2/crackers-wither-storm-mod/wiki/Advanced-Modification"
                        );
                     } else if (obj.has("by_phase")) {
                        LOGGER.debug("Populating per-phase color configuration");
                        JsonObject byPhase = GsonHelper.getAsJsonObject(obj, "by_phase");
                        colorSetsByPhase.int2ObjectEntrySet().forEach(e -> {
                           String phaseStrx = String.valueOf(e.getIntKey());
                           if (byPhase.has(phaseStrx)) {
                              LOGGER.debug("Found phase: {}", e.getIntKey());
                              populateColorSetBuilder((ColorSet.Builder)e.getValue(), GsonHelper.getAsJsonObject(byPhase, phaseStrx));
                           }
                        });
                     }
                  case "textures":
                     break label106;
               }
            }

            int versionx = 1;
            if (obj.has("format_version")) {
               versionx = GsonHelper.getAsInt(obj, "format_version");
            }

            if (versionx < 1) {
               LOGGER.info(
                  "Your textures.json file is out of date. Please update in order for your textures.json file to work correctly. More info here: https://github.com/nonamecrackers2/crackers-wither-storm-mod/wiki/Advanced-Modification"
               );
            }

            for (int i = 0; i <= 7; i++) {
               String phaseStr = String.valueOf(i);
               if (obj.has(phaseStr)) {
                  JsonObject phase = GsonHelper.getAsJsonObject(obj, phaseStr);
                  textureSetsByPhase.put(i, TextureSet.fromJson(phase));
               } else {
                  textureSetsByPhase.put(i, TextureSet.DEFAULT);
               }
            }
         }
      } catch (JsonSyntaxException var16) {
         LOGGER.warn("Failed to read 'colors.json'", var16);
      }

      if (colorSetsByPhase.isEmpty()) {
         this.defaultColors();
      } else {
         Int2ObjectMap<ColorSet> built = new Int2ObjectOpenHashMap();
         ObjectIterator var18 = colorSetsByPhase.int2ObjectEntrySet().iterator();

         while (var18.hasNext()) {
            it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry<ColorSet.Builder> entry = (it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry<ColorSet.Builder>)var18.next();
            built.put(entry.getIntKey(), ((ColorSet.Builder)entry.getValue()).build());
         }

         this.colors = built;
         LOGGER.info("Found a colors.json file");
      }

      if (textureSetsByPhase.isEmpty()) {
         this.defaultTextures();
      } else {
         this.textures = textureSetsByPhase;
         LOGGER.info("Found a textures.json file");
      }
   }

   public ColorSet getColorSetByPhase(int phase) {
      assertIsWithinPhaseRange(phase);
      return (ColorSet)this.colors.get(phase);
   }

   public TextureSet getTextureSetByPhase(int phase) {
      assertIsWithinPhaseRange(phase);
      return (TextureSet)this.textures.get(phase);
   }

   public Optional<Color> getBowelsFogColor() {
      return this.bowelsFogColor;
   }

   private static void assertIsWithinPhaseRange(int phase) {
      if (phase < 0 || phase > 7) {
         throw new IllegalArgumentException("Phase outside of range: 0 ~ 7");
      }
   }

   private static void populateColorSetBuilder(ColorSet.Builder builder, JsonObject obj) throws JsonSyntaxException {
      if (obj.has("tractor_beams")) {
         builder.setTractorBeamColor(colorFromJsonNoAlpha(GsonHelper.getAsJsonObject(obj, "tractor_beams")));
         LOGGER.debug("Found tractor beam color");
      }

      if (obj.has("tractor_beams_night")) {
         builder.setTractorBeamNightColor(colorFromJsonNoAlpha(GsonHelper.getAsJsonObject(obj, "tractor_beams_night")));
         LOGGER.debug("Found night time tractor beam color");
      }

      if (obj.has("night_shine")) {
         builder.setNightShineColor(colorFromJsonAlpha(GsonHelper.getAsJsonObject(obj, "night_shine")));
         LOGGER.debug("Found night shine color");
      }

      builder.setSkyColors(getColorSetFromJson(obj));
   }

   private static Color colorFromJsonNoAlpha(JsonObject object) throws JsonSyntaxException {
      int r = GsonHelper.getAsInt(object, "red");
      int g = GsonHelper.getAsInt(object, "green");
      int b = GsonHelper.getAsInt(object, "blue");
      return new Color(r, g, b);
   }

   private static Color colorFromJsonAlpha(JsonObject object) throws JsonSyntaxException {
      int r = GsonHelper.getAsInt(object, "red");
      int g = GsonHelper.getAsInt(object, "green");
      int b = GsonHelper.getAsInt(object, "blue");
      int a = 255;
      if (object.has("alpha")) {
         a = GsonHelper.getAsInt(object, "alpha");
      }

      return new Color(r, g, b, a);
   }

   private static SkyColorSet getColorSetFromJson(JsonObject object) throws JsonSyntaxException {
      SkyColorSet.Builder builder = SkyColorSet.builder();
      if ((!object.has("night") || object.has("day")) && (object.has("night") || !object.has("day"))) {
         if (object.has("night") && object.has("day")) {
            LOGGER.debug("Found day and night sky color set");
            JsonObject night = GsonHelper.getAsJsonObject(object, "night");
            JsonObject day = GsonHelper.getAsJsonObject(object, "day");
            if (day.has("sky_darken")) {
               builder.setSkyColor(colorFromJsonNoAlpha(GsonHelper.getAsJsonObject(day, "sky_darken")));
            }

            if (day.has("sky_darken_clouds")) {
               builder.setCloudColor(colorFromJsonNoAlpha(GsonHelper.getAsJsonObject(day, "sky_darken_clouds")));
            }

            if (day.has("sky_darken_fog")) {
               builder.setFogColor(colorFromJsonNoAlpha(GsonHelper.getAsJsonObject(day, "sky_darken_fog")));
            }

            if (night.has("sky_darken")) {
               builder.setNightSkyColor(colorFromJsonNoAlpha(GsonHelper.getAsJsonObject(night, "sky_darken")));
            }

            if (night.has("sky_darken_clouds")) {
               builder.setNightCloudColor(colorFromJsonNoAlpha(GsonHelper.getAsJsonObject(night, "sky_darken_clouds")));
            }

            if (night.has("sky_darken_fog")) {
               builder.setNightFogColor(colorFromJsonNoAlpha(GsonHelper.getAsJsonObject(night, "sky_darken_fog")));
            }
         } else {
            LOGGER.debug("Found sky color set");
            if (object.has("sky_darken")) {
               builder.setSkyColor(colorFromJsonNoAlpha(GsonHelper.getAsJsonObject(object, "sky_darken")));
            }

            if (object.has("sky_darken_clouds")) {
               builder.setCloudColor(colorFromJsonNoAlpha(GsonHelper.getAsJsonObject(object, "sky_darken_clouds")));
            }

            if (object.has("sky_darken_fog")) {
               builder.setFogColor(colorFromJsonNoAlpha(GsonHelper.getAsJsonObject(object, "sky_darken_fog")));
            }
         }

         return builder.build();
      } else {
         throw new JsonSyntaxException("Must contain both 'day' and 'night' entries, not just one");
      }
   }
}
