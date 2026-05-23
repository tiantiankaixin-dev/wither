package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;
import nonamecrackers2.witherstormmod.common.world.tainting.WorldTainting;

public class ConversionCommands {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            ((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("convert")
                     .then(
                        Commands.literal("canConvert")
                           .then(
                              ((RequiredArgumentBuilder)Commands.argument("mob", EntityArgument.entity()).executes(ctx -> canConvert(ctx, false)))
                                 .then(Commands.literal("fromWitherSickness").executes(ctx -> canConvert(ctx, true)))
                           )
                     ))
                  .then(
                     ((RequiredArgumentBuilder)Commands.argument("mob", EntityArgument.entity())
                           .then(Commands.literal("toSickened").executes(ConversionCommands::convertMob)))
                        .then(Commands.literal("toCured").executes(ConversionCommands::convertMobBack))
                  ))
               .then(
                  ((RequiredArgumentBuilder)Commands.argument("pos", BlockPosArgument.blockPos())
                        .then(Commands.argument("to", BlockPosArgument.blockPos()).executes(ConversionCommands::convertArea)))
                     .executes(ConversionCommands::convertBlock)
               )
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int canConvert(CommandContext<CommandSourceStack> context, boolean fromWitherSickness) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "mob");
      if (entity instanceof Mob) {
         if (WorldTainting.getInstance().canConvertMob(entity, fromWitherSickness)) {
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.conversion.convert.possible", new Object[]{entity.getDisplayName()}), false);
            return 1;
         } else {
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.conversion.convert.impossible", new Object[]{entity.getDisplayName()}), false);
            return 0;
         }
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.conversion.invalid"));
         return -1;
      }
   }

   private static int convertMob(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "mob");
      if (entity instanceof Mob mob) {
         if (WorldTainting.getInstance().convertMob(mob, false)) {
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.conversion.success", new Object[]{entity.getDisplayName()}), true);
            return 1;
         } else {
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.conversion.fail"), false);
            return 0;
         }
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.conversion.invalid"));
         return -1;
      }
   }

   private static int convertMobBack(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "mob");
      if (entity instanceof Mob && entity instanceof WitherSickened witherSickened) {
         if (witherSickened.cure(source.getLevel())) {
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.conversion.success", new Object[]{entity.getDisplayName()}), true);
            return 1;
         } else {
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.conversion.fail"), false);
            return 0;
         }
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.conversion.invalid"));
         return -1;
      }
   }

   private static int convertBlock(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
      if (WorldTainting.getInstance().convertBlock(pos, source.getLevel())) {
         source.sendSuccess(() -> Component.translatable("commands.witherstormmod.conversion.block.success"), true);
         return 1;
      } else {
         source.sendFailure(Component.translatable("commands.witherstormmod.conversion.block.fail"));
         return 0;
      }
   }

   private static int convertArea(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      BlockPos start = BlockPosArgument.getLoadedBlockPos(context, "pos");
      BlockPos end = BlockPosArgument.getLoadedBlockPos(context, "to");
      BoundingBox box = new BoundingBox(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ());
      int area = box.getXSpan() * box.getYSpan() * box.getZSpan();
      int maxAllowed = source.getLevel().getGameRules().getInt(GameRules.RULE_COMMAND_MODIFICATION_BLOCK_LIMIT);
      if (area > maxAllowed) {
         source.sendFailure(Component.translatable("commands.witherstormmod.conversion.block.area.excessive", new Object[]{maxAllowed}));
         return -1;
      } else {
         int count = WorldTainting.getInstance().convertBlocks(box, source.getLevel());
         source.sendSuccess(() -> Component.translatable("commands.witherstormmod.conversion.block.area.success", new Object[]{count}), true);
         return count;
      }
   }
}
