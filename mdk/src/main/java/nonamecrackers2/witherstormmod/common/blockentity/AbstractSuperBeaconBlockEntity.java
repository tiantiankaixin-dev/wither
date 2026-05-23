package nonamecrackers2.witherstormmod.common.blockentity;

import com.google.common.collect.ImmutableSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.extensions.IForgeBlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.RemoveDistantSuperBeaconMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateDistantSuperBeaconMessage;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;

public abstract class AbstractSuperBeaconBlockEntity extends BlockEntity implements MenuProvider {
   public static final Set<MobEffect> VALID_EFFECTS = ImmutableSet.of(
      MobEffects.DAMAGE_BOOST,
      MobEffects.DAMAGE_RESISTANCE,
      MobEffects.DIG_SPEED,
      MobEffects.JUMP,
      MobEffects.MOVEMENT_SPEED,
      MobEffects.NIGHT_VISION,
      new MobEffect[]{MobEffects.REGENERATION, MobEffects.SATURATION}
   );
   public static final int POWER_UP_ANIM_TIME = 80;
   public static final int POWER_UP_CLIMAX = 40;
   public static final int CONTAINER_DATA_SIZE = 4;
   public static final int SCAN_DIST = 5;
   public static final int COOLDOWN = 200;
   protected final Predicate<BlockEntity> isValidSupportBeacon = entity -> {
      if (this.isActive() && entity != null && entity instanceof SuperSupportBeaconBlockEntity support && !entity.isRemoved() && support.getColor() != null) {
         return true;
      }

      return false;
   };
   protected final Random random = new Random();
   protected int ticks;
   protected int activationTime;
   protected int beamHeight;
   @Nullable
   protected MobEffect effect;
   protected boolean isActive;
   protected float activateAnim;
   protected float activateAnimO;
   protected LockCode lockKey = LockCode.NO_LOCK;
   @Nullable
   protected Component name;
   public int beaconLevel;
   protected int randomOffset;
   protected final ContainerData data = new ContainerData() {
      public void set(int id, int value) {
         switch (id) {
            case 0:
               AbstractSuperBeaconBlockEntity.this.beaconLevel = value;
               break;
            case 1:
               MobEffect effect = MobEffect.byId(value);
               if (effect == null || AbstractSuperBeaconBlockEntity.this.getValidEffects().contains(effect)) {
                  AbstractSuperBeaconBlockEntity.this.effect = effect;
               }
               break;
            case 2:
               AbstractSuperBeaconBlockEntity.this.setShowWorkingArea(value == 1);
               break;
            case 3:
               AbstractSuperBeaconBlockEntity.this.effectSetCooldown = value;
         }
      }

      public int getCount() {
         return 4;
      }

      public int get(int id) {
         switch (id) {
            case 0:
               return AbstractSuperBeaconBlockEntity.this.beaconLevel;
            case 1:
               return MobEffect.getId(AbstractSuperBeaconBlockEntity.this.effect);
            case 2:
               return AbstractSuperBeaconBlockEntity.this.showWorkingArea() ? 1 : 0;
            case 3:
               return AbstractSuperBeaconBlockEntity.this.effectSetCooldown;
            default:
               return 0;
         }
      }
   };
   protected int poweringUpAnimation;
   protected boolean showWorkingArea;
   protected int effectSetCooldown;

   public AbstractSuperBeaconBlockEntity(BlockEntityType<? extends AbstractSuperBeaconBlockEntity> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
      this.randomOffset = this.random.nextInt(100);
   }

