package nonamecrackers2.witherstormmod.client.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import nonamecrackers2.witherstormmod.client.gui.menu.SuperBeaconScreen;
import nonamecrackers2.witherstormmod.client.gui.menu.WitheredPhlegmScreen;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMenuTypes;

public class WitherStormModMenuScreens {
   public static void register() {
      MenuScreens.register((MenuType)WitherStormModMenuTypes.SUPER_BEACON.get(), SuperBeaconScreen::new);
      MenuScreens.register((MenuType)WitherStormModMenuTypes.SUPER_SUPPORT_BEACON.get(), SuperBeaconScreen::new);
      MenuScreens.register((MenuType)WitherStormModMenuTypes.WITHERED_PHLEGM.get(), WitheredPhlegmScreen::new);
   }
}
