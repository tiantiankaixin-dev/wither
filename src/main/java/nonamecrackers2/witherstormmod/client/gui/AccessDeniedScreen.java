package nonamecrackers2.witherstormmod.client.gui;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import nonamecrackers2.witherstormmod.client.util.Contributors;

@Deprecated
public class AccessDeniedScreen extends Screen {
   private static final Component TITLE = Component.literal("Whoops!").withStyle(Style.EMPTY.withBold(true));
   private static final Component DESCRIPTION = Component.literal(
      "Looks like you do not have access to this build of Cracker's Wither Storm Mod. This is an in-development build exclusive for BETA testers, patrons, etc. The content in this in-development build will be made available freely to the public when it is deemed to be finished."
   );
   private static final Component PATREON = Component.literal(
      "If you're a nonamecrackers2 patron, open the link below and sign in with your Patreon account to gain access."
   );
   private static final Component NOTE = Component.literal(
         "NOTE: The Minecraft account used to launch this game will be associated with your Patreon account."
      )
      .withStyle(ChatFormatting.DARK_GRAY);
   private static final Component ERROR = Component.literal(
      "Something went wrong while authenticating! Please check your internet connection. If the problem persists, please notify nonamecrackers2 and provide the latest.log file."
   );
   private Button openLink;
   private Button refresh;
   private Contributors.Result access;
   @Nullable
   private CompletableFuture<Contributors.Result> resultGetter;

   public AccessDeniedScreen(Contributors.Result access) {
      super(Component.empty());
      this.access = access;
   }

   private String getUserId() {
      return this.minecraft.getUser().getProfileId().toString();
   }

   protected void init() {
      this.openLink = Button.builder(Component.literal("Open Link"), button -> {
         String url = "https://patronauthenticator-sp4uwbgqwa-uc.a.run.app/login?mc_uuid=" + this.getUserId();
         this.minecraft.setScreen(new ConfirmLinkScreen(b -> {
            if (b) {
               Util.getPlatform().openUri(url);
            }

            this.minecraft.setScreen(this);
         }, url, true));
      }).width(100).pos(this.width / 2 - 120, 210).build();
      this.refresh = Button.builder(Component.literal("Refresh"), button -> this.refresh())
         .width(100)
         .pos(this.width / 2 + 20, 210)
         .build();
      if (this.access != Contributors.Result.ERROR) {
         this.addRenderableWidget(this.openLink);
      } else {
         this.refresh.setX(this.width / 2 - this.refresh.getWidth() / 2);
      }

      this.addRenderableWidget(this.refresh);
   }

   public void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(stack, mouseX, mouseY, partialTicks);
      int y = this.height / 8;
      stack.drawCenteredString(this.font, TITLE, this.width / 2, y, -1);
      if (this.access != Contributors.Result.ERROR) {
         int var6 = drawSplitText(stack, this.font, DESCRIPTION, this.width - 50, this.width / 2, y + 30, -1);
         int var7 = drawSplitText(stack, this.font, PATREON, this.width - 50, this.width / 2, var6 + 20, -1);
         y = drawSplitText(stack, this.font, NOTE, this.width - 50, this.width / 2, var7 + 20, -1);
         this.openLink.setY(y + this.openLink.getHeight() + 10);
      } else {
         y = drawSplitText(stack, this.font, ERROR, this.width - 50, this.width / 2, y + 30, -1);
      }

      this.refresh.setY(y + this.openLink.getHeight() + 10);
      super.render(stack, mouseX, mouseY, partialTicks);
   }

   private static int drawSplitText(GuiGraphics stack, Font font, Component text, int width, int x, int y, int color) {
      List<FormattedCharSequence> desc = font.split(text, width);
      int lastY = y;

      for (int i = 0; i < desc.size(); i++) {
         FormattedCharSequence line = desc.get(i);
         lastY = y + i * (9 + 2);
         stack.drawCenteredString(font, line, x, lastY, color);
      }

      return lastY;
   }

   public void tick() {
      this.minecraft.getMusicManager().tick();
      this.minecraft.getSoundManager().tick(false);
      if (this.resultGetter != null && this.resultGetter.isDone()) {
         try {
            this.access = this.resultGetter.get();
            if (!this.access.canLaunchGame()) {
               this.repositionElements();
            } else {
               this.onClose();
            }
         } catch (ExecutionException | InterruptedException var2) {
         }

         this.resultGetter = null;
      }
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public void refresh() {
      this.refresh.active = false;
      this.openLink.active = false;
      if (this.resultGetter == null) {
         this.resultGetter = CompletableFuture.supplyAsync(() -> Contributors.getAccess(this.getUserId()));
      }
   }
}
