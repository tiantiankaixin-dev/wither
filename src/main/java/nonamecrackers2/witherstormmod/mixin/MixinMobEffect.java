package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({MobEffect.class})
public class MixinMobEffect {
   @Redirect(
      method = {"applyInstantenousEffect"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V"
      )
   )
   public void redirectHeal(LivingEntity entity, float amount) {
      if (!(entity instanceof CommandBlockEntity)) {
         entity.heal(amount);
      }
   }

   @Redirect(
      method = {"applyInstantenousEffect"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
      )
   )
   public boolean redirectHurt(LivingEntity entity, DamageSource source, float amount) {
      if (WitherStormModMobTypes.isSickened(entity) && !(entity instanceof WitherStormEntity)) {
         return false;
      } else {
         return entity instanceof WitherStormEntity && ((WitherStormEntity)entity).getPhase() >= 3 ? false : entity.hurt(source, amount);
      }
   }
}
