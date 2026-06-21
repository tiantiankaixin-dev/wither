package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.common.block.FireworkBundleBlock;
import nonamecrackers2.witherstormmod.common.block.FormidibombBlock;
import nonamecrackers2.witherstormmod.common.block.NonGrowableMushroomBlock;
import nonamecrackers2.witherstormmod.common.block.StrippableLogBlock;
import nonamecrackers2.witherstormmod.common.block.SuperBeaconBlock;
import nonamecrackers2.witherstormmod.common.block.SuperSupportBeaconBlock;
import nonamecrackers2.witherstormmod.common.block.SuperTNTBlock;
import nonamecrackers2.witherstormmod.common.block.TaintedCarvedPumpkinBlock;
import nonamecrackers2.witherstormmod.common.block.TaintedPumpkinBlock;
import nonamecrackers2.witherstormmod.common.block.TaintedStatue;
import nonamecrackers2.witherstormmod.common.block.TaintedTorchBlock;
import nonamecrackers2.witherstormmod.common.block.TaintedVeinBlock;
import nonamecrackers2.witherstormmod.common.block.TaintedWallTorchBlock;
import nonamecrackers2.witherstormmod.common.block.WireBlock;
import nonamecrackers2.witherstormmod.common.block.WitheredPhlegmBlock;

