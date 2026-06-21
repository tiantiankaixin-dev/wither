package nonamecrackers2.witherstormmod.common.init;

import java.lang.reflect.InvocationTargetException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor.PacketTarget;
import net.minecraftforge.network.SimpleChannel;
import nonamecrackers2.witherstormmod.common.network.LegacyNetworkEvent;
import nonamecrackers2.witherstormmod.common.network.Packet;
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
   private static final int PROTOCOL_VERSION = 4;
   private static int packetId;
   public static final WitherStormModPacketHandlers.LegacyChannel MAIN = new WitherStormModPacketHandlers.LegacyChannel(
      ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath("witherstormmod", "main"))
         .networkProtocolVersion(PROTOCOL_VERSION)
         .acceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
         .simpleChannel()
   );

   public static void registerPackets() {
      registerToClient(PlayerMotionMessage.class);
      registerToClient(GlobalSoundMessage.class);
      registerToClient(WitherStormBodyController.UpdateBodyRotMessage.class);
      registerToClient(WitherStormToDistantRendererMessage.class);
      registerToClient(RemoveStormFromDistantRendererMessage.class);
      registerToClient(UpdateStormPositionMessage.class);
      registerToClient(StormTeleportMessage.class);
      registerToClient(UpdateStormVelocityMessage.class);
      registerToClient(UpdateStormHeadLookMessage.class);
      registerToClient(StormMetadataMessage.class);
      registerToClient(StormAttributesMessage.class);
      registerToClient(CreateLoopingSoundMessage.class);
      registerToClient(StormSoundPositionMessage.class);
      registerToClient(RemoveSoundLoopMessage.class);
      registerToClient(NotifyHeadInjuryMessage.class);
      registerToClient(UpdateEffectInstanceMessage.class);
      registerToClient(UpdateWitherSicknessTrackerMessage.class);
      registerToClient(UpdatePlayDeadManagerMessage.class);
      registerToClient(CreateDebrisMessage.class);
      registerToClient(EntitySyncableDataMessage.class);
      registerToClient(PlayAdditionalLoopingSoundMessage.class);
      registerToClient(RemoveAdditionalLoopingSoundMessage.class);
      registerToClient(ShakeScreenMessage.class);
      registerToClient(FormidibombExplosionMessage.class);
      registerToClient(UpdateDamagingProjectileMessage.class);
      registerToClient(WitheredSymbiontEntity.SetSpellTimeMessage.class);
      registerToClient(CommandBlockEntity.ModeAnimationMessage.class);
      registerToClient(TentacleEntity.UpdateAnimationMessage.class);
      registerToClient(BlindScreenMessage.class);
      registerToClient(SuperBeaconValidEffectsMessage.class);
      registerToClient(UpdateDistantSuperBeaconMessage.class);
      registerToClient(RemoveDistantSuperBeaconMessage.class);
      registerToClient(OnHeadAttackedMessage.class);
      registerToServer(InjureHeadMessage.class);
      registerToServer(SuperBeaconSetEffectMessage.class);
      registerToServer(SuperBeaconToggleAreaMessage.class);
   }

   private static <T extends Packet> void registerToClient(Class<T> type) {
      register(type, PacketFlow.CLIENTBOUND);
   }

   private static <T extends Packet> void registerToServer(Class<T> type) {
      register(type, PacketFlow.SERVERBOUND);
   }

   private static <T extends Packet> void register(Class<T> type, PacketFlow flow) {
      MAIN.delegate
         .messageBuilder(type, packetId++)
         .direction(flow)
         .encoder(Packet::encodeCheck)
         .decoder(buffer -> Packet.decode(() -> createPacket(type), buffer))
         .consumerMainThread((message, context) -> message.getProcessor(new LegacyNetworkEvent.Context(context)).run())
         .add();
   }

   private static <T extends Packet> T createPacket(Class<T> type) {
      try {
         return type.getDeclaredConstructor().newInstance();
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
         throw new IllegalStateException("Could not create packet " + type.getName(), exception);
      }
   }

   public static class LegacyChannel {
      private final SimpleChannel delegate;

      private LegacyChannel(SimpleChannel delegate) {
         this.delegate = delegate;
      }

      public <MSG> void send(PacketTarget target, MSG message) {
         this.delegate.send(message, target);
      }

      public <MSG> void sendToServer(MSG message) {
         this.delegate.send(message, net.minecraftforge.network.PacketDistributor.SERVER.noArg());
      }
   }
}
