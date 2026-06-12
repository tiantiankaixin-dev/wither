package nonamecrackers2.witherstormmod.common.resources;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.registries.BuiltInRegistries;
import nonamecrackers2.witherstormmod.common.resources.taint.SingleBlockTaintRecipe;
import nonamecrackers2.witherstormmod.common.resources.taint.TagBasedTaintRecipe;
import nonamecrackers2.witherstormmod.common.resources.taint.TaintRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockTainting extends SimpleJsonResourceReloadListener {
   private static final Gson GSON = new GsonBuilder().create();
   private static final Logger LOGGER = LogManager.getLogger();
   @Nullable
   private Map<ResourceLocation, TaintRecipe> recipes;

   public BlockTainting() {
      super(GSON, "tainting/block");
   }

   protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
      Map<ResourceLocation, TaintRecipe> recipes = Maps.newHashMap();

      for (Entry<ResourceLocation, JsonElement> entry : files.entrySet()) {
         ResourceLocation id = entry.getKey();

         try {
            JsonObject object = entry.getValue().getAsJsonObject();
            MobEffect effect = null;
            if (object.has("potion_effect")) {
               ResourceLocation rawEffectId = (ResourceLocation)ResourceLocation.read(GsonHelper.getAsString(object, "potion_effect")).resultOrPartial(m -> {
                  throw new JsonSyntaxException(m);
               }).get();
               effect = BuiltInRegistries.MOB_EFFECT.get(rawEffectId);
               if (effect == null) {
                  throw new JsonSyntaxException("Unknown effect with id '" + rawEffectId + "'");
               }
            }

            BlockState replacement = (BlockState)BlockState.CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(object, "replacement")).resultOrPartial(m -> {
               throw new JsonSyntaxException(m);
            }).get();
            List<Property<?>> properties = Lists.newArrayList();
            if (object.has("properties_to_copy")) {
               for (JsonElement element : GsonHelper.getAsJsonArray(object, "properties_to_copy")) {
                  String propertyName = GsonHelper.convertToString(element, "property_name");
                  boolean flag = true;

                  for (Property<?> property : replacement.getProperties()) {
                     if (property.getName().equals(propertyName)) {
                        if (properties.contains(property)) {
                           throw new IllegalStateException("Property is already specified: '" + propertyName + "'");
                        }

                        properties.add(property);
                        flag = false;
                        break;
                     }
                  }

                  if (flag) {
                     throw new JsonSyntaxException("Unknown property with name '" + propertyName + "'");
                  }
               }
            }

            String blockEntry = GsonHelper.getAsString(object, "block");
            if (blockEntry.startsWith("#")) {
               TagKey<Block> tag = TagKey.create(
                  Registries.BLOCK, (ResourceLocation)ResourceLocation.read(blockEntry.replace("#", "")).resultOrPartial(ex -> {
                     throw new JsonSyntaxException(ex);
                  }).get()
               );
               recipes.put(id, new TagBasedTaintRecipe(tag, effect, replacement, properties));
            } else {
               ResourceLocation blockId = (ResourceLocation)ResourceLocation.read(blockEntry).resultOrPartial(ex -> {
                  throw new JsonSyntaxException(ex);
               }).get();
               if (!BuiltInRegistries.BLOCK.containsKey(blockId)) {
                  throw new JsonSyntaxException("Unknown block with id '" + blockEntry + "'");
               }

               Block block = (Block)BuiltInRegistries.BLOCK.get(blockId);
               recipes.put(id, new SingleBlockTaintRecipe(block, effect, replacement, properties));
            }
         } catch (IllegalStateException | JsonSyntaxException var19) {
            LOGGER.warn("Failed to read '" + id.toString() + "'", var19);
         }
      }

      this.recipes = recipes.entrySet()
         .stream()
         .sorted(Entry.<ResourceLocation, TaintRecipe>comparingByValue().reversed())
         .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue));
   }

   public Map<ResourceLocation, TaintRecipe> getRecipes() {
      return Objects.requireNonNull(this.recipes, "Recipes are not loaded");
   }
}
