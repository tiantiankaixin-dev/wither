/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  net.minecraft.data.recipes.FinishedRecipe
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.neoforged.neoforge.registries.NeoForgeRegistries
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers
 *  nonamecrackers2.witherstormmod.common.item.crafting.SuperBeaconRecipe$Condition
 *  nonamecrackers2.witherstormmod.common.item.crafting.builder.SuperBeaconRecipeBuilder
 *  nonamecrackers2.witherstormmod.common.item.crafting.builder.SuperBeaconRecipeBuilder$Result
 */
package nonamecrackers2.witherstormmod.common.item.crafting.builder;

import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.item.crafting.SuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.builder.SuperBeaconRecipeBuilder;

public class ResummonSuperBeaconRecipeBuilder
extends SuperBeaconRecipeBuilder {
    private final EntityType<?> entity;
    private final CompoundTag nbt;

    public ResummonSuperBeaconRecipeBuilder(SuperBeaconRecipe.Condition condition, EntityType<?> entity, CompoundTag tag) {
        super(condition);
        this.entity = entity;
        this.nbt = tag;
    }

    public Item getResult() {
        return null;
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept((FinishedRecipe)new Result(id, this.condition, this.entity, this.nbt, this.group == null ? "" : this.group, this.ingredients));
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        this.save(consumer, ResummonSuperBeaconRecipeBuilder.defaultRecipeId(this.entity));
    }

    public void save(Consumer<FinishedRecipe> consumer, String string) {
        ResourceLocation newId;
        ResourceLocation id = ResummonSuperBeaconRecipeBuilder.defaultRecipeId(this.entity);
        if (id.equals((newId = new ResourceLocation(id.getNamespace(), string)))) {
            throw new IllegalStateException("Recipe " + string + " should remove its 'save' argument as it is equal to the default one");
        }
        this.save(consumer, newId);
    }

    private static ResourceLocation defaultRecipeId(EntityType<?> type) {
        ResourceLocation id = NeoForgeRegistries.ENTITY_TYPES.getKey(type);
        return new ResourceLocation(id.getNamespace(), "summon_" + id.getPath());
    }

    public static class Result
    extends SuperBeaconRecipeBuilder.Result {
        private final EntityType<?> entity;
        private final CompoundTag nbt;

        public Result(ResourceLocation id, SuperBeaconRecipe.Condition condition, EntityType<?> entity, CompoundTag nbt, String group, List<Ingredient> ingredients) {
            super(id, condition, group, ingredients);
            this.entity = entity;
            this.nbt = nbt;
        }

        public void serializeRecipeData(JsonObject object) {
            super.serializeRecipeData(object);
            object.addProperty("entity", NeoForgeRegistries.ENTITY_TYPES.getKey(this.entity).toString());
            if (!this.nbt.isEmpty()) {
                object.addProperty("nbt", this.nbt.toString());
            }
        }

        public RecipeSerializer<?> getType() {
            return (RecipeSerializer)WitherStormModRecipeSerializers.RESUMMON_SUPER_BEACON.get();
        }
    }
}
