package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModDamageTypes;

public class DoDeathSequenceCommand {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> doDeathSequence = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(Commands.literal("kill").then(Commands.argument("witherstorm", EntityArgument.entity()).executes(DoDeathSequenceCommand::doDeathSequence)));
      dispatcher.register(doDeathSequence);
   }

   private static int doDeathSequence(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      int phase = 0;
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            phase = storm.getPhase();
            ServerPlayer player = source.getPlayer();
            if (player != null) {
               storm.hurt(WitherStormModDamageTypes.playerAttackWitherStorm(player), Float.MAX_VALUE);
            } else {
               storm.hurt(storm.damageSources().fellOutOfWorld(), Float.MAX_VALUE);
            }
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
         }
      }

      return phase;
   }
}
