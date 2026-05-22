package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.ultimatetarget.UltimateTargetManager;

public class UltimateTargetCommands {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("ultimateTarget")
                           .then(
                              Commands.literal("set")
                                 .then(
                                    ((RequiredArgumentBuilder)Commands.argument("storm", EntityArgument.entity())
                                          .then(Commands.argument("entity", EntityArgument.entity()).executes(UltimateTargetCommands::setUltimateTarget)))
                                       .then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(UltimateTargetCommands::setPos))
                                 )
                           ))
                        .then(
                           ((LiteralArgumentBuilder)Commands.literal("get")
                                 .then(
                                    Commands.literal("pos")
                                       .then(Commands.argument("storm", EntityArgument.entity()).executes(UltimateTargetCommands::getPos))
                                 ))
                              .then(
                                 Commands.literal("entity")
                                    .then(Commands.argument("storm", EntityArgument.entity()).executes(UltimateTargetCommands::getUltimateTarget))
                              )
                        ))
                     .then(
                        Commands.literal("clear")
                           .then(
                              ((RequiredArgumentBuilder)Commands.argument("storm", EntityArgument.entity())
                                    .then(Commands.literal("entity").executes(UltimateTargetCommands::clearUltimateTarget)))
                                 .then(Commands.literal("pos").executes(UltimateTargetCommands::clearPos))
                           )
                     ))
                  .then(
                     ((LiteralArgumentBuilder)Commands.literal("distractions")
                           .then(
                              Commands.literal("makeDistracted")
                                 .then(
                                    Commands.argument("witherstorm", EntityArgument.entity()).executes(UltimateTargetCommands::makeUltimateTargetDistracted)
                                 )
                           ))
                        .then(
                           Commands.literal("makeFocused")
                              .then(Commands.argument("witherstorm", EntityArgument.entity()).executes(UltimateTargetCommands::makeUltimateTargetFocused))
                        )
                  ))
               .then(
                  ((LiteralArgumentBuilder)Commands.literal("chase")
                        .then(
                           Commands.literal("begin")
                              .then(Commands.argument("witherstorm", EntityArgument.entity()).executes(UltimateTargetCommands::beginChase))
                        ))
                     .then(
                        Commands.literal("stop").then(Commands.argument("witherstorm", EntityArgument.entity()).executes(UltimateTargetCommands::stopChase))
                     )
               )
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int setUltimateTarget(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity target = EntityArgument.getEntity(context, "entity");
      if (EntityArgument.getEntity(context, "storm") instanceof WitherStormEntity storm) {
         if (target instanceof LivingEntity && !target.equals(storm) && !(target instanceof WitherStormSegmentEntity)) {
            UltimateTargetManager manager = storm.getUltimateTargetManager().orElse(null);
            if (manager != null) {
               if (target.getUUID().equals(manager.getTargetOverride())) {
                  source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.set.duplicate"));
               } else {
                  manager.setTargetOverride(target.getUUID());
                  source.sendSuccess(() -> Component.translatable("commands.witherstormmod.ultimateTarget.set.success", new Object[]{target.getDisplayName()}), true);
               }
            } else {
               source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.invalid"));
            }
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.set.entity.invalid"));
         }
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return target.getId();
   }

   private static int setPos(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
      if (EntityArgument.getEntity(context, "storm") instanceof WitherStormEntity storm) {
         UltimateTargetManager manager = storm.getUltimateTargetManager().orElse(null);
         if (manager != null) {
            if (pos.equals(manager.getBlockTargetOverride())) {
               source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.set.duplicate"));
            } else {
               manager.setBlockTargetOverride(pos);
               source.sendSuccess(() -> Component.translatable("commands.witherstormmod.ultimateTarget.set.success", new Object[]{pos}), true);
            }
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.invalid"));
         }
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return 0;
   }

   private static int getPos(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      if (EntityArgument.getEntity(context, "storm") instanceof WitherStormEntity storm) {
         Vec3 pos = storm.getUltimateTargetPos();
         if (pos != null) {
            source.sendSuccess(
               () -> {
                  double x = (double)Math.round(pos.x * 10.0) / 10.0;
                  double y = (double)Math.round(pos.y * 10.0) / 10.0;
                  double z = (double)Math.round(pos.z * 10.0) / 10.0;
                  String tpCommand = "/tp " + x + " " + y + " " + z;
                  ClickEvent event = new ClickEvent(Action.RUN_COMMAND, tpCommand);
                  Style style = Style.EMPTY.withClickEvent(event).withColor(ChatFormatting.BLUE);
                  return Component.translatable(
                     "commands.witherstormmod.ultimateTarget.get.pos",
                     new Object[]{storm.getDisplayName(), x, y, z, Component.translatable("commands.witherstormmod.ultimateTarget.get.pos.click").withStyle(style)}
                  );
               },
               false
            );
         } else {
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.ultimateTarget.get.pos.none", new Object[]{storm.getDisplayName()}), false);
         }
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return 0;
   }

   private static int getUltimateTarget(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      if (EntityArgument.getEntity(context, "storm") instanceof WitherStormEntity storm) {
         LivingEntity target = storm.getUltimateTarget();
         if (target != null) {
            source.sendSuccess(
               () -> {
                  String tpCommand = "/tp " + target.getStringUUID();
                  ClickEvent event = new ClickEvent(Action.RUN_COMMAND, tpCommand);
                  Style style = Style.EMPTY.withClickEvent(event).withColor(ChatFormatting.BLUE);
                  return Component.translatable(
                     "commands.witherstormmod.ultimateTarget.get.player",
                     new Object[]{
                        storm.getDisplayName(), target.getDisplayName(), Component.translatable("commands.witherstormmod.ultimateTarget.get.player.click").withStyle(style)
                     }
                  );
               },
               false
            );
         } else {
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.ultimateTarget.get.player.none", new Object[]{storm.getDisplayName()}), false);
         }
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return 0;
   }

   private static int clearUltimateTarget(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      if (EntityArgument.getEntity(context, "storm") instanceof WitherStormEntity storm) {
         UltimateTargetManager manager = storm.getUltimateTargetManager().orElse(null);
         if (manager != null) {
            manager.setTargetOverride(null);
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.ultimateTarget.clear.success"), true);
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.invalid"));
         }
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return 0;
   }

   private static int clearPos(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      if (EntityArgument.getEntity(context, "storm") instanceof WitherStormEntity storm) {
         UltimateTargetManager manager = storm.getUltimateTargetManager().orElse(null);
         if (manager != null) {
            manager.setBlockTargetOverride(null);
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.ultimateTarget.clear.success"), true);
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.invalid"));
         }
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
      }

      return 0;
   }

   private static int makeUltimateTargetDistracted(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      int phase = 0;
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            UltimateTargetManager manager = storm.getUltimateTargetManager().orElse(null);
            if (manager != null) {
               if (!manager.isDistracted()) {
                  manager.makeDistracted(UltimateTargetManager.DistractionReason.FORCED);
                  source.sendSuccess(
                     () -> Component.translatable(
                           "commands.witherstormmod.distractions.ultimateTargetDistractions.makeDistracted.success", new Object[]{storm.getDisplayName()}
                        ),
                     true
                  );
               } else {
                  source.sendFailure(Component.translatable("commands.witherstormmod.distractions.ultimateTargetDistractions.makeDistracted.fail"));
               }
            } else {
               source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.invalid"));
            }

            phase = storm.getPhase();
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
         }
      }

      return phase;
   }

   private static int makeUltimateTargetFocused(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      int phase = 0;
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            UltimateTargetManager manager = storm.getUltimateTargetManager().orElse(null);
            if (manager != null) {
               if (manager.isDistracted()) {
                  manager.makeFocused();
                  source.sendSuccess(
                     () -> Component.translatable(
                           "commands.witherstormmod.distractions.ultimateTargetDistractions.makeFocused.success", new Object[]{storm.getDisplayName()}
                        ),
                     true
                  );
               } else {
                  source.sendFailure(Component.translatable("commands.witherstormmod.distractions.ultimateTargetDistractions.makeFocused.fail"));
               }
            } else {
               source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.invalid"));
            }

            phase = storm.getPhase();
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
         }
      }

      return phase;
   }

   private static int beginChase(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            UltimateTargetManager manager = storm.getUltimateTargetManager().orElse(null);
            if (manager != null) {
               if (!manager.isTargetStationary()) {
                  manager.accelerate();
                  source.sendSuccess(() -> Component.translatable("commands.witherstormmod.ultimateTarget.beginChase", new Object[]{storm.getDisplayName()}), true);
               } else {
                  source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.cannotBeginChase"));
               }
            } else {
               source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.invalid"));
            }
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
         }
      }

      return 0;
   }

   private static int stopChase(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            UltimateTargetManager manager = storm.getUltimateTargetManager().orElse(null);
            if (manager != null) {
               if (manager.isTargetStationary()) {
                  manager.deaccelerate();
                  source.sendSuccess(() -> Component.translatable("commands.witherstormmod.ultimateTarget.stopChase", new Object[]{storm.getDisplayName()}), true);
               } else {
                  source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.cannotStopChase"));
               }
            } else {
               source.sendFailure(Component.translatable("commands.witherstormmod.ultimateTarget.invalid"));
            }
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
         }
      }

      return 0;
   }
}
