package nonamecrackers2.crackerslib.common.packet;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;

/**
 * Compatibility proxy that mimics the old SimpleChannel.send() API
 * on top of NeoForge 1.21.1's PacketDistributor static methods.
 * 
 * Call sites use: MAIN.send(target, message) or MAIN.sendToServer(message)
 * where target is a PacketTarget created by this class's static helpers.
 */
public class SimpleChannel {
   private final String modId;
   private final Map<Class<?>, String> packetNames;

   public SimpleChannel(String modId, Map<Class<?>, String> packetNames) {
      this.modId = modId;
      this.packetNames = packetNames;
   }

   @SuppressWarnings("unchecked")
   public <T extends Packet> void send(PacketTarget target, T message) {
      CustomPacketPayload.Type<PacketUtil.PacketPayload<T>> type = makeType(message);
      PacketUtil.PacketPayload<T> payload = new PacketUtil.PacketPayload<>(message, type);
      target.send(payload);
   }

   public <T extends Packet> void sendToServer(T message) {
      CustomPacketPayload.Type<PacketUtil.PacketPayload<T>> type = makeType(message);
      PacketUtil.PacketPayload<T> payload = new PacketUtil.PacketPayload<>(message, type);
      PacketDistributor.sendToServer(payload);
   }

   @SuppressWarnings("unchecked")
   private <T extends Packet> CustomPacketPayload.Type<PacketUtil.PacketPayload<T>> makeType(T message) {
      String name = packetNames.getOrDefault(message.getClass(), message.getClass().getSimpleName().toLowerCase());
      return new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(modId, name));
   }

   // --- PacketTarget factories (mimic old PacketDistributor usage) ---

   public static PacketTarget toPlayer(ServerPlayer player) {
      return payload -> PacketDistributor.sendToPlayer(player, payload);
   }

   public static PacketTarget toNear(ServerLevel level, double x, double y, double z, double radius) {
      return payload -> PacketDistributor.sendToPlayersNear(level, null, x, y, z, radius, payload);
   }

   public static PacketTarget toAll(ServerLevel level) {
      return payload -> PacketDistributor.sendToPlayersInDimension(level, payload);
   }

   public static PacketTarget toTracking(net.minecraft.world.entity.Entity entity) {
      return payload -> PacketDistributor.sendToPlayersTrackingEntity(entity, payload);
   }

   public static PacketTarget toTrackingAndSelf(net.minecraft.world.entity.Entity entity) {
      return payload -> PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, payload);
   }

   public static PacketTarget toAllPlayers() {
      return payload -> PacketDistributor.sendToAllPlayers(payload);
   }

   public static PacketTarget toDimension(ServerLevel level) {
      return payload -> PacketDistributor.sendToPlayersInDimension(level, payload);
   }

   /**
    * Functional interface for packet dispatch targets.
    */
   @FunctionalInterface
   public interface PacketTarget {
      void send(CustomPacketPayload payload);
   }
}
