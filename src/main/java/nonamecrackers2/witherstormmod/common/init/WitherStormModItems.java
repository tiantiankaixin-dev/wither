/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.food.FoodProperties
 *  net.minecraft.world.food.FoodProperties$Builder
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemNameBlockItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.SignItem
 *  net.minecraft.world.item.SimpleFoiledItem
 *  net.minecraft.world.item.StandingAndWallBlockItem
 *  net.minecraft.world.item.Tier
 *  net.minecraft.world.item.alchemy.Potion
 *  net.minecraft.world.item.alchemy.PotionUtils
 *  net.minecraft.world.item.alchemy.Potions
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 *  net.minecraftforge.common.ForgeSpawnEggItem
 *  net.minecraftforge.common.brewing.BrewingRecipe
 *  net.minecraftforge.common.brewing.BrewingRecipeRegistry
 *  net.minecraftforge.common.brewing.IBrewingRecipe
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.IForgeRegistry
 *  net.minecraftforge.registries.RegistryObject
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModPotions
 *  nonamecrackers2.witherstormmod.common.item.AmuletItem
 *  nonamecrackers2.witherstormmod.common.item.CommandBlockAxeItem
 *  nonamecrackers2.witherstormmod.common.item.CommandBlockHoeItem
 *  nonamecrackers2.witherstormmod.common.item.CommandBlockPickaxeItem
 *  nonamecrackers2.witherstormmod.common.item.CommandBlockShovelItem
 *  nonamecrackers2.witherstormmod.common.item.CommandBlockSwordItem
 *  nonamecrackers2.witherstormmod.common.item.EyeOfTheStormItem
 *  nonamecrackers2.witherstormmod.common.item.FormidiBladeItem
 *  nonamecrackers2.witherstormmod.common.item.FormidibombItem
 *  nonamecrackers2.witherstormmod.common.item.GoldenAppleStewItem
 *  nonamecrackers2.witherstormmod.common.item.PhasometerItem
 *  nonamecrackers2.witherstormmod.common.item.TaintedCarvedPumpkinItem
 *  nonamecrackers2.witherstormmod.common.item.WitheredNetherStarItem
 *  nonamecrackers2.witherstormmod.common.util.WitherStormModItemTier
 */
package nonamecrackers2.witherstormmod.common.init;

import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.event.brewing.BrewingRecipeRegisterEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPotions;
import nonamecrackers2.witherstormmod.common.item.AmuletItem;
import nonamecrackers2.witherstormmod.common.item.CommandBlockAxeItem;
import nonamecrackers2.witherstormmod.common.item.CommandBlockHoeItem;
import nonamecrackers2.witherstormmod.common.item.CommandBlockPickaxeItem;
import nonamecrackers2.witherstormmod.common.item.CommandBlockShovelItem;
import nonamecrackers2.witherstormmod.common.item.CommandBlockSwordItem;
import nonamecrackers2.witherstormmod.common.item.EyeOfTheStormItem;
import nonamecrackers2.witherstormmod.common.item.FormidiBladeItem;
import nonamecrackers2.witherstormmod.common.item.FormidibombItem;
import nonamecrackers2.witherstormmod.common.item.GoldenAppleStewItem;
import nonamecrackers2.witherstormmod.common.item.PhasometerItem;
import nonamecrackers2.witherstormmod.common.item.SourceTestFireballItem;
import nonamecrackers2.witherstormmod.common.item.TaintedCarvedPumpkinItem;
import nonamecrackers2.witherstormmod.common.item.WitheredNetherStarItem;
import nonamecrackers2.witherstormmod.common.util.PotionStackUtil;
import nonamecrackers2.witherstormmod.common.util.WitherStormModItemTier;

