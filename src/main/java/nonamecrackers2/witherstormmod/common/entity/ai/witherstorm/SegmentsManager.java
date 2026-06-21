package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.WitherStormHead;

public class SegmentsManager {
   protected final WitherStormEntity owner;
   protected final WitherStormSegmentEntity[] segments;

   public SegmentsManager(WitherStormEntity owner) {
      this.owner = owner;
      this.segments = new WitherStormSegmentEntity[2];
   }

   public void createSegments() {
      for (int i = 0; i < this.segments.length; i++) {
         this.createSegment(i);
      }
   }

   public void createSegment(int index) {
      if (this.owner.isAlive()) {
         WitherStormSegmentEntity existing = this.segments[index];
         if (existing == null || existing.getRemovalReason() != null && existing.getRemovalReason().shouldDestroy()) {
            BlockPos lastKnown = BlockPos.containing(
               this.owner.getDesiredSegmentX(index + 1), this.owner.getDesiredSegmentY(index + 1), this.owner.getDesiredSegmentZ(index + 1)
            );
            if (this.posLoaded(lastKnown)) {
               WitherStormSegmentEntity segment = new WitherStormSegmentEntity(this.owner);
               segment.setBaby(this.owner.isBaby());
               segment.setNoAi(this.owner.isNoAi());
               if (this.owner.isPersistenceRequired()) {
                  segment.setPersistenceRequired();
               }

               segment.setInvulnerable(this.owner.isInvulnerable());
               segment.setSilent(this.owner.isSilent());
               segment.setNoGravity(this.owner.isNoGravity());
               segment.setOtherHeadsDisabled(this.owner.areOtherHeadsDisabled());
               segment.setMirrored(index % 2 != 0);
               segment.setPhase(this.owner.getPhase());
               segment.setYBodyRot(this.owner.yBodyRot);
               this.setSegment(segment, index);
            }
         }
      }
   }

   public void removeSegments() {
      for (int i = 0; i < this.segments.length; i++) {
         WitherStormSegmentEntity segment = this.segments[i];
         if (segment != null && !segment.isRemoved()) {
            this.segments[i].getTrackedEntities().clearAndMakeAllFall();
            this.segments[i].discard();
         }
      }
   }

   public void readdSegments() {
      for (int i = 0; i < this.segments.length; i++) {
         WitherStormSegmentEntity segment = this.segments[i];
         if (segment != null && !segment.isAddedToWorld()) {
            if (segment.getRemovalReason() != null) {
               Vec3 desiredPos = new Vec3(
                  this.owner.getDesiredSegmentX(segment.isMirrored() ? 1 : 2),
                  this.owner.getDesiredSegmentY(segment.isMirrored() ? 1 : 2),
                  this.owner.getDesiredSegmentZ(segment.isMirrored() ? 1 : 2)
               );
               if (this.posLoaded(desiredPos)) {
                  segment.revive();
                  segment.regatherCapabilities();
                  segment.setPos(desiredPos);
                  segment.setBaby(this.owner.isBaby());
                  segment.setNoAi(this.owner.isNoAi());
                  if (this.owner.isPersistenceRequired()) {
                     segment.setPersistenceRequired();
                  }

                  segment.setInvulnerable(this.owner.isInvulnerable());
                  segment.setSilent(this.owner.isSilent());
                  segment.setNoGravity(this.owner.isNoGravity());
                  segment.setOtherHeadsDisabled(this.owner.areOtherHeadsDisabled());
                  segment.setMirrored(i % 2 != 0);
                  segment.setPhase(this.owner.getPhase());
                  this.addSegment(i);
               }
            }
         } else if (segment == null || !segment.isAlive()) {
            this.createSegment(i);
            this.addSegment(i);
         }
      }
   }

   public void addSegments() {
      for (int i = 0; i < this.segments.length; i++) {
         this.addSegment(i);
      }
   }

