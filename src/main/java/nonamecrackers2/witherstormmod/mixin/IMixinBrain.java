package nonamecrackers2.witherstormmod.mixin;

import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.ExpirableValue;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Brain.class})
public interface IMixinBrain<E extends LivingEntity> {
   @Accessor
   Map<Activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>>> getActivityRequirements();

   @Accessor
   Map<Activity, Set<MemoryModuleType<?>>> getActivityMemoriesToEraseWhenStopped();

   @Accessor
   Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> getMemories();

   @Accessor
   Map<SensorType<? extends Sensor<? super E>>, Sensor<? super E>> getSensors();

   @Accessor
   Set<Activity> getCoreActivities();
}