public class WitherStormModBlocks {
   public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "witherstormmod");
   public static final BlockSetType TAINTED_SET = BlockSetType.register(new BlockSetType("tainted"));
   public static final WoodType TAINTED = WoodType.register(new WoodType("tainted", TAINTED_SET));
   public static final RegistryObject<Block> SUPER_TNT = BLOCKS.register(
      "super_tnt", () -> new SuperTNTBlock(Properties.of().instabreak().sound(SoundType.GRASS))
   );
   public static final RegistryObject<Block> FORMIDIBOMB = BLOCKS.register(
      "formidibomb", () -> new FormidibombBlock(Properties.of().strength(0.8F).sound(SoundType.GRASS).lightLevel(state -> 7))
   );
   public static final RegistryObject<Block> SUPER_BEACON = BLOCKS.register(
      "super_beacon", () -> new SuperBeaconBlock(Properties.ofFullCopy(Blocks.GLASS).strength(3.0F).noOcclusion().lightLevel(block -> 12))
   );
   public static final RegistryObject<Block> SUPER_SUPPORT_BEACON = BLOCKS.register(
      "super_support_beacon", () -> new SuperSupportBeaconBlock(Properties.ofFullCopy(Blocks.GLASS).strength(2.5F).noOcclusion().lightLevel(block -> 12))
   );
   public static final RegistryObject<Block> FIREWORK_BUNDLE = BLOCKS.register(
      "firework_bundle", () -> new FireworkBundleBlock(Properties.of().strength(2.5F).sound(SoundType.GRASS))
   );
   public static final RegistryObject<Block> TAINTED_ZOMBIE_SITTING = BLOCKS.register(
      "tainted_zombie_sitting", () -> new TaintedStatue(Properties.ofFullCopy(Blocks.SLIME_BLOCK).noCollission().strength(1.0F, 6.0F).noOcclusion())
   );
   public static final RegistryObject<Block> TAINTED_ZOMBIE_WALL = BLOCKS.register(
      "tainted_zombie_wall", () -> new TaintedStatue(Properties.ofFullCopy(Blocks.SLIME_BLOCK).noCollission().strength(1.0F, 6.0F).noOcclusion())
   );
   public static final RegistryObject<Block> TAINTED_ZOMBIE_LYING = BLOCKS.register(
      "tainted_zombie_lying", () -> new TaintedStatue(Properties.ofFullCopy(Blocks.SLIME_BLOCK).noCollission().strength(1.0F, 6.0F).noOcclusion())
   );
   public static final RegistryObject<Block> TAINTED_BONE_PILE = BLOCKS.register(
      "tainted_bone_pile", () -> new TaintedStatue(Properties.ofFullCopy(Blocks.BONE_BLOCK).noCollission().strength(1.0F, 6.0F).noOcclusion())
   );
   public static final RegistryObject<Block> TAINTED_SKELETON_WALL = BLOCKS.register(
      "tainted_skeleton_wall", () -> new TaintedStatue(Properties.ofFullCopy(Blocks.BONE_BLOCK).noCollission().strength(1.0F, 6.0F).noOcclusion())
   );
   public static final RegistryObject<Block> TAINTED_SKULL_CEILING = BLOCKS.register(
      "tainted_skull_ceiling", () -> new TaintedStatue(Properties.ofFullCopy(Blocks.BONE_BLOCK).noCollission().strength(1.0F, 6.0F).noOcclusion())
   );
   public static final RegistryObject<Block> TAINTED_FLESH_VEINS = BLOCKS.register(
      "tainted_flesh_veins",
      () -> new TaintedVeinBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).noCollission().strength(0.4F).sound(SoundType.SLIME_BLOCK))
   );
   public static final RegistryObject<Block> TAINTED_FLESH_BLOCK = BLOCKS.register(
      "tainted_flesh_block", () -> new Block(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.6F).sound(SoundType.SLIME_BLOCK))
   );
   public static final RegistryObject<Block> INFECTED_FLESH_BLOCK = BLOCKS.register(
      "infected_flesh_block", () -> new Block(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.8F).sound(SoundType.SLIME_BLOCK))
   );
   public static final RegistryObject<Block> HARDENED_FLESH_BLOCK = BLOCKS.register(
      "hardened_flesh_block", () -> new Block(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(-1.0F, 3600000.0F).sound(SoundType.SLIME_BLOCK))
   );
   public static final RegistryObject<Block> WITHERED_PHLEGM_BLOCK = BLOCKS.register(
      "withered_phlegm_block",
      () -> new WitheredPhlegmBlock(
            Properties.of()
               .mapColor(MapColor.COLOR_PURPLE)
               .strength(0.5F)
               .sound(SoundType.SLIME_BLOCK)
               .lightLevel($ -> 4)
               .noOcclusion()
               .emissiveRendering((blockState, blockGetter, blockPos) -> true)
         )
   );
   public static final RegistryObject<Block> TAINTED_STONE = BLOCKS.register(
      "tainted_stone", () -> new Block(Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 6.0F))
   );
   public static final RegistryObject<Block> TAINTED_STONE_STAIRS = BLOCKS.register(
      "tainted_stone_stairs",
      () -> new StairBlock(((Block)TAINTED_STONE.get()).defaultBlockState(), Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 6.0F))
   );
   public static final RegistryObject<Block> TAINTED_STONE_SLAB = BLOCKS.register(
      "tainted_stone_slab", () -> new SlabBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 6.0F))
   );
   public static final RegistryObject<Block> TAINTED_STONE_BUTTON = BLOCKS.register(
      "tainted_stone_button",
      () -> new ButtonBlock(
            BlockSetType.STONE,
            20,
            Properties.of().mapColor(MapColor.COLOR_PURPLE).noCollission().noOcclusion().strength(0.5F).sound(SoundType.STONE)
         )
   );
   public static final RegistryObject<Block> TAINTED_STONE_PRESSURE_PLATE = BLOCKS.register(
      "tainted_stone_pressure_plate",
      () -> new PressurePlateBlock(
            BlockSetType.STONE,
            Properties.of().mapColor(MapColor.COLOR_PURPLE).noCollission().noOcclusion().strength(0.5F).sound(SoundType.STONE)
         )
   );
   public static final RegistryObject<Block> TAINTED_COBBLESTONE = BLOCKS.register(
      "tainted_cobblestone", () -> new Block(Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(2.0F, 6.0F))
   );
   public static final RegistryObject<Block> TAINTED_COBBLESTONE_STAIRS = BLOCKS.register(
      "tainted_cobblestone_stairs",
      () -> new StairBlock(
            ((Block)TAINTED_COBBLESTONE.get()).defaultBlockState(), Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 6.0F)
         )
   );
   public static final RegistryObject<Block> TAINTED_COBBLESTONE_SLAB = BLOCKS.register(
      "tainted_cobblestone_slab", () -> new SlabBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 6.0F))
   );
   public static final RegistryObject<Block> TAINTED_COBBLESTONE_WALL = BLOCKS.register(
      "tainted_cobblestone_wall", () -> new WallBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 6.0F))
   );
   public static final RegistryObject<Block> TAINTED_SAND = BLOCKS.register(
      "tainted_sand", () -> new ColoredFallingBlock(new ColorRGBA(10708917), Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.5F).sound(SoundType.SAND))
   );
   public static final RegistryObject<Block> TAINTED_DIRT = BLOCKS.register(
      "tainted_dirt", () -> new Block(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.5F).sound(SoundType.GRAVEL))
   );
   public static final RegistryObject<Block> TAINTED_SANDSTONE = BLOCKS.register(
      "tainted_sandstone", () -> new Block(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.8F).requiresCorrectToolForDrops().sound(SoundType.STONE))
   );
   public static final RegistryObject<Block> TAINTED_SANDSTONE_SLAB = BLOCKS.register(
      "tainted_sandstone_slab",
      () -> new SlabBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.8F).requiresCorrectToolForDrops().sound(SoundType.STONE))
   );
   public static final RegistryObject<Block> TAINTED_SANDSTONE_STAIRS = BLOCKS.register(
      "tainted_sandstone_stairs",
      () -> new StairBlock(
            ((Block)TAINTED_SANDSTONE.get()).defaultBlockState(),
            Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F).sound(SoundType.STONE)
         )
   );
   public static final RegistryObject<Block> TAINTED_SANDSTONE_WALL = BLOCKS.register(
      "tainted_sandstone_wall", () -> new WallBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(0.8F))
   );
   public static final RegistryObject<Block> TAINTED_CUT_SANDSTONE = BLOCKS.register(
      "tainted_cut_sandstone", () -> new Block(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.8F).requiresCorrectToolForDrops().sound(SoundType.STONE))
   );
   public static final RegistryObject<Block> TAINTED_CUT_SANDSTONE_SLAB = BLOCKS.register(
      "tainted_cut_sandstone_slab",
      () -> new SlabBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.8F).requiresCorrectToolForDrops().sound(SoundType.STONE))
   );
   public static final RegistryObject<Block> TAINTED_CHISELED_SANDSTONE = BLOCKS.register(
      "tainted_chiseled_sandstone",
      () -> new Block(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.8F).requiresCorrectToolForDrops().sound(SoundType.STONE))
   );
   public static final RegistryObject<Block> TAINTED_SMOOTH_SANDSTONE = BLOCKS.register(
      "tainted_smooth_sandstone", () -> new Block(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.8F).requiresCorrectToolForDrops().sound(SoundType.STONE))
   );
   public static final RegistryObject<Block> TAINTED_SMOOTH_SANDSTONE_SLAB = BLOCKS.register(
      "tainted_smooth_sandstone_slab",
      () -> new SlabBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.8F).requiresCorrectToolForDrops().sound(SoundType.STONE))
   );
   public static final RegistryObject<Block> TAINTED_SMOOTH_SANDSTONE_STAIRS = BLOCKS.register(
      "tainted_smooth_sandstone_stairs",
      () -> new StairBlock(
            ((Block)TAINTED_SMOOTH_SANDSTONE.get()).defaultBlockState(),
            Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F).sound(SoundType.STONE)
         )
   );
   public static final RegistryObject<Block> TAINTED_SMOOTH_SANDSTONE_WALL = BLOCKS.register(
      "tainted_smooth_sandstone_wall",
      () -> new WallBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.8F).requiresCorrectToolForDrops().sound(SoundType.STONE))
   );
   public static final RegistryObject<Block> TAINTED_GLASS = BLOCKS.register(
      "tainted_glass", () -> new TransparentBlock(Properties.ofFullCopy(Blocks.GLASS).mapColor(MapColor.COLOR_PURPLE).strength(0.6F, 1200.0F).noOcclusion())
   );
   public static final RegistryObject<Block> TAINTED_GLASS_PANE = BLOCKS.register(
      "tainted_glass_pane", () -> new IronBarsBlock(Properties.ofFullCopy(Blocks.GLASS).mapColor(MapColor.COLOR_PURPLE).strength(0.6F, 1200.0F).noOcclusion())
   );
   public static final RegistryObject<Block> TAINTED_PLANKS = BLOCKS.register(
      "tainted_planks", () -> new Block(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F).sound(SoundType.WOOD))
   );
   public static final RegistryObject<Block> TAINTED_TORCH = BLOCKS.register(
      "tainted_torch",
      () -> new TaintedTorchBlock(
            Properties.of().mapColor(MapColor.COLOR_PURPLE).noCollission().instabreak().lightLevel(light -> 8).sound(SoundType.WOOD),
            WitherStormModParticleTypes.PHLEGM
         )
   );
   public static final RegistryObject<Block> TAINTED_WALL_TORCH = BLOCKS.register(
      "tainted_wall_torch",
      () -> new TaintedWallTorchBlock(
            Properties.of().mapColor(MapColor.COLOR_PURPLE).noCollission().instabreak().lightLevel(light -> 8).sound(SoundType.WOOD),
            WitherStormModParticleTypes.PHLEGM
         )
   );
   public static final RegistryObject<Block> TAINTED_SIGN = BLOCKS.register(
      "tainted_sign",
      () -> new StandingSignBlock(TAINTED, Properties.of().mapColor(MapColor.COLOR_PURPLE).noCollission().strength(1.0F).sound(SoundType.WOOD))
   );
   public static final RegistryObject<Block> TAINTED_WALL_SIGN = BLOCKS.register(
      "tainted_wall_sign",
      () -> new WallSignBlock(TAINTED, Properties.of().mapColor(MapColor.COLOR_PURPLE).noCollission().strength(1.0F).sound(SoundType.WOOD))
   );
   public static final RegistryObject<Block> TAINTED_HANGING_SIGN = BLOCKS.register(
      "tainted_hanging_sign",
      () -> new CeilingHangingSignBlock(
            TAINTED,
            Properties.of()
               .mapColor(MapColor.COLOR_PURPLE)
               .forceSolidOn()
               .instrument(NoteBlockInstrument.BASS)
               .noCollission()
               .strength(1.0F)
               .ignitedByLava()
               .sound(SoundType.WOOD)
         )
   );
   public static final RegistryObject<Block> TAINTED_WALL_HANGING_SIGN = BLOCKS.register(
      "tainted_wall_hanging_sign",
      () -> new WallHangingSignBlock(
            TAINTED,
            Properties.of()
               .mapColor(MapColor.COLOR_PURPLE)
               .forceSolidOn()
               .instrument(NoteBlockInstrument.BASS)
               .lootFrom(TAINTED_HANGING_SIGN)
               .noCollission()
               .strength(1.0F)
               .ignitedByLava()
               .sound(SoundType.WOOD)
         )
   );
   public static final RegistryObject<Block> STRIPPED_TAINTED_LOG = BLOCKS.register(
      "stripped_tainted_log", () -> new RotatedPillarBlock(Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F))
   );
   public static final RegistryObject<Block> STRIPPED_TAINTED_WOOD = BLOCKS.register(
      "stripped_tainted_wood", () -> new RotatedPillarBlock(Properties.ofFullCopy(Blocks.OAK_WOOD).mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F))
   );
   public static final RegistryObject<Block> TAINTED_LOG = BLOCKS.register(
      "tainted_log",
      () -> new StrippableLogBlock(Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F), STRIPPED_TAINTED_LOG)
   );
   public static final RegistryObject<Block> TAINTED_WOOD = BLOCKS.register(
      "tainted_wood",
      () -> new StrippableLogBlock(Properties.ofFullCopy(Blocks.OAK_WOOD).mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F), STRIPPED_TAINTED_WOOD)
   );
   public static final RegistryObject<Block> TAINTED_LEAVES = BLOCKS.register(
      "tainted_leaves", () -> new LeavesBlock(Properties.ofFullCopy(Blocks.OAK_LEAVES).mapColor(MapColor.COLOR_PURPLE)) {
            public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
               return true;
            }

            public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
               return 20;
            }

            public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
               return 5;
            }
         }
   );
   public static final RegistryObject<Block> TAINTED_DOOR = BLOCKS.register(
      "tainted_door",
      () -> new DoorBlock(TAINTED_SET, Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion())
   );
   public static final RegistryObject<Block> TAINTED_TRAPDOOR = BLOCKS.register(
      "tainted_trapdoor",
      () -> new TrapDoorBlock(TAINTED_SET, Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion())
   );
   public static final RegistryObject<Block> TAINTED_BUTTON = BLOCKS.register(
      "tainted_button",
      () -> new ButtonBlock(
            TAINTED_SET, 30, Properties.of().mapColor(MapColor.COLOR_PURPLE).noCollission().noOcclusion().strength(0.5F).sound(SoundType.WOOD)
         )
   );
   public static final RegistryObject<Block> TAINTED_PRESSURE_PLATE = BLOCKS.register(
      "tainted_pressure_plate",
      () -> new PressurePlateBlock(
            TAINTED_SET,
            Properties.of().mapColor(MapColor.COLOR_PURPLE).noCollission().noOcclusion().strength(0.5F).sound(SoundType.WOOD)
         )
   );
   public static final RegistryObject<Block> TAINTED_STAIRS = BLOCKS.register(
      "tainted_stairs",
      () -> new StairBlock(
            ((Block)TAINTED_PLANKS.get()).defaultBlockState(),
            Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F).sound(SoundType.WOOD)
         )
   );
   public static final RegistryObject<Block> TAINTED_SLAB = BLOCKS.register(
      "tainted_slab", () -> new SlabBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F).sound(SoundType.WOOD))
   );
   public static final RegistryObject<Block> TAINTED_FENCE = BLOCKS.register(
      "tainted_fence", () -> new FenceBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F).sound(SoundType.WOOD))
   );
   public static final RegistryObject<Block> TAINTED_FENCE_GATE = BLOCKS.register(
      "tainted_fence_gate",
      () -> new FenceGateBlock(TAINTED, Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.0F, 3.0F).sound(SoundType.WOOD))
   );
   public static final RegistryObject<Block> TAINTED_MUSHROOM = BLOCKS.register(
      "tainted_mushroom",
      () -> new NonGrowableMushroomBlock(
            Properties.of()
               .mapColor(MapColor.COLOR_PURPLE)
               .noCollission()
               .randomTicks()
               .instabreak()
               .sound(SoundType.FUNGUS)
               .hasPostProcess((state, reader, pos) -> true)
         )
   );
   public static final RegistryObject<Block> POTTED_TAINTED_MUSHROOM = BLOCKS.register(
      "potted_tainted_mushroom",
      () -> new FlowerPotBlock(() -> (FlowerPotBlock)Blocks.FLOWER_POT, TAINTED_MUSHROOM, Properties.of().mapColor(MapColor.COLOR_PURPLE).instabreak())
   );
   public static final RegistryObject<Block> TAINTED_PUMPKIN = BLOCKS.register(
      "tainted_pumpkin", () -> new TaintedPumpkinBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(1.0F).sound(SoundType.WOOD))
   );
   public static final RegistryObject<Block> TAINTED_CARVED_PUMPKIN = BLOCKS.register(
      "tainted_carved_pumpkin",
      () -> new TaintedCarvedPumpkinBlock(
            Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(1.0F).sound(SoundType.WOOD).isValidSpawn((s, g, p, e) -> true)
         )
   );
   public static final RegistryObject<Block> TAINTED_JACK_O_LANTERN = BLOCKS.register(
      "tainted_jack_o_lantern",
      () -> new TaintedCarvedPumpkinBlock(Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(1.0F).sound(SoundType.WOOD).lightLevel(s -> 10))
   );
   public static final RegistryObject<Block> TAINTED_DUST = BLOCKS.register(
      "tainted_dust",
      () -> new WireBlock(1.0F, 0.2509804F, 0.8392157F, Properties.of().mapColor(MapColor.COLOR_PURPLE).noCollission().instabreak().sound(SoundType.FUNGUS))
   );
   public static final RegistryObject<Block> TAINTED_DUST_BLOCK = BLOCKS.register(
      "tainted_dust_block",
      () -> new RedstoneLampBlock(
            Properties.ofFullCopy(Blocks.REDSTONE_LAMP)
               .mapColor(MapColor.COLOR_PURPLE)
               .strength(1.2F, 1.2F)
               .noOcclusion()
               .lightLevel($ -> 12)
               .emissiveRendering((state, reader, pos) -> true)
         )
   );
}
