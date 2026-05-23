package nonamecrackers2.crackerslib.mixin;

import java.util.Set;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({BlockEntityType.class})
public interface MixinBlockEntityType {
   @Accessor("validBlocks")
   Set<Block> crackerslib$getValidBlocks();

   @Mutable
   @Accessor("validBlocks")
   void crackerslib$setValidBlocks(Set<Block> var1);
}
