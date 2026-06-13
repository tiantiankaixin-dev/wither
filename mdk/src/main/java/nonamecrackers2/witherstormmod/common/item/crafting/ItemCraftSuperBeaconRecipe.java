package nonamecrackers2.witherstormmod.common.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeTypes;
import org.jetbrains.annotations.Nullable;

public class ItemCraftSuperBeaconRecipe extends SuperBeaconRecipe {
   private final ItemStack result;

   public ItemCraftSuperBeaconRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result, SuperBeaconRecipe.Condition condition) {
      super(id, ingredients, condition);
      this.result = result;
   }

   public ItemStack getResultItem(RegistryAccess access) {
      return this.result;
   }

   public RecipeType<?> getType() {
      return (RecipeType<?>)WitherStormModRecipeTypes.SUPER_BEACON_ITEM.get();
   }

   @Override
   public boolean isResummonEntity() {
      return false;
   }

   public RecipeSerializer<?> getSerializer() {
      return (RecipeSerializer<?>)WitherStormModRecipeSerializers.ITEM_CRAFT_SUPER_BEACON.get();
   }

   public static class Serializer implements RecipeSerializer<ItemCraftSuperBeaconRecipe> {
      public ItemCraftSuperBeaconRecipe fromJson(ResourceLocation id, JsonObject object) {
         JsonArray array = GsonHelper.getAsJsonArray(object, "ingredients");
         NonNullList<Ingredient> ingredients = NonNullList.create();

         for (int i = 0; i < array.size(); i++) {
            ingredients.add(Ingredient.fromJson(array.get(i)));
         }

         if (!object.has("result")) {
            throw new JsonSyntaxException("Missing result, expected to find a string or object");
         } else {
            ItemStack stack;
            if (object.get("result").isJsonObject()) {
               stack = ShapedRecipe.itemStackFromJson(object.get("result").getAsJsonObject());
            } else {
               String rawId = GsonHelper.getAsString(object, "result");
               ResourceLocation itemId = ResourceLocation.parse(rawId);
               Item item = (Item)BuiltInRegistries.ITEM.get(itemId);
               if (item == null) {
                  throw new JsonSyntaxException("Unknown item '" + rawId + "'");
               }

               stack = new ItemStack(item);
            }

            return new ItemCraftSuperBeaconRecipe(id, ingredients, stack, SuperBeaconRecipe.Condition.fromJson(object, "condition"));
         }
      }

      @Nullable
      public ItemCraftSuperBeaconRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
         NonNullList<Ingredient> ingredients = (NonNullList<Ingredient>)buffer.readCollection(NonNullList::createWithCapacity, b -> Ingredient.fromNetwork(b));
         ItemStack item = buffer.readItem();
         SuperBeaconRecipe.Condition condition = (SuperBeaconRecipe.Condition)buffer.readEnum(SuperBeaconRecipe.Condition.class);
         return new ItemCraftSuperBeaconRecipe(id, ingredients, item, condition);
      }

      public void toNetwork(FriendlyByteBuf buffer, ItemCraftSuperBeaconRecipe recipe) {
         buffer.writeCollection(recipe.ingredients, (b, i) -> i.toNetwork(b));
         buffer.writeItem(recipe.result);
         buffer.writeEnum(recipe.getCondition());
      }
   }
}
