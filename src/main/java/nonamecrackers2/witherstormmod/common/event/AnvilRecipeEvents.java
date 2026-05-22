package nonamecrackers2.witherstormmod.common.event;

import java.util.List;
import java.util.Map;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeTypes;
import nonamecrackers2.witherstormmod.common.item.crafting.AnvilRecipe;

public class AnvilRecipeEvents {
   @SubscribeEvent
   public static void doModdedRecipes(AnvilUpdateEvent event) {
      ItemStack left = event.getLeft();
      ItemStack right = event.getRight();
      Level level = event.getPlayer().level();
      AnvilRecipe.AnvilContents contents = new AnvilRecipe.AnvilContents(left, right);
      List<AnvilRecipe> recipes = level.getRecipeManager().getRecipesFor((RecipeType)WitherStormModRecipeTypes.ANVIL.get(), contents, level);
      if (!recipes.isEmpty()) {
         AnvilRecipe recipe = recipes.get(0);
         Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(left);
         enchantments.putAll(EnchantmentHelper.getEnchantments(right));
         ItemStack output = recipe.assemble(contents, level.registryAccess());
         EnchantmentHelper.setEnchantments(enchantments, output);
         event.setOutput(output);
         event.setCost(recipe.getCost());
      }
   }
}
