package nonamecrackers2.crackerslib.common.init;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nonamecrackers2.crackerslib.common.command.argument.ConfigArgument;

public class CrackersLibCommandArguments {
   private static final DeferredRegister<ArgumentTypeInfo<?, ?>> TYPES = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, "crackerslib");
   public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ConfigArgument.Serializer> CONFIG_ARGUMENT = TYPES.register(
      "config", () -> (ConfigArgument.Serializer)ArgumentTypeInfos.registerByClass(ConfigArgument.class, new ConfigArgument.Serializer())
   );

   public static void register(IEventBus modBus) {
      TYPES.register(modBus);
   }
}