   public void tick() {
      this.ticks++;
      if (!this.level.isClientSide) {
         WitherStormModPacketHandlers.MAIN
            .send(
               PacketDistributor.DIMENSION.with(this.level::dimension),
               new UpdateDistantSuperBeaconMessage(
                  this.getBlockPos(), this.getBeamColor(), this.isActive(), this.getBeamHeight(), this.getThickness(), this.getOuterThickness()
               )
            );
      }

      this.tickActivationAnimation();
      this.activateAnimO = this.activateAnim;
      if (this.shouldDoActivatedAnim()) {
         this.activateAnim = this.activateAnim + (1.0F - this.activateAnim) / 8.0F;
      } else {
         this.activateAnim = this.activateAnim + (0.0F - this.activateAnim) / 8.0F;
      }

      if (this.isActive() && !this.isPoweringUp() && (this.ticks + this.randomOffset) % 80 == 0) {
         this.level.playSound(null, this.getBlockPos(), WitherStormModSoundEvents.WITHERED_BEACON_AMBIENT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
      }

      if (this.poweringUpAnimation > 0) {
         this.poweringUpAnimation--;
         this.doPoweringUpAnimation();
      }

      if (!this.level.isClientSide && this.effect != null && this.isActive()) {
         this.applyEffect((ServerLevel)this.level);
      }

      if (!this.level.isClientSide && this.effectSetCooldown > 0) {
         this.effectSetCooldown--;
      }
   }

   protected abstract void applyEffect(ServerLevel var1);

   public void doActivationSequence() {
      this.beamHeight = 0;
      this.activationTime = 0;
      this.markUpdated();
   }

   protected void tickActivationAnimation() {
      if (this.hasReachedPowerUpClimax()) {
         this.activationTime++;
         if (this.beamHeight < 1024) {
            this.beamHeight = this.beamHeight + this.activationTime / 2;
         }
      }
   }

   protected void doPoweringUpAnimation() {
   }

   public int getTicks() {
      return this.ticks;
   }

   public AABB getRenderBoundingBox() {
      return IForgeBlockEntity.INFINITE_EXTENT_AABB;
   }

   public int getBeamHeight() {
      return this.beamHeight;
   }

   public boolean isActive() {
      return this.isActive;
   }

   public float getActivateAnimation(float partialTicks) {
      return Mth.lerp(partialTicks, this.activateAnimO, this.activateAnim);
   }

   protected void activate() {
      this.playSound(WitherStormModSoundEvents.WITHERED_BEACON_ACTIVATE.get(), 1.0F, 1.0F);
      this.doActivationSequence();
   }

   protected void deactivate() {
      this.playSound(WitherStormModSoundEvents.WITHERED_BEACON_DEACTIVATE.get(), 1.0F, 1.0F);
   }

   protected void playSound(SoundEvent event, float volume, float pitch) {
      this.level.playSound(null, this.getBlockPos(), event, SoundSource.BLOCKS, volume, pitch + (this.random.nextFloat() - 0.5F) * 0.35F);
   }

   public void setRemoved() {
      super.setRemoved();
      if (this.isActive()) {
         this.deactivate();
      }

      if (!this.level.isClientSide) {
         WitherStormModPacketHandlers.MAIN
            .send(PacketDistributor.DIMENSION.with(this.level::dimension), new RemoveDistantSuperBeaconMessage(this.getBlockPos()));
      }
   }

   public abstract float getThickness();

   public abstract float getOuterThickness();

   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   public CompoundTag getUpdateTag() {
      return this.saveWithoutMetadata();
   }

   public void onLoad() {
      super.onLoad();
      this.level.getCapability(WitherStormModCapabilities.CHUNK_LOADING_BLOCK_ENTITIES).ifPresent(cap -> cap.add(this.getBlockPos()));
   }

   public void load(CompoundTag tag) {
      super.load(tag);
      this.activationTime = tag.getInt("ActivationTime");
      this.beamHeight = tag.getInt("BeamHeight");
      this.isActive = tag.getBoolean("IsActive");
      if (tag.contains("CustomName", 8)) {
         this.name = Serializer.fromJson(tag.getString("CustomName"));
      }

      this.poweringUpAnimation = tag.getInt("PowerUpTime");
      this.activateAnim = tag.getFloat("ActivationAnim");
      this.effect = MobEffect.byId(tag.getInt("Primary"));
      this.showWorkingArea = tag.getBoolean("ShowWorkingArea");
      this.effectSetCooldown = tag.getInt("Cooldown");
      this.lockKey = LockCode.fromTag(tag);
   }

   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      tag.putInt("ActivationTime", this.activationTime);
      tag.putInt("BeamHeight", this.beamHeight);
      tag.putBoolean("IsActive", this.isActive);
      if (this.name != null) {
         tag.putString("CustomName", Serializer.toJson(this.name));
      }

      tag.putInt("PowerUpTime", this.poweringUpAnimation);
      tag.putFloat("ActivationAnim", this.activateAnim);
      tag.putInt("Primary", MobEffect.getId(this.effect));
      tag.putBoolean("ShowWorkingArea", this.showWorkingArea);
      tag.putInt("Cooldown", this.effectSetCooldown);
      this.lockKey.addToTag(tag);
   }

