package nonamecrackers2.witherstormmod.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SourceTestFireballItem extends Item {
   public SourceTestFireballItem(Properties properties) {
      super(properties);
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
      if (!level.isClientSide) {
         Vec3 view = player.getViewVector(1.0F);
         LargeFireball fireball = new LargeFireball(level, player, view, 1);
         fireball.setPos(player.getX() + view.x * 2.0, player.getEyeY() - 0.1 + view.y * 2.0, player.getZ() + view.z * 2.0);
         level.addFreshEntity(fireball);
      }

      player.awardStat(Stats.ITEM_USED.get(this));
      player.getCooldowns().addCooldown(this, 10);
      return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
   }
}
