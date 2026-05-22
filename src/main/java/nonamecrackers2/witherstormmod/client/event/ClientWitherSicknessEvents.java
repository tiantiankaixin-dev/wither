package nonamecrackers2.witherstormmod.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;

public class ClientWitherSicknessEvents {
   @SubscribeEvent
   public static void onClientTick(ClientTickEvent event) {
      if (event.phase == Phase.END) {
         Minecraft mc = Minecraft.getInstance();
         ClientLevel world = mc.level;
         if (world != null && !mc.isPaused()) {
            for (Entity entity : world.entitiesForRendering()) {
               if (entity instanceof LivingEntity living) {
                  living.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(tracker -> tracker.tick());
               }
            }
         }
      }
   }
}
