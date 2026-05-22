package nonamecrackers2.witherstormmod.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({FishingHook.class})
public abstract class MixinFishingHook extends Projectile {
   private MixinFishingHook(EntityType<? extends Projectile> type, Level level) {
      super(type, level);
   }

   @Redirect(
      method = {"pullEntity"},
      at = @At(
         value = "NEW",
         target = "(DDD)Lnet/minecraft/world/phys/Vec3;"
      )
   )
   public Vec3 getPullEntityDelta(double x, double y, double z, Entity toPull) {
      Vec3 vec = new Vec3(x, y, z);

      for (WitherStormEntity storm : this.level().getEntitiesOfClass(WitherStormEntity.class, this.getBoundingBox().inflate(100.0, 200.0, 100.0))) {
         Pair<Boolean, Integer> result = TractorBeamHelper.isInsideTractorBeam(toPull, storm, 4.0);
         if (!storm.isDeadOrPlayingDead() && (Boolean)result.getFirst()) {
            storm.getIgnoredTargets().addEntityToIgnore(toPull);
         }
      }

      return vec;
   }
}
