package nonamecrackers2.witherstormmod.common.entity.section;

import java.util.function.Predicate;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class FallingSection extends Section {
   public FallingSection(WitherStormEntity entity, float height, float width, double x, double y, double z, Predicate<WitherStormEntity> isActive) {
      super(entity, height, width, x, y, z, isActive);
   }

   public FallingSection(WitherStormEntity entity, float height, float width, double x, double y, double z, int phase) {
      super(entity, height, width, x, y, z, phase);
   }

   @Override
   public Vec3 getOffset() {
      double sin = Math.sin(Math.toRadians((double)this.owner.xBodyRot));
      double x = this.offset.x * sin;
      double y = this.offset.y;
      double z = this.offset.z * sin;
      return new Vec3(x, y, z);
   }
}
