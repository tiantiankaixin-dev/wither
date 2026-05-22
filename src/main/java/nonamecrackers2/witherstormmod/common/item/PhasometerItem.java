package nonamecrackers2.witherstormmod.common.item;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.ultimatetarget.UltimateTargetManager;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class PhasometerItem extends SpyglassItem {
   public static final String UPGRADED = "IsUpgraded";

   public PhasometerItem(Properties properties) {
      super(properties);
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      clearDataTags(player.getItemInHand(hand).getOrCreateTag());
      return super.use(level, player, hand);
   }

   public void onUseTick(Level level, LivingEntity entity, ItemStack item, int count) {
      if (!level.isClientSide) {
         Vec3 pos = entity.getEyePosition();
         Vec3 view = entity.getViewVector(1.0F);
         Vec3 end = pos.add(view.scale(10000.0));
         EntityHitResult result = ProjectileUtil.getEntityHitResult(entity.level(), entity, pos, end, new AABB(pos, end).inflate(1.0), e -> !e.isSpectator(), 0.0F);
         CompoundTag tag = item.getOrCreateTag();
         if (result != null
            && result.getType() == Type.ENTITY
            && result.getEntity() instanceof WitherStormEntity storm
            && storm.getType() == WitherStormModEntityTypes.WITHER_STORM.get()) {
            BlockHitResult obstruction = WorldUtil.raycast(entity, storm, 150.0);
            if (obstruction.getType() == Type.MISS) {
               for (PhasometerItem.DataEntry entry : PhasometerItem.DataEntry.values()) {
                  if (!entry.requiresUpgraded || this.isUpgraded(item)) {
                     entry.apply(tag, storm);
                  }
               }

               tag.putBoolean(PhasometerItem.DataEntry.OBSTRUCTED.tagName, false);
               return;
            }

            BlockState state = level.getBlockState(obstruction.getBlockPos());
            if (!state.isCollisionShapeFullBlock(level, obstruction.getBlockPos())) {
               clearDataTags(tag, PhasometerItem.DataEntry.OBSTRUCTED);
               tag.putBoolean(PhasometerItem.DataEntry.OBSTRUCTED.tagName, true);
               return;
            }
         }

         clearDataTags(tag);
      }
   }

   private static void clearDataTags(CompoundTag tag, PhasometerItem.DataEntry... excluding) {
      label24:
      for (PhasometerItem.DataEntry entry : PhasometerItem.DataEntry.values()) {
         for (PhasometerItem.DataEntry toExclude : excluding) {
            if (entry == toExclude) {
               continue label24;
            }
         }

         tag.remove(entry.tagName);
      }
   }

   public static List<PhasometerItem.DataEntry> getEntries(CompoundTag tag) {
      List<PhasometerItem.DataEntry> entries = Lists.newArrayList();

      for (PhasometerItem.DataEntry entry : PhasometerItem.DataEntry.values()) {
         if (entry.hasData(tag) && entry.isInformational) {
            entries.add(entry);
         }
      }

      return entries;
   }

   public ItemStack finishUsingItem(ItemStack item, Level level, LivingEntity entity) {
      clearDataTags(item.getOrCreateTag());
      return super.finishUsingItem(item, level, entity);
   }

   public void appendHoverText(ItemStack stack, Level level, List<Component> text, TooltipFlag flag) {
      CompoundTag tag = stack.getOrCreateTag();
      text.add(Component.translatable("description.phasometer.use").withStyle(ChatFormatting.DARK_GRAY));
      if (tag.getBoolean("IsUpgraded")) {
         text.add(Component.translatable("description.phasometer.use.upgraded").withStyle(ChatFormatting.GOLD));
      }
   }

   public boolean isUpgraded(ItemStack stack) {
      return stack.getOrCreateTag().getBoolean("IsUpgraded");
   }

   public static enum DataEntry {
      OBSTRUCTED("IsObstructed", false, false) {
         @Override
         public Component getDisplayText(CompoundTag tag) {
            return Component.translatable("description.phasometer.obstructed").withStyle(ChatFormatting.RED);
         }

         @Override
         public boolean hasData(CompoundTag tag) {
            return tag.contains(this.tagName);
         }
      },
      PHASE("LookingAtPhase", false) {
         @Override
         protected void apply(CompoundTag tag, WitherStormEntity storm) {
            tag.putInt(this.tagName, storm.getPhase());
         }

         @Override
         public Component getDisplayText(CompoundTag tag) {
            return Component.translatable("description.phasometer.phase", new Object[]{tag.getInt(this.tagName)}).withStyle(ChatFormatting.GREEN);
         }

         @Override
         public boolean hasData(CompoundTag tag) {
            return tag.contains(this.tagName);
         }
      },
      FORMIDIBOMBABLE("IsFormidibombable", false) {
         @Override
         protected void apply(CompoundTag tag, WitherStormEntity storm) {
            tag.putBoolean(this.tagName, storm.getPhase() == 5 && storm.getConsumedEntities() >= storm.getConsumptionAmountForPhase(5));
         }

         @Override
         public Component getDisplayText(CompoundTag tag) {
            return Component.translatable("description.phasometer.formidibombable").withStyle(ChatFormatting.GOLD);
         }

         @Override
         public boolean hasData(CompoundTag tag) {
            return tag.getBoolean(this.tagName);
         }
      },
      BOWELS_ACCESSIBLE("BowelsAccessible", false) {
         @Override
         protected void apply(CompoundTag tag, WitherStormEntity storm) {
            tag.putBoolean(this.tagName, storm.isBeingTornApart());
         }

         @Override
         public Component getDisplayText(CompoundTag tag) {
            return Component.translatable("description.phasometer.bowelsAccessible").withStyle(ChatFormatting.GOLD);
         }

         @Override
         public boolean hasData(CompoundTag tag) {
            return tag.getBoolean(this.tagName);
         }
      },
      DISTRACTED("IsDistracted", false) {
         @Override
         protected void apply(CompoundTag tag, WitherStormEntity storm) {
            UltimateTargetManager manager = storm.getUltimateTargetManager().orElse(null);
            if (manager != null) {
               tag.putBoolean(this.tagName, manager.isDistracted());
            } else {
               tag.putBoolean(this.tagName, false);
            }
         }

         @Override
         public Component getDisplayText(CompoundTag tag) {
            return Component.translatable("description.phasometer.distracted").withStyle(ChatFormatting.GOLD);
         }

         @Override
         public boolean hasData(CompoundTag tag) {
            return tag.getBoolean(this.tagName);
         }
      },
      CHASING("IsChasing", false) {
         @Override
         protected void apply(CompoundTag tag, WitherStormEntity storm) {
            UltimateTargetManager manager = storm.getUltimateTargetManager().orElse(null);
            if (manager != null) {
               tag.putBoolean(this.tagName, manager.isTargetStationary());
            } else {
               tag.putBoolean(this.tagName, false);
            }
         }

         @Override
         public Component getDisplayText(CompoundTag tag) {
            return Component.translatable("description.phasometer.chasing").withStyle(ChatFormatting.GOLD);
         }

         @Override
         public boolean hasData(CompoundTag tag) {
            return tag.getBoolean(this.tagName);
         }
      },
      ULTIMATE_TARGET("UltimateTarget", true) {
         @Override
         protected void apply(CompoundTag tag, WitherStormEntity storm) {
            LivingEntity target = storm.getUltimateTarget();
            if (target != null) {
               tag.putString(this.tagName, target.getDisplayName().getString());
            } else {
               tag.remove(this.tagName);
            }
         }

         @Override
         public Component getDisplayText(CompoundTag tag) {
            return Component.translatable("description.phasometer.ultimateTarget", new Object[]{tag.getString(this.tagName)})
               .withStyle(ChatFormatting.LIGHT_PURPLE);
         }

         @Override
         public boolean hasData(CompoundTag tag) {
            return tag.contains(this.tagName);
         }
      },
      ULTIMATE_TARGET_DIRECTION("UltimateTargetDirection", true) {
         @Override
         protected void apply(CompoundTag tag, WitherStormEntity storm) {
            Vec3 pos = storm.getUltimateTargetPos();
            if (pos != null) {
               Vec3 normal = new Vec3(pos.x, storm.getY(), pos.z).subtract(storm.position()).normalize();
               tag.putString(this.tagName, Direction.getNearest(normal.x, normal.y, normal.z).getName());
            } else {
               tag.remove(this.tagName);
            }
         }

         @Override
         public Component getDisplayText(CompoundTag tag) {
            return Component.translatable("description.phasometer.ultimateTargetDirection", new Object[]{tag.getString(this.tagName)})
               .withStyle(ChatFormatting.LIGHT_PURPLE);
         }

         @Override
         public boolean hasData(CompoundTag tag) {
            return tag.contains(this.tagName);
         }
      },
      PHASE_PROGRESS("PhaseProgress", true) {
         @Override
         protected void apply(CompoundTag tag, WitherStormEntity storm) {
            if (storm.canEvolve(true)) {
               int progress = Math.round(storm.getPhaseProgress() * 100.0F);
               tag.putInt(this.tagName, progress);
            } else {
               tag.putInt(this.tagName, 100);
            }
         }

         @Override
         public Component getDisplayText(CompoundTag tag) {
            return Component.translatable("description.phasometer.phaseProgress", new Object[]{tag.getInt(this.tagName) + "%"})
               .withStyle(ChatFormatting.LIGHT_PURPLE);
         }

         @Override
         public boolean hasData(CompoundTag tag) {
            return tag.contains(this.tagName);
         }
      };

      public final String tagName;
      public final boolean requiresUpgraded;
      public final boolean isInformational;

      private DataEntry(String tagName, boolean requiresUpgraded, boolean isInformational) {
         this.tagName = tagName;
         this.requiresUpgraded = requiresUpgraded;
         this.isInformational = isInformational;
      }

      private DataEntry(String tagName, boolean requiresUpgraded) {
         this(tagName, requiresUpgraded, true);
      }

      protected void apply(CompoundTag tag, WitherStormEntity storm) {
      }

      public abstract Component getDisplayText(CompoundTag var1);

      public abstract boolean hasData(CompoundTag var1);
   }
}
