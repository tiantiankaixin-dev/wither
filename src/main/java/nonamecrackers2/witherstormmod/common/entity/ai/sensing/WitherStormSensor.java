package nonamecrackers2.witherstormmod.common.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.phys.AABB;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMemoryTypes;

public class WitherStormSensor extends Sensor<LivingEntity> {
   protected void doTick(ServerLevel level, LivingEntity entity) {
      AABB box = entity.getBoundingBox().inflate(300.0);
      List<WitherStormEntity> storms = level.getEntitiesOfClass(WitherStormEntity.class, box, stormx -> stormx != entity && stormx.isAlive());
      storms.sort(Comparator.comparingDouble(entity::distanceToSqr));
      Optional<WitherStormEntity> storm = storms.stream().findFirst();
      entity.getBrain().setMemory((MemoryModuleType<WitherStormEntity>)WitherStormModMemoryTypes.NEAREST_WITHER_STORM.get(), storm);
   }

   public Set<MemoryModuleType<?>> requires() {
      return ImmutableSet.of((MemoryModuleType<WitherStormEntity>)WitherStormModMemoryTypes.NEAREST_WITHER_STORM.get());
   }
}
