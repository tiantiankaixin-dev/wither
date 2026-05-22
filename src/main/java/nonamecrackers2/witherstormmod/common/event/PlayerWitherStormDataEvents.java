package nonamecrackers2.witherstormmod.common.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.capability.PlayerWitherStormData;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;

public class PlayerWitherStormDataEvents {
   @SubscribeEvent
   public static void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.END) {
         Player player = event.player;
         player.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA).ifPresent(data -> data.tick());
      }
   }

   @SubscribeEvent
   public static void onPlayerClone(Clone event) {
      if (event.isWasDeath()) {
         Player original = event.getOriginal();
         Player player = event.getEntity();
         original.reviveCaps();
         LazyOptional<PlayerWitherStormData> optional = original.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA);
         if (optional.isPresent()) {
            PlayerWitherStormData oldData = (PlayerWitherStormData)optional.resolve().get();
            player.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA).ifPresent(data -> data.copyFrom(oldData));
         }

         original.invalidateCaps();
      }
   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerRespawnEvent event) {
      event.getEntity().getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA).ifPresent(manager -> manager.makeInvulnerable(600));
   }
}
