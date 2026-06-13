package nonamecrackers2.witherstormmod.common.blockentity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.blockentity.inventory.SuperSupportBeaconMenu;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlockEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SuperSupportBeaconBlockEntity extends AbstractSuperBeaconBlockEntity {
   public static final int EFFECT_AREA_ARC = 90;
   private static final int[] FALLBACK_COLOR = new int[]{255, 255, 255};
   private static final Logger LOGGER = LogManager.getLogger();
   @Nullable
   private AbstractSuperBeaconBlockEntity.Color color;
   @Nullable
   private BlockPos connectedBeacon;

   public SuperSupportBeaconBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType<? extends AbstractSuperBeaconBlockEntity>)WitherStormModBlockEntityTypes.SUPER_SUPPORT_BEACON.get(), pos, state);
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.level().isClientSide) {
         BlockPos searchStart = this.worldPosition.below();
         int radius = 1;
         List<BlockState> blocks = Lists.newArrayList();

         for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
               blocks.add(this.level().getBlockState(searchStart.offset(x, 0, z)));
            }
         }

         AbstractSuperBeaconBlockEntity.Color matching = null;

         for (AbstractSuperBeaconBlockEntity.Color color : AbstractSuperBeaconBlockEntity.Color.values()) {
            if (blocks.stream().allMatch(block -> color.isValidBaseBlock(block))) {
               matching = color;
               break;
            }
         }

         if (matching != this.color) {
            this.color = matching;
            this.markUpdated();
         }

         SuperBeaconBlockEntity beacon = this.getNearbyValidBeacon();
         if (beacon != null) {
            if (this.connectedBeacon != beacon.getBlockPos()) {
               this.markUpdated();
            }

            this.connectedBeacon = beacon.getBlockPos();
         }

         boolean flag = this.color != null && beacon != null;
         if (flag != this.isActive) {
            if (flag) {
               this.isActive = true;
               this.activate();
            } else {
               this.isActive = false;
               this.deactivate();
            }

            this.markUpdated();
         }
      }

      SuperBeaconBlockEntity beaconx = this.getConnectedBeaconEntity();
      if (beaconx != null) {
         this.beaconLevel = beaconx.beaconLevel;
         this.showWorkingArea = beaconx.showWorkingArea();
         if (!this.level().isClientSide && this.color != null && beaconx.getResummonTicks() == this.getResummonThreshold()) {
            this.playSound(WitherStormModSoundEvents.WITHERED_BEACON_ACTIVATE.get(), 1.0F, 1.0F);
            this.playSound(WitherStormModSoundEvents.TREMBLE.get(), 10.0F, 1.0F);
            Vec3 pos = Vec3.atCenterOf(this.getConnectedBeacon());
            WitherStormModPacketHandlers.MAIN
               .send(
                  SimpleChannel.toNear((net.minecraft.server.level.ServerLevel)this.level(), pos.x, pos.y, pos.z, 20.0),
                  new ShakeScreenMessage(80.0F, 10.0F)
               );
         }
      }
   }

   @Override
   protected void applyEffect(ServerLevel level) {
      if (this.getConnectedBeacon() != null) {
         BlockPos target = this.getConnectedBeacon();
         Vec3 targetVec = Vec3.atCenterOf(target);
         float angleWithMainBeacon = getAngleBetween(this.getBlockPos(), this.getConnectedBeacon());

         for (ServerPlayer player : level.players()) {
            float angle = (float)(Mth.atan2(player.getX() - targetVec.x, player.getZ() - targetVec.z) * (180.0 / Math.PI));
            float diff = (angleWithMainBeacon - angle + 180.0F + 360.0F) % 360.0F - 180.0F;
            if (diff <= 45.0F && diff >= -45.0F) {
               player.addEffect(new MobEffectInstance(this.effect, 205, this.beaconLevel - 1, true, true));
            }
         }
      }
   }

   public static float getAngleBetween(BlockPos blockPos, BlockPos blockTarget) {
      Vec3 pos = Vec3.atCenterOf(blockPos);
      Vec3 target = Vec3.atCenterOf(blockTarget);
      return (float)(Mth.atan2(pos.x - target.x, pos.z - target.z) * (180.0 / Math.PI));
   }

   @Override
   public float getThickness() {
      return 0.15F;
   }

   @Override
   public float getOuterThickness() {
      return 0.2F;
   }

   public AbstractSuperBeaconBlockEntity.Color getColor() {
      return this.color;
   }

   @Override
   public int[] getBeamColor() {
      AbstractSuperBeaconBlockEntity.Color color = this.getColor();
      return color != null ? new int[]{color.getRed(), color.getGreen(), color.getBlue()} : FALLBACK_COLOR;
   }

   @Nullable
   private SuperBeaconBlockEntity getNearbyValidBeacon() {
      AABB box = new AABB(this.getBlockPos()).inflate(5.0);

      for (BlockEntity entity : WorldUtil.getBlockEntitiesInAABB(this.level, box)) {
         if (entity instanceof SuperBeaconBlockEntity beacon && beacon.isConnected(this.getBlockPos())) {
            return beacon;
         }
      }

      return null;
   }

   @Nullable
   public BlockPos getConnectedBeacon() {
      return this.connectedBeacon;
   }

   public SuperBeaconBlockEntity getConnectedBeaconEntity() {
      if (this.getConnectedBeacon() != null) {
         BlockEntity entity = this.level().getBlockEntity(this.getConnectedBeacon());
         if (entity instanceof SuperBeaconBlockEntity) {
            return (SuperBeaconBlockEntity)entity;
         }
      }

      return null;
   }

   @Override
   public Component getDisplayName() {
      return (Component)(this.name != null ? this.name : Component.translatable("container.witherstormmod.withered_support_beacon"));
   }

   @Override
   public void loadAdditional(CompoundTag tag) {
      super.load(tag);
      int colorIndex = tag.getInt("Color");
      if (colorIndex >= 0 && colorIndex < AbstractSuperBeaconBlockEntity.Color.values().length) {
         this.color = AbstractSuperBeaconBlockEntity.Color.values()[colorIndex];
      } else if (colorIndex == -1) {
         this.color = null;
      } else {
         LOGGER.warn("Read incorrect color index value {}", colorIndex);
      }

      if (tag.contains("Connected")) {
         this.connectedBeacon = NbtUtils.readBlockPos(tag, "Connected").orElse(null);
      } else {
         this.connectedBeacon = null;
      }
   }

   @Override
   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      if (this.color != null) {
         tag.putInt("Color", this.color.ordinal());
      } else {
         tag.putInt("Color", -1);
      }

      if (this.connectedBeacon != null) {
         tag.put("Connected", NbtUtils.writeBlockPos(this.connectedBeacon));
      } else {
         tag.remove("Connected");
      }
   }

   @Override
   public Set<MobEffect> getValidEffects() {
      return (Set<MobEffect>)(this.color != null ? this.color.getValidEffects() : ImmutableSet.of());
   }

   @Override
   public void setShowWorkingArea(boolean flag) {
      SuperBeaconBlockEntity entity = this.getConnectedBeaconEntity();
      if (entity != null) {
         entity.setShowWorkingArea(flag);
      }

      super.setShowWorkingArea(flag);
   }

   public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
      return BaseContainerBlockEntity.canUnlock(player, this.lockKey, this.getDisplayName())
         ? new SuperSupportBeaconMenu(
            id, inventory, this.data, ContainerLevelAccess.create(this.level, this.getBlockPos()), this::doPowerUp, this.getValidEffects()
         )
         : null;
   }

   @Nullable
   public BlockPos getBeamPos() {
      SuperBeaconBlockEntity connected = this.getConnectedBeaconEntity();
      boolean flag = false;
      if (connected != null && this.color != null && connected.getResummonTicks() > this.getResummonThreshold()) {
         flag = true;
      }

      BlockPos pos = this.getConnectedBeacon();
      return pos != null && flag ? pos.above(3) : pos;
   }

   @Override
   protected boolean shouldDoActivatedAnim() {
      SuperBeaconBlockEntity connected = this.getConnectedBeaconEntity();
      boolean flag = false;
      if (connected != null && this.color != null && connected.getResummonTicks() > this.getResummonThreshold()) {
         flag = true;
      }

      return super.shouldDoActivatedAnim() || flag;
   }

   @Override
   protected int getResummonThreshold() {
      return 60 + this.color.ordinal() * 40;
   }
}
