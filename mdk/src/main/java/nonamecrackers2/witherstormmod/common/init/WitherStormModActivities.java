package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class WitherStormModActivities {
   public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(BuiltInRegistries.ACTIVITIES, "witherstormmod");
   public static final DeferredHolder<Activity> WITHER_STORM_PANIC = ACTIVITIES.register("wither_storm_panic", () -> new Activity("wither_storm_panic"));
}
