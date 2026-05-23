package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
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

public class PhaseCommands {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            ((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("phase")
                     .then(
                        Commands.literal("set")
                           .then(
                              Commands.argument("witherstorm", EntityArgument.entity())
                                 .then(Commands.argument("phase", DoubleArgumentType.doubleArg(0.0)).executes(PhaseCommands::setPhase))
                           )
                     ))
                  .then(Commands.literal("get").then(Commands.argument("witherstorm", EntityArgument.entity()).executes(PhaseCommands::getPhase))))
               .then(
                  Commands.literal("evolve")
                     .then(
                        ((RequiredArgumentBuilder)Commands.argument("witherstorm", EntityArgument.entity()).executes(ctx -> evolve(ctx, false)))
                           .then(Commands.literal("force").executes(ctx -> evolve(ctx, true)))
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
            if (phase >= 1.1 && phase < 1.2) {
               setPhase = storm.setPhase(1, storm.adjustAmountForEvolutionSpeed(150));
            } else if (phase >= 1.2 && phase < 2.0) {
               setPhase = storm.setPhase(1, storm.adjustAmountForEvolutionSpeed(250));
            } else if (phase >= 2.1 && phase < 3.0) {
               setPhase = storm.setPhase(2, storm.adjustAmountForEvolutionSpeed(800));
            } else if (phase >= 3.1 && phase < 3.2) {
               setPhase = storm.setPhase(3, storm.adjustAmountForEvolutionSpeed(2350));
            } else if (phase >= 3.2 && phase < 4.0) {
               setPhase = storm.setPhase(3, storm.adjustAmountForEvolutionSpeed(3500));
            } else if (phase >= 4.5 && phase < 5.0) {
               setPhase = storm.setPhase(4, storm.getSubPhaseRequirement(4) + 1);
            } else if (phase >= 5.25 && phase < 5.5) {
               setPhase = storm.setPhase(5, storm.getSubPhaseRequirement(5) + 1);
            } else if (phase >= 5.5 && phase < 6.0) {
               setPhase = storm.setPhase(5, storm.getConsumptionAmountForPhase(5) + 1);
            } else if (phase >= 6.5 && phase < 7.0) {
               setPhase = storm.setPhase(6, storm.getSubPhaseRequirement(6) + 1);
            } else if (phase >= 7.5) {
               setPhase = storm.setPhase(7, storm.getConsumptionAmountForPhase(7) + 1);
            } else {
               setPhase = storm.setPhase(Mth.floor(phase));
            }

            if (!setPhase) {
               MutableComponent component = Component.translatable("commands.witherstormmod.setphase.invalid", new Object[]{phase, storm.getDisplayName()});
               source.sendFailure(component);
            } else {
               source.sendSuccess(() -> Component.translatable("commands.witherstormmod.setphase.success", new Object[]{phase, storm.getDisplayName()}), true);
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

   private static int getPhase(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      int phase = -1;
      if (entity instanceof WitherStormEntity storm) {
         phase = storm.getPhase();
         source.sendSuccess(() -> Component.translatable("commands.witherstormmod.getphase.result", new Object[]{storm.getDisplayName(), storm.getPhase()}), false);
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return phase;
   }

   private static int evolve(CommandContext<CommandSourceStack> context, boolean force) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      int phase = 0;
      if (entity instanceof WitherStormEntity storm) {
         if (storm.evolve(force)) {
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.evolve.success", new Object[]{storm.getDisplayName(), storm.getPhase()}), true);
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.evolve.fail"));
         }

         phase = storm.getPhase();
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return phase;
   }
}
