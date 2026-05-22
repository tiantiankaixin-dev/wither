package nonamecrackers2.witherstormmod.common.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeTypes;
import org.jetbrains.annotations.Nullable;

public class AnvilRecipe implements Recipe<AnvilRecipe.AnvilContents> {
   private final ResourceLocation id;
   private final Ingredient left;
   private final Ingredient right;
   private final ItemStack result;
   private final int cost;

   public AnvilRecipe(ResourceLocation id, Ingredient left, Ingredient right, ItemStack result, int cost) {
      this.id = id;
      this.left = left;
      this.right = right;
      this.result = result;
      this.cost = cost;
   }

   public boolean matches(AnvilRecipe.AnvilContents contents, Level level) {
      return this.left.test(contents.left) && this.right.test(contents.right);
   }

   public ItemStack assemble(AnvilRecipe.AnvilContents contents, RegistryAccess access) {
      return this.getResultItem(access).copy();
   }

   public boolean canCraftInDimensions(int width, int height) {
      return width == 1 && width == 2;
   }

   public ResourceLocation getId() {
      return this.id;
   }

   public ItemStack getResultItem(RegistryAccess access) {
      return this.result;
   }

   public RecipeSerializer<?> getSerializer() {
      return (RecipeSerializer<?>)WitherStormModRecipeSerializers.ANVIL_RECIPE.get();
   }

   public RecipeType<?> getType() {
      return (RecipeType<?>)WitherStormModRecipeTypes.ANVIL.get();
   }

   public int getCost() {
      return this.cost;
   }

   public Ingredient getLeft() {
      return this.left;
   }

   public Ingredient getRight() {
      return this.right;
   }

   public ItemStack getOutputRaw() {
      return this.result;
   }

   public static class AnvilContents implements Container {
      private final ItemStack left;
      private final ItemStack right;

      public AnvilContents(ItemStack left, ItemStack right) {
         this.left = left;
         this.right = right;
      }

      public void clearContent() {
      }

      public int getContainerSize() {
         return 2;
      }

      public boolean isEmpty() {
         return this.left.isEmpty() && this.right.isEmpty();
      }

      public ItemStack getItem(int slot) {
         switch (slot) {
            case 0:
               return this.left;
            case 1:
               return this.right;
            default:
               return ItemStack.EMPTY;
         }
      }

      public ItemStack removeItem(int slot, int amount) {
         return ItemStack.EMPTY;
      }

      public ItemStack removeItemNoUpdate(int slot) {
         return ItemStack.EMPTY;
      }

      public void setItem(int slot, ItemStack stack) {
      }

      public void setChanged() {
      }

      public boolean stillValid(Player player) {
         return false;
      }
   }

   public static class Serializer implements RecipeSerializer<AnvilRecipe> {
      public AnvilRecipe fromJson(ResourceLocation id, JsonObject object) {
         Ingredient left = Ingredient.fromJson(object.get("left"));
         Ingredient right = Ingredient.fromJson(object.get("right"));
         ItemStack stack;
         if (object.get("result").isJsonObject()) {
            stack = ShapedRecipe.itemStackFromJson(object.get("result").getAsJsonObject());
         } else {
            String rawId = GsonHelper.getAsString(object, "result");
            ResourceLocation itemId = new ResourceLocation(rawId);
            Item item = (Item)ForgeRegistries.ITEMS.getValue(itemId);
            if (item == null) {
               throw new JsonSyntaxException("Unknown item '" + rawId + "'");
            }

            stack = new ItemStack(item);
         }

         int cost = GsonHelper.getAsInt(object, "cost");
         return new AnvilRecipe(id, left, right, stack, cost);
      }

      @Nullable
      public AnvilRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
         Ingredient left = Ingredient.fromNetwork(buffer);
         Ingredient right = Ingredient.fromNetwork(buffer);
         ItemStack item = buffer.readItem();
         int cost = buffer.readVarInt();
         return new AnvilRecipe(id, left, right, item, cost);
      }

      public void toNetwork(FriendlyByteBuf buffer, AnvilRecipe recipe) {
         recipe.left.toNetwork(buffer);
         recipe.right.toNetwork(buffer);
         buffer.writeItem(recipe.result);
         buffer.writeVarInt(recipe.cost);
      }
   }
}
