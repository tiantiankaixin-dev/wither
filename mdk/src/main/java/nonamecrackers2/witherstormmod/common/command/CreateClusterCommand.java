package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;

public class CreateClusterCommand {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> createClusterCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            Commands.literal("cluster")
               .then(
                  ((LiteralArgumentBuilder)Commands.literal("create")
                        .then(
                           Commands.argument("start", BlockPosArgument.blockPos())
                              .then(
                                 Commands.argument("end", BlockPosArgument.blockPos())
                                    .then(
                                       ((RequiredArgumentBuilder)Commands.argument("time", IntegerArgumentType.integer(0))
                                             .then(
                                                ((RequiredArgumentBuilder)Commands.argument("data", CompoundTagArgument.compoundTag())
                                                      .then(
                                                         Commands.argument("storm", EntityArgument.entity())
                                                            .executes(
                                                               context -> createClusterFill(
                                                                     context,
                                                                     EntityArgument.getEntity(context, "storm"),
                                                                     CompoundTagArgument.getCompoundTag(context, "data")
                                                                  )
                                                            )
                                                      ))
                                                   .executes(context -> createClusterFill(context, null, CompoundTagArgument.getCompoundTag(context, "data")))
                                             ))
                                          .executes(context -> createClusterFill(context, null, null))
                                    )
                              )
                        ))
                     .then(
                        Commands.argument("pos", BlockPosArgument.blockPos())
                           .then(
                              Commands.argument("radius", IntegerArgumentType.integer(0))
                                 .then(
                                    ((RequiredArgumentBuilder)Commands.argument("time", IntegerArgumentType.integer(0))
                                          .then(
                                             ((RequiredArgumentBuilder)Commands.argument("data", CompoundTagArgument.compoundTag())
                                                   .then(
                                                      Commands.argument("storm", EntityArgument.entity())
                                                         .executes(
                                                            context -> createCluster(
                                                                  context,
                                                                  EntityArgument.getEntity(context, "storm"),
                                                                  CompoundTagArgument.getCompoundTag(context, "data")
                                                               )
                                                         )
                                                   ))
                                                .executes(context -> createCluster(context, null, CompoundTagArgument.getCompoundTag(context, "data")))
                                          ))
                                       .executes(context -> createCluster(context, null, null))
                                 )
                           )
                     )
               )
         );
      dispatcher.register(createClusterCommand);
   }

   private static int createCluster(CommandContext<CommandSourceStack> context, @Nullable Entity entity, @Nullable CompoundTag tag) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Level world = source.getLevel();
      BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
      int radius = IntegerArgumentType.getInteger(context, "radius");
      int time = IntegerArgumentType.getInteger(context, "time");
      BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(world);
      if (entity instanceof WitherStormEntity storm) {
         cluster.setNoGravity(true);
         cluster.setPhysics(false);
         storm.getTrackedEntities().trackEntityToConsume(cluster);
      } else if (entity != null) {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
         return 0;
      }

      if (tag != null) {
         cluster.load(tag);
      }

      cluster.populateWithRadius(pos, (float)radius, blockstate -> true);
      cluster.setTime(time);
      world.addFreshEntity(cluster);
      return cluster.getSize();
   }

   private static int createClusterFill(CommandContext<CommandSourceStack> context, @Nullable Entity entity, @Nullable CompoundTag tag) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Level world = source.getLevel();
      BlockPos start = BlockPosArgument.getLoadedBlockPos(context, "start");
      BlockPos end = BlockPosArgument.getLoadedBlockPos(context, "end");
      int time = IntegerArgumentType.getInteger(context, "time");
      BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(world);
      if (entity instanceof WitherStormEntity storm) {
         cluster.setNoGravity(true);
         cluster.setPhysics(false);
         storm.getTrackedEntities().trackEntityToConsume(cluster);
      } else if (entity != null) {
         source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
         return 0;
      }

      if (tag != null) {
         cluster.load(tag);
      }

      cluster.populate(start, end, blockstate -> true);
      cluster.setTime(time);
      world.addFreshEntity(cluster);
      return cluster.getSize();
   }
}
