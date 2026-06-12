package nonamecrackers2.crackerslib.common.command.argument;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.ValueSpec;

public class ConfigArgument implements ArgumentType<String> {
   private static final DynamicCommandExceptionType INVALID_VALUE = new DynamicCommandExceptionType(
      o -> Component.translatable("argument.crackerslib.config.invalidValue", new Object[]{o})
   );
   private final List<String> availableOptions;

   public ConfigArgument(List<String> availableOptions) {
      this.availableOptions = availableOptions;
   }

   protected List<String> getAvailableOptions() {
      return this.availableOptions;
   }

   public String parse(StringReader reader) throws CommandSyntaxException {
      String name = reader.readUnquotedString();

      for (String option : this.getAvailableOptions()) {
         if (option.equals(name)) {
            return option;
         }
      }

      throw INVALID_VALUE.create(name);
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return SharedSuggestionProvider.suggest(this.getAvailableOptions(), builder);
   }

   public static <T> ConfigValue<T> get(CommandContext<CommandSourceStack> context, String argName, ModConfigSpec spec) {
      String path = (String)context.getArgument(argName, String.class);
      return (ConfigValue<T>)spec.getValues().get(path);
   }

   public static ConfigArgument arg(Map<String, ValueSpec> allValues, Class<?> arg) {
      return new ConfigArgument(
         allValues.entrySet()
            .stream()
            .filter(
               e -> e.get().getDefault() instanceof Enum<?> enub
                  ? enub.getDeclaringClass().isAssignableFrom(arg)
                  : e.get().getDefault().getClass().isAssignableFrom(arg)
            )
            .map(Entry::getKey)
            .toList()
      );
   }

   public static ConfigArgument any(Map<String, ValueSpec> allValues) {
      return new ConfigArgument(allValues.entrySet().stream().map(Entry::getKey).toList());
   }

   public static class Serializer implements ArgumentTypeInfo<ConfigArgument, ConfigArgument.Serializer.Template> {
      public void serializeToNetwork(ConfigArgument.Serializer.Template template, FriendlyByteBuf buffer) {
         buffer.writeCollection(template.availableOptions, FriendlyByteBuf::writeUtf);
      }

      public ConfigArgument.Serializer.Template deserializeFromNetwork(FriendlyByteBuf buffer) {
         return new ConfigArgument.Serializer.Template(buffer.readList(FriendlyByteBuf::readUtf));
      }

      public void serializeToJson(ConfigArgument.Serializer.Template template, JsonObject object) {
         JsonArray array = new JsonArray();

         for (String option : template.availableOptions) {
            array.add(option);
         }

         object.add("available_options", array);
      }

      public ConfigArgument.Serializer.Template unpack(ConfigArgument argument) {
         return new ConfigArgument.Serializer.Template(argument.getAvailableOptions());
      }

      public final class Template implements net.minecraft.commands.synchronization.ArgumentTypeInfo.Template<ConfigArgument> {
         public final List<String> availableOptions;

         private Template(List<String> availableOptions) {
            this.availableOptions = availableOptions;
         }

         public ConfigArgument instantiate(CommandBuildContext context) {
            return new ConfigArgument(this.availableOptions);
         }

         public ArgumentTypeInfo<ConfigArgument, ?> type() {
            return Serializer.this;
         }
      }
   }
}
