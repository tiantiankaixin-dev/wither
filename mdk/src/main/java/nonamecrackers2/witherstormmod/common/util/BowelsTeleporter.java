package nonamecrackers2.witherstormmod.common.util;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.ITeleporter;
import net.minecraft.core.registries.BuiltInRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;

public class BowelsTeleporter implements ITeleporter {
   private final BlockPos pos;

   public BowelsTeleporter(BlockPos pos) {
      this.pos = pos;
   }

   public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
      return new PortalInfo(Vec3.atBottomCenterOf(this.pos), Vec3.ZERO, entity.getYRot(), entity.getXRot());
   }

   public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
      player.connection
         .send(
            new ClientboundSoundPacket(
               (Holder)NeoBuiltInRegistries.SOUND_EVENT.getHolder(WitherStormModSoundEvents.BOWELS_TRANSPORT.get()).get(),
               SoundSource.AMBIENT,
               player.getX(),
               player.getY(),
               player.getZ(),
               1.0F,
               1.0F,
               player.getRandom().nextLong()
            )
         );
      return false;
   }
}
