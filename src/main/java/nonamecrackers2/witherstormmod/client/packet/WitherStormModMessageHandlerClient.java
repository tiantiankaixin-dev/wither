package nonamecrackers2.witherstormmod.client.packet;

import com.ibm.icu.impl.locale.XCldrStub.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.VecDeltaCodec;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket.AttributeSnapshot;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.client.audio.WitherStormSoundLoop;
import nonamecrackers2.witherstormmod.client.audio.bosstheme.BossThemeManager;
import nonamecrackers2.witherstormmod.client.capability.WitherStormDistantRenderer;
import nonamecrackers2.witherstormmod.client.capability.WitherStormLoopingSoundManager;
import nonamecrackers2.witherstormmod.client.gui.menu.SuperBeaconScreen;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.PlayDeadManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.HeadManager;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEffects;
import nonamecrackers2.witherstormmod.common.packet.BlindScreenMessage;
import nonamecrackers2.witherstormmod.common.packet.CreateDebrisMessage;
import nonamecrackers2.witherstormmod.common.packet.CreateLoopingSoundMessage;
import nonamecrackers2.witherstormmod.common.packet.DistantRendererMessage;
import nonamecrackers2.witherstormmod.common.packet.EntitySyncableDataMessage;
import nonamecrackers2.witherstormmod.common.packet.FormidibombExplosionMessage;
import nonamecrackers2.witherstormmod.common.packet.GlobalSoundMessage;
import nonamecrackers2.witherstormmod.common.packet.NotifyHeadInjuryMessage;
import nonamecrackers2.witherstormmod.common.packet.OnHeadAttackedMessage;
import nonamecrackers2.witherstormmod.common.packet.PlayAdditionalLoopingSoundMessage;
import nonamecrackers2.witherstormmod.common.packet.PlayerMotionMessage;
import nonamecrackers2.witherstormmod.common.packet.RemoveAdditionalLoopingSoundMessage;
import nonamecrackers2.witherstormmod.common.packet.RemoveDistantSuperBeaconMessage;
import nonamecrackers2.witherstormmod.common.packet.RemoveSoundLoopMessage;
import nonamecrackers2.witherstormmod.common.packet.RemoveStormFromDistantRendererMessage;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;
import nonamecrackers2.witherstormmod.common.packet.StormAttributesMessage;
import nonamecrackers2.witherstormmod.common.packet.StormMetadataMessage;
import nonamecrackers2.witherstormmod.common.packet.StormSoundPositionMessage;
import nonamecrackers2.witherstormmod.common.packet.StormTeleportMessage;
import nonamecrackers2.witherstormmod.common.packet.SuperBeaconValidEffectsMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateDamagingProjectileMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateDistantSuperBeaconMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateEffectInstanceMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdatePlayDeadManagerMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateStormHeadLookMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateStormPositionMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateStormVelocityMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateWitherSicknessTrackerMessage;
import nonamecrackers2.witherstormmod.common.packet.WitherStormToDistantRendererMessage;
import nonamecrackers2.witherstormmod.common.util.EntitySyncableData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WitherStormModMessageHandlerClient {
   public static final Logger LOGGER = LogManager.getLogger();

   public static void processGlobalSoundMessage(GlobalSoundMessage message) {
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player != null) {
         player.playNotifySound(message.getSoundEvent(), SoundSource.HOSTILE, message.getVolume(), message.getPitch());
      }
   }

   public static void processPlayerMotionMessage(PlayerMotionMessage message) {
      Minecraft mc = Minecraft.getInstance();
      Entity vehicle = mc.player.getVehicle();
      if (mc.player.isPassenger()) {
         vehicle.setDeltaMovement(message.getMotion());
      } else {
         mc.player.setDeltaMovement(message.getMotion());
      }
   }

   public static void processWitherStormToDistantRendererMessage(WitherStormToDistantRendererMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      purgeNonApplicable(world, message);
      getDistantRenderer(world)
         .ifPresent(
            distantRenderer -> {
               WitherStormEntity entity = (WitherStormEntity)(ForgeRegistries.ENTITY_TYPES.getValue(message.getType())).create(mc.level);
               double x = message.getPos().x;
               double y = message.getPos().y;
               double z = message.getPos().z;
               float yRot = (float)(message.getYRot() * 360) / 256.0F;
               float xRot = (float)(message.getXRot() * 360) / 256.0F;
               entity.syncPacketPositionCodec(message.getPos().x, message.getPos().y, message.getPos().z);
               entity.yBodyRot = (float)(message.getHeadYRot() * 360) / 256.0F;
               entity.yHeadRot = (float)(message.getHeadYRot() * 360) / 256.0F;
               entity.getHeadManager().updateHeadsFromPacked(message.getRots());
               entity.setId(message.getId());
               entity.setUUID(message.getUUID());
               entity.absMoveTo(x, y, z, yRot, xRot);
               entity.setDeltaMovement(
                  new Vec3(
                     (double)message.getDeltaMovement().getX() / 8000.0,
                     (double)message.getDeltaMovement().getY() / 8000.0,
                     (double)message.getDeltaMovement().getZ() / 8000.0
                  )
               );
               entity.getEntityData().assignValues(message.getUnpackedData());
               entity.setOnDistantRenderer();
               if (!distantRenderer.contains(message.getId())) {
                  distantRenderer.addWitherStorm(message.getId(), entity);
               }

               if (entity instanceof IEntityAdditionalSpawnData) {
                  ((IEntityAdditionalSpawnData)entity).readSpawnData(message.getBuffer());
               }

               if (entity instanceof EntitySyncableData) {
                  entity.readData(message.getBuffer());
               }
            }
         );
   }

   public static void processRemoveStormFromDistantRendererMessage(RemoveStormFromDistantRendererMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      purgeNonApplicable(world, message);
      getDistantRenderer(world).ifPresent(distantRenderer -> {
         WitherStormEntity entity = distantRenderer.get(message.getId());
         if (entity != null) {
            entity.discard();
         }
      });
   }

   public static void processUpdateStormPositionMessage(UpdateStormPositionMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      purgeNonApplicable(world, message);
      getDistantRenderer(world).ifPresent(distantRenderer -> {
         WitherStormEntity entity = distantRenderer.get(message.getEntityID());
         if (entity != null && !entity.isControlledByLocalInstance()) {
            if (message.hasPosition()) {
               VecDeltaCodec codec = entity.getPositionCodec();
               Vec3 vector3d = codec.decode((long)message.getX(), (long)message.getY(), (long)message.getZ());
               codec.setBase(vector3d);
               float yRot = message.hasRotation() ? (float)(message.getYRot() * 360) / 256.0F : entity.getYRot();
               float xRot = message.hasRotation() ? (float)(message.getXRot() * 360) / 256.0F : entity.getXRot();
               entity.lerpTo(vector3d.x, vector3d.y, vector3d.z, yRot, xRot, 3, false);
            } else if (message.hasRotation()) {
               float yRot = (float)(message.getYRot() * 360) / 256.0F;
               float xRot = (float)(message.getXRot() * 360) / 256.0F;
               entity.lerpTo(entity.getX(), entity.getY(), entity.getZ(), yRot, xRot, 3, false);
            }

            entity.setOnGround(message.onGround());
         }
      });
   }

   public static void processStormTeleportMessage(StormTeleportMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      purgeNonApplicable(world, message);
      getDistantRenderer(world).ifPresent(distantRenderer -> {
         WitherStormEntity entity = distantRenderer.get(message.getEntityID());
         if (entity != null) {
            double x = message.getX();
            double y = message.getY();
            double z = message.getZ();
            entity.syncPacketPositionCodec(x, y, z);
            if (!entity.isControlledByLocalInstance()) {
               float yRot = (float)(message.getYRot() * 360) / 256.0F;
               float xRot = (float)(message.getXRot() * 360) / 256.0F;
               if (world.getEntity(entity.getId()) == null) {
                  entity.lerpTo(x, y, z, yRot, xRot, 3, false);
               } else {
                  entity.xo = x;
                  entity.yo = y;
                  entity.zo = z;
                  entity.xOld = x;
                  entity.yOld = y;
                  entity.zOld = z;
                  entity.yRotO = yRot;
                  entity.xRotO = xRot;
                  entity.setYRot(yRot);
                  entity.setXRot(xRot);
               }

               entity.setOnGround(message.onGround());
            }
         }
      });
   }

   public static void processUpdateStormVelocityMessage(UpdateStormVelocityMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      purgeNonApplicable(world, message);
      getDistantRenderer(world).ifPresent(distantRenderer -> {
         WitherStormEntity entity = distantRenderer.get(message.getEntityID());
         if (entity != null) {
            entity.lerpMotion(message.getX() / 8000.0, message.getY() / 8000.0, message.getZ() / 8000.0);
         }
      });
   }

   public static void processUpdateStormHeadLookMessage(UpdateStormHeadLookMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      purgeNonApplicable(world, message);
      getDistantRenderer(world).ifPresent(distantRenderer -> {
         WitherStormEntity entity = distantRenderer.get(message.getEntityID());
         if (entity != null) {
            float yHeadRot = (float)(message.getYRot() * 360) / 256.0F;
            entity.lerpHeadTo(yHeadRot, 3);
         }
      });
   }

   public static void processStormMetadataMessage(StormMetadataMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      purgeNonApplicable(world, message);
      getDistantRenderer(world).ifPresent(distantRenderer -> {
         WitherStormEntity entity = distantRenderer.get(message.getEntityID());
         if (entity != null && message.getUnpackedItems() != null) {
            entity.getEntityData().assignValues(message.getUnpackedItems());
         }
      });
   }

   public static void processStormAttributesMessage(StormAttributesMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      purgeNonApplicable(world, message);
      getDistantRenderer(world).ifPresent(distantRenderer -> {
         WitherStormEntity entity = distantRenderer.get(message.getEntityID());
         if (entity != null) {
            AttributeMap manager = entity.getAttributes();

            for (AttributeSnapshot snapshot : message.getAttributes()) {
               AttributeInstance attribute = manager.getInstance(snapshot.getAttribute());
               if (attribute == null) {
                  LOGGER.warn("WitherStormEntity {} does not have attribute {}", entity, ForgeRegistries.ATTRIBUTES.getKey(snapshot.getAttribute()));
               } else {
                  attribute.setBaseValue(snapshot.getBase());
                  attribute.removeModifiers();

                  for (AttributeModifier modifier : snapshot.getModifiers()) {
                     attribute.addTransientModifier(modifier);
                  }
               }
            }
         }
      });
   }

   public static void processCreateLoopingSoundMessage(CreateLoopingSoundMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      getLoopingSoundManager(world)
         .ifPresent(
            loopingManager -> {
               double distance = mc.player.distanceToSqr(message.getX(), message.getY(), message.getZ());
               float fade = Math.max(1.0F, (float)(distance / 1000.0) / 32.0F);
               loopingManager.putSound(
                  message.getEntityID(),
                  new WitherStormSoundLoop(
                     new Vec3(message.getX(), message.getY(), message.getZ()), WitherStormEntity.getSoundForLoop(message.getPhase(), fade)
                  )
               );
            }
         );
   }

   public static void processStormSoundPositionMessage(StormSoundPositionMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      getLoopingSoundManager(world)
         .ifPresent(
            loopingManager -> {
               WitherStormSoundLoop loop = loopingManager.getSound(message.getEntityID());
               if (loop != null) {
                  loop.prevPos = loop.pos;
                  loop.pos = new Vec3(message.getX(), message.getY(), message.getZ());
                  double distance = mc.player.distanceToSqr(message.getX(), message.getY(), message.getZ());
                  float fade = Math.max(1.0F, (float)(distance / 1000.0) / 32.0F);
                  SoundEvent event = WitherStormEntity.getSoundForLoop(message.getPhase(), fade);
                  if (!loop.soundevent.getLocation().equals(event.getLocation()) && !loopingManager.alreadyHasReplacement(message.getEntityID())) {
                     loopingManager.replace(message.getEntityID(), new WitherStormSoundLoop(new Vec3(message.getX(), message.getY(), message.getZ()), event));
                  }
               } else {
                  double distance = mc.player.distanceToSqr(message.getX(), message.getY(), message.getZ());
                  float fade = Math.max(1.0F, (float)(distance / 1000.0) / 32.0F);
                  loopingManager.putSound(
                     message.getEntityID(),
                     new WitherStormSoundLoop(
                        new Vec3(message.getX(), message.getY(), message.getZ()), WitherStormEntity.getSoundForLoop(message.getPhase(), fade)
                     )
                  );
               }

               WitherStormSoundLoop additional = loopingManager.getAdditional(message.getEntityID());
               if (additional != null) {
                  additional.prevPos = additional.pos;
                  additional.pos = new Vec3(message.getX(), message.getY(), message.getZ());
               }
            }
         );
   }

   public static void processRemoveSoundLoopMessage(RemoveSoundLoopMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      getLoopingSoundManager(world).ifPresent(loopingManager -> loopingManager.stopSound(message.getId()));
   }

   public static void processNotifyHeadInjuryMessage(NotifyHeadInjuryMessage message) {
      Minecraft mc = Minecraft.getInstance();
      WitherStormEntity entity = (WitherStormEntity)mc.level.getEntity(message.getEntityID());
      if (entity != null) {
         HeadManager manager = entity.getHeadManager();
         manager.getHead(message.getHead()).hurt(null, manager.getHeadInjuryTime());
      }
   }

   public static void processUpdateEffectInstanceMessage(UpdateEffectInstanceMessage message) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level.getEntity(message.getEntityID()) instanceof LivingEntity living) {
         MobEffectInstance effect = living.getEffect((MobEffect)WitherStormModEffects.WITHER_SICKNESS.get());
         if (effect != null) {
            MobEffectInstance newEffect = new MobEffectInstance(
               (MobEffect)WitherStormModEffects.WITHER_SICKNESS.get(), message.getDuration(), message.getAmplifier()
            );
            effect.update(newEffect);
         }
      }
   }

   public static void processUpdateWitherSicknessTrackerMessage(UpdateWitherSicknessTrackerMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      Entity entity = world.getEntity(message.getId());
      if (entity != null) {
         entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(tracker -> tracker.copyFromMessage(message));
      }
   }

   public static void processUpdatePlayDeadManagerMessage(UpdatePlayDeadManagerMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      Entity entity = world.getEntity(message.getEntityID());
      if (entity != null && entity instanceof WitherStormEntity storm) {
         updatePlayDeadManager(storm.getPlayDeadManager(), message);
      }

      getDistantRenderer(world).ifPresent(manager -> {
         WitherStormEntity stormx = manager.get(message.getEntityID());
         if (stormx != null) {
            updatePlayDeadManager(stormx.getPlayDeadManager(), message);
         }
      });
   }

   private static void updatePlayDeadManager(PlayDeadManager manager, UpdatePlayDeadManagerMessage message) {
      if (!message.shouldUpdateTick()) {
         manager.setState(message.getState());
         manager.setTicksSinceRevival(message.getTicksSinceRevival());
      }

      manager.setRecentlyRevived(message.hasRecentlyBeenRevived());
      manager.setTickAmount(message.getTicks());
      manager.setRevivalPlayerProtectionTime(message.getRevivalPlayerProtectionTime());
   }

   public static void processCreateDebrisMessage(CreateDebrisMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      Entity entity = world.getEntity(message.getEntityID());
      if (entity != null && entity instanceof WitherStormEntity storm) {
         storm.createDebrisClusters(message.isDebrisHidden());
         storm.createDebrisRings(message.isDebrisHidden());
      }

      getDistantRenderer(world).ifPresent(manager -> {
         WitherStormEntity stormx = manager.get(message.getEntityID());
         if (stormx != null) {
            stormx.createDebrisClusters(message.isDebrisHidden());
            stormx.createDebrisRings(message.isDebrisHidden());
         }
      });
   }

   public static void processEntitySyncableDataMessage(EntitySyncableDataMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      Entity entity = world.getEntity(message.getId());
      if (entity != null && entity instanceof EntitySyncableData syncable) {
         syncable.readData(message.getBuffer());
      }
   }

   public static void processPlayAdditionalLoopingSoundMessage(PlayAdditionalLoopingSoundMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      getLoopingSoundManager(world)
         .ifPresent(
            loopingManager -> {
               SoundEvent event = message.getSound();
               Entity entity = world.getEntity(message.getEntityID());
               if (entity instanceof WitherStormEntity) {
                  loopingManager.putAdditionalSound(
                     message.getEntityID(),
                     new WitherStormSoundLoop((WitherStormEntity)entity, new Vec3(message.getX(), message.getY(), message.getZ()), event)
                  );
               }
            }
         );
   }

   public static void processRemoveAdditionalLoopingSoundMessage(RemoveAdditionalLoopingSoundMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      getLoopingSoundManager(world).ifPresent(loopingManager -> loopingManager.stopAdditional(message.getId()));
   }

   public static void processShakeScreenMessage(ShakeScreenMessage message) {
      Minecraft mc = Minecraft.getInstance();
      mc.player.getCapability(WitherStormModClientCapabilities.CAMERA_SHAKER).ifPresent(shaker -> shaker.shake(message.getDuration(), message.getPower()));
   }

   public static void processFormidibombExplosionMessage(FormidibombExplosionMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      Entity entity = world.getEntity(message.getId());
      FormidibombEntity.explode(world, entity, message.getRadius(), message.getSquish(), message.getX(), message.getY(), message.getZ());
      mc.player.getCapability(WitherStormModClientCapabilities.CAMERA_SHAKER).ifPresent(shaker -> shaker.shake(100.0F, 7.5F));
      if (Math.sqrt(mc.player.distanceToSqr(message.getX(), message.getY(), message.getZ())) <= 250.0) {
         mc.player.getCapability(WitherStormModClientCapabilities.SCREEN_BLINDER).ifPresent(blinder -> blinder.blind(260, 40, 240));
      }

      getBossThemeManager(world).ifPresent(manager -> manager.forceStop());
   }

   public static void processUpdateDamagingProjectileMessage(UpdateDamagingProjectileMessage message) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      if (world.getEntity(message.getEntityId()) instanceof AbstractHurtingProjectile projectile) {
         projectile.xPower = message.getXPower();
         projectile.yPower = message.getYPower();
         projectile.zPower = message.getZPower();
      }
   }

   public static void processBlindScreenMessage(BlindScreenMessage message) {
      Minecraft mc = Minecraft.getInstance();
      mc.player
         .getCapability(WitherStormModClientCapabilities.SCREEN_BLINDER)
         .ifPresent(blinder -> blinder.blind(message.getDuration(), message.getFadeInDuration(), message.getFadeOutDuration()));
   }

   public static void processSuperBeaconValidEffectsMessage(SuperBeaconValidEffectsMessage message) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.screen instanceof SuperBeaconScreen screen) {
         screen.setValidEffects(ImmutableSet.copyOf(message.getEffects()));
      }
   }

   public static void processUpdateDistantSuperBeaconMessage(UpdateDistantSuperBeaconMessage message) {
      Minecraft mc = Minecraft.getInstance();
      getDistantRenderer(mc.level)
         .ifPresent(
            renderer -> renderer.addAndOrUpdateSuperBeacon(
                  message.getPos(), message.getColor(), message.isActive(), message.getBeaconHeight(), message.getBeamWidth(), message.getOuterBeamWidth()
               )
         );
   }

   public static void processRemoveDistantSuperBeaconMessage(RemoveDistantSuperBeaconMessage message) {
      Minecraft mc = Minecraft.getInstance();
      getDistantRenderer(mc.level).ifPresent(renderer -> renderer.removeSuperBeacon(message.getPos()));
   }

   public static void processOnHeadAttackedMessage(OnHeadAttackedMessage message) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level.getEntity(message.getEntityId()) instanceof WitherStormEntity storm) {
         storm.getHeadManager().getHead(message.getHeadIndex()).handleHeadAttackedOnClient();
      }
   }

   private static LazyOptional<WitherStormDistantRenderer> getDistantRenderer(ClientLevel world) {
      return world.getCapability(WitherStormModClientCapabilities.DISTANT_RENDERER);
   }

   private static LazyOptional<WitherStormLoopingSoundManager> getLoopingSoundManager(ClientLevel world) {
      return world.getCapability(WitherStormModClientCapabilities.LOOPING_MANAGER);
   }

   private static LazyOptional<BossThemeManager> getBossThemeManager(ClientLevel world) {
      return world.getCapability(WitherStormModClientCapabilities.BOSS_THEME_MANAGER);
   }

   private static void purgeNonApplicable(ClientLevel level, DistantRendererMessage message) {
      getDistantRenderer(level).ifPresent(renderer -> {
         for (WitherStormEntity known : renderer.getKnown()) {
            if (!message.getApplicable().contains(known.getId())) {
               known.discard();
            }
         }
      });
   }
}
