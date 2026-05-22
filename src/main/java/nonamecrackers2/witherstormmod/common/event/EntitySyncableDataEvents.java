package nonamecrackers2.witherstormmod.common.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.EntitySyncableDataMessage;
import nonamecrackers2.witherstormmod.common.util.EntitySyncableData;

public class EntitySyncableDataEvents {
   @SubscribeEvent
   public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
      sendChanges((ServerPlayer)event.getEntity());
   }

   @SubscribeEvent
   public static void onChangedDimensions(PlayerChangedDimensionEvent event) {
      sendChanges((ServerPlayer)event.getEntity());
   }

   @SubscribeEvent
   public static void onRespawn(PlayerRespawnEvent event) {
      sendChanges((ServerPlayer)event.getEntity());
   }

   @SubscribeEvent
   public static void onPlayerStartTracking(StartTracking event) {
      sendChanges((ServerPlayer)event.getEntity(), event.getTarget());
   }

   public static void sendChanges(ServerPlayer player) {
      ServerLevel world = (ServerLevel)player.level();

      for (Entity entity : world.getAllEntities()) {
         sendChanges(player, entity);
      }
   }

   public static void sendChanges(ServerPlayer player, Entity entity) {
      if (entity instanceof EntitySyncableData) {
         EntitySyncableDataMessage message = new EntitySyncableDataMessage(entity.getId(), (EntitySyncableData)entity);
         WitherStormModPacketHandlers.MAIN.send(PacketDistributor.PLAYER.with(() -> player), message);
      }
   }
}
