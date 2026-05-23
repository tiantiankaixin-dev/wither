/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerBossEvent
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.random.SimpleWeightedRandomList
 *  net.minecraft.world.BossEvent$BossBarColor
 *  net.minecraft.world.BossEvent$BossBarOverlay
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.SpawnPlacements$Type
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 *  net.minecraft.world.entity.animal.AbstractGolem
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.NaturalSpawner
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.PushReaction
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.neoforge.common.util.LogicalSidedProvider
 *  net.neoforged.neoforge.event.EventHooks
 *  // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT
 *  // TODO_MIG: NetworkEvent removed, use IPayloadContext$Context
 *  net.neoforged.neoforge.network.PacketDistributor
 *  nonamecrackers2.crackerslib.common.packet.Packet
 *  nonamecrackers2.witherstormmod.WitherStormMod
 *  nonamecrackers2.witherstormmod.common.config.WitherStormModConfig
 *  nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity
 *  nonamecrackers2.witherstormmod.common.entity.BossThemeEntity
 *  nonamecrackers2.witherstormmod.common.entity.SickenedIronGolem
 *  nonamecrackers2.witherstormmod.common.entity.SickenedVindicator
 *  nonamecrackers2.witherstormmod.common.entity.TentacleEntity
 *  nonamecrackers2.witherstormmod.common.entity.WitherStormEntity
 *  nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity
 *  nonamecrackers2.witherstormmod.common.entity.ai.commandblock.BowelsBossFightStages
 *  nonamecrackers2.witherstormmod.common.entity.bossfight.BossfightManager
 *  nonamecrackers2.witherstormmod.common.entity.bossfight.BossfightPhase
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModToolActions
 *  nonamecrackers2.witherstormmod.common.packet.PlayerMotionMessage
 *  nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage
 *  nonamecrackers2.witherstormmod.common.serializer.WitherStormModDataSerializers
 *  nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags
 *  nonamecrackers2.witherstormmod.common.tags.WitherStormModItemTags
 *  nonamecrackers2.witherstormmod.common.util.EntitySyncableData
 *  nonamecrackers2.witherstormmod.common.util.StructureAnimationHelper
 *  nonamecrackers2.witherstormmod.common.util.TentacleOffsets
 *  nonamecrackers2.witherstormmod.common.util.WorldUtil
 *  org.jetbrains.annotations.NotNull
 */
package nonamecrackers2.witherstormmod.common.entity;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.util.LogicalSidedProvider;
import net.neoforged.neoforge.event.EventHooks;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: NetworkEvent removed, use IPayloadContext
import net.neoforged.neoforge.network.PacketDistributor;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.BossThemeEntity;
import nonamecrackers2.witherstormmod.common.entity.SickenedIronGolem;
import nonamecrackers2.witherstormmod.common.entity.SickenedVindicator;
import nonamecrackers2.witherstormmod.common.entity.TentacleEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.commandblock.BowelsBossFightStages;
import nonamecrackers2.witherstormmod.common.entity.bossfight.BossfightManager;
import nonamecrackers2.witherstormmod.common.entity.bossfight.BossfightPhase;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModToolActions;
import nonamecrackers2.witherstormmod.common.packet.PlayerMotionMessage;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;
import nonamecrackers2.witherstormmod.common.serializer.WitherStormModDataSerializers;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModItemTags;
import nonamecrackers2.witherstormmod.common.util.EntitySyncableData;
import nonamecrackers2.witherstormmod.common.util.StructureAnimationHelper;
import nonamecrackers2.witherstormmod.common.util.TentacleOffsets;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import org.jetbrains.annotations.NotNull;

