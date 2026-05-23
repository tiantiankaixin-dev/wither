package nonamecrackers2.witherstormmod.common.init;

import net.neoforged.neoforge.common.NeoForge;
import nonamecrackers2.witherstormmod.common.capability.WitherStormBowelsManager;
import nonamecrackers2.witherstormmod.common.capability.WitherStormModChunkLoader;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.WitherStormSyncHelper;
import nonamecrackers2.witherstormmod.common.event.AnvilRecipeEvents;
import nonamecrackers2.witherstormmod.common.event.ChunkLoadingBlockEntitiesEvents;
import nonamecrackers2.witherstormmod.common.event.EntityConversionEvents;
import nonamecrackers2.witherstormmod.common.event.EntitySyncableDataEvents;
import nonamecrackers2.witherstormmod.common.event.InjectCustomGoalsEvents;
import nonamecrackers2.witherstormmod.common.event.PlayerWitherStormDataEvents;
import nonamecrackers2.witherstormmod.common.event.SuperBeaconEvents;
import nonamecrackers2.witherstormmod.common.event.WitherSicknessEvents;
import nonamecrackers2.witherstormmod.common.event.WitherStormEvents;
import nonamecrackers2.witherstormmod.common.event.WitherStormModCompatEvents;
import nonamecrackers2.witherstormmod.common.event.WitherStormModRegisterCommands;
import nonamecrackers2.witherstormmod.common.event.WitherStormModUtilEvents;
import nonamecrackers2.witherstormmod.common.event.WitherStormPatternChecker;

public class WitherStormModEvents {
   public static void registerEvents() {
      NeoForge.EVENT_BUS.register(WitherStormPatternChecker.class);
      NeoForge.EVENT_BUS.register(WitherStormModRegisterCommands.class);
      NeoForge.EVENT_BUS.register(WitherSicknessEvents.class);
      NeoForge.EVENT_BUS.register(WitherStormEvents.class);
      NeoForge.EVENT_BUS.register(EntityConversionEvents.class);
      NeoForge.EVENT_BUS.register(EntitySyncableDataEvents.class);
      NeoForge.EVENT_BUS.register(PlayerWitherStormDataEvents.class);
      NeoForge.EVENT_BUS.register(WitherStormBowelsManager.class);
      NeoForge.EVENT_BUS.register(InjectCustomGoalsEvents.class);
      NeoForge.EVENT_BUS.register(WitherStormModUtilEvents.class);
      NeoForge.EVENT_BUS.register(SuperBeaconEvents.class);
      NeoForge.EVENT_BUS.register(ChunkLoadingBlockEntitiesEvents.class);
      NeoForge.EVENT_BUS.register(WitherStormModCompatEvents.class);
      NeoForge.EVENT_BUS.register(AnvilRecipeEvents.class);
      NeoForge.EVENT_BUS.register(WitherStormModChunkLoader.Events.class);
      NeoForge.EVENT_BUS.register(WitherStormSyncHelper.class);
   }
}
