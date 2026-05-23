package nonamecrackers2.crackerslib.client.gui.widget.config;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import nonamecrackers2.crackerslib.client.util.SortType;
import nonamecrackers2.crackerslib.common.config.preset.ConfigPreset;

public class ConfigCategory implements ConfigListItem {
   private final Minecraft mc;
   private final List<ConfigListItem> children = Lists.newArrayList();
   private final List<ConfigCategory> categories = Lists.newArrayList();
   private final String path;
   private final ConfigOptionList list;
   private final String modid;
   private final Component name;
   private Component displayName;
   private Button expand;
   private boolean isExpanded;
   private int x;
   private SortType sortType = SortType.A_TO_Z;

   public ConfigCategory(Minecraft mc, String modid, String path, ConfigOptionList list) {
      this.mc = mc;
      this.modid = modid;
      this.path = path;
      this.list = list;
      this.name = Component.m_237115_("gui." + this.modid + ".config.category." + ConfigListItem.extractNameFromPath(path) + ".title")
         .m_130948_(Style.f_131099_.m_131136_(true).m_131140_(ChatFormatting.YELLOW));
   }

   public void setSorting(SortType sorting) {
      this.sortType = sorting;
   }

   public void addChild(ConfigListItem item) {
      if (!this.children.contains(item)) {
         this.children.add(item);
      }
   }

   @Override
   public void init(List<AbstractWidget> widgets, int x, int y, int width, int height) {
      Component buttonText;
      if (this.isExpanded) {
         buttonText = Component.m_237113_("-").m_130940_(ChatFormatting.RED);
      } else {
         buttonText = Component.m_237113_("+").m_130940_(ChatFormatting.GREEN);
      }

      this.expand = Button.m_253074_(buttonText, b -> {
         this.isExpanded = !this.isExpanded;
         this.list.rebuildList();
      }).m_252987_(x + 6, y, 20, 20).m_253136_();
      widgets.add(this.expand);
      if (!this.isExpanded) {
         this.children.forEach(child -> child.init(Lists.newArrayList(), x + 20, y, width, height));
      }

      this.x = x;
      this.displayName = ConfigListItem.shortenText(this.name, width - this.expand.m_5711_() - x - 5);
   }

   @Override
   public void render(GuiGraphics stack, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
      this.expand.m_253211_(y + height / 2 - this.expand.m_93694_() / 2);
      this.expand.m_88315_(stack, mouseX, mouseY, partialTicks);
      stack.m_280430_(this.mc.f_91062_, this.displayName, x + 5 + (this.expand.m_252754_() - x) + this.expand.m_5711_(), y + height / 2 - 9 / 2, -1);
   }

   @Override
   public void onSavedAndClosed() {
      this.children.forEach(child -> child.onSavedAndClosed());
   }

   @Override
   public void resetValue() {
      this.children.forEach(child -> child.resetValue());
   }

   @Override
   public boolean isValueReset() {
      return this.children.stream().allMatch(ConfigListItem::isValueReset);
   }

   @Override
   public boolean matchesPreset(ConfigPreset preset, Predicate<String> excluded) {
      return this.children.stream().allMatch(child -> child.matchesPreset(preset, excluded));
   }

   @Override
   public void setFromPreset(ConfigPreset preset, Predicate<String> excluded) {
      this.children.forEach(child -> child.setFromPreset(preset, excluded));
   }

   @Override
   public Tooltip getTooltip(ConfigPreset preset) {
      return null;
   }

   public String getPath() {
      return this.path;
   }

   public boolean isExpanded() {
      return this.isExpanded;
   }

   public void setExpanded(boolean flag) {
      this.isExpanded = flag;
   }

   public List<ConfigListItem> gatherChildren(String search, boolean expandOrContractCategories) {
      this.sortType.sortList(this.children);
      List<ConfigListItem> items = Lists.newArrayList();

      for (ConfigListItem item : this.children) {
         if (search.isEmpty() || item.matchesSearch(search)) {
            items.add(item);
            if (item instanceof ConfigCategory category && category.isExpanded()) {
               items.addAll(category.gatherChildren(search, expandOrContractCategories));
            }
         }
      }

      return items;
   }

   public List<ConfigListItem> getImmediateChildren() {
      return this.children;
   }

   public int getX() {
      return this.x;
   }

   public int compareTo(ConfigListItem item) {
      return item instanceof ConfigCategory category ? this.path.compareTo(category.path) : 0;
   }

   @Override
   public boolean matchesSearch(String text) {
      return this.children.stream().anyMatch(c -> c.matchesSearch(text));
   }

   public void addCategory(ConfigCategory category) {
      this.categories.add(category);
      this.children.add(category);
   }
}
