package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.item.AmuletItem;
import nonamecrackers2.witherstormmod.common.item.GoldenAppleStewItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Mob.class})
public abstract class MixinMob extends LivingEntity {
   private MixinMob(EntityType<? extends LivingEntity> type, Level level) {
      super(type, level);
   }

   @Inject(
      method = {"checkAndHandleImportantInteractions"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void checkAndHandleImportantInteractionsHead(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> ci) {
      ItemStack stack = player.getItemInHand(hand);
      if (stack.getItem() instanceof AmuletItem || stack.getItem() instanceof GoldenAppleStewItem) {
         InteractionResult result = stack.interactLivingEntity(player, this, hand);
         if (result.consumesAction()) {
            ci.setReturnValue(result);
         }
      }
   }

   @Inject(
      method = {"isSunBurnTick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void isSunBurnTickHead(CallbackInfoReturnable<Boolean> ci) {
      if (!this.level().isClientSide) {
         double distance = 200.0;

         for (WitherStormEntity storm : this.level()
            .getEntitiesOfClass(WitherStormEntity.class, this.getBoundingBox().inflate(distance, this.level().getHeight(), distance))) {
            if (storm.position().subtract(this.position()).horizontalDistance() <= distance && !storm.isDeadOrPlayingDead() && storm.getPhase() > 5) {
               ci.setReturnValue(false);
            }
         }
      }
   }
}
