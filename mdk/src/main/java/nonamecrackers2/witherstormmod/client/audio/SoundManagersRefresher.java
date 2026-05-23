package nonamecrackers2.witherstormmod.client.audio;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;

public class SoundManagersRefresher implements ResourceManagerReloadListener {
   public static final SoundManagersRefresher INSTANCE = new SoundManagersRefresher(Minecraft.getInstance());
   private final Minecraft minecraft;

   private SoundManagersRefresher(Minecraft mc) {
      this.minecraft = mc;
   }

   public void refresh() {
      if (this.minecraft.level != null) {
         ClientLevel world = this.minecraft.level;
         world.getCapability(WitherStormModClientCapabilities.SOUND_MANAGERS).ifPresent(holder -> holder.getManagers().forEach(manager -> manager.refresh()));
      }
   }

   public void onResourceManagerReload(ResourceManager manager) {
      this.refresh();
   }
}
