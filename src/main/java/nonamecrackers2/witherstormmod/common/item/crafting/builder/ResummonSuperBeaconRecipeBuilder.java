package nonamecrackers2.witherstormmod.common.item.crafting.builder;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.common.item.crafting.ResummonSuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.SuperBeaconRecipe;

public class ResummonSuperBeaconRecipeBuilder extends SuperBeaconRecipeBuilder {
   private final EntityType<?> entity;
   private final CompoundTag nbt;

   public ResummonSuperBeaconRecipeBuilder(SuperBeaconRecipe.Condition condition, EntityType<?> entity, CompoundTag tag) {
      super(condition);
      this.entity = entity;
      this.nbt = tag;
   }

   @Override
   public Item getResult() {
      return Items.AIR;
   }

   @Override
   public void save(RecipeOutput output, ResourceLocation id) {
      output.accept(id, new ResummonSuperBeaconRecipe(id, this.copyIngredients(), this.entity, this.nbt.copy(), this.condition), null);
   }

   @Override
   public void save(RecipeOutput output) {
      this.save(output, defaultRecipeId(this.entity));
   }

   @Override
   public void save(RecipeOutput output, String string) {
      ResourceLocation id = defaultRecipeId(this.entity);
      ResourceLocation newId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), string);
      if (id.equals(newId)) {
         throw new IllegalStateException("Recipe " + string + " should remove its 'save' argument as it is equal to the default one");
      }

      this.save(output, newId);
   }

   private static ResourceLocation defaultRecipeId(EntityType<?> type) {
      ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(type);
      return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "summon_" + id.getPath());
   }
}