   protected void markUpdated() {
      this.setChanged();
      this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
   }

   public void setCustomName(@Nullable Component component) {
      this.name = component;
   }

   public Component getDisplayName() {
      return (Component)(this.name != null ? this.name : Component.translatable("container.witherstormmod.withered_beacon"));
   }

   public boolean isPoweringUp() {
      return this.poweringUpAnimation > 0;
   }

   public boolean hasReachedPowerUpClimax() {
      return this.poweringUpAnimation <= 40;
   }

   public abstract Set<MobEffect> getValidEffects();

   public void doPowerUp(ServerPlayer player) {
      this.playSound(WitherStormModSoundEvents.WITHERED_BEACON_ACTIVATE.get(), 1.0F, 1.0F);
   }

   public void setShowWorkingArea(boolean flag) {
      this.showWorkingArea = flag;
      this.markUpdated();
   }

   public boolean showWorkingArea() {
      return this.showWorkingArea;
   }

   public abstract int[] getBeamColor();

   public int getCooldown() {
      return this.effectSetCooldown;
   }

   public void setCooldown(int cooldown) {
      this.effectSetCooldown = cooldown;
   }

   protected boolean shouldDoActivatedAnim() {
      return this.isActive() && this.hasReachedPowerUpClimax();
   }

   protected int getResummonThreshold() {
      return 60 + AbstractSuperBeaconBlockEntity.Color.values().length * 40;
   }

   public static enum Color {
      AQUA(
         ImmutableSet.of(MobEffects.NIGHT_VISION, MobEffects.WATER_BREATHING, MobEffects.DIG_SPEED),
         block -> block.is(WitherStormModBlockTags.AQUA_SUPPORT_BASE),
         5,
         255,
         255
      ),
      GREEN(
         ImmutableSet.of(MobEffects.MOVEMENT_SPEED, MobEffects.DOLPHINS_GRACE, MobEffects.JUMP),
         block -> block.is(WitherStormModBlockTags.GREEN_SUPPORT_BASE),
         26,
         255,
         0
      ),
      GRAY(
         ImmutableSet.of(MobEffects.DAMAGE_BOOST, MobEffects.INVISIBILITY, MobEffects.FIRE_RESISTANCE),
         block -> block.is(WitherStormModBlockTags.GRAY_SUPPORT_BASE),
         255,
         255,
         255
      ),
      RED(
         ImmutableSet.of(MobEffects.DAMAGE_RESISTANCE, MobEffects.REGENERATION, MobEffects.SATURATION),
         block -> block.is(WitherStormModBlockTags.RED_SUPPORT_BASE),
         240,
         39,
         7
      );

      private final Set<MobEffect> validEffects;
      private final Predicate<BlockState> block;
      private final int r;
      private final int g;
      private final int b;

      private Color(Set<MobEffect> effects, Predicate<BlockState> block, int r, int g, int b) {
         this.validEffects = effects;
         this.block = block;
         this.r = r;
         this.g = g;
         this.b = b;
      }

      public boolean isValidBaseBlock(BlockState block) {
         return this.block.test(block);
      }

      public int getRed() {
         return this.r;
      }

      public int getGreen() {
         return this.g;
      }

      public int getBlue() {
         return this.b;
      }

      public Set<MobEffect> getValidEffects() {
         return this.validEffects;
      }
   }
}
