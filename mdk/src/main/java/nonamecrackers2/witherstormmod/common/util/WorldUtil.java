package nonamecrackers2.witherstormmod.common.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.capability.PlayerWitherStormData;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;
import nonamecrackers2.witherstormmod.mixin.MixinFallingBlock;
import nonamecrackers2.witherstormmod.mixin.MixinPointedDripstoneBlock;

public class WorldUtil {
   public static <T extends Entity> List<T> getPerformantEntitiesOfClass(ServerLevel world, Class<T> clazz, AABB box, @Nullable Predicate<? super T> predicate) {
      return world.getEntitiesOfClass(clazz, box, predicate);
   }

   public static <T extends Entity> List<T> getPerformantEntitiesOfClass(ServerLevel world, Class<T> clazz, AABB box) {
      return getPerformantEntitiesOfClass(world, clazz, box, EntitySelector.NO_SPECTATORS);
   }

   public static List<BlockEntity> getBlockEntitiesInAABB(Level world, AABB box) {
      List<BlockEntity> blockEntities = new ArrayList<>();
      ChunkSource source = world.getChunkSource();
      int minChunkX = Mth.floor(box.minX / 16.0);
      int minChunkZ = Mth.floor(box.minZ / 16.0);
      int maxChunkX = Mth.ceil(box.maxX / 16.0);
      int maxChunkZ = Mth.ceil(box.maxZ / 16.0);

      for (int x = minChunkX; x < maxChunkX; x++) {
         for (int z = minChunkZ; z < maxChunkZ; z++) {
            ChunkAccess chunk = source.getChunk(x, z, false);
            if (chunk != null) {
               for (BlockPos pos : chunk.getBlockEntitiesPos()) {
                  if (box.contains(Vec3.atBottomCenterOf(pos))) {
                     blockEntities.add(chunk.getBlockEntity(pos));
                  }
               }
            }
         }
      }

      return blockEntities;
   }

   @Nullable
   public static <T> T getNearest(List<T> objects, Vec3 center, Function<T, Vec3> toVec) {
      double distance = -1.0;
      T toReturn = null;

      for (T t : objects) {
         Vec3 vec = toVec.apply(t);
         double d0 = vec.distanceToSqr(center);
         if (distance == -1.0 || d0 < distance) {
            distance = d0;
            toReturn = t;
         }
      }

      return toReturn;
   }

   public static List<WitherStormEntity> getAllStorms(ServerLevel level) {
      return Lists.newArrayList(level.getAllEntities())
         .stream()
         .filter(e -> e instanceof WitherStormEntity)
         .collect(Collectors.mapping(e -> (WitherStormEntity)e, Collectors.toList()));
   }

   public static boolean checkForIntersect(AABB box, Vec3 start, Vec3 end) {
      double d0 = Double.MAX_VALUE;
      boolean flag = false;
      Optional<Vec3> optional = box.clip(start, end);
      if (optional.isPresent()) {
         double d1 = start.distanceToSqr(optional.get());
         if (d1 < d0) {
            flag = true;
         }
      }

      if (box.contains(start)) {
         flag = true;
      }

      return flag;
   }

   public static boolean areaLoaded(Level level, BlockPos center, int radius) {
      for (int x = -radius; x <= radius; x++) {
         for (int z = -radius; z <= radius; z++) {
            ChunkPos chunkPos = new ChunkPos(center);
            ChunkAccess chunk = level.getChunk(chunkPos.x + x, chunkPos.z + z, ChunkStatus.FULL, false);
            if (!(chunk instanceof LevelChunk)) {
               return false;
            }

            FullChunkStatus type = ((LevelChunk)chunk).getFullStatus();
            if (!type.isOrAfter(FullChunkStatus.BLOCK_TICKING)) {
               return false;
            }
         }
      }

      return true;
   }

