package nonamecrackers2.witherstormmod.common.event;


import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: ItemAttributeModifierEvent removed, use DataComponents.ATTRIBUTE_MODIFIERS
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent.ImpactResult;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.capability.WitherStormAutoSpawner;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.crackerslib.common.packet.SimpleChannel;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.item.EyeOfTheStormItem;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;
import nonamecrackers2.witherstormmod.common.world.tainting.WorldTainting;

public class WitherStormModUtilEvents {
   @SubscribeEvent
   public static void onProjectileImpact(ProjectileImpactEvent event) {
      if (event.getProjectile() instanceof FishingHook) {
         if (event.getRayTraceResult() instanceof EntityHitResult hit
            && (hit.getEntity() instanceof CommandBlockEntity || hit.getEntity() instanceof WitherStormEntity)) {
            event.setImpactResult(ImpactResult.SKIP_ENTITY);
         }
      } else if (!event.getProjectile().level().isClientSide()
         && event.getProjectile() instanceof ThrownPotion projectile
         && event.getRayTraceResult() instanceof BlockHitResult hit) {
         Potion potion = PotionContents.getPotion(projectile.getItem());
         BoundingBox box = new BoundingBox(hit.getBlockPos()).inflatedBy(1);
         WorldTainting.getInstance().convertBlocks(box, projectile.level(), potion);
      }
   }

   @SubscribeEvent
   public static void modifyItemAttributes(ItemAttributeModifierEvent event) {
      if (event.getSlotType() == EquipmentSlot.MAINHAND) {
         ItemStack stack = event.getItemStack();
         if (stack.is((Item)WitherStormModItems.EYE_OF_THE_STORM.get()) && stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            float ratio = tag.getFloat("EntityHealthRatio");
            if (ratio > 0.0F) {
               event.addModifier(
                  Attributes.ATTACK_DAMAGE,
                  new AttributeModifier(EyeOfTheStormItem.DAMAGE_MODIFIER_ID, "Health damage modifier", (double)(-ratio * 5.0F), Operation.ADD_VALUE)
               );
            }
         }
      }
   }

   @SubscribeEvent
   public static void onLevelTick(LevelTickEvent event) {
      event.getLevel().getData(WitherStormModCapabilities.WITHER_STORM_AUTO_SPAWNER.get()).tick();
   }

   @SubscribeEvent
   public static void onPlayerAttack(AttackEntityEvent event) {
      Player player = event.getEntity();
      if (!player.level().isClientSide()) {
         ItemStack stack = player.getMainHandItem();
         if (stack.is((Item)WitherStormModItems.FORMIDI_BLADE.get()) && !player.getCooldowns().isOnCooldown(stack.getItem())) {
            Entity hit = event.getTarget();
            CompoundTag tag = stack.getOrCreateTag();
            float power = Math.min(1.0F, tag.getFloat("Power"));
            if (power > 0.0F) {
               tag.putBoolean("IsCharged", false);
               tag.putFloat("Power", 2.0F);
               player.level()
                  .playSound(
                     null,
                     player.getX(),
                     player.getY(),
                     player.getZ(),
                     WitherStormModSoundEvents.FORMIDI_BLADE_DECHARGE.get(),
                     SoundSource.PLAYERS,
                     1.0F,
                     1.0F
                  );
               player.getCooldowns().addCooldown(stack.getItem(), 100);
               player.level().explode(player, hit.getX(), hit.getY(), hit.getZ(), 4.0F * power, ExplosionInteraction.NONE);

               for (ServerPlayer nearby : player.level().getEntitiesOfClass(ServerPlayer.class, player.getBoundingBox().inflate(32.0))) {
                  WitherStormModPacketHandlers.MAIN.send(SimpleChannel.toPlayer(nearby), new ShakeScreenMessage(40.0F, 2.5F));
               }
            }
         }
      }
   }
}
