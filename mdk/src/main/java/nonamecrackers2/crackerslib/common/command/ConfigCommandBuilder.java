package nonamecrackers2.crackerslib.common.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.ValueSpec;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.server.command.EnumArgument;
import nonamecrackers2.crackerslib.common.command.argument.ConfigArgument;
import nonamecrackers2.crackerslib.common.config.ConfigHelper;
import nonamecrackers2.crackerslib.common.event.impl.OnConfigOptionSaved;

public class ConfigCommandBuilder {
   private final String modid;
   private final Map<Type, ModConfigSpec> specs = Maps.newEnumMap(Type.class);
   private final LiteralArgumentBuilder<CommandSourceStack> argumentBuilder;
   private final CommandDispatcher<CommandSourceStack> dispatcher;

   public ConfigCommandBuilder(String modid, LiteralArgumentBuilder<CommandSourceStack> argumentBuilder, CommandDispatcher<CommandSourceStack> dispatcher) {
      this.modid = modid;
      this.argumentBuilder = argumentBuilder;
      this.dispatcher = dispatcher;
   }

   @Deprecated
   public ConfigCommandBuilder(LiteralArgumentBuilder<CommandSourceStack> argumentBuilder, CommandDispatcher<CommandSourceStack> dispatcher) {
      this("UNKNOWN", argumentBuilder, dispatcher);
   }

   public static ConfigCommandBuilder builder(CommandDispatcher<CommandSourceStack> dispatcher, String modid) {
      return new ConfigCommandBuilder(modid, (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal(modid).requires(src -> src.hasPermission(2)), dispatcher);
   }

   public ConfigCommandBuilder addSpec(Type type, ModConfigSpec spec) {
      if (this.specs.containsKey(type)) {
         throw new IllegalArgumentException("Spec '" + type + "' already registered.");
      } else {
         this.specs.put(type, spec);
         return this;
      }
   }

   public void register() {
      LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("config");

      for (Entry<Type, ModConfigSpec> entry : this.specs.entrySet()) {
         Type type = entry.getKey();
         ModConfigSpec spec = entry.getValue();
         LiteralArgumentBuilder<CommandSourceStack> specArgument = Commands.literal(type.extension());
         addArgumentsForSpec(spec, this.modid, type, specArgument);
         root.then(specArgument);
      }

      this.argumentBuilder.then(root);
      this.dispatcher.register(this.argumentBuilder);
   }

   private static void addArgumentsForSpec(ModConfigSpec spec, String modid, Type type, LiteralArgumentBuilder<CommandSourceStack> specArgument) {
      Map<String, ValueSpec> allValues = ConfigHelper.getAllSpecs(spec);
      LiteralArgumentBuilder<CommandSourceStack> setArg = (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(
                     "set"
                  )
                  .then(
                     ((RequiredArgumentBuilder)Commands.addVertex("double", ConfigArgument.arg(allValues, Double.class))
                           .then(
                              Commands.addVertex("value", DoubleArgumentType.doubleArg())
                                 .executes(ctx -> set(ctx, "double", DoubleArgumentType::getDouble, spec, modid, type))
                           ))
                        .then(Commands.literal("default").executes(ctx -> setDefault(ctx, "double", spec, modid, type)))
                  ))
               .then(
                  ((RequiredArgumentBuilder)Commands.addVertex("boolean", ConfigArgument.arg(allValues, Boolean.class))
                        .then(
                           Commands.addVertex("value", BoolArgumentType.bool())
                              .executes(ctx -> set(ctx, "boolean", BoolArgumentType::getBool, spec, modid, type))
                        ))
                     .then(Commands.literal("default").executes(ctx -> setDefault(ctx, "boolean", spec, modid, type)))
               ))
            .then(
               ((RequiredArgumentBuilder)Commands.addVertex("integer", ConfigArgument.arg(allValues, Integer.class))
                     .then(
                        Commands.addVertex("value", IntegerArgumentType.integer())
                           .executes(ctx -> set(ctx, "integer", IntegerArgumentType::getInteger, spec, modid, type))
                     ))
                  .then(Commands.literal("default").executes(ctx -> setDefault(ctx, "integer", spec, modid, type)))
            ))
         .then(
            ((RequiredArgumentBuilder)Commands.addVertex("string", ConfigArgument.arg(allValues, String.class))
                  .then(
                     Commands.addVertex("value", StringArgumentType.greedyString())
                        .executes(ctx -> set(ctx, "string", StringArgumentType::getString, spec, modid, type))
                  ))
               .then(Commands.literal("default").executes(ctx -> setDefault(ctx, "string", spec, modid, type)))
         );

