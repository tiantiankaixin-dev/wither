package nonamecrackers2.witherstormmod.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;

public class ScreenBlinderEvents {
   @SubscribeEvent
   public static void onClientTick(ClientTickEvent event) {
      if (event.phase == Phase.START) {
         Minecraft mc = Minecraft.getInstance();
         ClientLevel world = mc.level;
         if (world != null && !mc.isPaused()) {
            for (Entity entity : world.entitiesForRendering()) {
               if (entity instanceof LocalPlayer player) {
                  player.getCapability(WitherStormModClientCapabilities.SCREEN_BLINDER).ifPresent(blinder -> blinder.tick());
               }
            }
         }
      }
   }
}
