package nonamecrackers2.witherstormmod.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;

public class BowelsTeleporter {
   private final BlockPos pos;

   public BowelsTeleporter(BlockPos pos) {
      this.pos = pos;
   }

   public DimensionTransition transition(ServerLevel destWorld, Entity entity) {
      return transition(destWorld, entity, this.pos);
   }

   public static DimensionTransition transition(ServerLevel destWorld, Entity entity, BlockPos pos) {
      return new DimensionTransition(destWorld, Vec3.atBottomCenterOf(pos), Vec3.ZERO, entity.getYRot(), entity.getXRot(), false, BowelsTeleporter::playTeleportSound);
   }

   private static void playTeleportSound(Entity entity) {
      if (entity instanceof ServerPlayer player) {
         player.connection
            .send(
               new ClientboundSoundPacket(
                  (Holder)ForgeRegistries.SOUND_EVENTS.getHolder(WitherStormModSoundEvents.BOWELS_TRANSPORT.get()).get(),
                  SoundSource.AMBIENT,
                  player.getX(),
                  player.getY(),
                  player.getZ(),
                  1.0F,
                  1.0F,
                  player.getRandom().nextLong()
               )
            );
      }
   }
}
