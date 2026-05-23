package nonamecrackers2.witherstormmod.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlockEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;

public class ClusterBuilderHelper {
   private static final ResourceLocation BOWELS_LOOT = ResourceLocation.fromNamespaceAndPath("witherstormmod", "chests/bowels_general");
   private static final SimpleWeightedRandomList<Block> OUTSIDE = SimpleWeightedRandomList.<Block>builder()
      .add(WitherStormModBlocks.TAINTED_FLESH_BLOCK.get(), 20)
      .add(WitherStormModBlocks.INFECTED_FLESH_BLOCK.get(), 3)
      .add(WitherStormModBlocks.TAINTED_PLANKS.get(), 1)
      .add(WitherStormModBlocks.TAINTED_COBBLESTONE.get(), 1)
      .build();
   private static final SimpleWeightedRandomList<Block> BLOCKS = SimpleWeightedRandomList.<Block>builder()
      .add(WitherStormModBlocks.TAINTED_FLESH_BLOCK.get(), 20)
      .add(WitherStormModBlocks.TAINTED_SAND.get(), 15)
      .add(WitherStormModBlocks.TAINTED_DIRT.get(), 10)
      .add(WitherStormModBlocks.TAINTED_COBBLESTONE.get(), 10)
      .add(WitherStormModBlocks.TAINTED_STONE.get(), 10)
      .add(WitherStormModBlocks.TAINTED_PLANKS.get(), 8)
      .add(WitherStormModBlocks.TAINTED_LOG.get(), 5)
      .add(WitherStormModBlocks.TAINTED_LEAVES.get(), 2)
      .add(WitherStormModBlocks.INFECTED_FLESH_BLOCK.get(), 5)
      .build();
   private static final SimpleWeightedRandomList<Block> JUNK = SimpleWeightedRandomList.<Block>builder()
      .add(WitherStormModBlocks.TAINTED_FLESH_BLOCK.get(), 8)
      .add(WitherStormModBlocks.TAINTED_SAND.get(), 5)
      .add(WitherStormModBlocks.TAINTED_SANDSTONE.get(), 3)
      .add(WitherStormModBlocks.TAINTED_DIRT.get(), 4)
      .add(WitherStormModBlocks.TAINTED_COBBLESTONE.get(), 6)
      .add(WitherStormModBlocks.TAINTED_STONE.get(), 6)
      .add(WitherStormModBlocks.TAINTED_DUST_BLOCK.get(), 2)
      .add(WitherStormModBlocks.TAINTED_GLASS.get(), 2)
      .add(WitherStormModBlocks.TAINTED_PUMPKIN.get(), 1)
      .add(WitherStormModBlocks.TAINTED_ZOMBIE_LYING.get(), 1)
      .add(WitherStormModBlocks.TAINTED_BONE_PILE.get(), 1)
      .add(WitherStormModBlocks.TAINTED_WOOD.get(), 4)
      .add(WitherStormModBlocks.TAINTED_PLANKS.get(), 6)
      .add(WitherStormModBlocks.TAINTED_LOG.get(), 6)
      .add(WitherStormModBlocks.TAINTED_LEAVES.get(), 4)
      .build();
   private static final SimpleWeightedRandomList<Block> DECORATION = SimpleWeightedRandomList.<Block>builder()
      .add(WitherStormModBlocks.TAINTED_DUST.get(), 10)
      .add(WitherStormModBlocks.TAINTED_MUSHROOM.get(), 5)
      .add(WitherStormModBlocks.TAINTED_BONE_PILE.get(), 1)
      .add(WitherStormModBlocks.TAINTED_ZOMBIE_SITTING.get(), 1)
      .build();
   private static final SimpleWeightedRandomList<Block> CHEWED = SimpleWeightedRandomList.<Block>builder()
      .add(WitherStormModBlocks.TAINTED_FLESH_BLOCK.get(), 10)
      .add(WitherStormModBlocks.INFECTED_FLESH_BLOCK.get(), 10)
      .add(WitherStormModBlocks.TAINTED_ZOMBIE_LYING.get(), 2)
      .add(WitherStormModBlocks.TAINTED_BONE_PILE.get(), 2)
      .build();

