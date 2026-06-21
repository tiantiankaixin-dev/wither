package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.ultimatetarget;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import nonamecrackers2.witherstormmod.api.common.event.WitherStormFindUltimateTargetEvent;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModItemTags;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModStructureTags;
import nonamecrackers2.witherstormmod.common.util.WitherStormModNBTUtil;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UltimateTargetManager {
   private static final Logger LOGGER = LogManager.getLogger("witherstormmod/UltimateTargetManager");
   private final WitherStormEntity entity;
   @Nullable
   protected LivingEntity ultimateTarget;
   @Nullable
   protected Vec3 ultimateTargetO;
   @Nullable
   protected UUID targetOverride;
   @Nullable
   protected BlockPos blockTargetOverride;
   @Nullable
   protected BlockPos alternativeUltimateTarget;
   @Nullable
   protected BlockPos distractedPos;
   @Nullable
   protected BlockPos randomStrollPos;
   @Nullable
   protected ChunkPos center;
   protected boolean ultimateTargetStationary;
   protected int ticksSinceStationary;
   protected int runawayDiminishTicks;
   protected final int chunkBoundaryRadius;
   protected int runawayAttempts;
   protected boolean canCountRunawayAttempt;
   protected boolean canBeDistracted;
   protected boolean isDistracted;
   protected int ticksSinceDistracted;
   protected int canBeDistractedFor;
   protected int distractionWait;
   protected int tillShowHole;
   protected int tillRandomStroll;
   protected int cannotSeeTargetFor;
   @Nullable
   protected UltimateTargetManager.DistractionReason distractionReason;
   protected int tiredOfChasingTicks;
   @Nullable
   protected UUID ignoredTarget;
   protected int ignoringTargetFor;
   protected int cannotReachTargetFor;
   protected int timeTillIgnoreTarget;
   @Nullable
   private UUID farthestPlayer;
   @Nullable
   private UUID randomPlayer;
   private long farthestLastSwitchTime;
   private long randomPlayerLastSwitchTime;
   @Nullable
   private UltimateTargetManager.TargetingType randomizedType;
   private long randomizedLastSwitchTime;

   public UltimateTargetManager(WitherStormEntity entity) {
      this.entity = entity;
      this.chunkBoundaryRadius = (Integer)WitherStormModConfig.SERVER.targetStationaryChunkRadius.get();
   }

   public void tick() {
      List<ServerPlayer> players = ((ServerLevel)this.entity.level())
         .players()
         .stream()
         .filter(p -> !p.getUUID().equals(this.ignoredTarget))
         .collect(Collectors.toList());
      LivingEntity ultimateTarget = this.findUltimateTarget(players);
      if (ultimateTarget != this.ultimateTarget) {
         this.cannotReachTargetFor = 0;
         this.timeTillIgnoreTarget = 1200 + this.entity.getRandom().nextInt(600);
      }

      this.setUltimateTarget(ultimateTarget);
      if (ultimateTarget != null) {
         this.setAlternativeUltimateTarget(ultimateTarget.blockPosition());
         if ((Boolean)WitherStormModConfig.SERVER.ignoreUltimateTargetIfHidden.get()
            && !this.isDistracted()
            && players.size() > 1
            && this.ignoredTarget == null
            && !WorldUtil.canSeeOrIsNotInASmallArea(this.entity, ultimateTarget)
            && this.entity.position().subtract(ultimateTarget.position()).horizontalDistance() < 150.0) {
            this.cannotReachTargetFor++;
            if (this.cannotReachTargetFor > this.timeTillIgnoreTarget) {
               this.ignoredTarget = ultimateTarget.getUUID();
               this.ignoringTargetFor = 12000 + this.entity.getRandom().nextInt(6000);
            }
         } else if (this.cannotReachTargetFor > 0) {
            this.cannotReachTargetFor--;
         }
      }

      if (this.ignoringTargetFor > 0) {
         this.ignoringTargetFor--;
         if (this.ignoringTargetFor == 0) {
            this.ignoredTarget = null;
         }
      }

      UltimateTargetManager.TargetingType targetingType = (UltimateTargetManager.TargetingType)WitherStormModConfig.SERVER.ultimateTargetingType.get();
      ServerPlayer randomStrollPlayer = UltimateTargetManager.TargetingType.NEAREST.getPlayer(this, this.entity, players, Predicate.not(Entity::isSpectator));
      boolean randomStrollNearPlayer = targetingType == UltimateTargetManager.TargetingType.RANDOM_STROLL_NEAR_PLAYER && randomStrollPlayer != null;
      if (targetingType == UltimateTargetManager.TargetingType.RANDOM_STROLL || randomStrollNearPlayer) {
         int maxRadius = (Integer)WitherStormModConfig.SERVER.maxRandomStrollTargetingTypeRadius.get();
         BlockPos alt = this.getAlternativeUltimateTarget();
         if (alt == null || this.entity.position().subtract(Vec3.atCenterOf(alt)).horizontalDistance() < 100.0) {
            BlockPos newPos = null;

            for (int i = 0; i < 10; i++) {
               int x = this.entity.getRandom().nextInt(maxRadius * 2) - maxRadius;
               int z = this.entity.getRandom().nextInt(maxRadius * 2) - maxRadius;
               BlockPos entityPos = this.entity.blockPosition();
               if (randomStrollNearPlayer) {
                  entityPos = randomStrollPlayer.blockPosition();
               }

               BlockPos current = entityPos.offset(x, 0, z);
               if ((
                     alt == null
                        || Math.sqrt(alt.distSqr(current)) > (double)maxRadius / 2.0
                           && this.entity.position().subtract(Vec3.atCenterOf(current)).horizontalDistance() > (double)maxRadius / 2.0
                  )
                  && this.entity.level().isInWorldBounds(current)
                  && this.entity.level().getWorldBorder().isWithinBounds(current)) {
                  newPos = current;
                  break;
               }
            }

            if (newPos != null) {
               this.setAlternativeUltimateTarget(newPos);
            }
         }
      }

      for (ServerPlayer player : players) {
         for (ItemStack stack : player.getInventory().items) {
            if (stack.is(WitherStormModItemTags.COMMAND_BLOCK_TOOLS)
               && this.tillShowHole() <= 0
               && !this.entity.isBeingTornApart()
               && this.entity.getPhase() > 6) {
               this.tillShowHole = (Integer)WitherStormModConfig.SERVER.tillShouldShowHole.get() * 1200 + this.entity.getRandom().nextInt(4800);
            }
         }
      }

      Vec3 ultimateTargetPos = this.getUltimateTargetPos();
      if (ultimateTargetPos != null) {
         BlockPos blockPos = BlockPos.containing(ultimateTargetPos);
         if (this.getCenter() == null) {
            this.setCenter(new ChunkPos(blockPos));
         }

         int distance = (int)(this.entity.position().distanceTo(ultimateTargetPos) * (Double)WitherStormModConfig.SERVER.distanceMultiplier.get());
         if ((Boolean)WitherStormModConfig.SERVER.usePhaseAsDistanceMultiplier.get()) {
            distance = (int)((double)distance * ((double)this.entity.getPhase() * 0.2 + 1.0));
         }

         int stationaryTicks = (Integer)WitherStormModConfig.SERVER.targetStationaryMinutes.get() * 1200;
         int runawayTicks = stationaryTicks - (Integer)WitherStormModConfig.SERVER.targetRunawayMinutes.get() * 1200;
         int runawayAttemptsDiminish = (Integer)WitherStormModConfig.SERVER.minutesTillRunawayAttemptDiminish.get() * 1200;
         if (this.isDistracted()) {
            this.ticksSinceDistracted++;
            if (this.getTicksSinceDistracted() > this.canBeDistractedFor) {
               this.makeFocused();
            }

            if (this.distractionReason != UltimateTargetManager.DistractionReason.TIRED_OF_CHASING
               && this.entity.position().distanceTo(ultimateTargetPos) < this.entity.getAttributeValue(Attributes.FOLLOW_RANGE) * 2.5) {
               this.makeFocused();
            }

            if (this.distractionReason == UltimateTargetManager.DistractionReason.TIRED_OF_CHASING && ultimateTarget != null && this.ultimateTargetO != null) {
               double speed = this.ultimateTargetO.subtract(ultimateTarget.position()).horizontalDistance();
               if (speed >= 0.39) {
                  if (this.tiredOfChasingTicks < (Integer)WitherStormModConfig.SERVER.boatingForTooLongSeconds.get() * 20) {
                     this.tiredOfChasingTicks++;
                  }
               } else if (this.tiredOfChasingTicks > 0) {
                  this.tiredOfChasingTicks--;
                  if (this.tiredOfChasingTicks == 0) {
                     this.makeFocused();
                  }
               }
            }
         } else {
            if (this.ultimateTarget != null && WorldUtil.hasLineOfSight(this.entity, this.ultimateTarget)) {
               this.cannotSeeTargetFor = 0;
            } else {
               this.cannotSeeTargetFor++;
            }

            Vec3 pos = this.getUltimateTargetPos();
            if (!(Boolean)WitherStormModConfig.SERVER.randomStrollingWhenTargetHidden.get() || pos == null || !(pos.distanceTo(this.entity.position()) < 300.0)) {
               this.randomStrollPos = null;
               this.tillRandomStroll = 0;
            } else if (this.cannotSeeTarget()) {
               if (this.tillRandomStroll == 0) {
                  this.tillRandomStroll = this.randomStrollPos == null ? 600 : 1200 + this.entity.getRandom().nextInt(600);
               }
            } else {
               this.randomStrollPos = null;
               this.tillRandomStroll = 0;
            }

            if (this.isPosInChunkRadius(blockPos)) {
               if (this.ticksSinceStationary > Math.max(2400, stationaryTicks - distance)) {
                  this.ultimateTargetStationary = true;
               }

               if (this.ticksSinceStationary <= Math.max(2400, stationaryTicks)) {
                  this.ticksSinceStationary++;
                  if (this.ticksSinceStationary > (Integer)WitherStormModConfig.SERVER.targetRunawayAttemptMinutes.get() * 1200
                     && (Boolean)WitherStormModConfig.SERVER.targetRunawayAttempts.get()) {
                     this.canCountRunawayAttempt = true;
                  }
               }

               if (this.runawayDiminishTicks > runawayAttemptsDiminish) {
                  this.reduceRunawayAttempts();
                  this.runawayDiminishTicks = 0;
               } else {
                  this.runawayDiminishTicks++;
               }
            } else if (this.ticksSinceStationary <= Math.max(2400, runawayTicks - distance)) {
               if (this.ultimateTargetStationary) {
                  boolean flag1 = true;
                  CommandBlockEntity commandBlock = this.entity.getBowelsCommandBlock();
                  if (commandBlock != null && commandBlock.getHealth() < commandBlock.getMaxHealth()) {
                     flag1 = false;
                  }

                  if (this.entity.getPhase() > 3 && flag1) {
                     boolean flag = (Boolean)WitherStormModConfig.SERVER.randomDistractionChances.get();
                     boolean shouldNotBeDistracted = flag && this.entity.getRandom().nextInt(30) == 1;
                     boolean shouldActuallyBeDistracted = flag && this.entity.getRandom().nextInt(10) == 1;
                     if ((this.canBeDistracted() || shouldActuallyBeDistracted) && !shouldNotBeDistracted) {
                        int minimumDistance = (Integer)WitherStormModConfig.SERVER.minimumDistractionDistance.get();
                        if (this.entity.position().distanceTo(ultimateTargetPos) < this.entity.getAttributeValue(Attributes.FOLLOW_RANGE) + (double)minimumDistance
                           && minimumDistance != 0) {
                           this.setDistractionWait(
                              (Integer)WitherStormModConfig.SERVER.distractionWaitTime.get() * 1200 + this.entity.getRandom().nextInt(1200)
                           );
                        }

                        this.makeDistracted(UltimateTargetManager.DistractionReason.FINISHED_CHASING);
                     }
                  }
               }

               this.ultimateTargetStationary = false;
               this.setCenter(new ChunkPos(blockPos));
               this.ticksSinceStationary = 0;
            } else {
               this.ticksSinceStationary--;
               this.ultimateTargetStationary = true;
            }

            if (this.isTargetStationary()
               && !this.canBeDistracted
               && this.entity.position().distanceTo(ultimateTargetPos)
                  < this.entity.getAttributeValue(Attributes.FOLLOW_RANGE) + (double)((Integer)WitherStormModConfig.SERVER.maximumDistractionDistance.get()).intValue()) {
               this.canBeDistracted = true;
            }

            if (this.getDistractionWait() > 0) {
               this.distractionWait--;
               if (this.distractionWait <= 0
                  && this.canBeDistracted()
                  && this.entity.position().distanceTo(ultimateTargetPos) >= this.entity.getAttributeValue(Attributes.FOLLOW_RANGE) + 50.0
                  && !this.ultimateTargetStationary) {
                  this.makeDistracted(UltimateTargetManager.DistractionReason.FINISHED_CHASING_DELAYED);
               }
            }

            if ((Boolean)WitherStormModConfig.SERVER.boatingForTooLongDistractions.get()
               && this.isTargetStationary()
               && this.canBeDistracted()
               && ultimateTarget != null
               && this.ultimateTargetO != null) {
               double speed = this.ultimateTargetO.subtract(ultimateTarget.position()).horizontalDistance();
               if (speed >= 0.39) {
                  this.tiredOfChasingTicks++;
               } else if (this.tiredOfChasingTicks > 0) {
                  this.tiredOfChasingTicks--;
               }

               if (this.tiredOfChasingTicks > (Integer)WitherStormModConfig.SERVER.boatingForTooLongSeconds.get() * 20) {
                  this.makeDistracted(UltimateTargetManager.DistractionReason.TIRED_OF_CHASING);
               }
            }
         }

         if (this.runawayAttempts >= (Integer)WitherStormModConfig.SERVER.targetRunawayAttemptsRequired.get()
            && (Boolean)WitherStormModConfig.SERVER.targetRunawayAttempts.get()
            && !this.isDistracted()) {
            this.accelerate();
            this.canCountRunawayAttempt = false;
            this.setRunawayAttempts(0);
         }
      }

      if (this.tillRandomStroll > 0) {
         this.tillRandomStroll--;
         if (this.tillRandomStroll == 0) {
            this.findAndSetRandomNearbyStrollPos();
         }
      }

      if (this.tillShowHole > 0) {
         this.tillShowHole--;
         if (this.tillShowHole == 0 && this.entity.getPhase() > 6) {
            this.entity.setShouldShowHole(true);
         }
      }

      if (ultimateTarget != null) {
         assert this.ultimateTarget != null;

         this.ultimateTargetO = this.ultimateTarget.position();
      }
   }

   @Nullable
   public LivingEntity findUltimateTarget(List<ServerPlayer> players) {
      LivingEntity override = null;
      Predicate<Entity> predicate = entityx -> !entityx.isSpectator();
      if ((Boolean)WitherStormModConfig.SERVER.amuletOverride.get()) {
         double d0 = -1.0;

         for (ServerPlayer player : players) {
            if (predicate.test(player)) {
               for (ItemStack stack : player.getInventory().items) {
                  if (stack.is((Item)WitherStormModItems.AMULET.get())) {
                     double d1 = player.distanceToSqr(this.entity);
                     if (d1 < d0 || d0 == -1.0) {
                        d0 = player.distanceToSqr(this.entity);
                        override = player;
                     }
                  }
               }
            }
         }
      }

      ServerLevel level = (ServerLevel)this.entity.level();
      if (this.getTargetOverride() != null) {
         Entity found = level.getEntity(this.getTargetOverride());
         if (found instanceof LivingEntity living && !found.equals(this.entity) && !(found instanceof WitherStormSegmentEntity)) {
            override = living;
         }
      }

      if ((Boolean)WitherStormModConfig.SERVER.witherStormsFollowBiggerStorms.get()) {
         double distance = -1.0;
         WitherStormEntity closest = null;

         for (Entity entity : level.getAllEntities()) {
            if (entity instanceof WitherStormEntity) {
               WitherStormEntity storm = (WitherStormEntity)entity;
               if (storm != this.entity
                  && (double)storm.distanceTo(this.entity) < 1000.0
                  && !storm.isDeadOrPlayingDead()
                  && storm.getConsumedEntities() > this.entity.getConsumedEntities()) {
                  double dist = (double)storm.distanceTo(this.entity);
                  if (distance == -1.0 || dist < distance) {
                     distance = dist;
                     closest = storm;
                  }
               }
            }
         }

         if (closest != null) {
            override = closest;
         }
      }

      LivingEntity finalTarget = (LivingEntity)(override != null
         ? override
         : ((UltimateTargetManager.TargetingType)WitherStormModConfig.SERVER.ultimateTargetingType.get()).getPlayer(this, this.entity, players, predicate));
      WitherStormFindUltimateTargetEvent event = new WitherStormFindUltimateTargetEvent(this.entity, finalTarget);
      MinecraftForge.EVENT_BUS.post(event);
      return event.getOriginalUltimateTarget();
   }

   public void accelerate() {
      if (this.isDistracted()) {
         this.makeFocused();
      }

      this.ticksSinceStationary = (Integer)WitherStormModConfig.SERVER.targetStationaryMinutes.get() * 1200;
      this.ultimateTargetStationary = true;
   }

   public void deaccelerate() {
      this.ticksSinceStationary = 0;
      this.ultimateTargetStationary = false;
   }

   public void setUltimateTarget(@Nullable LivingEntity player) {
      this.ultimateTarget = player;
   }

   public void setAlternativeUltimateTarget(@Nullable BlockPos pos) {
      this.alternativeUltimateTarget = pos;
   }

   @Nullable
   public LivingEntity getUltimateTarget() {
      return this.ultimateTarget;
   }

   @Nullable
   public BlockPos getAlternativeUltimateTarget() {
      return this.alternativeUltimateTarget;
   }

   @Nullable
   public Vec3 getUltimateTargetPos() {
      if (this.blockTargetOverride != null) {
         return Vec3.atCenterOf(this.blockTargetOverride);
      } else if (this.ultimateTarget != null) {
         return this.ultimateTarget.position();
      } else {
         return this.alternativeUltimateTarget != null ? Vec3.atCenterOf(this.alternativeUltimateTarget) : null;
      }
   }

   @Nullable
   public ChunkPos getCenter() {
      return this.center;
   }

   public void setCenter(ChunkPos pos) {
      this.center = pos;
      if (this.canCountRunawayAttempt) {
         this.countRunawayAttempt();
         this.canCountRunawayAttempt = false;
      }
   }

   public boolean isPosInChunkRadius(BlockPos pos) {
      if (pos != null && this.center != null) {
         for (int x = -this.chunkBoundaryRadius; x <= this.chunkBoundaryRadius; x++) {
            for (int z = -this.chunkBoundaryRadius; z <= this.chunkBoundaryRadius; z++) {
               ChunkPos chunk = new ChunkPos(this.center.x + x, this.center.z + z);
               if (chunk.equals(new ChunkPos(pos))) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean isTargetStationary() {
      return this.ultimateTargetStationary;
   }

   public int targetStationaryTicks() {
      return this.ticksSinceStationary;
   }

   public void setTargetStationaryTicks(int amount) {
      this.ticksSinceStationary = amount;
   }

   public int getRunawayAttempts() {
      return this.runawayAttempts;
   }

   public void setRunawayAttempts(int attempts) {
      this.runawayAttempts = attempts;
   }

   public void countRunawayAttempt() {
      this.runawayAttempts++;
   }

   public void reduceRunawayAttempts() {
      double amount = (double)(this.runawayAttempts - 1);
      if (amount >= 0.0) {
         this.runawayAttempts--;
      }
   }

   public int getRunawayDiminishTicks() {
      return this.runawayDiminishTicks;
   }

   @Nullable
   public BlockPos findDistractPos() {
      ServerLevel level = (ServerLevel)this.entity.level();
      BlockPos pos = level.findNearestMapStructure(WitherStormModStructureTags.WITHER_STORM_DISTRACTABLE, this.entity.blockPosition(), 100, false);
      if (pos == null) {
         int times = 10;

         for (int i = 0; i < times && pos == null; i++) {
            int multiplier = (Integer)WitherStormModConfig.SERVER.searchRangeMultiplier.get();
            double range = this.entity.getAttributeValue(Attributes.FOLLOW_RANGE) * 2.0 * (double)multiplier;
            BlockPos current = BlockPos.containing(this.entity.getRandomX(range), this.entity.getY(), this.entity.getRandomZ(range));
            if (this.entity.level().getBlockState(current).is(Blocks.AIR)
               && this.entity.distanceTo(current) > 2000.0 * (double)multiplier
               && Math.sqrt(current.distToCenterSqr(Objects.requireNonNull(this.getUltimateTargetPos()))) > 500.0) {
               pos = current;
            }
         }
      }

      return pos;
   }

   public boolean canBeDistracted() {
      return this.canBeDistracted && (Boolean)WitherStormModConfig.SERVER.targettingDistractionsEnabled.get();
   }

   public void setCanBeDistracted(boolean distractable) {
      this.canBeDistracted = distractable;
   }

   public boolean isDistracted() {
      return this.isDistracted;
   }

   public void markDistracted(boolean distracted) {
      this.isDistracted = distracted;
   }

   public int getTicksSinceDistracted() {
      return this.ticksSinceDistracted;
   }

   public void setTicksSinceDistracted(int ticks) {
      this.ticksSinceDistracted = ticks;
   }

   public void makeDistracted(UltimateTargetManager.DistractionReason reason) {
      if ((Boolean)WitherStormModConfig.SERVER.targettingDistractionsEnabled.get() && this.distractionWait <= 0) {
         this.distractedPos = this.findDistractPos();
         if (this.distractedPos != null) {
            this.isDistracted = true;
            this.canBeDistractedFor = Math.max(
               4800,
               (Integer)WitherStormModConfig.SERVER.distractionTimeMinutes.get() * 1200
                  + this.entity.getRandom().nextInt(12000)
                  - (int)this.entity.distanceTo(this.distractedPos) * 2
            );
            this.canBeDistracted = false;
            this.distractionReason = reason;
         }
      }
   }

   public void makeFocused() {
      this.isDistracted = false;
      this.ticksSinceDistracted = 0;
      this.canBeDistracted = false;
      this.distractedPos = null;
      this.canBeDistractedFor = 0;
      this.distractionWait = 0;
      this.distractionReason = null;
   }

   @Nullable
   public BlockPos getDistractedPos() {
      return this.distractedPos;
   }

   public void setDistractedPos(@Nullable BlockPos pos) {
      this.distractedPos = pos;
   }

   public int getDistractedTickTime() {
      return this.canBeDistractedFor;
   }

   public void setDistractedTickTime(int ticks) {
      this.canBeDistractedFor = ticks;
   }

   public int getDistractionWait() {
      return this.distractionWait;
   }

   public void setDistractionWait(int ticks) {
      this.distractionWait = ticks;
   }

   public int tillShowHole() {
      return this.tillShowHole;
   }

   public void setTillShowHole(int ticks) {
      this.tillShowHole = ticks;
   }

   public void setTargetOverride(@Nullable UUID uuid) {
      this.targetOverride = uuid;
   }

   @Nullable
   public UUID getTargetOverride() {
      return this.targetOverride;
   }

   public void setBlockTargetOverride(@Nullable BlockPos pos) {
      this.blockTargetOverride = pos;
   }

   @Nullable
   public BlockPos getBlockTargetOverride() {
      return this.blockTargetOverride;
   }

   public void findAndSetRandomNearbyStrollPos() {
      Vec3 pos = this.getUltimateTargetPos();
      if (pos != null) {
         BlockPos newPos = null;

         for (int i = 0; i < 10; i++) {
            float angle = this.entity.getRandom().nextFloat() * (float) (Math.PI * 2);
            int x = (int)(Mth.cos(angle) * ((float)this.entity.getRandom().nextInt(50) + 150.0F));
            int z = (int)(Mth.sin(angle) * ((float)this.entity.getRandom().nextInt(50) + 150.0F));
            BlockPos current = new BlockPos(x + Mth.floor(pos.x), Mth.floor(pos.y), z + Mth.floor(pos.z));
            if (this.randomStrollPos == null
               || Math.sqrt(this.randomStrollPos.distSqr(current)) > 100.0 && this.entity.position().subtract(Vec3.atCenterOf(current)).horizontalDistance() > 100.0) {
               newPos = current;
               break;
            }
         }

         this.randomStrollPos = newPos;
      } else {
         this.randomStrollPos = null;
      }
   }

   @Nullable
   public BlockPos getRandomStrollPos() {
      return this.randomStrollPos;
   }

   public boolean isRandomStrolling() {
      return this.randomStrollPos != null;
   }

   public boolean cannotSeeTarget() {
      return this.cannotSeeTargetFor > 600;
   }

   public void save(CompoundTag compound) {
      if (this.getAlternativeUltimateTarget() != null) {
         compound.put("AlternativeUltimateTarget", NbtUtils.writeBlockPos(this.getAlternativeUltimateTarget()));
      }

      if (this.getCenter() != null) {
         compound.put("UltimateTargetChunkPos", WitherStormModNBTUtil.writeChunkPos(this.getCenter()));
      }

      compound.putInt("TargetStationaryTicks", this.targetStationaryTicks());
      compound.putInt("TargetRunawayAttempts", this.getRunawayAttempts());
      if (this.getTargetOverride() != null) {
         compound.putUUID("TargetOverride", this.getTargetOverride());
      }

      if (this.getBlockTargetOverride() != null) {
         compound.put("BlockTargetOverride", NbtUtils.writeBlockPos(this.getBlockTargetOverride()));
      }

      CompoundTag ultimateTargetDistractions = new CompoundTag();
      if (this.getDistractedPos() != null) {
         ultimateTargetDistractions.put("DistractedPos", NbtUtils.writeBlockPos(this.getDistractedPos()));
      }

      ultimateTargetDistractions.putBoolean("CanBeDistracted", this.canBeDistracted());
      ultimateTargetDistractions.putBoolean("IsDistracted", this.isDistracted());
      ultimateTargetDistractions.putInt("TicksSinceDistracted", this.getTicksSinceDistracted());
      ultimateTargetDistractions.putInt("CanBeDistractedFor", this.getDistractedTickTime());
      ultimateTargetDistractions.putInt("DistractionWait", this.getDistractionWait());
      if (this.distractionReason != null) {
         ultimateTargetDistractions.putInt("DistractionReason", this.distractionReason.ordinal());
      }

      ultimateTargetDistractions.putInt("TiredOfChasingTicks", this.tiredOfChasingTicks);
      compound.put("UltimateTargetDistraction", ultimateTargetDistractions);
      if (this.randomStrollPos != null) {
         compound.put("RandomStrollPos", NbtUtils.writeBlockPos(this.randomStrollPos));
      }

      compound.putInt("RandomStrollTimer", this.tillRandomStroll);
      if (this.ignoredTarget != null) {
         compound.putUUID("IgnoredTarget", this.ignoredTarget);
      }

      compound.putInt("IgnoringTargetFor", this.ignoringTargetFor);
      compound.putInt("CannotReachTargetFor", this.cannotReachTargetFor);
      compound.putInt("TimeTillIgnoreTarget", this.timeTillIgnoreTarget);
      compound.putInt("CannotSeeTargetFor", this.cannotSeeTargetFor);
   }

   public void read(CompoundTag compound) {
      if (compound.contains("AlternativeUltimateTarget")) {
         WitherStormModNBTUtil.readBlockPos(compound, "AlternativeUltimateTarget").ifPresent(this::setAlternativeUltimateTarget);
      }

      if (compound.contains("UltimateTargetChunkPos")) {
         this.center = WitherStormModNBTUtil.readChunkPos(compound.getCompound("UltimateTargetChunkPos"));
      }

      this.setTargetStationaryTicks(compound.getInt("TargetStationaryTicks"));
      this.setRunawayAttempts(compound.getInt("TargetRunawayAttempts"));
      if (compound.contains("TargetOverride")) {
         this.setTargetOverride(compound.getUUID("TargetOverride"));
      }

      if (compound.contains("BlockTargetOverride")) {
         WitherStormModNBTUtil.readBlockPos(compound, "BlockTargetOverride").ifPresent(this::setBlockTargetOverride);
      }

      CompoundTag ultimateTargetDistractions = compound.getCompound("UltimateTargetDistraction");
      if (ultimateTargetDistractions.contains("DistractedPos")) {
         WitherStormModNBTUtil.readBlockPos(ultimateTargetDistractions, "DistractedPos").ifPresent(this::setDistractedPos);
      }

      this.setCanBeDistracted(ultimateTargetDistractions.getBoolean("CanBeDistracted"));
      this.markDistracted(ultimateTargetDistractions.getBoolean("IsDistracted"));
      this.setTicksSinceDistracted(ultimateTargetDistractions.getInt("TicksSinceDistracted"));
      this.setDistractedTickTime(ultimateTargetDistractions.getInt("CanBeDistractedFor"));
      this.setDistractionWait(ultimateTargetDistractions.getInt("DistractionWait"));
      if (ultimateTargetDistractions.contains("DistractionReason", 3)) {
         int ordinal = ultimateTargetDistractions.getInt("DistractionReason");
         if (ordinal >= 0 && ordinal < UltimateTargetManager.DistractionReason.values().length) {
            this.distractionReason = UltimateTargetManager.DistractionReason.values()[ordinal];
         }
      }

      this.tiredOfChasingTicks = ultimateTargetDistractions.getInt("TiredOfChasingTicks");
      if (compound.contains("RandomStrollPos")) {
         this.randomStrollPos = WitherStormModNBTUtil.readBlockPos(compound, "RandomStrollPos").orElse(null);
      }

      this.tillRandomStroll = compound.getInt("RandomStrollTimer");
      if (compound.hasUUID("IgnoredTarget")) {
         this.ignoredTarget = compound.getUUID("IgnoredTarget");
      }

      this.ignoringTargetFor = compound.getInt("IgnoringTargetFor");
      this.cannotReachTargetFor = compound.getInt("CannotReachTargetFor");
      this.timeTillIgnoreTarget = compound.getInt("TimeTillIgnoreTarget");
      this.cannotSeeTargetFor = compound.getInt("CannotSeeTargetFor");
   }

   public static enum DistractionReason {
      FINISHED_CHASING,
      FINISHED_CHASING_DELAYED,
      TIRED_OF_CHASING,
      FORCED;
   }

   public static enum TargetingType {
      NEAREST {
         @Nullable
         @Override
         public ServerPlayer getPlayer(UltimateTargetManager manager, WitherStormEntity storm, List<ServerPlayer> players, Predicate<Entity> predicate) {
            double d0 = -1.0;
            ServerPlayer closest = null;

            for (ServerPlayer player : players) {
               if (predicate.test(player)) {
                  double d1 = player.distanceToSqr(storm);
                  if (d1 < d0 || d0 == -1.0) {
                     d0 = player.distanceToSqr(storm);
                     closest = player;
                  }
               }
            }

            return closest;
         }
      },
      FARTHEST {
         @Nullable
         @Override
         public ServerPlayer getPlayer(UltimateTargetManager manager, WitherStormEntity storm, List<ServerPlayer> players, Predicate<Entity> predicate) {
            long currentTime = System.currentTimeMillis();
            long timeThreshold = (long)((Integer)WitherStormModConfig.SERVER.farthestTargetingTime.get() * 60 * 1000);
            ServerPlayer farthestPlayer = manager.farthestPlayer != null ? (ServerPlayer)storm.level().getPlayerByUUID(manager.farthestPlayer) : null;
            if (currentTime - manager.farthestLastSwitchTime >= timeThreshold || farthestPlayer == null) {
               double d0 = -1.0;

               for (ServerPlayer player : players) {
                  if (predicate.test(player)) {
                     double d1 = player.distanceToSqr(storm);
                     if (d1 > d0 || d0 == -1.0) {
                        d0 = player.distanceToSqr(storm);
                        manager.farthestPlayer = player.getUUID();
                        farthestPlayer = player;
                        UltimateTargetManager.LOGGER
                           .info(
                              "FARTHEST: Farthest Player was "
                                 + player
                                 + ", Going to them for "
                                 + WitherStormModConfig.SERVER.farthestTargetingTime.get()
                                 + " minute(s)"
                           );
                     }
                  }
               }

               manager.farthestLastSwitchTime = currentTime;
            }

            return farthestPlayer;
         }
      },
      GROUP {
         @Nullable
         @Override
         public ServerPlayer getPlayer(UltimateTargetManager manager, WitherStormEntity storm, List<ServerPlayer> players, Predicate<Entity> predicate) {
            double size = -1.0;
            ServerPlayer toTarget = null;

            for (ServerPlayer player : players) {
               if (predicate.test(player)) {
                  AABB box = player.getBoundingBox().inflate(20.0);
                  List<ServerPlayer> nearby = storm.level().getEntitiesOfClass(ServerPlayer.class, box, EntitySelector.LIVING_ENTITY_STILL_ALIVE.and(EntitySelector.NO_SPECTATORS));
                  if ((double)nearby.size() > size) {
                     size = (double)nearby.size();
                     toTarget = player;
                  }
               }
            }

            return toTarget;
         }
      },
      NONE {
         @Nullable
         @Override
         public ServerPlayer getPlayer(UltimateTargetManager manager, WitherStormEntity storm, List<ServerPlayer> players, Predicate<Entity> predicate) {
            return null;
         }
      },
      RANDOM_STROLL {
         @Nullable
         @Override
         public ServerPlayer getPlayer(UltimateTargetManager manager, WitherStormEntity storm, List<ServerPlayer> players, Predicate<Entity> predicate) {
            return null;
         }
      },
      RANDOM_PLAYER {
         @Nullable
         @Override
         public ServerPlayer getPlayer(UltimateTargetManager manager, WitherStormEntity storm, List<ServerPlayer> players, Predicate<Entity> predicate) {
            long currentTime = System.currentTimeMillis();
            long timeThreshold = 300000L;
            ServerPlayer randomPlayer = manager.randomPlayer != null ? (ServerPlayer)storm.level().getPlayerByUUID(manager.randomPlayer) : null;
            if (currentTime - manager.randomPlayerLastSwitchTime >= timeThreshold || randomPlayer == null) {
               Random random = new Random();
               List<ServerPlayer> survivalPlayers = players.stream().filter(player -> player.gameMode.isSurvival()).filter(predicate).toList();
               if (!survivalPlayers.isEmpty()) {
                  int randomizer = random.nextInt(survivalPlayers.size());
                  randomPlayer = survivalPlayers.get(randomizer);
                  manager.randomPlayer = randomPlayer.getUUID();
                  UltimateTargetManager.LOGGER.info("RANDOM_PLAYER: Chose a player: " + randomPlayer + ", Going to them for 5 minutes");
               } else {
                  ServerPlayer nearestPlayer = NEAREST.getPlayer(manager, storm, players, predicate);
                  if (nearestPlayer != null) {
                     manager.randomPlayer = nearestPlayer.getUUID();
                     randomPlayer = nearestPlayer;
                     UltimateTargetManager.LOGGER.info("RANDOM_PLAYER: Couldn't find a valid player in Survival, changing to NEAREST for 5 minutes");
                  }
               }

               manager.randomPlayerLastSwitchTime = currentTime;
            }

            return randomPlayer;
         }
      },
      RANDOMIZED {
         @Nullable
         @Override
         public ServerPlayer getPlayer(UltimateTargetManager manager, WitherStormEntity storm, List<ServerPlayer> players, Predicate<Entity> predicate) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - manager.randomizedLastSwitchTime >= (long)((Integer)WitherStormModConfig.SERVER.randomizedTargetingTime.get() * 60 * 1000)) {
               List<UltimateTargetManager.TargetingType> availableTypes;
               if (players.size() <= 1) {
                  availableTypes = Arrays.asList(NEAREST, RANDOM_STROLL, RANDOM_STROLL_NEAR_PLAYER);
               } else {
                  availableTypes = Arrays.asList(NEAREST, FARTHEST, GROUP, RANDOM_STROLL, RANDOM_PLAYER);
               }

               UltimateTargetManager.TargetingType lastType = manager.randomizedType;

               do {
                  manager.randomizedType = availableTypes.get(storm.getRandom().nextInt(availableTypes.size()));
               } while (manager.randomizedType == lastType);

               manager.randomizedLastSwitchTime = currentTime;
               UltimateTargetManager.LOGGER.info("RANDOMIZED: Targeting Type has been set to: " + manager.randomizedType);
               if (storm.getRandom().nextInt(1, 11) == 1
                  && storm.getPhase() >= 4
                  && (Boolean)WitherStormModConfig.SERVER.randomlySpeedUpWithTargetChange.get()) {
                  manager.accelerate();
                  UltimateTargetManager.LOGGER.info("RANDOMIZED: The Wither Storm will be speeding up!!!");
               }
            }

            assert manager.randomizedType != null;

            return manager.randomizedType.getPlayer(manager, storm, players, predicate);
         }
      },
      RANDOM_STROLL_NEAR_PLAYER {
         @Nullable
         @Override
         public ServerPlayer getPlayer(UltimateTargetManager manager, WitherStormEntity storm, List<ServerPlayer> players, Predicate<Entity> predicate) {
            return null;
         }
      };

      @Nullable
      public abstract ServerPlayer getPlayer(UltimateTargetManager var1, WitherStormEntity var2, List<ServerPlayer> var3, Predicate<Entity> var4);
   }
}
