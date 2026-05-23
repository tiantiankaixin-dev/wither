package nonamecrackers2.witherstormmod.common.resources;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import nonamecrackers2.witherstormmod.common.resources.taint.MobConversion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MobConversions extends SimpleJsonResourceReloadListener {
   private static final Gson GSON = new GsonBuilder().create();
   private static final Logger LOGGER = LogManager.getLogger();
   @Nullable
   private Map<ResourceLocation, MobConversion> mobConversions;

   public MobConversions() {
      super(GSON, "tainting/entity");
   }

   protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
      Map<ResourceLocation, MobConversion> conversions = Maps.newHashMap();

      for (Entry<ResourceLocation, JsonElement> entry : files.entrySet()) {
         ResourceLocation id = entry.getKey();

         try {
            JsonObject obj = entry.getValue().getAsJsonObject();
            EntityType<?> from = parseEntityType(GsonHelper.getAsString(obj, "from"));
            if (conversions.values().stream().anyMatch(c -> c.from().equals(from))) {
               LOGGER.warn("Duplicate mob conversion detected: {} has been registered multiple times", from);
            }

            EntityType<?> to = parseEntityType(GsonHelper.getAsString(obj, "to"));
            boolean canBeConvertedFromWitherSickness = GsonHelper.getAsBoolean(obj, "convert_from_sickness");
            conversions.put(id, new MobConversion(from, to, canBeConvertedFromWitherSickness));
         } catch (IllegalStateException | JsonSyntaxException var12) {
            LOGGER.warn("Failed to read '" + id.toString() + "'", var12);
         }
      }

      this.mobConversions = ImmutableMap.copyOf(conversions);
   }

   private static EntityType<?> parseEntityType(String rawId) throws JsonSyntaxException {
      ResourceLocation id = ResourceLocation.tryParse(rawId);
      if (id == null) {
         throw new JsonSyntaxException("Not a valid id: '" + rawId + "'");
      } else if (!NeoForgeRegistries.ENTITY_TYPES.containsKey(id)) {
         throw new JsonSyntaxException("Unknown entity with id '" + rawId + "'");
      } else {
         return (EntityType<?>)NeoForgeRegistries.ENTITY_TYPES.getValue(id);
      }
   }

   public Map<ResourceLocation, MobConversion> getConversions() {
      return Objects.requireNonNull(this.mobConversions, "Mob conversions are not loaded");
   }
}
