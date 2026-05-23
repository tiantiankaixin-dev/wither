package nonamecrackers2.witherstormmod.common.event;

import net.minecraft.world.entity.player.Player;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: LazyOptional removed, new Capability API returns T or null
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.Phase
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.PlayerTickEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent.Clone;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.neoforged.bus.api.SubscribeEvent;
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
