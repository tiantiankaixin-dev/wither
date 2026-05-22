package nonamecrackers2.witherstormmod.common.capability;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements.Type;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.level.LevelEvent.Load;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.TentacleEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormHeadEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModFeatures;
import nonamecrackers2.witherstormmod.common.init.WitherStormModStructures;
import nonamecrackers2.witherstormmod.common.util.BowelsTeleporter;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WitherStormBowelsManager {
   private static final Logger LOGGER = LogManager.getLogger("witherstormmod/WitherStormBowelsManager");
   private static final int ENTRANCE_SEARCH_RADIUS = 96;
   private static final int ENTRANCE_SEARCH_RADIUS_MIN = 35;
   private static BlockPos NORTH_HEAD_POS = new BlockPos(-2, 128, 27);
   private static BlockPos SOUTH_HEAD_POS = new BlockPos(-3, 128, -23);
   private final Map<UUID, Entity> queuedEnter = new HashMap<>();
   private final Map<Entity, Runnable> queuedLeave = new HashMap<>();
   private final List<WitherStormBowelsManager.BowelsInstance> instances = new ArrayList<>();
   private final ServerLevel world;
   private final Random random = new Random();
   @Nullable
   private Pair<BlockPos, StructureStart> nextAvailableStructure;

   public WitherStormBowelsManager(ServerLevel world) {
      this.world = world;
   }

   public WitherStormBowelsManager() {
      this.world = null;
   }

   public void read(CompoundTag compound) {
      ListTag list = compound.getList("Instances", 10);

      for (int i = 0; i < list.size(); i++) {
         CompoundTag instanceCompound = list.getCompound(i);
         this.instances.add(WitherStormBowelsManager.BowelsInstance.read(instanceCompound));
      }
   }

   public CompoundTag write() {
      CompoundTag compound = new CompoundTag();
      ListTag list = new ListTag();

      for (WitherStormBowelsManager.BowelsInstance instance : this.instances) {
         list.add(instance.write());
      }

      compound.put("Instances", list);
      return compound;
   }

   @Nullable
   public WitherStormBowelsManager.BowelsInstance getOrCreateInstanceFor(WitherStormEntity storm) {
      WitherStormBowelsManager.BowelsInstance instance = this.get(storm.getUUID());
      if (instance == null) {
         LOGGER.debug("Searching for available bowels arena for entity {}", storm);
         Pair<BlockPos, StructureStart> start;
         if (this.nextAvailableStructure != null) {
            LOGGER.debug("Using pre-located structure...");
            start = this.nextAvailableStructure;
            this.nextAvailableStructure = null;
         } else {
            start = this.getAvailableStructure();
         }

         if (start != null) {
            instance = new WitherStormBowelsManager.BowelsInstance(start, storm.getUUID());
         } else {
            LOGGER.error("Could not find an available bowels structure for {}. This shouldn't happen!", storm);
         }
      }

      return instance;
   }

   @Nullable
   public Pair<BlockPos, StructureStart> getAvailableStructure() {
      if (!this.world.getServer().getWorldData().worldGenOptions().generateStructures()) {
         this.world
            .getServer()
            .getPlayerList()
            .getPlayers()
            .forEach(player -> player.sendSystemMessage(Component.translatable("chat.witherstormmod.bowels.structuresDisabled").withStyle(ChatFormatting.RED)));
         LOGGER.warn("Structures are disabled, meaning the bowels cannot be accessed. Please enable structures to have access to the final boss battle.");
         return null;
      } else {
         Holder<Structure> holder = getBowels(this.world);
         StructureManager manager = this.world.structureManager();
         BlockPos startPos = BlockPos.ZERO;
         if (!this.instances.isEmpty()) {
            startPos = this.instances.get(this.instances.size() - 1).getPos();
         }

         RandomSpreadStructurePlacement placement = null;

         for (StructurePlacement structurePlacement : this.world.getChunkSource().getGeneratorState().getPlacementsForStructure(holder)) {
            if (structurePlacement instanceof RandomSpreadStructurePlacement p) {
               placement = p;
               break;
            }
         }

         if (placement == null) {
            LOGGER.error("The Bowels structure must be the random spread placement type!");
            return null;
         } else {
            int sectionX = SectionPos.blockToSectionCoord(startPos.getX());
            int sectionZ = SectionPos.blockToSectionCoord(startPos.getZ());

            for (int area = 0; area <= 100; area++) {
               int i = placement.spacing();

               for (int x = -area; x <= area; x++) {
                  boolean xFlag = x == -area || x == area;

                  for (int z = -area; z <= area; z++) {
                     boolean zFlag = z == -area || z == area;
                     if (xFlag || zFlag) {
                        int finalX = sectionX + i * x;
                        int finalZ = sectionZ + i * z;
                        ChunkPos potential = placement.getPotentialStructureChunk(this.world.getSeed(), finalX, finalZ);
                        StructureCheckResult result = manager.checkStructurePresence(potential, (Structure)holder.value(), false);
                        if (result != StructureCheckResult.START_NOT_PRESENT) {
                           ChunkAccess chunk = this.world.getChunk(potential.x, potential.z, ChunkStatus.STRUCTURE_STARTS);
                           StructureStart start = manager.getStartForStructure(SectionPos.bottomOf(chunk), (Structure)holder.value(), chunk);
                           if (start != null && start.isValid()) {
                              BlockPos locatePos = placement.getLocatePos(start.getChunkPos());
                              if (this.instances.stream().filter(instance -> instance.getPos().equals(locatePos)).collect(Collectors.toList()).isEmpty()) {
                                 return Pair.of(locatePos, start);
                              }
                           }
                        }
                     }
                  }
               }
            }

            return null;
         }
      }
   }

   @Nullable
   public WitherStormBowelsManager.BowelsInstance get(UUID uuid) {
      for (WitherStormBowelsManager.BowelsInstance instance : this.instances) {
         if (instance.witherStorm.equals(uuid) && !instance.isCompleted()) {
            return instance;
         }
      }

      return null;
   }

   public void add(WitherStormBowelsManager.BowelsInstance instance) {
      if (!this.instances.contains(instance)) {
         this.instances.add(instance);
      }
   }

   public void onLoad() {
      for (WitherStormBowelsManager.BowelsInstance instance : this.instances) {
         instance.setup(this.world);
      }

      this.nextAvailableStructure = this.getAvailableStructure();
      LOGGER.debug("Pre-locating a bowels arena: {}", this.nextAvailableStructure);
   }

   @Nullable
   private WitherStormEntity findStorm(UUID uuid) {
      MinecraftServer server = this.world.getServer();

      for (Level world : server.getAllLevels()) {
         ServerLevel serverWorld = (ServerLevel)world;
         Entity entity = serverWorld.getEntity(uuid);
         if (entity != null && entity instanceof WitherStormEntity) {
            return (WitherStormEntity)entity;
         }
      }

      return null;
   }

   public BlockPos calculateEntrancePos(WitherStormBowelsManager.BowelsInstance instance) {
      BlockPos pos = instance.getPos();
      if (instance.getStart() != null) {
         boolean flag = true;
         if ((Boolean)WitherStormModConfig.SERVER.randomBowelsEntrace.get()) {
            label73:
            for (int startRadius = 96; startRadius > 35; startRadius -= 10) {
               int randomStartAngle = this.random.nextInt(360);
               int angleOffset = 20;

               for (int i = 0; i < 360 / angleOffset; i++) {
                  float angle = (float)randomStartAngle + (float)(angleOffset * i);
                  int x = (int)(Mth.sin(angle * (float) (Math.PI / 180.0)) * (float)startRadius) + pos.getX();
                  int z = (int)(Mth.cos(angle * (float) (Math.PI / 180.0)) * (float)startRadius) + pos.getZ();
                  BlockPos currentPos = new BlockPos(x, 112, z);

                  for (int j = 0; j < 30 && this.world.getBlockState(currentPos.below()).is(Blocks.AIR); j++) {
                     currentPos = currentPos.below();
                  }

                  if (NaturalSpawner.isSpawnPositionOk(Type.ON_GROUND, this.world, currentPos, EntityType.PLAYER)) {
                     int scanRadius = 10;
                     int totalAir = 0;

                     for (BlockState state : WorldUtil.getBlockStatesBetweenClosed(
                        this.world, currentPos.offset(-scanRadius, -scanRadius, -scanRadius), currentPos.offset(scanRadius, scanRadius, scanRadius)
                     )) {
                        if (state.isAir()) {
                           totalAir++;
                        }
                     }

                     float bias = (float)totalAir / 8000.0F;
                     if (bias <= 0.2F) {
                        pos = currentPos;
                        flag = false;
                        break label73;
                     }
                  }
               }
            }
         }

         if (flag) {
            Rotation rotation = ((StructurePiece)instance.getStart().getPieces().get(0)).getRotation();
            BlockPos offset = BlockPos.ZERO.relative(Axis.X, -112).rotate(rotation);
            pos = pos.above(120);
            pos = pos.offset(offset);
            BlockState statex = this.world.getBlockState(pos);

            while (statex.is(Blocks.AIR) && pos.getY() > 0) {
               BlockPos below = pos.below();
               statex = this.world.getBlockState(below);
               pos = below;
            }

            pos = pos.above();
         }
      } else {
         LOGGER.error("Structure start is null: {}", instance);
      }

      return pos;
   }

   public void setCompleted(WitherStormEntity entity, boolean completed) {
      WitherStormBowelsManager.BowelsInstance instance = this.get(entity.getUUID());
      if (instance != null) {
         instance.setCompleted(completed);
      }
   }

   private void clean() {
      LOGGER.debug("Cleaning instances...");
      int successfullyCleaned = 0;

      for (WitherStormBowelsManager.BowelsInstance instance : this.instances) {
         WitherStormEntity storm = this.findStorm(instance.witherStorm);
         if (!instance.isCompleted()) {
            instance.setCompleted(storm == null || !storm.isAlive());
            if (instance.isCompleted()) {
               if (instance.getStart() != null) {
                  instance.forceChunks(this.world, instance.getCenter(), false);
                  successfullyCleaned++;
               } else {
                  LOGGER.error("Bowels instance start is null! Cannot properly clean instance: {}", instance);
               }
            }
         }
      }

      LOGGER.debug("Finished cleaning; successfully cleaned {} instances", successfullyCleaned);
   }

   @Nullable
   public WitherStormBowelsManager.BowelsInstance getInstanceFromEntity(Entity entity) {
      double distance = -1.0;
      WitherStormBowelsManager.BowelsInstance finalInstance = null;

      for (WitherStormBowelsManager.BowelsInstance instance : this.instances) {
         double d0 = entity.blockPosition().distSqr(instance.getPos());
         if (distance == -1.0 || d0 < distance) {
            distance = d0;
            finalInstance = instance;
         }
      }

      return finalInstance;
   }

   public void prepareArena(WitherStormBowelsManager.BowelsInstance instance, WitherStormEntity storm) {
      if (instance.getStart() != null) {
         Rotation rotation = ((StructurePiece)instance.getStart().getPieces().get(0)).getRotation();
         BlockPos pos = instance.getCenter().above(110);
         BlockPos offset = BlockPos.ZERO.relative(Axis.X, -3).rotate(rotation);
         pos = pos.offset(offset);
         if (!instance.hasPreparedArena()) {
            ((ConfiguredFeature)WitherStormModFeatures.getConfiguredFeature(this.world, WitherStormModFeatures.BOWELS_PODIUM_FEATURE.getId()).value())
               .place(this.world, this.world.getChunkSource().getGenerator(), RandomSource.create(), pos);
            int amount = 6 + this.random.nextInt(6);

            for (int i = 0; i < amount; i++) {
               this.trySpawnTentacle(50, pos);
            }

            this.spawnHeads(rotation, instance.getCenter());
            instance.setPreparedArena();
            LOGGER.debug("Prepared bowels arena for entity {}", storm);
         }

         if (instance.commandBlock == null) {
            CommandBlockEntity entity = this.createCommandBlock(pos, rotation, storm);
            instance.commandBlock = entity.getUUID();
            this.world.addFreshEntity(entity);
         }
      } else {
         LOGGER.error("Structure start is null: {}", instance);
      }
   }

   private void trySpawnTentacle(int diameter, Vec3i center) {
      BlockPos pos = null;

      for (int i = 0; i < 10; i++) {
         int x = center.getX() + this.random.nextInt(diameter) - diameter / 2;
         int z = center.getZ() + this.random.nextInt(diameter) - diameter / 2;
         int y = center.getY();
         BlockPos currentPos = new BlockPos(x, y, z);

         for (int j = 0; j < 10 && this.world.getBlockState(currentPos.below()).is(Blocks.AIR); j++) {
            currentPos = currentPos.below();
         }

         List<TentacleEntity> nearbyTentacles = this.world.getEntitiesOfClass(TentacleEntity.class, new AABB(currentPos).inflate(10.0));
         if (NaturalSpawner.isSpawnPositionOk(Type.ON_GROUND, this.world, currentPos, WitherStormModEntityTypes.TENTACLE.get())
            && nearbyTentacles.isEmpty()
            && Math.sqrt(currentPos.distSqr(center)) > 10.0) {
            pos = currentPos;
            break;
         }
      }

      if (pos != null) {
         TentacleEntity tentacle = (TentacleEntity)(WitherStormModEntityTypes.TENTACLE.get())
            .create(this.world, null, null, pos, MobSpawnType.EVENT, false, false);
         if (tentacle != null && this.hasEnoughSpace(tentacle, pos)) {
            tentacle.setDormant(true);
            tentacle.lerpCurlTo(0.1F, 0.05F * (float)tentacle.getRandom().nextGaussian(), 8);
            this.world.addFreshEntityWithPassengers(tentacle);
         }
      }
   }

   private boolean hasEnoughSpace(Entity entity, BlockPos spawnPos) {
      for (BlockPos pos : BlockPos.betweenClosed(spawnPos, spawnPos.offset(1, 8, 1))) {
         if (!this.world.getBlockState(pos).getCollisionShape(this.world, pos).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private void spawnHeads(Rotation rotation, BlockPos structurePos) {
      BlockPos[] offsets = new BlockPos[]{NORTH_HEAD_POS, SOUTH_HEAD_POS};

      for (int i = 0; i < 2; i++) {
         BlockPos offset = offsets[i];
         offset = offset.rotate(rotation);
         BlockPos pos = structurePos.offset(offset);
         WitherStormHeadEntity head = (WitherStormHeadEntity)(WitherStormModEntityTypes.WITHER_STORM_HEAD.get())
            .create(this.world, null, null, pos, MobSpawnType.EVENT, false, false);
         float rot = rotate(rotation) + (i == 0 ? 180.0F : 0.0F);
         head.setYRot(rot);
         head.setXRot(60.0F);
         head.setYBodyRot(head.getYRot());
         head.setYHeadRot(head.getYRot());
         head.setActive(false);
         this.world.addFreshEntity(head);
      }
   }

   private CommandBlockEntity createCommandBlock(BlockPos pos, Rotation rotation, WitherStormEntity storm) {
      CommandBlockEntity entity = (CommandBlockEntity)(WitherStormModEntityTypes.COMMAND_BLOCK.get()).create(this.world);
      entity.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
      entity.setState(CommandBlockEntity.State.BOSSFIGHT);
      entity.setMode(CommandBlockEntity.Mode.TENTACLES);
      entity.setOwner(storm);
      entity.setOwnerUUID(storm.getUUID());
      entity.setYRot(rotate(rotation) + 90.0F);
      entity.setYBodyRot(entity.getYRot());
      entity.setYHeadRot(entity.getYRot());
      return entity;
   }

   private static float rotate(Rotation rotation) {
      switch (rotation) {
         case CLOCKWISE_180:
            return 180.0F;
         case COUNTERCLOCKWISE_90:
            return 270.0F;
         case CLOCKWISE_90:
            return 90.0F;
         default:
            return 0.0F;
      }
   }

   private void resetEmptyTimeIfNeeded() {
      for (WitherStormBowelsManager.BowelsInstance instance : this.instances) {
         if (!instance.isCompleted()) {
            this.world.resetEmptyTime();
            break;
         }
      }
   }

   public static WitherStormBowelsManager.BowelsEnterStatus enter(ServerLevel world, WitherStormEntity storm, Entity entity) {
      WitherStormBowelsManager.BowelsEnterStatus flag = WitherStormBowelsManager.BowelsEnterStatus.ENTITY_CANNOT_CHANGE;
      if (entity.isAddedToWorld() && entity.canChangeDimensions() && !entity.isPassenger() && !entity.isVehicle()) {
         ServerLevel bowels = WitherStormMod.bowels(world);
         WitherStormBowelsManager manager = (WitherStormBowelsManager)bowels.getCapability(WitherStormModCapabilities.BOWELS_MANAGER).orElse(null);
         if (manager != null) {
            if (storm.isAlive()) {
               WitherStormBowelsManager.BowelsInstance instance = manager.getOrCreateInstanceFor(storm);
               if (instance != null) {
                  manager.add(instance);
                  instance.setup(bowels);
                  manager.prepareArena(instance, storm);
                  BlockPos pos = manager.calculateEntrancePos(instance);
                  entity.changeDimension(bowels, new BowelsTeleporter(pos));
                  LOGGER.info("{} is entering the bowels of {}", entity, storm);
                  flag = WitherStormBowelsManager.BowelsEnterStatus.SUCCESS;
               } else {
                  flag = WitherStormBowelsManager.BowelsEnterStatus.CANT_SETUP_BOWELS;
                  LOGGER.error("Failed to setup bowels! It's likely the mod could not locate an available bowels arena or failed to do so.");
               }
            }

            manager.clean();
         }
      }

      return flag;
   }

   public static void leave(ServerLevel world, Entity entity, @Nullable WitherStormBowelsManager.BowelsInstance instance) {
      if (entity.isAddedToWorld() && entity.canChangeDimensions() && !entity.isPassenger() && !entity.isVehicle()) {
         world.getCapability(WitherStormModCapabilities.BOWELS_MANAGER)
            .ifPresent(
               manager -> {
                  WitherStormBowelsManager.BowelsInstance newInstance = instance;
                  if (instance == null) {
                     newInstance = manager.getInstanceFromEntity(entity);
                  }

                  if (newInstance != null) {
                     WitherStormEntity storm = manager.findStorm(newInstance.witherStorm);
                     if (storm != null) {
                        BlockPos pos = storm.blockPosition().below(5);
                        entity.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA).ifPresent(data -> data.makeInvulnerable(2400));
                        if (entity.changeDimension((ServerLevel)storm.level(), new BowelsTeleporter(pos)) instanceof LivingEntity living
                           && (Boolean)WitherStormModConfig.SERVER.bowelsFallResistance.get()) {
                           living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 255, false, false, false));
                        }
                     } else {
                        ServerLevel overworld = world.getServer().getLevel(Level.OVERWORLD);
                        entity.changeDimension(overworld, new BowelsTeleporter(overworld.getSharedSpawnPos()));
                     }
                  } else {
                     ServerLevel overworld = world.getServer().getLevel(Level.OVERWORLD);
                     entity.changeDimension(overworld, new BowelsTeleporter(overworld.getSharedSpawnPos()));
                  }
               }
            );
      }
   }

   public static void queueEnter(ServerPlayer player, WitherStormEntity storm) {
      if (!player.level().dimension().location().equals(WitherStormMod.bowelsLocation()) && player.canChangeDimensions() && !player.isPassenger() && !player.isVehicle()) {
         ServerLevel bowels = WitherStormMod.bowels(player.serverLevel());
         bowels.getCapability(WitherStormModCapabilities.BOWELS_MANAGER).ifPresent(manager -> manager.queuedEnter.putIfAbsent(storm.getUUID(), player));
      }
   }

   public static void queueLeave(Entity entity) {
      queueLeave(entity, () -> {
      });
   }

   public static void queueLeave(Entity entity, Runnable action) {
      if (entity.level().dimension().location().equals(WitherStormMod.bowelsLocation()) && entity.canChangeDimensions() && !entity.isPassenger() && !entity.isVehicle()) {
         ServerLevel bowels = WitherStormMod.bowels((ServerLevel)entity.level());
         bowels.getCapability(WitherStormModCapabilities.BOWELS_MANAGER).ifPresent(manager -> manager.queuedLeave.putIfAbsent(entity, action));
      }
   }

   public static Holder<Structure> getBowels(ServerLevel level) {
      return level.registryAccess().registryOrThrow(Registries.STRUCTURE).getHolderOrThrow(ResourceKey.create(Registries.STRUCTURE, WitherStormModStructures.BOWELS.getId()));
   }

   @Nullable
   private static StructureStart getAvailableStartAt(ServerLevel world, BlockPos pos) {
      ChunkAccess chunk = world.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.STRUCTURE_STARTS);
      StructureStart start = world.structureManager().getStartForStructure(SectionPos.bottomOf(chunk), (Structure)getBowels(world).value(), chunk);
      return start != null && start.isValid() ? start : null;
   }

   @SubscribeEvent
   public static void onDimensionLoad(Load event) {
      Level level = (Level)event.getLevel();
      if (!level.isClientSide() && level.dimension().location().equals(WitherStormMod.bowelsLocation())) {
         level.getCapability(WitherStormModCapabilities.BOWELS_MANAGER).ifPresent(manager -> manager.onLoad());
      }
   }

   @SubscribeEvent
   public static void onWorldTick(LevelTickEvent event) {
      if (event.level instanceof ServerLevel world && event.phase == Phase.START && world.dimension().location().equals(WitherStormMod.bowelsLocation())) {
         world.getCapability(WitherStormModCapabilities.BOWELS_MANAGER).ifPresent(manager -> {
            manager.queuedEnter.forEach((uuid, player) -> {
               WitherStormEntity entityx = manager.findStorm(uuid);
               if (entityx != null) {
                  enter(world, entityx, player);
               }
            });
            manager.queuedEnter.clear();
            manager.queuedLeave.forEach((player, action) -> {
               leave(world, player, null);
               action.run();
            });
            manager.queuedLeave.clear();
            manager.resetEmptyTimeIfNeeded();
         });

         for (Entity entity : world.getAllEntities()) {
            if (entity != null && entity.getY() < 50.0) {
               queueLeave(entity);
            }
         }
      }
   }

   public static enum BowelsEnterStatus {
      SUCCESS,
      CANT_SETUP_BOWELS,
      ENTITY_CANNOT_CHANGE;
   }

   public static class BowelsInstance {
      private static final TicketType<ChunkPos> BOWELS = TicketType.create("witherstormmod:bowels", Comparator.comparingLong(ChunkPos::toLong));
      @Nullable
      private Pair<BlockPos, StructureStart> start;
      private final UUID witherStorm;
      @Nullable
      private UUID commandBlock;
      private boolean hasPreparedArena;
      private boolean isCompleted;

      public BowelsInstance(Pair<BlockPos, StructureStart> start, UUID storm) {
         this.start = start;
         this.witherStorm = storm;
      }

      public BowelsInstance(BlockPos pos, UUID storm) {
         this.start = Pair.of(pos, null);
         this.witherStorm = storm;
      }

      public void setup(ServerLevel world) {
         if (!world.dimension().location().equals(WitherStormMod.bowelsLocation())) {
            WitherStormBowelsManager.LOGGER.error("Cannot setup {} in {}", this, world.dimension());
         } else {
            if (this.getStart() == null) {
               StructureStart start = WitherStormBowelsManager.getAvailableStartAt(world, this.getPos());
               if (start == null) {
                  WitherStormBowelsManager.LOGGER
                     .error(
                        "Could not find saved structure start or start is not valid from {}. It is important that these values aren't modified!", this.getPos()
                     );
                  return;
               }

               this.start = Pair.of(this.getPos(), start);
            }

            WitherStormBowelsManager.LOGGER.debug("Successfully setup this bowels instance {}", this);
         }
      }

      public void doChunkLoading(ServerLevel level) {
         if (!this.isCompleted()) {
            this.forceChunks(level, this.getCenter(), true);
         } else {
            this.forceChunks(level, this.getCenter(), false);
         }
      }

      private void forceChunks(ServerLevel world, BlockPos pos, boolean force) {
         ChunkPos chunkPos = new ChunkPos(pos);
         if (force) {
            WitherStormBowelsManager.LOGGER.debug("Loaded chunks in arena");
            world.getChunkSource().addRegionTicket(BOWELS, chunkPos, 3, chunkPos, true);
         } else {
            WitherStormBowelsManager.LOGGER.debug("Unloaded chunks in arena");
            world.getChunkSource().removeRegionTicket(BOWELS, chunkPos, 3, chunkPos);
         }
      }

      public CompoundTag write() {
         CompoundTag compound = new CompoundTag();
         compound.put("Pos", NbtUtils.writeBlockPos(this.getPos()));
         compound.putUUID("Storm", this.witherStorm);
         if (this.commandBlock != null) {
            compound.putUUID("CommandBlock", this.commandBlock);
         }

         compound.putBoolean("HasPreparedArena", this.hasPreparedArena);
         compound.putBoolean("Completed", this.isCompleted);
         return compound;
      }

      public static WitherStormBowelsManager.BowelsInstance read(CompoundTag compound) {
         BlockPos pos = NbtUtils.readBlockPos(compound.getCompound("Pos"));
         UUID storm = compound.getUUID("Storm");
         UUID commandBlock = null;
         if (compound.contains("CommandBlock")) {
            commandBlock = compound.getUUID("CommandBlock");
         }

         WitherStormBowelsManager.BowelsInstance instance = new WitherStormBowelsManager.BowelsInstance(pos, storm);
         instance.commandBlock = commandBlock;
         instance.hasPreparedArena = compound.getBoolean("HasPreparedArena");
         instance.isCompleted = compound.getBoolean("Completed");
         return instance;
      }

      public void setCompleted(boolean completed) {
         this.isCompleted = completed;
      }

      public boolean isCompleted() {
         return this.isCompleted;
      }

      public void setPreparedArena() {
         this.hasPreparedArena = true;
      }

      public boolean hasPreparedArena() {
         return this.hasPreparedArena;
      }

      public UUID getCommandBlockUUID() {
         return this.commandBlock;
      }

      public BlockPos getPos() {
         return (BlockPos)this.start.getFirst();
      }

      @Nullable
      public BlockPos getCenter() {
         StructureStart start = this.getStart();
         if (start != null) {
            BlockPos center = ((StructurePiece)start.getPieces().get(0)).getBoundingBox().getCenter();
            return new BlockPos(center.getX(), 0, center.getZ());
         } else {
            return null;
         }
      }

      @Nullable
      public StructureStart getStart() {
         return (StructureStart)this.start.getSecond();
      }

      @Override
      public String toString() {
         return "BowelsInstance[start = "
            + this.start
            + ", pos = "
            + this.getPos()
            + ", storm = "
            + this.witherStorm
            + ", commandBlock = "
            + this.commandBlock
            + ", hasPreparedArena = "
            + this.hasPreparedArena
            + ", completed = "
            + this.isCompleted
            + "]";
      }
   }
}
