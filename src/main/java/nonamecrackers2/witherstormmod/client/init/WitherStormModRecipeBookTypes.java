package nonamecrackers2.witherstormmod.client.init;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeTypes;

public class WitherStormModRecipeBookTypes {
   public static void registerRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
      event.registerRecipeCategoryFinder((RecipeType)WitherStormModRecipeTypes.SUPER_BEACON_ITEM.get(), r -> RecipeBookCategories.UNKNOWN);
      event.registerRecipeCategoryFinder((RecipeType)WitherStormModRecipeTypes.SUPER_BEACON_RESUMMON.get(), r -> RecipeBookCategories.UNKNOWN);
   }
}
