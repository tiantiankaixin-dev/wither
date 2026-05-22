package nonamecrackers2.witherstormmod.common.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import nonamecrackers2.witherstormmod.WitherStormMod;

public class WitherStormModStructureTags {
   public static final TagKey<Structure> WITHER_STORM_DISTRACTABLE = TagKey.create(
      Registries.STRUCTURE, new ResourceLocation("witherstormmod", "wither_storm_distractable")
   );
   public static final TagKey<Structure> STORM_SPAWN_PLATFORMS = TagKey.create(Registries.STRUCTURE, WitherStormMod.id("storm_spawn_platforms"));
}
