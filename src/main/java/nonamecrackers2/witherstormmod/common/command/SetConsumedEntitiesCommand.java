package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class SetConsumedEntitiesCommand {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            Commands.literal("consumedEntities")
               .then(
                  Commands.literal("set")
                     .then(
                        Commands.argument("witherstorm", EntityArgument.entity())
                           .then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes(SetConsumedEntitiesCommand::setConsumedEntities))
                     )
               )
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int setConsumedEntities(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      int consumedAmount = IntegerArgumentType.getInteger(context, "amount");
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            storm.setConsumedEntities(consumedAmount);
            storm.checkConsumptionAmount();
            MutableComponent component = Component.translatable("commands.witherstormmod.setconsumed.success", new Object[]{consumedAmount, storm.getDisplayName()});
            source.sendSuccess(() -> component, true);
         } else {
            MutableComponent component = Component.translatable("commands.witherstormmod.entity.arg.invalid");
            source.sendFailure(component);
         }
      }

      return consumedAmount;
   }
}
