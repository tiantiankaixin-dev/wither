package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;

public class PersistentTrackedEntities {
   private static final int TICKS_TILL_REMOVE_SAVED = 80;
   private List<Entity> currentTrackedEntities = Lists.newArrayList();
   private List<UUID> savedTrackedEntities = Lists.newArrayList();
   private int tickCount;
   private boolean hasLoadedSaved;

   public void tick(ServerLevel level) {
      this.tickCount++;
      Iterator<UUID> iterator = this.savedTrackedEntities.iterator();

      while (iterator.hasNext()) {
         UUID id = iterator.next();
         Entity entity = level.getEntity(id);
         if (entity != null && !entity.isRemoved()) {
            this.currentTrackedEntities.add(entity);
            iterator.remove();
         } else if (this.tickCount > 80) {
            iterator.remove();
         }
      }
   }

   public List<Entity> getCurrentTrackedEntities() {
      return ImmutableList.copyOf(this.currentTrackedEntities);
   }

   public void trackEntityToConsume(Entity entity) {
      this.currentTrackedEntities.add(entity);
   }

   public void stopTrackingEntity(Entity entity) {
      this.currentTrackedEntities.remove(entity);
   }

   public boolean contains(Entity entity) {
      return this.currentTrackedEntities.contains(entity);
   }

   public void clearAll() {
      this.currentTrackedEntities.clear();
   }

   public void clearAndMakeAllFall() {
      int size = this.currentTrackedEntities.size();

      for (int i = 0; i < size; i++) {
         Entity tracked = this.currentTrackedEntities.get(i);
         tracked.setNoGravity(false);
         if (tracked instanceof BlockClusterEntity) {
            ((BlockClusterEntity)tracked).setPhysics(true);
         }
      }

      this.currentTrackedEntities.clear();
   }

   public void destroyAllClusters() {
      Iterator<Entity> iterator = this.currentTrackedEntities.iterator();

      while (iterator.hasNext()) {
         Entity tracked = iterator.next();
         if (tracked instanceof BlockClusterEntity) {
            iterator.remove();
            tracked.discard();
         }
      }
   }

   public CompoundTag save() {
      CompoundTag tag = new CompoundTag();
      ListTag list = new ListTag();
      List<UUID> toSave = Lists.newArrayList(this.savedTrackedEntities);
      toSave.addAll(this.currentTrackedEntities.stream().map(Entity::getUUID).toList());

      for (UUID id : toSave) {
         list.add(NbtUtils.createUUID(id));
      }

      tag.put("Entities", list);
      return tag;
   }

   public void read(CompoundTag tag) {
      if (!this.hasLoadedSaved) {
         this.savedTrackedEntities.clear();
         ListTag list = tag.getList("Entities", 11);

         for (int i = 0; i < list.size(); i++) {
            UUID id = NbtUtils.loadUUID(list.get(i));
            this.savedTrackedEntities.add(id);
         }

         this.hasLoadedSaved = true;
      }
   }
}
