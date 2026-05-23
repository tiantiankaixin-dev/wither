package nonamecrackers2.witherstormmod.common.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;

public class FormidiBladeItem extends CommandBlockSwordItem {
   public static final int DEFAULT_RELEASE_TIME = 40;
   public static final String POWER = "Power";
   public static final String IS_CHARGED = "IsCharged";
   private static final float POWER_DECREASE_PER_TICK = 0.2F;

   public FormidiBladeItem(Tier tier, int damage, float attackSpeed, Properties properties) {
      super(tier, damage, attackSpeed, properties);
   }

   public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
      CompoundTag tag = stack.getTag();
      if (tag != null && tag.contains("IsCharged", 1) && !tag.getBoolean("IsCharged")) {
         float power = tag.getFloat("Power");
         if (power > 0.2F) {
            power -= 0.2F;
            tag.putFloat("Power", power);
         } else {
            tag.remove("Power");
         }
      }
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!player.getCooldowns().isOnCooldown(this)) {
         CompoundTag tag = stack.getTag();
         if (tag == null || !tag.contains("Power", 10) || tag.getFloat("Power") < 1.0F) {
            player.startUsingItem(hand);
            return InteractionResultHolder.success(stack);
         }
      }

      return InteractionResultHolder.fail(stack);
   }

   public int getUseDuration(ItemStack stack) {
      return 72000;
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.NONE;
   }

   public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
      float power = (float)entity.getTicksUsingItem() / 40.0F;
      CompoundTag tag = stack.getOrCreateTag();
      tag.putFloat("Power", Math.min(1.0F, power));
      tag.putBoolean("IsCharged", true);
   }

   public void appendHoverText(ItemStack stack, Level level, List<Component> text, TooltipFlag flag) {
      text.add(Component.translatable("item.witherstormmod.formidi_blade.author").withStyle(ChatFormatting.DARK_GRAY));
      text.add(Component.translatable("item.witherstormmod.formidi_blade.use").withStyle(ChatFormatting.DARK_GRAY));
   }

   public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
      return slotChanged ? super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) : false;
   }

   public static float getPower(@Nullable LivingEntity entity, ItemStack stack, boolean useItemTime) {
      CompoundTag tag = stack.getTag();
      float chargingPower;
      if (useItemTime && entity != null) {
         chargingPower = Math.min((float)entity.getTicksUsingItem() / 40.0F, 1.0F);
      } else {
         chargingPower = 0.0F;
      }

      return tag != null && tag.contains("Power", 5) ? Math.max(tag.getFloat("Power"), chargingPower) : chargingPower;
   }

   public static void registerItemProperty() {
      ItemProperties.register(
         (Item)WitherStormModItems.FORMIDI_BLADE.get(),
         ResourceLocation.fromNamespaceAndPath("witherstormmod", "anim"),
         (stack, world, entity, i) -> entity == null
               ? 0.0F
               : getPower(entity, stack, entity.getItemInHand(InteractionHand.OFF_HAND) == stack || entity.getItemInHand(InteractionHand.MAIN_HAND) == stack)
      );
   }
}
