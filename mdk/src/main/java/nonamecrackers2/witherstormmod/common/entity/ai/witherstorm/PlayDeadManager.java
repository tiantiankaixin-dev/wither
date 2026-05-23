package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor.PacketTarget;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.WitherStormHead;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCriteriaTriggers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModFeatures;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.PlayAdditionalLoopingSoundMessage;
import nonamecrackers2.witherstormmod.common.packet.RemoveAdditionalLoopingSoundMessage;
import nonamecrackers2.witherstormmod.common.packet.RemoveSoundLoopMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdatePlayDeadManagerMessage;
import nonamecrackers2.witherstormmod.common.util.DebrisCluster;
import nonamecrackers2.witherstormmod.common.util.DebrisRingSettings;
import nonamecrackers2.witherstormmod.common.world.gen.feature.CommandBlockPodiumFeature;

public class PlayDeadManager {
   protected final WitherStormEntity entity;
   protected PlayDeadManager.State state = PlayDeadManager.State.NORMAL_BEHAVIOR;
   protected int ticksSinceInit;
   protected int ticksSinceInitO;
   protected final int updateInterval = 120;
   protected int totalTickCount;
   @Nullable
   protected FormidibombEntity formidibomb;
   @Nullable
   protected BlockPos podiumPos;
   protected boolean podiumPlaced;
   @Nullable
   protected CommandBlockEntity commandBlock;
   protected int revivalTicks;
   protected boolean hasRecentlyBeenRevived;
   protected int revivalPlayerProtection = (Integer)WitherStormModConfig.SERVER.revivalPlayerProtection.get();
   protected int timeSinceCommandBlockMissing;

   public PlayDeadManager(WitherStormEntity entity) {
      this.entity = entity;
   }

   public void tick() {
      Level world = this.entity.level();
      this.getState().tick(world, this.entity, this);
      this.totalTickCount++;
      if (this.totalTickCount % 120 == 0) {
         this.sendChanges(PacketDistributor.DIMENSION.with(() -> this.entity.level().dimension()), true);
      }

      if (this.revivalTicks > this.revivalPlayerProtection * 1200) {
         this.hasRecentlyBeenRevived = false;
      }
   }

   public void setState(PlayDeadManager.State state) {
      if (this.state != state) {
         this.state.finish(this.entity.level(), this.entity, this, state);
         this.state = state;
         this.state.init(this.entity.level(), this.entity, this);
         this.updateSegments();
         this.sendChanges(PacketDistributor.DIMENSION.with(() -> this.entity.level().dimension()), false);
      }
   }

   public void setStateRaw(PlayDeadManager.State state) {
      if (this.state != state) {
         this.state = state;
         this.sendChanges(PacketDistributor.DIMENSION.with(() -> this.entity.level().dimension()), false);
      }
   }

   public void nextState() {
      PlayDeadManager.State next = null;
      if (this.state.ordinal() + 1 < PlayDeadManager.State.values().length) {
         next = PlayDeadManager.State.values()[this.state.ordinal() + 1];
      }

      if (next != null) {
         this.state.finish(this.entity.level(), this.entity, this, next);
         this.state = next;
         this.state.init(this.entity.level(), this.entity, this);
         this.updateSegments();
         this.sendChanges(PacketDistributor.DIMENSION.with(() -> this.entity.level().dimension()), false);
      }
   }

   public void updateSegments() {
      this.entity.getSegmentsManager().ifPresent(segments -> {
         for (WitherStormSegmentEntity segment : segments.getSegments()) {
            if (segment != null) {
               segment.getPlayDeadManager().setState(this.getState());
            }
         }
      });
   }

   public PlayDeadManager.State getState() {
      return this.state;
   }

   public void explode() {
      this.setState(PlayDeadManager.State.FALLING);
      if (!this.entity.level().isClientSide) {
         for (Player player : this.entity.level().getNearbyPlayers(TargetingConditions.forNonCombat(), null, this.entity.getSearchBox().inflate(100.0))) {
            WitherStormModCriteriaTriggers.PLAY_DEAD_TRIGGER.trigger((ServerPlayer)player, this.entity);
         }
      }
   }

