package nonamecrackers2.witherstormmod.common.entity;

public interface ChunkLoader {
   int loadRadius();

   default boolean shouldLoad() {
      return true;
   }

   default boolean shouldContinueLoading() {
      return this.shouldLoad() && this.isStillValidForChunkLoading();
   }

   boolean isStillValidForChunkLoading();
}
