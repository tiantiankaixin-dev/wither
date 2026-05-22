package nonamecrackers2.witherstormmod.common.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.datafixers.util.Pair;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Entity.MovementEmission;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.Tags.Blocks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.WitherStormWorldInteractions;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.clustersource.BlockClusterSource;
import nonamecrackers2.witherstormmod.api.common.ai.witherstorm.pullbehavior.WitherStormPullBehavior;
import nonamecrackers2.witherstormmod.api.common.entity.WitherStormBase;
import nonamecrackers2.witherstormmod.api.common.event.CanWitherStormTargetMobEvent;
import nonamecrackers2.witherstormmod.api.common.event.WitherStormChangePhaseEvent;
import nonamecrackers2.witherstormmod.api.common.event.WitherStormConsumeEvent;
import nonamecrackers2.witherstormmod.api.common.event.WitherStormEvolveEvent;
import nonamecrackers2.witherstormmod.api.common.event.WitherStormModifyEvolutionSpeedEvent;
import nonamecrackers2.witherstormmod.api.common.event.WitherStormModifyFlyingSpeedEvent;
import nonamecrackers2.witherstormmod.client.capability.WitherStormLoopingSoundManager;
import nonamecrackers2.witherstormmod.common.capability.PlayerWitherStormData;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.capability.WitherStormBowelsManager;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.BowelsInstanceManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.IgnoredTargetsManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.PersistentTrackedEntities;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.PlayDeadManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.SegmentsManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.SymbiontSummoningManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.WitherStormSyncHelper;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.controller.WitherStormBodyController;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.controller.WitherStormLookController;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.AdditionalHead;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.HeadManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.MainHead;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.WitherStormHead;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.ultimatetarget.UltimateTargetManager;
import nonamecrackers2.witherstormmod.common.entity.goal.DoNothingGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.FindNearestFormidibombGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.LookAtDistractionGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.LookAtFormidibombGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.LookAtTargetGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.NearestBlockDistractionGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.RemovableGoals;
import nonamecrackers2.witherstormmod.common.entity.goal.RemovableGoalsManager;
import nonamecrackers2.witherstormmod.common.entity.goal.WitherStormHurtByTargetGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.WitherStormLookRandomlyGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.WitherStormNearestDistractionGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.WitherStormPriorityTargetingGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.WitherStormTargetingGoal;
import nonamecrackers2.witherstormmod.common.entity.section.CollisionActionSection;
import nonamecrackers2.witherstormmod.common.entity.section.FallingSection;
import nonamecrackers2.witherstormmod.common.entity.section.Section;
import nonamecrackers2.witherstormmod.common.init.WitherStormModAttributes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModDamageTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.CreateLoopingSoundMessage;
import nonamecrackers2.witherstormmod.common.packet.GlobalSoundMessage;
import nonamecrackers2.witherstormmod.common.packet.PlayerMotionMessage;
import nonamecrackers2.witherstormmod.common.packet.RemoveSoundLoopMessage;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;
import nonamecrackers2.witherstormmod.common.packet.StormSoundPositionMessage;
import nonamecrackers2.witherstormmod.common.particle.TractorBeamParticleOptions;
import nonamecrackers2.witherstormmod.common.predicate.EntityPredicateBuilder;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModEntityTags;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModItemTags;
import nonamecrackers2.witherstormmod.common.util.ClusterBuilderHelper;
import nonamecrackers2.witherstormmod.common.util.DebrisCluster;
import nonamecrackers2.witherstormmod.common.util.DebrisRingSettings;
import nonamecrackers2.witherstormmod.common.util.EntitySyncableData;
import nonamecrackers2.witherstormmod.common.util.EvolutionProfiler;
import nonamecrackers2.witherstormmod.common.util.StormHeadOffsets;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import org.jetbrains.annotations.NotNull;

