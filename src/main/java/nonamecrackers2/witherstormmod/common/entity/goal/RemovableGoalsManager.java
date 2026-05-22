package nonamecrackers2.witherstormmod.common.entity.goal;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.world.entity.ai.goal.GoalSelector;

public class RemovableGoalsManager {
   private final Map<String, RemovableGoals> goalLists = Maps.newHashMap();

   public void put(String id, RemovableGoals goals) {
      this.goalLists.put(id, goals);
   }

   public void putGoals(String id, GoalSelector selector) {
      this.goalLists.get(id).putGoals(selector);
   }

   public void removeGoals(String id, GoalSelector selector) {
      this.goalLists.get(id).removeGoals(selector);
   }
}