   public void revive() {
      this.setState(PlayDeadManager.State.REVIVING);
      if (!this.entity.level().isClientSide) {
         for (Player player : this.entity.level().getNearbyPlayers(TargetingConditions.forNonCombat(), null, this.entity.getSearchBox().inflate(100.0))) {
            WitherStormModCriteriaTriggers.REVIVAL_TRIGGER.trigger((ServerPlayer)player, this.entity);
         }
      }
   }

   public void sendChanges(PacketTarget target, boolean updateTick) {
      if (!this.entity.level().isClientSide) {
         UpdatePlayDeadManagerMessage message = new UpdatePlayDeadManagerMessage(this.entity.getId(), this, updateTick);
         WitherStormModPacketHandlers.MAIN.send(target, message);
         this.getState().sendAdditionalPackets(this.entity.level(), this.entity, this);
      }
   }

   public int getTicks() {
      return this.ticksSinceInit;
   }

   public void setTickAmount(int amount) {
      this.ticksSinceInit = amount;
   }

   public void setTickAmountAndO(int amount) {
      this.ticksSinceInit = amount;
      this.ticksSinceInitO = amount;
   }

   public void setFormidibomb(@Nullable FormidibombEntity entity) {
      this.formidibomb = entity;
   }

   @Nullable
   public FormidibombEntity getFormidibomb() {
      return this.formidibomb;
   }

   @Nullable
   public BlockPos getPodiumPos() {
      return this.podiumPos;
   }

   public void setPodiumPos(@Nullable BlockPos pos) {
      this.podiumPos = pos;
   }

   public boolean isPodiumPlaced() {
      return this.podiumPlaced;
   }

   public void setPodiumPlaced(boolean placed) {
      this.podiumPlaced = placed;
   }

