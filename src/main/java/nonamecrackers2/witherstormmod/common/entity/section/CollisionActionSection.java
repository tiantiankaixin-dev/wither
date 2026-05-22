package nonamecrackers2.witherstormmod.common.entity.section;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class CollisionActionSection extends Section {
   private final Consumer<Entity> action;

   public CollisionActionSection(WitherStormEntity entity, float height, float width, double x, double y, double z, int phase, Consumer<Entity> action) {
      super(entity, height, width, x, y, z, phase);
      this.action = action;
   }

   public CollisionActionSection(
      WitherStormEntity entity, float height, float width, double x, double y, double z, Predicate<WitherStormEntity> isActive, Consumer<Entity> action
   ) {
      super(entity, height, width, x, y, z, isActive);
      this.action = action;
   }

   @Override
   protected void collideWithNearbyEntities() {
      super.collideWithNearbyEntities();

      for (Entity entity : this.owner.level().getEntitiesOfClass(Entity.class, this.boundingBox)) {
         this.action.accept(entity);
      }
   }
}
