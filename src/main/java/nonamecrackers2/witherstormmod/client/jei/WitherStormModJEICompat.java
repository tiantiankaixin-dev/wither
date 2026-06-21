package nonamecrackers2.witherstormmod.client.jei;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import nonamecrackers2.witherstormmod.client.jei.category.SuperBeaconItemCrafting;
import nonamecrackers2.witherstormmod.client.jei.category.SuperBeaconSummoning;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeTypes;
import nonamecrackers2.witherstormmod.common.item.crafting.AnvilRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.ItemCraftSuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.ResummonSuperBeaconRecipe;

@JeiPlugin
public class WitherStormModJEICompat implements IModPlugin {
   private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("witherstormmod", "jei_compat");
   public static final RecipeType<ItemCraftSuperBeaconRecipe> SUPER_BEACON_ITEM_CRAFTING = RecipeType.create(
      "witherstormmod", "item_craft_super_beacon", ItemCraftSuperBeaconRecipe.class
   );
   public static final RecipeType<ResummonSuperBeaconRecipe> SUPER_BEACON_SUMMONING = RecipeType.create(
      "witherstormmod", "resummoning_super_beacon", ResummonSuperBeaconRecipe.class
   );

   public ResourceLocation getPluginUid() {
      return ID;
   }

   public void registerRecipes(IRecipeRegistration registration) {
      Minecraft mc = Minecraft.getInstance();
      RecipeManager manager = mc.level.getRecipeManager();
      registration.addRecipes(
         SUPER_BEACON_ITEM_CRAFTING, manager.getAllRecipesFor(WitherStormModRecipeTypes.SUPER_BEACON_ITEM.get()).stream().map(RecipeHolder::value).toList()
      );
      registration.addRecipes(
         SUPER_BEACON_SUMMONING, manager.getAllRecipesFor(WitherStormModRecipeTypes.SUPER_BEACON_RESUMMON.get()).stream().map(RecipeHolder::value).toList()
      );
      Component info = Component.translatable("withered_beacon.info");
      registration.addItemStackInfo(new ItemStack((ItemLike)WitherStormModBlocks.SUPER_BEACON.get()), new Component[]{info});
      registration.addItemStackInfo(new ItemStack((ItemLike)WitherStormModBlocks.SUPER_SUPPORT_BEACON.get()), new Component[]{info});
      Component pumpkinInfo = Component.translatable("witherstormmod.jei.tainted_pumpkin_info");
      registration.addItemStackInfo(new ItemStack((ItemLike)WitherStormModBlocks.TAINTED_PUMPKIN.get()), new Component[]{pumpkinInfo});
      registration.addItemStackInfo(new ItemStack((ItemLike)WitherStormModBlocks.TAINTED_CARVED_PUMPKIN.get()), new Component[]{pumpkinInfo});
      registration.addItemStackInfo(new ItemStack((ItemLike)WitherStormModBlocks.TAINTED_JACK_O_LANTERN.get()), new Component[]{pumpkinInfo});
      List<IJeiAnvilRecipe> recipes = Lists.newArrayList();

      for (RecipeHolder<AnvilRecipe> holder : manager.getAllRecipesFor(WitherStormModRecipeTypes.ANVIL.get())) {
         AnvilRecipe recipe = holder.value();
         recipes.add(
            registration.getVanillaRecipeFactory()
               .createAnvilRecipe(
                  Arrays.asList(recipe.getLeft().getItems()),
                  Arrays.asList(recipe.getRight().getItems()),
                  Lists.newArrayList(new ItemStack[]{recipe.getOutputRaw()})
               )
         );
      }

      registration.addRecipes(RecipeTypes.ANVIL, recipes);
   }

   public void registerCategories(IRecipeCategoryRegistration registration) {
      IJeiHelpers helpers = registration.getJeiHelpers();
      registration.addRecipeCategories(new IRecipeCategory[]{new SuperBeaconItemCrafting(helpers.getGuiHelper())});
      registration.addRecipeCategories(new IRecipeCategory[]{new SuperBeaconSummoning(helpers.getGuiHelper())});
   }

   public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
      registration.addRecipeCatalyst(new ItemStack((ItemLike)WitherStormModItems.SUPER_BEACON.get()), new RecipeType[]{SUPER_BEACON_ITEM_CRAFTING});
      registration.addRecipeCatalyst(new ItemStack((ItemLike)WitherStormModItems.SUPER_SUPPORT_BEACON.get()), new RecipeType[]{SUPER_BEACON_ITEM_CRAFTING});
      registration.addRecipeCatalyst(new ItemStack((ItemLike)WitherStormModItems.SUPER_BEACON.get()), new RecipeType[]{SUPER_BEACON_SUMMONING});
      registration.addRecipeCatalyst(new ItemStack((ItemLike)WitherStormModItems.SUPER_SUPPORT_BEACON.get()), new RecipeType[]{SUPER_BEACON_SUMMONING});
   }
}
