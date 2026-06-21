package nonamecrackers2.witherstormmod.client.gui.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.blockentity.inventory.WitheredPhlegmMenu;

public class WitheredPhlegmScreen extends AbstractContainerScreen<WitheredPhlegmMenu> {
   public static final ResourceLocation CONTAINER_BACKGROUND = WitherStormMod.id("textures/gui/container/withered_phlegm.png");

   public WitheredPhlegmScreen(WitheredPhlegmMenu menu, Inventory inventory, Component title) {
      super(menu, inventory, title);
      this.imageHeight = 207;
      this.inventoryLabelY = 114;
   }

   public void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(stack, mouseX, mouseY, partialTicks);
      super.render(stack, mouseX, mouseY, partialTicks);
      int xp = ((WitheredPhlegmMenu)this.menu).getXp();
      if (xp > 0) {
         int x = this.width / 2 - this.imageWidth / 2 + this.titleLabelX;
         int y = this.height / 2 - this.imageHeight / 2 + this.inventoryLabelY - 20;
         stack.drawString(this.font, Component.literal("XP:"), x, y - 9 - 2, 4210752, false);
         String level = String.valueOf(xp);
         stack.drawString(this.font, level, (float)(x + 1), (float)y, 0, false);
         stack.drawString(this.font, level, (float)(x - 1), (float)y, 0, false);
         stack.drawString(this.font, level, (float)x, (float)(y + 1), 0, false);
         stack.drawString(this.font, level, (float)x, (float)(y - 1), 0, false);
         stack.drawString(this.font, level, (float)x, (float)y, 8453920, false);
      }

      this.renderTooltip(stack, mouseX, mouseY);
   }

   protected void renderBg(GuiGraphics stack, float partialTick, int mouseX, int mouseY) {
      int x = (this.width - this.imageWidth) / 2;
      int y = (this.height - this.imageHeight) / 2;
      stack.blit(CONTAINER_BACKGROUND, x, y, 0, 0, this.imageWidth, this.imageHeight);
   }
}
