package nonamecrackers2.witherstormmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.Ticket;
import net.minecraft.util.SortedArraySet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import nonamecrackers2.witherstormmod.common.capability.WitherStormModChunkLoader;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.mixin.MixinDistanceManager;
import nonamecrackers2.witherstormmod.mixin.MixinServerChunkCache;

public class ChunkLoaderCommands {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            ((LiteralArgumentBuilder)Commands.literal("chunkLoader")
                  .then(
                     ((LiteralArgumentBuilder)Commands.literal("get")
                           .then(Commands.argument("storm", EntityArgument.entity()).executes(ctx -> get(ctx, true))))
                        .executes(ctx -> get(ctx, false))
                  ))
               .then(Commands.literal("refresh").executes(ChunkLoaderCommands::refresh))
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int get(CommandContext<CommandSourceStack> context, boolean hasStorm) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      WitherStormModChunkLoader loader = (WitherStormModChunkLoader)source.getLevel().getData(WitherStormModCapabilities.CHUNK_LOADER.get());
      if (loader != null) {
         if (!hasStorm) {
            Map<UUID, WitherStormModChunkLoader.Instance> instances = loader.getInstances();
            int totalChunkLoaders = 0;

            for (Entry<UUID, WitherStormModChunkLoader.Instance> entry : instances.entrySet()) {
               WitherStormModChunkLoader.Instance instance = entry.getValue();
               totalChunkLoaders++;
               ChunkPos pos = instance.getPos();
               source.sendSuccess(
                  () -> Component.translatable(
                           "commands.witherstormmod.chunkloader.get.specific",
                           new Object[]{entry.getKey(), pos.getMiddleBlockX(), pos.getMiddleBlockZ(), instance.getRadius()}
                        )
                        .withStyle(ChatFormatting.DARK_GRAY),
                  false
               );
            }

            Component text = Component.translatable("commands.witherstormmod.chunkloader.get", new Object[]{totalChunkLoaders}).withStyle(ChatFormatting.YELLOW);
            source.sendSuccess(() -> text, false);
            List<Ticket<?>> tickets = getTickets(source.getLevel())
               .values()
               .stream()
               .flatMap(Collection::stream)
               .filter(t -> ((Ticket<?>)t).getType() == WitherStormModChunkLoader.WITHER_STORM)
               .collect(Collectors.toList());

            source.sendSuccess(
               () -> Component.translatable(
                     "commands.witherstormmod.chunkloader.get.tickets", new Object[]{tickets.size(), source.getLevel().dimension().location()}
                  ),
               hasStorm
            );
            return totalChunkLoaders;
         }

         Entity entity = EntityArgument.getEntity(context, "storm");
         if (entity instanceof WitherStormEntity storm) {
            WitherStormModChunkLoader.Instance instance = loader.getInstance(storm.getUUID());
            if (instance != null) {
               source.sendSuccess(
                  () -> Component.translatable(
                        "commands.witherstormmod.chunkloader.get.specific",
                        new Object[]{storm.getDisplayName(), storm.blockPosition().getX(), storm.blockPosition().getZ(), instance.getRadius()}
                     ),
                  false
               );
               List<Ticket<?>> tickets = (List<Ticket<?>>)(List<?>)((java.util.Collection<?>)((SortedArraySet)getTickets(source.getLevel()).get(storm.chunkPosition().toLong())))
                  .stream()
                  .filter(t -> ((Ticket<?>)t).getType() == WitherStormModChunkLoader.WITHER_STORM)
                  .collect(Collectors.toList());

               source.sendSuccess(
                  () -> Component.translatable("commands.witherstormmod.chunkloader.get.specific.ticket", new Object[]{tickets.size(), entity.chunkPosition()})
                        .withStyle(ChatFormatting.DARK_GRAY),
                  hasStorm
               );
               return instance.getRadius();
            }

            source.sendFailure(Component.translatable("commands.witherstormmod.chunkloader.none"));
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
         }
      }

      return 0;
   }

   private static int refresh(CommandContext<CommandSourceStack> context) {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      WitherStormModChunkLoader loader = (WitherStormModChunkLoader)source.getLevel().getData(WitherStormModCapabilities.CHUNK_LOADER.get());
      if (loader != null) {
         loader.refreshAllLoaders();
         source.sendSuccess(() -> Component.translatable("commands.witherstormmod.chunkloader.refresh"), true);
      }

      return 0;
   }

   private static Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>> getTickets(ServerLevel level) {
      MixinServerChunkCache cache = (MixinServerChunkCache)level.getChunkSource();
      MixinDistanceManager manager = (MixinDistanceManager)cache.getDistanceManager();
      return manager.getTickets();
   }
}
