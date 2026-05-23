package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import nonamecrackers2.witherstormmod.common.item.crafting.AnvilRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.ItemCraftSuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.LockAmuletRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.ResummonSuperBeaconRecipe;

public class WitherStormModRecipeSerializers {
   public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(NeoBuiltInRegistries.RECIPE_SERIALIZER, "witherstormmod");
   public static final DeferredHolder<SimpleCraftingRecipeSerializer<LockAmuletRecipe>> LOCK_AMULET = RECIPE_SERIALIZERS.register(
      "lock_amulet", () -> new SimpleCraftingRecipeSerializer(LockAmuletRecipe::new)
   );
   public static final DeferredHolder<ResummonSuperBeaconRecipe.Serializer> RESUMMON_SUPER_BEACON = RECIPE_SERIALIZERS.register(
      "resummon_super_beacon", ResummonSuperBeaconRecipe.Serializer::new
   );
   public static final DeferredHolder<ItemCraftSuperBeaconRecipe.Serializer> ITEM_CRAFT_SUPER_BEACON = RECIPE_SERIALIZERS.register(
      "item_craft_super_beacon", ItemCraftSuperBeaconRecipe.Serializer::new
   );
   public static final DeferredHolder<AnvilRecipe.Serializer> ANVIL_RECIPE = RECIPE_SERIALIZERS.register("anvil", AnvilRecipe.Serializer::new);
}
