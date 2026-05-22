package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;

public class TractorBeamCommands {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            Commands.literal("tractorBeam")
               .then(
                  Commands.literal("isInBeam")
                     .then(
                        Commands.argument("entity", EntityArgument.entity())
                           .then(Commands.argument("witherstorm", EntityArgument.entity()).executes(TractorBeamCommands::isInBeam))
                     )
               )
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int isInBeam(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity target = EntityArgument.getEntity(context, "entity");
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      int head = -1;
      if (entity instanceof WitherStormEntity storm) {
         Pair<Boolean, Integer> result = TractorBeamHelper.isInsideTractorBeam(target, storm, 4.0);
         if ((Boolean)result.getFirst()) {
            head = (Integer)result.getSecond();
            source.sendSuccess(
               () -> Component.translatable("commands.witherstormmod.isInTractorBeam.success", new Object[]{target.getDisplayName(), result.getSecond()}), false
            );
         } else {
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.isInTractorBeam.fail", new Object[]{target.getDisplayName()}), false);
         }
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return head;
   }
}
