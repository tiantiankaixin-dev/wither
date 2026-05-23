package nonamecrackers2.witherstormmod.common.event;

import // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.LevelTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;

public class ChunkLoadingBlockEntitiesEvents {
   @SubscribeEvent
   public static void onWorldTick(LevelTickEvent event) {
      event.level.getCapability(WitherStormModCapabilities.CHUNK_LOADING_BLOCK_ENTITIES).ifPresent(cap -> cap.tick());
   }
}
