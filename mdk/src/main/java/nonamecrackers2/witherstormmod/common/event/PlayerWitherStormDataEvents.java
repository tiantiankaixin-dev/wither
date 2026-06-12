package nonamecrackers2.witherstormmod.common.event;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.Clone;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.capability.PlayerWitherStormData;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;

public class PlayerWitherStormDataEvents {
   @SubscribeEvent
   public static void onPlayerTick(PlayerTickEvent.Post event) {
      Player player = event.getEntity();
      player.getData(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA.get()).tick();
   }

   @SubscribeEvent
   public static void onPlayerClone(Clone event) {
      if (event.isWasDeath()) {
         Player original = event.getOriginal();
         Player player = event.getEntity();
         original.reviveCaps();
         PlayerWitherStormData oldData = original.getData(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA.get());
         player.getData(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA.get()).copyFrom(oldData);
         original.invalidateCaps();
      }
   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerRespawnEvent event) {
      event.getEntity().getData(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA.get()).makeInvulnerable(600);
   }
}