public class CommandBlockEntity
extends LivingEntity
implements EntitySyncableData,
BossThemeEntity {
    private static final EntityDataAccessor<State> STATE = SynchedEntityData.defineId(CommandBlockEntity.class, (EntityDataSerializer)WitherStormModDataSerializers.STATE_ENUM);
    private static final EntityDataAccessor<Mode> MODE = SynchedEntityData.defineId(CommandBlockEntity.class, (EntityDataSerializer)WitherStormModDataSerializers.MODE_ENUM);
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(CommandBlockEntity.class, (EntityDataSerializer)EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> PHASE_KEY = SynchedEntityData.defineId(CommandBlockEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final Predicate<LivingEntity> SEARCHABLE_PLAYER_SELECTOR = living -> {
        Player player;
        return living instanceof Player && (player = (Player)living).isAlive() && !player.getAbilities().invulnerable && !player.isCreative() && !player.isSpectator() && player.isAttackable();
    };
    private static final SimpleWeightedRandomList<EntityType<? extends Mob>> IDLE_BOWELS_MOBS = SimpleWeightedRandomList.<EntityType<? extends Mob>>builder().add((WitherStormModEntityTypes.SICKENED_ZOMBIE.get()), 10).add((WitherStormModEntityTypes.SICKENED_SKELETON.get()), 10).add((WitherStormModEntityTypes.SICKENED_SPIDER.get()), 6).add((WitherStormModEntityTypes.SICKENED_CREEPER.get()), 1).add((WitherStormModEntityTypes.SICKENED_CHICKEN.get()), 4).add((WitherStormModEntityTypes.SICKENED_COW.get()), 4).add((WitherStormModEntityTypes.SICKENED_PIG.get()), 4).add((WitherStormModEntityTypes.SICKENED_PARROT.get()), 2).add((WitherStormModEntityTypes.SICKENED_WOLF.get()), 1).add((WitherStormModEntityTypes.SICKENED_CAT.get()), 1).add((WitherStormModEntityTypes.SICKENED_BEE.get()), 1).add((WitherStormModEntityTypes.SICKENED_PILLAGER.get()), 3).add((WitherStormModEntityTypes.SICKENED_VINDICATOR.get()), 1).add((WitherStormModEntityTypes.SICKENED_VILLAGER.get()), 3).build();
    public static final int HIT_GLARE_TIME = 60;
    private int modeAnim;
    private int modeAnimO;
    private int stateTicks;
    @Nullable
    private Player toLure;
    private final List<StructureAnimationHelper> ribStructure = new ArrayList<StructureAnimationHelper>();
    private final TentacleManager tentacleStructure;
    private float protectionYOffset;
    private float protectionYOffsetO;
    @Nullable
    private WitherStormEntity owner;
    private final BossfightManager<CommandBlockEntity> bossfightManager;
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
    private final List<ServerPlayer> outsideBossBarViewers = Lists.newArrayList();
    private final List<ServerPlayer> tracking = Lists.newArrayList();
    @Nullable
    public BlockClusterEntity podiumCluster;
    @Nullable
    public UUID podiumClusterUUID;
    private int specialDeathTime;
    @Nullable
    public LivingEntity killer;
    private int hitGlareTime;

    public CommandBlockEntity(EntityType<? extends CommandBlockEntity> type, Level world) {
        super(type, world);
        this.createStructureHelpers();
        this.tentacleStructure = new TentacleManager(this, 6, new TentacleOffsets[]{new TentacleOffsets(2.0, -2.0, 3.0, 1.45f, 1.0f, 40.0f, -70.0f), new TentacleOffsets(0.0, -2.0, 4.0, 1.4f, 1.0f, 35.0f, -90.0f), new TentacleOffsets(-2.0, -2.0, 3.0, 1.45f, 1.0f, 40.0f, -110.0f), new TentacleOffsets(2.0, -2.0, -3.0, 1.45f, 1.0f, 40.0f, 70.0f), new TentacleOffsets(0.0, -2.0, -4.0, 1.4f, 1.0f, 35.0f, 90.0f), new TentacleOffsets(-2.0, -2.0, -3.0, 1.45f, 1.0f, 40.0f, 110.0f)});
        this.bossfightManager = new BossfightManager(BowelsBossFightStages.IDLE, (Entity)this).addPhase(1, BowelsBossFightStages.HIT).addPhase(2, BowelsBossFightStages.MOVE_PODIUM).addPhase(3, BowelsBossFightStages.WAIT).addPhase(4, BowelsBossFightStages.MOB_WAVE_1).addPhase(5, BowelsBossFightStages.IDLE).addPhase(6, BowelsBossFightStages.HIT).addPhase(7, BowelsBossFightStages.MOVE_PODIUM).addPhase(8, BowelsBossFightStages.WAIT).addPhase(9, BowelsBossFightStages.MOB_WAVE_2).addPhase(10, BowelsBossFightStages.PROTECT_IDLE).addPhase(11, BowelsBossFightStages.IDLE).addPhase(12, BowelsBossFightStages.HIT).addPhase(13, BowelsBossFightStages.MOVE_PODIUM).addPhase(14, BowelsBossFightStages.MOB_WAVE_3).addPhase(15, BowelsBossFightStages.PROTECT_IDLE).addPhase(16, BowelsBossFightStages.IDLE).addPhase(17, BowelsBossFightStages.DEATH).addPhase(18, BowelsBossFightStages.IDLE);
    }

    public CommandBlockEntity(Level world, WitherStormEntity owner, double x, double y, double z) {
        this((EntityType<? extends CommandBlockEntity>)(WitherStormModEntityTypes.COMMAND_BLOCK.get()), world);
        this.setPos(x, y, z);
        this.setState(State.PLAYING_DEAD);
        this.setMode(Mode.RIBS);
        this.setOwner(owner);
        this.setOwnerUUID(owner.getUUID());
        this.setYRot(owner.getYRot());
        this.setYBodyRot(owner.yBodyRot);
        this.setYHeadRot(owner.getYHeadRot());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 64.0).add(Attributes.ARMOR, 32.0).add(Attributes.ARMOR_TOUGHNESS, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 1024.0).add(Attributes.FOLLOW_RANGE, 32.0);
    }

    private void createStructureHelpers() {
        this.ribStructure.add(new StructureAnimationHelper());
        this.ribStructure.add(new StructureAnimationHelper().setBaseRotationAngle(0.0f, 180.0f));
        this.ribStructure.add(new StructureAnimationHelper().setBaseRotationAngle(0.0f, 145.0f));
        this.ribStructure.add(new StructureAnimationHelper().setBaseRotationAngle(0.0f, 35.0f));
        this.ribStructure.add(new StructureAnimationHelper().setBaseRotationAngle(0.0f, -35.0f));
        this.ribStructure.add(new StructureAnimationHelper().setBaseRotationAngle(0.0f, 215.0f));
    }

    public void tick() {
        if (!this.level().isClientSide) {
            this.entityData.set(PHASE_KEY, this.getBossfightManager().getCurrentPhaseIndex());
            int interval = (int)(this.getHealth() / this.getMaxHealth() * 80.0f);
            int secondaryInterval = (int)(this.getHealth() / this.getMaxHealth() * 16.0f);
            if (!this.tracking.isEmpty() && this.getHealth() < this.getMaxHealth() && interval > 0 && this.tickCount % interval == 0) {
                this.dropClusterFromCeiling();
            }
            if (!this.tracking.isEmpty() && this.getHealth() < this.getMaxHealth() && secondaryInterval > 0 && this.tickCount % secondaryInterval == 0) {
                this.dropAdditionalClustersFromCeiling();
            }
            int mobSpawnerTimer = 100;
            if (WorldUtil.areaLoaded((Level)this.level(), (BlockPos)this.blockPosition(), (int)8) && this.getState().equals(State.BOSSFIGHT) && this.level().dimension().location().equals(WitherStormMod.bowelsLocation()) && this.tickCount % mobSpawnerTimer == 0 && this.random.nextDouble() <= 0.25) {
                this.summonRandomBowelsMobNearPlayer(8, 16, IDLE_BOWELS_MOBS);
            }
        }
        super.tick();
        this.findOwner();
        this.getState().tick(this);
        this.modeAnimO = this.modeAnim;
        this.protectionYOffsetO = this.protectionYOffset;
        this.getStructures().forEach(StructureAnimationHelper::tick);
        if (!this.level().isClientSide) {
            this.tentacleStructure.findTentacles((ServerLevel)this.level());
            this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
            for (ServerPlayer player : this.outsideBossBarViewers) {
                if (!this.isDeadOrDying() && this.getHealth() < this.getMaxHealth()) {
                    this.bossInfo.addPlayer(player);
                    continue;
                }
                this.bossInfo.removePlayer(player);
            }
            if (this.getOwner() == null || this.getOwner().getBowelsCommandBlock() != this) {
                for (ServerPlayer player : this.outsideBossBarViewers) {
                    this.bossInfo.removePlayer(player);
                }
                this.outsideBossBarViewers.clear();
            }
        }
        if (this.hitGlareTime > 0) {
            --this.hitGlareTime;
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATE, State.IDLE);
        this.entityData.define(MODE, Mode.NONE);
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(PHASE_KEY, 0);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Mode", this.getMode().ordinal());
        compound.putInt("State", this.getState().ordinal());
        compound.putInt("StateTicks", this.stateTicks);
        compound.putInt("ModeAnim", this.modeAnim);
        compound.putFloat("YOffset", this.protectionYOffset);
        compound.putFloat("YBodyRot", this.yBodyRot);
        compound.put("Structures", this.writeStructures());
        if (this.getOwnerUUID() != null) {
            compound.putUUID("OwnerUUID", this.getOwnerUUID());
        }
        this.tentacleStructure.addSaveData(compound);
        if (this.podiumCluster != null && this.podiumCluster.isAlive()) {
            compound.putUUID("PodiumCluster", this.podiumCluster.getUUID());
        }
        compound.put("BossfightManager", (Tag)this.bossfightManager.write());
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        int state;
        super.readAdditionalSaveData(compound);
        this.protectionYOffsetO = this.protectionYOffset;
        int mode = compound.getInt("Mode");
        if (mode >= 0 && mode < Mode.values().length) {
            this.setMode(Mode.values()[mode]);
        }
        if ((state = compound.getInt("State")) >= 0 && state < State.values().length) {
            this.setState(State.values()[state]);
        }
        this.stateTicks = compound.getInt("StateTicks");
        this.modeAnimO = this.modeAnim = compound.getInt("ModeAnim");
        this.protectionYOffset = compound.getFloat("YOffset");
        this.setYBodyRot(compound.getFloat("YBodyRot"));
        this.readStructures(compound.getList("Structures", 10));
        if (compound.contains("OwnerUUID")) {
            this.setOwnerUUID(compound.getUUID("OwnerUUID"));
        }
        this.tentacleStructure.readSaveData(compound);
        if (compound.contains("PodiumCluster")) {
            this.podiumClusterUUID = compound.getUUID("PodiumCluster");
        }
        if (compound.contains("BossfightManager")) {
            this.bossfightManager.read(compound.getCompound("BossfightManager"));
        }
    }

    public Mode getMode() {
        return (Mode)(this.entityData.get(MODE));
    }

    public void setMode(Mode mode) {
        this.entityData.set(MODE, mode);
    }

    public State getState() {
        return (State)(this.entityData.get(STATE));
    }

    public void setState(State state) {
        this.entityData.set(STATE, state);
        this.initState();
    }

    private void initState() {
        this.getState().init(this);
    }

    public void nextState() {
        if (this.getState().ordinal() + 1 < State.values().length) {
            this.setState(State.values()[this.getState().ordinal() + 1]);
        }
    }

    public void setLuringPlayer(@Nullable Player player) {
        this.toLure = player;
    }

    @Nullable
    public Player getLuringPlayer() {
        return this.toLure;
    }

    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> parameter) {
        super.onSyncedDataUpdated(parameter);
        if (parameter.equals(STATE)) {
            this.initState();
        }
        if (parameter.equals(MODE)) {
            this.getMode().init(this, this.getState());
        }
    }

    public BlockState getBlockState() {
        return (BlockState)Blocks.COMMAND_BLOCK.defaultBlockState().setValue((Property)BlockStateProperties.FACING, (Comparable)this.getDirection());
    }

    public int getExperienceReward() {
        return 10;
    }

    @NotNull
    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(20.0);
    }

    public void knockback(double strength, double x, double z) {
    }

    public boolean ignoreExplosion() {
        return true;
    }

    public boolean hurt(DamageSource source, float amount) {
        LivingEntity entity;
        ItemStack stack;
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(source, amount);
        }
        boolean flag = false;
        Entity entity2 = source.getDirectEntity();
        if (entity2 instanceof LivingEntity && ((stack = (entity = (LivingEntity)entity2).getMainHandItem()).is(WitherStormModItemTags.COMMAND_BLOCK_TOOLS) || stack.canPerformAction(WitherStormModToolActions.COMMAND_BLOCK_DAMAGE))) {
            this.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_HIT.get(), 4.0f, 1.0f);
            if (!this.isVulnerable(source)) {
                if (this.getState() != State.BOSSFIGHT) {
                    entity.knockback(1.0, this.getX() - entity.getX(), this.getZ() - entity.getZ());
                }
                flag = true;
            } else {
                boolean hurt = false;
                float health = this.getHealth() - this.getMaxHealth() / 4.0f;
                if (health <= 0.0f) {
                    hurt = super.hurt(source, Float.MAX_VALUE);
                } else {
                    this.setHealth(health);
                    hurt = true;
                }
                if (!this.level().isClientSide && hurt && !this.isDeadOrDying()) {
                    this.getBossfightManager().goToNextPhase();
                    WitherStormEntity owner = this.getOwner();
                    if (owner != null && owner.isAlive()) {
                        owner.getSegmentsManager().ifPresent(manager -> {
                            for (WitherStormSegmentEntity segment : manager.getSegments()) {
                                if (segment == null || !segment.isAlive()) continue;
                                segment.getTrackedEntities().clearAndMakeAllFall();
                                for (int i = 0; i < segment.getTotalHeads(); ++i) {
                                    if (!(this.random.nextFloat() > 0.6f)) continue;
                                    segment.getHeadManager().getHead(i).hurt(null, segment.getHeadManager().getHeadInjuryTime());
                                }
                            }
                        });
                        owner.getTrackedEntities().clearAndMakeAllFall();
                        for (int i = 0; i < owner.getTotalHeads(); ++i) {
                            if (!(this.random.nextFloat() > 0.6f)) continue;
                            owner.getHeadManager().getHead(i).hurt(null, owner.getHeadManager().getHeadInjuryTime());
                        }
                        this.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_DAMAGE.get(), 16.0f, 1.0f);
                        this.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_CRACKS.get(), 16.0f, 1.0f);
                    }
                }
                if (this.level().isClientSide) {
                    for (int i = 0; i < 100; ++i) {
                        double x = this.random.nextFloat() - 0.5f;
                        double y = this.random.nextFloat() - 0.5f;
                        double z = this.random.nextFloat() - 0.5f;
                        Vec3 pos = this.getBoundingBox().getCenter();
                        this.level().addAlwaysVisibleParticle((ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(), pos.x, pos.y, pos.z, x, y, z);
                    }
                    if (health > 0.0f) {
                        this.hitGlareTime = 60;
                    }
                }
                return hurt;
            }
        }
        if ((source.is(DamageTypeTags.IS_EXPLOSION) || source.is(DamageTypeTags.IS_EXPLOSION) || flag) && this.getState().equals(State.PLAYING_DEAD)) {
            this.setState(State.REACTIVATING);
            return false;
        }
        return false;
    }

    public boolean isVulnerable(DamageSource source) {
        BossfightPhase<CommandBlockEntity> currentPhase = this.getCurrentPhase();
        return this.getState() == State.BOSSFIGHT && !this.isInvulnerableTo(source) && currentPhase.equals(BowelsBossFightStages.IDLE) && !currentPhase.equals(BowelsBossFightStages.PROTECT_IDLE);
    }

    @NotNull
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @NotNull
    public ItemStack getItemBySlot(@NotNull EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    public void setItemSlot(@NotNull EquipmentSlot slot, @NotNull ItemStack stack) {
    }

    @NotNull
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public boolean isNoGravity() {
        return true;
    }

    public boolean isPushable() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public void push(@NotNull Entity entity) {
    }

    public void push(double x, double y, double z) {
    }

    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size) {
        return 0.5f;
    }

    public float getModeAnim(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.modeAnimO, (float)this.modeAnim);
    }

    public List<StructureAnimationHelper> getRibStructure() {
        return this.ribStructure;
    }

    public TentacleManager getTentacleStructure() {
        return this.tentacleStructure;
    }

    public List<StructureAnimationHelper> getStructures() {
        return new ArrayList<StructureAnimationHelper>(this.getRibStructure());
    }

    public TargetingConditions searchablePlayersPredicate() {
        return TargetingConditions.forNonCombat().selector(SEARCHABLE_PLAYER_SELECTOR).range(6.0);
    }

    public void lerpStructureBaseRotTo(List<StructureAnimationHelper> structure, float xRot, float yRot, int steps) {
        for (StructureAnimationHelper helper : structure) {
            helper.lerpBaseTo(this, xRot, yRot, steps);
        }
    }

    public void lerpStructureRotTo(List<StructureAnimationHelper> structure, float xRot, float yRot, int steps) {
        for (StructureAnimationHelper helper : structure) {
            helper.lerpTo(this, xRot, yRot, steps);
        }
    }

    public void setProtectionYOffsetAndO(float offset) {
        this.protectionYOffset = offset;
        this.protectionYOffsetO = offset;
    }

    public void setProtectionYOffset(float offset) {
        this.protectionYOffset = offset;
    }

    public float getProtectionYOffset() {
        return this.protectionYOffset;
    }

    public float lerpProtectionYOffset(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.protectionYOffsetO, (float)this.protectionYOffset);
    }

    public int getStateTicks() {
        return this.stateTicks;
    }

    public Tag writeStructures() {
        ListTag structures = new ListTag();
        for (StructureAnimationHelper helper : this.getRibStructure()) {
            CompoundTag ribStructure = new CompoundTag();
            helper.write(ribStructure);
            structures.add(ribStructure);
        }
        return structures;
    }

    public void readStructures(ListTag list) {
        for (int i = 0; i < list.size(); ++i) {
            CompoundTag ribStructure = list.getCompound(i);
            if (i >= this.getRibStructure().size()) continue;
            this.getRibStructure().get(i).read(ribStructure);
        }
    }

    public void writeData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.stateTicks);
        buffer.writeInt(this.modeAnim);
        buffer.writeFloat(this.protectionYOffset);
        for (StructureAnimationHelper helper : this.getStructures()) {
            helper.writeBuffer(buffer);
        }
    }

    public void readData(FriendlyByteBuf buffer) {
        this.stateTicks = buffer.readInt();
        this.modeAnimO = this.modeAnim = buffer.readInt();
        this.protectionYOffsetO = this.protectionYOffset = buffer.readFloat();
        for (StructureAnimationHelper helper : this.getStructures()) {
            helper.readBuffer(buffer);
        }
    }

    public void findOwner() {
        block2: {
            block3: {
                if (this.getOwnerUUID() == null || this.getOwner() != null) break block2;
                if (this.getState() == State.BOSSFIGHT) break block3;
                for (WitherStormEntity entity : this.level().getEntitiesOfClass(WitherStormEntity.class, this.getBoundingBox().inflate(100.0))) {
                    if (!entity.getUUID().equals(this.getOwnerUUID())) continue;
                    this.setOwner(entity);
                    entity.getPlayDeadManager().setCommandBlock(this);
                }
                break block2;
            }
            if (this.level().isClientSide) break block2;
            MinecraftServer server = this.level().getServer();
            for (Level level : server.getAllLevels()) {
                Entity entity = ((ServerLevel)level).getEntity(this.getOwnerUUID());
                if (!(entity instanceof WitherStormEntity)) continue;
                this.setOwner((WitherStormEntity)entity);
                break;
            }
        }
    }

    public void setOwner(@Nullable WitherStormEntity entity) {
        this.owner = entity;
        if (entity != null) {
            this.setOwnerUUID(entity.getUUID());
            this.getState().initWithOwner(entity, this);
        } else {
            this.setOwnerUUID(null);
        }
    }

    @Nullable
    public WitherStormEntity getOwner() {
        return this.owner;
    }

    @Nullable
    public UUID getOwnerUUID() {
        return ((Optional<UUID>)this.entityData.get(OWNER_UUID)).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @NotNull
    public net.minecraft.network.protocol.Packet<ClientGamePacketListener> getAddEntityPacket() {
        WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new ModeAnimationMessage(this.getId(), this.modeAnim));
        return super.getAddEntityPacket();
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!this.level().isClientSide) {
            this.tentacleStructure.createTentacles();
        }
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
    }

    public void die(@NotNull DamageSource source) {
        if (!this.level().isClientSide && this.getState() == State.BOSSFIGHT && this.getBossfightManager().getCurrentPhase() != BowelsBossFightStages.DEATH) {
            this.getBossfightManager().setCurrentFromNextInOrder(BowelsBossFightStages.DEATH);
        }
        this.killer = this.getKillCredit();
        super.die(source);
    }

    protected void tickDeath() {
        if (this.getCurrentPhase().equals(BowelsBossFightStages.DEATH)) {
            ++this.specialDeathTime;
            if (!this.level().isClientSide) {
                if (this.specialDeathTime > 160) {
                    this.getBossfightManager().goToNextPhase();
                } else {
                    this.dropClusterFromCeiling();
                }
                if (this.specialDeathTime > 240) {
                    this.tentacleStructure.killTentacles();
                    this.remove(Entity.RemovalReason.KILLED);
                }
            }
        } else {
            super.tickDeath();
            if (!this.level().isClientSide && this.deathTime >= 20) {
                this.tentacleStructure.removeTentacles();
            }
        }
    }

    private void dropClusterFromCeiling() {
        if (WorldUtil.areaLoaded((Level)this.level(), (BlockPos)this.blockPosition(), (int)2)) {
            float angle = (float)Math.PI * 2 * this.random.nextFloat();
            float dist = 8.0f + this.random.nextFloat() * 24.0f;
            int x = Mth.floor((float)(Mth.cos((float)angle) * dist)) + this.getBlockX();
            int z = Mth.floor((float)(Mth.sin((float)angle) * dist)) + this.getBlockZ();
            int y = WorldUtil.getCeilingStartingAt((Level)this.level(), (int)(this.getBlockY() + 10), (int)x, (int)z);
            BlockPos pos = new BlockPos(x, y, z);
            if (this.level().isEmptyBlock(pos.below())) {
                BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(this.level());
                assert (cluster != null);
                cluster.populateWithRadius(pos, 1.0f, blockstate -> !blockstate.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST));
                cluster.setRotationDelta(new Vec2(10.0f * (this.random.nextFloat() - 0.5f), 10.0f * (this.random.nextFloat() - 0.5f)));
                cluster.setAntiStacking(true);
                this.level().addFreshEntity((Entity)cluster);
            }
        }
    }

    private void dropAdditionalClustersFromCeiling() {
        if (WorldUtil.areaLoaded((Level)this.level(), (BlockPos)this.blockPosition(), (int)8)) {
            for (int i = 0; i < 128; ++i) {
                float angle = (float)Math.PI * 2 * this.random.nextFloat();
                float dist = 32.0f + this.random.nextFloat() * 80.0f;
                int x = Mth.floor((float)(Mth.cos((float)angle) * dist)) + this.getBlockX();
                int z = Mth.floor((float)(Mth.sin((float)angle) * dist)) + this.getBlockZ();
                int y = WorldUtil.getCeilingStartingAt((Level)this.level(), (int)(this.getBlockY() + (this.random.nextInt(49) - 24)), (int)x, (int)z);
                BlockPos pos = new BlockPos(x, y, z);
                if (!this.level().isEmptyBlock(pos.below())) continue;
                BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(this.level());
                assert (cluster != null);
                cluster.populateWithRadius(pos, 1.0f, blockstate -> !blockstate.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST));
                cluster.setRotationDelta(new Vec2(10.0f * (this.random.nextFloat() - 0.5f), 10.0f * (this.random.nextFloat() - 0.5f)));
                cluster.setAntiStacking(true);
                this.level().addFreshEntity((Entity)cluster);
                break;
            }
        }
    }

    public BossfightManager<CommandBlockEntity> getBossfightManager() {
        return this.bossfightManager;
    }

    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
        this.tracking.add(player);
    }

    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
        this.tracking.remove(player);
    }

    public SoundEvent getBossTheme() {
        return WitherStormModSoundEvents.WITHER_STORM_FINAL_BOSS_THEME.get();
    }

    public boolean shouldPlayBossTheme() {
        return BossThemeEntity.super.shouldPlayBossTheme() && this.getHealth() < this.getMaxHealth() && this.getState() == State.BOSSFIGHT;
    }

    public Vec3 getPosition() {
        return this.position();
    }

    public boolean isStillAlive() {
        return this.isAlive();
    }

    public int priority() {
        return 3;
    }

    public int getFadeTime() {
        return 40;
    }

    public boolean checkConfig() {
        return (Boolean)WitherStormModConfig.CLIENT.playWitherStormTheme.get();
    }

    public void createPodiumCluster() {
        if (this.podiumCluster == null && this.podiumClusterUUID == null) {
            BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(this.level());
            BlockPos bottomCorner = new BlockPos(-5, -13, -5).offset((Vec3i)this.blockPosition());
            assert (cluster != null);
            cluster.populate(bottomCorner, bottomCorner.offset(10, 19, 10), blockstate -> true);
            cluster.setResetGravityOnLoad(false);
            cluster.setPhysics(false);
            cluster.setNoGravity(true);
            cluster.setForceRender(true);
            this.level().addFreshEntity((Entity)cluster);
            this.podiumCluster = cluster;
        }
    }

    public void movePodiumCluster(Vec3 delta) {
        if (this.podiumCluster != null) {
            this.podiumCluster.setDeltaMovement(delta);
        }
    }

    public void findPodiumCluster() {
        ServerLevel serverWorld;
        Entity entity;
        if (!this.level().isClientSide && this.podiumClusterUUID != null && this.podiumCluster == null && (entity = (serverWorld = (ServerLevel)this.level()).getEntity(this.podiumClusterUUID)) instanceof BlockClusterEntity) {
            this.podiumCluster = (BlockClusterEntity)entity;
        }
    }

    @Nullable
    public BlockClusterEntity getPodiumCluster() {
        return this.podiumCluster;
    }

    @Nullable
    public Mob summonRandomBowelsMobNearPlayer(int minRadius, int maxRadius, SimpleWeightedRandomList<EntityType<? extends Mob>> types) {
        ServerLevel world = (ServerLevel)this.level();
        List<ServerPlayer> players = world.getEntitiesOfClass(ServerPlayer.class, this.getBoundingBox().inflate(128.0));
        for (ServerPlayer player : players) {
            Mob entity = (Mob)Objects.requireNonNull(types.getRandomValue(this.random).orElse(null)).create((Level)world);
            assert (entity != null);
            BlockPos playerPos = player.blockPosition();
            BlockPos randomPos = this.getRandomPosAroundPlayer(entity.getType(), playerPos, minRadius, maxRadius, 10);
            if (randomPos == null || !this.hasEnoughSpace((Entity)entity, randomPos)) continue;
            entity.setPos((double)randomPos.getX() + 0.5, (double)(randomPos.getY() + 1), (double)randomPos.getZ() + 0.5);
            EventHooks.onFinalizeSpawn((Mob)entity, (ServerLevelAccessor)world, (DifficultyInstance)world.getCurrentDifficultyAt(randomPos), (MobSpawnType)MobSpawnType.EVENT, null, null);
            if (entity instanceof Monster || entity instanceof AbstractGolem) {
                this.addSpeedAttribute(entity);
            }
            this.addHealthAttribute(entity);
            entity.playAmbientSound();
            entity.spawnAnim();
            world.sendParticles((ParticleOptions)(WitherStormModParticleTypes.COMMAND_BLOCK.get()), entity.getX(), entity.getEyeY(), entity.getZ(), 20, this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian(), 0.2);
            world.sendParticles((ParticleOptions)ParticleTypes.LARGE_SMOKE, entity.getX(), entity.getEyeY(), entity.getZ(), 20, this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian(), 0.01);
            world.addFreshEntityWithPassengers((Entity)entity);
            return entity;
        }
        return null;
    }

    @Nullable
    public BlockPos getRandomPosAroundPlayer(EntityType<?> type, BlockPos playerPos, int minDistance, int maxDistance, int attempts) {
        for (int a = 0; a < attempts; ++a) {
            int z;
            int y;
            int x = playerPos.getX() + this.random.nextInt(maxDistance * 2 + 1) - maxDistance;
            BlockPos pos = new BlockPos(x, y = playerPos.getY() + this.random.nextInt(maxDistance), z = playerPos.getZ() + this.random.nextInt(maxDistance * 2 + 1) - maxDistance);
            double distanceSquared = playerPos.distSqr((Vec3i)pos);
            if (!(distanceSquared >= (double)(minDistance * minDistance)) || !(distanceSquared <= (double)(maxDistance * maxDistance))) continue;
            for (int i = 0; i < 30 && this.level().getBlockState(pos.below()).is(Blocks.AIR); ++i) {
                pos = pos.below();
            }
            if (!NaturalSpawner.isSpawnPositionOk((SpawnPlacementTypes)SpawnPlacementTypes.ON_GROUND, (LevelReader)this.level(), (BlockPos)pos, type) || !(Math.sqrt(playerPos.distSqr((Vec3i)pos)) > 6.0)) continue;
            return pos;
        }
        return null;
    }

    @Nullable
    public Mob summonRandomMob(int diameter, SimpleWeightedRandomList<EntityType<? extends Mob>> types) {
        ServerLevel world = (ServerLevel)this.level();
        Mob entity = (Mob)(types.getRandomValue(this.random).orElse(null)).create((Level)world);
        BlockPos pos = this.getRandomNearbyPos(entity.getType(), diameter, 5);
        if (pos != null && this.hasEnoughSpace((Entity)entity, pos)) {
            entity.setPos((double)pos.getX() + 0.5, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5);
            EventHooks.onFinalizeSpawn((Mob)entity, (ServerLevelAccessor)world, (DifficultyInstance)world.getCurrentDifficultyAt(pos), (MobSpawnType)MobSpawnType.EVENT, null, null);
            if (entity instanceof Monster || entity instanceof AbstractGolem) {
                this.addSpeedAttribute(entity);
            }
            this.addHealthAttribute(entity);
            entity.playAmbientSound();
            entity.spawnAnim();
            entity.setPersistenceRequired();
            world.sendParticles((ParticleOptions)(WitherStormModParticleTypes.COMMAND_BLOCK.get()), entity.getX(), entity.getEyeY(), entity.getZ(), 20, this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian(), 0.2);
            world.sendParticles((ParticleOptions)ParticleTypes.LARGE_SMOKE, entity.getX(), entity.getEyeY(), entity.getZ(), 20, this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian(), 0.01);
            world.addFreshEntityWithPassengers((Entity)entity);
            return entity;
        }
        return null;
    }

    @Nullable
    public BlockPos getRandomNearbyPos(EntityType<?> type, int diameter, int attempts) {
        BlockPos pos = null;
        BlockPos start = this.blockPosition();
        for (int i = 0; i < attempts; ++i) {
            int x = start.getX() + this.random.nextInt(diameter) - diameter / 2;
            int z = start.getZ() + this.random.nextInt(diameter) - diameter / 2;
            int y = start.getY();
            BlockPos currentPos = new BlockPos(x, y, z);
            for (int j = 0; j < 30 && this.level().getBlockState(currentPos.below()).is(Blocks.AIR); ++j) {
                currentPos = currentPos.below();
            }
            if (!NaturalSpawner.isSpawnPositionOk((SpawnPlacementTypes)SpawnPlacementTypes.ON_GROUND, (LevelReader)this.level(), (BlockPos)currentPos, type) || !(Math.sqrt(this.blockPosition().distSqr((Vec3i)currentPos)) > 6.0)) continue;
            pos = currentPos;
            break;
        }
        return pos;
    }

    private void addHealthAttribute(Mob mob) {
        Objects.requireNonNull(mob.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier("194fec31-b36e-41fc-ad72-02a5cb891def", -((mob.getRandom().nextDouble() + 0.5) * 2.0), AttributeModifier.Operation.ADDITION));
    }

    private void addSpeedAttribute(Mob mob) {
        if (mob instanceof SickenedVindicator || mob instanceof SickenedIronGolem) {
            Objects.requireNonNull(mob.getAttribute(Attributes.MOVEMENT_SPEED)).addPermanentModifier(new AttributeModifier("5965c24d-8ac1-4f04-92ee-3d2724f976e8", -0.08, AttributeModifier.Operation.ADDITION));
        } else {
            Objects.requireNonNull(mob.getAttribute(Attributes.MOVEMENT_SPEED)).addPermanentModifier(new AttributeModifier("5965c24d-8ac1-4f04-92ee-3d2724f976e8", -0.06, AttributeModifier.Operation.ADDITION));
        }
    }

    private boolean hasEnoughSpace(Entity entity, BlockPos spawnPos) {
        for (BlockPos pos : BlockPos.betweenClosed((BlockPos)spawnPos, (BlockPos)spawnPos.offset((Vec3i)BlockPos.containing((double)entity.getBbWidth(), (double)entity.getBbHeight(), (double)entity.getBbWidth())))) {
            if (this.level().getBlockState(pos).getCollisionShape((BlockGetter)this.level(), pos).isEmpty()) continue;
            return false;
        }
        return true;
    }

    public BossfightPhase<CommandBlockEntity> getCurrentPhase() {
        if (this.level().isClientSide) {
            return this.getBossfightManager().getPhase(((Integer)this.entityData.get(PHASE_KEY)).intValue());
        }
        return this.getBossfightManager().getCurrentPhase();
    }

    public int getSpecialDeathTime() {
        return this.specialDeathTime;
    }

    protected float getSoundVolume() {
        return 4.0f;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return WitherStormModSoundEvents.COMMAND_BLOCK_DAMAGE.get();
    }

    protected SoundEvent getDeathSound() {
        return WitherStormModSoundEvents.COMMAND_BLOCK_DEATH.get();
    }

    public float getVoicePitch() {
        return 1.0f;
    }

    public boolean shouldDropExperience() {
        return false;
    }

    protected boolean updateInWaterStateAndDoFluidPushing() {
        return false;
    }

    public int getHitGlareTime() {
        return this.hitGlareTime;
    }

    public List<ServerPlayer> getOutsideBossBarViewers() {
        return this.outsideBossBarViewers;
    }

    public void removeOutsideBossBarViewer(ServerPlayer player) {
        this.outsideBossBarViewers.remove(player);
        this.bossInfo.removePlayer(player);
    }

    public boolean canBeAffected(@NotNull MobEffectInstance instance) {
        return false;
    }

    @NotNull
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    public static class TentacleManager {
        private static final UUID KNOCKBACK_MODIFIER = UUID.fromString("72aeccbe-cdfe-41c9-9d21-261f29f6da60");
        private final CommandBlockEntity entity;
        public final int tentacles;
        public TentacleEntity[] tentacleStructure;
        private final UUID[] savedTentacleStructure;
        private final TentacleOffsets[] offsets;

        public TentacleManager(CommandBlockEntity entity, int amount, TentacleOffsets[] offsets) {
            this.entity = entity;
            this.tentacles = amount;
            this.tentacleStructure = new TentacleEntity[this.tentacles];
            this.savedTentacleStructure = new UUID[this.tentacles];
            this.offsets = offsets;
        }

        public void createTentacles() {
            for (int i = 0; i < this.tentacleStructure.length; ++i) {
                this.createTentacle(i);
            }
        }

        private void createTentacle(int index) {
            if (this.tentacleStructure[index] == null || !this.tentacleStructure[index].isAlive()) {
                TentacleEntity tentacle = (TentacleEntity)(WitherStormModEntityTypes.TENTACLE.get()).create(this.entity.level());
                this.getOffsetsForTentacle(index).apply((LivingEntity)this.entity, tentacle);
                assert (tentacle != null);
                tentacle.setDormant(true);
                tentacle.lerpCurlTo(0.0f, 0.0f, 1);
                tentacle.setInvulnerable(true);
                tentacle.setNoGravity(true);
                tentacle.setAnimationOffset(this.entity.random.nextInt(35) * 10000);
                tentacle.setCanStrangle(false);
                Objects.requireNonNull(tentacle.getAttribute(Attributes.ATTACK_KNOCKBACK)).addPermanentModifier(new AttributeModifier(KNOCKBACK_MODIFIER, "Command block's tentacles knockback modifier", 5.0, AttributeModifier.Operation.ADDITION));
                this.tentacleStructure[index] = tentacle;
            }
        }

        public void removeTentacles() {
            for (TentacleEntity tentacle : this.tentacleStructure) {
                if (tentacle == null || !tentacle.isAlive()) continue;
                tentacle.discard();
            }
        }

        public void readdTentacles() {
            this.createTentacles();
            this.addTentacles();
        }

        public void addTentacles() {
            if (this.entity.isAddedToWorld() && this.entity.isAlive()) {
                for (int i = 0; i < this.tentacleStructure.length; ++i) {
                    this.addTentacle(i);
                }
            }
        }

        private void addTentacle(int index) {
            TentacleEntity tentacle = this.tentacleStructure[index];
            if (tentacle != null && !tentacle.isAddedToWorld() && tentacle.isAlive()) {
                this.getOffsetsForTentacle(index).apply((LivingEntity)this.entity, tentacle);
                this.entity.level().addFreshEntity((Entity)tentacle);
            }
        }

        public void updateTentacles() {
            for (int i = 0; i < this.tentacleStructure.length; ++i) {
                TentacleEntity tentacle = this.tentacleStructure[i];
                if (tentacle == null || !tentacle.isAlive()) continue;
                this.getOffsetsForTentacle(i).apply((LivingEntity)this.entity, tentacle);
            }
        }

        public void killTentacles() {
            for (TentacleEntity tentacle : this.tentacleStructure) {
                if (tentacle == null) continue;
                tentacle.kill();
            }
        }

        public void findTentacles(ServerLevel world) {
            for (int i = 0; i < this.savedTentacleStructure.length; ++i) {
                UUID uuid = this.savedTentacleStructure[i];
                TentacleEntity preexisting = this.tentacleStructure[i];
                if ((uuid == null || preexisting == null || preexisting.getUUID().equals(uuid)) && preexisting != null) continue;
                assert (uuid != null);
                Entity entity = world.getEntity(uuid);
                if (!(entity instanceof TentacleEntity)) continue;
                TentacleEntity found = (TentacleEntity)entity;
                if (preexisting != null) {
                    preexisting.discard();
                }
                this.tentacleStructure[i] = found;
                this.savedTentacleStructure[i] = null;
            }
        }

        public TentacleOffsets getOffsetsForTentacle(int index) {
            return this.offsets[index];
        }

        public void addSaveData(CompoundTag compound) {
            ListTag list = new ListTag();
            for (TentacleEntity tentacle : this.tentacleStructure) {
                CompoundTag tentacleCompound = new CompoundTag();
                if (tentacle != null) {
                    tentacleCompound.putUUID("UUID", tentacle.getUUID());
                }
                list.add(tentacleCompound);
            }
            compound.put("Tentacles", (Tag)list);
        }

        public void readSaveData(CompoundTag compound) {
            ListTag list = compound.getList("Tentacles", 10);
            for (int i = 0; i < this.tentacleStructure.length; ++i) {
                CompoundTag tentacleCompound = list.getCompound(i);
                if (!tentacleCompound.contains("UUID") || this.savedTentacleStructure[i] != null) continue;
                this.savedTentacleStructure[i] = tentacleCompound.getUUID("UUID");
            }
        }
    }

    /*
     * Uses 'sealed' constructs - enablewith --sealed true
     */
    public static enum State {
        IDLE{

            @Override
            public void tick(CommandBlockEntity entity) {
                super.tick(entity);
                if (this.modeTickDelay() < entity.getStateTicks()) {
                    entity.getMode().idleTick(entity);
                }
            }
        }
        ,
        PLAYING_DEAD{

            @Override
            public void tick(CommandBlockEntity entity) {
                super.tick(entity);
                if (this.modeTickDelay() < entity.getStateTicks()) {
                    entity.getMode().playingDeadTick(entity);
                }
                if (!entity.level().isClientSide) {
                    Level world = entity.level();
                    Player player = world.getNearestPlayer(entity.searchablePlayersPredicate(), (LivingEntity)entity);
                    if (player != null) {
                        entity.setLuringPlayer(player);
                        entity.nextState();
                    }
                    int time = (Integer)WitherStormModConfig.SERVER.revivalTimeMinutes.get();
                    if (((Boolean)WitherStormModConfig.SERVER.revivalTimer.get()).booleanValue() && time > 0 && entity.getStateTicks() > time * 1200) {
                        entity.setState(REACTIVATING);
                    }
                }
            }
        }
        ,
        LURING{

            @Override
            public void tick(CommandBlockEntity entity) {
                super.tick(entity);
                if (this.modeTickDelay() < entity.getStateTicks()) {
                    entity.getMode().idleTick(entity);
                }
                if (!entity.level().isClientSide) {
                    Player player = entity.getLuringPlayer();
                    if (player != null && entity.searchablePlayersPredicate().range(12.0).test((LivingEntity)entity, (LivingEntity)player) && entity.getStateTicks() < 240) {
                        double speed = 0.025;
                        Vec3 motion = entity.position().subtract(player.position()).normalize().multiply(speed, speed, speed);
                        player.setDeltaMovement(motion.x, player.getDeltaMovement().y, motion.z);
                        WitherStormModPacketHandlers.MAIN.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), new PlayerMotionMessage(new Vec3(motion.x, player.getDeltaMovement().y, motion.z)));
                        if ((double)player.distanceTo((Entity)entity) < 3.0) {
                            entity.setLuringPlayer(null);
                            entity.nextState();
                        }
                        for (int i = 0; i < 4; ++i) {
                            double x = player.getX() + entity.random.nextGaussian() * player.getBoundingBox().getXsize() * 0.4;
                            double y = player.getBoundingBox().getCenter().y() + entity.random.nextGaussian() * player.getBoundingBox().getYsize() * 0.4;
                            double z = player.getZ() + entity.random.nextGaussian() * player.getBoundingBox().getZsize() * 0.4;
                            Vec3 delta = entity.getEyePosition(1.0f).subtract(x, y, z).normalize().scale(0.1);
                            entity.level().addParticle((ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(), x, y, z, delta.x(), delta.y(), delta.z());
                        }
                    } else {
                        entity.nextState();
                    }
                }
            }

            @Override
            public void init(CommandBlockEntity entity) {
                super.init(entity);
                entity.level().playSound(null, entity.blockPosition(), WitherStormModSoundEvents.COMMAND_BLOCK_ACTIVATES.get(), SoundSource.HOSTILE, 5.0f, 1.0f);
                entity.level().playSound(null, entity.blockPosition(), WitherStormModSoundEvents.TREMBLE.get(), SoundSource.AMBIENT, 10.0f, 1.0f);
                if (!entity.level().isClientSide) {
                    ShakeScreenMessage message = new ShakeScreenMessage(40.0f, 5.0f);
                    WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
                }
                for (int i = 0; i < 10; ++i) {
                    entity.level().addParticle((ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(), entity.getX(), entity.getEyeY(), entity.getZ(), entity.random.nextGaussian() * 0.5, entity.random.nextGaussian() * 0.5, entity.random.nextGaussian() * 0.5);
                }
            }

            @Override
            public boolean shouldShowOwnerBossBar() {
                return true;
            }

            @Override
            public int modeTickDelay() {
                return 40;
            }
        }
        ,
        REACTIVATING{

            @Override
            public void tick(CommandBlockEntity entity) {
                WitherStormEntity owner;
                super.tick(entity);
                if (this.modeTickDelay() < entity.getStateTicks()) {
                    entity.getMode().protectTick(entity);
                }
                if (!entity.level().isClientSide && entity.getOwner() != null && entity.getStateTicks() > 60 && !(owner = entity.getOwner()).isReviving()) {
                    owner.reviveFromPlayingDead();
                }
            }

            @Override
            public void init(CommandBlockEntity entity) {
                super.init(entity);
                if (!entity.level().isClientSide) {
                    ShakeScreenMessage message = new ShakeScreenMessage(120.0f, 5.0f);
                    WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
                }
                entity.level().playSound(null, entity.blockPosition(), WitherStormModSoundEvents.TREMBLE.get(), SoundSource.AMBIENT, 10.0f, 1.0f);
            }

            @Override
            public boolean shouldShowOwnerBossBar() {
                return true;
            }

            @Override
            public int modeTickDelay() {
                return 20;
            }
        }
        ,
        BOSSFIGHT{

            @Override
            public void tick(CommandBlockEntity entity) {
                super.tick(entity);
                if (!entity.level().isClientSide) {
                    entity.getBossfightManager().tick();
                    if (!entity.getCurrentPhase().equals(BowelsBossFightStages.IDLE) || entity.getHealth() < entity.getMaxHealth()) {
                        entity.bossInfo.setVisible(true);
                    }
                }
                entity.getMode().idleTick(entity);
            }

            @Override
            public void init(CommandBlockEntity entity) {
                super.init(entity);
            }
        };


        public void tick(CommandBlockEntity entity) {
            entity.getMode().tick(entity, entity.getState());
            ++entity.stateTicks;
            if (this != PLAYING_DEAD) {
                for (int i = 0; i < 5; ++i) {
                    double x = entity.getX() + entity.random.nextGaussian();
                    double y = entity.getEyeY() + entity.random.nextGaussian();
                    double z = entity.getZ() + entity.random.nextGaussian();
                    float particleSpeed = (entity.getMaxHealth() - entity.getHealth()) / entity.getMaxHealth() + 1.0f;
                    Vec3 delta = entity.getEyePosition(1.0f).subtract(x, y, z).normalize().scale(0.1 * (double)particleSpeed);
                    entity.level().addParticle((ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(), x, y, z, delta.x(), delta.y(), delta.z());
                }
            }
        }

        public void init(CommandBlockEntity entity) {
            entity.stateTicks = 0;
            entity.getMode().init(entity, entity.getState());
            entity.setProtectionYOffset(0.0f);
            if (entity.getOwner() != null) {
                this.initWithOwner(entity.getOwner(), entity);
            }
            entity.bossInfo.setVisible(false);
        }

        public int modeTickDelay() {
            return 0;
        }

        public boolean shouldShowOwnerBossBar() {
            return false;
        }

        public void initWithOwner(WitherStormEntity owner, CommandBlockEntity entity) {
            if (owner.isAlive()) {
                if (this.shouldShowOwnerBossBar()) {
                    owner.getBossInfo().ifPresent(info -> info.setVisible(true));
                } else {
                    owner.getBossInfo().ifPresent(info -> info.setVisible(!owner.isPlayingDead()));
                }
            }
        }
    }

    /*
     * Uses 'sealed' constructs - enablewith --sealed true
     */
    public static enum Mode {
        NONE,
        RIBS{

            @Override
            public void playingDeadTick(CommandBlockEntity entity) {
                super.playingDeadTick(entity);
                Random random = new Random(entity.getUUID().getLeastSignificantBits());
                for (StructureAnimationHelper helper : this.getStructure(entity)) {
                    helper.lerpBaseTo(entity, -50.0f, 0.0f, Math.max(4, random.nextInt(11)));
                    helper.lerpTo(entity, Math.max(30.0f, (float)random.nextInt(131)), (float)random.nextInt(21) - 10.0f, Math.max(4, random.nextInt(11)));
                }
            }

            @Override
            public void idleTick(CommandBlockEntity entity) {
                super.idleTick(entity);
                entity.lerpStructureBaseRotTo(this.getStructure(entity), -50.0f, 0.0f, 10);
                entity.lerpStructureRotTo(this.getStructure(entity), 60.0f, 0.0f, 20);
            }

            @Override
            public void protectTick(CommandBlockEntity entity) {
                super.protectTick(entity);
                entity.lerpStructureBaseRotTo(this.getStructure(entity), 0.0f, 0.0f, 40);
                entity.lerpStructureRotTo(this.getStructure(entity), 70.0f, 0.0f, 20);
                if (entity.getProtectionYOffset() > -0.8f && entity.getStateTicks() > 20 + entity.getState().modeTickDelay()) {
                    entity.protectionYOffset -= 0.05f;
                }
            }

            @Override
            public void playMovementSound(CommandBlockEntity entity) {
                for (int i = 0; i < this.getStructure(entity).size(); ++i) {
                    double x = entity.random.nextGaussian() * 3.0 + entity.getX();
                    double y = entity.getEyeY();
                    double z = entity.random.nextGaussian() * 3.0 + entity.getZ();
                    entity.level().playSound(null, x, y, z, WitherStormModSoundEvents.RIB_BONE_CRACK.get(), SoundSource.AMBIENT, 0.2f, 0.8f);
                }
            }

            @Override
            public List<StructureAnimationHelper> getStructure(CommandBlockEntity entity) {
                return entity.getRibStructure();
            }
        }
        ,
        TENTACLES{

            @Override
            public void tick(CommandBlockEntity entity, State state) {
                super.tick(entity, state);
                if (!entity.level().isClientSide) {
                    entity.tentacleStructure.addTentacles();
                    entity.tentacleStructure.updateTentacles();
                }
            }
        };


        public void tick(CommandBlockEntity entity, State state) {
            ++entity.modeAnim;
            if (!entity.level().isClientSide && entity.tickCount % 120 == 0) {
                WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new ModeAnimationMessage(entity.getId(), entity.modeAnim));
            }
        }

        public void idleTick(CommandBlockEntity entity) {
        }

        public void playingDeadTick(CommandBlockEntity entity) {
        }

        public void protectTick(CommandBlockEntity entity) {
        }

        public void init(CommandBlockEntity entity, State state) {
            entity.modeAnim = 0;
            if (!entity.level().isClientSide && entity.isAddedToWorld()) {
                if (this == TENTACLES) {
                    entity.tentacleStructure.readdTentacles();
                } else {
                    entity.tentacleStructure.removeTentacles();
                }
            }
        }

        public List<StructureAnimationHelper> getStructure(CommandBlockEntity entity) {
            return Lists.newArrayList();
        }

        public void playMovementSound(CommandBlockEntity entity) {
        }
    }

    public static class ModeAnimationMessage
    extends Packet {
        private int id;
        private int anim;

        public ModeAnimationMessage(int entityId, int anim) {
            super(true);
            this.id = entityId;
            this.anim = anim;
        }

        public ModeAnimationMessage() {
            super(false);
        }

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeVarInt(this.id);
            buffer.writeInt(this.anim);
        }

        public void decode(FriendlyByteBuf buffer) {
            this.id = buffer.readVarInt();
            this.anim = buffer.readInt();
        }

        public Runnable getProcessor(NetworkEvent.Context context) {
            return () -> DistExecutor.unsafeRunWhenOn((Dist)Dist.CLIENT, () -> () -> {
                Optional<Level> optional = (Optional<Level>)LogicalSidedProvider.CLIENTWORLD.get(context.getDirection().getReceptionSide());
                optional.ifPresent(world -> {
                    Entity entity = world.getEntity(this.id);
                    if (entity instanceof CommandBlockEntity) {
                        CommandBlockEntity commandBlock = (CommandBlockEntity)entity;
                        commandBlock.modeAnim = this.anim;
                    }
                });
            });
        }
    }
}

