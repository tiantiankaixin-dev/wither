package nonamecrackers2.crackerslib.client.config;

import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;
import nonamecrackers2.crackerslib.client.gui.ConfigHomeScreen;

@FunctionalInterface
public interface ConfigHomeScreenFactory {
   ConfigHomeScreen build(String var1, Map<Type, ModConfigSpec> var2, boolean var3, boolean var4, @Nullable Screen var5);
}
