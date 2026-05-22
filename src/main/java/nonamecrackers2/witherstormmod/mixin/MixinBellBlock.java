package nonamecrackers2.witherstormmod.mixin;

import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCriteriaTriggers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BellBlock.class})
public class MixinBellBlock {
   @Inject(
      method = {"onHit"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/resources/ResourceLocation;)V"
      )}
   )
   public void onHitInvoke(Level level, BlockState state, BlockHitResult hitResult, @Nullable Player player, boolean flag, CallbackInfoReturnable<Boolean> ci) {
      if (!level.isClientSide) {
         AABB box = player.getBoundingBox().inflate(300.0);
         List<WitherStormEntity> storms = level.getEntitiesOfClass(WitherStormEntity.class, box);
         if (!storms.isEmpty()) {
            storms.sort(Comparator.comparingDouble(player::distanceToSqr));
            WitherStormEntity storm = storms.get(0);
            WitherStormModCriteriaTriggers.RING_BELL_NEAR_STORM.trigger((ServerPlayer)player, storm);
         }
      }
   }
}
