package nonamecrackers2.witherstormmod.common.capability;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ChunkLoadingBlockEntities {
   private final ServerLevel level;
   private List<BlockPos> loadingEntities = Lists.newArrayList();

   public ChunkLoadingBlockEntities(ServerLevel level) {
      this.level = level;
   }

   public void tick() {
      Iterator<BlockPos> loadingEntities = this.loadingEntities.iterator();

      while (loadingEntities.hasNext()) {
         BlockPos pos = loadingEntities.next();
         if (this.level.isLoaded(pos)) {
            BlockEntity entity = this.level.getBlockEntity(pos);
            ChunkPos chunk = new ChunkPos(pos);
            if (entity != null && !entity.isRemoved()) {
               if (!this.level.getForcedChunks().contains(chunk.toLong())) {
                  this.level.setChunkForced(chunk.x, chunk.z, true);
               }
            } else {
               this.level.setChunkForced(chunk.x, chunk.z, false);
               loadingEntities.remove();
            }
         }
      }
   }

   public CompoundTag write() {
      CompoundTag tag = new CompoundTag();
      ListTag list = new ListTag();

      for (BlockPos pos : this.loadingEntities) {
         list.add(NbtUtils.writeBlockPos(pos));
      }

      tag.put("LoadedEntities", list);
      return tag;
   }

   public void read(CompoundTag tag) {
      ListTag list = tag.getList("LoadedEntities", 10);
      List<BlockPos> loadingEntities = Lists.newArrayList();

      for (int i = 0; i < list.size(); i++) {
         loadingEntities.add(NbtUtils.readBlockPos(list.getCompound(i)));
      }

      this.loadingEntities = loadingEntities;
   }

   public void add(BlockPos pos) {
      this.loadingEntities.add(pos);
   }

   public void remove(BlockPos pos) {
      this.loadingEntities.remove(pos);
   }
}
