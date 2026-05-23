package nonamecrackers2.witherstormmod.common.entity.goal;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

public class RemovableGoals {
   private final List<Pair<Integer, Goal>> goals;

   private RemovableGoals(List<Pair<Integer, Goal>> goals) {
      this.goals = goals;
   }

   public void putGoals(GoalSelector selector) {
      this.goals.forEach(pair -> {
         if (!selector.getAvailableGoals().stream().map(WrappedGoal::getGoal).collect(Collectors.toSet()).contains(pair.getSecond())) {
            selector.addGoal((Integer)pair.getFirst(), (Goal)pair.getSecond());
         }
      });
   }

   public void removeGoals(GoalSelector selector) {
      this.goals.forEach(pair -> selector.removeGoal((Goal)pair.getSecond()));
   }

   public static class Builder {
      private final List<Pair<Integer, Goal>> goals = Lists.newArrayList();

      private Builder() {
      }

      public static RemovableGoals.Builder builder() {
         return new RemovableGoals.Builder();
      }

      public RemovableGoals.Builder put(int priority, Goal goal) {
         this.goals.add(Pair.of(priority, goal));
         return this;
      }

      public RemovableGoals build(GoalSelector selector) {
         if (this.goals.size() > 0) {
            RemovableGoals goals = new RemovableGoals(this.goals);
            goals.putGoals(selector);
            return goals;
         } else {
            throw new IllegalStateException("No goals present");
         }
      }
   }
}
