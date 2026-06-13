package nonamecrackers2.witherstormmod.client.gui.menu;
import net.minecraft.core.registries.BuiltInRegistries;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.Button.Builder;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import nonamecrackers2.witherstormmod.common.blockentity.inventory.AbstractSuperBeaconMenu;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.SuperBeaconSetEffectMessage;
import nonamecrackers2.witherstormmod.common.packet.SuperBeaconToggleAreaMessage;

public class SuperBeaconScreen extends AbstractContainerScreen<AbstractSuperBeaconMenu> {
   private static final ResourceLocation BORDER = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/gui/container/super_beacon.png");
   private static final ResourceLocation WINDOW = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/gui/container/super_beacon_window.png");
   private static final ResourceLocation BUTTONS = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/gui/container/super_beacon_buttons.png");
   private static final Component SELECTED_EFFECT = Component.translatable("container.witherstormmod.withered_beacon.selected");
   private static final Component AVAILABLE_EFFECTS = Component.translatable("container.witherstormmod.withered_beacon.available_effects");
   private static final Component INFO = Component.translatable("withered_beacon.info");
   private static final int WINDOW_X = 10;
   private static final int WINDOW_Y = 9;
   private static final int WINDOW_WIDTH = 210;
   private static final int WINDOW_HEIGHT = 116;
   private static final int DIVIDED_WINDOW_WIDTH = 105;
   private static final int BUTTON_WIDTH = 22;
   private static final int BOTTOM_BAR_HEIGHT = 32;
   @Nullable
   private Holder<MobEffect> primary;
   private int level;
   private final SuperBeaconScreen.EffectList effectList;
   @Nullable
   private SuperBeaconScreen.BeaconButton select;
   @Nullable
   private SuperBeaconScreen.BeaconButton unselect;
   @Nullable
   private Button info;
   @Nullable
   private Button exitInfo;
   @Nullable
   private SuperBeaconScreen.BeaconButton showArea;
   private boolean shouldRenderInfo;
   private boolean shouldShowArea;
   private int setEffectCooldown;

   public SuperBeaconScreen(final AbstractSuperBeaconMenu menu, Inventory inventory, Component name) {
      super(menu, inventory, name);
      this.imageWidth = 230;
      this.imageHeight = 157;
      this.effectList = new SuperBeaconScreen.EffectList(Minecraft.getInstance(), 105, 116, 9, 125);
      this.effectList.setLeftPos(115);
      menu.addSlotListener(new ContainerListener() {
         public void slotChanged(AbstractContainerMenu container, int slot, ItemStack stack) {
         }

         public void dataChanged(AbstractContainerMenu container, int slot, int value) {
            SuperBeaconScreen.this.primary = menu.getPrimaryEffect();
            SuperBeaconScreen.this.level = menu.getLevel();
            SuperBeaconScreen.this.shouldShowArea = menu.shouldShowArea();
            SuperBeaconScreen.this.setEffectCooldown = menu.getCooldown();
         }
      });
   }

