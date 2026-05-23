package nonamecrackers2.crackerslib.client.gui;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import nonamecrackers2.crackerslib.client.gui.widget.SelectableNamedObjectList;

public class Popup extends Screen {
   private static final Queue<Popup> POPUP_QUEUE = Queues.newArrayDeque();
   private static final int BUTTON_WIDTH = 80;
   @Nullable
   private final Screen previous;
   private final Popup.Initializer onInitialized;
   private final MultiLineLabel text;
   private final int boxWidth;
   private final int widgetsHeight;
   private int x;
   private int y;
   private int boxHeight;

   public Popup(@Nullable Screen previous, Popup.Initializer onInitialized, int width, int widgetsHeight, Component pMessage) {
      super(pMessage);
      this.text = MultiLineLabel.m_94341_(Minecraft.m_91087_().f_91062_, pMessage, width - 20);
      this.previous = previous;
      this.onInitialized = onInitialized;
      this.boxWidth = width;
      this.widgetsHeight = widgetsHeight;
   }

   public static Popup createYesNoPopupWithCancel(@Nullable Screen screen, Runnable onAccepted, Runnable onNotAccepted, int width, Component message) {
      return new Popup(screen, (p, r) -> {
         GridLayout layout = new GridLayout().m_267750_(5);
         RowHelper row = layout.m_264606_(1);
         GridLayout yesNoLayout = (GridLayout)row.m_264139_(new GridLayout().m_267749_(10));
         RowHelper yesNoRow = yesNoLayout.m_264606_(2);
         Button yes = (Button)yesNoRow.m_264139_(Button.m_253074_(Component.m_237115_("gui.popup.yes"), b -> {
            p.close();
            onAccepted.run();
         }).m_252780_(80).m_253136_());
         Button no = (Button)yesNoRow.m_264139_(Button.m_253074_(Component.m_237115_("gui.popup.no"), b -> {
            p.close();
            onNotAccepted.run();
         }).m_252780_(80).m_253136_());
         GridLayout cancelLayout = (GridLayout)row.m_264139_(new GridLayout());
         RowHelper cancelRow = cancelLayout.m_264606_(1);
         Button cancel = (Button)cancelRow.m_264139_(Button.m_253074_(Component.m_237115_("gui.popup.cancel"), b -> p.close()).m_252780_(170).m_253136_());
         layout.m_264036_();
         FrameLayout.m_267781_(layout, r);
         p.m_142416_(yes);
         p.m_142416_(no);
         p.m_142416_(cancel);
      }, width, 45, message).open();
   }

   public static Popup createYesNoPopup(@Nullable Screen screen, Runnable onAccepted, Runnable onNotAccepted, int width, Component message) {
      return new Popup(screen, (p, r) -> {
         GridLayout layout = new GridLayout().m_267749_(10);
         RowHelper row = layout.m_264606_(2);
         Button yes = (Button)row.m_264139_(Button.m_253074_(Component.m_237115_("gui.popup.yes"), b -> {
            p.close();
            onAccepted.run();
         }).m_252780_(80).m_253136_());
         Button no = (Button)row.m_264139_(Button.m_253074_(Component.m_237115_("gui.popup.no"), b -> {
            p.close();
            onNotAccepted.run();
         }).m_252780_(80).m_253136_());
         layout.m_264036_();
         FrameLayout.m_267781_(layout, r);
         p.m_142416_(yes);
         p.m_142416_(no);
      }, width, 20, message).open();
   }

   public static Popup createYesNoPopup(@Nullable Screen screen, Runnable onAccepted, int width, Component message) {
      return createYesNoPopup(screen, onAccepted, () -> {}, width, message);
   }

   public static Popup createTextFieldPopup(@Nullable Screen screen, Consumer<String> onAccepted, int width, Component message, Predicate<String> filter) {
      Minecraft mc = Minecraft.m_91087_();
      return new Popup(screen, (p, r) -> {
         GridLayout layout = new GridLayout();
         layout.m_264211_().m_264356_().m_264623_().m_264174_(5);
         RowHelper row = layout.m_264606_(1);
         GridLayout textLayout = (GridLayout)row.m_264139_(new GridLayout());
         RowHelper textSubmit = textLayout.m_264606_(1);
         EditBox box = (EditBox)textSubmit.m_264139_(new EditBox(mc.f_91062_, 0, 0, width / 2, 20, CommonComponents.f_237098_));
         box.m_94153_(filter);
         GridLayout buttonLayout = (GridLayout)row.m_264139_(new GridLayout());
         buttonLayout.m_264211_().m_264215_(5);
         RowHelper buttonRow = buttonLayout.m_264606_(2);
         Button submit = (Button)buttonRow.m_264139_(Button.m_253074_(Component.m_237115_("gui.popup.submit"), b -> {
            p.close();
            onAccepted.accept(box.m_94155_());
         }).m_252780_(80).m_253136_());
         Button cancel = (Button)buttonRow.m_264139_(Button.m_253074_(Component.m_237115_("gui.popup.cancel"), b -> p.close()).m_252780_(80).m_253136_());
         layout.m_264036_();
         FrameLayout.m_267781_(layout, r);
         p.m_142416_(box);
         p.m_142416_(submit);
         p.m_142416_(cancel);
      }, width, 45, message).open();
   }

