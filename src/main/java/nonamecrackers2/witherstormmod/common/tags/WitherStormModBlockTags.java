package nonamecrackers2.witherstormmod.common.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import nonamecrackers2.witherstormmod.WitherStormMod;

public class WitherStormModBlockTags {
   public static final TagKey<Block> WITHER_STORM_SUMMON_BASE_BLOCKS = TagKey.create(
      Registries.BLOCK, WitherStormMod.id("wither_storm_summon_base_blocks")
   );
   public static final TagKey<Block> WITHER_STORM_SUMMON_COMMAND_BLOCKS = TagKey.create(
      Registries.BLOCK, WitherStormMod.id("wither_storm_summon_command_blocks")
   );
   public static final TagKey<Block> WITHER_STORM_BLOCK_BLACKLIST = TagKey.create(Registries.BLOCK, WitherStormMod.id("wither_storm_block_blacklist"));
   public static final TagKey<Block> CAVE_IN_BLACKLIST = TagKey.create(Registries.BLOCK, WitherStormMod.id("cave_in_blacklist"));
   public static final TagKey<Block> BEACONS = TagKey.create(Registries.BLOCK, WitherStormMod.id("beacons"));
   public static final TagKey<Block> SMALL_CLUSTER_BLACKLIST = TagKey.create(Registries.BLOCK, WitherStormMod.id("wither_storm_small_cluster_blacklist"));
   public static final TagKey<Block> NATURE_CLUSTER_WHITELIST = TagKey.create(
      Registries.BLOCK, WitherStormMod.id("wither_storm_nature_cluster_whitelist")
   );
   public static final TagKey<Block> LESS_FAVORABLE_BLOCKS = TagKey.create(Registries.BLOCK, WitherStormMod.id("less_favorable_blocks"));
   public static final TagKey<Block> LESS_FAVORABLE_BLOCKS_HUNCH = TagKey.create(Registries.BLOCK, WitherStormMod.id("less_favorable_blocks_hunch"));
   public static final TagKey<Block> WITHERED_BEACON_BASE = TagKey.create(Registries.BLOCK, WitherStormMod.id("withered_beacon_base"));
   public static final TagKey<Block> AQUA_SUPPORT_BASE = TagKey.create(Registries.BLOCK, WitherStormMod.id("aqua_support_base"));
   public static final TagKey<Block> GREEN_SUPPORT_BASE = TagKey.create(Registries.BLOCK, WitherStormMod.id("green_support_base"));
   public static final TagKey<Block> GRAY_SUPPORT_BASE = TagKey.create(Registries.BLOCK, WitherStormMod.id("gray_support_base"));
   public static final TagKey<Block> RED_SUPPORT_BASE = TagKey.create(Registries.BLOCK, WitherStormMod.id("red_support_base"));
   public static final TagKey<Block> BLOCK_CLUSTERS_CANNOT_PLACE = TagKey.create(Registries.BLOCK, WitherStormMod.id("block_clusters_cannot_place"));
   public static final TagKey<Block> TRACTOR_BEAM_DISTRACTION_BLOCKS = TagKey.create(
      Registries.BLOCK, WitherStormMod.id("tractor_beam_distraction_blocks")
   );
   public static final TagKey<Block> SICKENED_BEE_CAN_CONVERT = TagKey.create(Registries.BLOCK, WitherStormMod.id("sickened_bee_can_convert"));
   public static final TagKey<Block> TAINTED_BLOCKS = TagKey.create(Registries.BLOCK, WitherStormMod.id("tainted_blocks"));
}
