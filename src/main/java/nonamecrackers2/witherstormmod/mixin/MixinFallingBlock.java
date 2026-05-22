package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.FallingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({FallingBlock.class})
public interface MixinFallingBlock {
   @Invoker
   void callFalling(FallingBlockEntity var1);
}
