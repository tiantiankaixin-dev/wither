package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModEntityTags;

public class RandomizeSicknessModifiersCommand {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            Commands.literal("sickness")
               .then(
                  Commands.literal("randomizeModifiers")
                     .then(Commands.argument("entity", EntityArgument.entity()).executes(RandomizeSicknessModifiersCommand::randomizeModifiers))
               )
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int randomizeModifiers(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "entity");
      if (entity != null) {
         if (entity instanceof LivingEntity living) {
            if (!living.getType().is(WitherStormModEntityTags.WITHER_SICKNESS_IMMUNE)) {
               Optional<WitherSicknessTracker> optional = living.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).resolve();
               if (optional.isPresent()) {
                  WitherSicknessTracker tracker = optional.get();
                  tracker.randomizeModifiers();
                  source.sendSuccess(() -> Component.translatable("commands.witherstormmod.sickness.randomizeModifiers", new Object[]{living.getDisplayName()}), true);
               }
            } else {
               source.sendFailure(Component.translatable("commands.witherstormmod.sickness.immune", new Object[]{entity.getDisplayName()}));
            }
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.notLiving"));
         }
      }

      return 0;
   }
}
