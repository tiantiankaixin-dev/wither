package nonamecrackers2.witherstormmod.common.item.crafting.builder;

import javax.annotation.Nullable;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.item.crafting.AnvilRecipe;

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

   public RecipeBuilder unlockedBy(String str, Criterion<?> instance) {
      return this;
   }

   public RecipeBuilder group(String group) {
      this.group = group;
      return this;
   }

   public Item getResult() {
      return this.result;
   }

   public void save(RecipeOutput output, ResourceLocation id) {
      output.accept(id, new AnvilRecipe(id, this.left, this.right, new ItemStack(this.result), this.xpCost), null);
   }
}