   public static Popup createTextFieldPopup(@Nullable Screen screen, Consumer<String> onAccepted, int width, Component message) {
      return createTextFieldPopup(screen, onAccepted, width, message, str -> true);
   }

   public static <T> Popup createOptionListPopup(
      @Nullable Screen screen, Consumer<SelectableNamedObjectList<T>> valueApplier, Consumer<T> onAccepted, int width, int listHeight, Component message
   ) {
      Minecraft mc = Minecraft.m_91087_();
      return new Popup(screen, (p, r) -> {
         GridLayout layout = new GridLayout();
         layout.m_264211_().m_264356_().m_264623_().m_264174_(5);
         RowHelper row = layout.m_264606_(2);
         int listWidth = (int)(width / 1.2F);
         int listY = r.m_274449_();
         SelectableNamedObjectList<T> list = new SelectableNamedObjectList<>(mc, listWidth, listHeight, listY, listY + listHeight);
         list.m_93507_(p.boxX() + p.boxWidth() / 2 - list.getWidth() / 2);
         valueApplier.accept(list);
         Button select = (Button)row.m_264139_(Button.m_253074_(Component.m_237115_("gui.popup.select"), b -> {
            p.close();
            onAccepted.accept(list.getSelectedObject());
         }).m_252780_(80).m_253136_());
         select.f_93623_ = false;
         list.setOnObjectSelectedCallback(t -> select.f_93623_ = true);
         Button cancel = (Button)row.m_264139_(Button.m_253074_(Component.m_237115_("gui.popup.cancel"), b -> p.close()).m_252780_(80).m_253136_());
         layout.m_264036_();
         FrameLayout.m_264159_(layout, r.m_274563_(), list.getBottom() + 5, r.f_263770_(), 30);
         p.m_142416_((T)select);
         p.m_142416_((T)cancel);
         p.m_142416_((T)list);
      }, width, listHeight + 30, message).open();
   }

   public static Popup createInfoPopup(@Nullable Screen screen, int width, Component message) {
      return new Popup(
            screen,
            (p, r) -> {
               int buttonWidth = 100;
               Button close = Button.m_253074_(Component.m_237115_("gui.popup.close"), b -> p.close())
                  .m_252794_(p.boxX() + width / 2 - buttonWidth / 2, p.boxY() + p.boxHeight() - 30)
                  .m_252780_(buttonWidth)
                  .m_253136_();
               p.m_142416_(close);
            },
            width,
            20,
            message
         )
         .open();
   }

   public int boxX() {
      return this.x;
   }

   public int boxY() {
      return this.y;
   }

   public int boxWidth() {
      return this.boxWidth;
   }

   public int boxHeight() {
      return this.boxHeight;
   }

   protected void m_7856_() {
      this.boxHeight = 40 + this.messageHeight() + this.widgetsHeight;
      this.x = this.f_96543_ / 2 - this.boxWidth / 2;
      this.y = this.f_96544_ / 2 - this.boxHeight / 2;
      ScreenRectangle widgetsRectangle = new ScreenRectangle(this.x, this.messageTop() + this.messageHeight() + 10, this.boxWidth, this.widgetsHeight);
      this.onInitialized.init(this, widgetsRectangle);
      if (this.previous != null) {
         this.previous.m_6575_(this.f_96541_, this.f_96543_, this.f_96544_);
      }
   }

   private int messageTop() {
      return this.y + 20;
   }

   private int messageHeight() {
      return this.text.m_5770_() * 9;
   }

   public <T extends GuiEventListener & Renderable & NarratableEntry> T m_142416_(T pWidget) {
      return (T)super.m_142416_(pWidget);
   }

   private void close() {
      if (!POPUP_QUEUE.isEmpty()) {
         this.f_96541_.m_91152_(POPUP_QUEUE.poll());
      } else if (this.previous != null) {
         this.f_96541_.m_91152_(this.previous);
      } else {
         this.f_96541_.popGuiLayer();
      }
   }

   private Popup open() {
      Minecraft mc = Minecraft.m_91087_();
      if (mc.f_91080_ instanceof Popup) {
         POPUP_QUEUE.add(this);
      } else {
         mc.m_91152_(this);
      }

      return this;
   }

   public void m_86600_() {
      if (this.previous != null) {
         this.previous.m_86600_();
      }
   }

   public void m_88315_(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
      if (this.previous != null) {
         this.previous.m_88315_(stack, mouseX, mouseY, partialTicks);
      }

      RenderSystem.clear(256, Minecraft.f_91002_);
      stack.m_280024_(0, 0, this.f_96543_, this.f_96544_, -1072689136, -804253680);
      stack.m_280509_(this.x, this.y, this.x + this.boxWidth, this.y + this.boxHeight, 1426063360);
      this.text.m_6514_(stack, this.x + this.boxWidth / 2, this.messageTop(), 9, -1);
      super.m_88315_(stack, mouseX, mouseY, partialTicks);
   }

   public void m_7379_() {
      this.close();
   }

   @FunctionalInterface
   public interface Initializer {
      void init(Popup var1, ScreenRectangle var2);
   }
}