   public boolean isPodiumAreaLoaded(BlockPos pos) {
      for (int x = -3; x <= 3; x++) {
         for (int z = -3; z <= 3; z++) {
            ChunkPos chunkPos = new ChunkPos(pos);
            ChunkAccess chunk = this.entity.level().getChunk(chunkPos.x + x, chunkPos.z + z, ChunkStatus.FULL, false);
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

   public void placePodium() {
      if (!this.entity.level().isClientSide) {
         float yBodyRot = (this.entity.yBodyRot - 90.0F) * (float) (Math.PI / 180.0);
         float x = Mth.cos(yBodyRot) * 5.0F;
         float z = Mth.sin(yBodyRot) * 5.0F;
         BlockPos pos = this.entity.blockPosition().offset((int)x, -4, (int)z);
         if (!this.isPodiumPlaced() && this.isPodiumAreaLoaded(pos)) {
            ServerLevel world = (ServerLevel)this.entity.level();
            if (((ConfiguredFeature)WitherStormModFeatures.getConfiguredFeature(world, WitherStormModFeatures.COMMAND_BLOCK_PODIUM_FEATURE.getId()).value())
               .place(world, world.getChunkSource().getGenerator(), this.entity.getRandom(), pos)) {
               this.setPodiumPos(pos);
               this.setPodiumPlaced(true);
               if (this.getCommandBlock() == null || !this.getCommandBlock().isAlive()) {
                  CommandBlockEntity entity = new CommandBlockEntity(
                     world, this.entity, (double)pos.getX() + 0.5, (double)pos.getY() + 11.0, (double)pos.getZ() + 0.5
                  );
                  this.setCommandBlock(entity);
                  world.addFreshEntity(entity);
               }
            }
         }
      }
   }

   public void removePodium() {
      if (!this.entity.level().isClientSide) {
         BlockPos pos = this.getPodiumPos();
         if (pos != null && this.isPodiumPlaced() && this.isPodiumAreaLoaded(pos)) {
            ServerLevel world = (ServerLevel)this.entity.level();
            ConfiguredFeature<NoneFeatureConfiguration, CommandBlockPodiumFeature> feature = (ConfiguredFeature<NoneFeatureConfiguration, CommandBlockPodiumFeature>)WitherStormModFeatures.getConfiguredFeature(
                  world, WitherStormModFeatures.COMMAND_BLOCK_PODIUM_FEATURE.getId()
               )
               .value();
            if (((CommandBlockPodiumFeature)feature.feature())
               .remove(world, world.getChunkSource().getGenerator(), this.entity.getRandom(), pos, (NoneFeatureConfiguration)feature.config())) {
               this.setPodiumPos(null);
               this.setPodiumPlaced(false);
            }

            if (this.getCommandBlock() != null) {
               CommandBlockEntity entity = this.getCommandBlock();
               entity.discard();
               this.setCommandBlock(null);
            }
         }
      }
   }

   @Nullable
   public CommandBlockEntity getCommandBlock() {
      return this.commandBlock;
   }

   public void setCommandBlock(@Nullable CommandBlockEntity entity) {
      this.commandBlock = entity;
   }

   public int getTicksSinceRevival() {
      return this.revivalTicks;
   }

   public void setTicksSinceRevival(int ticks) {
      this.revivalTicks = ticks;
   }

   public void setRecentlyRevived(boolean flag) {
      this.hasRecentlyBeenRevived = flag;
   }

   public boolean hasRecentlyBeenRevived() {
      return this.hasRecentlyBeenRevived;
   }

   public int getRevivalPlayerProtectionTime() {
      return this.revivalPlayerProtection;
   }

   public void setRevivalPlayerProtectionTime(int time) {
      this.revivalPlayerProtection = time;
   }

   public int getTicksSinceCommandBlockMissing() {
      return this.timeSinceCommandBlockMissing;
   }

   public void setTicksSinceCommandBlockMissing(int time) {
      this.timeSinceCommandBlockMissing = time;
   }

   public static enum State {
      NORMAL_BEHAVIOR {
         @Override
         public void tick(Level world, WitherStormEntity entity, PlayDeadManager manager) {
            super.tick(world, entity, manager);
            float percentage = (float)Math.min(manager.ticksSinceInit, 80) / 80.0F;

            for (DebrisRingSettings settings : entity.getDebrisRings()) {
               settings.setAlpha(percentage);
            }

            entity.setShineAlpha(percentage);
         }

         @Override
         public void finish(Level world, WitherStormEntity entity, PlayDeadManager manager, PlayDeadManager.State next) {
            for (Mob mob : world.getEntitiesOfClass(Mob.class, entity.getSearchBox())) {
               if (mob.getTarget() == entity) {
                  mob.setTarget(null);
               }
            }
         }
      },
      FALLING {
         @Override
         public void tick(Level world, WitherStormEntity entity, PlayDeadManager manager) {
            super.tick(world, entity, manager);
            if (!world.isClientSide) {
               if (manager.getTicks() % 8 == 0 && !(entity instanceof WitherStormSegmentEntity)) {
                  for (int i = 0; i < 3; i++) {
                     entity.dropSmallMassCluster(1);
                  }
               }

               if (entity.getSegmentsManager().isPresent() && manager.getTicks() == 201) {
                  SegmentsManager segmentsManager = entity.getSegmentsManager().get();
                  if (entity.getPhase() == 5) {
                     entity.setPhase(6);
                     entity.playSoundToEveryone(WitherStormModSoundEvents.WITHER_STORM_SPLITS.get(), 1.0F, 1.0F);
                     entity.setOtherHeadsDisabled(true);
                     segmentsManager.createSegments();
                     segmentsManager.addSegments();
                  }
               }
            }

            if (!entity.onGround()) {
               float xOffset = (entity.getRandom().nextFloat() - 0.5F) * (float)entity.getBoundingBox().getYsize();
               float yOffset = (entity.getRandom().nextFloat() - 0.5F) * (float)entity.getBoundingBox().getYsize();
               float zOffset = (entity.getRandom().nextFloat() - 0.5F) * (float)entity.getBoundingBox().getYsize();
               world.addParticle(
                  ParticleTypes.EXPLOSION,
                  entity.getX() + (double)xOffset,
                  entity.getEyeY() + (double)yOffset,
                  entity.getZ() + (double)zOffset,
                  -5.0,
                  0.0,
                  0.0
               );
            }

            float percentage = (120.0F - (float)Math.min(manager.ticksSinceInit, 120)) / 120.0F;

            for (DebrisRingSettings settings : entity.getDebrisRings()) {
               settings.setAlpha(percentage);
            }

            entity.setShineAlpha(percentage);
         }

         @Override
         public void init(Level world, WitherStormEntity entity, PlayDeadManager manager) {
            super.init(world, entity, manager);
            if (!world.isClientSide) {
               for (WitherStormHead head : entity.getHeadManager().getHeads()) {
                  head.doRoar(entity.getRandom().nextBoolean());
               }

               if (entity.shouldPlaySoundLoop) {
                  PlayAdditionalLoopingSoundMessage message = new PlayAdditionalLoopingSoundMessage(
                     entity, WitherStormModSoundEvents.WITHER_STORM_TREMBLE.get()
                  );
                  WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
               }
            }

            for (WitherStormHead head : entity.getHeadManager().getHeads()) {
               head.lerpHeadTo(-50.0F, entity.yBodyRot, 64.0F);
            }
         }

         @Override
         public void finish(Level world, WitherStormEntity entity, PlayDeadManager manager, PlayDeadManager.State next) {
            super.finish(world, entity, manager, next);
            if (!world.isClientSide && entity.shouldPlaySoundLoop) {
               RemoveAdditionalLoopingSoundMessage message = new RemoveAdditionalLoopingSoundMessage(entity);
               WitherStormModPacketHandlers.MAIN.send(PacketDistributor.DIMENSION.with(() -> world.dimension()), message);
            }
         }

         @Override
         public boolean disablesAi() {
            return true;
         }

         @Override
         public boolean isPastInterval(Level world, WitherStormEntity entity, PlayDeadManager manager) {
            return entity.onGround();
         }

         @Override
         public void sendAdditionalPackets(Level world, WitherStormEntity entity, PlayDeadManager manager) {
            super.sendAdditionalPackets(world, entity, manager);
            if (entity.shouldPlaySoundLoop) {
               PlayAdditionalLoopingSoundMessage message = new PlayAdditionalLoopingSoundMessage(
                  entity, WitherStormModSoundEvents.WITHER_STORM_TREMBLE.get()
               );
               WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
            }
         }
      },
      PLAYING_DEAD {
         @Override
         public void tick(Level world, WitherStormEntity entity, PlayDeadManager manager) {
            super.tick(world, entity, manager);
            if (entity.isOnBack() && entity.isAlive() && !entity.isDeadOrDying()) {
               manager.placePodium();
               BlockPos pos = manager.getPodiumPos();
               if (pos != null) {
                  entity.spawnConsumedPets(new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 12.0, (double)pos.getZ() + 0.5));
               }
            }

            for (DebrisRingSettings settings : entity.getDebrisRings()) {
               settings.setAlpha(0.0F);
            }

            entity.setShineAlpha(0.0F);
            if (!world.isClientSide) {
               CommandBlockEntity commandBlock = manager.getCommandBlock();
               if (commandBlock != null && (!commandBlock.isAlive() || (double)commandBlock.distanceTo(entity) > 64.0)) {
                  manager.timeSinceCommandBlockMissing++;
               }
            }
         }

         @Override
         public boolean isPastInterval(Level world, WitherStormEntity entity, PlayDeadManager manager) {
            return manager.timeSinceCommandBlockMissing > 200;
         }

         @Override
         public boolean disablesAi() {
            return true;
         }

         @Override
         public void init(Level world, WitherStormEntity entity, PlayDeadManager manager) {
            super.init(world, entity, manager);
            manager.timeSinceCommandBlockMissing = 0;

            for (WitherStormHead head : entity.getHeadManager().getHeads()) {
               head.lerpHeadTo(40.0F, entity.yBodyRot, 16.0F);
            }

            if (!world.isClientSide && entity.getSegmentsManager().isPresent()) {
               SegmentsManager segmentsManager = entity.getSegmentsManager().get();
               if (entity.getPhase() == 5) {
                  entity.setPhase(6);
                  entity.setOtherHeadsDisabled(true);
                  segmentsManager.createSegments();
                  segmentsManager.addSegments();
               }
            }
         }

         @Override
         public void finish(Level world, WitherStormEntity entity, PlayDeadManager manager, PlayDeadManager.State next) {
            manager.timeSinceCommandBlockMissing = 0;
            if (next != PlayDeadManager.State.REVIVING) {
               manager.removePodium();
            }
         }
      },
      REVIVING {
         @Override
         public void tick(Level world, WitherStormEntity entity, PlayDeadManager manager) {
            super.tick(world, entity, manager);
            if (!world.isClientSide && manager.getTicks() > 20) {
               entity.shake(60.0F, 4.0F);
               BlockPos pos = manager.getPodiumPos();
               if (pos != null) {
                  world.explode(entity, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 16.0F, ExplosionInteraction.NONE);
               }

               world.playSound(null, entity.blockPosition(), WitherStormModSoundEvents.TREMBLE.get(), SoundSource.AMBIENT, 10.0F, 1.0F);
               manager.setState(PlayDeadManager.State.NORMAL_BEHAVIOR);
               manager.removePodium();
            }
         }

         @Override
         public void init(Level world, WitherStormEntity entity, PlayDeadManager manager) {
            manager.setRecentlyRevived(true);
            super.init(world, entity, manager);
            if (!world.isClientSide && entity.shouldPlayGlobalSounds) {
               entity.playSoundToEveryone(WitherStormModSoundEvents.WITHER_STORM_REACTIVATES.get(), 10.0F, 1.0F);
            }
         }
      };

      public void tick(Level world, WitherStormEntity entity, PlayDeadManager manager) {
         manager.ticksSinceInitO = manager.ticksSinceInit++;
         if (!this.disablesAi()) {
            manager.revivalTicks++;
         }

         if (this.isPastInterval(world, entity, manager)) {
            manager.nextState();
         }

         if (this.disablesAi()) {
            if (manager.ticksSinceInit % 5 == 0) {
               int size = Math.max(10, entity.getRandom().nextInt(15));

               for (int i = 0; i < size; i++) {
                  for (DebrisCluster cluster : entity.getDebrisClusters()) {
                     if (!cluster.isDisabled()) {
                        cluster.setDisabled(entity.getRandom().nextInt(entity.getDebrisClusters().size()) == 0);
                     }
                  }
               }
            }
         } else if (manager.ticksSinceInit % 10 == 0) {
            int size = Math.max(10, entity.getRandom().nextInt(15));

            for (int i = 0; i < size; i++) {
               for (DebrisCluster clusterx : entity.getDebrisClusters()) {
                  if (clusterx.isDisabled() && entity.getRandom().nextInt(entity.getDebrisClusters().size()) == 0) {
                     clusterx.setDisabled(false);
                  }
               }
            }
         }
      }

      public void init(Level world, WitherStormEntity entity, PlayDeadManager manager) {
         manager.ticksSinceInit = 0;
         manager.ticksSinceInitO = 0;
         if (this.disablesAi()) {
            manager.revivalTicks = 0;
            manager.setRecentlyRevived(false);
         }

         if (!world.isClientSide) {
            if (this.disablesAi()) {
               entity.getTrackedEntities().clearAndMakeAllFall();
               if (entity.shouldPlaySoundLoop) {
                  RemoveSoundLoopMessage message = new RemoveSoundLoopMessage(entity);
                  WitherStormModPacketHandlers.MAIN.send(PacketDistributor.DIMENSION.with(() -> world.dimension()), message);
               }
            } else {
               for (WitherStormHead head : entity.getHeadManager().getHeads()) {
                  head.doRoar(entity.getRandom().nextBoolean());
               }
            }

            entity.getBossInfo().ifPresent(info -> info.setVisible(!this.disablesAi()));
         }

         entity.refreshDimensions();
      }

      public void finish(Level world, WitherStormEntity entity, PlayDeadManager manager, PlayDeadManager.State next) {
      }

      public boolean disablesAi() {
         return false;
      }

      public boolean isPastInterval(Level world, WitherStormEntity entity, PlayDeadManager manager) {
         return false;
      }

      public void sendAdditionalPackets(Level world, WitherStormEntity entity, PlayDeadManager manager) {
      }
   }
}
