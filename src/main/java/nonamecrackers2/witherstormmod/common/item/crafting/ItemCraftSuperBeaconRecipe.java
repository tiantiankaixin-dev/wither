package nonamecrackers2.witherstormmod.common.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeTypes;

public class ItemCraftSuperBeaconRecipe extends SuperBeaconRecipe {
   private final ItemStack result;

   public ItemCraftSuperBeaconRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result, SuperBeaconRecipe.Condition condition) {
      super(id, ingredients, condition);
      this.result = result;
   }

   public ItemStack getResultItem(HolderLookup.Provider access) {
      return this.result;
   }

   public RecipeType<?> getType() {
      return WitherStormModRecipeTypes.SUPER_BEACON_ITEM.get();
   }

   @Override
   public boolean isResummonEntity() {
      return false;
   }

   public RecipeSerializer<?> getSerializer() {
      return WitherStormModRecipeSerializers.ITEM_CRAFT_SUPER_BEACON.get();
   }

   private static ResourceLocation defaultId(ItemStack result) {
      ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(result.getItem());
      return itemId != null
         ? ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), "super_beacon/" + itemId.getPath())
         : ResourceLocation.fromNamespaceAndPath("witherstormmod", "super_beacon/unknown");
   }

   public static class Serializer implements RecipeSerializer<ItemCraftSuperBeaconRecipe> {
      private static final MapCodec<ItemCraftSuperBeaconRecipe> CODEC = RecordCodecBuilder.mapCodec(
         instance -> instance.group(
               Ingredient.CODEC_NONEMPTY
                  .listOf()
                  .xmap(SuperBeaconRecipe::toNonNullList, SuperBeaconRecipe::asList)
                  .fieldOf("ingredients")
                  .forGetter(SuperBeaconRecipe::getIngredients),
               RecipeCodecs.ITEM_STACK_RESULT.fieldOf("result").forGetter(recipe -> recipe.result),
               SuperBeaconRecipe.CONDITION_CODEC.optionalFieldOf("condition", SuperBeaconRecipe.Condition.NONE).forGetter(SuperBeaconRecipe::getCondition)
            )
            .apply(
               instance,
               (ingredients, result, condition) -> new ItemCraftSuperBeaconRecipe(ItemCraftSuperBeaconRecipe.defaultId(result), ingredients, result, condition)
            )
      );
      private static final StreamCodec<RegistryFriendlyByteBuf, ItemCraftSuperBeaconRecipe> STREAM_CODEC = StreamCodec.of(
         Serializer::toNetwork, Serializer::fromNetwork
      );

      @Override
      public MapCodec<ItemCraftSuperBeaconRecipe> codec() {
         return CODEC;
      }

      @Override
      public StreamCodec<RegistryFriendlyByteBuf, ItemCraftSuperBeaconRecipe> streamCodec() {
         return STREAM_CODEC;
      }

      private static ItemCraftSuperBeaconRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
         ResourceLocation id = buffer.readResourceLocation();
         NonNullList<Ingredient> ingredients = NonNullList.create();
         int size = buffer.readVarInt();

         for (int i = 0; i < size; i++) {
            ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
         }

         ItemStack item = ItemStack.STREAM_CODEC.decode(buffer);
         SuperBeaconRecipe.Condition condition = buffer.readEnum(SuperBeaconRecipe.Condition.class);
         return new ItemCraftSuperBeaconRecipe(id, ingredients, item, condition);
      }

      private static void toNetwork(RegistryFriendlyByteBuf buffer, ItemCraftSuperBeaconRecipe recipe) {
         buffer.writeResourceLocation(recipe.getId());
         buffer.writeVarInt(recipe.getIngredients().size());

         for (Ingredient ingredient : recipe.getIngredients()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
         }

         ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
         buffer.writeEnum(recipe.getCondition());
      }
   }
}
