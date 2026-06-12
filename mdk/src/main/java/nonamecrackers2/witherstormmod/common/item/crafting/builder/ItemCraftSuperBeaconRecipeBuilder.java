/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraft.data.recipes.RecipeOutput
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.neoforge.registries.NeoForgeRegistries
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers
 *  nonamecrackers2.witherstormmod.common.item.crafting.SuperBeaconRecipe$Condition
 *  nonamecrackers2.witherstormmod.common.item.crafting.builder.SuperBeaconRecipeBuilder
 *  nonamecrackers2.witherstormmod.common.item.crafting.builder.SuperBeaconRecipeBuilder$Result
 */
package nonamecrackers2.witherstormmod.common.item.crafting.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.registries.BuiltInRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.item.crafting.SuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.builder.SuperBeaconRecipeBuilder;

public class ItemCraftSuperBeaconRecipeBuilder
extends SuperBeaconRecipeBuilder {
    private final Item result;
    private final int count;

    public ItemCraftSuperBeaconRecipeBuilder(SuperBeaconRecipe.Condition condition, ItemLike result, int count) {
        super(condition);
        this.result = result.asItem();
        this.count = count;
    }

    public Item getResult() {
        return this.result;
    }

    public void save(RecipeOutput consumer, ResourceLocation id) {
        consumer.accept((RecipeOutput)new Result(id, this.condition, this.result, this.count, this.group == null ? "" : this.group, this.ingredients));
    }

    public static class Result
    extends SuperBeaconRecipeBuilder.Result {
        private final Item result;
        private final int count;

        public Result(ResourceLocation id, SuperBeaconRecipe.Condition condition, Item result, int count, String group, List<Ingredient> ingredients) {
            super(id, condition, group, ingredients);
            this.result = result;
            this.count = count;
        }

        public void serializeRecipeData(JsonObject object) {
            super.serializeRecipeData(object);
            JsonObject result = new JsonObject();
            result.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                result.addProperty("count", (Number)this.count);
            }
            object.add("result", (JsonElement)result);
        }

        public RecipeSerializer<?> getType() {
            return (RecipeSerializer)WitherStormModRecipeSerializers.ITEM_CRAFT_SUPER_BEACON.get();
        }
    }
}