   protected void init() {
      super.init();
      int width = (this.width - this.imageWidth) / 2;
      int height = (this.height - this.imageHeight) / 2;
      this.effectList.updateSize(105, 116, height + 9 + 24, height + 9 + 116 - 4);
      this.effectList.setLeftPos(width + 10 + 105 + 1);
      this.addRenderableWidget(this.effectList);
      int buttonY = 125 + height + 16 - 11;
      int buttonMiddle = 115 + width;
      this.select = (SuperBeaconScreen.BeaconButton)Button.builder(Component.empty(), button -> {
         Holder<MobEffect> effect = this.getSelectedEffect();
         if (effect != null) {
            WitherStormModPacketHandlers.MAIN.sendToServer(new SuperBeaconSetEffectMessage(BuiltInRegistries.MOB_EFFECT.getId(effect.value())));
            this.minecraft.player.closeContainer();
         }
      }).pos(buttonMiddle - 2 - 22, buttonY).size(22, 22).build(builder -> new SuperBeaconScreen.BeaconButton(builder, 88));
      this.unselect = (SuperBeaconScreen.BeaconButton)Button.builder(Component.empty(), button -> {
         if (!this.shouldRenderInfo) {
            WitherStormModPacketHandlers.MAIN.sendToServer(new SuperBeaconSetEffectMessage(0));
         }
      }).pos(buttonMiddle + 2, buttonY).size(22, 22).build(builder -> new SuperBeaconScreen.BeaconButton(builder, 110));
      this.info = Button.builder(Component.literal("i"), button -> {
         this.shouldRenderInfo = true;
         this.addWidget(this.exitInfo);
      }).pos(width + 10, buttonY).size(20, 20).build();
      this.exitInfo = Button.builder(Component.literal("X").withStyle(ChatFormatting.RED), button -> {
         this.shouldRenderInfo = false;
         this.removeWidget(button);
      }).pos(width - 20, height - 30).size(20, 20).build();
      this.showArea = (SuperBeaconScreen.BeaconButton)Button.builder(Component.empty(), button -> {
            WitherStormModPacketHandlers.MAIN.sendToServer(new SuperBeaconToggleAreaMessage(!this.shouldShowArea));
            this.minecraft.player.closeContainer();
         })
         .pos(width + this.imageWidth - 10 - 22, buttonY)
         .size(22, 22)
         .tooltip(Tooltip.create(Component.translatable("gui.witherstormmod.button.showArea.description")))
         .build(builder -> new SuperBeaconScreen.BeaconButton(builder, 132));
      if (this.shouldRenderInfo) {
         this.addWidget(this.exitInfo);
      }

      this.exitInfo.setFGColor(16711680);
      this.addWidget(this.select);
      this.addWidget(this.unselect);
      this.addWidget(this.info);
      this.addWidget(this.showArea);
   }

   public void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(stack, mouseX, mouseY, partialTicks);
      super.render(stack, mouseX, mouseY, partialTicks);
      Holder<MobEffect> effect = this.getSelectedEffect();
      if (this.setEffectCooldown > 0) {
         this.select.setTooltip(Tooltip.create(Component.translatable("gui.witherstormmod.button.select.cooldown.description")));
      } else {
         this.select.setTooltip(null);
      }

