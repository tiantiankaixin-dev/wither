package nonamecrackers2.witherstormmod.common.data.loot;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemEnchantmentsPredicate;
import net.minecraft.advancements.critereon.ItemPredicate.Builder;
import net.minecraft.advancements.critereon.ItemSubPredicates;
import net.minecraft.advancements.critereon.MinMaxBounds.Ints;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;

public class WitherStormModBlockLootProvider extends BlockLootSubProvider {
   private static final Set<Item> EXPLOSION_RESISTANT = Set.of(
      (Item)WitherStormModItems.FORMIDIBOMB.get(),
      (Item)WitherStormModItems.INFECTED_FLESH_BLOCK.get(),
      (Item)WitherStormModItems.SUPER_BEACON.get(),
      (Item)WitherStormModItems.SUPER_SUPPORT_BEACON.get(),
      (Item)WitherStormModItems.SUPER_TNT.get()
   );

   public WitherStormModBlockLootProvider(HolderLookup.Provider registries) {
      super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags(), registries);
   }

   protected void generate() {
      this.dropSelf((Block)WitherStormModBlocks.FIREWORK_BUNDLE.get());
      this.dropSelf((Block)WitherStormModBlocks.FORMIDIBOMB.get());
      this.dropSelf((Block)WitherStormModBlocks.INFECTED_FLESH_BLOCK.get());
      this.dropPottedContents((Block)WitherStormModBlocks.POTTED_TAINTED_MUSHROOM.get());
      this.dropSelf((Block)WitherStormModBlocks.SUPER_BEACON.get());
      this.dropSelf((Block)WitherStormModBlocks.SUPER_SUPPORT_BEACON.get());
      this.dropSelf((Block)WitherStormModBlocks.SUPER_TNT.get());
      this.add((Block)WitherStormModBlocks.TAINTED_BONE_PILE.get(), this::createTaintedSkullRemainsTable);
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_BUTTON.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_CARVED_PUMPKIN.get());
      this.add((Block)WitherStormModBlocks.TAINTED_COBBLESTONE_SLAB.get(), x$0 -> this.createSlabItemTable(x$0));
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_COBBLESTONE_STAIRS.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_COBBLESTONE_WALL.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_COBBLESTONE.get());
      this.add((Block)WitherStormModBlocks.TAINTED_DOOR.get(), x$0 -> this.createDoorTable(x$0));
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_DUST_BLOCK.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_DUST.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_FENCE_GATE.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_FENCE.get());
      this.add(
         (Block)WitherStormModBlocks.TAINTED_FLESH_VEINS.get(),
         b -> this.createMultifaceBlockDrops(
               b,
               MatchTool.toolMatches(
                  Builder.item()
                     .withSubPredicate(
                        ItemSubPredicates.ENCHANTMENTS,
                        ItemEnchantmentsPredicate.enchantments(
                           List.of(
                              new EnchantmentPredicate(
                                 this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH),
                                 Ints.atLeast(1)
                              )
                           )
                        )
                     )
               )
            )
      );
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_FLESH_BLOCK.get());
      this.dropWhenSilkTouch((Block)WitherStormModBlocks.TAINTED_GLASS_PANE.get());
      this.dropWhenSilkTouch((Block)WitherStormModBlocks.TAINTED_GLASS.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_JACK_O_LANTERN.get());
      this.add((Block)WitherStormModBlocks.TAINTED_LEAVES.get(), x$0 -> this.createMangroveLeavesDrops(x$0));
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_LOG.get());
      this.dropSelf((Block)WitherStormModBlocks.STRIPPED_TAINTED_LOG.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_TORCH.get());
      this.add((Block)WitherStormModBlocks.TAINTED_WALL_TORCH.get(), this.createSingleItemTable((ItemLike)WitherStormModBlocks.TAINTED_TORCH.get()));
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_MUSHROOM.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_PLANKS.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_PRESSURE_PLATE.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_PUMPKIN.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_SAND.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_SANDSTONE.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_SANDSTONE_STAIRS.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_SANDSTONE_SLAB.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_SANDSTONE_WALL.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE_SLAB.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_CHISELED_SANDSTONE.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_STAIRS.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_SLAB.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_WALL.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_DIRT.get());
      this.add((Block)WitherStormModBlocks.TAINTED_SKELETON_WALL.get(), this::createTaintedSkullRemainsTable);
      this.add((Block)WitherStormModBlocks.TAINTED_SKULL_CEILING.get(), this::createTaintedSkullRemainsTable);
      this.add((Block)WitherStormModBlocks.TAINTED_SLAB.get(), x$0 -> this.createSlabItemTable(x$0));
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_STAIRS.get());
      this.add((Block)WitherStormModBlocks.TAINTED_STONE.get(), b -> this.createSingleItemTableWithSilkTouch(b, (ItemLike)WitherStormModBlocks.TAINTED_COBBLESTONE.get()));
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_STONE_BUTTON.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_STONE_PRESSURE_PLATE.get());
      this.add((Block)WitherStormModBlocks.TAINTED_STONE_SLAB.get(), x$0 -> this.createSlabItemTable(x$0));
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_STONE_STAIRS.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_TRAPDOOR.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_WOOD.get());
      this.dropSelf((Block)WitherStormModBlocks.STRIPPED_TAINTED_WOOD.get());
      this.add((Block)WitherStormModBlocks.TAINTED_ZOMBIE_LYING.get(), this::createTaintedZombieRemainsTable);
      this.add((Block)WitherStormModBlocks.TAINTED_ZOMBIE_SITTING.get(), this::createTaintedZombieRemainsTable);
      this.add((Block)WitherStormModBlocks.TAINTED_ZOMBIE_WALL.get(), this::createTaintedZombieRemainsTable);
      this.dropSelf((Block)WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.get());
      this.dropSelf((Block)WitherStormModBlocks.TAINTED_SIGN.get());
   }

   protected Iterable<Block> getKnownBlocks() {
      List<Block> list = Lists.newArrayList();
      list.add((Block)WitherStormModBlocks.FIREWORK_BUNDLE.get());
      list.add((Block)WitherStormModBlocks.FORMIDIBOMB.get());
      list.add((Block)WitherStormModBlocks.INFECTED_FLESH_BLOCK.get());
      list.add((Block)WitherStormModBlocks.POTTED_TAINTED_MUSHROOM.get());
      list.add((Block)WitherStormModBlocks.SUPER_BEACON.get());
      list.add((Block)WitherStormModBlocks.SUPER_SUPPORT_BEACON.get());
      list.add((Block)WitherStormModBlocks.SUPER_TNT.get());
      list.add((Block)WitherStormModBlocks.TAINTED_BONE_PILE.get());
      list.add((Block)WitherStormModBlocks.TAINTED_BUTTON.get());
      list.add((Block)WitherStormModBlocks.TAINTED_CARVED_PUMPKIN.get());
      list.add((Block)WitherStormModBlocks.TAINTED_COBBLESTONE_SLAB.get());
      list.add((Block)WitherStormModBlocks.TAINTED_COBBLESTONE_STAIRS.get());
      list.add((Block)WitherStormModBlocks.TAINTED_COBBLESTONE_WALL.get());
      list.add((Block)WitherStormModBlocks.TAINTED_COBBLESTONE.get());
      list.add((Block)WitherStormModBlocks.TAINTED_DOOR.get());
      list.add((Block)WitherStormModBlocks.TAINTED_DUST_BLOCK.get());
      list.add((Block)WitherStormModBlocks.TAINTED_DUST.get());
      list.add((Block)WitherStormModBlocks.TAINTED_FENCE_GATE.get());
      list.add((Block)WitherStormModBlocks.TAINTED_FENCE.get());
      list.add((Block)WitherStormModBlocks.TAINTED_FLESH_VEINS.get());
      list.add((Block)WitherStormModBlocks.TAINTED_FLESH_BLOCK.get());
      list.add((Block)WitherStormModBlocks.TAINTED_GLASS_PANE.get());
      list.add((Block)WitherStormModBlocks.TAINTED_GLASS.get());
      list.add((Block)WitherStormModBlocks.TAINTED_JACK_O_LANTERN.get());
      list.add((Block)WitherStormModBlocks.TAINTED_LEAVES.get());
      list.add((Block)WitherStormModBlocks.TAINTED_LOG.get());
      list.add((Block)WitherStormModBlocks.STRIPPED_TAINTED_LOG.get());
      list.add((Block)WitherStormModBlocks.TAINTED_MUSHROOM.get());
      list.add((Block)WitherStormModBlocks.TAINTED_TORCH.get());
      list.add((Block)WitherStormModBlocks.TAINTED_WALL_TORCH.get());
      list.add((Block)WitherStormModBlocks.TAINTED_PLANKS.get());
      list.add((Block)WitherStormModBlocks.TAINTED_PRESSURE_PLATE.get());
      list.add((Block)WitherStormModBlocks.TAINTED_PUMPKIN.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SAND.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SANDSTONE.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SANDSTONE_STAIRS.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SANDSTONE_SLAB.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SANDSTONE_WALL.get());
      list.add((Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE.get());
      list.add((Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE_SLAB.get());
      list.add((Block)WitherStormModBlocks.TAINTED_CHISELED_SANDSTONE.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_STAIRS.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_SLAB.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_WALL.get());
      list.add((Block)WitherStormModBlocks.TAINTED_DIRT.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SKELETON_WALL.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SKULL_CEILING.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SLAB.get());
      list.add((Block)WitherStormModBlocks.TAINTED_STAIRS.get());
      list.add((Block)WitherStormModBlocks.TAINTED_STONE_BUTTON.get());
      list.add((Block)WitherStormModBlocks.TAINTED_STONE_PRESSURE_PLATE.get());
      list.add((Block)WitherStormModBlocks.TAINTED_STONE_SLAB.get());
      list.add((Block)WitherStormModBlocks.TAINTED_STONE_STAIRS.get());
      list.add((Block)WitherStormModBlocks.TAINTED_STONE.get());
      list.add((Block)WitherStormModBlocks.TAINTED_TRAPDOOR.get());
      list.add((Block)WitherStormModBlocks.TAINTED_WOOD.get());
      list.add((Block)WitherStormModBlocks.STRIPPED_TAINTED_WOOD.get());
      list.add((Block)WitherStormModBlocks.TAINTED_ZOMBIE_LYING.get());
      list.add((Block)WitherStormModBlocks.TAINTED_ZOMBIE_SITTING.get());
      list.add((Block)WitherStormModBlocks.TAINTED_ZOMBIE_WALL.get());
      list.add((Block)WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.get());
      list.add((Block)WitherStormModBlocks.TAINTED_SIGN.get());
      return list;
   }

   protected net.minecraft.world.level.storage.loot.LootTable.Builder createTaintedSkullRemainsTable(Block block) {
      return LootTable.lootTable()
         .withPool(
            (net.minecraft.world.level.storage.loot.LootPool.Builder)this.applyExplosionCondition(
               block,
               LootPool.lootPool()
                  .setRolls(ConstantValue.exactly(2.0F))
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.TAINTED_DUST.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                  )
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_BONE.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                  )
            )
         );
   }

   protected net.minecraft.world.level.storage.loot.LootTable.Builder createTaintedZombieRemainsTable(Block block) {
      return LootTable.lootTable()
         .withPool(
            (net.minecraft.world.level.storage.loot.LootPool.Builder)this.applyExplosionCondition(
               block,
               LootPool.lootPool()
                  .setRolls(ConstantValue.exactly(2.0F))
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_FLESH.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                  )
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_BONE.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                  )
            )
         );
   }
}
