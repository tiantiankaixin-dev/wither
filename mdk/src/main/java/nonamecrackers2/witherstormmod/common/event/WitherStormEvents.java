package nonamecrackers2.witherstormmod.common.event;

import java.util.UUID;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent.Start;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent.Detonate;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.api.common.entity.WitherStormBase;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.WitherStormHead;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;

public class WitherStormEvents {
   @SubscribeEvent
   public static void onLivingDeath(LivingDeathEvent event) {
      LivingEntity entity = event.getEntity();
      LivingEntity attacker = entity.getKillCredit();
      if (!entity.level().isClientSide) {
         boolean flag = false;
         if (attacker instanceof WitherStormEntity storm) {
            if (ForgeEventFactory.getMobGriefingEvent(entity.level(), entity)) {
               BlockPos pos = BlockPos.containing(entity.position());
               BlockState state = Blocks.WITHER_ROSE.defaultBlockState();
               if (entity.level().isEmptyBlock(pos) && state.canSurvive(entity.level(), pos)) {
                  entity.level().setBlock(pos, state, 3);
                  flag = true;
               }
            }

            if (!flag) {
               ItemEntity item = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(Items.WITHER_ROSE));
               entity.level().addFreshEntity(item);
            }

            if (entity instanceof ServerPlayer player) {
               player.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA).ifPresent(data -> data.setKilledByStorm(storm.getUUID()));
            }
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerRespawnEvent event) {
      if (event.getEntity() instanceof ServerPlayer player && !event.isEndConquered()) {
         player.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA)
            .ifPresent(
               data -> {
                  UUID id = data.getKilledByStorm();
                  if ((Boolean)WitherStormModConfig.SERVER.preventWitherStormCamping.get()
                     && id != null
                     && player.serverLevel().getEntity(id) instanceof WitherStormEntity storm
                     && storm.getPhase() > 3
                     && (double)storm.distanceTo(player) < 300.0) {
                     ServerLevel level = player.serverLevel();

                     for (int i = 0; i < 10; i++) {
                        float angle = storm.getRandom().nextFloat() * (float) (Math.PI * 2);
                        int x = (int)(Mth.cos(angle) * ((float)storm.getRandom().nextInt(200) + 300.0F)) + storm.getBlockX();
                        int z = (int)(Mth.sin(angle) * ((float)storm.getRandom().nextInt(200) + 300.0F)) + storm.getBlockZ();
                        level.getChunkAt(new BlockPos(x, 0, z));
                        int y = level.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                        BlockPos pos = new BlockPos(x, y, z);
                        if (Level.isInSpawnableBounds(pos)) {
                           Vec3 vec = (Vec3)Player.findRespawnPositionAndUseSpawnBlock(level, pos, 0.0F, true, true).orElse(null);
                           if (vec != null) {
                              player.lookAt(Anchor.EYES, vec);
                              player.moveTo(vec.x, vec.y, vec.z);
                              break;
                           }
                        }
                     }
                  }

                  data.setKilledByStorm(null);
               }
            );
      }
   }

   @SubscribeEvent
   public static void onExplosionDetonate(Detonate event) {
      Level world = event.getLevel();
      if (!world.isClientSide) {
         Entity source = event.getExplosion().getExploder();
         if (source != null && !(source instanceof WitherStormEntity)) {
            for (Entity entity : world.getNearbyEntities(WitherStormEntity.class, TargetingConditions.DEFAULT, null, source.getBoundingBox().inflate(100.0))) {
               if (entity instanceof WitherStormEntity) {
                  WitherStormEntity storm = (WitherStormEntity)entity;

                  for (WitherStormHead head : storm.getHeadManager().getHeads()) {
                     if (storm.tractorBeamActive(head.getIndex())) {
                        if (source instanceof PrimedTnt
                           && !(source instanceof FormidibombEntity)
                           && storm.canBeDistracted(head.getIndex(), WitherStormBase.DistractionType.ENTITY_BASED)) {
                           int chance = Math.max(1, Mth.floor(storm.distanceTo(source) / 30.0F));
                           if (head.canSee(source) && storm.getRandom().nextInt(chance) == 0) {
                              head.makeDistracted(source.position(), storm.getRandom().nextInt(60) + 120);
                           }
                        }

                        Entity exploder = event.getExplosion().getExploder();
                        if (exploder != null && exploder instanceof Projectile) {
                           Projectile projectile = (Projectile)exploder;
                           if (projectile.getOwner() == storm) {
                              continue;
                           }
                        }

                        Vec3 headPos = head.getHeadPos();
                        if (event.getExplosion().getPosition().distanceTo(headPos) < (storm.getPhase() < 4 ? 5.0 : 12.0)
                           && !storm.isDeadOrPlayingDead()
                           && !head.isHeadInjured()
                           && head.checkAndCountAttack()) {
                           head.hurt(source, storm.getHeadManager().getHeadInjuryTime());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onEntityMount(EntityMountEvent event) {
      if (event.getEntityMounting() instanceof WitherStormEntity) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onEntityUseItem(Start event) {
      ItemStack item = event.getItem();
      if (item.is(Items.GOAT_HORN)) {
         for (WitherStormEntity storm : event.getEntity()
            .level()
            .getEntitiesOfClass(WitherStormEntity.class, event.getEntity().getBoundingBox().inflate(128.0, 256.0, 128.0))) {
            if (!storm.isDeadOrPlayingDead()) {
               for (WitherStormHead head : storm.getHeadManager().getHeads()) {
                  if (storm.tractorBeamActive(head.getIndex())
                     && storm.canBeDistracted(head.getIndex(), WitherStormBase.DistractionType.ENTITY_BASED)
                     && storm.getRandom().nextInt(3) == 0) {
                     head.makeDistracted(event.getEntity().getEyePosition(), 80);
                  }
               }
            }
         }
      }
   }
}
