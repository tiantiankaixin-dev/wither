package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.common.blockentity.inventory.SuperBeaconMenu;
import nonamecrackers2.witherstormmod.common.blockentity.inventory.SuperSupportBeaconMenu;
import nonamecrackers2.witherstormmod.common.blockentity.inventory.WitheredPhlegmMenu;

public class WitherStormModMenuTypes {
   public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "witherstormmod");
   public static final RegistryObject<MenuType<SuperBeaconMenu>> SUPER_BEACON = MENU_TYPES.register(
      "super_beacon", () -> new MenuType(SuperBeaconMenu::new, FeatureFlags.DEFAULT_FLAGS)
   );
   public static final RegistryObject<MenuType<SuperSupportBeaconMenu>> SUPER_SUPPORT_BEACON = MENU_TYPES.register(
      "super_support_beacon", () -> new MenuType(SuperSupportBeaconMenu::new, FeatureFlags.DEFAULT_FLAGS)
   );
   public static final RegistryObject<MenuType<WitheredPhlegmMenu>> WITHERED_PHLEGM = MENU_TYPES.register(
      "withered_phlegm", () -> new MenuType(WitheredPhlegmMenu::new, FeatureFlags.DEFAULT_FLAGS)
   );
}