      this.select.active = !this.shouldRenderInfo && effect != null && effect != this.primary && this.setEffectCooldown == 0;
      this.unselect.active = !this.shouldRenderInfo && this.primary != null;
      this.info.active = !this.shouldRenderInfo;
      this.showArea.active = !this.shouldRenderInfo;
      this.showArea.overlayTexY = 22 * (this.shouldShowArea ? 6 : 7);
      this.showArea.render(stack, mouseX, mouseY, partialTicks);
      this.select.render(stack, mouseX, mouseY, partialTicks);
      this.unselect.render(stack, mouseX, mouseY, partialTicks);
      this.info.render(stack, mouseX, mouseY, partialTicks);
      if (this.shouldRenderInfo) {
         stack.pose().pushPose();
         stack.pose().translate(0.0, 0.0, 1.0);
         this.renderBackground(stack, mouseX, mouseY, partialTicks);
         this.exitInfo.render(stack, mouseX, mouseY, partialTicks);
         int textX = (this.width - this.imageWidth) / 2 - 20;
         int textWidth = this.imageWidth + 40;
         int textHeight = this.font.wordWrapHeight(INFO.getString(), textWidth);
         int textY = (this.height - this.imageHeight) / 2 + this.imageHeight / 2 - textHeight / 2;
         stack.pose().translate(0.0, 0.0, 1.0);
         List<FormattedCharSequence> text = this.font.split(INFO, textWidth);

         for (int i = 0; i < text.size(); i++) {
            stack.drawString(this.font, text.get(i), textX, i * 9 + textY, -1);
         }

         stack.pose().popPose();
      }
   }

   protected void renderBg(GuiGraphics stack, float partialTicks, int mouseX, int mouseY) {
      this.drawMenu(stack, WINDOW);
   }

   protected void renderLabels(GuiGraphics stack, int mouseX, int mouseY) {
      stack.blit(BORDER, 0, 0, 0, 0, this.imageWidth, this.imageHeight);
      stack.blit(BORDER, 116, 13, 0, this.imageHeight, 105, 20);
      stack.drawCenteredString(this.font, SELECTED_EFFECT, 62, 19, -1);
      stack.drawCenteredString(this.font, AVAILABLE_EFFECTS, 167, 19, -1);
      stack.drawString(
         this.font,
         Component.translatable(
            "container.witherstormmod.withered_beacon.level", new Object[]{this.level > 0 ? Component.translatable("enchantment.level." + this.level) : ""}
         ),
         20,
         115 - 9,
         -1
      );
      if (this.primary != null) {
         MobEffectTextureManager textureManager = this.minecraft.getMobEffectTextures();
         TextureAtlasSprite sprite = textureManager.get(this.primary);
         int x = 62 - (int)((float)sprite.contents().width() * 1.5F);
         int y = 67 - (int)((float)sprite.contents().height() * 1.5F) - 8;
         stack.blit(x, y, 0, 54, 54, sprite);
         stack.drawCenteredString(this.font, this.primary.value().getDisplayName(), 62, y + sprite.contents().width() * 3, -1);
      }
   }

   private void drawMenu(GuiGraphics stack, ResourceLocation tex) {
      stack.setColor(1.0F, 1.0F, 1.0F, 1.0F);
      int width = (this.width - this.imageWidth) / 2;
      int height = (this.height - this.imageHeight) / 2;
      stack.blit(tex, width, height, 0, 0, this.imageWidth, this.imageHeight);
   }

   @Nullable
   private Holder<MobEffect> getSelectedEffect() {
      SuperBeaconScreen.EffectList.Entry entry = (SuperBeaconScreen.EffectList.Entry)this.effectList.getSelected();
      return entry != null ? entry.getEffect() : null;
   }

   public void setValidEffects(Set<Holder<MobEffect>> effects) {
      this.effectList.clear();

      for (Holder<MobEffect> effect : effects) {
         this.effectList.addEffect(effect);
      }
   }

   private static class BeaconButton extends Button {
      public int overlayTexY;

      public BeaconButton(Builder builder, int overlayTexY) {
         super(builder);
         this.overlayTexY = overlayTexY;
      }

      public void renderWidget(GuiGraphics stack, int mouseX, int mouseY, float partialTikcs) {
         int i = 0;
         if (this.isHoveredOrFocused()) {
            i += 22;
         }

         if (!this.isActive()) {
            i = 44;
         }

         RenderSystem.enableDepthTest();
         stack.blit(SuperBeaconScreen.BUTTONS, this.getX(), this.getY(), 0.0F, (float)i, this.width, this.height, 255, 255);
         stack.blit(SuperBeaconScreen.BUTTONS, this.getX(), this.getY(), 0.0F, (float)this.overlayTexY, this.width, this.height, 255, 255);
      }
   }

   public static class EffectList extends ObjectSelectionList<SuperBeaconScreen.EffectList.Entry> {
      public EffectList(Minecraft mc, int width, int height, int top, int bottom) {
         super(mc, width, bottom - top, top, 20);
      }

      public void addEffect(Holder<MobEffect> effect) {
         this.addEntry(new SuperBeaconScreen.EffectList.Entry(effect));
      }

      public int getRowWidth() {
         return this.getWidth();
      }

      protected int getScrollbarPosition() {
         return this.getX() + this.getWidth() - 7;
      }

      public void setLeftPos(int left) {
         this.setX(left);
      }

      public void updateSize(int width, int height, int top, int bottom) {
         this.setY(top);
         this.setSize(width, bottom - top);
      }

      public void clear() {
         this.clearEntries();
      }

      public class Entry extends net.minecraft.client.gui.components.ObjectSelectionList.Entry<SuperBeaconScreen.EffectList.Entry> {
         private final Holder<MobEffect> effect;
         private final Component name;

         private Entry(Holder<MobEffect> effect) {
            this.effect = effect;
            this.name = effect.value().getDisplayName().copy();
         }

         public Component getNarration() {
            return this.name;
         }

         public void render(
            GuiGraphics stack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean selected, float partialTicks
         ) {
            MobEffectTextureManager textureManager = EffectList.this.minecraft.getMobEffectTextures();
            TextureAtlasSprite sprite = textureManager.get(this.effect);
            stack.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            stack.blit(left, top + height / 2 - 9, 0, 18, 18, sprite);
            stack.drawString(EffectList.this.minecraft.font, this.name, left + 28, top + height / 2 - 9 / 2, -1);
         }

         public Holder<MobEffect> getEffect() {
            return this.effect;
         }

         public boolean mouseClicked(double x, double y, int clickType) {
            if (clickType == 0) {
               EffectList.this.setSelected(this);
               return true;
            } else {
               return false;
            }
         }
      }
   }
}
