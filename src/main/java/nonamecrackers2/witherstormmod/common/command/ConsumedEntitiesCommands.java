package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class ConsumedEntitiesCommands {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("consumedEntities")
                        .then(
                           Commands.literal("set")
                              .then(
                                 ((RequiredArgumentBuilder)Commands.argument("witherstorm", EntityArgument.entity())
                                       .then(
                                          Commands.argument("amount", IntegerArgumentType.integer(0))
                                             .executes(ctx -> setConsumedEntities(ctx, IntegerArgumentType.getInteger(ctx, "amount")))
                                       ))
                                    .then(Commands.literal("blackhole").executes(ctx -> setConsumedEntities(ctx, 16000)))
                              )
                        ))
                     .then(
                        Commands.literal("get")
                           .then(Commands.argument("witherstorm", EntityArgument.entity()).executes(ConsumedEntitiesCommands::getConsumedEntities))
                     ))
                  .then(
                     Commands.literal("lock")
                        .then(Commands.argument("witherstorm", EntityArgument.entities()).executes(ConsumedEntitiesCommands::lockWitherStorm))
                  ))
               .then(
                  Commands.literal("unlock")
                     .then(Commands.argument("witherstorm", EntityArgument.entities()).executes(ConsumedEntitiesCommands::unlockWitherStorm))
               )
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int setConsumedEntities(CommandContext<CommandSourceStack> context, int consumedAmount) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      if (EntityArgument.getEntity(context, "witherstorm") instanceof WitherStormEntity storm) {
         storm.setConsumedEntities(consumedAmount);
         storm.checkConsumptionAmount();
         source.sendSuccess(() -> Component.translatable("commands.witherstormmod.setconsumed.success", new Object[]{consumedAmount, storm.getDisplayName()}), true);
      } else {
         MutableComponent component = Component.translatable("commands.witherstormmod.entity.arg.invalid");
         source.sendFailure(component);
      }

      return consumedAmount;
   }

   private static int getConsumedEntities(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      int consumedAmount = 0;
      if (entity instanceof WitherStormEntity storm) {
         consumedAmount = storm.getConsumedEntities();
         source.sendSuccess(
            () -> Component.translatable(
                  "commands.witherstormmod.getconsumed.result",
                  new Object[]{storm.getDisplayName(), storm.getConsumedEntities(), storm.getConsumptionAmountForPhase(storm.getPhase())}
               ),
            false
         );
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return consumedAmount;
   }

   private static int lockWitherStorm(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      if (EntityArgument.getEntity(context, "witherstorm") instanceof WitherStormEntity storm) {
         if (!storm.isConsumptionLocked()) {
            storm.makeConsumptionLocked(true);
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.lock.success", new Object[]{storm.getDisplayName()}), true);
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.lock.fail"));
         }
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return 0;
   }

   private static int unlockWitherStorm(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      if (EntityArgument.getEntity(context, "witherstorm") instanceof WitherStormEntity storm) {
         if (storm.isConsumptionLocked()) {
            storm.makeConsumptionLocked(false);
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.unlock.success", new Object[]{storm.getDisplayName()}), true);
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.unlock.fail"));
         }
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return 0;
   }
}