      for (Class<Enum> clazz : gatherEnumValueClasses(allValues)) {
         String name = clazz.getSimpleName();
         setArg.then(
            ((RequiredArgumentBuilder)Commands.addVertex(name, ConfigArgument.arg(allValues, clazz))
                  .then(
                     Commands.addVertex("value", EnumArgument.enumArgument(clazz))
                        .executes(ctx -> set(ctx, name, (ctx1, arg) -> (Enum)ctx1.getArgument(arg, clazz), spec, modid, type))
                  ))
               .then(Commands.literal("default").executes(ctx -> setDefault(ctx, name, spec, modid, type)))
         );
      }

      specArgument.then(Commands.literal("get").then(Commands.addVertex("value", ConfigArgument.any(allValues)).executes(ctx -> get(ctx, spec))));
      specArgument.then(setArg);
   }

   private static List<Class<Enum>> gatherEnumValueClasses(Map<String, ValueSpec> allValues) {
      List<Class<Enum>> list = Lists.newArrayList();

      for (ValueSpec value : allValues.values()) {
         if (value.getDefault() instanceof Enum enu && !list.contains(enu.getDeclaringClass())) {
            list.add(enu.getDeclaringClass());
         }
      }

      return list;
   }

   private static <T> int set(
      CommandContext<CommandSourceStack> context,
      String arg,
      BiFunction<CommandContext<CommandSourceStack>, String, T> valueGetter,
      ModConfigSpec spec,
      String modid,
      Type type
   ) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      ConfigValue<T> config = ConfigArgument.get(context, arg, spec);
      T value = valueGetter.apply(context, "value");
      ValueSpec valueSpec = (ValueSpec)spec.getRaw(config.getPath());
      if (!valueSpec.test(value)) {
         return 0;
      } else {
         OnConfigOptionSaved<T> event = new OnConfigOptionSaved<>(
            modid, type, OnConfigOptionSaved.Source.COMMAND, config, value, !Objects.equals(config.get(), value)
         );
         NeoForge.EVENT_BUS.post(event);
         if (event.getOverrideValue() != null) {
            value = event.getOverrideValue();
         }

         if (!Objects.equals(config.get(), value) && valueSpec.test(value)) {
            config.set(value);
            String joinedPath = ConfigHelper.DOT_JOINER.join(config.getPath());
            Component result = Component.translatable("commands.crackerslib.setConfig.set.success", new Object[]{joinedPath, value});
            source.sendSuccess(() -> result, true);
            return 1;
         } else {
            source.sendFailure(Component.translatable("commands.crackerslib.setConfig.set.fail"));
            return 0;
         }
      }
   }

   private static int get(CommandContext<CommandSourceStack> context, ModConfigSpec spec) {
      ConfigValue<Object> config = ConfigArgument.get(context, "value", spec);
      Object val = config.get();
      ((CommandSourceStack)context.getSource())
         .getBuffer(
            () -> Component.translatable("commands.crackerslib.getConfig.get", new Object[]{ConfigHelper.DOT_JOINER.join(config.getPath()), config.get()}), false
         );
      if (val instanceof Integer integer) {
         return integer;
      } else if (val instanceof Boolean bool) {
         return bool ? 1 : 0;
      } else if (val instanceof Double decimal) {
         return (int)(decimal * 10.0);
      } else {
         return val instanceof Enum<?> enu ? enu.ordinal() : -1;
      }
   }

   public static int setDefault(CommandContext<CommandSourceStack> context, String arg, ModConfigSpec spec, String modid, Type type) {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      ConfigValue<Object> config = ConfigArgument.get(context, arg, spec);
      ValueSpec valueSpec = (ValueSpec)spec.getRaw(config.getPath());
      boolean flag = !Objects.equals(config.get(), config.getDefault());
      NeoForge.EVENT_BUS.post(new OnConfigOptionSaved<>(modid, type, OnConfigOptionSaved.Source.COMMAND, config, config.getDefault(), flag));
      if (flag) {
         config.set(config.getDefault());
         String name = ConfigHelper.DOT_JOINER.join(config.getPath());
         source.sendSuccess(() -> Component.translatable("commands.crackerslib.setDefault.success", new Object[]{name, config.get()}), true);
      } else {
         source.sendFailure(Component.translatable("commands.crackerslib.setConfig.set.fail"));
         return 0;
      }
   }
}
