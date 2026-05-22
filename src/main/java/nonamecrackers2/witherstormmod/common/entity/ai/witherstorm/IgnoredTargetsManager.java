package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import nonamecrackers2.witherstormmod.common.blockentity.FormidibombBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.item.FormidibombItem;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class IgnoredTargetsManager {
   public static final int DEFAULT_TIME = 80;
   private final WitherStormEntity storm;
   private final List<IgnoredTargetsManager.Ignored> entities = Lists.newArrayList();
   private final List<AABB> restrictedTargetingRegions = Lists.newArrayList();

   public IgnoredTargetsManager(WitherStormEntity storm) {
      this.storm = storm;
   }

   public static double getTargetRestrictionSize(Entity entity) {
      if (entity instanceof FormidibombEntity formidibomb) {
         if (formidibomb.isAlive() && formidibomb.getStartFuse() > 0) {
            return 20.0;
         }
      } else if (entity instanceof Player player) {
         for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof FormidibombItem item && item.getStartFuse(stack) > 0) {
               return 20.0;
            }
         }
      } else if (entity instanceof WitheredSymbiontEntity) {
         return 50.0;
      }

      return -1.0;
   }

   public void tick() {
      Iterator<IgnoredTargetsManager.Ignored> iterator = this.entities.iterator();

      while (iterator.hasNext()) {
         IgnoredTargetsManager.Ignored ignored = iterator.next();
         if (ignored.ticks > 0) {
            ignored.ticks--;
            if (ignored.ticks == 0) {
               iterator.remove();
            }
         }
      }

      this.restrictedTargetingRegions.clear();
      AABB searchBox = this.storm.getSearchBox();

      for (BlockEntity tile : WorldUtil.getBlockEntitiesInAABB(this.storm.level(), searchBox)) {
         if (tile instanceof FormidibombBlockEntity) {
            FormidibombBlockEntity formidibomb = (FormidibombBlockEntity)tile;
            if (formidibomb.getStartFuse() > 0) {
               this.restrictedTargetingRegions.add(new AABB(formidibomb.getBlockPos()).inflate(20.0));
            }
         }
      }

      for (Entity entity : this.storm.level().getEntitiesOfClass(Entity.class, searchBox)) {
         double size = getTargetRestrictionSize(entity);
         if (size != -1.0) {
            this.restrictedTargetingRegions.add(new AABB(entity.blockPosition()).inflate(size));
         }
      }
   }

   public void addEntityToIgnore(Entity entity) {
      this.addEntityToIgnore(entity, 80);
   }

   public void addEntityToIgnore(Entity entity, int time) {
      if (!this.shouldIgnoreEntity(entity)) {
         this.entities.add(new IgnoredTargetsManager.Ignored(entity.getUUID(), time));
      }
   }

   public boolean shouldIgnoreEntity(Entity entity) {
      return this.entities.stream().anyMatch(e -> e.entity.equals(entity.getUUID()))
         || this.restrictedTargetingRegions.stream().anyMatch(aabb -> aabb.contains(entity.getEyePosition()));
   }

   public CompoundTag save() {
      CompoundTag tag = new CompoundTag();
      ListTag list = new ListTag();

      for (IgnoredTargetsManager.Ignored ignored : this.entities) {
         CompoundTag entry = new CompoundTag();
         entry.putUUID("UUID", ignored.entity);
         entry.putInt("Ticks", ignored.ticks);
         list.add(entry);
      }

      tag.put("Entities", list);
      return tag;
   }

   public void read(CompoundTag tag) {
      this.entities.clear();
      ListTag list = tag.getList("Entities", 10);

      for (int i = 0; i < list.size(); i++) {
         CompoundTag entry = list.getCompound(i);
         UUID id = entry.getUUID("UUID");
         int ticks = entry.getInt("Ticks");
         this.entities.add(new IgnoredTargetsManager.Ignored(id, ticks));
      }
   }

   private class Ignored {
      private final UUID entity;
      private int ticks;

      public Ignored(UUID entity, int time) {
         this.entity = entity;
         this.ticks = time;
      }
   }
}
