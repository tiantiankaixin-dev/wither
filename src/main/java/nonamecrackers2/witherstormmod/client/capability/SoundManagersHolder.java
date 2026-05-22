package nonamecrackers2.witherstormmod.client.capability;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.client.audio.ISoundManager;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;

public class SoundManagersHolder {
   private final List<ISoundManager> managers = new ArrayList<>();

   public List<ISoundManager> getManagers() {
      return ImmutableList.copyOf(this.managers);
   }

   public void putManager(ISoundManager manager) {
      this.managers.add(manager);
   }

   public static class Events {
      @SubscribeEvent
      public static void onClientTick(ClientTickEvent event) {
         Minecraft mc = Minecraft.getInstance();
         if (event.phase == Phase.START) {
            ClientLevel world = mc.level;
            if (world != null && !mc.isPaused()) {
               world.getCapability(WitherStormModClientCapabilities.SOUND_MANAGERS).ifPresent(holder -> {
                  for (ISoundManager manager : holder.getManagers()) {
                     manager.tick();
                  }
               });
            }
         }
      }
   }
}
