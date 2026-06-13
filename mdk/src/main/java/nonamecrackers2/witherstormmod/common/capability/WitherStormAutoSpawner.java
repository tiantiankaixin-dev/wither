package nonamecrackers2.witherstormmod.common.capability;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModStructureTags;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import nonamecrackers2.witherstormmod.common.world.gen.feature.structure.StormSpawnPlatformStructure;

public class WitherStormAutoSpawner {
   private static final TicketType<BlockPos> INITIAL_SPAWN = TicketType.create("initial_spawn", Vec3i::compareTo, 100);
   private final ServerLevel level;
   private int tickCount;
   private boolean hasSpawnedWitherStorm;

   public WitherStormAutoSpawner(ServerLevel level) {
      this.level = level;
   }

   public void tick() {
      if ((Boolean)WitherStormModConfig.COMMON.autoSpawnWitherStorm.get()) {
         this.tickCount++;
         int maxTime = Math.max((Integer)WitherStormModConfig.COMMON.autoSpawnTime.get() * 60 * 20, 100);
         if (!this.hasSpawnedWitherStorm && this.tickCount > maxTime) {
            this.hasSpawnedWitherStorm = true;
            Pair<BlockPos, StructureStart> structure = WorldUtil.findNearestMapStructure(
               this.level, WitherStormModStructureTags.STORM_SPAWN_PLATFORMS, BlockPos.ZERO, 100, false
            );
            if (structure != null) {
               BlockPos pos = (BlockPos)structure.getFirst();
               this.level.getChunk(pos);
               StructureStart start = (StructureStart)structure.getSecond();

               for (StructurePiece piece : start.getPieces()) {
                  if (piece instanceof StormSpawnPlatformStructure.Piece spawnPiece && spawnPiece.getSpawnPos() != null) {
                     pos = spawnPiece.getSpawnPos();
                     break;
                  }
               }

               this.level.getChunkSource().addRegionTicket(INITIAL_SPAWN, new ChunkPos(pos), 2, pos, true);
               WitherStormEntity storm = (WitherStormEntity)(WitherStormModEntityTypes.WITHER_STORM.get()).create(this.level);
               storm.moveTo((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
               storm.makeInvulnerable();
               storm.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_ACTIVATES.get(), 4.0F, 1.0F);

               for (ServerPlayer player : this.level.getPlayers(EntitySelector.NO_SPECTATORS)) {
                  CriteriaTriggers.SUMMONED_ENTITY.trigger(player, storm);
               }

               this.level.addFreshEntity(storm);
            }
         }
      } else {
         this.hasSpawnedWitherStorm = true;
      }
   }

   public void read(CompoundTag tag) {
      this.tickCount = tag.getInt("TickCount");
      this.hasSpawnedWitherStorm = tag.getBoolean("HasSpawnedWitherStorm");
   }

   public CompoundTag write() {
      CompoundTag tag = new CompoundTag();
      tag.putInt("TickCount", this.tickCount);
      tag.putBoolean("HasSpawnedWitherStorm", this.hasSpawnedWitherStorm);
      return tag;
   }
}
