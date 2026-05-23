package nonamecrackers2.witherstormmod.common.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeTypes;
import org.jetbrains.annotations.Nullable;

public class ResummonSuperBeaconRecipe extends SuperBeaconRecipe {
   private final EntityType<?> entity;
   private final CompoundTag nbt;
   @Nullable
   public LivingEntity toRender;

   public ResummonSuperBeaconRecipe(
      ResourceLocation id, NonNullList<Ingredient> ingredients, EntityType<?> entity, CompoundTag nbt, SuperBeaconRecipe.Condition condition
   ) {
      super(id, ingredients, condition);
      this.entity = entity;
      this.nbt = nbt;
   }

   public ItemStack getResultItem(RegistryAccess access) {
      return ItemStack.EMPTY;
   }

   public RecipeType<?> getType() {
      return (RecipeType<?>)WitherStormModRecipeTypes.SUPER_BEACON_RESUMMON.get();
   }

   @Override
   public boolean isResummonEntity() {
      return true;
   }

   @Override
   public EntityType<?> getResummonEntity() {
      return this.entity;
   }

   @Override
   public CompoundTag getResummonEntityNBT() {
      return this.nbt;
   }

   public RecipeSerializer<?> getSerializer() {
      return (RecipeSerializer<?>)WitherStormModRecipeSerializers.RESUMMON_SUPER_BEACON.get();
   }

   public static class Serializer implements RecipeSerializer<ResummonSuperBeaconRecipe> {
      public ResummonSuperBeaconRecipe fromJson(ResourceLocation id, JsonObject object) {
         JsonArray array = GsonHelper.getAsJsonArray(object, "ingredients");
         NonNullList<Ingredient> ingredients = NonNullList.create();

         for (int i = 0; i < array.size(); i++) {
            ingredients.add(Ingredient.fromJson(array.get(i)));
         }

         String rawEntityId = GsonHelper.getAsString(object, "entity");
         ResourceLocation entityId = new ResourceLocation(rawEntityId);
         EntityType<?> type = (EntityType<?>)NeoForgeRegistries.ENTITY_TYPES.getValue(entityId);
         if (type == null) {
            throw new JsonSyntaxException("Unknown entity of id '" + rawEntityId + "'");
         } else {
            CompoundTag tag = new CompoundTag();
            if (object.has("nbt")) {
               try {
                  tag = TagParser.parseTag(GsonHelper.getAsString(object, "nbt"));
               } catch (CommandSyntaxException var10) {
                  throw new JsonSyntaxException("Invalid nbt tag: " + var10.getMessage());
               }
            }

            return new ResummonSuperBeaconRecipe(id, ingredients, type, tag, SuperBeaconRecipe.Condition.fromJson(object, "condition"));
         }
      }

      @Nullable
      public ResummonSuperBeaconRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
         NonNullList<Ingredient> ingredients = (NonNullList<Ingredient>)buffer.readCollection(NonNullList::createWithCapacity, b -> Ingredient.fromNetwork(b));
         EntityType<?> type = (EntityType<?>)buffer.readRegistryId();
         CompoundTag tag = buffer.readNbt();
         SuperBeaconRecipe.Condition condition = (SuperBeaconRecipe.Condition)buffer.readEnum(SuperBeaconRecipe.Condition.class);
         return new ResummonSuperBeaconRecipe(id, ingredients, type, tag, condition);
      }

      public void toNetwork(FriendlyByteBuf buffer, ResummonSuperBeaconRecipe recipe) {
         buffer.writeCollection(recipe.ingredients, (b, i) -> i.toNetwork(b));
         buffer.writeRegistryId(NeoForgeRegistries.ENTITY_TYPES, recipe.entity);
         buffer.writeNbt(recipe.nbt);
         buffer.writeEnum(recipe.getCondition());
      }
   }
}
