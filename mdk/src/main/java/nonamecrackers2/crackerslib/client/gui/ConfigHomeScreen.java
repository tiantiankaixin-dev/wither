package nonamecrackers2.crackerslib.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.config.ModConfig.Type;
import nonamecrackers2.crackerslib.client.config.ConfigHomeScreenFactory;
import nonamecrackers2.crackerslib.client.event.impl.OnConfigScreenOpened;
import nonamecrackers2.crackerslib.client.gui.title.TitleLogo;
import nonamecrackers2.crackerslib.client.util.GUIUtils;

public class ConfigHomeScreen extends Screen {
   private static final int BUTTON_WIDTH = 200;
   private static final int BUTTON_HEIGHT = 20;
   private static final int EXIT_BUTTON_OFFSET = 6;
   private static final int MAX_WIDTH = 200;
   private static final int COLUMN_SPACING = 4;
   private final String modid;
   private final Map<Type, ModConfigSpec> specs;
   private final TitleLogo title;
   private final boolean isWorldLoaded;
   private final boolean hasSinglePlayerServer;
   @Nullable
   private final Screen previous;
   private final List<Supplier<AbstractButton>> extraButtons;
   private final int totalColumns;
   @Nullable
   private Button commonButton;
   @Nullable
   private Button worldButton;
   private Button exit;
   private int elementSpacing;

   public ConfigHomeScreen(
      String modid,
      Map<Type, ModConfigSpec> specs,
      TitleLogo title,
      boolean isWorldLoaded,
      boolean hasSinglePlayerServer,
      @Nullable Screen previous,
      List<Supplier<AbstractButton>> extraButtons,
      int totalColumns
   ) {
      super(Component.m_237115_("gui." + modid + ".screen.config.home.title"));
      this.title = title;
      this.modid = modid;
      this.specs = specs;
      this.isWorldLoaded = isWorldLoaded;
      this.hasSinglePlayerServer = hasSinglePlayerServer;
      this.previous = previous;
      this.extraButtons = extraButtons;
      this.totalColumns = totalColumns;
   }

   protected void m_7856_() {
      GridLayout layout = new GridLayout().m_267750_(6);
      RowHelper rowHelper = layout.m_264606_(1);
      if (this.specs.containsKey(Type.CLIENT)) {
         rowHelper.m_264139_(
            Button.m_253074_(Component.m_237115_("gui.crackerslib.screen.clientOptions.title"), button -> this.openConfigMenu(Type.CLIENT))
               .m_253046_(200, 20)
               .m_257505_(Tooltip.m_257550_(Component.m_237115_("gui.crackerslib.screen.clientOptions.info")))
               .m_253136_()
         );
      }

      if (this.specs.containsKey(Type.COMMON)) {
         this.commonButton = (Button)rowHelper.m_264139_(
            Button.m_253074_(Component.m_237115_("gui.crackerslib.screen.commonOptions.title"), button -> this.openConfigMenu(Type.COMMON))
               .m_253046_(200, 20)
               .m_257505_(Tooltip.m_257550_(Component.m_237115_("gui.crackerslib.screen.commonOptions.info")))
               .m_253136_()
         );
      }

      if (this.specs.containsKey(Type.SERVER)) {
         this.worldButton = (Button)rowHelper.m_264139_(
            Button.m_253074_(Component.m_237115_("gui.crackerslib.screen.serverOptions.title"), button -> this.openConfigMenu(Type.SERVER))
               .m_253046_(200, 20)
               .m_253136_()
         );
      }

      this.initExtraButtons(rowHelper);
      this.exit = Button.m_253074_(Component.m_237115_("gui.crackerslib.button.exit.title"), button -> this.m_7379_())
         .m_252794_((this.f_96543_ - 200) / 2, this.f_96544_ - 6 - 20)
         .m_253046_(200, 20)
         .m_253136_();
      int exitButtonSpaceTaken = this.exit.m_93694_() + 20;
      int availableScreenHeight = this.f_96544_ - exitButtonSpaceTaken;
      layout.m_264036_();
      int layoutHeight = layout.m_93694_();
      int totalHeightTaken = layoutHeight + this.title.getHeight();
      int heightRemaining = availableScreenHeight - totalHeightTaken;
      this.elementSpacing = heightRemaining / 4;
      int top = this.elementSpacing * 2 + this.title.getHeight();
      FrameLayout.m_264159_(layout, 0, top, this.f_96543_, availableScreenHeight - top - this.elementSpacing);
      layout.m_264134_(x$0 -> {
         AbstractWidget var10000 = (AbstractWidget)this.m_142416_(x$0);
      });
      if (this.commonButton != null) {
         this.commonButton.f_93623_ = this.isWorldLoaded && this.hasSinglePlayerServer || !this.isWorldLoaded;
      }

      if (this.worldButton != null) {
         this.worldButton.f_93623_ = this.isWorldLoaded && this.hasSinglePlayerServer;
      }

      this.m_142416_(this.exit);
   }

