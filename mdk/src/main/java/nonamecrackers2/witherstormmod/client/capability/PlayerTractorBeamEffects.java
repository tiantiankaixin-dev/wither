package nonamecrackers2.witherstormmod.client.capability;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.Phase
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.PlayerTickEvent
import nonamecrackers2.witherstormmod.api.common.entity.WitherStormBase;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;

public class PlayerTractorBeamEffects {
   private final LocalPlayer player;
   private int ticksInTractorBeam;

   public PlayerTractorBeamEffects(LocalPlayer player) {
      this.player = player;
   }

   public PlayerTractorBeamEffects() {
      this.player = null;
   }

   public <T extends LivingEntity & WitherStormBase> void tick() {
      boolean flag = false;
      if (EntitySelector.NO_SPECTATORS.test(this.player)) {
         for (Entity entity : this.player.clientLevel.entitiesForRendering()) {
            if (entity instanceof LivingEntity) {
               LivingEntity living = (LivingEntity)entity;
               if (living instanceof WitherStormBase storm) {
                  T t = (T)storm;
                  if ((Boolean)TractorBeamHelper.isInsideTractorBeam(this.player, t, 4.0).getFirst()) {
                     flag = true;
                     break;
                  }
               }
            }
         }
      }

      if (flag) {
         if (this.ticksInTractorBeam < 240) {
            this.ticksInTractorBeam++;
         }
      } else if (this.ticksInTractorBeam > 0) {
         this.ticksInTractorBeam--;
      }
   }

   public int getTicksInTractorBeam() {
      return this.ticksInTractorBeam;
   }

   public float getPercent() {
      return (float)Math.min(this.getTicksInTractorBeam(), 120) / 240.0F;
   }

   public static void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.END) {
         event.player.getCapability(WitherStormModClientCapabilities.TRACTOR_BEAM_EFFECTS).ifPresent(PlayerTractorBeamEffects::tick);
      }
   }
}
