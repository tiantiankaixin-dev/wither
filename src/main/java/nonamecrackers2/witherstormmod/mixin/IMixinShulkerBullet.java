package nonamecrackers2.witherstormmod.mixin;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ShulkerBullet.class})
public interface IMixinShulkerBullet {
   @Accessor
   void setFinalTarget(Entity var1);

   @Accessor
   void setCurrentMoveDirection(Direction var1);

   @Invoker
   void callSelectNextMoveDirection(@Nullable Axis var1);
}
