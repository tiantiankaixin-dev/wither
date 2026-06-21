package nonamecrackers2.witherstormmod.common.item.crafting;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

final class RecipeCodecs {
   static final Codec<ItemStack> ITEM_STACK_RESULT = Codec.either(
         ResourceLocation.CODEC.xmap(id -> makeStack(id, 1), RecipeCodecs::itemKey),
         RecordCodecBuilder.<ItemStack>create(
            instance -> instance.group(
                  ResourceLocation.CODEC.fieldOf("item").forGetter(RecipeCodecs::itemKey),
                  Codec.INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount)
               )
               .apply(instance, RecipeCodecs::makeStack)
         )
      )
      .xmap(either -> either.map(stack -> stack, stack -> stack), stack -> Either.right(stack));

   private RecipeCodecs() {
   }

   private static ItemStack makeStack(ResourceLocation id, int count) {
      Item item = ForgeRegistries.ITEMS.getValue(id);
      return new ItemStack(item != null ? item : Items.AIR, count);
   }

   private static ResourceLocation itemKey(ItemStack stack) {
      ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
      return id != null ? id : ResourceLocation.fromNamespaceAndPath("minecraft", "air");
   }
}
