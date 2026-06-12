package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import nonamecrackers2.witherstormmod.common.item.crafting.AnvilRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.ItemCraftSuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.ResummonSuperBeaconRecipe;

public class WitherStormModRecipeTypes {
   public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, "witherstormmod");
   public static final DeferredHolder<RecipeType<?>, RecipeType<ResummonSuperBeaconRecipe>> SUPER_BEACON_RESUMMON = register("super_beacon_resummon");
   public static final DeferredHolder<RecipeType<?>, RecipeType<ItemCraftSuperBeaconRecipe>> SUPER_BEACON_ITEM = register("super_beacon_item");
   public static final DeferredHolder<RecipeType<?>, RecipeType<AnvilRecipe>> ANVIL = register("anvil");

   private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(String id) {
      return RECIPE_TYPES.register(id, () -> new RecipeType<T>() {
            @Override
            public String toString() {
               return id;
            }
         });
   }
}
