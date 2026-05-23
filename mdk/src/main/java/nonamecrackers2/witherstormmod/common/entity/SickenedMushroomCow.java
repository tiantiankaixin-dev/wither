package nonamecrackers2.witherstormmod.common.entity;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.IShearable;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SickenedMushroomCow extends SickenedCow implements IForgeShearable {
   public SickenedMushroomCow(EntityType<? extends SickenedMushroomCow> type, Level level) {
      super(type, level);
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.MAX_HEALTH, 26.0);
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (stack.is(Items.BOWL) && !this.isBaby()) {
         ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW);
         SuspiciousStewItem.saveMobEffect(stew, MobEffects.WITHER, 160 + this.random.nextInt(60));
         ItemStack filled = ItemUtils.createFilledResult(stack, player, stew, false);
         player.setItemInHand(hand, filled);
         this.playSound(SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY, 1.0F, 1.0F);
         return InteractionResult.sidedSuccess(this.level().isClientSide);
      } else {
         return super.mobInteract(player, hand);
      }
   }

   @NotNull
   public List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level level, BlockPos pos, int fortune) {
      this.level().playSound((Player)null, this, SoundEvents.MOOSHROOM_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
      if (!this.level().isClientSide) {
         SickenedCow cow = (SickenedCow)(WitherStormModEntityTypes.SICKENED_COW.get()).create(this.level());
         if (cow != null) {
            ((ServerLevel)this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            this.discard();
            cow.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
            cow.setHealth(this.getHealth());
            cow.yBodyRot = this.yBodyRot;
            if (this.hasCustomName()) {
               cow.setCustomName(this.getCustomName());
               cow.setCustomNameVisible(this.isCustomNameVisible());
            }

            if (this.isPersistenceRequired()) {
               cow.setPersistenceRequired();
            }

            cow.setInvulnerable(this.isInvulnerable());
            this.level().addFreshEntity(cow);
            List<ItemStack> items = Lists.newArrayList();

            for (int i = 0; i < 3; i++) {
               ItemStack drop = new ItemStack((ItemLike)WitherStormModItems.TAINTED_MUSHROOM.get());
               items.add(drop);
            }

            return items;
         }
      }

      return Collections.emptyList();
   }
}
