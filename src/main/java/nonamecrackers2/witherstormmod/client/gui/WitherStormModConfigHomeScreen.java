package nonamecrackers2.witherstormmod.client.gui;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;
import nonamecrackers2.crackerslib.client.gui.ConfigHomeScreen;
import nonamecrackers2.crackerslib.client.gui.title.TitleLogo;
import nonamecrackers2.witherstormmod.client.audio.SoundManagersRefresher;
import nonamecrackers2.witherstormmod.client.gui.widget.RefreshSoundsButton;

public class WitherStormModConfigHomeScreen extends ConfigHomeScreen {
   public WitherStormModConfigHomeScreen(
      String modid,
      Map<Type, ForgeConfigSpec> specs,
      TitleLogo title,
      boolean isWorldLoaded,
      boolean hasSinglePlayerServer,
      Screen previous,
      List<Supplier<AbstractButton>> extraButtons,
      int totalColumns
   ) {
      super(modid, specs, title, isWorldLoaded, hasSinglePlayerServer, previous, extraButtons, totalColumns);
   }

   protected void init() {
      super.init();
      ((Button)this.addRenderableWidget(
            Button.builder(Component.translatable("gui.witherstormmod.button.refreshSounds.title"), button -> SoundManagersRefresher.INSTANCE.refresh())
               .pos(5, 5)
               .size(20, 20)
               .tooltip(Tooltip.create(Component.translatable("gui.witherstormmod.button.refreshSounds.title")))
               .build(RefreshSoundsButton::new)
         ))
         .active = this.minecraft.level != null;
   }
}
