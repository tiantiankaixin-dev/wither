package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Phantom.class})
public interface IMixinPhantom {
   @Accessor
   Vec3 getMoveTargetPoint();

   @Accessor
   void setMoveTargetPoint(Vec3 var1);

   @Accessor
   BlockPos getAnchorPoint();

   @Accessor
   void setAnchorPoint(BlockPos var1);
}
