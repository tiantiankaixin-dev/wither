package nonamecrackers2.witherstormmod.client.capability;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.bus.api.SubscribeEvent;
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
      public static void onClientTick(net.neoforged.neoforge.client.event.ClientTickEvent.Pre event) {
         Minecraft mc = Minecraft.getInstance();
         ClientLevel world = mc.level;
         if (world != null && !mc.isPaused()) {
            SoundManagersHolder holder = world.getData(WitherStormModClientCapabilities.SOUND_MANAGERS);
            for (ISoundManager manager : holder.getManagers()) {
               manager.tick();
            }
         }
      }
   }
}
