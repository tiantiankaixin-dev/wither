package nonamecrackers2.witherstormmod.mixin;

import java.util.List;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Pos;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor.PacketTarget;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.packet.StormAttributesMessage;
import nonamecrackers2.witherstormmod.common.packet.StormMetadataMessage;
import nonamecrackers2.witherstormmod.common.packet.StormTeleportMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateStormHeadLookMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateStormPositionMessage;
import nonamecrackers2.witherstormmod.common.packet.UpdateStormVelocityMessage;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net.minecraft.server.level.ChunkMap$TrackedEntity"}
)
public class MixinTrackedEntity {
   @Shadow
   @Final
   private Entity entity;

   @Inject(
      method = {"broadcast"},
      at = {@At("TAIL")}
   )
   public void witherstormmod$copyVanillaAndSendToDistantRenderer_broadcast(Packet<?> packet, CallbackInfo ci) {
      if (this.entity instanceof WitherStormEntity storm) {
         PacketTarget target = PacketDistributor.DIMENSION.with(storm.level()::dimension);
         List<Integer> applicable = WorldUtil.getStormIds(storm);
         if (packet instanceof Rot rotPacket) {
            WitherStormModPacketHandlers.MAIN
               .send(
                  target,
                  new UpdateStormPositionMessage(applicable, this.entity.getId(), rotPacket.getyRot(), rotPacket.getxRot(), rotPacket.isOnGround())
               );
         } else if (packet instanceof Pos posPacket) {
            WitherStormModPacketHandlers.MAIN
               .send(
                  target,
                  new UpdateStormPositionMessage(
                     applicable, this.entity.getId(), posPacket.getXa(), posPacket.getYa(), posPacket.getZa(), posPacket.isOnGround()
                  )
               );
         } else if (packet instanceof PosRot posRotPacket) {
            WitherStormModPacketHandlers.MAIN
               .send(
                  target,
                  new UpdateStormPositionMessage(
                     applicable,
                     this.entity.getId(),
                     posRotPacket.getXa(),
                     posRotPacket.getYa(),
                     posRotPacket.getZa(),
                     posRotPacket.getyRot(),
                     posRotPacket.getxRot(),
                     posRotPacket.isOnGround()
                  )
               );
         } else if (packet instanceof ClientboundTeleportEntityPacket teleportPacket) {
            WitherStormModPacketHandlers.MAIN
               .send(
                  target,
                  new StormTeleportMessage(
                     applicable,
                     teleportPacket.getId(),
                     teleportPacket.getX(),
                     teleportPacket.getY(),
                     teleportPacket.getZ(),
                     teleportPacket.getyRot(),
                     teleportPacket.getxRot(),
                     teleportPacket.isOnGround()
                  )
               );
         } else if (packet instanceof ClientboundSetEntityMotionPacket motionPacket) {
            WitherStormModPacketHandlers.MAIN
               .send(
                  target,
                  new UpdateStormVelocityMessage(
                     applicable, motionPacket.getId(), motionPacket.getXa(), motionPacket.getYa(), motionPacket.getZa()
                  )
               );
         } else if (packet instanceof ClientboundRotateHeadPacket headPacket) {
            WitherStormModPacketHandlers.MAIN.send(target, new UpdateStormHeadLookMessage(applicable, storm.getId(), headPacket.getYHeadRot()));
         } else if (packet instanceof ClientboundSetEntityDataPacket dataPacket) {
            WitherStormModPacketHandlers.MAIN.send(target, new StormMetadataMessage(applicable, dataPacket.id(), dataPacket.packedItems()));
         } else if (packet instanceof ClientboundUpdateAttributesPacket attributesPacket) {
            WitherStormModPacketHandlers.MAIN.send(target, new StormAttributesMessage(applicable, attributesPacket.getEntityId(), attributesPacket.getValues()));
         }
      }
   }
}
