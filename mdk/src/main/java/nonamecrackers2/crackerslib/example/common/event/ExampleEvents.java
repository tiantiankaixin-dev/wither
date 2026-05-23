package nonamecrackers2.crackerslib.example.common.event;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.fml.config.ModConfig.Type;
import nonamecrackers2.crackerslib.common.command.ConfigCommandBuilder;
import nonamecrackers2.crackerslib.common.config.preset.ConfigPreset;
import nonamecrackers2.crackerslib.common.config.preset.RegisterConfigPresetsEvent;
import nonamecrackers2.crackerslib.example.client.event.common.config.ExampleConfig;

public class ExampleEvents {
   public static void registerPresetsEvent(RegisterConfigPresetsEvent event) {
      event.exclude(ExampleConfig.CLIENT.exampleListInteger);
      event.registerPreset(
         Type.SERVER,
         ConfigPreset.builder(Component.m_237113_("Another Example"))
            .setDescription(Component.m_237113_("Just another epic example preset"))
            .setPreset(ExampleConfig.CLIENT.exampleEnum, ExampleConfig.ExampleEnum.GOING)
            .build()
      );
      event.registerPreset(
         Type.SERVER,
         ConfigPreset.builder(Component.m_237113_("Example"))
            .setDescription(Component.m_237113_("Just an example preset"))
            .setPreset(ExampleConfig.CLIENT.exampleBoolean, false)
            .setPreset(ExampleConfig.CLIENT.exampleDouble, 0.5)
            .setPreset(ExampleConfig.CLIENT.exampleInteger, 90)
            .setPreset(ExampleConfig.CLIENT.exampleString, "Test preset FTW!")
            .setPreset(ExampleConfig.CLIENT.exampleListDouble, Lists.newArrayList(new Double[]{100.0, 110.0, 120.0}))
            .build()
      );
   }

   public static void registerCommands(RegisterCommandsEvent event) {
      ConfigCommandBuilder.builder(event.getDispatcher(), "crackerslib").addSpec(Type.SERVER, ExampleConfig.CLIENT_SPEC).register();
   }
}
