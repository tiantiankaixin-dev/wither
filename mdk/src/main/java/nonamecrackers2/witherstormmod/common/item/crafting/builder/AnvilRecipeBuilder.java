package nonamecrackers2.witherstormmod.common.item.crafting.builder;

import com.google.gson.JsonObject;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.registries.BuiltInRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;

public class AnvilRecipeBuilder implements RecipeBuilder {
   private final Ingredient left;
   private final Ingredient right;
   private final Item result;
   private final int xpCost;
   @Nullable
   private String group;

   public AnvilRecipeBuilder(Ingredient left, Ingredient right, ItemLike result, int xpCost) {
      this.left = left;
      this.right = right;
      this.result = result.asItem();
      this.xpCost = xpCost;
   }

   public static AnvilRecipeBuilder commandBlockTool(ItemLike requirement, ItemLike result) {
      return new AnvilRecipeBuilder(
         Ingredient.of(new ItemLike[]{requirement}),
         Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.COMMAND_BLOCK_BOOK.get()}),
         result,
         1
      );
   }

   public RecipeBuilder unlockedBy(String str, CriterionTriggerInstance instance) {
      return this;
   }

   public RecipeBuilder group(String group) {
      this.group = group;
      return this;
   }

   public Item getResult() {
      return this.result;
   }

   public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
      consumer.accept(new AnvilRecipeBuilder.Result(id, this.group == null ? "" : this.group, this.left, this.right, this.result, this.xpCost));
   }

   public static record Result(ResourceLocation id, String group, Ingredient left, Ingredient right, Item result, int xpCost) implements FinishedRecipe {
      public void serializeRecipeData(JsonObject object) {
         if (!this.group.isEmpty()) {
            object.addProperty("group", this.group);
         }

         object.add("left", this.left.toJson());
         object.add("right", this.right.toJson());
         object.addProperty("cost", this.xpCost);
         object.addProperty("result", NeoBuiltInRegistries.ITEM.getKey(this.result).toString());
      }

      public ResourceLocation getId() {
         return this.id;
      }

      public RecipeSerializer<?> getType() {
         return (RecipeSerializer<?>)WitherStormModRecipeSerializers.ANVIL_RECIPE.get();
      }

      public JsonObject serializeAdvancement() {
         return null;
      }

      public ResourceLocation getAdvancementId() {
         return null;
      }
   }
}
