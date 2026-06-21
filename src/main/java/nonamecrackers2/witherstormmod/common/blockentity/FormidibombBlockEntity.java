package nonamecrackers2.witherstormmod.common.blockentity;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlockEntityTypes;
import nonamecrackers2.witherstormmod.common.util.IFormidibomb;

public class FormidibombBlockEntity extends BlockEntity implements IFormidibomb {
   @Nullable
   private LivingEntity owner;
   private int fuse = 1200;
   private int startFuse = this.fuse;

   public FormidibombBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)WitherStormModBlockEntityTypes.FORMIDIBOMB.get(), pos, state);
   }

   protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
      super.saveAdditional(tag, registries);
      tag.putInt("Fuse", this.getFuseLife());
      tag.putInt("StartFuse", this.getStartFuse());
   }

   protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
      super.loadAdditional(compound, registries);
      if (compound.contains("Fuse")) {
         this.setLifeFuse(compound.getInt("Fuse"));
      }

      if (compound.contains("StartFuse")) {
         this.setStartFuse(compound.getInt("StartFuse"));
      }
   }

   public static void tick(Level level, BlockPos pos, BlockState state, FormidibombBlockEntity blockEntity) {
      if (blockEntity.getStartFuse() > 0) {
         if ((Boolean)WitherStormModConfig.SERVER.formidibombFuseEnabled.get()) {
            blockEntity.fuse--;
         }

         if (blockEntity.getFuseLife() <= 0) {
            level.setBlock(blockEntity.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
            level.removeBlockEntity(blockEntity.getBlockPos());
         } else if (blockEntity.getFuseLife() <= blockEntity.getStartFuse() / 4) {
            FormidibombEntity entity = new FormidibombEntity(
               level,
               (double)blockEntity.getBlockPos().getX() + 0.5,
               (double)blockEntity.getBlockPos().getY(),
               (double)blockEntity.getBlockPos().getZ() + 0.5,
               blockEntity.getFormidibombOwner(),
               blockEntity,
               blockEntity.getBlockState()
            );
            if (level.addFreshEntity(entity)) {
               level.setBlock(blockEntity.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
               level.removeBlockEntity(blockEntity.getBlockPos());
               level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
         }
      }
   }

   @Override
   public boolean isStillAlive() {
      return !this.isRemoved();
   }

   public ClientboundBlockEntityDataPacket getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
      return this.saveWithoutMetadata(registries);
   }

   @Override
   public int getFuseLife() {
      return this.fuse;
   }

   @Override
   public void setLifeFuse(int fuse) {
      this.fuse = fuse;
      this.setStartFuse(fuse);
   }

   @Override
   public int getStartFuse() {
      return this.startFuse;
   }

   @Override
   public void setStartFuse(int fuse) {
      this.startFuse = fuse;
   }

   @Nullable
   @Override
   public LivingEntity getFormidibombOwner() {
      return this.owner;
   }

   @Override
   public void setFormidibombOwner(@Nullable LivingEntity entity) {
      this.owner = entity;
   }

   @Override
   public Vec3 getPosition() {
      return new Vec3((double)this.getBlockPos().getX(), (double)this.getBlockPos().getY(), (double)this.getBlockPos().getZ());
   }
}
