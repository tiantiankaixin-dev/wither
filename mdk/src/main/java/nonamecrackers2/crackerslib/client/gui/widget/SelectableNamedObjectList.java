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
      this.setVisible(false);
      this.setFocused(false);
   }

   public void setOnObjectSelectedCallback(Consumer<T> callback) {
      this.onObjectSelected = callback;
   }

   public void addObject(Component name, T object) {
      this.removeEntry(new SelectableNamedObjectList.Entry<T>(this, name, object));
   }

   public int getRowWidth() {
      return this.getWidth();
   }

   @Nullable
   public T getSelectedObject() {
      return this.getSelected() != null ? ((SelectableNamedObjectList.Entry)this.getSelected()).object : null;
   }

   protected int getScrollbarPosition() {
      return this.getLeft() + this.getWidth() - 5;
   }

   protected void renderBackground(GuiGraphics stack) {
      stack.fill(this.x0, this.y0, this.x1, this.y1, 1426063360);
   }

   public void setSelected(SelectableNamedObjectList.Entry<T> pSelected) {
      if (this.onObjectSelected != null && pSelected != null) {
         this.onObjectSelected.accept(pSelected.object);
      }

      super.setSelected(pSelected);
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

      public Component getNarration() {
         return this.text;
      }

      public void render(
         GuiGraphics stack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTick
      ) {
         Font font = this.list.minecraft.font;
         stack.drawString(font, this.text, pLeft + 2, pTop + pHeight / 2 - 9 / 2, -1);
      }

      public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
         if (pButton == 0) {
            this.list.setSelected(this);
            return true;
         } else {
            return false;
         }
      }
   }
}
