package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import nonamecrackers2.crackerslib.common.packet.PacketUtil;
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
   private static final String PROTOCOL_VERSION = "4.0";
   public static final SimpleChannel MAIN = NetworkRegistry.newSimpleChannel(
      new ResourceLocation("witherstormmod", "main"), () -> "4.0", "4.0"::equals, "4.0"::equals
   );

   public static void registerPackets() {
      PacketUtil.registerToClient(MAIN, PlayerMotionMessage.class);
      PacketUtil.registerToClient(MAIN, GlobalSoundMessage.class);
      PacketUtil.registerToClient(MAIN, WitherStormBodyController.UpdateBodyRotMessage.class);
      PacketUtil.registerToClient(MAIN, WitherStormToDistantRendererMessage.class);
      PacketUtil.registerToClient(MAIN, RemoveStormFromDistantRendererMessage.class);
      PacketUtil.registerToClient(MAIN, UpdateStormPositionMessage.class);
      PacketUtil.registerToClient(MAIN, StormTeleportMessage.class);
      PacketUtil.registerToClient(MAIN, UpdateStormVelocityMessage.class);
      PacketUtil.registerToClient(MAIN, UpdateStormHeadLookMessage.class);
      PacketUtil.registerToClient(MAIN, StormMetadataMessage.class);
      PacketUtil.registerToClient(MAIN, StormAttributesMessage.class);
      PacketUtil.registerToClient(MAIN, CreateLoopingSoundMessage.class);
      PacketUtil.registerToClient(MAIN, StormSoundPositionMessage.class);
      PacketUtil.registerToClient(MAIN, RemoveSoundLoopMessage.class);
      PacketUtil.registerToClient(MAIN, NotifyHeadInjuryMessage.class);
      PacketUtil.registerToClient(MAIN, UpdateEffectInstanceMessage.class);
      PacketUtil.registerToClient(MAIN, UpdateWitherSicknessTrackerMessage.class);
      PacketUtil.registerToClient(MAIN, UpdatePlayDeadManagerMessage.class);
      PacketUtil.registerToClient(MAIN, CreateDebrisMessage.class);
      PacketUtil.registerToClient(MAIN, EntitySyncableDataMessage.class);
      PacketUtil.registerToClient(MAIN, PlayAdditionalLoopingSoundMessage.class);
      PacketUtil.registerToClient(MAIN, RemoveAdditionalLoopingSoundMessage.class);
      PacketUtil.registerToClient(MAIN, ShakeScreenMessage.class);
      PacketUtil.registerToClient(MAIN, FormidibombExplosionMessage.class);
      PacketUtil.registerToClient(MAIN, UpdateDamagingProjectileMessage.class);
      PacketUtil.registerToClient(MAIN, WitheredSymbiontEntity.SetSpellTimeMessage.class);
      PacketUtil.registerToClient(MAIN, CommandBlockEntity.ModeAnimationMessage.class);
      PacketUtil.registerToClient(MAIN, TentacleEntity.UpdateAnimationMessage.class);
      PacketUtil.registerToClient(MAIN, BlindScreenMessage.class);
      PacketUtil.registerToClient(MAIN, SuperBeaconValidEffectsMessage.class);
      PacketUtil.registerToClient(MAIN, UpdateDistantSuperBeaconMessage.class);
      PacketUtil.registerToClient(MAIN, RemoveDistantSuperBeaconMessage.class);
      PacketUtil.registerToClient(MAIN, OnHeadAttackedMessage.class);
      PacketUtil.registerToServer(MAIN, InjureHeadMessage.class);
      PacketUtil.registerToServer(MAIN, SuperBeaconSetEffectMessage.class);
      PacketUtil.registerToServer(MAIN, SuperBeaconToggleAreaMessage.class);
   }
}
