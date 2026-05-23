package nonamecrackers2.witherstormmod.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class WitherStormModNBTUtil {
   public static ListTag writeBlockStatePosMap(Map<BlockPos, BlockState> map) {
      ListTag list = new ListTag();

      for (Entry<BlockPos, BlockState> entry : map.entrySet()) {
         CompoundTag stateCompound = NbtUtils.writeBlockState(entry.getValue());
         stateCompound.put("RelativePos", NbtUtils.writeBlockPos(entry.getKey()));
         list.add(stateCompound);
      }

      return list;
   }

   public static ListTag writeCompoundList(List<CompoundTag> compounds) {
      ListTag list = new ListTag();

      for (int i = 0; i < compounds.size(); i++) {
         list.add((Tag)compounds.get(i));
      }

      return list;
   }

   public static CompoundTag writeChunkPosList(List<ChunkPos> chunks) {
      CompoundTag compound = new CompoundTag();

      for (int i = 0; i < chunks.size(); i++) {
         CompoundTag chunkCompound = new CompoundTag();
         ChunkPos pos = chunks.get(i);
         chunkCompound.putInt("x", pos.x);
         chunkCompound.putInt("z", pos.z);
         compound.put("Chunk" + i, chunkCompound);
      }

      return compound;
   }

   public static Map<BlockPos, BlockState> readBlockStatePosMap(HolderGetter<Block> getter, ListTag list) {
      Map<BlockPos, BlockState> blocks = new HashMap<>();

      for (int i = 0; i < list.size(); i++) {
         CompoundTag stateCompound = list.getCompound(i);
         BlockState state = NbtUtils.readBlockState(getter, stateCompound);
         BlockPos pos = NbtUtils.readBlockPos(stateCompound.getCompound("RelativePos"));
         blocks.put(pos, state);
      }

      return blocks;
   }

   public static List<CompoundTag> readCompoundList(ListTag list) {
      List<CompoundTag> compounds = new ArrayList<>();

      for (int i = 0; i < list.size(); i++) {
         compounds.add(list.getCompound(i));
      }

      return compounds;
   }

   public static List<ChunkPos> readChunkPosList(CompoundTag compound) {
      List<ChunkPos> list = new ArrayList<>();

      for (int i = 0; i < compound.getAllKeys().size(); i++) {
         if (compound.contains("Chunk" + i)) {
            CompoundTag chunkCompound = compound.getCompound("Chunk" + i);
            ChunkPos pos = new ChunkPos(chunkCompound.getInt("x"), chunkCompound.getInt("z"));
            list.add(pos);
         }
      }

      return list;
   }

   public static CompoundTag writeVector2f(Vec2 vector) {
      CompoundTag compound = new CompoundTag();
      compound.putFloat("x", vector.x);
      compound.putFloat("y", vector.y);
      return compound;
   }

   public static Vec2 readVector2f(CompoundTag compound) {
      return new Vec2(compound.getFloat("x"), compound.getFloat("y"));
   }

   public static CompoundTag writeChunkPos(ChunkPos pos) {
      CompoundTag compound = new CompoundTag();
      compound.putInt("x", pos.x);
      compound.putInt("z", pos.z);
      return compound;
   }

   public static ChunkPos readChunkPos(CompoundTag compound) {
      return new ChunkPos(compound.getInt("x"), compound.getInt("z"));
   }

   public static CompoundTag writeVector3d(Vec3 vector) {
      CompoundTag compound = new CompoundTag();
      compound.putDouble("X", vector.x());
      compound.putDouble("Y", vector.y());
      compound.putDouble("Z", vector.z());
      return compound;
   }

   public static Vec3 readVector3d(CompoundTag compound) {
      return new Vec3(compound.getDouble("X"), compound.getDouble("Y"), compound.getDouble("Z"));
   }
}
