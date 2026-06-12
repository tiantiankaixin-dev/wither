package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import nonamecrackers2.crackerslib.common.packet.SimpleChannel;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;

public class ShakeScreenCommand {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            Commands.literal("screenShake")
               .then(
                  Commands.argument("players", EntityArgument.players())
                     .then(
                        Commands.argument("time", TimeArgument.time())
                           .then(Commands.argument("strength", FloatArgumentType.floatArg(0.0F)).executes(ShakeScreenCommand::shakeScreen))
                     )
               )
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int shakeScreen(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack stack = (CommandSourceStack)context.getSource();
      Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
      int time = IntegerArgumentType.getInteger(context, "time");
      if (time > 1200) {
         stack.sendFailure(Component.translatable("commands.witherstormmod.screenShake.fail"));
         return -1;
      } else {
         float strength = FloatArgumentType.getFloat(context, "strength");

         for (ServerPlayer player : players) {
            WitherStormModPacketHandlers.MAIN.send(SimpleChannel.toPlayer(player), new ShakeScreenMessage((float)time, strength));
         }

         stack.sendSuccess(() -> Component.translatable("commands.witherstormmod.screenShake.success", new Object[]{players.size()}), true);
         return players.size();
      }
   }
}
