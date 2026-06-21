package nonamecrackers2.witherstormmod.common.entity.part;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class LinkedPartEntity<T extends Entity, P extends LinkedPartEntity<T, P>> extends PartEntity<T> {
   @Nullable
   protected final P linkedChild;
   public final int segment;
   protected boolean isBase = true;
   protected final EntityDimensions size;
   protected boolean pushEntities;

   public LinkedPartEntity(T parent, @Nullable P linkedChild, float width, float height, int segment) {
      super(parent);
      this.linkedChild = linkedChild;
      if (this.linkedChild != null) {
         this.linkedChild.isBase = false;
      }

      this.size = EntityDimensions.scalable(width, height);
      this.refreshDimensions();
      this.segment = segment;
   }

   public LinkedPartEntity(T parent, float width, float height, int segment) {
      this(parent, null, width, height, segment);
   }

   public void tick() {
      super.tick();
      if (this.pushEntities) {
         List<Entity> list = this.level().getEntities(this.getParent(), this.getBoundingBox(), EntitySelector.pushableBy(this.getParent()));
         if (!list.isEmpty()) {
            for (Entity entity : list) {
               if (!(entity instanceof PartEntity)) {
                  entity.push(this);
               }
            }
         }
      }

      if (this.linkedChild != null) {
         this.tickChild();
      }
   }

   protected void tickChild() {
      Vec3 look = this.getDeltaLinkPos();
      this.linkedChild.tickAndO();
      this.linkedChild.setPos(look.x + this.getX(), look.y + this.getY(), look.z + this.getZ());
   }

   protected Vec3 getDeltaLinkPos() {
      return this.getLookAngle().multiply((double)this.getBbWidth(), (double)this.getBbHeight(), (double)this.getBbWidth());
   }

   public void tickAndO() {
      this.setOldPosAndRot();
      this.tickCount++;
      this.tick();
   }

   protected void defineSynchedData(SynchedEntityData.Builder builder) {
   }

   protected void readAdditionalSaveData(CompoundTag compound) {
   }

   protected void addAdditionalSaveData(CompoundTag compound) {
   }

   @Nullable
   public P getLinkedChild() {
      return this.linkedChild;
   }

   public List<P> getChained() {
      P current = this.linkedChild;

      List<P> chained;
      for (chained = Lists.newArrayList(); current != null; current = current.getLinkedChild()) {
         chained.add(current);
      }

      return chained;
   }

   @Nullable
   public P getSegment(int segment) {
      for (P p : this.getChained()) {
         if (p.segment == segment) {
            return p;
         }
      }

      return null;
   }

   public P getLast() {
      List<P> chained = this.getChained();
      return chained.get(chained.size() - 1);
   }

   public boolean is(Entity entity) {
      return this == entity || this.getParent() == entity;
   }

   public EntityDimensions getDimensions(Pose pose) {
      return this.size;
   }
}
