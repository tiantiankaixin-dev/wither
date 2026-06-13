/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Lists
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.BlockParticleOption
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.NbtUtils
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerBossEvent
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.util.Mth
 *  net.minecraft.util.random.SimpleWeightedRandomList
 *  net.minecraft.world.BossEvent$BossBarColor
 *  net.minecraft.world.BossEvent$BossBarOverlay
 *  net.minecraft.world.Difficulty
 *  net.minecraft.world.DifficultyInstance
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.EntitySelector
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.MobSpawnType
 *  net.minecraft.world.entity.MobType
 *  net.minecraft.world.entity.NeutralMob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.SpawnGroupData
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.FloatGoal
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.ai.goal.GoalSelector
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.MeleeAttackGoal
 *  net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
 *  net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
 *  net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
 *  net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 *  net.minecraft.world.entity.animal.AbstractGolem
 *  net.minecraft.world.entity.animal.Animal
 *  net.minecraft.world.entity.boss.enderdragon.EnderDragon
 *  net.minecraft.world.entity.boss.wither.WitherBoss
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.monster.EnderMan
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.monster.WitherSkeleton
 *  net.minecraft.world.entity.npc.AbstractVillager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.AbstractArrow
 *  net.minecraft.world.entity.projectile.AbstractHurtingProjectile
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.minecraft.world.entity.vehicle.AbstractMinecart
 *  net.minecraft.world.entity.vehicle.Boat
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.Level$ExplosionInteraction
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.storage.loot.LootParams
 *  net.minecraft.world.level.storage.loot.LootParams$Builder
 *  net.minecraft.world.level.storage.loot.LootTable
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParams
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.neoforge.common.util.LogicalSidedProvider
 *  // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT
 *  // TODO_MIG: NetworkEvent removed, use IPayloadContext$Context
 *  net.neoforged.neoforge.network.PacketDistributor
 *  net.neoforged.neoforge.registries.Registry
 *  nonamecrackers2.crackerslib.common.packet.Packet
 *  nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType
 *  nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell
 *  nonamecrackers2.witherstormmod.api.common.registry.WitherStormModRegistries
 *  nonamecrackers2.witherstormmod.common.config.WitherStormModConfig
 *  nonamecrackers2.witherstormmod.common.entity.BossThemeEntity
 *  nonamecrackers2.witherstormmod.common.entity.TentacleEntity
 *  nonamecrackers2.witherstormmod.common.entity.WitherSickened
 *  nonamecrackers2.witherstormmod.common.entity.WitherStormEntity
 *  nonamecrackers2.witherstormmod.common.entity.WitherStormHeadEntity
 *  nonamecrackers2.witherstormmod.common.entity.goal.symbiont.PrepareSpellGoal
 *  nonamecrackers2.witherstormmod.common.entity.goal.symbiont.SummonMobsGoal
 *  nonamecrackers2.witherstormmod.common.entity.goal.symbiont.UseSpellGoal
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModSymbiontSpellTypes
 *  nonamecrackers2.witherstormmod.common.serializer.WitherStormModDataSerializers
 *  nonamecrackers2.witherstormmod.common.util.ConditionalLookController
 *  nonamecrackers2.witherstormmod.common.util.WorldUtil
 *  nonamecrackers2.witherstormmod.common.world.tainting.WorldTainting
 *  org.jetbrains.annotations.NotNull
 */
package nonamecrackers2.witherstormmod.common.entity;
import net.minecraft.core.Registry;

import net.neoforged.fml.config.ModConfig.Type;

import net.neoforged.fml.loading.FMLEnvironment;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
// TODO_MIG[MOBTYPE]: MobType removed in 1.21; getMobType() is gone 鈥?use entity tags
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.util.LogicalSidedProvider;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: NetworkEvent removed, use IPayloadContext
import net.neoforged.neoforge.network.PacketDistributor;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.api.common.registry.WitherStormModRegistries;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.BossThemeEntity;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.TentacleEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormHeadEntity;
import nonamecrackers2.witherstormmod.common.entity.goal.symbiont.PrepareSpellGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.symbiont.SummonMobsGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.symbiont.UseSpellGoal;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import nonamecrackers2.crackerslib.common.packet.SimpleChannel;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSymbiontSpellTypes;
import nonamecrackers2.witherstormmod.common.serializer.WitherStormModDataSerializers;
import nonamecrackers2.witherstormmod.common.util.ConditionalLookController;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import nonamecrackers2.witherstormmod.common.world.tainting.WorldTainting;
import org.jetbrains.annotations.NotNull;

