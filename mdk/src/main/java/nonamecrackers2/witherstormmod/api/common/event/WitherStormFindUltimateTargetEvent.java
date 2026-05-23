package nonamecrackers2.witherstormmod.api.common.event;

import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormFindUltimateTargetEvent extends WitherStormEvent {
   @Nullable
   private LivingEntity target;

   public WitherStormFindUltimateTargetEvent(WitherStormEntity storm, @Nullable LivingEntity originalTarget) {
      super(storm);
      this.target = originalTarget;
   }

   @Nullable
   public LivingEntity getOriginalUltimateTarget() {
      return this.target;
   }

   public void setUltimateTarget(@Nullable LivingEntity target) {
      this.target = target;
   }
}