   protected void initExtraButtons(RowHelper main) {
      if (!this.extraButtons.isEmpty()) {
         int totalButtons = this.extraButtons.size();
         int totalColumns = Math.min(totalButtons, this.totalColumns);
         GridLayout extraButtons = (GridLayout)main.m_264139_(new GridLayout().m_267750_(6).m_267749_(4));
         RowHelper extraButtonsRowHelper = extraButtons.m_264606_(totalColumns);
         int currentRow = 0;

         for (int i = 0; i < totalButtons; i += totalColumns) {
            currentRow++;
            int totalButtonsInRow = totalColumns;
            if (currentRow * totalColumns >= totalButtons) {
               totalButtonsInRow = totalColumns - (currentRow * totalColumns - totalButtons);
            }

            int occupiedColumns = totalColumns / totalButtonsInRow;
            int widthPerButton = (200 - 4 * (totalButtonsInRow - 1)) / totalButtonsInRow;

            for (int j = 0; j < totalButtonsInRow; j++) {
               int index = i + j;
               AbstractButton button = this.extraButtons.get(index).get();
               button.m_93674_(widthPerButton);
               extraButtonsRowHelper.m_264108_(button, occupiedColumns);
            }
         }
      }
   }

   public void m_88315_(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
      MutableComponent worldDesc = Component.m_237115_("gui.crackerslib.screen.serverOptions.notInWorld.info");
      if (this.isWorldLoaded) {
         worldDesc = Component.m_237115_("gui.crackerslib.screen.serverOptions.inWorld.info");
      }

      if (this.worldButton != null) {
         this.worldButton.m_257544_(Tooltip.m_257550_(worldDesc));
      }

      this.m_280273_(stack);
      int titleX = this.f_96543_ / 2 - this.title.getWidth() / 2;
      int titleY = this.elementSpacing;
      this.title.blit(stack, titleX, titleY, partialTicks);
      super.m_88315_(stack, mouseX, mouseY, partialTicks);
   }

   public void m_7379_() {
      if (this.previous == null) {
         super.m_7379_();
      } else {
         this.f_96541_.m_91152_(this.previous);
      }
   }

   protected void openConfigMenu(Type type) {
      ModConfigSpec spec = this.specs.get(type);
      if (spec != null) {
         OnConfigScreenOpened event = new OnConfigScreenOpened(this.modid, type);
         if (!NeoForge.EVENT_BUS.post(event)) {
            this.f_96541_.m_91152_(ConfigScreen.makeScreen(this.modid, spec, type, this, event.getInitialPath() != null ? event.getInitialPath() : ""));
         }
      }
   }

   public static ConfigHomeScreen.Builder builder(TitleLogo title) {
      return new ConfigHomeScreen.Builder(title);
   }

   public static class Builder {
      private final List<Supplier<AbstractButton>> extraButtons = Lists.newArrayList();
      private final TitleLogo title;
      private int totalColumns = 2;

      private Builder(TitleLogo title) {
         this.title = title;
      }

      public ConfigHomeScreen.Builder totalColumns(int columns) {
         this.totalColumns = columns;
         return this;
      }

      public ConfigHomeScreen.Builder addExtraButton(Supplier<AbstractButton> supplier) {
         this.extraButtons.add(supplier);
         return this;
      }

      public ConfigHomeScreen.Builder addLinkButton(Component title, String link, @Nullable Tooltip tooltip) {
         return this.addExtraButton(() -> Button.m_253074_(title, button -> GUIUtils.openLink(link)).m_253046_(200, 20).m_257505_(tooltip).m_253136_());
      }

      public ConfigHomeScreen.Builder addLinkButton(Component title, String link) {
         return this.addLinkButton(title, link, null);
      }

      public ConfigHomeScreen.Builder standardLinks(@Nullable String discordLink, @Nullable String patreonLink, @Nullable String githubLink) {
         if (discordLink != null) {
            this.addLinkButton(
               Component.m_237115_("gui.crackerslib.screen.config.discord").m_130948_(Style.f_131099_.m_178520_(-10983950)),
               discordLink,
               Tooltip.m_257550_(Component.m_237115_("gui.crackerslib.screen.config.discord.info"))
            );
         }

         if (githubLink != null) {
            this.addLinkButton(
               Component.m_237115_("gui.crackerslib.screen.config.github").m_130948_(Style.f_131099_.m_178520_(-5526613)),
               githubLink,
               Tooltip.m_257550_(Component.m_237115_("gui.crackerslib.screen.config.github.info"))
            );
         }

         if (patreonLink != null) {
            this.addLinkButton(
               Component.m_237115_("gui.crackerslib.screen.config.patreon").m_130940_(ChatFormatting.RED),
               patreonLink,
               Tooltip.m_257550_(Component.m_237115_("gui.crackerslib.screen.config.patreon.info"))
            );
         }

         return this;
      }

      public ConfigHomeScreen.Builder crackersDefault(@Nullable String github) {
         return this.standardLinks(
            "https://discord.com/invite/cracker-s-modded-community-987817685293355028", "https://www.patreon.com/nonamecrackers2", github
         );
      }

      public ConfigHomeScreen.Builder crackersDefault() {
         return this.crackersDefault(null);
      }

      public ConfigHomeScreenFactory build() {
         return this.build(ConfigHomeScreen::new);
      }

      public ConfigHomeScreenFactory build(ConfigHomeScreen.Builder.CustomHomeScreen constructor) {
         return (modid, specs, isWorldLoaded, hasSinglePlayerServer, previous) -> constructor.build(
            modid, specs, this.title, isWorldLoaded, hasSinglePlayerServer, previous, this.extraButtons, this.totalColumns
         );
      }

      @FunctionalInterface
      public interface CustomHomeScreen {
         ConfigHomeScreen build(
            String var1,
            Map<Type, ModConfigSpec> var2,
            TitleLogo var3,
            boolean var4,
            boolean var5,
            @Nullable Screen var6,
            List<Supplier<AbstractButton>> var7,
            int var8
         );
      }
   }
}
