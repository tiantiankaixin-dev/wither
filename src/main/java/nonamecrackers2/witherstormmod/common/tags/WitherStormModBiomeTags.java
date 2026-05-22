package nonamecrackers2.witherstormmod.common.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class WitherStormModBiomeTags {
   public static final TagKey<Biome> HAS_DESERT_STORM_SPAWN_PLATFORM = TagKey.create(
      Registries.BIOME, new ResourceLocation("witherstormmod", "has_structure/desert_storm_spawn_platform")
   );
   public static final TagKey<Biome> HAS_JUNGLE_STORM_SPAWN_PLATFORM = TagKey.create(
      Registries.BIOME, new ResourceLocation("witherstormmod", "has_structure/jungle_storm_spawn_platform")
   );
   public static final TagKey<Biome> HAS_SAVANNA_STORM_SPAWN_PLATFORM = TagKey.create(
      Registries.BIOME, new ResourceLocation("witherstormmod", "has_structure/savanna_storm_spawn_platform")
   );
   public static final TagKey<Biome> HAS_SNOWY_STORM_SPAWN_PLATFORM = TagKey.create(
      Registries.BIOME, new ResourceLocation("witherstormmod", "has_structure/snowy_storm_spawn_platform")
   );
   public static final TagKey<Biome> HAS_TAIGA_STORM_SPAWN_PLATFORM = TagKey.create(
      Registries.BIOME, new ResourceLocation("witherstormmod", "has_structure/taiga_storm_spawn_platform")
   );
   public static final TagKey<Biome> HAS_RUINS_STORM_SPAWN_PLATFORM = TagKey.create(
      Registries.BIOME, new ResourceLocation("witherstormmod", "has_structure/ruins_storm_spawn_platform")
   );
   public static final TagKey<Biome> HAS_ORDER_TEMPLE_STORM_SPAWN_PLATFORM = TagKey.create(
      Registries.BIOME, new ResourceLocation("witherstormmod", "has_structure/order_temple_storm_spawn_platform")
   );
   public static final TagKey<Biome> HAS_FOREST_STORM_SPAWN_PLATFORM = TagKey.create(
      Registries.BIOME, new ResourceLocation("witherstormmod", "has_structure/forest_storm_spawn_platform")
   );
}
