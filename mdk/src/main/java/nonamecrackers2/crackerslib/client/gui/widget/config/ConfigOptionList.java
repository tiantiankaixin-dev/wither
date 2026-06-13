package nonamecrackers2.crackerslib.client.gui.widget.config;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;
import nonamecrackers2.crackerslib.client.gui.widget.config.entry.ConfigEntry;
import nonamecrackers2.crackerslib.client.util.SortType;
import nonamecrackers2.crackerslib.common.config.preset.ConfigPreset;

public class ConfigOptionList extends ContainerObjectSelectionList<ConfigOptionList.Entry> {
   private static final Component NO_CONFIG_OPTIONS = Component.translatable("gui.crackerslib.config.noAvailableOptions");
   private static final int ROW_HEIGHT = 30;
   private final List<ConfigListItem> items = Lists.newArrayList();
   private final List<ConfigCategory> categories = Lists.newArrayList();
   private final Type type;
   private String lastSearch = "";
   private SortType sortType = SortType.A_TO_Z;
   private final Runnable valuesChangedResponder;
   private final String modid;
   private final ModConfigSpec spec;

   public ConfigOptionList(
      Minecraft mc, String modid, Type type, ModConfigSpec spec, int width, int height, int top, int bottom, Runnable valuesChangedResponder
   ) {
      super(mc, width, bottom - top, top, ROW_HEIGHT);
      this.modid = modid;
      this.type = type;
      this.spec = spec;
      this.setFocused(true);
      this.valuesChangedResponder = valuesChangedResponder;
   }

   public void updateSize(int width, int height, int top, int bottom) {
      this.setX(0);
      this.setY(top);
      this.setSize(width, bottom - top);
   }

   public String getModid() {
      return this.modid;
   }

   public <T> void addConfigValue(String path, ConfigOptionList.ConfigEntryBuilder itemBuilder, Optional<ConfigCategory> category) {
      category.ifPresentOrElse(
         c -> c.addChild(itemBuilder.build(this.minecraft, this.modid, this.type, path, this.spec, this.valuesChangedResponder)),
         () -> this.items.add(itemBuilder.build(this.minecraft, this.modid, this.type, path, this.spec, this.valuesChangedResponder))
      );
   }

   public ConfigCategory makeCategory(String path, Optional<ConfigCategory> previousCategory) {
      ConfigCategory category = new ConfigCategory(this.minecraft, this.modid, path, this);
      previousCategory.ifPresentOrElse(c -> c.addChild(category), () -> this.items.add(category));
      this.categories.add(category);
      return category;
   }

   @Nullable
   private ConfigCategory getCategoryByPath(String path) {
      for (ConfigCategory category : this.categories) {
         if (path.equals(category.getPath())) {
            return category;
         }
      }

      return null;
   }

   public void setSorting(SortType sorting) {
      this.sortType = sorting;
   }

   public void collapseAllCategories() {
      for (ConfigCategory category : this.categories) {
         category.setExpanded(false);
      }

      this.rebuildList();
   }

   public void buildList() {
      this.buildList("", false);
   }

   public void rebuildList() {
      this.buildList(this.getLastSearchingFor(), false);
   }

   public void buildList(String text, boolean expandOrContractCategories) {
      this.getSelected();
      this.sortType.sortList(this.items);
      List<ConfigListItem> items = Lists.newArrayList();

      for (ConfigCategory category : this.categories) {
         category.setSorting(this.sortType);
         if (expandOrContractCategories) {
            category.setExpanded(!text.isBlank());
         }
      }

      for (ConfigListItem item : this.items) {
         if (text.isEmpty() || item.matchesSearch(text)) {
            items.add(item);
            if (item instanceof ConfigCategory categoryx && categoryx.isExpanded()) {
               items.addAll(categoryx.gatherChildren(text, expandOrContractCategories));
            }
         }
      }

      this.clearEntries();

      for (ConfigListItem itemx : items) {
         this.addEntry(new ConfigOptionList.Entry(itemx));
      }

      this.lastSearch = text;
   }

