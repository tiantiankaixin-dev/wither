package nonamecrackers2.witherstormmod.common.item.crafting.builder;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import nonamecrackers2.witherstormmod.common.item.crafting.ItemCraftSuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.SuperBeaconRecipe;

public class ItemCraftSuperBeaconRecipeBuilder extends SuperBeaconRecipeBuilder {
   private final Item result;
   private final int count;

   public ItemCraftSuperBeaconRecipeBuilder(SuperBeaconRecipe.Condition condition, ItemLike result, int count) {
      super(condition);
      this.result = result.asItem();
      this.count = count;
   }

   @Override
   public Item getResult() {
      return this.result;
   }

   @Override
   public void save(RecipeOutput output, ResourceLocation id) {
      output.accept(id, new ItemCraftSuperBeaconRecipe(id, this.copyIngredients(), new ItemStack(this.result, this.count), this.condition), null);
   }
}
