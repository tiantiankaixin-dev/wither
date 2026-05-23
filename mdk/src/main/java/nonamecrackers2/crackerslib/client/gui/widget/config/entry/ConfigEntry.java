package nonamecrackers2.crackerslib.client.gui.widget.config.entry;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.ValueSpec;
import net.neoforged.fml.config.ModConfig.Type;
import nonamecrackers2.crackerslib.client.gui.widget.config.ConfigListItem;
import nonamecrackers2.crackerslib.common.config.preset.ConfigPreset;
import nonamecrackers2.crackerslib.common.event.impl.OnConfigOptionSaved;

public abstract class ConfigEntry<T, W extends AbstractWidget> implements ConfigListItem {
   protected final Minecraft mc;
   protected final String modid;
   protected final Type type;
   protected final ConfigValue<T> value;
   protected final ValueSpec valueSpec;
   protected final ModConfigSpec spec;
   protected final boolean requiresRestart;
   protected final String path;
   protected final Component name;
   protected final Component description;
   @Nullable
   protected final Component restartText;
   private final Runnable onValueUpdated;
   protected W widget;
   protected Component displayName;

   public ConfigEntry(Minecraft mc, String modid, Type type, String path, ModConfigSpec spec, Runnable onValueUpdated) {
      this.mc = mc;
      this.modid = modid;
      this.type = type;
      this.path = path;
      this.value = (ConfigValue<T>)spec.getValues().getRaw(path);
      this.valueSpec = (ValueSpec)spec.getRaw(path);
      this.requiresRestart = this.valueSpec.needsWorldRestart();
      this.spec = spec;
      this.name = Component.m_237115_("gui." + modid + ".config." + ConfigListItem.extractNameFromPath(path) + ".title");
      String key = this.valueSpec.getTranslationKey();
      if (key != null && !key.isEmpty()) {
         this.description = Component.m_237115_(key);
      } else {
         this.description = Component.m_237113_(this.valueSpec.getComment());
      }

      this.onValueUpdated = onValueUpdated;
      if (this.requiresRestart) {
         this.restartText = Component.m_237115_("gui.crackerslib.screen.config.requiresRestart").m_130940_(ChatFormatting.RED);
      } else {
         this.restartText = null;
      }
   }

   protected Runnable getValueUpdatedResponder() {
      return this.onValueUpdated;
   }

   public Component getName() {
      return this.name;
   }

   public Component getDescription() {
      return this.description;
   }

   protected abstract W buildWidget(int var1, int var2, int var3, int var4);

   protected abstract T getCurrentValue();

   protected abstract void setCurrentValue(T var1);

   @Override
   public void resetValue() {
      this.setCurrentValue((T)this.value.get());
   }

   @Override
   public void setFromPreset(ConfigPreset preset, Predicate<String> excluded) {
      if (!excluded.test(this.path)) {
         if (preset.hasValue(this.path)) {
            this.setCurrentValue(preset.getValue(this.path));
         } else {
            this.setCurrentValue((T)this.value.getDefault());
         }
      }
   }

   @Override
   public boolean isValueReset() {
      return this.value.get().equals(this.getCurrentValue());
   }

   @Override
   public boolean matchesPreset(ConfigPreset preset, Predicate<String> excluded) {
      if (!excluded.test(this.path)) {
         return preset.hasValue(this.path) ? preset.getValue(this.path).equals(this.getCurrentValue()) : this.value.getDefault().equals(this.getCurrentValue());
      } else {
         return true;
      }
   }

   @Override
   public void onSavedAndClosed() {
      T current = this.getCurrentValue();
      if (this.valueSpec.test(current)) {
         OnConfigOptionSaved<T> event = new OnConfigOptionSaved<>(
            this.modid, this.type, OnConfigOptionSaved.Source.CONFIG_SCREEN, this.value, current, !Objects.equals(current, this.value.get())
         );
         NeoForge.EVENT_BUS.post(event);
         if (event.getOverrideValue() != null && this.valueSpec.test(event.getOverrideValue())) {
            current = event.getOverrideValue();
         }

         this.value.set(current);
      }
   }

   @Override
   public void init(List<AbstractWidget> widgets, int x, int y, int width, int height) {
      if (this.widget == null) {
         this.widget = this.buildWidget(x, y, width, height);
      }

      widgets.add(this.widget);
      int allowedWidth = width - this.widget.m_5711_() - x - 5;
      if (this.requiresRestart) {
         allowedWidth -= this.mc.f_91062_.m_92852_(this.restartText);
      }

      this.displayName = ConfigListItem.shortenText(this.name, allowedWidth);
   }

   @Override
   public void render(GuiGraphics stack, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
      Component component = this.displayName;
      if (this.widget.m_93696_()) {
         component = component.m_6881_().m_130948_(Style.f_131099_.m_131136_(true).m_131140_(ChatFormatting.YELLOW));
      }

      stack.m_280430_(this.mc.f_91062_, component, x + 5 + (this.widget.m_252754_() - x) + this.widget.m_5711_(), y + height / 2 - 9 / 2, -1);
      this.widget.m_253211_(y + height / 2 - this.widget.m_93694_() / 2);
      this.widget.m_88315_(stack, mouseX, mouseY, partialTicks);
      if (this.restartText != null) {
         stack.m_280430_(this.mc.f_91062_, this.restartText, x + width - this.mc.f_91062_.m_92852_(this.restartText) - 5, y + height / 2 - 9 / 2, -1);
      }
   }

   @Nullable
   @Override
   public Tooltip getTooltip(ConfigPreset preset) {
      return this.createConfigTooltip(preset);
   }

   protected Tooltip createConfigTooltip(ConfigPreset preset) {
      MutableComponent comment = this.description.m_6881_();
      comment.m_130946_("\n");
      comment.m_7220_(Component.m_237113_(this.path).m_130940_(ChatFormatting.GRAY));
      String defaultName = "Default: ";
      T object;
      if (preset != null && !preset.isDefault() && preset.hasValue(this.path)) {
         defaultName = "Default (" + preset.name().getString() + "): ";
         object = preset.getValue(this.path);
      } else {
         object = (T)this.value.getDefault();
      }

      comment.m_130946_("\n");
      comment.m_7220_(Component.m_237113_(defaultName + object).m_130940_(ChatFormatting.GREEN));
      if (this.requiresRestart) {
         comment.m_130946_("\n");
         comment.m_7220_(Component.m_237115_("gui.crackerslib.screen.config.requiresRestart").m_130940_(ChatFormatting.YELLOW));
      }

      return Tooltip.m_257550_(comment);
   }

   public int compareTo(ConfigListItem item) {
      return item instanceof ConfigEntry<?, ?> entry ? this.path.compareTo(entry.path) : 0;
   }

   @Override
   public boolean matchesSearch(String text) {
      String lowerCase = text.toLowerCase();
      return this.path.toLowerCase().replace("_", " ").contains(lowerCase) || this.getName().getString().toLowerCase().contains(lowerCase);
   }
}
