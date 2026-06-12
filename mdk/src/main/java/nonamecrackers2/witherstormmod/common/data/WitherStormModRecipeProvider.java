package nonamecrackers2.witherstormmod.common.data;

import java.util.function.Consumer;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.item.crafting.SuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.builder.AnvilRecipeBuilder;
import nonamecrackers2.witherstormmod.common.item.crafting.builder.SuperBeaconRecipeBuilder;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModItemTags;
import org.jetbrains.annotations.NotNull;

public class WitherStormModRecipeProvider extends RecipeProvider {
   public WitherStormModRecipeProvider(PackOutput output) {
      super(output);
   }

   protected void buildRecipes(@NotNull RecipeOutput result) {
      SpecialRecipeBuilder.special((RecipeSerializer)WitherStormModRecipeSerializers.LOCK_AMULET.get()).save(result, "witherstormmod:amulet_lock");
      unlockedByItems(
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)WitherStormModItems.AMULET.get())
               .define('G', Items.GOLD_INGOT)
               .define('D', Items.DIAMOND)
               .define('R', Items.REDSTONE)
               .define('L', Items.LAPIS_LAZULI)
               .define('E', Items.EMERALD)
               .define('I', Items.IRON_INGOT)
               .pattern("GDG")
               .pattern("RLE")
               .pattern("GIG"),
            Items.GOLD_INGOT,
            Items.DIAMOND,
            Items.REDSTONE,
            Items.LAPIS_LAZULI,
            Items.EMERALD,
            Items.IRON_INGOT
         )
         .save(result);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, (ItemLike)WitherStormModItems.FIREWORK_BUNDLE.get())
         .requires(Items.FIREWORK_ROCKET, 8)
         .requires(Items.BARREL)
         .unlockedBy("has_firework_rocket", has(Items.FIREWORK_ROCKET))
         .unlockedBy("has_barrel", has(Items.BARREL))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike)WitherStormModItems.FORMIDIBOMB.get())
         .define('X', Items.GUNPOWDER)
         .define('#', Items.BLAZE_POWDER)
         .define('A', (ItemLike)WitherStormModItems.SUPER_TNT.get())
         .pattern("X#X")
         .pattern("#A#")
         .pattern("X#X")
         .unlockedBy("has_super_tnt", has((ItemLike)WitherStormModItems.SUPER_TNT.get()))
         .save(result);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, (ItemLike)WitherStormModItems.GOLDEN_APPLE_STEW.get())
         .requires(Ingredient.of(WitherStormModItemTags.CURE_INGREDIENT))
         .requires(Items.SUSPICIOUS_STEW)
         .requires(Items.GOLDEN_APPLE)
         .requires(Ingredient.of(WitherStormModItemTags.CURE_BASE))
         .unlockedBy("has_cure_ingredient", has(WitherStormModItemTags.CURE_INGREDIENT))
         .unlockedBy("has_suspicious_stew", has(Items.SUSPICIOUS_STEW))
         .unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE))
         .unlockedBy("has_cure_base", has(WitherStormModItemTags.CURE_BASE))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.INFECTED_FLESH_BLOCK.get())
         .define('#', (ItemLike)WitherStormModItems.TAINTED_FLESH_BLOCK.get())
         .define('A', (ItemLike)WitherStormModItems.TAINTED_DUST.get())
         .pattern("###")
         .pattern("#A#")
         .pattern("###")
         .unlockedBy("has_tainted_flesh_block", has((ItemLike)WitherStormModItems.TAINTED_FLESH_BLOCK.get()))
         .unlockedBy("has_tainted_dust", has((ItemLike)WitherStormModItems.TAINTED_DUST.get()))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, (ItemLike)WitherStormModItems.PHASOMETER.get())
         .define('#', Items.BLACK_DYE)
         .define('S', Items.STONE_BUTTON)
         .define('Z', Items.SPYGLASS)
         .define('R', Items.REDSTONE)
         .define('X', Items.REPEATER)
         .pattern(" # ")
         .pattern("SZR")
         .pattern(" X ")
         .unlockedBy("has_spyglass", has(Items.SPYGLASS))
         .save(result);
      SuperBeaconRecipeBuilder.entity(SuperBeaconRecipe.Condition.NONE, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.WITHER_STORM.get())
         .requires(Items.WITHER_SKELETON_SKULL, 3)
         .unlockedBy("has_wither_skull", has(Items.WITHER_SKELETON_SKULL))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULLY_COMLETED, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.WITHERED_SYMBIONT.get()
         )
         .requires((ItemLike)WitherStormModItems.INFECTED_FLESH_BLOCK.get(), 1)
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 2)
         .requires((ItemLike)WitherStormModItems.WITHERED_BONE.get(), 1)
         .requires((ItemLike)WitherStormModItems.TAINTED_DUST_BLOCK.get(), 1)
         .requires(Items.WITHER_SKELETON_SKULL)
         .unlockedBy("has_wither_skeleton_skull", has(Items.WITHER_SKELETON_SKULL))
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_withered_bone", has((ItemLike)WitherStormModItems.WITHERED_BONE.get()))
         .unlockedBy("has_infected_flesh_block", has((ItemLike)WitherStormModItems.INFECTED_FLESH_BLOCK.get()))
         .unlockedBy("has_tainted_dust_block", has((ItemLike)WitherStormModItems.TAINTED_DUST_BLOCK.get()))
         .save(result);
      reubenPig(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_CHICKEN.get()
         )
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get())
         .requires((ItemLike)WitherStormModItems.WITHERED_BONE.get())
         .requires(Items.FEATHER)
         .unlockedBy("has_withered_bone", has((ItemLike)WitherStormModItems.WITHERED_BONE.get()))
         .unlockedBy("has_feather", has(Items.FEATHER))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_COW.get()
         )
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 2)
         .requires(Items.LEATHER, 2)
         .requires(Items.BEEF, 2)
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_leather", has(Items.LEATHER))
         .unlockedBy("has_beef", has(Items.BEEF))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_PIG.get()
         )
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 3)
         .requires(Items.PORKCHOP, 3)
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_pork", has(Items.PORKCHOP))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_BEE.get()
         )
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 2)
         .requires(Items.HONEY_BOTTLE, 2)
         .requires(Items.HONEYCOMB, 4)
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_honey_bottle", has(Items.HONEY_BOTTLE))
         .unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_WOLF.get()
         )
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 3)
         .requires(Items.BONE, 2)
         .requires(Items.NAME_TAG, 1)
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_bone", has(Items.BONE))
         .unlockedBy("has_name_tag", has(Items.NAME_TAG))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_CAT.get()
         )
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 3)
         .requires(Ingredient.of(ItemTags.FISHES), 2)
         .requires(Items.NAME_TAG, 1)
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_bone", has(Items.BONE))
         .unlockedBy("has_name_tag", has(Items.NAME_TAG))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_PARROT.get()
         )
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 1)
         .requires(Ingredient.of(new ItemLike[]{Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.WHEAT_SEEDS, Items.TORCHFLOWER_SEEDS}), 8)
         .requires(Items.NAME_TAG, 1)
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_bone", has(Items.BONE))
         .unlockedBy("has_name_tag", has(Items.NAME_TAG))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_CREEPER.get()
         )
         .requires(Items.GUNPOWDER, 3)
         .requires((ItemLike)WitherStormModItems.TAINTED_DUST.get(), 3)
         .unlockedBy("has_gunpowder", has(Items.GUNPOWDER))
         .unlockedBy("has_tainted_dust", has((ItemLike)WitherStormModItems.TAINTED_DUST.get()))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_MUSHROOM_COW.get()
         )
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 2)
         .requires((ItemLike)WitherStormModItems.TAINTED_MUSHROOM.get(), 2)
         .requires(Items.BEEF, 2)
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_tainted_mushroom", has((ItemLike)WitherStormModItems.TAINTED_MUSHROOM.get()))
         .unlockedBy("has_beef", has(Items.BEEF))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_PHANTOM.get()
         )
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 2)
         .requires((ItemLike)WitherStormModItems.WITHERED_BONE.get(), 2)
         .requires(Items.PHANTOM_MEMBRANE)
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_withered_bone", has((ItemLike)WitherStormModItems.WITHERED_BONE.get()))
         .unlockedBy("has_phantom_membrane", has(Items.PHANTOM_MEMBRANE))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_PILLAGER.get()
         )
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 3)
         .requires(Items.CROSSBOW)
         .requires(Items.ARROW, 2)
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_crossbow", has(Items.CROSSBOW))
         .unlockedBy("has_arrow", has(Items.ARROW))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_VINDICATOR.get()
         )
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 4)
         .requires(Items.IRON_AXE)
         .requires(Items.EMERALD)
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_iron_axe", has(Items.IRON_AXE))
         .unlockedBy("has_emerald", has(Items.EMERALD))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_SKELETON.get()
         )
         .requires(Items.BONE, 3)
         .requires((ItemLike)WitherStormModItems.WITHERED_BONE.get(), 3)
         .unlockedBy("has_bone", has(Items.BONE))
         .unlockedBy("has_withered_bone", has((ItemLike)WitherStormModItems.WITHERED_BONE.get()))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_SPIDER.get()
         )
         .requires((ItemLike)WitherStormModItems.WITHERED_SPIDER_EYE.get(), 3)
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 3)
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_withered_spider_eye", has((ItemLike)WitherStormModItems.WITHERED_SPIDER_EYE.get()))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_VILLAGER.get()
         )
         .requires(Items.ROTTEN_FLESH, 2)
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 2)
         .requires(Items.EMERALD, 2)
         .requires(Items.GOLDEN_APPLE)
         .unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH))
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .unlockedBy("has_emerald", has(Items.EMERALD))
         .unlockedBy("has_golden_apple", has(Items.GOLDEN_APPLE))
         .save(result);
      SuperBeaconRecipeBuilder.entity(
            SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.MISC, (EntityType<?>)WitherStormModEntityTypes.SICKENED_ZOMBIE.get()
         )
         .requires(Items.ROTTEN_FLESH, 3)
         .requires((ItemLike)WitherStormModItems.WITHERED_FLESH.get(), 3)
         .unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH))
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike)WitherStormModItems.SUPER_BEACON.get())
         .define('G', (ItemLike)WitherStormModItems.TAINTED_GLASS.get())
         .define('S', (ItemLike)WitherStormModItems.WITHERED_NETHER_STAR.get())
         .define('I', (ItemLike)WitherStormModItems.TAINTED_FLESH_BLOCK.get())
         .define('A', (ItemLike)WitherStormModItems.AMULET.get())
         .pattern("GGG")
         .pattern("GSG")
         .pattern("IAI")
         .unlockedBy("has_withered_nether_star", has((ItemLike)WitherStormModItems.WITHERED_NETHER_STAR.get()))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike)WitherStormModItems.SUPER_SUPPORT_BEACON.get())
         .define('G', (ItemLike)WitherStormModItems.TAINTED_GLASS.get())
         .define('S', Items.NETHER_STAR)
         .define('I', (ItemLike)WitherStormModItems.TAINTED_FLESH_BLOCK.get())
         .pattern("GGG")
         .pattern("GSG")
         .pattern("III")
         .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike)WitherStormModItems.SUPER_TNT.get())
         .define('#', Items.TNT)
         .define('X', Items.GUNPOWDER)
         .define('A', (ItemLike)WitherStormModItems.COMMAND_BLOCK_BOOK.get())
         .pattern("X#X")
         .pattern("#A#")
         .pattern("X#X")
         .unlockedBy("has_command_block_book", has((ItemLike)WitherStormModItems.COMMAND_BLOCK_BOOK.get()))
         .save(result);
      buttonBuilder((ItemLike)WitherStormModItems.TAINTED_BUTTON.get(), Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_PLANKS.get()}))
         .unlockedBy("has_tainted_planks", has((ItemLike)WitherStormModItems.TAINTED_PLANKS.get()))
         .save(result);
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModBlocks.TAINTED_COBBLESTONE.get()}),
            RecipeCategory.DECORATIONS,
            (ItemLike)WitherStormModItems.TAINTED_COBBLESTONE_SLAB.get(),
            2
         )
         .unlockedBy("has_tainted_cobblestone", has((ItemLike)WitherStormModItems.TAINTED_COBBLESTONE.get()))
         .save(result, new ResourceLocation("witherstormmod", WitherStormModBlocks.TAINTED_COBBLESTONE_SLAB.getId().getPath() + "_stonecutting"));
      slab(
         result,
         RecipeCategory.DECORATIONS,
         (ItemLike)WitherStormModBlocks.TAINTED_COBBLESTONE_SLAB.get(),
         (ItemLike)WitherStormModBlocks.TAINTED_COBBLESTONE.get()
      );
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModBlocks.TAINTED_COBBLESTONE.get()}),
            RecipeCategory.BUILDING_BLOCKS,
            (ItemLike)WitherStormModBlocks.TAINTED_COBBLESTONE_STAIRS.get()
         )
         .unlockedBy("has_tainted_cobblestone", has((ItemLike)WitherStormModItems.TAINTED_COBBLESTONE.get()))
         .save(result, new ResourceLocation("witherstormmod", WitherStormModBlocks.TAINTED_COBBLESTONE_STAIRS.getId().getPath() + "_stonecutting"));
      stairBuilder(
            (ItemLike)WitherStormModItems.TAINTED_COBBLESTONE_STAIRS.get(),
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_COBBLESTONE.get()})
         )
         .unlockedBy("has_tainted_cobblestone", has((ItemLike)WitherStormModItems.TAINTED_COBBLESTONE.get()))
         .save(result);
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModBlocks.TAINTED_COBBLESTONE.get()}),
            RecipeCategory.DECORATIONS,
            (ItemLike)WitherStormModBlocks.TAINTED_COBBLESTONE_WALL.get()
         )
         .unlockedBy("has_tainted_cobblestone", has((ItemLike)WitherStormModItems.TAINTED_COBBLESTONE_WALL.get()))
         .save(result, new ResourceLocation("witherstormmod", WitherStormModBlocks.TAINTED_COBBLESTONE_WALL.getId().getPath() + "_stonecutting"));
      wall(
         result,
         RecipeCategory.DECORATIONS,
         (ItemLike)WitherStormModItems.TAINTED_COBBLESTONE_WALL.get(),
         (ItemLike)WitherStormModItems.TAINTED_COBBLESTONE.get()
      );
      SuperBeaconRecipeBuilder.item(
            SuperBeaconRecipe.Condition.MAIN_ACTIVATED, RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.TAINTED_COBBLESTONE.get()
         )
         .requires(Items.COBBLESTONE, 6)
         .unlockedBy("has_cobblestone", has(Items.COBBLESTONE))
         .save(result);
      doorBuilder((ItemLike)WitherStormModItems.TAINTED_DOOR.get(), Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_PLANKS.get()}))
         .unlockedBy("has_tainted_planks", has((ItemLike)WitherStormModItems.TAINTED_PLANKS.get()))
         .save(result);
      signBuilder((ItemLike)WitherStormModItems.TAINTED_SIGN.get(), Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_PLANKS.get()}))
         .unlockedBy("has_tainted_planks", has((ItemLike)WitherStormModItems.TAINTED_PLANKS.get()))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.TAINTED_DUST_BLOCK.get())
         .define('#', (ItemLike)WitherStormModItems.TAINTED_DUST.get())
         .pattern("##")
         .pattern("##")
         .unlockedBy("has_tainted_dust", has((ItemLike)WitherStormModItems.TAINTED_DUST.get()))
         .save(result);
      SuperBeaconRecipeBuilder.item(SuperBeaconRecipe.Condition.MAIN_ACTIVATED, RecipeCategory.DECORATIONS, (ItemLike)WitherStormModItems.TAINTED_DUST.get())
         .requires(Items.REDSTONE, 6)
         .unlockedBy("has_redstone", has(Items.REDSTONE))
         .save(result);
      fenceGateBuilder((ItemLike)WitherStormModItems.TAINTED_FENCE_GATE.get(), Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_PLANKS.get()}))
         .unlockedBy("has_tainted_planks", has((ItemLike)WitherStormModBlocks.TAINTED_PLANKS.get()))
         .save(result);
      fenceBuilder((ItemLike)WitherStormModItems.TAINTED_FENCE.get(), Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModBlocks.TAINTED_PLANKS.get()}))
         .unlockedBy("has_tainted_planks", has((ItemLike)WitherStormModBlocks.TAINTED_PLANKS.get()))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.TAINTED_FLESH_BLOCK.get())
         .define('#', (ItemLike)WitherStormModItems.WITHERED_FLESH.get())
         .pattern("###")
         .pattern("###")
         .pattern("###")
         .unlockedBy("has_withered_flesh", has((ItemLike)WitherStormModItems.WITHERED_FLESH.get()))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)WitherStormModItems.TAINTED_GLASS_PANE.get(), 16)
         .define('#', (ItemLike)WitherStormModItems.TAINTED_GLASS.get())
         .pattern("###")
         .pattern("###")
         .unlockedBy("has_tainted_glass", has((ItemLike)WitherStormModItems.TAINTED_GLASS.get()))
         .save(result);
      SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_SAND.get()}),
            RecipeCategory.DECORATIONS,
            (ItemLike)WitherStormModItems.TAINTED_GLASS.get(),
            0.1F,
            200
         )
         .unlockedBy("has_tainted_sand", has((ItemLike)WitherStormModItems.TAINTED_SAND.get()))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.TAINTED_JACK_O_LANTERN.get())
         .define('#', (ItemLike)WitherStormModItems.TAINTED_CARVED_PUMPKIN.get())
         .define('A', Ingredient.of(new ItemLike[]{Items.TORCH, (ItemLike)WitherStormModItems.TAINTED_TORCH.get()}))
         .pattern("#")
         .pattern("A")
         .unlockedBy("has_tainted_carved_pumpkin", has((ItemLike)WitherStormModItems.TAINTED_CARVED_PUMPKIN.get()))
         .save(result);
      SuperBeaconRecipeBuilder.item(SuperBeaconRecipe.Condition.MAIN_ACTIVATED, RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.TAINTED_LOG.get())
         .requires(Ingredient.of(ItemTags.LOGS), 6)
         .unlockedBy("has_log", has(ItemTags.LOGS))
         .save(result);
      planksFromLogs(result, (ItemLike)WitherStormModItems.TAINTED_PLANKS.get(), WitherStormModItemTags.TAINTED_LOGS, 4);
      pressurePlate(result, (ItemLike)WitherStormModItems.TAINTED_PRESSURE_PLATE.get(), (ItemLike)WitherStormModItems.TAINTED_PLANKS.get());
      SuperBeaconRecipeBuilder.item(
            SuperBeaconRecipe.Condition.MAIN_ACTIVATED, RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.TAINTED_SAND.get()
         )
         .requires(Items.SAND, 6)
         .unlockedBy("has_sand", has(Items.SAND))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get())
         .define('#', (ItemLike)WitherStormModItems.TAINTED_SAND.get())
         .pattern("##")
         .pattern("##")
         .unlockedBy("has_tainted_sand", has((ItemLike)WitherStormModItems.TAINTED_SAND.get()))
         .save(result);
      slab(
         result, RecipeCategory.DECORATIONS, (ItemLike)WitherStormModItems.TAINTED_SANDSTONE_SLAB.get(), (ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()
      );
      stairBuilder(
            (ItemLike)WitherStormModItems.TAINTED_SANDSTONE_STAIRS.get(),
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()})
         )
         .unlockedBy("has_tainted_sandstone", has((ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()))
         .save(result);
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()}),
            RecipeCategory.DECORATIONS,
            (ItemLike)WitherStormModItems.TAINTED_SANDSTONE_SLAB.get(),
            2
         )
         .unlockedBy("has_tainted_sandstone", has((ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()))
         .save(result, new ResourceLocation("witherstormmod", WitherStormModItems.TAINTED_SANDSTONE_SLAB.getId().getPath() + "_stonecutting"));
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()}),
            RecipeCategory.BUILDING_BLOCKS,
            (ItemLike)WitherStormModItems.TAINTED_SANDSTONE_STAIRS.get()
         )
         .unlockedBy("has_tainted_sandstone", has((ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()))
         .save(result, new ResourceLocation("witherstormmod", WitherStormModItems.TAINTED_SANDSTONE_STAIRS.getId().getPath() + "_stonecutting"));
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModBlocks.TAINTED_SANDSTONE.get()}),
            RecipeCategory.DECORATIONS,
            (ItemLike)WitherStormModBlocks.TAINTED_SANDSTONE_WALL.get()
         )
         .unlockedBy("has_tainted_sandstone", has((ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()))
         .save(result, new ResourceLocation("witherstormmod", WitherStormModBlocks.TAINTED_SANDSTONE_WALL.getId().getPath() + "_stonecutting"));
      wall(
         result, RecipeCategory.DECORATIONS, (ItemLike)WitherStormModItems.TAINTED_SANDSTONE_WALL.get(), (ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()
      );
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.TAINTED_CUT_SANDSTONE.get(), 4)
         .define('#', (ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get())
         .pattern("##")
         .pattern("##")
         .unlockedBy("has_tainted_sandstone", has((ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()))
         .save(result);
      slab(
         result,
         RecipeCategory.DECORATIONS,
         (ItemLike)WitherStormModItems.TAINTED_CUT_SANDSTONE_SLAB.get(),
         (ItemLike)WitherStormModItems.TAINTED_CUT_SANDSTONE.get()
      );
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_CUT_SANDSTONE.get()}),
            RecipeCategory.DECORATIONS,
            (ItemLike)WitherStormModItems.TAINTED_CUT_SANDSTONE_SLAB.get(),
            2
         )
         .unlockedBy("has_tainted_cut_sandstone", has((ItemLike)WitherStormModItems.TAINTED_CUT_SANDSTONE.get()))
         .save(result, new ResourceLocation("witherstormmod", WitherStormModItems.TAINTED_CUT_SANDSTONE_SLAB.getId().getPath() + "_stonecutting"));
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.TAINTED_CHISELED_SANDSTONE.get())
         .define('#', (ItemLike)WitherStormModItems.TAINTED_SANDSTONE_SLAB.get())
         .pattern("#")
         .pattern("#")
         .unlockedBy("has_tainted_sandstone_slab", has((ItemLike)WitherStormModItems.TAINTED_SANDSTONE_SLAB.get()))
         .save(result);
      SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()}),
            RecipeCategory.BUILDING_BLOCKS,
            (ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE.get(),
            0.1F,
            200
         )
         .unlockedBy("has_tainted_sandstone", has((ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()))
         .save(result);
      slab(
         result,
         RecipeCategory.DECORATIONS,
         (ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE_SLAB.get(),
         (ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE.get()
      );
      stairBuilder(
            (ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE_STAIRS.get(),
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE.get()})
         )
         .unlockedBy("has_tainted_smooth_sandstone", has((ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE.get()))
         .save(result);
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE.get()}),
            RecipeCategory.DECORATIONS,
            (ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE_SLAB.get(),
            2
         )
         .unlockedBy("has_tainted_smooth_sandstone", has((ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE.get()))
         .save(result, new ResourceLocation("witherstormmod", WitherStormModItems.TAINTED_SMOOTH_SANDSTONE_SLAB.getId().getPath() + "_stonecutting"));
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE.get()}),
            RecipeCategory.BUILDING_BLOCKS,
            (ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE_STAIRS.get()
         )
         .unlockedBy("has_tainted_smooth_sandstone", has((ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE.get()))
         .save(result, new ResourceLocation("witherstormmod", WitherStormModItems.TAINTED_SMOOTH_SANDSTONE_STAIRS.getId().getPath() + "_stonecutting"));
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE.get()}),
            RecipeCategory.DECORATIONS,
            (ItemLike)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_WALL.get()
         )
         .unlockedBy("has_tainted_smooth_sandstone", has((ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE.get()))
         .save(result, new ResourceLocation("witherstormmod", WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_WALL.getId().getPath() + "_stonecutting"));
      wall(
         result,
         RecipeCategory.DECORATIONS,
         (ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE_WALL.get(),
         (ItemLike)WitherStormModItems.TAINTED_SMOOTH_SANDSTONE.get()
      );
      slab(result, RecipeCategory.DECORATIONS, (ItemLike)WitherStormModItems.TAINTED_SLAB.get(), (ItemLike)WitherStormModItems.TAINTED_PLANKS.get());
      stairBuilder((ItemLike)WitherStormModItems.TAINTED_STAIRS.get(), Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_PLANKS.get()}))
         .unlockedBy("has_tainted_planks", has((ItemLike)WitherStormModItems.TAINTED_PLANKS.get()))
         .save(result);
      buttonBuilder(
            (ItemLike)WitherStormModItems.TAINTED_STONE_BUTTON.get(), Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_STONE.get()})
         )
         .unlockedBy("has_tainted_stone", has((ItemLike)WitherStormModItems.TAINTED_STONE.get()))
         .save(result);
      pressurePlate(result, (ItemLike)WitherStormModItems.TAINTED_STONE_PRESSURE_PLATE.get(), (ItemLike)WitherStormModItems.TAINTED_STONE.get());
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_STONE.get()}),
            RecipeCategory.DECORATIONS,
            (ItemLike)WitherStormModItems.TAINTED_STONE_SLAB.get(),
            2
         )
         .unlockedBy("has_tainted_stone", has((ItemLike)WitherStormModItems.TAINTED_STONE.get()))
         .save(result, new ResourceLocation("witherstormmod", WitherStormModItems.TAINTED_STONE_SLAB.getId().getPath() + "_stonecutting"));
      slab(result, RecipeCategory.DECORATIONS, (ItemLike)WitherStormModItems.TAINTED_STONE_SLAB.get(), (ItemLike)WitherStormModItems.TAINTED_STONE.get());
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_STONE.get()}),
            RecipeCategory.BUILDING_BLOCKS,
            (ItemLike)WitherStormModItems.TAINTED_STONE_STAIRS.get()
         )
         .unlockedBy("has_tainted_stone", has((ItemLike)WitherStormModItems.TAINTED_STONE.get()))
         .save(result, new ResourceLocation("witherstormmod", WitherStormModItems.TAINTED_STONE_STAIRS.getId().getPath() + "_stonecutting"));
      stairBuilder(
            (ItemLike)WitherStormModItems.TAINTED_STONE_STAIRS.get(), Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_STONE.get()})
         )
         .unlockedBy("has_tainted_stone", has((ItemLike)WitherStormModItems.TAINTED_STONE.get()))
         .save(result);
      SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_COBBLESTONE.get()}),
            RecipeCategory.BUILDING_BLOCKS,
            (ItemLike)WitherStormModItems.TAINTED_STONE.get(),
            0.1F,
            200
         )
         .unlockedBy("has_tainted_cobblestone", has((ItemLike)WitherStormModItems.TAINTED_COBBLESTONE.get()))
         .save(result);
      trapdoorBuilder((ItemLike)WitherStormModItems.TAINTED_TRAPDOOR.get(), Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_PLANKS.get()}))
         .unlockedBy("has_tainted_planks", has((ItemLike)WitherStormModItems.TAINTED_PLANKS.get()))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.TAINTED_WOOD.get())
         .define('#', (ItemLike)WitherStormModItems.TAINTED_LOG.get())
         .pattern("##")
         .pattern("##")
         .unlockedBy("has_tainted_log", has((ItemLike)WitherStormModItems.TAINTED_LOG.get()))
         .save(result);
      SuperBeaconRecipeBuilder.item(SuperBeaconRecipe.Condition.MAIN_ACTIVATED, RecipeCategory.MISC, (ItemLike)WitherStormModItems.WITHERED_BONE.get())
         .requires(Items.BONE, 6)
         .unlockedBy("has_bone", has(Items.BONE))
         .save(result);
      SuperBeaconRecipeBuilder.item(SuperBeaconRecipe.Condition.MAIN_ACTIVATED, RecipeCategory.MISC, (ItemLike)WitherStormModItems.WITHERED_FLESH.get())
         .requires(Items.ROTTEN_FLESH, 6)
         .unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH))
         .save(result);
      SuperBeaconRecipeBuilder.item(SuperBeaconRecipe.Condition.MAIN_ACTIVATED, RecipeCategory.MISC, (ItemLike)WitherStormModItems.WITHERED_SPIDER_EYE.get())
         .requires(Items.SPIDER_EYE, 6)
         .unlockedBy("has_spider_eye", has(Items.SPIDER_EYE))
         .save(result);
      SuperBeaconRecipeBuilder.item(SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.COMBAT, (ItemLike)WitherStormModItems.EYE_OF_THE_STORM.get())
         .requires((ItemLike)WitherStormModItems.COMMAND_BLOCK_SWORD.get())
         .requires((ItemLike)WitherStormModItems.TAINTED_FLESH_BLOCK.get(), 4)
         .requires((ItemLike)WitherStormModItems.TAINTED_DUST.get())
         .requires(Items.CARROT)
         .unlockedBy("has_command_block_sword", has((ItemLike)WitherStormModItems.COMMAND_BLOCK_SWORD.get()))
         .save(result);
      SuperBeaconRecipeBuilder.item(SuperBeaconRecipe.Condition.FULL_SUPPORTS, RecipeCategory.COMBAT, (ItemLike)WitherStormModItems.FORMIDI_BLADE.get())
         .requires((ItemLike)WitherStormModItems.COMMAND_BLOCK_SWORD.get())
         .requires(Items.END_CRYSTAL, 6)
         .requires((ItemLike)WitherStormModItems.FORMIDIBOMB.get())
         .unlockedBy("has_command_block_sword", has((ItemLike)WitherStormModItems.COMMAND_BLOCK_SWORD.get()))
         .save(result);
      ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, (ItemLike)WitherStormModItems.WITHERED_PHLEGM_BLOCK.get())
         .define('~', (ItemLike)WitherStormModItems.TAINTED_DUST.get())
         .define('H', Items.HOPPER)
         .define('B', (ItemLike)WitherStormModItems.TAINTED_DUST_BLOCK.get())
         .define('C', Items.CHEST)
         .define('S', Items.SLIME_BALL)
         .pattern("SHS")
         .pattern("~B~")
         .pattern("SCS")
         .unlockedBy("has_tainted_dust_block", has((ItemLike)WitherStormModItems.TAINTED_DUST_BLOCK.get()))
         .unlockedBy("has_tainted_dust", has((ItemLike)WitherStormModItems.TAINTED_DUST.get()))
         .save(result);
      SuperBeaconRecipeBuilder.item(
            SuperBeaconRecipe.Condition.MAIN_ACTIVATED, RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.TAINTED_DIRT.get()
         )
         .requires(Items.DIRT, 6)
         .unlockedBy("has_dirt", has(Items.DIRT))
         .save(result);
      SuperBeaconRecipeBuilder.item(
            SuperBeaconRecipe.Condition.MAIN_ACTIVATED, RecipeCategory.BUILDING_BLOCKS, (ItemLike)WitherStormModItems.TAINTED_TORCH.get()
         )
         .requires(Items.TORCH, 3)
         .unlockedBy("has_torch", has(Items.TORCH))
         .save(result, WitherStormMod.id("tainted_torch_beacon"));
      ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, (ItemLike)WitherStormModItems.TAINTED_TORCH.get())
         .define('~', Items.STICK)
         .define('1', (ItemLike)WitherStormModItems.TAINTED_DUST.get())
         .pattern("1")
         .pattern("~")
         .unlockedBy("has_tainted_dust", has((ItemLike)WitherStormModItems.TAINTED_DUST.get()))
         .save(result);
      SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()}),
            RecipeCategory.BUILDING_BLOCKS,
            (ItemLike)WitherStormModItems.TAINTED_CHISELED_SANDSTONE.get()
         )
         .unlockedBy("has_tainted_sandstone", has((ItemLike)WitherStormModItems.TAINTED_SANDSTONE.get()))
         .save(result, WitherStormMod.id("tainted_chiseled_sandstone_stonecutting"));
      cmdTool(result, Items.DIAMOND_SWORD, RecipeCategory.COMBAT, (Item)WitherStormModItems.COMMAND_BLOCK_SWORD.get());
      cmdTool(result, Items.DIAMOND_PICKAXE, RecipeCategory.TOOLS, (Item)WitherStormModItems.COMMAND_BLOCK_PICKAXE.get());
      cmdTool(result, Items.DIAMOND_AXE, RecipeCategory.TOOLS, (Item)WitherStormModItems.COMMAND_BLOCK_AXE.get());
      cmdTool(result, Items.DIAMOND_SHOVEL, RecipeCategory.TOOLS, (Item)WitherStormModItems.COMMAND_BLOCK_SHOVEL.get());
      cmdTool(result, Items.DIAMOND_HOE, RecipeCategory.TOOLS, (Item)WitherStormModItems.COMMAND_BLOCK_HOE.get());
      cmdTool(result, Items.WOODEN_SWORD, RecipeCategory.COMBAT, (Item)WitherStormModItems.WOOD_COMMAND_BLOCK_SWORD.get());
      cmdTool(result, Items.WOODEN_PICKAXE, RecipeCategory.TOOLS, (Item)WitherStormModItems.WOOD_COMMAND_BLOCK_PICKAXE.get());
      cmdTool(result, Items.WOODEN_AXE, RecipeCategory.TOOLS, (Item)WitherStormModItems.WOOD_COMMAND_BLOCK_AXE.get());
      cmdTool(result, Items.WOODEN_SHOVEL, RecipeCategory.TOOLS, (Item)WitherStormModItems.WOOD_COMMAND_BLOCK_SHOVEL.get());
      cmdTool(result, Items.WOODEN_HOE, RecipeCategory.TOOLS, (Item)WitherStormModItems.WOOD_COMMAND_BLOCK_HOE.get());
      cmdTool(result, Items.STONE_SWORD, RecipeCategory.COMBAT, (Item)WitherStormModItems.STONE_COMMAND_BLOCK_SWORD.get());
      cmdTool(result, Items.STONE_PICKAXE, RecipeCategory.TOOLS, (Item)WitherStormModItems.STONE_COMMAND_BLOCK_PICKAXE.get());
      cmdTool(result, Items.STONE_AXE, RecipeCategory.TOOLS, (Item)WitherStormModItems.STONE_COMMAND_BLOCK_AXE.get());
      cmdTool(result, Items.STONE_SHOVEL, RecipeCategory.TOOLS, (Item)WitherStormModItems.STONE_COMMAND_BLOCK_SHOVEL.get());
      cmdTool(result, Items.STONE_HOE, RecipeCategory.TOOLS, (Item)WitherStormModItems.STONE_COMMAND_BLOCK_HOE.get());
      cmdTool(result, Items.IRON_SWORD, RecipeCategory.COMBAT, (Item)WitherStormModItems.IRON_COMMAND_BLOCK_SWORD.get());
      cmdTool(result, Items.IRON_PICKAXE, RecipeCategory.TOOLS, (Item)WitherStormModItems.IRON_COMMAND_BLOCK_PICKAXE.get());
      cmdTool(result, Items.IRON_AXE, RecipeCategory.TOOLS, (Item)WitherStormModItems.IRON_COMMAND_BLOCK_AXE.get());
      cmdTool(result, Items.IRON_SHOVEL, RecipeCategory.TOOLS, (Item)WitherStormModItems.IRON_COMMAND_BLOCK_SHOVEL.get());
      cmdTool(result, Items.IRON_HOE, RecipeCategory.TOOLS, (Item)WitherStormModItems.IRON_COMMAND_BLOCK_HOE.get());
      cmdTool(result, Items.GOLDEN_SWORD, RecipeCategory.COMBAT, (Item)WitherStormModItems.GOLD_COMMAND_BLOCK_SWORD.get());
      cmdTool(result, Items.GOLDEN_PICKAXE, RecipeCategory.TOOLS, (Item)WitherStormModItems.GOLD_COMMAND_BLOCK_PICKAXE.get());
      cmdTool(result, Items.GOLDEN_AXE, RecipeCategory.TOOLS, (Item)WitherStormModItems.GOLD_COMMAND_BLOCK_AXE.get());
      cmdTool(result, Items.GOLDEN_SHOVEL, RecipeCategory.TOOLS, (Item)WitherStormModItems.GOLD_COMMAND_BLOCK_SHOVEL.get());
      cmdTool(result, Items.GOLDEN_HOE, RecipeCategory.TOOLS, (Item)WitherStormModItems.GOLD_COMMAND_BLOCK_HOE.get());
   }

   private static RecipeBuilder unlockedByItems(RecipeBuilder builder, Item... items) {
      for (Item item : items) {
         builder.unlockedBy(getHasName(item), has(item));
      }

      return builder;
   }

   private static void reubenPig(RecipeOutput consumer) {
      CompoundTag pigTag = new CompoundTag();
      pigTag.putInt("Age", -1200);
      CompoundTag name = new CompoundTag();
      name.putString("text", "reuben");
      pigTag.put("CustomName", name);
      SuperBeaconRecipeBuilder.entity(SuperBeaconRecipe.Condition.NONE, RecipeCategory.MISC, EntityType.PIG, pigTag)
         .requires(Items.PORKCHOP, 16)
         .save(consumer, ResourceLocation.fromNamespaceAndPath("witherstormmod", "summon_pig"));
   }

   private static void cmdTool(RecipeOutput result, Item required, RecipeCategory category, Item output) {
      AnvilRecipeBuilder.commandBlockTool(required, output).save(result, WitherStormMod.id(getItemName(output) + "_anvil"));
   }
}