   public void onClosed() {
      for (ConfigListItem item : this.items) {
         item.onSavedAndClosed();
      }
   }

   public void resetValues() {
      for (ConfigListItem item : this.items) {
         item.resetValue();
      }
   }

   public boolean areValuesReset() {
      for (ConfigListItem item : this.items) {
         if (!item.isValueReset()) {
            return false;
         }
      }

      return true;
   }

   @Nullable
   public ConfigPreset getMatchingPreset(List<ConfigPreset> presets, Predicate<String> excluded) {
      label21:
      for (ConfigPreset preset : presets) {
         for (ConfigListItem item : this.items) {
            if (!item.matchesPreset(preset, excluded)) {
               continue label21;
            }
         }

         return preset;
      }

      return null;
   }

   public void setFromPreset(ConfigPreset preset, Predicate<String> excluded) {
      for (ConfigListItem item : this.items) {
         item.setFromPreset(preset, excluded);
      }
   }

   public int getRowWidth() {
      return this.getWidth() - 40;
   }

   protected int getScrollbarPosition() {
      return this.getX() + this.getWidth() - 5;
   }

   protected void renderBackground(GuiGraphics stack) {
      if (this.minecraft.level != null) {
         stack.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
      } else {
         RenderSystem.setShaderColor(0.15F, 0.15F, 0.15F, 1.0F);
         stack.blit(Screen.MENU_BACKGROUND, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, 32, 32);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   public void renderWidget(GuiGraphics stack, int mouseX, int mouseY, float partialTick) {
      this.renderBackground(stack);
      super.renderWidget(stack, mouseX, mouseY, partialTick);
      if (this.children().isEmpty()) {
         stack.drawCenteredString(this.minecraft.font, NO_CONFIG_OPTIONS, this.width / 2, this.height / 2, -1);
      }
   }

   @Nullable
   public ConfigListItem getItemAt(int mouseX, int mouseY) {
      ConfigOptionList.Entry entry = (ConfigOptionList.Entry)this.getEntryAtPosition(mouseX, mouseY);
      return entry != null && entry.children.stream().anyMatch(w -> !w.isHovered()) ? entry.item : null;
   }

   @Nullable
   private ConfigCategory getCategoryFor(ConfigListItem item) {
      for (ConfigCategory category : this.categories) {
         if (category.getImmediateChildren().contains(item)) {
            return category;
         }
      }

      return null;
   }

   public String getLastSearchingFor() {
      return this.lastSearch;
   }

   @FunctionalInterface
   public interface ConfigEntryBuilder {
      ConfigEntry<?, ?> build(Minecraft var1, String var2, Type var3, String var4, ModConfigSpec var5, Runnable var6);
   }

   public class Entry extends net.minecraft.client.gui.components.ContainerObjectSelectionList.Entry<ConfigOptionList.Entry> {
      private final List<AbstractWidget> children = Lists.newArrayList();
      private final ConfigListItem item;
      private final int x;

      public Entry(ConfigListItem item) {
         this.item = item;
         ConfigCategory category = ConfigOptionList.this.getCategoryFor(item);
         int x = (ConfigOptionList.this.getWidth() - ConfigOptionList.this.getRowWidth()) / 2;
         if (category != null) {
            x = category.getX() + 20;
         }

         this.item.init(this.children, x, ConfigOptionList.this.getY(), ConfigOptionList.this.getRowWidth(), ConfigOptionList.this.itemHeight);
         this.x = x;
      }

      public List<? extends GuiEventListener> children() {
         return this.children;
      }

      public List<? extends NarratableEntry> narratables() {
         return this.children;
      }

      public void render(GuiGraphics stack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean selected, float partialTicks) {
         stack.renderOutline(this.x, top, width - (this.x - left), height, -1426063361);
         if (this.x > left) {
            stack.fill(this.x - 20, top + height / 2, this.x - 4, top + height / 2 + 1, 1442840575);
            stack.fill(this.x - 20, top - height / 2 - 3, this.x - 19, top + height / 2, 1442840575);
         }

         this.item.render(stack, left, top, width, height, mouseX, mouseY, partialTicks);
      }
   }
}
