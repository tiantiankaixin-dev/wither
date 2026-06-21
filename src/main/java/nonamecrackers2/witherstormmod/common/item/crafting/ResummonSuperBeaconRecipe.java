package nonamecrackers2.witherstormmod.common.item.crafting;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;
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

   public ItemStack getResultItem(HolderLookup.Provider access) {
      return ItemStack.EMPTY;
   }

   public RecipeType<?> getType() {
      return WitherStormModRecipeTypes.SUPER_BEACON_RESUMMON.get();
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
      return WitherStormModRecipeSerializers.RESUMMON_SUPER_BEACON.get();
   }

   private static ResourceLocation defaultRecipeId(EntityType<?> type) {
      ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(type);
      return id != null
         ? ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "summon_" + id.getPath())
         : ResourceLocation.fromNamespaceAndPath("witherstormmod", "summon_unknown");
   }

   public static class Serializer implements RecipeSerializer<ResummonSuperBeaconRecipe> {
      private static final Codec<EntityType<?>> ENTITY_CODEC = ResourceLocation.CODEC.xmap(Serializer::entityFromId, Serializer::entityId);
      private static final Codec<CompoundTag> LEGACY_NBT_CODEC = Codec.STRING.xmap(Serializer::parseTag, CompoundTag::toString);
      private static final StreamCodec<RegistryFriendlyByteBuf, EntityType<?>> ENTITY_STREAM_CODEC = ByteBufCodecs.registry(Registries.ENTITY_TYPE);
      private static final MapCodec<ResummonSuperBeaconRecipe> CODEC = RecordCodecBuilder.mapCodec(
         instance -> instance.group(
               Ingredient.CODEC_NONEMPTY
                  .listOf()
                  .xmap(SuperBeaconRecipe::toNonNullList, SuperBeaconRecipe::asList)
                  .fieldOf("ingredients")
                  .forGetter(SuperBeaconRecipe::getIngredients),
               ENTITY_CODEC.fieldOf("entity").forGetter(recipe -> recipe.entity),
               LEGACY_NBT_CODEC.optionalFieldOf("nbt", new CompoundTag()).forGetter(recipe -> recipe.nbt),
               SuperBeaconRecipe.CONDITION_CODEC.optionalFieldOf("condition", SuperBeaconRecipe.Condition.NONE).forGetter(SuperBeaconRecipe::getCondition)
            )
            .apply(
               instance,
               (ingredients, entity, nbt, condition) -> new ResummonSuperBeaconRecipe(
                     ResummonSuperBeaconRecipe.defaultRecipeId(entity), ingredients, entity, nbt, condition
                  )
            )
      );
      private static final StreamCodec<RegistryFriendlyByteBuf, ResummonSuperBeaconRecipe> STREAM_CODEC = StreamCodec.of(
         Serializer::toNetwork, Serializer::fromNetwork
      );

      @Override
      public MapCodec<ResummonSuperBeaconRecipe> codec() {
         return CODEC;
      }

      @Override
      public StreamCodec<RegistryFriendlyByteBuf, ResummonSuperBeaconRecipe> streamCodec() {
         return STREAM_CODEC;
      }

      private static ResummonSuperBeaconRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
         ResourceLocation id = buffer.readResourceLocation();
         NonNullList<Ingredient> ingredients = NonNullList.create();
         int size = buffer.readVarInt();

         for (int i = 0; i < size; i++) {
            ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
         }

         EntityType<?> type = ENTITY_STREAM_CODEC.decode(buffer);
         CompoundTag tag = ByteBufCodecs.COMPOUND_TAG.decode(buffer);
         SuperBeaconRecipe.Condition condition = buffer.readEnum(SuperBeaconRecipe.Condition.class);
         return new ResummonSuperBeaconRecipe(id, ingredients, type, tag, condition);
      }

      private static void toNetwork(RegistryFriendlyByteBuf buffer, ResummonSuperBeaconRecipe recipe) {
         buffer.writeResourceLocation(recipe.getId());
         buffer.writeVarInt(recipe.getIngredients().size());

         for (Ingredient ingredient : recipe.getIngredients()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
         }

         ENTITY_STREAM_CODEC.encode(buffer, recipe.entity);
         ByteBufCodecs.COMPOUND_TAG.encode(buffer, recipe.nbt);
         buffer.writeEnum(recipe.getCondition());
      }

      private static CompoundTag parseTag(String value) {
         try {
            return TagParser.parseTag(value);
         } catch (CommandSyntaxException exception) {
            throw new IllegalArgumentException("Invalid nbt tag: " + exception.getMessage(), exception);
         }
      }

      private static EntityType<?> entityFromId(ResourceLocation id) {
         EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(id);
         return type != null ? type : EntityType.PIG;
      }

      private static ResourceLocation entityId(EntityType<?> type) {
         ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(type);
         return id != null ? id : ResourceLocation.fromNamespaceAndPath("minecraft", "pig");
      }
   }
}
