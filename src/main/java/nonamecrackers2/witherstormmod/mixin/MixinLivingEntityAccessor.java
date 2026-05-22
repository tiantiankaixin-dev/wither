package nonamecrackers2.witherstormmod.mixin;

import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({LivingEntity.class})
public interface MixinLivingEntityAccessor {
   @Accessor("lastHurtByPlayer")
   @Nullable
   Player witherstormmod$getLastHurtByPlayer();
}
