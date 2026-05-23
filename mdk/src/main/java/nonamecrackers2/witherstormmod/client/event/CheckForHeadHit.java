package nonamecrackers2.witherstormmod.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.WitherStormHead;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.InjureHeadMessage;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class CheckForHeadHit {
   @SubscribeEvent
   public static void blockLeftHit(LeftClickEmpty event) {
      checkForHeadHit(event.getLevel(), event.getEntity(), event.getHand());
   }

   public static void playerAttack(AttackEntityEvent event) {
      checkForHeadHit(event.getEntity().level(), event.getEntity(), InteractionHand.MAIN_HAND);
   }

   private static void checkForHeadHit(Level level, Player player, InteractionHand hand) {
      if (level.isClientSide) {
         ClientLevel clientWorld = (ClientLevel)level;
         LocalPlayer localPlayer = (LocalPlayer)player;
         Minecraft mc = Minecraft.getInstance();
         if (mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            for (WitherStormEntity storm : clientWorld.getEntitiesOfClass(WitherStormEntity.class, localPlayer.getBoundingBox().inflate(50.0))) {
               for (WitherStormHead head : storm.getHeadManager().getHeads()) {
                  if (storm.tractorBeamActive(head.getIndex())) {
                     Vec3 pos = localPlayer.getEyePosition(1.0F);
                     Vec3 eye = localPlayer.getViewVector(1.0F);
                     float pickRange = mc.gameMode.getPickRange();
                     Vec3 reach = pos.add(eye.x * (double)pickRange, eye.y * (double)pickRange, eye.z * (double)pickRange);
                     if (WorldUtil.checkForIntersect(head.getBoundingBox(), pos, reach)) {
                        if (head.getHeadInjureAttemptCooldown() <= 0 && head.getHeadInjuryTicks() <= 0 && !storm.isDeadOrPlayingDead()) {
                           InjureHeadMessage message = new InjureHeadMessage(storm, head.getIndex(), hand);
                           WitherStormModPacketHandlers.MAIN.sendToServer(message);
                        } else {
                           localPlayer.playNotifySound(SoundEvents.PLAYER_ATTACK_WEAK, localPlayer.getSoundSource(), 1.0F, 1.0F);
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
