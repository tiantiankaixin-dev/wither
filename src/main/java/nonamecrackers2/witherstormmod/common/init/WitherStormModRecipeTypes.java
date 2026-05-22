package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.common.item.crafting.AnvilRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.ItemCraftSuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.ResummonSuperBeaconRecipe;

public class WitherStormModRecipeTypes {
   public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, "witherstormmod");
   public static final RegistryObject<RecipeType<ResummonSuperBeaconRecipe>> SUPER_BEACON_RESUMMON = register("super_beacon_resummon");
   public static final RegistryObject<RecipeType<ItemCraftSuperBeaconRecipe>> SUPER_BEACON_ITEM = register("super_beacon_item");
   public static final RegistryObject<RecipeType<AnvilRecipe>> ANVIL = register("anvil");

   private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(String id) {
      return RECIPE_TYPES.register(id, () -> new RecipeType<T>() {
            @Override
            public String toString() {
               return id;
            }
         });
   }
}
