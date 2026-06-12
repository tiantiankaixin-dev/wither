package nonamecrackers2.witherstormmod.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEffects;

public class RenderWitherSicknessOverlay {
   private static final ResourceLocation WITHER_SICKNESS_ICONS = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/gui/wither_sickness.png");
   private final RandomSource random = RandomSource.create();
   private int lastHealth;
   private int displayHealth;
   private long lastHealthTime;
   private long healthBlinkTime;

   @SubscribeEvent
   public void renderOverlay(RenderGuiLayerEvent.Pre event) {
      if (!event.getName().equals(VanillaGuiLayers.PLAYER_HEALTH)) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.options.hideGui) return;
      LocalPlayer player = mc.player;
      if (player == null) return;
      if (!player.hasEffect(WitherStormModEffects.WITHER_SICKNESS.get())) return;

      event.setCanceled(true);
      GuiGraphics guiGraphics = event.getGuiGraphics();
      int width = mc.getWindow().getGuiScaledWidth();
      int height = mc.getWindow().getGuiScaledHeight();

      RenderSystem.enableBlend();
      int health = Mth.ceil(player.getHealth());
      int tickCount = mc.gui.getGuiTicks();
      boolean highlight = this.healthBlinkTime > (long)tickCount && (this.healthBlinkTime - (long)tickCount) / 3L % 2L == 1L;
      if (health < this.lastHealth && player.invulnerableTime > 0) {
         this.lastHealthTime = Util.getMillis();
         this.healthBlinkTime = (long)(tickCount + 20);
      } else if (health > this.lastHealth && player.invulnerableTime > 0) {
         this.lastHealthTime = Util.getMillis();
         this.healthBlinkTime = (long)(tickCount + 10);
      }

      if (Util.getMillis() - this.lastHealthTime > 1000L) {
         this.lastHealth = health;
         this.displayHealth = health;
         this.lastHealthTime = Util.getMillis();
      }

      this.lastHealth = health;
      int healthLast = this.displayHealth;
      AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
      float healthMax = (float)maxHealth.get();
      int absorbtion = Mth.ceil(player.getAbsorptionAmount());
      int healthRows = Mth.ceil((healthMax + (float)absorbtion) / 2.0F / 10.0F);
      int rowHeight = Math.max(10 - (healthRows - 2), 3);
      this.random.setSeed((long)(tickCount * 312871));
      int left = width / 2 - 91;
      int top = height - 39; // approximate leftHeight baseline

      int regen = -1;
      if (player.hasEffect(MobEffects.REGENERATION)) {
         regen = tickCount % Mth.ceil(healthMax + 5.0F);
      }

      int TOP = player.level().getLevelData().isHardcore() ? 9 : 0;
      int BACKGROUND = highlight ? 25 : 16;
      int margin = 34;
      float absorbtionRemaining = (float)absorbtion;

      for (int i = Mth.ceil((healthMax + (float)absorbtion) / 2.0F) - 1; i >= 0; i--) {
         int row = Mth.ceil((float)(i + 1) / 10.0F) - 1;
         int x = left + i % 10 * 8;
         int y = top - row * rowHeight;
         if (health <= 4) {
            y += this.random.nextInt(2);
         }

         if (i == regen) {
            y -= 2;
         }

         guiGraphics.blit(WITHER_SICKNESS_ICONS, x, y, BACKGROUND, TOP, 9, 9);
         if (highlight) {
            if (i * 2 + 1 < healthLast) {
               guiGraphics.blit(WITHER_SICKNESS_ICONS, x, y, margin, TOP, 9, 9);
            } else if (i * 2 + 1 == healthLast) {
               guiGraphics.blit(WITHER_SICKNESS_ICONS, x, y, margin + 9, TOP, 9, 9);
            }
         }

         if (absorbtionRemaining > 0.0F) {
            if (absorbtionRemaining == (float)absorbtion && (float)absorbtion % 2.0F == 1.0F) {
               guiGraphics.blit(WITHER_SICKNESS_ICONS, x, y, margin + 9, TOP, 9, 9);
               absorbtionRemaining--;
            } else {
               guiGraphics.blit(WITHER_SICKNESS_ICONS, x, y, margin, TOP, 9, 9);
               absorbtionRemaining -= 2.0F;
            }
         } else if (i * 2 + 1 < health) {
            guiGraphics.blit(WITHER_SICKNESS_ICONS, x, y, margin, TOP, 9, 9);
         } else if (i * 2 + 1 == health) {
            guiGraphics.blit(WITHER_SICKNESS_ICONS, x, y, margin + 9, TOP, 9, 9);
         }
      }

      RenderSystem.disableBlend();
   }
}
