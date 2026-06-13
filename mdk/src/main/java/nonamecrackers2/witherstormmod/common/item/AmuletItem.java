package nonamecrackers2.witherstormmod.common.item;

import net.neoforged.api.distmarker.Dist;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.BuiltInRegistries;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCriteriaTriggers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class AmuletItem extends Item {
   private static final Predicate<Entity> BINDABLE = entity -> entity instanceof Mob || entity instanceof Player;
   public static final String TRACKING_BLUE = "TrackingBlue";
   public static final String TRACKING_AQUA = "TrackingAqua";
   public static final String TRACKING_GREEN = "TrackingGreen";
   public static final String TRACKING_GRAY = "TrackingGray";
   public static final String TRACKING_RED = "TrackingRed";
   public static final String[] TRACKING = new String[]{"TrackingBlue", "TrackingAqua", "TrackingGreen", "TrackingGray", "TrackingRed"};
   public static final String SELECTED_INDEX = "SelectedIndex";
   public static final String LOCKED = "Locked";
   public static final String TRACK_ENTITY_TYPES = "TrackEntityTypes";
   public static final int DEFAULT_SCAN_DISTANCE = 1000;

   public AmuletItem(Properties properties) {
      super(properties);
   }

   public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
      if (level instanceof ServerLevel serverLevel && entity instanceof Player player) {
         CompoundTag tag = stack.getOrCreateTag();
         if (tag.getInt("SelectedIndex") <= 0) {
            tag.putInt("SelectedIndex", 1);
         }

         for (String id : TRACKING) {
            if (!id.equals("TrackingBlue")) {
               if (tag.contains(id)) {
                  this.saveDistFor(serverLevel, tag, player, tag.getUUID(id), id);
               } else {
                  tag.putInt(id + "Dist", -1);
               }
            } else {
               List<WitherStormEntity> storms = Lists.newArrayList();

               for (Entity e : serverLevel.getAllEntities()) {
                  if (e instanceof WitherStormEntity) {
                     WitherStormEntity storm = (WitherStormEntity)e;
                     if (!(e instanceof WitherStormSegmentEntity)) {
                        storms.add(storm);
                     }
                  }
               }

               WitherStormEntity nearest = WorldUtil.getNearest(storms, player.position(), Entity::position);
               if (nearest != null) {
                  tag.putString(id + "Type", BuiltInRegistries.ENTITY_TYPE.getKey(nearest.getType()).toString());
                  tag.putUUID(id, nearest.getUUID());
                  tag.putInt(id + "Dist", (int)player.distanceTo(nearest));
                  tag.putString(id + "Name", nearest.getDisplayName().getString());
                  tag.put(id + "Pos", NbtUtils.writeBlockPos(nearest.blockPosition()));
               } else {
                  tag.putInt(id + "Dist", -1);
               }
            }
         }
      }
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!level.isClientSide && player.isShiftKeyDown() && stack.getItem() instanceof AmuletItem) {
         CompoundTag tag = stack.getOrCreateTag();
         int index = tag.getInt("SelectedIndex");
         if (++index < TRACKING.length) {
            tag.putInt("SelectedIndex", index);
         } else {
            tag.putInt("SelectedIndex", 1);
         }

         player.playNotifySound(WitherStormModSoundEvents.AMULET_SWAPS.get(), SoundSource.PLAYERS, 1.0F, 2.0F);
      }

      return InteractionResultHolder.pass(stack);
   }

   public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
      if (BINDABLE.test(entity) && !(entity instanceof WitherStormEntity) && !player.isShiftKeyDown()) {
         ItemStack item = player.getItemInHand(hand);
         if (item.getItem() instanceof AmuletItem) {
            CompoundTag tag = item.getOrCreateTag();
            if (!tag.getBoolean("Locked")) {
               int index = tag.getInt("SelectedIndex");
               if (index >= 1 && index < TRACKING.length) {
                  if (!player.level().isClientSide) {
                     String id = TRACKING[index];
                     if (tag.contains(id) && tag.getUUID(id).equals(entity.getUUID())) {
                        tag.remove(id);
                        tag.remove(id + "Type");
                        tag.remove(id + "Name");
                        tag.remove(id + "Pos");
                        tag.putInt(id + "Dist", -1);
                        player.playNotifySound(WitherStormModSoundEvents.AMULET_UNBIND.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                     } else {
                        tag.putString(id + "Type", BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
                        tag.putUUID(id, entity.getUUID());
                        player.playNotifySound(WitherStormModSoundEvents.AMULET_BIND.get(), SoundSource.PLAYERS, 1.0F, 0.0F);
                        if (player instanceof ServerPlayer serverPlayer) {
                           WitherStormModCriteriaTriggers.LINK_AMULET.trigger(serverPlayer, entity, this.getTotalUniqueLinked(stack));
                        }
                     }
                  }

                  return InteractionResult.sidedSuccess(player.level().isClientSide);
               }
            }
         }
      }

      return InteractionResult.PASS;
   }

   private void saveDistFor(ServerLevel level, CompoundTag tag, Player player, UUID uuid, String id) {
      Entity tracking = null;
      if (tag.getBoolean("TrackEntityTypes")) {
         EntityType<?> type = (EntityType<?>)BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(tag.getString(id + "Type")));
         List<Entity> entities = level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(500.0), e -> e.getType().equals(type) && e != player);
         tracking = WorldUtil.getNearest(entities, player.position(), Entity::position);
      } else if (tag.contains(id)) {
         tracking = level.getEntity(tag.getUUID(id));
      }

      if (tracking != null) {
         tag.putInt(id + "Dist", (int)player.distanceTo(tracking));
         tag.putString(id + "Name", tracking.getDisplayName().getString());
         tag.put(id + "Pos", NbtUtils.writeBlockPos(tracking.blockPosition()));
      } else {
         tag.putInt(id + "Dist", -1);
      }
   }

   public void appendHoverText(ItemStack stack, Level level, List<Component> text, TooltipFlag flag) {
      super.appendHoverText(stack, level, text, flag);
      CompoundTag tag = stack.getOrCreateTag();
      boolean locked = tag.getBoolean("Locked");
      if ((Boolean)WitherStormModConfig.SERVER.amuletOverride.get()) {
         text.add(Component.translatable("description.amulet.mainUse").withStyle(ChatFormatting.DARK_GRAY));
      }

      text.add(Component.translatable("description.amulet.trackingDesc").withStyle(ChatFormatting.DARK_GRAY));
      text.add(Component.translatable("description.amulet.swap").withStyle(ChatFormatting.DARK_GRAY));
      if (!locked) {
         text.add(Component.translatable("description.amulet.bind").withStyle(ChatFormatting.DARK_GRAY));
      }

      text.add(
         Component.translatable("description.amulet.tracking", new Object[]{getTrackingName("TrackingBlue", tag), getDistString("TrackingBlue", tag)})
            .withStyle(ChatFormatting.BLUE)
      );
      text.add(
         Component.translatable("description.amulet.tracking", new Object[]{getTrackingName("TrackingAqua", tag), getDistString("TrackingAqua", tag)})
            .withStyle(ChatFormatting.AQUA)
      );
      text.add(
         Component.translatable("description.amulet.tracking", new Object[]{getTrackingName("TrackingGreen", tag), getDistString("TrackingGreen", tag)})
            .withStyle(ChatFormatting.GREEN)
      );
      text.add(
         Component.translatable("description.amulet.tracking", new Object[]{getTrackingName("TrackingGray", tag), getDistString("TrackingGray", tag)})
            .withStyle(ChatFormatting.GRAY)
      );
      text.add(
         Component.translatable("description.amulet.tracking", new Object[]{getTrackingName("TrackingRed", tag), getDistString("TrackingRed", tag)})
            .withStyle(ChatFormatting.RED)
      );
      if (locked) {
         text.add(Component.translatable("description.amulet.locked").withStyle(ChatFormatting.YELLOW));
      }

      if (tag.getBoolean("TrackEntityTypes")) {
         text.add(Component.translatable("description.amulet.tracksEntityTypes").withStyle(ChatFormatting.GOLD));
      }
   }

   private static String getDistString(String id, CompoundTag tag) {
      int dist = tag.getInt(id + "Dist");
      if (dist >= 0) {
         return String.valueOf(dist);
      } else {
         return tag.contains(id + "Name") ? "Could not find nearby" : "";
      }
   }

   private static String getTrackingName(String id, CompoundTag tag) {
      if (!tag.contains(id + "Name")) {
         return id == "TrackingBlue" ? "No Nearby Wither Storm" : "Empty";
      } else {
         return tag.getString(id + "Name");
      }
   }

   public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
      return slotChanged ? super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) : false;
   }

   public int getTotalUniqueLinked(ItemStack stack) {
      CompoundTag tag = stack.getOrCreateTag();
      List<UUID> ids = Lists.newArrayList();

      for (int i = 0; i < TRACKING.length; i++) {
         String id = TRACKING[i];
         if (!id.equals("TrackingBlue") && tag.contains(id)) {
            UUID uuid = tag.getUUID(id);
            if (!ids.contains(uuid)) {
               ids.add(uuid);
            }
         }
      }

      return ids.size();
   }

   public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
      return !newStack.is(oldStack.getItem());
   }
}
