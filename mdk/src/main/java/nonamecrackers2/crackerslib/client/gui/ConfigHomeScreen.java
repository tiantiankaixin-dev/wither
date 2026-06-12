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
      super(Component.translatable("gui." + modid + ".screen.config.home.title"));
      this.title = title;
      this.modid = modid;
      this.specs = specs;
      this.isWorldLoaded = isWorldLoaded;
      this.hasSinglePlayerServer = hasSinglePlayerServer;
      this.previous = previous;
      this.extraButtons = extraButtons;
      this.totalColumns = totalColumns;
   }

   protected void init() {
      GridLayout layout = new GridLayout().rowSpacing(6);
      RowHelper rowHelper = layout.createRowHelper(1);
      if (this.specs.containsKey(Type.CLIENT)) {
         rowHelper.addChild(
            Button.builder(Component.translatable("gui.crackerslib.screen.clientOptions.title"), button -> this.openConfigMenu(Type.CLIENT))
               .size(200, 20)
               .tooltip(Tooltip.create(Component.translatable("gui.crackerslib.screen.clientOptions.info")))
               .build()
         );
      }

      if (this.specs.containsKey(Type.COMMON)) {
         this.commonButton = (Button)rowHelper.addChild(
            Button.builder(Component.translatable("gui.crackerslib.screen.commonOptions.title"), button -> this.openConfigMenu(Type.COMMON))
               .size(200, 20)
               .tooltip(Tooltip.create(Component.translatable("gui.crackerslib.screen.commonOptions.info")))
               .build()
         );
      }

      if (this.specs.containsKey(Type.SERVER)) {
         this.worldButton = (Button)rowHelper.addChild(
            Button.builder(Component.translatable("gui.crackerslib.screen.serverOptions.title"), button -> this.openConfigMenu(Type.SERVER))
               .size(200, 20)
               .build()
         );
      }

      this.initExtraButtons(rowHelper);
      this.exit = Button.builder(Component.translatable("gui.crackerslib.button.exit.title"), button -> this.onClose())
         .pos((this.width - 200) / 2, this.height - 6 - 20)
         .size(200, 20)
         .build();
      int exitButtonSpaceTaken = this.exit.getHeight() + 20;
      int availableScreenHeight = this.height - exitButtonSpaceTaken;
      layout.arrangeElements();
      int layoutHeight = layout.getHeight();
      int totalHeightTaken = layoutHeight + this.title.getHeight();
      int heightRemaining = availableScreenHeight - totalHeightTaken;
      this.elementSpacing = heightRemaining / 4;
      int top = this.elementSpacing * 2 + this.title.getHeight();
      FrameLayout.alignInRectangle(layout, 0, top, this.width, availableScreenHeight - top - this.elementSpacing);
      layout.visitWidgets(x$0 -> {
         AbstractWidget var10000 = (AbstractWidget)this.addRenderableWidget(x$0);
      });
      if (this.commonButton != null) {
         this.commonButton.active = this.isWorldLoaded && this.hasSinglePlayerServer || !this.isWorldLoaded;
      }

      if (this.worldButton != null) {
         this.worldButton.active = this.isWorldLoaded && this.hasSinglePlayerServer;
      }

      this.addRenderableWidget(this.exit);
   }

   protected void initExtraButtons(RowHelper main) {
      if (!this.extraButtons.isEmpty()) {
         int totalButtons = this.extraButtons.size();
         int totalColumns = Math.min(totalButtons, this.totalColumns);
         GridLayout extraButtons = (GridLayout)main.addChild(new GridLayout().rowSpacing(6).columnSpacing(4));
         RowHelper extraButtonsRowHelper = extraButtons.createRowHelper(totalColumns);
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
               button.setY(widthPerButton);
               extraButtonsRowHelper.addChild(button, occupiedColumns);
            }
         }
      }
   }

   public void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
      MutableComponent worldDesc = Component.translatable("gui.crackerslib.screen.serverOptions.notInWorld.info");
      if (this.isWorldLoaded) {
         worldDesc = Component.translatable("gui.crackerslib.screen.serverOptions.inWorld.info");
      }

      if (this.worldButton != null) {
         this.worldButton.setTooltip(Tooltip.create(worldDesc));
      }

      this.render(stack);
      int titleX = this.width / 2 - this.title.getWidth() / 2;
      int titleY = this.elementSpacing;
      this.title.blit(stack, titleX, titleY, partialTicks);
      super.render(stack, mouseX, mouseY, partialTicks);
   }

   public void onClose() {
      if (this.previous == null) {
         super.onClose();
      } else {
         this.minecraft.setScreen(this.previous);
      }
   }

   protected void openConfigMenu(Type type) {
      ModConfigSpec spec = this.specs.get(type);
      if (spec != null) {
         OnConfigScreenOpened event = new OnConfigScreenOpened(this.modid, type);
         if (!NeoForge.EVENT_BUS.post(event)) {
            this.minecraft.setScreen(ConfigScreen.makeScreen(this.modid, spec, type, this, event.getInitialPath() != null ? event.getInitialPath() : ""));
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
         return this.addExtraButton(() -> Button.builder(title, button -> GUIUtils.openLink(link)).size(200, 20).tooltip(tooltip).build());
      }

      public ConfigHomeScreen.Builder addLinkButton(Component title, String link) {
         return this.addLinkButton(title, link, null);
      }

      public ConfigHomeScreen.Builder standardLinks(@Nullable String discordLink, @Nullable String patreonLink, @Nullable String githubLink) {
         if (discordLink != null) {
            this.addLinkButton(
               Component.translatable("gui.crackerslib.screen.config.discord").withStyle(Style.EMPTY.withColor(-10983950)),
               discordLink,
               Tooltip.create(Component.translatable("gui.crackerslib.screen.config.discord.info"))
            );
         }

         if (githubLink != null) {
            this.addLinkButton(
               Component.translatable("gui.crackerslib.screen.config.github").withStyle(Style.EMPTY.withColor(-5526613)),
               githubLink,
               Tooltip.create(Component.translatable("gui.crackerslib.screen.config.github.info"))
            );
         }

         if (patreonLink != null) {
            this.addLinkButton(
               Component.translatable("gui.crackerslib.screen.config.patreon").withStyle(ChatFormatting.RED),
               patreonLink,
               Tooltip.create(Component.translatable("gui.crackerslib.screen.config.patreon.info"))
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
