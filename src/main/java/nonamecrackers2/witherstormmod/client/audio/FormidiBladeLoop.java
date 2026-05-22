package nonamecrackers2.witherstormmod.client.audio;

import java.util.Optional;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.item.FormidiBladeItem;

public class FormidiBladeLoop extends FadingSoundLoop implements IForceStoppableSound {
   public static final float MIN_POWER = 0.05F;
   public final AbstractClientPlayer player;

   public FormidiBladeLoop(AbstractClientPlayer player, SoundEvent event) {
      super(event, SoundSource.AMBIENT);
      this.player = player;
   }

   @Override
   public void tick() {
      super.tick();
      Vec3 view = this.player.getViewVector(1.0F).scale(0.2);
      this.x = this.player.getX() + view.x;
      this.y = this.player.getEyeY() + view.y;
      this.z = this.player.getZ() + view.z;
      boolean flag = false;
      Optional<Float> power = getPower(this.player);
      if (canPlay(power)) {
         flag = true;
         float powerf = power.get();
         this.dampen = (1.0F - powerf) * 50.0F;
         this.pitch = 0.5F + powerf / 2.0F;
      }

      if (!this.player.isAlive() || !flag) {
         this.stopSound();
      }
   }

   public static Optional<Float> getPower(AbstractClientPlayer player) {
      for (InteractionHand hand : InteractionHand.values()) {
         ItemStack stack = player.getItemInHand(hand);
         if (stack.getItem() instanceof FormidiBladeItem) {
            float power = FormidiBladeItem.getPower(player, stack, false);
            return Optional.of(power);
         }
      }

      return Optional.empty();
   }

   public static boolean canPlay(Optional<Float> power) {
      return power.<Boolean>map(p -> p > 0.05F).orElse(false);
   }

   public static boolean canPlay(AbstractClientPlayer player) {
      return canPlay(getPower(player));
   }

   @Override
   public void forceStop() {
      this.stop();
   }

   @Override
   protected int getFadeTime() {
      return 20;
   }
}
