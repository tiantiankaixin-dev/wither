package nonamecrackers2.witherstormmod.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerContainerEvent.Open;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.blockentity.inventory.AbstractSuperBeaconMenu;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.SuperBeaconValidEffectsMessage;

public class SuperBeaconEvents {
   @SubscribeEvent
   public static void onPlayerOpenContainer(Open event) {
      if (event.getContainer() instanceof AbstractSuperBeaconMenu menu) {
         WitherStormModPacketHandlers.MAIN
            .send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)event.getEntity()), new SuperBeaconValidEffectsMessage(menu.getValidEffects()));
      }
   }
}