   public void addSegment(int index) {
      if (this.owner.isAddedToWorld() && this.owner.isAlive()) {
         WitherStormSegmentEntity segment = this.segments[index];
         if (segment != null && !segment.isAddedToWorld() && (segment.getRemovalReason() != null && !segment.getRemovalReason().shouldDestroy() || segment.getRemovalReason() == null)) {
            Vec3 desiredPos = new Vec3(
               this.owner.getDesiredSegmentX(segment.isMirrored() ? 1 : 2),
               this.owner.getDesiredSegmentY(segment.isMirrored() ? 1 : 2),
               this.owner.getDesiredSegmentZ(segment.isMirrored() ? 1 : 2)
            );
            if (this.posLoaded(desiredPos)) {
               segment.setPos(desiredPos);
               segment.setYBodyRot(this.owner.yBodyRot);
               this.owner.level().addFreshEntity(segment);
               segment.setPhase(this.owner.getPhase());
               segment.getPlayDeadManager().setStateRaw(this.owner.getPlayDeadManager().getState());
               segment.getPlayDeadManager().setTickAmount(this.owner.getPlayDeadManager().getTicks());

               for (WitherStormHead head : segment.getHeadManager().getHeads()) {
                  if (segment.isPlayingDead()) {
                     head.setRoar(true);
                  } else {
                     head.doRoar(false);
                  }
               }
            }
         }
      }
   }

   public void killSegments() {
      for (int i = 0; i < this.segments.length; i++) {
         WitherStormSegmentEntity segment = this.segments[i];
         if (segment != null && segment.isAlive()) {
            segment.hurt(segment.damageSources().fellOutOfWorld(), Float.MAX_VALUE);
         }
      }
   }

   public WitherStormSegmentEntity[] getSegments() {
      return this.segments;
   }

   public void setSegment(@Nullable WitherStormSegmentEntity entity, int index) {
      this.segments[index] = entity;
   }

   public void findSegments(ServerLevel world) {
      for (int i = 0; i < this.segments.length; i++) {
         WitherStormSegmentEntity old = this.segments[i];
         if (old != null && old.getRemovalReason() != null && !old.getRemovalReason().shouldDestroy()) {
            for (Entity entity : world.getAllEntities()) {
               if (old.getUUID().equals(entity.getUUID()) && entity instanceof WitherStormSegmentEntity segment) {
                  this.setSegment(segment, i);
                  break;
               }
            }
         }
      }

      for (Entity entityx : world.getAllEntities()) {
         if (entityx instanceof WitherStormSegmentEntity) {
            WitherStormSegmentEntity segment = (WitherStormSegmentEntity)entityx;
            UUID parent = segment.getParentUUID();
            if (this.owner.getUUID().equals(parent)) {
               int index = segment.isMirrored() ? 1 : 0;
               WitherStormSegmentEntity existing = this.segments[index];
               if (existing != null) {
                  if (!segment.getUUID().equals(existing.getUUID())) {
                     if (segment.getTimeWithParent() > existing.getTimeWithParent()) {
                        existing.discard();
                        this.setSegment(segment, index);
                     } else if (existing.getTimeWithParent() > segment.getTimeWithParent()) {
                        segment.discard();
                        this.setSegment(existing, index);
                     }
                  }
               } else {
                  this.setSegment(segment, index);
               }
            }
         }
      }
   }

   private boolean posLoaded(Vec3 pos) {
      return this.posLoaded(BlockPos.containing(pos));
   }

   private boolean posLoaded(BlockPos pos) {
      ChunkPos chunkPos = new ChunkPos(pos);
      ChunkAccess chunk = this.owner.level().getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, false);
      if (!(chunk instanceof LevelChunk)) {
         return false;
      } else {
         FullChunkStatus type = ((LevelChunk)chunk).getFullStatus();
         return type.isOrAfter(FullChunkStatus.ENTITY_TICKING);
      }
   }
}
