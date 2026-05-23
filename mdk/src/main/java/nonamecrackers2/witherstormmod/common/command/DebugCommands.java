package nonamecrackers2.witherstormmod.common.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.command.EnumArgument;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.registry.WitherStormModRegistries;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.ultimatetarget.UltimateTargetManager;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModFeatures;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.CreateDebrisMessage;
import nonamecrackers2.witherstormmod.common.util.EvolutionProfiler;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import nonamecrackers2.witherstormmod.common.world.gen.feature.CommandBlockPodiumFeature;

public class DebugCommands {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      LiteralArgumentBuilder<CommandSourceStack> setPhaseCommand = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal(
               "witherstormmod"
            )
            .requires(commandSource -> commandSource.hasPermission(2)))
         .then(
            ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(
                                          "debug"
                                       )
                                       .then(
                                          ((LiteralArgumentBuilder)Commands.literal("podium")
                                                .then(
                                                   Commands.literal("place")
                                                      .then(
                                                         Commands.argument("dimension", DimensionArgument.dimension())
                                                            .then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(DebugCommands::placePodium))
                                                      )
                                                ))
                                             .then(
                                                Commands.literal("remove")
                                                   .then(
                                                      Commands.argument("dimension", DimensionArgument.dimension())
                                                         .then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(DebugCommands::removePodium))
                                                   )
                                             )
                                       ))
                                    .then(
                                       Commands.literal("debris")
                                          .then(
                                             Commands.literal("create")
                                                .then(Commands.argument("witherstorm", EntityArgument.entity()).executes(DebugCommands::createDebris))
                                          )
                                    ))
                                 .then(
                                    Commands.literal("deathClusters")
                                       .then(
                                          Commands.literal("drop")
                                             .then(Commands.argument("storm", EntityArgument.entity()).executes(DebugCommands::dropDeathClusters))
                                       )
                                 ))
                              .then(
                                 Commands.literal("beacon")
                                    .then(
                                       Commands.literal("reset")
                                          .then(Commands.argument("player", EntityArgument.player()).executes(DebugCommands::resetPlayerBeaconData))
                                    )
                              ))
                           .then(
                              ((LiteralArgumentBuilder)Commands.literal("evolutionProfiler")
                                    .then(Commands.literal("begin").executes(DebugCommands::beginEvolutionProfiler)))
                                 .then(
                                    Commands.literal("query")
                                       .then(Commands.argument("witherstorm", EntityArgument.entity()).executes(DebugCommands::queryEvolutionProfiler))
                                 )
                           ))
                        .then(
                           Commands.literal("splitCluster")
                              .then(
                                 Commands.argument("blockcluster", EntityArgument.entity())
                                    .then(Commands.argument("axis", EnumArgument.enumArgument(Axis.class)).executes(DebugCommands::splitBlockCluster))
                              )
                        ))
                     .then(
                        Commands.literal("symbiont")
                           .then(
                              Commands.literal("doSpell")
                                 .then(
                                    Commands.argument("symbiont", EntityArgument.entity())
                                       .then(Commands.argument("spell", ResourceLocationArgument.id()).executes(DebugCommands::doSymbiontSpell))
                                 )
                           )
                     ))
                  .then(Commands.literal("potionTest").executes(DebugCommands::potionTest)))
               .then(
                  Commands.literal("caveRumble")
                     .then(
                        Commands.argument("players", EntityArgument.players())
                           .then(Commands.argument("intensity", DoubleArgumentType.doubleArg(0.0, 1.0)).executes(DebugCommands::doCaveRumble))
                     )
               )
         );
      dispatcher.register(setPhaseCommand);
   }

   private static int placePodium(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      ServerLevel world = DimensionArgument.getDimension(context, "dimension");
      BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
      int result = 0;

      try {
         if (((ConfiguredFeature)WitherStormModFeatures.getConfiguredFeature(world, WitherStormModFeatures.COMMAND_BLOCK_PODIUM_FEATURE.getId()).value())
            .place(world, world.getChunkSource().getGenerator(), RandomSource.create(), pos)) {
            result = 1;
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return result;
   }

   private static int removePodium(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      ServerLevel world = DimensionArgument.getDimension(context, "dimension");
      BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
      int result = 0;

      try {
         ConfiguredFeature<NoneFeatureConfiguration, CommandBlockPodiumFeature> configured = (ConfiguredFeature<NoneFeatureConfiguration, CommandBlockPodiumFeature>)WitherStormModFeatures.getConfiguredFeature(
               world, WitherStormModFeatures.COMMAND_BLOCK_PODIUM_FEATURE.getId()
            )
            .value();
         if (((CommandBlockPodiumFeature)configured.feature())
            .remove(world, world.getChunkSource().getGenerator(), RandomSource.create(), pos, (NoneFeatureConfiguration)configured.config())) {
            result = 1;
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return result;
   }

   private static int createDebris(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      int phase = 0;
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            phase = storm.getPhase();
            WitherStormModPacketHandlers.MAIN
               .send(PacketDistributor.DIMENSION.with(() -> storm.level().dimension()), new CreateDebrisMessage(storm, storm.isDeadOrPlayingDead()));
            source.sendSuccess(() -> Component.translatable("commands.witherstormmod.createDebris.success", new Object[]{storm.getDisplayName()}), true);
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
         }
      }

      return phase;
   }

   private static int dropDeathClusters(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "storm");
      int phase = 0;
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            phase = storm.getPhase();
            storm.dropMassCluster(phase);
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
         }
      }

      return phase;
   }

   private static int resetPlayerBeaconData(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      ServerPlayer player = EntityArgument.getPlayer(context, "player");
      player.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA).ifPresent(data -> data.setActivatedSuperBeacon(false));
      return 0;
   }

   private static int beginEvolutionProfiler(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayer();
      WitherStormEntity storm = (WitherStormEntity)(WitherStormModEntityTypes.WITHER_STORM.get()).create(player.level());
      storm.moveTo(player.position().add(0.0, 10.0, 0.0));
      storm.getEvolutionProfiler().begin();
      player.level().addFreshEntity(storm);
      WitherStormModConfig.SERVER.ultimateTargetingType.set(UltimateTargetManager.TargetingType.NONE);
      WitherStormModConfig.SERVER.evolutionAttributeModifier.set((Double)WitherStormModConfig.SERVER.evolutionAttributeModifier.getDefault());
      return 0;
   }

   private static int queryEvolutionProfiler(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      Entity entity = EntityArgument.getEntity(context, "witherstorm");
      if (entity != null) {
         if (entity instanceof WitherStormEntity storm) {
            EvolutionProfiler profiler = storm.getEvolutionProfiler();
            if (profiler.isProfiling()) {
               source.sendSuccess(
                  () -> Component.literal(
                        "Consumed entities per second: " + (double)Math.round(storm.getEvolutionProfiler().getConsumedEntitiesPerSecond() * 10.0) / 10.0
                     ),
                  false
               );
            }
         } else {
            source.sendFailure(Component.translatable("commands.witherstormmod.entity.arg.invalid"));
         }
      }

      return 0;
   }

   private static int splitBlockCluster(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      Entity entity = EntityArgument.getEntity(context, "blockcluster");
      Axis axis = (Axis)context.getArgument("axis", Axis.class);
      if (entity instanceof BlockClusterEntity cluster) {
         BlockClusterEntity split = cluster.splitAt(axis);
         if (split != null) {
            entity.level().addFreshEntity(split);
         }
      }

      return 0;
   }

   private static int doSymbiontSpell(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      Entity entity = EntityArgument.getEntity(context, "symbiont");
      ResourceLocation spell = (ResourceLocation)context.getArgument("spell", ResourceLocation.class);
      if (entity instanceof WitheredSymbiontEntity symbiont) {
         SpellType type = (SpellType)WitherStormModRegistries.SPELL_TYPES.get().getValue(spell);
         if (type != null) {
            symbiont.setTarget(((CommandSourceStack)context.getSource()).getPlayer());
            symbiont.setAndCastSpell(type);
         }

         return 1;
      } else {
         return 0;
      }
   }

   private static int potionTest(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayer();
      ThrownPotion potion = new ThrownPotion(player.level(), player);
      ItemStack item = new ItemStack(Items.SPLASH_POTION);
      PotionUtils.setPotion(item, Potions.WATER);
      PotionUtils.setCustomEffects(item, Lists.newArrayList(new MobEffectInstance[]{new MobEffectInstance(MobEffects.WITHER, 60, 2)}));
      potion.setItem(item);
      player.level().addFreshEntity(potion);
      return 0;
   }

   private static int doCaveRumble(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
      double intensity = DoubleArgumentType.getDouble(context, "intensity");
      RandomSource random = RandomSource.create();

      for (ServerPlayer player : players) {
         WorldUtil.doCaveRumble(((CommandSourceStack)context.getSource()).getLevel(), player, intensity, random);
      }

      return 0;
   }
}
