package nonamecrackers2.witherstormmod.common.item.crafting;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.blockentity.SuperBeaconBlockEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;

public abstract class SuperBeaconRecipe implements Recipe<SuperBeaconBlockEntity> {
   public static final Codec<SuperBeaconRecipe.Condition> CONDITION_CODEC = StringRepresentable.fromEnum(SuperBeaconRecipe.Condition::values);
   protected final ResourceLocation id;
   protected final NonNullList<Ingredient> ingredients;
   protected final SuperBeaconRecipe.Condition condition;

   public SuperBeaconRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, SuperBeaconRecipe.Condition condition) {
      this.id = id;
      this.ingredients = ingredients;
      this.condition = condition;
   }

   public boolean matches(SuperBeaconBlockEntity block, Level level) {
      StackedContents contents = new StackedContents();
      int j = 0;

      for (int i = 0; i < block.getContainerSize(); i++) {
         ItemStack item = block.getItem(i);
         if (!item.isEmpty()) {
            j++;
            contents.accountStack(item, 1);
         }
      }

      return j == this.ingredients.size() && contents.canCraft(this, null);
   }

   public ItemStack assemble(SuperBeaconBlockEntity entity, HolderLookup.Provider access) {
      return !this.isResummonEntity() ? this.getResultItem(access).copy() : ItemStack.EMPTY;
   }

   public boolean canCraftInDimensions(int width, int height) {
      return true;
   }

   public ItemStack getToastSymbol() {
      return new ItemStack((ItemLike)WitherStormModBlocks.SUPER_BEACON.get());
   }

   public abstract boolean isResummonEntity();

   @Nullable
   public EntityType<?> getResummonEntity() {
      return null;
   }

   @Nullable
   public CompoundTag getResummonEntityNBT() {
      return null;
   }

   public NonNullList<Ingredient> getIngredients() {
      return this.ingredients;
   }

   public ResourceLocation getId() {
      return this.id;
   }

   public SuperBeaconRecipe.Condition getCondition() {
      return this.condition;
   }

   static NonNullList<Ingredient> toNonNullList(List<Ingredient> ingredients) {
      NonNullList<Ingredient> list = NonNullList.create();
      list.addAll(ingredients);
      return list;
   }

   static List<Ingredient> asList(NonNullList<Ingredient> ingredients) {
      return List.copyOf(ingredients);
   }

   public static enum Condition implements StringRepresentable {
      NONE("none", null, e -> true),
      MAIN_ACTIVATED("main_activated", "witherstormmod.jei.super_beacon.requiresMainActivated", e -> e.isActive()),
      FULL_SUPPORTS("all_supports", "witherstormmod.jei.super_beacon.requiresAllSupports", e -> e.isActive() && e.getConnected().size() == 4),
      FULLY_COMLETED(
         "fully_completed", "witherstormmod.jei.super_beacon.requiresFullBeacon", e -> e.isActive() && e.getConnected().size() == 4 && e.beaconLevel == 4
      );

      private final String name;
      @Nullable
      private final String description;
      private final Predicate<SuperBeaconBlockEntity> predicate;

      private Condition(String name, @Nullable String description, Predicate<SuperBeaconBlockEntity> predicate) {
         this.name = name;
         this.description = description;
         this.predicate = predicate;
      }

      public String getSerializedName() {
         return this.name;
      }

      public boolean canCraft(SuperBeaconBlockEntity entity) {
         return this.predicate.test(entity);
      }

      public String getDescription() {
         return this.description;
      }

      public static SuperBeaconRecipe.Condition fromJson(JsonObject object, String name) {
         if (object.has(name)) {
            String id = GsonHelper.getAsString(object, name);

            for (SuperBeaconRecipe.Condition value : values()) {
               if (value.name.equals(id)) {
                  return value;
               }
            }
         }

         return NONE;
      }
   }
}
