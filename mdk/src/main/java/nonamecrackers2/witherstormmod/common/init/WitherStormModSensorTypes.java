package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import nonamecrackers2.witherstormmod.common.entity.ai.sensing.WitherStormSensor;

public class WitherStormModSensorTypes {
   public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(BuiltInRegistries.SENSOR_TYPES, "witherstormmod");
   public static final DeferredHolder<SensorType<?>, SensorType<WitherStormSensor>> WITHER_STORM_SENSOR = SENSOR_TYPES.register(
      "wither_storm", () -> new SensorType(WitherStormSensor::new)
   );
}
