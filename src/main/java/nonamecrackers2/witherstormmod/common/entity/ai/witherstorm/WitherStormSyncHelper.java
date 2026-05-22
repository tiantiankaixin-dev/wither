package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.PacketTarget;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.CreateDebrisMessage;
import nonamecrackers2.witherstormmod.common.packet.CreateLoopingSoundMessage;
import nonamecrackers2.witherstormmod.common.packet.RemoveSoundLoopMessage;
import nonamecrackers2.witherstormmod.common.packet.RemoveStormFromDistantRendererMessage;
import nonamecrackers2.witherstormmod.common.packet.WitherStormToDistantRendererMessage;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class WitherStormSyncHelper {
   @SubscribeEvent
   public static void onPlayerJoin(PlayerLoggedInEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         sendWitherStormsToPlayer(player);
      }
   }

   @SubscribeEvent
   public static void onPlayerChangeDimensions(PlayerChangedDimensionEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         sendWitherStormsToPlayer(player);
      }
   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerRespawnEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         sendWitherStormsToPlayer(player);
      }
   }

   @SubscribeEvent
   public static void onPlayerStartTracking(StartTracking event) {
      Entity target = event.getTarget();
      if (event.getEntity() instanceof ServerPlayer player && target instanceof WitherStormEntity storm) {
         PacketTarget packetTarget = PacketDistributor.PLAYER.with(() -> player);
         storm.getPlayDeadManager().sendChanges(packetTarget, false);
         WitherStormModPacketHandlers.MAIN.send(packetTarget, new CreateDebrisMessage(storm, storm.isDeadOrPlayingDead()));
      }
   }

   private static void sendWitherStormsToPlayer(ServerPlayer player) {
      WorldUtil.getAllStorms(player.serverLevel()).forEach(storm -> sendWitherStormToClient(PacketDistributor.PLAYER.with(() -> player), storm));
   }

   public static void sendWitherStormToClient(PacketTarget target, WitherStormEntity storm) {
      WitherStormModPacketHandlers.MAIN.send(target, new WitherStormToDistantRendererMessage(WorldUtil.getStormIds(storm), storm));
      if (storm.shouldPlaySoundLoops()) {
         CreateLoopingSoundMessage message = new CreateLoopingSoundMessage(storm);
         WitherStormModPacketHandlers.MAIN.send(target, message);
      }

      storm.getPlayDeadManager().sendChanges(target, false);
      CreateDebrisMessage message = new CreateDebrisMessage(storm, storm.isDeadOrPlayingDead());
      WitherStormModPacketHandlers.MAIN.send(target, message);
   }

   public static void sendWitherStormToClient(WitherStormEntity storm) {
      sendWitherStormToClient(PacketDistributor.DIMENSION.with(storm.level()::dimension), storm);
   }

   public static void removeWitherStorm(PacketTarget target, WitherStormEntity storm) {
      WitherStormModPacketHandlers.MAIN.send(target, new RemoveStormFromDistantRendererMessage(WorldUtil.getStormIds(storm), storm));
      if (storm.shouldPlaySoundLoop) {
         RemoveSoundLoopMessage message = new RemoveSoundLoopMessage(storm);
         WitherStormModPacketHandlers.MAIN.send(target, message);
      }
   }

   public static void removeWitherStorm(WitherStormEntity storm) {
      removeWitherStorm(PacketDistributor.DIMENSION.with(storm.level()::dimension), storm);
   }
}
