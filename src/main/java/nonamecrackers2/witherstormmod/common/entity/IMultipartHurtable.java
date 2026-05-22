package nonamecrackers2.witherstormmod.common.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.entity.PartEntity;

public interface IMultipartHurtable<T extends PartEntity<?>> {
   boolean hurt(T var1, DamageSource var2, float var3);
}
