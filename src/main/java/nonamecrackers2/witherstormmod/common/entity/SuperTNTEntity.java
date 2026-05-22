package nonamecrackers2.witherstormmod.common.entity;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import nonamecrackers2.witherstormmod.common.init.WitherStormModDamageTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import org.jetbrains.annotations.NotNull;

public class SuperTNTEntity extends PrimedTnt {
   private static final EntityDataAccessor<Integer> START_FUSE = SynchedEntityData.defineId(SuperTNTEntity.class, EntityDataSerializers.INT);
   @Nullable
   private LivingEntity owner;
   private int startLife;

   public SuperTNTEntity(EntityType<? extends SuperTNTEntity> type, Level world) {
      super(type, world);
      this.setFuse(320);
   }

   public SuperTNTEntity(Level world, double x, double y, double z, @Nullable LivingEntity owner) {
      this((EntityType<? extends SuperTNTEntity>)WitherStormModEntityTypes.SUPER_TNT.get(), world);
      this.setPos(x, y, z);
      double d0 = world.random.nextDouble() * (float) (Math.PI * 2);
      this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2, -Math.cos(d0) * 0.02);
      this.setFuse(320);
      this.xo = x;
      this.yo = y;
      this.zo = z;
      this.owner = owner;
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(START_FUSE, 0);
   }

   protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("StartFuse")) {
         this.setStartFuse(compound.getInt("StartFuse"));
      }
   }

   protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("StartFuse", this.getStartLife());
   }

   @Nullable
   public LivingEntity getOwner() {
      return this.owner;
   }

   protected void explode() {
      this.level()
         .explode(
            this,
            WitherStormModDamageTypes.superTntExplosion(this.level()),
            null,
            this.getX(),
            this.getY(0.0625),
            this.getZ(),
            32.0F,
            false,
            ExplosionInteraction.TNT
         );
   }

   public void setFuse(int fuse) {
      super.setFuse(fuse);
      this.setStartFuse(fuse);
   }

   public void setStartFuse(int fuse) {
      this.entityData.set(START_FUSE, fuse);
      this.startLife = fuse;
   }

   public int getStartLife() {
      return this.startLife;
   }

   public ItemStack getPickedResult(HitResult target) {
      return new ItemStack((ItemLike)WitherStormModItems.SUPER_TNT.get());
   }

   @NotNull
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }
}
