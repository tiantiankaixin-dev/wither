package nonamecrackers2.witherstormmod.common.entity.ai.commandblock;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.capability.WitherStormBowelsManager;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.TentacleEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormHeadEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.entity.bossfight.BossfightPhase;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModDamageTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.BlindScreenMessage;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;
import nonamecrackers2.witherstormmod.common.util.AttributeModifierUtil;
import nonamecrackers2.witherstormmod.common.util.EquipmentHelper;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class BowelsBossFightStages {
   private static final SimpleWeightedRandomList<EntityType<? extends Mob>> WAVE_1_MOBS = SimpleWeightedRandomList.<EntityType<? extends Mob>>builder()
      .add(WitherStormModEntityTypes.SICKENED_ZOMBIE.get(), 15)
      .add(WitherStormModEntityTypes.SICKENED_SKELETON.get(), 10)
      .add(WitherStormModEntityTypes.SICKENED_SPIDER.get(), 8)
      .add(WitherStormModEntityTypes.SICKENED_CREEPER.get(), 2)
      .add(WitherStormModEntityTypes.SICKENED_VILLAGER.get(), 1)
      .add(WitherStormModEntityTypes.SICKENED_PHANTOM.get(), 1)
      .add(WitherStormModEntityTypes.SICKENED_CHICKEN.get(), 6)
      .add(WitherStormModEntityTypes.SICKENED_COW.get(), 6)
      .add(WitherStormModEntityTypes.SICKENED_MUSHROOM_COW.get(), 1)
      .add(WitherStormModEntityTypes.SICKENED_PIG.get(), 6)
      .add(WitherStormModEntityTypes.SICKENED_BEE.get(), 3)
      .add(WitherStormModEntityTypes.SICKENED_PARROT.get(), 2)
      .add(WitherStormModEntityTypes.SICKENED_WOLF.get(), 2)
      .add(WitherStormModEntityTypes.SICKENED_CAT.get(), 2)
      .add(WitherStormModEntityTypes.SICKENED_PILLAGER.get(), 4)
      .add(WitherStormModEntityTypes.SICKENED_VINDICATOR.get(), 2)
      .build();
   private static final SimpleWeightedRandomList<EntityType<? extends Mob>> WAVE_2_MOBS = SimpleWeightedRandomList.<EntityType<? extends Mob>>builder()
      .add(WitherStormModEntityTypes.SICKENED_ZOMBIE.get(), 10)
      .add(WitherStormModEntityTypes.SICKENED_SKELETON.get(), 10)
      .add(WitherStormModEntityTypes.SICKENED_SPIDER.get(), 8)
      .add(WitherStormModEntityTypes.SICKENED_CREEPER.get(), 6)
      .add(WitherStormModEntityTypes.SICKENED_IRON_GOLEM.get(), 4)
      .add(WitherStormModEntityTypes.SICKENED_VILLAGER.get(), 4)
      .add(WitherStormModEntityTypes.SICKENED_PHANTOM.get(), 2)
      .add(WitherStormModEntityTypes.SICKENED_CHICKEN.get(), 4)
      .add(WitherStormModEntityTypes.SICKENED_COW.get(), 4)
      .add(WitherStormModEntityTypes.SICKENED_MUSHROOM_COW.get(), 1)
      .add(WitherStormModEntityTypes.SICKENED_PIG.get(), 4)
      .add(WitherStormModEntityTypes.SICKENED_BEE.get(), 6)
      .add(WitherStormModEntityTypes.SICKENED_PARROT.get(), 4)
      .add(WitherStormModEntityTypes.SICKENED_WOLF.get(), 3)
      .add(WitherStormModEntityTypes.SICKENED_CAT.get(), 3)
      .add(WitherStormModEntityTypes.SICKENED_PILLAGER.get(), 8)
      .add(WitherStormModEntityTypes.SICKENED_VINDICATOR.get(), 4)
      .build();
   private static final SimpleWeightedRandomList<EntityType<? extends Mob>> WAVE_3_MOBS = SimpleWeightedRandomList.<EntityType<? extends Mob>>builder()
      .add(WitherStormModEntityTypes.SICKENED_ZOMBIE.get(), 10)
      .add(WitherStormModEntityTypes.SICKENED_SKELETON.get(), 10)
      .add(WitherStormModEntityTypes.SICKENED_SPIDER.get(), 4)
      .add(WitherStormModEntityTypes.SICKENED_CREEPER.get(), 6)
      .add(WitherStormModEntityTypes.SICKENED_IRON_GOLEM.get(), 6)
      .add(WitherStormModEntityTypes.SICKENED_VILLAGER.get(), 6)
      .add(WitherStormModEntityTypes.SICKENED_PHANTOM.get(), 4)
      .add(WitherStormModEntityTypes.SICKENED_CHICKEN.get(), 1)
      .add(WitherStormModEntityTypes.SICKENED_COW.get(), 1)
      .add(WitherStormModEntityTypes.SICKENED_MUSHROOM_COW.get(), 1)
      .add(WitherStormModEntityTypes.SICKENED_PIG.get(), 1)
      .add(WitherStormModEntityTypes.SICKENED_BEE.get(), 8)
      .add(WitherStormModEntityTypes.SICKENED_PARROT.get(), 6)
      .add(WitherStormModEntityTypes.SICKENED_WOLF.get(), 5)
      .add(WitherStormModEntityTypes.SICKENED_CAT.get(), 5)
      .add(WitherStormModEntityTypes.SICKENED_PILLAGER.get(), 10)
      .add(WitherStormModEntityTypes.SICKENED_VINDICATOR.get(), 5)
      .build();
   public static final BossfightPhase<CommandBlockEntity> IDLE = BossfightPhase.<CommandBlockEntity>blank();
   public static final BossfightPhase<CommandBlockEntity> HIT = new BossfightPhase<>((CommandBlockEntity entity) -> {
      WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(entity), new ShakeScreenMessage(240.0F, 12.0F));
      entity.level().playSound(null, entity.blockPosition(), WitherStormModSoundEvents.LOUD_TREMBLE.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
      entity.level().playSound(null, entity.blockPosition(), WitherStormModSoundEvents.BOWELS_LOUD_HURT.get(), SoundSource.HOSTILE, 1.0F, 1.0F);

      for (TentacleEntity tentacle : entity.getTentacleStructure().tentacleStructure) {
         if (tentacle != null && tentacle.isAlive()) {
            tentacle.setDormant(false);
            tentacle.doAwakeAnimation();
         }
      }

      if (entity.getHealth() / entity.getMaxHealth() >= 0.75F) {
         entity.playSound(WitherStormModSoundEvents.WITHER_STORM_REACTIVATES.get(), 64.0F, 1.0F);
      }
   }, 60);
   public static final BossfightPhase<CommandBlockEntity> MOVE_PODIUM = new BossfightPhase<>((CommandBlockEntity entity) -> {
      entity.createPodiumCluster();
      WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(entity), new ShakeScreenMessage(120.0F, 12.0F));
      entity.level().playSound(null, entity.blockPosition(), WitherStormModSoundEvents.LOUD_TREMBLE.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
   }, 100).setTickAction((Integer time, CommandBlockEntity entity) -> {
      entity.findPodiumCluster();
      Vec3 delta = new Vec3(0.0, 0.05, 0.0);
      entity.movePodiumCluster(delta);
      entity.move(MoverType.SELF, delta);
   }).setFinishAction((CommandBlockEntity entity) -> {
      BlockClusterEntity cluster = entity.getPodiumCluster();
      Vec3 pos = Vec3.atBottomCenterOf(entity.blockPosition());
      if (cluster != null && cluster.isAlive()) {
         Vec3 clusterPos = pos.add(0.0, cluster.getY() - entity.getY() + 1.0, 0.0);
         cluster.setPos(clusterPos.x, clusterPos.y, clusterPos.z);
         cluster.place();
      }

      entity.setPos(pos.x, pos.y, pos.z);
      entity.podiumCluster = null;
      entity.podiumClusterUUID = null;
   });
   public static final BossfightPhase<CommandBlockEntity> WAIT = BossfightPhase.<CommandBlockEntity>blank().setFixedTime(20);
   public static final BossfightPhase<CommandBlockEntity> MOB_WAVE_1 = new BossfightPhase<>(
         (CommandBlockEntity entity) -> {
            entity.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_ACTIVATES.get(), 5.0F, 1.0F);
            ((ServerLevel)entity.level())
               .sendParticles(
                  WitherStormModParticleTypes.COMMAND_BLOCK.get(),
                  entity.getX(),
                  entity.getEyeY(),
                  entity.getZ(),
                  60,
                  entity.getRandom().nextGaussian(),
                  entity.getRandom().nextGaussian(),
                  entity.getRandom().nextGaussian(),
                  0.2
               );
         },
         100
      )
      .setTickAction((Integer time, CommandBlockEntity entity) -> {
         if (time % 8 == 0) {
            Mob mob = entity.summonRandomMob(50, WAVE_1_MOBS);
            if (mob != null) {
               ServerLevel serverLevel = (ServerLevel)entity.level();
               DifficultyInstance difficulty = serverLevel.getCurrentDifficultyAt(mob.blockPosition());
               mob.getAttribute(Attributes.MAX_HEALTH)
                  .addPermanentModifier(new AttributeModifier(AttributeModifierUtil.id("extra_health_final_bossfight_wave_1"), 2.0, Operation.ADD_VALUE));
               if (WitherSickened.CAN_WEAR_ARMOR.test(mob) && mob instanceof Monster monster && entity.getRandom().nextDouble() >= 0.5) {
                  EquipmentHelper.applyEquipment(monster, difficulty, false);
               }
            }
         }
      })
      .setFinishAction((CommandBlockEntity entity) -> entity.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_POWER_DOWN.get(), 5.0F, 1.0F));
   public static final BossfightPhase<CommandBlockEntity> MOB_WAVE_2 = new BossfightPhase<>(
         (CommandBlockEntity entity) -> {
            WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(entity), new ShakeScreenMessage(120.0F, 8.0F));
            entity.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_ACTIVATES.get(), 5.0F, 1.0F);
            ((ServerLevel)entity.level())
               .sendParticles(
                  WitherStormModParticleTypes.COMMAND_BLOCK.get(),
                  entity.getX(),
                  entity.getEyeY(),
                  entity.getZ(),
                  60,
                  entity.getRandom().nextGaussian(),
                  entity.getRandom().nextGaussian(),
                  entity.getRandom().nextGaussian(),
                  0.2
               );

            for (Entity nearby : entity.level().getEntitiesOfClass(TentacleEntity.class, entity.getBoundingBox().inflate(50.0))) {
               TentacleEntity tentacle = (TentacleEntity)nearby;
               tentacle.setDormant(false);
               tentacle.doAwakeAnimation();
            }
         },
         100
      )
      .setTickAction((Integer time, CommandBlockEntity entity) -> {
         if (time % 10 == 0) {
            Mob mob = entity.summonRandomMob(50, WAVE_2_MOBS);
            if (mob != null) {
               ServerLevel serverLevel = (ServerLevel)entity.level();
               DifficultyInstance difficulty = serverLevel.getCurrentDifficultyAt(mob.blockPosition());
               mob.getAttribute(Attributes.MAX_HEALTH)
                  .addPermanentModifier(new AttributeModifier(AttributeModifierUtil.id("extra_health_final_bossfight_wave_2"), 4.0, Operation.ADD_VALUE));
               if (WitherSickened.CAN_WEAR_ARMOR.test(mob) && mob instanceof Monster monster) {
                  EquipmentHelper.applyEquipment(monster, difficulty, false);
               }
            }
         }
      })
      .setFinishAction(
         (CommandBlockEntity entity) -> {
            entity.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_POWER_DOWN.get(), 5.0F, 1.0F);
            BlockPos pos = entity.getRandomNearbyPos((EntityType<?>)WitherStormModEntityTypes.WITHERED_SYMBIONT.get(), 50, 20);
            if (pos != null) {
               ServerLevel world = (ServerLevel)entity.level();
               WitheredSymbiontEntity symbiont = WitherStormModEntityTypes.WITHERED_SYMBIONT.get().spawn(world, pos, MobSpawnType.EVENT);
               if (symbiont != null) {
                  symbiont.setNonBossMode(true);
                  symbiont.setRushMode(true);
                  symbiont.getAttribute(Attributes.MAX_HEALTH)
                     .addPermanentModifier(
                        new AttributeModifier(AttributeModifierUtil.id("withered_symbiont_final_boss_battle_low_health"), -0.5, Operation.ADD_MULTIPLIED_BASE)
                     );
                  symbiont.setPersistenceRequired();
                  symbiont.setHealth(symbiont.getMaxHealth());
                  world.sendParticles(
                     WitherStormModParticleTypes.COMMAND_BLOCK.get(),
                     symbiont.getX(),
                     symbiont.getEyeY(),
                     symbiont.getZ(),
                     40,
                     entity.getRandom().nextGaussian(),
                     entity.getRandom().nextGaussian(),
                     entity.getRandom().nextGaussian(),
                     0.2
                  );
                  world.sendParticles(
                     ParticleTypes.LARGE_SMOKE,
                     symbiont.getX(),
                     symbiont.getEyeY(),
                     symbiont.getZ(),
                     40,
                     entity.getRandom().nextGaussian(),
                     entity.getRandom().nextGaussian(),
                     entity.getRandom().nextGaussian(),
                     0.01
                  );
                  symbiont.playSound(WitherStormModSoundEvents.WITHERED_SYMBIONT_SPAWN.get(), 4.0F, 1.0F);
               }
            }
         }
      );
   public static final BossfightPhase<CommandBlockEntity> MOB_WAVE_3 = new BossfightPhase<>(
         (CommandBlockEntity entity) -> {
            WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(entity), new ShakeScreenMessage(120.0F, 16.0F));
            entity.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_ACTIVATES.get(), 6.0F, 1.0F);
            ((ServerLevel)entity.level())
               .sendParticles(
                  WitherStormModParticleTypes.COMMAND_BLOCK.get(),
                  entity.getX(),
                  entity.getEyeY(),
                  entity.getZ(),
                  80,
                  entity.getRandom().nextGaussian(),
                  entity.getRandom().nextGaussian(),
                  entity.getRandom().nextGaussian(),
                  0.2
               );

            for (Entity nearby : entity.level().getEntitiesOfClass(Entity.class, entity.getBoundingBox().inflate(50.0))) {
               if (nearby instanceof TentacleEntity tentacle) {
                  tentacle.setDormant(false);
                  tentacle.doAwakeAnimation();
               } else if (nearby instanceof WitherStormHeadEntity head) {
                  head.setActive(true);
                  head.setRoar(false);
                  head.setRoarTime(40);
               }
            }
         },
         120
      )
      .setTickAction((Integer time, CommandBlockEntity entity) -> {
         if (time % 5 == 0) {
            Mob mob = entity.summonRandomMob(50, WAVE_3_MOBS);
            if (mob != null) {
               ServerLevel serverLevel = (ServerLevel)entity.level();
               DifficultyInstance difficulty = serverLevel.getCurrentDifficultyAt(mob.blockPosition());
               mob.getAttribute(Attributes.MAX_HEALTH)
                  .addPermanentModifier(new AttributeModifier(AttributeModifierUtil.id("extra_health_final_bossfight_wave_3"), 8.0, Operation.ADD_VALUE));
               if (WitherSickened.CAN_WEAR_ARMOR.test(mob) && mob instanceof Monster monster) {
                  EquipmentHelper.applyEquipment(monster, difficulty, true);
               }
            }
         }
      })
      .setFinishAction((CommandBlockEntity entity) -> entity.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_POWER_DOWN.get(), 6.0F, 1.0F));
   public static final BossfightPhase<CommandBlockEntity> PROTECT_IDLE = BossfightPhase.copyOf(IDLE, (CommandBlockEntity entity) -> {
      for (TentacleEntity tentacle : entity.getTentacleStructure().tentacleStructure) {
         if (tentacle != null && tentacle.isAlive()) {
            tentacle.curlAround(entity.position());
         }
      }
   }, (CommandBlockEntity entity) -> {
      if (!WorldUtil.areaLoaded(entity.level(), entity.blockPosition(), 2)) {
         return false;
      } else {
         boolean flag = true;

         for (Mob nearby : entity.level().getEntitiesOfClass(Mob.class, entity.getBoundingBox().inflate(50.0))) {
            if (nearby instanceof WitherStormHeadEntity) {
               WitherStormHeadEntity head = (WitherStormHeadEntity)nearby;
               if (head.isAlive() && !head.isPlayingDead() && !head.isHurt()) {
                  flag = false;
               }
            } else if (nearby instanceof WitheredSymbiontEntity) {
               WitheredSymbiontEntity symbiont = (WitheredSymbiontEntity)nearby;
               if (!symbiont.isDeadOrDying()) {
                  flag = false;
               }
            }
         }

         return flag;
      }
   }).setTickAction((Integer time, CommandBlockEntity entity) -> {
      if (time % 40 == 0) {
         for (TentacleEntity tentacle : entity.getTentacleStructure().tentacleStructure) {
            if (tentacle != null && tentacle.isAlive() && !tentacle.isDoingSwingAttack()) {
               tentacle.curlAround(entity.position());
            }
         }
      }
   }).setFinishAction((CommandBlockEntity entity) -> {
      for (TentacleEntity tentacle : entity.getTentacleStructure().tentacleStructure) {
         if (tentacle != null && tentacle.isAlive()) {
            tentacle.stopCurlingAround();
         }
      }
   });
   public static final BossfightPhase<CommandBlockEntity> DEATH = new BossfightPhase<>((CommandBlockEntity entity) -> {
         WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(entity), new ShakeScreenMessage(240.0F, 14.0F));
         WitherStormModPacketHandlers.MAIN.send(PacketDistributor.TRACKING_ENTITY.with(entity), new BlindScreenMessage(240, 120, 80));
         entity.level().playSound(null, entity.blockPosition(), WitherStormModSoundEvents.LOUD_TREMBLE.get(), SoundSource.AMBIENT, 5.0F, 1.0F);
         entity.level().playSound(null, entity.blockPosition(), WitherStormModSoundEvents.BOWELS_LOUD_HURT.get(), SoundSource.HOSTILE, 5.0F, 1.0F);
         entity.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_DESTRUCT.get(), 64.0F, 1.0F);

         for (Entity nearby : entity.level().getEntitiesOfClass(Entity.class, entity.getBoundingBox().inflate(50.0))) {
            if (nearby.isAlive()) {
               if (nearby instanceof TentacleEntity tentacle) {
                  tentacle.doIndefiniteAwakeAnimation();
                  tentacle.setCanSwing(false);
                  tentacle.setCanStrangle(false);
               } else if (nearby instanceof WitherStormHeadEntity head) {
                  head.kill();
               } else if (nearby instanceof WitheredSymbiontEntity || nearby instanceof WitherSickened) {
                  nearby.kill();
               }
            }
         }
      }, (CommandBlockEntity entity) -> false)
      .setFinishAction(
         (CommandBlockEntity entity) -> {
            WitherStormEntity storm = entity.getOwner();
            if (storm != null && !storm.isRemoved()) {
               if (entity.killer != null) {
                  if (entity.killer instanceof Player) {
                     storm.hurt(WitherStormModDamageTypes.playerAttackWitherStorm((Player)entity.killer), Float.MAX_VALUE);
                  } else {
                     storm.hurt(WitherStormModDamageTypes.mobAttackWitherStorm(entity.killer), Float.MAX_VALUE);
                  }
               } else {
                  storm.hurt(storm.damageSources().fellOutOfWorld(), Float.MAX_VALUE);
               }

               for (ServerPlayer player : entity.level().getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(150.0))) {
                  if (player != entity.killer) {
                     player.awardStat(Stats.ENTITY_KILLED.get(storm.getType()));
                  }

                  if (player.getRespawnDimension().equals(WitherStormMod.bowels(player.serverLevel()).dimension())) {
                     player.setRespawnPosition(null, null, 0.0F, false, false);
                  }

                  player.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(WitherSicknessTracker::cure);
                  List<TamableAnimal> playersPets = player.level()
                     .getEntitiesOfClass(TamableAnimal.class, entity.getBoundingBox().inflate(150.0))
                     .stream()
                     .filter(animal -> player.getUUID().equals(animal.getOwnerUUID()))
                     .collect(Collectors.toList());
                  playersPets.forEach(animal -> WitherStormBowelsManager.leave(player.serverLevel(), animal, null));
                  WitherStormBowelsManager.queueLeave(
                     player,
                     () -> {
                        player.connection
                           .send(
                              new ClientboundSoundPacket(
                                 (Holder)ForgeRegistries.SOUND_EVENTS.getHolder(WitherStormModSoundEvents.WITHER_STORM_DEATH.get()).get(),
                                 SoundSource.HOSTILE,
                                 player.getX(),
                                 player.getY(),
                                 player.getZ(),
                                 1.0F,
                                 1.0F,
                                 entity.getRandom().nextLong()
                              )
                           );
                        if (storm != null) {
                           for (int i = 0; i < 10; i++) {
                              float angle = (storm.yBodyRot + 90.0F) * (float) (Math.PI / 180.0);
                              double x = (double)Mth.cos(angle) * 100.0 + storm.getX();
                              double z = (double)Mth.sin(angle) * 100.0 + storm.getZ();
                              x += storm.getRandom().nextGaussian() * 5.0;
                              z += storm.getRandom().nextGaussian() * 5.0;
                              BlockPos pos = BlockPos.containing(x, 0.0, z);
                              pos = storm.level().getHeightmapPos(Types.MOTION_BLOCKING, pos).below();
                              BlockState state = storm.level().getBlockState(pos);
                              if (state.isFaceSturdy(storm.level(), pos, Direction.UP)) {
                                 Vec3 finalPos = Vec3.upFromBottomCenterOf(pos, 1.0);
                                 player.moveTo(finalPos);
                                 player.lookAt(Anchor.EYES, storm.getEyePosition(1.0F));
                                 break;
                              }
                           }
                        }
                     }
                  );
               }
            }
         }
      );
}
