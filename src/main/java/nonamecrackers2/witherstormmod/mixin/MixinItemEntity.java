package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
   value = {ItemEntity.class},
   priority = 999
)
public abstract class MixinItemEntity {
   @Redirect(
      method = {"tick"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
         ordinal = 0
      )
   )
   public void tick_makeItemFloatInVoid(ItemEntity entity, Vec3 delta) {
      if (entity.getY() < (double)entity.level().getMinBuildHeight() && this.getItem().is(WitherStormModItemTags.CANNOT_FALL_IN_VOID)) {
         entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.04, 0.0));
      } else {
         entity.setDeltaMovement(delta);
      }
   }

   @Shadow
   public abstract ItemStack getItem();
}
