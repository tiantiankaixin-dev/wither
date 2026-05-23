package nonamecrackers2.witherstormmod.common.data.loot;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.advancements.critereon.EntityPredicate.Builder;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;

public class WitherStormModEntityLootProvider extends EntityLootSubProvider {
   public WitherStormModEntityLootProvider() {
      super(FeatureFlags.REGISTRY.allFlags());
   }

   public void generate() {
      this.add(
         WitherStormModEntityTypes.SICKENED_CHICKEN.get(),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem(Items.FEATHER)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_FLESH.get())
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
      );
      this.add(
         WitherStormModEntityTypes.SICKENED_COW.get(),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem(Items.LEATHER)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_FLESH.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
      );
      this.add(
         WitherStormModEntityTypes.SICKENED_PIG.get(),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_FLESH.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
      );
      this.add(
         WitherStormModEntityTypes.SICKENED_CREEPER.get(),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_FLESH.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem(Items.GUNPOWDER)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
            .withPool(
               LootPool.lootPool()
                  .add(TagEntry.expandTag(ItemTags.CREEPER_DROP_MUSIC_DISCS))
                  .when(LootItemEntityPropertyCondition.hasProperties(EntityTarget.KILLER, Builder.entity().of(EntityTypeTags.SKELETONS)))
            )
      );
      this.add(
         WitherStormModEntityTypes.SICKENED_MUSHROOM_COW.get(),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem(Items.LEATHER)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_FLESH.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
      );
      this.add(
         WitherStormModEntityTypes.SICKENED_PHANTOM.get(),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem(Items.PHANTOM_MEMBRANE)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
                  .when(LootItemKilledByPlayerCondition.killedByPlayer())
            )
      );
      this.add(
         WitherStormModEntityTypes.SICKENED_SKELETON.get(),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem(Items.ARROW)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_BONE.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
      );
      this.add(
         WitherStormModEntityTypes.SICKENED_SPIDER.get(),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_FLESH.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem(Items.STRING)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_SPIDER_EYE.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(-1.0F, 1.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
                  .when(LootItemKilledByPlayerCondition.killedByPlayer())
            )
      );
      this.add(
         WitherStormModEntityTypes.SICKENED_ZOMBIE.get(),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_FLESH.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
            .withPool(
               LootPool.lootPool()
                  .add(LootItem.lootTableItem(Items.IRON_INGOT))
                  .add(LootItem.lootTableItem(Items.CARROT))
                  .add(LootItem.lootTableItem(Items.POTATO))
                  .when(LootItemKilledByPlayerCondition.killedByPlayer())
                  .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))
            )
      );
      this.add(
         WitherStormModEntityTypes.WITHERED_SYMBIONT.get(),
         LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem((ItemLike)WitherStormModItems.COMMAND_BLOCK_BOOK.get())))
      );
      this.add(
         WitherStormModEntityTypes.SICKENED_VILLAGER.get(),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem((ItemLike)WitherStormModItems.WITHERED_FLESH.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                  )
            )
            .withPool(
               LootPool.lootPool()
                  .when(LootItemKilledByPlayerCondition.killedByPlayer())
                  .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))
                  .add(LootItem.lootTableItem(Items.IRON_INGOT))
                  .add(LootItem.lootTableItem(Items.CARROT))
                  .add(
                     LootItem.lootTableItem(Items.POTATO)
                        .apply(
                           SmeltItemFunction.smelted()
                              .when(
                                 LootItemEntityPropertyCondition.hasProperties(
                                    EntityTarget.THIS,
                                    Builder.entity()
                                       .flags(net.minecraft.advancements.critereon.EntityFlagsPredicate.Builder.flags().setOnFire(true).build())
                                       .build()
                                 )
                              )
                        )
                  )
            )
      );
      this.add(
         WitherStormModEntityTypes.SICKENED_IRON_GOLEM.get(),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool().add(LootItem.lootTableItem(Items.WITHER_ROSE).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))))
            )
            .withPool(
               LootPool.lootPool().add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F))))
            )
      );
      this.add(
         WitherStormModEntityTypes.SICKENED_SNOW_GOLEM.get(),
         LootTable.lootTable()
            .withPool(
               LootPool.lootPool().add(LootItem.lootTableItem(Items.SNOWBALL).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F))))
            )
      );
   }

   protected Stream<EntityType<?>> getKnownEntityTypes() {
      List<EntityType<?>> list = Lists.newArrayList();
      list.add((EntityType<?>)WitherStormModEntityTypes.SICKENED_CHICKEN.get());
      list.add((EntityType<?>)WitherStormModEntityTypes.SICKENED_COW.get());
      list.add((EntityType<?>)WitherStormModEntityTypes.SICKENED_PIG.get());
      list.add((EntityType<?>)WitherStormModEntityTypes.SICKENED_CREEPER.get());
      list.add((EntityType<?>)WitherStormModEntityTypes.SICKENED_MUSHROOM_COW.get());
      list.add((EntityType<?>)WitherStormModEntityTypes.SICKENED_PHANTOM.get());
      list.add((EntityType<?>)WitherStormModEntityTypes.SICKENED_SKELETON.get());
      list.add((EntityType<?>)WitherStormModEntityTypes.SICKENED_SPIDER.get());
      list.add((EntityType<?>)WitherStormModEntityTypes.SICKENED_ZOMBIE.get());
      list.add((EntityType<?>)WitherStormModEntityTypes.WITHERED_SYMBIONT.get());
      list.add((EntityType<?>)WitherStormModEntityTypes.SICKENED_VILLAGER.get());
      list.add((EntityType<?>)WitherStormModEntityTypes.SICKENED_IRON_GOLEM.get());
      list.add((EntityType<?>)WitherStormModEntityTypes.SICKENED_SNOW_GOLEM.get());
      return list.stream();
   }
}
