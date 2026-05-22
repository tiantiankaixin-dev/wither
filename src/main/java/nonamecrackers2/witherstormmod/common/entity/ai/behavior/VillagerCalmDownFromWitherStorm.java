package nonamecrackers2.witherstormmod.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class VillagerCalmDownFromWitherStorm extends Behavior<Villager> {
   public VillagerCalmDownFromWitherStorm() {
      super(ImmutableMap.of());
   }

   protected void start(ServerLevel level, Villager villager, long seed) {
      if (!WitherStormPanicTrigger.isNearWitherStorm(villager)) {
         villager.getBrain().eraseMemory(MemoryModuleType.HURT_BY);
         villager.getBrain().eraseMemory(MemoryModuleType.HURT_BY_ENTITY);
         villager.getBrain().updateActivityFromSchedule(level.getDayTime(), level.getGameTime());
      }
   }
}
