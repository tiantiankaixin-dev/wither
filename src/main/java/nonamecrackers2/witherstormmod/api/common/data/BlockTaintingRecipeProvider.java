package nonamecrackers2.witherstormmod.api.common.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.Target;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.common.resources.taint.SingleBlockTaintRecipe;
import nonamecrackers2.witherstormmod.common.resources.taint.TagBasedTaintRecipe;
import nonamecrackers2.witherstormmod.common.resources.taint.TaintRecipe;

public abstract class BlockTaintingRecipeProvider implements DataProvider {
   private final List<TaintRecipe> recipes = Lists.newArrayList();
   private final Path outputPath;

   public BlockTaintingRecipeProvider(PackOutput output, String modid) {
      this.outputPath = output.getOutputFolder(Target.DATA_PACK).resolve(modid).resolve("tainting/block");
   }

   protected abstract void addRecipes();

   protected void add(TaintRecipe recipe) {
      this.recipes.add(recipe);
   }

   protected void add(Block from, MobEffect effect, BlockState to, Property<?>... propertiesToCopy) {
      this.add(new SingleBlockTaintRecipe(from, effect, to, Lists.newArrayList(propertiesToCopy)));
   }

   protected void add(Block from, MobEffect effect, BlockState to) {
      this.add(from, effect, to);
   }

   protected void addAndCopyAllProperties(Block from, MobEffect effect, Block to) {
      BlockState state = to.defaultBlockState();
      this.add(from, effect, state, state.getProperties().toArray(Property[]::new));
   }

   protected void addAndCopyAllProperties(Block from, Block to) {
      BlockState state = to.defaultBlockState();
      this.add(from, state, state.getProperties().toArray(Property[]::new));
   }

   protected void add(Block from, BlockState to, Property<?>... propertiesToCopy) {
      this.add(new SingleBlockTaintRecipe(from, null, to, Lists.newArrayList(propertiesToCopy)));
   }

   protected void add(Block from, BlockState to) {
      this.add(from, to);
   }

   protected void add(TagKey<Block> from, MobEffect effect, BlockState to, Property<?>... propertiesToCopy) {
      this.add(new TagBasedTaintRecipe(from, effect, to, Lists.newArrayList(propertiesToCopy)));
   }

   protected void add(TagKey<Block> from, MobEffect effect, BlockState to) {
      this.add(from, effect, to);
   }

   protected void addAndCopyAllProperties(TagKey<Block> from, MobEffect effect, Block to) {
      BlockState state = to.defaultBlockState();
      this.add(from, effect, state, state.getProperties().toArray(Property[]::new));
   }

   protected void add(TagKey<Block> from, BlockState to, Property<?>... propertiesToCopy) {
      this.add(new TagBasedTaintRecipe(from, null, to, Lists.newArrayList(propertiesToCopy)));
   }

   protected void add(TagKey<Block> from, BlockState to) {
      this.add(from, to);
   }

   protected void addAndCopyAllProperties(TagKey<Block> from, Block to) {
      BlockState state = to.defaultBlockState();
      this.add(from, state, state.getProperties().toArray(Property[]::new));
   }

   public CompletableFuture<?> run(CachedOutput output) {
      this.addRecipes();
      return CompletableFuture.allOf(this.recipes.stream().map(recipe -> {
         JsonObject object = new JsonObject();
         recipe.serializeFrom(object);
         if (recipe.effect() != null) {
            object.addProperty("potion_effect", ForgeRegistries.MOB_EFFECTS.getKey(recipe.effect()).toString());
         }

         object.add("replacement", (JsonElement)BlockState.CODEC.encodeStart(JsonOps.INSTANCE, recipe.replacement()).result().get());
         if (!recipe.propertiesToCopy().isEmpty()) {
            JsonArray propertiesToCopy = new JsonArray();

            for (Property<?> property : recipe.propertiesToCopy()) {
               String name = property.getName();
               propertiesToCopy.add(name);
            }

            object.add("properties_to_copy", propertiesToCopy);
         }

         return DataProvider.saveStable(output, object, this.outputPath.resolve(recipe.getName() + "_tainting.json"));
      }).toArray(CompletableFuture[]::new));
   }

   public String getName() {
      return "Block tainting recipes";
   }
}
