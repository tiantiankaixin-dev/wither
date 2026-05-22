package nonamecrackers2.witherstormmod.common.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;

public class WitherStormModBlockStatesProvider extends BlockStateProvider {
   public WitherStormModBlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
      super(output, "witherstormmod", exFileHelper);
   }

   protected void registerStatesAndModels() {
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.SUPER_TNT.get(),
         this.models()
            .cubeBottomTop(
               WitherStormModBlocks.SUPER_TNT.getId().getPath(),
               this.modLoc("block/super_tnt_side"),
               this.modLoc("block/super_tnt_bottom"),
               this.modLoc("block/super_tnt_top")
            )
      );
      ModelFile formidibomb = this.models()
         .orientableWithBottom(
            WitherStormModBlocks.FORMIDIBOMB.getId().getPath(),
            this.modLoc("block/formidibomb_side"),
            this.modLoc("block/formidibomb_front"),
            this.modLoc("block/formidibomb_bottom"),
            this.modLoc("block/formidibomb_top")
         );
      this.horizontalBlock((Block)WitherStormModBlocks.FORMIDIBOMB.get(), formidibomb, 0);
      this.simpleBlockItem((Block)WitherStormModBlocks.FORMIDIBOMB.get(), formidibomb);
      ModelFile superBeacon = this.models().getExistingFile(WitherStormModBlocks.SUPER_BEACON.getId());
      this.simpleBlockWithItem((Block)WitherStormModBlocks.SUPER_BEACON.get(), superBeacon);
      ModelFile superSupportBeacon = this.models().getExistingFile(WitherStormModBlocks.SUPER_SUPPORT_BEACON.getId());
      this.simpleBlockWithItem((Block)WitherStormModBlocks.SUPER_SUPPORT_BEACON.get(), superSupportBeacon);
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.FIREWORK_BUNDLE.get(),
         this.models()
            .cubeBottomTop(
               WitherStormModBlocks.FIREWORK_BUNDLE.getId().getPath(),
               this.modLoc("block/firework_bundle_side"),
               this.modLoc("block/firework_bundle_bottom"),
               this.modLoc("block/firework_bundle_top")
            )
      );
      ModelFile taintedZombieSitting = this.models().getExistingFile(WitherStormModBlocks.TAINTED_ZOMBIE_SITTING.getId());
      this.horizontalBlock((Block)WitherStormModBlocks.TAINTED_ZOMBIE_SITTING.get(), taintedZombieSitting);
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_ZOMBIE_SITTING.get(), taintedZombieSitting);
      ModelFile taintedZombieWall = this.models().getExistingFile(WitherStormModBlocks.TAINTED_ZOMBIE_WALL.getId());
      this.horizontalBlock((Block)WitherStormModBlocks.TAINTED_ZOMBIE_WALL.get(), taintedZombieWall);
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_ZOMBIE_WALL.get(), taintedZombieWall);
      ModelFile taintedZombieLying = this.models().getExistingFile(WitherStormModBlocks.TAINTED_ZOMBIE_LYING.getId());
      this.horizontalBlock((Block)WitherStormModBlocks.TAINTED_ZOMBIE_LYING.get(), taintedZombieLying);
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_ZOMBIE_LYING.get(), taintedZombieLying);
      ModelFile taintedBonePile = this.models().getExistingFile(WitherStormModBlocks.TAINTED_BONE_PILE.getId());
      this.horizontalBlock((Block)WitherStormModBlocks.TAINTED_BONE_PILE.get(), taintedBonePile);
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_BONE_PILE.get(), taintedBonePile);
      ModelFile taintedSkeletonWall = this.models().getExistingFile(WitherStormModBlocks.TAINTED_SKELETON_WALL.getId());
      this.horizontalBlock((Block)WitherStormModBlocks.TAINTED_SKELETON_WALL.get(), taintedSkeletonWall);
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_SKELETON_WALL.get(), taintedSkeletonWall);
      ModelFile taintedSkullCeiling = this.models().getExistingFile(WitherStormModBlocks.TAINTED_SKULL_CEILING.getId());
      this.horizontalBlock((Block)WitherStormModBlocks.TAINTED_SKULL_CEILING.get(), taintedSkullCeiling);
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_SKULL_CEILING.get(), taintedSkullCeiling);
      ModelFile witheredPhlegmBlock = this.models().getExistingFile(WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.getId());
      this.simpleBlockItem((Block)WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.get(), witheredPhlegmBlock);
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.TAINTED_DIRT.get(),
         this.models().cubeAll(WitherStormModBlocks.TAINTED_DIRT.getId().getPath(), this.modLoc("block/tainted_dirt"))
      );
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.TAINTED_SANDSTONE.get(),
         this.models()
            .cubeBottomTop(
               WitherStormModBlocks.TAINTED_SANDSTONE.getId().getPath(),
               this.modLoc("block/tainted_sandstone"),
               this.modLoc("block/tainted_sandstone_bottom"),
               this.modLoc("block/tainted_sandstone_top")
            )
      );
      this.stairsBlock(
         (StairBlock)WitherStormModBlocks.TAINTED_SANDSTONE_STAIRS.get(),
         this.modLoc("block/tainted_sandstone"),
         this.modLoc("block/tainted_sandstone_bottom"),
         this.modLoc("block/tainted_sandstone_top")
      );
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.TAINTED_SANDSTONE_STAIRS.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_SANDSTONE_STAIRS.getId())
      );
      this.slabBlock(
         (SlabBlock)WitherStormModBlocks.TAINTED_SANDSTONE_SLAB.get(),
         WitherStormModBlocks.TAINTED_SANDSTONE_SLAB.getId(),
         this.modLoc("block/tainted_sandstone"),
         this.modLoc("block/tainted_sandstone_bottom"),
         this.modLoc("block/tainted_sandstone_top")
      );
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.TAINTED_SANDSTONE_SLAB.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_SANDSTONE_SLAB.getId())
      );
      this.wallBlock((WallBlock)WitherStormModBlocks.TAINTED_SANDSTONE_WALL.get(), this.modLoc("block/tainted_sandstone"));
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE.get(),
         this.models()
            .cubeBottomTop(
               WitherStormModBlocks.TAINTED_CUT_SANDSTONE.getId().getPath(),
               this.modLoc("block/tainted_cut_sandstone"),
               this.modLoc("block/tainted_sandstone_bottom"),
               this.modLoc("block/tainted_sandstone_top")
            )
      );
      this.slabBlock(
         (SlabBlock)WitherStormModBlocks.TAINTED_CUT_SANDSTONE_SLAB.get(),
         WitherStormModBlocks.TAINTED_CUT_SANDSTONE.getId(),
         this.modLoc("block/tainted_cut_sandstone"),
         this.modLoc("block/tainted_sandstone_bottom"),
         this.modLoc("block/tainted_sandstone_top")
      );
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE_SLAB.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_CUT_SANDSTONE_SLAB.getId())
      );
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.TAINTED_CHISELED_SANDSTONE.get(),
         this.models()
            .cubeBottomTop(
               WitherStormModBlocks.TAINTED_CHISELED_SANDSTONE.getId().getPath(),
               this.modLoc("block/tainted_chiseled_sandstone"),
               this.modLoc("block/tainted_sandstone_bottom"),
               this.modLoc("block/tainted_sandstone_top")
            )
      );
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE.get(),
         this.models().cubeAll(WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE.getId().getPath(), this.modLoc("block/tainted_sandstone_top"))
      );
      this.stairsBlock(
         (StairBlock)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_STAIRS.get(),
         this.modLoc("block/tainted_sandstone_top"),
         this.modLoc("block/tainted_sandstone_bottom"),
         this.modLoc("block/tainted_sandstone_top")
      );
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_STAIRS.get(),
         this.models().getExistingFile(WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_STAIRS.getId())
      );
      this.slabBlock(
         (SlabBlock)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_SLAB.get(),
         WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE.getId(),
         this.modLoc("block/tainted_sandstone_top")
      );
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_SLAB.get(),
         this.models().getExistingFile(WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_SLAB.getId())
      );
      this.wallBlock((WallBlock)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_WALL.get(), this.modLoc("block/tainted_sandstone_top"));
      this.torch((Block)WitherStormModBlocks.TAINTED_TORCH.get(), this.modLoc("block/tainted_torch"));
      this.wallTorch((Block)WitherStormModBlocks.TAINTED_WALL_TORCH.get(), this.modLoc("block/tainted_torch"));
      ModelFile taintedFleshVeins = this.models().getExistingFile(WitherStormModBlocks.TAINTED_FLESH_VEINS.getId());
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_FLESH_VEINS.get(), taintedFleshVeins);
      ModelFile flesh_block = this.models().cubeAll(WitherStormModBlocks.TAINTED_FLESH_BLOCK.getId().getPath(), this.modLoc("block/tainted_flesh_block"));
      this.getVariantBuilder((Block)WitherStormModBlocks.TAINTED_FLESH_BLOCK.get())
         .partialState()
         .modelForState()
         .modelFile(flesh_block)
         .nextModel()
         .modelFile(flesh_block)
         .rotationX(90)
         .nextModel()
         .modelFile(flesh_block)
         .rotationX(180)
         .nextModel()
         .modelFile(flesh_block)
         .rotationX(270)
         .nextModel()
         .modelFile(flesh_block)
         .rotationY(90)
         .nextModel()
         .modelFile(flesh_block)
         .rotationY(180)
         .nextModel()
         .modelFile(flesh_block)
         .rotationY(270)
         .addModel();
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_FLESH_BLOCK.get(), flesh_block);
      ModelFile infected_flesh_block = this.models()
         .cubeAll(WitherStormModBlocks.INFECTED_FLESH_BLOCK.getId().getPath(), this.modLoc("block/infected_flesh_block"));
      this.getVariantBuilder((Block)WitherStormModBlocks.INFECTED_FLESH_BLOCK.get())
         .partialState()
         .modelForState()
         .modelFile(infected_flesh_block)
         .nextModel()
         .modelFile(infected_flesh_block)
         .rotationX(90)
         .nextModel()
         .modelFile(infected_flesh_block)
         .rotationX(180)
         .nextModel()
         .modelFile(infected_flesh_block)
         .rotationX(270)
         .nextModel()
         .modelFile(infected_flesh_block)
         .rotationY(90)
         .nextModel()
         .modelFile(infected_flesh_block)
         .rotationY(180)
         .nextModel()
         .modelFile(infected_flesh_block)
         .rotationY(270)
         .addModel();
      this.simpleBlockItem((Block)WitherStormModBlocks.INFECTED_FLESH_BLOCK.get(), infected_flesh_block);
      ModelFile hardened_flesh_block = this.models()
         .cubeAll(WitherStormModBlocks.HARDENED_FLESH_BLOCK.getId().getPath(), this.modLoc("block/hardened_flesh_block"));
      this.getVariantBuilder((Block)WitherStormModBlocks.HARDENED_FLESH_BLOCK.get())
         .partialState()
         .modelForState()
         .modelFile(hardened_flesh_block)
         .nextModel()
         .modelFile(hardened_flesh_block)
         .rotationX(90)
         .nextModel()
         .modelFile(hardened_flesh_block)
         .rotationX(180)
         .nextModel()
         .modelFile(hardened_flesh_block)
         .rotationX(270)
         .nextModel()
         .modelFile(hardened_flesh_block)
         .rotationY(90)
         .nextModel()
         .modelFile(hardened_flesh_block)
         .rotationY(180)
         .nextModel()
         .modelFile(hardened_flesh_block)
         .rotationY(270)
         .addModel();
      this.simpleBlockItem((Block)WitherStormModBlocks.HARDENED_FLESH_BLOCK.get(), hardened_flesh_block);
      ModelFile stone = this.models().cubeAll(WitherStormModBlocks.TAINTED_STONE.getId().getPath(), this.modLoc("block/tainted_stone"));
      this.getVariantBuilder((Block)WitherStormModBlocks.TAINTED_STONE.get())
         .partialState()
         .modelForState()
         .modelFile(stone)
         .nextModel()
         .modelFile(stone)
         .rotationY(180)
         .addModel();
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_STONE.get(), stone);
      this.stairsBlock((StairBlock)WitherStormModBlocks.TAINTED_STONE_STAIRS.get(), this.modLoc("block/tainted_stone"));
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.TAINTED_STONE_STAIRS.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_STONE_STAIRS.getId())
      );
      this.slabBlock((SlabBlock)WitherStormModBlocks.TAINTED_STONE_SLAB.get(), WitherStormModBlocks.TAINTED_STONE.getId(), this.modLoc("block/tainted_stone"));
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_STONE_SLAB.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_STONE_SLAB.getId()));
      this.buttonBlock((ButtonBlock)WitherStormModBlocks.TAINTED_STONE_BUTTON.get(), this.modLoc("block/tainted_stone"));
      this.pressurePlateBlock((PressurePlateBlock)WitherStormModBlocks.TAINTED_STONE_PRESSURE_PLATE.get(), this.modLoc("block/tainted_stone"));
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.TAINTED_STONE_PRESSURE_PLATE.get(),
         this.models().getExistingFile(WitherStormModBlocks.TAINTED_STONE_PRESSURE_PLATE.getId())
      );
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.TAINTED_COBBLESTONE.get(),
         this.models().cubeAll(WitherStormModBlocks.TAINTED_COBBLESTONE.getId().getPath(), this.modLoc("block/tainted_cobblestone"))
      );
      this.stairsBlock((StairBlock)WitherStormModBlocks.TAINTED_COBBLESTONE_STAIRS.get(), this.modLoc("block/tainted_cobblestone"));
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.TAINTED_COBBLESTONE_STAIRS.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_COBBLESTONE_STAIRS.getId())
      );
      this.slabBlock(
         (SlabBlock)WitherStormModBlocks.TAINTED_COBBLESTONE_SLAB.get(),
         WitherStormModBlocks.TAINTED_COBBLESTONE.getId(),
         this.modLoc("block/tainted_cobblestone")
      );
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.TAINTED_COBBLESTONE_SLAB.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_COBBLESTONE_SLAB.getId())
      );
      this.wallBlock((WallBlock)WitherStormModBlocks.TAINTED_COBBLESTONE_WALL.get(), this.modLoc("block/tainted_cobblestone"));
      ModelFile sand = this.models().cubeAll(WitherStormModBlocks.TAINTED_SAND.getId().getPath(), this.modLoc("block/tainted_sand"));
      this.getVariantBuilder((Block)WitherStormModBlocks.TAINTED_SAND.get())
         .partialState()
         .modelForState()
         .modelFile(sand)
         .nextModel()
         .modelFile(sand)
         .rotationY(90)
         .nextModel()
         .modelFile(sand)
         .rotationY(180)
         .nextModel()
         .modelFile(sand)
         .rotationY(270)
         .addModel();
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_SAND.get(), sand);
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.TAINTED_GLASS.get(),
         ((BlockModelBuilder)this.models().cubeAll(WitherStormModBlocks.TAINTED_GLASS.getId().getPath(), this.modLoc("block/tainted_glass")))
            .renderType("translucent")
      );
      this.paneBlockWithRenderType(
         (IronBarsBlock)WitherStormModBlocks.TAINTED_GLASS_PANE.get(),
         this.modLoc("block/tainted_glass"),
         this.modLoc("block/tainted_glass_pane_top"),
         "translucent"
      );
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.TAINTED_PLANKS.get(),
         this.models().cubeAll(WitherStormModBlocks.TAINTED_PLANKS.getId().getPath(), this.modLoc("block/tainted_planks"))
      );
      this.axisBlock((RotatedPillarBlock)WitherStormModBlocks.TAINTED_LOG.get(), this.modLoc("block/tainted_log"), this.modLoc("block/tainted_log_top"));
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_LOG.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_LOG.getId()));
      this.axisBlock((RotatedPillarBlock)WitherStormModBlocks.TAINTED_WOOD.get(), this.modLoc("block/tainted_log"), this.modLoc("block/tainted_log"));
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_WOOD.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_WOOD.getId()));
      this.axisBlock(
         (RotatedPillarBlock)WitherStormModBlocks.STRIPPED_TAINTED_LOG.get(),
         this.modLoc("block/stripped_tainted_log"),
         this.modLoc("block/stripped_tainted_log_top")
      );
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.STRIPPED_TAINTED_LOG.get(), this.models().getExistingFile(WitherStormModBlocks.STRIPPED_TAINTED_LOG.getId())
      );
      this.axisBlock(
         (RotatedPillarBlock)WitherStormModBlocks.STRIPPED_TAINTED_WOOD.get(),
         this.modLoc("block/stripped_tainted_log"),
         this.modLoc("block/stripped_tainted_log")
      );
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.STRIPPED_TAINTED_WOOD.get(), this.models().getExistingFile(WitherStormModBlocks.STRIPPED_TAINTED_WOOD.getId())
      );
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.TAINTED_LEAVES.get(),
         ((BlockModelBuilder)((BlockModelBuilder)this.models().withExistingParent(WitherStormModBlocks.TAINTED_LEAVES.getId().getPath(), "block/leaves"))
               .texture("all", this.modLoc("block/tainted_leaves")))
            .renderType("cutout")
      );
      this.doorBlock((DoorBlock)WitherStormModBlocks.TAINTED_DOOR.get(), this.modLoc("block/tainted_door_bottom"), this.modLoc("block/tainted_door_top"));
      this.trapdoorBlockWithRenderType((TrapDoorBlock)WitherStormModBlocks.TAINTED_TRAPDOOR.get(), this.modLoc("block/tainted_trapdoor"), true, "cutout");
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.TAINTED_TRAPDOOR.get(),
         this.models().getExistingFile(this.extend(WitherStormModBlocks.TAINTED_TRAPDOOR.getId(), "_bottom"))
      );
      this.buttonBlock((ButtonBlock)WitherStormModBlocks.TAINTED_BUTTON.get(), this.modLoc("block/tainted_planks"));
      this.pressurePlateBlock((PressurePlateBlock)WitherStormModBlocks.TAINTED_PRESSURE_PLATE.get(), this.modLoc("block/tainted_planks"));
      this.simpleBlockItem(
         (Block)WitherStormModBlocks.TAINTED_PRESSURE_PLATE.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_PRESSURE_PLATE.getId())
      );
      this.stairsBlock((StairBlock)WitherStormModBlocks.TAINTED_STAIRS.get(), this.modLoc("block/tainted_planks"));
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_STAIRS.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_STAIRS.getId()));
      this.slabBlock((SlabBlock)WitherStormModBlocks.TAINTED_SLAB.get(), WitherStormModBlocks.TAINTED_PLANKS.getId(), this.modLoc("block/tainted_planks"));
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_SLAB.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_SLAB.getId()));
      this.fenceBlock((FenceBlock)WitherStormModBlocks.TAINTED_FENCE.get(), this.modLoc("block/tainted_planks"));
      this.fenceGateBlock((FenceGateBlock)WitherStormModBlocks.TAINTED_FENCE_GATE.get(), this.modLoc("block/tainted_planks"));
      this.simpleBlockItem((Block)WitherStormModBlocks.TAINTED_FENCE_GATE.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_FENCE_GATE.getId()));
      this.simpleBlock((Block)WitherStormModBlocks.TAINTED_MUSHROOM.get(), this.models().getExistingFile(WitherStormModBlocks.TAINTED_MUSHROOM.getId()));
      this.simpleBlock(
         (Block)WitherStormModBlocks.POTTED_TAINTED_MUSHROOM.get(), this.models().getExistingFile(WitherStormModBlocks.POTTED_TAINTED_MUSHROOM.getId())
      );
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.TAINTED_PUMPKIN.get(),
         this.models()
            .cubeColumn(
               WitherStormModBlocks.TAINTED_PUMPKIN.getId().getPath(), this.modLoc("block/tainted_pumpkin_side"), this.modLoc("block/tainted_pumpkin_top")
            )
      );
      this.pumpkin(
         (Block)WitherStormModBlocks.TAINTED_CARVED_PUMPKIN.get(),
         this.modLoc("block/tainted_pumpkin_side"),
         this.modLoc("block/tainted_carved_pumpkin"),
         this.modLoc("block/tainted_pumpkin_top")
      );
      this.pumpkin(
         (Block)WitherStormModBlocks.TAINTED_JACK_O_LANTERN.get(),
         this.modLoc("block/tainted_pumpkin_side"),
         this.modLoc("block/tainted_jack_o_lantern"),
         this.modLoc("block/tainted_pumpkin_top")
      );
      this.simpleBlockWithItem(
         (Block)WitherStormModBlocks.TAINTED_DUST_BLOCK.get(),
         this.models().cubeAll(WitherStormModBlocks.TAINTED_DUST_BLOCK.getId().getPath(), this.modLoc("block/tainted_dust_block"))
      );
      this.signBlock(
         (StandingSignBlock)WitherStormModBlocks.TAINTED_SIGN.get(),
         (WallSignBlock)WitherStormModBlocks.TAINTED_WALL_SIGN.get(),
         this.modLoc("block/tainted_planks")
      );
   }

   private ResourceLocation extend(ResourceLocation rl, String suffix) {
      return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
   }

   private void pumpkin(Block block, ResourceLocation side, ResourceLocation front, ResourceLocation top) {
      ModelFile file = this.models().orientable(ForgeRegistries.BLOCKS.getKey(block).getPath(), side, front, top);
      this.horizontalBlock(block, file);
      this.simpleBlockItem(block, file);
   }

   private void torch(Block block, ResourceLocation torch) {
      ModelFile file = ((BlockModelBuilder)this.models().torch(ForgeRegistries.BLOCKS.getKey(block).getPath(), torch)).renderType("cutout");
      this.simpleBlock(block, file);
   }

   private void wallTorch(Block block, ResourceLocation torch) {
      ModelFile file = ((BlockModelBuilder)this.models().torchWall(ForgeRegistries.BLOCKS.getKey(block).getPath(), torch)).renderType("cutout");
      this.horizontalBlock(block, file, 90);
   }
}
