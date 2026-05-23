package nonamecrackers2.witherstormmod.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;

public class GoldenAppleStewItem extends BowlFoodItem {
   public GoldenAppleStewItem(Properties properties) {
      super(properties);
   }

   public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
      boolean[] flag = new boolean[]{false};
      entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(tracker -> {
         if (tracker.isInfected() && !tracker.isBeingCured() && !tracker.isActuallyImmune()) {
            tracker.beginCure();
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.NEUTRAL, 1.0F, 1.0F);
            flag[0] = true;
         }
      });
      if (flag[0]) {
         if (!player.getAbilities().instabuild) {
            stack.shrink(1);
         }

         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.PASS;
      }
   }

   public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
      entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER).ifPresent(tracker -> {
         if (tracker.isInfected() && !tracker.isBeingCured() && !tracker.isActuallyImmune()) {
            tracker.beginCure();
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.NEUTRAL, 1.0F, 1.0F);
         }
      });
      return super.finishUsingItem(stack, world, entity);
   }
}
