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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModEntityTags;

public class StartInfectionCommand {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            Commands.literal("sickness")
               .then(Commands.literal("infect").then(Commands.argument("entity", EntityArgument.entity()).executes(StartInfectionCommand::startInfection)))
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int startInfection(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "entity");
      int timeTillInfected = 0;
      if (entity != null) {
         if (entity instanceof LivingEntity living) {
            if (!entity.getType().is(WitherStormModEntityTags.WITHER_SICKNESS_IMMUNE)) {
               Optional<WitherSicknessTracker> optional = living.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).resolve();
               if (optional.isPresent()) {
                  WitherSicknessTracker tracker = optional.get();
                  timeTillInfected = tracker.getDelayTicks();
                  if (!tracker.isInfected()) {
                     MutableComponent component = Component.translatable("commands.witherstormmod.sickness.startInfection", new Object[]{living.getDisplayName()});
                     source.sendSuccess(() -> component, true);
                     tracker.beginInfection();
                  } else {
                     MutableComponent component = Component.translatable("commands.witherstormmod.sickness.alreadyInfected", new Object[]{living.getDisplayName()});
                     source.sendFailure(component);
                  }
               }
            } else {
               MutableComponent component = Component.translatable("commands.witherstormmod.sickness.immune", new Object[]{entity.getDisplayName()});
               source.sendFailure(component);
            }
         } else {
            MutableComponent component = Component.translatable("commands.witherstormmod.entity.arg.notLiving");
            source.sendFailure(component);
         }
      }

      return timeTillInfected;
   }
}
