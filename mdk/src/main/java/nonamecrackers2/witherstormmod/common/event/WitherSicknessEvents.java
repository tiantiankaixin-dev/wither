package nonamecrackers2.witherstormmod.common.event;


import net.neoforged.neoforge.event.tick.LevelTickEvent;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: /* LazyOptional_REMOVED */ removed, new Capability API returns T or null
import net.neoforged.neoforge.event.entity.living.MobEffectEvent.Remove;
import net.neoforged.neoforge.event.entity.living.MobDespawnEvent;
import net.neoforged.neoforge.event.entity.living.MobDespawnEvent.Result;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.Clone;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.StartTracking;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.accessor.LivingEntityAccessor;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEffects;
import nonamecrackers2.crackerslib.common.packet.SimpleChannel;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.UpdateWitherSicknessTrackerMessage;

public class WitherSicknessEvents {
   @SubscribeEvent
   public static void onWorldTick(LevelTickEvent event) {
      if (event.getLevel() instanceof ServerLevel world) {
         List<WitherStormEntity> storms = Lists.newArrayList();

         for (Entity entity : world.getAllEntities()) {
            if (entity instanceof WitherStormEntity) {
               WitherStormEntity storm = (WitherStormEntity)entity;
               if (storm.getPhase() > 1) {
                  storms.add(storm);
               }
            }
         }

         for (Entity entityx : world.getAllEntities()) {
            if (entityx instanceof LivingEntity living) {
               { var tracker = living.getData(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER.get());
                  if (!tracker.isActuallyImmune()) {
                     boolean nearby = false;

                     for (WitherStormEntity storm : storms) {
                        nearby = storm.isEntityNearby(living);
                        if (nearby) {
                           break;
                        }
                     }

                     if (living.level().dimension().location().equals(WitherStormMod.bowelsLocation())) {
                        nearby = true;
                     }

                     tracker.setNearStorm(nearby);
                  }

                  tracker.tick();
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerClone(Clone event) {
      if (event.isWasDeath()) {
         Player original = event.getOriginal();
         Player player = event.getEntity();
         WitherSicknessTracker oldTracker = original.getData(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER.get());
         { var tracker = player.getData(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER.get());
            tracker.copyFrom(oldTracker);
            if ((Boolean)WitherStormModConfig.SERVER.keepSicknessAfterRespawn.get()) {
               MobEffectInstance effect = original.getEffect(WitherStormModEffects.WITHER_SICKNESS.getDelegate());
               if (effect != null) {
                  player.addEffect(effect);
               }
            } else {
               tracker.setInfected(false);
               tracker.setProximityTicks(0);
               tracker.setContacts(0);
               tracker.setContactDecreaseTicks(0);
            }
         }
      }
   }

   @SubscribeEvent
   public static void onCheckDespawn(MobDespawnEvent event) {
      LivingEntity entity = event.getEntity();
      { var tracker = entity.getData(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER.get());
         if (!tracker.isActuallyImmune() && (tracker.isInfected() || tracker.isBeingCured())) {
            event.setResult(Result.DENY);
         }
      }
      if (entity.getType().equals(EntityType.PHANTOM) && (Boolean)WitherStormModConfig.COMMON.phantomsOrbitWitherStorm.get()) {
         List<Phantom> phantoms = entity.level().getEntitiesOfClass(Phantom.class, entity.getBoundingBox().inflate(100.0));
         List<WitherStormEntity> storms = entity.level().getEntitiesOfClass(WitherStormEntity.class, entity.getBoundingBox().inflate(100.0));
         if (phantoms.size() < 12 && storms.size() >= 1) {
            event.setResult(Result.DENY);
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerJoin(PlayerLoggedInEvent event) {
      UpdateWitherSicknessTrackerMessage message = new UpdateWitherSicknessTrackerMessage(event.getEntity());
      WitherStormModPacketHandlers.MAIN.send(SimpleChannel.toPlayer((ServerPlayer)event.getEntity()), message);
   }

   @SubscribeEvent
   public static void onPlayerChangedDimensions(PlayerLoggedInEvent event) {
      UpdateWitherSicknessTrackerMessage message = new UpdateWitherSicknessTrackerMessage(event.getEntity());
      WitherStormModPacketHandlers.MAIN.send(SimpleChannel.toPlayer((ServerPlayer)event.getEntity()), message);
   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerRespawnEvent event) {
      UpdateWitherSicknessTrackerMessage message = new UpdateWitherSicknessTrackerMessage(event.getEntity());
      WitherStormModPacketHandlers.MAIN.send(SimpleChannel.toPlayer((ServerPlayer)event.getEntity()), message);
   }

   @SubscribeEvent
   public static void onPlayerStartTracking(StartTracking event) {
      UpdateWitherSicknessTrackerMessage message = new UpdateWitherSicknessTrackerMessage(event.getTarget());
      WitherStormModPacketHandlers.MAIN.send(SimpleChannel.toPlayer((ServerPlayer)event.getEntity()), message);
   }

   @SubscribeEvent
   public static void onMobEffectRemove(Remove event) {
      LivingEntity entity = event.getEntity();
      if (((LivingEntityAccessor)entity).hasDeathProtection() && event.getEffect().value() == WitherStormModEffects.WITHER_SICKNESS.get()) {
         event.setCanceled(true);
      }
   }
}
