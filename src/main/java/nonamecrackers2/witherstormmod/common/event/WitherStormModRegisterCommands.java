package nonamecrackers2.witherstormmod.common.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.loading.FMLEnvironment;
import nonamecrackers2.crackerslib.common.command.ConfigCommandBuilder;
import nonamecrackers2.witherstormmod.common.command.BowelsCommands;
import nonamecrackers2.witherstormmod.common.command.ChunkLoaderCommands;
import nonamecrackers2.witherstormmod.common.command.ConsumedEntitiesCommands;
import nonamecrackers2.witherstormmod.common.command.ConversionCommands;
import nonamecrackers2.witherstormmod.common.command.CreateClusterCommand;
import nonamecrackers2.witherstormmod.common.command.DebugCommands;
import nonamecrackers2.witherstormmod.common.command.DoDeathSequenceCommand;
import nonamecrackers2.witherstormmod.common.command.ExplodeStormCommand;
import nonamecrackers2.witherstormmod.common.command.PhaseCommands;
import nonamecrackers2.witherstormmod.common.command.RandomizeSicknessModifiersCommand;
import nonamecrackers2.witherstormmod.common.command.ReviveStormCommand;
import nonamecrackers2.witherstormmod.common.command.SetEvolutionAttributeCommand;
import nonamecrackers2.witherstormmod.common.command.ShakeScreenCommand;
import nonamecrackers2.witherstormmod.common.command.StartCureCommand;
import nonamecrackers2.witherstormmod.common.command.StartInfectionCommand;
import nonamecrackers2.witherstormmod.common.command.TractorBeamCommands;
import nonamecrackers2.witherstormmod.common.command.UltimateTargetCommands;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;

public class WitherStormModRegisterCommands {
   @SubscribeEvent
   public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
      CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
      PhaseCommands.register(dispatcher);
      ConsumedEntitiesCommands.register(dispatcher);
      CreateClusterCommand.register(dispatcher);
      SetEvolutionAttributeCommand.register(dispatcher);
      StartInfectionCommand.register(dispatcher);
      StartCureCommand.register(dispatcher);
      ExplodeStormCommand.register(dispatcher);
      ReviveStormCommand.register(dispatcher);
      BowelsCommands.register(dispatcher);
      UltimateTargetCommands.register(dispatcher);
      RandomizeSicknessModifiersCommand.register(dispatcher);
      DoDeathSequenceCommand.register(dispatcher);
      TractorBeamCommands.register(dispatcher);
      ChunkLoaderCommands.register(dispatcher);
      ConversionCommands.register(dispatcher);
      ShakeScreenCommand.register(dispatcher);
      if (!FMLEnvironment.production) {
         DebugCommands.register(dispatcher);
      }

      ConfigCommandBuilder.builder(event.getDispatcher(), "witherstormmod").addSpec(Type.SERVER, WitherStormModConfig.SERVER_SPEC).register();
   }
}
