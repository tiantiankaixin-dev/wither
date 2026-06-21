package nonamecrackers2.witherstormmod.common.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeTypes;

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

   public ItemStack assemble(AnvilRecipe.AnvilContents contents, HolderLookup.Provider access) {
      return this.getResultItem(access).copy();
   }

   public boolean canCraftInDimensions(int width, int height) {
      return width * height >= 2;
   }

   public ResourceLocation getId() {
      return this.id;
   }

   public ItemStack getResultItem(HolderLookup.Provider access) {
      return this.result;
   }

   public RecipeSerializer<?> getSerializer() {
      return WitherStormModRecipeSerializers.ANVIL_RECIPE.get();
   }

   public RecipeType<?> getType() {
      return WitherStormModRecipeTypes.ANVIL.get();
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

   private static ResourceLocation defaultId(ItemStack result) {
      ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(result.getItem());
      return itemId != null
         ? ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), "anvil/" + itemId.getPath())
         : ResourceLocation.fromNamespaceAndPath("witherstormmod", "anvil/unknown");
   }

   public static class AnvilContents implements Container, RecipeInput {
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

      public int size() {
         return this.getContainerSize();
      }

      public boolean isEmpty() {
         return this.left.isEmpty() && this.right.isEmpty();
      }

      public ItemStack getItem(int slot) {
         return switch (slot) {
            case 0 -> this.left;
            case 1 -> this.right;
            default -> ItemStack.EMPTY;
         };
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
      private static final MapCodec<AnvilRecipe> CODEC = RecordCodecBuilder.mapCodec(
         instance -> instance.group(
               Ingredient.CODEC_NONEMPTY.fieldOf("left").forGetter(recipe -> recipe.left),
               Ingredient.CODEC_NONEMPTY.fieldOf("right").forGetter(recipe -> recipe.right),
               RecipeCodecs.ITEM_STACK_RESULT.fieldOf("result").forGetter(recipe -> recipe.result),
               com.mojang.serialization.Codec.INT.fieldOf("cost").forGetter(recipe -> recipe.cost)
            )
            .apply(instance, (left, right, result, cost) -> new AnvilRecipe(AnvilRecipe.defaultId(result), left, right, result, cost))
      );
      private static final StreamCodec<RegistryFriendlyByteBuf, AnvilRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

      @Override
      public MapCodec<AnvilRecipe> codec() {
         return CODEC;
      }

      @Override
      public StreamCodec<RegistryFriendlyByteBuf, AnvilRecipe> streamCodec() {
         return STREAM_CODEC;
      }

      private static AnvilRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
         ResourceLocation id = buffer.readResourceLocation();
         Ingredient left = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
         Ingredient right = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
         ItemStack item = ItemStack.STREAM_CODEC.decode(buffer);
         int cost = buffer.readVarInt();
         return new AnvilRecipe(id, left, right, item, cost);
      }

      private static void toNetwork(RegistryFriendlyByteBuf buffer, AnvilRecipe recipe) {
         buffer.writeResourceLocation(recipe.getId());
         Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.left);
         Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.right);
         ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
         buffer.writeVarInt(recipe.cost);
      }
   }
}
