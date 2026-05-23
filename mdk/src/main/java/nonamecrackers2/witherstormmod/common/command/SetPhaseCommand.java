package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.EvolutionProfiler;

public class SetPhaseCommand {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            Commands.literal("phase")
               .then(
                  Commands.literal("set")
                     .then(
                        Commands.argument("witherstorm", EntityArgument.entity())
                           .then(Commands.argument("phase", DoubleArgumentType.doubleArg(0.0)).executes(SetPhaseCommand::setPhase))
                     )
               )
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int setPhase(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      double phase = DoubleArgumentType.getDouble(context, "phase");
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            boolean setPhase = false;
            if (phase == 4.5) {
               setPhase = storm.setPhase(4);
               if (setPhase) {
                  storm.setConsumedEntities(storm.getSubPhaseRequirement(4) + 1);
               }
            } else if (phase == 5.25) {
               setPhase = storm.setPhase(5);
               if (setPhase) {
                  storm.setConsumedEntities(storm.getSubPhaseRequirement(5) + 1);
               }
            } else if (phase == 5.5) {
               setPhase = storm.setPhase(5);
               if (setPhase) {
                  storm.setConsumedEntities(storm.getConsumptionAmountForPhase(5) + 1);
               }
            } else if (phase == 6.5) {
               setPhase = storm.setPhase(6);
               if (setPhase) {
                  storm.setConsumedEntities(storm.getSubPhaseRequirement(6) + 1);
               }
            } else if (phase == 7.5) {
               setPhase = storm.setPhase(7);
               if (setPhase) {
                  storm.setConsumedEntities(storm.getConsumptionAmountForPhase(7) + 1);
               }
            } else {
               int intPhase = Mth.floor(phase);
               setPhase = storm.setPhase(intPhase);
            }

            if (!setPhase) {
               MutableComponent component = Component.translatable("commands.witherstormmod.setphase.invalid", new Object[]{phase, storm.getDisplayName()});
               source.sendFailure(component);
            } else {
               MutableComponent component = Component.translatable("commands.witherstormmod.setphase.success", new Object[]{phase, storm.getDisplayName()});
               source.sendSuccess(() -> component, true);
               EvolutionProfiler profiler = storm.getEvolutionProfiler();
               if (profiler.isProfiling()) {
                  profiler.begin();
               }
            }
         } else {
            MutableComponent component = Component.translatable("commands.witherstormmod.entity.arg.invalid");
            source.sendFailure(component);
         }
      }

      return 0;
   }
}
