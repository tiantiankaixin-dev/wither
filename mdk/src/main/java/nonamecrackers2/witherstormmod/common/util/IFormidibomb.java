package nonamecrackers2.witherstormmod.common.util;

import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public interface IFormidibomb {
   int getFuseLife();

   void setLifeFuse(int var1);

   int getStartFuse();

   void setStartFuse(int var1);

   @Nullable
   LivingEntity getFormidibombOwner();

   void setFormidibombOwner(@Nullable LivingEntity var1);

   Vec3 getPosition();

   boolean isStillAlive();

   default void copyFrom(IFormidibomb formidibomb) {
      this.setLifeFuse(formidibomb.getFuseLife());
      this.setStartFuse(formidibomb.getStartFuse());
      this.setFormidibombOwner(formidibomb.getFormidibombOwner());
   }
}
