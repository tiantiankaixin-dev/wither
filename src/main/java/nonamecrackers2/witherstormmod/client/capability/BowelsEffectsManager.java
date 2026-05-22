package nonamecrackers2.witherstormmod.client.capability;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.util.BowelsSpecialEffects;

public class BowelsEffectsManager {
   private final Minecraft minecraft;
   private final Random random = new Random();
   private int nextTremble = 300 + this.random.nextInt(600);
   private int nextScream = 100 + this.random.nextInt(240);

   public BowelsEffectsManager(Minecraft minecraft) {
      this.minecraft = minecraft;
   }

   public BowelsEffectsManager() {
      this.minecraft = null;
   }

   public void tick() {
      LocalPlayer player = this.minecraft.player;
      float timeReduction = 1.0F;
      CommandBlockEntity lowest = null;
      float lowestHealth = -1.0F;

      for (Entity entity : this.minecraft.level.entitiesForRendering()) {
         if (entity instanceof CommandBlockEntity) {
            CommandBlockEntity commandBlock = (CommandBlockEntity)entity;
            if (lowestHealth == -1.0F || commandBlock.getHealth() < lowestHealth) {
               lowest = commandBlock;
               lowestHealth = commandBlock.getHealth();
            }
         }
      }

      if (lowest != null && lowest.getHealth() < lowest.getMaxHealth()) {
         timeReduction = Math.max(0.05F, lowest.getHealth() / lowest.getMaxHealth() * 0.3F);
         if (this.nextScream > 0) {
            this.nextScream--;
            if (this.nextScream == 0) {
               player.playNotifySound(WitherStormModSoundEvents.BOWELS_LOUD_HURT.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
               this.nextScream = 120 + this.random.nextInt(120);
            }
         }
      }

      if (this.nextTremble > 0) {
         this.nextTremble--;
         if (this.nextTremble == 0) {
            float extraShakeStrength = 0.0F;
            if (lowest != null && lowest.getHealth() < lowest.getMaxHealth()) {
               extraShakeStrength = 4.0F;
            }

            PlayerCameraShaker shaker = (PlayerCameraShaker)player.getCapability(WitherStormModClientCapabilities.CAMERA_SHAKER).orElse(null);
            if (shaker != null) {
               shaker.shake(60.0F, 2.0F + extraShakeStrength);
            }

            player.playNotifySound(WitherStormModSoundEvents.BOWELS_TREMBLE.get(), SoundSource.AMBIENT, 1.0F, 1.0F);
            this.nextTremble = (int)((float)(240 + this.random.nextInt(720)) * timeReduction);
         }
      }
   }

   public static class Events {
      @SubscribeEvent
      public static void tickAmbience(ClientTickEvent event) {
         Minecraft mc = Minecraft.getInstance();
         if (event.phase == Phase.END) {
            ClientLevel world = mc.level;
            if (world != null && !mc.isPaused()) {
               world.getCapability(WitherStormModClientCapabilities.BOWELS_EFFECTS_MANAGER).ifPresent(manager -> manager.tick());
            }
         }
      }

      public static void registerSpecialEffects(RegisterDimensionSpecialEffectsEvent event) {
         event.register(WitherStormMod.id("bowels"), new BowelsSpecialEffects());
      }
   }
}
