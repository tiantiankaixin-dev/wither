package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WitherStormModActivities {
   public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, "witherstormmod");
   public static final RegistryObject<Activity> WITHER_STORM_PANIC = ACTIVITIES.register("wither_storm_panic", () -> new Activity("wither_storm_panic"));
}