   public static boolean isLoaded(ServerLevel level, BlockPos pos) {
      ChunkAccess chunk = level.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
      if (!(chunk instanceof LevelChunk)) {
         return false;
      } else {
         FullChunkStatus type = ((LevelChunk)chunk).getFullStatus();
         return !type.isOrAfter(FullChunkStatus.ENTITY_TICKING) ? false : level.getChunkSource().chunkMap.getDistanceManager().inEntityTickingRange(ChunkPos.asLong(pos));
      }
   }

   public static boolean isInAnOpenArea(Entity entity) {
      int radius = 5;
      Integer lowest = null;

      for (int x = -radius; x < radius; x++) {
         for (int z = -radius; z < radius; z++) {
            int height = entity.level().getHeight(Types.WORLD_SURFACE, entity.getBlockX() + x, entity.getBlockZ() + z);
            if (lowest == null || lowest > height) {
               lowest = height;
            }
         }
      }

      return entity.getY() >= (double)lowest.intValue() - 10.0;
   }

   public static boolean hasLineOfSight(Entity caster, Entity target) {
      return raycast(caster, target, 300.0).getType() == Type.MISS;
   }

   public static boolean canSeeOrIsNotInASmallArea(Entity entity, Entity target) {
      PlayerWitherStormData data = (PlayerWitherStormData)target.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA).orElse(null);
      return data != null ? data.isInAnOpenArea() || hasLineOfSight(entity, target) : isInAnOpenArea(target) || hasLineOfSight(entity, target);
   }

   public static List<BlockState> getBlockStatesBetweenClosed(Level level, BlockPos min, BlockPos max) {
      List<BlockState> states = Lists.newArrayList();

      for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
         states.add(level.getBlockState(pos));
      }

      return states;
   }

   public static int getHeightStartingAt(Level level, int height, int x, int z) {
      BlockPos pos = new BlockPos(x, height, z);
      LevelChunk chunk = level.getChunkAt(pos);

      while (pos.getY() > level.getMinBuildHeight() && chunk.getBlockState(pos).isAir()) {
         pos = pos.below();
      }

      return pos.getY();
   }

   public static int getCeilingStartingAt(Level level, int height, int x, int z) {
      BlockPos pos = new BlockPos(x, height, z);

      while (pos.getY() < level.getMaxBuildHeight() && level.getBlockState(pos).isAir()) {
         pos = pos.above();
      }

      return pos.getY();
   }

   public static BlockHitResult raycast(Entity caster, Entity entity, double maxDist) {
      Vec3 pos = caster.getEyePosition();
      Vec3 entityPos = entity.getEyePosition();
      Vec3 delta = entityPos.subtract(pos);
      double dist = Math.sqrt(delta.x * delta.x + delta.y * delta.y + delta.z * delta.z);
      delta = delta.scale(dist > maxDist ? maxDist / dist : 1.0);
      Vec3 end = pos.add(delta);
      return caster.level().clip(new ClipContext(pos, end, Block.COLLIDER, Fluid.NONE, caster));
   }

   @Nullable
   public static BlockPos forEachBlockSpiralOutwards(Level level, BlockPos starting, int radius, Predicate<BlockPos> consumer) {
      if (consumer.test(starting)) {
         return starting;
      } else {
         for (int i = 0; i <= radius; i++) {
            for (int x = -i; x <= i; x++) {
               for (int z = -i; z <= i; z++) {
                  BlockPos first = new BlockPos(x, i, z).offset(starting);
                  BlockPos second = new BlockPos(x, -i, z).offset(starting);
                  if (consumer.test(first)) {
                     return first;
                  }

                  if (consumer.test(second)) {
                     return second;
                  }
               }
            }

            for (int y = -(i - 1); y <= i - 1; y++) {
               for (int x = -i; x <= i; x++) {
                  BlockPos firstx = new BlockPos(x, y, i).offset(starting);
                  BlockPos secondx = new BlockPos(x, y, -i).offset(starting);
                  if (consumer.test(firstx)) {
                     return firstx;
                  }

                  if (consumer.test(secondx)) {
                     return secondx;
                  }
               }
            }

            for (int y = -(i - 1); y <= i - 1; y++) {
               for (int z = -(i - 1); z <= i - 1; z++) {
                  BlockPos firstxx = new BlockPos(i, y, z).offset(starting);
                  BlockPos secondxx = new BlockPos(-i, -y, z).offset(starting);
                  if (consumer.test(firstxx)) {
                     return firstxx;
                  }

                  if (consumer.test(secondxx)) {
                     return secondxx;
                  }
               }
            }
         }

         return null;
      }
   }

   @Nullable
   public static Pair<BlockPos, StructureStart> findNearestMapStructure(
      ServerLevel level, TagKey<Structure> tag, BlockPos pos, int radius, boolean addReference
   ) {
      if (!level.getServer().getWorldData().worldGenOptions().generateStructures()) {
         return null;
      } else {
         Optional<Named<Structure>> optional = level.registryAccess().registryOrThrow(Registries.STRUCTURE).getTag(tag);
         return optional.isEmpty() ? null : findNearestMapStructure(level, (HolderSet<Structure>)optional.get(), pos, radius, addReference);
      }
   }

   @Nullable
   private static Pair<BlockPos, StructureStart> findNearestMapStructure(
      ServerLevel level, HolderSet<Structure> set, BlockPos pos, int radius, boolean addReference
   ) {
      ChunkGeneratorStructureState chunkgeneratorstructurestate = level.getChunkSource().getGeneratorState();
      Map<StructurePlacement, Set<Holder<Structure>>> map = new Object2ObjectArrayMap();

      for (Holder<Structure> holder : set) {
         for (StructurePlacement structureplacement : chunkgeneratorstructurestate.getPlacementsForStructure(holder)) {
            map.computeIfAbsent(structureplacement, p -> new ObjectArraySet()).add(holder);
         }
      }

      if (map.isEmpty()) {
         return null;
      } else {
         Pair<BlockPos, StructureStart> pair2 = null;
         double d2 = Double.MAX_VALUE;
         StructureManager structuremanager = level.structureManager();
         List<Entry<StructurePlacement, Set<Holder<Structure>>>> list = new ArrayList<>(map.size());

         for (Entry<StructurePlacement, Set<Holder<Structure>>> entry : map.entrySet()) {
            StructurePlacement structureplacement1 = entry.getKey();
            if (structureplacement1 instanceof ConcentricRingsStructurePlacement) {
               ConcentricRingsStructurePlacement concentricringsstructureplacement = (ConcentricRingsStructurePlacement)structureplacement1;
               Pair<BlockPos, StructureStart> pair = getNearestGeneratedConcentricRingStructure(
                  entry.getValue(), level, structuremanager, pos, addReference, concentricringsstructureplacement
               );
               if (pair != null) {
                  BlockPos blockpos = (BlockPos)pair.getFirst();
                  double d0 = pos.distSqr(blockpos);
                  if (d0 < d2) {
                     d2 = d0;
                     pair2 = pair;
                  }
               }
            } else if (structureplacement1 instanceof RandomSpreadStructurePlacement) {
               list.add(entry);
            }
         }

         if (!list.isEmpty()) {
            int i = SectionPos.blockToSectionCoord(pos.getX());
            int j = SectionPos.blockToSectionCoord(pos.getZ());

            for (int k = 0; k <= radius; k++) {
               boolean flag = false;

               for (Entry<StructurePlacement, Set<Holder<Structure>>> entry1 : list) {
                  RandomSpreadStructurePlacement randomspreadstructureplacement = (RandomSpreadStructurePlacement)entry1.getKey();
                  Pair<BlockPos, StructureStart> pair1 = getNearestGeneratedRandomPlacementStructure(
                     entry1.getValue(),
                     level,
                     structuremanager,
                     i,
                     j,
                     k,
                     addReference,
                     chunkgeneratorstructurestate.getLevelSeed(),
                     randomspreadstructureplacement
                  );
                  if (pair1 != null) {
                     flag = true;
                     double d1 = pos.distSqr((Vec3i)pair1.getFirst());
                     if (d1 < d2) {
                        d2 = d1;
                        pair2 = pair1;
                     }
                  }
               }

               if (flag) {
                  return pair2;
               }
            }
         }

         return pair2;
      }
   }

   @Nullable
   private static Pair<BlockPos, StructureStart> getNearestGeneratedConcentricRingStructure(
      Set<Holder<Structure>> set, ServerLevel level, StructureManager manager, BlockPos pos, boolean addReference, ConcentricRingsStructurePlacement placement
   ) {
      List<ChunkPos> list = level.getChunkSource().getGeneratorState().getRingPositionsFor(placement);
      if (list == null) {
         throw new IllegalStateException("Somehow tried to find structures for a placement that doesn't exist");
      } else {
         Pair<BlockPos, StructureStart> pair = null;
         double d0 = Double.MAX_VALUE;
         MutableBlockPos blockpos$mutableblockpos = new MutableBlockPos();

         for (ChunkPos chunkpos : list) {
            blockpos$mutableblockpos.set(SectionPos.sectionToBlockCoord(chunkpos.x, 8), 32, SectionPos.sectionToBlockCoord(chunkpos.z, 8));
            double d1 = blockpos$mutableblockpos.distSqr(pos);
            boolean flag = pair == null || d1 < d0;
            if (flag) {
               Pair<BlockPos, StructureStart> pair1 = getStructureGeneratingAt(set, level, manager, addReference, placement, chunkpos);
               if (pair1 != null) {
                  pair = pair1;
                  d0 = d1;
               }
            }
         }

         return pair;
      }
   }

   @Nullable
   private static Pair<BlockPos, StructureStart> getNearestGeneratedRandomPlacementStructure(
      Set<Holder<Structure>> set,
      LevelReader level,
      StructureManager manager,
      int xSectionCoord,
      int zSectionCoord,
      int radius,
      boolean addReference,
      long seed,
      RandomSpreadStructurePlacement placement
   ) {
      int i = placement.spacing();

      for (int j = -radius; j <= radius; j++) {
         boolean flag = j == -radius || j == radius;

         for (int k = -radius; k <= radius; k++) {
            boolean flag1 = k == -radius || k == radius;
            if (flag || flag1) {
               int l = xSectionCoord + i * j;
               int i1 = zSectionCoord + i * k;
               ChunkPos chunkpos = placement.getPotentialStructureChunk(seed, l, i1);
               Pair<BlockPos, StructureStart> pair = getStructureGeneratingAt(set, level, manager, addReference, placement, chunkpos);
               if (pair != null) {
                  return pair;
               }
            }
         }
      }

      return null;
   }

   @Nullable
   private static Pair<BlockPos, StructureStart> getStructureGeneratingAt(
      Set<Holder<Structure>> set, LevelReader level, StructureManager manager, boolean addReference, StructurePlacement placement, ChunkPos pos
   ) {
      for (Holder<Structure> holder : set) {
         StructureCheckResult structurecheckresult = manager.checkStructurePresence(pos, (Structure)holder.value(), addReference);
         if (structurecheckresult != StructureCheckResult.START_NOT_PRESENT) {
            ChunkAccess chunkaccess = level.getChunk(pos.x, pos.z, ChunkStatus.STRUCTURE_STARTS);
            StructureStart structurestart = manager.getStartForStructure(SectionPos.bottomOf(chunkaccess), (Structure)holder.value(), chunkaccess);
            if (structurestart != null && structurestart.isValid() && (!addReference || tryAddReference(manager, structurestart))) {
               return Pair.of(placement.getLocatePos(structurestart.getChunkPos()), structurestart);
            }
         }
      }

      return null;
   }

   private static boolean tryAddReference(StructureManager manager, StructureStart start) {
      if (start.canBeReferenced()) {
         manager.addReference(start);
         return true;
      } else {
         return false;
      }
   }

   public static boolean isBlockExposed(Level level, BlockPos pos) {
      BlockState posState = level.getBlockState(pos);
      if (posState.hasProperty(BlockStateProperties.WATERLOGGED)) {
         return true;
      } else if (!level.isEmptyBlock(pos) && !level.isWaterAt(pos)) {
         for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.isEmptyBlock(neighborPos) || level.isWaterAt(neighborPos)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static void doCaveRumble(ServerLevel level, ServerPlayer player, double rumbleIntensity, RandomSource random) {
      int ceiling = getCeilingStartingAt(level, player.getBlockY(), player.getBlockX(), player.getBlockZ());
      int chance = (int)(10.0 * (1.0 - Math.sqrt(rumbleIntensity)));
      int caveVinesBerryDropChance = (int)(4.0 * (1.0 - Math.sqrt(rumbleIntensity)));
      int caveInChance = (int)(12.0 * (1.0 - Math.sqrt(rumbleIntensity)));
      int dripStoneLengthChance = (int)(6.0 * (1.0 - Math.sqrt(rumbleIntensity)));
      WitherStormModPacketHandlers.MAIN
         .send(
            PacketDistributor.PLAYER.with(() -> player),
            new ShakeScreenMessage(180.0F, (float)(12.0 * Math.sqrt((Double)WitherStormModConfig.SERVER.caveRumbleIntensity.get())))
         );
      player.playNotifySound(WitherStormModSoundEvents.EARTH_RUMBLE.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
      if (ceiling < level.getMaxBuildHeight()) {
         int radius = 64;

         for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
               if (chance < 1 || random.nextInt(chance) == 0) {
                  int finalX = player.getBlockX() + x;
                  int finalZ = player.getBlockZ() + z;
                  int y = getCeilingStartingAt(level, player.getBlockY() + 4, finalX, finalZ);
                  BlockPos pos = new BlockPos(finalX, y, finalZ);
                  BlockState state = level.getBlockState(pos);
                  boolean flag = true;

                  for (int i = 0; i < 20; i++) {
                     BlockState current = level.getBlockState(pos);
                     if (current.is(Blocks.POINTED_DRIPSTONE) && (dripStoneLengthChance < 1 || random.nextInt(dripStoneLengthChance) == 0)) {
                        if (!MixinPointedDripstoneBlock.callIsStalagmite(current)) {
                           MixinPointedDripstoneBlock.callSpawnFallingStalactite(current, level, pos);
                        }

                        flag = true;
                        break;
                     }

                     if (current.getBlock() instanceof CaveVines
                        && (caveVinesBerryDropChance < 1 || random.nextInt(caveVinesBerryDropChance) == 0)
                        && CaveVines.hasGlowBerries(current)) {
                        CaveVines.use(null, current, level, pos);
                        flag = true;
                     }

                     pos = pos.above();
                  }

                  if (flag) {
                     pos = pos.atY(y);
                     BlockState below = level.getBlockState(pos.below());
                     boolean canDropBlock = FallingBlock.isFree(below) && pos.getY() >= level.getMinBuildHeight();
                     net.minecraft.world.level.block.Block bellBlock = state.getBlock();
                     if (bellBlock instanceof FallingBlock) {
                        FallingBlock block = (FallingBlock)bellBlock;
                        if (!state.is(Blocks.POINTED_DRIPSTONE) && canDropBlock) {
                           FallingBlockEntity entity = FallingBlockEntity.fall(level, pos, state);
                           ((MixinFallingBlock)block).callFalling(entity);
                        }
                     }

                     if (!state.is(WitherStormModBlockTags.CAVE_IN_BLACKLIST)
                        && canDropBlock
                        && (caveInChance < 1 || random.nextInt(caveInChance) == 0)) {
                        double randomMomentum = 0.25 * random.nextDouble();
                        int rotationDelta = random.nextInt(257) - 128;
                        BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(level);
                        cluster.populateWithRadius(pos, 1.0F, blockstate -> !blockstate.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST));
                        cluster.setTime(50);
                        cluster.setShouldCrumble(false);
                        cluster.setRotationDelta(new Vec2((float)rotationDelta * 0.0625F, (float)rotationDelta * 0.0625F));
                        cluster.setNoGravity(false);
                        cluster.setDeltaMovement(0.0, randomMomentum, 0.0);
                        cluster.setPhysics(true);
                        level.addFreshEntity(cluster);
                     }

                     if (isBlockExposed(level, pos)) {
                        level.sendParticles(
                           new BlockParticleOption(ParticleTypes.FALLING_DUST, state),
                           (double)finalX + 0.5,
                           (double)y,
                           (double)finalZ + 0.5,
                           2,
                           0.5,
                           0.5,
                           0.5,
                           0.0
                        );
                        level.sendParticles(
                           new BlockParticleOption(ParticleTypes.BLOCK, state),
                           (double)finalX + 0.5,
                           (double)y,
                           (double)finalZ + 0.5,
                           4,
                           0.5,
                           0.5,
                           0.5,
                           1.0
                        );
                     }
                  }
               }
            }
         }
      }

      int radius = 16;

      for (int x = -radius; x < radius; x++) {
         for (int y = -radius; y < radius; y++) {
            for (int zx = -radius; zx < radius; zx++) {
               if (chance < 1 || random.nextInt(chance) == 0) {
                  int finalX = player.getBlockX() + x;
                  int finalY = player.getBlockY() + y;
                  int finalZ = player.getBlockZ() + zx;
                  BlockPos pos = new BlockPos(finalX, finalY, finalZ);
                  BlockState state = level.getBlockState(pos);
                  net.minecraft.world.level.block.Block block = state.getBlock();
                  if (block instanceof RedstoneLampBlock) {
                     if ((Boolean)WitherStormModConfig.SERVER.caveRumblesMessWithRedstone.get() && !(Boolean)state.getValue(RedstoneLampBlock.LIT)) {
                        level.setBlock(pos, (BlockState)state.setValue(RedstoneLampBlock.LIT, Boolean.TRUE), 2, 0);
                        level.scheduleTick(pos, block, 20 + random.nextInt(10));
                     }
                  } else if (block instanceof TrapDoorBlock) {
                     boolean isOpen = (Boolean)state.getValue(TrapDoorBlock.OPEN);
                     boolean isBottom = state.getValue(TrapDoorBlock.HALF) == Half.BOTTOM;
                     if (isOpen && isBottom) {
                        level.setBlockAndUpdate(pos, (BlockState)state.setValue(TrapDoorBlock.OPEN, Boolean.FALSE));
                     }

                     if (!isOpen && !isBottom) {
                        level.setBlockAndUpdate(pos, (BlockState)state.setValue(TrapDoorBlock.OPEN, Boolean.TRUE));
                     }
                  } else if (block instanceof RedStoneOreBlock) {
                     int randomized = random.nextInt(70);
                     level.setBlock(pos, (BlockState)state.setValue(RedStoneOreBlock.LIT, Boolean.TRUE), 2);
                     level.scheduleTick(pos, block, 10 + randomized);
                  } else if (block instanceof LeverBlock) {
                     if ((Boolean)WitherStormModConfig.SERVER.caveRumblesMessWithRedstone.get()) {
                        level.setBlockAndUpdate(pos, (BlockState)state.cycle(LeverBlock.POWERED));
                     }
                  } else if (block instanceof ButtonBlock) {
                     ButtonBlock buttonBlock = (ButtonBlock)block;
                     if ((Boolean)WitherStormModConfig.SERVER.caveRumblesMessWithRedstone.get()) {
                        buttonBlock.press(state, level, pos);
                     }
                  } else if (block instanceof PressurePlateBlock) {
                     if ((Boolean)WitherStormModConfig.SERVER.caveRumblesMessWithRedstone.get()) {
                        int randomized = random.nextInt(60);
                        level.setBlock(pos, (BlockState)state.setValue(PressurePlateBlock.POWERED, Boolean.TRUE), 2);
                        level.scheduleTick(pos, block, randomized);
                     }
                  } else if (block instanceof SculkSensorBlock sculkSensor) {
                     sculkSensor.stepOn(level, pos, state, player);
                  } else if (block instanceof NoteBlock) {
                     if ((Boolean)WitherStormModConfig.SERVER.caveRumblesMessWithRedstone.get()) {
                        level.setBlockAndUpdate(pos, (BlockState)state.cycle(NoteBlock.NOTE));
                     }
                  } else if (block instanceof BellBlock bellBlockx) {
                     bellBlockx.attemptToRing(level, pos, null);
                  }
               }
            }
         }
      }
   }

   public static List<Integer> getStormIds(WitherStormEntity entity) {
      return getAllStorms((ServerLevel)entity.level()).stream().collect(Collectors.mapping(Entity::getId, Collectors.toList()));
   }

   @Nullable
   public static Mob summonRandomMob(
      ServerLevel world, BlockPos start, RandomSource random, int diameter, SimpleWeightedRandomList<EntityType<? extends Mob>> types, boolean advancedMobs
   ) {
      EntityType<? extends Mob> type = (EntityType<? extends Mob>)types.getRandomValue(random).get();
      BlockPos pos = getRandomNearbyPos(world, start, random, type, diameter, 5);
      if (pos != null && hasEnoughSpace(world, type.getDimensions(), pos)) {
         Mob entity = (Mob)type.create(world);
         DifficultyInstance difficulty = world.getCurrentDifficultyAt(entity.blockPosition());
         ForgeEventFactory.onFinalizeSpawn(entity, world, difficulty, MobSpawnType.TRIGGERED, null, null);
         if (WitherSickened.CAN_WEAR_ARMOR.test(entity) && entity instanceof Monster monster) {
            EquipmentHelper.applyEquipment(monster, difficulty, advancedMobs);
         }

         entity.setPos((double)pos.getX() + 0.5, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5);
         entity.playAmbientSound();
         entity.spawnAnim();
         entity.setPersistenceRequired();
         world.sendParticles(
            WitherStormModParticleTypes.COMMAND_BLOCK.get(),
            entity.getX(),
            entity.getEyeY(),
            entity.getZ(),
            20,
            random.nextGaussian(),
            random.nextGaussian(),
            random.nextGaussian(),
            0.2
         );
         world.sendParticles(
            ParticleTypes.LARGE_SMOKE,
            entity.getX(),
            entity.getEyeY(),
            entity.getZ(),
            20,
            random.nextGaussian(),
            random.nextGaussian(),
            random.nextGaussian(),
            0.01
         );
         world.addFreshEntityWithPassengers(entity);
         return entity;
      } else {
         return null;
      }
   }

   @Nullable
   public static BlockPos getRandomNearbyPos(Level level, BlockPos start, RandomSource random, EntityType<?> type, int diameter, int attempts) {
      for (int i = 0; i < attempts; i++) {
         int x = start.getX() + random.nextInt(diameter) - diameter / 2;
         int z = start.getZ() + random.nextInt(diameter) - diameter / 2;
         int y = level.getHeightmapPos(Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z)).getY();
         BlockPos pos = new BlockPos(x, y, z);
         if (NaturalSpawner.isSpawnPositionOk(net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND, level, pos, type) && pos.distSqr(start) > 6.0) {
            return pos;
         }
      }

      return null;
   }

   private static boolean hasEnoughSpace(Level level, EntityDimensions dimensions, BlockPos spawnPos) {
      for (BlockPos pos : BlockPos.betweenClosed(
         spawnPos, spawnPos.offset(BlockPos.containing((double)dimensions.width, (double)dimensions.height, (double)dimensions.width))
      )) {
         if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) {
            return false;
         }
      }

      return true;
   }
}
