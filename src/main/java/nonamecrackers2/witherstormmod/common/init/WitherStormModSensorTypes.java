package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.common.entity.ai.sensing.WitherStormSensor;

public class WitherStormModSensorTypes {
   public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, "witherstormmod");
   public static final RegistryObject<SensorType<WitherStormSensor>> WITHER_STORM_SENSOR = SENSOR_TYPES.register(
      "wither_storm", () -> new SensorType(WitherStormSensor::new)
   );
}
