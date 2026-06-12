package nonamecrackers2.witherstormmod.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;

public class PlayerCameraShakerTicker {
   @SubscribeEvent
   public static void onClientTick(ClientTickEvent.Pre event) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      if (world != null && !mc.isPaused()) {
         for (Entity entity : world.entitiesForRendering()) {
            if (entity instanceof LocalPlayer player) {
               player.getData(WitherStormModClientCapabilities.CAMERA_SHAKER).tick();
            }
         }
      }
   }
}