   public static BlockClusterEntity buildSmallRandomDeathCluster(Level level, RandomSource random, int radius) {
      Map<BlockPos, BlockState> states = Maps.newLinkedHashMap();

      for (int i = 0; i <= 3; i++) {
         BlockPos offset = new BlockPos(random.nextInt(radius) - radius, random.nextInt(radius) - radius, random.nextInt(radius) - radius);

         for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
               for (int z = -radius; z < radius; z++) {
                  int sqrt = Mth.floor(Mth.sqrt((float)(x * x) + 1.0F + (float)(y * y) + 1.0F + (float)(z * z) + 1.0F));
                  if (sqrt <= radius) {
                     BlockPos pos = new BlockPos(x, y, z).offset(offset);
                     if (!states.containsKey(pos)) {
                        BlockState state;
                        if (random.nextFloat() < 0.975F) {
                           state = JUNK.getRandomValue(random).orElse(Blocks.AIR).defaultBlockState();
                        } else {
                           state = (WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.get()).defaultBlockState();
                        }

                        states.put(pos, state);
                     }
                  }
               }
            }
         }
      }

      BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(level);

      assert cluster != null;

      cluster.populate(states);

      for (Entry<BlockPos, BlockState> entry : states.entrySet()) {
         BlockState state = entry.getValue();
         if (state.is(WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.get())) {
            BlockPos pos = entry.getKey().offset(cluster.getStartPos());
            CompoundTag tileData = new CompoundTag();
            BlockEntity.addEntityType(tileData, (BlockEntityType)WitherStormModBlockEntityTypes.WITHERED_PHLEGM.get());
            tileData.putInt("x", pos.getX());
            tileData.putInt("y", pos.getY());
            tileData.putInt("z", pos.getZ());
            tileData.putString("LootTable", BOWELS_LOOT.toString());
            cluster.addTileData(tileData);
         }
      }

      return cluster;
   }

   public static BlockClusterEntity buildRandomDeathCluster(Level level, RandomSource random, int radius) {
      Map<BlockPos, BlockState> states = Maps.newLinkedHashMap();
      radius += random.nextInt(2);
      float stretch = 3.0F;

      for (int i = 0; i <= 3; i++) {
         float xStretch = stretch * Math.max(0.5F, random.nextFloat());
         float yStretch = stretch;
         float zStretch = stretch * Math.max(0.5F, random.nextFloat());
         int offsetR = radius - 1;
         BlockPos offset = new BlockPos(random.nextInt(offsetR * 2) - offsetR, random.nextInt(radius * 2) - radius, random.nextInt(radius * 2) - radius);

         for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
               for (int z = -radius; z < radius; z++) {
                  int sqrt = Mth.floor(Mth.sqrt((float)(x * x) * xStretch + 1.0F + (float)(y * y) * yStretch + 1.0F + (float)(z * z) * zStretch + 1.0F));
                  if (sqrt <= radius) {
                     SimpleWeightedRandomList<Block> list = OUTSIDE;
                     if (sqrt < radius - 1) {
                        list = BLOCKS;
                     }

                     int randomY = random.nextInt(2);

                     for (int l = -randomY; l <= randomY; l++) {
                        BlockPos pos = new BlockPos(x, y - l, z).offset(offset);
                        if (!states.containsKey(pos)) {
                           BlockState state;
                           if (random.nextFloat() < 0.999F) {
                              state = list.getRandomValue(random).orElse(Blocks.AIR).defaultBlockState();
                           } else {
                              state = (WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.get()).defaultBlockState();
                           }

                           states.put(pos, state);
                        }
                     }
                  }
               }
            }
         }
      }

      Map<BlockPos, BlockState> toAdd = Maps.newHashMap();

      for (Entry<BlockPos, BlockState> entry : states.entrySet()) {
         BlockPos above = entry.getKey().above();
         if (!states.containsKey(above) && random.nextInt(3) == 0) {
            BlockState decoration = DECORATION.getRandomValue(random).orElse(Blocks.AIR).defaultBlockState();
            toAdd.put(above, decoration);
         }
      }

      BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(level);
      toAdd.forEach(states::put);
      cluster.populate(states);

      for (Entry<BlockPos, BlockState> entryx : states.entrySet()) {
         BlockState state = entryx.getValue();
         if (state.is(WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.get())) {
            BlockPos pos = entryx.getKey().offset(cluster.getStartPos());
            CompoundTag tileData = new CompoundTag();
            BlockEntity.addEntityType(tileData, (BlockEntityType)WitherStormModBlockEntityTypes.WITHERED_PHLEGM.get());
            tileData.putInt("x", pos.getX());
            tileData.putInt("y", pos.getY());
            tileData.putInt("z", pos.getZ());
            tileData.putString("LootTable", BOWELS_LOOT.toString());
            cluster.addTileData(tileData);
         }
      }

      return cluster;
   }

   public static BlockClusterEntity buildPhlegmClusterWithItems(
      Level level, RandomSource random, List<ItemStack> items, @Nullable Component name, int experience
   ) {
      int size = items.size();
      List<ListTag> seperatedItemTags = Lists.newArrayList();
      int currentSlot = 0;
      ListTag currentListTag = new ListTag();

      for (int i = 0; i < size; i++) {
         ItemStack stack = items.get(i);
         if (!stack.isEmpty()) {
            if (currentSlot >= 25) {
               currentSlot -= 25;
               seperatedItemTags.add(currentListTag);
               currentListTag = new ListTag();
            }

            CompoundTag tag = new CompoundTag();
            tag.putByte("Slot", (byte)currentSlot);
            stack.copy().save(tag);
            currentListTag.add(tag);
            currentSlot += random.nextIntBetweenInclusive(1, 3);
         }
      }

      seperatedItemTags.add(currentListTag);
      int experiencePerPhlegm = experience / seperatedItemTags.size();
      List<CompoundTag> tagsPerPhlegmBlock = seperatedItemTags.stream().filter(Predicate.not(ListTag::isEmpty)).map(l -> {
         CompoundTag tagx = new CompoundTag();
         tagx.put("Items", l);
         tagx.putInt("StoredXp", experiencePerPhlegm);
         return tagx;
      }).toList();
      BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(level);
      List<BlockPos> availablePositions = Lists.newArrayList();
      int radius = 1;

      for (int x = -radius; x <= radius; x++) {
         for (int y = -radius; y <= radius; y++) {
            for (int z = -radius; z <= radius; z++) {
               availablePositions.add(new BlockPos(x, y, z));
            }
         }
      }

      Map<BlockPos, CompoundTag> phlegmBlocks = Maps.newHashMap();

      for (int ix = 0; ix < tagsPerPhlegmBlock.size(); ix++) {
         if (ix < availablePositions.size()) {
            int index = 0;
            if (availablePositions.size() > 0) {
               index = random.nextInt(availablePositions.size());
            }

            BlockPos pos = availablePositions.get(index);
            availablePositions.remove(index);
            CompoundTag tag = tagsPerPhlegmBlock.get(ix);
            phlegmBlocks.put(pos, tag);
         }
      }

      Map<BlockPos, BlockState> blocks = phlegmBlocks.entrySet()
         .stream()
         .map(e -> Map.entry(e.getKey(), (WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.get()).defaultBlockState()))
         .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

      for (BlockPos leftOver : availablePositions) {
         if (random.nextFloat() > 0.6F) {
            blocks.put(leftOver, (CHEWED.getRandomValue(random).get()).defaultBlockState());
         }
      }

      cluster.populate(blocks);

      for (Entry<BlockPos, CompoundTag> entry : phlegmBlocks.entrySet()) {
         CompoundTag tag = entry.getValue();
         BlockPos pos = entry.getKey().offset(cluster.getStartPos());
         BlockEntity.addEntityType(tag, (BlockEntityType)WitherStormModBlockEntityTypes.WITHERED_PHLEGM.get());
         tag.putInt("x", pos.getX());
         tag.putInt("y", pos.getY());
         tag.putInt("z", pos.getZ());
         if (name != null) {
            tag.putString("CustomName", Serializer.toJson(name));
         }

         cluster.addTileData(tag);
      }

      return cluster;
   }
}
