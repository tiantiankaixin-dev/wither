package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Creeper.class})
public interface IMixinCreeper {
   @Accessor
   void setExplosionRadius(int var1);

   @Accessor
   void setMaxSwell(int var1);
}