public class WitherStormModItems {
    public static final FoodProperties GOLDEN_APPLE_STEW_FOOD = new FoodProperties.Builder().nutrition(5).saturationModifier(1.0f).effect(new MobEffectInstance(MobEffects.ABSORPTION, 2600, 0), 1.0f).effect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 1.0f).alwaysEdible().build();
    public static final FoodProperties WITHERED_FLESH_FOOD = new FoodProperties.Builder().nutrition(4).saturationModifier(0.1f).effect(new MobEffectInstance(MobEffects.HUNGER, 800, 0), 0.8f).effect(new MobEffectInstance(MobEffects.WITHER, 400, 0), 1.0f).build();
    public static final FoodProperties WITHERED_SPIDER_EYE_FOOD = new FoodProperties.Builder().nutrition(2).saturationModifier(0.8f).effect(new MobEffectInstance(MobEffects.POISON, 200, 0), 1.0f).effect(new MobEffectInstance(MobEffects.WITHER, 400, 0), 1.0f).build();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create((IForgeRegistry)ForgeRegistries.ITEMS, (String)"witherstormmod");
    public static final RegistryObject<Item> WITHERED_BONE = ITEMS.register("withered_bone", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> WITHERED_FLESH = ITEMS.register("withered_flesh", () -> new Item(new Item.Properties().food(WITHERED_FLESH_FOOD).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> TAINTED_DUST = ITEMS.register("tainted_dust", () -> new ItemNameBlockItem((Block)WitherStormModBlocks.TAINTED_DUST.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> WITHERED_SPIDER_EYE = ITEMS.register("withered_spider_eye", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).food(WITHERED_SPIDER_EYE_FOOD)));
    public static final RegistryObject<Item> GOLDEN_APPLE_STEW = ITEMS.register("golden_apple_stew", () -> new GoldenAppleStewItem(new Item.Properties().rarity(Rarity.RARE).food(GOLDEN_APPLE_STEW_FOOD).stacksTo(1).craftRemainder(Items.BOWL)));
    public static final RegistryObject<Item> AMULET = ITEMS.register("amulet", () -> new AmuletItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> COMMAND_BLOCK_BOOK = ITEMS.register("command_block_book", () -> new Item(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).fireResistant()) {
        @Override
        public boolean isFoil(ItemStack stack) {
            return true;
        }
    });
    public static final RegistryObject<Item> WITHERED_NETHER_STAR = ITEMS.register("withered_nether_star", () -> new WitheredNetherStarItem(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> SICKENED_CREEPER_SPAWN_EGG = ITEMS.register("sickened_creeper_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_CREEPER, 9851315, 3278099, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_SKELETON_SPAWN_EGG = ITEMS.register("sickened_skeleton_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_SKELETON, 13606575, 3612758, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_SPIDER_SPAWN_EGG = ITEMS.register("sickened_spider_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_SPIDER, 0x2B232B, 16056399, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_VILLAGER_SPAWN_EGG = ITEMS.register("sickened_villager_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_VILLAGER, 8551284, 11305627, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_ZOMBIE_SPAWN_EGG = ITEMS.register("sickened_zombie_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_ZOMBIE, 4808027, 10648470, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_PHANTOM_SPAWN_EGG = ITEMS.register("sickened_phantom_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_PHANTOM, 6967167, 16713046, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_CHICKEN_SPAWN_EGG = ITEMS.register("sickened_chicken_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_CHICKEN, 5977232, 0x550058, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_PARROT_SPAWN_EGG = ITEMS.register("sickened_parrot_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_PARROT, 7032441, 5911693, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_WOLF_SPAWN_EGG = ITEMS.register("sickened_wolf_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_WOLF, 4866401, 7960207, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_CAT_SPAWN_EGG = ITEMS.register("sickened_cat_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_CAT, 1775149, 9595267, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_COW_SPAWN_EGG = ITEMS.register("sickened_cow_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_COW, 3613496, 0x999999, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_PIG_SPAWN_EGG = ITEMS.register("sickened_pig_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_PIG, 6706811, 5786734, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_MUSHROOM_COW_SPAWN_EGG = ITEMS.register("sickened_mushroom_cow_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_MUSHROOM_COW, 8200599, 11887564, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_BEE_SPAWN_EGG = ITEMS.register("sickened_bee_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_BEE, 10711155, 3023140, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_PILLAGER_SPAWN_EGG = ITEMS.register("sickened_pillager_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_PILLAGER, 4403259, 10190758, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_VINDICATOR_SPAWN_EGG = ITEMS.register("sickened_vindicator_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_VINDICATOR, 10190758, 3422273, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_IRON_GOLEM_SPAWN_EGG = ITEMS.register("sickened_iron_golem_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_IRON_GOLEM, 13541842, 15270143, new Item.Properties()));
    public static final RegistryObject<Item> SICKENED_SNOW_GOLEM_SPAWN_EGG = ITEMS.register("sickened_snow_golem_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.SICKENED_SNOW_GOLEM, 15589887, 12754175, new Item.Properties()));
    public static final RegistryObject<Item> TENTACLE_SPAWN_EGG = ITEMS.register("tentacle_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.TENTACLE, 722193, 1379103, new Item.Properties()));
    public static final RegistryObject<Item> WITHERED_SYMBIONT_SPAWN_EGG = ITEMS.register("withered_symbiont_spawn_egg", () -> new ForgeSpawnEggItem((Supplier)WitherStormModEntityTypes.WITHERED_SYMBIONT, 2233397, 16056568, new Item.Properties()));
    public static final RegistryObject<Item> COMMAND_BLOCK_SWORD = ITEMS.register("command_block_sword", () -> new CommandBlockSwordItem((Tier)WitherStormModItemTier.COMMAND_BLOCK, 3, -2.4f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> COMMAND_BLOCK_PICKAXE = ITEMS.register("command_block_pickaxe", () -> new CommandBlockPickaxeItem((Tier)WitherStormModItemTier.COMMAND_BLOCK, 1, -2.8f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> COMMAND_BLOCK_AXE = ITEMS.register("command_block_axe", () -> new CommandBlockAxeItem((Tier)WitherStormModItemTier.COMMAND_BLOCK, 5, -3.0f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> COMMAND_BLOCK_SHOVEL = ITEMS.register("command_block_shovel", () -> new CommandBlockShovelItem((Tier)WitherStormModItemTier.COMMAND_BLOCK, 1.5f, -3.4f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> COMMAND_BLOCK_HOE = ITEMS.register("command_block_hoe", () -> new CommandBlockHoeItem((Tier)WitherStormModItemTier.COMMAND_BLOCK, -4, 0.0f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> WOOD_COMMAND_BLOCK_SWORD = ITEMS.register("wooden_command_block_sword", () -> new CommandBlockSwordItem((Tier)WitherStormModItemTier.WOOD_CMD, 3, -2.4f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> WOOD_COMMAND_BLOCK_PICKAXE = ITEMS.register("wooden_command_block_pickaxe", () -> new CommandBlockPickaxeItem((Tier)WitherStormModItemTier.WOOD_CMD, 1, -2.8f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> WOOD_COMMAND_BLOCK_AXE = ITEMS.register("wooden_command_block_axe", () -> new CommandBlockAxeItem((Tier)WitherStormModItemTier.WOOD_CMD, 6, -3.2f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> WOOD_COMMAND_BLOCK_SHOVEL = ITEMS.register("wooden_command_block_shovel", () -> new CommandBlockShovelItem((Tier)WitherStormModItemTier.WOOD_CMD, 1.5f, -3.0f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> WOOD_COMMAND_BLOCK_HOE = ITEMS.register("wooden_command_block_hoe", () -> new CommandBlockHoeItem((Tier)WitherStormModItemTier.WOOD_CMD, -4, 3.0f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> STONE_COMMAND_BLOCK_SWORD = ITEMS.register("stone_command_block_sword", () -> new CommandBlockSwordItem((Tier)WitherStormModItemTier.STONE_CMD, 3, -2.4f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> STONE_COMMAND_BLOCK_PICKAXE = ITEMS.register("stone_command_block_pickaxe", () -> new CommandBlockPickaxeItem((Tier)WitherStormModItemTier.STONE_CMD, 1, -2.8f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> STONE_COMMAND_BLOCK_AXE = ITEMS.register("stone_command_block_axe", () -> new CommandBlockAxeItem((Tier)WitherStormModItemTier.STONE_CMD, 6, -3.2f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> STONE_COMMAND_BLOCK_SHOVEL = ITEMS.register("stone_command_block_shovel", () -> new CommandBlockShovelItem((Tier)WitherStormModItemTier.STONE_CMD, 1.5f, -2.0f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> STONE_COMMAND_BLOCK_HOE = ITEMS.register("stone_command_block_hoe", () -> new CommandBlockHoeItem((Tier)WitherStormModItemTier.STONE_CMD, -3, -3.0f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> IRON_COMMAND_BLOCK_SWORD = ITEMS.register("iron_command_block_sword", () -> new CommandBlockSwordItem((Tier)WitherStormModItemTier.IRON_CMD, 4, -2.8f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> IRON_COMMAND_BLOCK_PICKAXE = ITEMS.register("iron_command_block_pickaxe", () -> new CommandBlockPickaxeItem((Tier)WitherStormModItemTier.IRON_CMD, 3, -3.2f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> IRON_COMMAND_BLOCK_AXE = ITEMS.register("iron_command_block_axe", () -> new CommandBlockAxeItem((Tier)WitherStormModItemTier.IRON_CMD, 6, -3.1f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> IRON_COMMAND_BLOCK_SHOVEL = ITEMS.register("iron_command_block_shovel", () -> new CommandBlockShovelItem((Tier)WitherStormModItemTier.IRON_CMD, 2.5f, -3.0f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> IRON_COMMAND_BLOCK_HOE = ITEMS.register("iron_command_block_hoe", () -> new CommandBlockHoeItem((Tier)WitherStormModItemTier.IRON_CMD, 9, -3.5f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> GOLD_COMMAND_BLOCK_SWORD = ITEMS.register("gold_command_block_sword", () -> new CommandBlockSwordItem((Tier)WitherStormModItemTier.GOLD_CMD, -1, -1.2f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> GOLD_COMMAND_BLOCK_PICKAXE = ITEMS.register("gold_command_block_pickaxe", () -> new CommandBlockPickaxeItem((Tier)WitherStormModItemTier.GOLD_CMD, 1, -2.8f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> GOLD_COMMAND_BLOCK_AXE = ITEMS.register("gold_command_block_axe", () -> new CommandBlockAxeItem((Tier)WitherStormModItemTier.GOLD_CMD, 6, -3.0f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> GOLD_COMMAND_BLOCK_SHOVEL = ITEMS.register("gold_command_block_shovel", () -> new CommandBlockShovelItem((Tier)WitherStormModItemTier.GOLD_CMD, 1.5f, -3.0f, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> GOLD_COMMAND_BLOCK_HOE = ITEMS.register("gold_command_block_hoe", () -> new CommandBlockHoeItem((Tier)WitherStormModItemTier.GOLD_CMD, 0, -3.0f, new Item.Properties().fireResistant().fireResistant().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> EYE_OF_THE_STORM = ITEMS.register("eye_of_the_storm", () -> new EyeOfTheStormItem((Tier)WitherStormModItemTier.EYE_OF_THE_STORM, 3, -2.4f, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> FORMIDI_BLADE = ITEMS.register("formidi_blade", () -> new FormidiBladeItem((Tier)WitherStormModItemTier.FORMIDI_BLADE, 3, -3.7f, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> SUPER_TNT = ITEMS.register("super_tnt", () -> new BlockItem((Block)WitherStormModBlocks.SUPER_TNT.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> FORMIDIBOMB = ITEMS.register("formidibomb", () -> new FormidibombItem((Block)WitherStormModBlocks.FORMIDIBOMB.get(), new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> SUPER_BEACON = ITEMS.register("super_beacon", () -> new BlockItem((Block)WitherStormModBlocks.SUPER_BEACON.get(), new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> SUPER_SUPPORT_BEACON = ITEMS.register("super_support_beacon", () -> new BlockItem((Block)WitherStormModBlocks.SUPER_SUPPORT_BEACON.get(), new Item.Properties().rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> FIREWORK_BUNDLE = ITEMS.register("firework_bundle", () -> new BlockItem((Block)WitherStormModBlocks.FIREWORK_BUNDLE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PHASOMETER = ITEMS.register("phasometer", () -> new PhasometerItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> SOURCE_TEST_FIREBALL = ITEMS.register("source_test_fireball", () -> new SourceTestFireballItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> TAINTED_ZOMBIE_SITTING = ITEMS.register("tainted_zombie_sitting", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_ZOMBIE_SITTING.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> TAINTED_ZOMBIE_WALL = ITEMS.register("tainted_zombie_wall", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_ZOMBIE_WALL.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> TAINTED_ZOMBIE_LYING = ITEMS.register("tainted_zombie_lying", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_ZOMBIE_LYING.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> TAINTED_BONE_PILE = ITEMS.register("tainted_bone_pile", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_BONE_PILE.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> TAINTED_SKELETON_WALL = ITEMS.register("tainted_skeleton_wall", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SKELETON_WALL.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> TAINTED_SKULL_CEILING = ITEMS.register("tainted_skull_ceiling", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SKULL_CEILING.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> TAINTED_FLESH_VEINS = ITEMS.register("tainted_flesh_veins", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_FLESH_VEINS.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_FLESH_BLOCK = ITEMS.register("tainted_flesh_block", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_FLESH_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> INFECTED_FLESH_BLOCK = ITEMS.register("infected_flesh_block", () -> new BlockItem((Block)WitherStormModBlocks.INFECTED_FLESH_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> HARDENED_FLESH_BLOCK = ITEMS.register("hardened_flesh_block", () -> new BlockItem((Block)WitherStormModBlocks.HARDENED_FLESH_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> WITHERED_PHLEGM_BLOCK = ITEMS.register("withered_phlegm_block", () -> new BlockItem((Block)WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> TAINTED_STONE = ITEMS.register("tainted_stone", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_STONE_STAIRS = ITEMS.register("tainted_stone_stairs", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_STONE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_STONE_SLAB = ITEMS.register("tainted_stone_slab", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_STONE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_STONE_BUTTON = ITEMS.register("tainted_stone_button", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_STONE_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_STONE_PRESSURE_PLATE = ITEMS.register("tainted_stone_pressure_plate", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_STONE_PRESSURE_PLATE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_COBBLESTONE = ITEMS.register("tainted_cobblestone", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_COBBLESTONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_COBBLESTONE_STAIRS = ITEMS.register("tainted_cobblestone_stairs", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_COBBLESTONE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_COBBLESTONE_SLAB = ITEMS.register("tainted_cobblestone_slab", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_COBBLESTONE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_COBBLESTONE_WALL = ITEMS.register("tainted_cobblestone_wall", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_COBBLESTONE_WALL.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_DIRT = ITEMS.register("tainted_dirt", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_DIRT.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_SAND = ITEMS.register("tainted_sand", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SAND.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_SANDSTONE = ITEMS.register("tainted_sandstone", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SANDSTONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_SANDSTONE_SLAB = ITEMS.register("tainted_sandstone_slab", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SANDSTONE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_SANDSTONE_STAIRS = ITEMS.register("tainted_sandstone_stairs", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SANDSTONE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_SANDSTONE_WALL = ITEMS.register("tainted_sandstone_wall", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SANDSTONE_WALL.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_CUT_SANDSTONE = ITEMS.register("tainted_cut_sandstone", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_CUT_SANDSTONE_SLAB = ITEMS.register("tainted_cut_sandstone_slab", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_CHISELED_SANDSTONE = ITEMS.register("tainted_chiseled_sandstone", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_CHISELED_SANDSTONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_SMOOTH_SANDSTONE = ITEMS.register("tainted_smooth_sandstone", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_SMOOTH_SANDSTONE_STAIRS = ITEMS.register("tainted_smooth_sandstone_stairs", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_SMOOTH_SANDSTONE_SLAB = ITEMS.register("tainted_smooth_sandstone_slab", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_SMOOTH_SANDSTONE_WALL = ITEMS.register("tainted_smooth_sandstone_wall", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_WALL.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_GLASS = ITEMS.register("tainted_glass", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_GLASS.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_GLASS_PANE = ITEMS.register("tainted_glass_pane", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_GLASS_PANE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_PLANKS = ITEMS.register("tainted_planks", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_LOG = ITEMS.register("tainted_log", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_WOOD = ITEMS.register("tainted_wood", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_TAINTED_LOG = ITEMS.register("stripped_tainted_log", () -> new BlockItem((Block)WitherStormModBlocks.STRIPPED_TAINTED_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> STRIPPED_TAINTED_WOOD = ITEMS.register("stripped_tainted_wood", () -> new BlockItem((Block)WitherStormModBlocks.STRIPPED_TAINTED_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_LEAVES = ITEMS.register("tainted_leaves", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_DOOR = ITEMS.register("tainted_door", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_DOOR.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_TRAPDOOR = ITEMS.register("tainted_trapdoor", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_TRAPDOOR.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_BUTTON = ITEMS.register("tainted_button", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_PRESSURE_PLATE = ITEMS.register("tainted_pressure_plate", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_PRESSURE_PLATE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_STAIRS = ITEMS.register("tainted_stairs", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_SLAB = ITEMS.register("tainted_slab", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_FENCE = ITEMS.register("tainted_fence", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_FENCE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_FENCE_GATE = ITEMS.register("tainted_fence_gate", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_FENCE_GATE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_MUSHROOM = ITEMS.register("tainted_mushroom", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_MUSHROOM.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_TORCH = ITEMS.register("tainted_torch", () -> new StandingAndWallBlockItem((Block)WitherStormModBlocks.TAINTED_TORCH.get(), (Block)WitherStormModBlocks.TAINTED_WALL_TORCH.get(), new Item.Properties(), Direction.DOWN));
    public static final RegistryObject<Item> TAINTED_SIGN = ITEMS.register("tainted_sign", () -> new SignItem(new Item.Properties(), (Block)WitherStormModBlocks.TAINTED_SIGN.get(), (Block)WitherStormModBlocks.TAINTED_WALL_SIGN.get()));
    public static final RegistryObject<Item> TAINTED_PUMPKIN = ITEMS.register("tainted_pumpkin", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_PUMPKIN.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_CARVED_PUMPKIN = ITEMS.register("tainted_carved_pumpkin", () -> new TaintedCarvedPumpkinItem((Block)WitherStormModBlocks.TAINTED_CARVED_PUMPKIN.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_JACK_O_LANTERN = ITEMS.register("tainted_jack_o_lantern", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_JACK_O_LANTERN.get(), new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_DUST_BLOCK = ITEMS.register("tainted_dust_block", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_DUST_BLOCK.get(), new Item.Properties()));

    public static void registerBrewingRecipes(BrewingRecipeRegisterEvent event) {
        Holder<Potion> wither = WitherStormModPotions.WITHER.getHolder().orElseThrow();
        Holder<Potion> longWither = WitherStormModPotions.LONG_WITHER.getHolder().orElseThrow();
        Holder<Potion> strongWither = WitherStormModPotions.STRONG_WITHER.getHolder().orElseThrow();
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.POTION, Potions.POISON, (Item)WITHERED_SPIDER_EYE.get(), wither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.POTION, Potions.LONG_POISON, (Item)WITHERED_SPIDER_EYE.get(), longWither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.POTION, Potions.STRONG_POISON, (Item)WITHERED_SPIDER_EYE.get(), strongWither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.SPLASH_POTION, Potions.POISON, (Item)WITHERED_SPIDER_EYE.get(), wither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.SPLASH_POTION, Potions.LONG_POISON, (Item)WITHERED_SPIDER_EYE.get(), longWither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.SPLASH_POTION, Potions.STRONG_POISON, (Item)WITHERED_SPIDER_EYE.get(), strongWither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.LINGERING_POTION, Potions.POISON, (Item)WITHERED_SPIDER_EYE.get(), wither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.LINGERING_POTION, Potions.LONG_POISON, (Item)WITHERED_SPIDER_EYE.get(), longWither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.LINGERING_POTION, Potions.STRONG_POISON, (Item)WITHERED_SPIDER_EYE.get(), strongWither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.POTION, wither, Items.REDSTONE, longWither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.POTION, wither, Items.GLOWSTONE_DUST, strongWither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.SPLASH_POTION, wither, Items.REDSTONE, longWither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.SPLASH_POTION, wither, Items.GLOWSTONE_DUST, strongWither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.LINGERING_POTION, wither, Items.REDSTONE, longWither));
        event.addRecipe(WitherStormModItems.createBrewingRecipe(Items.LINGERING_POTION, wither, Items.GLOWSTONE_DUST, strongWither));
    }

    public static BrewingRecipe createBrewingRecipe(Item potionType, Holder<Potion> potion, Item ingredient, Holder<Potion> output) {
        return new BrewingRecipe(
            Ingredient.of(new ItemStack[]{PotionStackUtil.setPotion(new ItemStack((ItemLike)potionType), potion)}),
            Ingredient.of(new ItemLike[]{ingredient}),
            PotionStackUtil.setPotion(new ItemStack((ItemLike)potionType), output)
        );
    }
}

