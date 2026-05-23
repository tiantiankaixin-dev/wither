package nonamecrackers2.witherstormmod.common.util;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.Util;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;

public class EquipmentHelper {
   private static final List<EquipmentHelper.EquipmentType> HELMETS = Util.make(
      new java.util.ArrayList<EquipmentHelper.EquipmentType>(), helmetTypes -> {
         helmetTypes.add(new EquipmentHelper.EquipmentType(Items.AIR, 15));
         helmetTypes.add(new EquipmentHelper.EquipmentType(Items.LEATHER_HELMET, 25));
         helmetTypes.add(new EquipmentHelper.EquipmentType(Items.CHAINMAIL_HELMET, 20));
         helmetTypes.add(new EquipmentHelper.EquipmentType(Items.GOLDEN_HELMET, 15));
         helmetTypes.add(new EquipmentHelper.EquipmentType(Items.IRON_HELMET, 10));
         helmetTypes.add(new EquipmentHelper.EquipmentType(Items.DIAMOND_HELMET, 5));
         helmetTypes.add(new EquipmentHelper.EquipmentType(Items.TURTLE_HELMET, 1));
      }
   );
   private static final List<EquipmentHelper.EquipmentType> ADVANCED_HELMETS = Util.make(
      new java.util.ArrayList<EquipmentHelper.EquipmentType>(), helmetTypes -> {
         helmetTypes.addAll(HELMETS);
         helmetTypes.add(new EquipmentHelper.EquipmentType(Items.LEATHER_HELMET, 5));
         helmetTypes.add(new EquipmentHelper.EquipmentType(Items.CHAINMAIL_HELMET, 10));
         helmetTypes.add(new EquipmentHelper.EquipmentType(Items.GOLDEN_HELMET, 15));
         helmetTypes.add(new EquipmentHelper.EquipmentType(Items.IRON_HELMET, 20));
         helmetTypes.add(new EquipmentHelper.EquipmentType(Items.DIAMOND_HELMET, 10));
      }
   );
   private static final List<EquipmentHelper.EquipmentType> CHESTPLATES = Util.make(
      new java.util.ArrayList<EquipmentHelper.EquipmentType>(), chestplateTypes -> {
         chestplateTypes.add(new EquipmentHelper.EquipmentType(Items.AIR, 15));
         chestplateTypes.add(new EquipmentHelper.EquipmentType(Items.LEATHER_CHESTPLATE, 25));
         chestplateTypes.add(new EquipmentHelper.EquipmentType(Items.CHAINMAIL_CHESTPLATE, 20));
         chestplateTypes.add(new EquipmentHelper.EquipmentType(Items.GOLDEN_CHESTPLATE, 15));
         chestplateTypes.add(new EquipmentHelper.EquipmentType(Items.IRON_CHESTPLATE, 10));
         chestplateTypes.add(new EquipmentHelper.EquipmentType(Items.DIAMOND_CHESTPLATE, 5));
      }
   );
   private static final List<EquipmentHelper.EquipmentType> ADVANCED_CHESTPLATES = Util.make(
      new java.util.ArrayList<EquipmentHelper.EquipmentType>(), chestplateTypes -> {
         chestplateTypes.addAll(CHESTPLATES);
         chestplateTypes.add(new EquipmentHelper.EquipmentType(Items.LEATHER_CHESTPLATE, 5));
         chestplateTypes.add(new EquipmentHelper.EquipmentType(Items.CHAINMAIL_CHESTPLATE, 10));
         chestplateTypes.add(new EquipmentHelper.EquipmentType(Items.GOLDEN_CHESTPLATE, 15));
         chestplateTypes.add(new EquipmentHelper.EquipmentType(Items.IRON_CHESTPLATE, 20));
         chestplateTypes.add(new EquipmentHelper.EquipmentType(Items.DIAMOND_CHESTPLATE, 10));
      }
   );
   private static final List<EquipmentHelper.EquipmentType> LEGGINGS = Util.make(
      new java.util.ArrayList<EquipmentHelper.EquipmentType>(), leggingsTypes -> {
         leggingsTypes.add(new EquipmentHelper.EquipmentType(Items.AIR, 15));
         leggingsTypes.add(new EquipmentHelper.EquipmentType(Items.LEATHER_LEGGINGS, 25));
         leggingsTypes.add(new EquipmentHelper.EquipmentType(Items.CHAINMAIL_LEGGINGS, 20));
         leggingsTypes.add(new EquipmentHelper.EquipmentType(Items.GOLDEN_LEGGINGS, 15));
         leggingsTypes.add(new EquipmentHelper.EquipmentType(Items.IRON_LEGGINGS, 10));
         leggingsTypes.add(new EquipmentHelper.EquipmentType(Items.DIAMOND_LEGGINGS, 5));
      }
   );
   private static final List<EquipmentHelper.EquipmentType> ADVANCED_LEGGINGS = Util.make(
      new java.util.ArrayList<EquipmentHelper.EquipmentType>(), leggingsTypes -> {
         leggingsTypes.addAll(LEGGINGS);
         leggingsTypes.add(new EquipmentHelper.EquipmentType(Items.LEATHER_LEGGINGS, 5));
         leggingsTypes.add(new EquipmentHelper.EquipmentType(Items.CHAINMAIL_LEGGINGS, 10));
         leggingsTypes.add(new EquipmentHelper.EquipmentType(Items.GOLDEN_LEGGINGS, 15));
         leggingsTypes.add(new EquipmentHelper.EquipmentType(Items.IRON_LEGGINGS, 20));
         leggingsTypes.add(new EquipmentHelper.EquipmentType(Items.DIAMOND_LEGGINGS, 10));
      }
   );
   private static final List<EquipmentHelper.EquipmentType> BOOTS = Util.make(Lists.newArrayList(), bootsTypes -> {
      bootsTypes.add(new EquipmentHelper.EquipmentType(Items.AIR, 15));
      bootsTypes.add(new EquipmentHelper.EquipmentType(Items.LEATHER_BOOTS, 25));
      bootsTypes.add(new EquipmentHelper.EquipmentType(Items.CHAINMAIL_BOOTS, 20));
      bootsTypes.add(new EquipmentHelper.EquipmentType(Items.GOLDEN_BOOTS, 15));
      bootsTypes.add(new EquipmentHelper.EquipmentType(Items.IRON_BOOTS, 10));
      bootsTypes.add(new EquipmentHelper.EquipmentType(Items.DIAMOND_BOOTS, 5));
   });
   private static final List<EquipmentHelper.EquipmentType> ADVANCED_BOOTS = Util.make(
      new java.util.ArrayList<EquipmentHelper.EquipmentType>(), bootsTypes -> {
         bootsTypes.addAll(BOOTS);
         bootsTypes.add(new EquipmentHelper.EquipmentType(Items.LEATHER_BOOTS, 5));
         bootsTypes.add(new EquipmentHelper.EquipmentType(Items.CHAINMAIL_BOOTS, 15));
         bootsTypes.add(new EquipmentHelper.EquipmentType(Items.GOLDEN_BOOTS, 10));
         bootsTypes.add(new EquipmentHelper.EquipmentType(Items.IRON_BOOTS, 20));
         bootsTypes.add(new EquipmentHelper.EquipmentType(Items.DIAMOND_BOOTS, 10));
      }
   );

