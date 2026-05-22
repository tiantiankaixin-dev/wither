package nonamecrackers2.witherstormmod.common.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class WitherStormModEntityTags {
   public static final TagKey<EntityType<?>> HIGH_IMMUNITY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("witherstormmod", "high_immunity"));
   public static final TagKey<EntityType<?>> WITHER_SICKNESS_IMMUNE = TagKey.create(
      Registries.ENTITY_TYPE, new ResourceLocation("witherstormmod", "wither_sickness_immune")
   );
   public static final TagKey<EntityType<?>> SICKENED_MOBS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("witherstormmod:sickened_mobs"));
   public static final TagKey<EntityType<?>> WITHER_STORM_TARGETING_BLACKLIST = TagKey.create(
      Registries.ENTITY_TYPE, new ResourceLocation("witherstormmod:wither_storm_targeting_blacklist")
   );
   public static final TagKey<EntityType<?>> FAVOURABLE_MOBS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("witherstormmod:favourable_mobs"));
}
