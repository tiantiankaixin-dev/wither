package nonamecrackers2.crackerslib.client.gui.widget;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

public class SelectableNamedObjectList<T> extends ObjectSelectionList<SelectableNamedObjectList.Entry<T>> {
   @Nullable
   private Consumer<T> onObjectSelected;

   public SelectableNamedObjectList(Minecraft pMinecraft, int pWidth, int pHeight, int pY0, int pY1) {
      super(pMinecraft, pWidth, pHeight, pY0, pY1, 9 + 5);
      this.m_93488_(false);
      this.m_93496_(false);
   }

   public void setOnObjectSelectedCallback(Consumer<T> callback) {
      this.onObjectSelected = callback;
   }

   public void addObject(Component name, T object) {
      this.m_7085_(new SelectableNamedObjectList.Entry<T>(this, name, object));
   }

   public int m_5759_() {
      return this.getWidth();
   }

   @Nullable
   public T getSelectedObject() {
      return this.m_93511_() != null ? ((SelectableNamedObjectList.Entry)this.m_93511_()).object : null;
   }

   protected int m_5756_() {
      return this.getLeft() + this.getWidth() - 5;
   }

   protected void m_7733_(GuiGraphics stack) {
      stack.m_280509_(this.f_93393_, this.f_93390_, this.f_93392_, this.f_93391_, 1426063360);
   }

   public void setSelected(SelectableNamedObjectList.Entry<T> pSelected) {
      if (this.onObjectSelected != null && pSelected != null) {
         this.onObjectSelected.accept(pSelected.object);
      }

      super.m_6987_(pSelected);
   }

   public static class Entry<T> extends net.minecraft.client.gui.components.ObjectSelectionList.Entry<SelectableNamedObjectList.Entry<T>> {
      private final SelectableNamedObjectList<T> list;
      private final Component text;
      private final T object;

      public Entry(SelectableNamedObjectList<T> list, Component text, T object) {
         this.list = list;
         this.text = text;
         this.object = object;
      }

      public Component m_142172_() {
         return this.text;
      }

      public void m_6311_(
         GuiGraphics stack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick
      ) {
         Font font = this.list.f_93386_.f_91062_;
         stack.m_280430_(font, this.text, pLeft + 2, pTop + pHeight / 2 - 9 / 2, -1);
      }

      public boolean m_6375_(double pMouseX, double pMouseY, int pButton) {
         if (pButton == 0) {
            this.list.setSelected(this);
            return true;
         } else {
            return false;
         }
      }
   }
}