   public static void applyEquipment(Monster monster, DifficultyInstance difficulty, boolean useAdvanced) {
      Map<EquipmentSlot, Supplier<Item>> equipmentMap = new HashMap<>();
      equipmentMap.put(EquipmentSlot.HEAD, () -> getRandomItemBasedOnWeight(monster.getRandom(), useAdvanced ? ADVANCED_HELMETS : HELMETS));
      equipmentMap.put(EquipmentSlot.CHEST, () -> getRandomItemBasedOnWeight(monster.getRandom(), useAdvanced ? ADVANCED_CHESTPLATES : HELMETS));
      equipmentMap.put(EquipmentSlot.LEGS, () -> getRandomItemBasedOnWeight(monster.getRandom(), useAdvanced ? ADVANCED_LEGGINGS : HELMETS));
      equipmentMap.put(EquipmentSlot.FEET, () -> getRandomItemBasedOnWeight(monster.getRandom(), useAdvanced ? ADVANCED_BOOTS : BOOTS));
      equipmentMap.forEach((equipmentSlot, itemSupplier) -> {
         if (!monster.hasItemInSlot(equipmentSlot)) {
            ItemStack equipmentStack = new ItemStack((ItemLike)itemSupplier.get());
            int enchantmentLevel = (int)(5.0F + difficulty.getSpecialMultiplier() * (float)monster.getRandom().nextInt(40));
            EnchantmentHelper.enchantItem(monster.getRandom(), equipmentStack, enchantmentLevel, false);
            monster.setItemSlot(equipmentSlot, equipmentStack);
         }
      });
   }

   private static Item getRandomItemBasedOnWeight(RandomSource random, List<EquipmentHelper.EquipmentType> equipment) {
      int rarityWeight = equipment.stream().mapToInt(EquipmentHelper.EquipmentType::getRarity).sum();
      int randomWeight = random.nextInt(rarityWeight);
      Item selectedItem = null;
      int cumWeight = 0;

      for (EquipmentHelper.EquipmentType type : equipment) {
         cumWeight += type.getRarity();
         if (randomWeight < cumWeight) {
            selectedItem = type.getItem();
            break;
         }
      }

      return Objects.requireNonNull(selectedItem, "Could not get an item! Is the equipment list empty?");
   }

   private static class EquipmentType {
      private final Item item;
      private final int rarity;

      public EquipmentType(Item item, int rarity) {
         this.item = item;
         this.rarity = rarity;
      }

      public Item getItem() {
         return this.item;
      }

      public int getRarity() {
         return this.rarity;
      }
   }
}
