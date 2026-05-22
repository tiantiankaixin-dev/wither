package nonamecrackers2.witherstormmod.common.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import nonamecrackers2.crackerslib.common.util.data.ConfigLangGeneratorHelper;
import nonamecrackers2.crackerslib.common.util.data.ConfigLangGeneratorHelper.Info;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;

public class WitherStormModLangProvider extends LanguageProvider {
   public WitherStormModLangProvider(PackOutput output) {
      super(output, "witherstormmod", "en_us");
   }

   protected void addTranslations() {
      this.add("entity.witherstormmod.wither_storm", "Wither Storm");
      this.add("entity.witherstormmod.block_cluster", "Block Cluster");
      this.add("entity.witherstormmod.wither_storm_segment", "Wither Storm Segment");
      this.add("entity.witherstormmod.flaming_wither_skull", "Flaming Wither Skull");
      this.add("entity.witherstormmod.blue_flaming_wither_skull", "Blue Flaming Wither Skull");
      this.add("entity.witherstormmod.sickened_zombie", "Sickened Zombie");
      this.add("entity.witherstormmod.sickened_skeleton", "Sickened Skeleton");
      this.add("entity.witherstormmod.sickened_spider", "Sickened Spider");
      this.add("entity.witherstormmod.sickened_creeper", "Sickened Creeper");
      this.add("entity.witherstormmod.sickened_villager", "Sickened Villager");
      this.add("entity.witherstormmod.sickened_phantom", "Sickened Phantom");
      this.add("entity.witherstormmod.sickened_chicken", "Sickened Chicken");
      this.add("entity.witherstormmod.sickened_parrot", "Sickened Parrot");
      this.add("entity.witherstormmod.sickened_wolf", "Sickened Wolf");
      this.add("entity.witherstormmod.sickened_cat", "Sickened Cat");
      this.add("entity.witherstormmod.sickened_cow", "Sickened Cow");
      this.add("entity.witherstormmod.sickened_mushroom_cow", "Sickened Mooshroom Cow");
      this.add("entity.witherstormmod.sickened_bee", "Sickened Bee");
      this.add("entity.witherstormmod.sickened_pig", "Sickened Pig");
      this.add("entity.witherstormmod.sickened_pillager", "Sickened Pillager");
      this.add("entity.witherstormmod.sickened_vindicator", "Sickened Vindicator");
      this.add("entity.witherstormmod.sickened_iron_golem", "Sickened Iron Golem");
      this.add("entity.witherstormmod.sickened_snow_golem", "Sickened Snow Golem");
      this.add("entity.witherstormmod.super_tnt", "Super TNT");
      this.add("entity.witherstormmod.formidibomb", "Formidibomb");
      this.add("entity.witherstormmod.command_block", "Command Block");
      this.add("entity.witherstormmod.withered_symbiont", "Withered Symbiont");
      this.add("entity.witherstormmod.wither_storm_head", "Wither Storm Head");
      this.add("entity.witherstormmod.tentacle", "Tentacle");
      this.add("entity.witherstormmod.tainted_slime", "Tainted Slime");
      this.add("entity.witherstormmod.tentacle_spike", "Tentacle Spike");
      this.add("effect.witherstormmod.wither_sickness", "Wither Sickness");
      this.add(
         "effect.witherstormmod.wither_sickness.description",
         "An evolved variant of wither, lasting seemingly forever while slowly draining its victim's health"
      );
      this.add("item.witherstormmod.golden_apple_stew", "Golden Apple Stew");
      this.add("item.witherstormmod.withered_flesh", "Withered Flesh");
      this.add("item.witherstormmod.withered_bone", "Withered Bone");
      this.add("item.witherstormmod.sickened_zombie_spawn_egg", "Sickened Zombie Spawn Egg");
      this.add("item.witherstormmod.sickened_skeleton_spawn_egg", "Sickened Skeleton Spawn Egg");
      this.add("item.witherstormmod.sickened_spider_spawn_egg", "Sickened Spider Spawn Egg");
      this.add("item.witherstormmod.sickened_creeper_spawn_egg", "Sickened Creeper Spawn Egg");
      this.add("item.witherstormmod.sickened_villager_spawn_egg", "Sickened Villager Spawn Egg");
      this.add("item.witherstormmod.sickened_phantom_spawn_egg", "Sickened Phantom Spawn Egg");
      this.add("item.witherstormmod.sickened_chicken_spawn_egg", "Sickened Chicken Spawn Egg");
      this.add("item.witherstormmod.sickened_parrot_spawn_egg", "Sickened Parrot Spawn Egg");
      this.add("item.witherstormmod.sickened_wolf_spawn_egg", "Sickened Wolf Spawn Egg");
      this.add("item.witherstormmod.sickened_cat_spawn_egg", "Sickened Cat Spawn Egg");
      this.add("item.witherstormmod.sickened_cow_spawn_egg", "Sickened Cow Spawn Egg");
      this.add("item.witherstormmod.sickened_mushroom_cow_spawn_egg", "Sickened Mooshroom Spawn Egg");
      this.add("item.witherstormmod.sickened_bee_spawn_egg", "Sickened Bee Spawn Egg");
      this.add("item.witherstormmod.sickened_pig_spawn_egg", "Sickened Pig Spawn Egg");
      this.add("item.witherstormmod.sickened_pillager_spawn_egg", "Sickened Pillager Spawn Egg");
      this.add("item.witherstormmod.sickened_vindicator_spawn_egg", "Sickened Vindicator Spawn Egg");
      this.add("item.witherstormmod.sickened_iron_golem_spawn_egg", "Sickened Iron Golem Spawn Egg");
      this.add("item.witherstormmod.sickened_snow_golem_spawn_egg", "Sickened Snow Golem Spawn Egg");
      this.add("item.witherstormmod.withered_symbiont_spawn_egg", "Withered Symbiont Spawn Egg");
      this.add("item.witherstormmod.tentacle_spawn_egg", "Tentacle Spawn Egg");
      this.add("item.witherstormmod.command_block_book", "Enchanted Command Block Book");
      this.add("item.witherstormmod.command_block_sword", "Command Block Sword");
      this.add("item.witherstormmod.command_block_pickaxe", "Command Block Pickaxe");
      this.add("item.witherstormmod.command_block_axe", "Command Block Axe");
      this.add("item.witherstormmod.command_block_shovel", "Command Block Shovel");
      this.add("item.witherstormmod.command_block_hoe", "Command Block Hoe");
      this.add("item.witherstormmod.wooden_command_block_sword", "Wooden Command Block Sword");
      this.add("item.witherstormmod.wooden_command_block_pickaxe", "Wooden Command Block Pickaxe");
      this.add("item.witherstormmod.wooden_command_block_axe", "Wooden Command Block Axe");
      this.add("item.witherstormmod.wooden_command_block_shovel", "Wooden Command Block Shovel");
      this.add("item.witherstormmod.wooden_command_block_hoe", "Wooden Command Block Hoe");
      this.add("item.witherstormmod.stone_command_block_sword", "Stone Command Block Sword");
      this.add("item.witherstormmod.stone_command_block_pickaxe", "Stone Command Block Pickaxe");
      this.add("item.witherstormmod.stone_command_block_axe", "Stone Command Block Axe");
      this.add("item.witherstormmod.stone_command_block_shovel", "Stone Command Block Shovel");
      this.add("item.witherstormmod.stone_command_block_hoe", "Stone Command Block Hoe");
      this.add("item.witherstormmod.iron_command_block_sword", "Iron Command Block Sword");
      this.add("item.witherstormmod.iron_command_block_pickaxe", "Iron Command Block Pickaxe");
      this.add("item.witherstormmod.iron_command_block_axe", "Iron Command Block Axe");
      this.add("item.witherstormmod.iron_command_block_shovel", "Iron Command Block Shovel");
      this.add("item.witherstormmod.iron_command_block_hoe", "Iron Command Block Hoe");
      this.add("item.witherstormmod.gold_command_block_sword", "Golden Command Block Sword");
      this.add("item.witherstormmod.gold_command_block_pickaxe", "Golden Command Block Pickaxe");
      this.add("item.witherstormmod.gold_command_block_axe", "Golden Command Block Axe");
      this.add("item.witherstormmod.gold_command_block_shovel", "Golden Command Block Shovel");
      this.add("item.witherstormmod.gold_command_block_hoe", "Golden Command Block Hoe");
      this.add("item.witherstormmod.tainted_dust", "Tainted Dust");
      this.add("item.witherstormmod.withered_nether_star", "Withered Nether Star");
      this.add("item.witherstormmod.amulet", "Amulet");
      this.add("item.witherstormmod.withered_spider_eye", "Withered Spider Eye");
      this.add("item.witherstormmod.phasometer", "Phasometer");
      this.add("item.witherstormmod.eye_of_the_storm", "Eye of the Storm");
      this.add("item.witherstormmod.eye_of_the_storm.author", "By: Blueskullz");
      this.add("item.witherstormmod.formidi_blade", "The Formidi-Blade");
      this.add("item.witherstormmod.formidi_blade.author", "By: MegaMathew200");
      this.add("item.witherstormmod.formidi_blade.use", "Right-click to charge. Attack to create an explosion.");
      this.add("item.witherstormmod.iron_command_tool.info", "Deals it's damage twice, once as normal damage and then again as piercing damage!");
      this.add("item.minecraft.potion.effect.wither", "Potion of Decay");
      this.add("item.minecraft.potion.effect.long_wither", "Potion of Decay");
      this.add("item.minecraft.potion.effect.strong_wither", "Potion of Decay");
      this.add("item.minecraft.splash_potion.effect.wither", "Splash Potion of Decay");
      this.add("item.minecraft.splash_potion.effect.long_wither", "Splash Potion of Decay");
      this.add("item.minecraft.splash_potion.effect.strong_wither", "Splash Potion of Decay");
      this.add("item.minecraft.lingering_potion.effect.wither", "Lingering Potion of Decay");
      this.add("item.minecraft.lingering_potion.effect.long_wither", "Lingering Potion of Decay");
      this.add("item.minecraft.lingering_potion.effect.strong_wither", "Lingering Potion of Decay");
      this.add("item.minecraft.tipped_arrow.effect.wither", "Arrow of Decay");
      this.add("item.minecraft.tipped_arrow.effect.long_wither", "Arrow of Decay");
      this.add("item.minecraft.tipped_arrow.effect.strong_wither", "Arrow of Decay");
      this.add("itemGroup.wither_storm_mod", "Wither Storm Mod");
      this.add("block.witherstormmod.super_tnt", "Super TNT");
      this.add("block.witherstormmod.formidibomb", "Formidibomb");
      this.add("block.witherstormmod.tainted_flesh_block", "Tainted Flesh Block");
      this.add("block.witherstormmod.infected_flesh_block", "Infected Flesh Block");
      this.add("block.witherstormmod.tainted_dust", "Tainted Dust");
      this.add("block.witherstormmod.tainted_stone", "Tainted Stone");
      this.add("block.witherstormmod.tainted_cobblestone", "Tainted Cobblestone");
      this.add("block.witherstormmod.tainted_sand", "Tainted Sand");
      this.add("block.witherstormmod.tainted_sandstone", "Tainted Sandstone");
      this.add("block.witherstormmod.tainted_sandstone_stairs", "Tainted Sandstone Stairs");
      this.add("block.witherstormmod.tainted_sandstone_slab", "Tainted Sandstone Slab");
      this.add("block.witherstormmod.tainted_sandstone_wall", "Tainted Sandstone Wall");
      this.add("block.witherstormmod.tainted_cut_sandstone", "Tainted Cut Sandstone");
      this.add("block.witherstormmod.tainted_cut_sandstone_slab", "Tainted Cut Sandstone Slab");
      this.add("block.witherstormmod.tainted_chiseled_sandstone", "Tainted Chiseled Sandstone");
      this.add("block.witherstormmod.tainted_smooth_sandstone", "Tainted Smooth Sandstone");
      this.add("block.witherstormmod.tainted_smooth_sandstone_stairs", "Tainted Smooth Sandstone Stairs");
      this.add("block.witherstormmod.tainted_smooth_sandstone_slab", "Tainted Smooth Sandstone Slab");
      this.add("block.witherstormmod.tainted_smooth_sandstone_wall", "Tainted Smooth Sandstone Wall");
      this.add("block.witherstormmod.tainted_torch", "Tainted Torch");
      this.add("block.witherstormmod.tainted_wall_torch", "Tainted Wall Torch");
      this.add("block.witherstormmod.tainted_dirt", "Tainted Dirt");
      this.add("block.witherstormmod.tainted_planks", "Tainted Planks");
      this.add("block.witherstormmod.tainted_mushroom", "Tainted Mushroom");
      this.add("block.witherstormmod.tainted_zombie_sitting", "Tainted Zombie Sitting");
      this.add("block.witherstormmod.tainted_zombie_wall", "Tainted Zombie Wall");
      this.add("block.witherstormmod.tainted_zombie_lying", "Tainted Zombie Lying");
      this.add("block.witherstormmod.tainted_bone_pile", "Tainted Bone Pile");
      this.add("block.witherstormmod.tainted_skull_ceiling", "Tainted Skull Ceiling");
      this.add("block.witherstormmod.tainted_skeleton_wall", "Tainted Skeleton Wall");
      this.add("block.witherstormmod.tainted_glass", "Tainted Glass");
      this.add("block.witherstormmod.tainted_glass_pane", "Tainted Glass Pane");
      this.add("block.witherstormmod.tainted_dust_block", "Tainted Dust Lamp");
      this.add("block.witherstormmod.tainted_leaves", "Tainted Leaves");
      this.add("block.witherstormmod.tainted_stone_stairs", "Tainted Stone Stairs");
      this.add("block.witherstormmod.tainted_stone_slab", "Tainted Stone Slab");
      this.add("block.witherstormmod.tainted_stone_button", "Tainted Stone Button");
      this.add("block.witherstormmod.tainted_stone_pressure_plate", "Tainted Stone Pressure Plate");
      this.add("block.witherstormmod.tainted_cobblestone_stairs", "Tainted Cobblestone Stairs");
      this.add("block.witherstormmod.tainted_cobblestone_slab", "Tainted Cobblestone Slab");
      this.add("block.witherstormmod.tainted_cobblestone_wall", "Tainted Cobblestone Wall");
      this.add("block.witherstormmod.tainted_log", "Tainted Log");
      this.add("block.witherstormmod.tainted_wood", "Tainted Wood");
      this.add("block.witherstormmod.stripped_tainted_log", "Stripped Tainted Log");
      this.add("block.witherstormmod.stripped_tainted_wood", "Stripped Tainted Wood");
      this.add("block.witherstormmod.tainted_stairs", "Tainted Stairs");
      this.add("block.witherstormmod.tainted_slab", "Tainted Slab");
      this.add("block.witherstormmod.tainted_fence", "Tainted Fence");
      this.add("block.witherstormmod.tainted_fence_gate", "Tainted Fence Gate");
      this.add("block.witherstormmod.tainted_door", "Tainted Door");
      this.add("block.witherstormmod.tainted_trapdoor", "Tainted Trapdoor");
      this.add("block.witherstormmod.tainted_button", "Tainted Button");
      this.add("block.witherstormmod.tainted_pressure_plate", "Tainted Pressure Plate");
      this.add("block.witherstormmod.hardened_flesh_block", "Hardened Flesh Block");
      this.add("block.witherstormmod.super_beacon", "Withered Beacon");
      this.add("block.witherstormmod.super_support_beacon", "Withered Support Beacon");
      this.add("block.witherstormmod.firework_bundle", "Firework Bundle");
      this.add("block.witherstormmod.potted_tainted_mushroom", "Potted Tainted Mushroom");
      this.add("block.witherstormmod.tainted_pumpkin", "Tainted Pumpkin");
      this.add("block.witherstormmod.tainted_carved_pumpkin", "Tainted Carved Pumpkin");
      this.add("block.witherstormmod.tainted_jack_o_lantern", "Tainted Jack o'Lantern");
      this.add("block.witherstormmod.tainted_sign", "Tainted Sign");
      this.add("block.witherstormmod.tainted_wall_sign", "Tainted Wall Sign");
      this.add("block.witherstormmod.tainted_flesh_veins", "Tainted Flesh Veins");
      this.add("biome.witherstormmod.bowels", "Bowels");
      this.add("description.formidibomb.fuse", "%s seconds till detonation!");
      this.add("description.amulet.mainUse", "Forces the Wither Storm to track the nearest player with an amulet");
      this.add("description.amulet.trackingDesc", "Tracks bound entities. Blue by default tracks the nearest Wither Storm");
      this.add("description.amulet.swap", "Shift+Right-Click to swap colors");
      this.add("description.amulet.bind", "Right-Click an entity to bind them to the selected color");
      this.add("description.amulet.tracking", "%s: %s");
      this.add("description.amulet.locked", "Locked");
      this.add("description.amulet.tracksEntityTypes", "Tracks nearest entity of bounded type");
      this.add("description.phasometer.searching", "Searching%s");
      this.add("description.phasometer.phase", "Phase: %s");
      this.add("description.phasometer.formidibombable", "Is Formidibombable!");
      this.add("description.phasometer.bowelsAccessible", "Bowels Accessible!");
      this.add("description.phasometer.distracted", "Is Distracted!");
      this.add("description.phasometer.chasing", "Is Chasing!");
      this.add("description.phasometer.obstructed", "Obstructed!");
      this.add("description.phasometer.ultimateTarget", "Ultimate Target: %s");
      this.add("description.phasometer.ultimateTargetDirection", "Is Going: %s");
      this.add("description.phasometer.phaseProgress", "Phase Progress: %s");
      this.add(
         "description.phasometer.use", "Acts like a normal spyglass. Looking at the Wither Storm through it will show the phase and other useful information"
      );
      this.add("description.phasometer.use.upgraded", "Shows the Wither Storm's ultimate target, its travel direction, and its phase progress");
      this.add(
         "description.withered_phlegm.use",
         "A sticky block regurgitated by the Wither Storm containing some items it thought unsavory...\nStores players items when they get chomped by the Wither Storm\nActs as a vacuum hopper. Power with a lever to disable"
      );
      this.add("container.witherstormmod.withered_beacon", "Withered Beacon");
      this.add("container.witherstormmod.withered_beacon.selected", "Selected Effect");
      this.add("container.witherstormmod.withered_beacon.level", "Level: %s");
      this.add("container.witherstormmod.withered_beacon.available_effects", "Effects");
      this.add("container.witherstormmod.withered_support_beacon", "Withered Support Beacon");
      this.add("container.witherstormmod.phlegm_block", "Withered Phlegm Block");
      this.add("stat.witherstormmod.interact_with_super_beacon", "Interactions with Withered Beacon");
      this.add("painting.witherstormmod.amulet.title", "Amulet");
      this.add("painting.witherstormmod.amulet.author", "From MC:SM");
      this.add("witherstormmod.subtitle.wither_storm_roar", "Wither Storm roars");
      this.add("witherstormmod.subtitle.wither_storm_evolves", "Wither Storm evolves");
      this.add("witherstormmod.subtitle.wither_storm_splits", "Wither Storm splits");
      this.add("witherstormmod.subtitle.wither_storm_reactivates", "Wither Storm reactivates");
      this.add("witherstormmod.subtitle.wither_storm_loop", "Wither Storm ambience");
      this.add("witherstormmod.subtitle.wither_storm_ambient", "Wither Storm growls");
      this.add("witherstormmod.subtitle.wither_storm_boss_theme", "Wither Storm Theme plays");
      this.add("witherstormmod.subtitle.wither_storm_death", "Wither Storm dies");
      this.add("witherstormmod.subtitle.wither_storm_hurt", "Wither Storm screams");
      this.add("witherstormmod.subtitle.wither_storm_bite", "Wither Storm chomps");
      this.add("witherstormmod.subtitle.wither_storm_tractor_beam", "Tractor beam activates");
      this.add("witherstormmod.subtitle.wither_storm_shoot", "Wither Storm attacks");
      this.add("witherstormmod.subtitle.wither_storm_thump", "Wither Storm splats");
      this.add("witherstormmod.subtitle.wither_storm_tremble", "Wither Storm trembles");
      this.add("witherstormmod.subtitle.super_tnt_fuse", "Super TNT fizzes");
      this.add("witherstormmod.subtitle.formidibomb_explosion", "Formidibomb explosion");
      this.add("witherstormmod.subtitle.tremble", "Tremble");
      this.add("witherstormmod.subtitle.formidibomb_pulse_loop", "Formidibomb pulses");
      this.add("witherstormmod.subtitle.command_block_pulse_loop", "Command Block pulses");
      this.add("witherstormmod.subtitle.rib_bone_crack", "Rib bone cracks");
      this.add("witherstormmod.subtitle.command_block_activates", "Command Block activates");
      this.add("witherstormmod.subtitle.command_block_summon", "Command Block summons");
      this.add("witherstormmod.subtitle.withered_symbiont_cast_spell", "Withered Symbiont casts spell");
      this.add("witherstormmod.subtitle.withered_symbiont_summon", "Withered Symbiont summons");
      this.add("witherstormmod.subtitle.withered_symbiont_prepare_spell", "Withered Symbiont prepares spell");
      this.add("witherstormmod.subtitle.withered_symbiont_death", "Withered Symbiont dies");
      this.add("witherstormmod.subtitle.withered_symbiont_hurt", "Withered Symbiont hurts");
      this.add("witherstormmod.subtitle.withered_symbiont_ambient", "Withered Symbiont groans");
      this.add("witherstormmod.subtitle.withered_symbiont_step", "Withered Symbiont steps");
      this.add("witherstormmod.subtitle.withered_symbiont_pull", "Withered Symbiont pulls");
      this.add("witherstormmod.subtitle.withered_symbiont_launch_mob", "Withered Symbiont launches mob");
      this.add("witherstormmod.subtitle.withered_symbiont_power_down", "Withered Symbiont powers down");
      this.add("witherstormmod.subtitle.withered_symbiont_heart_beat", "Withered Symbiont heart beats");
      this.add("witherstormmod.subtitle.withered_symbiont_theme", "Withered Symbiont theme plays");
      this.add("witherstormmod.subtitle.withered_symbiont_intense_theme", "Withered Symbiont intense theme plays");
      this.add("witherstormmod.subtitle.withered_symbiont_spawn", "Withered Symbiont spawns");
      this.add("witherstormmod.subtitle.command_block_hit", "Command Block blocks");
      this.add("witherstormmod.subtitle.bowels_ambience", "Bowels ambience");
      this.add("witherstormmod.subtitle.bowels_transport", "Player is transported");
      this.add("witherstormmod.subtitle.bowels_tremble", "Bowels trembles");
      this.add("witherstormmod.subtitle.whoosh", "Tentacle whooshes");
      this.add("witherstormmod.subtitle.command_block_damage", "Command Block is damaged");
      this.add("witherstormmod.subtitle.command_block_power_down", "Command Block powers down");
      this.add("witherstormmod.subtitle.command_block_death", "Command Block dies");
      this.add("witherstormmod.subtitle.wither_storm_tractor_beam_activate", "Tractor beam activates");
      this.add("witherstormmod.subtitle.amulet_bind", "Amulet binds");
      this.add("witherstormmod.subtitle.amulet_unbind", "Amulet unbinds");
      this.add("witherstormmod.subtitle.amulet_swap", "Amulet swaps");
      this.add("witherstormmod.subtitle.block_cluster_shake", "Block cluster shakes");
      this.add("witherstormmod.subtitle.withered_beacon_activate", "Withered beacon activates");
      this.add("witherstormmod.subtitle.withered_beacon_deactivate", "Withered beacon deactivates");
      this.add("witherstormmod.subtitle.withered_beacon_ambient", "Withered beacon hums");
      this.add("witherstormmod.subtitle.withered_beacon_power_up", "Withered beacon powers up");
      this.add("witherstormmod.subtitle.command_block_build", "Command block builds");
      this.add("witherstormmod.subtitle.earth_rumble", "Earth rumbles");
      this.add("witherstormmod.subtitle.cave_wind", "Cave wind howls");
      this.add("witherstormmod.subtitle.tentacle_spike_stab", "Tentacle spike stabs");
      this.add("witherstormmod.subtitle.formidi_blade_charging", "Formidi-Blade Charges");
      this.add("witherstormmod.subtitle.formidi_blade_decharge", "Formidi-Blade Decharges");
      this.add("witherstormmod.subtitle.command_block_cracks", "Command Block Cracks");
      this.add("witherstormmod.subtitle.command_block_destruct", "Command Block Destructs");
      this.add("witherstormmod.subtitle.withered_phlegm_block_open", "Withered Phlegm Block Opens");
      this.add("witherstormmod.subtitle.withered_phlegm_block_close", "Withered Phlegm Block Closes");
      this.add("witherstormmod.subtitle.mob_infected", "Mob is infected");
      this.add("witherstormmod.subtitle.mob_cured", "Mob is cured");
      this.add("witherstormmod.subtitle.flaming_skull_impact", "Flaming Skull Explodes");
      this.add("witherstormmod.watermark.withered_symbiont_theme", "Bound by Darkness by Mar Mar");
      this.add("commands.witherstormmod.setphase.success", "Set phase %s for %s");
      this.add("commands.witherstormmod.setphase.invalid", "Could not set phase");
      this.add("commands.witherstormmod.getphase.result", "%s is currently at phase %s");
      this.add("commands.witherstormmod.evolve.success", "Successfully evolved %s to phase %s");
      this.add("commands.witherstormmod.evolve.fail", "Could not evolve selector");
      this.add("commands.witherstormmod.setconsumed.success", "Set consumed entity amount %s for %s");
      this.add("commands.witherstormmod.setconsumed.invalid", "Could not set consumed entity amount");
      this.add("commands.witherstormmod.getconsumed.result", "%s has consumed %s objects out of %s");
      this.add("commands.witherstormmod.setevolution.success", "Set Evolution Speed %s for %s");
      this.add("commands.witherstormmod.entity.arg.invalid", "Entity argument must be a Wither Storm");
      this.add("commands.witherstormmod.chunkloader.get.specific", "%s is loading chunks at X: %s Z: %s with radius %s");
      this.add("commands.witherstormmod.chunkloader.get.specific.ticket", "%s ticket(s) of type 'wither_storm' at chunk %s");
      this.add("commands.witherstormmod.chunkloader.get", "CWSM has %s chunk loaders active");
      this.add("commands.witherstormmod.chunkloader.get.tickets", "CWSM has %s ticket(s) of type 'wither_storm' loaded in dimension %s");
      this.add("commands.witherstormmod.chunkloader.none", "Selector has no chunks loaded!");
      this.add("commands.witherstormmod.chunkloader.refresh", "Refreshing all chunk loaders");
      this.add("commands.witherstormmod.entity.arg.notLiving", "Entity argument must be mob");
      this.add("commands.witherstormmod.sickness.startInfection", "Beginning infection for %s");
      this.add("commands.witherstormmod.sickness.startCure", "Curing %s");
      this.add("commands.witherstormmod.sickness.alreadyInfected", "%s is already infected");
      this.add("commands.witherstormmod.sickness.alreadyBeingCured", "%s is already being cured");
      this.add("commands.witherstormmod.sickness.cureNotInfected", "Can't cure a mob that isn't infected");
      this.add("commands.witherstormmod.sickness.immune", "%s is immune to wither sickness");
      this.add("commands.witherstormmod.sickness.randomizeModifiers", "Randomizing modifiers for %s");
      this.add("commands.witherstormmod.explodeStorm.success", "Exploding %s");
      this.add("commands.witherstormmod.explodeStorm.failure", "%s is already playing dead");
      this.add("commands.witherstormmod.reviveStorm.success", "Reviving %s");
      this.add("commands.witherstormmod.reviveStorm.failure", "%s has already been revived");
      this.add("commands.witherstormmod.enterBowels.failure", "Something went wrong when trying to setup the bowels for %s (report to mod author)");
      this.add("commands.witherstormmod.enterBowels.failure.cannotChangeDim", "Selector cannot change dimensions");
      this.add("commands.witherstormmod.enterBowels.dim.invalid", "Selector is already in the bowels");
      this.add("commands.witherstormmod.createDebris.success", "Creating debris for %s");
      this.add("commands.witherstormmod.distractions.ultimateTargetDistractions.makeDistracted.success", "Successfully distracted %s");
      this.add("commands.witherstormmod.distractions.ultimateTargetDistractions.makeDistracted.fail", "Selector is already distracted");
      this.add("commands.witherstormmod.distractions.ultimateTargetDistractions.makeFocused.success", "Focusing %s");
      this.add("commands.witherstormmod.distractions.ultimateTargetDistractions.makeFocused.fail", "Selector is already focused");
      this.add("commands.witherstormmod.newBowels.success", "Marking current bowels for %s as complete; new bowels will be used upon entrance");
      this.add("commands.witherstormmod.newBowels.failure", "%s must be entered first!");
      this.add("commands.witherstormmod.ultimateTarget.set.duplicate", "That target is already being targeted!");
      this.add("commands.witherstormmod.ultimateTarget.set.success", "Now targeting %s");
      this.add("commands.witherstormmod.ultimateTarget.set.entity.invalid", "Entity is invalid");
      this.add("commands.witherstormmod.ultimateTarget.clear.success", "Clearing target overrides");
      this.add("commands.witherstormmod.ultimateTarget.invalid", "Selector does not have an ultimate target manager");
      this.add("commands.witherstormmod.ultimateTarget.beginChase", "Beginning chase sequence for %s");
      this.add("commands.witherstormmod.ultimateTarget.stopChase", "Stopping chase sequence for %s");
      this.add("commands.witherstormmod.ultimateTarget.cannotBeginChase", "Selector is already chasing ultimate target!");
      this.add("commands.witherstormmod.ultimateTarget.cannotStopChase", "Cannot stop chase sequence; selector is not chasing ultimate target!");
      this.add("commands.witherstormmod.ultimateTarget.get.pos.none", "%s has no target pos");
      this.add("commands.witherstormmod.ultimateTarget.get.pos", "%s is currently moving towards X: %s Y: %s Z: %s (%s)");
      this.add("commands.witherstormmod.ultimateTarget.get.pos.click", "click to teleport");
      this.add("commands.witherstormmod.ultimateTarget.get.player.none", "%s has no target entity");
      this.add("commands.witherstormmod.ultimateTarget.get.player", "%s is currently moving towards %s (%s)");
      this.add("commands.witherstormmod.ultimateTarget.get.player.click", "click to teleport");
      this.add("commands.witherstormmod.isInTractorBeam.success", "%s is currently in head number %s's tractor beam");
      this.add("commands.witherstormmod.isInTractorBeam.fail", "%s is not currently in a tractor beam");
      this.add("commands.witherstormmod.lock.success", "Locked %s");
      this.add("commands.witherstormmod.lock.fail", "Wither Storm is already locked!");
      this.add("commands.witherstormmod.unlock.success", "Unlocked %s");
      this.add("commands.witherstormmod.unlock.fail", "Wither Storm is already unlocked!");
      this.add("commands.witherstormmod.conversion.convert.possible", "%s can convert");
      this.add("commands.witherstormmod.conversion.convert.impossible", "%s cannot convert");
      this.add("commands.witherstormmod.conversion.invalid", "Invalid mob");
      this.add("commands.witherstormmod.conversion.success", "Successfully converted %s");
      this.add("commands.witherstormmod.conversion.fail", "Could not convert mob");
      this.add("commands.witherstormmod.conversion.block.success", "Successfully tainted block");
      this.add("commands.witherstormmod.conversion.block.fail", "Could not taint block");
      this.add("commands.witherstormmod.conversion.block.area.excessive", "Area is too large; max allowed: %s");
      this.add("commands.witherstormmod.conversion.block.area.success", "Successfully tainted %s blocks");
      this.add("commands.witherstormmod.screenShake.success", "Causing screen shake for %s players");
      this.add("commands.witherstormmod.screenShake.fail", "Time duration is too long");
      this.add("argument.witherstormmod.wither_storm.invalid", "Selector is not a Wither Storm!");
      this.add("argument.witherstormmod.entity.selector.wither_storm", "Nearest Wither Storm");
      this.add("gui.witherstormmod.screen.wsmoptions.nazaKofi", "Nazaru's Ko-fi");
      this.add("gui.witherstormmod.screen.wsmoptions.nazaKofi.info", "Support Nazaru on Ko-fi!");
      this.add("gui.witherstormmod.button.exitAndSave.title", "Save and Exit");
      this.add("gui.witherstormmod.button.exit.title", "Exit");
      this.add("gui.witherstormmod.button.preset.title", "Preset");
      this.add("gui.witherstormmod.button.preset.holdShift", "Hold SHIFT to see a description");
      this.add("gui.witherstormmod.button.reset.title", "Reset");
      this.add("gui.witherstormmod.button.refreshSounds.title", "Refresh Sounds");
      this.add("gui.witherstormmod.button.showArea.description", "Show connected support beacons working area");
      this.add("gui.witherstormmod.button.select.cooldown.description", "You must wait 10 seconds before selecting a new effect");
      this.add("gui.witherstormmod.config.renderDebrisTwoDimensional.title", "Render Debris 2D");
      this.add("config.witherstormmod.preset.client.high.title", "High");
      this.add("config.witherstormmod.preset.client.medium.title", "Medium");
      this.add("config.witherstormmod.preset.client.low.title", "Low");
      this.add("config.witherstormmod.preset.client.ultra_low.title", "Ultra Low");
      this.add("config.witherstormmod.preset.server.performance.title", "Server Performance");
      this.add("config.witherstormmod.preset.server.mass_destruction.title", "Mass Destruction");
      this.add(
         "config.witherstormmod.preset.client.high.description",
         "Default preset for users with decent systems. Not recommended for users with low-er end PC's who want \n a somewhat stable framerate."
      );
      this.add(
         "config.witherstormmod.preset.client.medium.description",
         "Designed for lower-end users who want to be able to have the default Wither Storm models while sacrificing block cluster rendering."
      );
      this.add(
         "config.witherstormmod.preset.client.low.description",
         "Designed for low-end systems. Disables the debris cloud and enables the Wither Storm LOD option, while also disabling block cluster rendering."
      );
      this.add(
         "config.witherstormmod.preset.client.ultra_low.description",
         "Designed if your system is really struggling to run CWSM. Enables low res models, disables block cluster rendering, disables the debris particles, etc."
      );
      this.add("config.witherstormmod.preset.server.performance.description", "Designed to help performance on a multiplayer setting.");
      this.add(
         "config.witherstormmod.preset.server.mass_destruction.description",
         "Increases cluster pick up rate and increases the explosion size of flaming wither skulls. Watch the world burn!"
      );
      this.add("attribute.witherstormmod.name.target_stationary_flying_speed", "Target Stationary Flying Speed");
      this.add("attribute.witherstormmod.name.slow_flying_speed", "Slow Flying Speed");
      this.add("attribute.witherstormmod.name.evolution_speed", "Evolution Speed");
      this.add("attribute.witherstormmod.name.hunchback_follow_range", "Hunchback Follow Range");
      this.add("advancements.witherstormmod.main.root.title", "The Wither Storm");
      this.add("advancements.witherstormmod.main.root.description", "The start of the worlds impending doom");
      this.add("advancements.witherstormmod.main.summon_wither_storm.title", "Nothing Built Can Last Forever");
      this.add("advancements.witherstormmod.main.summon_wither_storm.description", "Summon the Wither Storm, a mutated Wither capable of mass destruction");
      this.add("advancements.witherstormmod.main.harbinger_of_cataclysmic_fates.title", "Harbinger of Cataclysmic Fates");
      this.add(
         "advancements.witherstormmod.main.harbinger_of_cataclysmic_fates.description",
         "Encounter a Withered Symbiont, a wither sickened, symbiotic agglomeration with Command Block powers"
      );
      this.add("advancements.witherstormmod.main.infinite_potential.title", "Infinite Potential");
      this.add("advancements.witherstormmod.main.infinite_potential.description", "Obtain an Enchanted Command Block Book dropped by a Withered Symbiont");
      this.add("advancements.witherstormmod.main.fbomb.title", "F-Bomb");
      this.add("advancements.witherstormmod.main.fbomb.description", "Obtain a Formidibomb, a bomb capable of destroying almost anything in its path");
      this.add("advancements.witherstormmod.main.strong_grow_weak.title", "The Strong Grow Weak...");
      this.add("advancements.witherstormmod.main.strong_grow_weak.description", "Blow up the Wither Storm using a Formidibomb");
      this.add("advancements.witherstormmod.main.weakened_arise_stronger.title", "...The Weakened Arise Stronger");
      this.add("advancements.witherstormmod.main.weakened_arise_stronger.description", "Watch the Wither Storm arise from its slumber, more powerful than ever");
      this.add("advancements.witherstormmod.main.silver_lining.title", "Every Cloud Has a Silver Lining");
      this.add("advancements.witherstormmod.main.silver_lining.description", "Craft a Command Block tool, imbued with the powers of the Command Block");
      this.add("advancements.witherstormmod.main.insane_dedication.title", "Insane Dedication");
      this.add("advancements.witherstormmod.main.insane_dedication.description", "Craft a Command Block hoe, then look back at what got you to this point");
      this.add("advancements.witherstormmod.main.overly_dedicated.title", "Overly Dedicated");
      this.add("advancements.witherstormmod.main.overly_dedicated.description", "Craft a Wooden Command Block hoe, you are a strange one, but I respect it");
      this.add("advancements.witherstormmod.main.escape_wither_storm.title", "Brushing Shoulders With Death");
      this.add("advancements.witherstormmod.main.escape_wither_storm.description", "Escape from the Wither Storm's clutches by inflicting a blow to its heads");
      this.add("advancements.witherstormmod.main.cured_sickened_mob.title", "Licensed Clinician");
      this.add("advancements.witherstormmod.main.cured_sickened_mob.description", "Cure a wither sickened mob using golden apple stew");
      this.add("advancements.witherstormmod.main.belly_of_the_beast.title", "The Belly of the Beast");
      this.add("advancements.witherstormmod.main.belly_of_the_beast.description", "Enter the bowels of the Wither Storm");
      this.add("advancements.witherstormmod.main.wither_storm_defeated.title", "One Story Ends, Another Begins");
      this.add("advancements.witherstormmod.main.wither_storm_defeated.description", "Defeat the Wither Storm, once and for all");
      this.add("advancements.witherstormmod.main.spyglass_at_wither_storm.title", "Is it the end?");
      this.add("advancements.witherstormmod.main.spyglass_at_wither_storm.description", "Look at the Wither Storm through a spyglass");
      this.add("advancements.witherstormmod.main.amulet.title", "I Spy With My Little Eye");
      this.add("advancements.witherstormmod.main.amulet.description", "Craft an amulet, capable of tracking any mob desired");
      this.add("advancements.witherstormmod.main.activate_super_beacon.title", "Beaconator 4000");
      this.add(
         "advancements.witherstormmod.main.activate_super_beacon.description", "Activate the central Withered Beacon with all four external colors activated"
      );
      this.add("advancements.witherstormmod.main.resummon_wither_storm.title", "Back to the Beginning Again");
      this.add(
         "advancements.witherstormmod.main.resummon_wither_storm.description",
         "Resummon the Wither Storm by right-clicking a withered beacon with three wither skulls"
      );
      this.add("advancements.witherstormmod.main.ring_bell_near_storm.title", "The Storm is Coming!");
      this.add("advancements.witherstormmod.main.ring_bell_near_storm.description", "Alert nearby entities by ringing a bell near the Wither Storm");
      this.add("advancements.witherstormmod.main.resummon_withered_symbiont.title", "Need More Books");
      this.add("advancements.witherstormmod.main.resummon_withered_symbiont.description", "Resummon the Withered Symbiont using a withered beacon");
      this.add("advancements.witherstormmod.main.summon_mob_withered_beacon.title", "Infection Generator");
      this.add("advancements.witherstormmod.main.summon_mob_withered_beacon.description", "Summon any mob using a withered beacon");
      this.add("advancements.witherstormmod.main.fully_link_amulet.title", "Order of the Stone");
      this.add("advancements.witherstormmod.main.fully_link_amulet.description", "Link all four colors of the amulet to different mobs");
      this.add("advancements.witherstormmod.main.nearly_kill_wither_storm.title", "A Nasty Surprise");
      this.add(
         "advancements.witherstormmod.main.nearly_kill_wither_storm.description",
         "Almost kill the Wither Storm during it's hunchback phases using melee/projectile weapons"
      );
      this.add("death.attack.flamingWitherSkull", "%1$s was incinerated by a flaming skull from %2$s");
      this.add("death.attack.witherSickness", "%1$s wilted away");
      this.add("death.attack.witherSickness.player", "%1$s wilted away whilst fighting %2$s");
      this.add("death.attack.super_tnt_explosion", "%1$s forgot to craft the Formidibomb");
      this.add("death.attack.super_tnt_explosion.player", "%1$s forgot to craft the Formidibomb");
      this.add("death.attack.super_tnt_explosion.player.item", "%1$s forgot to craft the Formidibomb");
      this.add("death.attack.formidibomb", "%1$s experienced a catastrophic explosion");
      this.add("death.attack.formidibomb.player", "%1$s experienced a catastrophic explosion at the hands of %2$s");
      this.add("death.attack.formidibomb.player.item", "%1$s experienced a catastrophic explosion at the hands of %2$s using %3$s");
      this.add("death.attack.witherStorm", "%1$s was chomped by %2$s");
      this.add("chat.witherstormmod.bowels.structuresDisabled", "WARNING: Structures are disabled in this world! The bowels cannot be accessed.");
      this.add(
         "chat.witherstormmod.optifine.notice",
         "WARNING: Cracker's Wither Storm Mod does not support OptiFine, issues may arise and certain performance features may be disabled. Click this message to see a list of incompatibilities and their potential fixes. This message will not show again."
      );
      this.add(
         "chat.witherstormmod.flyingDisabled.notice",
         "WARNING: Flying is disabled on this server. Non-opped players may get kicked when being pulled by the Wither Storm's tractor beams, among other reasons. It is recommended to set 'allow-flight' to 'true' in your server.properties file. This message will not show again."
      );
      this.add(
         "withered_beacon.info",
         "The Withered Beacon will apply the selected effect to all nearby players in a very large radius.\n\nIn order to activate the Withered Beacon, a 3x3 square of lapis blocks must be constructed directly below it. Then, more layers of any valid beacon base blocks can be added in a pyramid shape to increase the strength of the selected effect.\n\nWithered Support Beacons can be connected and will apply effects from an infinite distance in the direction the connecting beam makes. To connect a support beacon, make a 3x3 square directly underneath the beacon of either emerald, diamond, redstone, or iron blocks. Make sure the support beacon is within 5 blocks of the main beacon. There can only be one of each color and a maximum of four can be connected."
      );
      this.add("witherstormmod.jei.item_craft_super_beacon.title", "Withered Beacon Item Crafting");
      this.add("witherstormmod.jei.resummoning_super_beacon.title", "Withered Beacon Summoning");
      this.add("witherstormmod.jei.super_beacon.requiresMainActivated", "Requires Central Beacon Activated");
      this.add("witherstormmod.jei.super_beacon.requiresAllSupports", "Requires All Four Supports");
      this.add("witherstormmod.jei.super_beacon.requiresFullBeacon", "Requires Completed Level 4 Beacon");
      this.add("witherstormmod.jei.tainted_pumpkin_info", "Throw a potion of decay on a normal pumpkin block to convert it to its tainted variant");
      ConfigLangGeneratorHelper.langForSpec("witherstormmod", WitherStormModConfig.CLIENT_SPEC, this, Info.ONLY_RANGE);
      ConfigLangGeneratorHelper.langForSpec("witherstormmod", WitherStormModConfig.COMMON_SPEC, this, Info.ONLY_RANGE);
      ConfigLangGeneratorHelper.langForSpec("witherstormmod", WitherStormModConfig.SERVER_SPEC, this, Info.ONLY_RANGE);
      this.replace("gui.witherstormmod.config.lowResModels.title", "Low Resolution Models");
      this.replace("gui.witherstormmod.config.renderDebrisCloud.title", "Render Debris Particles");
      this.replace("gui.witherstormmod.config.playerFavorability.title", "Players Are More Favorable");
      this.replace("gui.witherstormmod.config.distanceMultiplier.title", "Target Stationary Distance Multiplier");
      this.replace("gui.witherstormmod.config.targetRunawayAttempts.title", "Count Target Runaway Attempts");
      this.replace("gui.witherstormmod.config.targetRunawayAttemptMinutes.title", "Target Runaway Required Minutes");
      this.replace("gui.witherstormmod.config.targetRunawayAttemptsRequired.title", "Runaway Attempts Till Chase");
      this.replace("gui.witherstormmod.config.clustersRemoveItems.title", "Block Clusters Remove Items");
      this.replace("gui.witherstormmod.config.witherSicknessEnabled.title", "Enabled");
      this.replace("gui.witherstormmod.config.lowImmuneRequiredProximitySeconds.title", "LI Required Proximity Seconds");
      this.replace("gui.witherstormmod.config.lowImmuneApplicationDelay.title", "LI Application Delay");
      this.replace("gui.witherstormmod.config.lowImmuneCureDelay.title", "LI Cure Delay");
      this.replace("gui.witherstormmod.config.lowImmuneProximityModifierMax.title", "LI Proximity Seconds Modifier Max");
      this.replace("gui.witherstormmod.config.lowImmuneApplicationModifierMax.title", "LI Application Delay Modifier Max");
      this.replace("gui.witherstormmod.config.lowImmuneCureDelayModifierMax.title", "LI Cure Delay Modifier Max");
      this.replace("gui.witherstormmod.config.targettingDistractionsEnabled.title", "Can Be Distracted");
      this.replace("gui.witherstormmod.config.distractionTimeMinutes.title", "Distraction Time");
      this.replace("gui.witherstormmod.config.searchableRangeMultiplier.title", "Search Range Multiplier");
      this.replace("gui.witherstormmod.config.revivalTimer.title", "Should Use Revival Timer");
      this.replace("gui.witherstormmod.config.revivalTimeMinutes.title", "Revival Time");
      this.replace("gui.witherstormmod.config.revivalPlayerProtection.title", "Player Protection Time");
      this.replace("gui.witherstormmod.config.canPickupMobClusters.title", "Can Pick Up Mob Clusters");
      this.replace("gui.witherstormmod.config.renderSkyAmbienceEffects.title", "Sky Ambience Effects");
      this.replace("gui.witherstormmod.config.injectCustomAiBehavior.title", "Inject Custom AI Behavior");
      this.replace("gui.witherstormmod.config.lowerDebrisResWithPhase.title", "Lower Debris Resolution with Phase");
      this.replace("gui.witherstormmod.config.keepSicknessAfterRespawn.title", "Keep After Respawn");
      this.replace("gui.witherstormmod.config.devourerClusterPickupInterval.title", "Devourer Cluster Pick Up Interval");
      this.replace("gui.witherstormmod.config.chunksToLoad.title", "Chunk Loading Max Count");
      this.replace("gui.witherstormmod.config.optifineWarning.title", "OptiFine Warning");
      this.replace("gui.witherstormmod.config.aprilFools.title", "April Fools Render Effects");
      this.replace("gui.witherstormmod.config.randomStrollingWhenTargetHidden.title", "Random Strolling if Ultimate Target Hidden");
      this.replace("gui.witherstormmod.config.flamingSkullExplosionSize.title", "Explosion Size");
      this.replace("gui.witherstormmod.config.flamingSkullSpeedModifier.title", "Speed Modifier");
      this.replace("gui.witherstormmod.config.resummonedPhase.title", "Withered Beacon Resummoned Phase");
      this.replace("gui.witherstormmod.config.clusterSizeModifier.title", "Block Cluster Size Modifier");
      this.replace("gui.witherstormmod.config.caveRumbleIntervalMin.title", "Cave Rumble Interval Minimum");
      this.replace("gui.witherstormmod.config.caveRumbleIntervalMax.title", "Cave Rumble Interval Maximum");
      this.replace("gui.witherstormmod.config.injectAiMobBlacklist.title", "Inject AI Mob Blacklist");
      this.replace("gui.witherstormmod.config.formidibombFuseEnabled.title", "Fuse Enabled");
      this.replace("gui.witherstormmod.config.endOfPhaseFiveBombableExclusively.title", "End Of Phase Five Formidibombable Exclusively");
      this.replace("gui.witherstormmod.config.shouldPlayGlobalSoundsCrossDimensionally.title", "Global Sounds Cross Dimensionally");
      this.add((Block)WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.get(), "Withered Phlegm Block");
      this.add("witherstormmod.resourcepacks.programmer_art", "CWSM Programmer Art");
   }

   private void replace(String key, String entry) {
      try {
         this.add(key, entry);
      } catch (IllegalStateException var4) {
      }
   }
}
