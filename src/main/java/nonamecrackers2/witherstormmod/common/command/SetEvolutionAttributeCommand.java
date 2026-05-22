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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModAttributes;

public class SetEvolutionAttributeCommand {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            Commands.literal("evolutionSpeed")
               .then(
                  Commands.literal("set")
                     .then(
                        Commands.argument("witherstorm", EntityArgument.entity())
                           .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.1, 32.0)).executes(SetEvolutionAttributeCommand::setEvolutionSpeed))
                     )
               )
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int setEvolutionSpeed(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      double value = DoubleArgumentType.getDouble(context, "value");
      int phase = 0;
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            phase = storm.getPhase();
            storm.getAttribute((Attribute)WitherStormModAttributes.EVOLUTION_SPEED.get()).setBaseValue(value);
            storm.setPhase(storm.getPhase());
            MutableComponent component = Component.translatable("commands.witherstormmod.setevolution.success", new Object[]{value, storm.getDisplayName()});
            source.sendSuccess(() -> component, true);
         } else {
            MutableComponent component = Component.translatable("commands.witherstormmod.entity.arg.invalid");
            source.sendFailure(component);
         }
      }

      return phase;
   }
}
