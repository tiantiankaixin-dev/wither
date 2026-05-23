package nonamecrackers2.witherstormmod.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.ClientTickEvent;
import // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.Phase;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.client.audio.TractorBeamLoop;
import nonamecrackers2.witherstormmod.client.audio.WitherStormTractorBeamLoop;
import nonamecrackers2.witherstormmod.client.capability.WitherStormLoopingSoundManager;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.WitherStormHead;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;

public class PlayTractorBeamLoopEvents {
   @SubscribeEvent
   public static void clientTickEvent(ClientTickEvent event) {
      if (event.phase == Phase.START) {
         Minecraft mc = Minecraft.getInstance();
         ClientLevel clientWorld = mc.level;
         if (clientWorld != null && !mc.isPaused()) {
            for (Entity entity : clientWorld.entitiesForRendering()) {
               if (entity instanceof WitherStormEntity) {
                  WitherStormEntity storm = (WitherStormEntity)entity;
                  if (!storm.isDeadOrPlayingDead() && !storm.isSilent()) {
                     for (WitherStormHead head : storm.getHeadManager().getHeads()) {
                        LocalPlayer player = mc.player;
                        if (storm.tractorBeamActive(head.getIndex())) {
                           Vec3 pos = TractorBeamHelper.calculateClosestPoint(player.position(), storm, head.getIndex());
                           double distance = Math.sqrt(player.distanceToSqr(pos));
                           WitherStormLoopingSoundManager manager = (WitherStormLoopingSoundManager)clientWorld.getCapability(
                                 WitherStormModClientCapabilities.LOOPING_MANAGER
                              )
                              .orElse(null);
                           if (manager != null && !manager.alreadyHasLoop(storm.getId(), head.getIndex()) && distance <= TractorBeamLoop.DISTANCE_REQUIRED) {
                              WitherStormTractorBeamLoop loop = new WitherStormTractorBeamLoop(storm, head.getIndex());
                              loop.setPos(pos);
                              manager.putBeamSound(storm.getId(), head.getIndex(), loop);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
