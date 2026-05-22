package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.capability.WitherStormBowelsManager;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;

public class BowelsCommands {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            ((LiteralArgumentBuilder)Commands.literal("bowels")
                  .then(
                     Commands.argument("entity", EntityArgument.entity())
                        .then(Commands.argument("witherstorm", EntityArgument.entity()).executes(BowelsCommands::enterStorm))
                  ))
               .then(Commands.literal("new").then(Commands.argument("witherstorm", EntityArgument.entity()).executes(BowelsCommands::newBowels)))
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int enterStorm(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity enteringEntity = EntityArgument.getEntity(context, "entity");
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      int phase = 0;
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            phase = storm.getPhase();
            if (!enteringEntity.level().dimension().location().equals(WitherStormMod.bowelsLocation())) {
               WitherStormBowelsManager.BowelsEnterStatus flag = WitherStormBowelsManager.enter((ServerLevel)storm.level(), storm, enteringEntity);
               switch (flag) {
                  case ENTITY_CANNOT_CHANGE:
                     source.sendFailure(Component.translatable("commands.witherstormmod.enterBowels.failure.cannotChangeDim"));
                  case CANT_SETUP_BOWELS:
                     source.sendFailure(Component.translatable("commands.witherstormmod.enterBowels.failure", new Object[]{storm.getDisplayName()}));
               }
            } else {
               source.sendFailure(Component.translatable("commands.witherstormmod.enterBowels.dim.invalid"));
            }
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
         }
      }

      return phase;
   }

   private static int newBowels(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      ServerLevel world = source.getLevel();
      int phase = 0;
      if (entity instanceof WitherStormEntity storm) {
         phase = storm.getPhase();
         ServerLevel bowels = WitherStormMod.bowels(world);
         bowels.getCapability(WitherStormModCapabilities.BOWELS_MANAGER).ifPresent(manager -> {
            WitherStormBowelsManager.BowelsInstance instance = manager.get(storm.getUUID());
            if (instance != null && !instance.isCompleted()) {
               instance.setCompleted(true);
               source.sendSuccess(() -> Component.translatable("commands.witherstormmod.newBowels.success", new Object[]{storm.getDisplayName()}), true);
            } else {
               source.sendFailure(Component.translatable("commands.witherstormmod.newBowels.failure", new Object[]{storm.getDisplayName()}));
            }
         });
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return phase;
   }
}
