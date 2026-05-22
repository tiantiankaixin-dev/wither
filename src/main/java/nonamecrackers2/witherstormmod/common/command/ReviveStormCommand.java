package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
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

public class ReviveStormCommand {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(Commands.literal("revive").then(Commands.argument("witherstorm", EntityArgument.entity()).executes(ReviveStormCommand::reviveStorm)));
      dispatcher.register(setPhaseCommand);
   }

   private static int reviveStorm(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      int timeSincePlayingDead = 0;
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            timeSincePlayingDead = storm.getPlayDeadManager().getTicksSinceRevival();
            if (storm.getPlayDeadManager().getState().disablesAi()) {
               storm.reviveFromPlayingDead();
               MutableComponent component = Component.translatable("commands.witherstormmod.reviveStorm.success", new Object[]{storm.getDisplayName()});
               source.sendSuccess(() -> component, true);
            } else {
               MutableComponent component = Component.translatable("commands.witherstormmod.reviveStorm.failure", new Object[]{storm.getDisplayName()});
               source.sendFailure(component);
            }
         } else {
            MutableComponent component = Component.translatable("commands.witherstormmod.entity.arg.invalid");
            source.sendFailure(component);
         }
      }

      return timeSincePlayingDead;
   }
}
