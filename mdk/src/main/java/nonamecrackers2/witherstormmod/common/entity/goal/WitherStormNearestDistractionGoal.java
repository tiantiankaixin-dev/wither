package nonamecrackers2.witherstormmod.common.entity.goal;

import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormNearestDistractionGoal extends NearestDistractionGoal<WitherStormEntity> {
   public WitherStormNearestDistractionGoal(WitherStormEntity entity, int head, Predicate<Entity> condition, int interval) {
      super(entity, head, condition, interval);
   }

   @Override
   protected AABB getSearchArea() {
      return this.entity.getSearchBox();
   }

   @Override
   protected Vec3 getTargetPos() {
      return this.target instanceof FireworkRocketEntity ? super.getTargetPos().add(0.0, 10.0, 0.0) : super.getTargetPos();
   }
}
