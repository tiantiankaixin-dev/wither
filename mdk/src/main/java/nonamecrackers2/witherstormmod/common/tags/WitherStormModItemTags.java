package nonamecrackers2.witherstormmod.common.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class WitherStormModItemTags {
   public static final TagKey<Item> CURE_INGREDIENT = TagKey.create(Registries.ITEM, new ResourceLocation("witherstormmod", "cure_ingredient"));
   public static final TagKey<Item> CURE_BASE = TagKey.create(Registries.ITEM, new ResourceLocation("witherstormmod", "cure_base"));
   public static final TagKey<Item> UNAPPETIZING = TagKey.create(Registries.ITEM, new ResourceLocation("witherstormmod", "unappetizing"));
   public static final TagKey<Item> COMMAND_BLOCK_TOOLS = TagKey.create(Registries.ITEM, new ResourceLocation("witherstormmod", "command_block_tools"));
   public static final TagKey<Item> CANNOT_FALL_IN_VOID = TagKey.create(Registries.ITEM, new ResourceLocation("witherstormmod", "cannot_fall_in_void"));
   public static final TagKey<Item> TAINTED_LOGS = TagKey.create(Registries.ITEM, new ResourceLocation("witherstormmod", "tainted_logs"));
   public static final TagKey<Item> JUNK = TagKey.create(Registries.ITEM, new ResourceLocation("witherstormmod", "junk"));
}
