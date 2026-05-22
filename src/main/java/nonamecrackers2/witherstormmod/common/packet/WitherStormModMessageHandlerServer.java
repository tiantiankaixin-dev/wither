package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import nonamecrackers2.witherstormmod.common.blockentity.inventory.AbstractSuperBeaconMenu;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.WitherStormHead;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WitherStormModMessageHandlerServer {
   public static final Logger LOGGER = LogManager.getLogger();

   public static void processInjureHeadMessage(InjureHeadMessage message, ServerPlayer player) {
      ServerLevel world = player.serverLevel();
      double pickRange = player.getAttribute((Attribute)ForgeMod.BLOCK_REACH.get()).getValue();
      Vec3 pos = player.getEyePosition(1.0F);
      Vec3 eye = player.getViewVector(1.0F);
      Vec3 reach = pos.add(eye.x * pickRange, eye.y * pickRange, eye.z * pickRange);
      Entity entity = world.getEntity(message.getEntityID());
      int headIndex = message.getHead();
      if (entity instanceof WitherStormEntity storm) {
         WitherStormHead head = storm.getHeadManager().getHead(headIndex);
         if (storm.tractorBeamActive(headIndex) && WorldUtil.checkForIntersect(head.getBoundingBox(), pos, reach)) {
            if (head.getHeadInjureAttemptCooldown() <= 0 && head.getHeadInjuryTicks() <= 0) {
               head.setHeadInjureAttemptCooldown(20);
               if (head.checkAndCountAttack()) {
                  head.hurt(player, storm.getHeadManager().getHeadInjuryTime());
               }

               player.playNotifySound(SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0F, 1.0F);
            } else {
               player.playNotifySound(SoundEvents.PLAYER_ATTACK_WEAK, player.getSoundSource(), 1.0F, 1.0F);
            }
         }
      } else {
         LOGGER.warn("Received entity " + entity + " that is not an instance of WitherStormEntity");
      }
   }

   public static void processSuperBeaconSetEffectMessage(SuperBeaconSetEffectMessage message, ServerPlayer player) {
      if (player.containerMenu instanceof AbstractSuperBeaconMenu menu) {
         MobEffect effect = MobEffect.byId(message.getEffectId());
         if (menu.getCooldown() == 0 || effect == null) {
            if (effect != null && effect != menu.getPrimaryEffect()) {
               menu.doPowerUp(player);
               menu.activateCooldown(200);
            }

            menu.updateEffects(message.getEffectId());
         }
      }
   }

   public static void processSuperBeaconToggleAreaMessage(SuperBeaconToggleAreaMessage message, ServerPlayer player) {
      if (player.containerMenu instanceof AbstractSuperBeaconMenu menu) {
         menu.setShowArea(message.shouldShowArea());
      }
   }
}
