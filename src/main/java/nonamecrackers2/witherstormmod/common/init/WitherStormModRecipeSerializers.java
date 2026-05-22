package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.common.item.crafting.AnvilRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.ItemCraftSuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.LockAmuletRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.ResummonSuperBeaconRecipe;

public class WitherStormModRecipeSerializers {
   public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "witherstormmod");
   public static final RegistryObject<SimpleCraftingRecipeSerializer<LockAmuletRecipe>> LOCK_AMULET = RECIPE_SERIALIZERS.register(
      "lock_amulet", () -> new SimpleCraftingRecipeSerializer(LockAmuletRecipe::new)
   );
   public static final RegistryObject<ResummonSuperBeaconRecipe.Serializer> RESUMMON_SUPER_BEACON = RECIPE_SERIALIZERS.register(
      "resummon_super_beacon", ResummonSuperBeaconRecipe.Serializer::new
   );
   public static final RegistryObject<ItemCraftSuperBeaconRecipe.Serializer> ITEM_CRAFT_SUPER_BEACON = RECIPE_SERIALIZERS.register(
      "item_craft_super_beacon", ItemCraftSuperBeaconRecipe.Serializer::new
   );
   public static final RegistryObject<AnvilRecipe.Serializer> ANVIL_RECIPE = RECIPE_SERIALIZERS.register("anvil", AnvilRecipe.Serializer::new);
}
