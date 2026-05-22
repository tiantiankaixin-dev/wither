package nonamecrackers2.witherstormmod.common.init;

import net.minecraftforge.common.MinecraftForge;
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
      MinecraftForge.EVENT_BUS.register(WitherStormPatternChecker.class);
      MinecraftForge.EVENT_BUS.register(WitherStormModRegisterCommands.class);
      MinecraftForge.EVENT_BUS.register(WitherSicknessEvents.class);
      MinecraftForge.EVENT_BUS.register(WitherStormEvents.class);
      MinecraftForge.EVENT_BUS.register(EntityConversionEvents.class);
      MinecraftForge.EVENT_BUS.register(EntitySyncableDataEvents.class);
      MinecraftForge.EVENT_BUS.register(PlayerWitherStormDataEvents.class);
      MinecraftForge.EVENT_BUS.register(WitherStormBowelsManager.class);
      MinecraftForge.EVENT_BUS.register(InjectCustomGoalsEvents.class);
      MinecraftForge.EVENT_BUS.register(WitherStormModUtilEvents.class);
      MinecraftForge.EVENT_BUS.register(SuperBeaconEvents.class);
      MinecraftForge.EVENT_BUS.register(ChunkLoadingBlockEntitiesEvents.class);
      MinecraftForge.EVENT_BUS.register(WitherStormModCompatEvents.class);
      MinecraftForge.EVENT_BUS.register(AnvilRecipeEvents.class);
      MinecraftForge.EVENT_BUS.register(WitherStormModChunkLoader.Events.class);
      MinecraftForge.EVENT_BUS.register(WitherStormSyncHelper.class);
   }
}
