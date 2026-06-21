package nonamecrackers2.witherstormmod.common.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.MovementEmission;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.serializer.WitherStormModDataSerializers;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;
import nonamecrackers2.witherstormmod.common.util.WitherStormModNBTUtil;

public class BlockClusterEntity extends Entity {
   protected static final EntityDataAccessor<Map<BlockPos, BlockState>> BLOCKS = SynchedEntityData.defineId(
      BlockClusterEntity.class, WitherStormModDataSerializers.BLOCK_STATE_POS_MAP
   );
   private static final EntityDataAccessor<List<CompoundTag>> TILE_DATA = SynchedEntityData.defineId(
      BlockClusterEntity.class, WitherStormModDataSerializers.COMPOUND_LIST
   );
   private static final EntityDataAccessor<BlockPos> START_POS = SynchedEntityData.defineId(BlockClusterEntity.class, EntityDataSerializers.BLOCK_POS);
   private static final EntityDataAccessor<Vec2> ROTATION_DELTA = SynchedEntityData.defineId(BlockClusterEntity.class, WitherStormModDataSerializers.VECTOR_2F);
   private static final EntityDataAccessor<Boolean> PHYSICS = SynchedEntityData.defineId(BlockClusterEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> FORCE_RENDER = SynchedEntityData.defineId(BlockClusterEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Float> X_SIZE = SynchedEntityData.defineId(BlockClusterEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> Y_SIZE = SynchedEntityData.defineId(BlockClusterEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Float> Z_SIZE = SynchedEntityData.defineId(BlockClusterEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Integer> SHAKE_TIME = SynchedEntityData.defineId(BlockClusterEntity.class, EntityDataSerializers.INT);
   protected static final EntityDataAccessor<Optional<BlockPos>> FADE_POINT = SynchedEntityData.defineId(
      BlockClusterEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS
   );
   private static final EntityDataAccessor<Float> FADE_STRENGTH = SynchedEntityData.defineId(BlockClusterEntity.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Integer> FADE_DISTANCE_OFFSET = SynchedEntityData.defineId(
      BlockClusterEntity.class, EntityDataSerializers.INT
   );
   public int time;
   public boolean dropItems = (Boolean)WitherStormModConfig.COMMON.blockClustersDropItems.get();
   public boolean resetGravityOnLoad = true;
   private int shakeTime;
   @Nonnull
   public Vec2 shakeO = Vec2.ZERO;
   @Nonnull
   public Vec2 shake = Vec2.ZERO;
   private int sink;
   private boolean antiStacking;
   private boolean shouldCrumble;
   private boolean shouldntCountToConsumedEntities;
   private float xClusterRot;
   private float xClusterRotO;
   private float yClusterRot;
   private float yClusterRotO;
   private boolean createdFromBeam;
   private boolean createdFromFallingBlock;
   private int headCreatedFrom = -1;
   private double tractorBeamDistanceThreshold;

   public BlockClusterEntity(EntityType<? extends BlockClusterEntity> entityType, Level world) {
      super(entityType, world);
   }

   public void populate(Map<BlockPos, BlockState> states) {
      if (states.size() > 0) {
         int minX = 0;
         int minY = 0;
         int minZ = 0;
         int maxX = 0;
         int maxY = 0;
         int maxZ = 0;

         for (Entry<BlockPos, BlockState> entry : states.entrySet()) {
            BlockPos pos = entry.getKey();
            if (pos.getX() < minX) {
               minX = pos.getX();
            }

            if (pos.getY() < minY) {
               minY = pos.getY();
            }

            if (pos.getZ() < minZ) {
               minZ = pos.getZ();
            }

            if (pos.getX() > maxX) {
               maxX = pos.getX();
            }

            if (pos.getY() > maxY) {
               maxY = pos.getY();
            }

            if (pos.getZ() > maxZ) {
               maxZ = pos.getZ();
            }
         }

         float x = (float)(maxX - minX);
         float y = (float)(maxY - minY);
         float z = (float)(maxZ - minZ);
         this.setSize(Math.abs(x) + 1.0F, Math.abs(y) + 1.0F, Math.abs(z) + 1.0F);
         this.setStartPos(BlockPos.containing((double)minX + (double)x / 2.0, (double)minY + (double)y / 2.0, (double)minZ + (double)z / 2.0));
         this.setBlocks(states);
      }
   }

   public void populate(BlockPos start, BlockPos end, Predicate<BlockState> filter) {
      float x = (float)Mth.floor((float)(end.getX() - start.getX()));
      float y = (float)Mth.floor((float)(end.getY() - start.getY()));
      float z = (float)Mth.floor((float)(end.getZ() - start.getZ()));
      Vec3 clusterPos = Vec3.atLowerCornerOf(start).add((double)x / 2.0 + 0.5, Math.min((double)y, 0.0), (double)z / 2.0 + 0.5);
      this.setPos(clusterPos.x, clusterPos.y, clusterPos.z);
      this.setSize(Math.abs(x) + 1.0F, Math.abs(y) + 1.0F, Math.abs(z) + 1.0F);
      this.setStartPos(start.offset((int)((double)x / 2.0), (int)((double)y / 2.0), (int)((double)z / 2.0)));

      for (BlockPos pos : BlockPos.betweenClosed(start, end)) {
         BlockState state = this.level().getBlockState(pos);
         if (!state.isAir() && filter.test(state)) {
            if (state.hasBlockEntity()) {
               BlockEntity tile = this.level().getBlockEntity(pos);
               if (tile != null) {
                  this.addTileData(tile.saveWithFullMetadata(this.level().registryAccess()));
                  this.level().removeBlockEntity(pos);
               }
            }

            BlockPos relative = pos.subtract(this.getStartPos());
            this.addBlock(state, relative);
         }
      }

      for (Entry<BlockPos, BlockState> entry : this.getBlocks().entrySet()) {
         BlockPos posx = this.getStartPos().offset((Vec3i)entry.getKey());
         this.level().setBlock(posx, Blocks.AIR.defaultBlockState(), 3);
      }
   }

   public void populateWithRadius(BlockPos start, float radius, Predicate<BlockState> filter) {
      this.setSize((float)(Mth.ceil(radius) * 2 - 1), (float)(Mth.ceil(radius) * 2 - 1), (float)(Mth.ceil(radius) * 2 - 1));
      this.setStartPos(start);
      this.setPos(
         (double)((float)start.getX() + 0.5F),
         (double)start.getY() - this.getBoundingBox().getCenter().y + 0.5,
         (double)((float)start.getZ() + 0.5F)
      );

      for (int x = -Mth.floor(radius); x < Mth.ceil(radius); x++) {
         for (int y = -Mth.floor(radius); y < Mth.ceil(radius); y++) {
            for (int z = -Mth.floor(radius); z < Mth.ceil(radius); z++) {
               if (Mth.sqrt((float)(x * x + y * y + z * z)) < radius) {
                  BlockPos currentPos = new BlockPos(x + start.getX(), y + start.getY(), z + start.getZ());
                  BlockPos relativePos = new BlockPos(x, y, z);
                  BlockState state = this.level().getBlockState(currentPos);
                  if (!state.isAir() && filter.test(state)) {
                     if (state.hasBlockEntity()) {
                        BlockEntity tile = this.level().getBlockEntity(currentPos);
                        if (tile != null) {
                           this.addTileData(tile.saveWithFullMetadata(this.level().registryAccess()));
                           this.level().removeBlockEntity(currentPos);
                        }
                     }

                     this.addBlock(state, relativePos);
                  }
               }
            }
         }
      }

      for (Entry<BlockPos, BlockState> entry : this.getBlocks().entrySet()) {
         BlockPos pos = start.offset((Vec3i)entry.getKey());
         this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
      }
   }

   public void setTime(int time) {
      this.time = time;
   }

   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      builder.define(START_POS, BlockPos.ZERO);
      builder.define(BLOCKS, new HashMap());
      builder.define(TILE_DATA, new ArrayList());
      builder.define(ROTATION_DELTA, new Vec2(0.0F, 0.0F));
      builder.define(PHYSICS, true);
      builder.define(FORCE_RENDER, false);
      builder.define(X_SIZE, 1.0F);
      builder.define(Y_SIZE, 1.0F);
      builder.define(Z_SIZE, 1.0F);
      builder.define(SHAKE_TIME, 0);
      builder.define(FADE_POINT, Optional.empty());
      builder.define(FADE_STRENGTH, 10.0F);
      builder.define(FADE_DISTANCE_OFFSET, 0);
   }

   protected void readAdditionalSaveData(CompoundTag compound) {
      if (compound.contains("StartPos")) {
         WitherStormModNBTUtil.readBlockPos(compound, "StartPos").ifPresent(this::setStartPos);
      }

      if (compound.contains("Blocks")) {
         this.setBlocks(WitherStormModNBTUtil.readBlockStatePosMap(this.level().holderLookup(Registries.BLOCK), compound.getList("Blocks", 10)));
      }

      if (compound.contains("TileData")) {
         this.setTileData(WitherStormModNBTUtil.readCompoundList(compound.getList("TileData", 10)));
      }

      if (compound.contains("RotationDelta")) {
         CompoundTag deltaCompound = compound.getCompound("RotationDelta");
         this.setRotationDelta(WitherStormModNBTUtil.readVector2f(deltaCompound));
      }

      if (compound.contains("Width")) {
         this.entityData.set(X_SIZE, compound.getFloat("Width"));
         this.entityData.set(Z_SIZE, compound.getFloat("Width"));
      } else {
         this.entityData.set(X_SIZE, compound.getFloat("XSize"));
         this.entityData.set(Z_SIZE, compound.getFloat("ZSize"));
      }

      if (compound.contains("Height")) {
         this.entityData.set(Y_SIZE, compound.getFloat("Height"));
      } else {
         this.entityData.set(Y_SIZE, compound.getFloat("YSize"));
      }

      this.refreshDimensions();
      this.time = compound.getInt("Time");
      this.dropItems = compound.getBoolean("DropItems");
      this.resetGravityOnLoad = compound.getBoolean("ResetGravity");
      if (this.resetGravityOnLoad) {
         this.setNoGravity(false);
      }

      this.setForceRender(compound.getBoolean("ForceRender"));
      this.setShakeTime(compound.getInt("ShakeTime"));
      this.setSink(compound.getInt("GroundSink"));
      this.setAntiStacking(compound.getBoolean("AntiStacking"));
      if (compound.contains("StaticFadePos")) {
         this.entityData.set(FADE_POINT, WitherStormModNBTUtil.readBlockPos(compound, "StaticFadePos"));
      }

      this.shouldCrumble = compound.getBoolean("ShouldCrumble");
      this.shouldntCountToConsumedEntities = compound.getBoolean("ShouldntCountToConsumedEntities");
      this.createdFromBeam = compound.getBoolean("CreatedFromBeam");
      this.createdFromFallingBlock = compound.getBoolean("CreatedFromFallingBlock");
      this.headCreatedFrom = compound.getInt("HeadCreatedFrom");
      this.tractorBeamDistanceThreshold = compound.getDouble("TractorBeamDistanceThreshold");
      if (compound.contains("HasPhysics")) {
         this.setPhysics(compound.getBoolean("HasPhysics"));
      }
   }

   protected void addAdditionalSaveData(CompoundTag compound) {
      compound.put("StartPos", NbtUtils.writeBlockPos(this.getStartPos()));
      compound.put("Blocks", WitherStormModNBTUtil.writeBlockStatePosMap(this.getBlocks()));
      compound.put("TileData", WitherStormModNBTUtil.writeCompoundList(this.getTileData()));
      compound.putFloat("XSize", (Float)this.entityData.get(X_SIZE));
      compound.putFloat("YSize", (Float)this.entityData.get(Y_SIZE));
      compound.putFloat("ZSize", (Float)this.entityData.get(Z_SIZE));
      compound.putInt("Time", this.time);
      compound.putBoolean("DropItems", this.dropItems);
      compound.put("RotationDelta", WitherStormModNBTUtil.writeVector2f(this.getRotationDelta()));
      compound.putBoolean("ResetGravity", this.resetGravityOnLoad);
      compound.putBoolean("ForceRender", this.forceRender());
      compound.putInt("ShakeTime", this.shakeTime);
      compound.putInt("GroundSink", this.getSink());
      compound.putBoolean("AntiStacking", this.antiStacking());
      ((Optional<BlockPos>)this.entityData.get(FADE_POINT)).ifPresent(pos -> compound.put("StaticFadePos", NbtUtils.writeBlockPos(pos)));
      compound.putBoolean("ShouldCrumble", this.shouldCrumble);
      compound.putBoolean("ShouldntCountToConsumedEntities", this.shouldntCountToConsumedEntities);
      compound.putBoolean("CreatedFromBeam", this.createdFromBeam);
      compound.putBoolean("CreatedFromFallingBlock", this.createdFromFallingBlock);
      compound.putInt("HeadCreatedFrom", this.headCreatedFrom);
      compound.putDouble("TractorBeamDistanceThreshold", this.tractorBeamDistanceThreshold);
      compound.putBoolean("HasPhysics", this.physicsEnabled());
   }

   public BlockClusterEntity splitAt(Axis axis) {
      Map<BlockPos, BlockState> split = Maps.newHashMap();
      Map<BlockPos, BlockState> current = Maps.newHashMap(this.getBlocks());
      Iterator<Entry<BlockPos, BlockState>> iterator = current.entrySet().iterator();

      while (iterator.hasNext()) {
         Entry<BlockPos, BlockState> entry = iterator.next();
         BlockPos pos = entry.getKey();
         switch (axis) {
            case Y:
               if (pos.getY() < 0) {
                  split.put(pos, entry.getValue());
                  iterator.remove();
               }
               break;
            case X:
               if (pos.getX() < 0) {
                  split.put(pos, entry.getValue());
                  iterator.remove();
               }
               break;
            case Z:
               if (pos.getZ() < 0) {
                  split.put(pos, entry.getValue());
                  iterator.remove();
               }
         }
      }

      if (!split.isEmpty() && !current.isEmpty()) {
         if (this.getSize() < 10) {
            this.setShouldCrumble(false);
         }

         this.setBlocks(current);
         BlockClusterEntity splitCluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(this.level());
         splitCluster.setSize((Float)this.entityData.get(X_SIZE), (Float)this.entityData.get(Y_SIZE), (Float)this.entityData.get(Z_SIZE));
         splitCluster.setStartPos(this.getStartPos());
         splitCluster.setBlocks(split);
         splitCluster.moveTo(this.position());
         splitCluster.setNoGravity(this.isNoGravity());
         splitCluster.setPhysics(this.physicsEnabled());
         splitCluster.setRotationDelta(this.getRotationDelta());
         splitCluster.setDeltaMovement(this.getDeltaMovement().scale(0.8));
         splitCluster.setShouldCrumble(this.shouldCrumble());
         splitCluster.setFadePos(this.getFadePos());
         splitCluster.setFadeDistanceOffset(this.getFadeDistanceOffset());
         splitCluster.setFadeStrength(this.getFadeStrength());
         return splitCluster;
      } else {
         return null;
      }
   }

   public void tick() {
      this.shakeO = new Vec2(this.shake.x, this.shake.y);
      if (this.shakeTime > 0) {
         float shakeTime = (float)this.getShakeTime();
         float x = Mth.cos(shakeTime * 4.5F) * 0.05F + (this.random.nextFloat() - 0.5F) * 0.05F;
         float z = Mth.sin(shakeTime * 3.5F) * 0.15F + (this.random.nextFloat() - 0.5F) * 0.2F;
         this.shake = new Vec2(x, z);
         this.shakeTime--;
         if (this.shakeTime == 0) {
            this.setShakeTime(0);
         }
      } else {
         this.shake = new Vec2(0.0F, 0.0F);
      }

      if (!this.isNoGravity()) {
         this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
      }

      this.move(MoverType.SELF, this.getDeltaMovement());
      this.time++;
      this.noPhysics = !this.physicsEnabled();
      super.tick();
      this.xClusterRotO = this.xClusterRot;
      this.yClusterRotO = this.yClusterRot;
      if (this.getShakeTime() <= 0) {
         this.xClusterRot = this.xClusterRot + this.getRotationDelta().x;
         this.yClusterRot = this.yClusterRot + this.getRotationDelta().y;
      }

      if (!this.level().isClientSide) {
         BlockPos pos = this.blockPosition();
         if (this.getBlocks().isEmpty()) {
            this.discard();
         }

         Map<BlockPos, BlockState> blocks = this.getBlocks();
         boolean isAir = true;

         for (Entry<BlockPos, BlockState> entry : blocks.entrySet()) {
            BlockState state = entry.getValue();
            if (isAir) {
               isAir = state.isAir();
            }
         }

         if (isAir) {
            this.discard();
         }

         if (!this.onGround()) {
            if ((float)pos.getY() + this.getBbHeight() <= (float)this.level().getMinBuildHeight() || this.time > 600) {
               if (this.dropItems && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                  for (Entry<BlockPos, BlockState> entryx : blocks.entrySet()) {
                     BlockState state = entryx.getValue();
                     BlockPos position = pos.offset((Vec3i)entryx.getKey());
                     this.spawnAtSpecificLocation(state.getBlock().asItem(), position);
                  }
               }

               this.discard();
            }
         } else {
            this.place();
         }

         if ((Boolean)WitherStormModConfig.SERVER.clustersRemoveItems.get() && this.getSize() != 1 && !this.createdFromTractorBeam()) {
            for (ItemEntity entity : this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox())) {
               if (entity.isAlive() && entity.getOwner() == null) {
                  entity.discard();
               }
            }
         }
      } else {
         this.refreshDimensions();
         this.reapplyPosition();
      }
   }

   public void place() {
      this.discard();
      BlockPos pos = this.blockPosition();
      if (this.antiStacking()) {
         BlockPos currentPos = this.blockPosition();
         BlockState current = this.level().getBlockState(currentPos);

         for (int i = 0; i < 50 && current.isAir(); i++) {
            currentPos = currentPos.below();
            current = this.level().getBlockState(currentPos);
         }

         pos = pos.atY(currentPos.getY());
      }

      for (Entry<BlockPos, BlockState> entry : this.getBlocks().entrySet()) {
         BlockState state = entry.getValue();
         BlockPos relativePos = entry.getKey();
         BlockPos basePos = pos.offset(relativePos.getX(), relativePos.getY() - this.getSink(), relativePos.getZ());
         BlockPos placementPos = basePos.above(Mth.floor(this.getBoundingBox().getYsize() / 2.0 - 0.5));
         if (this.level().getBlockEntity(placementPos) == null
            && !this.level().getBlockState(placementPos).is(BlockTags.DRAGON_IMMUNE)
            && !state.is(WitherStormModBlockTags.BLOCK_CLUSTERS_CANNOT_PLACE)
            && this.level().setBlock(placementPos, state, 3)) {
            if (state.hasBlockEntity()) {
               CompoundTag tileData = this.getTileDataFromOffsetPos(relativePos);
               if (tileData != null) {
                  BlockEntity tile = this.level().getBlockEntity(placementPos);
                  if (tile != null) {
                     tileData.putInt("x", placementPos.getX());
                     tileData.putInt("y", placementPos.getY());
                     tileData.putInt("z", placementPos.getZ());
                     tile.loadWithComponents(tileData, this.level().registryAccess());
                     tile.setChanged();
                  }
               }
            }

            this.level().updateNeighborsAt(placementPos, state.getBlock());
         } else if (this.dropItems && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.spawnAtSpecificLocation(state.getBlock().asItem(), placementPos);
         }
      }
   }

   public void spawnAtSpecificLocation(ItemLike item, BlockPos position) {
      ItemStack stack = new ItemStack(item);
      ItemEntity itemEntity = new ItemEntity(this.level(), (double)position.getX(), (double)position.getY(), (double)position.getZ(), stack);
      itemEntity.setDefaultPickUpDelay();
      if (this.captureDrops() != null) {
         this.captureDrops().add(itemEntity);
      } else {
         this.level().addFreshEntity(itemEntity);
      }
   }

   public void setRotationDelta(Vec2 rotation) {
      this.entityData.set(ROTATION_DELTA, rotation);
   }

   public Vec2 getRotationDelta() {
      return (Vec2)this.entityData.get(ROTATION_DELTA);
   }

   public Map<BlockPos, BlockState> getBlocks() {
      return (Map<BlockPos, BlockState>)this.entityData.get(BLOCKS);
   }

   public void addBlock(BlockState state, BlockPos relativePosition) {
      this.put(BLOCKS, state, relativePosition);
   }

   private void put(EntityDataAccessor<Map<BlockPos, BlockState>> param, BlockState state, BlockPos pos) {
      Map<BlockPos, BlockState> map = Maps.newHashMap(this.getBlocks());
      map.put(pos, state);
      this.entityData.set(param, map);
   }

   public void setBlocks(Map<BlockPos, BlockState> blocks) {
      this.entityData.set(BLOCKS, blocks);
   }

   public void setStartPos(BlockPos pos) {
      this.entityData.set(START_POS, pos);
   }

   public BlockPos getStartPos() {
      return (BlockPos)this.entityData.get(START_POS);
   }

   public List<CompoundTag> getTileData() {
      return (List<CompoundTag>)this.entityData.get(TILE_DATA);
   }

   @Nullable
   public CompoundTag getTileDataFromOffsetPos(BlockPos pos) {
      BlockPos actualPos = this.getStartPos().offset(pos);

      for (CompoundTag data : this.getTileData()) {
         if (data.getInt("x") == actualPos.getX() && data.getInt("y") == actualPos.getY() && data.getInt("z") == actualPos.getZ()) {
            return data;
         }
      }

      return null;
   }

   public void addTileData(CompoundTag compound) {
      List<CompoundTag> list = Lists.newArrayList(this.getTileData());
      list.add(compound);
      this.entityData.set(TILE_DATA, list, true);
   }

   public void setTileData(List<CompoundTag> list) {
      this.entityData.set(TILE_DATA, list);
   }

   public EntityDimensions getDimensions(Pose pose) {
      return EntityDimensions.scalable(
         Math.max((Float)this.entityData.get(X_SIZE), (Float)this.entityData.get(Z_SIZE)), (Float)this.entityData.get(Y_SIZE)
      );
   }

   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
      return new ClientboundAddEntityPacket(this, serverEntity);
   }

   public int getSize() {
      return this.getBlocks().size();
   }

   public boolean physicsEnabled() {
      return (Boolean)this.entityData.get(PHYSICS);
   }

   public void setPhysics(boolean physics) {
      this.entityData.set(PHYSICS, physics);
      this.noPhysics = !physics;
   }

   public boolean containsBlock(Block block) {
      for (Entry<BlockPos, BlockState> entry : this.getBlocks().entrySet()) {
         if (entry.getValue().is(block)) {
            return true;
         }
      }

      return false;
   }

   public void setResetGravityOnLoad(boolean shouldReset) {
      this.resetGravityOnLoad = shouldReset;
   }

   public void onSyncedDataUpdated(EntityDataAccessor<?> parameter) {
      super.onSyncedDataUpdated(parameter);
      if (parameter.equals(X_SIZE) || parameter.equals(Y_SIZE) || parameter.equals(Z_SIZE)) {
         this.refreshDimensions();
      } else if (parameter.equals(SHAKE_TIME)) {
         this.shakeTime = (Integer)this.entityData.get(SHAKE_TIME);
      }
   }

   public boolean forceRender() {
      return (Boolean)this.entityData.get(FORCE_RENDER);
   }

   public void setForceRender(boolean flag) {
      this.entityData.set(FORCE_RENDER, flag);
   }

   public void setSize(float x, float y, float z) {
      this.entityData.set(X_SIZE, x);
      this.entityData.set(Y_SIZE, y);
      this.entityData.set(Z_SIZE, z);
      this.refreshDimensions();
   }

   protected AABB makeBoundingBox() {
      float x = (Float)this.entityData.get(X_SIZE);
      float y = (Float)this.entityData.get(Y_SIZE);
      float z = (Float)this.entityData.get(Z_SIZE);
      return new AABB(
         this.getX() - (double)(x / 2.0F),
         this.getY(),
         this.getZ() - (double)z / 2.0,
         this.getX() + (double)x / 2.0,
         this.getY() + (double)y,
         this.getZ() + (double)z / 2.0
      );
   }

   public void setShakeTime(int time) {
      this.shakeTime = time;
      this.entityData.set(SHAKE_TIME, time);
   }

   public int getShakeTime() {
      return this.shakeTime;
   }

   public void setSink(int sink) {
      this.sink = sink;
   }

   public int getSink() {
      return this.sink;
   }

   public void setAntiStacking(boolean flag) {
      this.antiStacking = flag;
   }

   public boolean antiStacking() {
      return this.antiStacking;
   }

   @Nullable
   public BlockPos getFadePos() {
      return (BlockPos)((Optional)this.entityData.get(FADE_POINT)).orElse(null);
   }

   public void setFadePos(@Nullable BlockPos pos) {
      this.entityData.set(FADE_POINT, Optional.ofNullable(pos));
   }

   public void setFadeStrength(float strength) {
      this.entityData.set(FADE_STRENGTH, strength);
   }

   public float getFadeStrength() {
      return (Float)this.entityData.get(FADE_STRENGTH);
   }

   public int getFadeDistanceOffset() {
      return (Integer)this.entityData.get(FADE_DISTANCE_OFFSET);
   }

   public void setFadeDistanceOffset(int offset) {
      this.entityData.set(FADE_DISTANCE_OFFSET, offset);
   }

   public void setShouldCrumble(boolean flag) {
      this.shouldCrumble = flag;
   }

   public boolean shouldCrumble() {
      return this.shouldCrumble;
   }

   public void setShouldntCountToConsumedEntities(boolean flag) {
      this.shouldntCountToConsumedEntities = flag;
   }

   public boolean shouldntCountToConsumedEntities() {
      return this.shouldntCountToConsumedEntities;
   }

   public boolean createdFromTractorBeam() {
      return this.createdFromBeam;
   }

   public void setCreatedFromTractorBeam(boolean flag) {
      this.createdFromBeam = flag;
   }

   public boolean createdFromFallingBlock() {
      return this.createdFromFallingBlock;
   }

   public void setCreatedFromFallingBlock(boolean flag) {
      this.createdFromFallingBlock = flag;
   }

   public int getHeadCreatedFrom() {
      return this.headCreatedFrom;
   }

   public void setHeadCreatedFrom(int head) {
      this.headCreatedFrom = head;
   }

   public float getClusterXRot(float partialTicks) {
      return Mth.lerp(partialTicks, this.xClusterRotO, this.xClusterRot);
   }

   public float getClusterYRot(float partialTicks) {
      return Mth.lerp(partialTicks, this.yClusterRotO, this.yClusterRot);
   }

   public void setTractorBeamDistanceThreshold(double distance) {
      this.tractorBeamDistanceThreshold = distance;
   }

   public double getTractorBeamDistanceThreshold() {
      return this.tractorBeamDistanceThreshold;
   }

   public boolean canChangeDimensions(Level currentLevel, Level destinationLevel) {
      return false;
   }

   protected boolean updateInWaterStateAndDoFluidPushing() {
      this.fluidHeight.clear();
      this.forgeFluidTypeHeight.clear();
      return this.isInFluidType();
   }

   protected MovementEmission getMovementEmission() {
      return MovementEmission.NONE;
   }

   public void updateSwimming() {
   }

   public boolean canSpawnSprintParticle() {
      return false;
   }

   public void baseTick() {
      this.level().getProfiler().push("entityBaseTick");
      this.walkDistO = this.walkDist;
      this.xRotO = this.getXRot();
      this.yRotO = this.getYRot();
      this.checkBelowWorld();
      this.firstTick = false;
      this.level().getProfiler().pop();
   }
}
