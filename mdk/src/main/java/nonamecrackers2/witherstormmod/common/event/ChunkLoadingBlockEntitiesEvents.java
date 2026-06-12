package nonamecrackers2.witherstormmod.common.event;


import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;

public class ChunkLoadingBlockEntitiesEvents {
   @SubscribeEvent
   public static void onWorldTick(LevelTickEvent event) {
            event.getLevel().getData(WitherStormModCapabilities.CHUNK_LOADING_BLOCK_ENTITIES.get()).tick();
   }
}
