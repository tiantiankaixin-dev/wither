package nonamecrackers2.witherstormmod.mixin;

import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({AreaEffectCloud.class})
public interface MixinAreaEffectCloud {
   @Accessor("effects")
   List<MobEffectInstance> witherstormmod$getEffects();
}
