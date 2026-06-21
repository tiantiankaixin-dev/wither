package nonamecrackers2.witherstormmod.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent.ImpactResult;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.common.capability.WitherStormAutoSpawner;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;
import nonamecrackers2.witherstormmod.common.util.ItemStackDataUtil;
import nonamecrackers2.witherstormmod.common.util.PotionStackUtil;
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
         Potion potion = PotionStackUtil.getPotion(projectile.getItem());
         if (potion != null) {
            BoundingBox box = new BoundingBox(hit.getBlockPos()).inflatedBy(1);
            WorldTainting.getInstance().convertBlocks(box, projectile.level(), potion);
         }
      }
   }

   @SubscribeEvent
   public static void onLevelTick(LevelTickEvent event) {
      if (event.phase == Phase.END) {
         event.level.getCapability(WitherStormModCapabilities.WITHER_STORM_AUTO_SPAWNER).ifPresent(WitherStormAutoSpawner::tick);
      }
   }

   @SubscribeEvent
   public static void onPlayerAttack(AttackEntityEvent event) {
      Player player = event.getEntity();
      if (!player.level().isClientSide()) {
         ItemStack stack = player.getMainHandItem();
         if (stack.is((Item)WitherStormModItems.FORMIDI_BLADE.get()) && !player.getCooldowns().isOnCooldown(stack.getItem())) {
            Entity hit = event.getTarget();
            var tag = ItemStackDataUtil.getOrCreateTag(stack);
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
                  WitherStormModPacketHandlers.MAIN.send(PacketDistributor.PLAYER.with(nearby), new ShakeScreenMessage(40.0F, 2.5F));
               }
            }
         }
      }
   }
}
