package nonamecrackers2.witherstormmod.common.data;

import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import nonamecrackers2.witherstormmod.api.common.data.BlockTaintingRecipeProvider;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;

public class WitherStormModBlockTaintingRecipeProvider extends BlockTaintingRecipeProvider {
   public WitherStormModBlockTaintingRecipeProvider(PackOutput output) {
      super(output, "witherstormmod");
   }

   @Override
   protected void addRecipes() {
      this.addAndCopyAllProperties(Blocks.CARVED_PUMPKIN, MobEffects.WITHER, (Block)WitherStormModBlocks.TAINTED_CARVED_PUMPKIN.get());
      this.addAndCopyAllProperties(Blocks.JACK_O_LANTERN, MobEffects.WITHER, (Block)WitherStormModBlocks.TAINTED_JACK_O_LANTERN.get());
      this.add(Blocks.PUMPKIN, MobEffects.WITHER, ((Block)WitherStormModBlocks.TAINTED_PUMPKIN.get()).defaultBlockState());
      this.addAndCopyAllProperties(Blocks.BROWN_MUSHROOM, (Block)WitherStormModBlocks.TAINTED_MUSHROOM.get());
      this.addAndCopyAllProperties(Blocks.RED_MUSHROOM, (Block)WitherStormModBlocks.TAINTED_MUSHROOM.get());
      this.addAndCopyAllProperties(Blocks.REDSTONE_WIRE, (Block)WitherStormModBlocks.TAINTED_DUST.get());
      this.addAndCopyAllProperties(BlockTags.SMALL_FLOWERS, Blocks.WITHER_ROSE);
      this.addAndCopyAllProperties(BlockTags.BASE_STONE_OVERWORLD, (Block)WitherStormModBlocks.TAINTED_STONE.get());
      this.addAndCopyAllProperties(Blocks.STONE_STAIRS, (Block)WitherStormModBlocks.TAINTED_STONE_STAIRS.get());
      this.addAndCopyAllProperties(Blocks.STONE_SLAB, (Block)WitherStormModBlocks.TAINTED_STONE_SLAB.get());
      this.addAndCopyAllProperties(Blocks.STONE_BUTTON, (Block)WitherStormModBlocks.TAINTED_STONE_BUTTON.get());
      this.addAndCopyAllProperties(Blocks.STONE_PRESSURE_PLATE, (Block)WitherStormModBlocks.TAINTED_STONE_PRESSURE_PLATE.get());
      this.addAndCopyAllProperties(net.minecraftforge.common.Tags.Blocks.COBBLESTONE, (Block)WitherStormModBlocks.TAINTED_COBBLESTONE.get());
      this.addAndCopyAllProperties(Blocks.COBBLESTONE_STAIRS, (Block)WitherStormModBlocks.TAINTED_COBBLESTONE_STAIRS.get());
      this.addAndCopyAllProperties(Blocks.COBBLESTONE_SLAB, (Block)WitherStormModBlocks.TAINTED_COBBLESTONE_SLAB.get());
      this.addAndCopyAllProperties(BlockTags.WALLS, (Block)WitherStormModBlocks.TAINTED_COBBLESTONE_WALL.get());
      this.addAndCopyAllProperties(BlockTags.DIRT, (Block)WitherStormModBlocks.TAINTED_DIRT.get());
      this.addAndCopyAllProperties(net.minecraftforge.common.Tags.Blocks.SAND, (Block)WitherStormModBlocks.TAINTED_SAND.get());
      this.addAndCopyAllProperties(net.minecraftforge.common.Tags.Blocks.GLASS, (Block)WitherStormModBlocks.TAINTED_GLASS.get());
      this.addAndCopyAllProperties(net.minecraftforge.common.Tags.Blocks.GLASS_PANES, (Block)WitherStormModBlocks.TAINTED_GLASS_PANE.get());
      this.addAndCopyAllProperties(Blocks.REDSTONE_BLOCK, (Block)WitherStormModBlocks.TAINTED_DUST_BLOCK.get());
      this.addAndCopyAllProperties(BlockTags.PLANKS, (Block)WitherStormModBlocks.TAINTED_PLANKS.get());
      this.addAndCopyAllProperties(BlockTags.OVERWORLD_NATURAL_LOGS, (Block)WitherStormModBlocks.TAINTED_LOG.get());
      this.addAndCopyAllProperties(BlockTags.LEAVES, (Block)WitherStormModBlocks.TAINTED_LEAVES.get());
      this.addAndCopyAllProperties(BlockTags.WOODEN_DOORS, (Block)WitherStormModBlocks.TAINTED_DOOR.get());
      this.addAndCopyAllProperties(BlockTags.WOODEN_TRAPDOORS, (Block)WitherStormModBlocks.TAINTED_TRAPDOOR.get());
      this.addAndCopyAllProperties(BlockTags.WOODEN_BUTTONS, (Block)WitherStormModBlocks.TAINTED_BUTTON.get());
      this.addAndCopyAllProperties(BlockTags.WOODEN_PRESSURE_PLATES, (Block)WitherStormModBlocks.TAINTED_PRESSURE_PLATE.get());
      this.addAndCopyAllProperties(BlockTags.WOODEN_STAIRS, (Block)WitherStormModBlocks.TAINTED_STAIRS.get());
      this.addAndCopyAllProperties(BlockTags.WOODEN_SLABS, (Block)WitherStormModBlocks.TAINTED_SLAB.get());
      this.addAndCopyAllProperties(BlockTags.WOODEN_FENCES, (Block)WitherStormModBlocks.TAINTED_FENCE.get());
      this.addAndCopyAllProperties(BlockTags.FENCE_GATES, (Block)WitherStormModBlocks.TAINTED_FENCE_GATE.get());
      this.addAndCopyAllProperties(net.minecraftforge.common.Tags.Blocks.SANDSTONE, (Block)WitherStormModBlocks.TAINTED_SANDSTONE.get());
      this.addAndCopyAllProperties(Blocks.SANDSTONE_SLAB, (Block)WitherStormModBlocks.TAINTED_SANDSTONE_SLAB.get());
      this.addAndCopyAllProperties(Blocks.SANDSTONE_STAIRS, (Block)WitherStormModBlocks.TAINTED_SANDSTONE_STAIRS.get());
      this.addAndCopyAllProperties(Blocks.SANDSTONE_WALL, (Block)WitherStormModBlocks.TAINTED_SANDSTONE_WALL.get());
      this.addAndCopyAllProperties(Blocks.CUT_SANDSTONE, (Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE.get());
      this.addAndCopyAllProperties(Blocks.CUT_SANDSTONE_SLAB, (Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE_SLAB.get());
      this.addAndCopyAllProperties(Blocks.SMOOTH_SANDSTONE, (Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE.get());
      this.addAndCopyAllProperties(Blocks.SMOOTH_SANDSTONE_SLAB, (Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_SLAB.get());
      this.addAndCopyAllProperties(Blocks.SMOOTH_SANDSTONE_STAIRS, (Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_STAIRS.get());
      this.addAndCopyAllProperties(Blocks.RED_SANDSTONE, (Block)WitherStormModBlocks.TAINTED_SANDSTONE.get());
      this.addAndCopyAllProperties(Blocks.RED_SANDSTONE_SLAB, (Block)WitherStormModBlocks.TAINTED_SANDSTONE_SLAB.get());
      this.addAndCopyAllProperties(Blocks.RED_SANDSTONE_STAIRS, (Block)WitherStormModBlocks.TAINTED_SANDSTONE_STAIRS.get());
      this.addAndCopyAllProperties(Blocks.RED_SANDSTONE_WALL, (Block)WitherStormModBlocks.TAINTED_SANDSTONE_WALL.get());
      this.addAndCopyAllProperties(Blocks.CUT_RED_SANDSTONE, (Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE.get());
      this.addAndCopyAllProperties(Blocks.CUT_RED_SANDSTONE_SLAB, (Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE_SLAB.get());
      this.addAndCopyAllProperties(Blocks.SMOOTH_RED_SANDSTONE, (Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE.get());
      this.addAndCopyAllProperties(Blocks.SMOOTH_RED_SANDSTONE_SLAB, (Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_SLAB.get());
      this.addAndCopyAllProperties(Blocks.SMOOTH_RED_SANDSTONE_STAIRS, (Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_STAIRS.get());
      this.addAndCopyAllProperties(Blocks.TORCH, (Block)WitherStormModBlocks.TAINTED_TORCH.get());
      this.addAndCopyAllProperties(Blocks.WALL_TORCH, (Block)WitherStormModBlocks.TAINTED_WALL_TORCH.get());
   }
}
