package nonamecrackers2.witherstormmod.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModActivities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMemoryTypes;

public class WitherStormPanicTrigger extends Behavior<LivingEntity> {
   public WitherStormPanicTrigger() {
      super(ImmutableMap.of());
   }

   protected boolean canStillUse(ServerLevel level, LivingEntity entity, long seed) {
      return entity.getBrain().hasMemoryValue((MemoryModuleType)WitherStormModMemoryTypes.NEAREST_WITHER_STORM.get());
   }

   protected void start(ServerLevel level, LivingEntity entity, long seed) {
      if (isNearWitherStorm(entity)) {
         Brain<?> brain = entity.getBrain();
         if (!brain.isActive(Activity.PANIC)) {
            brain.eraseMemory(MemoryModuleType.PATH);
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            brain.eraseMemory(MemoryModuleType.BREED_TARGET);
            brain.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            brain.eraseMemory(MemoryModuleType.DOORS_TO_CLOSE);
            brain.eraseMemory(MemoryModuleType.INTERACTABLE_DOORS);
            brain.eraseMemory(MemoryModuleType.MEETING_POINT);
            brain.eraseMemory(MemoryModuleType.HIDING_PLACE);
            brain.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            brain.eraseMemory(MemoryModuleType.HOME);
         }

         brain.setActiveActivityIfPossible((Activity)WitherStormModActivities.WITHER_STORM_PANIC.get());
      }
   }

   public static boolean isNearWitherStorm(LivingEntity entity) {
      WitherStormEntity storm = (WitherStormEntity)entity.getBrain()
         .getMemory((MemoryModuleType)WitherStormModMemoryTypes.NEAREST_WITHER_STORM.get())
         .orElse(null);
      if (storm != null && !storm.isDeadOrPlayingDead()) {
         return storm.getPhase() < 4 ? entity.getBoundingBox().inflate(100.0).contains(storm.position()) : true;
      } else {
         return false;
      }
   }
}