public class WitherStormEntity extends Monster implements PowerableMob, EntitySyncableData, BossThemeEntity, WitherStormBase, ChunkLoader {
   private static final List<WitherStormEntity.DataAccessorHolder<?>> DATA_ACCESSORS = Lists.newArrayList();
   protected static final EntityDataAccessor<Integer> INVULNERABLE = SynchedEntityData.defineId(WitherStormEntity.class, EntityDataSerializers.INT);
   protected static final EntityDataAccessor<Integer> STARTING_INVULNERABLE = SynchedEntityData.defineId(
      WitherStormEntity.class, EntityDataSerializers.INT
   );
   protected static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(WitherStormEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> CONSUMED_ENTITIES = SynchedEntityData.defineId(WitherStormEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> MIRRORED = SynchedEntityData.defineId(WitherStormEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> SHOULD_SHOW_HOLE = SynchedEntityData.defineId(WitherStormEntity.class, EntityDataSerializers.BOOLEAN);
   private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("9B8DA22B-138B-4B68-879D-3FD329FAF903");
   private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("C806DBFA-2B10-4BEA-B16C-C3233707399C");
   public static final Predicate<LivingEntity> DESTROYER_LIVING_ENTITY_SELECTOR = ((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)EntityPredicateBuilder.and()
                                          .addTest(Entity::isAttackable))
                                       .isNotTag(WitherStormModEntityTags.WITHER_STORM_TARGETING_BLACKLIST)
                                       .isNotInstanceOf(WitherBoss.class))
                                    .isNotInstanceOf(FlyingMob.class))
                                 .isNotInstanceOf(EnderDragon.class))
                              .isNotInstanceOf(WitherStormEntity.class))
                           .isNotInstanceOf(WitherSickened.class))
                        .isNotInstanceOf(CommandBlockEntity.class))
                     .isNotInstanceOf(WitheredSymbiontEntity.class))
                  .isNotInstanceOf(WitherStormHeadEntity.class))
               .isNotInstanceOf(TentacleEntity.class))
            .isNotInstanceOf(AmbientCreature.class))
         .isNotInstanceOf(ArmorStand.class))
      .build();
   public static final Predicate<LivingEntity> HUNCHBACK_LIVING_ENTITY_SELECTOR = ((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)EntityPredicateBuilder.and()
               .addTest((Predicate)DESTROYER_LIVING_ENTITY_SELECTOR))
            .isNotInstanceOf(AbstractSchoolingFish.class))
         .isNotInstanceOf(Squid.class))
      .build();
   public static final Predicate<Entity> DISTRACTION_SELECTOR = ((EntityPredicateBuilder)EntityPredicateBuilder.or().isInstanceOf(FireworkRocketEntity.class))
      .build();
   public static final Predicate<Entity> TRACTOR_BEAM_PULLABLE = ((EntityPredicateBuilder)((EntityPredicateBuilder)EntityPredicateBuilder.or()
            .isExactly(EntityType.TNT)
            .isInstanceOf(Boat.class))
         .isInstanceOf(AbstractMinecart.class))
      .build();
   public static final Predicate<Entity> ABSORBABLE = ((EntityPredicateBuilder)((EntityPredicateBuilder)EntityPredicateBuilder.and()
            .isNotInstanceOf(Player.class))
         .isNotInstanceOf(PrimedTnt.class))
      .build();
   public static final Predicate<Entity> CAN_TRAVEL_TO_BOWELS = ((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)EntityPredicateBuilder.and()
                  .isNotInstanceOf(BlockClusterEntity.class))
               .isNotInstanceOf(Projectile.class))
            .isNotInstanceOf(ItemEntity.class))
         .isNotInstanceOf(Phantom.class))
      .build();
   public static final Predicate<Entity> PICKABLE = ((EntityPredicateBuilder)EntityPredicateBuilder.or().isInstanceOf(ItemEntity.class)).addTest(e -> {
      if (e instanceof Slime slime && slime.isTiny()) {
         return true;
      }

      return false;
   }).build();
   private static final EntityDimensions STARTING_SIZE = EntityDimensions.scalable(0.9F, 3.5F);
   private static final EntityDimensions DESTROYER_SIZE = EntityDimensions.scalable(10.0F, 30.0F);
   private static final EntityDimensions EVOLVED_DESTROYER_SIZE = EntityDimensions.scalable(10.0F, 60.0F);
   private static final EntityDimensions DEVOURER_SIZE = EntityDimensions.scalable(15.0F, 90.0F);
   private static final EntityDimensions EVOLVED_DEVOURER_SIZE = EntityDimensions.scalable(15.0F, 120.0F);
   private static final double[] SEGMENT_DESIRED_X = new double[]{75.0, 75.0};
   private static final double[] SEGMENT_DESIRED_Z = new double[]{50.0, -50.0};
   public static final String[] REMOVABLE_LOOK_GOALS = new String[]{"lookGoals0", "lookGoals1", "lookGoals2"};
   public static final String[] REMOVABLE_TARGET_GOALS = new String[]{"targetGoals0", "targetGoals1", "targetGoals2"};
   public static final int MAX_PHASE = 7;
   protected final HeadManager headManager;
   protected final PlayDeadManager playDeadManager;
   protected final Optional<UltimateTargetManager> targetManager;
   protected final Optional<SegmentsManager> segments;
   protected final Optional<SymbiontSummoningManager> summoningManager;
   protected final Optional<BowelsInstanceManager> bowelsInstance;
   protected RemovableGoalsManager removableGoals;
   protected final Optional<ServerBossEvent> bossEvent;
   private int lastConsumedEntities;
   private int tentacleTickCount;
   private int tentacleTickCountO;
   private int destroyBlocksTick;
   protected int entityConsumptionRadius = 16;
   protected float clusterRadius = 1.0F;
   public int idleTargetTicks;
   public boolean chunkloads = true;
   protected Section[] sections = new Section[3];
   private FallingSection fallingSection;
   public boolean partsEnabled = true;
   public boolean shouldFollowUltimateTarget = true;
   public boolean shouldPlaySoundLoop = true;
   public boolean shouldPlayGlobalSounds = true;
   public int witherStormDeathTime;
   public boolean shouldDoCustomMovement = true;
   protected List<DebrisCluster> debrisClusters = ImmutableList.of();
   protected List<DebrisCluster> hunchbackDebrisClusters = ImmutableList.of();
   protected List<DebrisRingSettings> debrisRings = ImmutableList.of();
   private boolean isOnDistantRenderer;
   protected float onGroundAnimation;
   protected float onGroundAnimationO;
   public float xBodyRot;
   public float xBodyRotO;
   public boolean shouldIgnoreFormidibomb;
   private List<GoalSelector> headGoalSelectors;
   private List<GoalSelector> headTargetSelectors;
   private int flickerTime;
   private int nextFlicker = 40;
   private boolean shouldFlicker;
   private final List<ServerPlayer> playersTracking = Lists.newArrayList();
   private float shineAlpha;
   private float shineAlphaO;
   private boolean resummoned;
   private final EvolutionProfiler evolutionProfiler = new EvolutionProfiler();
   private final Map<UUID, CompoundTag> consumedPets = Maps.newHashMap();
   private final PersistentTrackedEntities trackedEntities = new PersistentTrackedEntities();
   private final IgnoredTargetsManager ignoredTargets;
   private final List<BlockPos> playingJukeboxes = Lists.newArrayList();
   private int nextUndergroundRumble = 1200 + this.random.nextInt(1200);
   private float shineScale;
   private float phaseProgress;
   private boolean isLocked;
   private int lastFlyingSwitchTime;
   private double currentFlyingHeight = 10.0;
   private float lerpBodyXRot;
   private float lerpBodyYRot;
   private int bodyLerpSteps;
   private Predicate<LivingEntity> entitySelector;

   public WitherStormEntity(EntityType<? extends WitherStormEntity> entityTypeIn, Level worldIn) {
      super(entityTypeIn, worldIn);
      this.setHealth(this.getMaxHealth());
      this.getNavigation().setCanFloat(true);
      this.xpReward = 10000;
      this.noCulling = true;
      this.lookControl = new WitherStormLookController(this);
      this.buildSections();
      this.segments = this.makeSegmentsManager();
      this.bowelsInstance = this.makeBowelsInstanceManager();
      this.headManager = this.makeHeadManager();
      this.targetManager = this.makeUltimateTargetManager();
      this.summoningManager = this.makeSummoningManager();
      this.playDeadManager = new PlayDeadManager(this);
      this.bossEvent = this.makeBossEvent();
      this.ignoredTargets = new IgnoredTargetsManager(this);
   }

   protected HeadManager makeHeadManager() {
      return new HeadManager(this, StormHeadOffsets.MAIN);
   }

   protected Optional<SegmentsManager> makeSegmentsManager() {
      return Optional.of(new SegmentsManager(this));
   }

   protected Optional<SymbiontSummoningManager> makeSummoningManager() {
      return Optional.of(new SymbiontSummoningManager(this));
   }

   protected Optional<BowelsInstanceManager> makeBowelsInstanceManager() {
      return Optional.of(new BowelsInstanceManager(this));
   }

   protected Optional<UltimateTargetManager> makeUltimateTargetManager() {
      return Optional.of(new UltimateTargetManager(this));
   }

   protected Optional<ServerBossEvent> makeBossEvent() {
      return Optional.of((ServerBossEvent)new ServerBossEvent(this.getDisplayName(), BossBarColor.PURPLE, BossBarOverlay.PROGRESS).setDarkenScreen(true));
   }

   protected void buildSections() {
      this.sections[0] = new Section(this, 30.0F, 15.0F, 13.0, 28.0, 0.0, 4);
      this.sections[1] = new Section(this, 30.0F, 15.0F, -13.0, 28.0, 0.0, 4);
      this.fallingSection = new FallingSection(this, 50.0F, 70.0F, 0.0, 60.0, 30.0, 4);
      this.sections[2] = new CollisionActionSection(
            this, 16.0F, 16.0F, -3.0, 34.0, -24.0, storm -> storm.isBeingTornApart() && storm.getPhase() >= 7, entity -> {
               if (!this.level().isClientSide && (CAN_TRAVEL_TO_BOWELS.test(entity) || entity instanceof ThrownEnderpearl) && !this.alreadyATarget(entity, true)) {
                  if (entity instanceof ThrownEnderpearl pearl) {
                     Entity owner = pearl.getOwner();
                     if (owner instanceof ServerPlayer player) {
                        if (player.connection.connection.isConnected() && player.level() == pearl.level() && !player.isSleeping()) {
                           if (player.isPassenger()) {
                              player.stopRiding();
                           }

                           player.teleportTo(pearl.getX(), pearl.getY(), pearl.getZ());
                           player.fallDistance = 0.0F;
                           player.hurt(this.damageSources().fall(), 5.0F);
                           this.sendToBowels(owner);
                        }
                     } else if (owner != null) {
                        owner.teleportTo(pearl.getX(), pearl.getY(), pearl.getZ());
                        owner.fallDistance = 0.0F;
                        this.sendToBowels(owner);
                     }

                     pearl.discard();
                  } else {
                     this.sendToBowels(entity);
                  }
               }
            }
         )
         .setColor(0.0F, 1.0F, 1.0F);
   }

   @NotNull
   protected BodyRotationControl createBodyControl() {
      return new WitherStormBodyController(this);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(INVULNERABLE, 0);
      this.entityData.define(STARTING_INVULNERABLE, (Integer)WitherStormModConfig.SERVER.invulnerabilityTime.get() * 20);
      this.entityData.define(PHASE, 0);
      this.entityData.define(CONSUMED_ENTITIES, 0);
      this.entityData.define(MIRRORED, false);
      this.entityData.define(SHOULD_SHOW_HOLE, false);

      for (WitherStormEntity.DataAccessorHolder<?> holder : DATA_ACCESSORS) {
         holder.defineTo(this.entityData);
      }
   }

   protected void registerGoals() {
      this.entitySelector = entity -> this.getPhase() <= 3 ? HUNCHBACK_LIVING_ENTITY_SELECTOR.test(entity) : DESTROYER_LIVING_ENTITY_SELECTOR.test(entity);
      this.headGoalSelectors = ImmutableList.<GoalSelector>builder()
         .add(new GoalSelector(this.level().getProfilerSupplier()))
         .add(new GoalSelector(this.level().getProfilerSupplier()))
         .build();
      this.headTargetSelectors = ImmutableList.<GoalSelector>builder()
         .add(new GoalSelector(this.level().getProfilerSupplier()))
         .add(new GoalSelector(this.level().getProfilerSupplier()))
         .build();
      this.removableGoals = new RemovableGoalsManager();
      this.goalSelector.addGoal(0, new DoNothingGoal(this, 0));
      this.removableGoals
         .put(
            REMOVABLE_LOOK_GOALS[0],
            RemovableGoals.Builder.builder()
               .put(1, new LookAtFormidibombGoal(this))
               .put(2, new LookAtDistractionGoal(this, 0, () -> 300.0))
               .put(4, new LookAtTargetGoal<>(this, 0, s -> s.getPhase() > 3 ? 50 : 3))
               .build(this.goalSelector)
         );
      this.goalSelector.addGoal(7, new WitherStormLookRandomlyGoal(this, 0, 120));
      this.removableGoals
         .put(
            REMOVABLE_TARGET_GOALS[0],
            RemovableGoals.Builder.builder()
               .put(0, new FindNearestFormidibombGoal(this, 0))
               .put(1, new WitherStormHurtByTargetGoal(this, this.entitySelector))
               .put(2, new WitherStormNearestDistractionGoal(this, 0, DISTRACTION_SELECTOR, 2))
               .put(3, new WitherStormPriorityTargetingGoal(this, this.entitySelector, 0))
               .put(4, new NearestBlockDistractionGoal(this, 0))
               .put(5, new WitherStormTargetingGoal(this, this.entitySelector, 0))
               .build(this.targetSelector)
         );
      this.addHeadGoals(
         (selector, head) -> {
            selector.addGoal(0, new DoNothingGoal(this, head));
            this.removableGoals
               .put(
                  REMOVABLE_LOOK_GOALS[head],
                  RemovableGoals.Builder.builder()
                     .put(1, new LookAtDistractionGoal(this, head, () -> 300.0))
                     .put(2, new LookAtTargetGoal<>(this, head, s -> s.getPhase() > 3 ? 50 : 3))
                     .build(selector)
               );
            selector.addGoal(3, new WitherStormLookRandomlyGoal(this, head, 120));
         }
      );
      this.addHeadTargetGoals(
         (selector, head) -> this.removableGoals
               .put(
                  REMOVABLE_TARGET_GOALS[head],
                  RemovableGoals.Builder.builder()
                     .put(0, new WitherStormNearestDistractionGoal(this, head, DISTRACTION_SELECTOR, 2))
                     .put(1, new WitherStormPriorityTargetingGoal(this, this.entitySelector, head))
                     .put(3, new NearestBlockDistractionGoal(this, head))
                     .put(4, new WitherStormTargetingGoal(this, this.entitySelector, head))
                     .build(selector)
               )
      );
   }

   public Predicate<LivingEntity> getEntitySelector() {
      return this.entitySelector;
   }

   public void removeAllGoals(Predicate<Goal> predicate) {
      super.removeAllGoals(predicate);
      this.headGoalSelectors.forEach(sel -> sel.removeAllGoals(predicate));
   }

   protected void addHeadGoals(BiConsumer<GoalSelector, Integer> goals) {
      for (int i = 0; i < this.headGoalSelectors.size(); i++) {
         goals.accept(this.headGoalSelectors.get(i), i + 1);
      }
   }

   protected void addHeadTargetGoals(BiConsumer<GoalSelector, Integer> goals) {
      for (int i = 0; i < this.headTargetSelectors.size(); i++) {
         goals.accept(this.headTargetSelectors.get(i), i + 1);
      }
   }

   public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add((Attribute)WitherStormModAttributes.TARGET_STATIONARY_FLYING_SPEED.get())
         .add((Attribute)WitherStormModAttributes.SLOW_FLYING_SPEED.get())
         .add((Attribute)WitherStormModAttributes.EVOLUTION_SPEED.get(), 1.0)
         .add(Attributes.FLYING_SPEED, 0.0)
         .add(Attributes.MAX_HEALTH, 400.0)
         .add(Attributes.MOVEMENT_SPEED, 0.6)
         .add(Attributes.FOLLOW_RANGE, 120.0)
         .add((Attribute)WitherStormModAttributes.HUNCHBACK_FOLLOW_RANGE.get(), 40.0)
         .add(Attributes.ARMOR, 8.0)
         .add(Attributes.ATTACK_DAMAGE, 3.5);
   }

   public boolean shouldSpeedUp() {
      UltimateTargetManager manager = this.targetManager.orElse(null);
      if (manager == null) {
         return false;
      } else {
         Vec3 pos = manager.getUltimateTargetPos();
         return this.getPhase() >= 4 && WitherStormModConfig.SERVER.shouldChaseWhenTargetStopped.get() && pos != null
            ? manager.isTargetStationary() && this.position().distanceTo(pos) > 122.0 && !manager.isDistracted()
            : false;
      }
   }

   public double getFlyingSpeed(Vec3 ultimateTargetDist) {
      if (this.getPhase() > 3) {
         double flyingSpeed = this.getAttributeValue(Attributes.FLYING_SPEED);
         if (this.shouldSpeedUp()) {
            flyingSpeed += Math.min(this.getDefaultChasingSpeed(), this.position().distanceTo(Objects.requireNonNull(this.getUltimateTargetPos())) * 0.001);
         } else if (ultimateTargetDist.horizontalDistance() > 205.0) {
            flyingSpeed += this.getDefaultNormalSpeed() + 0.03;
         } else {
            flyingSpeed += this.getDefaultNormalSpeed();
         }

         return flyingSpeed;
      } else {
         return this.getAttributeValue((Attribute)WitherStormModAttributes.SLOW_FLYING_SPEED.get());
      }
   }

   protected double getDefaultChasingSpeed() {
      return this.attributeOrConfigValue(
         (Attribute)WitherStormModAttributes.TARGET_STATIONARY_FLYING_SPEED.get(), WitherStormModConfig.SERVER.chasingFlyingSpeed
      );
   }

   protected double getDefaultNormalSpeed() {
      return this.attributeOrConfigValue((Attribute)WitherStormModAttributes.SLOW_FLYING_SPEED.get(), WitherStormModConfig.SERVER.normalFlyingSpeed);
   }

   public void aiStep() {
      if (this.bodyLerpSteps > 0) {
         this.xBodyRot = this.xBodyRot + (this.lerpBodyXRot - this.xBodyRot) / (float)this.bodyLerpSteps;
         this.yBodyRot = this.yBodyRot + Mth.wrapDegrees(this.lerpBodyYRot - this.yBodyRot) / (float)this.bodyLerpSteps;
         this.bodyLerpSteps--;
      }

      Vec3 vector3d = this.getDeltaMovement().multiply(1.0, 0.6, 1.0);
      if (!this.level().isClientSide && !this.shouldDoNothing()) {
         this.targetManager.ifPresent(UltimateTargetManager::tick);
         this.summoningManager.ifPresent(SymbiontSummoningManager::tick);
         if (!this.isDeadOrPlayingDead()) {
            vector3d = this.doFlying(vector3d);
         }
      }

      if (this.shouldDoCustomMovement()) {
         this.setDeltaMovement(vector3d);
      }

      if (vector3d.horizontalDistanceSqr() > 0.05) {
         this.setYRot((float)Mth.atan2(vector3d.z, vector3d.x) * (180.0F / (float)Math.PI) - 90.0F);
      }

      super.aiStep();
      if (!this.isDeadOrPlayingDead() && !this.shouldDoNothing()) {
         this.headManager.aiStep();
      }

      boolean flag = this.isPowered();
      if (this.getPhase() < 4) {
         for (WitherStormHead head : this.headManager.getHeads()) {
            Vec3 pos = head.getHeadPos();
            double d8 = pos.x;
            double d10 = pos.y;
            double d2 = pos.z;
            if (head instanceof MainHead) {
               d8 = this.getX();
               d10 = this.getEyeY();
               d2 = this.getZ();
            }

            this.level()
               .addParticle(
                  ParticleTypes.SMOKE,
                  d8 + this.random.nextGaussian() * 0.3F,
                  d10 + this.random.nextGaussian() * 0.3F,
                  d2 + this.random.nextGaussian() * 0.3F,
                  0.0,
                  0.0,
                  0.0
               );
            if (flag && this.level().random.nextInt(4) == 0) {
               this.level()
                  .addParticle(
                     ParticleTypes.ENTITY_EFFECT,
                     d8 + this.random.nextGaussian() * 0.3F,
                     d10 + this.random.nextGaussian() * 0.3F,
                     d2 + this.random.nextGaussian() * 0.3F,
                     0.7F,
                     0.7F,
                     0.5
                  );
            }
         }
      }

      if (this.getPhase() < 3) {
         for (int i = 0; i < 5; i++) {
            float angle = (this.yBodyRot + 90.0F) * (float) (Math.PI / 180.0);
            double x = (double)Mth.cos(angle) * 0.3 + this.getX();
            double z = (double)Mth.sin(angle) * 0.3 + this.getZ();
            double y = this.getY() + 1.4;
            double startX = x + this.random.nextGaussian();
            double startY = y + this.random.nextGaussian();
            double startZ = z + this.random.nextGaussian();
            Vec3 delta = new Vec3(x, y, z).subtract(startX, startY, startZ).normalize().scale(0.1);
            this.level()
               .addParticle(
                  (ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(), startX, startY, startZ, delta.x, delta.y, delta.z
               );
         }
      }

      if (this.getInvulnerableTicks() > 0 && this.getPhase() < 4) {
         for (int i1 = 0; i1 < 3; i1++) {
            this.level()
               .addParticle(
                  ParticleTypes.ENTITY_EFFECT,
                  this.getX() + this.random.nextGaussian(),
                  this.getY() + (double)(this.random.nextFloat() * 3.3F),
                  this.getZ() + this.random.nextGaussian(),
                  0.7F,
                  0.7F,
                  0.9F
               );
         }
      }

      if (!this.isDeadOrDying() && this.isAlive()) {
         this.getPlayDeadManager().tick();
      }
   }

   protected Vec3 doFlying(Vec3 vector3d) {
      double ascendSpeed = 0.02;
      if (this.getPhase() > 3) {
         if (!(Boolean)WitherStormModConfig.SERVER.dynamicFlyingHeight.get()) {
            this.currentFlyingHeight = (double)((Integer)WitherStormModConfig.SERVER.flyingHeight.get()).intValue();
            ascendSpeed = 0.005;
         } else {
            int timeDiff = this.tickCount - this.lastFlyingSwitchTime;
            if (timeDiff >= (Integer)WitherStormModConfig.SERVER.dynamicFlyingHeightTime.get() * 20) {
               this.lastFlyingSwitchTime = this.tickCount;
               this.currentFlyingHeight = (double)(40 + this.getRandom().nextInt(41));
            }
         }
      }

      double finalHeight = this.getHeightToAscendTo(vector3d, this.currentFlyingHeight, ascendSpeed);
      vector3d = new Vec3(vector3d.x, finalHeight, vector3d.z);
      if (this.getTarget() != null && this.getPhase() < 4) {
         LivingEntity entity = this.getTarget();
         Vec3 vector3d1 = new Vec3(entity.getX() - this.getX(), 0.0, entity.getZ() - this.getZ());
         if (vector3d1.horizontalDistance() > 20.0) {
            Vec3 vector3d2 = vector3d1.normalize();
            vector3d = vector3d.add(vector3d2.x * 0.3 - vector3d.x * 0.6, 0.0, vector3d2.z * 0.3 - vector3d.z * 0.6);
         }
      } else if (!this.isNearbyTickingFormidibomb() && this.shouldTrackUltimateTarget() && (this.getTarget() == null || this.getPhase() > 3)) {
         double minDistance = 12000.0;
         if (this.getPhase() > 3) {
            minDistance = 6000.0;
         }

         Vec3 pos = this.getUltimateTargetPos();
         if (pos != null) {
            Vec3 vector3d1 = new Vec3(pos.x() - this.getX(), 0.0, pos.z() - this.getZ());
            double speed = this.getFlyingSpeed(vector3d1);
            WitherStormModifyFlyingSpeedEvent event = new WitherStormModifyFlyingSpeedEvent(this, speed);
            MinecraftForge.EVENT_BUS.post(event);
            speed = event.getOriginalSpeed();
            if (vector3d1.horizontalDistanceSqr() > minDistance) {
               Vec3 vector3d2 = vector3d1.normalize();
               vector3d = vector3d.add(vector3d2.x * speed - vector3d.x * 0.6, 0.0, vector3d2.z * speed - vector3d.z * 0.6);
            }
         }
      }

      return vector3d;
   }

   public double getHeightToAscendTo(Vec3 vector3d, double height, double ascendSpeed) {
      double finalHeight = vector3d.y;
      double yToStart = Math.min((double)(this.level().getMaxBuildHeight() - 1), this.getBoundingBox().getCenter().y());
      int radius = (int)(this.getDimensions(this.getPose()).width * 1.5F);
      double highest = -1.0;

      for (int x = -radius; x < radius; x++) {
         for (int z = -radius; z < radius; z++) {
            Types type = Types.MOTION_BLOCKING_NO_LEAVES;
            if (this.getPhase() < 4) {
               type = Types.WORLD_SURFACE;
            }

            int currentHeight;
            if (this.getPhase() > 3) {
               currentHeight = this.level().getHeight(type, x + Mth.floor(this.getX()), z + Mth.floor(this.getZ()));
            } else {
               currentHeight = WorldUtil.getHeightStartingAt(this.level(), Mth.floor(yToStart), x + this.getBlockX(), z + this.getBlockZ());
            }

            if (currentHeight > (int)yToStart) {
               currentHeight = (int)yToStart;
            }

            if (highest == -1.0 || (double)currentHeight > highest) {
               highest = (double)currentHeight;
            }
         }
      }

      double heightToAscendTo = highest + height;
      if (this.getY() < heightToAscendTo || !this.isPowered() && this.getY() < heightToAscendTo + 5.0) {
         finalHeight = (heightToAscendTo - this.getY()) * ascendSpeed;
      }

      return finalHeight;
   }

   public double distanceTo(BlockPos pos) {
      float x = (float)(this.getX() - (double)pos.getX());
      float y = (float)(this.getY() - (double)pos.getY());
      float z = (float)(this.getZ() - (double)pos.getZ());
      return (double)Mth.sqrt(x * x + y * y + z * z);
   }

   public boolean targetInUseBySegment(Entity entity) {
      if (this.getSegmentsManager().isPresent()) {
         WitherStormSegmentEntity[] segments = this.getSegmentsManager().get().getSegments();

         for (WitherStormSegmentEntity segment : segments) {
            if (segment != null && segment.alreadyATarget(entity, true)) {
               return true;
            }
         }
      }

      return false;
   }

   protected SoundEvent getAmbientSound() {
      if (!this.isDeadOrPlayingDead()) {
         return this.getPhase() < 4 ? SoundEvents.WITHER_AMBIENT : WitherStormModSoundEvents.WITHER_STORM_GROWL.get();
      } else {
         return null;
      }
   }

   protected SoundEvent getHurtSound(@NotNull DamageSource source) {
      return this.getPhase() < 4 ? SoundEvents.WITHER_HURT : WitherStormModSoundEvents.WITHER_STORM_HURT.get();
   }

   protected SoundEvent getDeathSound() {
      return this.getPhase() < 4 ? SoundEvents.WITHER_DEATH : null;
   }

   public int getAmbientSoundInterval() {
      return this.getPhase() > 3 ? Math.max(80, this.random.nextInt(120)) : super.getAmbientSoundInterval();
   }

   public float getSoundVolume() {
      return this.getPhase() > 3 ? 25.0F : super.getSoundVolume();
   }

   public static float rotlerp(float p_82204_1_, float p_82204_2_, float p_82204_3_) {
      float f = Mth.wrapDegrees(p_82204_2_ - p_82204_1_);
      if (f > p_82204_3_) {
         f = p_82204_3_;
      }

      if (f < -p_82204_3_) {
         f = -p_82204_3_;
      }

      return p_82204_1_ + f;
   }

   @Override
   public float getHeadYRot(int head) {
      return this.headManager.getHead(head).getHeadYRot();
   }

   @Override
   public float getHeadXRot(int head) {
      return this.headManager.getHead(head).getHeadXRot();
   }

   @Override
   public float getHeadYRotO(int head) {
      return this.headManager.getHead(head).getHeadYRotO();
   }

   @Override
   public float getHeadXRotO(int head) {
      return this.headManager.getHead(head).getHeadXRotO();
   }

   private void tickExtraHeadGoals() {
      int i = this.level().getServer().getTickCount() + this.getId();
      if (i % 2 != 0 && this.tickCount > 1) {
         this.level().getProfiler().push("targetSelectorHeads");
         this.headTargetSelectors.forEach(sel -> sel.tickRunningGoals(false));
         this.level().getProfiler().pop();
         this.level().getProfiler().push("goalSelectorHeads");
         this.headGoalSelectors.forEach(sel -> sel.tickRunningGoals(false));
         this.level().getProfiler().pop();
      } else {
         this.level().getProfiler().push("targetSelectorHeads");
         this.headTargetSelectors.forEach(GoalSelector::tick);
         this.level().getProfiler().pop();
         this.level().getProfiler().push("goalSelectorHeads");
         this.headGoalSelectors.forEach(GoalSelector::tick);
         this.level().getProfiler().pop();
      }
   }

   protected void searchForPlayingJukeboxes() {
      for (BlockEntity entity : WorldUtil.getBlockEntitiesInAABB(this.level(), this.getSearchBox())) {
         if (entity instanceof JukeboxBlockEntity) {
            JukeboxBlockEntity jukebox = (JukeboxBlockEntity)entity;
            if (jukebox.isRecordPlaying() && !this.playingJukeboxes.contains(jukebox.getBlockPos())) {
               this.playingJukeboxes.add(jukebox.getBlockPos());
            }
         }
      }
   }

   protected void customServerAiStep() {
      this.tickExtraHeadGoals();
      if (this.getInvulnerableTicks() > 0) {
         int ticks = this.getInvulnerableTicks() - 1;
         if (ticks <= 0) {
            this.level().explode(this, this.getX(), this.getEyeY(), this.getZ(), 7.0F, false, ExplosionInteraction.MOB);
            if (!this.isSilent()) {
               this.level().globalLevelEvent(1023, this.blockPosition(), 0);
            }
         }

         this.setInvulnerableTicks(ticks);
         float healthPerTick = (this.getMaxHealth() - 1.0F) / (float)this.getStartingInvulnerableTicks() * 10.0F;
         if (this.tickCount % 10 == 0) {
            this.heal(healthPerTick);
         }

         this.bossEvent.ifPresent(bossEvent -> bossEvent.setProgress(this.getHealth() / this.getMaxHealth()));
      } else {
         super.customServerAiStep();
         if (!this.isDeadOrPlayingDead()) {
            this.headManager.customServerAiStep();
            if (this.destroyBlocksTick > 0) {
               this.destroyBlocksTick--;
               if (this.destroyBlocksTick == 0 && ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                  int i1 = Mth.floor(this.getY());
                  int l1 = Mth.floor(this.getX());
                  int i2 = Mth.floor(this.getZ());
                  boolean flag = false;

                  for (int k2 = -1; k2 <= 1; k2++) {
                     for (int l2 = -1; l2 <= 1; l2++) {
                        for (int j = 0; j <= 3; j++) {
                           int i3 = l1 + k2;
                           int k = i1 + j;
                           int l = i2 + l2;
                           BlockPos blockpos = new BlockPos(i3, k, l);
                           BlockState blockstate = this.level().getBlockState(blockpos);
                           if (blockstate.canEntityDestroy(this.level(), blockpos, this)
                              && !blockstate.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST)
                              && ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                              flag = this.level().destroyBlock(blockpos, true, this) || flag;
                           }
                        }
                     }
                  }

                  if (flag) {
                     this.level().levelEvent(null, 1022, this.blockPosition(), 0);
                  }
               }
            }

            for (BlockClusterSource source : WitherStormWorldInteractions.getInstance().getClusterSources()) {
               source.tick(this);
            }

            if ((Boolean)WitherStormModConfig.SERVER.convertFallingBlocks.get()) {
               for (FallingBlockEntity fallingBlock : this.level().getEntitiesOfClass(FallingBlockEntity.class, this.getSearchBox())) {
                  if (WorldUtil.isLoaded((ServerLevel)this.level(), fallingBlock.blockPosition()) && WorldUtil.canSeeOrIsNotInASmallArea(this, fallingBlock)) {
                     BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(this.level());
                     Map<BlockPos, BlockState> map = Maps.newHashMap();
                     map.put(BlockPos.ZERO, fallingBlock.getBlockState());
                     cluster.populate(map);
                     cluster.moveTo(fallingBlock.position());
                     cluster.setRotationDelta(new Vec2((float)this.random.nextInt(20) * 0.1F / 2.0F, (float)this.random.nextInt(20) * 0.1F / 2.0F));
                     cluster.setNoGravity(true);
                     cluster.setPhysics(false);
                     cluster.setCreatedFromFallingBlock(true);
                     fallingBlock.discard();
                     this.level().addFreshEntity(cluster);
                     this.trackedEntities.trackEntityToConsume(cluster);
                  }
               }
            }

            if (ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
               double addRad;
               double consumptionRadius;
               if (this.getPhase() >= 6) {
                  addRad = (double)this.entityConsumptionRadius + 50.0 + 100.0;
                  consumptionRadius = (double)this.entityConsumptionRadius;
               } else if (this.getPhase() <= 3) {
                  addRad = (double)this.entityConsumptionRadiusHunch() + 50.0 + 0.0;
                  consumptionRadius = (double)this.entityConsumptionRadiusHunch();
               } else {
                  addRad = (double)this.entityConsumptionRadius + 50.0 + 0.0;
                  consumptionRadius = (double)this.entityConsumptionRadius;
               }

               AABB searchBB = this.getBoundingBox().inflate(addRad);
               List<AABB> playerBoundingBoxes = this.level().getEntitiesOfClass(ServerPlayer.class, searchBB).stream().map(p -> p.getBoundingBox().inflate(8.0)).toList();

               for (Entity entity : this.level().getEntitiesOfClass(Entity.class, searchBB, PICKABLE)) {
                  if (!this.trackedEntities.contains(entity) && this.random.nextFloat() >= 0.9F) {
                     if (entity instanceof ItemEntity) {
                        ItemEntity itemEntity = (ItemEntity)entity;
                        ItemStack item = itemEntity.getItem();
                        if (this.getPhase() > 3 && item.is(WitherStormModItemTags.JUNK) && (Boolean)WitherStormModConfig.SERVER.removeNearbyJunk.get()) {
                           if (playerBoundingBoxes.stream().allMatch(box -> !box.contains(entity.position()))) {
                              entity.discard();
                           }
                        } else if ((double)entity.distanceTo(this) <= consumptionRadius
                           && (
                              item.is(WitherStormModItemTags.UNAPPETIZING) && entity.distanceTo(this) < (float)(this.getPhase() > 3 ? 35 : 2)
                                 || !item.is(WitherStormModItemTags.UNAPPETIZING)
                           )
                           && !item.is((Item)WitherStormModItems.COMMAND_BLOCK_BOOK.get())
                           && !item.is(WitherStormModItemTags.COMMAND_BLOCK_TOOLS)
                           && !item.is((Item)WitherStormModItems.WITHERED_NETHER_STAR.get())
                           && this.canTrackEntity(entity)) {
                           this.trackedEntities.trackEntityToConsume(entity);
                           entity.setNoGravity(true);
                        }
                     } else if (entity instanceof Slime && (double)entity.distanceTo(this) <= consumptionRadius && this.canTrackEntity(entity)) {
                        this.trackedEntities.trackEntityToConsume(entity);
                        entity.setNoGravity(true);
                     }
                  }
               }
            }

            double defaultSpeed = (Double)WitherStormModConfig.SERVER.tractorPullSpeedModifier.get();
            if (this.getPhase() < 4) {
               for (WitherStormHead head : this.headManager.getHeads()) {
                  this.pullInTarget(head.getTarget(), defaultSpeed, head);
               }
            }

            for (Entity entityx : this.level().getEntitiesOfClass(Entity.class, this.getSearchBox())) {
               if (!this.getIgnoredTargets().shouldIgnoreEntity(entityx)) {
                  for (WitherStormHead head : this.headManager.getHeads()) {
                     if (TractorBeamHelper.isInsideTractorBeam(entityx, this, 4.0, head.getIndex())) {
                        if (this.getPhase() > 3 && head.getTarget() == entityx) {
                           this.pullInTarget(entityx, defaultSpeed, head);
                        } else if ((Boolean)WitherStormModConfig.SERVER.canPickupMobClusters.get() && !this.isDistracted(head.getIndex())) {
                           if (entityx instanceof LivingEntity) {
                              LivingEntity living = (LivingEntity)entityx;
                              if (!this.entitySelector.test(living)) {
                                 continue;
                              }
                           } else if (!TRACTOR_BEAM_PULLABLE.test(entityx)) {
                              continue;
                           }

                           if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entityx)
                              && !this.alreadyATarget(entityx, true)
                              && !this.targetInUseBySegment(entityx)
                              && head.canSee(entityx)
                              && !this.trackedEntities.contains(entityx)) {
                              double speed;
                              if (TRACTOR_BEAM_PULLABLE.test(entityx)) {
                                 speed = 0.4;
                              } else if (!(entityx instanceof Player)) {
                                 speed = defaultSpeed - 0.05 + new Random((long)entityx.getId()).nextDouble() * 0.1;
                              } else {
                                 speed = defaultSpeed;
                              }

                              this.pullInTarget(entityx, speed, head);
                              if (ABSORBABLE.test(entityx) && entityx.position().distanceTo(head.getHeadPos()) < 20.0) {
                                 this.trackedEntities.trackEntityToConsume(entityx);
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (this.isAddedToWorld() && this.tickCount % 20 == 0 && this.isCompletelyInvulnerable()) {
               this.heal(10.0F);
            }

            this.bossEvent.ifPresent(bossEvent -> bossEvent.setProgress(this.getHealth() / this.getMaxHealth()));
            if (this.getTarget() != null) {
               this.idleTargetTicks++;
            }

            if (this.idleTargetTicks > 1800 || this.getTarget() == null) {
               this.setTarget(null);
               this.idleTargetTicks = 0;
            }

            if (this.getPhase() > 3) {
               for (WitherStormHead headx : this.headManager.getHeads()) {
                  if (this.tractorBeamActive(headx.getIndex())) {
                     AABB headBox = headx.getBoundingBox();

                     for (Projectile projectile : this.level().getEntitiesOfClass(Projectile.class, headBox)) {
                        if (projectile.getOwner() != this) {
                           if (headx.getHeadInjureAttemptCooldown() <= 0 && headx.getHeadInjuryTicks() <= 0) {
                              headx.setHeadInjureAttemptCooldown(40);
                              if (!this.isDeadOrPlayingDead() && headx.checkAndCountAttack()) {
                                 headx.hurt(projectile.getOwner(), this.headManager.getHeadInjuryTime());
                                 if (projectile.getOwner() instanceof Player owner) {
                                    owner.playNotifySound(SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 1.0F, 1.0F);
                                 }
                              }
                           }

                           if (!(projectile instanceof ThrownTrident)) {
                              projectile.discard();
                           }
                        }
                     }
                  }
               }
            }

            CommandBlockEntity commandBlock = this.getBowelsCommandBlock();
            if (commandBlock != null && commandBlock.getHealth() < commandBlock.getMaxHealth() && this.nextFlicker > 0) {
               this.nextFlicker--;
               if (this.nextFlicker == 0) {
                  this.doFlicker();
                  this.nextFlicker = (int)(
                     (double)this.random.nextInt(40) + 60.0 * Math.max(0.2, (double)(commandBlock.getHealth() / commandBlock.getMaxHealth()))
                  );
               }
            }

            if ((this.horizontalCollision || this.verticalCollision) && ForgeEventFactory.getMobGriefingEvent(this.level(), this) && this.getPhase() > 3) {
               for (int i = 0; i < 10; i++) {
                  Direction direction = Direction.getRandom(this.random);
                  AABB box = this.getBoundingBox();
                  int scale = 2;
                  BlockPos pos;
                  switch (direction.getAxis()) {
                     case Y: {
                        int x = Mth.floor(box.minX + (box.maxX - box.minX) * this.random.nextDouble());
                        int y = Mth.floor(direction == Direction.DOWN ? box.minY - (double)scale : box.maxY + (double)scale);
                        int z = Mth.floor(box.minZ + (box.maxZ - box.minZ) * this.random.nextDouble());
                        pos = new BlockPos(x, y, z);
                        break;
                     }
                     case X: {
                        int x = Mth.floor(direction == Direction.WEST ? box.minX - (double)scale : box.maxX + (double)scale);
                        int y = Mth.floor(box.minY + (box.maxY - box.minY) * this.random.nextDouble());
                        int z = Mth.floor(box.minZ + (box.maxZ - box.minZ) * this.random.nextDouble());
                        pos = new BlockPos(x, y, z);
                        break;
                     }
                     case Z: {
                        int x = Mth.floor(box.minX + (box.maxX - box.minX) * this.random.nextDouble());
                        int y = Mth.floor(box.minY + (box.maxY - box.minY) * this.random.nextDouble());
                        int z = Mth.floor(direction == Direction.NORTH ? box.minZ - (double)scale : box.maxZ + (double)scale);
                        pos = new BlockPos(x, y, z);
                        break;
                     }
                     default:
                        throw new IllegalArgumentException("Unexpected value: " + direction.getAxis());
                  }

                  BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(this.level());

                  assert cluster != null;

                  cluster.populateWithRadius(
                     pos, 3.0F + this.random.nextFloat() * 2.0F, state -> !state.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST)
                  );
                  if (cluster.getSize() > 0) {
                     this.trackedEntities.trackEntityToConsume(cluster);
                     cluster.setRotationDelta(new Vec2((float)this.random.nextInt(20) * 0.1F / 2.0F, (float)this.random.nextInt(20) * 0.1F / 2.0F));
                     cluster.setNoGravity(true);
                     cluster.setPhysics(false);
                     cluster.setShakeTime(5);
                     this.level().addFreshEntity(cluster);
                  }
               }
            }

            if ((Boolean)WitherStormModConfig.SERVER.caveRumbles.get() && this.nextUndergroundRumble > 0) {
               this.nextUndergroundRumble--;
               if (this.nextUndergroundRumble == 0) {
                  if (this.getPhase() > 3) {
                     for (ServerPlayer tracking : this.level().getEntitiesOfClass(ServerPlayer.class, this.getSearchBox().inflate(50.0))) {
                        if (!WorldUtil.canSeeOrIsNotInASmallArea(this, tracking)) {
                           WorldUtil.doCaveRumble(
                              (ServerLevel)this.level(), tracking, (Double)WitherStormModConfig.SERVER.caveRumbleIntensity.get(), this.random
                           );
                        }
                     }
                  }

                  if ((Boolean)WitherStormModConfig.SERVER.chanceForExtendedRumbles.get() && this.random.nextInt(3) != 0) {
                     this.nextUndergroundRumble = 100 + this.random.nextInt(60);
                  } else {
                     int min = (Integer)WitherStormModConfig.SERVER.caveRumbleIntervalMin.get() * 20;
                     int diff = (Integer)WitherStormModConfig.SERVER.caveRumbleIntervalMax.get() * 20 - min;
                     if (diff > 0) {
                        this.nextUndergroundRumble = min + this.random.nextInt(diff);
                     } else {
                        this.nextUndergroundRumble = min;
                     }
                  }
               }
            }

            this.searchForPlayingJukeboxes();
            Iterator<BlockPos> iterator = this.playingJukeboxes.iterator();

            while (iterator.hasNext()) {
               BlockPos pos = iterator.next();
               if (!this.getSearchBox().contains(Vec3.atCenterOf(pos))) {
                  iterator.remove();
               } else {
                  BlockEntity entityxx = this.level().getBlockEntity(pos);
                  if (entityxx instanceof JukeboxBlockEntity) {
                     JukeboxBlockEntity jukebox = (JukeboxBlockEntity)entityxx;
                     if (!jukebox.isRecordPlaying()) {
                        iterator.remove();
                     }
                  } else {
                     iterator.remove();
                  }
               }
            }
         }
      }
   }

   protected boolean canTrackEntity(Entity entity) {
      SegmentsManager manager = this.getSegmentsManager().orElse(null);
      if (manager != null) {
         for (WitherStormSegmentEntity segment : manager.getSegments()) {
            if (segment != null && segment.isAlive() && segment.getTrackedEntities().contains(entity)) {
               return false;
            }
         }
      }

      return WorldUtil.canSeeOrIsNotInASmallArea(this, entity);
   }

   protected void tickDeath() {
      if (this.getPhase() > 3) {
         this.witherStormDeathTime++;
      } else {
         this.deathTime++;
      }

      if (!this.onGround()) {
         for (WitherStormHead head : this.headManager.getHeads()) {
            head.lerpHeadXTo(-50.0F, 64.0F);
         }
      }

      if (!this.level().isClientSide) {
         if (this.getPhase() > 5 && this.getDeathTime() < 240 && ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
            this.dropDeathClusters();
         }

         if (this.getPhase() > 3 && this.getDeathTime() == 360) {
            this.remove(RemovalReason.KILLED);
            Player nearest = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), this.getAttributeValue(Attributes.FOLLOW_RANGE) + 50.0, false);
            if (nearest != null) {
               this.dropDropsAt(nearest);
            }
         } else if (this.getPhase() < 4 && this.deathTime == 20) {
            this.remove(RemovalReason.KILLED);
         }

         this.bossEvent.ifPresent(event -> event.setProgress(1.0F - (float)this.getDeathTime() / 360.0F));
      } else {
         if (this.tickCount % 20 == 0) {
            int size = Math.max(10, this.random.nextInt(15));

            for (int i = 0; i < size; i++) {
               for (DebrisCluster cluster : this.getDebrisClusters()) {
                  if (!cluster.isDisabled()) {
                     cluster.setDisabled(this.random.nextInt(this.getDebrisClusters().size()) == 0);
                  }
               }
            }
         }

         float percentage = Math.max(0.0F, (360.0F - (float)this.getDeathTime()) / 360.0F);

         for (DebrisRingSettings settings : this.getDebrisRings()) {
            settings.setAlpha(percentage);
         }

         this.setShineAlpha(percentage);
      }
   }

   protected void dropDeathClusters() {
      int interval = 240 / this.getPhase();
      if (this.getDeathTime() % interval == 0) {
         this.dropMassCluster(this.getPhase() - 2);
      }

      if (this.getDeathTime() % 5 == 0) {
         this.dropMassCluster(2);
      }

      if (this.getDeathTime() > 5) {
         for (int i = 0; i < 3; i++) {
            this.dropSmallMassCluster(1);
         }
      }
   }

   public boolean alreadyATarget(Entity entity, boolean countMain) {
      for (WitherStormHead head : this.headManager.getHeads()) {
         if ((countMain || !(head instanceof MainHead)) && entity.equals(head.getTarget())) {
            return true;
         }
      }

      return false;
   }

   public void removeFluidFromLook(float x, float y, int head) {
      Vec3 vecPos = this.getHeadPos(head);
      Vec3 end = vecPos.add(this.getViewVector(x, y, 200.0F));
      if (ForgeEventFactory.getMobGriefingEvent(this.level(), this) && this.getPhase() > 3) {
         BlockHitResult result = this.level().clip(new ClipContext(vecPos, end, Block.COLLIDER, Fluid.ANY, null));
         BlockPos hitPos = result.getBlockPos();
         if (WorldUtil.isLoaded((ServerLevel)this.level(), hitPos)
            && hitPos.getY() > (Integer)WitherStormModConfig.SERVER.tractorBeamFluidRemovalHeight.get()) {
            for (int hitX = -6; hitX <= 6; hitX++) {
               for (int hitY = -6; hitY <= 6; hitY++) {
                  for (int hitZ = -6; hitZ <= 6; hitZ++) {
                     BlockPos pos = hitPos.offset(hitX, hitY, hitZ);
                     BlockState state = this.level().getBlockState(pos);
                     if (state.getFluidState().is(Fluids.WATER)) {
                        this.level().setBlock(pos, Fluids.FLOWING_WATER.defaultFluidState().createLegacyBlock(), 3);
                     }

                     if (state.getFluidState().is(Fluids.LAVA)) {
                        this.level().setBlock(pos, Fluids.FLOWING_LAVA.defaultFluidState().createLegacyBlock(), 3);
                     }
                  }
               }
            }
         }
      }
   }

   public void createClusterFromLook(float x, float y, int time, int head) {
      Vec3 vecPos = this.getHeadPos(head);
      Vec3 end = vecPos.add(this.getViewVector(x, y, 200.0F));
      if (ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
         BlockHitResult result = this.level().clip(new ClipContext(vecPos, end, Block.COLLIDER, Fluid.NONE, null));
         BlockPos hitPos = result.getBlockPos();
         if (WorldUtil.isLoaded((ServerLevel)this.level(), hitPos)) {
            for (int i = 0; i < 512; i++) {
               int offsetX;
               int offsetY;
               int offsetZ;
               if (this.getPhase() <= 3) {
                  offsetX = (int)Math.round(this.random.nextGaussian() * 1.0);
                  offsetY = (int)Math.round(this.random.nextGaussian() * 1.0);
                  offsetZ = (int)Math.round(this.random.nextGaussian() * 1.0);
               } else {
                  offsetX = (int)Math.round(this.random.nextGaussian() * 2.25);
                  offsetY = (int)Math.round(this.random.nextGaussian() * 2.25);
                  offsetZ = (int)Math.round(this.random.nextGaussian() * 2.25);
               }

               BlockPos pos = hitPos.offset(offsetX, offsetY, offsetZ);
               if (WorldUtil.isBlockExposed(this.level(), pos)) {
                  BlockState state = this.level().getBlockState(pos);
                  if (!state.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST)
                     && (
                        !(this.random.nextDouble() > 0.999)
                           || !state.is(Blocks.STONE) && !state.is(BlockTags.DIRT) && !state.is(BlockTags.SAND)
                     )
                     && (
                        !(Boolean)WitherStormModConfig.SERVER.onlyTryPickingUpTractorTagged.get()
                           || state.is(WitherStormModBlockTags.TRACTOR_BEAM_DISTRACTION_BLOCKS)
                     )) {
                     double radius = 1.0;
                     if (this.getPhase() == 4) {
                        radius = Math.max(1.0, Math.min(1.5, 1.0 + 0.125 * this.random.nextGaussian()));
                     } else if (this.getPhase() == 5) {
                        radius = Math.max(1.0, Math.min(3.0, 1.0 + 0.5 * this.random.nextGaussian()));
                     } else if (this.getPhase() == 6) {
                        radius = Math.max(1.0, Math.min(4.5, 1.0 + 0.75 * this.random.nextGaussian()));
                     } else if (this.getPhase() == 7) {
                        radius = Math.max(1.0, Math.min(8.0, 1.5 + 1.25 * this.random.nextGaussian()));
                     }

                     BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(this.level());

                     assert cluster != null;

                     cluster.populateWithRadius(pos, (float)radius, blockstate -> !blockstate.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST));
                     cluster.setTime(time);
                     if (this.random.nextInt(3) == 0) {
                        cluster.playSound(
                           WitherStormModSoundEvents.BLOCK_CLUSTER_SHAKE.get(),
                           2.0F,
                           (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F
                        );
                     }

                     cluster.setCreatedFromTractorBeam(true);
                     cluster.setHeadCreatedFrom(head);
                     cluster.setTractorBeamDistanceThreshold(this.random.nextDouble() * 5.0);
                     this.trackedEntities.trackEntityToConsume(cluster);
                     cluster.setRotationDelta(
                        new Vec2(
                           (float)(this.random.nextInt(120) - 60) * 0.05F / ((float)radius * 3.0F),
                           (float)(this.random.nextInt(120) - 60) * 0.05F / ((float)radius * 3.0F)
                        )
                     );
                     cluster.setNoGravity(true);
                     cluster.setPhysics(false);
                     this.level().addFreshEntity(cluster);
                     break;
                  }
               }
            }
         }
      }
   }

   private void setPlayerDeltaMovement(ServerPlayer player, Vec3 motion) {
      PlayerMotionMessage message = new PlayerMotionMessage(motion);
      WitherStormModPacketHandlers.MAIN.send(PacketDistributor.PLAYER.with(() -> player), message);
   }

   @NotNull
   public AABB getBoundingBoxForCulling() {
      return this.getBoundingBox().inflate(50.0);
   }

   @NotNull
   public EntityDimensions getDimensions(@NotNull Pose pose) {
      EntityDimensions size = this.getUnmodifiedDimensions(pose);
      if ((Boolean)WitherStormModConfig.SERVER.squashHitbox.get() && this.getPhase() > 3) {
         size = EntityDimensions.scalable(size.width, 1.0F);
      }

      if (this.getPlayDeadManager().getState() == PlayDeadManager.State.PLAYING_DEAD) {
         size = EntityDimensions.scalable(size.width, 0.1F);
      }

      return size;
   }

   public EntityDimensions getUnmodifiedDimensions(Pose pos) {
      EntityDimensions size = STARTING_SIZE;
      if (this.getPhase() == 4) {
         size = DESTROYER_SIZE;
      } else if (this.getPhase() == 5) {
         size = EVOLVED_DESTROYER_SIZE;
      } else if (this.getPhase() == 6) {
         size = DEVOURER_SIZE;
      } else if (this.getPhase() == 7) {
         size = EVOLVED_DEVOURER_SIZE;
      }

      return size;
   }

   public float getUnmodifiedHeight() {
      return this.getUnmodifiedDimensions(this.getPose()).height;
   }

   public float getUnmodifiedWidth() {
      return this.getUnmodifiedDimensions(this.getPose()).width;
   }

   public float getUnmodifiedSize() {
      float width = this.getUnmodifiedWidth();
      float height = this.getUnmodifiedHeight();
      return (width + height + width) / 3.0F;
   }

   protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size) {
      return this.getPhase() > 3 ? 17.5F : super.getStandingEyeHeight(pose, size);
   }

   public void baseTick() {
      this.headManager.baseTick();
      super.baseTick();
      this.xBodyRotO = this.xBodyRot;
      this.shineAlphaO = this.shineAlpha;
   }

   public void tick() {
      super.tick();
      if (!this.level().isClientSide && this.evolutionProfiler.isProfiling()) {
         this.evolutionProfiler.tick(this);
      }

      this.tentacleTickCountO = this.tentacleTickCount;
      if (!this.isDeadOrPlayingDead()) {
         this.tentacleTickCount = this.tentacleTickCount + (int)(1.0F + this.walkAnimation.speed());
      }

      this.lastConsumedEntities = this.getConsumedEntities();
      if (!this.level().isClientSide) {
         this.trackedEntities.tick((ServerLevel)this.level());
         this.ignoredTargets.tick();
         this.bowelsInstance.ifPresent(BowelsInstanceManager::tick);
         Vec3 absorptionPoint = this.getBoundingBox().getCenter();
         List<Entity> currentTracked = this.trackedEntities.getCurrentTrackedEntities();
         List<Entity> toAdd = Lists.newArrayList();
         AABB absorptionBox = this.getBoundingBox();
         if (this.getPhase() > 3) {
            absorptionBox = this.getBoundingBox().deflate((double)this.getUnmodifiedWidth() / 1.5);
         }

         for (Entity entity : currentTracked) {
            if (entity.isAlive()) {
               this.doEntityPulling(entity, absorptionPoint, absorptionBox);
               if (entity instanceof BlockClusterEntity) {
                  BlockClusterEntity cluster = (BlockClusterEntity)entity;
                  if (WorldUtil.isLoaded((ServerLevel)this.level(), cluster.blockPosition())
                     && cluster.getShakeTime() <= 0
                     && cluster.shouldCrumble()
                     && this.tickCount % 20 == 0
                     && this.random.nextInt(3) == 0) {
                     this.splitCluster(cluster, toAdd);
                  }
               }
            } else {
               this.trackedEntities.stopTrackingEntity(entity);
            }
         }

         for (Entity entityx : toAdd) {
            this.trackedEntities.trackEntityToConsume(entityx);
         }

         if (this.getPhase() > 5) {
            this.findSegments();
            this.createSegments();
            this.addSegments();
         }

         if (this.getPhase() < 6) {
            this.removeSegments();
         } else {
            this.readdSegments();
         }

         this.bossEvent.ifPresent(event -> {
            for (ServerPlayer tracking : this.playersTracking) {
               if (this.smartBossMusic() && !WorldUtil.canSeeOrIsNotInASmallArea(this, tracking)) {
                  event.removePlayer(tracking);
               } else {
                  event.addPlayer(tracking);
               }
            }
         });
      }

      int phase = this.getPhase();
      float prevConsumption = (float)this.getConsumptionAmountForPhase(phase - 1);
      float consumptionForPhase = (float)this.getConsumptionAmountForPhase(phase);
      float consumption = (float)this.getConsumedEntities();
      this.phaseProgress = Mth.clamp((consumption - prevConsumption) / (consumptionForPhase - prevConsumption), 0.0F, 1.0F);
      if (this.level().isClientSide()) {
         for (DebrisCluster cluster : this.getDebrisClusters()) {
            if (!cluster.isDisabled()) {
               cluster.tick();
            }
         }

         if (this.shouldShine()) {
            if (phase > 0) {
               float factor = (float)phase - this.phaseProgress;
               this.shineScale = this.getUnmodifiedHeight() * (10.0F / factor);
            } else {
               this.shineScale = this.getUnmodifiedHeight();
            }
         }
      }

      this.checkConsumptionAmount();
      if (this.partsEnabled) {
         Section[] sections = this.getSections();

         for (Section section : sections) {
            if (section.isActive()) {
               section.tick();
            }
         }
      }

      if (!this.level().isClientSide && this.shouldPlaySoundLoops()) {
         StormSoundPositionMessage message = new StormSoundPositionMessage(
            this.getId(), this.getX(), this.getEyeY(), this.getZ(), (byte)this.getPhase()
         );
         WitherStormModPacketHandlers.MAIN.send(PacketDistributor.DIMENSION.with(() -> this.level().dimension()), message);
      }

      this.headManager.tick();
      this.onGroundAnimationO = this.onGroundAnimation;
      if (this.onGround() && this.isDeadOrPlayingDead()) {
         this.onGroundAnimation = this.onGroundAnimation + 1.0F + this.random.nextFloat() * 2.0F;
         if (this.onGroundAnimation > 300.0F) {
            this.onGroundAnimation = 300.0F;
         }
      } else {
         this.onGroundAnimation = this.onGroundAnimation - (1.0F + this.random.nextFloat() * 2.0F);
         if (this.onGroundAnimation < 0.0F) {
            this.onGroundAnimation = 0.0F;
         }
      }

      if (this.flickerTime > 0) {
         this.flickerTime--;
         this.shouldFlicker = Mth.cos((float)(this.flickerTime + this.random.nextInt(20)))
               * Mth.sin((float)(this.flickerTime + 30 + this.random.nextInt(20)))
            < -0.5F;
         if (this.flickerTime == 0) {
            this.shouldFlicker = false;
         }
      }

      if (this.level().isClientSide && (Boolean)WitherStormModConfig.CLIENT.tractorBeamParticles.get() && !this.isOnDistantRenderer()) {
         for (int i = 0; i < this.getTotalHeads(); i++) {
            if (this.tractorBeamActive(i) && !this.isDeadOrPlayingDead() && this.getXBodyRot() == 0.0F) {
               Vec3 headPos = this.getHeadPos(i);
               float x = this.getHeadXRot(i);
               float y = this.getHeadYRot(i);

               for (int amount = 0; amount < 5; amount++) {
                  Vec3 lookVec = this.getViewVector(x, y, this.random.nextFloat() * 200.0F);
                  Vec3 pos = headPos.add(lookVec).add(0.0, 5.5, 0.0);
                  double distanceFromHead = Math.sqrt(pos.distanceToSqr(headPos));
                  double distanceAllowed = distanceFromHead * 2.0 * 0.02;
                  double randX = this.random.nextGaussian() * distanceAllowed;
                  double randY = this.random.nextGaussian() * distanceAllowed;
                  double randZ = this.random.nextGaussian() * distanceAllowed;
                  pos = pos.add(randX, randY, randZ);
                  Pair<Boolean, Integer> result = TractorBeamHelper.isInsideTractorBeam(pos, this, 4.0);
                  if ((Boolean)result.getFirst()) {
                     Vec3 delta = pos.subtract(headPos).normalize().scale(-0.8);
                     if (WitherStormMod.isAprilFools() && (Boolean)WitherStormModConfig.CLIENT.aprilFools.get()) {
                        this.level()
                           .addParticle(ParticleTypes.HEART, true, pos.x, pos.y, pos.z, delta.x, delta.y, delta.z);
                     } else {
                        this.level()
                           .addParticle(
                              new TractorBeamParticleOptions(this.getId(), (Integer)result.getSecond()),
                              true,
                              pos.x,
                              pos.y,
                              pos.z,
                              delta.x,
                              delta.y,
                              delta.z
                           );
                     }
                  }
               }

               if (this.getPhase() >= 4) {
                  float spread = 8.0F;

                  for (int p = 0; p < 10; p++) {
                     double cutoff = this.getTractorBeamCutoffDistance(i);
                     Vec3 end = headPos.add(
                           this.getViewVector(
                              x + this.random.nextFloat() * spread - spread / 2.0F,
                              y + this.random.nextFloat() * spread - spread / 2.0F,
                              (float)(cutoff == -1.0 ? 200.0 : cutoff + 30.0)
                           )
                        )
                        .add(0.0, 5.5, 0.0);
                     BlockHitResult blockHitResult = this.level().clip(new ClipContext(headPos, end, Block.COLLIDER, Fluid.NONE, null));
                     BlockPos hitPos = blockHitResult.getBlockPos();
                     if (WorldUtil.isBlockExposed(this.level(), hitPos)) {
                        BlockState hitState = this.level().getBlockState(hitPos);
                        Vec3 delta = Vec3.atCenterOf(hitPos).subtract(headPos).normalize();
                        this.level()
                           .addParticle(
                              new BlockParticleOption(ParticleTypes.BLOCK, hitState).setPos(hitPos),
                              true,
                              (double)hitPos.getX() + 0.5,
                              (double)hitPos.getY() + 1.0,
                              (double)hitPos.getZ(),
                              -delta.x,
                              -delta.y,
                              -delta.z
                           );
                     }
                  }
               }
            }
         }
      }
   }

   protected <T extends Entity> void doEntityPulling(T entity, Vec3 absorptionPoint, AABB absorptionBox) {
      WitherStormPullBehavior<T> behavior = null;
      if (WitherStormWorldInteractions.getInstance().hasPullBehavior(entity.getType())) {
         behavior = WitherStormWorldInteractions.getInstance().getPullBehavior(entity.getType());
      }

      if (behavior == null || behavior.canPullIn(entity, this)) {
         double speed = 0.5;
         if (behavior != null) {
            speed = behavior.getSpeed(entity, this, absorptionPoint);
         }

         Vec3 delta = absorptionPoint.subtract(entity.position());
         double distanceToStorm = delta.length();
         Vec3 defaultVelocity = delta.normalize().multiply(speed, speed, speed);
         if (distanceToStorm >= 320.0 || !WorldUtil.isLoaded((ServerLevel)this.level(), entity.blockPosition())) {
            entity.teleportTo(absorptionPoint.x, absorptionPoint.y, absorptionPoint.z);
         }

         if (behavior != null) {
            entity.setDeltaMovement(behavior.pullEntity(entity, this, absorptionPoint, defaultVelocity, speed));
         } else {
            entity.setDeltaMovement(defaultVelocity);
         }

         if (entity instanceof ItemEntity || behavior != null && behavior.doClientsideVelocityUpdates(entity, this)) {
            ServerLevel world = (ServerLevel)this.level();

            for (ServerPlayer player : world.players()) {
               player.connection.send(new ClientboundSetEntityMotionPacket(entity));
            }
         }

         if (absorptionBox.contains(entity.position())) {
            this.trackedEntities.stopTrackingEntity(entity);
            if (entity instanceof BlockClusterEntity cluster && !cluster.shouldntCountToConsumedEntities()) {
               this.consumeEntity(cluster, cluster.getSize());
            }

            if (entity instanceof ItemEntity itemEntity) {
               this.consumeEntity(itemEntity, itemEntity.getItem().getCount());
            }

            if (!(entity instanceof BlockClusterEntity) && !(entity instanceof ItemEntity)) {
               this.consumeEntity(entity, 1);
            }

            if (entity instanceof LivingEntity living) {
               if (living instanceof TamableAnimal tamable && tamable.getOwnerUUID() != null) {
                  this.storePet(tamable);
               }

               living.hurt(WitherStormModDamageTypes.witherStormAttackMob(this), Float.MAX_VALUE);
            } else {
               entity.discard();
            }
         }
      }
   }

   public Optional<UltimateTargetManager> getUltimateTargetManager() {
      return this.targetManager;
   }

   public void checkConsumptionAmount() {
      if (!this.isDeadOrPlayingDead()
         && this.getConsumedEntities() > this.getConsumptionAmountForPhase(this.getPhase())
         && this.getConsumedEntities() != this.lastConsumedEntities) {
         this.evolve(false);
      }
   }

   public boolean evolve(boolean force) {
      int nextPhase = this.getPhase() + 1;
      if (this.canEvolve(force) && !MinecraftForge.EVENT_BUS.post(new WitherStormEvolveEvent(this, nextPhase))) {
         this.evolveToPhase(nextPhase);
         return true;
      } else {
         return false;
      }
   }

   public void evolveToPhase(int phase) {
      this.setPhase(phase);
      if (this.evolutionProfiler.isProfiling()) {
         this.evolutionProfiler.onEvolve(this);
      }

      this.targetManager.ifPresent(manager -> {
         if ((Boolean)WitherStormModConfig.SERVER.chaseOnPhaseChange.get() && this.getPhase() > 3) {
            manager.accelerate();
         }
      });
      if (this.shouldPlayGlobalSounds && this.getPhase() == 4) {
         this.playSoundToEveryone(WitherStormModSoundEvents.WITHER_STORM_EVOLVES.get(), 1.0F, 1.0F);
      }
   }

   public void refreshDimensions() {
      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      super.refreshDimensions();
      this.setPos(x, y, z);
   }

   public void playSoundToEveryone(SoundEvent event, float volume, float pitch) {
      GlobalSoundMessage message = new GlobalSoundMessage(event, volume, pitch);
      if ((Boolean)WitherStormModConfig.SERVER.shouldPlayGlobalSoundsCrossDimensionally.get()) {
         WitherStormModPacketHandlers.MAIN.send(PacketDistributor.ALL.noArg(), message);
      } else {
         WitherStormModPacketHandlers.MAIN.send(PacketDistributor.DIMENSION.with(() -> this.level().dimension()), message);
      }
   }

   public void onRemovedFromWorld() {
      super.onRemovedFromWorld();
      if (!this.level().isClientSide) {
         WitherStormSyncHelper.removeWitherStorm(this);
      }

      if (!this.isAlive()) {
         this.getPlayDeadManager().removePodium();
         this.trackedEntities.destroyAllClusters();
         this.trackedEntities.clearAndMakeAllFall();
      }
   }

   public void die(@NotNull DamageSource source) {
      super.die(source);
      if (!this.level().isClientSide) {
         this.spawnConsumedPets(Vec3.atBottomCenterOf(this.level().getHeightmapPos(Types.MOTION_BLOCKING_NO_LEAVES, this.blockPosition())));
         if (this.getPhase() > 3) {
            if (this.shouldPlaySoundLoop) {
               this.level()
                  .playSound(
                     null,
                     this.getX(),
                     this.getY(),
                     this.getZ(),
                     WitherStormModSoundEvents.WITHER_STORM_DEATH.get(),
                     SoundSource.HOSTILE,
                     20.0F,
                     1.0F
                  );
               RemoveSoundLoopMessage message = new RemoveSoundLoopMessage(this);
               WitherStormModPacketHandlers.MAIN.send(PacketDistributor.DIMENSION.with(() -> this.level().dimension()), message);
            }

            for (WitherStormHead head : this.headManager.getHeads()) {
               head.doRoar(false);
            }

            this.trackedEntities.clearAndMakeAllFall();
         }

         for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, this.getSearchBox())) {
            living.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(WitherSicknessTracker::cure);
            if (living instanceof WitherSickened sickened) {
               sickened.cure((ServerLevel)this.level());
            }
         }

         this.getSegmentsManager().ifPresent(SegmentsManager::killSegments);
      }
   }

   public void performRangedAttack(int head, LivingEntity entity) {
      this.performRangedAttack(
         head, entity.getX(), entity.getY() + (double)entity.getEyeHeight() * 0.5, entity.getZ(), head == 0 && this.random.nextFloat() < 0.001F
      );
   }

   public void performRangedAttack(int head, double x, double y, double z, boolean dangerous) {
      if (!this.isSilent()) {
         this.level().levelEvent(null, 1024, this.blockPosition(), 0);
      }

      Vec3 pos = this.getHeadPos(head);
      double d3 = x - pos.x;
      double d4 = y - pos.y;
      double d5 = z - pos.z;
      WitherSkull witherskullentity = new WitherSkull(this.level(), this, d3, d4, d5);
      witherskullentity.setOwner(this);
      if (dangerous) {
         witherskullentity.setDangerous(true);
      }

      witherskullentity.setPosRaw(pos.x, pos.y, pos.z);
      this.level().addFreshEntity(witherskullentity);
   }

   public void spawnFlamingWitherSkull(int head, double x, double y, double z) {
      this.playSound(WitherStormModSoundEvents.WITHER_STORM_SHOOT.get(), head, Math.max(5.0F, this.getSoundVolume() - 5.0F), 1.0F);
      Vec3 pos = this.getHeadPos(head);
      double speed = (Double)WitherStormModConfig.SERVER.flamingSkullSpeedModifier.get();
      double d3 = (x - pos.x) * speed;
      double d4 = (y - pos.y) * speed;
      double d5 = (z - pos.z) * speed;
      FlamingWitherSkullEntity skull = new FlamingWitherSkullEntity(this.level(), this, d3, d4, d5);
      skull.setOwner(this);
      skull.setPosRaw(pos.x, pos.y, pos.z);
      this.level().addFreshEntity(skull);
   }

   public void spawnBlueFlamingWitherSkull(int head, double x, double y, double z) {
      this.playSound(WitherStormModSoundEvents.WITHER_STORM_SHOOT.get(), head, Math.max(5.0F, this.getSoundVolume() - 5.0F), 1.0F);
      Vec3 pos = this.getHeadPos(head);
      double speed = (Double)WitherStormModConfig.SERVER.flamingSkullSpeedModifier.get();
      double d3 = (x - pos.x) * speed;
      double d4 = (y - pos.y) * speed;
      double d5 = (z - pos.z) * speed;
      BlueFlamingWitherSkullEntity skull = new BlueFlamingWitherSkullEntity(this.level(), this, d3, d4, d5);
      skull.setOwner(this);
      skull.setPosRaw(pos.x, pos.y, pos.z);
      this.level().addFreshEntity(skull);
   }

   public double getDesiredSegmentX(int segment) {
      if (segment <= 0) {
         return this.getX();
      } else {
         double staticX = SEGMENT_DESIRED_X[segment - 1];
         double staticZ = SEGMENT_DESIRED_Z[segment - 1];
         if (this.isPlayingDead()) {
            staticX = 45.0;
            staticZ = 0.0;
         }

         float f = (this.yBodyRot + (float)(180 * (segment - 1))) * (float) (Math.PI / 180.0);
         float offset = (float)Mth.atan2(staticZ, staticX);
         float f1 = Mth.cos(f + offset);
         return this.getX() + (double)f1 * Math.sqrt(staticX * staticX + staticZ * staticZ);
      }
   }

   public double getDesiredSegmentY(int ignoredSegment) {
      double pos = this.getEyeY();
      if (this.isPlayingDead()) {
         pos = this.getBoundingBox().getCenter().y() + 10.0;
      }

      return pos;
   }

   public double getDesiredSegmentZ(int segment) {
      if (segment <= 0) {
         return this.getZ();
      } else {
         double staticX = SEGMENT_DESIRED_X[segment - 1];
         double staticZ = SEGMENT_DESIRED_Z[segment - 1];
         if (this.isPlayingDead()) {
            staticX = 45.0;
            staticZ = 0.0;
         }

         float f = (this.yBodyRot + (float)(180 * (segment - 1))) * (float) (Math.PI / 180.0);
         float offset = (float)Mth.atan2(staticZ, staticX);
         float f1 = Mth.sin(f + offset);
         return this.getZ() + (double)f1 * Math.sqrt(staticX * staticX + staticZ * staticZ);
      }
   }

   public void push(double deltaX, double deltaY, double deltaZ) {
   }

   public void knockback(double strength, double x, double z) {
   }

   @Override
   public Vec3 getHeadPos(int head) {
      return this.headManager.getHead(head).getHeadPos();
   }

   public void addAdditionalSaveData(@NotNull CompoundTag compound) {
      CompoundTag headsTag = new CompoundTag();

      for (WitherStormHead head : this.headManager.getHeads()) {
         headsTag.put(String.valueOf(head.getIndex()), head.save());
      }

      compound.put("Heads", headsTag);
      CompoundTag playDeadManagerNBT = new CompoundTag();
      PlayDeadManager playDeadManager = this.getPlayDeadManager();
      playDeadManagerNBT.putBoolean("PodiumPlaced", playDeadManager.isPodiumPlaced());
      if (playDeadManager.getPodiumPos() != null) {
         playDeadManagerNBT.put("PodiumPos", NbtUtils.writeBlockPos(playDeadManager.getPodiumPos()));
      }

      playDeadManagerNBT.putInt("StateTicks", playDeadManager.getTicks());
      playDeadManagerNBT.putInt("State", playDeadManager.getState().ordinal());
      playDeadManagerNBT.putInt("RevivalTime", playDeadManager.getTicksSinceRevival());
      playDeadManagerNBT.putBoolean("RecentlyRevived", playDeadManager.hasRecentlyBeenRevived());
      playDeadManagerNBT.putInt("CommandBlockMissingTicks", playDeadManager.getTicksSinceCommandBlockMissing());
      compound.put("PlayDeadManager", playDeadManagerNBT);
      compound.putInt("Phase", this.getPhase());
      super.addAdditionalSaveData(compound);
      compound.putInt("Invul", this.getInvulnerableTicks());
      compound.putInt("StartingInvul", this.getStartingInvulnerableTicks());
      compound.putInt("ConsumedEntities", this.getConsumedEntities());
      compound.putBoolean("OtherHeadsDisabled", this.areOtherHeadsDisabled());
      this.targetManager.ifPresent(manager -> manager.save(compound));
      compound.putFloat("YBodyRot", this.yBodyRot);
      compound.putFloat("XBodyRot", this.xBodyRot);
      compound.putBoolean("Mirrored", this.isMirrored());
      this.summoningManager.ifPresent(manager -> compound.putInt("SymbiontSummoningCooldown", manager.getSummoningDelay()));
      compound.putBoolean("ShouldShowHole", (Boolean)this.entityData.get(SHOULD_SHOW_HOLE));
      compound.putBoolean("Resummoned", this.resummoned);
      CompoundTag tag = new CompoundTag();
      this.evolutionProfiler.save(tag);
      compound.put("EvolutionProfiler", tag);
      ListTag consumedPets = new ListTag();

      for (Entry<UUID, CompoundTag> entry : this.consumedPets.entrySet()) {
         UUID id = entry.getKey();
         CompoundTag entity = entry.getValue();
         CompoundTag entityTag = new CompoundTag();
         entityTag.putUUID("id", id);
         entityTag.put("Entity", entity);
         consumedPets.add(entityTag);
      }

      compound.put("ConsumedPets", consumedPets);
      compound.put("TrackedEntities", this.getTrackedEntities().save());
      compound.put("IgnoredTargets", this.getIgnoredTargets().save());
      ListTag playingJukeboxes = new ListTag();

      for (BlockPos pos : this.playingJukeboxes) {
         CompoundTag entry = new CompoundTag();
         entry.put("Pos", NbtUtils.writeBlockPos(pos));
         playingJukeboxes.add(entry);
      }

      compound.put("PlayingJukeboxes", playingJukeboxes);
      compound.putBoolean("IsConsumptionLocked", this.isLocked);
   }

   public void readAdditionalSaveData(CompoundTag compound) {
      CompoundTag headsTag = compound.getCompound("Heads");

      for (WitherStormHead head : this.headManager.getHeads()) {
         head.read(headsTag.getCompound(String.valueOf(head.getIndex())));
      }

      CompoundTag playDeadManagerNBT = compound.getCompound("PlayDeadManager");
      PlayDeadManager playDeadManager = this.getPlayDeadManager();
      playDeadManager.setPodiumPlaced(playDeadManagerNBT.getBoolean("PodiumPlaced"));
      if (playDeadManagerNBT.contains("PodiumPos")) {
         playDeadManager.setPodiumPos(NbtUtils.readBlockPos(playDeadManagerNBT.getCompound("PodiumPos")));
      }

      playDeadManager.setTickAmountAndO(playDeadManagerNBT.getInt("StateTicks"));
      int state = playDeadManagerNBT.getInt("State");
      if (state >= 0 && state < PlayDeadManager.State.values().length) {
         playDeadManager.setState(PlayDeadManager.State.values()[state]);
      }

      playDeadManager.setTicksSinceRevival(playDeadManagerNBT.getInt("RevivalTime"));
      playDeadManager.setRecentlyRevived(playDeadManagerNBT.getBoolean("RecentlyRevived"));
      playDeadManager.setTicksSinceCommandBlockMissing(playDeadManagerNBT.getInt("CommandBlockMissingTicks"));
      if (compound.contains("ConsumedEntities")) {
         this.setPhase(compound.getInt("Phase"), compound.getInt("ConsumedEntities"));
      } else {
         this.setPhase(compound.getInt("Phase"));
      }

      super.readAdditionalSaveData(compound);
      this.setInvulnerableTicks(compound.getInt("Invul"));
      this.setStartingInvulnerableTicks(compound.getInt("StartingInvul"));
      if (this.hasCustomName()) {
         this.bossEvent.ifPresent(bossEvent -> bossEvent.setName(this.getDisplayName()));
      }

      this.setOtherHeadsDisabled(compound.getBoolean("OtherHeadsDisabled"));
      this.targetManager.ifPresent(manager -> manager.read(compound));
      this.setYBodyRot(compound.getFloat("YBodyRot"));
      this.setXBodyRot(compound.getFloat("XBodyRot"));
      this.setMirrored(compound.getBoolean("Mirrored"));
      this.getBossInfo().ifPresent(info -> info.setVisible(!this.isPlayingDead()));
      this.summoningManager.ifPresent(manager -> manager.setSummoningDelay(compound.getInt("SymbiontSummoningCooldown")));
      this.entityData.set(SHOULD_SHOW_HOLE, compound.getBoolean("ShouldShowHole"));
      this.resummoned = compound.getBoolean("Resummoned");
      CompoundTag tag = compound.getCompound("EvolutionProfiler");
      this.evolutionProfiler.read(tag);
      this.consumedPets.clear();
      ListTag consumedPets = compound.getList("ConsumedPets", 10);

      for (int i = 0; i < consumedPets.size(); i++) {
         CompoundTag entityTag = consumedPets.getCompound(i);
         this.consumedPets.put(entityTag.getUUID("id"), entityTag.getCompound("Entity"));
      }

      this.getTrackedEntities().read(compound.getCompound("TrackedEntities"));
      this.getIgnoredTargets().read(compound.getCompound("IgnoredEntities"));
      this.playingJukeboxes.clear();
      ListTag playingJukeboxes = compound.getList("PlayingJukeboxes", 10);

      for (int i = 0; i < playingJukeboxes.size(); i++) {
         CompoundTag entry = playingJukeboxes.getCompound(i);
         this.playingJukeboxes.add(NbtUtils.readBlockPos(entry.getCompound("Pos")));
      }

      this.isLocked = compound.getBoolean("IsConsumptionLocked");
   }

   public void setCustomName(Component component) {
      super.setCustomName(component);
      this.bossEvent.ifPresent(bossEvent -> bossEvent.setName(this.getDisplayName()));
   }

   public void setInvulnerableTicks(int ticks) {
      this.entityData.set(INVULNERABLE, ticks);
   }

   public int phaseRadiusMultiplier(int phase) {
      double multiplier = switch (phase) {
         case 0 -> 1.0;
         case 1, 2 -> 1.25;
         case 3 -> 1.5;
         case 4 -> 1.75;
         case 5 -> 2.0;
         case 6 -> 2.25;
         case 7 -> 2.5;
         default -> throw new IllegalStateException("Unexpected Wither Storm Phase value: " + phase);
      };
      return (int)multiplier;
   }

   public int phaseRadiusMultiplierNature(int phase) {
      double multiplier = switch (phase) {
         case 0 -> 1.0;
         case 1, 2 -> 1.125;
         case 3 -> 1.25;
         case 4 -> 1.5;
         case 5, 6 -> 1.75;
         case 7 -> 2.25;
         default -> throw new IllegalStateException("Erm... Unexpected Wither Storm Phase value: " + phase);
      };
      return (int)multiplier;
   }

   public int getInvulnerableTicks() {
      return (Integer)this.entityData.get(INVULNERABLE);
   }

   public void setStartingInvulnerableTicks(int ticks) {
      this.entityData.set(STARTING_INVULNERABLE, ticks);
   }

   public int getStartingInvulnerableTicks() {
      return (Integer)this.entityData.get(STARTING_INVULNERABLE);
   }

   public int getPhase() {
      return (Integer)this.entityData.get(PHASE);
   }

   public int getConsumptionAmountForPhase(int phase) {
      int consumptionAmount = switch (phase) {
         case 0 -> 100;
         case 1 -> 400;
         case 2 -> 1200;
         case 3 -> 18800;
         case 4 -> 195000;
         case 5 -> 351400;
         case 6 -> 580800;
         case 7 -> 2125000;
         default -> 0;
      };
      return this.adjustAmountForEvolutionSpeed(consumptionAmount);
   }

   public int adjustAmountForEvolutionSpeed(int consumptionAmount) {
      return (int)((double)consumptionAmount * this.getEvolutionSpeedModifier());
   }

   public double getEvolutionSpeedModifier() {
      double modifier = this.attributeOrConfigValue(
         (Attribute)WitherStormModAttributes.EVOLUTION_SPEED.get(), WitherStormModConfig.SERVER.evolutionAttributeModifier
      );
      WitherStormModifyEvolutionSpeedEvent event = new WitherStormModifyEvolutionSpeedEvent(this, modifier);
      MinecraftForge.EVENT_BUS.post(event);
      return event.getOriginalEvolutionSpeedModifier();
   }

   public boolean setPhase(int phase) {
      return this.setPhase(phase, this.getConsumptionAmountForPhase(phase - 1));
   }

   public boolean setPhase(int phase, int consumedEntities) {
      if (phase >= 0 && phase <= 7) {
         MinecraftForge.EVENT_BUS.post(new WitherStormChangePhaseEvent(this, phase));
         this.entityData.set(PHASE, phase);
         this.clusterRadius = (float)((int)Math.max(1.0F, (float)this.getPhase() * 0.75F));
         this.entityConsumptionRadius = this.getPhase() > 3 ? 80 : 12 + Math.round((float)this.getConsumedEntities() * 0.00445F);
         this.setConsumedEntities(consumedEntities);
         this.reapplyPosition();
         this.refreshDimensions();
         if (this.isAddedToWorld()) {
            if (phase < 6) {
               this.removeSegments();
            } else {
               this.readdSegments();
            }
         }

         if (phase == 6) {
            if (this.getConsumedEntities() < this.getSubPhaseRequirement(phase)) {
               this.setOtherHeadsDisabled(true);
            }
         } else {
            this.setOtherHeadsDisabled(false);
         }

         this.updateSections();
         if (!this.level().isClientSide) {
            if (this.shouldPlaySoundLoops()) {
               CreateLoopingSoundMessage message = new CreateLoopingSoundMessage(this);
               WitherStormModPacketHandlers.MAIN.send(PacketDistributor.DIMENSION.with(() -> this.level().dimension()), message);
            } else {
               RemoveSoundLoopMessage message = new RemoveSoundLoopMessage(this);
               WitherStormModPacketHandlers.MAIN.send(PacketDistributor.DIMENSION.with(() -> this.level().dimension()), message);
            }
         }

         this.headManager.update(phase);
         AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
         AttributeInstance armor = this.getAttribute(Attributes.ARMOR);
         if (!this.level().isClientSide) {
            if (phase < 4) {
               health.removePermanentModifier(HEALTH_MODIFIER_UUID);
               armor.removePermanentModifier(ARMOR_MODIFIER_UUID);
               this.currentFlyingHeight = 10.0;
            } else {
               AttributeModifier healthModifier = new AttributeModifier(HEALTH_MODIFIER_UUID, "Phase health modifier", 624.0, Operation.ADDITION);

               assert health != null;

               if (!health.hasModifier(healthModifier)) {
                  health.addPermanentModifier(healthModifier);
               }

               AttributeModifier armorModifier = new AttributeModifier(
                  ARMOR_MODIFIER_UUID, "Phase armor modifier", (double)((phase + 1) * 2), Operation.ADDITION
               );

               assert armor != null;

               if (!armor.hasModifier(armorModifier)) {
                  armor.addPermanentModifier(armorModifier);
               }

               this.currentFlyingHeight = (double)((Integer)WitherStormModConfig.SERVER.flyingHeight.get()).intValue();
            }
         }

         this.segments.ifPresent(manager -> {
            for (WitherStormSegmentEntity entity : manager.getSegments()) {
               if (entity != null) {
                  entity.setPhase(this.getPhase());
               }
            }
         });
         return true;
      } else {
         return false;
      }
   }

   protected void updateSections() {
      if (this.getPhase() == 5) {
         if (this.getConsumptionAmountForPhase(this.getPhase()) <= this.getConsumedEntities()) {
            this.sections[0].setSize(60.0F, 35.0F);
            this.sections[1].setSize(60.0F, 35.0F);
            this.sections[0].setOffset(24.0, 28.0, 0.0);
            this.sections[1].setOffset(-24.0, 28.0, 0.0);
            this.fallingSection.setSize(50.0F, 70.0F);
            this.fallingSection.setOffset(0.0, 60.0, 30.0);
         } else {
            this.sections[0].setSize(30.0F, 15.0F);
            this.sections[1].setSize(30.0F, 15.0F);
            this.sections[0].setOffset(13.0, 28.0, 0.0);
            this.sections[1].setOffset(-13.0, 28.0, 0.0);
            this.fallingSection.setSize(30.0F, 35.0F);
            this.fallingSection.setOffset(0.0, 35.0, 15.0);
         }
      } else if (this.getPhase() == 6) {
         this.sections[0].setSize(60.0F, 35.0F);
         this.sections[1].setSize(60.0F, 35.0F);
         this.sections[0].setOffset(24.0, 28.0, 0.0);
         this.sections[1].setOffset(-24.0, 28.0, 0.0);
         this.fallingSection.setSize(50.0F, 70.0F);
         this.fallingSection.setOffset(0.0, 60.0, 30.0);
      } else if (this.getPhase() == 7) {
         this.sections[0].setSize(95.0F, 45.0F);
         this.sections[1].setSize(95.0F, 45.0F);
         this.sections[0].setOffset(28.0, 28.0, 0.0);
         this.sections[1].setOffset(-28.0, 28.0, 0.0);
         this.fallingSection.setSize(60.0F, 85.0F);
         this.fallingSection.setOffset(0.0, 80.0, 30.0);
      }
   }

   public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> parameter) {
      super.onSyncedDataUpdated(parameter);
      if (parameter.equals(PHASE)) {
         this.updateSections();
         this.refreshDimensions();
         this.headManager.update(this.getPhase());
      } else if (parameter.equals(CONSUMED_ENTITIES)) {
         this.updateSections();
      }
   }

   public void makeInvulnerable() {
      this.setInvulnerableTicks((Integer)WitherStormModConfig.SERVER.invulnerabilityTime.get() * 20);
      this.bossEvent.ifPresent(event -> event.setProgress(0.0F));
      this.setHealth(1.0F);
   }

   public void makeStuckInBlock(@NotNull BlockState blockState, @NotNull Vec3 vector3d) {
   }

   public void startSeenByPlayer(@NotNull ServerPlayer player) {
      super.startSeenByPlayer(player);
      this.playersTracking.add(player);
      CommandBlockEntity commandBlock = this.getBowelsCommandBlock();
      if (commandBlock != null) {
         commandBlock.getOutsideBossBarViewers().add(player);
      }
   }

   @Override
   public SoundEvent getBossTheme() {
      if (this.hasRecentlyBeenRevived()) {
         return WitherStormModSoundEvents.WITHER_STORM_REVIVAL_THEME.get();
      } else if (this.getPhase() == 5 && this.getConsumptionAmountForPhase(5) < this.getConsumedEntities()) {
         return WitherStormModSoundEvents.WITHER_STORM_FORMIDIBOMB_THEME.get();
      } else {
         return this.isBeingTornApart()
            ? WitherStormModSoundEvents.WITHER_STORM_BOWELS_EXPOSED_THEME.get()
            : WitherStormModSoundEvents.WITHER_STORM_BOSS_THEME.get();
      }
   }

   @Override
   public int priority() {
      return 1;
   }

   @Override
   public boolean shouldPlayBossTheme() {
      boolean commandBlockOverride = false;
      CommandBlockEntity commandBlock = this.getPlayDeadManager().getCommandBlock();
      if (commandBlock != null && commandBlock.isAlive()) {
         commandBlockOverride = commandBlock.getState().shouldShowOwnerBossBar();
      }

      return BossThemeEntity.super.shouldPlayBossTheme()
         && this.shouldPlaySoundLoop
         && !this.isNoAi()
         && !this.isSilent()
         && (!this.isDeadOrPlayingDead() || commandBlockOverride);
   }

   @Override
   public boolean checkConfig() {
      return (Boolean)WitherStormModConfig.CLIENT.playWitherStormTheme.get();
   }

   @Override
   public boolean smartBossMusic() {
      return (Boolean)WitherStormModConfig.SERVER.smartBossbar.get();
   }

   @Override
   public boolean isStillAlive() {
      return this.isAlive();
   }

   @Override
   public Vec3 getPosition() {
      return this.position();
   }

   public void stopSeenByPlayer(@NotNull ServerPlayer player) {
      super.stopSeenByPlayer(player);
      this.playersTracking.remove(player);
      this.bossEvent.ifPresent(bossEvent -> bossEvent.removePlayer(player));
      CommandBlockEntity commandBlock = this.getBowelsCommandBlock();
      if (commandBlock != null) {
         commandBlock.removeOutsideBossBarViewer(player);
      }
   }

   public void checkDespawn() {
      this.noActionTime = 0;
   }

   public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, @NotNull DamageSource source) {
      int damage = this.calculateFallDamage(p_225503_1_, p_225503_2_);
      if (damage > 15) {
         this.onBigFall();
      }

      return false;
   }

   public void onBigFall() {
      if (this.getPhase() > 3) {
         this.playSound(WitherStormModSoundEvents.WITHER_STORM_THUMP.get(), this.getSoundVolume() + 3.0F, 1.0F);
         this.shake(30.0F, 12.0F);
      }
   }

   public boolean addEffect(@NotNull MobEffectInstance effect, @Nullable Entity entity) {
      return false;
   }

   @NotNull
   public MobType getMobType() {
      return WitherStormModMobTypes.SICKENED;
   }

   protected boolean canRide(@NotNull Entity entity) {
      return false;
   }

   public boolean canChangeDimensions() {
      return false;
   }

   public boolean canBeAffected(@NotNull MobEffectInstance effect) {
      return false;
   }

   public boolean isPowered() {
      return this.getInvulnerableTicks() > 900 && this.getPhase() < 4;
   }

   public boolean attackable() {
      return this.shouldDoNothing() && this.getPhase() < 4;
   }

   public boolean hurt(DamageSource source, float floatIn) {
      if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
         return super.hurt(source, floatIn);
      } else if (this.isInvulnerableTo(source)) {
         return false;
      } else if (source.is(DamageTypes.DROWN) || source.getEntity() instanceof WitherStormEntity) {
         return false;
      } else if (this.shouldDoNothing() && !source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
         return false;
      } else if (this.getPhase() > 3 && this.isCompletelyInvulnerable()) {
         return false;
      } else {
         if (this.isPowered()) {
            Entity entity = source.getDirectEntity();
            if (entity instanceof AbstractArrow) {
               return false;
            }
         }

         Entity entity1 = source.getEntity();
         if (!(entity1 instanceof Player) && entity1 instanceof LivingEntity && ((LivingEntity)entity1).getMobType() == this.getMobType()) {
            return false;
         } else {
            if (this.destroyBlocksTick <= 0) {
               this.destroyBlocksTick = 20;
            }

            for (AdditionalHead head : this.headManager.getOtherHeads()) {
               head.idleHeadUpdates += 3;
            }

            return super.hurt(source, floatIn);
         }
      }
   }

   public int getConsumedEntities() {
      return (Integer)this.entityData.get(CONSUMED_ENTITIES);
   }

   public void setConsumedEntities(int newAmount) {
      this.entityData.set(CONSUMED_ENTITIES, newAmount);
      this.updateSections();
      if (this.getPhase() == 6) {
         int amount = this.getSubPhaseRequirement(this.getPhase());
         if (newAmount > amount && this.areOtherHeadsDisabled()) {
            this.setOtherHeadsDisabled(false);

            for (AdditionalHead head : this.headManager.getOtherHeads()) {
               head.setNextRoarTick(this.tickCount + this.random.nextInt(30));
            }

            this.getSegmentsManager().ifPresent(manager -> {
               for (WitherStormSegmentEntity entity : manager.getSegments()) {
                  if (entity != null) {
                     for (AdditionalHead headx : entity.headManager.getOtherHeads()) {
                        headx.setNextRoarTick(entity.tickCount + entity.random.nextInt(30));
                     }
                  }
               }
            });
         }
      }
   }

   public void consumeEntity(@Nullable Entity entity, int amount) {
      WitherStormConsumeEvent event = new WitherStormConsumeEvent(this, entity, amount);
      if (!MinecraftForge.EVENT_BUS.post(event)) {
         amount = event.getConsumedAmount();
         this.addToConsumedEntities(amount);
      }
   }

   public void addToConsumedEntities(int amount) {
      if (!this.isConsumptionLocked()) {
         this.setConsumedEntities(this.getConsumedEntities() + amount);
      }
   }

   public int getMaxHeadXRot() {
      return this.getPhase() > 3 ? 180 : super.getMaxHeadXRot();
   }

   @Nullable
   public LivingEntity getUltimateTarget() {
      return this.targetManager.map(UltimateTargetManager::getUltimateTarget).orElse(null);
   }

   @Nullable
   public Vec3 getUltimateTargetPos() {
      return this.targetManager.<Vec3>map(manager -> {
         if (manager.isDistracted()) {
            assert manager.getDistractedPos() != null;

            return Vec3.atCenterOf(manager.getDistractedPos());
         } else if (manager.isRandomStrolling()) {
            assert manager.getRandomStrollPos() != null;

            return Vec3.atCenterOf(manager.getRandomStrollPos());
         } else {
            return manager.getUltimateTargetPos();
         }
      }).orElse(null);
   }

   public Section[] getSections() {
      return this.xBodyRot != 0.0F ? new Section[]{this.fallingSection, this.sections[2]} : this.sections;
   }

   public void onAddedToWorld() {
      super.onAddedToWorld();
      if (!this.level().isClientSide) {
         WitherStormSyncHelper.sendWitherStormToClient(this);
         this.bowelsInstance.ifPresent(BowelsInstanceManager::loadInstance);
      }
   }

   public void findSegments() {
      if (!this.level().isClientSide) {
         this.getSegmentsManager().ifPresent(manager -> {
            ServerLevel world = (ServerLevel)this.level();
            manager.findSegments(world);
         });
      }
   }

   public void createSegments() {
      this.getSegmentsManager().ifPresent(SegmentsManager::createSegments);
   }

   public void removeSegments() {
      this.getSegmentsManager().ifPresent(SegmentsManager::removeSegments);
   }

   public void readdSegments() {
      this.getSegmentsManager().ifPresent(SegmentsManager::readdSegments);
   }

   public Optional<SegmentsManager> getSegmentsManager() {
      return this.segments;
   }

   public void addSegments() {
      this.getSegmentsManager().ifPresent(SegmentsManager::addSegments);
   }

   @Override
   public boolean areOtherHeadsDisabled() {
      return this.headManager.areOtherHeadsDisabled();
   }

   public void setOtherHeadsDisabled(boolean value) {
      this.headManager.setOtherHeadsDisabled(value);
   }

   public void setMirrored(boolean mirrored) {
      this.entityData.set(MIRRORED, mirrored);
   }

   public boolean isMirrored() {
      return (Boolean)this.entityData.get(MIRRORED);
   }

   public void createDebrisClusters(boolean hidden) {
      Builder<DebrisCluster> builder = ImmutableList.builder();

      for (int i = 0; i < 100; i++) {
         DebrisCluster cluster = new DebrisCluster(
            this.random.nextFloat() * 360.0F,
            this.random.nextFloat() * 100.0F,
            40.0F + this.random.nextFloat() * 160.0F,
            this.random.nextFloat() * 2.0F - 1.0F,
            1.0F
         );
         cluster.randomize(this.random, 15, 18.0F);
         cluster.setDisabled(hidden);
         cluster.determineRenderPhase();
         builder.add(cluster);
      }

      Builder<DebrisCluster> hunchbackClustersBuilder = ImmutableList.builder();

      for (int i = 0; i < 30; i++) {
         DebrisCluster cluster = new DebrisCluster(
            this.random.nextFloat() * 360.0F,
            this.random.nextFloat() * 3.0F,
            this.random.nextFloat() * 0.75F + 1.25F,
            this.random.nextFloat() * 8.0F - 4.0F,
            0.125F
         );
         cluster.randomize(this.random, 1, 0.1F);
         cluster.setDisabled(hidden);
         cluster.setGlowing(false);
         cluster.setRenderPhase(1);
         hunchbackClustersBuilder.add(cluster);
      }

      for (int i = 0; i < 20; i++) {
         DebrisCluster cluster = new DebrisCluster(
            this.random.nextFloat() * 360.0F,
            this.random.nextFloat() * 4.0F,
            this.random.nextFloat() * 5.0F + 2.0F,
            this.random.nextFloat() * 12.0F - 6.0F,
            0.25F
         );
         cluster.randomize(this.random, 1, 0.5F);
         cluster.setDisabled(hidden);
         cluster.setGlowing(false);
         cluster.setRenderPhase(2);
         hunchbackClustersBuilder.add(cluster);
      }

      for (int i = 0; i < 5; i++) {
         DebrisCluster cluster = new DebrisCluster(
            this.random.nextFloat() * 360.0F,
            this.random.nextFloat() * 4.0F,
            this.random.nextFloat() * 1.25F + 3.75F,
            this.random.nextFloat() * 6.0F - 3.0F,
            0.125F
         );
         cluster.randomize(this.random, 3, 0.5F);
         cluster.setGlowing(true);
         cluster.setDisabled(hidden);
         cluster.setRenderPhase(2);
         hunchbackClustersBuilder.add(cluster);
      }

      for (int i = 0; i < 25; i++) {
         DebrisCluster cluster = new DebrisCluster(
            this.random.nextFloat() * 360.0F,
            this.random.nextFloat() * 20.0F - 10.0F,
            this.random.nextFloat() * 20.0F + 5.0F,
            this.random.nextFloat() * 6.0F - 3.0F,
            0.25F
         );
         cluster.randomize(this.random, 4, 2.5F);
         cluster.setGlowing(false);
         cluster.setDisabled(hidden);
         cluster.setRenderPhase(3);
         hunchbackClustersBuilder.add(cluster);
      }

      for (int i = 0; i < 5; i++) {
         DebrisCluster cluster = new DebrisCluster(
            this.random.nextFloat() * 360.0F,
            this.random.nextFloat() * 15.0F - 5.0F,
            this.random.nextFloat() * 7.5F + 20.0F,
            this.random.nextFloat() * 2.0F - 1.0F,
            0.125F
         );
         cluster.randomize(this.random, 8, 1.0F);
         cluster.setGlowing(true);
         cluster.setDisabled(hidden);
         cluster.setRenderPhase(3);
         hunchbackClustersBuilder.add(cluster);
      }

      for (int i = 0; i < 15; i++) {
         DebrisCluster cluster = new DebrisCluster(
            this.random.nextFloat() * 360.0F,
            this.random.nextFloat() * 10.0F - 5.0F,
            this.random.nextFloat() * 24.0F + 6.0F,
            this.random.nextFloat() * 5.0F - 2.5F,
            1.0F
         );
         cluster.randomize(this.random, 1, 0.5F);
         cluster.setDisabled(hidden);
         cluster.setGlowing(false);
         cluster.setRenderPhase(3);
         hunchbackClustersBuilder.add(cluster);
      }

      this.hunchbackDebrisClusters = hunchbackClustersBuilder.build();
      this.debrisClusters = builder.build();
   }

   public void createDebrisRings(boolean hidden) {
      this.debrisRings = ImmutableList.of(
         new DebrisRingSettings(16, 100.0F, 60.0F, 30.0F, 25.0F, 0.02F, true, 4, hidden),
         new DebrisRingSettings(24, 160.0F, 120.0F, 10.0F, 50.0F, 0.005F, false, 4, hidden),
         new DebrisRingSettings(24, 180.0F, 100.0F, 30.0F, 60.0F, 0.001F, true, 4, hidden),
         new DebrisRingSettings(24, 130.0F, 50.0F, 80.0F, 10.0F, 0.008F, false, 4, hidden),
         new DebrisRingSettings(36, 240.0F, 200.0F, 0.0F, 40.0F, 0.002F, true, 6, hidden),
         new DebrisRingSettings(36, 250.0F, 210.0F, -30.0F, 10.0F, 0.001F, true, 6, hidden)
      );
   }

   public List<DebrisRingSettings> getDebrisRings() {
      return this.debrisRings;
   }

   public List<DebrisCluster> getDebrisClusters() {
      return this.getPhase() > 3 ? this.debrisClusters : this.hunchbackDebrisClusters;
   }

   public int getDeathTime() {
      return this.witherStormDeathTime;
   }

   public void startSleeping(@NotNull BlockPos pos) {
   }

   public boolean canBeLeashed(@NotNull Player player) {
      return false;
   }

   public final boolean isOnDistantRenderer() {
      return this.isOnDistantRenderer;
   }

   public final void setOnDistantRenderer() {
      this.isOnDistantRenderer = true;
   }

   public boolean isEntityNearby(Entity entity) {
      return this.getSearchBox().contains(entity.position());
   }

   public AABB getSearchBox() {
      double range = this.getPhase() > 3 ? this.getAttributeValue(Attributes.FOLLOW_RANGE) : this.getAttributeValue((Attribute)WitherStormModAttributes.HUNCHBACK_FOLLOW_RANGE.get());
      return this.getPhase() > 3 ? this.getBoundingBox().inflate(range, range + 255.0, range) : this.getBoundingBox().inflate(range, range * 2.0, range);
   }

   public void playSound(SoundEvent event, int head, float volume, float pitch) {
      Vec3 pos = this.getHeadPos(head);
      if (!this.isSilent()) {
         this.level().playSound(null, pos.x, pos.y, pos.z, event, this.getSoundSource(), volume, pitch);
      }
   }

   @Override
   public float getTentacleAnimation(float partialTicks) {
      return Mth.lerp(partialTicks, (float)this.tentacleTickCountO, (float)this.tentacleTickCount);
   }

   @Override
   public float getFadeAnimation(float partialTicks) {
      return Mth.lerp(partialTicks, this.onGroundAnimationO, this.onGroundAnimation);
   }

   @Override
   public float getMouthAnimation(int head, float partialTicks) {
      return this.headManager.getHead(head).getMouthAnimation(partialTicks);
   }

   @Override
   public float getBrokenJawAnimation(int head, float partialTicks) {
      return this.headManager.getHead(head).getBrokenJawAnimation(partialTicks);
   }

   @Override
   public float getFadeAnimation() {
      return this.onGroundAnimation;
   }

   public boolean isBeingTornApart() {
      if (!(Boolean)WitherStormModConfig.SERVER.shouldShowHole.get() || this.getPhase() < 7) {
         return false;
      } else {
         return this.getConsumedEntities() >= this.getConsumptionAmountForPhase(7) ? true : (Boolean)this.entityData.get(SHOULD_SHOW_HOLE);
      }
   }

   public void setShouldShowHole(boolean flag) {
      this.entityData.set(SHOULD_SHOW_HOLE, flag);
   }

   public PlayDeadManager getPlayDeadManager() {
      return this.playDeadManager;
   }

   public boolean shouldDoNothing() {
      return this.getInvulnerableTicks() > 0 ? true : this.getPlayDeadManager().getState().disablesAi();
   }

   @Override
   public boolean isDeadOrPlayingDead() {
      return this.isDeadOrDying() ? true : this.getPlayDeadManager().getState().disablesAi();
   }

   @Override
   public boolean isPlayingDead() {
      return this.getPlayDeadManager().getState().disablesAi();
   }

   public boolean canEvolve(boolean force) {
      return force ? this.getPhase() < 7 : this.evolutionProfiler.isProfiling() || this.getPhase() < 5 || this.getPhase() > 5 && this.getPhase() < 7;
   }

   public int getSubPhaseRequirement(int phase) {
      int amount = this.getConsumptionAmountForPhase(phase - 1);
      return amount + (this.getConsumptionAmountForPhase(phase) - amount) / 2;
   }

   public boolean shouldDoCustomMovement() {
      PlayDeadManager.State state = this.getPlayDeadManager().getState();
      return state == PlayDeadManager.State.FALLING && this.getPlayDeadManager().getTicks() > 300 ? false : this.shouldDoCustomMovement;
   }

   public PersistentTrackedEntities getTrackedEntities() {
      return this.trackedEntities;
   }

   public Optional<ServerBossEvent> getBossInfo() {
      return this.bossEvent;
   }

   public boolean canFallOnBack() {
      return true;
   }

   @Override
   public void writeData(FriendlyByteBuf buffer) {
      this.headManager.packHeadRotations().toPacket(buffer);
      buffer.writeBoolean(this.onGround());
      buffer.writeByte((byte)Mth.floor(this.yBodyRot * 256.0F / 360.0F));
      buffer.writeByte((byte)Mth.floor(this.xBodyRot * 256.0F / 360.0F));
   }

   @Override
   public void readData(FriendlyByteBuf buffer) {
      this.headManager.updateHeadsFromPacked(HeadManager.PackedHeadRots.fromPacket(buffer));
      this.setOnGround(buffer.readBoolean());
      this.yBodyRot = (float)(buffer.readByte() * 360) / 256.0F;
      this.yBodyRotO = this.yBodyRot;
      this.xBodyRot = (float)(buffer.readByte() * 360) / 256.0F;
      this.xBodyRotO = this.xBodyRot;
   }

   public boolean isPushable() {
      return false;
   }

   public boolean isPushedByFluid() {
      return false;
   }

   public void setXBodyRot(float rot) {
      this.xBodyRot = rot;
   }

   protected void pushEntities() {
      if (!this.canFallOnBack() || this.getPlayDeadManager().getState() != PlayDeadManager.State.PLAYING_DEAD) {
         super.pushEntities();
      }
   }

   public void onFallOnBack() {
      this.playSound(WitherStormModSoundEvents.WITHER_STORM_THUMP.get(), this.getSoundVolume() + 3.0F, 1.0F);
      this.shake(30.0F, 12.0F);
   }

   public boolean isOnBack() {
      return this.xBodyRot >= 90.0F;
   }

   public void shake(float duration, float power) {
      if (!this.level().isClientSide) {
         ShakeScreenMessage message = new ShakeScreenMessage(duration, power);
         WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), message);
      }
   }

   public void setFormidibomb(FormidibombEntity entity) {
      this.getPlayDeadManager().setFormidibomb(entity);
   }

   @Nullable
   public FormidibombEntity getFormidibomb() {
      return this.getPlayDeadManager().getFormidibomb();
   }

   public boolean canBeFormidibombed(boolean isExplosion) {
      if (this.shouldIgnoreFormidibomb) {
         return false;
      } else if (this.getPhase() < 5) {
         return false;
      } else if (this.getPhase() > 6 && this.isBeingTornApart()) {
         return false;
      } else if (this.getFormidibomb() == null) {
         return false;
      } else if ((Boolean)WitherStormModConfig.SERVER.endOfPhaseFiveBombableExclusively.get() && this.getPhaseProgress() < 1.0F) {
         return false;
      } else {
         FormidibombEntity formidibomb = this.getFormidibomb();
         return !isExplosion && !formidibomb.isAlive() ? false : !((float)formidibomb.getFuseLife() > 600.0F + this.distanceTo(formidibomb));
      }
   }

   public void explode() {
      if (this.getPhase() > 3 && !this.isDeadOrPlayingDead()) {
         this.getPlayDeadManager().explode();
      }
   }

   public void reviveFromPlayingDead() {
      if (this.isPlayingDead()) {
         this.getPlayDeadManager().revive();
      }
   }

   public boolean isReviving() {
      return this.getPlayDeadManager().getState() == PlayDeadManager.State.REVIVING;
   }

   public boolean ignoreExplosion() {
      return true;
   }

   public boolean isAttractingFormidibomb() {
      boolean flag = false;
      Stream<WrappedGoal> goals = this.goalSelector.getRunningGoals();

      for (WrappedGoal prioritizedGoal : goals.toArray(WrappedGoal[]::new)) {
         Goal var8 = prioritizedGoal.getGoal();
         if (var8 instanceof LookAtFormidibombGoal) {
            LookAtFormidibombGoal goal = (LookAtFormidibombGoal)var8;
            if (goal.hasTarget()) {
               flag = true;
            }
         }
      }

      return flag;
   }

   public boolean isNearbyTickingFormidibomb() {
      FormidibombEntity formidibomb = this.getFormidibomb();
      return formidibomb != null && formidibomb.isAlive() && formidibomb.getStartFuse() > 0 && formidibomb.getFuseLife() <= 800;
   }

   public boolean targetApplicable(LivingEntity entity, int head, TargetingConditions conditions) {
      if (entity == this) {
         return false;
      } else if (!conditions.test(this, entity)) {
         return false;
      } else if (!this.headManager.getHead(head).canSee(entity)) {
         return false;
      } else if (this.ignoredTargets.shouldIgnoreEntity(entity)) {
         return false;
      } else if (this.trackedEntities.contains(entity)) {
         return false;
      } else {
         if (entity instanceof Player player) {
            if (player.getMainHandItem().getItem() instanceof ShieldItem) {
               if (player.isUsingItem()) {
                  return false;
               }
            } else if (player.getOffhandItem().getItem() instanceof ShieldItem && player.isUsingItem()) {
               return false;
            }

            if (this.hasRecentlyBeenRevived()) {
               return false;
            }
         }

         if (this.getPhase() > 3 && entity.isInvisible()) {
            return false;
         } else if (this.getPhase() <= 3 || !this.targetInUseBySegment(entity) && !this.alreadyATarget(entity, head != 0)) {
            if (this.isInsideOtherTractorBeam(entity, head)) {
               return false;
            } else {
               for (LivingEntity entityBelow : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(10.0, 255.0, 10.0))) {
                  if (entityBelow.is(entity)) {
                     return false;
                  }
               }

               if (this.getPhase() > 3 && this.isEntityBehindBack(entity)) {
                  return false;
               } else {
                  LazyOptional<PlayerWitherStormData> optional = entity.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA);
                  if (optional.isPresent()) {
                     PlayerWitherStormData data = (PlayerWitherStormData)optional.resolve().get();
                     if (data.hasKilledSymbiontRecently()) {
                        return false;
                     }
                  }

                  return MinecraftForge.EVENT_BUS.post(new CanWitherStormTargetMobEvent(this, entity))
                     ? false
                     : !MinecraftForge.EVENT_BUS.post(new CanWitherStormTargetMobEvent(this, entity));
               }
            }
         } else {
            return false;
         }
      }
   }

   protected boolean isInsideOtherTractorBeam(LivingEntity entity, int head) {
      List<WitherStormEntity> storms = Lists.newArrayList(new WitherStormEntity[]{this});
      this.getSegmentsManager().ifPresent(manager -> {
         for (WitherStormSegmentEntity segment : manager.getSegments()) {
            if (segment != null && segment.isAlive()) {
               storms.add(segment);
            }
         }
      });

      for (WitherStormEntity storm : storms) {
         Pair<Boolean, Integer> flag = TractorBeamHelper.isInsideTractorBeam(entity, storm, 5.0);
         if ((Boolean)flag.getFirst() && (Integer)flag.getSecond() != head) {
            return true;
         }
      }

      return false;
   }

   public boolean shouldRenderAtSqrDistance(double sqrDistance) {
      double d0 = this.getBoundingBox().getSize();
      if (Double.isNaN(d0)) {
         d0 = 1.0;
      }

      d0 = d0 * 248.0 * Entity.getViewScale();
      return sqrDistance < d0 * d0;
   }

   public boolean hasRecentlyBeenRevived() {
      return this.getPlayDeadManager().hasRecentlyBeenRevived();
   }

   @Nullable
   @Override
   public Vec3 getDistractedPos(int head) {
      return this.headManager.getHead(head).getDistractedPos();
   }

   @Override
   public void setDistractedPos(int head, @Nullable Vec3 pos) {
      this.headManager.getHead(head).setDistractedPos(pos);
   }

   @Override
   public void makeDistracted(Vec3 pos, int time, int head) {
      this.headManager.getHead(head).makeDistracted(pos, time);
   }

   @Override
   public boolean isPosBehindBack(Vec3 pos) {
      float angle = (float)(Mth.atan2(pos.x() - this.getX(), pos.z() - this.getZ()) * (180.0 / Math.PI));
      float angleDiff = (Mth.wrapDegrees(-this.yBodyRot) - angle + 180.0F + 360.0F) % 360.0F - 180.0F;
      return !(angleDiff <= 80.0F) || !(angleDiff >= -80.0F);
   }

   @Override
   public boolean canBeDistracted(int head, WitherStormBase.DistractionType type) {
      return WitherStormBase.super.canBeDistracted(head, type) && this.getPhase() > 3;
   }

   public void sendToBowels(Entity entity) {
      if (entity instanceof ServerPlayer player) {
         WitherStormBowelsManager.queueEnter(player, this);
      } else {
         WitherStormBowelsManager.enter((ServerLevel)entity.level(), this, entity);
      }
   }

   @Override
   public float getXBodyRot() {
      return this.xBodyRot;
   }

   @Override
   public float getXBodyRotO() {
      return this.xBodyRotO;
   }

   public void doFlicker() {
      this.flickerTime = 60;
      this.level().broadcastEntityEvent(this, (byte)11);
   }

   public void handleEntityEvent(byte event) {
      if (event == 11) {
         this.doFlicker();
      } else {
         super.handleEntityEvent(event);
      }
   }

   public boolean shouldFlicker() {
      return this.shouldFlicker;
   }

   @Nullable
   public CommandBlockEntity getBowelsCommandBlock() {
      BowelsInstanceManager manager = this.bowelsInstance.orElse(null);
      return manager != null ? manager.getCommandBlock() : null;
   }

   @Nullable
   public WitherStormBowelsManager.BowelsInstance getBowelsInstance() {
      BowelsInstanceManager manager = this.bowelsInstance.orElse(null);
      return manager != null ? manager.getBowelsInstance() : null;
   }

   public void dropDropsAt(Entity player) {
      ItemStack stack = new ItemStack((ItemLike)WitherStormModItems.WITHERED_NETHER_STAR.get());
      ItemEntity item = new ItemEntity(this.level(), player.getX(), player.getEyeY() + 2.0, player.getZ(), stack);
      item.setDeltaMovement(0.0, -0.08, 0.0);
      item.setNoGravity(true);
      this.level().addFreshEntity(item);
      ServerLevel level = (ServerLevel)this.level();
      ExperienceOrb.award(level, player.position().add(0.0, 10.0, 0.0), ForgeEventFactory.getExperienceDrop(this, this.lastHurtByPlayer, this.getExperienceReward()));

      for (Player nearby : this.level().getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(15.0))) {
         BlockPos nearestVillage = level.findNearestMapStructure(StructureTags.VILLAGE, player.blockPosition(), 50, false);
         if (nearestVillage != null && Math.sqrt(nearestVillage.distSqr(player.blockPosition())) < 200.0) {
            nearby.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 48000, 4, false, false, true));
         }
      }
   }

   public boolean shouldPlaySoundLoops() {
      return this.shouldPlaySoundLoop && !this.isSilent() && !this.isDeadOrPlayingDead();
   }

   public static SoundEvent getSoundForLoop(int phase, float fade) {
      SoundEvent event = WitherStormModSoundEvents.WITHER_STORM_LOOP.get();
      if (phase > 3) {
         event = WitherStormLoopingSoundManager.getSoundBasedOnDistance(fade);
      } else if (phase < 3) {
         event = WitherStormModSoundEvents.COMMAND_BLOCK_PULSE_LOOP.get();
      }

      return event;
   }

   public boolean shouldTrackUltimateTarget() {
      CommandBlockEntity entity = this.getBowelsCommandBlock();
      return entity != null && entity.getHealth() < entity.getMaxHealth() ? false : this.shouldFollowUltimateTarget;
   }

   public boolean shouldRotateTowardsUltimateTarget() {
      CommandBlockEntity entity = this.getBowelsCommandBlock();
      return entity == null || !(entity.getHealth() / entity.getMaxHealth() <= 0.25F);
   }

   @Override
   public boolean isHeadInjured(int head) {
      return this.headManager.getHead(head).isHeadInjured();
   }

   public GoalSelector getGoalSelectorForHead(int head) {
      return head > 0 ? this.headGoalSelectors.get(head - 1) : this.goalSelector;
   }

   public GoalSelector getTargetSelectorForHead(int head) {
      return head > 0 ? this.headTargetSelectors.get(head - 1) : this.targetSelector;
   }

   @Override
   public float getHeadShakeAnim(int head, float partialTicks) {
      return this.headManager.getHead(head).getRollAngle(partialTicks);
   }

   @Override
   public boolean tractorBeamActive(int head) {
      boolean flag = false;
      if (this.getPhase() < 4) {
         flag = head == 0 && this.getPhase() > 1;
      } else if (this.getPhase() > 1) {
         flag = true;
      }

      return WitherStormBase.super.tractorBeamActive(head) && flag && !this.isDeadOrPlayingDead();
   }

   public void pullInTarget(Entity target, double speed, WitherStormHead head) {
      if (target != null && !(target instanceof WitherStormEntity) && this.tractorBeamActive(head.getIndex())) {
         Entity vehicle = target.getVehicle();
         Vec3 targetPullPos = null;
         Vec3 headPos = head.getHeadPos();
         if (!(target instanceof Player) && !(target.position().distanceTo(headPos) < 25.0)) {
            targetPullPos = TractorBeamHelper.calculateClosestPoint(target.position(), this, head.getIndex(), -5.0);
         } else {
            targetPullPos = headPos;
         }

         Vec3 delta;
         boolean var10000;
         label66: {
            speed *= Mth.clamp(target.position().distanceTo(targetPullPos), 0.1, 1.0);
            delta = targetPullPos.subtract(target.position()).normalize().scale(speed);
            if (vehicle instanceof LivingEntity living && !this.entitySelector.test(living)) {
               var10000 = false;
               break label66;
            }

            var10000 = true;
         }

         boolean flag = var10000;
         if (target.isPassenger() && (Boolean)WitherStormModConfig.COMMON.shouldPickUpVehicles.get() && flag) {
            vehicle.setDeltaMovement(delta);
         } else {
            target.setDeltaMovement(delta);
         }

         AABB headBB = new AABB(
            headPos.x - 2.0, headPos.y - 4.0, headPos.z - 2.0, headPos.x + 2.0, headPos.y + 2.0, headPos.z + 2.0
         );
         if (target instanceof ServerPlayer player) {
            this.setPlayerDeltaMovement(player, delta);
         }

         if (headBB.intersects(target.getBoundingBox()) && target instanceof LivingEntity living && !living.isDeadOrDying()) {
            if (target instanceof Player player) {
               if (!player.isDeadOrDying()) {
                  float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                  if ((Boolean)WitherStormModConfig.SERVER.instantChomp.get()) {
                     damage = Float.MAX_VALUE;
                  }

                  player.hurt(WitherStormModDamageTypes.witherStormAttack(this), damage);
                  if (player.isDeadOrDying()) {
                     this.consumeEntity(player, 1);
                  }
               }

               head.startBiting();
            } else {
               this.consumeEntity(living, 1);
               if ((Boolean)WitherStormModConfig.SERVER.healFromChomp.get()) {
                  this.heal(living.getMaxHealth() / 2.0F);
               }

               if (living instanceof TamableAnimal tamable && tamable.getOwnerUUID() != null) {
                  this.storePet(tamable);
               }

               target.hurt(WitherStormModDamageTypes.witherStormAttackMob(this), Float.MAX_VALUE);
               head.startBiting();
               if (head instanceof AdditionalHead additionalHead) {
                  additionalHead.nextHeadUpdate = this.tickCount + this.random.nextInt(20) + this.random.nextInt(60);
               }
            }
         }
      }
   }

   protected void dropExperience() {
   }

   public void dropMassCluster(int radius) {
      BlockClusterEntity cluster = ClusterBuilderHelper.buildRandomDeathCluster(this.level(), this.random, radius);
      cluster.setSink(radius / 2 + 1);
      cluster.setPos(this.position().add(0.0, (double)this.getUnmodifiedHeight() / 2.0, 0.0));
      cluster.setDeltaMovement(this.random.nextGaussian() * 0.3, 0.0, this.random.nextGaussian() * 0.3);
      cluster.setRotationDelta(new Vec2((float)this.random.nextInt(20) * 0.3F / 2.0F, (float)this.random.nextInt(20) * 0.3F / 2.0F));
      cluster.setAntiStacking(true);
      this.level().addFreshEntity(cluster);
   }

   public void dropSmallMassCluster(int radius) {
      BlockClusterEntity cluster = ClusterBuilderHelper.buildSmallRandomDeathCluster(this.level(), this.random, radius);
      cluster.setSink(-1);
      cluster.setDeltaMovement(this.random.nextGaussian() * 0.6, this.random.nextGaussian() * 0.3, this.random.nextGaussian() * 0.6);
      cluster.setPos(
         this.position()
            .add(
               this.random.nextGaussian() * 20.0, (double)this.getUnmodifiedHeight() / 2.0 + this.random.nextGaussian() * 40.0, this.random.nextGaussian() * 20.0
            )
      );
      cluster.setRotationDelta(new Vec2((float)this.random.nextInt(90) * 0.3F / 2.0F, (float)this.random.nextInt(90) * 0.3F / 2.0F));
      this.level().addFreshEntity(cluster);
   }

   public boolean isCompletelyInvulnerable() {
      return (Boolean)WitherStormModConfig.SERVER.witherStormInvulnerability.get();
   }

   public boolean shouldShine() {
      return this.getPhase() > 3;
   }

   public float getShineAlpha(float partialTicks) {
      return Mth.lerp(partialTicks, this.shineAlphaO, this.shineAlpha);
   }

   public void setShineAlpha(float alpha) {
      this.shineAlpha = alpha;
   }

   public void setResummoned(boolean flag) {
      this.resummoned = flag;
   }

   public boolean canFreeze() {
      return false;
   }

   public float getClusterRadius() {
      return this.clusterRadius;
   }

   public HeadManager getHeadManager() {
      return this.headManager;
   }

   public RemovableGoalsManager getRemovableGoalsManager() {
      return this.removableGoals;
   }

   public int getHeadRotSpeed() {
      if (this.getPhase() > 3 && !this.isHeadInjured(0) && !this.isDistracted(0)) {
         return 1;
      } else {
         return this.isDistracted(0) ? 2 : super.getHeadRotSpeed();
      }
   }

   public void kill() {
      this.remove(RemovalReason.KILLED);
      this.gameEvent(GameEvent.ENTITY_DIE);
      this.getSegmentsManager().ifPresent(manager -> {
         for (WitherStormSegmentEntity entity : manager.getSegments()) {
            if (entity != null) {
               entity.kill();
            }
         }
      });
   }

   @Override
   public double getTractorBeamCutoffDistance(int head) {
      return this.headManager.getHead(head).getTractorBeamCutoff();
   }

   public EvolutionProfiler getEvolutionProfiler() {
      return this.evolutionProfiler;
   }

   public void spawnConsumedPets(Vec3 pos) {
      for (Entry<UUID, CompoundTag> entry : this.consumedPets.entrySet()) {
         CompoundTag tag = entry.getValue();
         if (!tag.isEmpty()) {
            EntityType.create(tag, this.level()).ifPresent(entity -> {
               entity.setPos(pos);
               if (entity instanceof LivingEntity living) {
                  living.setHealth(living.getMaxHealth());
                  living.removeAllEffects();
                  living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
               }

               ((ServerLevel)this.level()).addWithUUID(entity);
            });
         }
      }

      this.consumedPets.clear();
   }

   public IgnoredTargetsManager getIgnoredTargets() {
      return this.ignoredTargets;
   }

   public List<BlockPos> getPlayingJukeboxes() {
      return this.playingJukeboxes;
   }

   public float getShineScale() {
      return this.shineScale;
   }

   @Override
   public void setLookAt(int head, Vec3 pos, int time) {
      this.headManager.getHead(head).setLookPos(pos, time);
   }

   @Override
   public LivingEntity getTarget(int head) {
      return this.headManager.getHead(head).getTarget();
   }

   @Override
   public void setTarget(int head, LivingEntity entity) {
      this.headManager.getHead(head).setTarget(entity);
   }

   protected void splitCluster(BlockClusterEntity cluster, List<Entity> toAdd) {
      BlockClusterEntity split = cluster.splitAt(Axis.getRandom(this.random));
      if (split != null) {
         this.level().addFreshEntity(split);
         this.segments.ifPresentOrElse(manager -> {
            if (this.random.nextBoolean()) {
               toAdd.add(split);
            } else {
               int size = manager.getSegments().length;
               int index = this.random.nextInt(size);
               WitherStormSegmentEntity segment = manager.getSegments()[index];
               if (segment != null && !segment.isRemoved()) {
                  segment.getTrackedEntities().trackEntityToConsume(split);
               } else {
                  toAdd.add(split);
               }
            }
         }, () -> toAdd.add(split));
      }
   }

   @Override
   public int loadRadius() {
      return (Integer)WitherStormModConfig.SERVER.chunkLoadingRadius.get();
   }

   @Override
   public boolean isStillValidForChunkLoading() {
      return this.getRemovalReason() == null || !this.getRemovalReason().shouldDestroy();
   }

   public float getPhaseProgress() {
      return this.phaseProgress;
   }

   public int entityConsumptionRadiusHunch() {
      int radiusValue = 12;
      radiusValue = (int)((long)radiusValue + Math.round((double)this.getConsumedEntities() * 0.00445));
      if (radiusValue > 48) {
         radiusValue = 48;
      }

      return radiusValue;
   }

   public int getEntityConsumptionRadius() {
      return this.entityConsumptionRadius;
   }

   public void makeConsumptionLocked(boolean flag) {
      this.isLocked = flag;
   }

   public boolean isConsumptionLocked() {
      return this.isLocked;
   }

   public Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 vec, float f) {
      this.moveRelative(this.getSpeed(), vec);
      this.move(MoverType.SELF, this.getDeltaMovement());
      return this.getDeltaMovement();
   }

   private Vec3 wsCollide(Vec3 delta) {
      AABB aabb = this.getBoundingBox();
      List<VoxelShape> list = this.level().getEntityCollisions(this, aabb.expandTowards(delta));
      return delta.lengthSqr() == 0.0 ? delta : collideBoundingBox(this, delta, aabb, this.level(), list);
   }

   public float getStepHeight() {
      return 0.0F;
   }

   public void move(MoverType moverType, Vec3 delta) {
      if (this.noPhysics) {
         this.setPos(this.getX() + delta.x, this.getY() + delta.y, this.getZ() + delta.z);
      } else {
         this.level().getProfiler().push("move");
         if (this.stuckSpeedMultiplier.lengthSqr() > 1.0E-7) {
            delta = delta.multiply(this.stuckSpeedMultiplier);
            this.stuckSpeedMultiplier = Vec3.ZERO;
            this.setDeltaMovement(Vec3.ZERO);
         }

         Vec3 vec3 = this.wsCollide(delta);
         double d0 = vec3.lengthSqr();
         if (d0 > 1.0E-7) {
            if (this.fallDistance != 0.0F && d0 >= 1.0) {
               BlockHitResult blockhitresult = this.level()
                  .clip(new ClipContext(this.position(), this.position().add(vec3), Block.FALLDAMAGE_RESETTING, Fluid.WATER, this));
               if (blockhitresult.getType() != Type.MISS) {
                  this.resetFallDistance();
               }
            }

            this.setPos(this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z);
         }

         this.level().getProfiler().pop();
         this.level().getProfiler().push("rest");
         boolean noXMovement = !Mth.equal(delta.x, vec3.x);
         boolean noZMovement = !Mth.equal(delta.z, vec3.z);
         this.horizontalCollision = noXMovement || noZMovement;
         this.verticalCollision = delta.y != vec3.y;
         this.verticalCollisionBelow = this.verticalCollision && delta.y < 0.0;
         this.setOnGroundWithKnownMovement(this.verticalCollisionBelow, vec3);
         BlockPos pos = this.getOnPosLegacy();
         BlockState state = this.level().getBlockState(pos);
         if (!this.isRemoved()) {
            if (this.horizontalCollision) {
               Vec3 vec31 = this.getDeltaMovement();
               this.setDeltaMovement(noXMovement ? 0.0 : vec31.x, vec31.y, noZMovement ? 0.0 : vec31.z);
            }

            net.minecraft.world.level.block.Block block = state.getBlock();
            if (delta.y != vec3.y) {
               block.updateEntityAfterFallOn(this.level(), this);
            }

            if (this.onGround()) {
               block.stepOn(this.level(), pos, state, this);
            }
         }

         this.level().getProfiler().pop();
      }
   }

   protected void checkInsideBlocks() {
   }

   protected void tryCheckInsideBlocks() {
   }

   protected MovementEmission getMovementEmission() {
      return MovementEmission.NONE;
   }

   public void lerpBodyRotationTo(float xBodyRot, float yBodyRot, int steps) {
      this.lerpBodyXRot = xBodyRot;
      this.lerpBodyYRot = yBodyRot;
      this.bodyLerpSteps = steps;
   }

   private double attributeOrConfigValue(Attribute attribute, ConfigValue<Double> config) {
      AttributeInstance instance = this.getAttribute(attribute);
      return instance.getValue() != attribute.getDefaultValue() ? instance.getValue() : (Double)config.get();
   }

   @Override
   public boolean canSee(int head, Entity entity) {
      return this.headManager.getHead(head).canSee(entity);
   }

   public void storePet(Entity entity) {
      this.consumedPets.computeIfAbsent(entity.getUUID(), u -> {
         CompoundTag tag = new CompoundTag();
         tag.putString("id", Objects.requireNonNull(entity.getEncodeId()));
         entity.saveWithoutId(tag);
         return tag;
      });
   }

   @Override
   public boolean equals(Object obj) {
      if (super.equals(obj) && obj instanceof WitherStormEntity storm && storm.isOnDistantRenderer() == this.isOnDistantRenderer()) {
         return true;
      }

      return false;
   }

   public static boolean isOccludedSound(SoundEvent event) {
      if (event == WitherStormModSoundEvents.WITHER_STORM_GROWL.get()) {
         return true;
      } else if (event == WitherStormModSoundEvents.WITHER_STORM_HURT.get()) {
         return true;
      } else if (event == WitherStormModSoundEvents.WITHER_STORM_SHOOT.get()) {
         return true;
      } else if (event == WitherStormModSoundEvents.WITHER_STORM_BITE.get()) {
         return true;
      } else {
         return event == WitherStormModSoundEvents.WITHER_STORM_ROAR.get()
            ? true
            : event == WitherStormModSoundEvents.WITHER_STORM_TRACTOR_BEAM_ACTIVATES.get();
      }
   }

   public static <T> EntityDataAccessor<T> registerDataAccessor(EntityDataSerializer<T> serializer, Supplier<T> defaultValue) {
      try {
         Class<?> oclass = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
         if (oclass.getPackageName().startsWith("nonamecrackers2.witherstormmod.common.entity.ai.witherstorm")) {
            EntityDataAccessor<T> accessor = SynchedEntityData.defineId(WitherStormEntity.class, serializer);
            DATA_ACCESSORS.add(new WitherStormEntity.DataAccessorHolder<>(accessor, defaultValue));
            return accessor;
         } else {
            throw new RuntimeException("This method is for internal use only!");
         }
      } catch (ClassNotFoundException var4) {
         return null;
      }
   }

   private static record DataAccessorHolder<T>(EntityDataAccessor<T> accessor, Supplier<T> defaultValue) {
      private void defineTo(SynchedEntityData data) {
         data.define(this.accessor, this.defaultValue.get());
      }
   }
}
