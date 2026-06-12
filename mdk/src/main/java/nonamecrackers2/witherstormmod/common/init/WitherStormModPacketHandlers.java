package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.crackerslib.common.packet.PacketUtil;
import nonamecrackers2.crackerslib.common.packet.SimpleChannel;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.TentacleEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.controller.WitherStormBodyController;
import nonamecrackers2.witherstormmod.common.packet.BlindScreenMessage;
import nonamecrackers2.witherstormmod.common.packet.CreateDebrisMessage;
import nonamecrackers2.witherstormmod.common.packet.CreateLoopingSoundMessage;
import nonamecrackers2.witherstormmod.common.packet.EntitySyncableDataMessage;
import nonamecrackers2.witherstormmod.common.packet.FormidibombExplosionMessage;
import nonamecrackers2.witherstormmod.common.packet.GlobalSoundMessage;
import nonamecrackers2.witherstormmod.common.packet.InjureHeadMessage;
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
import nonamecrackers2.witherstormmod.common.packet.SuperBeaconSetEffectMessage;
import nonamecrackers2.witherstormmod.common.packet.SuperBeaconToggleAreaMessage;
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

public class WitherStormModPacketHandlers {
   private static final String MOD_ID = "witherstormmod";

   private static final java.util.Map<Class<?>, String> PACKET_NAMES = new java.util.HashMap<>();
   static {
      PACKET_NAMES.put(PlayerMotionMessage.class, "player_motion");
      PACKET_NAMES.put(GlobalSoundMessage.class, "global_sound");
      PACKET_NAMES.put(WitherStormBodyController.UpdateBodyRotMessage.class, "update_body_rot");
      PACKET_NAMES.put(WitherStormToDistantRendererMessage.class, "storm_to_distant");
      PACKET_NAMES.put(RemoveStormFromDistantRendererMessage.class, "remove_storm_distant");
      PACKET_NAMES.put(UpdateStormPositionMessage.class, "update_storm_pos");
      PACKET_NAMES.put(StormTeleportMessage.class, "storm_teleport");
      PACKET_NAMES.put(UpdateStormVelocityMessage.class, "update_storm_vel");
      PACKET_NAMES.put(UpdateStormHeadLookMessage.class, "update_storm_head_look");
      PACKET_NAMES.put(StormMetadataMessage.class, "storm_metadata");
      PACKET_NAMES.put(StormAttributesMessage.class, "storm_attributes");
      PACKET_NAMES.put(CreateLoopingSoundMessage.class, "create_looping_sound");
      PACKET_NAMES.put(StormSoundPositionMessage.class, "storm_sound_pos");
      PACKET_NAMES.put(RemoveSoundLoopMessage.class, "remove_sound_loop");
      PACKET_NAMES.put(NotifyHeadInjuryMessage.class, "notify_head_injury");
      PACKET_NAMES.put(UpdateEffectInstanceMessage.class, "update_effect_instance");
      PACKET_NAMES.put(UpdateWitherSicknessTrackerMessage.class, "update_sickness_tracker");
      PACKET_NAMES.put(UpdatePlayDeadManagerMessage.class, "update_play_dead");
      PACKET_NAMES.put(CreateDebrisMessage.class, "create_debris");
      PACKET_NAMES.put(EntitySyncableDataMessage.class, "entity_syncable_data");
      PACKET_NAMES.put(PlayAdditionalLoopingSoundMessage.class, "play_additional_loop");
      PACKET_NAMES.put(RemoveAdditionalLoopingSoundMessage.class, "remove_additional_loop");
      PACKET_NAMES.put(ShakeScreenMessage.class, "shake_screen");
      PACKET_NAMES.put(FormidibombExplosionMessage.class, "formidibomb_explosion");
      PACKET_NAMES.put(UpdateDamagingProjectileMessage.class, "update_damaging_proj");
      PACKET_NAMES.put(WitheredSymbiontEntity.SetSpellTimeMessage.class, "set_spell_time");
      PACKET_NAMES.put(CommandBlockEntity.ModeAnimationMessage.class, "mode_animation");
      PACKET_NAMES.put(TentacleEntity.UpdateAnimationMessage.class, "update_tentacle_anim");
      PACKET_NAMES.put(BlindScreenMessage.class, "blind_screen");
      PACKET_NAMES.put(SuperBeaconValidEffectsMessage.class, "super_beacon_effects");
      PACKET_NAMES.put(UpdateDistantSuperBeaconMessage.class, "update_distant_beacon");
      PACKET_NAMES.put(RemoveDistantSuperBeaconMessage.class, "remove_distant_beacon");
      PACKET_NAMES.put(OnHeadAttackedMessage.class, "on_head_attacked");
      PACKET_NAMES.put(InjureHeadMessage.class, "injure_head");
      PACKET_NAMES.put(SuperBeaconSetEffectMessage.class, "super_beacon_set_effect");
      PACKET_NAMES.put(SuperBeaconToggleAreaMessage.class, "super_beacon_toggle_area");
   }

