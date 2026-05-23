package nonamecrackers2.witherstormmod.common.util;

import java.util.function.Supplier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;

public enum WitherStormModItemTier implements Tier {
   WOOD_CMD(5, 0, 3.5F, 3.75F, 32, () -> Ingredient.of(ItemTags.PLANKS)),
   STONE_CMD(3, 0, 16.0F, 5.25F, 5, () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS)),
   IRON_CMD(5, 0, 8.0F, 6.5F, 10, () -> Ingredient.of(new ItemLike[]{Items.IRON_INGOT})),
   GOLD_CMD(1, 0, 32.0F, 2.5F, 64, () -> Ingredient.of(new ItemLike[]{Items.GOLD_INGOT})),
   COMMAND_BLOCK(5, 0, 14.0F, 6.0F, 15, () -> Ingredient.of(new ItemLike[]{Items.DIAMOND})),
   EYE_OF_THE_STORM(5, 0, 12.0F, 7.5F, 25, () -> Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.TAINTED_DUST.get()})),
   FORMIDI_BLADE(5, 0, 12.0F, 6.0F, 15, () -> Ingredient.of(new ItemLike[]{(ItemLike)WitherStormModItems.FORMIDIBOMB.get()}));

   private final int level;
   private final int uses;
   private final float speed;
   private final float damage;
   private final int enchantmentValue;
   private final Supplier<Ingredient> repairIngredient;

   private WitherStormModItemTier(int level, int uses, float speed, float damage, int enchantmentValue, Supplier<Ingredient> ingredient) {
      this.level = level;
      this.uses = uses;
      this.speed = speed;
      this.damage = damage;
      this.enchantmentValue = enchantmentValue;
      this.repairIngredient = ingredient;
   }

   public int getUses() {
      return this.uses;
   }

   public float getSpeed() {
      return this.speed;
   }

   public float getAttackDamageBonus() {
      return this.damage;
   }

   public int getLevel() {
      return this.level;
   }

   public int getEnchantmentValue() {
      return this.enchantmentValue;
   }

   public Ingredient getRepairIngredient() {
      return this.repairIngredient.get();
   }
}