public class WitheredSymbiontEntity
extends Monster
implements BossThemeEntity {
    private static final EntityDataAccessor<BossfightStage> BOSSFIGHT_STAGE = SynchedEntityData.defineId(WitheredSymbiontEntity.class, (EntityDataSerializer)WitherStormModDataSerializers.BOSSFIGHT_STAGE_ENUM);
    private static final EntityDataAccessor<SpellType> SPELL_TYPE = SynchedEntityData.defineId(WitheredSymbiontEntity.class, (EntityDataSerializer)WitherStormModDataSerializers.SPELL_TYPE);
    private static final EntityDataAccessor<Boolean> NON_BOSS_MODE = SynchedEntityData.defineId(WitheredSymbiontEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RUSH_MODE = SynchedEntityData.defineId(WitheredSymbiontEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHOULD_NOT_GO_OVER_HALF = SynchedEntityData.defineId(WitheredSymbiontEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    public static final Predicate<LivingEntity> TARGET_PREDICATE = entity -> entity.isAttackable() && entity instanceof Player;
    public static final Predicate<LivingEntity> MOB_TARGET_PREDICATE = entity -> entity.isAttackable() && !(entity instanceof WitherSickened) && !(entity instanceof WitheredSymbiontEntity) && !(entity instanceof WitherStormEntity) && !(entity instanceof WitherStormHeadEntity) && !(entity instanceof TentacleEntity) && !(entity instanceof EnderMan) && !(entity instanceof EnderDragon) && !(entity instanceof WitherBoss) && !(entity instanceof WitherSkeleton) && (entity instanceof AbstractVillager || entity instanceof AbstractGolem || entity instanceof Monster || entity instanceof Animal || entity instanceof NeutralMob || entity instanceof Player);
    public static final Predicate<LivingEntity> PULSE_PREDICATE = entity -> entity.isAttackable() && !(entity instanceof WitheredSymbiontEntity) && !(entity instanceof WitherStormEntity) && !(entity instanceof WitherStormHeadEntity) && !(entity instanceof TentacleEntity) && !(entity instanceof CommandBlockEntity);
    private static final SimpleWeightedRandomList<EntityType<? extends Mob>> SYMBIONT_NORMAL_MOBS = SimpleWeightedRandomList.<EntityType<? extends Mob>>builder().add((WitherStormModEntityTypes.SICKENED_ZOMBIE.get()), 8).add((WitherStormModEntityTypes.SICKENED_VILLAGER.get()), 4).add((WitherStormModEntityTypes.SICKENED_SKELETON.get()), 8).add((WitherStormModEntityTypes.SICKENED_SPIDER.get()), 4).add((WitherStormModEntityTypes.SICKENED_CREEPER.get()), 1).add((WitherStormModEntityTypes.SICKENED_SNOW_GOLEM.get()), 2).add((WitherStormModEntityTypes.SICKENED_CHICKEN.get()), 3).add((WitherStormModEntityTypes.SICKENED_COW.get()), 3).add((WitherStormModEntityTypes.SICKENED_MUSHROOM_COW.get()), 1).add((WitherStormModEntityTypes.SICKENED_PIG.get()), 3).add((WitherStormModEntityTypes.SICKENED_BEE.get()), 4).add((WitherStormModEntityTypes.SICKENED_PARROT.get()), 4).add((WitherStormModEntityTypes.SICKENED_WOLF.get()), 4).add((WitherStormModEntityTypes.SICKENED_CAT.get()), 4).add((WitherStormModEntityTypes.SICKENED_PILLAGER.get()), 3).add((WitherStormModEntityTypes.SICKENED_VINDICATOR.get()), 3).build();
    private static final SimpleWeightedRandomList<EntityType<? extends Mob>> SYMBIONT_HARDER_MOBS = SimpleWeightedRandomList.<EntityType<? extends Mob>>builder().add((WitherStormModEntityTypes.SICKENED_ZOMBIE.get()), 8).add((WitherStormModEntityTypes.SICKENED_VILLAGER.get()), 6).add((WitherStormModEntityTypes.SICKENED_SKELETON.get()), 8).add((WitherStormModEntityTypes.SICKENED_SPIDER.get()), 6).add((WitherStormModEntityTypes.SICKENED_CREEPER.get()), 1).add((WitherStormModEntityTypes.SICKENED_SNOW_GOLEM.get()), 1).add((WitherStormModEntityTypes.SICKENED_PHANTOM.get()), 3).add((WitherStormModEntityTypes.SICKENED_BEE.get()), 6).add((WitherStormModEntityTypes.SICKENED_PARROT.get()), 1).add((WitherStormModEntityTypes.SICKENED_WOLF.get()), 2).add((WitherStormModEntityTypes.SICKENED_CAT.get()), 2).add((WitherStormModEntityTypes.SICKENED_PILLAGER.get()), 3).add((WitherStormModEntityTypes.SICKENED_VINDICATOR.get()), 6).build();
    private List<Goal> bossFightGoals;
    private MeleeAttackGoal attackGoal;
    private PrepareSpellGoal prepareSpellGoal;
    private UseSpellGoal useSpellGoal;
    private SummonMobsGoal summonMobsGoal;
    private DoNothingGoal doNothingGoal;
    private int stageTicks;
    private int spellCastingTime;
    private int nextSpellPickCount;
    private boolean isDoingSmash;
    private int smashAirTime;
    private List<LivingEntity> entitiesToThrow = new ArrayList<LivingEntity>();
    private int spellsUsed;
    private float crouchAnim;
    private float crouchAnimO;
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);
    private int specialDeathTime;
    @Nullable
    private UUID summoner;
    private int attackDelay;
    private List<ItemStack> dropItems = Lists.newArrayList();
    private float tearAlpha;
    private float tearAlphaO;
    private List<UUID> fightContributors = Lists.newArrayList();
    @Nullable
    private SymbiontSpell spellInstance;

    public WitheredSymbiontEntity(EntityType<? extends WitheredSymbiontEntity> type, Level world) {
        super(type, world);
        this.xpReward = 150;
        this.lookControl = new ConditionalLookController<>(this, (WitheredSymbiontEntity entity) -> !entity.isVulnerable() && !entity.isDeadOrDying());
    }

    private static TargetingConditions protectPredicate(double radius) {
        return TargetingConditions.forNonCombat().range(radius).selector(entity -> entity instanceof Player);
    }

    public float getStepHeight() {
        return 1.0f;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BOSSFIGHT_STAGE, BossfightStage.ATTACKING);
        builder.define(SPELL_TYPE, ((SpellType)WitherStormModSymbiontSpellTypes.EMPTY.get()));
        builder.define(NON_BOSS_MODE, false);
        builder.define(RUSH_MODE, false);
        builder.define(SHOULD_NOT_GO_OVER_HALF, true);
    }

    protected void registerGoals() {
        this.bossFightGoals = new ArrayList<Goal>();
        this.attackGoal = new MeleeAttackGoal((PathfinderMob)this, 1.0, true);
        this.prepareSpellGoal = new PrepareSpellGoal(this);
        this.useSpellGoal = new UseSpellGoal(this);
        this.summonMobsGoal = new SummonMobsGoal(this, SYMBIONT_NORMAL_MOBS, SYMBIONT_HARDER_MOBS);
        this.doNothingGoal = new DoNothingGoal(this);
        this.bossFightGoals.add((Goal)this.attackGoal);
        this.bossFightGoals.add((Goal)this.prepareSpellGoal);
        this.bossFightGoals.add((Goal)this.useSpellGoal);
        this.bossFightGoals.add((Goal)this.summonMobsGoal);
        this.bossFightGoals.add(this.doNothingGoal);
        this.goalSelector.addGoal(1, (Goal)this.prepareSpellGoal);
        this.goalSelector.addGoal(2, (Goal)this.useSpellGoal);
        this.goalSelector.addGoal(3, (Goal)this.attackGoal);
        this.goalSelector.addGoal(4, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, (double)0.7f));
        this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
        this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, TARGET_PREDICATE));
        if (((Boolean)WitherStormModConfig.SERVER.shouldSymbiontAttackMobs.get()).booleanValue()) {
            this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Mob.class, 10, true, false, MOB_TARGET_PREDICATE));
        }
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsNonBossMode", this.isNonBossMode());
        compound.putBoolean("IsRushMode", this.isRushMode());
        compound.putInt("Stage", this.getStage().ordinal());
        compound.putInt("StageTicks", this.getStageTicks());
        compound.putString("Spell", Objects.requireNonNull(((Registry)WitherStormModRegistries.SPELL_TYPES.get()).getKey(this.getSpell()), "Unregistered spell").toString());
        compound.putInt("SpellCastingTicks", this.spellCastingTime);
        compound.putInt("NextSpellPick", this.nextSpellPickCount);
        compound.putBoolean("Smashing", this.isSmashing());
        compound.putInt("SmashAirTime", this.smashAirTime);
        compound.putInt("SpellsUsed", this.getSpellsUsed());
        if (this.summoner != null) {
            compound.putUUID("Summoner", this.summoner);
        }
        compound.putInt("AttackDelay", this.attackDelay);
        if (!this.dropItems.isEmpty()) {
            ListTag dropItems = new ListTag();
            for (ItemStack stack : this.dropItems) {
                if (stack.isEmpty()) continue;
                CompoundTag tag = new CompoundTag();
                stack.save(tag);
                dropItems.add(tag);
            }
            compound.put("DropItems", (Tag)dropItems);
        }
        compound.putBoolean("ShouldNotGoOverHalf", ((Boolean)this.entityData.get(SHOULD_NOT_GO_OVER_HALF)).booleanValue());
        ListTag fightContributors = new ListTag();
        for (UUID id : this.fightContributors) {
            fightContributors.add(NbtUtils.createUUID((UUID)id));
        }
        compound.put("FightContributors", (Tag)fightContributors);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        SpellType type;
        String rawId;
        ResourceLocation loc;
        int ordinal;
        super.readAdditionalSaveData(compound);
        if (compound.contains("IsNonBossMode")) {
            this.setNonBossMode(compound.getBoolean("IsNonBossMode"));
        }
        if (compound.contains("IsRushMode")) {
            this.setRushMode(compound.getBoolean("IsRushMode"));
        }
        if (compound.contains("Stage") && (ordinal = compound.getInt("Stage")) >= 0 && ordinal < BossfightStage.values().length) {
            this.setStage(BossfightStage.values()[ordinal]);
        }
        this.setStageTicks(compound.getInt("StageTicks"));
        if (compound.contains("Spell", 8) && (loc = ResourceLocation.tryParse((String)(rawId = compound.getString("Spell")))) != null && (type = (SpellType)((Registry)WitherStormModRegistries.SPELL_TYPES.get()).getValue(loc)) != null) {
            this.setSpell(type);
        }
        this.spellCastingTime = compound.getInt("SpellCastingTicks");
        this.nextSpellPickCount = compound.getInt("NextSpellPick");
        this.setSmashing(compound.getBoolean("Smashing"));
        this.smashAirTime = compound.getInt("SmashAirTime");
        this.spellsUsed = compound.getInt("SpellsUsed");
        if (compound.contains("Summoner")) {
            this.summoner = compound.getUUID("Summoner");
        }
        this.attackDelay = compound.getInt("AttackDelay");
        if (compound.contains("DropItems")) {
            ListTag dropItems = compound.getList("DropItems", 10);
            for (int i = 0; i < dropItems.size(); ++i) {
                this.dropItems.add(ItemStack.of((CompoundTag)dropItems.getCompound(i)));
            }
        }
        if (compound.contains("ShouldNotGoOverHalf")) {
            this.entityData.set(SHOULD_NOT_GO_OVER_HALF, compound.getBoolean("ShouldNotGoOverHalf"));
        }
        this.fightContributors.clear();
        ListTag fightContributors = compound.getList("FightContributors", 11);
        for (Tag tag : fightContributors) {
            this.fightContributors.add(NbtUtils.loadUUID((Tag)tag));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 60.0).add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.ATTACK_DAMAGE, 16.0).add(Attributes.FOLLOW_RANGE, 45.0);
    }

    protected int decreaseAirSupply(int supply) {
        return supply;
    }

    protected void doPush(@NotNull Entity entity) {
        if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && entity instanceof LivingEntity && TARGET_PREDICATE.test(((LivingEntity)entity)) && this.getRandom().nextInt(20) == 0) {
            this.setTarget((LivingEntity)entity);
        }
        super.doPush(entity);
    }

    public void aiStep() {
        super.aiStep();
        ++this.stageTicks;
        if (this.spellCastingTime > 0) {
            SpellType type;
            --this.spellCastingTime;
            this.doSpellCasting();
            if (this.spellCastingTime <= 0) {
                this.castSpell();
            }
            if ((type = this.getSpell()).doProtection()) {
                double radius = type.protectionRadius();
                List<Player> entities = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(radius));
                for (Player player : entities) {
                    if (this.entitiesToThrow.contains(player) || !WitheredSymbiontEntity.protectPredicate(radius).test((LivingEntity)this, (LivingEntity)player)) continue;
                    this.entitiesToThrow.add((LivingEntity)player);
                    if (this.level().isClientSide) continue;
                    this.playSound(WitherStormModSoundEvents.WITHERED_SYMBIONT_LAUNCH_MOB.get(), 16.0f, 1.0f);
                }
            }
            for (int i = 0; i < this.entitiesToThrow.size(); ++i) {
                LivingEntity entity = this.entitiesToThrow.get(i);
                if (WitheredSymbiontEntity.protectPredicate(type.protectionRadius()).test((LivingEntity)this, entity)) {
                    Vec3 delta = this.position().subtract(entity.position()).normalize().add(0.0, -0.5, 0.0).scale(-type.protectionThrowStrength());
                    entity.setDeltaMovement(delta);
                    continue;
                }
                this.entitiesToThrow.remove(i);
            }
        }
        if (this.nextSpellPickCount > 0) {
            --this.nextSpellPickCount;
        }
        if (this.isSmashing()) {
            if (this.smashAirTime > 0) {
                --this.smashAirTime;
                if (this.smashAirTime <= 0) {
                    this.setDeltaMovement(this.getDeltaMovement().x(), -5.0, this.getDeltaMovement().z());
                }
            } else if (this.onGround()) {
                this.setSmashing(false);
                if (!this.level().isClientSide()) {
                    float strength = 1.5f;
                    if (this.shouldIncreaseDifficulty()) {
                        strength = 2.5f;
                    }
                    this.level().explode((Entity)this, this.getX(), this.getY(), this.getZ(), strength, Level.ExplosionInteraction.MOB);
                }
            }
        }
        if (this.isCastingSpell() || this.isSummoningMobs()) {
            for (int i = 0; i < 5; ++i) {
                double x = this.getX() + this.random.nextGaussian() * 2.0;
                double y = this.getEyeY() + this.random.nextGaussian() * 2.0;
                double z = this.getZ() + this.random.nextGaussian() * 2.0;
                Vec3 delta = this.getEyePosition(1.0f).subtract(x, y, z).normalize().multiply(0.2, 0.2, 0.2);
                this.level().addParticle((ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(), x, y, z, delta.x(), delta.y(), delta.z());
            }
        }
        if (this.getDeltaMovement().horizontalDistanceSqr() > 2.500000277905201E-7 && this.random.nextInt(5) == 0) {
            int i = Mth.floor((double)this.getX());
            int j = Mth.floor((double)(this.getY() - (double)0.2f));
            int k = Mth.floor((double)this.getZ());
            BlockPos pos = new BlockPos(i, j, k);
            BlockState state = this.level().getBlockState(pos);
            if (!state.is(Blocks.AIR)) {
                this.level().addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, state).setPos(pos), this.getX() + ((double)this.random.nextFloat() - 0.5) * (double)this.getBbWidth(), this.getY() + 0.1, this.getZ() + ((double)this.random.nextFloat() - 0.5) * (double)this.getBbWidth(), 4.0 * ((double)this.random.nextFloat() - 0.5), 0.5, ((double)this.random.nextFloat() - 0.5) * 4.0);
            }
        }
        if (!this.level().isClientSide() && this.getStage().shouldMoveToNextStage(this)) {
            this.nextStage();
        }
        if (this.attackDelay > 0) {
            --this.attackDelay;
            if (this.attackDelay <= 0 && this.isVulnerable()) {
                this.setStage(BossfightStage.ATTACKING);
            }
        }
    }

    public void tick() {
        super.tick();
        this.crouchAnimO = this.crouchAnim;
        if (this.isVulnerable()) {
            this.crouchAnim += (1.0f - this.crouchAnim) * 0.1f + 0.02f;
            if (this.crouchAnim > 0.6f) {
                this.crouchAnim = 0.6f;
            }
        } else {
            this.crouchAnim += -this.crouchAnim * 0.4f - 0.1f;
            if (this.crouchAnim < 0.0f) {
                this.crouchAnim = 0.0f;
            }
        }
        this.tearAlphaO = this.tearAlpha;
        if (((Boolean)WitherStormModConfig.SERVER.attackableWhenNotVulnerable.get()).booleanValue() || this.isVulnerable()) {
            if (this.tearAlpha < 1.0f) {
                this.tearAlpha += 0.05f;
            }
        } else if (this.tearAlpha > 0.0f) {
            this.tearAlpha -= 0.05f;
        }
        if (!this.level().isClientSide()) {
            this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    public boolean doHurtTarget(Entity entity) {
        float f = this.getAttackDamage();
        float f1 = (int)f > 0 ? f / 2.0f + (float)this.random.nextInt((int)f) : f;
        boolean flag = entity.hurt(this.damageSources().mobAttack((LivingEntity)this), f1);
        if (flag) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.8, 0.0));
            this.doEnchantDamageEffects((LivingEntity)this, entity);
        }
        return flag;
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    protected SoundEvent getAmbientSound() {
        return this.isVulnerable() ? null : WitherStormModSoundEvents.WITHERED_SYMBIONT_AMBIENT.get();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean killedEntity(ServerLevel level, LivingEntity entity) {
        if (!(entity instanceof Mob)) return false;
        Mob mob = (Mob)entity;
        if (!WorldTainting.getInstance().convertMob(mob, false)) return false;
        return true;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return WitherStormModSoundEvents.WITHERED_SYMBIONT_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        if (this.isNonBossMode()) {
            return WitherStormModSoundEvents.WITHERED_SYMBIONT_NORMAL_DEATH.get();
        }
        return WitherStormModSoundEvents.WITHERED_SYMBIONT_DEATH.get();
    }

    public float getVoicePitch() {
        return this.isDeadOrDying() ? 1.0f : super.getVoicePitch();
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(WitherStormModSoundEvents.WITHERED_SYMBIONT_STEP.get(), 0.3f, 1.0f);
    }

    public boolean canBeLeashed(@NotNull Player player) {
        return false;
    }

    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else if (!this.isPersistenceRequired() && !this.requiresCustomPersistence()) {
            List entities = WorldUtil.getPerformantEntitiesOfClass((ServerLevel)((ServerLevel)this.level()), WitherStormEntity.class, (AABB)this.getBoundingBox().inflate(400.0));
            if (entities.isEmpty()) {
                super.checkDespawn();
            } else {
                this.noActionTime = 0;
            }
        } else {
            this.noActionTime = 0;
        }
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, @NotNull DamageSource source) {
        return false;
    }
    @NotNull
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(3.0);
    }

    public boolean hurt(DamageSource source, float amount) {
        Projectile projectile;
        if ((source.getDirectEntity() instanceof AbstractHurtingProjectile || source.getDirectEntity() instanceof AbstractArrow) && ((projectile = (Projectile)source.getDirectEntity()).getOwner() instanceof WitherSickened || projectile.getOwner() instanceof WitheredSymbiontEntity)) {
            return false;
        }
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(source, amount);
        }
        if (!this.isCastingSpell() && ((Boolean)WitherStormModConfig.SERVER.attackableWhenNotVulnerable.get()).booleanValue() || this.isVulnerable()) {
            double angle;
            double angleDiff;
            Entity entity = source.getEntity();
            if (entity != null && ((angleDiff = ((double)(-this.yBodyRot) - (angle = Math.atan2(entity.getX() - this.getX(), entity.getZ() - this.getZ()) * 57.29577951308232) + 180.0 + 360.0) % 360.0) <= 40.0 || angleDiff >= 320.0)) {
                if (this.isVulnerable() && this.attackDelay <= 0) {
                    this.attackDelay = 20;
                }
                if (!this.isVulnerable() && entity instanceof LivingEntity && !this.isDeadOrDying()) {
                    ((SpellType)WitherStormModSymbiontSpellTypes.SMASH.get()).makeSpell(this).cast((LivingEntity)entity);
                }
                if (source.is(DamageTypeTags.IS_EXPLOSION)) {
                    amount /= 4.0f;
                }
                if (this.shouldNotGoOverHalfHealth()) {
                    float predictedHealth = this.getHealth() - amount;
                    float maxHealthHalf = this.getMaxHealth() / 2.0f;
                    amount = Math.min(amount - (maxHealthHalf - predictedHealth), amount);
                }
                float healthCurrent = this.getHealth();
                boolean flag = super.hurt(source, amount);
                float damageDealt = healthCurrent - this.getHealth();
                if (damageDealt >= 5.0f && entity instanceof Player && !this.fightContributors.contains(entity.getUUID())) {
                    this.fightContributors.add(entity.getUUID());
                }
                return flag;
            }
            return false;
        }
        return false;
    }

    public int getStageTicks() {
        return this.stageTicks;
    }

    public void setStageTicks(int ticks) {
        this.stageTicks = ticks;
    }

    public BossfightStage getStage() {
        return (BossfightStage)(this.entityData.get(BOSSFIGHT_STAGE));
    }

    protected void clearBossFightGoals() {
        this.bossFightGoals.forEach(arg_0 -> ((GoalSelector)this.goalSelector).removeGoal(arg_0));
    }

    protected void addBossFightGoal(int level, Goal goal) {
        this.goalSelector.addGoal(level, goal);
    }

    public void setStage(BossfightStage stage) {
        if (this.getStage() != stage) {
            this.getStage().finish(this);
        }
        this.entityData.set(BOSSFIGHT_STAGE, stage);
        this.getStage().init(this);
    }

    public void nextStage() {
        BossfightStage nextStage = this.getNextStage(1);
        if (nextStage == BossfightStage.SUMMONING && this.isNonBossMode()) {
            this.setStage(this.getNextStage(2));
        } else {
            this.setStage(nextStage);
        }
    }

    private BossfightStage getNextStage(int advance) {
        int next = this.getStage().ordinal() + advance;
        if (next < BossfightStage.values().length) {
            return BossfightStage.values()[next];
        }
        return BossfightStage.values()[0];
    }

    public SpellType getSpell() {
        return (SpellType)this.entityData.get(SPELL_TYPE);
    }

    public void setSpell(SpellType spell) {
        this.entityData.set(SPELL_TYPE, spell);
        if (!this.level().isClientSide()) {
            this.spellInstance = spell.makeSpell(this);
        }
    }

    @Nullable
    public SymbiontSpell getSpellInstance() {
        return this.spellInstance;
    }

    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> parameter) {
        super.onSyncedDataUpdated(parameter);
        if (BOSSFIGHT_STAGE.equals(parameter)) {
            this.getStage().init(this);
        }
    }

    public boolean hasSpell() {
        return this.getSpell() != WitherStormModSymbiontSpellTypes.EMPTY.get();
    }

    public void beginSpellCasting() {
        if (!this.level().isClientSide() && this.spellInstance != null) {
            this.spellInstance.start(this.getTarget());
            this.spellCastingTime = this.getSpell().spellTime();
            WitherStormModPacketHandlers.MAIN.send(SimpleChannel.toTracking(this), new SetSpellTimeMessage(this.getId(), this.spellCastingTime));
        }
    }

    public boolean isCastingSpell() {
        return this.spellCastingTime > 0;
    }

    public boolean isSummoningMobs() {
        return this.getStage() == BossfightStage.SUMMONING;
    }

    public boolean isVulnerable() {
        return this.getStage() == BossfightStage.VULNERABLE;
    }

    public void breakSpell() {
        if (this.isCastingSpell()) {
            this.spellCastingTime = 0;
            if (!this.level().isClientSide() && this.spellInstance != null) {
                this.spellInstance.finish();
            }
            this.level().broadcastEntityEvent((Entity)this, (byte)11);
        }
    }

    public void handleEntityEvent(byte event) {
        if (event == 11) {
            this.breakSpell();
        } else if (event == 12) {
            this.activateAttackDelay();
        } else {
            super.handleEntityEvent(event);
        }
    }

    public void castSpell() {
        if (!this.level().isClientSide) {
            if (this.spellInstance != null) {
                if (this.getTarget() != null) {
                    this.spellInstance.cast(this.getTarget());
                }
                this.spellInstance.finish();
            }
        } else {
            for (int i = 0; i < 10; ++i) {
                this.level().addParticle((ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(), this.getX(), this.getEyeY(), this.getZ(), this.random.nextGaussian() * 0.5, this.random.nextGaussian() * 0.5, this.random.nextGaussian() * 0.5);
            }
        }
    }

    public void doSpellCasting() {
        if (!this.level().isClientSide() && this.spellInstance != null) {
            int spellCastingTime = this.getSpell().spellTime() - this.spellCastingTime;
            if (this.getTarget() != null && this.getTarget().isAlive()) {
                this.spellInstance.doCasting(this.getTarget());
            } else if (spellCastingTime % 20 == 0) {
                this.breakSpell();
            }
        }
    }

    public boolean canPickSpell() {
        return !this.hasSpell() || this.nextSpellPickCount <= 0;
    }

    public void setAndCastSpell(SpellType type) {
        if (type != WitherStormModSymbiontSpellTypes.EMPTY.get() && !this.isVulnerable()) {
            this.nextSpellPickCount = 0;
            this.setSpell(type);
            this.useSpellGoal.nextAttackTickCount = this.tickCount + 1;
            this.playSound(WitherStormModSoundEvents.WITHERED_SYMBIONT_PREPARE_SPELL.get(), 4.0f, 1.0f);
            this.nextSpellPickCount = 400 + this.getRandom().nextInt(400) - (this.shouldIncreaseDifficulty() ? 320 : 0);
            if (this.shouldNotGoOverHalfHealth() && this.getHealth() / this.getMaxHealth() <= 0.5f) {
                this.entityData.set(SHOULD_NOT_GO_OVER_HALF, false);
            }
        }
    }

    public void setSmashing(boolean flag) {
        this.isDoingSmash = flag;
        if (flag) {
            this.smashAirTime = 20;
        }
    }

    public boolean isSmashing() {
        return this.isDoingSmash;
    }

    public int getSpellsUsed() {
        return this.spellsUsed;
    }

    public void spellUsed() {
        ++this.spellsUsed;
    }

    public float getVulnerableAnim(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.crouchAnimO, (float)this.crouchAnim);
    }

    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public boolean shouldIncreaseDifficulty() {
        return this.isRushMode() || this.getHealth() / this.getMaxHealth() <= 0.5f;
    }

    public boolean shouldNotGoOverHalfHealth() {
        return !this.isNonBossMode() && (Boolean)this.entityData.get(SHOULD_NOT_GO_OVER_HALF) != false;
    }

    protected void tickDeath() {
        if (this.isNonBossMode()) {
            super.tickDeath();
        } else {
            int totalTime = 320;
            ++this.specialDeathTime;
            for (int i = 0; i < (totalTime - this.specialDeathTime) / 40; ++i) {
                this.level().addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, this.getRandomX(1.0), this.getRandomY(), this.getRandomZ(1.0), this.random.nextGaussian() * 0.02, this.random.nextGaussian() * 0.02, this.random.nextGaussian() * 0.02);
            }
            float speed = 3.0f;
            float f = Mth.degreesDifference((float)this.getXRot(), (float)-50.0f);
            float f1 = Mth.clamp((float)f, (float)(-speed), (float)speed);
            this.setXRot(this.getXRot() + f1);
            if (this.specialDeathTime == totalTime) {
                this.remove(Entity.RemovalReason.KILLED);
                if (!this.level().isClientSide() && this.dropItems != null) {
                    List<Player> players = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(20.0), EntitySelector.NO_SPECTATORS);
                    if (players.size() > 1 && !this.fightContributors.isEmpty()) {
                        for (UUID id : this.fightContributors) {
                            { var player = players.stream().filter(p -> p.getUUID().equals(id)).findFirst();
                                for (ItemStack stack : this.dropItems) {
                                    if (stack.isEmpty()) continue;
                                    ItemStack copy = stack.copy();
                                    if (player.getInventory().add(copy)) continue;
                                    ItemEntity entity = this.spawnAtLocation(copy);
                                    assert (entity != null);
                                    entity.moveTo(player.position());
                                    entity.setTarget(id);
                                }
                            }
                        }
                    } else {
                        this.dropDrops();
                    }
                }
                for (int i = 0; i < 20; ++i) {
                    this.level().addParticle((ParticleOptions)ParticleTypes.POOF, this.getRandomX(1.0), this.getRandomY(), this.getRandomZ(1.0), this.random.nextGaussian() * 0.02, this.random.nextGaussian() * 0.02, this.random.nextGaussian() * 0.02);
                }
            }
        }
    }

    private void dropDrops() {
        for (ItemStack stack : this.dropItems) {
            if (stack.isEmpty()) continue;
            ItemEntity entity = this.spawnAtLocation(stack, 8.0f);
            assert (entity != null);
            entity.setDeltaMovement(0.0, -0.08, 0.0);
            entity.setNoGravity(true);
        }
        this.dropItems.clear();
    }

    public void die(@NotNull DamageSource source) {
        super.die(source);
        LivingEntity livingEntity = this.getKillCredit();
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            { var data = player.getData(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA.get());
                WitherStormEntity owner = this.getOwner();
                if (owner != null) {
                    data.markKilledSymbiont(owner);
                }
            }
        }
        for (Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(20.0))) {
                        player.getData(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA.get()).makeInvulnerable(Mth.clamp((int)((Integer)WitherStormModConfig.SERVER.playerInvulnerableTime.get()), (int)1, (int)10) * 1200 + player.getRandom().nextInt(1200));
        }
    }

    public void setOwner(WitherStormEntity entity) {
        this.summoner = entity.getUUID();
    }

    @Nullable
    public WitherStormEntity getOwner() {
        if (!this.level().isClientSide()) {
            ServerLevel world = (ServerLevel)this.level();
            for (Entity entity : world.getAllEntities()) {
                if (!entity.getUUID().equals(this.summoner) || !(entity instanceof WitherStormEntity)) continue;
                return (WitherStormEntity)entity;
            }
        }
        return null;
    }

    public SoundEvent getBossTheme() {
        if (!this.shouldIncreaseDifficulty()) {
            return WitherStormModSoundEvents.WITHERED_SYMBIONT_THEME.get();
        }
        return WitherStormModSoundEvents.WITHERED_SYMBIONT_INTENSE_THEME.get();
    }

    public boolean isStillAlive() {
        return this.isAlive();
    }

    public Vec3 getPosition() {
        return this.position();
    }

    public double distanceToPlay() {
        return 45.0;
    }

    public int priority() {
        return 2;
    }

    public int getFadeTime() {
        return 120;
    }

    public boolean checkConfig() {
        return (Boolean)WitherStormModConfig.CLIENT.playSymbiontTheme.get();
    }

    public void activateAttackDelay() {
        this.attackDelay = 20;
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent((Entity)this, (byte)12);
        }
    }

    public boolean hasAttackDelay() {
        return this.attackDelay > 0;
    }

    public boolean isNonBossMode() {
        return (Boolean)this.entityData.get(NON_BOSS_MODE);
    }

    public void setNonBossMode(boolean mode) {
        this.entityData.set(NON_BOSS_MODE, mode);
        this.xpReward = mode ? 25 : 150;
    }

    public boolean isRushMode() {
        return (Boolean)this.entityData.get(RUSH_MODE);
    }

    public void setRushMode(boolean mode) {
        this.entityData.set(RUSH_MODE, mode);
    }

    protected void dropFromLootTable(@NotNull DamageSource source, boolean player) {
        ResourceLocation id = this.getLootTable();
        LootTable table = this.level().getServer().getLootData().getLootTable(id);
        LootParams.Builder builder = new LootParams.Builder((ServerLevel)this.level()).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.DAMAGE_SOURCE, source).withOptionalParameter(LootContextParams.KILLER_ENTITY, source.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, source.getDirectEntity());
        if (player && this.lastHurtByPlayer != null) {
            builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
        }
        LootParams params = builder.create(LootContextParamSets.ENTITY);
        this.dropItems = table.getRandomItems(params, this.getLootTableSeed());
    }

    protected boolean canRide(@NotNull Entity entity) {
        return super.canRide(entity) && !(entity instanceof Boat) && !(entity instanceof AbstractMinecart);
    }

    public Component getWatermark() {
        return Component.translatable((String)"witherstormmod.watermark.withered_symbiont_theme");
    }

    public float getTearAlpha(float partialTicks) {
        return Mth.lerp((float)partialTicks, (float)this.tearAlphaO, (float)this.tearAlpha);
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, SpawnGroupData groupData, CompoundTag tag) {
        double healthAddition;
        List nearbyPlayers = level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(150.0), e -> e.isAlive() && !e.isSpectator());
        if (nearbyPlayers.size() > 1 && (healthAddition = (double)nearbyPlayers.size() * (Double)WitherStormModConfig.SERVER.healthScalePerPlayer.get()) > 0.0) {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier("Health scaling", healthAddition, AttributeModifier.Operation.ADD_VALUE));
            this.setHealth(this.getMaxHealth());
        }
        return super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
    }

    public LivingEntity getRandomNearbyTargetOrFallback(LivingEntity entity, Predicate<LivingEntity> selector) {
        List entities = this.getNearbyTargets(selector).filter(e -> e != entity).collect(Collectors.toList());
        if (!entities.isEmpty() && this.random.nextInt(entities.size() + 1) != 0) {
            Collections.shuffle(entities);
            return (LivingEntity)entities.get(0);
        }
        return entity;
    }

    public Stream<LivingEntity> getNearbyTargets(Predicate<LivingEntity> selector) {
        double range = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        TargetingConditions conditions = TargetingConditions.forCombat().range(this.getAttributeValue(Attributes.FOLLOW_RANGE)).selector(selector);
        return this.level().getNearbyEntities(LivingEntity.class, conditions, (LivingEntity)this, this.getBoundingBox().inflate(range)).stream().filter(e -> e != this);
    }

    public int getSpellCastingTime() {
        return this.spellCastingTime;
    }

    public float getJumpPower() {
        return super.getJumpPower();
    }

    public UseSpellGoal getUseSpellGoal() {
        return this.useSpellGoal;
    }

    public void setHalfHealthLimit(boolean flag) {
        this.entityData.set(SHOULD_NOT_GO_OVER_HALF, flag);
    }

    public int getNextSpellPickCount() {
        return this.nextSpellPickCount;
    }

    public void setNextSpellPickCount(int count) {
        this.nextSpellPickCount = count;
    }

    /*
     * Uses 'sealed' constructs - enablewith --sealed true
     */
    public static enum BossfightStage {
        ATTACKING{

            @Override
            public void init(WitheredSymbiontEntity entity) {
                super.init(entity);
                if (!entity.level().isClientSide) {
                    entity.spellsUsed = 0;
                    entity.clearBossFightGoals();
                    entity.addBossFightGoal(1, (Goal)entity.prepareSpellGoal);
                    entity.addBossFightGoal(2, (Goal)entity.useSpellGoal);
                    entity.addBossFightGoal(3, (Goal)entity.attackGoal);
                }
            }

            @Override
            public void finish(WitheredSymbiontEntity entity) {
                super.finish(entity);
                if (!entity.level().isClientSide) {
                    entity.spellsUsed = 0;
                    entity.setSpell((SpellType)WitherStormModSymbiontSpellTypes.EMPTY.get());
                }
            }

            @Override
            public boolean shouldMoveToNextStage(WitheredSymbiontEntity entity) {
                return entity.getSpellsUsed() > 5 && !entity.isCastingSpell() && entity.getStageTicks() % 80 == 0 && entity.getTarget() != null;
            }
        }
        ,
        SUMMONING{

            @Override
            public void init(WitheredSymbiontEntity entity) {
                super.init(entity);
                if (!entity.level().isClientSide) {
                    entity.clearBossFightGoals();
                    entity.addBossFightGoal(1, (Goal)entity.summonMobsGoal);
                }
            }
        }
        ,
        VULNERABLE{

            @Override
            public void init(WitheredSymbiontEntity entity) {
                super.init(entity);
                if (!entity.level().isClientSide) {
                    entity.clearBossFightGoals();
                    entity.addBossFightGoal(1, entity.doNothingGoal);
                    entity.playSound(WitherStormModSoundEvents.WITHERED_SYMBIONT_POWER_DOWN.get(), 4.0f, 1.0f);
                }
            }

            @Override
            public void finish(WitheredSymbiontEntity entity) {
                super.finish(entity);
            }

            @Override
            public boolean shouldMoveToNextStage(WitheredSymbiontEntity entity) {
                return entity.getStageTicks() > 4800;
            }
        };


        public boolean shouldDoNothing() {
            return false;
        }

        public void init(WitheredSymbiontEntity entity) {
            entity.setStageTicks(0);
        }

        public void finish(WitheredSymbiontEntity entity) {
        }

        public boolean shouldMoveToNextStage(WitheredSymbiontEntity entity) {
            return false;
        }
    }

    public static class DoNothingGoal
    extends Goal {
        protected final WitheredSymbiontEntity entity;

        public DoNothingGoal(WitheredSymbiontEntity entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.TARGET, Goal.Flag.MOVE, Goal.Flag.JUMP));
        }

        public boolean canUse() {
            return this.entity.isVulnerable();
        }

        public void tick() {
            float speed = 3.0f;
            float f = Mth.degreesDifference((float)this.entity.getXRot(), (float)55.0f);
            float f1 = Mth.clamp((float)f, (float)(-speed), (float)speed);
            this.entity.setXRot(this.entity.getXRot() + f1);
        }
    }

    public static class SetSpellTimeMessage
    extends Packet {
        private int id;
        private int time;

        public SetSpellTimeMessage(int id, int time) {
            super(true);
            this.id = id;
            this.time = time;
        }

        public SetSpellTimeMessage() {
            super(false);
        }

        public void decode(FriendlyByteBuf buffer) {
            this.id = buffer.readVarInt();
            this.time = buffer.readInt();
        }

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeVarInt(this.id);
            buffer.writeInt(this.time);
        }

        public Runnable getProcessor(NetworkEvent.Context context) {
            return () -> {
                if (FMLEnvironment.dist == Dist.CLIENT) {
                    Optional<Level> optional = (Optional<Level>)LogicalSidedProvider.CLIENTWORLD.get(context.getDirection().getReceptionSide());
                    optional.ifPresent(world -> {
                        Entity entity = world.getEntity(this.id);
                        if (entity instanceof WitheredSymbiontEntity symbiont) {
                            symbiont.spellCastingTime = this.time;
                        }
                    });
                }
            };
        }
    }
}