   public static final SimpleChannel MAIN = new SimpleChannel(MOD_ID, PACKET_NAMES);

   public static void registerPackets(RegisterPayloadHandlersEvent event) {
      PayloadRegistrar registrar = event.registrar(MOD_ID).versioned("4.0");
      PacketUtil.registerToClient(registrar, MOD_ID, "player_motion", PlayerMotionMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "global_sound", GlobalSoundMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "update_body_rot", WitherStormBodyController.UpdateBodyRotMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "storm_to_distant", WitherStormToDistantRendererMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "remove_storm_distant", RemoveStormFromDistantRendererMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "update_storm_pos", UpdateStormPositionMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "storm_teleport", StormTeleportMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "update_storm_vel", UpdateStormVelocityMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "update_storm_head_look", UpdateStormHeadLookMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "storm_metadata", StormMetadataMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "storm_attributes", StormAttributesMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "create_looping_sound", CreateLoopingSoundMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "storm_sound_pos", StormSoundPositionMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "remove_sound_loop", RemoveSoundLoopMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "notify_head_injury", NotifyHeadInjuryMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "update_effect_instance", UpdateEffectInstanceMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "update_sickness_tracker", UpdateWitherSicknessTrackerMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "update_play_dead", UpdatePlayDeadManagerMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "create_debris", CreateDebrisMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "entity_syncable_data", EntitySyncableDataMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "play_additional_loop", PlayAdditionalLoopingSoundMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "remove_additional_loop", RemoveAdditionalLoopingSoundMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "shake_screen", ShakeScreenMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "formidibomb_explosion", FormidibombExplosionMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "update_damaging_proj", UpdateDamagingProjectileMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "set_spell_time", WitheredSymbiontEntity.SetSpellTimeMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "mode_animation", CommandBlockEntity.ModeAnimationMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "update_tentacle_anim", TentacleEntity.UpdateAnimationMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "blind_screen", BlindScreenMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "super_beacon_effects", SuperBeaconValidEffectsMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "update_distant_beacon", UpdateDistantSuperBeaconMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "remove_distant_beacon", RemoveDistantSuperBeaconMessage.class);
      PacketUtil.registerToClient(registrar, MOD_ID, "on_head_attacked", OnHeadAttackedMessage.class);
      PacketUtil.registerToServer(registrar, MOD_ID, "injure_head", InjureHeadMessage.class);
      PacketUtil.registerToServer(registrar, MOD_ID, "super_beacon_set_effect", SuperBeaconSetEffectMessage.class);
      PacketUtil.registerToServer(registrar, MOD_ID, "super_beacon_toggle_area", SuperBeaconToggleAreaMessage.class);
   }

   private static String getPacketName(Class<?> clazz) {
      return PACKET_NAMES.getOrDefault(clazz, clazz.getSimpleName().toLowerCase());
   }
}
