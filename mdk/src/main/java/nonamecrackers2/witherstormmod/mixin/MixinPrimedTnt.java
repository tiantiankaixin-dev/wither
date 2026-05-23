package nonamecrackers2.witherstormmod.mixin;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({PrimedTnt.class})
public abstract class MixinPrimedTnt extends Entity {
   private MixinPrimedTnt(EntityType<?> type, Level level) {
      super(type, level);
   }

   @ModifyVariable(
      method = {"tick"},
      at = @At("STORE"),
      ordinal = 0
   )
   public int tickModifyFuse(int i) {
      if (!((PrimedTnt)(Object)this instanceof FormidibombEntity)) {
         List<WitherStormEntity> storms = this.level().getEntitiesOfClass(WitherStormEntity.class, this.getBoundingBox().inflate(100.0, 200.0, 100.0));
         WitherStormEntity storm = WorldUtil.getNearest(storms, this.position(), Entity::position);
         if (storm != null) {
            Pair<Boolean, Integer> pair = TractorBeamHelper.isInsideTractorBeam(this, storm, 4.0);
            if ((Boolean)pair.getFirst()) {
               Vec3 pos = storm.getHeadPos((Integer)pair.getSecond());
               if (!(pos.distanceTo(this.position()) > 12.0)) {
                  return 0;
               }

               if (i == 20) {
                  return 80;
               }
            }
         }
      }

      return i;
   }

   @Shadow
   public abstract void setFuse(int var1);
}
